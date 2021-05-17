package kr.co.eicn.ippbx.front.model.search;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.co.eicn.ippbx.front.util.ReflectionUtils;
import kr.co.eicn.ippbx.server.config.Constants;
import kr.co.eicn.ippbx.server.model.search.MaindbDataSearchRequest;
import kr.co.eicn.ippbx.server.model.search.ResultCustomInfoSearchRequest;
import kr.co.eicn.ippbx.server.util.page.PageForm;
import kr.co.eicn.ippbx.server.util.page.PageQueryable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.sql.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class MaindbResultSearch extends PageForm {
    @PageQueryable
    private Integer groupSeq;
    @PageQueryable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constants.DEFAULT_TIMEZONE)
    private Date createdStartDate;
    @PageQueryable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constants.DEFAULT_TIMEZONE)
    private Date createdEndDate;
    @PageQueryable
    private String userId;

    @PageQueryable
    private String channelType; //멀티채널유형
    @PageQueryable
    private String channelData; //멀티채널 값.

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

    public ResultCustomInfoSearchRequest convertToRequest(String dbTypeFieldPrefix) {
        final ResultCustomInfoSearchRequest search = new ResultCustomInfoSearchRequest();
        ReflectionUtils.copy(search, this);

        if (StringUtils.isNotEmpty(searchType)) {
            search.getDbTypeFields().put(dbTypeFieldPrefix + searchType, new MaindbDataSearchRequest.FieldCondition(keyword, code, startDate, endDate));
        }

        return search;
    }
}
