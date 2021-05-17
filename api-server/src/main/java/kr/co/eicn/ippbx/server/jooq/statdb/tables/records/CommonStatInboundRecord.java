package kr.co.eicn.ippbx.server.jooq.statdb.tables.records;

import org.jooq.Record1;
import org.jooq.Table;
import org.jooq.impl.UpdatableRecordImpl;

import java.sql.Date;

public class CommonStatInboundRecord  extends UpdatableRecordImpl<CommonStatInboundRecord> {

    public CommonStatInboundRecord(Table<CommonStatInboundRecord> table) {
        super(table);
    }

    /**
     * Setter for <code>STATDB.stat_inbound.seq</code>. 고유키
     */
    public void setSeq(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>STATDB.stat_inbound.seq</code>. 고유키
     */
    public Integer getSeq() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>STATDB.stat_inbound.company_id</code>. 회사 ID
     */
    public void setCompanyId(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>STATDB.stat_inbound.company_id</code>. 회사 ID
     */
    public String getCompanyId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>STATDB.stat_inbound.group_code</code>. 조직코드
     */
    public void setGroupCode(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>STATDB.stat_inbound.group_code</code>. 조직코드
     */
    public String getGroupCode() {
        return (String) get(2);
    }

    /**
     * Setter for <code>STATDB.stat_inbound.group_tree_name</code>. 조직트리명
     */
    public void setGroupTreeName(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>STATDB.stat_inbound.group_tree_name</code>. 조직트리명
     */
    public String getGroupTreeName() {
        return (String) get(3);
    }

    /**
     * Setter for <code>STATDB.stat_inbound.group_level</code>. 조직레벨
     */
    public void setGroupLevel(Integer value) {
        set(4, value);
    }

    /**
     * Getter for <code>STATDB.stat_inbound.group_level</code>. 조직레벨
     */
    public Integer getGroupLevel() {
        return (Integer) get(4);
    }

    /**
     * Setter for <code>STATDB.stat_inbound.service_number</code>. 대표번호070
     */
    public void setServiceNumber(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>STATDB.stat_inbound.service_number</code>. 대표번호070
     */
    public String getServiceNumber() {
        return (String) get(5);
    }

    /**
     * Setter for <code>STATDB.stat_inbound.hunt_number</code>. 헌트번호
     */
    public void setHuntNumber(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>STATDB.stat_inbound.hunt_number</code>. 헌트번호
     */
    public String getHuntNumber() {
        return (String) get(6);
    }

    /**
     * Setter for <code>STATDB.stat_inbound.worktime_yn</code>. 업무시간인지 아닌지 스케쥴유형에서 결정됨
     */
    public void setWorktimeYn(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>STATDB.stat_inbound.worktime_yn</code>. 업무시간인지 아닌지 스케쥴유형에서 결정됨
     */
    public String getWorktimeYn() {
        return (String) get(7);
    }

    /**
     * Setter for <code>STATDB.stat_inbound.category</code>. 인입경로
     */
    public void setCategory(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>STATDB.stat_inbound.category</code>. 인입경로
     */
    public String getCategory() {
        return (String) get(8);
    }

    /**
     /**
     * Setter for <code>STATDB.stat_inbound.ivr_tree_name</code>. ivr_tree의 tree_name
     */
    public void setIvrTreeName(String value) {
        set(9, value);
    }

    /**
     * Getter for <code>STATDB.stat_inbound.ivr_tree_name</code>. ivr_tree의 tree_name
     */
    public String getIvrTreeName() {
        return (String) get(9);
    }


    /**
     * Setter for <code>STATDB.stat_inbound.dcontext</code>. 다이얼플랜컨텍스트
     */
    public void setDcontext(String value) {
        set(10, value);
    }

    /**
     * Getter for <code>STATDB.stat_inbound.dcontext</code>. 다이얼플랜컨텍스트
     */
    public String getDcontext() {
        return (String) get(10);
    }

    /**
     * Setter for <code>STATDB.stat_inbound.stat_date</code>. 생성일
     */
    public void setStatDate(Date value) {
        set(11, value);
    }

    /**
     * Getter for <code>STATDB.stat_inbound.stat_date</code>. 생성일
     */
    public Date getStatDate() {
        return (Date) get(11);
    }

    /**
     * Setter for <code>STATDB.stat_inbound.stat_hour</code>. 생성시간
     */
    public void setStatHour(Byte value) {
        set(12, value);
    }

    /**
     * Getter for <code>STATDB.stat_inbound.stat_hour</code>. 생성시간
     */
    public Byte getStatHour() {
        return (Byte) get(12);
    }

    /**
     * Setter for <code>STATDB.stat_inbound.total</code>. 총계
     */
    public void setTotal(Integer value) {
        set(13, value);
    }

    /**
     * Getter for <code>STATDB.stat_inbound.total</code>. 총계
     */
    public Integer getTotal() {
        return (Integer) get(13);
    }

    /**
     * Setter for <code>STATDB.stat_inbound.onlyread</code>. 단순조회
     */
    public void setOnlyread(Integer value) {
        set(14, value);
    }

    /**
     * Getter for <code>STATDB.stat_inbound.onlyread</code>. 단순조회
     */
    public Integer getOnlyread() {
        return (Integer) get(14);
    }

    /**
     * Setter for <code>STATDB.stat_inbound.connreq</code>. 연결요청
     */
    public void setConnreq(Integer value) {
        set(15, value);
    }

    /**
     * Getter for <code>STATDB.stat_inbound.connreq</code>. 연결요청
     */
    public Integer getConnreq() {
        return (Integer) get(15);
    }

    /**
     * Setter for <code>STATDB.stat_inbound.success</code>. 응답
     */
    public void setSuccess(Integer value) {
        set(16, value);
    }

    /**
     * Getter for <code>STATDB.stat_inbound.success</code>. 응답
     */
    public Integer getSuccess() {
        return (Integer) get(16);
    }

    /**
     * Setter for <code>STATDB.stat_inbound.callback</code>. 콜백건수
     */
    public void setCallback(Integer value) {
        set(17, value);
    }

    /**
     * Getter for <code>STATDB.stat_inbound.callback</code>. 콜백건수
     */
    public Integer getCallback() {
        return (Integer) get(17);
    }

    /**
     * Setter for <code>STATDB.stat_inbound.callback_success</code>. 콜백성공건수-콜백이접수된건이지 처리된건은 아님
     */
    public void setCallbackSuccess(Integer value) {
        set(18, value);
    }

    /**
     * Getter for <code>STATDB.stat_inbound.callback_success</code>. 콜백성공건수-콜백이접수된건이지 처리된건은 아님
     */
    public Integer getCallbackSuccess() {
        return (Integer) get(18);
    }

    /**
     * Setter for <code>STATDB.stat_inbound.transfer</code>. 전환한콜수-첫전환콜만
     */
    public void setTransfer(Integer value) {
        set(19, value);
    }

    /**
     * Getter for <code>STATDB.stat_inbound.transfer</code>. 전환한콜수-첫전환콜만
     */
    public Integer getTransfer() {
        return (Integer) get(19);
    }

    /**
     * Setter for <code>STATDB.stat_inbound.cancel</code>. 포기호
     */
    public void setCancel(Integer value) {
        set(20, value);
    }

    /**
     * Getter for <code>STATDB.stat_inbound.cancel</code>. 포기호
     */
    public Integer getCancel() {
        return (Integer) get(20);
    }

    /**
     * Setter for <code>STATDB.stat_inbound.cancel_timeout</code>. 헌트타임아웃으로인한포기호
     */
    public void setCancelTimeout(Integer value) {
        set(21, value);
    }

    /**
     * Getter for <code>STATDB.stat_inbound.cancel_timeout</code>. 헌트타임아웃으로인한포기호
     */
    public Integer getCancelTimeout() {
        return (Integer) get(21);
    }

    /**
     * Setter for <code>STATDB.stat_inbound.cancel_callback</code>. 헌트타임아웃으로인한포기후콜백으로넘어감
     */
    public void setCancelCallback(Integer value) {
        set(22, value);
    }

    /**
     * Getter for <code>STATDB.stat_inbound.cancel_callback</code>. 헌트타임아웃으로인한포기후콜백으로넘어감
     */
    public Integer getCancelCallback() {
        return (Integer) get(22);
    }

    /**
     * Setter for <code>STATDB.stat_inbound.cancel_custom</code>. 고객포기호
     */
    public void setCancelCustom(Integer value) {
        set(23, value);
    }

    /**
     * Getter for <code>STATDB.stat_inbound.cancel_custom</code>. 고객포기호
     */
    public Integer getCancelCustom() {
        return (Integer) get(23);
    }

    /**
     * Setter for <code>STATDB.stat_inbound.service_level_ok</code>. 서비스레벨안에 전화를 받은건수
     */
    public void setServiceLevelOk(Integer value) {
        set(24, value);
    }

    /**
     * Getter for <code>STATDB.stat_inbound.service_level_ok</code>. 서비스레벨안에 전화를 받은건수
     */
    public Integer getServiceLevelOk() {
        return (Integer) get(24);
    }

    /**
     * Setter for <code>STATDB.stat_inbound.billsec_sum</code>. 통화시간
     */
    public void setBillsecSum(Integer value) {
        set(25, value);
    }

    /**
     * Getter for <code>STATDB.stat_inbound.billsec_sum</code>. 통화시간
     */
    public Integer getBillsecSum() {
        return (Integer) get(25);
    }

    /**
     * Setter for <code>STATDB.stat_inbound.wait_sum</code>. 대기시간합계
     */
    public void setWaitSum(Integer value) {
        set(26, value);
    }

    /**
     * Getter for <code>STATDB.stat_inbound.wait_sum</code>. 대기시간합계
     */
    public Integer getWaitSum() {
        return (Integer) get(26);
    }

    /**
     * Setter for <code>STATDB.stat_inbound.billsec_max</code>. 최대통화시간
     */
    public void setBillsecMax(Integer value) {
        set(27, value);
    }

    /**
     * Getter for <code>STATDB.stat_inbound.billsec_max</code>. 최대통화시간
     */
    public Integer getBillsecMax() {
        return (Integer) get(27);
    }

    /**
     * Setter for <code>STATDB.stat_inbound.wait_max</code>. 최대대기시간
     */
    public void setWaitMax(Integer value) {
        set(28, value);
    }

    /**
     * Getter for <code>STATDB.stat_inbound.wait_max</code>. 최대대기시간
     */
    public Integer getWaitMax() {
        return (Integer) get(28);
    }

    /**
     * Setter for <code>STATDB.stat_inbound.wait_cancel_sum</code>. 포기호들의대기시간합계
     */
    public void setWaitCancelSum(Integer value) {
        set(29, value);
    }

    /**
     * Getter for <code>STATDB.stat_inbound.wait_cancel_sum</code>. 포기호들의대기시간합계
     */
    public Integer getWaitCancelSum() {
        return (Integer) get(29);
    }

    /**
     * Setter for <code>STATDB.stat_inbound.wait_succ_0_10</code>. 응대호 0초&lt;대기시간=&lt;10초 건수
     */
    public void setWaitSucc_0_10(Integer value) {
        set(30, value);
    }

    /**
     * Getter for <code>STATDB.stat_inbound.wait_succ_0_10</code>. 응대호 0초&lt;대기시간=&lt;10초 건수
     */
    public Integer getWaitSucc_0_10() {
        return (Integer) get(30);
    }

    /**
     * Setter for <code>STATDB.stat_inbound.wait_succ_10_20</code>. 응대호 10초&lt;대기시간&lt;=20초 건수
     */
    public void setWaitSucc_10_20(Integer value) {
        set(31, value);
    }

    /**
     * Getter for <code>STATDB.stat_inbound.wait_succ_10_20</code>. 응대호 10초&lt;대기시간&lt;=20초 건수
     */
    public Integer getWaitSucc_10_20() {
        return (Integer) get(31);
    }

    /**
     * Setter for <code>STATDB.stat_inbound.wait_succ_20_30</code>. 응대호 20초&lt;대기시간&lt;=30초 건수
     */
    public void setWaitSucc_20_30(Integer value) {
        set(32, value);
    }

    /**
     * Getter for <code>STATDB.stat_inbound.wait_succ_20_30</code>. 응대호 20초&lt;대기시간&lt;=30초 건수
     */
    public Integer getWaitSucc_20_30() {
        return (Integer) get(32);
    }

    /**
     * Setter for <code>STATDB.stat_inbound.wait_succ_30_40</code>. 응대호 30초&lt;대기시간&lt;=40초 건수
     */
    public void setWaitSucc_30_40(Integer value) {
        set(33, value);
    }

    /**
     * Getter for <code>STATDB.stat_inbound.wait_succ_30_40</code>. 응대호 30초&lt;대기시간&lt;=40초 건수
     */
    public Integer getWaitSucc_30_40() {
        return (Integer) get(33);
    }

    /**
     * Setter for <code>STATDB.stat_inbound.wait_succ_40</code>. 응대호 40초&lt;대기시간 건수
     */
    public void setWaitSucc_40(Integer value) {
        set(34, value);
    }

    /**
     * Getter for <code>STATDB.stat_inbound.wait_succ_40</code>. 응대호 40초&lt;대기시간 건수
     */
    public Integer getWaitSucc_40() {
        return (Integer) get(34);
    }

    /**
     * Setter for <code>STATDB.stat_inbound.wait_cancel_0_10</code>. 포기호 0초&lt;대기시간=&lt;10초 건수
     */
    public void setWaitCancel_0_10(Integer value) {
        set(35, value);
    }

    /**
     * Getter for <code>STATDB.stat_inbound.wait_cancel_0_10</code>. 포기호 0초&lt;대기시간=&lt;10초 건수
     */
    public Integer getWaitCancel_0_10() {
        return (Integer) get(35);
    }

    /**
     * Setter for <code>STATDB.stat_inbound.wait_cancel_10_20</code>. 포기호 10초&lt;대기시간&lt;=20초 건수
     */
    public void setWaitCancel_10_20(Integer value) {
        set(36, value);
    }

    /**
     * Getter for <code>STATDB.stat_inbound.wait_cancel_10_20</code>. 포기호 10초&lt;대기시간&lt;=20초 건수
     */
    public Integer getWaitCancel_10_20() {
        return (Integer) get(36);
    }

    /**
     * Setter for <code>STATDB.stat_inbound.wait_cancel_20_30</code>. 포기호 20초&lt;대기시간&lt;=30초 건수
     */
    public void setWaitCancel_20_30(Integer value) {
        set(37, value);
    }

    /**
     * Getter for <code>STATDB.stat_inbound.wait_cancel_20_30</code>. 포기호 20초&lt;대기시간&lt;=30초 건수
     */
    public Integer getWaitCancel_20_30() {
        return (Integer) get(37);
    }

    /**
     * Setter for <code>STATDB.stat_inbound.wait_cancel_30_40</code>. 포기호 30초&lt;대기시간&lt;=40초 건수
     */
    public void setWaitCancel_30_40(Integer value) {
        set(38, value);
    }

    /**
     * Getter for <code>STATDB.stat_inbound.wait_cancel_30_40</code>. 포기호 30초&lt;대기시간&lt;=40초 건수
     */
    public Integer getWaitCancel_30_40() {
        return (Integer) get(38);
    }

    /**
     * Setter for <code>STATDB.stat_inbound.wait_cancel_40</code>. 포기호 40초&lt;대기시간 건수
     */
    public void setWaitCancel_40(Integer value) {
        set(39, value);
    }

    /**
     * Getter for <code>STATDB.stat_inbound.wait_cancel_40</code>. 포기호 40초&lt;대기시간 건수
     */
    public Integer getWaitCancel_40() {
        return (Integer) get(39);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }
}
