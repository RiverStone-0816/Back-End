package kr.co.eicn.ippbx.server.model.dto.eicn;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import lombok.Data;

@Data
public class FileNameDetailResponse extends BaseForm {
    private Long id;    //아이디
    private String path;    //파일 경로
    private String originalName;    //파일명
}
