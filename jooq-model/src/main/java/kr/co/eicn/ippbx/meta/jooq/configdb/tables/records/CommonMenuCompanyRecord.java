package kr.co.eicn.ippbx.meta.jooq.configdb.tables.records;

import org.jooq.Record1;
import org.jooq.Table;
import org.jooq.impl.UpdatableRecordImpl;

public class CommonMenuCompanyRecord extends UpdatableRecordImpl<CommonMenuCompanyRecord> {

    public CommonMenuCompanyRecord(Table<CommonMenuCompanyRecord> table) {
        super(table);
    }

    /**
     * Setter for <code>CONFIGDB.menu_company.seq</code>.
     */
    public void setSeq(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>CONFIGDB.menu_company.seq</code>.
     */
    public Integer getSeq() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>CONFIGDB.menu_company.userid</code>. 사용자 아이디
     */
    public void setUserid(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>CONFIGDB.menu_company.userid</code>. 사용자 아이디
     */
    public String getUserid() {
        return (String) get(1);
    }

    /**
     * Setter for <code>CONFIGDB.menu_company.menu_code</code>. 메뉴코드
     */
    public void setMenuCode(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>CONFIGDB.menu_company.menu_code</code>. 메뉴코드
     */
    public String getMenuCode() {
        return (String) get(2);
    }

    /**
     * Setter for <code>CONFIGDB.menu_company.menu_name</code>. 메뉴명
     */
    public void setMenuName(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>CONFIGDB.menu_company.menu_name</code>. 메뉴명
     */
    public String getMenuName() {
        return (String) get(3);
    }

    /**
     * Setter for <code>CONFIGDB.menu_company.menu_tree_name</code>. 메뉴 트리
     */
    public void setMenuTreeName(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>CONFIGDB.menu_company.menu_tree_name</code>. 메뉴 트리
     */
    public String getMenuTreeName() {
        return (String) get(4);
    }

    /**
     * Setter for <code>CONFIGDB.menu_company.menu_level</code>. 메뉴 단계
     */
    public void setMenuLevel(Integer value) {
        set(5, value);
    }

    /**
     * Getter for <code>CONFIGDB.menu_company.menu_level</code>. 메뉴 단계
     */
    public Integer getMenuLevel() {
        return (Integer) get(5);
    }

    /**
     * Setter for <code>CONFIGDB.menu_company.parent_menu_code</code>. 상위 메뉴 코드
     */
    public void setParentMenuCode(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>CONFIGDB.menu_company.parent_menu_code</code>. 상위 메뉴 코드
     */
    public String getParentMenuCode() {
        return (String) get(6);
    }

    /**
     * Setter for <code>CONFIGDB.menu_company.parent_tree_name</code>. 상위 메뉴 트리
     */
    public void setParentTreeName(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>CONFIGDB.menu_company.parent_tree_name</code>. 상위 메뉴 트리
     */
    public String getParentTreeName() {
        return (String) get(7);
    }

    /**
     * Setter for <code>CONFIGDB.menu_company.menu_action_exe_id</code>. 연결액션(링크)
     */
    public void setMenuActionExeId(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>CONFIGDB.menu_company.menu_action_exe_id</code>. 연결액션(링크)
     */
    public String getMenuActionExeId() {
        return (String) get(8);
    }

    /**
     * Setter for <code>CONFIGDB.menu_company.sequence</code>. 정렬순서
     */
    public void setSequence(Integer value) {
        set(9, value);
    }

    /**
     * Getter for <code>CONFIGDB.menu_company.sequence</code>. 정렬순서
     */
    public Integer getSequence() {
        return (Integer) get(9);
    }

    /**
     * Setter for <code>CONFIGDB.menu_company.view_yn</code>. 보임여부
     */
    public void setViewYn(String value) {
        set(10, value);
    }

    /**
     * Getter for <code>CONFIGDB.menu_company.view_yn</code>. 보임여부
     */
    public String getViewYn() {
        return (String) get(10);
    }

    /**
     * Setter for <code>CONFIGDB.menu_company.icon</code>. 메뉴 아이콘
     */
    public void setIcon(String value) {
        set(11, value);
    }

    /**
     * Getter for <code>CONFIGDB.menu_company.icon</code>. 메뉴 아이콘
     */
    public String getIcon() {
        return (String) get(11);
    }

    /**
     * Setter for <code>CONFIGDB.menu_company.action_type</code>. 연결액션
     */
    public void setActionType(String value) {
        set(12, value);
    }

    /**
     * Getter for <code>CONFIGDB.menu_company.action_type</code>. 연결액션
     */
    public String getActionType() {
        return (String) get(12);
    }

    /**
     * Setter for <code>CONFIGDB.menu_company.auth_type</code>. 권한타입
     */
    public void setAuthType(String value) {
        set(13, value);
    }

    /**
     * Getter for <code>CONFIGDB.menu_company.auth_type</code>. 권한타입
     */
    public String getAuthType() {
        return (String) get(13);
    }

    /**
     * Setter for <code>CONFIGDB.menu_company.group_level_auth_yn</code>. 조직권한부여여부
     */
    public void setGroupLevelAuthYn(String value) {
        set(14, value);
    }

    /**
     * Getter for <code>CONFIGDB.menu_company.group_level_auth_yn</code>. 조직권한부여여부
     */
    public String getGroupLevelAuthYn() {
        return (String) get(14);
    }

    /**
     * Setter for <code>CONFIGDB.menu_company.group_code</code>. 조직코드
     */
    public void setGroupCode(String value) {
        set(15, value);
    }

    /**
     * Getter for <code>CONFIGDB.menu_company.group_code</code>. 조직코드
     */
    public String getGroupCode() {
        return (String) get(15);
    }

    /**
     * Setter for <code>CONFIGDB.menu_company.group_level</code>. 조직레벨
     */
    public void setGroupLevel(Integer value) {
        set(16, value);
    }

    /**
     * Getter for <code>CONFIGDB.menu_company.group_level</code>. 조직레벨
     */
    public Integer getGroupLevel() {
        return (Integer) get(16);
    }

    /**
     * Setter for <code>CONFIGDB.menu_company.service_kind</code>. 구축유형
     */
    public void setServiceKind(String value) {
        set(17, value);
    }

    /**
     * Getter for <code>CONFIGDB.menu_company.service_kind</code>. 구축유형
     */
    public String getServiceKind() {
        return (String) get(17);
    }

    /**
     * Setter for <code>CONFIGDB.menu_company.service</code>. 관련서비스
     */
    public void setService(String value) {
        set(18, value);
    }

    /**
     * Getter for <code>CONFIGDB.menu_company.service</code>. 관련서비스
     */
    public String getService() {
        return (String) get(18);
    }

    /**
     * Setter for <code>CONFIGDB.menu_company.solution</code>. 솔루션유형
     */
    public void setSolution(String value) {
        set(19, value);
    }

    /**
     * Getter for <code>CONFIGDB.menu_company.solution</code>. 솔루션유형
     */
    public String getSolution() {
        return (String) get(19);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }
}
