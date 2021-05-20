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
public class EmailConsultationHistoryFormRequest extends BaseForm {
    @NotNull("아이디")
    private List<Integer> ids = new ArrayList<>();
    @NotNull("재분배할 사용자아이디")
    private List<String> userIds = new ArrayList<>();
}
