package kr.co.eicn.ippbx.meta.jooq.customdb.tables;

import kr.co.eicn.ippbx.meta.jooq.customdb.Customdb;
import kr.co.eicn.ippbx.meta.jooq.customdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.KakaoSkillMsgRecord;
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

public class CommonKakaoSkillMsg extends TableImpl<KakaoSkillMsgRecord> {
    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.seq</code>.
     */
    public final TableField<KakaoSkillMsgRecord, Integer> SEQ = createField(DSL.name("seq"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.insert_date</code>.
     */
    public final TableField<KakaoSkillMsgRecord, Timestamp> INSERT_DATE = createField(DSL.name("insert_date"), SQLDataType.TIMESTAMP(0).defaultValue(DSL.field("'2021-01-01 00:00:00'", SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.bot_id</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> BOT_ID = createField(DSL.name("bot_id"), SQLDataType.VARCHAR(50).nullable(false), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.bot_name</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> BOT_NAME = createField(DSL.name("bot_name"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.intent_id</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> INTENT_ID = createField(DSL.name("intent_id"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.intent_name</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> INTENT_NAME = createField(DSL.name("intent_name"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.intent_extra_reason_code</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> INTENT_EXTRA_REASON_CODE = createField(DSL.name("intent_extra_reason_code"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.intent_extra_reason_message</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> INTENT_EXTRA_REASON_MESSAGE = createField(DSL.name("intent_extra_reason_message"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.action_id</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> ACTION_ID = createField(DSL.name("action_id"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.action_name</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> ACTION_NAME = createField(DSL.name("action_name"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.request_block_id</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> REQUEST_BLOCK_ID = createField(DSL.name("request_block_id"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.request_block_name</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> REQUEST_BLOCK_NAME = createField(DSL.name("request_block_name"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.request_user_id</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> REQUEST_USER_ID = createField(DSL.name("request_user_id"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.request_user_type</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> REQUEST_USER_TYPE = createField(DSL.name("request_user_type"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.request_user_isfriend</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> REQUEST_USER_ISFRIEND = createField(DSL.name("request_user_isfriend"), SQLDataType.VARCHAR(30).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.request_user_plusfriend_userkey</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> REQUEST_USER_PLUSFRIEND_USERKEY = createField(DSL.name("request_user_plusfriend_userkey"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.request_utterance</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> REQUEST_UTTERANCE = createField(DSL.name("request_utterance"), SQLDataType.VARCHAR(500).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.request_event_name</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> REQUEST_EVENT_NAME = createField(DSL.name("request_event_name"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.request_event_data</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> REQUEST_EVENT_DATA = createField(DSL.name("request_event_data"), SQLDataType.VARCHAR(500).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.request_params_param_event_id</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> REQUEST_PARAMS_PARAM_EVENT_ID = createField(DSL.name("request_params_param_event_id"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.request_params_surface</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> REQUEST_PARAMS_SURFACE = createField(DSL.name("request_params_surface"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.request_lang</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> REQUEST_LANG = createField(DSL.name("request_lang"), SQLDataType.VARCHAR(10).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.app_user_status</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> APP_USER_STATUS = createField(DSL.name("app_user_status"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.app_user_id</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> APP_USER_ID = createField(DSL.name("app_user_id"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.response_type</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> RESPONSE_TYPE = createField(DSL.name("response_type"), SQLDataType.VARCHAR(30).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_skill_msg.response_data</code>.
     */
    public final TableField<KakaoSkillMsgRecord, String> RESPONSE_DATA = createField(DSL.name("response_data"), SQLDataType.VARCHAR(1000).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

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
    public Identity<KakaoSkillMsgRecord, Integer> getIdentity() {
        return Internal.createIdentity(this, this.SEQ);
    }

    @NotNull
    @Override
    public List<UniqueKey<KakaoSkillMsgRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ));
    }

    @NotNull
    @Override
    public CommonKakaoSkillMsg as(String alias) {
        return new CommonKakaoSkillMsg(DSL.name(alias), this);
    }

    @NotNull
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

