package kr.co.eicn.ippbx.meta.jooq.statdb.tables.records;

import org.jooq.Record1;
import org.jooq.Table;
import org.jooq.impl.UpdatableRecordImpl;

import java.sql.Date;

public class CommonStatWtalkRecord extends UpdatableRecordImpl<CommonStatWtalkRecord> {

    public CommonStatWtalkRecord(Table<CommonStatWtalkRecord> table) {
        super(table);
    }

    /**
     * Setter for <code>STATDB.stat_wtalk.seq</code>.
     */
    public void setSeq(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>STATDB.stat_wtalk.seq</code>.
     */
    public Integer getSeq() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>STATDB.stat_wtalk.company_id</code>. 회사아이디
     */
    public void setCompanyId(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>STATDB.stat_wtalk.company_id</code>. 회사아이디
     */
    public String getCompanyId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>STATDB.stat_wtalk.channel_type</code>. 채널타입
     */
    public void setChannelType(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>STATDB.stat_wtalk.channel_type</code>. 채널타입
     */
    public String getChannelType() {
        return (String) get(2);
    }

    /**
     * Setter for <code>STATDB.stat_wtalk.sender_key</code>. 상담톡카카오가입서비스아이디
     */
    public void setSenderKey(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>STATDB.stat_wtalk.sender_key</code>. 상담톡카카오가입서비스아이디
     */
    public String getSenderKey() {
        return (String) get(3);
    }

    /**
     * Setter for <code>STATDB.stat_wtalk.userid</code>. 상담원 아이디
     */
    public void setUserid(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>STATDB.stat_wtalk.userid</code>. 상담원 아이디
     */
    public String getUserid() {
        return (String) get(4);
    }

    /**
     * Setter for <code>STATDB.stat_wtalk.group_code</code>. 조직코드
     */
    public void setGroupCode(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>STATDB.stat_wtalk.group_code</code>. 조직코드
     */
    public String getGroupCode() {
        return (String) get(5);
    }

    /**
     * Setter for <code>STATDB.stat_wtalk.group_tree_name</code>. 조직트리명
     */
    public void setGroupTreeName(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>STATDB.stat_wtalk.group_tree_name</code>. 조직트리명
     */
    public String getGroupTreeName() {
        return (String) get(6);
    }

    /**
     * Setter for <code>STATDB.stat_wtalk.group_level</code>. 조직레벨
     */
    public void setGroupLevel(Integer value) {
        set(7, value);
    }

    /**
     * Getter for <code>STATDB.stat_wtalk.group_level</code>. 조직레벨
     */
    public Integer getGroupLevel() {
        return (Integer) get(7);
    }

    /**
     * Setter for <code>STATDB.stat_wtalk.worktime_yn</code>.
     */
    public void setWorktimeYn(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>STATDB.stat_wtalk.worktime_yn</code>.
     */
    public String getWorktimeYn() {
        return (String) get(8);
    }

    /**
     * Setter for <code>STATDB.stat_wtalk.stat_date</code>.
     */
    public void setStatDate(Date value) {
        set(9, value);
    }

    /**
     * Getter for <code>STATDB.stat_wtalk.stat_date</code>.
     */
    public Date getStatDate() {
        return (Date) get(9);
    }

    /**
     * Setter for <code>STATDB.stat_wtalk.stat_hour</code>.
     */
    public void setStatHour(Byte value) {
        set(10, value);
    }

    /**
     * Getter for <code>STATDB.stat_wtalk.stat_hour</code>.
     */
    public Byte getStatHour() {
        return (Byte) get(10);
    }

    /**
     * Setter for <code>STATDB.stat_wtalk.action_type</code>. 액션타입
     */
    public void setActionType(String value) {
        set(11, value);
    }

    /**
     * Getter for <code>STATDB.stat_wtalk.action_type</code>. 액션타입
     */
    public String getActionType() {
        return (String) get(11);
    }

    /**
     * Setter for <code>STATDB.stat_wtalk.cnt</code>.
     */
    public void setCnt(Integer value) {
        set(12, value);
    }

    /**
     * Getter for <code>STATDB.stat_wtalk.cnt</code>.
     */
    public Integer getCnt() {
        return (Integer) get(12);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }
}
