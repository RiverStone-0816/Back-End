package kr.co.eicn.ippbx.server.model.search;

import kr.co.eicn.ippbx.server.util.page.PageForm;
import kr.co.eicn.ippbx.server.util.page.PageQueryable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
@Data
public class WebSecureHistorySearchRequest extends PageForm {
    @PageQueryable
    private Timestamp startTimestamp;   //시작시간
    @PageQueryable
    private Timestamp endTimestamp;     //종료시간
    @PageQueryable
    private String userName;       //상담자
    @PageQueryable
    private String actionId;       //실행명
}
