package kr.co.eicn.ippbx.meta.jooq.customdb.tables;

import kr.co.eicn.ippbx.meta.jooq.customdb.Customdb;
import kr.co.eicn.ippbx.meta.jooq.customdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.customdb.Keys;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.ProhibitedKeywordRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

public class CommonProhibitedKeyword extends TableImpl<ProhibitedKeywordRecord> {
    /**
     * The column <code>CUSTOMDB.prohibited_keyword.keyword</code>. 키워드
     */
    public final TableField<ProhibitedKeywordRecord, String> KEYWORD = createField(DSL.name("keyword"), SQLDataType.VARCHAR(100).nullable(false), this, "키워드");

    /**
     * The column <code>CUSTOMDB.prohibited_keyword.creator</code>. 작성자
     */
    public final TableField<ProhibitedKeywordRecord, String> CREATOR = createField(DSL.name("creator"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("NULL", SQLDataType.VARCHAR)), this, "작성자");

    /**
     * The column <code>CUSTOMDB.prohibited_keyword.created_at</code>. 생성시각
     */
    public final TableField<ProhibitedKeywordRecord, Timestamp> CREATED_AT = createField(DSL.name("created_at"), SQLDataType.TIMESTAMP(0).nullable(false).defaultValue(DSL.field("current_timestamp()", SQLDataType.TIMESTAMP)), this, "생성시각");

    /**
     * The column <code>CUSTOMDB.prohibited_keyword.prohibit_yn</code>. 금지어여부
     */
    public final TableField<ProhibitedKeywordRecord, String> PROHIBIT_YN = createField(DSL.name("prohibit_yn"), SQLDataType.CHAR(1).defaultValue(DSL.field("'N'", SQLDataType.CHAR)), this, "금지어여부");

    /**
     * The column <code>CUSTOMDB.prohibited_keyword.keyword_yn</code>. 어드민등록키워드여부
     */
    public final TableField<ProhibitedKeywordRecord, String> KEYWORD_YN = createField(DSL.name("keyword_yn"), SQLDataType.CHAR(1).defaultValue(DSL.field("'N'", SQLDataType.CHAR)), this, "어드민등록키워드여부");

    /**
     * The column <code>CUSTOMDB.prohibited_keyword.kmshashtag_yn</code>. KMS해쉬태그여부
     */
    public final TableField<ProhibitedKeywordRecord, String> KMSHASHTAG_YN = createField(DSL.name("kmshashtag_yn"), SQLDataType.CHAR(1).defaultValue(DSL.field("'N'", SQLDataType.CHAR)), this, "KMS해쉬태그여부");

    /**
     * The column <code>CUSTOMDB.prohibited_keyword.company_id</code>. 회사
     */
    public final TableField<ProhibitedKeywordRecord, String> COMPANY_ID = createField(DSL.name("company_id"), SQLDataType.VARCHAR(30).nullable(false), this, "회사");

    private String tableName;

    /**
     * Create a <code>CUSTOMDB.talk_room</code> table reference
     */
    public CommonProhibitedKeyword(String tableName) {
        this(DSL.name("prohibited_keyword_" + tableName), null);
    }

    private CommonProhibitedKeyword(Name alias, Table<ProhibitedKeywordRecord> aliased) {
        this(alias, aliased, null);
    }

    private CommonProhibitedKeyword(Name alias, Table<ProhibitedKeywordRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
        this.tableName = alias.last();
    }

    public <O extends Record> CommonProhibitedKeyword(CommonProhibitedKeyword table, Table<O> child, ForeignKey<O, ProhibitedKeywordRecord> key) {
        super(child, key, table);
        this.tableName = table.getName();
    }

    @Override
    public Schema getSchema() {
        return Customdb.CUSTOMDB;
    }

    @NotNull
    @Override
    public List<Index> getIndexes() {
        return Arrays.asList();
    }

    @Override
    public UniqueKey<ProhibitedKeywordRecord> getPrimaryKey() {
        return Keys.KEY_PROHIBITED_KEYWORD_PRIMARY;
    }

    @NotNull
    @Override
    public List<UniqueKey<ProhibitedKeywordRecord>> getKeys() {
        return Arrays.<UniqueKey<ProhibitedKeywordRecord>>asList(Keys.KEY_PROHIBITED_KEYWORD_PRIMARY);
    }

    @NotNull
    @Override
    public CommonProhibitedKeyword as(String alias) {
        return new CommonProhibitedKeyword(DSL.name(alias), this);
    }

    @NotNull
    @Override
    public CommonProhibitedKeyword as(Name alias) {
        return new CommonProhibitedKeyword(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonProhibitedKeyword rename(String name) {
        return new CommonProhibitedKeyword(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonProhibitedKeyword rename(Name name) {
        return new CommonProhibitedKeyword(name, null);
    }
}
