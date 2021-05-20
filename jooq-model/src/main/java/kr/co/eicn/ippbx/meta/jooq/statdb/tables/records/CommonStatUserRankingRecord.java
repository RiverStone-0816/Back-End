package kr.co.eicn.ippbx.meta.jooq.statdb.tables.records;

import org.jooq.Record1;
import org.jooq.Table;
import org.jooq.impl.UpdatableRecordImpl;

import java.sql.Date;

public class CommonStatUserRankingRecord  extends UpdatableRecordImpl<CommonStatUserRankingRecord> {
    public CommonStatUserRankingRecord(Table<CommonStatUserRankingRecord> table) {
        super(table);
    }

    /**
            * Setter for <code>STATDB.stat_user_ranking.seq</code>. 고유키
     */
    public void setSeq(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_ranking.seq</code>. 고유키
     */
    public Integer getSeq() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>STATDB.stat_user_ranking.company_id</code>. 회사 ID
     */
    public void setCompanyId(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_ranking.company_id</code>. 회사 ID
     */
    public String getCompanyId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>STATDB.stat_user_ranking.group_code</code>. 조직코드
     */
    public void setGroupCode(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_ranking.group_code</code>. 조직코드
     */
    public String getGroupCode() {
        return (String) get(2);
    }

    /**
     * Setter for <code>STATDB.stat_user_ranking.group_tree_name</code>. 조직트리명
     */
    public void setGroupTreeName(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_ranking.group_tree_name</code>. 조직트리명
     */
    public String getGroupTreeName() {
        return (String) get(3);
    }

    /**
     * Setter for <code>STATDB.stat_user_ranking.group_level</code>. 조직레벨
     */
    public void setGroupLevel(Integer value) {
        set(4, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_ranking.group_level</code>. 조직레벨
     */
    public Integer getGroupLevel() {
        return (Integer) get(4);
    }

    /**
     * Setter for <code>STATDB.stat_user_ranking.userid</code>. 상담원아이디
     */
    public void setUserid(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_ranking.userid</code>. 상담원아이디
     */
    public String getUserid() {
        return (String) get(5);
    }

    /**
     * Setter for <code>STATDB.stat_user_ranking.worktime_yn</code>. 업무시간여부
     */
    public void setWorktimeYn(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_ranking.worktime_yn</code>. 업무시간여부
     */
    public String getWorktimeYn() {
        return (String) get(6);
    }

    /**
     * Setter for <code>STATDB.stat_user_ranking.stat_date</code>. 생성일
     */
    public void setStatDate(Date value) {
        set(7, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_ranking.stat_date</code>. 생성일
     */
    public Date getStatDate() {
        return (Date) get(7);
    }

    /**
     * Setter for <code>STATDB.stat_user_ranking.total_success</code>. 통화성공총계
     */
    public void setTotalSuccess(Integer value) {
        set(8, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_ranking.total_success</code>. 통화성공총계
     */
    public Integer getTotalSuccess() {
        return (Integer) get(8);
    }

    /**
     * Setter for <code>STATDB.stat_user_ranking.in_success</code>. 인바운드성공
     */
    public void setInSuccess(Integer value) {
        set(9, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_ranking.in_success</code>. 인바운드성공
     */
    public Integer getInSuccess() {
        return (Integer) get(9);
    }

    /**
     * Setter for <code>STATDB.stat_user_ranking.out_success</code>. 아웃바운드성공
     */
    public void setOutSuccess(Integer value) {
        set(10, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_ranking.out_success</code>. 아웃바운드성공
     */
    public Integer getOutSuccess() {
        return (Integer) get(10);
    }

    /**
     * Setter for <code>STATDB.stat_user_ranking.total_billsec_sum</code>. 통화성공통화시간총계
     */
    public void setTotalBillsecSum(Integer value) {
        set(11, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_ranking.total_billsec_sum</code>. 통화성공통화시간총계
     */
    public Integer getTotalBillsecSum() {
        return (Integer) get(11);
    }

    /**
     * Setter for <code>STATDB.stat_user_ranking.in_billsec_sum</code>. 인바운드성공통화시간
     */
    public void setInBillsecSum(Integer value) {
        set(12, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_ranking.in_billsec_sum</code>. 인바운드성공통화시간
     */
    public Integer getInBillsecSum() {
        return (Integer) get(12);
    }

    /**
     * Setter for <code>STATDB.stat_user_ranking.out_billsec_sum</code>. 아웃바운드성공통화시간
     */
    public void setOutBillsecSum(Integer value) {
        set(13, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_ranking.out_billsec_sum</code>. 아웃바운드성공통화시간
     */
    public Integer getOutBillsecSum() {
        return (Integer) get(13);
    }

    /**
     * Setter for <code>STATDB.stat_user_ranking.callback_success</code>. 콜백처리성공
     */
    public void setCallbackSuccess(Integer value) {
        set(14, value);
    }

    /**
     * Getter for <code>STATDB.stat_user_ranking.callback_success</code>. 콜백처리성공
     */
    public Integer getCallbackSuccess() {
        return (Integer) get(14);
    }

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }
}
