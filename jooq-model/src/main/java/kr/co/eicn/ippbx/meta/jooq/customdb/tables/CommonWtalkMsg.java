package kr.co.eicn.ippbx.meta.jooq.customdb.tables;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.CommonWtalkMsgRecord;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.jooq.impl.TableImpl;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

public class CommonWtalkMsg extends TableImpl<CommonWtalkMsgRecord> {
    /**
     * The column <code>CUSTOMDB.talk_msg.seq</code>.
     */
    public final TableField<CommonWtalkMsgRecord, Integer> SEQ = createField(DSL.name("seq"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>CUSTOMDB.talk_msg.channel_type</code>. 상담톡 서비스 유형, eicn/kakao/navertt/naverline
     */
    public final TableField<CommonWtalkMsgRecord, String> CHANNEL_TYPE = createField(DSL.name("channel_type"), org.jooq.impl.SQLDataType.VARCHAR(20).nullable(false).defaultValue(DSL.inline("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "상담톡 서비스 유형, eicn/kakao/navertt/naverline");

    /**
     * The column <code>CUSTOMDB.talk_msg.insert_time</code>.
     */
    public final TableField<CommonWtalkMsgRecord, Timestamp> INSERT_TIME = createField(DSL.name("insert_time"), org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(DSL.inline("2009-07-01 00:00:00", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.talk_msg.send_receive</code>.
     */
    public final TableField<CommonWtalkMsgRecord, String> SEND_RECEIVE = createField(DSL.name("send_receive"), org.jooq.impl.SQLDataType.CHAR(3).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.CHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.talk_msg.company_id</code>.
     */
    public final TableField<CommonWtalkMsgRecord, String> COMPANY_ID = createField(DSL.name("company_id"), org.jooq.impl.SQLDataType.VARCHAR(30).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.talk_msg.userid</code>.
     */
    public final TableField<CommonWtalkMsgRecord, String> USERID = createField(DSL.name("userid"), org.jooq.impl.SQLDataType.VARCHAR(30).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.talk_msg.user_key</code>.
     */
    public final TableField<CommonWtalkMsgRecord, String> USER_KEY = createField(DSL.name("user_key"), org.jooq.impl.SQLDataType.VARCHAR(30).nullable(false).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.talk_msg.sender_key</code>.
     */
    public final TableField<CommonWtalkMsgRecord, String> SENDER_KEY = createField(DSL.name("sender_key"), org.jooq.impl.SQLDataType.VARCHAR(50).nullable(false).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.talk_msg.message_id</code>.
     */
    public final TableField<CommonWtalkMsgRecord, String> MESSAGE_ID = createField(DSL.name("message_id"), org.jooq.impl.SQLDataType.VARCHAR(40).nullable(false).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.talk_msg.time</code>.
     */
    public final TableField<CommonWtalkMsgRecord, String> TIME = createField(DSL.name("time"), org.jooq.impl.SQLDataType.VARCHAR(30).nullable(false).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.talk_msg.type</code>.
     */
    public final TableField<CommonWtalkMsgRecord, String> TYPE = createField(DSL.name("type"), org.jooq.impl.SQLDataType.VARCHAR(10).nullable(false).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.talk_msg.content</code>.
     */
    public final TableField<CommonWtalkMsgRecord, String> CONTENT = createField(DSL.name("content"), org.jooq.impl.SQLDataType.VARCHAR(1100).nullable(false).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.talk_msg.attachment</code>.
     */
    public final TableField<CommonWtalkMsgRecord, String> ATTACHMENT = createField(DSL.name("attachment"), org.jooq.impl.SQLDataType.VARCHAR(300).nullable(false).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.talk_msg.extra</code>.
     */
    public final TableField<CommonWtalkMsgRecord, String> EXTRA = createField(DSL.name("extra"), org.jooq.impl.SQLDataType.VARCHAR(300).nullable(false).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.talk_msg.room_id</code>.
     */
    public final TableField<CommonWtalkMsgRecord, String> ROOM_ID = createField(DSL.name("room_id"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    private String tableName;

    /**
     * Create a <code>CUSTOMDB.talk_msg</code> table reference
     */
    public CommonWtalkMsg(String tableName) {
        this(DSL.name("wtalk_msg_" + tableName), null);
    }

    private CommonWtalkMsg(Name alias, Table<CommonWtalkMsgRecord> aliased) {
        this(alias, aliased, null);

    }

    private CommonWtalkMsg(Name alias, Table<CommonWtalkMsgRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
        this.tableName = alias.last();
    }

    public <O extends Record> CommonWtalkMsg(CommonWtalkMsg table, Table<O> child, ForeignKey<O, CommonWtalkMsgRecord> key) {
        super(child, key, table);
        this.tableName = table.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<CommonWtalkMsgRecord, Integer> getIdentity() {
        return Internal.createIdentity(this, this.SEQ);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<CommonWtalkMsgRecord> getPrimaryKey() {
        return Internal.createUniqueKey(this, "KEY_" + getName() + "_PRIMARY", this.SEQ);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<CommonWtalkMsgRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, "KEY_" + getName() + "_PRIMARY", this.SEQ));
    }

    @Override
    public CommonWtalkMsg as(String alias) {
        return new CommonWtalkMsg(DSL.name(alias), this);
    }

    @Override
    public CommonWtalkMsg as(Name alias) {
        return new CommonWtalkMsg(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonWtalkMsg rename(String name) {
        return new CommonWtalkMsg(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonWtalkMsg rename(Name name) {
        return new CommonWtalkMsg(name, null);
    }
}
