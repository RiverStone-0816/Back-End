package kr.co.eicn.ippbx.meta.jooq.customdb.tables;

import io.swagger.models.auth.In;
import kr.co.eicn.ippbx.meta.jooq.customdb.Customdb;
import kr.co.eicn.ippbx.meta.jooq.customdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.customdb.Keys;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.SttTextRecord;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.jooq.impl.TableImpl;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommonSttText extends TableImpl<SttTextRecord> {

    public final TableField<SttTextRecord, Integer> SEQ = createField(DSL.name("seq"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "");
    public final TableField<SttTextRecord, String> MESSAGE_ID = createField(DSL.name("message_id"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    public final TableField<SttTextRecord, Timestamp> INSERT_TIME = createField(DSL.name("insert_time"), org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(DSL.inline("'2009-07-01 00:00:00'", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");
    public final TableField<SttTextRecord, String> CALL_UNIQUEID = createField(DSL.name("call_uniqueid"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(DSL.inline("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    public final TableField<SttTextRecord, String> IPCC_USERID = createField(DSL.name("ipcc_userid"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(DSL.inline("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    public final TableField<SttTextRecord, String> MY_EXTENSION = createField(DSL.name("my_extension"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(DSL.inline("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    public final TableField<SttTextRecord, String> KIND = createField(DSL.name("kind"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(DSL.inline("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    public final TableField<SttTextRecord, String> TEXT = createField(DSL.name("text"), org.jooq.impl.SQLDataType.VARCHAR(500).defaultValue(DSL.inline("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    public final TableField<SttTextRecord, Integer> START_MS = createField(DSL.name("start_ms"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");
    public final TableField<SttTextRecord, Integer> STOP_MS = createField(DSL.name("stop_ms"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");
    private final TableField<SttTextRecord, String> KMS_KEYWORD = createField(DSL.name("kms_keyword"), org.jooq.impl.SQLDataType.VARCHAR(500).nullable(false).defaultValue(DSL.inline("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    public final TableField<SttTextRecord, String> REMIND_YN = createField(DSL.name("remind_yn"), org.jooq.impl.SQLDataType.CHAR(1).defaultValue(org.jooq.impl.DSL.inline("'N'", org.jooq.impl.SQLDataType.CHAR)), this, "");

    private String tableName;

    /**
     * Create a <code>CUSTOMDB.talk_room</code> table reference
     */
    public CommonSttText(String tableName) {
        this(DSL.name("stt_text_" + tableName), null);
    }

    private CommonSttText(Name alias, Table<SttTextRecord> aliased) {
        this(alias, aliased, null);
    }

    private CommonSttText(Name alias, Table<SttTextRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
        this.tableName = alias.last();
    }

    public <O extends Record> CommonSttText(CommonSttText table, Table<O> child, ForeignKey<O, SttTextRecord> key) {
        super(child, key, table);
        this.tableName = table.getName();
    }

    /**
     * The class holding records for this type
     */
    @Override
    public Class<SttTextRecord> getRecordType() {
        return SttTextRecord.class;
    }

    @Override
    public Schema getSchema() {
        return Customdb.CUSTOMDB;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.WTALK_ROOM_COMPANY_ID, Indexes.WTALK_ROOM_ROOM_ID, Indexes.WTALK_ROOM_SENDER_KEY, Indexes.WTALK_ROOM_USER_KEY);
    }

    @Override
    public Identity<SttTextRecord, Integer> getIdentity() {
        return (Identity<SttTextRecord, Integer>) super.getIdentity();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<SttTextRecord> getPrimaryKey() {
        return org.jooq.impl.Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ);
    }

    @Override
    public List<UniqueKey<SttTextRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ));
    }

    @Override
    public CommonSttText as(String alias) {
        return new CommonSttText(DSL.name(alias), this);
    }

    @Override
    public CommonSttText as(Name alias) {
        return new CommonSttText(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonSttText rename(String name) {
        return new CommonSttText(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonSttText rename(Name name) {
        return new CommonSttText(name, null);
    }
}
