package kr.co.eicn.ippbx.meta.jooq.customdb.tables.records;

import org.jooq.Record1;
import org.jooq.Table;
import org.jooq.impl.UpdatableRecordImpl;

import java.sql.Timestamp;

public class CommonMemberStatusRecord extends UpdatableRecordImpl<CommonMemberStatusRecord> {

    public CommonMemberStatusRecord(Table<CommonMemberStatusRecord> table) {
        super(table);
    }

    /**
     * Setter for <code>CUSTOMDB.member_status.seq</code>. 고유키
     */
    public void setSeq(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>CUSTOMDB.member_status.seq</code>. 고유키
     */
    public Integer getSeq() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>CUSTOMDB.member_status.start_date</code>. 시작일
     */
    public void setStartDate(Timestamp value) {
        set(1, value);
    }

    /**
     * Getter for <code>CUSTOMDB.member_status.start_date</code>. 시작일
     */
    public Timestamp getStartDate() {
        return (Timestamp) get(1);
    }

    /**
     * Setter for <code>CUSTOMDB.member_status.end_date</code>. 종료일
     */
    public void setEndDate(Timestamp value) {
        set(2, value);
    }

    /**
     * Getter for <code>CUSTOMDB.member_status.end_date</code>. 종료일
     */
    public Timestamp getEndDate() {
        return (Timestamp) get(2);
    }

    /**
     * Setter for <code>CUSTOMDB.member_status.phoneid</code>. 전화아이디
     */
    public void setPhoneid(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>CUSTOMDB.member_status.phoneid</code>. 전화아이디
     */
    public String getPhoneid() {
        return (String) get(3);
    }

    /**
     * Setter for <code>CUSTOMDB.member_status.phonename</code>. 전화이름
     */
    public void setPhonename(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>CUSTOMDB.member_status.phonename</code>. 전화이름
     */
    public String getPhonename() {
        return (String) get(4);
    }

    /**
     * Setter for <code>CUSTOMDB.member_status.status</code>. 상태
     */
    public void setStatus(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>CUSTOMDB.member_status.status</code>. 상태
     */
    public String getStatus() {
        return (String) get(5);
    }

    /**
     * Setter for <code>CUSTOMDB.member_status.next_status</code>. 다음상태
     */
    public void setNextStatus(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>CUSTOMDB.member_status.next_status</code>. 다음상태
     */
    public String getNextStatus() {
        return (String) get(6);
    }

    /**
     * Setter for <code>CUSTOMDB.member_status.in_out</code>. status가 1일때만 의미가 있음
     */
    public void setInOut(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>CUSTOMDB.member_status.in_out</code>. status가 1일때만 의미가 있음
     */
    public String getInOut() {
        return (String) get(7);
    }

    /**
     * Setter for <code>CUSTOMDB.member_status.company_id</code>. 회사아이디
     */
    public void setCompanyId(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>CUSTOMDB.member_status.company_id</code>. 회사아이디
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
