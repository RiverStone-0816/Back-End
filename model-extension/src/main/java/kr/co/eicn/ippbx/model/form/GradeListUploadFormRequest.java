package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class GradeListUploadFormRequest extends BaseForm {
    @NotNull("엑셀업로드정보.")
    private List<GradeListFormRequest> gradeUpload;
}
