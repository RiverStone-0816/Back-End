package kr.co.eicn.ippbx.meta.jooq.customdb.tables.records;

import org.jooq.Record1;
import org.jooq.Table;
import org.jooq.impl.UpdatableRecordImpl;

public class CommonLearnGroupRecord extends UpdatableRecordImpl<CommonLearnGroupRecord> {

    public CommonLearnGroupRecord(Table<CommonLearnGroupRecord> table){super(table);}

    /**
     * Setter for <code>CUSTOMDB.transcribe_learn_*.seq</code>.
     */
    public void setSeq(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>CUSTOMDB.transcribe_learn_*.seq</code>.
     */
    public Integer getSeq() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>CUSTOMDB.transcribe_learn_*.company_id</code>. 컴퍼니아이디
     */
    public void setCompanyId(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>CUSTOMDB.transcribe_learn_*.company_id</code>. 컴퍼니아이디
     */
    public String getCompanyId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>CUSTOMDB.transcribe_learn_*.group_name</code>. 학습그룹명
     */
    public void setGroupName(String value) { set(2, value); }

    /**
     * Getter for <code>CUSTOMDB.transcribe_learn_*.groupName</code>. 학습그룹명
     */
    public String getGroupName() {
        return (String) get(2);
    }

    /**
     * Setter for <code>CUSTOMDB.transcribe_learn_*.learnGroupCode</code>. 학습그룹정보
     */
    public void setLearnGroupCode(String value) { set(3, value); }

    /**
     * Getter for <code>CUSTOMDB.transcribe_learn_*.learnGroupCode</code>. 학습그룹정보
     */
    public String getLearnGroupCode() {
        return (String) get(3);
    }

    /**
     * Setter for <code>CUSTOMDB.transcribe_learn_*.learnStatus</code>. 학습요청상태
     */
    public void setLearnStatus(String value) { set(4, value); }

    /**
     * Getter for <code>CUSTOMDB.transcribe_learn.learnStatus</code>. 학습요청상태
     */
    public String getLearnStatus() {
        return (String) get(4);
    }

    /**
     * Setter for <code>CUSTOMDB.transcribe_group_*.group_name</code>. 학습모델파일
     */
    public void setLearnFileName(String value) { set(5, value); }

    /**
     * Getter for <code>CUSTOMDB.transcribe_group.groupName</code>. 학습모델파일
     */
    public String getLearnFileName() {
        return (String) get(5);
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
