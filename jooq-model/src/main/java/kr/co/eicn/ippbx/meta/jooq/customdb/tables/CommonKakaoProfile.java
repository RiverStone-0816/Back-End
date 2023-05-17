package kr.co.eicn.ippbx.meta.jooq.customdb.tables;

import kr.co.eicn.ippbx.meta.jooq.customdb.Customdb;
import kr.co.eicn.ippbx.meta.jooq.customdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.customdb.Keys;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.KakaoProfileRecord;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.KakaoSkillMsgRecord;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

public class CommonKakaoProfile extends TableImpl<KakaoProfileRecord>  {

    /**
     * The reference instance of <code>CUSTOMDB.kakao_profile</code>
     */
    public static final KakaoProfile KAKAO_PROFILE = new KakaoProfile();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<KakaoProfileRecord> getRecordType() {
        return KakaoProfileRecord.class;
    }

    /**
     * The column <code>CUSTOMDB.kakao_profile.seq</code>.
     */
    public final TableField<KakaoProfileRecord, Integer> SEQ = createField(DSL.name("seq"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_profile.insert_date</code>.
     */
    public final TableField<KakaoProfileRecord, Timestamp> INSERT_DATE = createField(DSL.name("insert_date"), org.jooq.impl.SQLDataType.TIMESTAMP.defaultValue(org.jooq.impl.DSL.inline("2021-01-01 00:00:00", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_profile.update_date</code>.
     */
    public final TableField<KakaoProfileRecord, Timestamp> UPDATE_DATE = createField(DSL.name("update_date"), org.jooq.impl.SQLDataType.TIMESTAMP.defaultValue(org.jooq.impl.DSL.inline("2021-01-01 00:00:00", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_profile.bot_id</code>.
     */
    public final TableField<KakaoProfileRecord, String> BOT_ID = createField(DSL.name("bot_id"), org.jooq.impl.SQLDataType.VARCHAR(50).nullable(false), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_profile.request_user_id</code>.
     */
    public final TableField<KakaoProfileRecord, String> REQUEST_USER_ID = createField(DSL.name("request_user_id"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_profile.request_user_plusfriend_userkey</code>.
     */
    public final TableField<KakaoProfileRecord, String> REQUEST_USER_PLUSFRIEND_USERKEY = createField(DSL.name("request_user_plusfriend_userkey"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_profile.nickname</code>.
     */
    public final TableField<KakaoProfileRecord, String> NICKNAME = createField(DSL.name("nickname"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_profile.profile_image_url</code>.
     */
    public final TableField<KakaoProfileRecord, String> PROFILE_IMAGE_URL = createField(DSL.name("profile_image_url"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_profile.phone_number</code>.
     */
    public final TableField<KakaoProfileRecord, String> PHONE_NUMBER = createField(DSL.name("phone_number"), org.jooq.impl.SQLDataType.VARCHAR(30).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_profile.email</code>.
     */
    public final TableField<KakaoProfileRecord, String> EMAIL = createField(DSL.name("email"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_profile.app_user_id</code>.
     */
    public final TableField<KakaoProfileRecord, String> APP_USER_ID = createField(DSL.name("app_user_id"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_profile.maindb_group_id</code>.
     */
    public final TableField<KakaoProfileRecord, Integer> MAINDB_GROUP_ID = createField(DSL.name("maindb_group_id"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_profile.maindb_custom_id</code>.
     */
    public final TableField<KakaoProfileRecord, String> MAINDB_CUSTOM_ID = createField(DSL.name("maindb_custom_id"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_profile.maindb_custom_name</code>.
     */
    public final TableField<KakaoProfileRecord, String> MAINDB_CUSTOM_NAME = createField(DSL.name("maindb_custom_name"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
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
        super(child, key, KAKAO_PROFILE);
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
    public Identity<KakaoProfileRecord, Integer> getIdentity() {
        return (Identity<KakaoProfileRecord, Integer>) super.getIdentity();
    }

    @Override
    public List<UniqueKey<KakaoProfileRecord>> getKeys() {
        return Arrays.<UniqueKey<KakaoProfileRecord>>asList(Keys.KEY_KAKAO_PROFILE_SEQ);
    }

    @Override
    public CommonKakaoProfile as(String alias) {
        return new CommonKakaoProfile(DSL.name(alias), this);
    }

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

