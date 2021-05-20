package kr.co.eicn.ippbx.meta.jooq.customdb.tables.records;

import org.jooq.Table;
import org.jooq.impl.UpdatableRecordImpl;

import java.sql.Timestamp;

public class CommonChattRoomMemberRecord extends UpdatableRecordImpl<CommonChattRoomMemberRecord> {

    public CommonChattRoomMemberRecord(Table<CommonChattRoomMemberRecord> table) {
        super(table);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_room_member.room_id</code>.
     */
    public void setRoomId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_room_member.room_id</code>.
     */
    public String getRoomId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_room_member.userid</code>.
     */
    public void setUserid(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_room_member.userid</code>.
     */
    public String getUserid() {
        return (String) get(1);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_room_member.room_name</code>.
     */
    public void setRoomName(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_room_member.room_name</code>.
     */
    public String getRoomName() {
        return (String) get(2);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_room_member.member_md5</code>.
     */
    public void setMemberMd5(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_room_member.member_md5</code>.
     */
    public String getMemberMd5() {
        return (String) get(3);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_room_member.is_join</code>.
     */
    public void setIsJoin(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_room_member.is_join</code>.
     */
    public String getIsJoin() {
        return (String) get(4);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_room_member.invite_time</code>.
     */
    public void setInviteTime(Timestamp value) {
        set(5, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_room_member.invite_time</code>.
     */
    public Timestamp getInviteTime() {
        return (Timestamp) get(5);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_room_member.last_msg</code>.
     */
    public void setLastMsg(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_room_member.last_msg</code>.
     */
    public String getLastMsg() {
        return (String) get(6);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_room_member.last_type</code>.
     */
    public void setLastType(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_room_member.last_type</code>.
     */
    public String getLastType() {
        return (String) get(7);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_room_member.last_time</code>.
     */
    public void setLastTime(Timestamp value) {
        set(8, value);
    }

    /**
     * Getter for <code>CUSTOMDB.chatt_room_member.last_time</code>.
     */
    public Timestamp getLastTime() {
        return (Timestamp) get(8);
    }

    /**
     * Setter for <code>CUSTOMDB.chatt_room_member.read_time</code>.
     */
    public void setReadTime(Timestamp value) {
        set(9, value);
    }
    /**
     * Getter for <code>CUSTOMDB.chatt_room_member.read_time</code>.
     */
    public Timestamp getReadTime() {
        return (Timestamp) get(9);
    }
}
