package kr.co.eicn.ippbx.model.dto.customdb;

import kr.co.eicn.ippbx.model.dto.eicn.GroupUploadLogResponse;
import lombok.Data;

@Data
public class MainRelationCustomersResponse {
    private CommonMultiChannelInfoResponse multiChannelInfo;
    private GroupUploadLogResponse affiliatedGroup;
}
