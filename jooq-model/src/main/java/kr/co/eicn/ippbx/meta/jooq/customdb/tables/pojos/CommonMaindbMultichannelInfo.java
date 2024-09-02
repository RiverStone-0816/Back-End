package kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommonMaindbMultichannelInfo implements Serializable {
    private Integer seq;
    private String  channelType;
    private String  channelData;
    private Integer maindbGroupId;
    private String  maindbCustomId;
    private String  maindbCustomName;
    private String  companyId;
}
