package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

import java.util.List;

@Data
public class CallbackDistListResponse {
    private String svcName;
    private String svcNumber;
    private String svcCid;

    private List<CallbackHuntListResponse> hunts;
}
