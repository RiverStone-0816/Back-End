package kr.co.eicn.ippbx.meta.jooq.customdb.tables.records;

import org.jooq.Record1;
import org.jooq.Table;
import org.jooq.impl.UpdatableRecordImpl;

public class CommonTranscribeDataRecord extends UpdatableRecordImpl<CommonTranscribeDataRecord> {

    public CommonTranscribeDataRecord(Table<CommonTranscribeDataRecord> table) {
        super(table);
    }

    /**
     * Setter for <code>CUSTOMDB.transcribe_data.seq</code>. 고유키
     */
    public void setSeq(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>CUSTOMDB.transcribe_data.seq</code>. 고유키
     */
    public Integer getSeq() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>CUSTOMDB.transcribe_data.company_id</code>. 고객사 아이디
     */
    public void setCompanyId(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>CUSTOMDB.transcribe_data.company_id</code>. 고객사 아이디
     */
    public String getCompanyId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>CUSTOMDB.transcribe_data.groupCode</code>. 전사그룹코드
     */
    public void setGroupcode(Integer value) {
        set(2, value);
    }

    /**
     * Getter for <code>CUSTOMDB.transcribe_data.groupCode</code>. 전사그룹코드
     */
    public Integer getGroupcode() {
        return (Integer) get(2);
    }

    /**
     * Setter for <code>CUSTOMDB.transcribe_data.filePath</code>. 전사파일경로
     */
    public void setFilepath(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>CUSTOMDB.transcribe_data.filePath</code>. 전사파일경로
     */
    public String getFilepath() {
        return (String) get(3);
    }

    /**
     * Setter for <code>CUSTOMDB.transcribe_data.fileName</code>. 전사파일명
     */
    public void setFilename(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>CUSTOMDB.transcribe_data.fileName</code>. 전사파일명
     */
    public String getFilename() {
        return (String) get(4);
    }

    /**
     * Setter for <code>CUSTOMDB.transcribe_data.userId</code>. 전사상담원ID
     */
    public void setUserid(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>CUSTOMDB.transcribe_data.userId</code>. 전사상담원ID
     */
    public String getUserid() {
        return (String) get(5);
    }

    /**
     * Setter for <code>CUSTOMDB.transcribe_data.hypInfo</code>. STT정보
     */
    public void setHypinfo(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>CUSTOMDB.transcribe_data.hypInfo</code>. STT정보
     */
    public String getHypinfo() {
        return (String) get(6);
    }

    /**
     * Setter for <code>CUSTOMDB.transcribe_data.refInfo</code>. 전사처리정보
     */
    public void setRefinfo(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>CUSTOMDB.transcribe_data.refInfo</code>. 전사처리정보
     */
    public String getRefinfo() {
        return (String) get(7);
    }

    /**
     * Setter for <code>CUSTOMDB.transcribe_data.status</code>. 처리상태
     */
    public void setStatus(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>CUSTOMDB.transcribe_data.status</code>. 처리상태
     */
    public String getStatus() {
        return (String) get(8);
    }

    /**
     * Setter for <code>CUSTOMDB.transcribe_data.sttStatus</code>. STT 요청상태
     */
    public void setSttstatus(String value) {
        set(9, value);
    }

    /**
     * Getter for <code>CUSTOMDB.transcribe_data.sttStatus</code>. STT 요청상태
     */
    public String getSttstatus() {
        return (String) get(9);
    }

    /**
     * Setter for <code>CUSTOMDB.transcribe_data.recStatus</code>. 인식률측정 요청상태
     */
    public void setRecstatus(String value) {
        set(10, value);
    }

    /**
     * Getter for <code>CUSTOMDB.transcribe_data.recStatus</code>. 인식률측정 요청상태
     */
    public String getRecstatus() {
        return (String) get(10);
    }

    /**
     * Setter for <code>CUSTOMDB.transcribe_data.learn</code>.
     */
    public void setLearn(String value) {
        set(11, value);
    }

    /**
     * Getter for <code>CUSTOMDB.transcribe_data.learn</code>.
     */
    public String getLearn() {
        return (String) get(11);
    }

    /**
     * Setter for <code>CUSTOMDB.transcribe_data.nRate</code>. N인식률
     */
    public void setNrate(Double value) {
        set(12, value);
    }

    /**
     * Getter for <code>CUSTOMDB.transcribe_data.nRate</code>. N인식률
     */
    public Double getNrate() {
        return (Double) get(12);
    }

    /**
     * Setter for <code>CUSTOMDB.transcribe_data.dRate</code>. D인식률
     */
    public void setDrate(Double value) {
        set(13, value);
    }

    /**
     * Getter for <code>CUSTOMDB.transcribe_data.dRate</code>. D인식률
     */
    public Double getDrate() {
        return (Double) get(13);
    }

    /**
     * Setter for <code>CUSTOMDB.transcribe_data.sRate</code>. S인식률
     */
    public void setSrate(Double value) {
        set(14, value);
    }

    /**
     * Getter for <code>CUSTOMDB.transcribe_data.sRate</code>. S인식률
     */
    public Double getSrate() {
        return (Double) get(14);
    }

    /**
     * Setter for <code>CUSTOMDB.transcribe_data.iRate</code>. I인식률
     */
    public void setIrate(Double value) {
        set(15, value);
    }

    /**
     * Getter for <code>CUSTOMDB.transcribe_data.iRate</code>. I인식률
     */
    public Double getIrate() {
        return (Double) get(15);
    }

    /**
     * Setter for <code>CUSTOMDB.transcribe_data.aRate</code>. A인식률
     */
    public void setArate(Double value) {
        set(16, value);
    }

    /**
     * Getter for <code>CUSTOMDB.transcribe_data.aRate</code>. A인식률
     */
    public Double getArate() {
        return (Double) get(16);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }
}
