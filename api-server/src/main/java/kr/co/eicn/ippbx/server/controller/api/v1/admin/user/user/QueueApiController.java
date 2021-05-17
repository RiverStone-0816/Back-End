package kr.co.eicn.ippbx.server.controller.api.v1.admin.user.user;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.exception.ValidationException;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.*;
import kr.co.eicn.ippbx.server.model.dto.eicn.*;
import kr.co.eicn.ippbx.server.model.entity.eicn.QueueEntity;
import kr.co.eicn.ippbx.server.model.enums.NoConnectKind;
import kr.co.eicn.ippbx.server.model.form.QueueFormRequest;
import kr.co.eicn.ippbx.server.model.form.QueueFormUpdateRequest;
import kr.co.eicn.ippbx.server.model.form.QueueUpdateBlendingFormRequest;
import kr.co.eicn.ippbx.server.model.search.QueueSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.*;
import kr.co.eicn.ippbx.server.service.OrganizationService;
import kr.co.eicn.ippbx.server.util.FunctionUtils;
import kr.co.eicn.ippbx.server.util.JsonResult;
import kr.co.eicn.ippbx.server.util.ReflectionUtils;
import kr.co.eicn.ippbx.server.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.impl.DSL;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static kr.co.eicn.ippbx.server.jooq.eicn.tables.PersonList.PERSON_LIST;
import static kr.co.eicn.ippbx.server.jooq.eicn.tables.PhoneInfo.PHONE_INFO;
import static kr.co.eicn.ippbx.server.jooq.eicn.tables.QueueMemberTable.QUEUE_MEMBER_TABLE;
import static kr.co.eicn.ippbx.server.util.JsonResult.create;
import static kr.co.eicn.ippbx.server.util.JsonResult.data;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * 번호/그룹/사용자 > 사용자관리 > 헌트그룹설정
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/user/user/queue", produces = MediaType.APPLICATION_JSON_VALUE)
public class QueueApiController extends ApiBaseController {
    private final QueueRepository repository;
    private final Number070Repository number070Repository;
    private final OrganizationService organizationService;
    private final QueueMemberTableRepository queueMemberTableRepository;
    private final PhoneInfoRepository phoneInfoRepository;
    private final ServerInfoRepository serverInfoRepository;
    private final PersonListRepository personListRepository;
    private final ServiceRepository serviceRepository;
    private final MohListRepository mohListRepository;
    private final ContextInfoRepository contextInfoRepository;

    @GetMapping("")
    public ResponseEntity<JsonResult<Pagination<QueueSummaryResponse>>> pagination(QueueSearchRequest search) {
        final Pagination<QueueEntity> pagination = repository.pagination(search);
        final List<CompanyTree> companyTrees = organizationService.getAllCompanyTrees();
        final Map<String, List<QueueMemberTable>> groupingQueueNameQueueMemberTableMap = queueMemberTableRepository.findAll().stream()
                .collect(groupingBy(QueueMemberTable::getQueueName));

        final List<QueueSummaryResponse> rows = pagination.getRows().stream()
                .map((e) -> {
                    final QueueSummaryResponse summary = convertDto(e, QueueSummaryResponse.class);
                    if (e.getSubGroupQueueName() != null)
                        summary.setSubGroupName(e.getSubGroupQueueName().getHanName());

                    if (isNotEmpty(e.getGroupCode()))
                        summary.setCompanyTrees(organizationService.getCompanyTrees(companyTrees, e.getGroupCode())
                                .stream()
                                .map(group -> convertDto(group, OrganizationSummaryResponse.class))
                                .collect(toList()));

                    if (groupingQueueNameQueueMemberTableMap.containsKey(e.getName()))
                        summary.setPersonCount(groupingQueueNameQueueMemberTableMap.get(e.getName()).size());

                    return summary;
                })
                .collect(toList());

        return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
    }

    @GetMapping("{name}")
    public ResponseEntity<JsonResult<QueueDetailResponse>> get(@PathVariable String name) {
        final QueueEntity queue = repository.findOneIfNullThrow(name);
        final QueueDetailResponse detail = convertDto(queue, QueueDetailResponse.class);
        detail.setNoConnectKind(NoConnectKind.of(queue.getNoConnectKind()));
        detail.setRetryMaxCount(queue.getRetryMaxCnt());
        final List<ServerInfo> serverInfos = serverInfoRepository.findAll();
        final List<CompanyTree> companyTrees = organizationService.getAllCompanyTrees();

        final List<QueueMemberTable> queueMemberTables = queueMemberTableRepository.findAll(QUEUE_MEMBER_TABLE.QUEUE_NAME.eq(queue.getName()), Arrays.asList(QUEUE_MEMBER_TABLE.PENALTY.asc(), QUEUE_MEMBER_TABLE.UNIQUEID.asc()));

        final List<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PhoneInfo> phoneInfos = phoneInfoRepository.findAll(PHONE_INFO.HOST.eq(queue.getHost()));

        final Map<String, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PersonList> personListMap = personListRepository.findAll(PERSON_LIST.EXTENSION.isNotNull().and(PERSON_LIST.EXTENSION.notEqual(EMPTY)))
                .stream()
                .filter(FunctionUtils.distinctByKey(PersonList::getExtension))
                .collect(Collectors.toMap(kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PersonList::getExtension, e -> e));

        if (queue.getRetryMaxCnt() > 0) {
            String[] sound = queue.getRetrySound().split("\\|");
            if (sound.length == 2) {
                detail.setRetrySoundCode(sound[0]);
                detail.setTtsData(sound[1]);
            } else {
                detail.setRetrySoundCode(sound[0]);
            }
        }
        ReflectionUtils.copy(detail, queue.getQueueTable(), "strategy");

        if (queue.getSubGroupQueueName() != null)
        	detail.setSubGroupName(queue.getSubGroupQueueName().getHanName());
        if (isNotEmpty(detail.getMusiconhold())) {
            final MohList ringBackTone = mohListRepository.findOne(detail.getMusiconhold());
            if (ringBackTone != null)
                detail.setMusiconholdName(ringBackTone.getMohName());
        }
        if (isNotEmpty(queue.getGroupCode()))
        	detail.setCompanyTrees(organizationService.getCompanyTrees(companyTrees, queue.getGroupCode())
                    .stream()
                    .map(group -> convertDto(group, OrganizationSummaryResponse.class))
                    .collect(toList()));
        if (queueMemberTables.size() > 0)
            detail.setPersonCount(queueMemberTables.size());

        // 추가된 사용자 -> 없음 []
        detail.setAddPersons(queueMemberTables.stream()
                .map((e) -> {
                    final SummaryQueuePersonResponse summaryPerson = convertDto(e, SummaryQueuePersonResponse.class);

                    if (isNotEmpty(queue.getHost()))
                        serverInfos.stream().filter(server -> isNotEmpty(server.getHost()) && server.getHost().equals(queue.getHost())).findFirst()
                                .ifPresent(server -> summaryPerson.setHostName(server.getName()));

                    phoneInfos.stream()
                            .filter(phone -> phone.getPeer().equals(e.getMembername()))
                            .findFirst()
                            .ifPresent(phone -> {
                                summaryPerson.setPeer(phone.getPeer());
                                summaryPerson.setExtension(phone.getExtension());

                                final PersonList personList = personListMap.get(phone.getExtension());
                                if (personList != null) {
                                    summaryPerson.setIdName(personList.getIdName());

                                    summaryPerson.setCompanyTrees(
                                            organizationService.getCompanyTrees(companyTrees, personList.getGroupCode())
                                                    .stream()
                                                    .map(group -> convertDto(group, OrganizationSummaryResponse.class))
                                                    .collect(toList()));
                                }
                                else
                                    summaryPerson.setIdName("");


                            });



                    return summaryPerson;
                })
                .collect(toList()));

        return ResponseEntity.ok(data(detail));
    }

    @PostMapping("")
    public ResponseEntity<JsonResult<String>> register(@Valid @RequestBody QueueFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        return ResponseEntity.created(URI.create("/api/v1/admin/user/user/queue")).body(data(repository.insertOnGeneratedKey(form)));
    }

    @PutMapping("{name}")
    public ResponseEntity<JsonResult<Void>> update(@Valid @RequestBody QueueFormUpdateRequest form, BindingResult bindingResult, @PathVariable String name) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        form.setName(name);
        repository.update(form);
        return ResponseEntity.ok(create());
    }

    @DeleteMapping("{name}")
    public ResponseEntity<JsonResult<Void>> delete(@PathVariable String name) {
    	repository.delete(name);
        return ResponseEntity.ok(create());
    }

    @PatchMapping("{name}/blending")
    public ResponseEntity<JsonResult<Void>> updateBlending(@Valid @RequestBody QueueUpdateBlendingFormRequest form, BindingResult bindingResult, @PathVariable String name) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        repository.blendingUpdate(form, name);
        repository.blending(form.getBlendingUser(), name);

        return ResponseEntity.ok(create());
    }

    @GetMapping("{name}/blending")
    public ResponseEntity<JsonResult<List<BlendingPersonsResponse>>> getBlendingPersons(@PathVariable String name) {
        List<PersonList> personLists = personListRepository.findAll();
        return ResponseEntity.ok(data(
                queueMemberTableRepository.getBlendingPersons(name).stream()
                        .map(e -> {
                            BlendingPersonsResponse response = convertDto(e, BlendingPersonsResponse.class);

                            response.setIdName(
                                    personLists.stream().filter(person -> person.getPeer().equals(e.getMembername()))
                                    .map(PersonList::getIdName).findFirst().orElse("")
                            );

                            return response;
                        }).collect(Collectors.toList())
        ));
    }

    /**
     * 헌트그룹 관련서비스 목록조회
     */
    @GetMapping("services")
    public ResponseEntity<JsonResult<List<SummaryServiceListResponse>>> services(@RequestParam(required = false) String host) {
        return ResponseEntity.ok(
                data(serviceRepository.findAllJoinNumber070().stream()
                    .filter(service -> {
                    	if (isNotEmpty(host))
                    		return service.getNumber070().getHost().equals(host);
                        return true;
                    })
                    .map((e) -> convertDto(e, SummaryServiceListResponse.class))
                    .collect(toList())
                )
        );
    }

    /**
     *  헌트그룹 예비헌트 목록조회
     */
    @GetMapping("sub-groups")
    public ResponseEntity<JsonResult<List<SummaryQueueResponse>>> subGroups(@RequestParam(required = false) String name/*queue_name.name*/) {
        return ResponseEntity.ok(
                data(repository.findAll().stream()
                    .filter(queue -> {
                        if (isNotEmpty(name))
                        	return !queue.getName().equals(name);
                        return true;
                    })
                    .map(subQueue -> convertDto(subQueue, SummaryQueueResponse.class))
                    .collect(Collectors.toList())
                )
        );
    }

    /**
     *  추가 가능한 사용자 목록조회
     */
    @GetMapping("add-on-persons")
    public ResponseEntity<JsonResult<List<SummaryPersonResponse>>> addOnPersons(@RequestParam(required = false) String name) {
    	QueueEntity queue = isNotEmpty(name) ? repository.findOneIfNullThrow(name) : null;
        final List<ServerInfo> serverInfos = serverInfoRepository.findAll();
        final List<CompanyTree> companyTrees = organizationService.getAllCompanyTrees();

        final List<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PhoneInfo> phoneInfos = phoneInfoRepository.findAll(queue != null
                        ? PHONE_INFO.HOST.eq(queue.getHost()) : DSL.noCondition()
                , PHONE_INFO.EXTENSION.asc());

        final List<QueueMemberTable> queueMemberTables = queueMemberTableRepository.findAll(queue != null
                        ? QUEUE_MEMBER_TABLE.QUEUE_NAME.eq(queue.getName()) : DSL.noCondition()
                , Arrays.asList(QUEUE_MEMBER_TABLE.PENALTY.asc(), QUEUE_MEMBER_TABLE.UNIQUEID.asc()));

        final Map<String, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PersonList> personListMap = personListRepository.findAll(PERSON_LIST.EXTENSION.isNotNull().and(PERSON_LIST.EXTENSION.notEqual(EMPTY)))
                .stream()
                .collect(Collectors.toMap(kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PersonList::getExtension, e -> e));

        final List<SummaryPersonResponse> persons = phoneInfos
                .stream()
                .filter(e -> {
                            if (queue != null) {
                                return queueMemberTables.stream()
                                    .noneMatch(queueMemberTable -> queueMemberTable.getMembername().equals(e.getPeer()));
                            }
                            return true;
                        }
                )
                .map((e) -> {
                    final SummaryPersonResponse summaryPerson = convertDto(e, SummaryPersonResponse.class);

                    if (isNotEmpty(e.getHost()))
                        serverInfos.stream().filter(server -> isNotEmpty(server.getHost()) && server.getHost().equals(e.getHost())).findFirst()
                                .ifPresent(server -> summaryPerson.setHostName(server.getName()));

                    final PersonList personList = personListMap.get(e.getExtension());
                    summaryPerson.setIdName(EMPTY);

                    if (personList != null) {
                        summaryPerson.setIdName(personList.getIdName());

                        if (isNotEmpty(personList.getGroupCode()))
                            summaryPerson.setCompanyTrees(organizationService.getCompanyTrees(companyTrees, personList.getGroupCode())
                                    .stream()
                                    .map(group -> convertDto(group, OrganizationSummaryResponse.class))
                                    .collect(toList()));
                    }

                    return summaryPerson;
                })
                .collect(toList());

        return ResponseEntity.ok(data(persons));
    }

    /**
     *  대기음원 목록 조회
     */
    @GetMapping("ring-back-tones")
    public ResponseEntity<JsonResult<List<SummaryMohListResponse>>> ringBackTone() {
        return ResponseEntity.ok(
                data(mohListRepository.findAll().stream()
                        .map((e) -> convertDto(e, SummaryMohListResponse.class))
                        .sorted(comparing(SummaryMohListResponse::getMohName))
                        .collect(Collectors.toList())
                )
        );
    }

    /**
     *  컨텍스트 목록 조회
     */
    @GetMapping("context")
    public ResponseEntity<JsonResult<List<SummaryContextInfoResponse>>> context() {
        return ResponseEntity.ok(
                data(contextInfoRepository.findAll().stream()
                        .map((e) -> convertDto(e, SummaryContextInfoResponse.class))
                        .sorted(comparing(SummaryContextInfoResponse::getName))
                        .collect(Collectors.toList())
                )
        );
    }

    /**
     * queue_name 목록 조회
     */
    @GetMapping("add-queue")
    public ResponseEntity<JsonResult<List<SummaryQueueResponse>>> addQueueNames() {
        return ResponseEntity.ok(data(repository.findAll()
                .stream()
                .map(e -> convertDto(e, SummaryQueueResponse.class))
                .collect(Collectors.toList()))
        );
    }

    /**
     * PDS_QUEUE
     */
    @GetMapping("pds-queue-status")
    public ResponseEntity<JsonResult<List<PDSQueuePersonResponse>>> getPdsQueuePersonStatus() {
        return ResponseEntity.ok(data(queueMemberTableRepository.getPDSQueuePersonStatus()));
    }
}
