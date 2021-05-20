package kr.co.eicn.ippbx.meta.jooq.pds.tables;

import kr.co.eicn.ippbx.meta.jooq.pds.Indexes;
import kr.co.eicn.ippbx.meta.jooq.pds.Keys;
import kr.co.eicn.ippbx.meta.jooq.pds.Pds;
import kr.co.eicn.ippbx.meta.jooq.pds.tables.records.PdsCustomInfoRecord;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

public class CommonPDSCustomInfo extends TableImpl<PdsCustomInfoRecord> {

    /**
     * The column <code>PDS.pds_custom_info.PDS_SYS_CUSTOM_ID</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_SYS_CUSTOM_ID = createField(DSL.name("PDS_SYS_CUSTOM_ID"), org.jooq.impl.SQLDataType.VARCHAR(100).nullable(false).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_SYS_UPLOAD_DATE</code>.
     */
    public final TableField<PdsCustomInfoRecord, Timestamp> PDS_SYS_UPLOAD_DATE = createField(DSL.name("PDS_SYS_UPLOAD_DATE"), org.jooq.impl.SQLDataType.TIMESTAMP.defaultValue(org.jooq.impl.DSL.inline("2010-01-01 00:00:00", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_SYS_UPDATE_DATE</code>.
     */
    public final TableField<PdsCustomInfoRecord, Timestamp> PDS_SYS_UPDATE_DATE = createField(DSL.name("PDS_SYS_UPDATE_DATE"), org.jooq.impl.SQLDataType.TIMESTAMP.defaultValue(org.jooq.impl.DSL.inline("2010-01-01 00:00:00", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_SYS_GROUP_ID</code>.
     */
    public final TableField<PdsCustomInfoRecord, Integer> PDS_SYS_GROUP_ID = createField(DSL.name("PDS_SYS_GROUP_ID"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_SYS_GROUP_TYPE</code>.
     */
    public final TableField<PdsCustomInfoRecord, Integer> PDS_SYS_GROUP_TYPE = createField(DSL.name("PDS_SYS_GROUP_TYPE"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_SYS_RESULT_TYPE</code>.
     */
    public final TableField<PdsCustomInfoRecord, Integer> PDS_SYS_RESULT_TYPE = createField(DSL.name("PDS_SYS_RESULT_TYPE"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_SYS_DAMDANG_ID</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_SYS_DAMDANG_ID = createField(DSL.name("PDS_SYS_DAMDANG_ID"), org.jooq.impl.SQLDataType.VARCHAR(20).nullable(false).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_SYS_RID_NUMBER</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_SYS_RID_NUMBER = createField(DSL.name("PDS_SYS_RID_NUMBER"), org.jooq.impl.SQLDataType.VARCHAR(20).nullable(false).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_SYS_LAST_RESULT_ID</code>.
     */
    public final TableField<PdsCustomInfoRecord, Integer> PDS_SYS_LAST_RESULT_ID = createField(DSL.name("PDS_SYS_LAST_RESULT_ID"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_SYS_LAST_RESULT_DATE</code>.
     */
    public final TableField<PdsCustomInfoRecord, Timestamp> PDS_SYS_LAST_RESULT_DATE = createField(DSL.name("PDS_SYS_LAST_RESULT_DATE"), org.jooq.impl.SQLDataType.TIMESTAMP.defaultValue(org.jooq.impl.DSL.inline("2010-01-01 00:00:00", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_SYS_LAST_CALL_DATE</code>.
     */
    public final TableField<PdsCustomInfoRecord, Timestamp> PDS_SYS_LAST_CALL_DATE = createField(DSL.name("PDS_SYS_LAST_CALL_DATE"), org.jooq.impl.SQLDataType.TIMESTAMP.defaultValue(org.jooq.impl.DSL.inline("2010-01-01 00:00:00", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_SYS_LAST_HANGUP_CAUSE</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_SYS_LAST_HANGUP_CAUSE = createField(DSL.name("PDS_SYS_LAST_HANGUP_CAUSE"), org.jooq.impl.SQLDataType.VARCHAR(30).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_SYS_LAST_CALL_UNIQUEID</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_SYS_LAST_CALL_UNIQUEID = createField(DSL.name("PDS_SYS_LAST_CALL_UNIQUEID"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_SYS_COMPANY_ID</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_SYS_COMPANY_ID = createField(DSL.name("PDS_SYS_COMPANY_ID"), org.jooq.impl.SQLDataType.VARCHAR(50).nullable(false).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_DATE_1</code>.
     */
    public final TableField<PdsCustomInfoRecord, Date> PDS_DATE_1 = createField(DSL.name("PDS_DATE_1"), org.jooq.impl.SQLDataType.DATE.defaultValue(org.jooq.impl.DSL.inline("2010-01-01", org.jooq.impl.SQLDataType.DATE)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_DATE_2</code>.
     */
    public final TableField<PdsCustomInfoRecord, Date> PDS_DATE_2 = createField(DSL.name("PDS_DATE_2"), org.jooq.impl.SQLDataType.DATE.defaultValue(org.jooq.impl.DSL.inline("2010-01-01", org.jooq.impl.SQLDataType.DATE)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_DATE_3</code>.
     */
    public final TableField<PdsCustomInfoRecord, Date> PDS_DATE_3 = createField(DSL.name("PDS_DATE_3"), org.jooq.impl.SQLDataType.DATE.defaultValue(org.jooq.impl.DSL.inline("2010-01-01", org.jooq.impl.SQLDataType.DATE)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_DAY_1</code>.
     */
    public final TableField<PdsCustomInfoRecord, Date> PDS_DAY_1 = createField(DSL.name("PDS_DAY_1"), org.jooq.impl.SQLDataType.DATE.defaultValue(org.jooq.impl.DSL.inline("2010-01-01", org.jooq.impl.SQLDataType.DATE)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_DAY_2</code>.
     */
    public final TableField<PdsCustomInfoRecord, Date> PDS_DAY_2 = createField(DSL.name("PDS_DAY_2"), org.jooq.impl.SQLDataType.DATE.defaultValue(org.jooq.impl.DSL.inline("2010-01-01", org.jooq.impl.SQLDataType.DATE)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_DAY_3</code>.
     */
    public final TableField<PdsCustomInfoRecord, Date> PDS_DAY_3 = createField(DSL.name("PDS_DAY_3"), org.jooq.impl.SQLDataType.DATE.defaultValue(org.jooq.impl.DSL.inline("2010-01-01", org.jooq.impl.SQLDataType.DATE)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_DATETIME_1</code>.
     */
    public final TableField<PdsCustomInfoRecord, Timestamp> PDS_DATETIME_1 = createField(DSL.name("PDS_DATETIME_1"), org.jooq.impl.SQLDataType.TIMESTAMP.defaultValue(org.jooq.impl.DSL.inline("2010-01-01 00:00:00", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_DATETIME_2</code>.
     */
    public final TableField<PdsCustomInfoRecord, Timestamp> PDS_DATETIME_2 = createField(DSL.name("PDS_DATETIME_2"), org.jooq.impl.SQLDataType.TIMESTAMP.defaultValue(org.jooq.impl.DSL.inline("2010-01-01 00:00:00", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_DATETIME_3</code>.
     */
    public final TableField<PdsCustomInfoRecord, Timestamp> PDS_DATETIME_3 = createField(DSL.name("PDS_DATETIME_3"), org.jooq.impl.SQLDataType.TIMESTAMP.defaultValue(org.jooq.impl.DSL.inline("2010-01-01 00:00:00", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_INT_1</code>.
     */
    public final TableField<PdsCustomInfoRecord, Integer> PDS_INT_1 = createField(DSL.name("PDS_INT_1"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_INT_2</code>.
     */
    public final TableField<PdsCustomInfoRecord, Integer> PDS_INT_2 = createField(DSL.name("PDS_INT_2"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_INT_3</code>.
     */
    public final TableField<PdsCustomInfoRecord, Integer> PDS_INT_3 = createField(DSL.name("PDS_INT_3"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_INT_4</code>.
     */
    public final TableField<PdsCustomInfoRecord, Integer> PDS_INT_4 = createField(DSL.name("PDS_INT_4"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_INT_5</code>.
     */
    public final TableField<PdsCustomInfoRecord, Integer> PDS_INT_5 = createField(DSL.name("PDS_INT_5"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_NUMBER_1</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_NUMBER_1 = createField(DSL.name("PDS_NUMBER_1"), org.jooq.impl.SQLDataType.VARCHAR(15).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_NUMBER_2</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_NUMBER_2 = createField(DSL.name("PDS_NUMBER_2"), org.jooq.impl.SQLDataType.VARCHAR(15).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_NUMBER_3</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_NUMBER_3 = createField(DSL.name("PDS_NUMBER_3"), org.jooq.impl.SQLDataType.VARCHAR(15).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_NUMBER_4</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_NUMBER_4 = createField(DSL.name("PDS_NUMBER_4"), org.jooq.impl.SQLDataType.VARCHAR(15).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_NUMBER_5</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_NUMBER_5 = createField(DSL.name("PDS_NUMBER_5"), org.jooq.impl.SQLDataType.VARCHAR(15).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_STRING_1</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_STRING_1 = createField(DSL.name("PDS_STRING_1"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_STRING_2</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_STRING_2 = createField(DSL.name("PDS_STRING_2"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_STRING_3</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_STRING_3 = createField(DSL.name("PDS_STRING_3"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_STRING_4</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_STRING_4 = createField(DSL.name("PDS_STRING_4"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_STRING_5</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_STRING_5 = createField(DSL.name("PDS_STRING_5"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_STRING_6</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_STRING_6 = createField(DSL.name("PDS_STRING_6"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_STRING_7</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_STRING_7 = createField(DSL.name("PDS_STRING_7"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_STRING_8</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_STRING_8 = createField(DSL.name("PDS_STRING_8"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_STRING_9</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_STRING_9 = createField(DSL.name("PDS_STRING_9"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_STRING_10</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_STRING_10 = createField(DSL.name("PDS_STRING_10"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_STRING_11</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_STRING_11 = createField(DSL.name("PDS_STRING_11"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_STRING_12</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_STRING_12 = createField(DSL.name("PDS_STRING_12"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_STRING_13</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_STRING_13 = createField(DSL.name("PDS_STRING_13"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_STRING_14</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_STRING_14 = createField(DSL.name("PDS_STRING_14"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_STRING_15</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_STRING_15 = createField(DSL.name("PDS_STRING_15"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_STRING_16</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_STRING_16 = createField(DSL.name("PDS_STRING_16"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_STRING_17</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_STRING_17 = createField(DSL.name("PDS_STRING_17"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_STRING_18</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_STRING_18 = createField(DSL.name("PDS_STRING_18"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_STRING_19</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_STRING_19 = createField(DSL.name("PDS_STRING_19"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_STRING_20</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_STRING_20 = createField(DSL.name("PDS_STRING_20"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_CODE_1</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_CODE_1 = createField(DSL.name("PDS_CODE_1"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_CODE_2</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_CODE_2 = createField(DSL.name("PDS_CODE_2"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_CODE_3</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_CODE_3 = createField(DSL.name("PDS_CODE_3"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_CODE_4</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_CODE_4 = createField(DSL.name("PDS_CODE_4"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_CODE_5</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_CODE_5 = createField(DSL.name("PDS_CODE_5"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_CODE_6</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_CODE_6 = createField(DSL.name("PDS_CODE_6"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_CODE_7</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_CODE_7 = createField(DSL.name("PDS_CODE_7"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_CODE_8</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_CODE_8 = createField(DSL.name("PDS_CODE_8"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_CODE_9</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_CODE_9 = createField(DSL.name("PDS_CODE_9"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_CODE_10</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_CODE_10 = createField(DSL.name("PDS_CODE_10"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_MULTICODE_1</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_MULTICODE_1 = createField(DSL.name("PDS_MULTICODE_1"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_MULTICODE_2</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_MULTICODE_2 = createField(DSL.name("PDS_MULTICODE_2"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_MULTICODE_3</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_MULTICODE_3 = createField(DSL.name("PDS_MULTICODE_3"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_CONCODE_1</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_CONCODE_1 = createField(DSL.name("PDS_CONCODE_1"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_CONCODE_2</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_CONCODE_2 = createField(DSL.name("PDS_CONCODE_2"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_CONCODE_3</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_CONCODE_3 = createField(DSL.name("PDS_CONCODE_3"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_CSCODE_1</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_CSCODE_1 = createField(DSL.name("PDS_CSCODE_1"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_CSCODE_2</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_CSCODE_2 = createField(DSL.name("PDS_CSCODE_2"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_custom_info.PDS_CSCODE_3</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_CSCODE_3 = createField(DSL.name("PDS_CSCODE_3"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    private final String tableName;

    /**
     * Create an aliased <code>PDS.pds_custom_info</code> table reference
     */
    public CommonPDSCustomInfo(String table) {
        this(DSL.name("pds_custom_info_" + table), null);
    }

    private CommonPDSCustomInfo(String alias, Table<PdsCustomInfoRecord> aliased) {
        this(DSL.name(alias), aliased);
    }

    private CommonPDSCustomInfo(Name alias, Table<PdsCustomInfoRecord> aliased) {
        this(alias, aliased, null);
    }

    private CommonPDSCustomInfo(Name alias, Table<PdsCustomInfoRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
        this.tableName = alias.last();
    }

    public <O extends Record> CommonPDSCustomInfo(CommonPDSCustomInfo table, Table<O> child, ForeignKey<O, PdsCustomInfoRecord> key) {
        super(child, key, table);
        this.tableName = table.getName();
    }

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PdsCustomInfoRecord> getRecordType() {
        return PdsCustomInfoRecord.class;
    }

    @Override
    public Schema getSchema() {
        return Pds.PDS;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.PDS_CUSTOM_INFO_PDS_SYS_GROUP_ID);
    }

    @Override
    public UniqueKey<PdsCustomInfoRecord> getPrimaryKey() {
        return Keys.KEY_PDS_CUSTOM_INFO_PRIMARY;
    }

    @Override
    public List<UniqueKey<PdsCustomInfoRecord>> getKeys() {
        return Arrays.<UniqueKey<PdsCustomInfoRecord>>asList(Keys.KEY_PDS_CUSTOM_INFO_PRIMARY);
    }

    @Override
    public CommonPDSCustomInfo as(String alias) {
        return new CommonPDSCustomInfo(DSL.name(alias), this);
    }

    @Override
    public CommonPDSCustomInfo as(Name alias) {
        return new CommonPDSCustomInfo(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonPDSCustomInfo rename(String name) {
        return new CommonPDSCustomInfo(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonPDSCustomInfo rename(Name name) {
        return new CommonPDSCustomInfo(name, null);
    }
}
