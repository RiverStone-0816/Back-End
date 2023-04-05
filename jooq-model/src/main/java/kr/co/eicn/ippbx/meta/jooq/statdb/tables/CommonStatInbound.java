package kr.co.eicn.ippbx.meta.jooq.statdb.tables;

import kr.co.eicn.ippbx.meta.jooq.statdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.statdb.Statdb;
import kr.co.eicn.ippbx.meta.jooq.statdb.tables.records.CommonStatInboundRecord;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.jooq.impl.TableImpl;

import java.sql.Date;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommonStatInbound extends TableImpl<CommonStatInboundRecord> {
    /**
     * The column <code>STATDB.stat_inbound.seq</code>. 고유키
     */
    public final TableField<CommonStatInboundRecord, Integer> SEQ = createField(DSL.name("seq"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "고유키");

    /**
     * The column <code>STATDB.stat_inbound.company_id</code>. 회사 ID
     */
    public final TableField<CommonStatInboundRecord, String> COMPANY_ID = createField(DSL.name("company_id"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "회사 ID");

    /**
     * The column <code>STATDB.stat_inbound.group_code</code>. 조직코드
     */
    public final TableField<CommonStatInboundRecord, String> GROUP_CODE = createField(DSL.name("group_code"), org.jooq.impl.SQLDataType.CHAR(4), this, "조직코드");

    /**
     * The column <code>STATDB.stat_inbound.group_tree_name</code>. 조직트리명
     */
    public final TableField<CommonStatInboundRecord, String> GROUP_TREE_NAME = createField(DSL.name("group_tree_name"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "조직트리명");

    /**
     * The column <code>STATDB.stat_inbound.group_level</code>. 조직레벨
     */
    public final TableField<CommonStatInboundRecord, Integer> GROUP_LEVEL = createField(DSL.name("group_level"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "조직레벨");

    /**
     * The column <code>STATDB.stat_inbound.service_number</code>. 대표번호070
     */
    public final TableField<CommonStatInboundRecord, String> SERVICE_NUMBER = createField(DSL.name("service_number"), org.jooq.impl.SQLDataType.VARCHAR(30).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "대표번호070");

    /**
     * The column <code>STATDB.stat_inbound.hunt_number</code>. 헌트번호
     */
    public final TableField<CommonStatInboundRecord, String> HUNT_NUMBER = createField(DSL.name("hunt_number"), org.jooq.impl.SQLDataType.VARCHAR(30).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "헌트번호");

    /**
     * The column <code>STATDB.stat_inbound.worktime_yn</code>. 업무시간인지 아닌지 스케쥴유형에서 결정됨
     */
    public final TableField<CommonStatInboundRecord, String> WORKTIME_YN = createField(DSL.name("worktime_yn"), org.jooq.impl.SQLDataType.CHAR(1).defaultValue(org.jooq.impl.DSL.inline("N", org.jooq.impl.SQLDataType.CHAR)), this, "업무시간인지 아닌지 스케쥴유형에서 결정됨");

    /**
     * The column <code>STATDB.stat_inbound.category</code>. 인입경로
     */
    public final TableField<CommonStatInboundRecord, String> CATEGORY = createField(DSL.name("category"), org.jooq.impl.SQLDataType.VARCHAR(300).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "인입경로");

    /**
     * The column <code>STATDB.stat_inbound.ivr_tree_name</code>. ivr_tree의 tree_name
     */
    public final TableField<CommonStatInboundRecord, String> IVR_TREE_NAME = createField(DSL.name("ivr_tree_name"), org.jooq.impl.SQLDataType.VARCHAR(300).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "ivr_tree의 tree_name");

    /**
     * The column <code>STATDB.stat_inbound.dcontext</code>. 다이얼플랜컨텍스트
     */
    public final TableField<CommonStatInboundRecord, String> DCONTEXT = createField(DSL.name("dcontext"), org.jooq.impl.SQLDataType.VARCHAR(30).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "다이얼플랜컨텍스트");

    /**
     * The column <code>STATDB.stat_inbound.stat_date</code>. 생성일
     */
    public final TableField<CommonStatInboundRecord, Date> STAT_DATE = createField(DSL.name("stat_date"), org.jooq.impl.SQLDataType.DATE.defaultValue(org.jooq.impl.DSL.inline("2009-01-01", org.jooq.impl.SQLDataType.DATE)), this, "생성일");

    /**
     * The column <code>STATDB.stat_inbound.stat_hour</code>. 생성시간
     */
    public final TableField<CommonStatInboundRecord, Byte> STAT_HOUR = createField(DSL.name("stat_hour"), org.jooq.impl.SQLDataType.TINYINT.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.TINYINT)), this, "생성시간");

    /**
     * The column <code>STATDB.stat_inbound.total</code>. 총계
     */
    public final TableField<CommonStatInboundRecord, Integer> TOTAL = createField(DSL.name("total"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "총계");

    /**
     * The column <code>STATDB.stat_inbound.onlyread</code>. 단순조회
     */
    public final TableField<CommonStatInboundRecord, Integer> ONLYREAD = createField(DSL.name("onlyread"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "단순조회");

    /**
     * The column <code>STATDB.stat_inbound.connreq</code>. 연결요청
     */
    public final TableField<CommonStatInboundRecord, Integer> CONNREQ = createField(DSL.name("connreq"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "연결요청");

    /**
     * The column <code>STATDB.stat_inbound.success</code>. 응답
     */
    public final TableField<CommonStatInboundRecord, Integer> SUCCESS = createField(DSL.name("success"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "응답");

    /**
     * The column <code>STATDB.stat_inbound.callback</code>. 콜백건수
     */
    public final TableField<CommonStatInboundRecord, Integer> CALLBACK = createField(DSL.name("callback"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "콜백건수");

    /**
     * The column <code>STATDB.stat_inbound.callback_success</code>. 콜백성공건수-콜백이접수된건이지 처리된건은 아님
     */
    public final TableField<CommonStatInboundRecord, Integer> CALLBACK_SUCCESS = createField(DSL.name("callback_success"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "콜백성공건수-콜백이접수된건이지 처리된건은 아님");

    /**
     * The column <code>STATDB.stat_inbound.transfer</code>. 전환한콜수-첫전환콜만
     */
    public final TableField<CommonStatInboundRecord, Integer> TRANSFER = createField(DSL.name("transfer"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "전환한콜수-첫전환콜만");

    /**
     * The column <code>STATDB.stat_inbound.cancel</code>. 포기호
     */
    public final TableField<CommonStatInboundRecord, Integer> CANCEL = createField(DSL.name("cancel"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "포기호");

    /**
     * The column <code>STATDB.stat_inbound.cancel_timeout</code>. 헌트타임아웃으로인한포기호
     */
    public final TableField<CommonStatInboundRecord, Integer> CANCEL_TIMEOUT = createField(DSL.name("cancel_timeout"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "헌트타임아웃으로인한포기호");

    /**
     * The column <code>STATDB.stat_inbound.cancel_noanswer</code>. 비수신포기호-상담사인입O
     */
    public final TableField<CommonStatInboundRecord, Integer> CANCEL_NOANSWER = createField(DSL.name("cancel_noanswer"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "비수신포기호-상담사인입O");

    /**
     * The column <code>STATDB.stat_inbound.cancel_custom</code>. 고객포기호
     */
    public final TableField<CommonStatInboundRecord, Integer> CANCEL_CUSTOM = createField(DSL.name("cancel_custom"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "고객포기호");

    /**
     * The column <code>STATDB.stat_inbound.service_level_ok</code>. 서비스레벨안에 전화를 받은건수
     */
    public final TableField<CommonStatInboundRecord, Integer> SERVICE_LEVEL_OK = createField(DSL.name("service_level_ok"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "서비스레벨안에 전화를 받은건수");

    /**
     * The column <code>STATDB.stat_inbound.billsec_sum</code>. 통화시간
     */
    public final TableField<CommonStatInboundRecord, Integer> BILLSEC_SUM = createField(DSL.name("billsec_sum"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "통화시간");

    /**
     * The column <code>STATDB.stat_inbound.wait_sum</code>. 대기시간합계
     */
    public final TableField<CommonStatInboundRecord, Integer> WAIT_SUM = createField(DSL.name("wait_sum"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "대기시간합계");

    /**
     * The column <code>STATDB.stat_inbound.billsec_max</code>. 최대통화시간
     */
    public final TableField<CommonStatInboundRecord, Integer> BILLSEC_MAX = createField(DSL.name("billsec_max"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "최대통화시간");

    /**
     * The column <code>STATDB.stat_inbound.wait_max</code>. 최대대기시간
     */
    public final TableField<CommonStatInboundRecord, Integer> WAIT_MAX = createField(DSL.name("wait_max"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "최대대기시간");

    /**
     * The column <code>STATDB.stat_inbound.wait_cancel_sum</code>. 포기호들의대기시간합계
     */
    public final TableField<CommonStatInboundRecord, Integer> WAIT_CANCEL_SUM = createField(DSL.name("wait_cancel_sum"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "포기호들의대기시간합계");

    /**
     * The column <code>STATDB.stat_inbound.wait_succ_0_10</code>. 응대호 0초&lt;대기시간=&lt;10초 건수
     */
    public final TableField<CommonStatInboundRecord, Integer> WAIT_SUCC_0_10 = createField(DSL.name("wait_succ_0_10"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "응대호 0초<대기시간=<10초 건수");

    /**
     * The column <code>STATDB.stat_inbound.wait_succ_10_20</code>. 응대호 10초&lt;대기시간&lt;=20초 건수
     */
    public final TableField<CommonStatInboundRecord, Integer> WAIT_SUCC_10_20 = createField(DSL.name("wait_succ_10_20"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "응대호 10초<대기시간<=20초 건수");

    /**
     * The column <code>STATDB.stat_inbound.wait_succ_20_30</code>. 응대호 20초&lt;대기시간&lt;=30초 건수
     */
    public final TableField<CommonStatInboundRecord, Integer> WAIT_SUCC_20_30 = createField(DSL.name("wait_succ_20_30"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "응대호 20초<대기시간<=30초 건수");

    /**
     * The column <code>STATDB.stat_inbound.wait_succ_30_40</code>. 응대호 30초&lt;대기시간&lt;=40초 건수
     */
    public final TableField<CommonStatInboundRecord, Integer> WAIT_SUCC_30_40 = createField(DSL.name("wait_succ_30_40"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "응대호 30초<대기시간<=40초 건수");

    /**
     * The column <code>STATDB.stat_inbound.wait_succ_40</code>. 응대호 40초&lt;대기시간 건수
     */
    public final TableField<CommonStatInboundRecord, Integer> WAIT_SUCC_40 = createField(DSL.name("wait_succ_40"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "응대호 40초<대기시간 건수");

    /**
     * The column <code>STATDB.stat_inbound.wait_cancel_0_10</code>. 포기호 0초&lt;대기시간=&lt;10초 건수
     */
    public final TableField<CommonStatInboundRecord, Integer> WAIT_CANCEL_0_10 = createField(DSL.name("wait_cancel_0_10"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "포기호 0초<대기시간=<10초 건수");

    /**
     * The column <code>STATDB.stat_inbound.wait_cancel_10_20</code>. 포기호 10초&lt;대기시간&lt;=20초 건수
     */
    public final TableField<CommonStatInboundRecord, Integer> WAIT_CANCEL_10_20 = createField(DSL.name("wait_cancel_10_20"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "포기호 10초<대기시간<=20초 건수");

    /**
     * The column <code>STATDB.stat_inbound.wait_cancel_20_30</code>. 포기호 20초&lt;대기시간&lt;=30초 건수
     */
    public final TableField<CommonStatInboundRecord, Integer> WAIT_CANCEL_20_30 = createField(DSL.name("wait_cancel_20_30"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "포기호 20초<대기시간<=30초 건수");

    /**
     * The column <code>STATDB.stat_inbound.wait_cancel_30_40</code>. 포기호 30초&lt;대기시간&lt;=40초 건수
     */
    public final TableField<CommonStatInboundRecord, Integer> WAIT_CANCEL_30_40 = createField(DSL.name("wait_cancel_30_40"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "포기호 30초<대기시간<=40초 건수");

    /**
     * The column <code>STATDB.stat_inbound.wait_cancel_40</code>. 포기호 40초&lt;대기시간 건수
     */
    public final TableField<CommonStatInboundRecord, Integer> WAIT_CANCEL_40 = createField(DSL.name("wait_cancel_40"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "포기호 40초<대기시간 건수");

    private final String tableName;

    /**
     * Create a <code>STATDB.stat_inbound</code> table reference
     */
    public CommonStatInbound(String companyName) {
        this(DSL.name("stat_inbound_" + companyName), null);
    }

    private CommonStatInbound(Name alias, Table<CommonStatInboundRecord> aliased) {
        this(alias, aliased, null);
    }

    private CommonStatInbound(Name alias, Table<CommonStatInboundRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
        this.tableName = alias.last();
    }

    public <O extends Record> CommonStatInbound(CommonStatInbound table, Table<O> child, ForeignKey<O, CommonStatInboundRecord> key) {
        super(child, key, table);
        this.tableName = table.getName();
    }

    @Override
    public Schema getSchema() {
        return Statdb.STATDB;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.STAT_INBOUND_CATEGORY, Indexes.STAT_INBOUND_COMPANY_ID, Indexes.STAT_INBOUND_DCONTEXT, Indexes.STAT_INBOUND_GROUP_CODE, Indexes.STAT_INBOUND_GROUP_LEVEL, Indexes.STAT_INBOUND_GROUP_TREE_NAME, Indexes.STAT_INBOUND_HUNT_NUMBER, Indexes.STAT_INBOUND_SERVICE_NUMBER, Indexes.STAT_INBOUND_STAT_DATE, Indexes.STAT_INBOUND_STAT_HOUR);
    }

    @Override
    public Identity<CommonStatInboundRecord, Integer> getIdentity() {
        return Internal.createIdentity(this, this.SEQ);
    }

    @Override
    public UniqueKey<CommonStatInboundRecord> getPrimaryKey() {
        return Internal.createUniqueKey(this, "KEY_" + getName() + "_PRIMARY", this.SEQ);
    }

    @Override
    public List<UniqueKey<CommonStatInboundRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, "KEY_" + getName() + "_PRIMARY", this.SEQ));
    }

    @Override
    public CommonStatInbound as(String alias) {
        return new CommonStatInbound(DSL.name(alias), this);
    }

    @Override
    public CommonStatInbound as(Name alias) {
        return new CommonStatInbound(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonStatInbound rename(String name) {
        return new CommonStatInbound(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonStatInbound rename(Name name) {
        return new CommonStatInbound(name, null);
    }
}
