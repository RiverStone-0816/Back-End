package kr.co.eicn.ippbx.server.model.search;

import kr.co.eicn.ippbx.server.util.page.PageForm;
import kr.co.eicn.ippbx.server.util.page.PageQueryable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ChattingSearchRequest extends PageForm {
    @PageQueryable
    private String startMessageId;
    @PageQueryable
    private String endMessageId;
    @PageQueryable
    private String message;
    @PageQueryable
    private Integer limit = 100;
}

