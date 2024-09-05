package kr.co.eicn.ippbx.meta.jooq.pds.tables;

import kr.co.eicn.ippbx.meta.jooq.pds.Indexes;
import kr.co.eicn.ippbx.meta.jooq.pds.Pds;
import kr.co.eicn.ippbx.meta.jooq.pds.tables.records.PdsCustomInfoRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommonPDSCustomInfo extends TableImpl<PdsCustomInfoRecord> {
    /**
     * The class holding records for this type
     */
    @NotNull
    @Override
    public Class<PdsCustomInfoRecord> getRecordType() {
        return PdsCustomInfoRecord.class;
    }

    /**
     * The column <code>PDS.pds_custom_info.PDS_SYS_CUSTOM_ID</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_SYS_CUSTOM_ID = createField(DSL.name("PDS_SYS_CUSTOM_ID"), SQLDataType.VARCHAR(100).nullable(false).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_SYS_UPLOAD_DATE</code>.
     */
    public final TableField<PdsCustomInfoRecord, Timestamp> PDS_SYS_UPLOAD_DATE = createField(DSL.name("PDS_SYS_UPLOAD_DATE"), SQLDataType.TIMESTAMP(0).defaultValue(DSL.field("'2010-01-01 00:00:00'", SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_SYS_UPDATE_DATE</code>.
     */
    public final TableField<PdsCustomInfoRecord, Timestamp> PDS_SYS_UPDATE_DATE = createField(DSL.name("PDS_SYS_UPDATE_DATE"), SQLDataType.TIMESTAMP(0).defaultValue(DSL.field("'2010-01-01 00:00:00'", SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_SYS_GROUP_ID</code>.
     */
    public final TableField<PdsCustomInfoRecord, Integer> PDS_SYS_GROUP_ID = createField(DSL.name("PDS_SYS_GROUP_ID"), SQLDataType.INTEGER.nullable(false).defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_SYS_GROUP_TYPE</code>.
     */
    public final TableField<PdsCustomInfoRecord, Integer> PDS_SYS_GROUP_TYPE = createField(DSL.name("PDS_SYS_GROUP_TYPE"), SQLDataType.INTEGER.nullable(false).defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_SYS_RESULT_TYPE</code>.
     */
    public final TableField<PdsCustomInfoRecord, Integer> PDS_SYS_RESULT_TYPE = createField(DSL.name("PDS_SYS_RESULT_TYPE"), SQLDataType.INTEGER.nullable(false).defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_SYS_DAMDANG_ID</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_SYS_DAMDANG_ID = createField(DSL.name("PDS_SYS_DAMDANG_ID"), SQLDataType.VARCHAR(20).nullable(false).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_SYS_RID_NUMBER</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_SYS_RID_NUMBER = createField(DSL.name("PDS_SYS_RID_NUMBER"), SQLDataType.VARCHAR(20).nullable(false).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_SYS_LAST_RESULT_ID</code>.
     */
    public final TableField<PdsCustomInfoRecord, Integer> PDS_SYS_LAST_RESULT_ID = createField(DSL.name("PDS_SYS_LAST_RESULT_ID"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_SYS_LAST_RESULT_DATE</code>.
     */
    public final TableField<PdsCustomInfoRecord, Timestamp> PDS_SYS_LAST_RESULT_DATE = createField(DSL.name("PDS_SYS_LAST_RESULT_DATE"), SQLDataType.TIMESTAMP(0).defaultValue(DSL.field("'2010-01-01 00:00:00'", SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_SYS_LAST_CALL_DATE</code>.
     */
    public final TableField<PdsCustomInfoRecord, Timestamp> PDS_SYS_LAST_CALL_DATE = createField(DSL.name("PDS_SYS_LAST_CALL_DATE"), SQLDataType.TIMESTAMP(0).defaultValue(DSL.field("'2010-01-01 00:00:00'", SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_SYS_LAST_HANGUP_CAUSE</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_SYS_LAST_HANGUP_CAUSE = createField(DSL.name("PDS_SYS_LAST_HANGUP_CAUSE"), SQLDataType.VARCHAR(30).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_SYS_LAST_CALL_UNIQUEID</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_SYS_LAST_CALL_UNIQUEID = createField(DSL.name("PDS_SYS_LAST_CALL_UNIQUEID"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_SYS_COMPANY_ID</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_SYS_COMPANY_ID = createField(DSL.name("PDS_SYS_COMPANY_ID"), SQLDataType.VARCHAR(50).nullable(false).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_DATE_1</code>.
     */
    public final TableField<PdsCustomInfoRecord, Date> PDS_DATE_1 = createField(DSL.name("PDS_DATE_1"), SQLDataType.DATE.defaultValue(DSL.field("'2010-01-01'", SQLDataType.DATE)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_DATE_2</code>.
     */
    public final TableField<PdsCustomInfoRecord, Date> PDS_DATE_2 = createField(DSL.name("PDS_DATE_2"), SQLDataType.DATE.defaultValue(DSL.field("'2010-01-01'", SQLDataType.DATE)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_DATE_3</code>.
     */
    public final TableField<PdsCustomInfoRecord, Date> PDS_DATE_3 = createField(DSL.name("PDS_DATE_3"), SQLDataType.DATE.defaultValue(DSL.field("'2010-01-01'", SQLDataType.DATE)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_DAY_1</code>.
     */
    public final TableField<PdsCustomInfoRecord, Date> PDS_DAY_1 = createField(DSL.name("PDS_DAY_1"), SQLDataType.DATE.defaultValue(DSL.field("'2010-01-01'", SQLDataType.DATE)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_DAY_2</code>.
     */
    public final TableField<PdsCustomInfoRecord, Date> PDS_DAY_2 = createField(DSL.name("PDS_DAY_2"), SQLDataType.DATE.defaultValue(DSL.field("'2010-01-01'", SQLDataType.DATE)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_DAY_3</code>.
     */
    public final TableField<PdsCustomInfoRecord, Date> PDS_DAY_3 = createField(DSL.name("PDS_DAY_3"), SQLDataType.DATE.defaultValue(DSL.field("'2010-01-01'", SQLDataType.DATE)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_DATETIME_1</code>.
     */
    public final TableField<PdsCustomInfoRecord, Timestamp> PDS_DATETIME_1 = createField(DSL.name("PDS_DATETIME_1"), SQLDataType.TIMESTAMP(0).defaultValue(DSL.field("'2010-01-01 00:00:00'", SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_DATETIME_2</code>.
     */
    public final TableField<PdsCustomInfoRecord, Timestamp> PDS_DATETIME_2 = createField(DSL.name("PDS_DATETIME_2"), SQLDataType.TIMESTAMP(0).defaultValue(DSL.field("'2010-01-01 00:00:00'", SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_DATETIME_3</code>.
     */
    public final TableField<PdsCustomInfoRecord, Timestamp> PDS_DATETIME_3 = createField(DSL.name("PDS_DATETIME_3"), SQLDataType.TIMESTAMP(0).defaultValue(DSL.field("'2010-01-01 00:00:00'", SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_INT_1</code>.
     */
    public final TableField<PdsCustomInfoRecord, Integer> PDS_INT_1 = createField(DSL.name("PDS_INT_1"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_INT_2</code>.
     */
    public final TableField<PdsCustomInfoRecord, Integer> PDS_INT_2 = createField(DSL.name("PDS_INT_2"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_INT_3</code>.
     */
    public final TableField<PdsCustomInfoRecord, Integer> PDS_INT_3 = createField(DSL.name("PDS_INT_3"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_INT_4</code>.
     */
    public final TableField<PdsCustomInfoRecord, Integer> PDS_INT_4 = createField(DSL.name("PDS_INT_4"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_INT_5</code>.
     */
    public final TableField<PdsCustomInfoRecord, Integer> PDS_INT_5 = createField(DSL.name("PDS_INT_5"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_NUMBER_1</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_NUMBER_1 = createField(DSL.name("PDS_NUMBER_1"), SQLDataType.VARCHAR(15).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_NUMBER_2</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_NUMBER_2 = createField(DSL.name("PDS_NUMBER_2"), SQLDataType.VARCHAR(15).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_NUMBER_3</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_NUMBER_3 = createField(DSL.name("PDS_NUMBER_3"), SQLDataType.VARCHAR(15).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_NUMBER_4</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_NUMBER_4 = createField(DSL.name("PDS_NUMBER_4"), SQLDataType.VARCHAR(15).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_NUMBER_5</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_NUMBER_5 = createField(DSL.name("PDS_NUMBER_5"), SQLDataType.VARCHAR(15).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_STRING_1</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_STRING_1 = createField(DSL.name("PDS_STRING_1"), SQLDataType.VARCHAR(1000).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_STRING_2</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_STRING_2 = createField(DSL.name("PDS_STRING_2"), SQLDataType.VARCHAR(1000).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_STRING_3</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_STRING_3 = createField(DSL.name("PDS_STRING_3"), SQLDataType.VARCHAR(1000).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_STRING_4</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_STRING_4 = createField(DSL.name("PDS_STRING_4"), SQLDataType.VARCHAR(1000).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_STRING_5</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_STRING_5 = createField(DSL.name("PDS_STRING_5"), SQLDataType.VARCHAR(1000).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_STRING_6</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_STRING_6 = createField(DSL.name("PDS_STRING_6"), SQLDataType.VARCHAR(1000).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_STRING_7</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_STRING_7 = createField(DSL.name("PDS_STRING_7"), SQLDataType.VARCHAR(1000).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_STRING_8</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_STRING_8 = createField(DSL.name("PDS_STRING_8"), SQLDataType.VARCHAR(1000).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_STRING_9</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_STRING_9 = createField(DSL.name("PDS_STRING_9"), SQLDataType.VARCHAR(1000).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_STRING_10</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_STRING_10 = createField(DSL.name("PDS_STRING_10"), SQLDataType.VARCHAR(1000).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_STRING_11</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_STRING_11 = createField(DSL.name("PDS_STRING_11"), SQLDataType.VARCHAR(1000).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_STRING_12</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_STRING_12 = createField(DSL.name("PDS_STRING_12"), SQLDataType.VARCHAR(1000).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_STRING_13</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_STRING_13 = createField(DSL.name("PDS_STRING_13"), SQLDataType.VARCHAR(1000).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_STRING_14</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_STRING_14 = createField(DSL.name("PDS_STRING_14"), SQLDataType.VARCHAR(1000).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_STRING_15</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_STRING_15 = createField(DSL.name("PDS_STRING_15"), SQLDataType.VARCHAR(1000).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_STRING_16</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_STRING_16 = createField(DSL.name("PDS_STRING_16"), SQLDataType.VARCHAR(1000).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_STRING_17</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_STRING_17 = createField(DSL.name("PDS_STRING_17"), SQLDataType.VARCHAR(1000).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_STRING_18</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_STRING_18 = createField(DSL.name("PDS_STRING_18"), SQLDataType.VARCHAR(1000).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_STRING_19</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_STRING_19 = createField(DSL.name("PDS_STRING_19"), SQLDataType.VARCHAR(1000).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_STRING_20</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_STRING_20 = createField(DSL.name("PDS_STRING_20"), SQLDataType.VARCHAR(1000).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_CODE_1</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_CODE_1 = createField(DSL.name("PDS_CODE_1"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_CODE_2</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_CODE_2 = createField(DSL.name("PDS_CODE_2"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_CODE_3</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_CODE_3 = createField(DSL.name("PDS_CODE_3"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_CODE_4</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_CODE_4 = createField(DSL.name("PDS_CODE_4"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_CODE_5</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_CODE_5 = createField(DSL.name("PDS_CODE_5"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_CODE_6</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_CODE_6 = createField(DSL.name("PDS_CODE_6"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_CODE_7</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_CODE_7 = createField(DSL.name("PDS_CODE_7"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_CODE_8</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_CODE_8 = createField(DSL.name("PDS_CODE_8"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_CODE_9</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_CODE_9 = createField(DSL.name("PDS_CODE_9"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_CODE_10</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_CODE_10 = createField(DSL.name("PDS_CODE_10"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_MULTICODE_1</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_MULTICODE_1 = createField(DSL.name("PDS_MULTICODE_1"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_MULTICODE_2</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_MULTICODE_2 = createField(DSL.name("PDS_MULTICODE_2"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_MULTICODE_3</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_MULTICODE_3 = createField(DSL.name("PDS_MULTICODE_3"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_CONCODE_1</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_CONCODE_1 = createField(DSL.name("PDS_CONCODE_1"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_CONCODE_2</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_CONCODE_2 = createField(DSL.name("PDS_CONCODE_2"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_CONCODE_3</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_CONCODE_3 = createField(DSL.name("PDS_CONCODE_3"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_CSCODE_1</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_CSCODE_1 = createField(DSL.name("PDS_CSCODE_1"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_custom_info.PDS_CSCODE_2</code>.
     */
    public final TableField<PdsCustomInfoRecord, String> PDS_CSCODE_2 = createField(DSL.name("PDS_CSCODE_2"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

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

    @Override
    public Schema getSchema() {
        return Pds.PDS;
    }

    @NotNull
    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.PDS_CUSTOM_INFO_PDS_SYS_GROUP_ID);
    }

    @Override
    public UniqueKey<PdsCustomInfoRecord> getPrimaryKey() {
        return Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.PDS_SYS_CUSTOM_ID);
    }

    @NotNull
    @Override
    public List<UniqueKey<PdsCustomInfoRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.PDS_SYS_CUSTOM_ID));
    }

    @NotNull
    @Override
    public CommonPDSCustomInfo as(String alias) {
        return new CommonPDSCustomInfo(DSL.name(alias), this);
    }

    @NotNull
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
