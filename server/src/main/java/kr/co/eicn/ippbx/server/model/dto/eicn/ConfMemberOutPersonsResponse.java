package kr.co.eicn.ippbx.server.model.dto.eicn;

//import kr.co.eicn.ippbx.server.model.enums.MailProtocolType;

import lombok.Data;

@Data
public class ConfMemberOutPersonsResponse {
    private Integer seq;          //시퀀스
    private String memberType;  //멤버타입
    private String memberName;  //멤버네임
    private String memberNumber;
}
