package kr.co.eicn.ippbx.model.dto.eicn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MultichannelLoginResponse {
    @JsonProperty("COMPANY_ID")
    private String companyId;
    @JsonProperty("ADMIN_ID")
    private String id;
    @JsonProperty("JSESSIONID")
    private String jSessionId;
}
