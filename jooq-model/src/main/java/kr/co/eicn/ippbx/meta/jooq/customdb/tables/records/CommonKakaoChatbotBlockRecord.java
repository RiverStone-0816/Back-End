package kr.co.eicn.ippbx.meta.jooq.customdb.tables.records;

import org.jooq.Table;
import org.jooq.impl.UpdatableRecordImpl;

public class CommonKakaoChatbotBlockRecord extends UpdatableRecordImpl<CommonKakaoChatbotBlockRecord> {

    public CommonKakaoChatbotBlockRecord(Table<CommonKakaoChatbotBlockRecord> table) {
        super(table);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_chatbot_block.seq</code>.
     */
    public void setSeq(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_chatbot_block.seq</code>.
     */
    public Integer getSeq() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_chatbot_block.bot_id</code>.
     */
    public void setBotId(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_chatbot_block.bot_id</code>.
     */
    public String getBotId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_chatbot_block.bot_name</code>.
     */
    public void setBotName(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_chatbot_block.bot_name</code>.
     */
    public String getBotName() {
        return (String) get(2);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_chatbot_block.block_id</code>.
     */
    public void setBlockId(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_chatbot_block.block_id</code>.
     */
    public String getBlockId() {
        return (String) get(3);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_chatbot_block.block_name</code>.
     */
    public void setBlockName(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_chatbot_block.block_name</code>.
     */
    public String getBlockName() {
        return (String) get(4);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_chatbot_block.response_type</code>.
     */
    public void setResponseType(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_chatbot_block.response_type</code>.
     */
    public String getResponseType() {
        return (String) get(5);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_chatbot_block.response_get_url</code>.
     */
    public void setResponseGetUrl(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_chatbot_block.response_get_url</code>.
     */
    public String getResponseGetUrl() {
        return (String) get(6);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_chatbot_block.response_param_names</code>.
     */
    public void setResponseParamNames(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_chatbot_block.response_param_names</code>.
     */
    public String getResponseParamNames() {
        return (String) get(7);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_chatbot_block.event_name</code>.
     */
    public void setEventName(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_chatbot_block.event_name</code>.
     */
    public String getEventName() {
        return (String) get(8);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_chatbot_block.use_yn</code>.
     */
    public void setUseYn(String value) {
        set(9, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_chatbot_block.use_yn</code>.
     */
    public String getUseYn() {
        return (String) get(9);
    }
}
