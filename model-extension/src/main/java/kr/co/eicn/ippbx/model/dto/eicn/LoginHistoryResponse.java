package kr.co.eicn.ippbx.model.dto.eicn;

import kr.co.eicn.ippbx.model.enums.DialStatus;
import kr.co.eicn.ippbx.model.enums.LogoutStatus;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class LoginHistoryResponse {
    private Integer seq;                    //seq값
    private String userName;                //로그인명
    private String userId;                  //아이디
    private Timestamp loginDate;               //로그인시간
    private Timestamp logoutDate;              //로그아웃시간
    private String extension;               //로그인내선
    private List<String> companyTrees ;     //내선부서
    private LogoutStatus logoutStatus;            //로그아웃상태
    private DialStatus dialStatus;              //로그인시전화끊은후상태
}
