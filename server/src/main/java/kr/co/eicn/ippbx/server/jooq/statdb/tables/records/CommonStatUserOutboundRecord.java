package kr.co.eicn.ippbx.server.jooq.statdb.tables.records;

import org.jooq.Record1;
import org.jooq.Table;
import org.jooq.impl.UpdatableRecordImpl;

import java.sql.Date;

public class CommonStatUserOutboundRecord extends UpdatableRecordImpl<CommonStatUserOutboundRecord> {

    public CommonStatUserOutboundRecord(Table<CommonStatUserOutboundRecord> table) {
        super(table);
    }

    /**
     * Setter for <code>STATDB.stat_user_outbound.seq</code>. 번호
     */
    public void setSeq(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_outbound.seq</code>. 번호
     */
    public Integer getSeq() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>STATDB.stat_user_outbound.company_id</code>. 회사아이디
     */
    public void setCompanyId(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_outbound.company_id</code>. 회사아이디
     */
    public String getCompanyId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>STATDB.stat_user_outbound.group_code</code>. 조직코드
     */
    public void setGroupCode(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_outbound.group_code</code>. 조직코드
     */
    public String getGroupCode() {
        return (String) get(2);
    }

    /**
     * Setter for <code>STATDB.stat_user_outbound.group_tree_name</code>. 조직트리명
     */
    public void setGroupTreeName(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_outbound.group_tree_name</code>. 조직트리명
     */
    public String getGroupTreeName() {
        return (String) get(3);
    }

    /**
     * Setter for <code>STATDB.stat_user_outbound.group_level</code>. 조직레벨
     */
    public void setGroupLevel(Integer value) {
        set(4, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_outbound.group_level</code>. 조직레벨
     */
    public Integer getGroupLevel() {
        return (Integer) get(4);
    }

    /**
     * Setter for <code>STATDB.stat_user_outbound.userid</code>. 상담원아이디
     */
    public void setUserid(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_outbound.userid</code>. 상담원아이디
     */
    public String getUserid() {
        return (String) get(5);
    }

    /**
     * Setter for <code>STATDB.stat_user_outbound.from_org</code>. 일반콜-NOR, 콜백콜-CALLBACK,예약콜-RESERVE, PDS콜-PDS, 고객DB-MAINDB
     */
    public void setFromOrg(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_outbound.from_org</code>. 일반콜-NOR, 콜백콜-CALLBACK,예약콜-RESERVE, PDS콜-PDS, 고객DB-MAINDB
     */
    public String getFromOrg() {
        return (String) get(6);
    }

    /**
     * Setter for <code>STATDB.stat_user_outbound.worktime_yn</code>.
     */
    public void setWorktimeYn(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_outbound.worktime_yn</code>.
     */
    public String getWorktimeYn() {
        return (String) get(7);
    }

    /**
     * Setter for <code>STATDB.stat_user_outbound.dcontext</code>. 다이얼플랜컨택스트
     */
    public void setDcontext(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_outbound.dcontext</code>. 다이얼플랜컨택스트
     */
    public String getDcontext() {
        return (String) get(8);
    }

    /**
     * Setter for <code>STATDB.stat_user_outbound.stat_date</code>. 날짜
     */
    public void setStatDate(Date value) {
        set(9, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_outbound.stat_date</code>. 날짜
     */
    public Date getStatDate() {
        return (Date) get(9);
    }

    /**
     * Setter for <code>STATDB.stat_user_outbound.stat_hour</code>. 시간
     */
    public void setStatHour(Byte value) {
        set(10, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_outbound.stat_hour</code>. 시간
     */
    public Byte getStatHour() {
        return (Byte) get(10);
    }

    /**
     * Setter for <code>STATDB.stat_user_outbound.out_total</code>. 총계
     */
    public void setOutTotal(Integer value) {
        set(11, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_outbound.out_total</code>. 총계
     */
    public Integer getOutTotal() {
        return (Integer) get(11);
    }

    /**
     * Setter for <code>STATDB.stat_user_outbound.out_success</code>. 응답
     */
    public void setOutSuccess(Integer value) {
        set(12, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_outbound.out_success</code>. 응답
     */
    public Integer getOutSuccess() {
        return (Integer) get(12);
    }

    /**
     * Setter for <code>STATDB.stat_user_outbound.out_billsec_sum</code>. 통화시간
     */
    public void setOutBillsecSum(Integer value) {
        set(13, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_outbound.out_billsec_sum</code>. 통화시간
     */
    public Integer getOutBillsecSum() {
        return (Integer) get(13);
    }

    /**
     * Setter for <code>STATDB.stat_user_outbound.callback_call_cnt</code>. 콜백처리를위한 콜건수
     */
    public void setCallbackCallCnt(Integer value) {
        set(14, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_outbound.callback_call_cnt</code>. 콜백처리를위한 콜건수
     */
    public Integer getCallbackCallCnt() {
        return (Integer) get(14);
    }

    /**
     * Setter for <code>STATDB.stat_user_outbound.callback_call_succ</code>. 콜백처리를위한 콜이후 완료건수
     */
    public void setCallbackCallSucc(Integer value) {
        set(15, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_outbound.callback_call_succ</code>. 콜백처리를위한 콜이후 완료건수
     */
    public Integer getCallbackCallSucc() {
        return (Integer) get(15);
    }

    /**
     * Setter for <code>STATDB.stat_user_outbound.reserve_call_cnt</code>. 예약콜처리를 위한 콜건수
     */
    public void setReserveCallCnt(Integer value) {
        set(16, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_outbound.reserve_call_cnt</code>. 예약콜처리를 위한 콜건수
     */
    public Integer getReserveCallCnt() {
        return (Integer) get(16);
    }

    /**
     * Setter for <code>STATDB.stat_user_outbound.reserve_call_succ</code>. 예약콜처리를 콜이후 완료건수
     */
    public void setReserveCallSucc(Integer value) {
        set(17, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_outbound.reserve_call_succ</code>. 예약콜처리를 콜이후 완료건수
     */
    public Integer getReserveCallSucc() {
        return (Integer) get(17);
    }

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }
}
