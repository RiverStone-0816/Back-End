package kr.co.eicn.ippbx.meta.jooq.statdb.tables;

import kr.co.eicn.ippbx.meta.jooq.statdb.tables.records.CommonStatOutboundRecord;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.jooq.impl.TableImpl;

import java.sql.Date;
import java.util.Collections;
import java.util.List;

public class CommonStatOutbound extends TableImpl<CommonStatOutboundRecord> {
    /**
     * The column <code>STATDB.stat_outbound.seq</code>. 고유키
     */
    public final TableField<CommonStatOutboundRecord, Integer> SEQ = createField(DSL.name("seq"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "고유키");

    /**
     * The column <code>STATDB.stat_outbound.company_id</code>. 회사아이디
     */
    public final TableField<CommonStatOutboundRecord, String> COMPANY_ID = createField(DSL.name("company_id"), org.jooq.impl.SQLDataType.VARCHAR(100).nullable(false).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "회사아이디");

    /**
     * The column <code>STATDB.stat_outbound.group_code</code>.  ex&gt;0001
     */
    public final TableField<CommonStatOutboundRecord, String> GROUP_CODE = createField(DSL.name("group_code"), org.jooq.impl.SQLDataType.CHAR(4).nullable(false), this, " ex>0001");

    /**
     * The column <code>STATDB.stat_outbound.group_tree_name</code>. ex&gt;0003_0008_0001
     */
    public final TableField<CommonStatOutboundRecord, String> GROUP_TREE_NAME = createField(DSL.name("group_tree_name"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "ex>0003_0008_0001");

    /**
     * The column <code>STATDB.stat_outbound.group_level</code>. 3
     */
    public final TableField<CommonStatOutboundRecord, Integer> GROUP_LEVEL = createField(DSL.name("group_level"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "3");

    /**
     * The column <code>STATDB.stat_outbound.from_org</code>. 일반콜-NOR, 콜백콜-CALLBACK,예약콜-RESERVE, PDS콜-PDS, 고객DB-MAINDB
     */
    public final TableField<CommonStatOutboundRecord, String> FROM_ORG = createField(DSL.name("from_org"), org.jooq.impl.SQLDataType.VARCHAR(30).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "일반콜-NOR, 콜백콜-CALLBACK,예약콜-RESERVE, PDS콜-PDS, 고객DB-MAINDB");

    /**
     * The column <code>STATDB.stat_outbound.stat_date</code>. 생성일
     */
    public final TableField<CommonStatOutboundRecord, Date> STAT_DATE = createField(DSL.name("stat_date"), org.jooq.impl.SQLDataType.DATE.nullable(false).defaultValue(org.jooq.impl.DSL.inline("2009-01-01", org.jooq.impl.SQLDataType.DATE)), this, "생성일");

    /**
     * The column <code>STATDB.stat_outbound.stat_hour</code>. 생성시간
     */
    public final TableField<CommonStatOutboundRecord, Byte> STAT_HOUR = createField(DSL.name("stat_hour"), org.jooq.impl.SQLDataType.TINYINT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.TINYINT)), this, "생성시간");

    /**
     * The column <code>STATDB.stat_outbound.total</code>. 총계
     */
    public final TableField<CommonStatOutboundRecord, Integer> TOTAL = createField(DSL.name("total"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "총계");

    /**
     * The column <code>STATDB.stat_outbound.success</code>. 응답
     */
    public final TableField<CommonStatOutboundRecord, Integer> SUCCESS = createField(DSL.name("success"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "응답");

    /**
     * The column <code>STATDB.stat_outbound.billsec_sum</code>. 통화시간
     */
    public final TableField<CommonStatOutboundRecord, Integer> BILLSEC_SUM = createField(DSL.name("billsec_sum"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "통화시간");

    /**
     * The column <code>STATDB.stat_outbound.wait_sum</code>. 통화전대기시간
     */
    public final TableField<CommonStatOutboundRecord, Integer> WAIT_SUM = createField(DSL.name("wait_sum"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "통화전대기시간");

    /**
     * The column <code>STATDB.stat_outbound.dcontext</code>. 다이얼플랜컨텍스트
     */
    public final TableField<CommonStatOutboundRecord, String> DCONTEXT = createField(DSL.name("dcontext"), org.jooq.impl.SQLDataType.VARCHAR(30).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "다이얼플랜컨텍스트");

    /**
     * The column <code>STATDB.stat_outbound.worktime_yn</code>.
     */
    public final TableField<CommonStatOutboundRecord, String> WORKTIME_YN = createField(DSL.name("worktime_yn"), org.jooq.impl.SQLDataType.CHAR(1).defaultValue(org.jooq.impl.DSL.inline("Y", org.jooq.impl.SQLDataType.CHAR)), this, "");

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

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<CommonStatOutboundRecord, Integer> getIdentity() {
        return org.jooq.impl.Internal.createIdentity(this, this.SEQ);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<CommonStatOutboundRecord> getPrimaryKey() {
        return org.jooq.impl.Internal.createUniqueKey(this, "KEY_" + getName() + "_PRIMARY", this.SEQ);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<CommonStatOutboundRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, "KEY_" + getName() + "_PRIMARY", this.SEQ));
    }

    @Override
    public CommonStatOutbound as(String alias) {
        return new CommonStatOutbound(DSL.name(alias), this);
    }

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
