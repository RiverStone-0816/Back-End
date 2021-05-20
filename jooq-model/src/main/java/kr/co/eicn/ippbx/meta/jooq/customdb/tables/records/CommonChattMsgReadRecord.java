package kr.co.eicn.ippbx.meta.jooq.customdb.tables.records;

import org.jooq.Table;
import org.jooq.impl.UpdatableRecordImpl;

public class CommonChattMsgReadRecord extends UpdatableRecordImpl<CommonChattMsgReadRecord> {

    public CommonChattMsgReadRecord(Table<CommonChattMsgReadRecord> table) {
        super(table);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_msg_read.message_id</code>.
     */
    public void setMessageId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_msg_read.message_id</code>.
     */
    public String getMessageId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_msg_read.room_id</code>.
     */
    public void setRoomId(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_msg_read.room_id</code>.
     */
    public String getRoomId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_msg_read.userid</code>.
     */
    public void setUserid(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_msg_read.userid</code>.
     */
    public String getUserid() {
        return (String) get(2);
    }
}
