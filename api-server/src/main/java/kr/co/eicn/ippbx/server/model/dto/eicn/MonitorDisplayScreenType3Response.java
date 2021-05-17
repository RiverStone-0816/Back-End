package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

import java.util.List;

@Data
public class MonitorDisplayScreenType3Response {
    private String screenName;
    private String statDate; // 날짜
    private Integer waitSum;    //대기호
    private Integer callBack;   //콜백
    private Long  svcLevelAvg;    //서비스레벨
    private Integer memberCnt;  //총인원

    private List<MonitorDisplayResponse> displayResponses;
    private List<DisplayMemberStatusListResponse> memberStatusList; //상담원상태리스트
    private List<DisplayHuntMemberListResponse> memberList; //상담원정보리스트
}
