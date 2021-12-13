package kr.co.eicn.ippbx.server.controller.api.v1.admin.email.history;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.*;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.form.EmailConsultationHistoryFormRequest;
import kr.co.eicn.ippbx.model.search.EmailConsultationHistorySearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.CommonCodeRepository;
import kr.co.eicn.ippbx.server.repository.eicn.EmailConsultationHistoryRepository;
import kr.co.eicn.ippbx.server.repository.eicn.PersonListRepository;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 이메일상담관리 > 이메일상담이력 > 이메일상담이력
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/email/history/consultation-history", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmailConsultationHistoryApiController extends ApiBaseController {

    private final EmailConsultationHistoryRepository repository;
    private final PersonListRepository personListRepository;
    private final CommonCodeRepository commonCodeRepository;

    @GetMapping("")
    public ResponseEntity<JsonResult<Pagination<EmailConsultationHistorySummaryResponse>>> pagination(EmailConsultationHistorySearchRequest search) {
        final Pagination<EmailList> pagination = repository.pagination(search);
        final List<PersonList> personList = personListRepository.findAll();
        final List<CommonCode> codes = commonCodeRepository.findAll().stream().filter(c -> c.getType().equals(5))
                .filter(c -> c.getFieldId().equals("RS_CODE_1") || c.getFieldId().equals("RS_CODE_2") || c.getFieldId().equals("RS_CODE_3")).collect(Collectors.toList());

        final List<EmailConsultationHistorySummaryResponse> rows = pagination.getRows().stream()
                .map((e) -> {
                    final EmailConsultationHistorySummaryResponse response = convertDto(e, EmailConsultationHistorySummaryResponse.class);
                    for (PersonList person : personList) {
                        if (person.getId().equals(e.getUserid()))
                            response.setUserName(person.getIdName());

                        if (person.getId().equals(e.getUseridTr()))
                            response.setUserTrName(person.getIdName());
                    }

                    for (CommonCode code : codes) {
                        if (code.getCodeId().equals(e.getResultCode()) && code.getFieldId().equals("RS_CODE_1"))
                            response.setResultCodeName(code.getCodeName());
                        if (code.getCodeId().equals(e.getResultService()) && code.getFieldId().equals("RS_CODE_3"))
                            response.setResultServiceName(code.getCodeName());
                        if (code.getCodeId().equals(e.getResultKind()) && code.getFieldId().equals("RS_CODE_2"))
                            response.setResultKindName(code.getCodeName());
                    }

                    return response;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
    }

    @PutMapping("/redistribution")
    public ResponseEntity<JsonResult<Void>> redistribution(@Valid @RequestBody EmailConsultationHistoryFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        repository.redistribution(form);
        return ResponseEntity.created(URI.create("api/v1/admin/email/history/consultation-history")).body(create());
    }

    /**
     *   처리상태 목록조회
     */
    @GetMapping("common-code")
    public ResponseEntity<JsonResult<List<CommonCodeResponse>>> emailCommonCode() {
        return ResponseEntity.ok(data(commonCodeRepository.findAll().stream()
                .filter(e -> e.getFieldId().equals("RS_CODE_1") && e.getType().equals(5))
                .map(e -> convertDto(e, CommonCodeResponse.class)).collect(Collectors.toList())));
    }
}
