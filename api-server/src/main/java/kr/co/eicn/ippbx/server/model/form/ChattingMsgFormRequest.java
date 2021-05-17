package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import lombok.Data;

@Data
public class ChattingMsgFormRequest extends BaseForm {
    /**
     * @see kr.co.eicn.ippbx.server.model.enums.ChattingMsgType
     */
    private String type;
    private String content;
}
