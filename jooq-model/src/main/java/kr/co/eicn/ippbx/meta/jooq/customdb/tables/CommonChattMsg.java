package kr.co.eicn.ippbx.meta.jooq.customdb.tables;

import kr.co.eicn.ippbx.meta.jooq.customdb.Customdb;
import kr.co.eicn.ippbx.meta.jooq.customdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.customdb.Keys;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.ChattMsgRecord;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

public class CommonChattMsg extends TableImpl<ChattMsgRecord> {

    public static final ChattMsg CHATT_MSG = new ChattMsg();
    /**
     * The column <code>CUSTOMDB.chatt_msg.seq</code>.
     */
    public final TableField<ChattMsgRecord, Integer> SEQ = createField(DSL.name("seq"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "");
    /**
     * The column <code>CUSTOMDB.chatt_msg.room_id</code>.
     */
    public final TableField<ChattMsgRecord, String> ROOM_ID = createField(DSL.name("room_id"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>CUSTOMDB.chatt_msg.userid</code>.
     */
    public final TableField<ChattMsgRecord, String> USERID = createField(DSL.name("userid"), org.jooq.impl.SQLDataType.VARCHAR(30).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>CUSTOMDB.chatt_msg.insert_time</code>.
     */
    public final TableField<ChattMsgRecord, Timestamp> INSERT_TIME = createField(DSL.name("insert_time"), org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(org.jooq.impl.DSL.inline("2009-07-01 00:00:00", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");
    /**
     * The column <code>CUSTOMDB.chatt_msg.send_receive</code>.
     */
    public final TableField<ChattMsgRecord, String> SEND_RECEIVE = createField(DSL.name("send_receive"), org.jooq.impl.SQLDataType.CHAR(3).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.CHAR)), this, "");
    /**
     * The column <code>CUSTOMDB.chatt_msg.message_id</code>.
     */
    public final TableField<ChattMsgRecord, String> MESSAGE_ID = createField(DSL.name("message_id"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>CUSTOMDB.chatt_msg.type</code>.
     */
    public final TableField<ChattMsgRecord, String> TYPE = createField(DSL.name("type"), org.jooq.impl.SQLDataType.VARCHAR(10).nullable(false).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>CUSTOMDB.chatt_msg.content</code>.
     */
    public final TableField<ChattMsgRecord, String> CONTENT = createField(DSL.name("content"), org.jooq.impl.SQLDataType.VARCHAR(1100).nullable(false).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    private String tableName;
    /**
     * Create a <code>CUSTOMDB.chatt_msg</code> table reference
     */
    public CommonChattMsg(String companyName) {
        this(DSL.name("chatt_msg_" + companyName), null);
    }

    /**
     * Create an aliased <code>CUSTOMDB.chatt_msg</code> table reference
     */
    public CommonChattMsg(String alias, Table<ChattMsgRecord> aliased) {
        this(DSL.name(alias), aliased);
    }

    /**
     * Create an aliased <code>CUSTOMDB.chatt_msg</code> table reference
     */
    public CommonChattMsg(Name alias) {
        this(alias, CHATT_MSG);
    }

    private CommonChattMsg(Name alias, Table<ChattMsgRecord> aliased) {
        this(alias, aliased, null);
    }

    private CommonChattMsg(Name alias, Table<ChattMsgRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
        this.tableName = alias.last();
    }

    public <O extends Record> CommonChattMsg(CommonChattMsg table, Table<O> child, ForeignKey<O, ChattMsgRecord> key) {
        super(child, key, table);
        this.tableName = table.getName();
    }

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ChattMsgRecord> getRecordType() {
        return ChattMsgRecord.class;
    }

    @Override
    public Schema getSchema() {
        return Customdb.CUSTOMDB;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.CHATT_MSG_INSERT_TIME, Indexes.CHATT_MSG_ROOM_ID, Indexes.CHATT_MSG_USERID);
    }

    @Override
    public Identity<ChattMsgRecord, Integer> getIdentity() {
        return Keys.IDENTITY_CHATT_MSG;
    }

    @Override
    public List<UniqueKey<ChattMsgRecord>> getKeys() {
        return Arrays.<UniqueKey<ChattMsgRecord>>asList(Keys.KEY_CHATT_MSG_SEQ);
    }

    @Override
    public CommonChattMsg as(String alias) {
        return new CommonChattMsg(DSL.name(alias), this);
    }

    @Override
    public CommonChattMsg as(Name alias) {
        return new CommonChattMsg(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonChattMsg rename(String name) {
        return new CommonChattMsg(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonChattMsg rename(Name name) {
        return new CommonChattMsg(name, null);
    }
}
