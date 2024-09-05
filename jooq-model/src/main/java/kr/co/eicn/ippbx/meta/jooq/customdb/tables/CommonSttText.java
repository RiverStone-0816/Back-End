package kr.co.eicn.ippbx.meta.jooq.customdb.tables;

import kr.co.eicn.ippbx.meta.jooq.customdb.Customdb;
import kr.co.eicn.ippbx.meta.jooq.customdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.SttTextRecord;
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

public class CommonSttText extends TableImpl<SttTextRecord> {
    /**
     * The column <code>CUSTOMDB.stt_text.seq</code>.
     */
    public final TableField<SttTextRecord, Integer> SEQ = createField(DSL.name("seq"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>CUSTOMDB.stt_text.message_id</code>.
     */
    public final TableField<SttTextRecord, String> MESSAGE_ID = createField(DSL.name("message_id"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.stt_text.insert_time</code>.
     */
    public final TableField<SttTextRecord, Timestamp> INSERT_TIME = createField(DSL.name("insert_time"), SQLDataType.TIMESTAMP(0).nullable(false).defaultValue(DSL.field("'2009-07-01 00:00:00'", SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.stt_text.call_uniqueid</code>.
     */
    public final TableField<SttTextRecord, String> CALL_UNIQUEID = createField(DSL.name("call_uniqueid"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.stt_text.ipcc_userid</code>.
     */
    public final TableField<SttTextRecord, String> IPCC_USERID = createField(DSL.name("ipcc_userid"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.stt_text.my_extension</code>.
     */
    public final TableField<SttTextRecord, String> MY_EXTENSION = createField(DSL.name("my_extension"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.stt_text.kind</code>.
     */
    public final TableField<SttTextRecord, String> KIND = createField(DSL.name("kind"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.stt_text.text</code>.
     */
    public final TableField<SttTextRecord, String> TEXT = createField(DSL.name("text"), SQLDataType.VARCHAR(500).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.stt_text.start_ms</code>.
     */
    public final TableField<SttTextRecord, Integer> START_MS = createField(DSL.name("start_ms"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.stt_text.stop_ms</code>.
     */
    public final TableField<SttTextRecord, Integer> STOP_MS = createField(DSL.name("stop_ms"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.stt_text.kms_keyword</code>.
     */
    public final TableField<SttTextRecord, String> KMS_KEYWORD = createField(DSL.name("kms_keyword"), SQLDataType.VARCHAR(500).nullable(false).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.stt_text.remind_yn</code>.
     */
    public final TableField<SttTextRecord, String> REMIND_YN = createField(DSL.name("remind_yn"), SQLDataType.CHAR(1).defaultValue(DSL.field("'N'", SQLDataType.CHAR)), this, "");

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

    @Override
    public Schema getSchema() {
        return Customdb.CUSTOMDB;
    }

    @NotNull
    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.STT_TEXT_CALL_UNIQUEID, Indexes.STT_TEXT_INSERT_TIME, Indexes.STT_TEXT_IPCC_USERID, Indexes.STT_TEXT_MESSAGE_ID, Indexes.STT_TEXT_MY_EXTENSION, Indexes.STT_TEXT_REMIND_YN);
    }

    @Override
    public Identity<SttTextRecord, Integer> getIdentity() {
        return Internal.createIdentity(this, this.SEQ);
    }

    @NotNull
    @Override
    public List<UniqueKey<SttTextRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ));
    }

    @NotNull
    @Override
    public CommonSttText as(String alias) {
        return new CommonSttText(DSL.name(alias), this);
    }

    @NotNull
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
