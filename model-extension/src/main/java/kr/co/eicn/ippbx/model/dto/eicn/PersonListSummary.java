package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

@Data
public class PersonListSummary {
    private String id;                  //사용자 아이디
    private String idName;              //사용자 이름
    private String extension;           //내선번호
    private String peer;                //개인번호
    /**
     * @see kr.co.eicn.ippbx.model.enums.PersonPausedStatus
     * */
    private Integer paused = 0;  //상태
    private String isLogin;
}
