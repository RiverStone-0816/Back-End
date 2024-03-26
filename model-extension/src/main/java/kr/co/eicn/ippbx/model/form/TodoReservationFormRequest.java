package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.meta.jooq.eicn.enums.TodoListTodoKind;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
@Data
public class TodoReservationFormRequest extends BaseForm {
    private TodoListTodoKind kind = TodoListTodoKind.RESERVE;
    @NotNull("전화번호")
    private String customNumber;
    @NotNull("예약시간")
    private Timestamp reservationTime;
}
