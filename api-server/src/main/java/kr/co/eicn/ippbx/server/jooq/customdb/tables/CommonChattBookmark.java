package kr.co.eicn.ippbx.server.jooq.customdb.tables;

import kr.co.eicn.ippbx.server.jooq.customdb.Customdb;
import kr.co.eicn.ippbx.server.jooq.customdb.Indexes;
import kr.co.eicn.ippbx.server.jooq.customdb.Keys;
import kr.co.eicn.ippbx.server.jooq.customdb.tables.records.ChattBookmarkRecord;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import java.util.Arrays;
import java.util.List;

public class CommonChattBookmark extends TableImpl<ChattBookmarkRecord> {

    public static final ChattBookmark CHATT_BOOKMARK = new ChattBookmark();

    /**
     * The column <code>CUSTOMDB.chatt_bookmark.seq</code>.
     */
    public final TableField<ChattBookmarkRecord, Integer> SEQ = createField(DSL.name("seq"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_bookmark.userid</code>.
     */
    public final TableField<ChattBookmarkRecord, String> USERID = createField(DSL.name("userid"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.chatt_bookmark.memberid</code>.
     */
    public final TableField<ChattBookmarkRecord, String> MEMBERID = createField(DSL.name("memberid"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    private String tableName;

    /**
     * Create a <code>CUSTOMDB.chatt_bookmark</code> table reference
     */
    public CommonChattBookmark(String companyName) {
        this(DSL.name("chatt_bookmark_" + companyName), null);
    }

    private CommonChattBookmark(Name alias, Table<ChattBookmarkRecord> aliased) {
        this(alias, aliased, null);
    }

    private CommonChattBookmark(Name alias, Table<ChattBookmarkRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
        this.tableName = alias.last();
    }

    public <O extends Record> CommonChattBookmark(CommonChattBookmark table, Table<O> child, ForeignKey<O, ChattBookmarkRecord> key) {
        super(child, key, table);
        this.tableName = table.getName();
    }

    @Override
    public Schema getSchema() {
        return Customdb.CUSTOMDB;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.CHATT_BOOKMARK_SEQ);
    }

    @Override
    public Identity<ChattBookmarkRecord, Integer> getIdentity() {
        return Keys.IDENTITY_CHATT_BOOKMARK;
    }

    @Override
    public List<UniqueKey<ChattBookmarkRecord>> getKeys() {
        return Arrays.<UniqueKey<ChattBookmarkRecord>>asList(Keys.KEY_CHATT_BOOKMARK_SEQ);
    }

    @Override
    public CommonChattBookmark as(String alias) {
        return new CommonChattBookmark(DSL.name(alias), this);
    }

    @Override
    public CommonChattBookmark as(Name alias) {
        return new CommonChattBookmark(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonChattBookmark rename(String name) {
        return new CommonChattBookmark(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonChattBookmark rename(Name name) {
        return new CommonChattBookmark(name, null);
    }
}
