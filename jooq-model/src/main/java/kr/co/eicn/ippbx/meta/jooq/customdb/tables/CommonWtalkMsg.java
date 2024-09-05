package kr.co.eicn.ippbx.meta.jooq.customdb.tables;

import kr.co.eicn.ippbx.meta.jooq.customdb.Customdb;
import kr.co.eicn.ippbx.meta.jooq.customdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.CommonWtalkMsgRecord;
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

public class CommonWtalkMsg extends TableImpl<CommonWtalkMsgRecord> {
    /**
     * The column <code>CUSTOMDB.wtalk_msg.seq</code>.
     */
    public final TableField<CommonWtalkMsgRecord, Integer> SEQ = createField(DSL.name("seq"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>CUSTOMDB.wtalk_msg.room_id</code>.
     */
    public final TableField<CommonWtalkMsgRecord, String> ROOM_ID = createField(DSL.name("room_id"), SQLDataType.VARCHAR(150).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.wtalk_msg.channel_type</code>.
     */
    public final TableField<CommonWtalkMsgRecord, String> CHANNEL_TYPE = createField(DSL.name("channel_type"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("'eicn'", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.wtalk_msg.insert_time</code>.
     */
    public final TableField<CommonWtalkMsgRecord, Timestamp> INSERT_TIME = createField(DSL.name("insert_time"), SQLDataType.TIMESTAMP(0).nullable(false).defaultValue(DSL.field("'2009-07-01 00:00:00'", SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.wtalk_msg.send_receive</code>.
     */
    public final TableField<CommonWtalkMsgRecord, String> SEND_RECEIVE = createField(DSL.name("send_receive"), SQLDataType.VARCHAR(10).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.wtalk_msg.company_id</code>.
     */
    public final TableField<CommonWtalkMsgRecord, String> COMPANY_ID = createField(DSL.name("company_id"), SQLDataType.VARCHAR(30).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.wtalk_msg.userid</code>.
     */
    public final TableField<CommonWtalkMsgRecord, String> USERID = createField(DSL.name("userid"), SQLDataType.VARCHAR(30).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.wtalk_msg.user_key</code>.
     */
    public final TableField<CommonWtalkMsgRecord, String> USER_KEY = createField(DSL.name("user_key"), SQLDataType.VARCHAR(100).nullable(false).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.wtalk_msg.sender_key</code>.
     */
    public final TableField<CommonWtalkMsgRecord, String> SENDER_KEY = createField(DSL.name("sender_key"), SQLDataType.VARCHAR(100).nullable(false).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.wtalk_msg.message_id</code>.
     */
    public final TableField<CommonWtalkMsgRecord, String> MESSAGE_ID = createField(DSL.name("message_id"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.wtalk_msg.time</code>.
     */
    public final TableField<CommonWtalkMsgRecord, String> TIME = createField(DSL.name("time"), SQLDataType.VARCHAR(30).nullable(false).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.wtalk_msg.type</code>.
     */
    public final TableField<CommonWtalkMsgRecord, String> TYPE = createField(DSL.name("type"), SQLDataType.VARCHAR(10).nullable(false).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.wtalk_msg.content</code>.
     */
    public final TableField<CommonWtalkMsgRecord, String> CONTENT = createField(DSL.name("content"), SQLDataType.VARCHAR(5000).defaultValue(DSL.field("NULL", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.wtalk_msg.attachment</code>.
     */
    public final TableField<CommonWtalkMsgRecord, String> ATTACHMENT = createField(DSL.name("attachment"), SQLDataType.VARCHAR(300).nullable(false).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.wtalk_msg.extra</code>.
     */
    public final TableField<CommonWtalkMsgRecord, String> EXTRA = createField(DSL.name("extra"), SQLDataType.VARCHAR(500).nullable(false).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.wtalk_msg.delete_request</code>.
     */
    public final TableField<CommonWtalkMsgRecord, String> DELETE_REQUEST = createField(DSL.name("delete_request"), SQLDataType.CHAR(1).defaultValue(DSL.field("''", SQLDataType.CHAR)), this, "");

    private String tableName;

    /**
     * Create an aliased <code>CUSTOMDB.wtalk_record</code> table reference
     */
    public CommonWtalkMsg(String tableName) {
        this(DSL.name("wtalk_msg_" + tableName), null);
    }

    /**
     * Create an aliased <code>CUSTOMDB.wtalk_record</code> table reference
     */
    private CommonWtalkMsg(Name alias, Table<CommonWtalkMsgRecord> aliased) {
        this(alias, aliased, null);
    }

    /**
     * Create an aliased <code>CUSTOMDB.wtalk_record</code> table reference
     */
    private CommonWtalkMsg(Name alias, Table<CommonWtalkMsgRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
        this.tableName = alias.last();
    }

    public <O extends Record> CommonWtalkMsg(CommonWtalkMsg table, Table<O> child, ForeignKey<O, CommonWtalkMsgRecord> key) {
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
        return Arrays.<Index>asList(Indexes.WTALK_MSG_COMPANY_ID, Indexes.WTALK_MSG_INSERT_TIME, Indexes.WTALK_MSG_ROOM_ID, Indexes.WTALK_MSG_USER_KEY, Indexes.WTALK_MSG_USERID);
    }

    @Override
    public Identity<CommonWtalkMsgRecord, Integer> getIdentity() {
        return Internal.createIdentity(this, this.SEQ);
    }

    @NotNull
    @Override
    public List<UniqueKey<CommonWtalkMsgRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ));
    }

    @NotNull
    @Override
    public CommonWtalkMsg as(String alias) {
        return new CommonWtalkMsg(DSL.name(alias), this);
    }

    @NotNull
    @Override
    public CommonWtalkMsg as(Name alias) {
        return new CommonWtalkMsg(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonWtalkMsg rename(String name) {
        return new CommonWtalkMsg(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonWtalkMsg rename(Name name) {
        return new CommonWtalkMsg(name, null);
    }
}
