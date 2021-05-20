package kr.co.eicn.ippbx.meta.jooq.customdb.tables;

import kr.co.eicn.ippbx.meta.jooq.customdb.Customdb;
import kr.co.eicn.ippbx.meta.jooq.customdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.ChattMsgReadRecord;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import java.util.Arrays;
import java.util.List;

public class CommonChattMsgRead extends TableImpl<ChattMsgReadRecord> {
    /**
     * The reference instance of <code>CUSTOMDB.chatt_msg_read</code>
     */
    public static final ChattMsgRead CHATT_MSG_READ = new ChattMsgRead();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ChattMsgReadRecord> getRecordType() {
        return ChattMsgReadRecord.class;
    }

    /**
     * The column <code>CUSTOMDB.chatt_msg_read.message_id</code>.
     */
    public final TableField<ChattMsgReadRecord, String> MESSAGE_ID = createField(DSL.name("message_id"), org.jooq.impl.SQLDataType.VARCHAR(100).nullable(false).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_msg_read.room_id</code>.
     */
    public final TableField<ChattMsgReadRecord, String> ROOM_ID = createField(DSL.name("room_id"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_msg_read.userid</code>.
     */
    public final TableField<ChattMsgReadRecord, String> USERID = createField(DSL.name("userid"), org.jooq.impl.SQLDataType.VARCHAR(30).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
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

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.CHATT_MSG_READ_MESSAGE_ID, Indexes.CHATT_MSG_READ_ROOM_ID, Indexes.CHATT_MSG_READ_USERID);
    }

    @Override
    public CommonChattMsgRead as(String alias) {
        return new CommonChattMsgRead(DSL.name(alias), this);
    }

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

