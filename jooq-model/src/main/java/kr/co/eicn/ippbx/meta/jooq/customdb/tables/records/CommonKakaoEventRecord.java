package kr.co.eicn.ippbx.meta.jooq.customdb.tables.records;

import org.jooq.Table;
import org.jooq.impl.TableRecordImpl;

import java.sql.Timestamp;

public class CommonKakaoEventRecord extends TableRecordImpl<CommonKakaoEventRecord> {

    public CommonKakaoEventRecord(Table<CommonKakaoEventRecord> table) {
        super(table);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_event.seq</code>.
     */
    public void setSeq(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_event.seq</code>.
     */
    public Integer getSeq() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_event.insert_date</code>.
     */
    public void setInsertDate(Timestamp value) {
        set(1, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_event.insert_date</code>.
     */
    public Timestamp getInsertDate() {
        return (Timestamp) get(1);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_event.update_date</code>.
     */
    public void setUpdateDate(Timestamp value) {
        set(2, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_event.update_date</code>.
     */
    public Timestamp getUpdateDate() {
        return (Timestamp) get(2);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_event.bot_id</code>.
     */
    public void setBotId(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_event.bot_id</code>.
     */
    public String getBotId() {
        return (String) get(3);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_event.bot_name</code>.
     */
    public void setBotName(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_event.bot_name</code>.
     */
    public String getBotName() {
        return (String) get(4);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_event.event_name</code>.
     */
    public void setEventName(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_event.event_name</code>.
     */
    public String getEventName() {
        return (String) get(5);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_event.user_type</code>.
     */
    public void setUserType(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_event.user_type</code>.
     */
    public String getUserType() {
        return (String) get(6);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_event.user_id</code>.
     */
    public void setUserId(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_event.user_id</code>.
     */
    public String getUserId() {
        return (String) get(7);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_event.user_name</code>.
     */
    public void setUserName(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_event.user_name</code>.
     */
    public String getUserName() {
        return (String) get(8);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_event.user_data</code>.
     */
    public void setUserData(String value) {
        set(9, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_event.user_data</code>.
     */
    public String getUserData() {
        return (String) get(9);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_event.task_id</code>.
     */
    public void setTaskId(String value) {
        set(10, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_event.task_id</code>.
     */
    public String getTaskId() {
        return (String) get(10);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_event.response_block_id</code>.
     */
    public void setResponseBlockId(String value) {
        set(11, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_event.response_block_id</code>.
     */
    public String getResponseBlockId() {
        return (String) get(11);
    }

    /**
     * Setter for <code>CUSTOMDB.kakao_event.response_block_name</code>.
     */
    public void setResponseBlockName(String value) {
        set(12, value);
    }

    /**
     * Getter for <code>CUSTOMDB.kakao_event.response_block_name</code>.
     */
    public String getResponseBlockName() {
        return (String) get(12);
    }
}
