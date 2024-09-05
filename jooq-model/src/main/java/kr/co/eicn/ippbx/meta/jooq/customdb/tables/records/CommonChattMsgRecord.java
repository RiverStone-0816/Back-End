package kr.co.eicn.ippbx.meta.jooq.customdb.tables.records;

import org.jooq.Table;
import org.jooq.impl.UpdatableRecordImpl;

import java.sql.Timestamp;

public class CommonChattMsgRecord extends UpdatableRecordImpl<CommonChattMsgRecord> {

    public CommonChattMsgRecord(Table<CommonChattMsgRecord> table) {
        super(table);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_msg.seq</code>.
     */
    public void setSeq(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_msg.seq</code>.
     */
    public Integer getSeq() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_msg.room_id</code>.
     */
    public void setRoomId(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_msg.room_id</code>.
     */
    public String getRoomId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_msg.userid</code>.
     */
    public void setUserid(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_msg.userid</code>.
     */
    public String getUserid() {
        return (String) get(2);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_msg.insert_time</code>.
     */
    public void setInsertTime(Timestamp value) {
        set(3, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_msg.insert_time</code>.
     */
    public Timestamp getInsertTime() {
        return (Timestamp) get(3);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_msg.send_receive</code>.
     */
    public void setSendReceive(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_msg.send_receive</code>.
     */
    public String getSendReceive() {
        return (String) get(4);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_msg.message_id</code>.
     */
    public void setMessageId(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_msg.message_id</code>.
     */
    public String getMessageId() {
        return (String) get(5);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_msg.type</code>.
     */
    public void setType(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_msg.type</code>.
     */
    public String getType() {
        return (String) get(6);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_msg.content</code>.
     */
    public void setContent(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_msg.content</code>.
     */
    public String getContent() {
        return (String) get(7);
    }
}
