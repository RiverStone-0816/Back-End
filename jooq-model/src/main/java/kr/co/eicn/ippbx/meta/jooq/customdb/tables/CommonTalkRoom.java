package kr.co.eicn.ippbx.meta.jooq.customdb.tables;

import kr.co.eicn.ippbx.meta.jooq.customdb.Customdb;
import kr.co.eicn.ippbx.meta.jooq.customdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.customdb.Keys;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.CommonEicnCdrRecord;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.TalkRoomRecord;
import kr.co.eicn.ippbx.meta.jooq.pds.tables.records.ExecutePdsCustomInfoRecord;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.jooq.impl.TableImpl;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommonTalkRoom extends TableImpl<TalkRoomRecord> {
    /**
     * The column <code>CUSTOMDB.talk_room.seq</code>.
     */
    public final TableField<TalkRoomRecord, Integer> SEQ = createField(DSL.name("seq"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>CUSTOMDB.talk_room.room_id</code>.
     */
    public final TableField<TalkRoomRecord, String> ROOM_ID = createField(DSL.name("room_id"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.talk_room.room_start_time</code>.
     */
    public final TableField<TalkRoomRecord, Timestamp> ROOM_START_TIME = createField(DSL.name("room_start_time"), org.jooq.impl.SQLDataType.TIMESTAMP.defaultValue(org.jooq.impl.DSL.inline("2009-07-01 00:00:00", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.talk_room.room_last_time</code>.
     */
    public final TableField<TalkRoomRecord, Timestamp> ROOM_LAST_TIME = createField(DSL.name("room_last_time"), org.jooq.impl.SQLDataType.TIMESTAMP.defaultValue(org.jooq.impl.DSL.inline("2009-07-01 00:00:00", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.talk_room.room_status</code>.
     */
    public final TableField<TalkRoomRecord, String> ROOM_STATUS = createField(DSL.name("room_status"), org.jooq.impl.SQLDataType.CHAR(1).defaultValue(org.jooq.impl.DSL.inline("S", org.jooq.impl.SQLDataType.CHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.talk_room.maindb_group_id</code>.
     */
    public final TableField<TalkRoomRecord, Integer> MAINDB_GROUP_ID = createField(DSL.name("maindb_group_id"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.talk_room.maindb_custom_id</code>.
     */
    public final TableField<TalkRoomRecord, String> MAINDB_CUSTOM_ID = createField(DSL.name("maindb_custom_id"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.talk_room.maindb_custom_name</code>.
     */
    public final TableField<TalkRoomRecord, String> MAINDB_CUSTOM_NAME = createField(DSL.name("maindb_custom_name"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.talk_room.userid</code>.
     */
    public final TableField<TalkRoomRecord, String> USERID = createField(DSL.name("userid"), org.jooq.impl.SQLDataType.VARCHAR(30).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.talk_room.user_key</code>.
     */
    public final TableField<TalkRoomRecord, String> USER_KEY = createField(DSL.name("user_key"), org.jooq.impl.SQLDataType.VARCHAR(30).nullable(false).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.talk_room.sender_key</code>.
     */
    public final TableField<TalkRoomRecord, String> SENDER_KEY = createField(DSL.name("sender_key"), org.jooq.impl.SQLDataType.VARCHAR(50).nullable(false).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.talk_room.company_id</code>.
     */
    public final TableField<TalkRoomRecord, String> COMPANY_ID = createField(DSL.name("company_id"), org.jooq.impl.SQLDataType.VARCHAR(30).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.talk_room.room_name</code>.
     */
    public final TableField<TalkRoomRecord, String> ROOM_NAME = createField(DSL.name("room_name"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.talk_room.schedule_kind</code>.
     */
    public final TableField<TalkRoomRecord, String> SCHEDULE_KIND = createField(DSL.name("schedule_kind"), org.jooq.impl.SQLDataType.CHAR(1).nullable(false).defaultValue(org.jooq.impl.DSL.inline("D", org.jooq.impl.SQLDataType.CHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.talk_room.schedule_data</code>.
     */
    public final TableField<TalkRoomRecord, String> SCHEDULE_DATA = createField(DSL.name("schedule_data"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    private String tableName;

    /**
     * Create a <code>CUSTOMDB.talk_room</code> table reference
     */
    public CommonTalkRoom(String tableName) {
        this(DSL.name("talk_room_" + tableName), null);
    }

    private CommonTalkRoom(Name alias, Table<TalkRoomRecord> aliased) {
        this(alias, aliased, null);
    }

    private CommonTalkRoom(Name alias, Table<TalkRoomRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
        this.tableName = alias.last();
    }

    public <O extends Record> CommonTalkRoom(CommonTalkRoom table, Table<O> child, ForeignKey<O, TalkRoomRecord> key) {
        super(child, key, table);
        this.tableName = table.getName();
    }

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TalkRoomRecord> getRecordType() {
        return TalkRoomRecord.class;
    }

    @Override
    public Schema getSchema() {
        return Customdb.CUSTOMDB;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.TALK_ROOM_COMPANY_ID, Indexes.TALK_ROOM_ROOM_ID, Indexes.TALK_ROOM_SENDER_KEY, Indexes.TALK_ROOM_USER_KEY);
    }

    @Override
    public Identity<TalkRoomRecord, Integer> getIdentity() {
        return Keys.IDENTITY_TALK_ROOM;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<TalkRoomRecord> getPrimaryKey() {
        return Internal.createUniqueKey(this, "KEY_" + getName() + "_PRIMARY", this.SEQ);
    }

    @Override
    public List<UniqueKey<TalkRoomRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, "KEY_" + getName() + "_PRIMARY", this.SEQ));
    }

    @Override
    public CommonTalkRoom as(String alias) {
        return new CommonTalkRoom(DSL.name(alias), this);
    }

    @Override
    public CommonTalkRoom as(Name alias) {
        return new CommonTalkRoom(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonTalkRoom rename(String name) {
        return new CommonTalkRoom(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonTalkRoom rename(Name name) {
        return new CommonTalkRoom(name, null);
    }
}
