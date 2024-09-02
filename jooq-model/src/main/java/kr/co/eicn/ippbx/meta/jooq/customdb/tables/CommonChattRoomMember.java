package kr.co.eicn.ippbx.meta.jooq.customdb.tables;

import kr.co.eicn.ippbx.meta.jooq.customdb.Customdb;
import kr.co.eicn.ippbx.meta.jooq.customdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.customdb.Keys;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.ChattRoomMemberRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

public class CommonChattRoomMember extends TableImpl<ChattRoomMemberRecord> {
    /**
     * The column <code>CUSTOMDB.chatt_room_member.room_id</code>.
     */
    public final TableField<ChattRoomMemberRecord, String> ROOM_ID = createField(DSL.name("room_id"), SQLDataType.VARCHAR(50).nullable(false), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_room_member.userid</code>.
     */
    public final TableField<ChattRoomMemberRecord, String> USERID = createField(DSL.name("userid"), SQLDataType.VARCHAR(30).nullable(false), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_room_member.room_name</code>.
     */
    public final TableField<ChattRoomMemberRecord, String> ROOM_NAME = createField(DSL.name("room_name"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_room_member.member_md5</code>.
     */
    public final TableField<ChattRoomMemberRecord, String> MEMBER_MD5 = createField(DSL.name("member_md5"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_room_member.is_join</code>.
     */
    public final TableField<ChattRoomMemberRecord, String> IS_JOIN = createField(DSL.name("is_join"), SQLDataType.CHAR(1).defaultValue(DSL.field("''", SQLDataType.CHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_room_member.invite_time</code>.
     */
    public final TableField<ChattRoomMemberRecord, Timestamp> INVITE_TIME = createField(DSL.name("invite_time"), SQLDataType.TIMESTAMP(0).defaultValue(DSL.field("'2019-04-01 00:00:00'", SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_room_member.last_msg</code>.
     */
    public final TableField<ChattRoomMemberRecord, String> LAST_MSG = createField(DSL.name("last_msg"), SQLDataType.VARCHAR(500).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_room_member.last_type</code>.
     */
    public final TableField<ChattRoomMemberRecord, String> LAST_TYPE = createField(DSL.name("last_type"), SQLDataType.VARCHAR(10).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_room_member.last_time</code>.
     */
    public final TableField<ChattRoomMemberRecord, Timestamp> LAST_TIME = createField(DSL.name("last_time"), SQLDataType.TIMESTAMP(0).defaultValue(DSL.field("'2019-04-01 00:00:00'", SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_room_member.read_time</code>.
     */
    public final TableField<ChattRoomMemberRecord, Timestamp> READ_TIME = createField(DSL.name("read_time"), SQLDataType.TIMESTAMP(0).defaultValue(DSL.field("'2019-04-01 00:00:00'", SQLDataType.TIMESTAMP)), this, "");

    private String tableName;
    /**
     * Create a <code>CUSTOMDB.chatt_room_member</code> table reference
     */
    public CommonChattRoomMember(String table) {
        this(DSL.name("chatt_room_member_" + table), null);
    }

    private CommonChattRoomMember(Name alias, Table<ChattRoomMemberRecord> aliased) {
        this(alias, aliased, null);
    }

    private CommonChattRoomMember(Name alias, Table<ChattRoomMemberRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
        this.tableName = alias.last();
    }

    public <O extends Record> CommonChattRoomMember(CommonChattRoomMember table, Table<O> child, ForeignKey<O, ChattRoomMemberRecord> key) {
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
        return Arrays.<Index>asList(Indexes.CHATT_ROOM_MEMBER_LAST_TIME, Indexes.CHATT_ROOM_MEMBER_MEMBER_MD5, Indexes.CHATT_ROOM_MEMBER_ROOM_ID, Indexes.CHATT_ROOM_MEMBER_USERID);
    }

    @Override
    public UniqueKey<ChattRoomMemberRecord> getPrimaryKey() {
        return Keys.KEY_CHATT_ROOM_MEMBER_PRIMARY;
    }

    @NotNull
    @Override
    public List<UniqueKey<ChattRoomMemberRecord>> getKeys() {
        return Arrays.<UniqueKey<ChattRoomMemberRecord>>asList(Keys.KEY_CHATT_ROOM_MEMBER_PRIMARY);
    }

    @NotNull
    @Override
    public CommonChattRoomMember as(String alias) {
        return new CommonChattRoomMember(DSL.name(alias), this);
    }

    @NotNull
    @Override
    public CommonChattRoomMember as(Name alias) {
        return new CommonChattRoomMember(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonChattRoomMember rename(String name) {
        return new CommonChattRoomMember(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonChattRoomMember rename(Name name) {
        return new CommonChattRoomMember(name, null);
    }
}
