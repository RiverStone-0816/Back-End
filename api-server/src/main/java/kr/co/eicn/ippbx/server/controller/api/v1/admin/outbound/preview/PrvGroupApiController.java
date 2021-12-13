package kr.co.eicn.ippbx.server.controller.api.v1.admin.outbound.preview;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.*;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.enums.CommonTypeKind;
import kr.co.eicn.ippbx.model.form.PrvGroupFormRequest;
import kr.co.eicn.ippbx.model.search.PrvGroupSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.*;
import kr.co.eicn.ippbx.server.service.CommonFieldPoster;
import kr.co.eicn.ippbx.server.service.OrganizationService;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 아웃바운드 관리 > 프리뷰 > 그룹관리
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/outbound/preview/group", produces = MediaType.APPLICATION_JSON_VALUE)
public class PrvGroupApiController extends ApiBaseController {

    private final PrvGroupRepository repository;
    private final CommonTypeRepository commonTypeRepository;
    private final CommonMemberRepository commonMemberRepository;
    private final CommonFieldRepository fieldRepository;
    private final PersonListRepository personListRepository;
    private final Number070Repository numberRepository;
    private final OrganizationService organizationService;
    private final CommonFieldPoster commonFieldPoster;

    @GetMapping("")
    public ResponseEntity<JsonResult<Pagination<PrvGroupSummaryResponse>>> pagination(PrvGroupSearchRequest search) {
        final Pagination<PrvGroup> pagination = repository.pagination(search);
        final Map<Integer, String> commonTypeMap = commonTypeRepository.findAll().stream().collect(Collectors.toMap(CommonType::getSeq, CommonType::getName));

        final List<PrvGroupSummaryResponse> rows = pagination.getRows().stream()
                .map((e) -> {
                    final PrvGroupSummaryResponse response = convertDto(e, PrvGroupSummaryResponse.class);

                    if (Objects.nonNull(commonTypeMap.get(e.getPrvType())))
                        response.setPrvTypeName(commonTypeMap.get(e.getPrvType()));
                    if (Objects.nonNull(commonTypeMap.get(e.getResultType())))
                        response.setResultTypeName(commonTypeMap.get(e.getResultType()));

                    final List<String> companyTrees = organizationService.getCompanyTrees(e.getGroupCode()).stream()
                            .map(CompanyTree::getGroupName)
                            .collect(Collectors.toList());
                    response.setGroupTreeNames(companyTrees);

                    return response;
                })
                .sorted(comparing(PrvGroupSummaryResponse::getSeq))
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
    }

    @GetMapping(value = "{seq}")
    public ResponseEntity<JsonResult<PrvGroupDetailResponse>> get(@PathVariable Integer seq) {
        final PrvGroup prvGroup = repository.findOneIfNullThrow(seq);
        final List<PersonList> personList = personListRepository.findAll();
        final List<String> commonMember = commonMemberRepository.findAll().stream().filter(e -> e.getGroupId().equals(seq)).map(CommonMember::getUserid).collect(Collectors.toList());
        final List<String> companyTrees = organizationService.getCompanyTrees(prvGroup.getGroupCode()).stream().map(CompanyTree::getGroupName).collect(Collectors.toList());
        final Map<Integer, String> commonTypeMap = commonTypeRepository.findAll().stream().collect(Collectors.toMap(CommonType::getSeq, CommonType::getName));

        final PrvGroupDetailResponse response = convertDto(prvGroup, PrvGroupDetailResponse.class);

        if (Objects.nonNull(commonTypeMap.get(prvGroup.getPrvType())))
            response.setPrvTypeName(commonTypeMap.get(prvGroup.getPrvType()));
        if (Objects.nonNull(commonTypeMap.get(prvGroup.getResultType())))
            response.setResultTypeName(commonTypeMap.get(prvGroup.getResultType()));

        response.setGroupTreeNames(companyTrees);
        response.setFieldInfoType(fieldRepository.findAllCommonField(prvGroup.getPrvType()).stream()
                .collect(Collectors.toMap(CommonField::getFieldInfo, CommonField::getFieldType)));

        response.setMemberDataList(personList.stream()
                .filter(e -> commonMember.stream().anyMatch(id -> id.equals(e.getId())))
                .map(e -> convertDto(e, CommonMemberResponse.class))
                .collect(Collectors.toList())
        );

        return ResponseEntity.ok(data(response));
    }

    @PostMapping(value = "")
    public ResponseEntity<JsonResult<Integer>> post(@Valid @RequestBody PrvGroupFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        repository.insertOnGeneratedKey(form);
        return ResponseEntity.created(URI.create("api/v1/admin/outbound/preview/group")).body(create());
    }

    @PutMapping(value = "{seq}")
    public ResponseEntity<JsonResult<Void>> put(@Valid @RequestBody PrvGroupFormRequest form, BindingResult bindingResult,
                                                @PathVariable Integer seq) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        repository.updateByKeyWithCommonMember(form, seq);
        return ResponseEntity.ok(create());
    }

    @DeleteMapping(value = "{seq}")
    public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer seq) {
        repository.deleteWithCommonMember(seq);
        return ResponseEntity.ok(create());
    }

    @GetMapping("preview-type")
    public ResponseEntity<JsonResult<List<CommonTypeResponse>>> prvType() {
        return ResponseEntity.ok(data(commonTypeRepository.findAll().stream()
                .filter(prvType -> prvType.getKind().equals(CommonTypeKind.PREVIEW.getCode()) && prvType.getStatus().equals("U"))
                .map(e -> convertDto(e, CommonTypeResponse.class))
                .sorted(comparing(CommonTypeResponse::getSeq))
                .collect(Collectors.toList())));
    }

    @GetMapping("result-type")
    public ResponseEntity<JsonResult<List<CommonTypeResponse>>> resultType() {
        return ResponseEntity.ok(data(commonTypeRepository.findAll().stream()
                .filter(resultType -> resultType.getKind().equals(CommonTypeKind.CONSULTATION_RESULTS.getCode()) && resultType.getStatus().equals("U"))
                .map(e -> convertDto(e, CommonTypeResponse.class))
                .sorted(comparing(CommonTypeResponse::getSeq))
                .collect(Collectors.toList())));
    }

    @GetMapping("preview-number")
    public ResponseEntity<JsonResult<List<PrvGroupNumber070Response>>> prvNumber() {
        return ResponseEntity.ok(data(numberRepository.prvGroupNumber070().stream()
                .map(e -> convertDto(e, PrvGroupNumber070Response.class))
                .sorted(comparing(PrvGroupNumber070Response::getNumber))
                .collect(Collectors.toList())));
    }

    @GetMapping("preview-group")
    public ResponseEntity<JsonResult<List<PrvGroupResponse>>> prvGroup() {
        final List<PrvGroupResponse> list = repository.findAll().stream()
                .map((e) -> convertDto(e, PrvGroupResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(data(list));
    }

    @PostMapping(value = "{seq}/fields/by-excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<JsonResult<Void>> postFieldsByExcel(@PathVariable Integer seq, @RequestParam MultipartFile file) throws IOException {
        commonFieldPoster.postByExcel(CommonFieldPoster.ExcelType.PRV, seq, file);
        return ResponseEntity.ok(create());
    }
}
