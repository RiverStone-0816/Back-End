package kr.co.eicn.ippbx.meta.jooq.statdb.tables;

import kr.co.eicn.ippbx.meta.jooq.statdb.tables.records.StatQueueWaitRecord;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.jooq.impl.TableImpl;

import java.sql.Date;
import java.util.Collections;
import java.util.List;

public class CommonStatQueueWait extends TableImpl<StatQueueWaitRecord> {

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<StatQueueWaitRecord> getRecordType() {
		return StatQueueWaitRecord.class;
	}

	/**
	 * The column <code>STATDB.stat_queue_wait.seq</code>. 고유키
	 */
	public final TableField<StatQueueWaitRecord, Integer> SEQ = createField(DSL.name("seq"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "고유키");

	/**
	 * The column <code>STATDB.stat_queue_wait.company_id</code>. 회사 ID
	 */
	public final TableField<StatQueueWaitRecord, String> COMPANY_ID = createField(DSL.name("company_id"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "회사 ID");

	/**
	 * The column <code>STATDB.stat_queue_wait.group_code</code>. 조직코드
	 */
	public final TableField<StatQueueWaitRecord, String> GROUP_CODE = createField(DSL.name("group_code"), org.jooq.impl.SQLDataType.CHAR(4), this, "조직코드");

	/**
	 * The column <code>STATDB.stat_queue_wait.group_tree_name</code>. 조직트리명
	 */
	public final TableField<StatQueueWaitRecord, String> GROUP_TREE_NAME = createField(DSL.name("group_tree_name"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "조직트리명");

	/**
	 * The column <code>STATDB.stat_queue_wait.group_level</code>. 조직레벨
	 */
	public final TableField<StatQueueWaitRecord, Integer> GROUP_LEVEL = createField(DSL.name("group_level"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "조직레벨");

	/**
	 * The column <code>STATDB.stat_queue_wait.queue_name</code>. QUEUE 이름
	 */
	public final TableField<StatQueueWaitRecord, String> QUEUE_NAME = createField(DSL.name("queue_name"), org.jooq.impl.SQLDataType.VARCHAR(128).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "QUEUE 이름");

	/**
	 * The column <code>STATDB.stat_queue_wait.stat_date</code>. 생성일
	 */
	public final TableField<StatQueueWaitRecord, Date> STAT_DATE = createField(DSL.name("stat_date"), org.jooq.impl.SQLDataType.DATE.defaultValue(org.jooq.impl.DSL.inline("2009-01-01", org.jooq.impl.SQLDataType.DATE)), this, "생성일");

	/**
	 * The column <code>STATDB.stat_queue_wait.stat_hour</code>. 생성시간
	 */
	public final TableField<StatQueueWaitRecord, Byte> STAT_HOUR = createField(DSL.name("stat_hour"), org.jooq.impl.SQLDataType.TINYINT.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.TINYINT)), this, "생성시간");

	/**
	 * The column <code>STATDB.stat_queue_wait.total_wait</code>. 시간당대기자수합계
	 */
	public final TableField<StatQueueWaitRecord, Integer> TOTAL_WAIT = createField(DSL.name("total_wait"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "시간당대기자수합계");

	/**
	 * The column <code>STATDB.stat_queue_wait.max_wait</code>. 최대대기자수
	 */
	public final TableField<StatQueueWaitRecord, Integer> MAX_WAIT = createField(DSL.name("max_wait"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "최대대기자수");

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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Identity<StatQueueWaitRecord, Integer> getIdentity() {
		return org.jooq.impl.Internal.createIdentity(this, this.SEQ);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<StatQueueWaitRecord> getPrimaryKey() {
		return org.jooq.impl.Internal.createUniqueKey(this, "KEY_" + getName() + "_PRIMARY", this.SEQ);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<StatQueueWaitRecord>> getKeys() {
		return Collections.singletonList(Internal.createUniqueKey(this, "KEY_" + getName() + "_PRIMARY", this.SEQ));
	}
	@Override
	public CommonStatQueueWait as(String alias) {
		return new CommonStatQueueWait(DSL.name(alias), this);
	}

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

	// -------------------------------------------------------------------------
	// Row10 type methods
	// -------------------------------------------------------------------------

	@Override
	public Row10<Integer, String, String, String, Integer, String, Date, Byte, Integer, Integer> fieldsRow() {
		return (Row10) super.fieldsRow();
	}
}
