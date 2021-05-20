package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

import java.util.List;

@Data
public class EmailReceiveGroupDetailResponse {
    private Integer groupId;           //그룹아이디
    private Integer serviceId;         //서비스아이디
    private String groupName;          //이메일그룹명
    private String serviceName;        //관련이메일 서비스명

    private List<EmailMemberListSummaryResponse> emailMemberLists;  //그룹멤버
}
