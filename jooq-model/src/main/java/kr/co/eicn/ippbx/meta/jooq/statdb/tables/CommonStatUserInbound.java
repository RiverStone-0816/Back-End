package kr.co.eicn.ippbx.meta.jooq.statdb.tables;

import kr.co.eicn.ippbx.meta.jooq.statdb.tables.records.CommonStatUserInboundRecord;
import kr.co.eicn.ippbx.meta.jooq.statdb.tables.records.StatUserInboundRecord;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.jooq.impl.TableImpl;

import java.sql.Date;
import java.util.Collections;
import java.util.List;

public class CommonStatUserInbound extends TableImpl<CommonStatUserInboundRecord> {
    /**
     * The column <code>STATDB.stat_user_inbound.seq</code>. 번호
     */
    public final TableField<CommonStatUserInboundRecord, Integer> SEQ = createField(DSL.name("seq"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "번호");

    /**
     * The column <code>STATDB.stat_user_inbound.company_id</code>. 회사아이디
     */
    public final TableField<CommonStatUserInboundRecord, String> COMPANY_ID = createField(DSL.name("company_id"), org.jooq.impl.SQLDataType.VARCHAR(100).nullable(false).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "회사아이디");

    /**
     * The column <code>STATDB.stat_user_inbound.group_code</code>.  ex&gt;0001
     */
    public final TableField<CommonStatUserInboundRecord, String> GROUP_CODE = createField(DSL.name("group_code"), org.jooq.impl.SQLDataType.CHAR(4).nullable(false), this, " ex>0001");

    /**
     * The column <code>STATDB.stat_user_inbound.group_tree_name</code>. ex&gt;0003_0008_0001
     */
    public final TableField<CommonStatUserInboundRecord, String> GROUP_TREE_NAME = createField(DSL.name("group_tree_name"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "ex>0003_0008_0001");

    /**
     * The column <code>STATDB.stat_user_inbound.group_level</code>. 3
     */
    public final TableField<CommonStatUserInboundRecord, Integer> GROUP_LEVEL = createField(DSL.name("group_level"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "3");

    /**
     * The column <code>STATDB.stat_user_inbound.userid</code>. 상담원아이디
     */
    public final TableField<CommonStatUserInboundRecord, String> USERID = createField(DSL.name("userid"), org.jooq.impl.SQLDataType.VARCHAR(30).nullable(false).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "상담원아이디");

    /**
     * The column <code>STATDB.stat_user_inbound.user_stat_yn</code>. 상담원통계라이센스여부
     */
    public final TableField<CommonStatUserInboundRecord, String> USER_STAT_YN = createField(DSL.name("user_stat_yn"), org.jooq.impl.SQLDataType.CHAR(1).defaultValue(org.jooq.impl.DSL.inline("'Y'", org.jooq.impl.SQLDataType.CHAR)), this, "상담원통계라이센스여부");


    /**
     * The column <code>STATDB.stat_user_inbound.service_number</code>. 대표번호070
     */
    public final TableField<CommonStatUserInboundRecord, String> SERVICE_NUMBER = createField(DSL.name("service_number"), org.jooq.impl.SQLDataType.VARCHAR(30).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "대표번호070");

    /**
     * The column <code>STATDB.stat_user_inbound.hunt_number</code>. 헌트번호
     */
    public final TableField<CommonStatUserInboundRecord, String> HUNT_NUMBER = createField(DSL.name("hunt_number"), org.jooq.impl.SQLDataType.VARCHAR(30).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "헌트번호");

    /**
     * The column <code>STATDB.stat_user_inbound.worktime_yn</code>.
     */
    public final TableField<CommonStatUserInboundRecord, String> WORKTIME_YN = createField(DSL.name("worktime_yn"), org.jooq.impl.SQLDataType.CHAR(1).defaultValue(org.jooq.impl.DSL.inline("Y", org.jooq.impl.SQLDataType.CHAR)), this, "");

    /**
     * The column <code>STATDB.stat_user_inbound.category</code>. 인입경로
     */
    public final TableField<CommonStatUserInboundRecord, String> CATEGORY = createField(DSL.name("category"), org.jooq.impl.SQLDataType.VARCHAR(300).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "인입경로");

    /**
     * The column <code>STATDB.stat_user_inbound.dcontext</code>. 다이얼플랜컨택스트
     */
    public final TableField<CommonStatUserInboundRecord, String> DCONTEXT = createField(DSL.name("dcontext"), org.jooq.impl.SQLDataType.VARCHAR(30).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "다이얼플랜컨택스트");

    /**
     * The column <code>STATDB.stat_user_inbound.stat_date</code>. 날짜
     */
    public final TableField<CommonStatUserInboundRecord, Date> STAT_DATE = createField(DSL.name("stat_date"), org.jooq.impl.SQLDataType.DATE.nullable(false).defaultValue(org.jooq.impl.DSL.inline("2009-01-01", org.jooq.impl.SQLDataType.DATE)), this, "날짜");

    /**
     * The column <code>STATDB.stat_user_inbound.stat_hour</code>. 시간
     */
    public final TableField<CommonStatUserInboundRecord, Byte> STAT_HOUR = createField(DSL.name("stat_hour"), org.jooq.impl.SQLDataType.TINYINT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.TINYINT)), this, "시간");

    /**
     * The column <code>STATDB.stat_user_inbound.in_total</code>. 총계
     */
    public final TableField<CommonStatUserInboundRecord, Integer> IN_TOTAL = createField(DSL.name("in_total"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "총계");

    /**
     * The column <code>STATDB.stat_user_inbound.in_success</code>. 응답
     */
    public final TableField<CommonStatUserInboundRecord, Integer> IN_SUCCESS = createField(DSL.name("in_success"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "응답");

    /**
     * The column <code>STATDB.stat_user_inbound.in_hunt_noanswer</code>. 헌트시비응답
     */
    public final TableField<CommonStatUserInboundRecord, Integer> IN_HUNT_NOANSWER = createField(DSL.name("in_hunt_noanswer"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "헌트시비응답");

    /**
     * The column <code>STATDB.stat_user_inbound.in_billsec_sum</code>. 통화시간
     */
    public final TableField<CommonStatUserInboundRecord, Integer> IN_BILLSEC_SUM = createField(DSL.name("in_billsec_sum"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "통화시간");

    /**
     * The column <code>STATDB.stat_user_inbound.in_waitsec_sum</code>. 고객대기시간
     */
    public final TableField<CommonStatUserInboundRecord, Integer> IN_WAITSEC_SUM = createField(DSL.name("in_waitsec_sum"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "고객대기시간");

    /**
     * The column <code>STATDB.stat_user_inbound.transferer</code>. 전환한콜수
     */
    public final TableField<CommonStatUserInboundRecord, Integer> TRANSFERER = createField(DSL.name("transferer"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "전환한콜수");

    /**
     * The column <code>STATDB.stat_user_inbound.transferee</code>. 전환받은콜수
     */
    public final TableField<CommonStatUserInboundRecord, Integer> TRANSFEREE = createField(DSL.name("transferee"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "전환받은콜수");

    /**
     * The column <code>STATDB.stat_user_inbound.transferee_succ</code>. 전환받은성공콜수
     */
    public final TableField<CommonStatUserInboundRecord, Integer> TRANSFEREE_SUCC = createField(DSL.name("transferee_succ"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "전환받은성공콜수");

    /**
     * The column <code>STATDB.stat_user_inbound.callback_dist</code>. 콜백분배받은건수
     */
    public final TableField<CommonStatUserInboundRecord, Integer> CALLBACK_DIST = createField(DSL.name("callback_dist"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "콜백분배받은건수");

    /**
     * The column <code>STATDB.stat_user_inbound.callback_succ</code>. 분배받은 콜백을 처리한건수
     */
    public final TableField<CommonStatUserInboundRecord, Integer> CALLBACK_SUCC = createField(DSL.name("callback_succ"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "분배받은 콜백을 처리한건수");

    /**
     * The column <code>STATDB.stat_user_inbound.service_level_ok</code>. 서비스레벨안에 전화를 받은건수
     */
    public final TableField<CommonStatUserInboundRecord, Integer> SERVICE_LEVEL_OK = createField(DSL.name("service_level_ok"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "서비스레벨안에 전화를 받은건수");

    private final String tableName;

    /**
     * Create a <code>STATDB.stat_user_inbound</code> table reference
     */
    public CommonStatUserInbound(String companyName) {
        this(DSL.name("stat_user_inbound_" + companyName), null);
    }

    private CommonStatUserInbound(Name alias, Table<CommonStatUserInboundRecord> aliased) {
        this(alias, aliased, null);
    }

    private CommonStatUserInbound(Name alias, Table<CommonStatUserInboundRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment("상담원의 아웃바운드 통계 테이블"));
        this.tableName = alias.last();
    }

    public <O extends Record> CommonStatUserInbound(CommonStatUserInbound table, Table<O> child, ForeignKey<O, CommonStatUserInboundRecord> key) {
        super(child, key, table);
        this.tableName = table.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<CommonStatUserInboundRecord, Integer> getIdentity() {
        return org.jooq.impl.Internal.createIdentity(this, this.SEQ);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<CommonStatUserInboundRecord> getPrimaryKey() {
        return org.jooq.impl.Internal.createUniqueKey(this, "KEY_" + getName() + "_PRIMARY", this.SEQ);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<CommonStatUserInboundRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, "KEY_" + getName() + "_PRIMARY", this.SEQ));
    }

    @Override
    public CommonStatUserInbound as(String alias) {
        return new CommonStatUserInbound(DSL.name(alias), this);
    }

    @Override
    public CommonStatUserInbound as(Name alias) {
        return new CommonStatUserInbound(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonStatUserInbound rename(String name) {
        return new CommonStatUserInbound(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonStatUserInbound rename(Name name) {
        return new CommonStatUserInbound(name, null);
    }
}
