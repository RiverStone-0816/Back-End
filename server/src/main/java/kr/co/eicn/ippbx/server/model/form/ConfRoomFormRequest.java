package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;

@Data
public class ConfRoomFormRequest extends BaseForm {
    @NotNull("회의실명")
    private String roomName;
    @NotNull("회의실 번호")
    private String roomNumber;
    @NotNull("회의실 RID")
    private String roomCid;
    @NotNull("회의실 단축번호")
    private String roomShortNum;
    @NotNull("그룹코드")
    private String groupCode;
//    @NotNull("그룹코드나열")
//    private String groupTreeName;
//    @NotNull("그룹레벨")
//    private int groupLevel;
//    @NotNull("회사아이디")
//    private String companyId;
}
