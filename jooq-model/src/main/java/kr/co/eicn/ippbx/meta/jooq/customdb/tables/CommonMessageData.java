package kr.co.eicn.ippbx.meta.jooq.customdb.tables;

import kr.co.eicn.ippbx.meta.jooq.customdb.Customdb;
import kr.co.eicn.ippbx.meta.jooq.customdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.MessageDataRecord;
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

public class CommonMessageData extends TableImpl<MessageDataRecord> {
    /**
     * The column <code>CUSTOMDB.message_data.seq</code>.
     */
    public final TableField<MessageDataRecord, Integer> SEQ = createField(DSL.name("seq"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>CUSTOMDB.message_data.send_cli_key</code>. 메세지아이디
     */
    public final TableField<MessageDataRecord, String> SEND_CLI_KEY = createField(DSL.name("send_cli_key"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "메세지아이디");

    /**
     * The column <code>CUSTOMDB.message_data.title</code>. MMS타이틀
     */
    public final TableField<MessageDataRecord, String> TITLE = createField(DSL.name("title"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "MMS타이틀");

    /**
     * The column <code>CUSTOMDB.message_data.status</code>. 상태S,R
     */
    public final TableField<MessageDataRecord, String> STATUS = createField(DSL.name("status"), SQLDataType.CHAR(1).defaultValue(DSL.field("''", SQLDataType.CHAR)), this, "상태S,R");

    /**
     * The column <code>CUSTOMDB.message_data.result_status</code>. 상태S,R
     */
    public final TableField<MessageDataRecord, String> RESULT_STATUS = createField(DSL.name("result_status"), SQLDataType.CHAR(1).defaultValue(DSL.field("''", SQLDataType.CHAR)), this, "상태S,R");

    /**
     * The column <code>CUSTOMDB.message_data.confirm_status</code>. 상태S,R
     */
    public final TableField<MessageDataRecord, String> CONFIRM_STATUS = createField(DSL.name("confirm_status"), SQLDataType.CHAR(1).defaultValue(DSL.field("''", SQLDataType.CHAR)), this, "상태S,R");

    /**
     * The column <code>CUSTOMDB.message_data.insert_time</code>.
     */
    public final TableField<MessageDataRecord, Timestamp> INSERT_TIME = createField(DSL.name("insert_time"), SQLDataType.TIMESTAMP(0).defaultValue(DSL.field("'2009-07-01 00:00:00'", SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.message_data.send_time</code>.
     */
    public final TableField<MessageDataRecord, Timestamp> SEND_TIME = createField(DSL.name("send_time"), SQLDataType.TIMESTAMP(0).defaultValue(DSL.field("'2009-07-01 00:00:00'", SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.message_data.result_time</code>.
     */
    public final TableField<MessageDataRecord, Timestamp> RESULT_TIME = createField(DSL.name("result_time"), SQLDataType.TIMESTAMP(0).defaultValue(DSL.field("'2009-07-01 00:00:00'", SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.message_data.confirm_time</code>.
     */
    public final TableField<MessageDataRecord, Timestamp> CONFIRM_TIME = createField(DSL.name("confirm_time"), SQLDataType.TIMESTAMP(0).defaultValue(DSL.field("'2009-07-01 00:00:00'", SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.message_data.service</code>. SMS,MMS,LMS,KAKAO,RCS
     */
    public final TableField<MessageDataRecord, String> SERVICE = createField(DSL.name("service"), SQLDataType.CHAR(10).defaultValue(DSL.field("''", SQLDataType.CHAR)), this, "SMS,MMS,LMS,KAKAO,RCS");

    /**
     * The column <code>CUSTOMDB.message_data.callback</code>. 발신번호
     */
    public final TableField<MessageDataRecord, String> CALLBACK = createField(DSL.name("callback"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "발신번호");

    /**
     * The column <code>CUSTOMDB.message_data.phone_number</code>. 수신번호
     */
    public final TableField<MessageDataRecord, String> PHONE_NUMBER = createField(DSL.name("phone_number"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "수신번호");

    /**
     * The column <code>CUSTOMDB.message_data.message</code>.
     */
    public final TableField<MessageDataRecord, String> MESSAGE = createField(DSL.name("message"), SQLDataType.VARCHAR(2000).defaultValue(DSL.field("NULL", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.message_data.userid</code>. 상담사아이디
     */
    public final TableField<MessageDataRecord, String> USERID = createField(DSL.name("userid"), SQLDataType.VARCHAR(30).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "상담사아이디");

    /**
     * The column <code>CUSTOMDB.message_data.project_id</code>. 메세지허브프로젝트아이디
     */
    public final TableField<MessageDataRecord, String> PROJECT_ID = createField(DSL.name("project_id"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "메세지허브프로젝트아이디");

    /**
     * The column <code>CUSTOMDB.message_data.api_key</code>. 메세지허브프로젝트API키
     */
    public final TableField<MessageDataRecord, String> API_KEY = createField(DSL.name("api_key"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "메세지허브프로젝트API키");

    /**
     * The column <code>CUSTOMDB.message_data.attach_file</code>. 첨부파일명
     */
    public final TableField<MessageDataRecord, String> ATTACH_FILE = createField(DSL.name("attach_file"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "첨부파일명");

    /**
     * The column <code>CUSTOMDB.message_data.company_id</code>. 회사아이디
     */
    public final TableField<MessageDataRecord, String> COMPANY_ID = createField(DSL.name("company_id"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "회사아이디");

    /**
     * The column <code>CUSTOMDB.message_data.res_data_msgkey</code>. 결과메세지
     */
    public final TableField<MessageDataRecord, String> RES_DATA_MSGKEY = createField(DSL.name("res_data_msgkey"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "결과메세지");

    /**
     * The column <code>CUSTOMDB.message_data.res_code</code>. 결과코드
     */
    public final TableField<MessageDataRecord, String> RES_CODE = createField(DSL.name("res_code"), SQLDataType.VARCHAR(10).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "결과코드");

    /**
     * The column <code>CUSTOMDB.message_data.res_message</code>. 결과메세지
     */
    public final TableField<MessageDataRecord, String> RES_MESSAGE = createField(DSL.name("res_message"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "결과메세지");

    /**
     * The column <code>CUSTOMDB.message_data.res_data_code</code>. 메세지발송코드
     */
    public final TableField<MessageDataRecord, String> RES_DATA_CODE = createField(DSL.name("res_data_code"), SQLDataType.VARCHAR(10).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "메세지발송코드");

    /**
     * The column <code>CUSTOMDB.message_data.res_data_message</code>. 메세지발송메세지
     */
    public final TableField<MessageDataRecord, String> RES_DATA_MESSAGE = createField(DSL.name("res_data_message"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "메세지발송메세지");

    /**
     * The column <code>CUSTOMDB.message_data.confirm_code</code>. 메세지발송코드
     */
    public final TableField<MessageDataRecord, String> CONFIRM_CODE = createField(DSL.name("confirm_code"), SQLDataType.VARCHAR(10).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "메세지발송코드");

    /**
     * The column <code>CUSTOMDB.message_data.confirm_message</code>. 메세지발송메세지
     */
    public final TableField<MessageDataRecord, String> CONFIRM_MESSAGE = createField(DSL.name("confirm_message"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "메세지발송메세지");

    /**
     * The column <code>CUSTOMDB.message_data.telco</code>. 통신사
     */
    public final TableField<MessageDataRecord, String> TELCO = createField(DSL.name("telco"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "통신사");

    /**
     * The column <code>CUSTOMDB.message_data.retry_cnt</code>. 재전송횟수
     */
    public final TableField<MessageDataRecord, Integer> RETRY_CNT = createField(DSL.name("retry_cnt"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "재전송횟수");

    private final String tableName;

    /**
     * Create a <code>CUSTOMDB.message_data</code> table reference
     */
    public CommonMessageData(String companyName) {
        this(DSL.name("message_data_" + companyName), null);
    }

    /**
     * Create an aliased <code>CUSTOMDB.message_data</code> table reference
     */
    public CommonMessageData(String alias, Table<MessageDataRecord> aliased) {
        this(DSL.name(alias), aliased);
    }

    /**
     * Create an aliased <code>CUSTOMDB.message_data</code> table reference
     */
    private CommonMessageData(Name alias, Table<MessageDataRecord> aliased) {
        this(alias, aliased, null);
    }

    private CommonMessageData(Name alias, Table<MessageDataRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
        this.tableName = alias.last();
    }

    public <O extends Record> CommonMessageData(CommonMessageData table, Table<O> child, ForeignKey<O, MessageDataRecord> key) {
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
        return Arrays.<Index>asList(Indexes.MESSAGE_DATA_INSERT_TIME, Indexes.MESSAGE_DATA_SEND_CLI_KEY, Indexes.MESSAGE_DATA_USERID);
    }

    @Override
    public Identity<MessageDataRecord, Integer> getIdentity() {
        return Internal.createIdentity(this, this.SEQ);
    }

    @NotNull
    @Override
    public List<UniqueKey<MessageDataRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ));
    }

    @NotNull
    @Override
    public CommonMessageData as(String alias) {
        return new CommonMessageData(DSL.name(alias), this);
    }

    @NotNull
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
