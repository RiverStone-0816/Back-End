package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class MaindbGroupDetailResponse extends MaindbGroupSummaryResponse{
    private List<SummaryCommonFieldResponse> fieldInfoType; //유형업로드 종류
}
