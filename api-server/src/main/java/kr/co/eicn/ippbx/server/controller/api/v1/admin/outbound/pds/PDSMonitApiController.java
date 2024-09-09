package kr.co.eicn.ippbx.server.controller.api.v1.admin.outbound.pds;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.*;
import kr.co.eicn.ippbx.model.enums.PDSGroupConnectKind;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.entity.eicn.ExecutePDSGroupEntity;
import kr.co.eicn.ippbx.model.search.PDSMonitSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.*;
import kr.co.eicn.ippbx.server.service.ExecutePDSCustomInfoService;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.PdsIvr.PDS_IVR;
import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 아웃바운드관리 > PDS > 실행/모니터링
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/outbound/pds/monit", produces = MediaType.APPLICATION_JSON_VALUE)
public class PDSMonitApiController extends ApiBaseController {

    private final ExecutePDSCustomInfoService executePDSCustomInfoService;
    private final PDSGroupRepository          pdsGroupRepository;
    private final ExecutePDSGroupRepository   executePDSGroupRepository;
    private final QueueMemberTableRepository  queueMemberTableRepository;
    private final PersonListRepository        personListRepository;
    private final CommonTypeRepository        commonTypeRepository;
    private final PDSIvrRepository            pdsIvrRepository;
    private final PDSQueueNameRepository      pdsQueueNameRepository;
    private final ResearchListRepository      researchListRepository;

    /**
     * pds 실행/모니터링 목록 조회
     **/
    @GetMapping("")
    public ResponseEntity<JsonResult<Pagination<PDSMonitResponse>>> pagination(PDSMonitSearchRequest search) {
        final Pagination<PdsGroup> pagination = pdsGroupRepository.pagination(search);
        final List<QueueMemberTable> queueMemberList = queueMemberTableRepository.findAllPDSMember();
        final List<PersonList> personList = personListRepository.findAll();
        final List<CommonType> commonTypeList = commonTypeRepository.findAll();
        final Map<String, String> personListMap = personListRepository.getIdAndNameMap();

        final Map<String, Map<String, String>> connectDataValueMap = new HashMap<>();
        connectDataValueMap.put(PDSGroupConnectKind.PDS_IVR.getCode(), pdsIvrRepository.findAll(PDS_IVR.PARENT.eq(0)).stream().collect(Collectors.toMap(e -> String.valueOf(e.getCode()), PdsIvr::getName)));
        connectDataValueMap.put(PDSGroupConnectKind.MEMBER.getCode(), pdsQueueNameRepository.findAll().stream().collect(Collectors.toMap(PdsQueueName::getName, PdsQueueName::getHanName)));
        if (g.isServiceAvailable("RSH"))
            connectDataValueMap.put(PDSGroupConnectKind.ARS_RSCH.getCode(), researchListRepository.findAll().stream().collect(Collectors.toMap(e -> String.valueOf(e.getResearchId()), ResearchList::getResearchName)));

        final List<PDSMonitResponse> rows = pagination.getRows().stream()
                .map(pdsGroup -> {
                    final PDSMonitResponse pdsMonitResponse = new PDSMonitResponse();

                    final PDSMonitGroupResponse pdsGroupEntity = convertDto(pdsGroup, PDSMonitGroupResponse.class);
                    pdsGroupEntity.setConnectDataValue(connectDataValueMap.containsKey(pdsGroup.getConnectKind()) ? connectDataValueMap.get(pdsGroup.getConnectKind()).getOrDefault(pdsGroup.getConnectData(), "") : "");
                    pdsMonitResponse.setPdsGroup(pdsGroupEntity);

                    final ExecutePDSGroupEntity executePDSGroupEntity = executePDSGroupRepository.findByRunHost(pdsGroup.getRunHost(), pdsGroup.getLastExecuteId());
                    final ExecutePDSGroupResponse executePDSGroupResponse = convertDto(executePDSGroupEntity, ExecutePDSGroupResponse.class);
                    executePDSGroupResponse.setSpeedData(executePDSGroupEntity.getSpeedDataBySpeedKind());
                    executePDSGroupResponse.setPdsTypeName(commonTypeList.stream().filter(type -> type.getSeq().equals(executePDSGroupEntity.getPdsType())).map(CommonType::getName).findFirst().orElse(""));
                    executePDSGroupResponse.setPdsAdminName(personListMap.getOrDefault(executePDSGroupEntity.getAdminid(), executePDSGroupEntity.getAdminid()));
                    pdsMonitResponse.setExecuteGroup(executePDSGroupResponse);

                    pdsMonitResponse.setCount(executePDSCustomInfoService.findAllCount(pdsGroup.getLastExecuteId(), pdsGroup.getRunHost()));

                    final Map<String, Integer> memberList = queueMemberList.stream().filter(queue -> queue.getQueueName().equals(pdsGroup.getConnectData()))
                            .collect(Collectors.toMap(QueueMemberTable::getMembername, QueueMemberTable::getPaused));
                    pdsMonitResponse.setPersons(
                            personList.stream().filter(person -> memberList.containsKey(person.getId()))
                                    .map(person -> {
                                        final PersonListSummary personListSummary = convertDto(person, PersonListSummary.class);
                                        personListSummary.setPaused(memberList.get(person.getId()));

                                        return personListSummary;
                                    }).collect(Collectors.toList())
                    );

                    return pdsMonitResponse;
                }).collect(Collectors.toList());

        return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
    }

    /**
     * 실행중인 pds 목록 조회
     **/
    @GetMapping("running-pds-list")
    public ResponseEntity<JsonResult<List<PDSExecuteListResponse>>> getPDSList(PDSMonitSearchRequest search) {
        return ResponseEntity.ok(data(pdsGroupRepository.findAll(search).stream().map(e -> convertDto(e, PDSExecuteListResponse.class)).collect(Collectors.toList())));
    }
}
