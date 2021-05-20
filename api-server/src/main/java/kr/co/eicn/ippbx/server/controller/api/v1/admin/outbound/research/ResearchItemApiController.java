package kr.co.eicn.ippbx.server.controller.api.v1.admin.outbound.research;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTree;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ResearchItem;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SoundList;
import kr.co.eicn.ippbx.model.dto.eicn.OrganizationSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.ResearchItemResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryResearchItemResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummarySoundListResponse;
import kr.co.eicn.ippbx.model.form.ResearchItemFormRequest;
import kr.co.eicn.ippbx.model.search.ResearchItemSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.ResearchItemRepository;
import kr.co.eicn.ippbx.server.repository.eicn.SoundListRepository;
import kr.co.eicn.ippbx.server.service.OrganizationService;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.ResearchItem.RESEARCH_ITEM;
import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * 아웃바운드관리 > 설문관리 > 설문문항관리[마]
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/outbound/research/item", produces = MediaType.APPLICATION_JSON_VALUE)
public class ResearchItemApiController extends ApiBaseController {

    private final ResearchItemRepository repository;
    private final OrganizationService organizationService;
    private final SoundListRepository soundListRepository;

    @GetMapping("")
    public ResponseEntity<JsonResult<List<ResearchItem>>> list(ResearchItemSearchRequest search) {
        return ResponseEntity.ok(data(repository.findAll(search)));
    }

    @GetMapping("search")
    public ResponseEntity<JsonResult<Pagination<ResearchItemResponse>>> pagination(ResearchItemSearchRequest search) {
        final Pagination<ResearchItem> pagination = repository.pagination(search);
        final List<CompanyTree> companyTrees = organizationService.getAllCompanyTrees();
        final Map<Integer, List<ResearchItem>> groupIngItemIdMap = repository.findAll(RESEARCH_ITEM.MAPPING_NUMBER.ne((byte) 0))
                .stream()
                .sorted(Comparator.comparingInt(ResearchItem::getMappingNumber))
                .collect(Collectors.groupingBy(ResearchItem::getItemId));

        final List<ResearchItemResponse> rows = pagination.getRows().stream()
                .map((e) -> {
                    final ResearchItemResponse summary = convertDto(e, ResearchItemResponse.class);

                    if (groupIngItemIdMap.get(e.getItemId()) != null)
                        summary.setAnswers(groupIngItemIdMap.get(e.getItemId()).stream().map(ResearchItem::getWord).collect(Collectors.toList()));

                    if (isNotEmpty(summary.getGroupCode()))
                        summary.setCompanyTrees(organizationService.getCompanyTrees(companyTrees, summary.getGroupCode())
                                .stream()
                                .map(organization -> convertDto(organization, OrganizationSummaryResponse.class))
                                .collect(Collectors.toList())
                        );
                    return summary;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(data(new Pagination<>(rows, search.getPage(), pagination.getTotalCount(), search.getLimit())));
    }

    @GetMapping("{seq}")
    public ResponseEntity<JsonResult<ResearchItemResponse>> get(@PathVariable Integer seq) {
        final ResearchItemResponse detail = convertDto(repository.findOneIfNullThrow(RESEARCH_ITEM.SEQ.eq(seq).and(RESEARCH_ITEM.MAPPING_NUMBER.eq((byte) 0))), ResearchItemResponse.class);
        // null? []?
        detail.setAnswers(repository.findAllItemId(detail.getItemId()).stream()
                .filter(e -> e.getMappingNumber() != 0)
                .map(ResearchItem::getWord)
                .collect(Collectors.toList()));

        if (isNotEmpty(detail.getGroupCode()))
            detail.setCompanyTrees(organizationService.getCompanyTrees(detail.getGroupCode())
                    .stream()
                    .map(organization -> convertDto(organization, OrganizationSummaryResponse.class))
                    .collect(Collectors.toList())
            );

        return ResponseEntity.ok(data(detail));
    }

    @PostMapping("")
    public ResponseEntity<JsonResult<Void>> register(@Valid @RequestBody ResearchItemFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        repository.insert(form);
        return ResponseEntity.created(URI.create("api/v1/admin/outbound/research/item")).body(create());
    }

    @PutMapping("{seq}")
    public ResponseEntity<JsonResult<Void>> update(@Valid @RequestBody ResearchItemFormRequest form, BindingResult bindingResult, @PathVariable Integer seq) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        repository.updateByKey(form, seq);
        return ResponseEntity.ok(create());
    }

    @DeleteMapping("{seq}")
    public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer seq) {
        repository.deleteItemId(seq);
        return ResponseEntity.ok(create());
    }

    /**
     * 음원 목록 조회
     */
    @GetMapping("add-sounds")
    public ResponseEntity<JsonResult<List<SummarySoundListResponse>>> addSounds() {
        return ResponseEntity.ok(data(soundListRepository.findAll()
                .stream()
                .sorted(comparing(SoundList::getSoundName))
                .map(e -> convertDto(e, SummarySoundListResponse.class))
                .collect(Collectors.toList()))
        );
    }

    /**
     * 설문문항 조회
     */
    @GetMapping("add-research-item")
    public ResponseEntity<JsonResult<List<SummaryResearchItemResponse>>> addResearchItem() {
        return ResponseEntity.ok(data(repository.findAll(RESEARCH_ITEM.MAPPING_NUMBER.eq((byte) 0))
                .stream()
                .sorted(Comparator.comparingInt(ResearchItem::getMappingNumber))
                .map(e -> convertDto(e, SummaryResearchItemResponse.class))
                .collect(Collectors.toList()))
        );
    }
}
