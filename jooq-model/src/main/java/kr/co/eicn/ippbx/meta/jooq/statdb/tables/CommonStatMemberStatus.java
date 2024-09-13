package kr.co.eicn.ippbx.meta.jooq.statdb.tables;

import kr.co.eicn.ippbx.meta.jooq.statdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.statdb.Statdb;
import kr.co.eicn.ippbx.meta.jooq.statdb.tables.records.CommonStatMemberStatusRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import java.sql.Date;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommonStatMemberStatus extends TableImpl<CommonStatMemberStatusRecord> {
    /**
     * The column <code>STATDB.stat_member_status.seq</code>. 번호
     */
    public final TableField<CommonStatMemberStatusRecord, Integer> SEQ = createField(DSL.name("seq"), SQLDataType.INTEGER.nullable(false).identity(true), this, "번호");

    /**
     * The column <code>STATDB.stat_member_status.stat_date</code>. 날짜
     */
    public final TableField<CommonStatMemberStatusRecord, Date> STAT_DATE = createField(DSL.name("stat_date"), SQLDataType.DATE.defaultValue(DSL.field("'2009-01-01'", SQLDataType.DATE)), this, "날짜");

    /**
     * The column <code>STATDB.stat_member_status.stat_hour</code>. 시간
     */
    public final TableField<CommonStatMemberStatusRecord, Byte> STAT_HOUR = createField(DSL.name("stat_hour"), SQLDataType.TINYINT.defaultValue(DSL.field("0", SQLDataType.TINYINT)), this, "시간");

    /**
     * The column <code>STATDB.stat_member_status.status</code>. 상태
     */
    public final TableField<CommonStatMemberStatusRecord, String> STATUS = createField(DSL.name("status"), SQLDataType.CHAR(5).defaultValue(DSL.field("''", SQLDataType.CHAR)), this, "상태");

    /**
     * The column <code>STATDB.stat_member_status.in_out</code>. status가 1일때만 의미가 있음
     */
    public final TableField<CommonStatMemberStatusRecord, String> IN_OUT = createField(DSL.name("in_out"), SQLDataType.CHAR(1).defaultValue(DSL.field("'I'", SQLDataType.CHAR)), this, "status가 1일때만 의미가 있음");

    /**
     * The column <code>STATDB.stat_member_status.total</code>. 횟수
     */
    public final TableField<CommonStatMemberStatusRecord, Integer> TOTAL = createField(DSL.name("total"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "횟수");

    /**
     * The column <code>STATDB.stat_member_status.diff_sum</code>. 시간
     */
    public final TableField<CommonStatMemberStatusRecord, Integer> DIFF_SUM = createField(DSL.name("diff_sum"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "시간");

    /**
     * The column <code>STATDB.stat_member_status.userid</code>. 상담원아이디
     */
    public final TableField<CommonStatMemberStatusRecord, String> USERID = createField(DSL.name("userid"), SQLDataType.VARCHAR(30).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "상담원아이디");

    /**
     * The column <code>STATDB.stat_member_status.company_id</code>. 회사아이디
     */
    public final TableField<CommonStatMemberStatusRecord, String> COMPANY_ID = createField(DSL.name("company_id"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "회사아이디");

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

    @Override
    public Schema getSchema() {
        return Statdb.STATDB;
    }

    @NotNull
    @Override
    public List<Index> getIndexes() {
        return Arrays.asList();
    }

    @Override
    public Identity<CommonStatMemberStatusRecord, Integer> getIdentity() {
        return Internal.createIdentity(this, this.SEQ);
    }

    @Override
    public UniqueKey<CommonStatMemberStatusRecord> getPrimaryKey() {
        return Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ);
    }

    @NotNull
    @Override
    public List<UniqueKey<CommonStatMemberStatusRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ));
    }

    @NotNull
    @Override
    public CommonStatMemberStatus as(String alias) {
        return new CommonStatMemberStatus(DSL.name(alias), this);
    }

    @NotNull
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
