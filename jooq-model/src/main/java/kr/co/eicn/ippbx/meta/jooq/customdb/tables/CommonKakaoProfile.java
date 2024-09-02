package kr.co.eicn.ippbx.meta.jooq.customdb.tables;

import kr.co.eicn.ippbx.meta.jooq.customdb.Customdb;
import kr.co.eicn.ippbx.meta.jooq.customdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.KakaoProfileRecord;
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

public class CommonKakaoProfile extends TableImpl<KakaoProfileRecord> {
    /**
     * The column <code>CUSTOMDB.kakao_profile.seq</code>.
     */
    public final TableField<KakaoProfileRecord, Integer> SEQ = createField(DSL.name("seq"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_profile.insert_date</code>.
     */
    public final TableField<KakaoProfileRecord, Timestamp> INSERT_DATE = createField(DSL.name("insert_date"), SQLDataType.TIMESTAMP(0).defaultValue(DSL.field("'2021-01-01 00:00:00'", SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_profile.update_date</code>.
     */
    public final TableField<KakaoProfileRecord, Timestamp> UPDATE_DATE = createField(DSL.name("update_date"), SQLDataType.TIMESTAMP(0).defaultValue(DSL.field("'2021-01-01 00:00:00'", SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_profile.bot_id</code>.
     */
    public final TableField<KakaoProfileRecord, String> BOT_ID = createField(DSL.name("bot_id"), SQLDataType.VARCHAR(50).nullable(false), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_profile.request_user_id</code>.
     */
    public final TableField<KakaoProfileRecord, String> REQUEST_USER_ID = createField(DSL.name("request_user_id"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_profile.request_user_plusfriend_userkey</code>.
     */
    public final TableField<KakaoProfileRecord, String> REQUEST_USER_PLUSFRIEND_USERKEY = createField(DSL.name("request_user_plusfriend_userkey"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_profile.nickname</code>.
     */
    public final TableField<KakaoProfileRecord, String> NICKNAME = createField(DSL.name("nickname"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_profile.profile_image_url</code>.
     */
    public final TableField<KakaoProfileRecord, String> PROFILE_IMAGE_URL = createField(DSL.name("profile_image_url"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_profile.phone_number</code>.
     */
    public final TableField<KakaoProfileRecord, String> PHONE_NUMBER = createField(DSL.name("phone_number"), SQLDataType.VARCHAR(30).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_profile.email</code>.
     */
    public final TableField<KakaoProfileRecord, String> EMAIL = createField(DSL.name("email"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_profile.app_user_id</code>.
     */
    public final TableField<KakaoProfileRecord, String> APP_USER_ID = createField(DSL.name("app_user_id"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_profile.maindb_group_id</code>.
     */
    public final TableField<KakaoProfileRecord, Integer> MAINDB_GROUP_ID = createField(DSL.name("maindb_group_id"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_profile.maindb_custom_id</code>.
     */
    public final TableField<KakaoProfileRecord, String> MAINDB_CUSTOM_ID = createField(DSL.name("maindb_custom_id"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_profile.maindb_custom_name</code>.
     */
    public final TableField<KakaoProfileRecord, String> MAINDB_CUSTOM_NAME = createField(DSL.name("maindb_custom_name"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    private String tableName;

    /**
     * Create a <code>CUSTOMDB.kakao_event</code> table reference
     */
    public CommonKakaoProfile(String table) {
        this(DSL.name("kakao_profile_" + table), null);
    }

    private CommonKakaoProfile(Name alias, Table<KakaoProfileRecord> aliased) {
        this(alias, aliased, null);
    }

    private CommonKakaoProfile(Name alias, Table<KakaoProfileRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
        this.tableName = alias.last();
    }

    public <O extends Record> CommonKakaoProfile(CommonKakaoProfile table, Table<O> child, ForeignKey<O, KakaoProfileRecord> key) {
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
        return Arrays.<Index>asList(Indexes.KAKAO_PROFILE_APP_USER_ID, Indexes.KAKAO_PROFILE_BOT_ID, Indexes.KAKAO_PROFILE_INSERT_DATE, Indexes.KAKAO_PROFILE_MAINDB_CUSTOM_ID, Indexes.KAKAO_PROFILE_REQUEST_USER_PLUSFRIEND_USERKEY);
    }

    @Override
    public Identity<KakaoProfileRecord, Integer> getIdentity() {
        return Internal.createIdentity(this, this.SEQ);
    }

    @NotNull
    @Override
    public List<UniqueKey<KakaoProfileRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ));
    }

    @NotNull
    @Override
    public CommonKakaoProfile as(String alias) {
        return new CommonKakaoProfile(DSL.name(alias), this);
    }

    @NotNull
    @Override
    public CommonKakaoProfile as(Name alias) {
        return new CommonKakaoProfile(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonKakaoProfile rename(String name) {
        return new CommonKakaoProfile(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonKakaoProfile rename(Name name) {
        return new CommonKakaoProfile(name, null);
    }
}

