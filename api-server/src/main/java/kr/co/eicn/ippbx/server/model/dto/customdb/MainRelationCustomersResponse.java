package kr.co.eicn.ippbx.server.model.dto.customdb;

import kr.co.eicn.ippbx.server.model.dto.eicn.GroupUploadLogResponse;
import lombok.Data;

@Data
public class MainRelationCustomersResponse {
    private CommonMultiChannelInfoResponse multiChannelInfo;
    private GroupUploadLogResponse affiliatedGroup;
}
