package kr.co.eicn.ippbx.front.model.search;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.co.eicn.ippbx.model.Constants;
import kr.co.eicn.ippbx.model.search.MaindbDataSearchRequest;
import kr.co.eicn.ippbx.model.search.PrvCustomInfoSearchRequest;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.util.page.PageForm;
import kr.co.eicn.ippbx.util.page.PageQueryable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.sql.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class PreviewDataSearch extends PageForm {
    @PageQueryable
    private Integer groupSeq;
    @PageQueryable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constants.DEFAULT_TIMEZONE)
    private Date createdStartDate;
    @PageQueryable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constants.DEFAULT_TIMEZONE)
    private Date createdEndDate;
    @PageQueryable
    private String personIdInCharge;
    @PageQueryable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constants.DEFAULT_TIMEZONE)
    private Date lastResultStartDate;
    @PageQueryable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constants.DEFAULT_TIMEZONE)
    private Date lastResultEndDate;

    @PageQueryable
    private String searchType;

    @PageQueryable
    private String keyword;
    @PageQueryable
    private String code;
    @PageQueryable
    private Date startDate;
    @PageQueryable
    private Date endDate;

    public PrvCustomInfoSearchRequest convertToRequest(String dbTypeFieldPrefix) {
        final PrvCustomInfoSearchRequest search = new PrvCustomInfoSearchRequest();
        ReflectionUtils.copy(search, this);

        if (StringUtils.isNotEmpty(searchType)) {
            search.getDbTypeFields().put(dbTypeFieldPrefix + searchType, new MaindbDataSearchRequest.FieldCondition(keyword, code, startDate, endDate));
        }

        return search;
    }
}
