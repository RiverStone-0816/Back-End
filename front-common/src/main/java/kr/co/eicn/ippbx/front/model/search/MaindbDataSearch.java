package kr.co.eicn.ippbx.front.model.search;

import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.model.enums.MultichannelChannelType;
import kr.co.eicn.ippbx.model.search.MaindbDataSearchRequest;
import kr.co.eicn.ippbx.util.page.PageForm;
import kr.co.eicn.ippbx.util.page.PageQueryable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.sql.Date;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
public class MaindbDataSearch extends PageForm {
    public final String SEARCH_TYPE_CREATE_DATE = "CREATE_DATE";

    @PageQueryable
    private Integer groupSeq;
    @PageQueryable
    private String searchType;

    @PageQueryable
    private MultichannelChannelType channelType;
    @PageQueryable
    private String channelData;

    @PageQueryable
    private String keyword;
    @PageQueryable
    private String code;
    @PageQueryable
    private Date startDate;
    @PageQueryable
    private Date endDate;

    public MaindbDataSearchRequest convertToRequest(String dbTypeFieldPrefix) {
        final MaindbDataSearchRequest search = new MaindbDataSearchRequest();
        ReflectionUtils.copy(search, this);

        if (Objects.equals(SEARCH_TYPE_CREATE_DATE, searchType)) {
            search.setCreatedStartDate(startDate);
            search.setCreatedEndDate(endDate);
        } else if (StringUtils.isNotEmpty(searchType)) {
            search.getDbTypeFields().put(dbTypeFieldPrefix + searchType, new MaindbDataSearchRequest.FieldCondition(keyword, code, startDate, endDate));
        }

        return search;
    }
}
