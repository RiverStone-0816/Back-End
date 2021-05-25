package kr.co.eicn.ippbx.front.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.BindingResult;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class TaskScriptForm extends BaseForm {
    @NotNull("분류")
    private Long categoryId;
    @NotNull("제목")
    private String title;
    @NotNull("태그")
    private String tag;
    @NotNull("내용")
    private String content;
    /**
     * @see kr.co.eicn.ippbx.model.enums.NoticeType
     */
    private String noticeType;

    private List<FileForm> addingFiles;
    private List<Long> deletingFiles;

    @Override
    public boolean validate(BindingResult bindingResult) {
        if (addingFiles != null)
            for (int i = 0; i < addingFiles.size(); i++)
                if (addingFiles.get(i) != null)
                    addingFiles.get(i).validate("files[" + i + "].", bindingResult);

        return super.validate(bindingResult);
    }
}
