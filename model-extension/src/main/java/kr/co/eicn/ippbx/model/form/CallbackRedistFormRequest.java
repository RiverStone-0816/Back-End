package kr.co.eicn.ippbx.model.form;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CallbackRedistFormRequest extends BaseForm {
    @NotNull("재분배상담원")
    private List<String> users = new ArrayList<>();
    @NotNull("재분배서비스")
    private List<Integer> serviceSequences = new ArrayList<>();
}
