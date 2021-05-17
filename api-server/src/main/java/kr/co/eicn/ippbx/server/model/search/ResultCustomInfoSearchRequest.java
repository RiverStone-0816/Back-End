package kr.co.eicn.ippbx.server.model.search;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.co.eicn.ippbx.server.config.Constants;
import kr.co.eicn.ippbx.server.util.page.PageForm;
import kr.co.eicn.ippbx.server.util.page.PageQueryable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResultCustomInfoSearchRequest extends PageForm {
    @PageQueryable
    private Integer seq;
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
    private String clickKey;

    private Map<String, MaindbDataSearchRequest.FieldCondition> dbTypeFields = new HashMap<>(); // 타입에 따른 필드 검색 조건
}
