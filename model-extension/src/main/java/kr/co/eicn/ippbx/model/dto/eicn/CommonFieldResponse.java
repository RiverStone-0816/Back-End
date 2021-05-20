package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CommonFieldResponse extends SummaryCommonFieldResponse{
    private String relatedFieldId;
    private String relatedFieldInfo;
    private Integer displaySeq;

    private List<CommonCodeDetailResponse> commonCodes;
}
