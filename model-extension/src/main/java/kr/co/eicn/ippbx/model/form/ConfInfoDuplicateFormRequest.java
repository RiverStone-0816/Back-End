package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ConfInfoDuplicateFormRequest extends BaseForm {
    @NotNull("회의시작시간")
    private Integer reserveFromTime;
    @NotNull("회의종료시간")
    private Integer reserveToTime;
    @NotNull("회의실번호")
    private String roomNumber;
    @NotNull("회의날짜")
    private String reserveDate;
}
