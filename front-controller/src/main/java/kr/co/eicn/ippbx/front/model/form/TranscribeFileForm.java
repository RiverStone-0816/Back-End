package kr.co.eicn.ippbx.front.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.BindingResult;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class TranscribeFileForm extends BaseForm {
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
