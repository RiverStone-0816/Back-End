package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CommonTypeDetailResponse extends CommonTypeResponse {
    /**
     * @see kr.co.eicn.ippbx.server.model.enums.CommonTypeKind
     **/
    private String kind;
    private String type;

    private List<CommonFieldResponse> commonFields;
}
