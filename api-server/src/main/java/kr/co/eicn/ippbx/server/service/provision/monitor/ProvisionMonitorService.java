package kr.co.eicn.ippbx.server.service.provision.monitor;

import java.util.HashMap;
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
  public Map<String, List<ProvisionStatUserResponse>> getAgentCallStatus() {
    // StatUser 데이터를 가져오기
    var statUserResponse = statUserApiController.getTodayAgentActivitySummary(new StatUserSearchRequest());
    var responseData = Objects.requireNonNull(statUserResponse.getBody()).getData();

    // 상담원 상태 데이터를 가져오기
    List<PersonLastStatusInfoResponse> filterPersonStatusInfo = memberStatusService.getAllPersonStatusInfo();

    // 데이터를 List로 매핑
    List<ProvisionStatUserResponse> provisionResponseList = responseData.stream()
        .flatMap(stat -> stat.getUserStatList().stream().map(userStat -> {
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
        }))
        .collect(Collectors.toList());

    // List를 Map에 추가 (키는 고정값 "list"로 설정)
    Map<String, List<ProvisionStatUserResponse>> resultMap = new HashMap<>();
    resultMap.put("list", provisionResponseList);

    return resultMap;
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