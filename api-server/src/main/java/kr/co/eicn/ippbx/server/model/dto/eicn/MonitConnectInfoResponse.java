package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

import java.util.List;

@Data
public class MonitConnectInfoResponse {
    private String companyId;   //회사아이디
    private String userId;  //유저아이디
    private String exten;   //내선
    private String pbxName; //pbx이름
    private String nodejsUrl;   //소켓 연결 url
    private String errMsg;  //에러메시지
}
