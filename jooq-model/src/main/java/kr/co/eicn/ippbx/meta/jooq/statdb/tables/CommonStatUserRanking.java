package kr.co.eicn.ippbx.meta.jooq.statdb.tables;

import kr.co.eicn.ippbx.meta.jooq.statdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.statdb.Statdb;
import kr.co.eicn.ippbx.meta.jooq.statdb.tables.records.CommonStatUserRankingRecord;
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

public class CommonStatUserRanking extends TableImpl<CommonStatUserRankingRecord> {
    /**
     * The column <code>STATDB.stat_user_ranking.seq</code>. 고유키
     */
    public final TableField<CommonStatUserRankingRecord, Integer> SEQ = createField(DSL.name("seq"), SQLDataType.INTEGER.nullable(false).identity(true), this, "고유키");

    /**
     * The column <code>STATDB.stat_user_ranking.company_id</code>. 회사 ID
     */
    public final TableField<CommonStatUserRankingRecord, String> COMPANY_ID = createField(DSL.name("company_id"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "회사 ID");

    /**
     * The column <code>STATDB.stat_user_ranking.group_code</code>. 조직코드
     */
    public final TableField<CommonStatUserRankingRecord, String> GROUP_CODE = createField(DSL.name("group_code"), SQLDataType.CHAR(4).defaultValue(DSL.field("NULL", SQLDataType.CHAR)), this, "조직코드");

    /**
     * The column <code>STATDB.stat_user_ranking.group_tree_name</code>. 조직트리명
     */
    public final TableField<CommonStatUserRankingRecord, String> GROUP_TREE_NAME = createField(DSL.name("group_tree_name"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "조직트리명");

    /**
     * The column <code>STATDB.stat_user_ranking.group_level</code>. 조직레벨
     */
    public final TableField<CommonStatUserRankingRecord, Integer> GROUP_LEVEL = createField(DSL.name("group_level"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "조직레벨");

    /**
     * The column <code>STATDB.stat_user_ranking.userid</code>. 상담원아이디
     */
    public final TableField<CommonStatUserRankingRecord, String> USERID = createField(DSL.name("userid"), SQLDataType.VARCHAR(30).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "상담원아이디");

    /**
     * The column <code>STATDB.stat_user_ranking.worktime_yn</code>. 업무시간여부
     */
    public final TableField<CommonStatUserRankingRecord, String> WORKTIME_YN = createField(DSL.name("worktime_yn"), SQLDataType.CHAR(1).defaultValue(DSL.field("'Y'", SQLDataType.CHAR)), this, "업무시간여부");

    /**
     * The column <code>STATDB.stat_user_ranking.stat_date</code>. 생성일
     */
    public final TableField<CommonStatUserRankingRecord, Date> STAT_DATE = createField(DSL.name("stat_date"), SQLDataType.DATE.defaultValue(DSL.field("'2009-01-01'", SQLDataType.DATE)), this, "생성일");

    /**
     * The column <code>STATDB.stat_user_ranking.total_success</code>. 통화성공총계
     */
    public final TableField<CommonStatUserRankingRecord, Integer> TOTAL_SUCCESS = createField(DSL.name("total_success"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "통화성공총계");

    /**
     * The column <code>STATDB.stat_user_ranking.in_success</code>. 인바운드성공
     */
    public final TableField<CommonStatUserRankingRecord, Integer> IN_SUCCESS = createField(DSL.name("in_success"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "인바운드성공");

    /**
     * The column <code>STATDB.stat_user_ranking.out_success</code>. 아웃바운드성공
     */
    public final TableField<CommonStatUserRankingRecord, Integer> OUT_SUCCESS = createField(DSL.name("out_success"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "아웃바운드성공");

    /**
     * The column <code>STATDB.stat_user_ranking.total_billsec_sum</code>. 통화성공통화시간총계
     */
    public final TableField<CommonStatUserRankingRecord, Integer> TOTAL_BILLSEC_SUM = createField(DSL.name("total_billsec_sum"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "통화성공통화시간총계");

    /**
     * The column <code>STATDB.stat_user_ranking.in_billsec_sum</code>. 인바운드성공통화시간
     */
    public final TableField<CommonStatUserRankingRecord, Integer> IN_BILLSEC_SUM = createField(DSL.name("in_billsec_sum"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "인바운드성공통화시간");

    /**
     * The column <code>STATDB.stat_user_ranking.out_billsec_sum</code>. 아웃바운드성공통화시간
     */
    public final TableField<CommonStatUserRankingRecord, Integer> OUT_BILLSEC_SUM = createField(DSL.name("out_billsec_sum"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "아웃바운드성공통화시간");

    /**
     * The column <code>STATDB.stat_user_ranking.callback_success</code>. 콜백처리성공
     */
    public final TableField<CommonStatUserRankingRecord, Integer> CALLBACK_SUCCESS = createField(DSL.name("callback_success"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "콜백처리성공");

    private final String tableName;

    public CommonStatUserRanking(String companyName) {
        this(DSL.name("stat_user_ranking_" + companyName), null);
    }

    private CommonStatUserRanking(Name alias, Table<CommonStatUserRankingRecord> aliased) {
        this(alias, aliased, null);
    }

    private CommonStatUserRanking(Name alias, Table<CommonStatUserRankingRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment("상담원별랭킹 테이블"));
        this.tableName = alias.last();
    }

    public <O extends Record> CommonStatUserRanking(CommonStatUserRanking table, Table<O> child, ForeignKey<O, CommonStatUserRankingRecord> key) {
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
    public Identity<CommonStatUserRankingRecord, Integer> getIdentity() {
        return org.jooq.impl.Internal.createIdentity(this, this.SEQ);
    }

    @Override
    public UniqueKey<CommonStatUserRankingRecord> getPrimaryKey() {
        return org.jooq.impl.Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ);
    }

    @NotNull
    @Override
    public List<UniqueKey<CommonStatUserRankingRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ));
    }

    @NotNull
    @Override
    public CommonStatUserRanking as(String alias) {
        return new CommonStatUserRanking(DSL.name(alias), this);
    }

    @NotNull
    @Override
    public CommonStatUserRanking as(Name alias) {
        return new CommonStatUserRanking(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonStatUserRanking rename(String name) {
        return new CommonStatUserRanking(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonStatUserRanking rename(Name name) {
        return new CommonStatUserRanking(name, null);
    }
}
