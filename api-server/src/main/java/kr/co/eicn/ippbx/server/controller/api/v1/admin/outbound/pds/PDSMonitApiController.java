package kr.co.eicn.ippbx.server.controller.api.v1.admin.outbound.pds;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonType;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsGroup;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueMemberTable;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.entity.eicn.ExecutePDSGroupEntity;
import kr.co.eicn.ippbx.model.search.PDSMonitSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.*;
import kr.co.eicn.ippbx.server.service.ExecutePDSCustomInfoService;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 아웃바운드관리 > PDS > 실행/모니터링
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/admin/outbound/pds/monit")
public class PDSMonitApiController extends ApiBaseController {
    private final ExecutePDSCustomInfoService executePDSCustomInfoService;
    private final PDSGroupRepository pdsGroupRepository;
    private final ExecutePDSGroupRepository executePDSGroupRepository;
    private final QueueMemberTableRepository queueMemberTableRepository;
    private final PersonListRepository personListRepository;
    private final CommonTypeRepository commonTypeRepository;

    /**
     * pds 실행 목록 조회
     **/
    @GetMapping("")
    public ResponseEntity<JsonResult<Pagination<PDSMonitResponse>>> pagination(PDSMonitSearchRequest search) {
        Pagination<PdsGroup> pagination = pdsGroupRepository.pagination(search);
        List<QueueMemberTable> queueMemberList = queueMemberTableRepository.findAllPDSMember();
        List<PersonList> personList = personListRepository.findAll();
        List<CommonType> commonTypeList = commonTypeRepository.findAll();

        List<PDSMonitResponse> rows = pagination.getRows().stream()
                .map(pdsGroup -> {
                    PDSMonitResponse pdsMonitResponse = new PDSMonitResponse();
                    pdsMonitResponse.setPdsGroup(convertDto(pdsGroup, PDSMonitGroupResponse.class));
                    ExecutePDSGroupEntity executePDSGroupEntity = executePDSGroupRepository.findByRunHost(pdsGroup.getRunHost(), pdsGroup.getLastExecuteId());
                    ExecutePDSGroupResponse executePDSGroupResponse = convertDto(executePDSGroupEntity, ExecutePDSGroupResponse.class);
                    executePDSGroupResponse.setSpeedData(executePDSGroupEntity.getSpeedDataBySpeedKind());
                    executePDSGroupResponse.setPdsTypeName(commonTypeList.stream().filter(type -> type.getSeq().equals(executePDSGroupEntity.getPdsType())).map(CommonType::getName).findFirst().orElse(""));
                    pdsMonitResponse.setExecuteGroup(executePDSGroupResponse);

                    pdsMonitResponse.setCount(executePDSCustomInfoService.findAllCount(pdsGroup.getLastExecuteId(), pdsGroup.getRunHost()));

                    Map<String, Integer> memberList = queueMemberList.stream().filter(queue -> queue.getQueueName().equals(pdsGroup.getConnectData()))
                            .collect(Collectors.toMap(QueueMemberTable::getMembername, QueueMemberTable::getPaused));

                    pdsMonitResponse.setPersons(
                            personList.stream().filter(person -> memberList.containsKey(person.getId()))
                                    .map(person -> {
                                        PersonListSummary personListSummary = convertDto(person, PersonListSummary.class);

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
