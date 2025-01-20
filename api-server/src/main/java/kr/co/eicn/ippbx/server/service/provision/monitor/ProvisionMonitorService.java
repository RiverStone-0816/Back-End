package kr.co.eicn.ippbx.server.service.provision.monitor;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import kr.co.eicn.ippbx.model.dto.customdb.PersonLastStatusInfoResponse;
import kr.co.eicn.ippbx.model.dto.provision.ProvisionStatUserResponse;
import kr.co.eicn.ippbx.model.search.StatUserSearchRequest;
import kr.co.eicn.ippbx.server.controller.api.v1.admin.stat.user.StatUserApiController;
import kr.co.eicn.ippbx.server.service.MemberStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProvisionMonitorService {

  private final StatUserApiController statUserApiController;
  private final MemberStatusService memberStatusService;

  /**
   * 상담원 상태 정보를 조회 및 매핑
   */
  public Map<String, ProvisionStatUserResponse> getAgentCallStatus() {
    // StatUser 데이터를 가져오기
    var statUserResponse = statUserApiController.getTodayAgentActivitySummary(new StatUserSearchRequest());
    var responseData = Objects.requireNonNull(statUserResponse.getBody()).getData();

    // 상담원 상태 데이터를 가져오기
    List<PersonLastStatusInfoResponse> filterPersonStatusInfo = memberStatusService.getAllPersonStatusInfo();

    // 데이터를 매핑하여 Map으로 반환
    return responseData.stream()
        .flatMap(stat -> stat.getUserStatList().stream())
        .collect(Collectors.toMap(
            // Map의 키를 userId로 설정
            userStat -> userStat.getUserId(),
            // Map의 값을 ProvisionStatUserResponse로 설정
            userStat -> {
              ProvisionStatUserResponse provisionResponse = new ProvisionStatUserResponse();
              provisionResponse.setUserId(userStat.getUserId());
              provisionResponse.setIdName(userStat.getIdName());
              provisionResponse.setStatUser(getNextStatus(filterPersonStatusInfo, userStat.getUserId()));
              provisionResponse.setInboundTotal(userStat.getInboundStat().getTotal());
              provisionResponse.setInboundCancel(userStat.getInboundStat().getCancel());
              provisionResponse.setTotalCount(userStat.getTotalCnt());
              Map<Integer, Long> statusCountMap = userStat.getMemberStatusStat().getStatusCountMap();
              provisionResponse.setPostProcess(Math.toIntExact(statusCountMap.getOrDefault(2, 0L)));
              provisionResponse.setRest(Math.toIntExact(statusCountMap.getOrDefault(3, 0L)));
              return provisionResponse;
            }
        ));
  }

  /**
   * 상담원의 다음 상태를 조회
   */
  private Integer getNextStatus(List<PersonLastStatusInfoResponse> filterPersonStatusInfo, String userId) {
    return filterPersonStatusInfo.stream()
        .filter(info -> userId.equals(info.getPhonename()))
        .map(PersonLastStatusInfoResponse::getNextStatus)
        .findFirst()
        .orElse(null);
  }
}
