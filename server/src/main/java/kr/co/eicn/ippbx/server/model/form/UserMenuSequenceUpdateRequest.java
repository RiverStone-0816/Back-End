package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserMenuSequenceUpdateRequest extends BaseForm {
    @NotNull("그룹 메뉴 순서")
    private List<String> menuCodes = new ArrayList<>();
}
