package kr.co.eicn.ippbx.meta.jooq.customdb.tables;

import kr.co.eicn.ippbx.meta.jooq.customdb.Customdb;
import kr.co.eicn.ippbx.meta.jooq.customdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.WtalkRoomRecord;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommonWtalkRoom extends TableImpl<WtalkRoomRecord> {

    /**
     * The column <code>CUSTOMDB.wtalk_room.seq</code>.
     */
    public final TableField<WtalkRoomRecord, Integer> SEQ = createField(DSL.name("seq"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>CUSTOMDB.wtalk_room.room_id</code>.
     */
    public final TableField<WtalkRoomRecord, String> ROOM_ID = createField(DSL.name("room_id"), SQLDataType.VARCHAR(150).defaultValue(DSL.inline("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.wtalk_room.room_start_time</code>.
     */
    public final TableField<WtalkRoomRecord, Timestamp> ROOM_START_TIME = createField(DSL.name("room_start_time"), SQLDataType.TIMESTAMP(0).defaultValue(DSL.inline("'2009-07-01 00:00:00'", SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.wtalk_room.room_last_time</code>.
     */
    public final TableField<WtalkRoomRecord, Timestamp> ROOM_LAST_TIME = createField(DSL.name("room_last_time"), SQLDataType.TIMESTAMP(0).defaultValue(DSL.inline("'2009-07-01 00:00:00'", SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.wtalk_room.room_status</code>.
     */
    public final TableField<WtalkRoomRecord, String> ROOM_STATUS = createField(DSL.name("room_status"), SQLDataType.CHAR(1).defaultValue(DSL.inline("'S'", SQLDataType.CHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.wtalk_room.room_mode</code>.
     */
    public final TableField<WtalkRoomRecord, String> ROOM_MODE = createField(DSL.name("room_mode"), SQLDataType.VARCHAR(10).defaultValue(DSL.inline("'service'", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.wtalk_room.maindb_group_id</code>.
     */
    public final TableField<WtalkRoomRecord, Integer> MAINDB_GROUP_ID = createField(DSL.name("maindb_group_id"), SQLDataType.INTEGER.defaultValue(DSL.inline("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.wtalk_room.maindb_custom_id</code>.
     */
    public final TableField<WtalkRoomRecord, String> MAINDB_CUSTOM_ID = createField(DSL.name("maindb_custom_id"), SQLDataType.VARCHAR(100).defaultValue(DSL.inline("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.wtalk_room.maindb_custom_name</code>.
     */
    public final TableField<WtalkRoomRecord, String> MAINDB_CUSTOM_NAME = createField(DSL.name("maindb_custom_name"), SQLDataType.VARCHAR(100).defaultValue(DSL.inline("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.wtalk_room.auth_phone_number</code>.
     */
    public final TableField<WtalkRoomRecord, String> AUTH_PHONE_NUMBER = createField(DSL.name("auth_phone_number"), SQLDataType.VARCHAR(50).defaultValue(DSL.inline("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.wtalk_room.userid</code>.
     */
    public final TableField<WtalkRoomRecord, String> USERID = createField(DSL.name("userid"), SQLDataType.VARCHAR(30).defaultValue(DSL.inline("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.wtalk_room.user_key</code>.
     */
    public final TableField<WtalkRoomRecord, String> USER_KEY = createField(DSL.name("user_key"), SQLDataType.VARCHAR(100).nullable(false).defaultValue(DSL.inline("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.wtalk_room.channel_type</code>.
     */
    public final TableField<WtalkRoomRecord, String> CHANNEL_TYPE = createField(DSL.name("channel_type"), SQLDataType.VARCHAR(20).nullable(false).defaultValue(DSL.inline("'eicn'", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.wtalk_room.sender_key</code>.
     */
    public final TableField<WtalkRoomRecord, String> SENDER_KEY = createField(DSL.name("sender_key"), SQLDataType.VARCHAR(100).nullable(false).defaultValue(DSL.inline("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.wtalk_room.company_id</code>.
     */
    public final TableField<WtalkRoomRecord, String> COMPANY_ID = createField(DSL.name("company_id"), SQLDataType.VARCHAR(30).defaultValue(DSL.inline("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.wtalk_room.room_name</code>.
     */
    public final TableField<WtalkRoomRecord, String> ROOM_NAME = createField(DSL.name("room_name"), SQLDataType.VARCHAR(100).defaultValue(DSL.inline("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.wtalk_room.schedule_kind</code>.
     */
    public final TableField<WtalkRoomRecord, String> SCHEDULE_KIND = createField(DSL.name("schedule_kind"), SQLDataType.CHAR(1).nullable(false).defaultValue(DSL.inline("'D'", SQLDataType.CHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.wtalk_room.schedule_data</code>.
     */
    public final TableField<WtalkRoomRecord, String> SCHEDULE_DATA = createField(DSL.name("schedule_data"), SQLDataType.VARCHAR(50).defaultValue(DSL.inline("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.wtalk_room.schedule_stat_yn</code>.
     */
    public final TableField<WtalkRoomRecord, String> SCHEDULE_STAT_YN = createField(DSL.name("schedule_stat_yn"), SQLDataType.CHAR(1).defaultValue(DSL.inline("'Y'", SQLDataType.CHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.wtalk_room.schedule_worktime_yn</code>.
     */
    public final TableField<WtalkRoomRecord, String> SCHEDULE_WORKTIME_YN = createField(DSL.name("schedule_worktime_yn"), SQLDataType.CHAR(1).defaultValue(DSL.inline("'Y'", SQLDataType.CHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.wtalk_room.is_auto_enable</code>.
     */
    public final TableField<WtalkRoomRecord, String> IS_AUTO_ENABLE = createField(DSL.name("is_auto_enable"), SQLDataType.CHAR(1).defaultValue(DSL.inline("'Y'", SQLDataType.CHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.wtalk_room.is_custom_upload_enable</code>.
     */
    public final TableField<WtalkRoomRecord, String> IS_CUSTOM_UPLOAD_ENABLE = createField(DSL.name("is_custom_upload_enable"), SQLDataType.CHAR(1).defaultValue(DSL.inline("'N'", SQLDataType.CHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.wtalk_room.audio_use_cnt</code>.
     */
    public final TableField<WtalkRoomRecord, Integer> AUDIO_USE_CNT = createField(DSL.name("audio_use_cnt"), SQLDataType.INTEGER.defaultValue(DSL.inline("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.wtalk_room.video_use_cnt</code>.
     */
    public final TableField<WtalkRoomRecord, Integer> VIDEO_USE_CNT = createField(DSL.name("video_use_cnt"), SQLDataType.INTEGER.defaultValue(DSL.inline("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.wtalk_room.key_id</code>.
     */
    public final TableField<WtalkRoomRecord, Integer> KEY_ID = createField(DSL.name("key_id"), SQLDataType.INTEGER.defaultValue(DSL.inline("NULL", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.wtalk_room.last_user_yn</code>.
     */
    public final TableField<WtalkRoomRecord, String> LAST_USER_YN = createField(DSL.name("last_user_yn"), SQLDataType.CHAR(1).defaultValue(DSL.inline("''", SQLDataType.CHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.wtalk_room.last_type</code>.
     */
    public final TableField<WtalkRoomRecord, String> LAST_TYPE = createField(DSL.name("last_type"), SQLDataType.VARCHAR(10).defaultValue(DSL.inline("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.wtalk_room.last_send_receive</code>. S-발신 R-수신
     */
    public final TableField<WtalkRoomRecord, String> LAST_SEND_RECEIVE = createField(DSL.name("last_send_receive"), SQLDataType.CHAR(3).defaultValue(DSL.inline("'S'", SQLDataType.CHAR)), this, "S-발신 R-수신");

    /**
     * The column <code>CUSTOMDB.wtalk_room.last_content</code>.
     */
    public final TableField<WtalkRoomRecord, String> LAST_CONTENT = createField(DSL.name("last_content"), SQLDataType.VARCHAR(5000).defaultValue(DSL.inline("NULL", SQLDataType.VARCHAR)), this, "");

    private String tableName;

    /**
     * Create a <code>CUSTOMDB.talk_room</code> table reference
     */
    public CommonWtalkRoom(String tableName) {
        this(DSL.name("wtalk_room_" + tableName), null);
    }

    private CommonWtalkRoom(Name alias, Table<WtalkRoomRecord> aliased) {
        this(alias, aliased, null);
    }

    private CommonWtalkRoom(Name alias, Table<WtalkRoomRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
        this.tableName = alias.last();
    }

    public <O extends Record> CommonWtalkRoom(CommonWtalkRoom table, Table<O> child, ForeignKey<O, WtalkRoomRecord> key) {
        super(child, key, table);
        this.tableName = table.getName();
    }

    /**
     * The class holding records for this type
     */
    @Override
    public Class<WtalkRoomRecord> getRecordType() {
        return WtalkRoomRecord.class;
    }

    @Override
    public Schema getSchema() {
        return Customdb.CUSTOMDB;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.WTALK_ROOM_COMPANY_ID, Indexes.WTALK_ROOM_ROOM_ID, Indexes.WTALK_ROOM_ROOM_START_TIME, Indexes.WTALK_ROOM_ROOM_STATUS, Indexes.WTALK_ROOM_SENDER_KEY, Indexes.WTALK_ROOM_USER_KEY, Indexes.WTALK_ROOM_USERID);
    }

    @Override
    public Identity<WtalkRoomRecord, Integer> getIdentity() {
        return (Identity<WtalkRoomRecord, Integer>) super.getIdentity();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<WtalkRoomRecord> getPrimaryKey() {
        return Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ);
    }

    @Override
    public List<UniqueKey<WtalkRoomRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ));
    }

    @Override
    public CommonWtalkRoom as(String alias) {
        return new CommonWtalkRoom(DSL.name(alias), this);
    }

    @Override
    public CommonWtalkRoom as(Name alias) {
        return new CommonWtalkRoom(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonWtalkRoom rename(String name) {
        return new CommonWtalkRoom(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonWtalkRoom rename(Name name) {
        return new CommonWtalkRoom(name, null);
    }
}
