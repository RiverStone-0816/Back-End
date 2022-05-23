package kr.co.eicn.ippbx.meta.jooq.customdb.tables.records;

import org.jooq.Table;
import org.jooq.impl.TableRecordImpl;

import java.sql.Timestamp;

public class CommonWtalkMsgRecord extends TableRecordImpl<CommonWtalkMsgRecord> {

    public CommonWtalkMsgRecord(Table<CommonWtalkMsgRecord> table) {
        super(table);
    }

    /**
     * Setter for <code>CUSTOMDB.talk_msg.seq</code>.
     */
    public void setSeq(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>CUSTOMDB.talk_msg.seq</code>.
     */
    public Integer getSeq() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>CUSTOMDB.talk_msg.channel_type</code>. 상담톡 서비스 유형, eicn/kakao/navertt/naverline
     */
    public void setChannelType(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>CUSTOMDB.talk_msg.channel_type</code>. 상담톡 서비스 유형, eicn/kakao/navertt/naverline
     */
    public String getChannelType() {
        return (String) get(1);
    }

    /**
     * Setter for <code>CUSTOMDB.talk_msg.insert_time</code>.
     */
    public void setInsertTime(Timestamp value) {
        set(2, value);
    }

    /**
     * Getter for <code>CUSTOMDB.talk_msg.insert_time</code>.
     */
    public Timestamp getInsertTime() {
        return (Timestamp) get(2);
    }

    /**
     * Setter for <code>CUSTOMDB.talk_msg.send_receive</code>.
     */
    public void setSendReceive(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>CUSTOMDB.talk_msg.send_receive</code>.
     */
    public String getSendReceive() {
        return (String) get(3);
    }

    /**
     * Setter for <code>CUSTOMDB.talk_msg.company_id</code>.
     */
    public void setCompanyId(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>CUSTOMDB.talk_msg.company_id</code>.
     */
    public String getCompanyId() {
        return (String) get(4);
    }

    /**
     * Setter for <code>CUSTOMDB.talk_msg.userid</code>.
     */
    public void setUserid(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>CUSTOMDB.talk_msg.userid</code>.
     */
    public String getUserid() {
        return (String) get(5);
    }

    /**
     * Setter for <code>CUSTOMDB.talk_msg.user_key</code>.
     */
    public void setUserKey(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>CUSTOMDB.talk_msg.user_key</code>.
     */
    public String getUserKey() {
        return (String) get(6);
    }

    /**
     * Setter for <code>CUSTOMDB.talk_msg.sender_key</code>.
     */
    public void setSenderKey(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>CUSTOMDB.talk_msg.sender_key</code>.
     */
    public String getSenderKey() {
        return (String) get(7);
    }

    /**
     * Setter for <code>CUSTOMDB.talk_msg.message_id</code>.
     */
    public void setMessageId(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>CUSTOMDB.talk_msg.message_id</code>.
     */
    public String getMessageId() {
        return (String) get(8);
    }

    /**
     * Setter for <code>CUSTOMDB.talk_msg.time</code>.
     */
    public void setTime(String value) {
        set(9, value);
    }

    /**
     * Getter for <code>CUSTOMDB.talk_msg.time</code>.
     */
    public String getTime() {
        return (String) get(9);
    }

    /**
     * Setter for <code>CUSTOMDB.talk_msg.type</code>.
     */
    public void setType(String value) {
        set(10, value);
    }

    /**
     * Getter for <code>CUSTOMDB.talk_msg.type</code>.
     */
    public String getType() {
        return (String) get(10);
    }

    /**
     * Setter for <code>CUSTOMDB.talk_msg.content</code>.
     */
    public void setContent(String value) {
        set(11, value);
    }

    /**
     * Getter for <code>CUSTOMDB.talk_msg.content</code>.
     */
    public String getContent() {
        return (String) get(11);
    }

    /**
     * Setter for <code>CUSTOMDB.talk_msg.attachment</code>.
     */
    public void setAttachment(String value) {
        set(12, value);
    }

    /**
     * Getter for <code>CUSTOMDB.talk_msg.attachment</code>.
     */
    public String getAttachment() {
        return (String) get(12);
    }

    /**
     * Setter for <code>CUSTOMDB.talk_msg.extra</code>.
     */
    public void setExtra(String value) {
        set(13, value);
    }

    /**
     * Getter for <code>CUSTOMDB.talk_msg.extra</code>.
     */
    public String getExtra() {
        return (String) get(13);
    }

    /**
     * Setter for <code>CUSTOMDB.talk_msg.room_id</code>.
     */
    public void setRoomId(String value) {
        set(14, value);
    }

    /**
     * Getter for <code>CUSTOMDB.talk_msg.room_id</code>.
     */
    public String getRoomId() {
        return (String) get(14);
    }
}
