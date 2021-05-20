package kr.co.eicn.ippbx.server.controller.api.v1.admin.record.callback;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.model.dto.eicn.CallbackHistoryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryCallbackDistPersonResponse;
import kr.co.eicn.ippbx.model.entity.eicn.CallbackDistEntity;
import kr.co.eicn.ippbx.model.entity.eicn.CallbackEntity;
import kr.co.eicn.ippbx.model.enums.CallbackStatus;
import kr.co.eicn.ippbx.model.form.CallbackListUpdateFormRequest;
import kr.co.eicn.ippbx.model.form.CallbackRedistFormRequest;
import kr.co.eicn.ippbx.model.search.CallbackHistorySearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.CallbackDistRepository;
import kr.co.eicn.ippbx.server.repository.eicn.CallbackRepository;
import kr.co.eicn.ippbx.server.repository.eicn.PersonListRepository;
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
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 녹취관리 > 콜백관리 > 콜백이력관리
 **/
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/record/callback/history", produces = MediaType.APPLICATION_JSON_VALUE)
public class CallbackHistoryApiController extends ApiBaseController {
    private final CallbackDistRepository callBackDistRepository;
    private final CallbackRepository callBackRepository;
    private final PersonListRepository personListRepository;

    /**
     *  재분배
     **/
    @PutMapping("/redistribution")
    public ResponseEntity<JsonResult<Void>> redistribution(@RequestBody @Valid CallbackRedistFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        callBackDistRepository.redistribution(form);
        return ResponseEntity.created(URI.create("api/v1/admin/service/callback/history")).body(create());
    }

    /**
     *  처리
     **/
    @PutMapping("")
    public ResponseEntity<JsonResult<Void>> put(@Valid @RequestBody CallbackListUpdateFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        callBackRepository.update(form);
        return ResponseEntity.ok(create());
    }

    /**
     *  콜백이력 삭제
     **/
    @DeleteMapping("")
    public ResponseEntity<JsonResult<Void>> delete(@RequestParam List<Integer> seq) {
        callBackRepository.deleteByPk(seq);
        return ResponseEntity.ok(create());
    }

    /**
     *  콜백이력 조회
     **/
    @GetMapping("")
    public ResponseEntity<JsonResult<Pagination<CallbackHistoryResponse>>> pagination(CallbackHistorySearchRequest search) {
        if (search.getStartDate().after(search.getEndDate()))
            throw new IllegalArgumentException(message.getText("messages.validator.enddate.after.startdate"));
        final Pagination<CallbackEntity> pagination = callBackRepository.pagination(search);
        final List<CallbackHistoryResponse> rows = pagination.getRows().stream()
                .map((e) -> {
                    CallbackHistoryResponse entity = convertDto(e, CallbackHistoryResponse.class);
                    entity.setStatus(CallbackStatus.of(e.getStatus()));
                    entity.setQueueNumber(e.getHuntNumber());
                    entity.setQueueName(e.getHuntName());

                    return entity;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
    }

    /**
     * 분배 가능한 사용자 목록조회
     */
    @GetMapping("add-persons")
    public ResponseEntity<JsonResult<List<SummaryCallbackDistPersonResponse>>> addPersons() {
        final List<PersonList> personLists = personListRepository.findAll().stream().sorted(Comparator.comparing(PersonList::getIdName)).collect(Collectors.toList());
        final List<CallbackDistEntity> callbackDistList = callBackDistRepository.findAll();

        return ResponseEntity.ok(data(
                personLists.stream()/*.filter(e -> {
                            if (callbackDistList != null)
                                return callbackDistList.stream().noneMatch(f -> f.getUserid().equals(e.getId()));
                            return true;

                })*/
                .map((e) -> convertDto(e, SummaryCallbackDistPersonResponse.class))
                .collect(Collectors.toList())
        ));
    }
}
