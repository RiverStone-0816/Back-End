package kr.co.eicn.ippbx.meta.jooq.statdb.tables;

import kr.co.eicn.ippbx.meta.jooq.statdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.statdb.Statdb;
import kr.co.eicn.ippbx.meta.jooq.statdb.tables.records.StatQueueWaitRecord;
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

public class CommonStatQueueWait extends TableImpl<StatQueueWaitRecord> {
    /**
     * The column <code>STATDB.stat_queue_wait.seq</code>. 고유키
     */
    public final TableField<StatQueueWaitRecord, Integer> SEQ = createField(DSL.name("seq"), SQLDataType.INTEGER.nullable(false).identity(true), this, "고유키");

    /**
     * The column <code>STATDB.stat_queue_wait.company_id</code>. 회사 ID
     */
    public final TableField<StatQueueWaitRecord, String> COMPANY_ID = createField(DSL.name("company_id"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "회사 ID");

    /**
     * The column <code>STATDB.stat_queue_wait.group_code</code>. 조직코드
     */
    public final TableField<StatQueueWaitRecord, String> GROUP_CODE = createField(DSL.name("group_code"), SQLDataType.CHAR(4).defaultValue(DSL.field("NULL", SQLDataType.CHAR)), this, "조직코드");

    /**
     * The column <code>STATDB.stat_queue_wait.group_tree_name</code>. 조직트리명
     */
    public final TableField<StatQueueWaitRecord, String> GROUP_TREE_NAME = createField(DSL.name("group_tree_name"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "조직트리명");

    /**
     * The column <code>STATDB.stat_queue_wait.group_level</code>. 조직레벨
     */
    public final TableField<StatQueueWaitRecord, Integer> GROUP_LEVEL = createField(DSL.name("group_level"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "조직레벨");

    /**
     * The column <code>STATDB.stat_queue_wait.queue_name</code>. QUEUE 이름
     */
    public final TableField<StatQueueWaitRecord, String> QUEUE_NAME = createField(DSL.name("queue_name"), SQLDataType.VARCHAR(128).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "QUEUE 이름");

    /**
     * The column <code>STATDB.stat_queue_wait.stat_date</code>. 생성일
     */
    public final TableField<StatQueueWaitRecord, Date> STAT_DATE = createField(DSL.name("stat_date"), SQLDataType.DATE.defaultValue(DSL.field("'2009-01-01'", SQLDataType.DATE)), this, "생성일");

    /**
     * The column <code>STATDB.stat_queue_wait.stat_hour</code>. 생성시간
     */
    public final TableField<StatQueueWaitRecord, Byte> STAT_HOUR = createField(DSL.name("stat_hour"), SQLDataType.TINYINT.defaultValue(DSL.field("0", SQLDataType.TINYINT)), this, "생성시간");

    /**
     * The column <code>STATDB.stat_queue_wait.total_wait</code>. 시간당대기자수합계
     */
    public final TableField<StatQueueWaitRecord, Integer> TOTAL_WAIT = createField(DSL.name("total_wait"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "시간당대기자수합계");

    /**
     * The column <code>STATDB.stat_queue_wait.max_wait</code>. 최대대기자수
     */
    public final TableField<StatQueueWaitRecord, Integer> MAX_WAIT = createField(DSL.name("max_wait"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "최대대기자수");

    private final String tableName;

    /**
     * Create a <code>STATDB.stat_outbound</code> table reference
     */
    public CommonStatQueueWait(String companyName) {
        this(DSL.name("stat_queue_wait_" + companyName), null);
    }

    private CommonStatQueueWait(Name alias, Table<StatQueueWaitRecord> aliased) {
        this(alias, aliased, null);
    }

    private CommonStatQueueWait(Name alias, Table<StatQueueWaitRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment("헌트대기자수통계 테이블"));
        this.tableName = alias.last();
    }

    public <O extends Record> CommonStatQueueWait(CommonStatQueueWait table, Table<O> child, ForeignKey<O, StatQueueWaitRecord> key) {
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
        return Arrays.<Index>asList(Indexes.STAT_QUEUE_WAIT_COMPANY_ID, Indexes.STAT_QUEUE_WAIT_GROUP_CODE, Indexes.STAT_QUEUE_WAIT_GROUP_LEVEL, Indexes.STAT_QUEUE_WAIT_GROUP_TREE_NAME, Indexes.STAT_QUEUE_WAIT_QUEUE_NAME, Indexes.STAT_QUEUE_WAIT_STAT_DATE, Indexes.STAT_QUEUE_WAIT_STAT_HOUR);
    }

    @Override
    public Identity<StatQueueWaitRecord, Integer> getIdentity() {
        return Internal.createIdentity(this, this.SEQ);
    }

    @Override
    public UniqueKey<StatQueueWaitRecord> getPrimaryKey() {
        return Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ);
    }

    @NotNull
    @Override
    public List<UniqueKey<StatQueueWaitRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ));
    }

    @NotNull
    @Override
    public CommonStatQueueWait as(String alias) {
        return new CommonStatQueueWait(DSL.name(alias), this);
    }

    @NotNull
    @Override
    public CommonStatQueueWait as(Name alias) {
        return new CommonStatQueueWait(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonStatQueueWait rename(String name) {
        return new CommonStatQueueWait(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonStatQueueWait rename(Name name) {
        return new CommonStatQueueWait(name, null);
    }
}
