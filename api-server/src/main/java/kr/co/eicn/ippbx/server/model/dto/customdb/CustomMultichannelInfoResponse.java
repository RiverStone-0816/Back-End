package kr.co.eicn.ippbx.server.model.dto.customdb;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomMultichannelInfoResponse extends CommonMultiChannelInfoResponse{
    private String groupName;
}
