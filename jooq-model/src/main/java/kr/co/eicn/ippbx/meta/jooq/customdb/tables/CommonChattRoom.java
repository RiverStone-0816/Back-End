package kr.co.eicn.ippbx.meta.jooq.customdb.tables;

import kr.co.eicn.ippbx.meta.jooq.customdb.Customdb;
import kr.co.eicn.ippbx.meta.jooq.customdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.ChattRoomRecord;
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

public class CommonChattRoom extends TableImpl<ChattRoomRecord> {
    /**
     * The column <code>CUSTOMDB.chatt_room.room_id</code>.
     */
    public final TableField<ChattRoomRecord, String> ROOM_ID = createField(DSL.name("room_id"), SQLDataType.VARCHAR(50).nullable(false), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_room.room_name</code>.
     */
    public final TableField<ChattRoomRecord, String> ROOM_NAME = createField(DSL.name("room_name"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_room.room_name_change</code>.
     */
    public final TableField<ChattRoomRecord, String> ROOM_NAME_CHANGE = createField(DSL.name("room_name_change"), SQLDataType.CHAR(1).defaultValue(DSL.field("''", SQLDataType.CHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_room.member_md5</code>.
     */
    public final TableField<ChattRoomRecord, String> MEMBER_MD5 = createField(DSL.name("member_md5"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_room.start_time</code>.
     */
    public final TableField<ChattRoomRecord, Timestamp> START_TIME = createField(DSL.name("start_time"), SQLDataType.TIMESTAMP(0).defaultValue(DSL.field("'2009-07-01 00:00:00'", SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_room.last_time</code>.
     */
    public final TableField<ChattRoomRecord, Timestamp> LAST_TIME = createField(DSL.name("last_time"), SQLDataType.TIMESTAMP(0).defaultValue(DSL.field("'2009-07-01 00:00:00'", SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_room.last_msg</code>.
     */
    public final TableField<ChattRoomRecord, String> LAST_MSG = createField(DSL.name("last_msg"), SQLDataType.VARCHAR(500).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_room.last_msg_type</code>.
     */
    public final TableField<ChattRoomRecord, String> LAST_MSG_TYPE = createField(DSL.name("last_msg_type"), SQLDataType.VARCHAR(10).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_room.last_msg_send_receive</code>.
     */
    public final TableField<ChattRoomRecord, String> LAST_MSG_SEND_RECEIVE = createField(DSL.name("last_msg_send_receive"), SQLDataType.CHAR(3).defaultValue(DSL.field("''", SQLDataType.CHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_room.last_userid</code>.
     */
    public final TableField<ChattRoomRecord, String> LAST_USERID = createField(DSL.name("last_userid"), SQLDataType.VARCHAR(30).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_room.make_userid</code>.
     */
    public final TableField<ChattRoomRecord, String> MAKE_USERID = createField(DSL.name("make_userid"), SQLDataType.VARCHAR(30).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_room.cur_member_cnt</code>.
     */
    public final TableField<ChattRoomRecord, Integer> CUR_MEMBER_CNT = createField(DSL.name("cur_member_cnt"), SQLDataType.INTEGER.nullable(false).defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_room.org_member_cnt</code>.
     */
    public final TableField<ChattRoomRecord, Integer> ORG_MEMBER_CNT = createField(DSL.name("org_member_cnt"), SQLDataType.INTEGER.nullable(false).defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    private String tableName;

    /**
     * Create a <code>CUSTOMDB.chatt_room</code> table reference
     */
    public CommonChattRoom(String table) {
        this(DSL.name("chatt_room_" + table), null);
    }

    private CommonChattRoom(Name alias, Table<ChattRoomRecord> aliased) {
        this(alias, aliased, null);
    }

    private CommonChattRoom(Name alias, Table<ChattRoomRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
        this.tableName = alias.last();
    }

    public <O extends Record> CommonChattRoom(CommonChattRoom table, Table<O> child, ForeignKey<O, ChattRoomRecord> key) {
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
        return Arrays.<Index>asList(Indexes.CHATT_ROOM_LAST_TIME, Indexes.CHATT_ROOM_MEMBER_MD5);
    }

    @Override
    public UniqueKey<ChattRoomRecord> getPrimaryKey() {
        return Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.ROOM_ID);
    }

    @NotNull
    @Override
    public List<UniqueKey<ChattRoomRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.ROOM_ID));
    }

    @NotNull
    @Override
    public CommonChattRoom as(String alias) {
        return new CommonChattRoom(DSL.name(alias), this);
    }

    @NotNull
    @Override
    public CommonChattRoom as(Name alias) {
        return new CommonChattRoom(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonChattRoom rename(String name) {
        return new CommonChattRoom(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonChattRoom rename(Name name) {
        return new CommonChattRoom(name, null);
    }

}
