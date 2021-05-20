package kr.co.eicn.ippbx.model.dto.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.enums.TodoListTodoKind;
import lombok.Data;

@Data
public class TodoDataResponse {
    private TodoListTodoKind todoKind;
    private Integer total;   //접수
    private Integer success; //처리
    private float successRate; //처리율
}
