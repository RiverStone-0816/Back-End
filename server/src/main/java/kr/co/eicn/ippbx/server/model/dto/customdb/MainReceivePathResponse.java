package kr.co.eicn.ippbx.server.model.dto.customdb;

import kr.co.eicn.ippbx.server.model.enums.PathNumberType;
import lombok.Data;

@Data
public class MainReceivePathResponse {
    /**
     * @see kr.co.eicn.ippbx.server.model.enums.PathNumberType
     */
    private PathNumberType pathNumber;
    private String number;
    private String name;
    /**
     * @see kr.co.eicn.ippbx.server.model.enums.NumberType
     */
    private Byte type;
}
