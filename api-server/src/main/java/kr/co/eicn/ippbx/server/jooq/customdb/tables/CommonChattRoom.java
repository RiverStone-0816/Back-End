package kr.co.eicn.ippbx.server.jooq.customdb.tables;

import kr.co.eicn.ippbx.server.jooq.customdb.Customdb;
import kr.co.eicn.ippbx.server.jooq.customdb.Indexes;
import kr.co.eicn.ippbx.server.jooq.customdb.Keys;
import kr.co.eicn.ippbx.server.jooq.customdb.tables.records.ChattRoomRecord;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

public class CommonChattRoom extends TableImpl<ChattRoomRecord> {

    /**
     * The reference instance of <code>CUSTOMDB.chatt_room</code>
     */
    public static final ChattRoom CHATT_ROOM = new ChattRoom();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ChattRoomRecord> getRecordType() {
        return ChattRoomRecord.class;
    }

    /**
     * The column <code>CUSTOMDB.chatt_room.room_id</code>.
     */
    public final TableField<ChattRoomRecord, String> ROOM_ID = createField(DSL.name("room_id"), org.jooq.impl.SQLDataType.VARCHAR(50).nullable(false), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_room.room_name</code>.
     */
    public final TableField<ChattRoomRecord, String> ROOM_NAME = createField(DSL.name("room_name"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_room.room_name_yn</code>.
     */
    public final TableField<ChattRoomRecord, String> ROOM_NAME_CHANGE = createField(DSL.name("room_name_change"), org.jooq.impl.SQLDataType.CHAR(1).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.CHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_room.member_md5</code>.
     */
    public final TableField<ChattRoomRecord, String> MEMBER_MD5 = createField(DSL.name("member_md5"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_room.start_time</code>.
     */
    public final TableField<ChattRoomRecord, Timestamp> START_TIME = createField(DSL.name("start_time"), org.jooq.impl.SQLDataType.TIMESTAMP.defaultValue(DSL.inline("2009-07-01 00:00:00", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_room.last_time</code>.
     */
    public final TableField<ChattRoomRecord, Timestamp> LAST_TIME = createField(DSL.name("last_time"), org.jooq.impl.SQLDataType.TIMESTAMP.defaultValue(DSL.inline("2009-07-01 00:00:00", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_room.last_msg</code>.
     */
    public final TableField<ChattRoomRecord, String> LAST_MSG = createField(DSL.name("last_msg"), org.jooq.impl.SQLDataType.VARCHAR(500).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_room.last_msg_type</code>.
     */
    public final TableField<ChattRoomRecord, String> LAST_MSG_TYPE = createField(DSL.name("last_msg_type"), org.jooq.impl.SQLDataType.VARCHAR(10).nullable(false).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_room.last_msg_send_receive</code>.
     */
    public final TableField<ChattRoomRecord, String> LAST_MSG_SEND_RECEIVE = createField(DSL.name("last_msg_send_receive"), org.jooq.impl.SQLDataType.CHAR(3).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.CHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_room.last_userid</code>.
     */
    public final TableField<ChattRoomRecord, String> LAST_USERID = createField(DSL.name("last_userid"), org.jooq.impl.SQLDataType.VARCHAR(30).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_room.make_userid</code>.
     */
    public final TableField<ChattRoomRecord, String> MAKE_USERID = createField(DSL.name("make_userid"), org.jooq.impl.SQLDataType.VARCHAR(30).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_room.cur_member_cnt</code>.
     */
    public final TableField<ChattRoomRecord, Integer> CUR_MEMBER_CNT = createField(DSL.name("cur_member_cnt"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_room.org_member_cnt</code>.
     */
    public final TableField<ChattRoomRecord, Integer> ORG_MEMBER_CNT = createField(DSL.name("org_member_cnt"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");
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
        super(child, key, CHATT_ROOM);
        this.tableName = table.getName();
    }

    @Override
    public Schema getSchema() {
        return Customdb.CUSTOMDB;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.CHATT_ROOM_LAST_TIME, Indexes.CHATT_ROOM_MEMBER_MD5, Indexes.CHATT_ROOM_PRIMARY);
    }

    @Override
    public UniqueKey<ChattRoomRecord> getPrimaryKey() {
        return Keys.KEY_CHATT_ROOM_PRIMARY;
    }

    @Override
    public List<UniqueKey<ChattRoomRecord>> getKeys() {
        return Arrays.<UniqueKey<ChattRoomRecord>>asList(Keys.KEY_CHATT_ROOM_PRIMARY);
    }

    @Override
    public CommonChattRoom as(String alias) {
        return new CommonChattRoom(DSL.name(alias), this);
    }

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
