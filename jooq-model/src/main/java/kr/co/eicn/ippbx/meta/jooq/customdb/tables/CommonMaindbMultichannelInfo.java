package kr.co.eicn.ippbx.meta.jooq.customdb.tables;

import kr.co.eicn.ippbx.meta.jooq.customdb.Customdb;
import kr.co.eicn.ippbx.meta.jooq.customdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.CommonMaindbMultichannelInfoRecord;
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

public class CommonMaindbMultichannelInfo extends TableImpl<CommonMaindbMultichannelInfoRecord> {
    /**
     * The column <code>CUSTOMDB.maindb_multichannel_info.seq</code>.
     */
    public final TableField<CommonMaindbMultichannelInfoRecord, Integer> SEQ = createField(DSL.name("seq"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_multichannel_info.channel_type</code>.
     */
    public final TableField<CommonMaindbMultichannelInfoRecord, String> CHANNEL_TYPE = createField(DSL.name("channel_type"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_multichannel_info.channel_data</code>.
     */
    public final TableField<CommonMaindbMultichannelInfoRecord, String> CHANNEL_DATA = createField(DSL.name("channel_data"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_multichannel_info.maindb_group_id</code>.
     */
    public final TableField<CommonMaindbMultichannelInfoRecord, Integer> MAINDB_GROUP_ID = createField(DSL.name("maindb_group_id"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_multichannel_info.maindb_custom_id</code>.
     */
    public final TableField<CommonMaindbMultichannelInfoRecord, String> MAINDB_CUSTOM_ID = createField(DSL.name("maindb_custom_id"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_multichannel_info.maindb_custom_name</code>.
     */
    public final TableField<CommonMaindbMultichannelInfoRecord, String> MAINDB_CUSTOM_NAME = createField(DSL.name("maindb_custom_name"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.maindb_multichannel_info.company_id</code>.
     */
    public final TableField<CommonMaindbMultichannelInfoRecord, String> COMPANY_ID = createField(DSL.name("company_id"), SQLDataType.VARCHAR(30).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    private final String tableName;

    /**
     * Create a <code>CUSTOMDB.maindb_multichannel_info_*</code> table reference
     */
    public CommonMaindbMultichannelInfo() {
        this(DSL.name("maindb_multichannel_info"), null);
    }

    public CommonMaindbMultichannelInfo(String companyName) {
        this(DSL.name("maindb_multichannel_info_" + companyName), null);
    }

    /**
     * Create an aliased <code>CUSTOMDB.maindb_multichannel_info_*</code> table reference
     */
    private CommonMaindbMultichannelInfo(String alias, Table<CommonMaindbMultichannelInfoRecord> aliased) {
        this(DSL.name(alias), aliased);
    }

    /**
     * Create an aliased <code>CUSTOMDB.maindb_multichannel_info_*</code> table reference
     */
    public CommonMaindbMultichannelInfo(Name alias, Table<CommonMaindbMultichannelInfoRecord> aliased) {
        this(alias, aliased, null);
    }

    /**
     * Create an aliased <code>CUSTOMDB.maindb_multichannel_info_*</code> table reference
     */
    private CommonMaindbMultichannelInfo(Name alias, Table<CommonMaindbMultichannelInfoRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
        this.tableName = alias.last();
    }

    public <O extends Record> CommonMaindbMultichannelInfo(CommonMaindbMultichannelInfo table, Table<O> child, ForeignKey<O, CommonMaindbMultichannelInfoRecord> key) {
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
    public Identity<CommonMaindbMultichannelInfoRecord, Integer> getIdentity() {
        return Internal.createIdentity(this, this.SEQ);
    }

    @Override
    public UniqueKey<CommonMaindbMultichannelInfoRecord> getPrimaryKey() {
        return Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ);
    }

    @NotNull
    @Override
    public List<UniqueKey<CommonMaindbMultichannelInfoRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ));
    }

    @NotNull
    @Override
    public CommonMaindbMultichannelInfo as(String alias) {
        return new CommonMaindbMultichannelInfo(DSL.name(alias), this);
    }

    @NotNull
    @Override
    public CommonMaindbMultichannelInfo as(Name alias) {
        return new CommonMaindbMultichannelInfo(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonMaindbMultichannelInfo rename(String name) {
        return new CommonMaindbMultichannelInfo(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonMaindbMultichannelInfo rename(Name name) {
        return new CommonMaindbMultichannelInfo(name, null);
    }
}
