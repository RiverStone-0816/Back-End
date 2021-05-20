package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

import java.util.List;

@Data
public class CallbackHuntListResponse {
    private String hanName;
    private String queueNumber;

    private List<CallbackPersonResponse> idNames;
}
