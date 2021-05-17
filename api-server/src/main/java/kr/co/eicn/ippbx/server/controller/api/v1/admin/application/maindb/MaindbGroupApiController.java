package kr.co.eicn.ippbx.server.controller.api.v1.admin.application.maindb;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.exception.ValidationException;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CommonField;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CommonType;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CompanyTree;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.MaindbGroup;
import kr.co.eicn.ippbx.server.model.dto.eicn.CommonTypeResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.MaindbGroupDetailResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.MaindbGroupSummaryResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.SummaryCommonFieldResponse;
import kr.co.eicn.ippbx.server.model.enums.CommonTypeKind;
import kr.co.eicn.ippbx.server.model.enums.CommonTypeStatus;
import kr.co.eicn.ippbx.server.model.form.MaindbGroupFormRequest;
import kr.co.eicn.ippbx.server.model.form.MaindbGroupUpdateRequest;
import kr.co.eicn.ippbx.server.model.search.MaindbGroupSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.CommonFieldRepository;
import kr.co.eicn.ippbx.server.repository.eicn.CommonTypeRepository;
import kr.co.eicn.ippbx.server.repository.eicn.MaindbGroupRepository;
import kr.co.eicn.ippbx.server.service.CommonFieldPoster;
import kr.co.eicn.ippbx.server.service.OrganizationService;
import kr.co.eicn.ippbx.server.util.JsonResult;
import kr.co.eicn.ippbx.server.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static kr.co.eicn.ippbx.server.jooq.eicn.tables.CommonField.COMMON_FIELD;
import static kr.co.eicn.ippbx.server.util.JsonResult.create;
import static kr.co.eicn.ippbx.server.util.JsonResult.data;

/**
 * 상담어플리케이션 관리 > 고객DB 관리 > 그룹관리
 */

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/application/maindb/group", produces = MediaType.APPLICATION_JSON_VALUE)
public class MaindbGroupApiController extends ApiBaseController {

    private final MaindbGroupRepository repository;
    private final CommonTypeRepository typeRepository;
    private final CommonFieldRepository fieldRepository;
    private final OrganizationService organizationService;
    private final CommonFieldPoster commonFieldPoster;

    @GetMapping("search")
    public ResponseEntity<JsonResult<List<MaindbGroupSummaryResponse>>> list(MaindbGroupSearchRequest search) {
        final List<MaindbGroup> list = repository.findAll(search);
        final Map<Integer, String> commonTypeMap = typeRepository.findAll().stream().collect(Collectors.toMap(CommonType::getSeq, CommonType::getName));

        final List<MaindbGroupSummaryResponse> rows = list.stream()
                .map((e) -> {
                    final MaindbGroupSummaryResponse response = convertDto(e, MaindbGroupSummaryResponse.class);
                    if (Objects.nonNull(commonTypeMap.get(e.getMaindbType())))
                        response.setMaindbName(commonTypeMap.get(e.getMaindbType()));
                    if (Objects.nonNull(commonTypeMap.get(e.getResultType())))
                        response.setResultName(commonTypeMap.get(e.getResultType()));

                    final List<String> companyTrees = organizationService.getCompanyTrees(e.getGroupCode()).stream()
                            .map(CompanyTree::getGroupName)
                            .collect(Collectors.toList());
                    response.setGroupTreeNames(companyTrees);

                    return response;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(rows));
    }

    @GetMapping("")
    public ResponseEntity<JsonResult<Pagination<MaindbGroupSummaryResponse>>> pagination(MaindbGroupSearchRequest search) {
        final Pagination<MaindbGroup> pagination = repository.pagination(search);
        final Map<Integer, String> commonTypeMap = typeRepository.findAll().stream().collect(Collectors.toMap(CommonType::getSeq, CommonType::getName));

        final List<MaindbGroupSummaryResponse> rows = pagination.getRows().stream()
                .map(e -> {
                    final MaindbGroupSummaryResponse response = convertDto(e, MaindbGroupSummaryResponse.class);
                    if (Objects.nonNull(commonTypeMap.get(e.getMaindbType())))
                        response.setMaindbName(commonTypeMap.get(e.getMaindbType()));
                    if (Objects.nonNull(commonTypeMap.get(e.getResultType())))
                        response.setResultName(commonTypeMap.get(e.getResultType()));

                    final List<String> companyTrees = organizationService.getCompanyTrees(e.getGroupCode()).stream()
                            .map(CompanyTree::getGroupName)
                            .collect(Collectors.toList());
                    response.setGroupTreeNames(companyTrees);

                    return response;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
    }

    @GetMapping(value = "{seq}")
    public ResponseEntity<JsonResult<MaindbGroupDetailResponse>> get(@PathVariable Integer seq) {
        final MaindbGroup maindbGroup = repository.findOneIfNullThrow(seq);
        final Map<Integer, String> commonTypeMap = typeRepository.findAll().stream().filter(e -> e.getStatus().equals(CommonTypeStatus.USING.getCode())).collect(Collectors.toMap(CommonType::getSeq, CommonType::getName));
        final List<String> companyTrees = organizationService.getCompanyTrees(maindbGroup.getGroupCode()).stream().map(CompanyTree::getGroupName).collect(Collectors.toList());

        final MaindbGroupDetailResponse response = convertDto(maindbGroup, MaindbGroupDetailResponse.class);

        if (Objects.nonNull(commonTypeMap.get(maindbGroup.getMaindbType())))
            response.setMaindbName(commonTypeMap.get(maindbGroup.getMaindbType()));
        if (Objects.nonNull(commonTypeMap.get(maindbGroup.getResultType())))
            response.setResultName(commonTypeMap.get(maindbGroup.getResultType()));

        response.setFieldInfoType(
                fieldRepository.findAllCommonField(maindbGroup.getMaindbType()).stream().sorted(Comparator.comparing(CommonField::getDisplaySeq)).map(e -> convertDto(e, SummaryCommonFieldResponse.class)).collect(Collectors.toList())
        );

        response.setGroupTreeNames(companyTrees);

        return ResponseEntity.ok(data(response));
    }

    @PostMapping(value = "")
    public ResponseEntity<JsonResult<Void>> post(@Valid @RequestBody MaindbGroupFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        repository.insertOnGeneratedKey(form);
        return ResponseEntity.created(URI.create("api/v1/admin/application/maindb/group")).body(create());
    }

    @PutMapping(value = "{seq}")
    public ResponseEntity<JsonResult<Void>> put(@Valid @RequestBody MaindbGroupUpdateRequest form, BindingResult bindingResult,
                                                @PathVariable Integer seq) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        repository.updateGroup(form, seq);
        return ResponseEntity.ok(create());
    }

    @DeleteMapping(value = "{seq}")
    public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer seq) {
        repository.deleteOnIfNullThrow(seq);
        return ResponseEntity.ok(create());
    }

    /**
     * 필수항목 선택
     */
    @GetMapping(value = "maindb-field")
    public ResponseEntity<JsonResult<List<CommonField>>> maindbField() {
        return ResponseEntity.ok(data(fieldRepository.findAll(COMMON_FIELD.FIELD_ID.like("MAINDB%").and(COMMON_FIELD.ISNEED.eq("Y"))).stream()
		        .sorted(Comparator.comparing(CommonField::getType).thenComparing(CommonField::getDisplaySeq))
                .collect(Collectors.toList())));
    }

    /**
     * 고객정보유형 목록조회
     */
    @GetMapping("maindb-type")
    public ResponseEntity<JsonResult<List<CommonTypeResponse>>> maindbType() {
        return ResponseEntity.ok(data(typeRepository.findAll().stream()
                .filter(maindbType -> maindbType.getKind().equals(CommonTypeKind.MAIN_DB.getCode()) && maindbType.getStatus().equals("U"))
                .map(e -> convertDto(e, CommonTypeResponse.class))
                .sorted(comparing(CommonTypeResponse::getSeq))
                .collect(Collectors.toList())));
    }

    /**
     * 상담결과유형 목록조회
     */
    @GetMapping("result-type")
    public ResponseEntity<JsonResult<List<CommonTypeResponse>>> resultType() {
        return ResponseEntity.ok(data(typeRepository.findAll().stream()
                .filter(resultType -> resultType.getKind().equals(CommonTypeKind.CONSULTATION_RESULTS.getCode()) && resultType.getStatus().equals("U"))
                .map(e -> convertDto(e, CommonTypeResponse.class))
                .sorted(comparing(CommonTypeResponse::getSeq))
                .collect(Collectors.toList())));
    }

    @PostMapping(value = "{seq}/fields/by-excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<JsonResult<Void>> postFieldsByExcel(@PathVariable Integer seq, @RequestParam MultipartFile file) throws IOException {
        commonFieldPoster.postByExcel(CommonFieldPoster.ExcelType.MAINDB, seq, file);
        return ResponseEntity.ok(create());
    }
}
