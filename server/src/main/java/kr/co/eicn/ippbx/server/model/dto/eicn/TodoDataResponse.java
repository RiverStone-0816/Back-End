package kr.co.eicn.ippbx.server.model.dto.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.enums.TodoListTodoKind;
import lombok.Data;

@Data
public class TodoDataResponse {
    private TodoListTodoKind todoKind;
    private Integer total;   //접수
    private Integer success; //처리
    private float successRate; //처리율
}
