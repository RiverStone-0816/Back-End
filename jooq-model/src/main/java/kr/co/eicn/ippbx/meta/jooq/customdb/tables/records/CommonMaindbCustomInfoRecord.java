package kr.co.eicn.ippbx.meta.jooq.customdb.tables.records;

import org.jooq.Record1;
import org.jooq.Table;
import org.jooq.impl.UpdatableRecordImpl;

import java.sql.Date;
import java.sql.Timestamp;

public class CommonMaindbCustomInfoRecord extends UpdatableRecordImpl<CommonMaindbCustomInfoRecord> {

    public CommonMaindbCustomInfoRecord(Table<CommonMaindbCustomInfoRecord> table) {
        super(table);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_SYS_CUSTOM_ID</code>.
     */
    public void setMaindbSysCustomId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_SYS_CUSTOM_ID</code>.
     */
    public String getMaindbSysCustomId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_SYS_UPLOAD_DATE</code>.
     */
    public void setMaindbSysUploadDate(Timestamp value) {
        set(1, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_SYS_UPLOAD_DATE</code>.
     */
    public Timestamp getMaindbSysUploadDate() {
        return (Timestamp) get(1);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_SYS_UPDATE_DATE</code>.
     */
    public void setMaindbSysUpdateDate(Timestamp value) {
        set(2, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_SYS_UPDATE_DATE</code>.
     */
    public Timestamp getMaindbSysUpdateDate() {
        return (Timestamp) get(2);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_SYS_GROUP_ID</code>.
     */
    public void setMaindbSysGroupId(Integer value) {
        set(3, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_SYS_GROUP_ID</code>.
     */
    public Integer getMaindbSysGroupId() {
        return (Integer) get(3);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_SYS_GROUP_TYPE</code>.
     */
    public void setMaindbSysGroupType(Integer value) {
        set(4, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_SYS_GROUP_TYPE</code>.
     */
    public Integer getMaindbSysGroupType() {
        return (Integer) get(4);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_SYS_RESULT_TYPE</code>.
     */
    public void setMaindbSysResultType(Integer value) {
        set(5, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_SYS_RESULT_TYPE</code>.
     */
    public Integer getMaindbSysResultType() {
        return (Integer) get(5);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_SYS_DAMDANG_ID</code>.
     */
    public void setMaindbSysDamdangId(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_SYS_DAMDANG_ID</code>.
     */
    public String getMaindbSysDamdangId() {
        return (String) get(6);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_SYS_LAST_RESULT_ID</code>.
     */
    public void setMaindbSysLastResultId(Integer value) {
        set(7, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_SYS_LAST_RESULT_ID</code>.
     */
    public Integer getMaindbSysLastResultId() {
        return (Integer) get(7);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_SYS_LAST_RESULT_DATE</code>.
     */
    public void setMaindbSysLastResultDate(Timestamp value) {
        set(8, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_SYS_LAST_RESULT_DATE</code>.
     */
    public Timestamp getMaindbSysLastResultDate() {
        return (Timestamp) get(8);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_SYS_COMPANY_ID</code>.
     */
    public void setMaindbSysCompanyId(String value) {
        set(9, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_SYS_COMPANY_ID</code>.
     */
    public String getMaindbSysCompanyId() {
        return (String) get(9);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_DATE_1</code>.
     */
    public void setMaindbDate_1(Date value) {
        set(10, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_DATE_1</code>.
     */
    public Date getMaindbDate_1() {
        return (Date) get(10);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_DATE_2</code>.
     */
    public void setMaindbDate_2(Date value) {
        set(11, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_DATE_2</code>.
     */
    public Date getMaindbDate_2() {
        return (Date) get(11);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_DATE_3</code>.
     */
    public void setMaindbDate_3(Date value) {
        set(12, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_DATE_3</code>.
     */
    public Date getMaindbDate_3() {
        return (Date) get(12);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_DAY_1</code>.
     */
    public void setMaindbDay_1(Date value) {
        set(13, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_DAY_1</code>.
     */
    public Date getMaindbDay_1() {
        return (Date) get(13);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_DAY_2</code>.
     */
    public void setMaindbDay_2(Date value) {
        set(14, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_DAY_2</code>.
     */
    public Date getMaindbDay_2() {
        return (Date) get(14);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_DAY_3</code>.
     */
    public void setMaindbDay_3(Date value) {
        set(15, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_DAY_3</code>.
     */
    public Date getMaindbDay_3() {
        return (Date) get(15);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_DATETIME_1</code>.
     */
    public void setMaindbDatetime_1(Timestamp value) {
        set(16, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_DATETIME_1</code>.
     */
    public Timestamp getMaindbDatetime_1() {
        return (Timestamp) get(16);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_DATETIME_2</code>.
     */
    public void setMaindbDatetime_2(Timestamp value) {
        set(17, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_DATETIME_2</code>.
     */
    public Timestamp getMaindbDatetime_2() {
        return (Timestamp) get(17);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_DATETIME_3</code>.
     */
    public void setMaindbDatetime_3(Timestamp value) {
        set(18, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_DATETIME_3</code>.
     */
    public Timestamp getMaindbDatetime_3() {
        return (Timestamp) get(18);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_INT_1</code>.
     */
    public void setMaindbInt_1(Integer value) {
        set(19, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_INT_1</code>.
     */
    public Integer getMaindbInt_1() {
        return (Integer) get(19);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_INT_2</code>.
     */
    public void setMaindbInt_2(Integer value) {
        set(20, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_INT_2</code>.
     */
    public Integer getMaindbInt_2() {
        return (Integer) get(20);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_INT_3</code>.
     */
    public void setMaindbInt_3(Integer value) {
        set(21, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_INT_3</code>.
     */
    public Integer getMaindbInt_3() {
        return (Integer) get(21);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_INT_4</code>.
     */
    public void setMaindbInt_4(Integer value) {
        set(22, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_INT_4</code>.
     */
    public Integer getMaindbInt_4() {
        return (Integer) get(22);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_INT_5</code>.
     */
    public void setMaindbInt_5(Integer value) {
        set(23, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_INT_5</code>.
     */
    public Integer getMaindbInt_5() {
        return (Integer) get(23);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_1</code>.
     */
    public void setMaindbString_1(String value) {
        set(24, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_1</code>.
     */
    public String getMaindbString_1() {
        return (String) get(24);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_2</code>.
     */
    public void setMaindbString_2(String value) {
        set(25, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_2</code>.
     */
    public String getMaindbString_2() {
        return (String) get(25);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_3</code>.
     */
    public void setMaindbString_3(String value) {
        set(26, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_3</code>.
     */
    public String getMaindbString_3() {
        return (String) get(26);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_4</code>.
     */
    public void setMaindbString_4(String value) {
        set(27, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_4</code>.
     */
    public String getMaindbString_4() {
        return (String) get(27);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_5</code>.
     */
    public void setMaindbString_5(String value) {
        set(28, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_5</code>.
     */
    public String getMaindbString_5() {
        return (String) get(28);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_6</code>.
     */
    public void setMaindbString_6(String value) {
        set(29, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_6</code>.
     */
    public String getMaindbString_6() {
        return (String) get(29);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_7</code>.
     */
    public void setMaindbString_7(String value) {
        set(30, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_7</code>.
     */
    public String getMaindbString_7() {
        return (String) get(30);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_8</code>.
     */
    public void setMaindbString_8(String value) {
        set(31, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_8</code>.
     */
    public String getMaindbString_8() {
        return (String) get(31);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_9</code>.
     */
    public void setMaindbString_9(String value) {
        set(32, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_9</code>.
     */
    public String getMaindbString_9() {
        return (String) get(32);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_10</code>.
     */
    public void setMaindbString_10(String value) {
        set(33, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_10</code>.
     */
    public String getMaindbString_10() {
        return (String) get(33);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_11</code>.
     */
    public void setMaindbString_11(String value) {
        set(34, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_11</code>.
     */
    public String getMaindbString_11() {
        return (String) get(34);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_12</code>.
     */
    public void setMaindbString_12(String value) {
        set(35, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_12</code>.
     */
    public String getMaindbString_12() {
        return (String) get(35);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_13</code>.
     */
    public void setMaindbString_13(String value) {
        set(36, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_13</code>.
     */
    public String getMaindbString_13() {
        return (String) get(36);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_14</code>.
     */
    public void setMaindbString_14(String value) {
        set(37, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_14</code>.
     */
    public String getMaindbString_14() {
        return (String) get(37);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_15</code>.
     */
    public void setMaindbString_15(String value) {
        set(38, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_15</code>.
     */
    public String getMaindbString_15() {
        return (String) get(38);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_16</code>.
     */
    public void setMaindbString_16(String value) {
        set(39, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_16</code>.
     */
    public String getMaindbString_16() {
        return (String) get(39);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_17</code>.
     */
    public void setMaindbString_17(String value) {
        set(40, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_17</code>.
     */
    public String getMaindbString_17() {
        return (String) get(40);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_18</code>.
     */
    public void setMaindbString_18(String value) {
        set(41, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_18</code>.
     */
    public String getMaindbString_18() {
        return (String) get(41);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_19</code>.
     */
    public void setMaindbString_19(String value) {
        set(42, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_19</code>.
     */
    public String getMaindbString_19() {
        return (String) get(42);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_20</code>.
     */
    public void setMaindbString_20(String value) {
        set(43, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_20</code>.
     */
    public String getMaindbString_20() {
        return (String) get(43);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_CODE_1</code>.
     */
    public void setMaindbCode_1(String value) {
        set(44, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_CODE_1</code>.
     */
    public String getMaindbCode_1() {
        return (String) get(44);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_CODE_2</code>.
     */
    public void setMaindbCode_2(String value) {
        set(45, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_CODE_2</code>.
     */
    public String getMaindbCode_2() {
        return (String) get(45);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_CODE_3</code>.
     */
    public void setMaindbCode_3(String value) {
        set(46, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_CODE_3</code>.
     */
    public String getMaindbCode_3() {
        return (String) get(46);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_CODE_4</code>.
     */
    public void setMaindbCode_4(String value) {
        set(47, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_CODE_4</code>.
     */
    public String getMaindbCode_4() {
        return (String) get(47);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_CODE_5</code>.
     */
    public void setMaindbCode_5(String value) {
        set(48, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_CODE_5</code>.
     */
    public String getMaindbCode_5() {
        return (String) get(48);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_CODE_6</code>.
     */
    public void setMaindbCode_6(String value) {
        set(49, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_CODE_6</code>.
     */
    public String getMaindbCode_6() {
        return (String) get(49);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_CODE_7</code>.
     */
    public void setMaindbCode_7(String value) {
        set(50, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_CODE_7</code>.
     */
    public String getMaindbCode_7() {
        return (String) get(50);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_CODE_8</code>.
     */
    public void setMaindbCode_8(String value) {
        set(51, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_CODE_8</code>.
     */
    public String getMaindbCode_8() {
        return (String) get(51);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_CODE_9</code>.
     */
    public void setMaindbCode_9(String value) {
        set(52, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_CODE_9</code>.
     */
    public String getMaindbCode_9() {
        return (String) get(52);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_CODE_10</code>.
     */
    public void setMaindbCode_10(String value) {
        set(53, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_CODE_10</code>.
     */
    public String getMaindbCode_10() {
        return (String) get(53);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_MULTICODE_1</code>.
     */
    public void setMaindbMulticode_1(String value) {
        set(54, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_MULTICODE_1</code>.
     */
    public String getMaindbMulticode_1() {
        return (String) get(54);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_MULTICODE_2</code>.
     */
    public void setMaindbMulticode_2(String value) {
        set(55, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_MULTICODE_2</code>.
     */
    public String getMaindbMulticode_2() {
        return (String) get(55);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_MULTICODE_3</code>.
     */
    public void setMaindbMulticode_3(String value) {
        set(56, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_MULTICODE_3</code>.
     */
    public String getMaindbMulticode_3() {
        return (String) get(56);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_IMG_1</code>.
     */
    public void setMaindbImg_1(String value) {
        set(57, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_IMG_1</code>.
     */
    public String getMaindbImg_1() {
        return (String) get(57);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_IMG_2</code>.
     */
    public void setMaindbImg_2(String value) {
        set(58, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_IMG_2</code>.
     */
    public String getMaindbImg_2() {
        return (String) get(58);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_IMG_3</code>.
     */
    public void setMaindbImg_3(String value) {
        set(59, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_IMG_3</code>.
     */
    public String getMaindbImg_3() {
        return (String) get(59);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_CONCODE_1</code>.
     */
    public void setMaindbConcode_1(String value) {
        set(60, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_CONCODE_1</code>.
     */
    public String getMaindbConcode_1() {
        return (String) get(60);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_CONCODE_2</code>.
     */
    public void setMaindbConcode_2(String value) {
        set(61, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_CONCODE_2</code>.
     */
    public String getMaindbConcode_2() {
        return (String) get(61);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_CONCODE_3</code>.
     */
    public void setMaindbConcode_3(String value) {
        set(62, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_CONCODE_3</code>.
     */
    public String getMaindbConcode_3() {
        return (String) get(62);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_CSCODE_1</code>.
     */
    public void setMaindbCscode_1(String value) {
        set(63, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_CSCODE_1</code>.
     */
    public String getMaindbCscode_1() {
        return (String) get(63);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_CSCODE_2</code>.
     */
    public void setMaindbCscode_2(String value) {
        set(64, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_CSCODE_2</code>.
     */
    public String getMaindbCscode_2() {
        return (String) get(64);
    }

    /**
     * Setter for <code>CUSTOMDB.maindb_custom_info.MAINDB_CSCODE_3</code>.
     */
    public void setMaindbCscode_3(String value) {
        set(65, value);
    }

    /**
     * Getter for <code>CUSTOMDB.maindb_custom_info.MAINDB_CSCODE_3</code>.
     */
    public String getMaindbCscode_3() {
        return (String) get(65);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }
}
