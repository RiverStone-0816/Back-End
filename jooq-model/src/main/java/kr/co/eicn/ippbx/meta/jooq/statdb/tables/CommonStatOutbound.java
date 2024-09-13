package kr.co.eicn.ippbx.meta.jooq.statdb.tables;

import kr.co.eicn.ippbx.meta.jooq.statdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.statdb.Statdb;
import kr.co.eicn.ippbx.meta.jooq.statdb.tables.records.CommonStatOutboundRecord;
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

public class CommonStatOutbound extends TableImpl<CommonStatOutboundRecord> {
    /**
     * The column <code>STATDB.stat_outbound.seq</code>. 고유키
     */
    public final TableField<CommonStatOutboundRecord, Integer> SEQ = createField(DSL.name("seq"), SQLDataType.INTEGER.nullable(false).identity(true), this, "고유키");

    /**
     * The column <code>STATDB.stat_outbound.company_id</code>. 회사아이디
     */
    public final TableField<CommonStatOutboundRecord, String> COMPANY_ID = createField(DSL.name("company_id"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "회사아이디");

    /**
     * The column <code>STATDB.stat_outbound.group_code</code>. 조직코드
     */
    public final TableField<CommonStatOutboundRecord, String> GROUP_CODE = createField(DSL.name("group_code"), SQLDataType.CHAR(4).defaultValue(DSL.field("NULL", SQLDataType.CHAR)), this, "조직코드");

    /**
     * The column <code>STATDB.stat_outbound.group_tree_name</code>. 조직트리명
     */
    public final TableField<CommonStatOutboundRecord, String> GROUP_TREE_NAME = createField(DSL.name("group_tree_name"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "조직트리명");

    /**
     * The column <code>STATDB.stat_outbound.group_level</code>. 조직레벨
     */
    public final TableField<CommonStatOutboundRecord, Integer> GROUP_LEVEL = createField(DSL.name("group_level"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "조직레벨");

    /**
     * The column <code>STATDB.stat_outbound.cid_number</code>. 발신번호
     */
    public final TableField<CommonStatOutboundRecord, String> CID_NUMBER = createField(DSL.name("cid_number"), SQLDataType.VARCHAR(30).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "발신번호");

    /**
     * The column <code>STATDB.stat_outbound.from_org</code>. 일반콜-NOR, 콜백콜-CALLBACK,예약콜-RESERVE, PDS콜-PDS, 고객DB-MAINDB
     */
    public final TableField<CommonStatOutboundRecord, String> FROM_ORG = createField(DSL.name("from_org"), SQLDataType.VARCHAR(30).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "일반콜-NOR, 콜백콜-CALLBACK,예약콜-RESERVE, PDS콜-PDS, 고객DB-MAINDB");

    /**
     * The column <code>STATDB.stat_outbound.worktime_yn</code>. 업무시간여부
     */
    public final TableField<CommonStatOutboundRecord, String> WORKTIME_YN = createField(DSL.name("worktime_yn"), SQLDataType.CHAR(1).defaultValue(DSL.field("'Y'", SQLDataType.CHAR)), this, "업무시간여부");

    /**
     * The column <code>STATDB.stat_outbound.dcontext</code>. 다이얼플랜컨텍스트
     */
    public final TableField<CommonStatOutboundRecord, String> DCONTEXT = createField(DSL.name("dcontext"), SQLDataType.VARCHAR(30).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "다이얼플랜컨텍스트");

    /**
     * The column <code>STATDB.stat_outbound.stat_date</code>. 생성일
     */
    public final TableField<CommonStatOutboundRecord, Date> STAT_DATE = createField(DSL.name("stat_date"), SQLDataType.DATE.defaultValue(DSL.field("'2009-01-01'", SQLDataType.DATE)), this, "생성일");

    /**
     * The column <code>STATDB.stat_outbound.stat_hour</code>. 생성시간
     */
    public final TableField<CommonStatOutboundRecord, Byte> STAT_HOUR = createField(DSL.name("stat_hour"), SQLDataType.TINYINT.defaultValue(DSL.field("0", SQLDataType.TINYINT)), this, "생성시간");

    /**
     * The column <code>STATDB.stat_outbound.total</code>. 총계
     */
    public final TableField<CommonStatOutboundRecord, Integer> TOTAL = createField(DSL.name("total"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "총계");

    /**
     * The column <code>STATDB.stat_outbound.success</code>. 응답
     */
    public final TableField<CommonStatOutboundRecord, Integer> SUCCESS = createField(DSL.name("success"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "응답");

    /**
     * The column <code>STATDB.stat_outbound.billsec_sum</code>. 통화시간합계
     */
    public final TableField<CommonStatOutboundRecord, Integer> BILLSEC_SUM = createField(DSL.name("billsec_sum"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "통화시간합계");

    /**
     * The column <code>STATDB.stat_outbound.wait_sum</code>. 통화전대기시간합계
     */
    public final TableField<CommonStatOutboundRecord, Integer> WAIT_SUM = createField(DSL.name("wait_sum"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "통화전대기시간합계");

    private final String tableName;

    /**
     * Create a <code>STATDB.stat_outbound</code> table reference
     */
    public CommonStatOutbound(String companyName) {
        this(DSL.name("stat_outbound_" + companyName), null);
    }

    private CommonStatOutbound(Name alias, Table<CommonStatOutboundRecord> aliased) {
        this(alias, aliased, null);
    }

    private CommonStatOutbound(Name alias, Table<CommonStatOutboundRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment("OUTBOUND 통계 테이블"));
        this.tableName = alias.last();
    }

    public <O extends Record> CommonStatOutbound(CommonStatOutbound table, Table<O> child, ForeignKey<O, CommonStatOutboundRecord> key) {
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
    public Identity<CommonStatOutboundRecord, Integer> getIdentity() {
        return Internal.createIdentity(this, this.SEQ);
    }

    @Override
    public UniqueKey<CommonStatOutboundRecord> getPrimaryKey() {
        return Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ);
    }

    @NotNull
    @Override
    public List<UniqueKey<CommonStatOutboundRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ));
    }

    @NotNull
    @Override
    public CommonStatOutbound as(String alias) {
        return new CommonStatOutbound(DSL.name(alias), this);
    }

    @NotNull
    @Override
    public CommonStatOutbound as(Name alias) {
        return new CommonStatOutbound(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonStatOutbound rename(String name) {
        return new CommonStatOutbound(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonStatOutbound rename(Name name) {
        return new CommonStatOutbound(name, null);
    }
}
