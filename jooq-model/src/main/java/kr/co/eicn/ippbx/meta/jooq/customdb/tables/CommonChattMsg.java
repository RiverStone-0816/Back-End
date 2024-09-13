package kr.co.eicn.ippbx.meta.jooq.customdb.tables;

import kr.co.eicn.ippbx.meta.jooq.customdb.Customdb;
import kr.co.eicn.ippbx.meta.jooq.customdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.ChattMsgRecord;
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

public class CommonChattMsg extends TableImpl<ChattMsgRecord> {
    /**
     * The column <code>CUSTOMDB.chatt_msg.seq</code>.
     */
    public final TableField<ChattMsgRecord, Integer> SEQ = createField(DSL.name("seq"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_msg.room_id</code>.
     */
    public final TableField<ChattMsgRecord, String> ROOM_ID = createField(DSL.name("room_id"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_msg.userid</code>.
     */
    public final TableField<ChattMsgRecord, String> USERID = createField(DSL.name("userid"), SQLDataType.VARCHAR(30).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_msg.insert_time</code>.
     */
    public final TableField<ChattMsgRecord, Timestamp> INSERT_TIME = createField(DSL.name("insert_time"), SQLDataType.TIMESTAMP(0).nullable(false).defaultValue(DSL.field("'2009-07-01 00:00:00'", SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_msg.send_receive</code>.
     */
    public final TableField<ChattMsgRecord, String> SEND_RECEIVE = createField(DSL.name("send_receive"), SQLDataType.CHAR(3).defaultValue(DSL.field("''", SQLDataType.CHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_msg.message_id</code>.
     */
    public final TableField<ChattMsgRecord, String> MESSAGE_ID = createField(DSL.name("message_id"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_msg.type</code>.
     */
    public final TableField<ChattMsgRecord, String> TYPE = createField(DSL.name("type"), SQLDataType.VARCHAR(10).nullable(false).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_msg.content</code>.
     */
    public final TableField<ChattMsgRecord, String> CONTENT = createField(DSL.name("content"), SQLDataType.VARCHAR(1100).nullable(false).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    private final String tableName;

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
    public Identity<ChattMsgRecord, Integer> getIdentity() {
        return Internal.createIdentity(this, this.SEQ);
    }

    @NotNull
    @Override
    public List<UniqueKey<ChattMsgRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ));
    }

    @NotNull
    @Override
    public CommonChattMsg as(String alias) {
        return new CommonChattMsg(DSL.name(alias), this);
    }

    @NotNull
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
