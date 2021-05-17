package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MaindbUploadUpdateRequest extends BaseForm {
    @NotNull("업로드파일")
    private MultipartFile file;
}
