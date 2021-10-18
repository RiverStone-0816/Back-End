package kr.co.eicn.ippbx.meta.jooq.customdb.tables;

import kr.co.eicn.ippbx.meta.jooq.customdb.Customdb;
import kr.co.eicn.ippbx.meta.jooq.customdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.KakaoEventRecord;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

public class CommonKakaoEvent extends TableImpl<KakaoEventRecord>  {

    /**
     * The reference instance of <code>CUSTOMDB.kakao_event</code>
     */
    public static final KakaoEvent KAKAO_EVENT = new KakaoEvent();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<KakaoEventRecord> getRecordType() {
        return KakaoEventRecord.class;
    }

    /**
     * The column <code>CUSTOMDB.kakao_event.seq</code>.
     */
    public final TableField<KakaoEventRecord, Integer> SEQ = createField(DSL.name("seq"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_event.insert_date</code>.
     */
    public final TableField<KakaoEventRecord, Timestamp> INSERT_DATE = createField(DSL.name("insert_date"), org.jooq.impl.SQLDataType.TIMESTAMP.defaultValue(org.jooq.impl.DSL.inline("2021-01-01 00:00:00", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_event.update_date</code>.
     */
    public final TableField<KakaoEventRecord, Timestamp> UPDATE_DATE = createField(DSL.name("update_date"), org.jooq.impl.SQLDataType.TIMESTAMP.defaultValue(org.jooq.impl.DSL.inline("2021-01-01 00:00:00", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_event.bot_id</code>.
     */
    public final TableField<KakaoEventRecord, String> BOT_ID = createField(DSL.name("bot_id"), org.jooq.impl.SQLDataType.VARCHAR(50).nullable(false), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_event.bot_name</code>.
     */
    public final TableField<KakaoEventRecord, String> BOT_NAME = createField(DSL.name("bot_name"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_event.event_name</code>.
     */
    public final TableField<KakaoEventRecord, String> EVENT_NAME = createField(DSL.name("event_name"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_event.user_type</code>.
     */
    public final TableField<KakaoEventRecord, String> USER_TYPE = createField(DSL.name("user_type"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_event.user_id</code>.
     */
    public final TableField<KakaoEventRecord, String> USER_ID = createField(DSL.name("user_id"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_event.user_name</code>.
     */
    public final TableField<KakaoEventRecord, String> USER_NAME = createField(DSL.name("user_name"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_event.user_data</code>.
     */
    public final TableField<KakaoEventRecord, String> USER_DATA = createField(DSL.name("user_data"), org.jooq.impl.SQLDataType.VARCHAR(500).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_event.task_id</code>.
     */
    public final TableField<KakaoEventRecord, String> TASK_ID = createField(DSL.name("task_id"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_event.response_block_id</code>.
     */
    public final TableField<KakaoEventRecord, String> RESPONSE_BLOCK_ID = createField(DSL.name("response_block_id"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_event.response_block_name</code>.
     */
    public final TableField<KakaoEventRecord, String> RESPONSE_BLOCK_NAME = createField(DSL.name("response_block_name"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    private final String tableName;

    /**
     * Create a <code>CUSTOMDB.kakao_event</code> table reference
     */
    public CommonKakaoEvent(String table) {
        this(DSL.name("kakao_event_" + table), null);
    }

    private CommonKakaoEvent(Name alias, Table<KakaoEventRecord> aliased) {
        this(alias, aliased, null);
    }

    private CommonKakaoEvent(Name alias, Table<KakaoEventRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
        this.tableName = alias.last();
    }

    public <O extends Record> CommonKakaoEvent(CommonKakaoEvent table, Table<O> child, ForeignKey<O, KakaoEventRecord> key) {
        super(child, key, KAKAO_EVENT);
        this.tableName = table.getName();
    }

    @Override
    public Schema getSchema() {
        return Customdb.CUSTOMDB;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.asList();
    }

    @Override
    public CommonKakaoEvent as(String alias) {
        return new CommonKakaoEvent(DSL.name(alias), this);
    }

    @Override
    public CommonKakaoEvent as(Name alias) {
        return new CommonKakaoEvent(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonKakaoEvent rename(String name) {
        return new CommonKakaoEvent(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonKakaoEvent rename(Name name) {
        return new CommonKakaoEvent(name, null);
    }
}

