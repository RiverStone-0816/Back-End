package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SendMessageFormRequest extends BaseForm {
    private String customId;
    private String customNumber;
    private String serviceNumber;
    @NotNull("수신번호")
    private List<String> targetNumbers;
    @NotNull("메세지")
    private String content;
    private Integer template;
}