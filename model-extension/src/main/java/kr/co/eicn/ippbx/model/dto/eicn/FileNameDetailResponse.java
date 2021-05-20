package kr.co.eicn.ippbx.model.dto.eicn;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FileNameDetailResponse extends BaseForm {
    private Long id;    //아이디
    private String path;    //파일 경로
    private String originalName;    //파일명
}
