package kr.co.eicn.ippbx.model.dto.eicn;

import kr.co.eicn.ippbx.model.enums.DialStatus;
import kr.co.eicn.ippbx.model.enums.FirstStatus;
import kr.co.eicn.ippbx.model.enums.LogoutStatus;
import lombok.Data;

@Data
public class PhoneInfoResponse {
    private String peer;
    private String extension;
    private String localPrefix;
    private String cid;
    private String billingNumber;
    private DialStatus dialStatus;
    private LogoutStatus logoutStatus;
    private FirstStatus firstStatus;
}
