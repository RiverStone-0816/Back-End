package kr.co.eicn.ippbx.meta.jooq.configdb.tables.records;

import org.jooq.Record1;
import org.jooq.Table;
import org.jooq.impl.UpdatableRecordImpl;

public class CommonMenuCompanyRecord extends UpdatableRecordImpl<CommonMenuCompanyRecord> {

	public CommonMenuCompanyRecord(Table<CommonMenuCompanyRecord> table) {
		super(table);
	}

	/**
	 * Setter for <code>CONFIGDB.menu_company_*.seq</code>.
	 */
	public void setSeq(Integer value) {
		set(0, value);
	}

	/**
	 * Getter for <code>CONFIGDB.menu_company_*.seq</code>.
	 */
	public Integer getSeq() {
		return (Integer) get(0);
	}

	/**
	 * Setter for <code>CONFIGDB.menu_company_*.userid</code>.
	 */
	public void setUserid(String value) {
		set(1, value);
	}

	/**
	 * Getter for <code>CONFIGDB.menu_company_*.userid</code>.
	 */
	public String getUserid() {
		return (String) get(1);
	}

	/**
	 * Setter for <code>CONFIGDB.menu_company_*.menu_code</code>.
	 */
	public void setMenuCode(String value) {
		set(2, value);
	}

	/**
	 * Getter for <code>CONFIGDB.menu_company_*.menu_code</code>.
	 */
	public String getMenuCode() {
		return (String) get(2);
	}

	/**
	 * Setter for <code>CONFIGDB.menu_company_*.menu_name</code>.
	 */
	public void setMenuName(String value) {
		set(3, value);
	}

	/**
	 * Getter for <code>CONFIGDB.menu_company_*.menu_name</code>.
	 */
	public String getMenuName() {
		return (String) get(3);
	}

	/**
	 * Setter for <code>CONFIGDB.menu_company_*.menu_tree_name</code>.
	 */
	public void setMenuTreeName(String value) {
		set(4, value);
	}

	/**
	 * Getter for <code>CONFIGDB.menu_company_*.menu_tree_name</code>.
	 */
	public String getMenuTreeName() {
		return (String) get(4);
	}

	/**
	 * Setter for <code>CONFIGDB.menu_company_*.menu_level</code>.
	 */
	public void setMenuLevel(Integer value) {
		set(5, value);
	}

	/**
	 * Getter for <code>CONFIGDB.menu_company_*.menu_level</code>.
	 */
	public Integer getMenuLevel() {
		return (Integer) get(5);
	}

	/**
	 * Setter for <code>CONFIGDB.menu_company_*.parent_menu_code</code>.
	 */
	public void setParentMenuCode(String value) {
		set(6, value);
	}

	/**
	 * Getter for <code>CONFIGDB.menu_company_*.parent_menu_code</code>.
	 */
	public String getParentMenuCode() {
		return (String) get(6);
	}

	/**
	 * Setter for <code>CONFIGDB.menu_company_*.parent_tree_name</code>.
	 */
	public void setParentTreeName(String value) {
		set(7, value);
	}

	/**
	 * Getter for <code>CONFIGDB.menu_company_*.parent_tree_name</code>.
	 */
	public String getParentTreeName() {
		return (String) get(7);
	}

	/**
	 * Setter for <code>CONFIGDB.menu_company_*.menu_action_exe_id</code>.
	 */
	public void setMenuActionExeId(String value) {
		set(8, value);
	}

	/**
	 * Getter for <code>CONFIGDB.menu_company_*.menu_action_exe_id</code>.
	 */
	public String getMenuActionExeId() {
		return (String) get(8);
	}

	/**
	 * Setter for <code>CONFIGDB.menu_company_*.sequence</code>.
	 */
	public void setSequence(Integer value) {
		set(9, value);
	}

	/**
	 * Getter for <code>CONFIGDB.menu_company_*.sequence</code>.
	 */
	public Integer getSequence() {
		return (Integer) get(9);
	}

	/**
	 * Setter for <code>CONFIGDB.menu_company_*.view_yn</code>.
	 */
	public void setViewYn(String value) {
		set(10, value);
	}

	/**
	 * Getter for <code>CONFIGDB.menu_company_*.view_yn</code>.
	 */
	public String getViewYn() {
		return (String) get(10);
	}

	/**
	 * Setter for <code>CONFIGDB.menu_company_*.icon</code>.
	 */
	public void setIcon(String value) {
		set(11, value);
	}

	/**
	 * Getter for <code>CONFIGDB.menu_company_*.icon</code>.
	 */
	public String getIcon() {
		return (String) get(11);
	}

	/**
	 * Setter for <code>CONFIGDB.menu_company_*.action_type</code>.
	 */
	public void setActionType(String value) {
		set(12, value);
	}

	/**
	 * Getter for <code>CONFIGDB.menu_company_*.action_type</code>.
	 */
	public String getActionType() {
		return (String) get(12);
	}

	/**
	 * Setter for <code>CONFIGDB.menu_company_*.auth_type</code>.
	 */
	public void setAuthType(String value) {
		set(13, value);
	}

	/**
	 * Getter for <code>CONFIGDB.menu_company_*.auth_type</code>.
	 */
	public String getAuthType() {
		return (String) get(13);
	}

	/**
	 * Setter for <code>CONFIGDB.menu_company_*.group_level_auth_yn</code>.
	 */
	public void setGroupLevelAuthYn(String value) {
		set(14, value);
	}

	/**
	 * Getter for <code>CONFIGDB.menu_company_*.group_level_auth_yn</code>.
	 */
	public String getGroupLevelAuthYn() {
		return (String) get(14);
	}

	/**
	 * Setter for <code>CONFIGDB.menu_company_*.group_code</code>.
	 */
	public void setGroupCode(String value) {
		set(15, value);
	}

	/**
	 * Getter for <code>CONFIGDB.menu_company_*.group_code</code>.
	 */
	public String getGroupCode() {
		return (String) get(15);
	}

	/**
	 * Setter for <code>CONFIGDB.menu_company_*.group_level</code>.
	 */
	public void setGroupLevel(Integer value) {
		set(16, value);
	}

	/**
	 * Getter for <code>CONFIGDB.menu_company_*.group_level</code>.
	 */
	public Integer getGroupLevel() {
		return (Integer) get(16);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	@Override
	public Record1<Integer> key() {
		return (Record1) super.key();
	}
}
