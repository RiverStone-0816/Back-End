package kr.co.eicn.ippbx.meta.jooq.customdb.tables;

import kr.co.eicn.ippbx.meta.jooq.customdb.Customdb;
import kr.co.eicn.ippbx.meta.jooq.customdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.CommonMemberStatusRecord;
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

public class CommonMemberStatus extends TableImpl<CommonMemberStatusRecord> {
    /**
     * The column <code>CUSTOMDB.member_status.seq</code>. 고유키
     */
    public final TableField<CommonMemberStatusRecord, Integer> SEQ = createField(DSL.name("seq"), SQLDataType.INTEGER.nullable(false).identity(true), this, "고유키");

    /**
     * The column <code>CUSTOMDB.member_status.start_date</code>. 시작일
     */
    public final TableField<CommonMemberStatusRecord, Timestamp> START_DATE = createField(DSL.name("start_date"), SQLDataType.TIMESTAMP(0).defaultValue(DSL.field("'2009-07-01 00:00:00'", SQLDataType.TIMESTAMP)), this, "시작일");

    /**
     * The column <code>CUSTOMDB.member_status.end_date</code>. 종료일
     */
    public final TableField<CommonMemberStatusRecord, Timestamp> END_DATE = createField(DSL.name("end_date"), SQLDataType.TIMESTAMP(0).defaultValue(DSL.field("'2009-07-01 00:00:00'", SQLDataType.TIMESTAMP)), this, "종료일");

    /**
     * The column <code>CUSTOMDB.member_status.phoneid</code>. 전화아이디
     */
    public final TableField<CommonMemberStatusRecord, String> PHONEID = createField(DSL.name("phoneid"), SQLDataType.VARCHAR(30).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "전화아이디");

    /**
     * The column <code>CUSTOMDB.member_status.phonename</code>. 전화이름
     */
    public final TableField<CommonMemberStatusRecord, String> PHONENAME = createField(DSL.name("phonename"), SQLDataType.VARCHAR(30).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "전화이름");

    /**
     * The column <code>CUSTOMDB.member_status.status</code>. 상태
     */
    public final TableField<CommonMemberStatusRecord, String> STATUS = createField(DSL.name("status"), SQLDataType.CHAR(5).defaultValue(DSL.field("''", SQLDataType.CHAR)), this, "상태");

    /**
     * The column <code>CUSTOMDB.member_status.next_status</code>. 다음상태
     */
    public final TableField<CommonMemberStatusRecord, String> NEXT_STATUS = createField(DSL.name("next_status"), SQLDataType.CHAR(5).defaultValue(DSL.field("''", SQLDataType.CHAR)), this, "다음상태");

    /**
     * The column <code>CUSTOMDB.member_status.in_out</code>. status가 1일때만 의미가 있음
     */
    public final TableField<CommonMemberStatusRecord, String> IN_OUT = createField(DSL.name("in_out"), SQLDataType.CHAR(1).defaultValue(DSL.field("'I'", SQLDataType.CHAR)), this, "status가 1일때만 의미가 있음");

    /**
     * The column <code>CUSTOMDB.member_status.company_id</code>. 회사아이디
     */
    public final TableField<CommonMemberStatusRecord, String> COMPANY_ID = createField(DSL.name("company_id"), SQLDataType.VARCHAR(30).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "회사아이디");

    private final String tableName;

    /**
     * Create a <code>CUSTOMDB.member_status</code> table reference
     */
    public CommonMemberStatus() {
        this(DSL.name("member_status"), null);
    }

    public CommonMemberStatus(String companyName) {
        this(DSL.name("member_status_" + companyName), null);
    }

    /**
     * Create an aliased <code>CUSTOMDB.member_status</code> table reference
     */
    private CommonMemberStatus(String alias, Table<CommonMemberStatusRecord> aliased) {
        this(DSL.name(alias), aliased);
    }

    private CommonMemberStatus(Name alias, Table<CommonMemberStatusRecord> aliased) {
        this(alias, aliased, null);
    }

    private CommonMemberStatus(Name alias, Table<CommonMemberStatusRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
        this.tableName = alias.last();
    }

    public <O extends Record> CommonMemberStatus(CommonMemberStatus table, Table<O> child, ForeignKey<O, CommonMemberStatusRecord> key) {
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
        return Arrays.asList();
    }

    @Override
    public Identity<CommonMemberStatusRecord, Integer> getIdentity() {
        return Internal.createIdentity(this, this.SEQ);
    }

    @Override
    public UniqueKey<CommonMemberStatusRecord> getPrimaryKey() {
        return Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ);
    }

    @NotNull
    @Override
    public List<UniqueKey<CommonMemberStatusRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ));
    }

    @NotNull
    @Override
    public CommonMemberStatus as(String alias) {
        return new CommonMemberStatus(DSL.name(alias), this);
    }

    @NotNull
    @Override
    public CommonMemberStatus as(Name alias) {
        return new CommonMemberStatus(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonMemberStatus rename(String name) {
        return new CommonMemberStatus(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonMemberStatus rename(Name name) {
        return new CommonMemberStatus(name, null);
    }
}
