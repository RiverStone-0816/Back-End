package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.model.enums.NoticeType;
import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class NoticeFormRequest extends BaseForm {
    @NotNull("제목")
    private String title;
    @NotNull("내용")
    private String content;
    /**
     * @see kr.co.eicn.ippbx.server.model.enums.NoticeType
     */
    private String noticeType = NoticeType.CANCEL.getCode();;
    private List<MultipartFile> files;
}
