package kr.co.eicn.ippbx.meta.jooq.statdb.tables.records;

import org.jooq.Record1;
import org.jooq.Table;
import org.jooq.impl.UpdatableRecordImpl;

import java.sql.Date;

public class CommonStatUserInboundRecord extends UpdatableRecordImpl<CommonStatUserInboundRecord> {

    public CommonStatUserInboundRecord(Table<CommonStatUserInboundRecord> table) {
        super(table);
    }

    /**
     * Setter for <code>STATDB.stat_user_inbound.seq</code>. 번호
     */
    public void setSeq(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_inbound.seq</code>. 번호
     */
    public Integer getSeq() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>STATDB.stat_user_inbound.company_id</code>. 회사아이디
     */
    public void setCompanyId(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_inbound.company_id</code>. 회사아이디
     */
    public String getCompanyId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>STATDB.stat_user_inbound.group_code</code>. 조직코드
     */
    public void setGroupCode(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_inbound.group_code</code>. 조직코드
     */
    public String getGroupCode() {
        return (String) get(2);
    }

    /**
     * Setter for <code>STATDB.stat_user_inbound.group_tree_name</code>. 조직트리명
     */
    public void setGroupTreeName(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_inbound.group_tree_name</code>. 조직트리명
     */
    public String getGroupTreeName() {
        return (String) get(3);
    }

    /**
     * Setter for <code>STATDB.stat_user_inbound.group_level</code>. 조직레벨
     */
    public void setGroupLevel(Integer value) {
        set(4, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_inbound.group_level</code>. 조직레벨
     */
    public Integer getGroupLevel() {
        return (Integer) get(4);
    }

    /**
     * Setter for <code>STATDB.stat_user_inbound.userid</code>. 상담원아이디
     */
    public void setUserid(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_inbound.userid</code>. 상담원아이디
     */
    public String getUserid() {
        return (String) get(5);
    }

    /**
     * Setter for <code>STATDB.stat_user_inbound.user_stat_yn</code>. 상담원통계라이센스여부
     */
    public void setUserStatYn(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_inbound.user_stat_yn</code>. 상담원통계라이센스여부
     */
    public String getUserStatYn() {
        return (String) get(6);
    }

    /**
     * Setter for <code>STATDB.stat_user_inbound.service_number</code>. 대표번호070
     */
    public void setServiceNumber(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_inbound.service_number</code>. 대표번호070
     */
    public String getServiceNumber() {
        return (String) get(7);
    }

    /**
     * Setter for <code>STATDB.stat_user_inbound.hunt_number</code>. 헌트번호
     */
    public void setHuntNumber(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_inbound.hunt_number</code>. 헌트번호
     */
    public String getHuntNumber() {
        return (String) get(8);
    }

    /**
     * Setter for <code>STATDB.stat_user_inbound.worktime_yn</code>.
     */
    public void setWorktimeYn(String value) {
        set(9, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_inbound.worktime_yn</code>.
     */
    public String getWorktimeYn() {
        return (String) get(9);
    }

    /**
     * Setter for <code>STATDB.stat_user_inbound.category</code>. 인입경로
     */
    public void setCategory(String value) {
        set(10, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_inbound.category</code>. 인입경로
     */
    public String getCategory() {
        return (String) get(10);
    }

    /**
     * Setter for <code>STATDB.stat_user_inbound.dcontext</code>. 다이얼플랜컨택스트
     */
    public void setDcontext(String value) {
        set(11, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_inbound.dcontext</code>. 다이얼플랜컨택스트
     */
    public String getDcontext() {
        return (String) get(11);
    }

    /**
     * Setter for <code>STATDB.stat_user_inbound.stat_date</code>. 날짜
     */
    public void setStatDate(Date value) {
        set(12, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_inbound.stat_date</code>. 날짜
     */
    public Date getStatDate() {
        return (Date) get(12);
    }

    /**
     * Setter for <code>STATDB.stat_user_inbound.stat_hour</code>. 시간
     */
    public void setStatHour(Byte value) {
        set(13, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_inbound.stat_hour</code>. 시간
     */
    public Byte getStatHour() {
        return (Byte) get(13);
    }

    /**
     * Setter for <code>STATDB.stat_user_inbound.in_total</code>. 총계
     */
    public void setInTotal(Integer value) {
        set(14, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_inbound.in_total</code>. 총계
     */
    public Integer getInTotal() {
        return (Integer) get(14);
    }

    /**
     * Setter for <code>STATDB.stat_user_inbound.in_success</code>. 응답
     */
    public void setInSuccess(Integer value) {
        set(15, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_inbound.in_success</code>. 응답
     */
    public Integer getInSuccess() {
        return (Integer) get(15);
    }

    public void setInHuntNoanswer(Integer value) {
        set(16, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_inbound.in_hunt_noanswer</code>. 헌트시비응답
     */
    public Integer getInHuntNoanswer() {
        return (Integer) get(16);
    }

    /**
     * Setter for <code>STATDB.stat_user_inbound.in_billsec_sum</code>. 통화시간
     */
    public void setInBillsecSum(Integer value) {
        set(17, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_inbound.in_billsec_sum</code>. 통화시간
     */
    public Integer getInBillsecSum() {
        return (Integer) get(17);
    }

    /**
     * Setter for <code>STATDB.stat_user_inbound.in_waitsec_sum</code>. 고객대기시간
     */
    public void setInWaitsecSum(Integer value) {
        set(18, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_inbound.in_waitsec_sum</code>. 고객대기시간
     */
    public Integer getInWaitsecSum() {
        return (Integer) get(18);
    }

    /**
     * Setter for <code>STATDB.stat_user_inbound.transferer</code>. 전환한콜수
     */
    public void setTransferer(Integer value) {
        set(19, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_inbound.transferer</code>. 전환한콜수
     */
    public Integer getTransferer() {
        return (Integer) get(19);
    }

    /**
     * Setter for <code>STATDB.stat_user_inbound.transferee</code>. 전환받은콜수
     */
    public void setTransferee(Integer value) {
        set(20, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_inbound.transferee</code>. 전환받은콜수
     */
    public Integer getTransferee() {
        return (Integer) get(20);
    }

    /**
     * Setter for <code>STATDB.stat_user_inbound.transferee_succ</code>. 전환받은성공콜수
     */
    public void setTransfereeSucc(Integer value) {
        set(21, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_inbound.transferee_succ</code>. 전환받은성공콜수
     */
    public Integer getTransfereeSucc() {
        return (Integer) get(21);
    }

    /**
     * Setter for <code>STATDB.stat_user_inbound.callback_dist</code>. 콜백분배받은건수
     */
    public void setCallbackDist(Integer value) {
        set(22, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_inbound.callback_dist</code>. 콜백분배받은건수
     */
    public Integer getCallbackDist() {
        return (Integer) get(22);
    }

    /**
     * Setter for <code>STATDB.stat_user_inbound.callback_succ</code>. 분배받은 콜백을 처리한건수
     */
    public void setCallbackSucc(Integer value) {
        set(23, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_inbound.callback_succ</code>. 분배받은 콜백을 처리한건수
     */
    public Integer getCallbackSucc() {
        return (Integer) get(23);
    }

    /**
     * Setter for <code>STATDB.stat_user_inbound.service_level_ok</code>. 서비스레벨안에 전화를 받은건수
     */
    public void setServiceLevelOk(Integer value) {
        set(24, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_inbound.service_level_ok</code>. 서비스레벨안에 전화를 받은건수
     */
    public Integer getServiceLevelOk() {
        return (Integer) get(24);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }
}
