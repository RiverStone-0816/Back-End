package kr.co.eicn.ippbx.model.search;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.co.eicn.ippbx.model.Constants;
import kr.co.eicn.ippbx.meta.jooq.eicn.enums.RouteApplicationResult;
import kr.co.eicn.ippbx.meta.jooq.eicn.enums.RouteApplicationType;
import kr.co.eicn.ippbx.util.page.PageForm;
import kr.co.eicn.ippbx.util.page.PageQueryable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class RouteApplicationSearchRequest extends PageForm {
    @PageQueryable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constants.DEFAULT_TIMEZONE)
    private Date startDate = new Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000);
    @PageQueryable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constants.DEFAULT_TIMEZONE)
    private Date endDate = new Date(System.currentTimeMillis());
    @PageQueryable
    private RouteApplicationType type;
    @PageQueryable
    private String number;
    @PageQueryable
    private String appUserId;
    @PageQueryable
    private String appUserName;
    @PageQueryable
    private String rstUserId;
    @PageQueryable
    private String rstUserName;
    @PageQueryable
    private RouteApplicationResult result;
}
