package kr.co.eicn.ippbx.meta.jooq.statdb.tables;

import kr.co.eicn.ippbx.meta.jooq.statdb.tables.records.CommonStatWtalkRecord;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.jooq.impl.TableImpl;

import java.sql.Date;
import java.util.Collections;
import java.util.List;

public class CommonStatWtalk extends TableImpl<CommonStatWtalkRecord> {

    /**
     * The column <code>STATDB.stat_wtalk.seq</code>.
     */
    public final TableField<CommonStatWtalkRecord, Integer> SEQ = createField(DSL.name("seq"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>STATDB.stat_wtalk.company_id</code>. 회사아이디
     */
    public final TableField<CommonStatWtalkRecord, String> COMPANY_ID = createField(DSL.name("company_id"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "회사아이디");

    /**
     * The column <code>STATDB.stat_wtalk.channel_type</code>. 상담톡서비스유형
     */
    public final TableField<CommonStatWtalkRecord, String> CHANNEL_TYPE = createField(DSL.name("channel_type"), org.jooq.impl.SQLDataType.VARCHAR(10).defaultValue(org.jooq.impl.DSL.inline("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "상담톡서비스유형");

    /**
     * The column <code>STATDB.stat_wtalk.sender_key</code>. 상담톡카카오가입서비스아이디
     */
    public final TableField<CommonStatWtalkRecord, String> SENDER_KEY = createField(DSL.name("sender_key"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "상담톡카카오가입서비스아이디");

    /**
     * The column <code>STATDB.stat_wtalk.userid</code>. 상담원 아이디
     */
    public final TableField<CommonStatWtalkRecord, String> USERID = createField(DSL.name("userid"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "상담원 아이디");

    /**
     * The column <code>STATDB.stat_wtalk.group_code</code>. 조직코드
     */
    public final TableField<CommonStatWtalkRecord, String> GROUP_CODE = createField(DSL.name("group_code"), org.jooq.impl.SQLDataType.CHAR(4).defaultValue(org.jooq.impl.DSL.inline("NULL", org.jooq.impl.SQLDataType.CHAR)), this, "조직코드");

    /**
     * The column <code>STATDB.stat_wtalk.group_tree_name</code>. 조직트리명
     */
    public final TableField<CommonStatWtalkRecord, String> GROUP_TREE_NAME = createField(DSL.name("group_tree_name"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "조직트리명");

    /**
     * The column <code>STATDB.stat_wtalk.group_level</code>. 조직레벨
     */
    public final TableField<CommonStatWtalkRecord, Integer> GROUP_LEVEL = createField(DSL.name("group_level"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "조직레벨");

    /**
     * The column <code>STATDB.stat_wtalk.worktime_yn</code>.
     */
    public final TableField<CommonStatWtalkRecord, String> WORKTIME_YN = createField(DSL.name("worktime_yn"), org.jooq.impl.SQLDataType.CHAR(1).defaultValue(org.jooq.impl.DSL.inline("'Y'", org.jooq.impl.SQLDataType.CHAR)), this, "");

    /**
     * The column <code>STATDB.stat_wtalk.stat_date</code>.
     */
    public final TableField<CommonStatWtalkRecord, Date> STAT_DATE = createField(DSL.name("stat_date"), org.jooq.impl.SQLDataType.DATE.nullable(false).defaultValue(org.jooq.impl.DSL.inline("'2009-01-01'", org.jooq.impl.SQLDataType.DATE)), this, "");

    /**
     * The column <code>STATDB.stat_wtalk.stat_hour</code>.
     */
    public final TableField<CommonStatWtalkRecord, Byte> STAT_HOUR = createField(DSL.name("stat_hour"), org.jooq.impl.SQLDataType.TINYINT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.TINYINT)), this, "");

    /**
     * The column <code>STATDB.stat_wtalk.action_type</code>. 액션타입
     */
    public final TableField<CommonStatWtalkRecord, String> ACTION_TYPE = createField(DSL.name("action_type"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "액션타입");

    /**
     * The column <code>STATDB.stat_wtalk.cnt</code>.
     */
    public final TableField<CommonStatWtalkRecord, Integer> CNT = createField(DSL.name("cnt"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    private final String tableName;

    /**
     * Create a <code>STATDB.stat_talk_*</code> table reference
     */
    public CommonStatWtalk(String companyName) {
        this(DSL.name("stat_wtalk_" + companyName), null);
    }

    /**
     * Create an aliased <code>STATDB.stat_talk_*</code> table reference
     */
    public CommonStatWtalk(String alias, Table<CommonStatWtalkRecord> aliased) {
        this(DSL.name(alias), aliased);
    }

    /**
     * Create an aliased <code>STATDB.stat_talk_*_*</code> table reference
     */
    public CommonStatWtalk(Name alias, Table<CommonStatWtalkRecord> aliased) {
        this(alias, aliased, null);
    }

    private CommonStatWtalk(Name alias, Table<CommonStatWtalkRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
        this.tableName = alias.last();
    }

    public <O extends Record> CommonStatWtalk(CommonStatWtalk table, Table<O> child, ForeignKey<O, CommonStatWtalkRecord> key) {
        super(child, key, table);
        this.tableName = table.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<CommonStatWtalkRecord, Integer> getIdentity() {
        return Internal.createIdentity(this, this.SEQ);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<CommonStatWtalkRecord> getPrimaryKey() {
        return Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<CommonStatWtalkRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ));
    }

    @Override
    public CommonStatWtalk as(String alias) {
        return new CommonStatWtalk(DSL.name(alias), this);
    }

    @Override
    public CommonStatWtalk as(Name alias) {
        return new CommonStatWtalk(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonStatWtalk rename(String name) {
        return new CommonStatWtalk(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonStatWtalk rename(Name name) {
        return new CommonStatWtalk(name, null);
    }
}
