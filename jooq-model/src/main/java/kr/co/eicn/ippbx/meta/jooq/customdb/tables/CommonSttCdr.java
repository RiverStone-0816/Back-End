package kr.co.eicn.ippbx.meta.jooq.customdb.tables;

import kr.co.eicn.ippbx.meta.jooq.customdb.Customdb;
import kr.co.eicn.ippbx.meta.jooq.customdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.customdb.Keys;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.SttCdrRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

public class CommonSttCdr extends TableImpl<SttCdrRecord> {
    /**
     * The column <code>CUSTOMDB.stt_cdr.start_time</code>. 통화시작
     */
    public final TableField<SttCdrRecord, Timestamp> START_TIME = createField(DSL.name("start_time"), SQLDataType.TIMESTAMP(0).defaultValue(DSL.field("'2010-01-01 01:00:00'", SQLDataType.TIMESTAMP)), this, "통화시작");

    /**
     * The column <code>CUSTOMDB.stt_cdr.end_time</code>. 통화종료
     */
    public final TableField<SttCdrRecord, Timestamp> END_TIME = createField(DSL.name("end_time"), SQLDataType.TIMESTAMP(0).defaultValue(DSL.field("'2010-01-01 01:00:00'", SQLDataType.TIMESTAMP)), this, "통화종료");

    /**
     * The column <code>CUSTOMDB.stt_cdr.call_status</code>.
     */
    public final TableField<SttCdrRecord, String> CALL_STATUS = createField(DSL.name("call_status"), SQLDataType.CHAR(1).defaultValue(DSL.field("'D'", SQLDataType.CHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.stt_cdr.call_uniqueid</code>.
     */
    public final TableField<SttCdrRecord, String> CALL_UNIQUEID = createField(DSL.name("call_uniqueid"), SQLDataType.VARCHAR(50).nullable(false).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.stt_cdr.in_out</code>.
     */
    public final TableField<SttCdrRecord, String> IN_OUT = createField(DSL.name("in_out"), SQLDataType.VARCHAR(5).defaultValue(DSL.field("'I'", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.stt_cdr.ipcc_userid</code>.
     */
    public final TableField<SttCdrRecord, String> IPCC_USERID = createField(DSL.name("ipcc_userid"), SQLDataType.VARCHAR(50).nullable(false).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.stt_cdr.ipcc_username</code>.
     */
    public final TableField<SttCdrRecord, String> IPCC_USERNAME = createField(DSL.name("ipcc_username"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.stt_cdr.custom_number</code>.
     */
    public final TableField<SttCdrRecord, String> CUSTOM_NUMBER = createField(DSL.name("custom_number"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.stt_cdr.extension</code>.
     */
    public final TableField<SttCdrRecord, String> EXTENSION = createField(DSL.name("extension"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.stt_cdr.service_number</code>.
     */
    public final TableField<SttCdrRecord, String> SERVICE_NUMBER = createField(DSL.name("service_number"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.stt_cdr.message_yn</code>.
     */
    public final TableField<SttCdrRecord, String> MESSAGE_YN = createField(DSL.name("message_yn"), SQLDataType.CHAR(1).defaultValue(DSL.field("''", SQLDataType.CHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.stt_cdr.company_id</code>. 컴퍼니아이디
     */
    public final TableField<SttCdrRecord, String> COMPANY_ID = createField(DSL.name("company_id"), SQLDataType.VARCHAR(30).nullable(false).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "컴퍼니아이디");

    /**
     * The column <code>CUSTOMDB.stt_cdr.etc1</code>.
     */
    public final TableField<SttCdrRecord, String> ETC1 = createField(DSL.name("etc1"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.stt_cdr.etc2</code>.
     */
    public final TableField<SttCdrRecord, String> ETC2 = createField(DSL.name("etc2"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.stt_cdr.etc3</code>.
     */
    public final TableField<SttCdrRecord, String> ETC3 = createField(DSL.name("etc3"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    private final String tableName;

    /**
     * Create a <code>CUSTOMDB.talk_room</code> table reference
     */
    public CommonSttCdr(String tableName) {
        this(DSL.name("stt_cdr_" + tableName), null);
    }

    private CommonSttCdr(Name alias, Table<SttCdrRecord> aliased) {
        this(alias, aliased, null);
    }

    private CommonSttCdr(Name alias, Table<SttCdrRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
        this.tableName = alias.last();
    }

    public <O extends Record> CommonSttCdr(CommonSttCdr table, Table<O> child, ForeignKey<O, SttCdrRecord> key) {
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
        return Arrays.<Index>asList(Indexes.STT_CDR_CALL_UNIQUEID, Indexes.STT_CDR_CUSTOM_NUMBER, Indexes.STT_CDR_END_TIME, Indexes.STT_CDR_EXTENSION, Indexes.STT_CDR_IPCC_USERID, Indexes.STT_CDR_START_TIME);
    }

    @Override
    public UniqueKey<SttCdrRecord> getPrimaryKey() {
        return Keys.KEY_STT_CDR_PRIMARY;
    }

    @NotNull
    @Override
    public List<UniqueKey<SttCdrRecord>> getKeys() {
        return Arrays.<UniqueKey<SttCdrRecord>>asList(Keys.KEY_STT_CDR_PRIMARY);
    }

    @NotNull
    @Override
    public CommonSttCdr as(String alias) {
        return new CommonSttCdr(DSL.name(alias), this);
    }

    @NotNull
    @Override
    public CommonSttCdr as(Name alias) {
        return new CommonSttCdr(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonSttCdr rename(String name) {
        return new CommonSttCdr(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonSttCdr rename(Name name) {
        return new CommonSttCdr(name, null);
    }
}
