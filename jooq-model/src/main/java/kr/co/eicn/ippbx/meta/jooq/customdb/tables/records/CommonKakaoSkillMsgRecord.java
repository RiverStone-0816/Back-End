package kr.co.eicn.ippbx.meta.jooq.customdb.tables.records;

import org.jooq.Table;
import org.jooq.impl.TableRecordImpl;

import java.sql.Timestamp;

public class CommonKakaoSkillMsgRecord extends TableRecordImpl<CommonKakaoSkillMsgRecord> {

    public CommonKakaoSkillMsgRecord(Table<CommonKakaoSkillMsgRecord> table) {
        super(table);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_skill_msg.seq</code>.
     */
    public void setSeq(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_skill_msg.seq</code>.
     */
    public Integer getSeq() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_skill_msg.insert_date</code>.
     */
    public void setInsertDate(Timestamp value) {
        set(1, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_skill_msg.insert_date</code>.
     */
    public Timestamp getInsertDate() {
        return (Timestamp) get(1);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_skill_msg.bot_id</code>.
     */
    public void setBotId(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_skill_msg.bot_id</code>.
     */
    public String getBotId() {
        return (String) get(2);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_skill_msg.bot_name</code>.
     */
    public void setBotName(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_skill_msg.bot_name</code>.
     */
    public String getBotName() {
        return (String) get(3);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_skill_msg.intent_id</code>.
     */
    public void setIntentId(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_skill_msg.intent_id</code>.
     */
    public String getIntentId() {
        return (String) get(4);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_skill_msg.intent_name</code>.
     */
    public void setIntentName(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_skill_msg.intent_name</code>.
     */
    public String getIntentName() {
        return (String) get(5);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_skill_msg.intent_extra_reason_code</code>.
     */
    public void setIntentExtraReasonCode(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_skill_msg.intent_extra_reason_code</code>.
     */
    public String getIntentExtraReasonCode() {
        return (String) get(6);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_skill_msg.intent_extra_reason_message</code>.
     */
    public void setIntentExtraReasonMessage(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_skill_msg.intent_extra_reason_message</code>.
     */
    public String getIntentExtraReasonMessage() {
        return (String) get(7);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_skill_msg.action_id</code>.
     */
    public void setActionId(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_skill_msg.action_id</code>.
     */
    public String getActionId() {
        return (String) get(8);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_skill_msg.action_name</code>.
     */
    public void setActionName(String value) {
        set(9, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_skill_msg.action_name</code>.
     */
    public String getActionName() {
        return (String) get(9);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_skill_msg.request_block_id</code>.
     */
    public void setRequestBlockId(String value) {
        set(10, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_skill_msg.request_block_id</code>.
     */
    public String getRequestBlockId() {
        return (String) get(10);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_skill_msg.request_block_name</code>.
     */
    public void setRequestBlockName(String value) {
        set(11, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_skill_msg.request_block_name</code>.
     */
    public String getRequestBlockName() {
        return (String) get(11);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_skill_msg.request_user_id</code>.
     */
    public void setRequestUserId(String value) {
        set(12, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_skill_msg.request_user_id</code>.
     */
    public String getRequestUserId() {
        return (String) get(12);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_skill_msg.request_user_type</code>.
     */
    public void setRequestUserType(String value) {
        set(13, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_skill_msg.request_user_type</code>.
     */
    public String getRequestUserType() {
        return (String) get(13);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_skill_msg.request_user_isfriend</code>.
     */
    public void setRequestUserIsfriend(String value) {
        set(14, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_skill_msg.request_user_isfriend</code>.
     */
    public String getRequestUserIsfriend() {
        return (String) get(14);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_skill_msg.request_user_plusfriend_userkey</code>.
     */
    public void setRequestUserPlusfriendUserkey(String value) {
        set(15, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_skill_msg.request_user_plusfriend_userkey</code>.
     */
    public String getRequestUserPlusfriendUserkey() {
        return (String) get(15);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_skill_msg.request_utterance</code>.
     */
    public void setRequestUtterance(String value) {
        set(16, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_skill_msg.request_utterance</code>.
     */
    public String getRequestUtterance() {
        return (String) get(16);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_skill_msg.request_event_name</code>.
     */
    public void setRequestEventName(String value) {
        set(17, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_skill_msg.request_event_name</code>.
     */
    public String getRequestEventName() {
        return (String) get(17);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_skill_msg.request_event_data</code>.
     */
    public void setRequestEventData(String value) {
        set(18, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_skill_msg.request_event_data</code>.
     */
    public String getRequestEventData() {
        return (String) get(18);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_skill_msg.request_params_param_event_id</code>.
     */
    public void setRequestParamsParamEventId(String value) {
        set(19, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_skill_msg.request_params_param_event_id</code>.
     */
    public String getRequestParamsParamEventId() {
        return (String) get(19);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_skill_msg.request_params_surface</code>.
     */
    public void setRequestParamsSurface(String value) {
        set(20, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_skill_msg.request_params_surface</code>.
     */
    public String getRequestParamsSurface() {
        return (String) get(20);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_skill_msg.request_lang</code>.
     */
    public void setRequestLang(String value) {
        set(21, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_skill_msg.request_lang</code>.
     */
    public String getRequestLang() {
        return (String) get(21);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_skill_msg.app_user_status</code>.
     */
    public void setAppUserStatus(String value) {
        set(22, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_skill_msg.app_user_status</code>.
     */
    public String getAppUserStatus() {
        return (String) get(22);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_skill_msg.app_user_id</code>.
     */
    public void setAppUserId(String value) {
        set(23, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_skill_msg.app_user_id</code>.
     */
    public String getAppUserId() {
        return (String) get(23);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_skill_msg.response_type</code>.
     */
    public void setResponseType(String value) {
        set(24, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_skill_msg.response_type</code>.
     */
    public String getResponseType() {
        return (String) get(24);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_skill_msg.response_data</code>.
     */
    public void setResponseData(String value) {
        set(25, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_skill_msg.response_data</code>.
     */
    public String getResponseData() {
        return (String) get(25);
    }
}
