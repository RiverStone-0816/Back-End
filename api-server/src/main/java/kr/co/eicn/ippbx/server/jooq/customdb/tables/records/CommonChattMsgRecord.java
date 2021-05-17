package kr.co.eicn.ippbx.server.jooq.customdb.tables.records;

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
     * Setter for <code>CUSTOMDB.chatt_msg.write_date</code>.
     */
    public void setWriteDate(Timestamp value) {
        set(1, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_msg.write_date</code>.
     */
    public Timestamp getWriteDate() {
        return (Timestamp) get(1);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_msg.roomid</code>.
     */
    public void setRoomid(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_msg.roomid</code>.
     */
    public String getRoomid() {
        return (String) get(2);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_msg.parent_roomid</code>.
     */
    public void setParentRoomid(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_msg.parent_roomid</code>.
     */
    public String getParentRoomid() {
        return (String) get(3);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_msg.userid</code>.
     */
    public void setUserid(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_msg.userid</code>.
     */
    public String getUserid() {
        return (String) get(4);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_msg.username</code>.
     */
    public void setUsername(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_msg.username</code>.
     */
    public String getUsername() {
        return (String) get(5);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_msg.msg_kind</code>.
     */
    public void setMsgKind(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_msg.msg_kind</code>.
     */
    public String getMsgKind() {
        return (String) get(6);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_msg.msg</code>.
     */
    public void setMsg(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_msg.msg</code>.
     */
    public String getMsg() {
        return (String) get(7);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_msg.company_id</code>.
     */
    public void setCompanyId(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_msg.company_id</code>.
     */
    public String getCompanyId() {
        return (String) get(8);
    }

}
