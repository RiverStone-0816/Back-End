package kr.co.eicn.ippbx.meta.jooq.customdb.tables.records;

import kr.co.eicn.ippbx.meta.jooq.customdb.Customdb;
import kr.co.eicn.ippbx.meta.jooq.customdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.customdb.Keys;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonChattMsg;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.MemoMsg;
import kr.co.eicn.ippbx.meta.jooq.pds.tables.records.PdsResearchResultRecord;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

public class CommonMemoMsg extends TableImpl<MemoMsgRecord> {

    /**
     * The reference instance of <code>CUSTOMDB.memo_msg</code>
     */
    public static final MemoMsg MEMO_MSG = new MemoMsg();
    /**
     * The column <code>CUSTOMDB.memo_msg.seq</code>.
     */
    public final TableField<MemoMsgRecord, Integer> SEQ = createField(DSL.name("seq"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "");
    /**
     * The column <code>CUSTOMDB.memo_msg.message_id</code>. 메모메세지아이디
     */
    public final TableField<MemoMsgRecord, String> MESSAGE_ID = createField(DSL.name("message_id"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "메모메세지아이디");
    /**
     * The column <code>CUSTOMDB.memo_msg.send_userid</code>. 메모발신자아이디
     */
    public final TableField<MemoMsgRecord, String> SEND_USERID = createField(DSL.name("send_userid"), org.jooq.impl.SQLDataType.VARCHAR(30).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "메모발신자아이디");
    /**
     * The column <code>CUSTOMDB.memo_msg.receive_userid</code>. 메모수신자아이디
     */
    public final TableField<MemoMsgRecord, String> RECEIVE_USERID = createField(DSL.name("receive_userid"), org.jooq.impl.SQLDataType.VARCHAR(30).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "메모수신자아이디");
    /**
     * The column <code>CUSTOMDB.memo_msg.insert_time</code>. 메세지인서트시간
     */
    public final TableField<MemoMsgRecord, Timestamp> INSERT_TIME = createField(DSL.name("insert_time"), org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(DSL.inline("2009-07-01 00:00:00", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "메세지인서트시간");
    /**
     * The column <code>CUSTOMDB.memo_msg.send_receive</code>. S-발신 R-수신
     */
    public final TableField<MemoMsgRecord, String> SEND_RECEIVE = createField(DSL.name("send_receive"), org.jooq.impl.SQLDataType.CHAR(3).defaultValue(DSL.inline("S", org.jooq.impl.SQLDataType.CHAR)), this, "S-발신 R-수신");
    /**
     * The column <code>CUSTOMDB.memo_msg.read_yn</code>. 읽음여부
     */
    public final TableField<MemoMsgRecord, String> READ_YN = createField(DSL.name("read_yn"), org.jooq.impl.SQLDataType.CHAR(1).defaultValue(DSL.inline("Y", org.jooq.impl.SQLDataType.CHAR)), this, "읽음여부");
    /**
     * The column <code>CUSTOMDB.memo_msg.content</code>. 메세지
     */
    public final TableField<MemoMsgRecord, String> CONTENT = createField(DSL.name("content"), org.jooq.impl.SQLDataType.VARCHAR(1100).nullable(false).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "메세지");
    private String tableName;

    /**
     * Create a <code>CUSTOMDB.memo_msg</code> table reference
     */
    public CommonMemoMsg(String companyName) {
        this(DSL.name("memo_msg_" + companyName), null);
    }

    /**
     * Create an aliased <code>CUSTOMDB.chatt_msg</code> table reference
     */
    public CommonMemoMsg(String alias, Table<MemoMsgRecord> aliased) {
        this(DSL.name(alias), aliased);
    }

    /**
     * Create an aliased <code>CUSTOMDB.memo_msg</code> table reference
     */
    public CommonMemoMsg(Name alias) {
        this(alias, MEMO_MSG);
    }

    private CommonMemoMsg(Name alias, Table<MemoMsgRecord> aliased) {
        this(alias, aliased, null);
    }

    private CommonMemoMsg(Name alias, Table<MemoMsgRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
        this.tableName = alias.last();
    }

    public <O extends Record> CommonMemoMsg(CommonChattMsg table, Table<O> child, ForeignKey<O, MemoMsgRecord> key) {
        super(child, key, MEMO_MSG);
        this.tableName = table.getName();
    }

    /**
     * The class holding records for this type
     */
    @Override
    public Class<MemoMsgRecord> getRecordType() {
        return MemoMsgRecord.class;
    }

    @Override
    public Schema getSchema() {
        return Customdb.CUSTOMDB;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.MEMO_MSG_INSERT_TIME, Indexes.MEMO_MSG_MESSAGE_ID, Indexes.MEMO_MSG_RECEIVE_USERID, Indexes.MEMO_MSG_SEND_USERID);
    }

    @Override
    public Identity<MemoMsgRecord, Integer> getIdentity() {
        return (Identity<MemoMsgRecord, Integer>) super.getIdentity();
    }

    @Override
    public List<UniqueKey<MemoMsgRecord>> getKeys() {
        return Arrays.<UniqueKey<MemoMsgRecord>>asList(Keys.KEY_MEMO_MSG_SEQ);
    }

    @Override
    public CommonMemoMsg as(String alias) {
        return new CommonMemoMsg(DSL.name(alias), this);
    }

    @Override
    public CommonMemoMsg as(Name alias) {
        return new CommonMemoMsg(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonMemoMsg rename(String name) {
        return new CommonMemoMsg(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonMemoMsg rename(Name name) {
        return new CommonMemoMsg(name, null);
    }
}
