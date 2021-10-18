package kr.co.eicn.ippbx.meta.jooq.customdb.tables;

import kr.co.eicn.ippbx.meta.jooq.customdb.Customdb;
import kr.co.eicn.ippbx.meta.jooq.customdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.customdb.Keys;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.KakaoSkillMsgRecord;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

public class CommonKakaoSkillMsg  extends TableImpl<KakaoSkillMsgRecord> {
    /**
     * The reference instance of <code>CUSTOMDB.kakao_skill_msg</code>
     */
    public static final KakaoSkillMsg KAKAO_SKILL_MSG = new KakaoSkillMsg();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<KakaoSkillMsgRecord> getRecordType() {
        return KakaoSkillMsgRecord.class;
    }

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.seq</code>.
     */
    public final TableField<KakaoSkillMsgRecord, Integer> SEQ = createField(DSL.name("seq"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.insert_date</code>.
     */
    public final TableField<KakaoSkillMsgRecord, Timestamp> INSERT_DATE = createField(DSL.name("insert_date"), org.jooq.impl.SQLDataType.TIMESTAMP.defaultValue(org.jooq.impl.DSL.inline("2021-01-01 00:00:00", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.bot_id</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> BOT_ID = createField(DSL.name("bot_id"), org.jooq.impl.SQLDataType.VARCHAR(50).nullable(false), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.bot_name</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> BOT_NAME = createField(DSL.name("bot_name"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.intent_id</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> INTENT_ID = createField(DSL.name("intent_id"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.intent_name</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> INTENT_NAME = createField(DSL.name("intent_name"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.intent_extra_reason_code</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> INTENT_EXTRA_REASON_CODE = createField(DSL.name("intent_extra_reason_code"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.intent_extra_reason_message</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> INTENT_EXTRA_REASON_MESSAGE = createField(DSL.name("intent_extra_reason_message"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.action_id</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> ACTION_ID = createField(DSL.name("action_id"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.action_name</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> ACTION_NAME = createField(DSL.name("action_name"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.request_block_id</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> REQUEST_BLOCK_ID = createField(DSL.name("request_block_id"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.request_block_name</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> REQUEST_BLOCK_NAME = createField(DSL.name("request_block_name"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.request_user_id</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> REQUEST_USER_ID = createField(DSL.name("request_user_id"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.request_user_type</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> REQUEST_USER_TYPE = createField(DSL.name("request_user_type"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.request_user_isfriend</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> REQUEST_USER_ISFRIEND = createField(DSL.name("request_user_isfriend"), org.jooq.impl.SQLDataType.VARCHAR(30).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.request_user_plusfriend_userkey</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> REQUEST_USER_PLUSFRIEND_USERKEY = createField(DSL.name("request_user_plusfriend_userkey"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.request_utterance</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> REQUEST_UTTERANCE = createField(DSL.name("request_utterance"), org.jooq.impl.SQLDataType.VARCHAR(500).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.request_event_name</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> REQUEST_EVENT_NAME = createField(DSL.name("request_event_name"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.request_event_data</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> REQUEST_EVENT_DATA = createField(DSL.name("request_event_data"), org.jooq.impl.SQLDataType.VARCHAR(500).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.request_params_param_event_id</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> REQUEST_PARAMS_PARAM_EVENT_ID = createField(DSL.name("request_params_param_event_id"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.request_params_surface</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> REQUEST_PARAMS_SURFACE = createField(DSL.name("request_params_surface"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.request_lang</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> REQUEST_LANG = createField(DSL.name("request_lang"), org.jooq.impl.SQLDataType.VARCHAR(10).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.app_user_status</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> APP_USER_STATUS = createField(DSL.name("app_user_status"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.app_user_id</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> APP_USER_ID = createField(DSL.name("app_user_id"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.response_type</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> RESPONSE_TYPE = createField(DSL.name("response_type"), org.jooq.impl.SQLDataType.VARCHAR(30).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.response_data</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> RESPONSE_DATA = createField(DSL.name("response_data"), org.jooq.impl.SQLDataType.VARCHAR(1000).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * Create a <code>CUSTOMDB.kakao_event</code> table reference
     */
    private String tableName;

    public CommonKakaoSkillMsg(String table) {
        this(DSL.name("kakao_skill_msg_" + table), null);
    }

    private CommonKakaoSkillMsg(Name alias, Table<KakaoSkillMsgRecord> aliased) {
        this(alias, aliased, null);
    }

    private CommonKakaoSkillMsg(Name alias, Table<KakaoSkillMsgRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
        this.tableName = alias.last();
    }

    public <O extends Record> CommonKakaoSkillMsg(CommonKakaoSkillMsg table, Table<O> child, ForeignKey<O, KakaoSkillMsgRecord> key) {
        super(child, key, KAKAO_SKILL_MSG);
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
    public Identity<KakaoSkillMsgRecord, Integer> getIdentity() {
        return Keys.IDENTITY_KAKAO_SKILL_MSG;
    }

    @Override
    public List<UniqueKey<KakaoSkillMsgRecord>> getKeys() {
        return Arrays.<UniqueKey<KakaoSkillMsgRecord>>asList(Keys.KEY_KAKAO_SKILL_MSG_SEQ);
    }

    @Override
    public CommonKakaoSkillMsg as(String alias) {
        return new CommonKakaoSkillMsg(DSL.name(alias), this);
    }

    @Override
    public CommonKakaoSkillMsg as(Name alias) {
        return new CommonKakaoSkillMsg(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonKakaoSkillMsg rename(String name) {
        return new CommonKakaoSkillMsg(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonKakaoSkillMsg rename(Name name) {
        return new CommonKakaoSkillMsg(name, null);
    }
}

