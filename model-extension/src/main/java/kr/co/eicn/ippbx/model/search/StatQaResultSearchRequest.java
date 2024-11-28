package kr.co.eicn.ippbx.model.search;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.co.eicn.ippbx.model.Constants;
import kr.co.eicn.ippbx.util.page.PageQueryable;
import kr.co.eicn.ippbx.util.page.PageQueryableForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class StatQaResultSearchRequest extends PageQueryableForm {
    @PageQueryable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constants.DEFAULT_TIMEZONE)
    private Date startDate = new Date(System.currentTimeMillis());
    @PageQueryable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constants.DEFAULT_TIMEZONE)
    private Date endDate = new Date(System.currentTimeMillis());
    @PageQueryable
    private SendReceiveType type;
    @PageQueryable
    private Integer groupSeq;
    @PageQueryable
    private String fieldId;
    @PageQueryable
    private Integer resultType;

    public enum SendReceiveType {
        SEND, RECEIVE;
    }
}
