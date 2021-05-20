package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class TaskScriptFormRequest extends BaseForm {
    @NotNull("분류")
    private Long categoryId;
    @NotNull("제목")
    private String title;
    @NotNull("태그")
    private String tag;
    @NotNull("내용")
    private String content;
    private List<MultipartFile> files;
}
