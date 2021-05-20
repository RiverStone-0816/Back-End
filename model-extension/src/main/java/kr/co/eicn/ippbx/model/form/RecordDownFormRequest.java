package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class RecordDownFormRequest extends BaseForm {
    @NotNull("다운명")
    private String downName; // 다운명
    @NotNull("시퀀스")
    private List<Integer> sequences;
}
