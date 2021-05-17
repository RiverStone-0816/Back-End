package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import lombok.Data;

import java.util.List;

@Data
public class MemoMsgFormRequest extends BaseForm {
    private List<String> receiveUserIds;
    private String content;
    private Boolean isWriteToMe = false; //내게 쓰기
}
