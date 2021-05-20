package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class PrvCustomInfoRedistributionFormRequest extends BaseForm {
    @NotNull("customIdList")
    private List<String> customIdList = new ArrayList<>();
    @NotNull("userIdList")
    private List<String> userIdList = new ArrayList<>();
}
