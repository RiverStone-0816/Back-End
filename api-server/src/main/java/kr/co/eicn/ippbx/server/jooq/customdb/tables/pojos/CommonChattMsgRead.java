package kr.co.eicn.ippbx.server.jooq.customdb.tables.pojos;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommonChattMsgRead implements Serializable {
    private String messageId;
    private String roomId;
    private String userid;
}
