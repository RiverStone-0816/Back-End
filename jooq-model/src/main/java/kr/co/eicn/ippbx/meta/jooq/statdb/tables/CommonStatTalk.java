package kr.co.eicn.ippbx.meta.jooq.statdb.tables;

import kr.co.eicn.ippbx.meta.jooq.statdb.tables.records.CommonStatTalkRecord;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.jooq.impl.TableImpl;

import java.sql.Date;
import java.util.Collections;
import java.util.List;

public class CommonStatTalk extends TableImpl<CommonStatTalkRecord> {
	/**
	 * The column <code>STATDB.stat_talk_*.seq</code>.
	 */
	public final TableField<CommonStatTalkRecord, Integer> SEQ = createField(DSL.name("seq"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "");
	/**
	 * The column <code>STATDB.stat_talk_*.sender_key</code>.
	 */
	public final TableField<CommonStatTalkRecord, String> SENDER_KEY = createField(DSL.name("sender_key"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
	/**
	 * The column <code>STATDB.stat_talk_*.userid</code>. 상담원 아이디
	 */
	public final TableField<CommonStatTalkRecord, String> USERID = createField(DSL.name("userid"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "상담원 아이디");
	/**
	 * The column <code>STATDB.stat_talk_*.group_code</code>.
	 */
	public final TableField<CommonStatTalkRecord, String> GROUP_CODE = createField(DSL.name("group_code"), org.jooq.impl.SQLDataType.CHAR(4).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.CHAR)), this, "");
	/**
	 * The column <code>STATDB.stat_talk_*.group_tree_name</code>.
	 */
	public final TableField<CommonStatTalkRecord, String> GROUP_TREE_NAME = createField(DSL.name("group_tree_name"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
	/**
	 * The column <code>STATDB.stat_talk_*.group_level</code>.
	 */
	public final TableField<CommonStatTalkRecord, Integer> GROUP_LEVEL = createField(DSL.name("group_level"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");
	/**
	 * The column <code>STATDB.stat_talk_*.stat_date</code>.
	 */
	public final TableField<CommonStatTalkRecord, Date> STAT_DATE = createField(DSL.name("stat_date"), org.jooq.impl.SQLDataType.DATE.nullable(false).defaultValue(org.jooq.impl.DSL.inline("2009-01-01", org.jooq.impl.SQLDataType.DATE)), this, "");
	/**
	 * The column <code>STATDB.stat_talk_*.stat_hour</code>.
	 */
	public final TableField<CommonStatTalkRecord, Byte> STAT_HOUR = createField(DSL.name("stat_hour"), org.jooq.impl.SQLDataType.TINYINT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.TINYINT)), this, "");
	/**
	 * The column <code>STATDB.stat_talk_*.start_room_cnt</code>.
	 */
	public final TableField<CommonStatTalkRecord, Integer> START_ROOM_CNT = createField(DSL.name("start_room_cnt"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");
	/**
	 * The column <code>STATDB.stat_talk_*.end_room_cnt</code>.
	 */
	public final TableField<CommonStatTalkRecord, Integer> END_ROOM_CNT = createField(DSL.name("end_room_cnt"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");
	/**
	 * The column <code>STATDB.stat_talk_*.in_msg_cnt</code>.
	 */
	public final TableField<CommonStatTalkRecord, Integer> IN_MSG_CNT = createField(DSL.name("in_msg_cnt"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");
	/**
	 * The column <code>STATDB.stat_talk_*.out_msg_cnt</code>.
	 */
	public final TableField<CommonStatTalkRecord, Integer> OUT_MSG_CNT = createField(DSL.name("out_msg_cnt"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");
	/**
	 * The column <code>STATDB.stat_talk_*.auto_ment_cnt</code>.
	 */
	public final TableField<CommonStatTalkRecord, Integer> AUTO_MENT_CNT = createField(DSL.name("auto_ment_cnt"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");
	/**
	 * The column <code>STATDB.stat_talk_*.auto_ment_exceed_cnt</code>.
	 */
	public final TableField<CommonStatTalkRecord, Integer> AUTO_MENT_EXCEED_CNT = createField(DSL.name("auto_ment_exceed_cnt"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");
	/**
	 * The column <code>STATDB.stat_talk_*.room_time_sum</code>.
	 */
	public final TableField<CommonStatTalkRecord, Integer> ROOM_TIME_SUM = createField(DSL.name("room_time_sum"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

	private final String tableName;

	/**
	 * Create a <code>STATDB.stat_talk_*</code> table reference
	 */
	public CommonStatTalk(String companyName) {
		this(DSL.name("stat_talk_" + companyName), null);
	}
	/**
	 * Create an aliased <code>STATDB.stat_talk_*</code> table reference
	 */
	public CommonStatTalk(String alias, Table<CommonStatTalkRecord> aliased) {
		this(DSL.name(alias), aliased);
	}
	/**
	 * Create an aliased <code>STATDB.stat_talk_*_*</code> table reference
	 */
	public CommonStatTalk(Name alias, Table<CommonStatTalkRecord> aliased) {
		this(alias, aliased, null);
	}

	private CommonStatTalk(Name alias, Table<CommonStatTalkRecord> aliased, Field<?>[] parameters) {
		super(alias, null, aliased, parameters, DSL.comment(""));
		this.tableName = alias.last();
	}

	public <O extends Record> CommonStatTalk(CommonStatTalk table, Table<O> child, ForeignKey<O, CommonStatTalkRecord> key) {
		super(child, key, table);
		this.tableName = table.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Identity<CommonStatTalkRecord, Integer> getIdentity() {
		return Internal.createIdentity(this, this.SEQ);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<CommonStatTalkRecord> getPrimaryKey() {
		return Internal.createUniqueKey(this, "KEY_" + getName() + "_PRIMARY", this.SEQ);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<CommonStatTalkRecord>> getKeys() {
		return Collections.singletonList(Internal.createUniqueKey(this, "KEY_" + getName() + "_PRIMARY", this.SEQ));
	}

	@Override
	public CommonStatTalk as(String alias) {
		return new CommonStatTalk(DSL.name(alias), this);
	}

	@Override
	public CommonStatTalk as(Name alias) {
		return new CommonStatTalk(alias, this);
	}

	/**
	 * Rename this table
	 */
	@Override
	public CommonStatTalk rename(String name) {
		return new CommonStatTalk(DSL.name(name), null);
	}

	/**
	 * Rename this table
	 */
	@Override
	public CommonStatTalk rename(Name name) {
		return new CommonStatTalk(name, null);
	}
}
