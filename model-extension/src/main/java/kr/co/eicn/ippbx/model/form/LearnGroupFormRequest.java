package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class LearnGroupFormRequest extends BaseForm {
    @NotNull("그룹명")
    private String groupName;
    private List<String> groupList;

    private String learnStatus;
}
