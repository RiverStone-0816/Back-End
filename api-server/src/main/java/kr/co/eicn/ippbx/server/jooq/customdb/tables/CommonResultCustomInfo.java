package kr.co.eicn.ippbx.server.jooq.customdb.tables;

import kr.co.eicn.ippbx.server.jooq.customdb.Customdb;
import kr.co.eicn.ippbx.server.jooq.customdb.Indexes;
import kr.co.eicn.ippbx.server.jooq.customdb.Keys;
import kr.co.eicn.ippbx.server.jooq.customdb.tables.records.ResultCustomInfoRecord;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

public class CommonResultCustomInfo extends TableImpl<ResultCustomInfoRecord> {
	/**
	 * The reference instance of <code>CUSTOMDB.result_custom_info</code>
	 */
	public static final ResultCustomInfo RESULT_CUSTOM_INFO = new ResultCustomInfo();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<ResultCustomInfoRecord> getRecordType() {
		return ResultCustomInfoRecord.class;
	}

	/**
	 * The column <code>CUSTOMDB.result_custom_info.seq</code>.
	 */
	public final TableField<ResultCustomInfoRecord, Integer> SEQ = createField(DSL.name("seq"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.company_id</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> COMPANY_ID = createField(DSL.name("company_id"), org.jooq.impl.SQLDataType.VARCHAR(30).nullable(false).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.result_date</code>.
	 */
	public final TableField<ResultCustomInfoRecord, Timestamp> RESULT_DATE = createField(DSL.name("result_date"), org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(DSL.inline("2010-01-01 00:00:00", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.update_date</code>.
	 */
	public final TableField<ResultCustomInfoRecord, Timestamp> UPDATE_DATE = createField(DSL.name("update_date"), org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(DSL.inline("2010-01-01 00:00:00", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.uniqueid</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> UNIQUEID = createField(DSL.name("uniqueid"), org.jooq.impl.SQLDataType.VARCHAR(50).nullable(false).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.hangup_cause</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> HANGUP_CAUSE = createField(DSL.name("hangup_cause"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.hangup_msg</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> HANGUP_MSG = createField(DSL.name("hangup_msg"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.billsec</code>.
	 */
	public final TableField<ResultCustomInfoRecord, Integer> BILLSEC = createField(DSL.name("billsec"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.custom_id</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> CUSTOM_ID = createField(DSL.name("custom_id"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.click_key</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> CLICK_KEY = createField(DSL.name("click_key"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.custom_number</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> CUSTOM_NUMBER = createField(DSL.name("custom_number"), org.jooq.impl.SQLDataType.VARCHAR(50).nullable(false).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.execute_id</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> EXECUTE_ID = createField(DSL.name("execute_id"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.group_id</code>.
	 */
	public final TableField<ResultCustomInfoRecord, Integer> GROUP_ID = createField(DSL.name("group_id"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.userid</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> USERID = createField(DSL.name("userid"), org.jooq.impl.SQLDataType.VARCHAR(30).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.userid_org</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> USERID_ORG = createField(DSL.name("userid_org"), org.jooq.impl.SQLDataType.VARCHAR(30).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.userid_tr</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> USERID_TR = createField(DSL.name("userid_tr"), org.jooq.impl.SQLDataType.VARCHAR(30).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.result_type</code>.
	 */
	public final TableField<ResultCustomInfoRecord, Integer> RESULT_TYPE = createField(DSL.name("result_type"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.call_type</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> CALL_TYPE = createField(DSL.name("call_type"), org.jooq.impl.SQLDataType.VARCHAR(5).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.from_org</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> FROM_ORG = createField(DSL.name("from_org"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.group_kind</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> GROUP_KIND = createField(DSL.name("group_kind"), org.jooq.impl.SQLDataType.VARCHAR(10).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.group_type</code>.
	 */
	public final TableField<ResultCustomInfoRecord, Integer> GROUP_TYPE = createField(DSL.name("group_type"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_DATE_1</code>.
	 */
	public final TableField<ResultCustomInfoRecord, Date> RS_DATE_1 = createField(DSL.name("RS_DATE_1"), org.jooq.impl.SQLDataType.DATE.defaultValue(DSL.inline("2010-01-01", org.jooq.impl.SQLDataType.DATE)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_DATE_2</code>.
	 */
	public final TableField<ResultCustomInfoRecord, Date> RS_DATE_2 = createField(DSL.name("RS_DATE_2"), org.jooq.impl.SQLDataType.DATE.defaultValue(DSL.inline("2010-01-01", org.jooq.impl.SQLDataType.DATE)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_DATE_3</code>.
	 */
	public final TableField<ResultCustomInfoRecord, Date> RS_DATE_3 = createField(DSL.name("RS_DATE_3"), org.jooq.impl.SQLDataType.DATE.defaultValue(DSL.inline("2010-01-01", org.jooq.impl.SQLDataType.DATE)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_DAY_1</code>.
	 */
	public final TableField<ResultCustomInfoRecord, Date> RS_DAY_1 = createField(DSL.name("RS_DAY_1"), org.jooq.impl.SQLDataType.DATE.defaultValue(DSL.inline("2010-01-01", org.jooq.impl.SQLDataType.DATE)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_DAY_2</code>.
	 */
	public final TableField<ResultCustomInfoRecord, Date> RS_DAY_2 = createField(DSL.name("RS_DAY_2"), org.jooq.impl.SQLDataType.DATE.defaultValue(DSL.inline("2010-01-01", org.jooq.impl.SQLDataType.DATE)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_DAY_3</code>.
	 */
	public final TableField<ResultCustomInfoRecord, Date> RS_DAY_3 = createField(DSL.name("RS_DAY_3"), org.jooq.impl.SQLDataType.DATE.defaultValue(DSL.inline("2010-01-01", org.jooq.impl.SQLDataType.DATE)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_DATETIME_1</code>.
	 */
	public final TableField<ResultCustomInfoRecord, Timestamp> RS_DATETIME_1 = createField(DSL.name("RS_DATETIME_1"), org.jooq.impl.SQLDataType.TIMESTAMP.defaultValue(DSL.inline("2010-01-01 00:00:00", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_DATETIME_2</code>.
	 */
	public final TableField<ResultCustomInfoRecord, Timestamp> RS_DATETIME_2 = createField(DSL.name("RS_DATETIME_2"), org.jooq.impl.SQLDataType.TIMESTAMP.defaultValue(DSL.inline("2010-01-01 00:00:00", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_DATETIME_3</code>.
	 */
	public final TableField<ResultCustomInfoRecord, Timestamp> RS_DATETIME_3 = createField(DSL.name("RS_DATETIME_3"), org.jooq.impl.SQLDataType.TIMESTAMP.defaultValue(DSL.inline("2010-01-01 00:00:00", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_INT_1</code>.
	 */
	public final TableField<ResultCustomInfoRecord, Integer> RS_INT_1 = createField(DSL.name("RS_INT_1"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_INT_2</code>.
	 */
	public final TableField<ResultCustomInfoRecord, Integer> RS_INT_2 = createField(DSL.name("RS_INT_2"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_INT_3</code>.
	 */
	public final TableField<ResultCustomInfoRecord, Integer> RS_INT_3 = createField(DSL.name("RS_INT_3"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_INT_4</code>.
	 */
	public final TableField<ResultCustomInfoRecord, Integer> RS_INT_4 = createField(DSL.name("RS_INT_4"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_INT_5</code>.
	 */
	public final TableField<ResultCustomInfoRecord, Integer> RS_INT_5 = createField(DSL.name("RS_INT_5"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_NUMBER_1</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_NUMBER_1 = createField(DSL.name("RS_NUMBER_1"), org.jooq.impl.SQLDataType.VARCHAR(15).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_NUMBER_2</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_NUMBER_2 = createField(DSL.name("RS_NUMBER_2"), org.jooq.impl.SQLDataType.VARCHAR(15).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_NUMBER_3</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_NUMBER_3 = createField(DSL.name("RS_NUMBER_3"), org.jooq.impl.SQLDataType.VARCHAR(15).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_NUMBER_4</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_NUMBER_4 = createField(DSL.name("RS_NUMBER_4"), org.jooq.impl.SQLDataType.VARCHAR(15).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_NUMBER_5</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_NUMBER_5 = createField(DSL.name("RS_NUMBER_5"), org.jooq.impl.SQLDataType.VARCHAR(15).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_STRING_1</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_STRING_1 = createField(DSL.name("RS_STRING_1"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_STRING_2</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_STRING_2 = createField(DSL.name("RS_STRING_2"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_STRING_3</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_STRING_3 = createField(DSL.name("RS_STRING_3"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_STRING_4</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_STRING_4 = createField(DSL.name("RS_STRING_4"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_STRING_5</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_STRING_5 = createField(DSL.name("RS_STRING_5"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_STRING_6</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_STRING_6 = createField(DSL.name("RS_STRING_6"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_STRING_7</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_STRING_7 = createField(DSL.name("RS_STRING_7"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_STRING_8</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_STRING_8 = createField(DSL.name("RS_STRING_8"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_STRING_9</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_STRING_9 = createField(DSL.name("RS_STRING_9"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_STRING_10</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_STRING_10 = createField(DSL.name("RS_STRING_10"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_STRING_11</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_STRING_11 = createField(DSL.name("RS_STRING_11"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_STRING_12</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_STRING_12 = createField(DSL.name("RS_STRING_12"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_STRING_13</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_STRING_13 = createField(DSL.name("RS_STRING_13"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_STRING_14</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_STRING_14 = createField(DSL.name("RS_STRING_14"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_STRING_15</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_STRING_15 = createField(DSL.name("RS_STRING_15"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_STRING_16</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_STRING_16 = createField(DSL.name("RS_STRING_16"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_STRING_17</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_STRING_17 = createField(DSL.name("RS_STRING_17"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_STRING_18</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_STRING_18 = createField(DSL.name("RS_STRING_18"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_STRING_19</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_STRING_19 = createField(DSL.name("RS_STRING_19"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_STRING_20</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_STRING_20 = createField(DSL.name("RS_STRING_20"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_CODE_1</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_CODE_1 = createField(DSL.name("RS_CODE_1"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_CODE_2</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_CODE_2 = createField(DSL.name("RS_CODE_2"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_CODE_3</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_CODE_3 = createField(DSL.name("RS_CODE_3"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_CODE_4</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_CODE_4 = createField(DSL.name("RS_CODE_4"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_CODE_5</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_CODE_5 = createField(DSL.name("RS_CODE_5"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_CODE_6</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_CODE_6 = createField(DSL.name("RS_CODE_6"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_CODE_7</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_CODE_7 = createField(DSL.name("RS_CODE_7"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_CODE_8</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_CODE_8 = createField(DSL.name("RS_CODE_8"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_CODE_9</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_CODE_9 = createField(DSL.name("RS_CODE_9"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_CODE_10</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_CODE_10 = createField(DSL.name("RS_CODE_10"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_MULTICODE_1</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_MULTICODE_1 = createField(DSL.name("RS_MULTICODE_1"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_MULTICODE_2</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_MULTICODE_2 = createField(DSL.name("RS_MULTICODE_2"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_MULTICODE_3</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_MULTICODE_3 = createField(DSL.name("RS_MULTICODE_3"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_IMG_1</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_IMG_1 = createField(DSL.name("RS_IMG_1"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_IMG_2</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_IMG_2 = createField(DSL.name("RS_IMG_2"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_IMG_3</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_IMG_3 = createField(DSL.name("RS_IMG_3"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_CONCODE_1</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_CONCODE_1 = createField(DSL.name("RS_CONCODE_1"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_CONCODE_2</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_CONCODE_2 = createField(DSL.name("RS_CONCODE_2"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_CONCODE_3</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_CONCODE_3 = createField(DSL.name("RS_CONCODE_3"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_CSCODE_1</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_CSCODE_1 = createField(DSL.name("RS_CSCODE_1"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_CSCODE_2</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_CSCODE_2 = createField(DSL.name("RS_CSCODE_2"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CUSTOMDB.result_custom_info.RS_CSCODE_3</code>.
	 */
	public final TableField<ResultCustomInfoRecord, String> RS_CSCODE_3 = createField(DSL.name("RS_CSCODE_3"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	private final String tableName;

	/**
	 * Create a <code>CUSTOMDB.result_custom_info</code> table reference
	 */
	public CommonResultCustomInfo(String table) {
		this(DSL.name("result_custom_info_" + table), null);
	}

	private CommonResultCustomInfo(Name alias, Table<ResultCustomInfoRecord> aliased) {
		this(alias, aliased, null);
	}

	private CommonResultCustomInfo(Name alias, Table<ResultCustomInfoRecord> aliased, Field<?>[] parameters) {
		super(alias, null, aliased, parameters, DSL.comment(""));
		this.tableName = alias.last();
	}

	public <O extends Record> CommonResultCustomInfo(CommonResultCustomInfo table, Table<O> child, ForeignKey<O, ResultCustomInfoRecord> key) {
		super(child, key, RESULT_CUSTOM_INFO);
		this.tableName = table.getName();
	}

	@Override
	public Schema getSchema() {
		return Customdb.CUSTOMDB;
	}

	@Override
	public List<Index> getIndexes() {
		return Arrays.<Index>asList(Indexes.RESULT_CUSTOM_INFO_CLICK_KEY, Indexes.RESULT_CUSTOM_INFO_CUSTOM_ID, Indexes.RESULT_CUSTOM_INFO_CUSTOM_NUMBER, Indexes.RESULT_CUSTOM_INFO_GROUP_ID, Indexes.RESULT_CUSTOM_INFO_GROUP_KIND, Indexes.RESULT_CUSTOM_INFO_PRIMARY, Indexes.RESULT_CUSTOM_INFO_RESULT_DATE, Indexes.RESULT_CUSTOM_INFO_UNIQUEID, Indexes.RESULT_CUSTOM_INFO_USERID);
	}

	@Override
	public Identity<ResultCustomInfoRecord, Integer> getIdentity() {
		return Keys.IDENTITY_RESULT_CUSTOM_INFO;
	}

	@Override
	public UniqueKey<ResultCustomInfoRecord> getPrimaryKey() {
		return Keys.KEY_RESULT_CUSTOM_INFO_PRIMARY;
	}

	@Override
	public List<UniqueKey<ResultCustomInfoRecord>> getKeys() {
		return Arrays.<UniqueKey<ResultCustomInfoRecord>>asList(Keys.KEY_RESULT_CUSTOM_INFO_PRIMARY);
	}

	@Override
	public CommonResultCustomInfo as(String alias) {
		return new CommonResultCustomInfo(DSL.name(alias), this);
	}

	@Override
	public CommonResultCustomInfo as(Name alias) {
		return new CommonResultCustomInfo(alias, this);
	}

	/**
	 * Rename this table
	 */
	@Override
	public CommonResultCustomInfo rename(String name) {
		return new CommonResultCustomInfo(DSL.name(name), null);
	}

	/**
	 * Rename this table
	 */
	@Override
	public CommonResultCustomInfo rename(Name name) {
		return new CommonResultCustomInfo(name, null);
	}
}
