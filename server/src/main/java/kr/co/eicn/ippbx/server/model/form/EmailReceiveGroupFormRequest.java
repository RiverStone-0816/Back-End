package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class EmailReceiveGroupFormRequest extends BaseForm {
    @NotNull("이메일그룹명")
    private String groupName;
    @NotNull("관련이메일서비스")
    private Integer emailId;
    @NotNull("그룹멤버")
    private Set<String> emailMemberLists;
}