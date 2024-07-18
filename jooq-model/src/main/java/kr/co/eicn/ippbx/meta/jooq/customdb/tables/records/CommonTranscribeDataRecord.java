package kr.co.eicn.ippbx.meta.jooq.customdb.tables.records;

import org.jooq.Record1;
import org.jooq.Table;
import org.jooq.impl.UpdatableRecordImpl;

public class CommonTranscribeDataRecord extends UpdatableRecordImpl<CommonTranscribeDataRecord> {

    public CommonTranscribeDataRecord(Table<CommonTranscribeDataRecord> table) {
        super(table);
    }

    /**
     * Setter for <code>CUSTOMDB.transcribe_data.seq</code>. SEQUENCE KEY
     */
    public CommonTranscribeDataRecord setSeq(Integer value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>CUSTOMDB.transcribe_data.seq</code>. SEQUENCE KEY
     */
    public Integer getSeq() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>CUSTOMDB.transcribe_data.company_id</code>. 고객사 아이디
     */
    public CommonTranscribeDataRecord setCompanyId(String value) {
        set(1, value);
        return this;
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
    public CommonTranscribeDataRecord setGroupcode(Integer value) {
        set(2, value);
        return this;
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
    public CommonTranscribeDataRecord setFilepath(String value) {
        set(3, value);
        return this;
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
    public CommonTranscribeDataRecord setFilename(String value) {
        set(4, value);
        return this;
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
    public CommonTranscribeDataRecord setUserid(String value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>CUSTOMDB.transcribe_data.userId</code>. 전사상담원ID
     */
    public String getUserid() {
        return (String) get(5);
    }

    /**
     * Setter for <code>CUSTOMDB.transcribe_data.hypInfo</code>.
     */
    public CommonTranscribeDataRecord setHypinfo(String value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>CUSTOMDB.transcribe_data.hypInfo</code>.
     */
    public String getHypinfo() {
        return (String) get(6);
    }

    /**
     * Setter for <code>CUSTOMDB.transcribe_data.refInfo</code>.
     */
    public CommonTranscribeDataRecord setRefinfo(String value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>CUSTOMDB.transcribe_data.refInfo</code>.
     */
    public String getRefinfo() {
        return (String) get(7);
    }

    /**
     * Setter for <code>CUSTOMDB.transcribe_data.status</code>.
     */
    public CommonTranscribeDataRecord setStatus(String value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for <code>CUSTOMDB.transcribe_data.status</code>.
     */
    public String getStatus() {
        return (String) get(8);
    }

    /**
     * Setter for <code>CUSTOMDB.transcribe_data.sttStatus</code>. STT 요청상태
     */
    public CommonTranscribeDataRecord setSttstatus(String value) {
        set(9, value);
        return this;
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
    public CommonTranscribeDataRecord setRecstatus(String value) {
        set(10, value);
        return this;
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
    public CommonTranscribeDataRecord setLearn(String value) {
        set(11, value);
        return this;
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
    public CommonTranscribeDataRecord setNrate(Double value) {
        set(12, value);
        return this;
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
    public CommonTranscribeDataRecord setDrate(Double value) {
        set(13, value);
        return this;
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
    public CommonTranscribeDataRecord setSrate(Double value) {
        set(14, value);
        return this;
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
    public CommonTranscribeDataRecord setIrate(Double value) {
        set(15, value);
        return this;
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
    public CommonTranscribeDataRecord setArate(Double value) {
        set(16, value);
        return this;
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
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------


}
