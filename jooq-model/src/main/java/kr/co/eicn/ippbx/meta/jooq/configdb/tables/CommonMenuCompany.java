package kr.co.eicn.ippbx.meta.jooq.configdb.tables;

import kr.co.eicn.ippbx.meta.jooq.configdb.Configdb;
import kr.co.eicn.ippbx.meta.jooq.configdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.configdb.tables.records.CommonMenuCompanyRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommonMenuCompany extends TableImpl<CommonMenuCompanyRecord> {
    /**
     * The column <code>CONFIGDB.menu_company.seq</code>.
     */
    public final TableField<CommonMenuCompanyRecord, Integer> SEQ = createField(DSL.name("seq"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>CONFIGDB.menu_company.userid</code>. 사용자 아이디
     */
    public final TableField<CommonMenuCompanyRecord, String> USERID = createField(DSL.name("userid"), SQLDataType.VARCHAR(30).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "사용자 아이디");

    /**
     * The column <code>CONFIGDB.menu_company.menu_code</code>. 메뉴코드
     */
    public final TableField<CommonMenuCompanyRecord, String> MENU_CODE = createField(DSL.name("menu_code"), SQLDataType.CHAR(4).defaultValue(DSL.field("''", SQLDataType.CHAR)), this, "메뉴코드");

    /**
     * The column <code>CONFIGDB.menu_company.menu_name</code>. 메뉴명
     */
    public final TableField<CommonMenuCompanyRecord, String> MENU_NAME = createField(DSL.name("menu_name"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "메뉴명");

    /**
     * The column <code>CONFIGDB.menu_company.menu_tree_name</code>. 메뉴 트리
     */
    public final TableField<CommonMenuCompanyRecord, String> MENU_TREE_NAME = createField(DSL.name("menu_tree_name"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "메뉴 트리");

    /**
     * The column <code>CONFIGDB.menu_company.menu_level</code>. 메뉴 단계
     */
    public final TableField<CommonMenuCompanyRecord, Integer> MENU_LEVEL = createField(DSL.name("menu_level"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "메뉴 단계");

    /**
     * The column <code>CONFIGDB.menu_company.parent_menu_code</code>. 상위 메뉴 코드
     */
    public final TableField<CommonMenuCompanyRecord, String> PARENT_MENU_CODE = createField(DSL.name("parent_menu_code"), SQLDataType.CHAR(4).defaultValue(DSL.field("''", SQLDataType.CHAR)), this, "상위 메뉴 코드");

    /**
     * The column <code>CONFIGDB.menu_company.parent_tree_name</code>. 상위 메뉴 트리
     */
    public final TableField<CommonMenuCompanyRecord, String> PARENT_TREE_NAME = createField(DSL.name("parent_tree_name"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "상위 메뉴 트리");

    /**
     * The column <code>CONFIGDB.menu_company.menu_action_exe_id</code>. 연결액션(링크)
     */
    public final TableField<CommonMenuCompanyRecord, String> MENU_ACTION_EXE_ID = createField(DSL.name("menu_action_exe_id"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "연결액션(링크)");

    /**
     * The column <code>CONFIGDB.menu_company.sequence</code>. 정렬순서
     */
    public final TableField<CommonMenuCompanyRecord, Integer> SEQUENCE = createField(DSL.name("sequence"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "정렬순서");

    /**
     * The column <code>CONFIGDB.menu_company.view_yn</code>. 보임여부
     */
    public final TableField<CommonMenuCompanyRecord, String> VIEW_YN = createField(DSL.name("view_yn"), SQLDataType.CHAR(1).defaultValue(DSL.field("''", SQLDataType.CHAR)), this, "보임여부");

    /**
     * The column <code>CONFIGDB.menu_company.icon</code>. 메뉴 아이콘
     */
    public final TableField<CommonMenuCompanyRecord, String> ICON = createField(DSL.name("icon"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "메뉴 아이콘");

    /**
     * The column <code>CONFIGDB.menu_company.action_type</code>. 연결액션
     */
    public final TableField<CommonMenuCompanyRecord, String> ACTION_TYPE = createField(DSL.name("action_type"), SQLDataType.VARCHAR(10).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "연결액션");

    /**
     * The column <code>CONFIGDB.menu_company.auth_type</code>. 권한타입
     */
    public final TableField<CommonMenuCompanyRecord, String> AUTH_TYPE = createField(DSL.name("auth_type"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "권한타입");

    /**
     * The column <code>CONFIGDB.menu_company.group_level_auth_yn</code>. 조직권한부여여부
     */
    public final TableField<CommonMenuCompanyRecord, String> GROUP_LEVEL_AUTH_YN = createField(DSL.name("group_level_auth_yn"), SQLDataType.CHAR(1).defaultValue(DSL.field("''", SQLDataType.CHAR)), this, "조직권한부여여부");

    /**
     * The column <code>CONFIGDB.menu_company.group_code</code>. 조직코드
     */
    public final TableField<CommonMenuCompanyRecord, String> GROUP_CODE = createField(DSL.name("group_code"), SQLDataType.CHAR(4).defaultValue(DSL.field("''", SQLDataType.CHAR)), this, "조직코드");

    /**
     * The column <code>CONFIGDB.menu_company.group_level</code>. 조직레벨
     */
    public final TableField<CommonMenuCompanyRecord, Integer> GROUP_LEVEL = createField(DSL.name("group_level"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "조직레벨");

    /**
     * The column <code>CONFIGDB.menu_company.service_kind</code>. 구축유형
     */
    public final TableField<CommonMenuCompanyRecord, String> SERVICE_KIND = createField(DSL.name("service_kind"), SQLDataType.VARCHAR(10).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "구축유형");

    /**
     * The column <code>CONFIGDB.menu_company.service</code>. 관련서비스
     */
    public final TableField<CommonMenuCompanyRecord, String> SERVICE = createField(DSL.name("service"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "관련서비스");

    /**
     * The column <code>CONFIGDB.menu_company.solution</code>. 솔루션유형
     */
    public final TableField<CommonMenuCompanyRecord, String> SOLUTION = createField(DSL.name("solution"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "솔루션유형");

    private final String tableName;

    public CommonMenuCompany(String companyName) {
        this(DSL.name("menu_company_" + companyName), null);
    }

    /**
     * Create an aliased <code>CONFIGDB.menu_company</code> table reference
     */
    public CommonMenuCompany(String alias, Table<CommonMenuCompanyRecord> aliased) {
        this(DSL.name(alias), aliased);
    }

    /**
     * Create an aliased <code>CONFIGDB.menu_company</code> table reference
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
    public Schema getSchema() {
        return Configdb.CONFIGDB;
    }

    @NotNull
    @Override
    public List<Index> getIndexes() {
        return Arrays.asList();
    }

    @Override
    public Identity<CommonMenuCompanyRecord, Integer> getIdentity() {
        return Internal.createIdentity(this, this.SEQ);
    }

    @Override
    public UniqueKey<CommonMenuCompanyRecord> getPrimaryKey() {
        return Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ);
    }

    @NotNull
    @Override
    public List<UniqueKey<CommonMenuCompanyRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ));
    }

    @NotNull
    @Override
    public CommonMenuCompany as(String alias) {
        return new CommonMenuCompany(DSL.name(alias), this);
    }

    @NotNull
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
}
