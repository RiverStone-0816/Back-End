package kr.co.eicn.ippbx.server.jooq.customdb.tables.records;

import org.jooq.Table;
import org.jooq.impl.TableRecordImpl;

import java.sql.Date;
import java.sql.Timestamp;

public class CommonViewResultCstByResultRecord extends TableRecordImpl<CommonViewResultCstByResultRecord> {

    public CommonViewResultCstByResultRecord(Table<CommonViewResultCstByResultRecord> table) {
        super(table);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.seq</code>.
     */
    public void setSeq(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.seq</code>.
     */
    public Integer getSeq() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.company_id</code>.
     */
    public void setCompanyId(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.company_id</code>.
     */
    public String getCompanyId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.result_date</code>.
     */
    public void setResultDate(Timestamp value) {
        set(2, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.result_date</code>.
     */
    public Timestamp getResultDate() {
        return (Timestamp) get(2);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.update_date</code>.
     */
    public void setUpdateDate(Timestamp value) {
        set(3, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.update_date</code>.
     */
    public Timestamp getUpdateDate() {
        return (Timestamp) get(3);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.uniqueid</code>.
     */
    public void setUniqueid(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.uniqueid</code>.
     */
    public String getUniqueid() {
        return (String) get(4);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.hangup_cause</code>.
     */
    public void setHangupCause(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.hangup_cause</code>.
     */
    public String getHangupCause() {
        return (String) get(5);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.hangup_msg</code>.
     */
    public void setHangupMsg(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.hangup_msg</code>.
     */
    public String getHangupMsg() {
        return (String) get(6);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.billsec</code>.
     */
    public void setBillsec(Integer value) {
        set(7, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.billsec</code>.
     */
    public Integer getBillsec() {
        return (Integer) get(7);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.custom_id</code>.
     */
    public void setCustomId(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.custom_id</code>.
     */
    public String getCustomId() {
        return (String) get(8);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.click_key</code>.
     */
    public void setClickKey(String value) {
        set(9, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.click_key</code>.
     */
    public String getClickKey() {
        return (String) get(9);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.custom_number</code>.
     */
    public void setCustomNumber(String value) {
        set(10, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.custom_number</code>.
     */
    public String getCustomNumber() {
        return (String) get(10);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.execute_id</code>.
     */
    public void setExecuteId(String value) {
        set(11, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.execute_id</code>.
     */
    public String getExecuteId() {
        return (String) get(11);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.group_id</code>.
     */
    public void setGroupId(Integer value) {
        set(12, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.group_id</code>.
     */
    public Integer getGroupId() {
        return (Integer) get(12);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.userid</code>.
     */
    public void setUserid(String value) {
        set(13, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.userid</code>.
     */
    public String getUserid() {
        return (String) get(13);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.userid_org</code>.
     */
    public void setUseridOrg(String value) {
        set(14, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.userid_org</code>.
     */
    public String getUseridOrg() {
        return (String) get(14);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.userid_tr</code>.
     */
    public void setUseridTr(String value) {
        set(15, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.userid_tr</code>.
     */
    public String getUseridTr() {
        return (String) get(15);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.result_type</code>.
     */
    public void setResultType(Integer value) {
        set(16, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.result_type</code>.
     */
    public Integer getResultType() {
        return (Integer) get(16);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.call_type</code>.
     */
    public void setCallType(String value) {
        set(17, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.call_type</code>.
     */
    public String getCallType() {
        return (String) get(17);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.from_org</code>.
     */
    public void setFromOrg(String value) {
        set(18, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.from_org</code>.
     */
    public String getFromOrg() {
        return (String) get(18);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.group_kind</code>.
     */
    public void setGroupKind(String value) {
        set(19, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.group_kind</code>.
     */
    public String getGroupKind() {
        return (String) get(19);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.group_type</code>.
     */
    public void setGroupType(Integer value) {
        set(20, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.group_type</code>.
     */
    public Integer getGroupType() {
        return (Integer) get(20);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_DATE_1</code>.
     */
    public void setRsDate_1(Date value) {
        set(21, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_DATE_1</code>.
     */
    public Date getRsDate_1() {
        return (Date) get(21);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_DATE_2</code>.
     */
    public void setRsDate_2(Date value) {
        set(22, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_DATE_2</code>.
     */
    public Date getRsDate_2() {
        return (Date) get(22);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_DATE_3</code>.
     */
    public void setRsDate_3(Date value) {
        set(23, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_DATE_3</code>.
     */
    public Date getRsDate_3() {
        return (Date) get(23);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_DAY_1</code>.
     */
    public void setRsDay_1(Date value) {
        set(24, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_DAY_1</code>.
     */
    public Date getRsDay_1() {
        return (Date) get(24);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_DAY_2</code>.
     */
    public void setRsDay_2(Date value) {
        set(25, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_DAY_2</code>.
     */
    public Date getRsDay_2() {
        return (Date) get(25);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_DAY_3</code>.
     */
    public void setRsDay_3(Date value) {
        set(26, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_DAY_3</code>.
     */
    public Date getRsDay_3() {
        return (Date) get(26);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_DATETIME_1</code>.
     */
    public void setRsDatetime_1(Timestamp value) {
        set(27, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_DATETIME_1</code>.
     */
    public Timestamp getRsDatetime_1() {
        return (Timestamp) get(27);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_DATETIME_2</code>.
     */
    public void setRsDatetime_2(Timestamp value) {
        set(28, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_DATETIME_2</code>.
     */
    public Timestamp getRsDatetime_2() {
        return (Timestamp) get(28);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_DATETIME_3</code>.
     */
    public void setRsDatetime_3(Timestamp value) {
        set(29, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_DATETIME_3</code>.
     */
    public Timestamp getRsDatetime_3() {
        return (Timestamp) get(29);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_INT_1</code>.
     */
    public void setRsInt_1(Integer value) {
        set(30, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_INT_1</code>.
     */
    public Integer getRsInt_1() {
        return (Integer) get(30);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_INT_2</code>.
     */
    public void setRsInt_2(Integer value) {
        set(31, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_INT_2</code>.
     */
    public Integer getRsInt_2() {
        return (Integer) get(31);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_INT_3</code>.
     */
    public void setRsInt_3(Integer value) {
        set(32, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_INT_3</code>.
     */
    public Integer getRsInt_3() {
        return (Integer) get(32);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_INT_4</code>.
     */
    public void setRsInt_4(Integer value) {
        set(33, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_INT_4</code>.
     */
    public Integer getRsInt_4() {
        return (Integer) get(33);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_INT_5</code>.
     */
    public void setRsInt_5(Integer value) {
        set(34, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_INT_5</code>.
     */
    public Integer getRsInt_5() {
        return (Integer) get(34);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_NUMBER_1</code>.
     */
    public void setRsNumber_1(String value) {
        set(35, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_NUMBER_1</code>.
     */
    public String getRsNumber_1() {
        return (String) get(35);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_NUMBER_2</code>.
     */
    public void setRsNumber_2(String value) {
        set(36, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_NUMBER_2</code>.
     */
    public String getRsNumber_2() {
        return (String) get(36);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_NUMBER_3</code>.
     */
    public void setRsNumber_3(String value) {
        set(37, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_NUMBER_3</code>.
     */
    public String getRsNumber_3() {
        return (String) get(37);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_NUMBER_4</code>.
     */
    public void setRsNumber_4(String value) {
        set(38, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_NUMBER_4</code>.
     */
    public String getRsNumber_4() {
        return (String) get(38);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_NUMBER_5</code>.
     */
    public void setRsNumber_5(String value) {
        set(39, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_NUMBER_5</code>.
     */
    public String getRsNumber_5() {
        return (String) get(39);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_1</code>.
     */
    public void setRsString_1(String value) {
        set(40, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_1</code>.
     */
    public String getRsString_1() {
        return (String) get(40);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_2</code>.
     */
    public void setRsString_2(String value) {
        set(41, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_2</code>.
     */
    public String getRsString_2() {
        return (String) get(41);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_3</code>.
     */
    public void setRsString_3(String value) {
        set(42, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_3</code>.
     */
    public String getRsString_3() {
        return (String) get(42);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_4</code>.
     */
    public void setRsString_4(String value) {
        set(43, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_4</code>.
     */
    public String getRsString_4() {
        return (String) get(43);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_5</code>.
     */
    public void setRsString_5(String value) {
        set(44, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_5</code>.
     */
    public String getRsString_5() {
        return (String) get(44);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_6</code>.
     */
    public void setRsString_6(String value) {
        set(45, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_6</code>.
     */
    public String getRsString_6() {
        return (String) get(45);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_7</code>.
     */
    public void setRsString_7(String value) {
        set(46, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_7</code>.
     */
    public String getRsString_7() {
        return (String) get(46);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_8</code>.
     */
    public void setRsString_8(String value) {
        set(47, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_8</code>.
     */
    public String getRsString_8() {
        return (String) get(47);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_9</code>.
     */
    public void setRsString_9(String value) {
        set(48, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_9</code>.
     */
    public String getRsString_9() {
        return (String) get(48);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_10</code>.
     */
    public void setRsString_10(String value) {
        set(49, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_10</code>.
     */
    public String getRsString_10() {
        return (String) get(49);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_11</code>.
     */
    public void setRsString_11(String value) {
        set(50, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_11</code>.
     */
    public String getRsString_11() {
        return (String) get(50);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_12</code>.
     */
    public void setRsString_12(String value) {
        set(51, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_12</code>.
     */
    public String getRsString_12() {
        return (String) get(51);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_13</code>.
     */
    public void setRsString_13(String value) {
        set(52, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_13</code>.
     */
    public String getRsString_13() {
        return (String) get(52);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_14</code>.
     */
    public void setRsString_14(String value) {
        set(53, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_14</code>.
     */
    public String getRsString_14() {
        return (String) get(53);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_15</code>.
     */
    public void setRsString_15(String value) {
        set(54, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_15</code>.
     */
    public String getRsString_15() {
        return (String) get(54);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_16</code>.
     */
    public void setRsString_16(String value) {
        set(55, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_16</code>.
     */
    public String getRsString_16() {
        return (String) get(55);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_17</code>.
     */
    public void setRsString_17(String value) {
        set(56, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_17</code>.
     */
    public String getRsString_17() {
        return (String) get(56);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_18</code>.
     */
    public void setRsString_18(String value) {
        set(57, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_18</code>.
     */
    public String getRsString_18() {
        return (String) get(57);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_19</code>.
     */
    public void setRsString_19(String value) {
        set(58, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_19</code>.
     */
    public String getRsString_19() {
        return (String) get(58);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_20</code>.
     */
    public void setRsString_20(String value) {
        set(59, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_20</code>.
     */
    public String getRsString_20() {
        return (String) get(59);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CODE_1</code>.
     */
    public void setRsCode_1(String value) {
        set(60, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CODE_1</code>.
     */
    public String getRsCode_1() {
        return (String) get(60);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CODE_2</code>.
     */
    public void setRsCode_2(String value) {
        set(61, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CODE_2</code>.
     */
    public String getRsCode_2() {
        return (String) get(61);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CODE_3</code>.
     */
    public void setRsCode_3(String value) {
        set(62, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CODE_3</code>.
     */
    public String getRsCode_3() {
        return (String) get(62);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CODE_4</code>.
     */
    public void setRsCode_4(String value) {
        set(63, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CODE_4</code>.
     */
    public String getRsCode_4() {
        return (String) get(63);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CODE_5</code>.
     */
    public void setRsCode_5(String value) {
        set(64, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CODE_5</code>.
     */
    public String getRsCode_5() {
        return (String) get(64);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CODE_6</code>.
     */
    public void setRsCode_6(String value) {
        set(65, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CODE_6</code>.
     */
    public String getRsCode_6() {
        return (String) get(65);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CODE_7</code>.
     */
    public void setRsCode_7(String value) {
        set(66, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CODE_7</code>.
     */
    public String getRsCode_7() {
        return (String) get(66);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CODE_8</code>.
     */
    public void setRsCode_8(String value) {
        set(67, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CODE_8</code>.
     */
    public String getRsCode_8() {
        return (String) get(67);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CODE_9</code>.
     */
    public void setRsCode_9(String value) {
        set(68, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CODE_9</code>.
     */
    public String getRsCode_9() {
        return (String) get(68);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CODE_10</code>.
     */
    public void setRsCode_10(String value) {
        set(69, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CODE_10</code>.
     */
    public String getRsCode_10() {
        return (String) get(69);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_MULTICODE_1</code>.
     */
    public void setRsMulticode_1(String value) {
        set(70, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_MULTICODE_1</code>.
     */
    public String getRsMulticode_1() {
        return (String) get(70);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_MULTICODE_2</code>.
     */
    public void setRsMulticode_2(String value) {
        set(71, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_MULTICODE_2</code>.
     */
    public String getRsMulticode_2() {
        return (String) get(71);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_MULTICODE_3</code>.
     */
    public void setRsMulticode_3(String value) {
        set(72, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_MULTICODE_3</code>.
     */
    public String getRsMulticode_3() {
        return (String) get(72);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CONCODE_1</code>.
     */
    public void setRsConcode_1(String value) {
        set(73, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CONCODE_1</code>.
     */
    public String getRsConcode_1() {
        return (String) get(73);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CONCODE_2</code>.
     */
    public void setRsConcode_2(String value) {
        set(74, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CONCODE_2</code>.
     */
    public String getRsConcode_2() {
        return (String) get(74);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CONCODE_3</code>.
     */
    public void setRsConcode_3(String value) {
        set(75, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CONCODE_3</code>.
     */
    public String getRsConcode_3() {
        return (String) get(75);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CSCODE_1</code>.
     */
    public void setRsCscode_1(String value) {
        set(76, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CSCODE_1</code>.
     */
    public String getRsCscode_1() {
        return (String) get(76);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CSCODE_2</code>.
     */
    public void setRsCscode_2(String value) {
        set(77, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CSCODE_2</code>.
     */
    public String getRsCscode_2() {
        return (String) get(77);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CSCODE_3</code>.
     */
    public void setRsCscode_3(String value) {
        set(78, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CSCODE_3</code>.
     */
    public String getRsCscode_3() {
        return (String) get(78);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_SYS_CUSTOM_ID</code>.
     */
    public void setCstSysCustomId(String value) {
        set(79, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_SYS_CUSTOM_ID</code>.
     */
    public String getCstSysCustomId() {
        return (String) get(79);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_SYS_UPLOAD_DATE</code>.
     */
    public void setCstSysUploadDate(Timestamp value) {
        set(80, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_SYS_UPLOAD_DATE</code>.
     */
    public Timestamp getCstSysUploadDate() {
        return (Timestamp) get(80);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_SYS_UPDATE_DATE</code>.
     */
    public void setCstSysUpdateDate(Timestamp value) {
        set(81, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_SYS_UPDATE_DATE</code>.
     */
    public Timestamp getCstSysUpdateDate() {
        return (Timestamp) get(81);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_SYS_GROUP_ID</code>.
     */
    public void setCstSysGroupId(Integer value) {
        set(82, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_SYS_GROUP_ID</code>.
     */
    public Integer getCstSysGroupId() {
        return (Integer) get(82);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_SYS_GROUP_TYPE</code>.
     */
    public void setCstSysGroupType(Integer value) {
        set(83, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_SYS_GROUP_TYPE</code>.
     */
    public Integer getCstSysGroupType() {
        return (Integer) get(83);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_SYS_RESULT_TYPE</code>.
     */
    public void setCstSysResultType(Integer value) {
        set(84, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_SYS_RESULT_TYPE</code>.
     */
    public Integer getCstSysResultType() {
        return (Integer) get(84);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_SYS_DAMDANG_ID</code>.
     */
    public void setCstSysDamdangId(String value) {
        set(85, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_SYS_DAMDANG_ID</code>.
     */
    public String getCstSysDamdangId() {
        return (String) get(85);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_SYS_RID_NUMBER</code>.
     */
    public void setCstSysRidNumber(String value) {
        set(86, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_SYS_RID_NUMBER</code>.
     */
    public String getCstSysRidNumber() {
        return (String) get(86);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_SYS_LAST_RESULT_ID</code>.
     */
    public void setCstSysLastResultId(Integer value) {
        set(87, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_SYS_LAST_RESULT_ID</code>.
     */
    public Integer getCstSysLastResultId() {
        return (Integer) get(87);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_SYS_LAST_RESULT_DATE</code>.
     */
    public void setCstSysLastResultDate(Timestamp value) {
        set(88, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_SYS_LAST_RESULT_DATE</code>.
     */
    public Timestamp getCstSysLastResultDate() {
        return (Timestamp) get(88);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_SYS_LAST_CALL_DATE</code>.
     */
    public void setCstSysLastCallDate(Timestamp value) {
        set(89, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_SYS_LAST_CALL_DATE</code>.
     */
    public Timestamp getCstSysLastCallDate() {
        return (Timestamp) get(89);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_SYS_LAST_HANGUP_CAUSE</code>.
     */
    public void setCstSysLastHangupCause(String value) {
        set(90, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_SYS_LAST_HANGUP_CAUSE</code>.
     */
    public String getCstSysLastHangupCause() {
        return (String) get(90);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_SYS_LAST_CALL_UNIQUEID</code>.
     */
    public void setCstSysLastCallUniqueid(String value) {
        set(91, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_SYS_LAST_CALL_UNIQUEID</code>.
     */
    public String getCstSysLastCallUniqueid() {
        return (String) get(91);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_SYS_COMPANY_ID</code>.
     */
    public void setCstSysCompanyId(String value) {
        set(92, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_SYS_COMPANY_ID</code>.
     */
    public String getCstSysCompanyId() {
        return (String) get(92);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_DATE_1</code>.
     */
    public void setCstDate_1(Date value) {
        set(93, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_DATE_1</code>.
     */
    public Date getCstDate_1() {
        return (Date) get(93);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_DATE_2</code>.
     */
    public void setCstDate_2(Date value) {
        set(94, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_DATE_2</code>.
     */
    public Date getCstDate_2() {
        return (Date) get(94);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_DATE_3</code>.
     */
    public void setCstDate_3(Date value) {
        set(95, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_DATE_3</code>.
     */
    public Date getCstDate_3() {
        return (Date) get(95);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_DAY_1</code>.
     */
    public void setCstDay_1(Date value) {
        set(96, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_DAY_1</code>.
     */
    public Date getCstDay_1() {
        return (Date) get(96);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_DAY_2</code>.
     */
    public void setCstDay_2(Date value) {
        set(97, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_DAY_2</code>.
     */
    public Date getCstDay_2() {
        return (Date) get(97);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_DAY_3</code>.
     */
    public void setCstDay_3(Date value) {
        set(98, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_DAY_3</code>.
     */
    public Date getCstDay_3() {
        return (Date) get(98);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_DATETIME_1</code>.
     */
    public void setCstDatetime_1(Timestamp value) {
        set(99, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_DATETIME_1</code>.
     */
    public Timestamp getCstDatetime_1() {
        return (Timestamp) get(99);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_DATETIME_2</code>.
     */
    public void setCstDatetime_2(Timestamp value) {
        set(100, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_DATETIME_2</code>.
     */
    public Timestamp getCstDatetime_2() {
        return (Timestamp) get(100);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_DATETIME_3</code>.
     */
    public void setCstDatetime_3(Timestamp value) {
        set(101, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_DATETIME_3</code>.
     */
    public Timestamp getCstDatetime_3() {
        return (Timestamp) get(101);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_INT_1</code>.
     */
    public void setCstInt_1(Integer value) {
        set(102, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_INT_1</code>.
     */
    public Integer getCstInt_1() {
        return (Integer) get(102);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_INT_2</code>.
     */
    public void setCstInt_2(Integer value) {
        set(103, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_INT_2</code>.
     */
    public Integer getCstInt_2() {
        return (Integer) get(103);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_INT_3</code>.
     */
    public void setCstInt_3(Integer value) {
        set(104, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_INT_3</code>.
     */
    public Integer getCstInt_3() {
        return (Integer) get(104);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_INT_4</code>.
     */
    public void setCstInt_4(Integer value) {
        set(105, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_INT_4</code>.
     */
    public Integer getCstInt_4() {
        return (Integer) get(105);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_INT_5</code>.
     */
    public void setCstInt_5(Integer value) {
        set(106, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_INT_5</code>.
     */
    public Integer getCstInt_5() {
        return (Integer) get(106);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_NUMBER_1</code>.
     */
    public void setCstNumber_1(String value) {
        set(107, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_NUMBER_1</code>.
     */
    public String getCstNumber_1() {
        return (String) get(107);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_NUMBER_2</code>.
     */
    public void setCstNumber_2(String value) {
        set(108, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_NUMBER_2</code>.
     */
    public String getCstNumber_2() {
        return (String) get(108);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_NUMBER_3</code>.
     */
    public void setCstNumber_3(String value) {
        set(109, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_NUMBER_3</code>.
     */
    public String getCstNumber_3() {
        return (String) get(109);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_NUMBER_4</code>.
     */
    public void setCstNumber_4(String value) {
        set(110, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_NUMBER_4</code>.
     */
    public String getCstNumber_4() {
        return (String) get(110);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_NUMBER_5</code>.
     */
    public void setCstNumber_5(String value) {
        set(111, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_NUMBER_5</code>.
     */
    public String getCstNumber_5() {
        return (String) get(111);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_1</code>.
     */
    public void setCstString_1(String value) {
        set(112, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_1</code>.
     */
    public String getCstString_1() {
        return (String) get(112);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_2</code>.
     */
    public void setCstString_2(String value) {
        set(113, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_2</code>.
     */
    public String getCstString_2() {
        return (String) get(113);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_3</code>.
     */
    public void setCstString_3(String value) {
        set(114, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_3</code>.
     */
    public String getCstString_3() {
        return (String) get(114);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_4</code>.
     */
    public void setCstString_4(String value) {
        set(115, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_4</code>.
     */
    public String getCstString_4() {
        return (String) get(115);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_5</code>.
     */
    public void setCstString_5(String value) {
        set(116, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_5</code>.
     */
    public String getCstString_5() {
        return (String) get(116);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_6</code>.
     */
    public void setCstString_6(String value) {
        set(117, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_6</code>.
     */
    public String getCstString_6() {
        return (String) get(117);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_7</code>.
     */
    public void setCstString_7(String value) {
        set(118, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_7</code>.
     */
    public String getCstString_7() {
        return (String) get(118);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_8</code>.
     */
    public void setCstString_8(String value) {
        set(119, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_8</code>.
     */
    public String getCstString_8() {
        return (String) get(119);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_9</code>.
     */
    public void setCstString_9(String value) {
        set(120, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_9</code>.
     */
    public String getCstString_9() {
        return (String) get(120);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_10</code>.
     */
    public void setCstString_10(String value) {
        set(121, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_10</code>.
     */
    public String getCstString_10() {
        return (String) get(121);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_11</code>.
     */
    public void setCstString_11(String value) {
        set(122, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_11</code>.
     */
    public String getCstString_11() {
        return (String) get(122);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_12</code>.
     */
    public void setCstString_12(String value) {
        set(123, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_12</code>.
     */
    public String getCstString_12() {
        return (String) get(123);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_13</code>.
     */
    public void setCstString_13(String value) {
        set(124, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_13</code>.
     */
    public String getCstString_13() {
        return (String) get(124);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_14</code>.
     */
    public void setCstString_14(String value) {
        set(125, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_14</code>.
     */
    public String getCstString_14() {
        return (String) get(125);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_15</code>.
     */
    public void setCstString_15(String value) {
        set(126, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_15</code>.
     */
    public String getCstString_15() {
        return (String) get(126);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_16</code>.
     */
    public void setCstString_16(String value) {
        set(127, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_16</code>.
     */
    public String getCstString_16() {
        return (String) get(127);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_17</code>.
     */
    public void setCstString_17(String value) {
        set(128, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_17</code>.
     */
    public String getCstString_17() {
        return (String) get(128);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_18</code>.
     */
    public void setCstString_18(String value) {
        set(129, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_18</code>.
     */
    public String getCstString_18() {
        return (String) get(129);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_19</code>.
     */
    public void setCstString_19(String value) {
        set(130, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_19</code>.
     */
    public String getCstString_19() {
        return (String) get(130);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_20</code>.
     */
    public void setCstString_20(String value) {
        set(131, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_20</code>.
     */
    public String getCstString_20() {
        return (String) get(131);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CODE_1</code>.
     */
    public void setCstCode_1(String value) {
        set(132, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CODE_1</code>.
     */
    public String getCstCode_1() {
        return (String) get(132);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CODE_2</code>.
     */
    public void setCstCode_2(String value) {
        set(133, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CODE_2</code>.
     */
    public String getCstCode_2() {
        return (String) get(133);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CODE_3</code>.
     */
    public void setCstCode_3(String value) {
        set(134, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CODE_3</code>.
     */
    public String getCstCode_3() {
        return (String) get(134);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CODE_4</code>.
     */
    public void setCstCode_4(String value) {
        set(135, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CODE_4</code>.
     */
    public String getCstCode_4() {
        return (String) get(135);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CODE_5</code>.
     */
    public void setCstCode_5(String value) {
        set(136, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CODE_5</code>.
     */
    public String getCstCode_5() {
        return (String) get(136);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CODE_6</code>.
     */
    public void setCstCode_6(String value) {
        set(137, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CODE_6</code>.
     */
    public String getCstCode_6() {
        return (String) get(137);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CODE_7</code>.
     */
    public void setCstCode_7(String value) {
        set(138, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CODE_7</code>.
     */
    public String getCstCode_7() {
        return (String) get(138);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CODE_8</code>.
     */
    public void setCstCode_8(String value) {
        set(139, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CODE_8</code>.
     */
    public String getCstCode_8() {
        return (String) get(139);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CODE_9</code>.
     */
    public void setCstCode_9(String value) {
        set(140, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CODE_9</code>.
     */
    public String getCstCode_9() {
        return (String) get(140);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CODE_10</code>.
     */
    public void setCstCode_10(String value) {
        set(141, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CODE_10</code>.
     */
    public String getCstCode_10() {
        return (String) get(141);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_MULTICODE_1</code>.
     */
    public void setCstMulticode_1(String value) {
        set(142, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_MULTICODE_1</code>.
     */
    public String getCstMulticode_1() {
        return (String) get(142);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_MULTICODE_2</code>.
     */
    public void setCstMulticode_2(String value) {
        set(143, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_MULTICODE_2</code>.
     */
    public String getCstMulticode_2() {
        return (String) get(143);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_MULTICODE_3</code>.
     */
    public void setCstMulticode_3(String value) {
        set(144, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_MULTICODE_3</code>.
     */
    public String getCstMulticode_3() {
        return (String) get(144);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CONCODE_1</code>.
     */
    public void setCstConcode_1(String value) {
        set(145, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CONCODE_1</code>.
     */
    public String getCstConcode_1() {
        return (String) get(145);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CONCODE_2</code>.
     */
    public void setCstConcode_2(String value) {
        set(146, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CONCODE_2</code>.
     */
    public String getCstConcode_2() {
        return (String) get(146);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CONCODE_3</code>.
     */
    public void setCstConcode_3(String value) {
        set(147, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CONCODE_3</code>.
     */
    public String getCstConcode_3() {
        return (String) get(147);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CSCODE_1</code>.
     */
    public void setCstCscode_1(String value) {
        set(148, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CSCODE_1</code>.
     */
    public String getCstCscode_1() {
        return (String) get(148);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CSCODE_2</code>.
     */
    public void setCstCscode_2(String value) {
        set(149, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CSCODE_2</code>.
     */
    public String getCstCscode_2() {
        return (String) get(149);
    }

    /**
     * Setter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CSCODE_3</code>.
     */
    public void setCstCscode_3(String value) {
        set(150, value);
    }

    /**
     * Getter for <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CSCODE_3</code>.
     */
    public String getCstCscode_3() {
        return (String) get(150);
    }
}
