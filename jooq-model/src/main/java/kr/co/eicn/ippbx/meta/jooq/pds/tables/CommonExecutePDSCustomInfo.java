package kr.co.eicn.ippbx.meta.jooq.pds.tables;

import kr.co.eicn.ippbx.meta.jooq.pds.Indexes;
import kr.co.eicn.ippbx.meta.jooq.pds.Pds;
import kr.co.eicn.ippbx.meta.jooq.pds.tables.records.ExecutePdsCustomInfoRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommonExecutePDSCustomInfo extends TableImpl<ExecutePdsCustomInfoRecord> {
    /**
     * The class holding records for this type
     */
    @NotNull
    @Override
    public Class<ExecutePdsCustomInfoRecord> getRecordType() {
        return ExecutePdsCustomInfoRecord.class;
    }

    /**
     * The column <code>PDS.execute_pds_custom_info.seq</code>.
     */
    public final TableField<ExecutePdsCustomInfoRecord, Integer> SEQ = createField(DSL.name("seq"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>PDS.execute_pds_custom_info.custom_id</code>.
     */
    public final TableField<ExecutePdsCustomInfoRecord, String> CUSTOM_ID = createField(DSL.name("custom_id"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.execute_pds_custom_info.execute_id</code>.
     */
    public final TableField<ExecutePdsCustomInfoRecord, String> EXECUTE_ID = createField(DSL.name("execute_id"), SQLDataType.VARCHAR(50).nullable(false).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.execute_pds_custom_info.pds_group_id</code>.
     */
    public final TableField<ExecutePdsCustomInfoRecord, Integer> PDS_GROUP_ID = createField(DSL.name("pds_group_id"), SQLDataType.INTEGER.nullable(false).defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>PDS.execute_pds_custom_info.number_field</code>.
     */
    public final TableField<ExecutePdsCustomInfoRecord, String> NUMBER_FIELD = createField(DSL.name("number_field"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.execute_pds_custom_info.field_seq</code>.
     */
    public final TableField<ExecutePdsCustomInfoRecord, Byte> FIELD_SEQ = createField(DSL.name("field_seq"), SQLDataType.TINYINT.defaultValue(DSL.field("0", SQLDataType.TINYINT)), this, "");

    /**
     * The column <code>PDS.execute_pds_custom_info.custom_number</code>.
     */
    public final TableField<ExecutePdsCustomInfoRecord, String> CUSTOM_NUMBER = createField(DSL.name("custom_number"), SQLDataType.VARCHAR(15).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.execute_pds_custom_info.rid</code>.
     */
    public final TableField<ExecutePdsCustomInfoRecord, String> RID = createField(DSL.name("rid"), SQLDataType.VARCHAR(15).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.execute_pds_custom_info.status</code>.
     */
    public final TableField<ExecutePdsCustomInfoRecord, String> STATUS = createField(DSL.name("status"), SQLDataType.CHAR(1).defaultValue(DSL.field("''", SQLDataType.CHAR)), this, "");

    /**
     * The column <code>PDS.execute_pds_custom_info.tts_data</code>.
     */
    public final TableField<ExecutePdsCustomInfoRecord, String> TTS_DATA = createField(DSL.name("tts_data"), SQLDataType.VARCHAR(500).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.execute_pds_custom_info.click_key</code>.
     */
    public final TableField<ExecutePdsCustomInfoRecord, String> CLICK_KEY = createField(DSL.name("click_key"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.execute_pds_custom_info.hangup_cause</code>.
     */
    public final TableField<ExecutePdsCustomInfoRecord, String> HANGUP_CAUSE = createField(DSL.name("hangup_cause"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.execute_pds_custom_info.hangup_msg</code>.
     */
    public final TableField<ExecutePdsCustomInfoRecord, String> HANGUP_MSG = createField(DSL.name("hangup_msg"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.execute_pds_custom_info.hangup_time</code>.
     */
    public final TableField<ExecutePdsCustomInfoRecord, Timestamp> HANGUP_TIME = createField(DSL.name("hangup_time"), SQLDataType.TIMESTAMP(0).nullable(false).defaultValue(DSL.field("'2010-01-01 00:00:00'", SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>PDS.execute_pds_custom_info.read_status</code>.
     */
    public final TableField<ExecutePdsCustomInfoRecord, String> READ_STATUS = createField(DSL.name("read_status"), SQLDataType.CHAR(1).defaultValue(DSL.field("''", SQLDataType.CHAR)), this, "");

    /**
     * The column <code>PDS.execute_pds_custom_info.company_id</code>.
     */
    public final TableField<ExecutePdsCustomInfoRecord, String> COMPANY_ID = createField(DSL.name("company_id"), SQLDataType.VARCHAR(30).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.execute_pds_custom_info.user_data1</code>.
     */
    public final TableField<ExecutePdsCustomInfoRecord, String> USER_DATA1 = createField(DSL.name("user_data1"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.execute_pds_custom_info.user_data2</code>.
     */
    public final TableField<ExecutePdsCustomInfoRecord, String> USER_DATA2 = createField(DSL.name("user_data2"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.execute_pds_custom_info.user_data3</code>.
     */
    public final TableField<ExecutePdsCustomInfoRecord, String> USER_DATA3 = createField(DSL.name("user_data3"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    private final String tableName;

    /**
     * Create a <code>PDS.execute_pds_custom_info</code> table reference
     */
    public CommonExecutePDSCustomInfo(String name) {
        this(DSL.name("execute_pds_custom_info_" + name), null);
    }

    /**
     * Create an aliased <code>PDS.execute_pds_custom_info</code> table reference
     */
    public CommonExecutePDSCustomInfo(String alias, Table<ExecutePdsCustomInfoRecord> aliased) {
        this(DSL.name(alias), aliased);
    }

    /**
     * Create an aliased <code>PDS.execute_pds_custom_info</code> table reference
     */
    private CommonExecutePDSCustomInfo(Name alias, Table<ExecutePdsCustomInfoRecord> aliased) {
        this(alias, aliased, null);
    }

    private CommonExecutePDSCustomInfo(Name alias, Table<ExecutePdsCustomInfoRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
        this.tableName = alias.last();
    }

    public <O extends Record> CommonExecutePDSCustomInfo(CommonExecutePDSCustomInfo table, Table<O> child, ForeignKey<O, ExecutePdsCustomInfoRecord> key) {
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
        return Arrays.<Index>asList(Indexes.EXECUTE_PDS_CUSTOM_INFO_CLICK_KEY, Indexes.EXECUTE_PDS_CUSTOM_INFO_COMPANY_ID, Indexes.EXECUTE_PDS_CUSTOM_INFO_CUSTOM_ID, Indexes.EXECUTE_PDS_CUSTOM_INFO_FIELD_SEQ, Indexes.EXECUTE_PDS_CUSTOM_INFO_PDS_GROUP_ID);
    }

    @Override
    public Identity<ExecutePdsCustomInfoRecord, Integer> getIdentity() {
        return Internal.createIdentity(this, this.SEQ);
    }

    @Override
    public UniqueKey<ExecutePdsCustomInfoRecord> getPrimaryKey() {
        return Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ);
    }

    @NotNull
    @Override
    public List<UniqueKey<ExecutePdsCustomInfoRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ));
    }

    @NotNull
    @Override
    public CommonExecutePDSCustomInfo as(String alias) {
        return new CommonExecutePDSCustomInfo(DSL.name(alias), this);
    }

    @NotNull
    @Override
    public CommonExecutePDSCustomInfo as(Name alias) {
        return new CommonExecutePDSCustomInfo(alias, this);
    }

    @Override
    public CommonExecutePDSCustomInfo rename(String name) {
        return new CommonExecutePDSCustomInfo(DSL.name(name), null);
    }

    @Override
    public CommonExecutePDSCustomInfo rename(Name name) {
        return new CommonExecutePDSCustomInfo(name, null);
    }
}
