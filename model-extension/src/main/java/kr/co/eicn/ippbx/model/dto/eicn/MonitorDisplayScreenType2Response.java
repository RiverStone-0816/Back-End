package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

import java.util.List;

@Data
public class MonitorDisplayScreenType2Response {
    private String screenName;
    private Double  resAve;     //응답률
    private Integer connReq;    //연결요청
    private Integer success;    //응답호
    private Integer waitCnt;
    private Integer callbackWaitCnt;
    private List<DisplayMemberStatusListResponse> memberStatusList; //상담원상태리스트
    private List<DisplayMemberListResponse> memberList; //상담원정보리스트
    private String slidingText;
}
