package kr.co.eicn.ippbx.server.controller.api.v1.admin.outbound.voc;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.exception.ValidationException;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchList;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.VocGroup;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.VocMemberList;
import kr.co.eicn.ippbx.server.model.dto.eicn.SummaryResearchListResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.VOCGroupResponse;
import kr.co.eicn.ippbx.server.model.enums.IsArsSms;
import kr.co.eicn.ippbx.server.model.form.VOCGroupFormRequest;
import kr.co.eicn.ippbx.server.model.search.VOCGroupSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.ResearchListRepository;
import kr.co.eicn.ippbx.server.repository.eicn.VOCGroupRepository;
import kr.co.eicn.ippbx.server.repository.eicn.VocMemberListRepository;
import kr.co.eicn.ippbx.server.util.JsonResult;
import kr.co.eicn.ippbx.server.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.server.jooq.eicn.tables.ResearchList.RESEARCH_LIST;
import static kr.co.eicn.ippbx.server.util.JsonResult.create;
import static kr.co.eicn.ippbx.server.util.JsonResult.data;

/**
 * 아웃바운드관리 > VOC/해피콜 관리 > VOC/해피콜 관리
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/outbound/voc/group", produces = MediaType.APPLICATION_JSON_VALUE)
public class VOCGroupApiController extends ApiBaseController {

    private final VOCGroupRepository repository;
    private final ResearchListRepository researchListRepository;
    private final VocMemberListRepository memberListRepository;

    @GetMapping("search")
    public ResponseEntity<JsonResult<Pagination<VOCGroupResponse>>> pagination(VOCGroupSearchRequest search) {
        final Pagination<VocGroup> pagination = repository.pagination(search);
        final List<VocMemberList> memberList = memberListRepository.findAll();
        final Map<Integer, ResearchList> researchListMap = researchListRepository.findAll().stream().collect(Collectors.toMap(ResearchList::getResearchId, e -> e));

        final List<VOCGroupResponse> rows = pagination.getRows().stream()
                .map((e) -> {
                    final VOCGroupResponse response = convertDto(e, VOCGroupResponse.class);
                    if (Objects.nonNull(researchListMap.get(e.getArsResearchId())))
                        response.setResearch(convertDto(researchListMap.get(e.getArsResearchId()), SummaryResearchListResponse.class));

                    response.setInboundMemberList(
                            memberList.stream().filter(m -> m.getVocGroupId().equals(e.getSeq()) && Objects.nonNull(e.getInboundCallKind()))
                                    .map(VocMemberList::getUserid).collect(Collectors.toList())
                    );
                    response.setOutboundMemberList(
                            memberList.stream().filter(m -> m.getVocGroupId().equals(e.getSeq()) && Objects.nonNull(e.getOutboundCallKind()))
                                    .map(VocMemberList::getUserid).collect(Collectors.toList())
                    );
                    return response;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(new Pagination<>(rows, search.getPage(), pagination.getTotalCount(), search.getLimit())));
    }

    @GetMapping("{seq}")
    public ResponseEntity<JsonResult<VOCGroupResponse>> get(@PathVariable Integer seq) {
        final VOCGroupResponse detail = convertDto(repository.findOneIfNullThrow(seq), VOCGroupResponse.class);
        final List<VocMemberList> memberList = memberListRepository.findAll().stream().filter(e -> e.getVocGroupId().equals(seq)).collect(Collectors.toList());

        final ResearchList researchList = researchListRepository.findOne(RESEARCH_LIST.RESEARCH_ID.eq(detail.getArsResearchId()));
        if (Objects.nonNull(researchList))
            detail.setResearch(convertDto(researchList, SummaryResearchListResponse.class));

        detail.setInboundMemberList(memberList.stream().filter(m -> Objects.nonNull(m.getInboundCallKind())).map(VocMemberList::getUserid).collect(Collectors.toList()));
        detail.setOutboundMemberList(memberList.stream().filter(m -> Objects.nonNull(m.getOutboundCallKind())).map(VocMemberList::getUserid).collect(Collectors.toList()));

        return ResponseEntity.ok(data(detail));
    }

    @PostMapping("")
    public ResponseEntity<JsonResult<Void>> post(@Valid @RequestBody VOCGroupFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        repository.insertAllPbxServers(form);
        return ResponseEntity.created(URI.create("api/v1/admin/outbound/voc/group")).body(create());
    }

    @PutMapping("{seq}")
    public ResponseEntity<JsonResult<Void>> put(@Valid @RequestBody VOCGroupFormRequest form, BindingResult bindingResult, @PathVariable Integer seq) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        repository.updateByKey(form, seq);
        return ResponseEntity.ok(create());
    }

    @DeleteMapping("{seq}")
    public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer seq) {
        repository.deleteOnIfNullThrow(seq);
        return ResponseEntity.ok(create());
    }

    @GetMapping("")
    public ResponseEntity<JsonResult<List<VocGroup>>> list(VOCGroupSearchRequest search) {
        return ResponseEntity.ok(data(repository.findAll(search).stream().filter(e -> e.getIsArsSms().equals(IsArsSms.ARS.getCode())).collect(Collectors.toList())));
    }

    /**
     * 상담화면 ARS, SMS
     */
    @GetMapping("{type}/ars-sms-list")
    public ResponseEntity<JsonResult<List<VocGroup>>> getArsSmsList(@PathVariable String type) {
        return ResponseEntity.ok(data(repository.getArsSmsList(type)));
    }
}
