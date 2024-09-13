package kr.co.eicn.ippbx.meta.jooq.customdb.tables;

import kr.co.eicn.ippbx.meta.jooq.customdb.Customdb;
import kr.co.eicn.ippbx.meta.jooq.customdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.SttMessageRecord;
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

public class CommonSttMessage extends TableImpl<SttMessageRecord> {
    /**
     * The column <code>CUSTOMDB.stt_message.seq</code>.
     */
    public final TableField<SttMessageRecord, Integer> SEQ = createField(DSL.name("seq"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>CUSTOMDB.stt_message.message_id</code>.
     */
    public final TableField<SttMessageRecord, String> MESSAGE_ID = createField(DSL.name("message_id"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.stt_message.insert_time</code>.
     */
    public final TableField<SttMessageRecord, Timestamp> INSERT_TIME = createField(DSL.name("insert_time"), SQLDataType.TIMESTAMP(0).nullable(false).defaultValue(DSL.field("'2009-07-01 00:00:00'", SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.stt_message.call_uniqueid</code>.
     */
    public final TableField<SttMessageRecord, String> CALL_UNIQUEID = createField(DSL.name("call_uniqueid"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.stt_message.userid</code>.
     */
    public final TableField<SttMessageRecord, String> USERID = createField(DSL.name("userid"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.stt_message.target_userid</code>.
     */
    public final TableField<SttMessageRecord, String> TARGET_USERID = createField(DSL.name("target_userid"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.stt_message.message</code>.
     */
    public final TableField<SttMessageRecord, String> MESSAGE = createField(DSL.name("message"), SQLDataType.VARCHAR(500).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    private String tableName;

    /**
     * Create a <code>CUSTOMDB.talk_room</code> table reference
     */
    public CommonSttMessage(String tableName) {
        this(DSL.name("stt_message_" + tableName), null);
    }

    private CommonSttMessage(Name alias, Table<SttMessageRecord> aliased) {
        this(alias, aliased, null);
    }

    private CommonSttMessage(Name alias, Table<SttMessageRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
        this.tableName = alias.last();
    }

    public <O extends Record> CommonSttMessage(CommonSttMessage table, Table<O> child, ForeignKey<O, SttMessageRecord> key) {
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
    public Identity<SttMessageRecord, Integer> getIdentity() {
        return Internal.createIdentity(this, this.SEQ);
    }

    @NotNull
    @Override
    public List<UniqueKey<SttMessageRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ));
    }

    @NotNull
    @Override
    public CommonSttMessage as(String alias) {
        return new CommonSttMessage(DSL.name(alias), this);
    }

    @NotNull
    @Override
    public CommonSttMessage as(Name alias) {
        return new CommonSttMessage(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonSttMessage rename(String name) {
        return new CommonSttMessage(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonSttMessage rename(Name name) {
        return new CommonSttMessage(name, null);
    }
}
