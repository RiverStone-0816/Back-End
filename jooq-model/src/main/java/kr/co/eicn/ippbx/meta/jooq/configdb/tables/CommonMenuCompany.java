package kr.co.eicn.ippbx.meta.jooq.configdb.tables;

import kr.co.eicn.ippbx.meta.jooq.configdb.tables.records.CommonMenuCompanyRecord;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.jooq.impl.TableImpl;

import java.util.Collections;
import java.util.List;

public class CommonMenuCompany extends TableImpl<CommonMenuCompanyRecord> {

	/**
	 * The column <code>CONFIGDB.menu_company_*.seq</code>.
	 */
	public final TableField<CommonMenuCompanyRecord, Integer> SEQ = createField(DSL.name("seq"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "");

	/**
	 * The column <code>CONFIGDB.menu_company_*.userid</code>.
	 */
	public final TableField<CommonMenuCompanyRecord, String> USERID = createField(DSL.name("userid"), org.jooq.impl.SQLDataType.VARCHAR(30).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CONFIGDB.menu_company_*.menu_code</code>.
	 */
	public final TableField<CommonMenuCompanyRecord, String> MENU_CODE = createField(DSL.name("menu_code"), org.jooq.impl.SQLDataType.CHAR(4).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.CHAR)), this, "");

	/**
	 * The column <code>CONFIGDB.menu_company_*.menu_name</code>.
	 */
	public final TableField<CommonMenuCompanyRecord, String> MENU_NAME = createField(DSL.name("menu_name"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CONFIGDB.menu_company_*.menu_tree_name</code>.
	 */
	public final TableField<CommonMenuCompanyRecord, String> MENU_TREE_NAME = createField(DSL.name("menu_tree_name"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CONFIGDB.menu_company_*.menu_level</code>.
	 */
	public final TableField<CommonMenuCompanyRecord, Integer> MENU_LEVEL = createField(DSL.name("menu_level"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

	/**
	 * The column <code>CONFIGDB.menu_company_*.parent_menu_code</code>.
	 */
	public final TableField<CommonMenuCompanyRecord, String> PARENT_MENU_CODE = createField(DSL.name("parent_menu_code"), org.jooq.impl.SQLDataType.CHAR(4).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.CHAR)), this, "");

	/**
	 * The column <code>CONFIGDB.menu_company_*.parent_tree_name</code>.
	 */
	public final TableField<CommonMenuCompanyRecord, String> PARENT_TREE_NAME = createField(DSL.name("parent_tree_name"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CONFIGDB.menu_company_*.menu_action_exe_id</code>.
	 */
	public final TableField<CommonMenuCompanyRecord, String> MENU_ACTION_EXE_ID = createField(DSL.name("menu_action_exe_id"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CONFIGDB.menu_company_*.sequence</code>.
	 */
	public final TableField<CommonMenuCompanyRecord, Integer> SEQUENCE = createField(DSL.name("sequence"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

	/**
	 * The column <code>CONFIGDB.menu_company_*.view_yn</code>.
	 */
	public final TableField<CommonMenuCompanyRecord, String> VIEW_YN = createField(DSL.name("view_yn"), org.jooq.impl.SQLDataType.CHAR(1).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.CHAR)), this, "");

	/**
	 * The column <code>CONFIGDB.menu_company_*.icon</code>.
	 */
	public final TableField<CommonMenuCompanyRecord, String> ICON = createField(DSL.name("icon"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CONFIGDB.menu_company_*.action_type</code>.
	 */
	public final TableField<CommonMenuCompanyRecord, String> ACTION_TYPE = createField(DSL.name("action_type"), org.jooq.impl.SQLDataType.VARCHAR(10).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CONFIGDB.menu_company_*.auth_type</code>.
	 */
	public final TableField<CommonMenuCompanyRecord, String> AUTH_TYPE = createField(DSL.name("auth_type"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

	/**
	 * The column <code>CONFIGDB.menu_company_*.group_level_auth_yn</code>.
	 */
	public final TableField<CommonMenuCompanyRecord, String> GROUP_LEVEL_AUTH_YN = createField(DSL.name("group_level_auth_yn"), org.jooq.impl.SQLDataType.CHAR(1).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.CHAR)), this, "");

	/**
	 * The column <code>CONFIGDB.menu_company_*.group_code</code>.
	 */
	public final TableField<CommonMenuCompanyRecord, String> GROUP_CODE = createField(DSL.name("group_code"), org.jooq.impl.SQLDataType.CHAR(4).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.CHAR)), this, "");

	/**
	 * The column <code>CONFIGDB.menu_company_*.group_level</code>.
	 */
	public final TableField<CommonMenuCompanyRecord, Integer> GROUP_LEVEL = createField(DSL.name("group_level"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

	private final String tableName;

	public CommonMenuCompany(String companyName) {
		this(DSL.name("menu_company_" + companyName), null);
	}

	/**
	 * Create an aliased <code>CONFIGDB.menu_company_*</code> table reference
	 */
	public CommonMenuCompany(String alias, Table<CommonMenuCompanyRecord> aliased) {
		this(DSL.name(alias), aliased);
	}

	/**
	 * Create an aliased <code>CONFIGDB.menu_company_*</code> table reference
	 */
	public CommonMenuCompany(Name alias, Table<CommonMenuCompanyRecord> aliased) {
		this(alias, aliased, null);
	}

	private CommonMenuCompany(Name alias, Table<CommonMenuCompanyRecord> aliased, Field<?>[] parameters) {
		super(alias, null, aliased, parameters, DSL.comment(""));
		this.tableName = alias.last();
	}

	public <O extends Record> CommonMenuCompany(CommonMenuCompany table, Table<O> child, ForeignKey<O, CommonMenuCompanyRecord> key) {
		super(child, key, table);
		this.tableName = table.getName();
	}

	@Override
	public Identity<CommonMenuCompanyRecord, Integer> getIdentity() {
		return Internal.createIdentity(this, this.SEQ);
	}

	@Override
	public UniqueKey<CommonMenuCompanyRecord> getPrimaryKey() {
		return Internal.createUniqueKey(this, "KEY_" + getName() + "_PRIMARY", this.SEQ);
	}

	@Override
	public List<UniqueKey<CommonMenuCompanyRecord>> getKeys() {
		return Collections.singletonList(Internal.createUniqueKey(this, "KEY_" + getName() + "_PRIMARY", this.SEQ));
	}

	@Override
	public CommonMenuCompany as(String alias) {
		return new CommonMenuCompany(DSL.name(alias), this);
	}

	@Override
	public CommonMenuCompany as(Name alias) {
		return new CommonMenuCompany(alias, this);
	}

	/**
	 * Rename this table
	 */
	@Override
	public CommonMenuCompany rename(String name) {
		return new CommonMenuCompany(DSL.name(name), null);
	}

	/**
	 * Rename this table
	 */
	@Override
	public CommonMenuCompany rename(Name name) {
		return new CommonMenuCompany(name, null);
	}


	// -------------------------------------------------------------------------
	// Row17 type methods
	// -------------------------------------------------------------------------

	@Override
	public Row17<Integer, String, String, String, String, Integer, String, String, String, Integer, String, String, String, String, String, String, Integer> fieldsRow() {
		return (Row17) super.fieldsRow();
	}
}
