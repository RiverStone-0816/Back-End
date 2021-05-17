package kr.co.eicn.ippbx.server.model.search;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.co.eicn.ippbx.server.config.Constants;
import kr.co.eicn.ippbx.server.model.enums.MultichannelChannelType;
import kr.co.eicn.ippbx.server.util.page.PageForm;
import kr.co.eicn.ippbx.server.util.page.PageQueryable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class MaindbDataSearchRequest extends PageForm {
    @PageQueryable
    private Integer groupSeq;
    @PageQueryable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constants.DEFAULT_TIMEZONE)
    private Date createdStartDate;
    @PageQueryable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constants.DEFAULT_TIMEZONE)
    private Date createdEndDate;

    @PageQueryable
    private MultichannelChannelType channelType;
    @PageQueryable
    private String channelData;

    /**
     * dbTypeFields[MAINDB_DATE_1].startDate = "2020-01-01"
     * dbTypeFields[MAINDB_DATE_1].endDate = "2020-01-01"
     * dbTypeFields[MAINDB_STRING_1].keyword = "blublu"
     */
    private Map<String, FieldCondition> dbTypeFields = new HashMap<>(); // 타입에 따른 필드 검색 조건

    // TODO: extract
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FieldCondition {
        private String keyword;
        private String code;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constants.DEFAULT_TIMEZONE)
        private Date startDate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constants.DEFAULT_TIMEZONE)
        private Date endDate;
    }
}
