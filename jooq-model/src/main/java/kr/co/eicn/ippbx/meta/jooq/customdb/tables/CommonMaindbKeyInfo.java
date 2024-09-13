package kr.co.eicn.ippbx.meta.jooq.customdb.tables;

import kr.co.eicn.ippbx.meta.jooq.customdb.Customdb;
import kr.co.eicn.ippbx.meta.jooq.customdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.MaindbKeyInfoRecord;
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

public class CommonMaindbKeyInfo extends TableImpl<MaindbKeyInfoRecord> {
    /**
     * The column <code>CUSTOMDB.maindb_key_info.key_value</code>.
     */
    public final TableField<MaindbKeyInfoRecord, String> KEY_VALUE = createField(DSL.name("key_value"), SQLDataType.VARCHAR(40).nullable(false).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_key_info.custom_id</code>.
     */
    public final TableField<MaindbKeyInfoRecord, String> CUSTOM_ID = createField(DSL.name("custom_id"), SQLDataType.VARCHAR(100).nullable(false).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_key_info.group_id</code>.
     */
    public final TableField<MaindbKeyInfoRecord, Integer> GROUP_ID = createField(DSL.name("group_id"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    private String tableName;

    /**
     * Create a <code>CUSTOMDB.maindb_key_info</code> table reference
     */
    public CommonMaindbKeyInfo(String companyName) {
        this(DSL.name("maindb_key_info_" + companyName), null);
    }

    /**
     * Create an aliased <code>CUSTOMDB.maindb_key_info</code> table reference
     */
    public CommonMaindbKeyInfo(String alias, Table<MaindbKeyInfoRecord> aliased) {
        this(DSL.name(alias), aliased);
    }

    /**
     * Create an aliased <code>CUSTOMDB.maindb_key_info</code> table reference
     */
    public CommonMaindbKeyInfo(Name alias, Table<MaindbKeyInfoRecord> aliased) {
        this(alias, aliased, null);
    }

    /**
     * Create an aliased <code>CUSTOMDB.maindb_key_info_*</code> table reference
     */
    private CommonMaindbKeyInfo(Name alias, Table<MaindbKeyInfoRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
        this.tableName = alias.last();
    }

    public <O extends Record> CommonMaindbKeyInfo(CommonMaindbKeyInfo table, Table<O> child, ForeignKey<O, MaindbKeyInfoRecord> key) {
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
    public UniqueKey<MaindbKeyInfoRecord> getPrimaryKey() {
        return Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.KEY_VALUE);
    }

    @NotNull
    @Override
    public List<UniqueKey<MaindbKeyInfoRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.KEY_VALUE));
    }

    @NotNull
    @Override
    public CommonMaindbKeyInfo as(String alias) {
        return new CommonMaindbKeyInfo(DSL.name(alias), this);
    }

    @NotNull
    @Override
    public CommonMaindbKeyInfo as(Name alias) {
        return new CommonMaindbKeyInfo(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonMaindbKeyInfo rename(String name) {
        return new CommonMaindbKeyInfo(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonMaindbKeyInfo rename(Name name) {
        return new CommonMaindbKeyInfo(name, null);
    }
}
