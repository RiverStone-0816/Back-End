package kr.co.eicn.ippbx.meta.jooq.customdb.tables;

import kr.co.eicn.ippbx.meta.jooq.customdb.Customdb;
import kr.co.eicn.ippbx.meta.jooq.customdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.CommonMessageDataRecord;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.jooq.impl.TableImpl;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommonMessageData extends TableImpl<CommonMessageDataRecord> {
    /**
     * The class holding records for this type
     */
    @Override
    public Class<CommonMessageDataRecord> getRecordType() {
        return CommonMessageDataRecord.class;
    }

    /**
     * The column <code>CUSTOMDB.message_data.seq</code>.
     */
    public final TableField<CommonMessageDataRecord, Integer> SEQ = createField(DSL.name("seq"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>CUSTOMDB.message_data.send_cli_key</code>. 메세지아이디
     */
    public final TableField<CommonMessageDataRecord, String> SEND_CLI_KEY = createField(DSL.name("send_cli_key"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "메세지아이디");

    /**
     * The column <code>CUSTOMDB.message_data.title</code>. MMS타이틀
     */
    public final TableField<CommonMessageDataRecord, String> TITLE = createField(DSL.name("title"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "MMS타이틀");

    /**
     * The column <code>CUSTOMDB.message_data.status</code>. 상태S,R
     */
    public final TableField<CommonMessageDataRecord, String> STATUS = createField(DSL.name("status"), org.jooq.impl.SQLDataType.CHAR(1).defaultValue(org.jooq.impl.DSL.inline("''", org.jooq.impl.SQLDataType.CHAR)), this, "상태S,R");

    /**
     * The column <code>CUSTOMDB.message_data.result_status</code>. 상태S,R
     */
    public final TableField<CommonMessageDataRecord, String> RESULT_STATUS = createField(DSL.name("result_status"), org.jooq.impl.SQLDataType.CHAR(1).defaultValue(org.jooq.impl.DSL.inline("''", org.jooq.impl.SQLDataType.CHAR)), this, "상태S,R");

    /**
     * The column <code>CUSTOMDB.message_data.insert_time</code>.
     */
    public final TableField<CommonMessageDataRecord, Timestamp> INSERT_TIME = createField(DSL.name("insert_time"), org.jooq.impl.SQLDataType.TIMESTAMP.defaultValue(org.jooq.impl.DSL.inline("'2009-07-01 00:00:00'", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.message_data.send_time</code>.
     */
    public final TableField<CommonMessageDataRecord, Timestamp> SEND_TIME = createField(DSL.name("send_time"), org.jooq.impl.SQLDataType.TIMESTAMP.defaultValue(org.jooq.impl.DSL.inline("'2009-07-01 00:00:00'", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.message_data.result_time</code>.
     */
    public final TableField<CommonMessageDataRecord, Timestamp> RESULT_TIME = createField(DSL.name("result_time"), org.jooq.impl.SQLDataType.TIMESTAMP.defaultValue(org.jooq.impl.DSL.inline("'2009-07-01 00:00:00'", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.message_data.service</code>. SMS,MMS,LMS,KAKAO,RCS
     */
    public final TableField<CommonMessageDataRecord, String> SERVICE = createField(DSL.name("service"), org.jooq.impl.SQLDataType.CHAR(10).defaultValue(org.jooq.impl.DSL.inline("''", org.jooq.impl.SQLDataType.CHAR)), this, "SMS,MMS,LMS,KAKAO,RCS");

    /**
     * The column <code>CUSTOMDB.message_data.callback</code>. 발신번호
     */
    public final TableField<CommonMessageDataRecord, String> CALLBACK = createField(DSL.name("callback"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "발신번호");

    /**
     * The column <code>CUSTOMDB.message_data.phone_number</code>. 수신번호
     */
    public final TableField<CommonMessageDataRecord, String> PHONE_NUMBER = createField(DSL.name("phone_number"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "수신번호");

    /**
     * The column <code>CUSTOMDB.message_data.message</code>. 메세지
     */
    public final TableField<CommonMessageDataRecord, String> MESSAGE = createField(DSL.name("message"), org.jooq.impl.SQLDataType.VARCHAR(160).defaultValue(org.jooq.impl.DSL.inline("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "메세지");

    /**
     * The column <code>CUSTOMDB.message_data.userid</code>. 상담사아이디
     */
    public final TableField<CommonMessageDataRecord, String> USERID = createField(DSL.name("userid"), org.jooq.impl.SQLDataType.VARCHAR(30).defaultValue(org.jooq.impl.DSL.inline("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "상담사아이디");

    /**
     * The column <code>CUSTOMDB.message_data.project_id</code>. 메세지허브프로젝트아이디
     */
    public final TableField<CommonMessageDataRecord, String> PROJECT_ID = createField(DSL.name("project_id"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "메세지허브프로젝트아이디");

    /**
     * The column <code>CUSTOMDB.message_data.api_key</code>. 메세지허브프로젝트API키
     */
    public final TableField<CommonMessageDataRecord, String> API_KEY = createField(DSL.name("api_key"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "메세지허브프로젝트API키");

    /**
     * The column <code>CUSTOMDB.message_data.attach_file</code>. 첨부파일명
     */
    public final TableField<CommonMessageDataRecord, String> ATTACH_FILE = createField(DSL.name("attach_file"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "첨부파일명");

    /**
     * The column <code>CUSTOMDB.message_data.company_id</code>. 회사아이디
     */
    public final TableField<CommonMessageDataRecord, String> COMPANY_ID = createField(DSL.name("company_id"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "회사아이디");

    /**
     * The column <code>CUSTOMDB.message_data.res_data_msgkey</code>. 결과메세지
     */
    public final TableField<CommonMessageDataRecord, String> RES_DATA_MSGKEY = createField(DSL.name("res_data_msgkey"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "결과메세지");

    /**
     * The column <code>CUSTOMDB.message_data.res_code</code>. 결과코드
     */
    public final TableField<CommonMessageDataRecord, String> RES_CODE = createField(DSL.name("res_code"), org.jooq.impl.SQLDataType.VARCHAR(10).defaultValue(org.jooq.impl.DSL.inline("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "결과코드");

    /**
     * The column <code>CUSTOMDB.message_data.res_message</code>. 결과메세지
     */
    public final TableField<CommonMessageDataRecord, String> RES_MESSAGE = createField(DSL.name("res_message"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "결과메세지");

    /**
     * The column <code>CUSTOMDB.message_data.res_data_code</code>. 메세지발송코드
     */
    public final TableField<CommonMessageDataRecord, String> RES_DATA_CODE = createField(DSL.name("res_data_code"), org.jooq.impl.SQLDataType.VARCHAR(10).defaultValue(org.jooq.impl.DSL.inline("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "메세지발송코드");

    /**
     * The column <code>CUSTOMDB.message_data.res_data_message</code>. 메세지발송메세지
     */
    public final TableField<CommonMessageDataRecord, String> RES_DATA_MESSAGE = createField(DSL.name("res_data_message"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "메세지발송메세지");

    /**
     * The column <code>CUSTOMDB.message_data.retry_cnt</code>. 재전송횟수
     */
    public final TableField<CommonMessageDataRecord, Integer> RETRY_CNT = createField(DSL.name("retry_cnt"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "재전송횟수");

    /**
     * Create a <code>CUSTOMDB.message_data</code> table reference
     */
    public CommonMessageData(String companyName) {
        this(DSL.name("message_data_" + companyName), null);
    }

    /**
     * Create an aliased <code>CUSTOMDB.message_data</code> table reference
     */
    public CommonMessageData(String alias, Table<CommonMessageDataRecord> aliased) {
        this(DSL.name(alias), aliased);
    }

    /**
     * Create an aliased <code>CUSTOMDB.message_data</code> table reference
     */
    private CommonMessageData(Name alias, Table<CommonMessageDataRecord> aliased) {
        this(alias, aliased, null);
    }

    private CommonMessageData(Name alias, Table<CommonMessageDataRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    @Override
    public Schema getSchema() {
        return Customdb.CUSTOMDB;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.MESSAGE_DATA_INSERT_TIME, Indexes.MESSAGE_DATA_SEND_CLI_KEY, Indexes.MESSAGE_DATA_USERID);
    }

    @Override
    public Identity<CommonMessageDataRecord, Integer> getIdentity() {
        return org.jooq.impl.Internal.createIdentity(this, this.SEQ);
    }

    @Override
    public List<UniqueKey<CommonMessageDataRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, "KEY_" + getName() + "_PRIMARY", this.SEQ));
    }

    @Override
    public CommonMessageData as(String alias) {
        return new CommonMessageData(DSL.name(alias), this);
    }

    @Override
    public CommonMessageData as(Name alias) {
        return new CommonMessageData(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonMessageData rename(String name) {
        return new CommonMessageData(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonMessageData rename(Name name) {
        return new CommonMessageData(name, null);
    }
}