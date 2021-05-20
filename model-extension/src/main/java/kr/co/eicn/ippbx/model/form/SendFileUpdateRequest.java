package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

@EqualsAndHashCode(callSuper = true)
@Data
public class SendFileUpdateRequest extends BaseForm {
    @NotNull("발송물 설명")
    private String desc;
    @NotNull("발송물명")
    private String name;
    @NotNull("발송물")
    private MultipartFile file;
}
