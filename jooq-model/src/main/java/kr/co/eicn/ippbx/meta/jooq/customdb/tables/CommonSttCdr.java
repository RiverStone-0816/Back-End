package kr.co.eicn.ippbx.meta.jooq.customdb.tables;

import kr.co.eicn.ippbx.meta.jooq.customdb.Customdb;
import kr.co.eicn.ippbx.meta.jooq.customdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.SttCdrRecord;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.SttCdrRecord;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommonSttCdr extends TableImpl<SttCdrRecord> {
    public final TableField<SttCdrRecord, Timestamp> START_TIME = createField(DSL.name("start_time"), SQLDataType.TIMESTAMP(0).defaultValue(DSL.inline("'2010-01-01 01:00:00'", SQLDataType.TIMESTAMP)), this, "통화시작");

    /**
     * The column <code>CUSTOMDB.stt_cdr.end_time</code>. 통화종료
     */
    public final TableField<SttCdrRecord, Timestamp> END_TIME = createField(DSL.name("end_time"), SQLDataType.TIMESTAMP(0).defaultValue(DSL.inline("'2010-01-01 01:00:00'", SQLDataType.TIMESTAMP)), this, "통화종료");

    /**
     * The column <code>CUSTOMDB.stt_cdr.call_status</code>.
     */
    public final TableField<SttCdrRecord, String> CALL_STATUS = createField(DSL.name("call_status"), SQLDataType.CHAR(1).defaultValue(DSL.inline("'D'", SQLDataType.CHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.stt_cdr.call_uniqueid</code>.
     */
    public final TableField<SttCdrRecord, String> CALL_UNIQUEID = createField(DSL.name("call_uniqueid"), SQLDataType.VARCHAR(50).nullable(false).defaultValue(DSL.inline("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.stt_cdr.in_out</code>.
     */
    public final TableField<SttCdrRecord, String> IN_OUT = createField(DSL.name("in_out"), SQLDataType.VARCHAR(5).defaultValue(DSL.inline("'I'", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.stt_cdr.ipcc_userid</code>.
     */
    public final TableField<SttCdrRecord, String> IPCC_USERID = createField(DSL.name("ipcc_userid"), SQLDataType.VARCHAR(50).nullable(false).defaultValue(DSL.inline("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.stt_cdr.ipcc_username</code>.
     */
    public final TableField<SttCdrRecord, String> IPCC_USERNAME = createField(DSL.name("ipcc_username"), SQLDataType.VARCHAR(50).defaultValue(DSL.inline("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.stt_cdr.custom_number</code>.
     */
    public final TableField<SttCdrRecord, String> CUSTOM_NUMBER = createField(DSL.name("custom_number"), SQLDataType.VARCHAR(20).defaultValue(DSL.inline("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.stt_cdr.extension</code>.
     */
    public final TableField<SttCdrRecord, String> EXTENSION = createField(DSL.name("extension"), SQLDataType.VARCHAR(20).defaultValue(DSL.inline("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.stt_cdr.service_number</code>.
     */
    public final TableField<SttCdrRecord, String> SERVICE_NUMBER = createField(DSL.name("service_number"), SQLDataType.VARCHAR(20).defaultValue(DSL.inline("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.stt_cdr.message_yn</code>.
     */
    public final TableField<SttCdrRecord, String> MESSAGE_YN = createField(DSL.name("message_yn"), SQLDataType.CHAR(1).defaultValue(DSL.inline("''", SQLDataType.CHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.stt_cdr.company_id</code>. 컴퍼니아이디
     */
    public final TableField<SttCdrRecord, String> COMPANY_ID = createField(DSL.name("company_id"), SQLDataType.VARCHAR(30).nullable(false).defaultValue(DSL.inline("''", SQLDataType.VARCHAR)), this, "컴퍼니아이디");

    /**
     * The column <code>CUSTOMDB.stt_cdr.etc1</code>.
     */
    public final TableField<SttCdrRecord, String> ETC1 = createField(DSL.name("etc1"), SQLDataType.VARCHAR(100).defaultValue(DSL.inline("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.stt_cdr.etc2</code>.
     */
    public final TableField<SttCdrRecord, String> ETC2 = createField(DSL.name("etc2"), SQLDataType.VARCHAR(100).defaultValue(DSL.inline("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.stt_cdr.etc3</code>.
     */
    public final TableField<SttCdrRecord, String> ETC3 = createField(DSL.name("etc3"), SQLDataType.VARCHAR(100).defaultValue(DSL.inline("''", SQLDataType.VARCHAR)), this, "");


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

    /**
     * The class holding records for this type
     */
    @Override
    public Class<SttCdrRecord> getRecordType() {
        return SttCdrRecord.class;
    }

    @Override
    public Schema getSchema() {
        return Customdb.CUSTOMDB;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.asList(Indexes.WTALK_ROOM_COMPANY_ID, Indexes.WTALK_ROOM_ROOM_ID, Indexes.WTALK_ROOM_SENDER_KEY, Indexes.WTALK_ROOM_USER_KEY);
    }

    @Override
    public Identity<SttCdrRecord, Integer> getIdentity() {
        return (Identity<SttCdrRecord, Integer>) super.getIdentity();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<SttCdrRecord> getPrimaryKey() {
        return org.jooq.impl.Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.COMPANY_ID, this.IPCC_USERID, this.CALL_UNIQUEID);
    }

    @Override
    public List<UniqueKey<SttCdrRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.COMPANY_ID, this.IPCC_USERID, this.CALL_UNIQUEID));
    }

    @Override
    public CommonSttCdr as(String alias) {
        return new CommonSttCdr(DSL.name(alias), this);
    }

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
