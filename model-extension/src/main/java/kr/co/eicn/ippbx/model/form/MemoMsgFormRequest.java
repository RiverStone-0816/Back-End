package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class MemoMsgFormRequest extends BaseForm {
    private List<String> receiveUserIds;
    private String content;
    private Boolean isWriteToMe = false; //내게 쓰기
}
