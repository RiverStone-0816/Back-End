package kr.co.eicn.ippbx.model.search;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.co.eicn.ippbx.model.Constants;
import kr.co.eicn.ippbx.model.enums.SearchCycle;
import kr.co.eicn.ippbx.util.page.PageQueryable;
import kr.co.eicn.ippbx.util.page.PageQueryableForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class AbstractStatSearchRequest extends PageQueryableForm {
    @PageQueryable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constants.DEFAULT_TIMEZONE)
    private Date startDate = new Date(System.currentTimeMillis() - 6 * 24 * 60 * 60 * 1000);
    @PageQueryable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constants.DEFAULT_TIMEZONE)
    private Date endDate = new Date(System.currentTimeMillis());
    @PageQueryable
    private SearchCycle timeUnit = SearchCycle.DATE;

    @PageQueryable
    private Boolean inner = false; // 내선
    @PageQueryable
    private Boolean workHour = false; // 업무시간
    @PageQueryable
    private Boolean person = false; // 직통
}
