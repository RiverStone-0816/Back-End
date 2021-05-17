package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;

@Data
public class MaindbGroupUpdateRequest extends BaseForm {
    @NotNull("그룹명")
    private String name;
    @NotNull("고객정보유형")
    private Integer maindbType;
    @NotNull("상담결과유형")
    private Integer resultType;
    private String info;
    private String groupCode;
}
