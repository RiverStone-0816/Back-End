package kr.co.eicn.ippbx.front.model.form;

import kr.co.eicn.ippbx.front.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.model.enums.NoticeType;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.BindingResult;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class NoticeForm extends BaseForm {
    @NotNull("제목")
    private String title;
    @NotNull("내용")
    private String content;
    /**
     * @see kr.co.eicn.ippbx.server.model.enums.NoticeType
     */
    private String noticeType = NoticeType.CANCEL.getCode();

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
