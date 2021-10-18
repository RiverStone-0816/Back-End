package kr.co.eicn.ippbx.meta.jooq.customdb.tables.records;

import org.jooq.Table;
import org.jooq.impl.TableRecordImpl;

import java.sql.Timestamp;

public class CommonKakaoProfileRecord extends TableRecordImpl<CommonKakaoProfileRecord> {

    public CommonKakaoProfileRecord(Table<CommonKakaoProfileRecord> table) {
        super(table);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_profile.seq</code>.
     */
    public void setSeq(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_profile.seq</code>.
     */
    public Integer getSeq() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_profile.insert_date</code>.
     */
    public void setInsertDate(Timestamp value) {
        set(1, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_profile.insert_date</code>.
     */
    public Timestamp getInsertDate() {
        return (Timestamp) get(1);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_profile.update_date</code>.
     */
    public void setUpdateDate(Timestamp value) {
        set(2, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_profile.update_date</code>.
     */
    public Timestamp getUpdateDate() {
        return (Timestamp) get(2);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_profile.bot_id</code>.
     */
    public void setBotId(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_profile.bot_id</code>.
     */
    public String getBotId() {
        return (String) get(3);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_profile.request_user_id</code>.
     */
    public void setRequestUserId(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_profile.request_user_id</code>.
     */
    public String getRequestUserId() {
        return (String) get(4);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_profile.request_user_plusfriend_userkey</code>.
     */
    public void setRequestUserPlusfriendUserkey(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_profile.request_user_plusfriend_userkey</code>.
     */
    public String getRequestUserPlusfriendUserkey() {
        return (String) get(5);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_profile.nickname</code>.
     */
    public void setNickname(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_profile.nickname</code>.
     */
    public String getNickname() {
        return (String) get(6);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_profile.profile_image_url</code>.
     */
    public void setProfileImageUrl(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_profile.profile_image_url</code>.
     */
    public String getProfileImageUrl() {
        return (String) get(7);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_profile.phone_number</code>.
     */
    public void setPhoneNumber(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_profile.phone_number</code>.
     */
    public String getPhoneNumber() {
        return (String) get(8);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_profile.email</code>.
     */
    public void setEmail(String value) {
        set(9, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_profile.email</code>.
     */
    public String getEmail() {
        return (String) get(9);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_profile.app_user_id</code>.
     */
    public void setAppUserId(String value) {
        set(10, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_profile.app_user_id</code>.
     */
    public String getAppUserId() {
        return (String) get(10);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_profile.maindb_group_id</code>.
     */
    public void setMaindbGroupId(Integer value) {
        set(11, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_profile.maindb_group_id</code>.
     */
    public Integer getMaindbGroupId() {
        return (Integer) get(11);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_profile.maindb_custom_id</code>.
     */
    public void setMaindbCustomId(String value) {
        set(12, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_profile.maindb_custom_id</code>.
     */
    public String getMaindbCustomId() {
        return (String) get(12);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_profile.maindb_custom_name</code>.
     */
    public void setMaindbCustomName(String value) {
        set(13, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_profile.maindb_custom_name</code>.
     */
    public String getMaindbCustomName() {
        return (String) get(13);
    }
}
