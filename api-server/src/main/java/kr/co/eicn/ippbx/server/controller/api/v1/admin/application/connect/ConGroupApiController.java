package kr.co.eicn.ippbx.server.controller.api.v1.admin.application.connect;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonField;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTree;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ConGroup;
import kr.co.eicn.ippbx.model.dto.eicn.CommonTypeResponse;
import kr.co.eicn.ippbx.model.dto.eicn.ConGroupSummaryResponse;
import kr.co.eicn.ippbx.model.enums.CommonTypeKind;
import kr.co.eicn.ippbx.model.form.ConGroupFormRequest;
import kr.co.eicn.ippbx.model.search.ConGroupSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.CommonFieldRepository;
import kr.co.eicn.ippbx.server.repository.eicn.CommonTypeRepository;
import kr.co.eicn.ippbx.server.repository.eicn.ConGroupRepository;
import kr.co.eicn.ippbx.server.service.CommonFieldPoster;
import kr.co.eicn.ippbx.server.service.OrganizationService;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
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
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 상담어플리케이션 관리 > 연동DB 관리 > 그룹관리
 */

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/application/connect/group", produces = MediaType.APPLICATION_JSON_VALUE)
public class ConGroupApiController extends ApiBaseController {

    private final ConGroupRepository repository;
    private final CommonTypeRepository typeRepository;
    private final CommonFieldRepository fieldRepository;
    private final OrganizationService organizationService;
    private final CommonFieldPoster commonFieldPoster;

    @GetMapping("")
    public ResponseEntity<JsonResult<Pagination<ConGroupSummaryResponse>>> pagination(ConGroupSearchRequest search) {
        final Pagination<ConGroup> pagination = repository.pagination(search);

        final List<ConGroupSummaryResponse> rows = pagination.getRows().stream()
                .map((e) -> {
                    final ConGroupSummaryResponse response = convertDto(e, ConGroupSummaryResponse.class);

                    final List<String> companyTrees = organizationService.getCompanyTrees(e.getGroupCode()).stream()
                            .map(CompanyTree::getGroupName)
                            .collect(Collectors.toList());
                    response.setGroupTreeNames(companyTrees);

                    return response;
                })
                .sorted(comparing(ConGroupSummaryResponse::getSeq))
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
    }

    @GetMapping(value = "{seq}")
    public ResponseEntity<JsonResult<ConGroupSummaryResponse>> get(@PathVariable Integer seq) {
        final ConGroup conGroup = repository.findOneIfNullThrow(seq);
        final List<String> companyTrees = organizationService.getCompanyTrees(conGroup.getGroupCode()).stream().map(CompanyTree::getGroupName).collect(Collectors.toList());
        final ConGroupSummaryResponse response = convertDto(conGroup, ConGroupSummaryResponse.class);

        response.setFieldInfoType(fieldRepository.findAllCommonField(conGroup.getConType()).stream()
                .collect(Collectors.toMap(CommonField::getFieldInfo, CommonField::getFieldType)));

        response.setGroupTreeNames(companyTrees);

        return ResponseEntity.ok(data(response));
    }

    @PostMapping(value = "")
    public ResponseEntity<JsonResult<Void>> post(@Valid @RequestBody ConGroupFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        repository.insertOnGeneratedKey(form);
        return ResponseEntity.created(URI.create("api/v1/admin/application/connect/group")).body(create());
    }

    @PutMapping(value = "{seq}")
    public ResponseEntity<JsonResult<Void>> put(@Valid @RequestBody ConGroupFormRequest form, BindingResult bindingResult,
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

    @GetMapping("con-type")
    public ResponseEntity<JsonResult<List<CommonTypeResponse>>> conType() {
        return ResponseEntity.ok(data(typeRepository.findAll().stream()
                .filter(conType -> conType.getKind().equals(CommonTypeKind.LINK_DB.getCode()) && conType.getStatus().equals("U"))
                .map(e -> convertDto(e, CommonTypeResponse.class))
                .sorted(comparing(CommonTypeResponse::getSeq).reversed())
                .collect(Collectors.toList())));
    }

    @PostMapping(value = "{seq}/fields/by-excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<JsonResult<Void>> postFieldsByExcel(@PathVariable Integer seq, @RequestParam MultipartFile file) throws IOException {
        commonFieldPoster.postByExcel(CommonFieldPoster.ExcelType.CON, seq, file);
        return ResponseEntity.ok(create());
    }
}
