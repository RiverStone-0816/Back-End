package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TranscribeGroupFormRequest extends BaseForm {
    @NotNull("그룹명")
    private String groupName;
    @NotNull("상담원아이디")
    private String userId;

    private String status;
    private Integer fileCnt;
    private Double recRate;
}
