package kr.co.eicn.ippbx.meta.jooq.statdb.tables.records;

import org.jooq.Record1;
import org.jooq.Table;
import org.jooq.impl.UpdatableRecordImpl;

import java.sql.Date;

public class CommonStatOutboundRecord extends UpdatableRecordImpl<CommonStatOutboundRecord> {

    public CommonStatOutboundRecord(Table<CommonStatOutboundRecord> table) {
        super(table);
    }

    public void setSeq(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>STATDB.stat_outbound.seq</code>. 고유키
     */
    public Integer getSeq() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>STATDB.stat_outbound.company_id</code>. 회사아이디
     */
    public void setCompanyId(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>STATDB.stat_outbound.company_id</code>. 회사아이디
     */
    public String getCompanyId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>STATDB.stat_outbound.group_code</code>.  ex&gt;0001
     */
    public void setGroupCode(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>STATDB.stat_outbound.group_code</code>.  ex&gt;0001
     */
    public String getGroupCode() {
        return (String) get(2);
    }

    /**
     * Setter for <code>STATDB.stat_outbound.group_tree_name</code>. ex&gt;0003_0008_0001
     */
    public void setGroupTreeName(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>STATDB.stat_outbound.group_tree_name</code>. ex&gt;0003_0008_0001
     */
    public String getGroupTreeName() {
        return (String) get(3);
    }

    /**
     * Setter for <code>STATDB.stat_outbound.group_level</code>. 3
     */
    public void setGroupLevel(Integer value) {
        set(4, value);
    }

    /**
     * Getter for <code>STATDB.stat_outbound.group_level</code>. 3
     */
    public Integer getGroupLevel() {
        return (Integer) get(4);
    }

    /**
     * Setter for <code>STATDB.stat_outbound.from_org</code>. 일반콜-NOR, 콜백콜-CALLBACK,예약콜-RESERVE, PDS콜-PDS, 고객DB-MAINDB
     */
    public void setFromOrg(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>STATDB.stat_outbound.from_org</code>. 일반콜-NOR, 콜백콜-CALLBACK,예약콜-RESERVE, PDS콜-PDS, 고객DB-MAINDB
     */
    public String getFromOrg() {
        return (String) get(5);
    }

    /**
     * Setter for <code>STATDB.stat_outbound.stat_date</code>. 생성일
     */
    public void setStatDate(Date value) {
        set(6, value);
    }

    /**
     * Getter for <code>STATDB.stat_outbound.stat_date</code>. 생성일
     */
    public Date getStatDate() {
        return (Date) get(6);
    }

    /**
     * Setter for <code>STATDB.stat_outbound.stat_hour</code>. 생성시간
     */
    public void setStatHour(Byte value) {
        set(7, value);
    }

    /**
     * Getter for <code>STATDB.stat_outbound.stat_hour</code>. 생성시간
     */
    public Byte getStatHour() {
        return (Byte) get(7);
    }

    /**
     * Setter for <code>STATDB.stat_outbound.total</code>. 총계
     */
    public void setTotal(Integer value) {
        set(8, value);
    }

    /**
     * Getter for <code>STATDB.stat_outbound.total</code>. 총계
     */
    public Integer getTotal() {
        return (Integer) get(8);
    }

    /**
     * Setter for <code>STATDB.stat_outbound.success</code>. 응답
     */
    public void setSuccess(Integer value) {
        set(9, value);
    }

    /**
     * Getter for <code>STATDB.stat_outbound.success</code>. 응답
     */
    public Integer getSuccess() {
        return (Integer) get(9);
    }

    /**
     * Setter for <code>STATDB.stat_outbound.billsec_sum</code>. 통화시간
     */
    public void setBillsecSum(Integer value) {
        set(10, value);
    }

    /**
     * Getter for <code>STATDB.stat_outbound.billsec_sum</code>. 통화시간
     */
    public Integer getBillsecSum() {
        return (Integer) get(10);
    }

    /**
     * Setter for <code>STATDB.stat_outbound.wait_sum</code>. 통화전대기시간
     */
    public void setWaitSum(Integer value) {
        set(11, value);
    }

    /**
     * Getter for <code>STATDB.stat_outbound.wait_sum</code>. 통화전대기시간
     */
    public Integer getWaitSum() {
        return (Integer) get(11);
    }

    /**
     * Setter for <code>STATDB.stat_outbound.dcontext</code>. 다이얼플랜컨텍스트
     */
    public void setDcontext(String value) {
        set(12, value);
    }

    /**
     * Getter for <code>STATDB.stat_outbound.dcontext</code>. 다이얼플랜컨텍스트
     */
    public String getDcontext() {
        return (String) get(12);
    }

    /**
     * Setter for <code>STATDB.stat_outbound.worktime_yn</code>.
     */
    public void setWorktimeYn(String value) {
        set(13, value);
    }

    /**
     * Getter for <code>STATDB.stat_outbound.worktime_yn</code>.
     */
    public String getWorktimeYn() {
        return (String) get(13);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }
}
