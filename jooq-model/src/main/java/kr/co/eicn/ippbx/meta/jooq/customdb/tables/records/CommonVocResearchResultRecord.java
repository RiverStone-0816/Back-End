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
     * Setter for <code>CUSTOMDB.voc_research_result.seq</code>.
     */
    public void setSeq(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_research_result.seq</code>.
     */
    public Integer getSeq() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_research_result.company_id</code>.
     */
    public void setCompanyId(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_research_result.company_id</code>.
     */
    public String getCompanyId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_research_result.result_date</code>.
     */
    public void setResultDate(Timestamp value) {
        set(2, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_research_result.result_date</code>.
     */
    public Timestamp getResultDate() {
        return (Timestamp) get(2);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_research_result.uniqueid</code>.
     */
    public void setUniqueid(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_research_result.uniqueid</code>.
     */
    public String getUniqueid() {
        return (String) get(3);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_research_result.hangup_cause</code>.
     */
    public void setHangupCause(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_research_result.hangup_cause</code>.
     */
    public String getHangupCause() {
        return (String) get(4);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_research_result.hangup_msg</code>.
     */
    public void setHangupMsg(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_research_result.hangup_msg</code>.
     */
    public String getHangupMsg() {
        return (String) get(5);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_research_result.billsec</code>.
     */
    public void setBillsec(Integer value) {
        set(6, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_research_result.billsec</code>.
     */
    public Integer getBillsec() {
        return (Integer) get(6);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_research_result.click_key</code>.
     */
    public void setClickKey(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_research_result.click_key</code>.
     */
    public String getClickKey() {
        return (String) get(7);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_research_result.custom_number</code>.
     */
    public void setCustomNumber(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_research_result.custom_number</code>.
     */
    public String getCustomNumber() {
        return (String) get(8);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_research_result.voc_group_id</code>.
     */
    public void setVocGroupId(Integer value) {
        set(9, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_research_result.voc_group_id</code>.
     */
    public Integer getVocGroupId() {
        return (Integer) get(9);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_research_result.userid</code>.
     */
    public void setUserid(String value) {
        set(10, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_research_result.userid</code>.
     */
    public String getUserid() {
        return (String) get(10);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_research_result.extension</code>.
     */
    public void setExtension(String value) {
        set(11, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_research_result.extension</code>.
     */
    public String getExtension() {
        return (String) get(11);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_research_result.research_id</code>.
     */
    public void setResearchId(Integer value) {
        set(12, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_research_result.research_id</code>.
     */
    public Integer getResearchId() {
        return (Integer) get(12);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_research_result.tree_path</code>.
     */
    public void setTreePath(String value) {
        set(13, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_research_result.tree_path</code>.
     */
    public String getTreePath() {
        return (String) get(13);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_research_result.my_path_1</code>.
     */
    public void setMyPath_1(String value) {
        set(14, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_research_result.my_path_1</code>.
     */
    public String getMyPath_1() {
        return (String) get(14);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_research_result.my_path_2</code>.
     */
    public void setMyPath_2(String value) {
        set(15, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_research_result.my_path_2</code>.
     */
    public String getMyPath_2() {
        return (String) get(15);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_research_result.my_path_3</code>.
     */
    public void setMyPath_3(String value) {
        set(16, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_research_result.my_path_3</code>.
     */
    public String getMyPath_3() {
        return (String) get(16);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_research_result.my_path_4</code>.
     */
    public void setMyPath_4(String value) {
        set(17, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_research_result.my_path_4</code>.
     */
    public String getMyPath_4() {
        return (String) get(17);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_research_result.my_path_5</code>.
     */
    public void setMyPath_5(String value) {
        set(18, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_research_result.my_path_5</code>.
     */
    public String getMyPath_5() {
        return (String) get(18);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_research_result.my_path_6</code>.
     */
    public void setMyPath_6(String value) {
        set(19, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_research_result.my_path_6</code>.
     */
    public String getMyPath_6() {
        return (String) get(19);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_research_result.my_path_7</code>.
     */
    public void setMyPath_7(String value) {
        set(20, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_research_result.my_path_7</code>.
     */
    public String getMyPath_7() {
        return (String) get(20);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_research_result.my_path_8</code>.
     */
    public void setMyPath_8(String value) {
        set(21, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_research_result.my_path_8</code>.
     */
    public String getMyPath_8() {
        return (String) get(21);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_research_result.my_path_9</code>.
     */
    public void setMyPath_9(String value) {
        set(22, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_research_result.my_path_9</code>.
     */
    public String getMyPath_9() {
        return (String) get(22);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_research_result.my_path_10</code>.
     */
    public void setMyPath_10(String value) {
        set(23, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_research_result.my_path_10</code>.
     */
    public String getMyPath_10() {
        return (String) get(23);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_research_result.my_path_11</code>.
     */
    public void setMyPath_11(String value) {
        set(24, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_research_result.my_path_11</code>.
     */
    public String getMyPath_11() {
        return (String) get(24);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_research_result.my_path_12</code>.
     */
    public void setMyPath_12(String value) {
        set(25, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_research_result.my_path_12</code>.
     */
    public String getMyPath_12() {
        return (String) get(25);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_research_result.my_path_13</code>.
     */
    public void setMyPath_13(String value) {
        set(26, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_research_result.my_path_13</code>.
     */
    public String getMyPath_13() {
        return (String) get(26);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_research_result.my_path_14</code>.
     */
    public void setMyPath_14(String value) {
        set(27, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_research_result.my_path_14</code>.
     */
    public String getMyPath_14() {
        return (String) get(27);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_research_result.my_path_15</code>.
     */
    public void setMyPath_15(String value) {
        set(28, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_research_result.my_path_15</code>.
     */
    public String getMyPath_15() {
        return (String) get(28);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_research_result.my_path_16</code>.
     */
    public void setMyPath_16(String value) {
        set(29, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_research_result.my_path_16</code>.
     */
    public String getMyPath_16() {
        return (String) get(29);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_research_result.my_path_17</code>.
     */
    public void setMyPath_17(String value) {
        set(30, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_research_result.my_path_17</code>.
     */
    public String getMyPath_17() {
        return (String) get(30);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_research_result.my_path_18</code>.
     */
    public void setMyPath_18(String value) {
        set(31, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_research_result.my_path_18</code>.
     */
    public String getMyPath_18() {
        return (String) get(31);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_research_result.my_path_19</code>.
     */
    public void setMyPath_19(String value) {
        set(32, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_research_result.my_path_19</code>.
     */
    public String getMyPath_19() {
        return (String) get(32);
    }

    /**
     * Setter for <code>CUSTOMDB.voc_research_result.my_path_20</code>.
     */
    public void setMyPath_20(String value) {
        set(33, value);
    }

    /**
     * Getter for <code>CUSTOMDB.voc_research_result.my_path_20</code>.
     */
    public String getMyPath_20() {
        return (String) get(33);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

}
