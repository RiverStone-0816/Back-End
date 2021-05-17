package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChattingMemberFormRequest extends BaseForm {
    private List<String> memberList = new ArrayList<>();
}
