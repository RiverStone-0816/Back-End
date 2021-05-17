package kr.co.eicn.ippbx.server.model.search;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.co.eicn.ippbx.server.config.Constants;
import kr.co.eicn.ippbx.server.util.page.PageForm;
import kr.co.eicn.ippbx.server.util.page.PageQueryable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class MemoMsgSearchRequest extends PageForm {
    @PageQueryable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constants.DEFAULT_TIMEZONE)
    private Date startDate;
    @PageQueryable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constants.DEFAULT_TIMEZONE)
    private Date endDate;
    @PageQueryable
    private String receiveUser;   //받은 사람
    @PageQueryable
    private String sendUser;     //보낸 사람
    @PageQueryable
    private String content;      //내용
    @PageQueryable
    private Boolean displayUnreadMemo = false;  //안읽은 쪽지만 표시
}
