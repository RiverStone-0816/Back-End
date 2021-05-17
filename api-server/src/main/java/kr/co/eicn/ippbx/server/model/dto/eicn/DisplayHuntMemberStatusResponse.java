package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

import java.util.List;

@Data
public class DisplayHuntMemberStatusResponse {
    private String queueHanName;     //큐한글명
    private String queueName;     //큐명
    private Integer customWaitCnt;     //고객대기수
    private List<DisplayMemberStatusListResponse> memberStatusList; //상담원상태리스트
    private List<DisplayHuntMemberListResponse> queueMemberList; //상담원정보리스트
}
