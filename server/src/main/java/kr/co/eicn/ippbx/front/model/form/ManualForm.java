package kr.co.eicn.ippbx.front.model.form;

import kr.co.eicn.ippbx.front.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.BindingResult;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ManualForm extends BaseForm {
    @NotNull("제목")
    private String title;

    private List<FileForm> addingFiles;
    private List<Long> deletingFiles;

    @Override
    public boolean validate(BindingResult bindingResult) {
        if (addingFiles != null)
            for (int i = 0; i < addingFiles.size(); i++)
                addingFiles.get(0).validate("files[" + i + "].", bindingResult);

        return super.validate(bindingResult);
    }
}
