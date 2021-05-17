package kr.co.eicn.ippbx.front.model.search;

import kr.co.eicn.ippbx.server.util.page.PageForm;
import kr.co.eicn.ippbx.server.util.page.PageQueryable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class WebSecureHistorySearch extends PageForm {
    @PageQueryable
    private Date startDate;   //시작날짜
    @PageQueryable
    private Integer startHour;
    @PageQueryable
    private Date endDate;     //종료날짜
    @PageQueryable
    private Integer endHour;
    @PageQueryable
    private String userName;       //상담자
    @PageQueryable
    private String actionId;       //실행명
}
