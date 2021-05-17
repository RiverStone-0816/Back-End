package kr.co.eicn.ippbx.server.controller.api.v1.admin.monitor.consultant;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ScheduleInfo;
import kr.co.eicn.ippbx.server.model.dto.eicn.MonitorIvrResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.MonitorIvrScheduleGroupListResponse;
import kr.co.eicn.ippbx.server.model.enums.ScheduleKind;
import kr.co.eicn.ippbx.server.repository.eicn.IvrTreeRepository;
import kr.co.eicn.ippbx.server.repository.eicn.ScheduleGroupListRepository;
import kr.co.eicn.ippbx.server.repository.eicn.ScheduleGroupRepository;
import kr.co.eicn.ippbx.server.repository.eicn.ScheduleInfoRepository;
import kr.co.eicn.ippbx.server.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

/**
 * 모니터링 > 상담원 모니터링 > 모니터링[IVR]
 */
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/monitor/consultant/ivr", produces = MediaType.APPLICATION_JSON_VALUE)
public class MonitorIVRApiController extends ApiBaseController {
    private final IvrTreeRepository ivrTreeRepository;
    private final ScheduleInfoRepository scheduleInfoRepository;
    private final ScheduleGroupRepository scheduleGroupRepository;
    private final ScheduleGroupListRepository scheduleGroupListRepository;

    @GetMapping("")
    public ResponseEntity<JsonResult<MonitorIvrResponse>> serviceByMonitoring(@RequestParam String serviceNumber) {
        ScheduleInfo scheduleInfo = scheduleInfoRepository.getScheduleByService(serviceNumber);
        MonitorIvrResponse monitorIvrResponse = new MonitorIvrResponse();

        if (scheduleInfo != null) {
            monitorIvrResponse.setScheduleInfo(scheduleInfo);
            monitorIvrResponse.setScheduleGroup(scheduleGroupRepository.findOne(scheduleInfo.getGroupId()));
            monitorIvrResponse.setScheduleContents(
                    scheduleGroupListRepository.findAllByGroupId(scheduleInfo.getGroupId()).stream().map(e -> {
                        MonitorIvrScheduleGroupListResponse content = convertDto(e, MonitorIvrScheduleGroupListResponse.class);

                        if (content.getScheduleGroupList().getKind().equals(ScheduleKind.IVR_CONNECT.getCode()))
                            content.setMonitorIvrTree(ivrTreeRepository.getMonitorIvrTree(Integer.parseInt(content.getScheduleGroupList().getKindData())));

                        return content;
                    }).collect(Collectors.toList())
            );
        }

        return ResponseEntity.ok(JsonResult.data(monitorIvrResponse));
    }
}
