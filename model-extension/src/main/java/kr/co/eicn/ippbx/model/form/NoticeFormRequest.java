package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.model.enums.NoticeType;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class NoticeFormRequest extends BaseForm {
    @NotNull("제목")
    private String title;
    @NotNull("내용")
    private String content;
    /**
     * @see kr.co.eicn.ippbx.model.enums.NoticeType
     */
    private String noticeType = NoticeType.CANCEL.getCode();
    private List<MultipartFile> files;
}
