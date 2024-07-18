package kr.co.eicn.ippbx.meta.jooq.customdb.tables;

import kr.co.eicn.ippbx.meta.jooq.customdb.Customdb;
import kr.co.eicn.ippbx.meta.jooq.customdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.MemoRecord;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommonMemo extends TableImpl<MemoRecord> {
    /**
     * The column <code>CUSTOMDB.memo.id</code>. 식별자
     */
    public final TableField<MemoRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "식별자");

    /**
     * The column <code>CUSTOMDB.memo.company_id</code>. 회사
     */
    public final TableField<MemoRecord, String> COMPANY_ID = createField(DSL.name("company_id"), SQLDataType.VARCHAR(30).nullable(false), this, "회사");

    /**
     * The column <code>CUSTOMDB.memo.content</code>. 메모
     */
    public final TableField<MemoRecord, String> CONTENT = createField(DSL.name("content"), SQLDataType.CLOB.defaultValue(DSL.inline("NULL", SQLDataType.CLOB)), this, "메모");

    /**
     * The column <code>CUSTOMDB.memo.user</code>. 작성자
     */
    public final TableField<MemoRecord, String> USER = createField(DSL.name("user"), SQLDataType.VARCHAR(100).defaultValue(DSL.inline("NULL", SQLDataType.VARCHAR)), this, "작성자");

    /**
     * The column <code>CUSTOMDB.memo.created_at</code>. 생성시각
     */
    public final TableField<MemoRecord, Timestamp> CREATED_AT = createField(DSL.name("created_at"), SQLDataType.TIMESTAMP(0).nullable(false).defaultValue(DSL.field("current_timestamp()", SQLDataType.TIMESTAMP)), this, "생성시각");

    /**
     * The column <code>CUSTOMDB.memo.updated_at</code>. 갱신시각
     */
    public final TableField<MemoRecord, Timestamp> UPDATED_AT = createField(DSL.name("updated_at"), SQLDataType.TIMESTAMP(0).defaultValue(DSL.inline("NULL", SQLDataType.TIMESTAMP)), this, "갱신시각");

    /**
     * The column <code>CUSTOMDB.memo.deleted_at</code>. 삭제시각
     */
    public final TableField<MemoRecord, Timestamp> DELETED_AT = createField(DSL.name("deleted_at"), SQLDataType.TIMESTAMP(0).defaultValue(DSL.inline("NULL", SQLDataType.TIMESTAMP)), this, "삭제시각");

    /**
     * The column <code>CUSTOMDB.memo.title</code>. 북마크된
     */
    public final TableField<MemoRecord, String> TITLE = createField(DSL.name("title"), SQLDataType.VARCHAR(128).nullable(false), this, "북마크된");

    /**
     * The column <code>CUSTOMDB.memo.bookmarked</code>. 북마크된
     */
    public final TableField<MemoRecord, Boolean> BOOKMARKED = createField(DSL.name("bookmarked"), SQLDataType.BOOLEAN.nullable(false).defaultValue(DSL.inline("0", SQLDataType.BOOLEAN)), this, "북마크된");

    private String tableName;

    /**
     * Create a <code>CUSTOMDB.talk_room</code> table reference
     */
    public CommonMemo(String tableName) {
        this(DSL.name("memo_" + tableName), null);
    }

    private CommonMemo(Name alias, Table<MemoRecord> aliased) {
        this(alias, aliased, null);
    }

    private CommonMemo(Name alias, Table<MemoRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
        this.tableName = alias.last();
    }

    public <O extends Record> CommonMemo(CommonMemo table, Table<O> child, ForeignKey<O, MemoRecord> key) {
        super(child, key, table);
        this.tableName = table.getName();
    }

    /**
     * The class holding records for this type
     */
    @Override
    public Class<MemoRecord> getRecordType() {
        return MemoRecord.class;
    }

    @Override
    public Schema getSchema() {
        return Customdb.CUSTOMDB;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.asList(Indexes.MEMO_COMPANY_ID, Indexes.MEMO_USER);
    }

    @Override
    public Identity<MemoRecord, Integer> getIdentity() {
        return (Identity<MemoRecord, Integer>) super.getIdentity();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<MemoRecord> getPrimaryKey() {
        return org.jooq.impl.Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.ID);
    }

    @Override
    public List<UniqueKey<MemoRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.ID));
    }

    @Override
    public CommonMemo as(String alias) {
        return new CommonMemo(DSL.name(alias), this);
    }

    @Override
    public CommonMemo as(Name alias) {
        return new CommonMemo(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonMemo rename(String name) {
        return new CommonMemo(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonMemo rename(Name name) {
        return new CommonMemo(name, null);
    }
}
