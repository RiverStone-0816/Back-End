package kr.co.eicn.ippbx.meta.jooq.customdb.tables;

import kr.co.eicn.ippbx.meta.jooq.customdb.Customdb;
import kr.co.eicn.ippbx.meta.jooq.customdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.ChattMsgReadRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import java.util.Arrays;
import java.util.List;

public class CommonChattMsgRead extends TableImpl<ChattMsgReadRecord> {
    /**
     * The column <code>CUSTOMDB.chatt_msg_read.message_id</code>.
     */
    public final TableField<ChattMsgReadRecord, String> MESSAGE_ID = createField(DSL.name("message_id"), SQLDataType.VARCHAR(100).nullable(false).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_msg_read.room_id</code>.
     */
    public final TableField<ChattMsgReadRecord, String> ROOM_ID = createField(DSL.name("room_id"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_msg_read.userid</code>.
     */
    public final TableField<ChattMsgReadRecord, String> USERID = createField(DSL.name("userid"), SQLDataType.VARCHAR(30).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    private String tableName;

    /**
     * Create a <code>CUSTOMDB.chatt_msg_read</code> table reference
     */
    public CommonChattMsgRead(String companyName) {
        this(DSL.name("chatt_msg_read_" + companyName), null);
    }

    private CommonChattMsgRead(Name alias, Table<ChattMsgReadRecord> aliased) {
        this(alias, aliased, null);
    }

    private CommonChattMsgRead(Name alias, Table<ChattMsgReadRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
        this.tableName = alias.last();
    }

    public <O extends Record> CommonChattMsgRead(CommonChattMsgRead table, Table<O> child, ForeignKey<O, ChattMsgReadRecord> key) {
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
        return Arrays.<Index>asList(Indexes.CHATT_MSG_READ_MESSAGE_ID, Indexes.CHATT_MSG_READ_ROOM_ID, Indexes.CHATT_MSG_READ_USERID);
    }

    @NotNull
    @Override
    public CommonChattMsgRead as(String alias) {
        return new CommonChattMsgRead(DSL.name(alias), this);
    }

    @NotNull
    @Override
    public CommonChattMsgRead as(Name alias) {
        return new CommonChattMsgRead(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonChattMsgRead rename(String name) {
        return new CommonChattMsgRead(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonChattMsgRead rename(Name name) {
        return new CommonChattMsgRead(name, null);
    }
}

