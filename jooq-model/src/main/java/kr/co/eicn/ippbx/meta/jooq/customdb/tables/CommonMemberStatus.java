package kr.co.eicn.ippbx.meta.jooq.customdb.tables;

import kr.co.eicn.ippbx.meta.jooq.customdb.Customdb;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.CommonMemberStatusRecord;
import kr.co.eicn.ippbx.meta.jooq.eicn.Indexes;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.jooq.impl.TableImpl;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommonMemberStatus extends TableImpl<CommonMemberStatusRecord> {

    public static final CommonMemberStatus MEMBER_STATUS = new CommonMemberStatus();

    /**
     * The column <code>CUSTOMDB.member_status.seq</code>. 고유키
     */
    public final TableField<CommonMemberStatusRecord, Integer> SEQ = createField(DSL.name("seq"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "고유키");

    /**
     * The column <code>CUSTOMDB.member_status.start_date</code>. 시작일
     */
    public final TableField<CommonMemberStatusRecord, Timestamp> START_DATE = createField(DSL.name("start_date"), org.jooq.impl.SQLDataType.TIMESTAMP.defaultValue(org.jooq.impl.DSL.field("'2009-07-01 00:00:00'", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "시작일");

    /**
     * The column <code>CUSTOMDB.member_status.end_date</code>. 종료일
     */
    public final TableField<CommonMemberStatusRecord, Timestamp> END_DATE = createField(DSL.name("end_date"), org.jooq.impl.SQLDataType.TIMESTAMP.defaultValue(org.jooq.impl.DSL.field("'2009-07-01 00:00:00'", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "종료일");

    /**
     * The column <code>CUSTOMDB.member_status.phoneid</code>. 전화아이디
     */
    public final TableField<CommonMemberStatusRecord, String> PHONEID = createField(DSL.name("phoneid"), org.jooq.impl.SQLDataType.VARCHAR(30).defaultValue(org.jooq.impl.DSL.field("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "전화아이디");

    /**
     * The column <code>CUSTOMDB.member_status.phonename</code>. 전화이름
     */
    public final TableField<CommonMemberStatusRecord, String> PHONENAME = createField(DSL.name("phonename"), org.jooq.impl.SQLDataType.VARCHAR(30).defaultValue(org.jooq.impl.DSL.field("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "전화이름");

    /**
     * The column <code>CUSTOMDB.member_status.status</code>. 상태
     */
    public final TableField<CommonMemberStatusRecord, String> STATUS = createField(DSL.name("status"), org.jooq.impl.SQLDataType.CHAR(5).defaultValue(org.jooq.impl.DSL.field("''", org.jooq.impl.SQLDataType.CHAR)), this, "상태");

    /**
     * The column <code>CUSTOMDB.member_status.next_status</code>. 다음상태
     */
    public final TableField<CommonMemberStatusRecord, String> NEXT_STATUS = createField(DSL.name("next_status"), org.jooq.impl.SQLDataType.CHAR(5).defaultValue(org.jooq.impl.DSL.field("''", org.jooq.impl.SQLDataType.CHAR)), this, "다음상태");

    /**
     * The column <code>CUSTOMDB.member_status.in_out</code>. status가 1일때만 의미가 있음
     */
    public final TableField<CommonMemberStatusRecord, String> IN_OUT = createField(DSL.name("in_out"), org.jooq.impl.SQLDataType.CHAR(1).defaultValue(org.jooq.impl.DSL.field("'I'", org.jooq.impl.SQLDataType.CHAR)), this, "status가 1일때만 의미가 있음");

    /**
     * The column <code>CUSTOMDB.member_status.company_id</code>. 회사아이디
     */
    public final TableField<CommonMemberStatusRecord, String> COMPANY_ID = createField(DSL.name("company_id"), org.jooq.impl.SQLDataType.VARCHAR(30).defaultValue(org.jooq.impl.DSL.field("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "회사아이디");
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
     * Create an aliased <code>CUSTOMDB.maindb_multichannel_info_*</code> table reference
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
        super(child, key, MEMBER_STATUS);
        this.tableName = table.getName();
    }

    @Override
    public Schema getSchema() {
        return Customdb.CUSTOMDB;
    }

    @Override
    public Identity<CommonMemberStatusRecord, Integer> getIdentity() {
        return Internal.createIdentity(this, this.SEQ);
    }

    @Override
    public UniqueKey<CommonMemberStatusRecord> getPrimaryKey() {
        return Internal.createUniqueKey(this, "KEY_" + getName() + "_PRIMARY", this.SEQ);
    }

    @Override
    public List<UniqueKey<CommonMemberStatusRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, "KEY_" + getName() + "_PRIMARY", this.SEQ));
    }

    @Override
    public CommonMemberStatus as(String alias) {
        return new CommonMemberStatus(DSL.name(alias), this);
    }

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
