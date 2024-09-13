package kr.co.eicn.ippbx.meta.jooq.statdb.tables;

import kr.co.eicn.ippbx.meta.jooq.statdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.statdb.Statdb;
import kr.co.eicn.ippbx.meta.jooq.statdb.tables.records.CommonStatUserOutboundRecord;
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

public class CommonStatUserOutbound extends TableImpl<CommonStatUserOutboundRecord> {
    /**
     * The column <code>STATDB.stat_user_outbound.seq</code>. 고유키
     */
    public final TableField<CommonStatUserOutboundRecord, Integer> SEQ = createField(DSL.name("seq"), SQLDataType.INTEGER.nullable(false).identity(true), this, "고유키");

    /**
     * The column <code>STATDB.stat_user_outbound.company_id</code>. 회사아이디
     */
    public final TableField<CommonStatUserOutboundRecord, String> COMPANY_ID = createField(DSL.name("company_id"), SQLDataType.VARCHAR(100).nullable(false).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "회사아이디");

    /**
     * The column <code>STATDB.stat_user_outbound.group_code</code>. 조직코드
     */
    public final TableField<CommonStatUserOutboundRecord, String> GROUP_CODE = createField(DSL.name("group_code"), SQLDataType.CHAR(4).defaultValue(DSL.field("NULL", SQLDataType.CHAR)), this, "조직코드");

    /**
     * The column <code>STATDB.stat_user_outbound.group_tree_name</code>. 조직트리명
     */
    public final TableField<CommonStatUserOutboundRecord, String> GROUP_TREE_NAME = createField(DSL.name("group_tree_name"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "조직트리명");

    /**
     * The column <code>STATDB.stat_user_outbound.group_level</code>. 조직트리레벨
     */
    public final TableField<CommonStatUserOutboundRecord, Integer> GROUP_LEVEL = createField(DSL.name("group_level"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "조직트리레벨");

    /**
     * The column <code>STATDB.stat_user_outbound.cid_number</code>. 발신번호
     */
    public final TableField<CommonStatUserOutboundRecord, String> CID_NUMBER = createField(DSL.name("cid_number"), SQLDataType.VARCHAR(30).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "발신번호");

    /**
     * The column <code>STATDB.stat_user_outbound.userid</code>.
     */
    public final TableField<CommonStatUserOutboundRecord, String> USERID = createField(DSL.name("userid"), SQLDataType.VARCHAR(30).nullable(false).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>STATDB.stat_user_outbound.user_stat_yn</code>.
     */
    public final TableField<CommonStatUserOutboundRecord, String> USER_STAT_YN = createField(DSL.name("user_stat_yn"), SQLDataType.CHAR(1).defaultValue(DSL.field("'Y'", SQLDataType.CHAR)), this, "");

    /**
     * The column <code>STATDB.stat_user_outbound.from_org</code>. 일반콜-NOR, 콜백콜-CALLBACK,예약콜-RESERVE, PDS콜-PDS, 고객DB-MAINDB
     */
    public final TableField<CommonStatUserOutboundRecord, String> FROM_ORG = createField(DSL.name("from_org"), SQLDataType.VARCHAR(30).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "일반콜-NOR, 콜백콜-CALLBACK,예약콜-RESERVE, PDS콜-PDS, 고객DB-MAINDB");

    /**
     * The column <code>STATDB.stat_user_outbound.worktime_yn</code>. 업무시간여부
     */
    public final TableField<CommonStatUserOutboundRecord, String> WORKTIME_YN = createField(DSL.name("worktime_yn"), SQLDataType.CHAR(1).defaultValue(DSL.field("'Y'", SQLDataType.CHAR)), this, "업무시간여부");

    /**
     * The column <code>STATDB.stat_user_outbound.dcontext</code>. 다이얼플랜컨텍스트
     */
    public final TableField<CommonStatUserOutboundRecord, String> DCONTEXT = createField(DSL.name("dcontext"), SQLDataType.VARCHAR(30).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "다이얼플랜컨텍스트");

    /**
     * The column <code>STATDB.stat_user_outbound.stat_date</code>.
     */
    public final TableField<CommonStatUserOutboundRecord, Date> STAT_DATE = createField(DSL.name("stat_date"), SQLDataType.DATE.defaultValue(DSL.field("'2009-01-01'", SQLDataType.DATE)), this, "");

    /**
     * The column <code>STATDB.stat_user_outbound.stat_hour</code>.
     */
    public final TableField<CommonStatUserOutboundRecord, Byte> STAT_HOUR = createField(DSL.name("stat_hour"), SQLDataType.TINYINT.defaultValue(DSL.field("0", SQLDataType.TINYINT)), this, "");

    /**
     * The column <code>STATDB.stat_user_outbound.out_total</code>.
     */
    public final TableField<CommonStatUserOutboundRecord, Integer> OUT_TOTAL = createField(DSL.name("out_total"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>STATDB.stat_user_outbound.out_success</code>.
     */
    public final TableField<CommonStatUserOutboundRecord, Integer> OUT_SUCCESS = createField(DSL.name("out_success"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>STATDB.stat_user_outbound.out_billsec_sum</code>.
     */
    public final TableField<CommonStatUserOutboundRecord, Integer> OUT_BILLSEC_SUM = createField(DSL.name("out_billsec_sum"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>STATDB.stat_user_outbound.callback_call_cnt</code>.
     */
    public final TableField<CommonStatUserOutboundRecord, Integer> CALLBACK_CALL_CNT = createField(DSL.name("callback_call_cnt"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>STATDB.stat_user_outbound.callback_call_succ</code>.
     */
    public final TableField<CommonStatUserOutboundRecord, Integer> CALLBACK_CALL_SUCC = createField(DSL.name("callback_call_succ"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>STATDB.stat_user_outbound.reserve_call_cnt</code>.
     */
    public final TableField<CommonStatUserOutboundRecord, Integer> RESERVE_CALL_CNT = createField(DSL.name("reserve_call_cnt"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>STATDB.stat_user_outbound.reserve_call_succ</code>.
     */
    public final TableField<CommonStatUserOutboundRecord, Integer> RESERVE_CALL_SUCC = createField(DSL.name("reserve_call_succ"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

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
    public Identity<CommonStatUserOutboundRecord, Integer> getIdentity() {
        return Internal.createIdentity(this, this.SEQ);
    }

    @Override
    public UniqueKey<CommonStatUserOutboundRecord> getPrimaryKey() {
        return Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ);
    }

    @NotNull
    @Override
    public List<UniqueKey<CommonStatUserOutboundRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ));
    }

    @NotNull
    @Override
    public CommonStatUserOutbound as(String alias) {
        return new CommonStatUserOutbound(DSL.name(alias), this);
    }

    @NotNull
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
