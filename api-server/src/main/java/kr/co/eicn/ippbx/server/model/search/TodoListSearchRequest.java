package kr.co.eicn.ippbx.server.model.search;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.co.eicn.ippbx.server.config.Constants;
import kr.co.eicn.ippbx.server.jooq.eicn.enums.TodoListTodoStatus;
import kr.co.eicn.ippbx.server.util.page.PageQueryable;
import kr.co.eicn.ippbx.server.util.page.PageQueryableForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class TodoListSearchRequest extends PageQueryableForm {
    @PageQueryable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constants.DEFAULT_TIMEZONE)
    private Date startDate;
    @PageQueryable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constants.DEFAULT_TIMEZONE)
    private Date endDate;
    @PageQueryable
    private List<TodoListTodoStatus> statuses = new ArrayList<>();
    @PageQueryable
    private List<String> phoneNumbers = new ArrayList<>();
    @PageQueryable
    private String userId;
}
