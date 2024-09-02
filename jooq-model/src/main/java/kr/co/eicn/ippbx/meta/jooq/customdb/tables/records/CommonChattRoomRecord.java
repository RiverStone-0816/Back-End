package kr.co.eicn.ippbx.meta.jooq.customdb.tables.records;

import org.jooq.Record1;
import org.jooq.Table;
import org.jooq.impl.UpdatableRecordImpl;

import java.sql.Timestamp;

public class CommonChattRoomRecord extends UpdatableRecordImpl<CommonChattRoomRecord> {

    public CommonChattRoomRecord(Table<CommonChattRoomRecord> table) {
        super(table);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_room.room_id</code>.
     */
    public void setRoomId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_room.room_id</code>.
     */
    public String getRoomId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_room.room_name</code>.
     */
    public void setRoomName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_room.room_name</code>.
     */
    public String getRoomName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_room.room_name_change</code>.
     */
    public void setRoomNameChange(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_room.room_name_change</code>.
     */
    public String getRoomNameChange() {
        return (String) get(2);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_room.member_md5</code>.
     */
    public void setMemberMd5(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_room.member_md5</code>.
     */
    public String getMemberMd5() {
        return (String) get(3);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_room.start_time</code>.
     */
    public void setStartTime(Timestamp value) {
        set(4, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_room.start_time</code>.
     */
    public Timestamp getStartTime() {
        return (Timestamp) get(4);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_room.last_time</code>.
     */
    public void setLastTime(Timestamp value) {
        set(5, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_room.last_time</code>.
     */
    public Timestamp getLastTime() {
        return (Timestamp) get(5);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_room.last_msg</code>.
     */
    public void setLastMsg(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_room.last_msg</code>.
     */
    public String getLastMsg() {
        return (String) get(6);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_room.last_msg_type</code>.
     */
    public void setLastMsgType(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_room.last_msg_type</code>.
     */
    public String getLastMsgType() {
        return (String) get(7);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_room.last_msg_send_receive</code>.
     */
    public void setLastMsgSendReceive(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_room.last_msg_send_receive</code>.
     */
    public String getLastMsgSendReceive() {
        return (String) get(8);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_room.last_userid</code>.
     */
    public void setLastUserid(String value) {
        set(9, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_room.last_userid</code>.
     */
    public String getLastUserid() {
        return (String) get(9);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_room.make_userid</code>.
     */
    public void setMakeUserid(String value) {
        set(10, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_room.make_userid</code>.
     */
    public String getMakeUserid() {
        return (String) get(10);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_room.cur_member_cnt</code>.
     */
    public void setCurMemberCnt(Integer value) {
        set(11, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_room.cur_member_cnt</code>.
     */
    public Integer getCurMemberCnt() {
        return (Integer) get(11);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_room.org_member_cnt</code>.
     */
    public void setOrgMemberCnt(Integer value) {
        set(12, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_room.org_member_cnt</code>.
     */
    public Integer getOrgMemberCnt() {
        return (Integer) get(12);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }
}
