package kr.co.eicn.ippbx.model.dto.customdb;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomMultichannelInfoResponse extends CommonMultiChannelInfoResponse{
    private String groupName;
}
