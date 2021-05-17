package kr.co.eicn.ippbx.server.jooq.statdb.tables;

import kr.co.eicn.ippbx.server.jooq.statdb.tables.records.CommonStatUserOutboundRecord;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.jooq.impl.TableImpl;

import java.sql.Date;
import java.util.Collections;
import java.util.List;

public class CommonStatUserOutbound extends TableImpl<CommonStatUserOutboundRecord> {
    /**
     * The column <code>STATDB.stat_user_outbound.seq</code>. 번호
     */
    public final TableField<CommonStatUserOutboundRecord, Integer> SEQ = createField(DSL.name("seq"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "번호");

    /**
     * The column <code>STATDB.stat_user_outbound.company_id</code>. 회사아이디
     */
    public final TableField<CommonStatUserOutboundRecord, String> COMPANY_ID = createField(DSL.name("company_id"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "회사아이디");

    /**
     * The column <code>STATDB.stat_user_outbound.group_code</code>. 조직코드
     */
    public final TableField<CommonStatUserOutboundRecord, String> GROUP_CODE = createField(DSL.name("group_code"), org.jooq.impl.SQLDataType.CHAR(4), this, "조직코드");

    /**
     * The column <code>STATDB.stat_user_outbound.group_tree_name</code>. 조직트리명
     */
    public final TableField<CommonStatUserOutboundRecord, String> GROUP_TREE_NAME = createField(DSL.name("group_tree_name"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "조직트리명");

    /**
     * The column <code>STATDB.stat_user_outbound.group_level</code>. 조직레벨
     */
    public final TableField<CommonStatUserOutboundRecord, Integer> GROUP_LEVEL = createField(DSL.name("group_level"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "조직레벨");

    /**
     * The column <code>STATDB.stat_user_outbound.userid</code>. 상담원아이디
     */
    public final TableField<CommonStatUserOutboundRecord, String> USERID = createField(DSL.name("userid"), org.jooq.impl.SQLDataType.VARCHAR(30).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "상담원아이디");

    /**
     * The column <code>STATDB.stat_user_outbound.from_org</code>. 일반콜-NOR, 콜백콜-CALLBACK,예약콜-RESERVE, PDS콜-PDS, 고객DB-MAINDB
     */
    public final TableField<CommonStatUserOutboundRecord, String> FROM_ORG = createField(DSL.name("from_org"), org.jooq.impl.SQLDataType.VARCHAR(30).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "일반콜-NOR, 콜백콜-CALLBACK,예약콜-RESERVE, PDS콜-PDS, 고객DB-MAINDB");

    /**
     * The column <code>STATDB.stat_user_outbound.worktime_yn</code>.
     */
    public final TableField<CommonStatUserOutboundRecord, String> WORKTIME_YN = createField(DSL.name("worktime_yn"), org.jooq.impl.SQLDataType.CHAR(1).defaultValue(org.jooq.impl.DSL.inline("Y", org.jooq.impl.SQLDataType.CHAR)), this, "");

    /**
     * The column <code>STATDB.stat_user_outbound.dcontext</code>. 다이얼플랜컨택스트
     */
    public final TableField<CommonStatUserOutboundRecord, String> DCONTEXT = createField(DSL.name("dcontext"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "다이얼플랜컨택스트");

    /**
     * The column <code>STATDB.stat_user_outbound.stat_date</code>. 날짜
     */
    public final TableField<CommonStatUserOutboundRecord, Date> STAT_DATE = createField(DSL.name("stat_date"), org.jooq.impl.SQLDataType.DATE.defaultValue(org.jooq.impl.DSL.inline("2009-01-01", org.jooq.impl.SQLDataType.DATE)), this, "날짜");

    /**
     * The column <code>STATDB.stat_user_outbound.stat_hour</code>. 시간
     */
    public final TableField<CommonStatUserOutboundRecord, Byte> STAT_HOUR = createField(DSL.name("stat_hour"), org.jooq.impl.SQLDataType.TINYINT.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.TINYINT)), this, "시간");

    /**
     * The column <code>STATDB.stat_user_outbound.out_total</code>. 총계
     */
    public final TableField<CommonStatUserOutboundRecord, Integer> OUT_TOTAL = createField(DSL.name("out_total"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "총계");

    /**
     * The column <code>STATDB.stat_user_outbound.out_success</code>. 응답
     */
    public final TableField<CommonStatUserOutboundRecord, Integer> OUT_SUCCESS = createField(DSL.name("out_success"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "응답");

    /**
     * The column <code>STATDB.stat_user_outbound.out_billsec_sum</code>. 통화시간
     */
    public final TableField<CommonStatUserOutboundRecord, Integer> OUT_BILLSEC_SUM = createField(DSL.name("out_billsec_sum"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "통화시간");

    /**
     * The column <code>STATDB.stat_user_outbound.callback_call_cnt</code>. 콜백처리를위한 콜건수
     */
    public final TableField<CommonStatUserOutboundRecord, Integer> CALLBACK_CALL_CNT = createField(DSL.name("callback_call_cnt"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "콜백처리를위한 콜건수");

    /**
     * The column <code>STATDB.stat_user_outbound.callback_call_succ</code>. 콜백처리를위한 콜이후 완료건수
     */
    public final TableField<CommonStatUserOutboundRecord, Integer> CALLBACK_CALL_SUCC = createField(DSL.name("callback_call_succ"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "콜백처리를위한 콜이후 완료건수");

    /**
     * The column <code>STATDB.stat_user_outbound.reserve_call_cnt</code>. 예약콜처리를 위한 콜건수
     */
    public final TableField<CommonStatUserOutboundRecord, Integer> RESERVE_CALL_CNT = createField(DSL.name("reserve_call_cnt"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "예약콜처리를 위한 콜건수");

    /**
     * The column <code>STATDB.stat_user_outbound.reserve_call_succ</code>. 예약콜처리를 콜이후 완료건수
     */
    public final TableField<CommonStatUserOutboundRecord, Integer> RESERVE_CALL_SUCC = createField(DSL.name("reserve_call_succ"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "예약콜처리를 콜이후 완료건수");

    private final String tableName;

    /**
     * Create a <code>STATDB.stat_user_outbound</code> table reference
     */
    public CommonStatUserOutbound(String companyName) {
        this(DSL.name("stat_user_outbound_" + companyName), null);
    }

    private CommonStatUserOutbound(Name alias, Table<CommonStatUserOutboundRecord> aliased) {
        this(alias, aliased, null);
    }

    private CommonStatUserOutbound(Name alias, Table<CommonStatUserOutboundRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment("상담원의 아웃바운드 통계 테이블"));
        this.tableName = alias.last();
    }

    public <O extends Record> CommonStatUserOutbound(CommonStatUserOutbound table, Table<O> child, ForeignKey<O, CommonStatUserOutboundRecord> key) {
        super(child, key, table);
        this.tableName = table.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<CommonStatUserOutboundRecord, Integer> getIdentity() {
        return org.jooq.impl.Internal.createIdentity(this, this.SEQ);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<CommonStatUserOutboundRecord> getPrimaryKey() {
        return org.jooq.impl.Internal.createUniqueKey(this, "KEY_" + getName() + "_PRIMARY", this.SEQ);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<CommonStatUserOutboundRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, "KEY_" + getName() + "_PRIMARY", this.SEQ));
    }

    @Override
    public CommonStatUserOutbound as(String alias) {
        return new CommonStatUserOutbound(DSL.name(alias), this);
    }

    @Override
    public CommonStatUserOutbound as(Name alias) {
        return new CommonStatUserOutbound(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonStatUserOutbound rename(String name) {
        return new CommonStatUserOutbound(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonStatUserOutbound rename(Name name) {
        return new CommonStatUserOutbound(name, null);
    }
}
