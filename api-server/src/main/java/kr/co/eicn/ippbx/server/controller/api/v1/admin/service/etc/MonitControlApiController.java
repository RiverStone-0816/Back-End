package kr.co.eicn.ippbx.server.controller.api.v1.admin.service.etc;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.model.dto.eicn.MonitControlResponse;
import kr.co.eicn.ippbx.model.dto.eicn.PersonListSummary;
import kr.co.eicn.ippbx.model.form.MonitControlChangeRequest;
import kr.co.eicn.ippbx.model.search.MonitControlSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.CompanyTreeRepository;
import kr.co.eicn.ippbx.server.repository.eicn.QueueMemberTableRepository;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 서비스운영관리 > 기타관리 > 상담원상태변경
 **/
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/service/etc/monit", produces = MediaType.APPLICATION_JSON_VALUE)
public class MonitControlApiController extends ApiBaseController {
    private final QueueMemberTableRepository queueMemberTableRepository;
    private final CompanyTreeRepository companyTreeRepository;

    @GetMapping("")
    public ResponseEntity<JsonResult<List<MonitControlResponse>>> list(MonitControlSearchRequest search) {
        final List<MonitControlResponse> list = companyTreeRepository.getAllOrganizationByBranch(search).stream()
                .map(e -> {
                    MonitControlResponse entity = convertDto(e, MonitControlResponse.class);
                    if (e.getPerson() != null) {
                        entity.setPerson(
                                e.getPerson().stream()
                                        .map(f -> convertDto(f, PersonListSummary.class))
                                        .collect(Collectors.toList())
                        );
                    }

                    return entity;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(data(list));
    }

    @PutMapping("")
    public ResponseEntity<JsonResult<Void>> put(@Valid @RequestBody MonitControlChangeRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);
        queueMemberTableRepository.updatePause(form);
        return ResponseEntity.ok(create());
    }
}
