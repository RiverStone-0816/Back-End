package kr.co.eicn.ippbx.server.controller.api.v1.admin.record.callback;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.exception.ValidationException;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.QueueName;
import kr.co.eicn.ippbx.server.model.dto.eicn.*;
import kr.co.eicn.ippbx.server.model.entity.eicn.CallbackDistEntity;
import kr.co.eicn.ippbx.server.model.form.CallbackHuntDistFormRequest;
import kr.co.eicn.ippbx.server.model.form.CallbackUserDistFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.*;
import kr.co.eicn.ippbx.server.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.server.util.JsonResult.create;
import static kr.co.eicn.ippbx.server.util.JsonResult.data;

/**
 * 녹취관리 > 콜백관리 > 콜백분배관리
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/record/callback/distribution", produces = MediaType.APPLICATION_JSON_VALUE)
public class CallbackDistApiController extends ApiBaseController {
    private final CallbackDistRepository callBackDistRepository;
    private final ServiceRepository serviceRepository;
    private final QueueNameRepository queueNameRepository;
    private final QueueMemberTableRepository queueMemberTableRepository;
    private final PersonListRepository personListRepository;

    /**
     * 헌트분배
     **/
    @PostMapping("service/{svcNumber}/hunts")
    public ResponseEntity<JsonResult<Void>> huntDistribution(@RequestBody CallbackHuntDistFormRequest form, @PathVariable String svcNumber) {
        callBackDistRepository.huntDistribution(form, svcNumber);
        return ResponseEntity.created(URI.create("api/v1/admin/service/callback")).body(create());
    }

    /**
     * 상담원분배
     **/
    @PostMapping("service/{svcNumber}/hunt/{huntNumber}/users")
    public ResponseEntity<JsonResult<Void>> userDistribution(@RequestBody @Valid CallbackUserDistFormRequest form, BindingResult bindingResult, @PathVariable String svcNumber, @PathVariable String huntNumber) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        callBackDistRepository.userDistribution(form, svcNumber, huntNumber);
        return ResponseEntity.created(URI.create("api/v1/admin/service/callback")).body(create());
    }

    /**
     * 서비스목록
     **/
    @GetMapping("")
    public ResponseEntity<JsonResult<List<CallbackDistListResponse>>> list() {
        final List<QueueName> queueNameList = queueNameRepository.findAll();
        final List<CallbackDistEntity> callbackDistList = callBackDistRepository.findAll();
        final List<PersonList> personLists = personListRepository.findAll();

        final List<CallbackDistListResponse> rows = serviceRepository.findAll().stream()
                .map(e -> {
                    CallbackDistListResponse entity = convertDto(e, CallbackDistListResponse.class);

                    Map<String, List<CallbackDistEntity>> groupingHuntNumber = callbackDistList.stream()
                            .filter(callback -> callback.getServiceNumber().equals(e.getSvcNumber()))
                            .collect(Collectors.groupingBy(CallbackDistEntity::getHuntNumber));

                    List<CallbackHuntListResponse> huntListResponses = new ArrayList<>();

                    groupingHuntNumber.forEach((key, value) -> {
                        if (entity.getSvcNumber().equals(key)) {
                            final CallbackHuntListResponse huntListResponse = new CallbackHuntListResponse();
                            huntListResponse.setQueueNumber(entity.getSvcNumber());

                            List<CallbackPersonResponse> idNames = groupingHuntNumber.get(key).stream()
                                    .map(callback -> {
                                        CallbackPersonResponse personResponse = convertDto(callback, CallbackPersonResponse.class);
                                        if (StringUtils.isNotEmpty(personResponse.getUserid()))
                                            personLists.stream().filter(person -> person.getId().equals(personResponse.getUserid()))
                                                    .findFirst()
                                                    .ifPresent(person -> personResponse.setIdName(person.getIdName()));
                                        return personResponse;
                                    })
                                    .collect(Collectors.toList());

                            huntListResponse.setIdNames(idNames);
                            huntListResponse.setHanName(entity.getSvcName());
                            huntListResponses.add(huntListResponse);
                        }
                        queueNameList.stream().filter(queue -> queue.getNumber().equals(key)).findFirst()
                                .ifPresent(queue -> {
                                    List<CallbackDistEntity> callbackDistEntityList = groupingHuntNumber.get(key);
                                    CallbackHuntListResponse huntListResponse = new CallbackHuntListResponse();
                                    huntListResponse.setQueueNumber(key);

                                    huntListResponse.setHanName(queue.getHanName());

                                    List<CallbackPersonResponse> idNames = callbackDistEntityList.stream()
                                            .map(callback -> {
                                                CallbackPersonResponse personResponse = convertDto(callback, CallbackPersonResponse.class);
                                                if (StringUtils.isNotEmpty(personResponse.getUserid()))
                                                    personLists.stream().filter(person -> person.getId().equals(personResponse.getUserid()))
                                                            .findFirst()
                                                            .ifPresent(person -> personResponse.setIdName(person.getIdName()));
                                                return personResponse;
                                            })
                                            .collect(Collectors.toList());

                                    huntListResponse.setIdNames(idNames);

                                    huntListResponses.add(huntListResponse);
                                });
                    });

                    entity.setHunts(huntListResponses);

                    return entity;
                }).collect(Collectors.toList());

        return ResponseEntity.ok(data(rows));
    }

    /**
     * 분배 가능한 헌트 목록조회
     */
    @GetMapping("divisible-hunts")
    public ResponseEntity<JsonResult<List<SummaryQueueResponse>>> addHunts() {
        List<SummaryQueueResponse> addHunts = serviceRepository.findAll().stream()
                .map(e -> {
                    final SummaryQueueResponse response = convertDto(e, SummaryQueueResponse.class);
                    response.setNumber(e.getSvcNumber());
                    response.setName(e.getSvcName());
                    response.setHanName(e.getSvcName());

                    return response;
                })
                .collect(Collectors.toList());
        addHunts.addAll(queueNameRepository.findAll().stream()
                .map(e -> convertDto(e, SummaryQueueResponse.class)).collect(Collectors.toList()));

        return ResponseEntity.ok(data(addHunts));
    }

    /**
     * 분배 가능한 사용자 목록조회
     */
    @GetMapping("divisible-persons")
    public ResponseEntity<JsonResult<List<SummaryCallbackDistPersonResponse>>> addPersons() {
        return ResponseEntity.ok(data(
                personListRepository.findAll().stream()
                        .filter(e -> StringUtils.isNotEmpty(e.getPeer()))
                        .map(e -> convertDto(e, SummaryCallbackDistPersonResponse.class))
                        .collect(Collectors.toList())
        ));
    }
}
