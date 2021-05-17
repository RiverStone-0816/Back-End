package kr.co.eicn.ippbx.server.controller.api.v1.admin.user.tel;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.exception.ValidationException;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CompanyTree;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.RandomCid;
import kr.co.eicn.ippbx.server.model.dto.eicn.OrganizationSummaryResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.RandomCidResponse;
import kr.co.eicn.ippbx.server.model.form.RandomCidFormRequest;
import kr.co.eicn.ippbx.server.model.search.RandomCidSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.RandomRidRepository;
import kr.co.eicn.ippbx.server.service.OrganizationService;
import kr.co.eicn.ippbx.server.util.JsonResult;
import kr.co.eicn.ippbx.server.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.server.util.JsonResult.create;
import static kr.co.eicn.ippbx.server.util.JsonResult.data;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * 번호/그룹/사용자 > 번호/서비스관리 > 랜덤RID관리
 */
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/user/tel/random-rid", produces = MediaType.APPLICATION_JSON_VALUE)
public class RandomRidApiController extends ApiBaseController {

    private final RandomRidRepository repository;
    private final OrganizationService organizationService;

    @GetMapping("")
    public ResponseEntity<JsonResult<Pagination<RandomCidResponse>>> pagination(RandomCidSearchRequest search) {
        final Pagination<RandomCid> pagination = repository.pagination(search);
        final List<CompanyTree> companyTrees = organizationService.getAllCompanyTrees();

        final List<RandomCidResponse> rows = pagination.getRows().stream()
                .map((e) -> {
                    RandomCidResponse summary = convertDto(e, RandomCidResponse.class);
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
    public ResponseEntity<JsonResult<RandomCidResponse>> get(@PathVariable Integer seq) {
        final RandomCidResponse detail = convertDto(repository.findOneIfNullThrow(seq), RandomCidResponse.class);

        if (isNotEmpty(detail.getGroupCode()))
            detail.setCompanyTrees(organizationService.getCompanyTrees(detail.getGroupCode())
                    .stream()
                    .map(organization -> convertDto(organization, OrganizationSummaryResponse.class))
                    .collect(Collectors.toList())
            );

        return ResponseEntity.ok(data(detail));
    }

    @PostMapping("")
    public ResponseEntity<JsonResult<Void>> register(@Valid @RequestBody RandomCidFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        repository.insert(form);
        return ResponseEntity.created(URI.create("/api/v1/admin/user/tel/random-rid")).body(create());
    }

    @PutMapping("{seq}")
    public ResponseEntity<JsonResult<Void>> update(@Valid @RequestBody RandomCidFormRequest form, BindingResult bindingResult, @PathVariable Integer seq) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        repository.updateByKey(form, seq);
        return ResponseEntity.ok(create());
    }

    @DeleteMapping("{seq}")
    public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer seq) {
        repository.delete(seq);
        return ResponseEntity.ok(create());
    }
}
