package kr.co.eicn.ippbx.front.model.form;

import kr.co.eicn.ippbx.front.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;

@EqualsAndHashCode(callSuper = true)
@Data
public class FileForm extends BaseForm {
    @NotNull("파일이름")
    private String fileName;
    @NotNull("원래이름")
    private String originalName;
    @NotNull("파일경로")
    private String filePath;

    public void validate(String prefix, BindingResult bindingResult) {
        if (StringUtils.isEmpty(fileName))
            reject(bindingResult, prefix + "fileName", "validator.blank", "파일이름");
        if (StringUtils.isEmpty(originalName))
            reject(bindingResult, prefix + "originalName", "validator.blank", "원래이름");
        if (StringUtils.isEmpty(filePath))
            reject(bindingResult, prefix + "filePath", "validator.blank", "파일경로");
    }
}
