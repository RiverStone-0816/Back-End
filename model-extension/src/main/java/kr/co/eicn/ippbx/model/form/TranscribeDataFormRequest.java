package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TranscribeDataFormRequest extends BaseForm {
    @NotNull("그룹코드")
    private Integer groupCode;
    @NotNull("파일명")
    private String fileName;
    private String info;
    private Double recRate;
    private String status;
    private String sttStatus;
    private String recStatus;
    private String studyStatus;
}
