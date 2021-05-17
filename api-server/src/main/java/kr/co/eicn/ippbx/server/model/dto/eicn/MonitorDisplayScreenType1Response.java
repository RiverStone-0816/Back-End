package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

import java.util.List;

@Data
public class MonitorDisplayScreenType1Response {
    private String screenName;
    private List<DisplayHuntMemberStatusResponse> huntMemberStatusResponse;
    private List<DisplayHuntSvcStatResponse> huntSvcStatResponses;
}
