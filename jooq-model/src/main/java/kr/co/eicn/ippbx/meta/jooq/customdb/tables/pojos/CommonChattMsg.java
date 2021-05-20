package kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class CommonChattMsg implements Serializable {
    private Integer seq;
    private String roomId;
    private String userid;
    private Timestamp insertTime;
    private String sendReceive;
    private String messageId;
    private String type;
    private String content;
}
