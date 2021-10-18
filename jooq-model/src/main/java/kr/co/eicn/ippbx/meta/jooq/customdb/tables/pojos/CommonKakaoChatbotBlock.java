package kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos;

import lombok.Data;

@Data
public class CommonKakaoChatbotBlock {
    private Integer seq;
    private String  botId;
    private String  botName;
    private String  blockId;
    private String  blockName;
    private String  responseType;
    private String  responseGetUrl;
    private String  responseParamNames;
    private String  eventName;
    private String  useYn;
}
