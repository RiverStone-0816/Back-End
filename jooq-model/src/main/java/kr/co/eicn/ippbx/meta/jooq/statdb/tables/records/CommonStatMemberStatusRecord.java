package kr.co.eicn.ippbx.meta.jooq.statdb.tables.records;

import org.jooq.Record1;
import org.jooq.Table;
import org.jooq.impl.UpdatableRecordImpl;

import java.sql.Date;

public class CommonStatMemberStatusRecord extends UpdatableRecordImpl<CommonStatMemberStatusRecord> {

    public CommonStatMemberStatusRecord(Table<CommonStatMemberStatusRecord> table) {
        super(table);
    }

    /**
     * Setter for <code>STATDB.stat_member_status.seq</code>. 번호
     */
    public void setSeq(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>STATDB.stat_member_status.seq</code>. 번호
     */
    public Integer getSeq() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>STATDB.stat_member_status.stat_date</code>. 날짜
     */
    public void setStatDate(Date value) {
        set(1, value);
    }

    /**
     * Getter for <code>STATDB.stat_member_status.stat_date</code>. 날짜
     */
    public Date getStatDate() {
        return (Date) get(1);
    }

    /**
     * Setter for <code>STATDB.stat_member_status.stat_hour</code>. 시간
     */
    public void setStatHour(Byte value) {
        set(2, value);
    }

    /**
     * Getter for <code>STATDB.stat_member_status.stat_hour</code>. 시간
     */
    public Byte getStatHour() {
        return (Byte) get(2);
    }

    /**
     * Setter for <code>STATDB.stat_member_status.status</code>. 상태
     */
    public void setStatus(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>STATDB.stat_member_status.status</code>. 상태
     */
    public String getStatus() {
        return (String) get(3);
    }

    /**
     * Setter for <code>STATDB.stat_member_status.in_out</code>. status가 1일때만 의미가 있음
     */
    public void setInOut(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>STATDB.stat_member_status.in_out</code>. status가 1일때만 의미가 있음
     */
    public String getInOut() {
        return (String) get(4);
    }

    /**
     * Setter for <code>STATDB.stat_member_status.total</code>. 횟수
     */
    public void setTotal(Integer value) {
        set(5, value);
    }

    /**
     * Getter for <code>STATDB.stat_member_status.total</code>. 횟수
     */
    public Integer getTotal() {
        return (Integer) get(5);
    }

    /**
     * Setter for <code>STATDB.stat_member_status.diff_sum</code>. 시간
     */
    public void setDiffSum(Integer value) {
        set(6, value);
    }

    /**
     * Getter for <code>STATDB.stat_member_status.diff_sum</code>. 시간
     */
    public Integer getDiffSum() {
        return (Integer) get(6);
    }

    /**
     * Setter for <code>STATDB.stat_member_status.userid</code>. 상담원아이디
     */
    public void setUserid(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>STATDB.stat_member_status.userid</code>. 상담원아이디
     */
    public String getUserid() {
        return (String) get(7);
    }

    /**
     * Setter for <code>STATDB.stat_member_status.company_id</code>. 회사아이디
     */
    public void setCompanyId(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>STATDB.stat_member_status.company_id</code>. 회사아이디
     */
    public String getCompanyId() {
        return (String) get(8);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }
}
