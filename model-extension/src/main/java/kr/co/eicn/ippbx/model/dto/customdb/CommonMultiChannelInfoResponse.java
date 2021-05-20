package kr.co.eicn.ippbx.model.dto.customdb;

import lombok.Data;

@Data
public class CommonMultiChannelInfoResponse {
    private Integer seq;
    private String  channel_type;
    private String  channel_data;
    private Integer maindb_group_id;
    private String  maindb_custom_id;
    private String  maindb_custom_name;
    private String  company_id;
}
