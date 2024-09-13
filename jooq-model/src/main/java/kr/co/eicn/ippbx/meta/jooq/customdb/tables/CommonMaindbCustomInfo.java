package kr.co.eicn.ippbx.meta.jooq.customdb.tables;

import kr.co.eicn.ippbx.meta.jooq.customdb.Customdb;
import kr.co.eicn.ippbx.meta.jooq.customdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.MaindbCustomInfoRecord;
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

public class CommonMaindbCustomInfo extends TableImpl<MaindbCustomInfoRecord> {
    /**
     * The class holding records for this type
     */
    @NotNull
    @Override
    public Class<MaindbCustomInfoRecord> getRecordType() {
        return MaindbCustomInfoRecord.class;
    }

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_SYS_CUSTOM_ID</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_SYS_CUSTOM_ID = createField(DSL.name("MAINDB_SYS_CUSTOM_ID"), SQLDataType.VARCHAR(100).nullable(false).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_SYS_UPLOAD_DATE</code>.
     */
    public final TableField<MaindbCustomInfoRecord, Timestamp> MAINDB_SYS_UPLOAD_DATE = createField(DSL.name("MAINDB_SYS_UPLOAD_DATE"), SQLDataType.TIMESTAMP(0).defaultValue(DSL.field("'2010-01-01 00:00:00'", SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_SYS_UPDATE_DATE</code>.
     */
    public final TableField<MaindbCustomInfoRecord, Timestamp> MAINDB_SYS_UPDATE_DATE = createField(DSL.name("MAINDB_SYS_UPDATE_DATE"), SQLDataType.TIMESTAMP(0).defaultValue(DSL.field("'2010-01-01 00:00:00'", SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_SYS_GROUP_ID</code>.
     */
    public final TableField<MaindbCustomInfoRecord, Integer> MAINDB_SYS_GROUP_ID = createField(DSL.name("MAINDB_SYS_GROUP_ID"), SQLDataType.INTEGER.nullable(false).defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_SYS_GROUP_TYPE</code>.
     */
    public final TableField<MaindbCustomInfoRecord, Integer> MAINDB_SYS_GROUP_TYPE = createField(DSL.name("MAINDB_SYS_GROUP_TYPE"), SQLDataType.INTEGER.nullable(false).defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_SYS_RESULT_TYPE</code>.
     */
    public final TableField<MaindbCustomInfoRecord, Integer> MAINDB_SYS_RESULT_TYPE = createField(DSL.name("MAINDB_SYS_RESULT_TYPE"), SQLDataType.INTEGER.nullable(false).defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_SYS_DAMDANG_ID</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_SYS_DAMDANG_ID = createField(DSL.name("MAINDB_SYS_DAMDANG_ID"), SQLDataType.VARCHAR(20).nullable(false).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_SYS_LAST_RESULT_ID</code>.
     */
    public final TableField<MaindbCustomInfoRecord, Integer> MAINDB_SYS_LAST_RESULT_ID = createField(DSL.name("MAINDB_SYS_LAST_RESULT_ID"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_SYS_LAST_RESULT_DATE</code>.
     */
    public final TableField<MaindbCustomInfoRecord, Timestamp> MAINDB_SYS_LAST_RESULT_DATE = createField(DSL.name("MAINDB_SYS_LAST_RESULT_DATE"), SQLDataType.TIMESTAMP(0).defaultValue(DSL.field("'2010-01-01 00:00:00'", SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_SYS_COMPANY_ID</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_SYS_COMPANY_ID = createField(DSL.name("MAINDB_SYS_COMPANY_ID"), SQLDataType.VARCHAR(50).nullable(false).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_DATE_1</code>.
     */
    public final TableField<MaindbCustomInfoRecord, Date> MAINDB_DATE_1 = createField(DSL.name("MAINDB_DATE_1"), SQLDataType.DATE.defaultValue(DSL.field("'2010-01-01'", SQLDataType.DATE)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_DATE_2</code>.
     */
    public final TableField<MaindbCustomInfoRecord, Date> MAINDB_DATE_2 = createField(DSL.name("MAINDB_DATE_2"), SQLDataType.DATE.defaultValue(DSL.field("'2010-01-01'", SQLDataType.DATE)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_DATE_3</code>.
     */
    public final TableField<MaindbCustomInfoRecord, Date> MAINDB_DATE_3 = createField(DSL.name("MAINDB_DATE_3"), SQLDataType.DATE.defaultValue(DSL.field("'2010-01-01'", SQLDataType.DATE)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_DAY_1</code>.
     */
    public final TableField<MaindbCustomInfoRecord, Date> MAINDB_DAY_1 = createField(DSL.name("MAINDB_DAY_1"), SQLDataType.DATE.defaultValue(DSL.field("'2010-01-01'", SQLDataType.DATE)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_DAY_2</code>.
     */
    public final TableField<MaindbCustomInfoRecord, Date> MAINDB_DAY_2 = createField(DSL.name("MAINDB_DAY_2"), SQLDataType.DATE.defaultValue(DSL.field("'2010-01-01'", SQLDataType.DATE)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_DAY_3</code>.
     */
    public final TableField<MaindbCustomInfoRecord, Date> MAINDB_DAY_3 = createField(DSL.name("MAINDB_DAY_3"), SQLDataType.DATE.defaultValue(DSL.field("'2010-01-01'", SQLDataType.DATE)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_DATETIME_1</code>.
     */
    public final TableField<MaindbCustomInfoRecord, Timestamp> MAINDB_DATETIME_1 = createField(DSL.name("MAINDB_DATETIME_1"), SQLDataType.TIMESTAMP(0).defaultValue(DSL.field("'2010-01-01 00:00:00'", SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_DATETIME_2</code>.
     */
    public final TableField<MaindbCustomInfoRecord, Timestamp> MAINDB_DATETIME_2 = createField(DSL.name("MAINDB_DATETIME_2"), SQLDataType.TIMESTAMP(0).defaultValue(DSL.field("'2010-01-01 00:00:00'", SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_DATETIME_3</code>.
     */
    public final TableField<MaindbCustomInfoRecord, Timestamp> MAINDB_DATETIME_3 = createField(DSL.name("MAINDB_DATETIME_3"), SQLDataType.TIMESTAMP(0).defaultValue(DSL.field("'2010-01-01 00:00:00'", SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_INT_1</code>.
     */
    public final TableField<MaindbCustomInfoRecord, Integer> MAINDB_INT_1 = createField(DSL.name("MAINDB_INT_1"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_INT_2</code>.
     */
    public final TableField<MaindbCustomInfoRecord, Integer> MAINDB_INT_2 = createField(DSL.name("MAINDB_INT_2"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_INT_3</code>.
     */
    public final TableField<MaindbCustomInfoRecord, Integer> MAINDB_INT_3 = createField(DSL.name("MAINDB_INT_3"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_INT_4</code>.
     */
    public final TableField<MaindbCustomInfoRecord, Integer> MAINDB_INT_4 = createField(DSL.name("MAINDB_INT_4"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_INT_5</code>.
     */
    public final TableField<MaindbCustomInfoRecord, Integer> MAINDB_INT_5 = createField(DSL.name("MAINDB_INT_5"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_1</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_STRING_1 = createField(DSL.name("MAINDB_STRING_1"), SQLDataType.VARCHAR(1000).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_2</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_STRING_2 = createField(DSL.name("MAINDB_STRING_2"), SQLDataType.VARCHAR(1000).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_3</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_STRING_3 = createField(DSL.name("MAINDB_STRING_3"), SQLDataType.VARCHAR(1000).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_4</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_STRING_4 = createField(DSL.name("MAINDB_STRING_4"), SQLDataType.VARCHAR(1000).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_5</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_STRING_5 = createField(DSL.name("MAINDB_STRING_5"), SQLDataType.VARCHAR(1000).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_6</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_STRING_6 = createField(DSL.name("MAINDB_STRING_6"), SQLDataType.VARCHAR(1000).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_7</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_STRING_7 = createField(DSL.name("MAINDB_STRING_7"), SQLDataType.VARCHAR(1000).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_8</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_STRING_8 = createField(DSL.name("MAINDB_STRING_8"), SQLDataType.VARCHAR(1000).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_9</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_STRING_9 = createField(DSL.name("MAINDB_STRING_9"), SQLDataType.VARCHAR(1000).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_10</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_STRING_10 = createField(DSL.name("MAINDB_STRING_10"), SQLDataType.VARCHAR(500).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_11</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_STRING_11 = createField(DSL.name("MAINDB_STRING_11"), SQLDataType.VARCHAR(500).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_12</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_STRING_12 = createField(DSL.name("MAINDB_STRING_12"), SQLDataType.VARCHAR(500).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_13</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_STRING_13 = createField(DSL.name("MAINDB_STRING_13"), SQLDataType.VARCHAR(500).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_14</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_STRING_14 = createField(DSL.name("MAINDB_STRING_14"), SQLDataType.VARCHAR(500).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_15</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_STRING_15 = createField(DSL.name("MAINDB_STRING_15"), SQLDataType.VARCHAR(500).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_16</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_STRING_16 = createField(DSL.name("MAINDB_STRING_16"), SQLDataType.VARCHAR(500).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_17</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_STRING_17 = createField(DSL.name("MAINDB_STRING_17"), SQLDataType.VARCHAR(500).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_18</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_STRING_18 = createField(DSL.name("MAINDB_STRING_18"), SQLDataType.VARCHAR(500).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_19</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_STRING_19 = createField(DSL.name("MAINDB_STRING_19"), SQLDataType.VARCHAR(500).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_STRING_20</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_STRING_20 = createField(DSL.name("MAINDB_STRING_20"), SQLDataType.VARCHAR(500).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_CODE_1</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_CODE_1 = createField(DSL.name("MAINDB_CODE_1"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_CODE_2</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_CODE_2 = createField(DSL.name("MAINDB_CODE_2"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_CODE_3</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_CODE_3 = createField(DSL.name("MAINDB_CODE_3"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_CODE_4</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_CODE_4 = createField(DSL.name("MAINDB_CODE_4"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_CODE_5</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_CODE_5 = createField(DSL.name("MAINDB_CODE_5"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_CODE_6</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_CODE_6 = createField(DSL.name("MAINDB_CODE_6"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_CODE_7</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_CODE_7 = createField(DSL.name("MAINDB_CODE_7"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_CODE_8</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_CODE_8 = createField(DSL.name("MAINDB_CODE_8"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_CODE_9</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_CODE_9 = createField(DSL.name("MAINDB_CODE_9"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_CODE_10</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_CODE_10 = createField(DSL.name("MAINDB_CODE_10"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_MULTICODE_1</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_MULTICODE_1 = createField(DSL.name("MAINDB_MULTICODE_1"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_MULTICODE_2</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_MULTICODE_2 = createField(DSL.name("MAINDB_MULTICODE_2"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_MULTICODE_3</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_MULTICODE_3 = createField(DSL.name("MAINDB_MULTICODE_3"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_IMG_1</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_IMG_1 = createField(DSL.name("MAINDB_IMG_1"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_IMG_2</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_IMG_2 = createField(DSL.name("MAINDB_IMG_2"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_IMG_3</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_IMG_3 = createField(DSL.name("MAINDB_IMG_3"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_CONCODE_1</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_CONCODE_1 = createField(DSL.name("MAINDB_CONCODE_1"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_CONCODE_2</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_CONCODE_2 = createField(DSL.name("MAINDB_CONCODE_2"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_CONCODE_3</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_CONCODE_3 = createField(DSL.name("MAINDB_CONCODE_3"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_CSCODE_1</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_CSCODE_1 = createField(DSL.name("MAINDB_CSCODE_1"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_CSCODE_2</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_CSCODE_2 = createField(DSL.name("MAINDB_CSCODE_2"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_custom_info.MAINDB_CSCODE_3</code>.
     */
    public final TableField<MaindbCustomInfoRecord, String> MAINDB_CSCODE_3 = createField(DSL.name("MAINDB_CSCODE_3"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    private String tableName;

    /**
     * Create a <code>CUSTOMDB.maindb_custom_info</code> table reference
     */
    public CommonMaindbCustomInfo(String companyName) {
        this(DSL.name("maindb_custom_info_" + companyName), null);
    }

    /**
     * Create an aliased <code>CUSTOMDB.maindb_custom_info</code> table reference
     */
    public CommonMaindbCustomInfo(String alias, Table<MaindbCustomInfoRecord> aliased) {
        this(DSL.name(alias), aliased);
    }

    /**
     * Create an aliased <code>CUSTOMDB.maindb_custom_info</code> table reference
     */
    public CommonMaindbCustomInfo(Name alias, Table<MaindbCustomInfoRecord> aliased) {
        this(alias, aliased, null);
    }

    /**
     * Create an aliased <code>CUSTOMDB.maindb_multichannel_info_*</code> table reference
     */
    private CommonMaindbCustomInfo(Name alias, Table<MaindbCustomInfoRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
        this.tableName = alias.last();
    }

    public <O extends Record> CommonMaindbCustomInfo(CommonMaindbCustomInfo table, Table<O> child, ForeignKey<O, MaindbCustomInfoRecord> key) {
        super(child, key, table);
        this.tableName = table.getName();
    }

    @Override
    public Schema getSchema() {
        return Customdb.CUSTOMDB;
    }

    @NotNull
    @Override
    public List<Index> getIndexes() {
        return Arrays.asList();
    }

    @Override
    public UniqueKey<MaindbCustomInfoRecord> getPrimaryKey() {
        return Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.MAINDB_SYS_CUSTOM_ID);
    }

    @NotNull
    @Override
    public List<UniqueKey<MaindbCustomInfoRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.MAINDB_SYS_CUSTOM_ID));
    }

    @NotNull
    @Override
    public CommonMaindbCustomInfo as(String alias) {
        return new CommonMaindbCustomInfo(DSL.name(alias), this);
    }

    @NotNull
    @Override
    public CommonMaindbCustomInfo as(Name alias) {
        return new CommonMaindbCustomInfo(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonMaindbCustomInfo rename(String name) {
        return new CommonMaindbCustomInfo(DSL.name(name), null);
    }


    /**
     * Rename this table
     */
    @Override
    public CommonMaindbCustomInfo rename(Name name) {
        return new CommonMaindbCustomInfo(name, null);
    }
}
