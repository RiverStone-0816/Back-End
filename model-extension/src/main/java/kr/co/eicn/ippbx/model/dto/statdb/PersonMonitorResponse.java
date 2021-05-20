package kr.co.eicn.ippbx.model.dto.statdb;

import lombok.Data;

@Data
public class PersonMonitorResponse {
    private String groupName;
    private String groupTreeName;
    private String idName;      //상담사명
    private String userId;      //상담사ID
    private String extension;   //내선번호
    private String peer;        //개인번호

    private PersonStatusResponse statusResponse;    //상담사 개인별 상태
    private PersonStatResponse statResponse;        //상담사 개인별 통계
}
