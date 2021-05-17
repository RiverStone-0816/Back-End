package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.jooq.eicn.enums.TodoListTodoStatus;
import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class TodoListUpdateFormRequest extends BaseForm {
    @NotNull("시퀀스")
    private Integer seq;
    @NotNull("처리상태")
    private TodoListTodoStatus todoStatus;
}
