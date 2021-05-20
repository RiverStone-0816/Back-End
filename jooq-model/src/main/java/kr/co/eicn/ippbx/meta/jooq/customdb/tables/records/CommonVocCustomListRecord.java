package kr.co.eicn.ippbx.meta.jooq.customdb.tables.records;

import org.jooq.Table;
import org.jooq.impl.TableRecordImpl;

import java.sql.Timestamp;

public class CommonVocCustomListRecord extends TableRecordImpl<CommonVocCustomListRecord> {

    public CommonVocCustomListRecord(Table<CommonVocCustomListRecord> table) {
        super(table);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_custom_list.seq</code>. SEQUENCE KEY
     */
    public void setSeq(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_custom_list.seq</code>. SEQUENCE KEY
     */
    public Integer getSeq() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_custom_list.insert_date</code>.
     */
    public void setInsertDate(Timestamp value) {
        set(1, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_custom_list.insert_date</code>.
     */
    public Timestamp getInsertDate() {
        return (Timestamp) get(1);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_custom_list.voc_group_id</code>. VOC_HAPPYCALL그룹 아이디
     */
    public void setVocGroupId(Integer value) {
        set(2, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_custom_list.voc_group_id</code>. VOC_HAPPYCALL그룹 아이디
     */
    public Integer getVocGroupId() {
        return (Integer) get(2);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_custom_list.sender</code>. 진행을 결정하는 주체 MEMBER-상담사가 화면에서인서트함 AUTO-자동으로 조건이 맞아서 시스템에서 인서트
     */
    public void setSender(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_custom_list.sender</code>. 진행을 결정하는 주체 MEMBER-상담사가 화면에서인서트함 AUTO-자동으로 조건이 맞아서 시스템에서 인서트
     */
    public String getSender() {
        return (String) get(3);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_custom_list.custom_number</code>. HAPPYCALL대상고객전화번호
     */
    public void setCustomNumber(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_custom_list.custom_number</code>. HAPPYCALL대상고객전화번호
     */
    public String getCustomNumber() {
        return (String) get(4);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_custom_list.company_id</code>.
     */
    public void setCompanyId(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_custom_list.company_id</code>.
     */
    public String getCompanyId() {
        return (String) get(5);
    }
}
