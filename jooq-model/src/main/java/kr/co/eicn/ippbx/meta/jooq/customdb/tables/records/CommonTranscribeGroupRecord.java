package kr.co.eicn.ippbx.meta.jooq.customdb.tables.records;

import org.jooq.Record1;
import org.jooq.Table;
import org.jooq.impl.UpdatableRecordImpl;

public class CommonTranscribeGroupRecord extends UpdatableRecordImpl<CommonTranscribeGroupRecord> {

    public CommonTranscribeGroupRecord(Table<CommonTranscribeGroupRecord> table) {
        super(table);
    }

    /**
     * Setter for <code>CUSTOMDB.transcribe_group.seq</code>.
     */
    public void setSeq(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>CUSTOMDB.transcribe_group.seq</code>.
     */
    public Integer getSeq() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>CUSTOMDB.transcribe_group.company_id</code>. 컴퍼니아이디
     */
    public void setCompanyId(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>CUSTOMDB.transcribe_group.company_id</code>. 컴퍼니아이디
     */
    public String getCompanyId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>CUSTOMDB.transcribe_group.groupName</code>. 전사그룹명
     */
    public void setGroupname(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>CUSTOMDB.transcribe_group.groupName</code>. 전사그룹명
     */
    public String getGroupname() {
        return (String) get(2);
    }

    /**
     * Setter for <code>CUSTOMDB.transcribe_group.userId</code>. 그룹담당자ID
     */
    public void setUserid(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>CUSTOMDB.transcribe_group.userId</code>. 그룹담당자ID
     */
    public String getUserid() {
        return (String) get(3);
    }

    /**
     * Setter for <code>CUSTOMDB.transcribe_group.status</code>. 그룹진행상태
     */
    public void setStatus(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>CUSTOMDB.transcribe_group.status</code>. 그룹진행상태
     */
    public String getStatus() {
        return (String) get(4);
    }

    /**
     * Setter for <code>CUSTOMDB.transcribe_group.fileCnt</code>. 녹취파일갯수
     */
    public void setFilecnt(Integer value) {
        set(5, value);
    }

    /**
     * Getter for <code>CUSTOMDB.transcribe_group.fileCnt</code>. 녹취파일갯수
     */
    public Integer getFilecnt() {
        return (Integer) get(5);
    }

    /**
     * Setter for <code>CUSTOMDB.transcribe_group.recRate</code>. 인식률
     */
    public void setRecrate(Double value) {
        set(6, value);
    }

    /**
     * Getter for <code>CUSTOMDB.transcribe_group.recRate</code>. 인식률
     */
    public Double getRecrate() {
        return (Double) get(6);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }
}
