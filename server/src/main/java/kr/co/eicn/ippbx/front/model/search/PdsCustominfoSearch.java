package kr.co.eicn.ippbx.front.model.search;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.co.eicn.ippbx.front.util.ReflectionUtils;
import kr.co.eicn.ippbx.server.config.Constants;
import kr.co.eicn.ippbx.server.model.search.PDSDataSearchRequest;
import kr.co.eicn.ippbx.server.util.page.PageForm;
import kr.co.eicn.ippbx.server.util.page.PageQueryable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.sql.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class PdsCustominfoSearch extends PageForm {
    @PageQueryable
    private Integer groupSeq;
    @PageQueryable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constants.DEFAULT_TIMEZONE)
    private Date createdStartDate;
    @PageQueryable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constants.DEFAULT_TIMEZONE)
    private Date createdEndDate;
    @PageQueryable
    private String searchType;

    @PageQueryable
    private String keyword;
    @PageQueryable
    private Date startDate;
    @PageQueryable
    private Date endDate;

    public PDSDataSearchRequest convertToRequest(String dbTypeFieldPrefix) {
        final PDSDataSearchRequest search = new PDSDataSearchRequest();
        ReflectionUtils.copy(search, this);

        if (StringUtils.isNotEmpty(searchType)) {
            search.getDbTypeFields().put(dbTypeFieldPrefix + searchType, new PDSDataSearchRequest.FieldCondition(keyword, startDate, endDate));
        }

        return search;
    }
}
