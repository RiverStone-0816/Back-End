package kr.co.eicn.ippbx.meta.jooq.customdb.tables.records;

import org.jooq.Record1;
import org.jooq.Table;
import org.jooq.impl.UpdatableRecordImpl;

public class CommonTranscribeLearnRecord extends UpdatableRecordImpl<CommonTranscribeLearnRecord> {

    public CommonTranscribeLearnRecord(Table<CommonTranscribeLearnRecord> table) {
        super(table);
    }

    /**
     * Setter for <code>CUSTOMDB.transcribe_learn.seq</code>. 고유키
     */
    public void setSeq(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>CUSTOMDB.transcribe_learn.seq</code>. 고유키
     */
    public Integer getSeq() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>CUSTOMDB.transcribe_learn.company_id</code>. 고객사 아이디
     */
    public void setCompanyId(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>CUSTOMDB.transcribe_learn.company_id</code>. 고객사 아이디
     */
    public String getCompanyId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>CUSTOMDB.transcribe_learn.groupName</code>. 학습그룹명
     */
    public void setGroupname(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>CUSTOMDB.transcribe_learn.groupName</code>. 학습그룹명
     */
    public String getGroupname() {
        return (String) get(2);
    }

    /**
     * Setter for <code>CUSTOMDB.transcribe_learn.learnGroupCode</code>. 학습그룹코드모음
     */
    public void setLearngroupcode(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>CUSTOMDB.transcribe_learn.learnGroupCode</code>. 학습그룹코드모음
     */
    public String getLearngroupcode() {
        return (String) get(3);
    }

    /**
     * Setter for <code>CUSTOMDB.transcribe_learn.learnStatus</code>. 학습요청상태
     */
    public void setLearnstatus(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>CUSTOMDB.transcribe_learn.learnStatus</code>. 학습요청상태
     */
    public String getLearnstatus() {
        return (String) get(4);
    }

    /**
     * Setter for <code>CUSTOMDB.transcribe_learn.learnFileName</code>. 학습모델파일
     */
    public void setLearnfilename(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>CUSTOMDB.transcribe_learn.learnFileName</code>. 학습모델파일
     */
    public String getLearnfilename() {
        return (String) get(5);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }
}
