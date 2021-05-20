package kr.co.eicn.ippbx.meta.jooq.statdb.tables;

import kr.co.eicn.ippbx.meta.jooq.statdb.tables.records.CommonStatMemberStatusRecord;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.jooq.impl.TableImpl;

import java.sql.Date;
import java.util.Collections;
import java.util.List;

public class CommonStatMemberStatus extends TableImpl<CommonStatMemberStatusRecord> {
    /**
     * The column <code>STATDB.stat_member_status.seq</code>. 번호
     */
    public final TableField<CommonStatMemberStatusRecord, Integer> SEQ = createField(DSL.name("seq"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "번호");

    /**
     * The column <code>STATDB.stat_member_status.stat_date</code>. 날짜
     */
    public final TableField<CommonStatMemberStatusRecord, Date> STAT_DATE = createField(DSL.name("stat_date"), org.jooq.impl.SQLDataType.DATE.nullable(false).defaultValue(org.jooq.impl.DSL.inline("2009-01-01", org.jooq.impl.SQLDataType.DATE)), this, "날짜");

    /**
     * The column <code>STATDB.stat_member_status.stat_hour</code>. 시간
     */
    public final TableField<CommonStatMemberStatusRecord, Byte> STAT_HOUR = createField(DSL.name("stat_hour"), org.jooq.impl.SQLDataType.TINYINT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.TINYINT)), this, "시간");

    /**
     * The column <code>STATDB.stat_member_status.status</code>. 상태
     */
    public final TableField<CommonStatMemberStatusRecord, String> STATUS = createField(DSL.name("status"), org.jooq.impl.SQLDataType.CHAR(1).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.CHAR)), this, "상태");

    /**
     * The column <code>STATDB.stat_member_status.total</code>. 횟수
     */
    public final TableField<CommonStatMemberStatusRecord, Integer> TOTAL = createField(DSL.name("total"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "횟수");

    /**
     * The column <code>STATDB.stat_member_status.diff_sum</code>. 시간
     */
    public final TableField<CommonStatMemberStatusRecord, Integer> DIFF_SUM = createField(DSL.name("diff_sum"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "시간");

    /**
     * The column <code>STATDB.stat_member_status.userid</code>. 상담원아이디
     */
    public final TableField<CommonStatMemberStatusRecord, String> USERID = createField(DSL.name("userid"), org.jooq.impl.SQLDataType.VARCHAR(30).nullable(false).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "상담원아이디");

    /**
     * The column <code>STATDB.stat_member_status.company_id</code>. 회사아이디
     */
    public final TableField<CommonStatMemberStatusRecord, String> COMPANY_ID = createField(DSL.name("company_id"), org.jooq.impl.SQLDataType.VARCHAR(100).nullable(false).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "회사아이디");

    private final String tableName;

    /**
     * Create a <code>STATDB.stat_member_status</code> table reference
     */
    public CommonStatMemberStatus(String companyName) {
        this(DSL.name("stat_member_status_" + companyName), null);
    }

    private CommonStatMemberStatus(Name alias, Table<CommonStatMemberStatusRecord> aliased) {
        this(alias, aliased, null);
    }

    private CommonStatMemberStatus(Name alias, Table<CommonStatMemberStatusRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment("상담원의 아웃바운드 통계 테이블"));
        this.tableName = alias.last();
    }

    public <O extends Record> CommonStatMemberStatus(CommonStatMemberStatus table, Table<O> child, ForeignKey<O, CommonStatMemberStatusRecord> key) {
        super(child, key, table);
        this.tableName = table.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<CommonStatMemberStatusRecord, Integer> getIdentity() {
        return org.jooq.impl.Internal.createIdentity(this, this.SEQ);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<CommonStatMemberStatusRecord> getPrimaryKey() {
        return org.jooq.impl.Internal.createUniqueKey(this, "KEY_" + getName() + "_PRIMARY", this.SEQ);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<CommonStatMemberStatusRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, "KEY_" + getName() + "_PRIMARY", this.SEQ));
    }

    @Override
    public CommonStatMemberStatus as(String alias) {
        return new CommonStatMemberStatus(DSL.name(alias), this);
    }

    @Override
    public CommonStatMemberStatus as(Name alias) {
        return new CommonStatMemberStatus(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonStatMemberStatus rename(String name) {
        return new CommonStatMemberStatus(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonStatMemberStatus rename(Name name) {
        return new CommonStatMemberStatus(name, null);
    }
}
