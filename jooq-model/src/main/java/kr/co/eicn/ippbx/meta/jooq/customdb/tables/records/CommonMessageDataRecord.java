package kr.co.eicn.ippbx.meta.jooq.customdb.tables.records;

import org.jooq.Table;
import org.jooq.impl.TableRecordImpl;

import java.sql.Timestamp;

public class CommonMessageDataRecord extends TableRecordImpl<CommonMessageDataRecord> {

    public CommonMessageDataRecord(Table<CommonMessageDataRecord> table) {
        super(table);
    }

    /**
     * Setter for <code>CUSTOMDB.message_data.seq</code>.
     */
    public CommonMessageDataRecord setSeq(Integer value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>CUSTOMDB.message_data.seq</code>.
     */
    public Integer getSeq() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>CUSTOMDB.message_data.send_cli_key</code>. 메세지아이디
     */
    public CommonMessageDataRecord setSendCliKey(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>CUSTOMDB.message_data.send_cli_key</code>. 메세지아이디
     */
    public String getSendCliKey() {
        return (String) get(1);
    }

    /**
     * Setter for <code>CUSTOMDB.message_data.title</code>. MMS타이틀
     */
    public CommonMessageDataRecord setTitle(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>CUSTOMDB.message_data.title</code>. MMS타이틀
     */
    public String getTitle() {
        return (String) get(2);
    }

    /**
     * Setter for <code>CUSTOMDB.message_data.status</code>. 상태S,R
     */
    public CommonMessageDataRecord setStatus(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>CUSTOMDB.message_data.status</code>. 상태S,R
     */
    public String getStatus() {
        return (String) get(3);
    }

    /**
     * Setter for <code>CUSTOMDB.message_data.result_status</code>. 상태S,R
     */
    public CommonMessageDataRecord setResultStatus(String value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>CUSTOMDB.message_data.result_status</code>. 상태S,R
     */
    public String getResultStatus() {
        return (String) get(4);
    }

    /**
     * Setter for <code>CUSTOMDB.message_data.insert_time</code>.
     */
    public CommonMessageDataRecord setInsertTime(Timestamp value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>CUSTOMDB.message_data.insert_time</code>.
     */
    public Timestamp getInsertTime() {
        return (Timestamp) get(5);
    }

    /**
     * Setter for <code>CUSTOMDB.message_data.send_time</code>.
     */
    public CommonMessageDataRecord setSendTime(Timestamp value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>CUSTOMDB.message_data.send_time</code>.
     */
    public Timestamp getSendTime() {
        return (Timestamp) get(6);
    }

    /**
     * Setter for <code>CUSTOMDB.message_data.result_time</code>.
     */
    public CommonMessageDataRecord setResultTime(Timestamp value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>CUSTOMDB.message_data.result_time</code>.
     */
    public Timestamp getResultTime() {
        return (Timestamp) get(7);
    }

    /**
     * Setter for <code>CUSTOMDB.message_data.service</code>. SMS,MMS,LMS,KAKAO,RCS
     */
    public CommonMessageDataRecord setService(String value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for <code>CUSTOMDB.message_data.service</code>. SMS,MMS,LMS,KAKAO,RCS
     */
    public String getService() {
        return (String) get(8);
    }

    /**
     * Setter for <code>CUSTOMDB.message_data.callback</code>. 발신번호
     */
    public CommonMessageDataRecord setCallback(String value) {
        set(9, value);
        return this;
    }

    /**
     * Getter for <code>CUSTOMDB.message_data.callback</code>. 발신번호
     */
    public String getCallback() {
        return (String) get(9);
    }

    /**
     * Setter for <code>CUSTOMDB.message_data.phone_number</code>. 수신번호
     */
    public CommonMessageDataRecord setPhoneNumber(String value) {
        set(10, value);
        return this;
    }

    /**
     * Getter for <code>CUSTOMDB.message_data.phone_number</code>. 수신번호
     */
    public String getPhoneNumber() {
        return (String) get(10);
    }

    /**
     * Setter for <code>CUSTOMDB.message_data.message</code>. 메세지
     */
    public CommonMessageDataRecord setMessage(String value) {
        set(11, value);
        return this;
    }

    /**
     * Getter for <code>CUSTOMDB.message_data.message</code>. 메세지
     */
    public String getMessage() {
        return (String) get(11);
    }

    /**
     * Setter for <code>CUSTOMDB.message_data.userid</code>. 상담사아이디
     */
    public CommonMessageDataRecord setUserid(String value) {
        set(12, value);
        return this;
    }

    /**
     * Getter for <code>CUSTOMDB.message_data.userid</code>. 상담사아이디
     */
    public String getUserid() {
        return (String) get(12);
    }

    /**
     * Setter for <code>CUSTOMDB.message_data.project_id</code>. 메세지허브프로젝트아이디
     */
    public CommonMessageDataRecord setProjectId(String value) {
        set(13, value);
        return this;
    }

    /**
     * Getter for <code>CUSTOMDB.message_data.project_id</code>. 메세지허브프로젝트아이디
     */
    public String getProjectId() {
        return (String) get(13);
    }

    /**
     * Setter for <code>CUSTOMDB.message_data.api_key</code>. 메세지허브프로젝트API키
     */
    public CommonMessageDataRecord setApiKey(String value) {
        set(14, value);
        return this;
    }

    /**
     * Getter for <code>CUSTOMDB.message_data.api_key</code>. 메세지허브프로젝트API키
     */
    public String getApiKey() {
        return (String) get(14);
    }

    /**
     * Setter for <code>CUSTOMDB.message_data.attach_file</code>. 첨부파일명
     */
    public CommonMessageDataRecord setAttachFile(String value) {
        set(15, value);
        return this;
    }

    /**
     * Getter for <code>CUSTOMDB.message_data.attach_file</code>. 첨부파일명
     */
    public String getAttachFile() {
        return (String) get(15);
    }

    /**
     * Setter for <code>CUSTOMDB.message_data.company_id</code>. 회사아이디
     */
    public CommonMessageDataRecord setCompanyId(String value) {
        set(16, value);
        return this;
    }

    /**
     * Getter for <code>CUSTOMDB.message_data.company_id</code>. 회사아이디
     */
    public String getCompanyId() {
        return (String) get(16);
    }

    /**
     * Setter for <code>CUSTOMDB.message_data.res_data_msgkey</code>. 결과메세지
     */
    public CommonMessageDataRecord setResDataMsgkey(String value) {
        set(17, value);
        return this;
    }

    /**
     * Getter for <code>CUSTOMDB.message_data.res_data_msgkey</code>. 결과메세지
     */
    public String getResDataMsgkey() {
        return (String) get(17);
    }

    /**
     * Setter for <code>CUSTOMDB.message_data.res_code</code>. 결과코드
     */
    public CommonMessageDataRecord setResCode(String value) {
        set(18, value);
        return this;
    }

    /**
     * Getter for <code>CUSTOMDB.message_data.res_code</code>. 결과코드
     */
    public String getResCode() {
        return (String) get(18);
    }

    /**
     * Setter for <code>CUSTOMDB.message_data.res_message</code>. 결과메세지
     */
    public CommonMessageDataRecord setResMessage(String value) {
        set(19, value);
        return this;
    }

    /**
     * Getter for <code>CUSTOMDB.message_data.res_message</code>. 결과메세지
     */
    public String getResMessage() {
        return (String) get(19);
    }

    /**
     * Setter for <code>CUSTOMDB.message_data.res_data_code</code>. 메세지발송코드
     */
    public CommonMessageDataRecord setResDataCode(String value) {
        set(20, value);
        return this;
    }

    /**
     * Getter for <code>CUSTOMDB.message_data.res_data_code</code>. 메세지발송코드
     */
    public String getResDataCode() {
        return (String) get(20);
    }

    /**
     * Setter for <code>CUSTOMDB.message_data.res_data_message</code>. 메세지발송메세지
     */
    public CommonMessageDataRecord setResDataMessage(String value) {
        set(21, value);
        return this;
    }

    /**
     * Getter for <code>CUSTOMDB.message_data.res_data_message</code>. 메세지발송메세지
     */
    public String getResDataMessage() {
        return (String) get(21);
    }

    /**
     * Setter for <code>CUSTOMDB.message_data.retry_cnt</code>. 재전송횟수
     */
    public CommonMessageDataRecord setRetryCnt(Integer value) {
        set(22, value);
        return this;
    }

    /**
     * Getter for <code>CUSTOMDB.message_data.retry_cnt</code>. 재전송횟수
     */
    public Integer getRetryCnt() {
        return (Integer) get(22);
    }


}
