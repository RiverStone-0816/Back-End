package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SummaryCallbackDistPersonResponse extends SummaryPersonResponse {
    private String id;        // 상담원아이디
}
