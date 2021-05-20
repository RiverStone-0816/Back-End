package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

import java.util.List;

@Data
public class MonitorDisplayScreenType4Response {
    private String screenName;
    private String  statDate; // 날짜
    private Integer connReq;    //연결요청
    private Integer customWaitCnt;  //고객대기

    private List<MonitorDisplayResponse> displayResponses;
    private List<DisplayMemberStatusListResponse> memberStatusList; //상담원상태리스트
    private List<DisplayHuntMemberListResponse> memberList; //상담원정보리스트
}
