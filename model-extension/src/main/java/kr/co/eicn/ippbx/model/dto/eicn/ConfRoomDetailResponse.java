package kr.co.eicn.ippbx.model.dto.eicn;

//import kr.co.eicn.ippbx.server.model.enums.MailProtocolType;
import lombok.Data;

import java.util.List;

@Data
public class ConfRoomDetailResponse {
    private Integer seq;          //시퀀스
    private String roomName;  //회의실명
    private String roomNumber;         //회의실번호
    private String roomCid;            //회의실RID
    private String roomShortNum;        //회의실단축번호
    private String groupCode;   //그룹코드
    private String groupTreeName;   //그룹코드나열
    private Integer groupLevel;     //그룹레벨
    private String companyId;   //회사아이디
    private List<OrganizationSummaryResponse> companyTrees;
}
