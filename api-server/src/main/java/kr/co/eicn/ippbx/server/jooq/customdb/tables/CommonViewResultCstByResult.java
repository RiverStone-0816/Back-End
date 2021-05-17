package kr.co.eicn.ippbx.server.jooq.customdb.tables;

import kr.co.eicn.ippbx.server.jooq.customdb.Customdb;
import kr.co.eicn.ippbx.server.jooq.customdb.tables.records.CommonViewResultCstByResultRecord;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import java.sql.Date;
import java.sql.Timestamp;

public class CommonViewResultCstByResult extends TableImpl<CommonViewResultCstByResultRecord> {
    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.seq</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Integer> SEQ = createField(DSL.name("seq"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.company_id</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> COMPANY_ID = createField(DSL.name("company_id"), org.jooq.impl.SQLDataType.VARCHAR(30).nullable(false).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.result_date</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Timestamp> RESULT_DATE = createField(DSL.name("result_date"), org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(org.jooq.impl.DSL.inline("2010-01-01 00:00:00", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.update_date</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Timestamp> UPDATE_DATE = createField(DSL.name("update_date"), org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(org.jooq.impl.DSL.inline("2010-01-01 00:00:00", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.uniqueid</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> UNIQUEID = createField(DSL.name("uniqueid"), org.jooq.impl.SQLDataType.VARCHAR(50).nullable(false).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.hangup_cause</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> HANGUP_CAUSE = createField(DSL.name("hangup_cause"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.hangup_msg</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> HANGUP_MSG = createField(DSL.name("hangup_msg"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.billsec</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Integer> BILLSEC = createField(DSL.name("billsec"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.custom_id</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CUSTOM_ID = createField(DSL.name("custom_id"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.click_key</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CLICK_KEY = createField(DSL.name("click_key"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.custom_number</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CUSTOM_NUMBER = createField(DSL.name("custom_number"), org.jooq.impl.SQLDataType.VARCHAR(50).nullable(false).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.execute_id</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> EXECUTE_ID = createField(DSL.name("execute_id"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.group_id</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Integer> GROUP_ID = createField(DSL.name("group_id"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.userid</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> USERID = createField(DSL.name("userid"), org.jooq.impl.SQLDataType.VARCHAR(30).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.userid_org</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> USERID_ORG = createField(DSL.name("userid_org"), org.jooq.impl.SQLDataType.VARCHAR(30).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.userid_tr</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> USERID_TR = createField(DSL.name("userid_tr"), org.jooq.impl.SQLDataType.VARCHAR(30).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.result_type</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Integer> RESULT_TYPE = createField(DSL.name("result_type"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.call_type</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CALL_TYPE = createField(DSL.name("call_type"), org.jooq.impl.SQLDataType.VARCHAR(5).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.from_org</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> FROM_ORG = createField(DSL.name("from_org"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.group_kind</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> GROUP_KIND = createField(DSL.name("group_kind"), org.jooq.impl.SQLDataType.VARCHAR(10).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.group_type</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Integer> GROUP_TYPE = createField(DSL.name("group_type"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_DATE_1</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Date> RS_DATE_1 = createField(DSL.name("RS_DATE_1"), org.jooq.impl.SQLDataType.DATE.defaultValue(org.jooq.impl.DSL.inline("2010-01-01", org.jooq.impl.SQLDataType.DATE)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_DATE_2</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Date> RS_DATE_2 = createField(DSL.name("RS_DATE_2"), org.jooq.impl.SQLDataType.DATE.defaultValue(org.jooq.impl.DSL.inline("2010-01-01", org.jooq.impl.SQLDataType.DATE)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_DATE_3</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Date> RS_DATE_3 = createField(DSL.name("RS_DATE_3"), org.jooq.impl.SQLDataType.DATE.defaultValue(org.jooq.impl.DSL.inline("2010-01-01", org.jooq.impl.SQLDataType.DATE)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_DAY_1</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Date> RS_DAY_1 = createField(DSL.name("RS_DAY_1"), org.jooq.impl.SQLDataType.DATE.defaultValue(org.jooq.impl.DSL.inline("2010-01-01", org.jooq.impl.SQLDataType.DATE)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_DAY_2</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Date> RS_DAY_2 = createField(DSL.name("RS_DAY_2"), org.jooq.impl.SQLDataType.DATE.defaultValue(org.jooq.impl.DSL.inline("2010-01-01", org.jooq.impl.SQLDataType.DATE)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_DAY_3</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Date> RS_DAY_3 = createField(DSL.name("RS_DAY_3"), org.jooq.impl.SQLDataType.DATE.defaultValue(org.jooq.impl.DSL.inline("2010-01-01", org.jooq.impl.SQLDataType.DATE)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_DATETIME_1</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Timestamp> RS_DATETIME_1 = createField(DSL.name("RS_DATETIME_1"), org.jooq.impl.SQLDataType.TIMESTAMP.defaultValue(org.jooq.impl.DSL.inline("2010-01-01 00:00:00", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_DATETIME_2</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Timestamp> RS_DATETIME_2 = createField(DSL.name("RS_DATETIME_2"), org.jooq.impl.SQLDataType.TIMESTAMP.defaultValue(org.jooq.impl.DSL.inline("2010-01-01 00:00:00", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_DATETIME_3</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Timestamp> RS_DATETIME_3 = createField(DSL.name("RS_DATETIME_3"), org.jooq.impl.SQLDataType.TIMESTAMP.defaultValue(org.jooq.impl.DSL.inline("2010-01-01 00:00:00", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_INT_1</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Integer> RS_INT_1 = createField(DSL.name("RS_INT_1"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_INT_2</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Integer> RS_INT_2 = createField(DSL.name("RS_INT_2"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_INT_3</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Integer> RS_INT_3 = createField(DSL.name("RS_INT_3"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_INT_4</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Integer> RS_INT_4 = createField(DSL.name("RS_INT_4"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_INT_5</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Integer> RS_INT_5 = createField(DSL.name("RS_INT_5"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_NUMBER_1</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_NUMBER_1 = createField(DSL.name("RS_NUMBER_1"), org.jooq.impl.SQLDataType.VARCHAR(15).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_NUMBER_2</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_NUMBER_2 = createField(DSL.name("RS_NUMBER_2"), org.jooq.impl.SQLDataType.VARCHAR(15).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_NUMBER_3</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_NUMBER_3 = createField(DSL.name("RS_NUMBER_3"), org.jooq.impl.SQLDataType.VARCHAR(15).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_NUMBER_4</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_NUMBER_4 = createField(DSL.name("RS_NUMBER_4"), org.jooq.impl.SQLDataType.VARCHAR(15).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_NUMBER_5</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_NUMBER_5 = createField(DSL.name("RS_NUMBER_5"), org.jooq.impl.SQLDataType.VARCHAR(15).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_1</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_STRING_1 = createField(DSL.name("RS_STRING_1"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_2</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_STRING_2 = createField(DSL.name("RS_STRING_2"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_3</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_STRING_3 = createField(DSL.name("RS_STRING_3"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_4</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_STRING_4 = createField(DSL.name("RS_STRING_4"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_5</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_STRING_5 = createField(DSL.name("RS_STRING_5"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_6</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_STRING_6 = createField(DSL.name("RS_STRING_6"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_7</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_STRING_7 = createField(DSL.name("RS_STRING_7"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_8</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_STRING_8 = createField(DSL.name("RS_STRING_8"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_9</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_STRING_9 = createField(DSL.name("RS_STRING_9"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_10</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_STRING_10 = createField(DSL.name("RS_STRING_10"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_11</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_STRING_11 = createField(DSL.name("RS_STRING_11"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_12</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_STRING_12 = createField(DSL.name("RS_STRING_12"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_13</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_STRING_13 = createField(DSL.name("RS_STRING_13"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_14</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_STRING_14 = createField(DSL.name("RS_STRING_14"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_15</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_STRING_15 = createField(DSL.name("RS_STRING_15"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_16</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_STRING_16 = createField(DSL.name("RS_STRING_16"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_17</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_STRING_17 = createField(DSL.name("RS_STRING_17"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_18</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_STRING_18 = createField(DSL.name("RS_STRING_18"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_19</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_STRING_19 = createField(DSL.name("RS_STRING_19"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_STRING_20</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_STRING_20 = createField(DSL.name("RS_STRING_20"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CODE_1</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_CODE_1 = createField(DSL.name("RS_CODE_1"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CODE_2</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_CODE_2 = createField(DSL.name("RS_CODE_2"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CODE_3</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_CODE_3 = createField(DSL.name("RS_CODE_3"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CODE_4</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_CODE_4 = createField(DSL.name("RS_CODE_4"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CODE_5</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_CODE_5 = createField(DSL.name("RS_CODE_5"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CODE_6</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_CODE_6 = createField(DSL.name("RS_CODE_6"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CODE_7</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_CODE_7 = createField(DSL.name("RS_CODE_7"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CODE_8</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_CODE_8 = createField(DSL.name("RS_CODE_8"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CODE_9</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_CODE_9 = createField(DSL.name("RS_CODE_9"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CODE_10</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_CODE_10 = createField(DSL.name("RS_CODE_10"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_MULTICODE_1</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_MULTICODE_1 = createField(DSL.name("RS_MULTICODE_1"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_MULTICODE_2</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_MULTICODE_2 = createField(DSL.name("RS_MULTICODE_2"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_MULTICODE_3</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_MULTICODE_3 = createField(DSL.name("RS_MULTICODE_3"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CONCODE_1</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_CONCODE_1 = createField(DSL.name("RS_CONCODE_1"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CONCODE_2</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_CONCODE_2 = createField(DSL.name("RS_CONCODE_2"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CONCODE_3</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_CONCODE_3 = createField(DSL.name("RS_CONCODE_3"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CSCODE_1</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_CSCODE_1 = createField(DSL.name("RS_CSCODE_1"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CSCODE_2</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_CSCODE_2 = createField(DSL.name("RS_CSCODE_2"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.RS_CSCODE_3</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> RS_CSCODE_3 = createField(DSL.name("RS_CSCODE_3"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_SYS_CUSTOM_ID</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_SYS_CUSTOM_ID = createField(DSL.name("CST_SYS_CUSTOM_ID"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_SYS_UPLOAD_DATE</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Timestamp> CST_SYS_UPLOAD_DATE = createField(DSL.name("CST_SYS_UPLOAD_DATE"), org.jooq.impl.SQLDataType.TIMESTAMP.defaultValue(org.jooq.impl.DSL.inline("2010-01-01 00:00:00", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_SYS_UPDATE_DATE</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Timestamp> CST_SYS_UPDATE_DATE = createField(DSL.name("CST_SYS_UPDATE_DATE"), org.jooq.impl.SQLDataType.TIMESTAMP.defaultValue(org.jooq.impl.DSL.inline("2010-01-01 00:00:00", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_SYS_GROUP_ID</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Integer> CST_SYS_GROUP_ID = createField(DSL.name("CST_SYS_GROUP_ID"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_SYS_GROUP_TYPE</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Integer> CST_SYS_GROUP_TYPE = createField(DSL.name("CST_SYS_GROUP_TYPE"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_SYS_RESULT_TYPE</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Integer> CST_SYS_RESULT_TYPE = createField(DSL.name("CST_SYS_RESULT_TYPE"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_SYS_DAMDANG_ID</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_SYS_DAMDANG_ID = createField(DSL.name("CST_SYS_DAMDANG_ID"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_SYS_RID_NUMBER</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_SYS_RID_NUMBER = createField(DSL.name("CST_SYS_RID_NUMBER"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_SYS_LAST_RESULT_ID</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Integer> CST_SYS_LAST_RESULT_ID = createField(DSL.name("CST_SYS_LAST_RESULT_ID"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_SYS_LAST_RESULT_DATE</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Timestamp> CST_SYS_LAST_RESULT_DATE = createField(DSL.name("CST_SYS_LAST_RESULT_DATE"), org.jooq.impl.SQLDataType.TIMESTAMP.defaultValue(org.jooq.impl.DSL.inline("2010-01-01 00:00:00", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_SYS_LAST_CALL_DATE</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Timestamp> CST_SYS_LAST_CALL_DATE = createField(DSL.name("CST_SYS_LAST_CALL_DATE"), org.jooq.impl.SQLDataType.TIMESTAMP.defaultValue(org.jooq.impl.DSL.inline("2010-01-01 00:00:00", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_SYS_LAST_HANGUP_CAUSE</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_SYS_LAST_HANGUP_CAUSE = createField(DSL.name("CST_SYS_LAST_HANGUP_CAUSE"), org.jooq.impl.SQLDataType.VARCHAR(30).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_SYS_LAST_CALL_UNIQUEID</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_SYS_LAST_CALL_UNIQUEID = createField(DSL.name("CST_SYS_LAST_CALL_UNIQUEID"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_SYS_COMPANY_ID</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_SYS_COMPANY_ID = createField(DSL.name("CST_SYS_COMPANY_ID"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_DATE_1</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Date> CST_DATE_1 = createField(DSL.name("CST_DATE_1"), org.jooq.impl.SQLDataType.DATE.defaultValue(org.jooq.impl.DSL.inline("2010-01-01", org.jooq.impl.SQLDataType.DATE)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_DATE_2</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Date> CST_DATE_2 = createField(DSL.name("CST_DATE_2"), org.jooq.impl.SQLDataType.DATE.defaultValue(org.jooq.impl.DSL.inline("2010-01-01", org.jooq.impl.SQLDataType.DATE)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_DATE_3</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Date> CST_DATE_3 = createField(DSL.name("CST_DATE_3"), org.jooq.impl.SQLDataType.DATE.defaultValue(org.jooq.impl.DSL.inline("2010-01-01", org.jooq.impl.SQLDataType.DATE)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_DAY_1</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Date> CST_DAY_1 = createField(DSL.name("CST_DAY_1"), org.jooq.impl.SQLDataType.DATE.defaultValue(org.jooq.impl.DSL.inline("2010-01-01", org.jooq.impl.SQLDataType.DATE)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_DAY_2</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Date> CST_DAY_2 = createField(DSL.name("CST_DAY_2"), org.jooq.impl.SQLDataType.DATE.defaultValue(org.jooq.impl.DSL.inline("2010-01-01", org.jooq.impl.SQLDataType.DATE)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_DAY_3</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Date> CST_DAY_3 = createField(DSL.name("CST_DAY_3"), org.jooq.impl.SQLDataType.DATE.defaultValue(org.jooq.impl.DSL.inline("2010-01-01", org.jooq.impl.SQLDataType.DATE)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_DATETIME_1</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Timestamp> CST_DATETIME_1 = createField(DSL.name("CST_DATETIME_1"), org.jooq.impl.SQLDataType.TIMESTAMP.defaultValue(org.jooq.impl.DSL.inline("2010-01-01 00:00:00", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_DATETIME_2</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Timestamp> CST_DATETIME_2 = createField(DSL.name("CST_DATETIME_2"), org.jooq.impl.SQLDataType.TIMESTAMP.defaultValue(org.jooq.impl.DSL.inline("2010-01-01 00:00:00", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_DATETIME_3</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Timestamp> CST_DATETIME_3 = createField(DSL.name("CST_DATETIME_3"), org.jooq.impl.SQLDataType.TIMESTAMP.defaultValue(org.jooq.impl.DSL.inline("2010-01-01 00:00:00", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_INT_1</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Integer> CST_INT_1 = createField(DSL.name("CST_INT_1"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_INT_2</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Integer> CST_INT_2 = createField(DSL.name("CST_INT_2"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_INT_3</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Integer> CST_INT_3 = createField(DSL.name("CST_INT_3"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_INT_4</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Integer> CST_INT_4 = createField(DSL.name("CST_INT_4"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_INT_5</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, Integer> CST_INT_5 = createField(DSL.name("CST_INT_5"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_NUMBER_1</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_NUMBER_1 = createField(DSL.name("CST_NUMBER_1"), org.jooq.impl.SQLDataType.VARCHAR(15).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_NUMBER_2</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_NUMBER_2 = createField(DSL.name("CST_NUMBER_2"), org.jooq.impl.SQLDataType.VARCHAR(15).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_NUMBER_3</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_NUMBER_3 = createField(DSL.name("CST_NUMBER_3"), org.jooq.impl.SQLDataType.VARCHAR(15).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_NUMBER_4</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_NUMBER_4 = createField(DSL.name("CST_NUMBER_4"), org.jooq.impl.SQLDataType.VARCHAR(15).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_NUMBER_5</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_NUMBER_5 = createField(DSL.name("CST_NUMBER_5"), org.jooq.impl.SQLDataType.VARCHAR(15).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_1</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_STRING_1 = createField(DSL.name("CST_STRING_1"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_2</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_STRING_2 = createField(DSL.name("CST_STRING_2"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_3</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_STRING_3 = createField(DSL.name("CST_STRING_3"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_4</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_STRING_4 = createField(DSL.name("CST_STRING_4"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_5</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_STRING_5 = createField(DSL.name("CST_STRING_5"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_6</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_STRING_6 = createField(DSL.name("CST_STRING_6"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_7</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_STRING_7 = createField(DSL.name("CST_STRING_7"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_8</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_STRING_8 = createField(DSL.name("CST_STRING_8"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_9</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_STRING_9 = createField(DSL.name("CST_STRING_9"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_10</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_STRING_10 = createField(DSL.name("CST_STRING_10"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_11</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_STRING_11 = createField(DSL.name("CST_STRING_11"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_12</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_STRING_12 = createField(DSL.name("CST_STRING_12"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_13</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_STRING_13 = createField(DSL.name("CST_STRING_13"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_14</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_STRING_14 = createField(DSL.name("CST_STRING_14"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_15</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_STRING_15 = createField(DSL.name("CST_STRING_15"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_16</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_STRING_16 = createField(DSL.name("CST_STRING_16"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_17</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_STRING_17 = createField(DSL.name("CST_STRING_17"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_18</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_STRING_18 = createField(DSL.name("CST_STRING_18"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_19</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_STRING_19 = createField(DSL.name("CST_STRING_19"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_STRING_20</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_STRING_20 = createField(DSL.name("CST_STRING_20"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CODE_1</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_CODE_1 = createField(DSL.name("CST_CODE_1"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CODE_2</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_CODE_2 = createField(DSL.name("CST_CODE_2"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CODE_3</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_CODE_3 = createField(DSL.name("CST_CODE_3"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CODE_4</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_CODE_4 = createField(DSL.name("CST_CODE_4"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CODE_5</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_CODE_5 = createField(DSL.name("CST_CODE_5"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CODE_6</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_CODE_6 = createField(DSL.name("CST_CODE_6"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CODE_7</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_CODE_7 = createField(DSL.name("CST_CODE_7"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CODE_8</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_CODE_8 = createField(DSL.name("CST_CODE_8"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CODE_9</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_CODE_9 = createField(DSL.name("CST_CODE_9"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CODE_10</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_CODE_10 = createField(DSL.name("CST_CODE_10"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_MULTICODE_1</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_MULTICODE_1 = createField(DSL.name("CST_MULTICODE_1"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_MULTICODE_2</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_MULTICODE_2 = createField(DSL.name("CST_MULTICODE_2"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_MULTICODE_3</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_MULTICODE_3 = createField(DSL.name("CST_MULTICODE_3"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CONCODE_1</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_CONCODE_1 = createField(DSL.name("CST_CONCODE_1"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CONCODE_2</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_CONCODE_2 = createField(DSL.name("CST_CONCODE_2"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CONCODE_3</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_CONCODE_3 = createField(DSL.name("CST_CONCODE_3"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CSCODE_1</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_CSCODE_1 = createField(DSL.name("CST_CSCODE_1"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CSCODE_2</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_CSCODE_2 = createField(DSL.name("CST_CSCODE_2"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.view_result_cst_by_result_primium.CST_CSCODE_3</code>.
     */
    public final TableField<CommonViewResultCstByResultRecord, String> CST_CSCODE_3 = createField(DSL.name("CST_CSCODE_3"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    private String tableName;

    /**
     * Create a <code>CUSTOMDB.view_result_cst_by_result_primium</code> table reference
     */
    public CommonViewResultCstByResult(String companyName) {
        this(DSL.name("view_result_cst_by_result_" + companyName), null);
    }


    private CommonViewResultCstByResult(Name alias, Table<CommonViewResultCstByResultRecord> aliased) {
        this(alias, aliased, null);
    }

    private CommonViewResultCstByResult(Name alias, Table<CommonViewResultCstByResultRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment("VIEW"));
        this.tableName = alias.last();
    }

    public <O extends Record> CommonViewResultCstByResult(CommonViewResultCstByResult table, Table<O> child, ForeignKey<O, CommonViewResultCstByResultRecord> key) {
        super(child, key, table);
        this.tableName = table.getName();
    }

    @Override
    public Schema getSchema() {
        return Customdb.CUSTOMDB;
    }

    @Override
    public CommonViewResultCstByResult as(String alias) {
        return new CommonViewResultCstByResult(DSL.name(alias), this);
    }

    @Override
    public CommonViewResultCstByResult as(Name alias) {
        return new CommonViewResultCstByResult(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonViewResultCstByResult rename(String name) {
        return new CommonViewResultCstByResult(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonViewResultCstByResult rename(Name name) {
        return new CommonViewResultCstByResult(name, null);
    }
}
