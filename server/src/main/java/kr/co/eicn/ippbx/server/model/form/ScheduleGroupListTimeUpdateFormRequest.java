package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ScheduleGroupListTimeUpdateFormRequest extends BaseForm {
    @NotNull("스케쥴 유형")
    private Integer parent;
    @NotNull("시작 시간")
    private Integer fromhour;
    @NotNull("종료 시간")
    private Integer tohour;
}
