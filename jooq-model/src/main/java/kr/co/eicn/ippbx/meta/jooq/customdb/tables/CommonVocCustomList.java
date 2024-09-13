package kr.co.eicn.ippbx.meta.jooq.customdb.tables;

import kr.co.eicn.ippbx.meta.jooq.customdb.Customdb;
import kr.co.eicn.ippbx.meta.jooq.customdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.VocCustomListRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommonVocCustomList extends TableImpl<VocCustomListRecord> {
    /**
     * The column <code>CUSTOMDB.voc_custom_list.seq</code>. SEQUENCE KEY
     */
    public final TableField<VocCustomListRecord, Integer> SEQ = createField(DSL.name("seq"), SQLDataType.INTEGER.nullable(false).identity(true), this, "SEQUENCE KEY");

    /**
     * The column <code>CUSTOMDB.voc_custom_list.insert_date</code>.
     */
    public final TableField<VocCustomListRecord, Timestamp> INSERT_DATE = createField(DSL.name("insert_date"), SQLDataType.TIMESTAMP(0).defaultValue(DSL.field("'2020-01-01 00:00:00'", SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.voc_custom_list.voc_group_id</code>. VOC_HAPPYCALL그룹 아이디
     */
    public final TableField<VocCustomListRecord, Integer> VOC_GROUP_ID = createField(DSL.name("voc_group_id"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "VOC_HAPPYCALL그룹 아이디");

    /**
     * The column <code>CUSTOMDB.voc_custom_list.sender</code>. 진행을 결정하는 주체 MEMBER-상담사가 화면에서인서트함 AUTO-자동으로 조건이 맞아서 시스템에서 인서트
     */
    public final TableField<VocCustomListRecord, String> SENDER = createField(DSL.name("sender"), SQLDataType.VARCHAR(10).defaultValue(DSL.field("'MEMBER'", SQLDataType.VARCHAR)), this, "진행을 결정하는 주체 MEMBER-상담사가 화면에서인서트함 AUTO-자동으로 조건이 맞아서 시스템에서 인서트");

    /**
     * The column <code>CUSTOMDB.voc_custom_list.custom_number</code>. HAPPYCALL대상고객전화번호
     */
    public final TableField<VocCustomListRecord, String> CUSTOM_NUMBER = createField(DSL.name("custom_number"), SQLDataType.VARCHAR(30).nullable(false).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "HAPPYCALL대상고객전화번호");

    /**
     * The column <code>CUSTOMDB.voc_custom_list.company_id</code>.
     */
    public final TableField<VocCustomListRecord, String> COMPANY_ID = createField(DSL.name("company_id"), SQLDataType.VARCHAR(30).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    private final String tableName;

    /**
     * Create a <code>CUSTOMDB.voc_custom_list</code> table reference
     */
    public CommonVocCustomList(String companyGroup) {
        this(DSL.name("voc_custom_list_" + companyGroup), null);
    }

    /**
     * Create an aliased <code>CUSTOMDB.voc_custom_list</code> table reference
     */
    public CommonVocCustomList(String alias, Table<VocCustomListRecord> aliased) {
        this(DSL.name(alias), aliased);
    }

    private CommonVocCustomList(Name alias, Table<VocCustomListRecord> aliased) {
        this(alias, aliased, null);
    }

    private CommonVocCustomList(Name alias, Table<VocCustomListRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
        this.tableName = alias.last();
    }

    public <O extends Record> CommonVocCustomList(CommonVocCustomList table, Table<O> child, ForeignKey<O, VocCustomListRecord> key) {
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
    public Identity<VocCustomListRecord, Integer> getIdentity() {
        return Internal.createIdentity(this, this.SEQ);
    }

    @NotNull
    @Override
    public List<UniqueKey<VocCustomListRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ));
    }

    @NotNull
    @Override
    public CommonVocCustomList as(String alias) {
        return new CommonVocCustomList(DSL.name(alias), this);
    }

    @NotNull
    @Override
    public CommonVocCustomList as(Name alias) {
        return new CommonVocCustomList(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonVocCustomList rename(String name) {
        return new CommonVocCustomList(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonVocCustomList rename(Name name) {
        return new CommonVocCustomList(name, null);
    }
}
