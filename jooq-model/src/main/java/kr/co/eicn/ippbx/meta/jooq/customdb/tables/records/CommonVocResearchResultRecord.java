package kr.co.eicn.ippbx.meta.jooq.customdb.tables.records;

import org.jooq.Record1;
import org.jooq.Table;
import org.jooq.impl.UpdatableRecordImpl;

import java.sql.Timestamp;

public class CommonVocResearchResultRecord extends UpdatableRecordImpl<CommonVocResearchResultRecord> {

    public CommonVocResearchResultRecord(Table<CommonVocResearchResultRecord> table) {
        super(table);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_result.seq</code>. SEQUENCE KEY
     */
    public void setSeq(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_result.seq</code>. SEQUENCE KEY
     */
    public Integer getSeq() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_result.result_date</code>. 설문일시
     */
    public void setResultDate(Timestamp value) {
        set(1, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_result.result_date</code>. 설문일시
     */
    public Timestamp getResultDate() {
        return (Timestamp) get(1);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_result.uniqueid</code>. 유니크 아이디
     */
    public void setUniqueid(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_result.uniqueid</code>. 유니크 아이디
     */
    public String getUniqueid() {
        return (String) get(2);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_result.hangup_cause</code>. 전화행업코드
     */
    public void setHangupCause(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_result.hangup_cause</code>. 전화행업코드
     */
    public String getHangupCause() {
        return (String) get(3);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_result.hangup_msg</code>. 전화행업메시지
     */
    public void setHangupMsg(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_result.hangup_msg</code>. 전화행업메시지
     */
    public String getHangupMsg() {
        return (String) get(4);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_result.billsec</code>. 통화시간
     */
    public void setBillsec(Integer value) {
        set(5, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_result.billsec</code>. 통화시간
     */
    public Integer getBillsec() {
        return (Integer) get(5);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_result.custom_number</code>. 고객 전화번호
     */
    public void setCustomNumber(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_result.custom_number</code>. 고객 전화번호
     */
    public String getCustomNumber() {
        return (String) get(6);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_result.userid</code>. 상담자
     */
    public void setUserid(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_result.userid</code>. 상담자
     */
    public String getUserid() {
        return (String) get(7);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_result.voc_group_id</code>. VOC그룹 아이디
     */
    public void setVocGroupId(Integer value) {
        set(8, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_result.voc_group_id</code>. VOC그룹 아이디
     */
    public Integer getVocGroupId() {
        return (Integer) get(8);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_result.research_id</code>. 설문 아이디
     */
    public void setResearchId(Integer value) {
        set(9, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_result.research_id</code>. 설문 아이디
     */
    public Integer getResearchId() {
        return (Integer) get(9);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_result.tree_path</code>. 설문단계
     */
    public void setTreePath(String value) {
        set(10, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_result.tree_path</code>. 설문단계
     */
    public String getTreePath() {
        return (String) get(10);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_result.company_id</code>. 고객사 아이디
     */
    public void setCompanyId(String value) {
        set(11, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_result.company_id</code>. 고객사 아이디
     */
    public String getCompanyId() {
        return (String) get(11);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }
}
