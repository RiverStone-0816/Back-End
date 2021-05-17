package kr.co.eicn.ippbx.front.model.form;

import kr.co.eicn.ippbx.front.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.model.enums.Bool;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ArsForm extends BaseForm {
    @NotNull("음원명")
    private String  soundName;
    private String  comment;
    private Bool protectArsYn = Bool.N;

    @NotNull("파일이름")
    private String fileName;
    @NotNull("원래이름")
    private String originalName;
    @NotNull("파일경로")
    private String filePath;
}
