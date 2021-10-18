package kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class CommonKakaoSkillMsg implements Serializable {
    private Integer   seq;
    private Timestamp insertDate;
    private String    botId;
    private String    botName;
    private String    intentId;
    private String    intentName;
    private String    intentExtraReasonCode;
    private String    intentExtraReasonMessage;
    private String    actionId;
    private String    actionName;
    private String    requestBlockId;
    private String    requestBlockName;
    private String    requestUserId;
    private String    requestUserType;
    private String    requestUserIsfriend;
    private String    requestUserPlusfriendUserkey;
    private String    requestUtterance;
    private String    requestEventName;
    private String    requestEventData;
    private String    requestParamsParamEventId;
    private String    requestParamsSurface;
    private String    requestLang;
    private String    appUserStatus;
    private String    appUserId;
    private String    responseType;
    private String    responseData;
}
