package kr.co.eicn.ippbx.meta.jooq.customdb.tables;

import kr.co.eicn.ippbx.meta.jooq.customdb.Customdb;
import kr.co.eicn.ippbx.meta.jooq.customdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.VocResearchResultRecord;
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

public class CommonVocResearchResult extends TableImpl<VocResearchResultRecord> {
    /**
     * The column <code>CUSTOMDB.voc_research_result.seq</code>.
     */
    public final TableField<VocResearchResultRecord, Integer> SEQ = createField(DSL.name("seq"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>CUSTOMDB.voc_research_result.company_id</code>.
     */
    public final TableField<VocResearchResultRecord, String> COMPANY_ID = createField(DSL.name("company_id"), SQLDataType.VARCHAR(30).nullable(false).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.voc_research_result.result_date</code>.
     */
    public final TableField<VocResearchResultRecord, Timestamp> RESULT_DATE = createField(DSL.name("result_date"), SQLDataType.TIMESTAMP(0).nullable(false).defaultValue(DSL.field("'2009-12-31 15:00:00'", SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>CUSTOMDB.voc_research_result.uniqueid</code>.
     */
    public final TableField<VocResearchResultRecord, String> UNIQUEID = createField(DSL.name("uniqueid"), SQLDataType.VARCHAR(50).nullable(false).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.voc_research_result.hangup_cause</code>.
     */
    public final TableField<VocResearchResultRecord, String> HANGUP_CAUSE = createField(DSL.name("hangup_cause"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.voc_research_result.hangup_msg</code>.
     */
    public final TableField<VocResearchResultRecord, String> HANGUP_MSG = createField(DSL.name("hangup_msg"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.voc_research_result.billsec</code>.
     */
    public final TableField<VocResearchResultRecord, Integer> BILLSEC = createField(DSL.name("billsec"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.voc_research_result.click_key</code>.
     */
    public final TableField<VocResearchResultRecord, String> CLICK_KEY = createField(DSL.name("click_key"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.voc_research_result.custom_number</code>.
     */
    public final TableField<VocResearchResultRecord, String> CUSTOM_NUMBER = createField(DSL.name("custom_number"), SQLDataType.VARCHAR(50).nullable(false).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.voc_research_result.voc_group_id</code>.
     */
    public final TableField<VocResearchResultRecord, Integer> VOC_GROUP_ID = createField(DSL.name("voc_group_id"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.voc_research_result.userid</code>.
     */
    public final TableField<VocResearchResultRecord, String> USERID = createField(DSL.name("userid"), SQLDataType.VARCHAR(30).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.voc_research_result.extension</code>.
     */
    public final TableField<VocResearchResultRecord, String> EXTENSION = createField(DSL.name("extension"), SQLDataType.VARCHAR(30).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.voc_research_result.research_id</code>.
     */
    public final TableField<VocResearchResultRecord, Integer> RESEARCH_ID = createField(DSL.name("research_id"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>CUSTOMDB.voc_research_result.tree_path</code>.
     */
    public final TableField<VocResearchResultRecord, String> TREE_PATH = createField(DSL.name("tree_path"), SQLDataType.VARCHAR(800).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.voc_research_result.my_path_1</code>.
     */
    public final TableField<VocResearchResultRecord, String> MY_PATH_1 = createField(DSL.name("my_path_1"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.voc_research_result.my_path_2</code>.
     */
    public final TableField<VocResearchResultRecord, String> MY_PATH_2 = createField(DSL.name("my_path_2"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.voc_research_result.my_path_3</code>.
     */
    public final TableField<VocResearchResultRecord, String> MY_PATH_3 = createField(DSL.name("my_path_3"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.voc_research_result.my_path_4</code>.
     */
    public final TableField<VocResearchResultRecord, String> MY_PATH_4 = createField(DSL.name("my_path_4"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.voc_research_result.my_path_5</code>.
     */
    public final TableField<VocResearchResultRecord, String> MY_PATH_5 = createField(DSL.name("my_path_5"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.voc_research_result.my_path_6</code>.
     */
    public final TableField<VocResearchResultRecord, String> MY_PATH_6 = createField(DSL.name("my_path_6"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.voc_research_result.my_path_7</code>.
     */
    public final TableField<VocResearchResultRecord, String> MY_PATH_7 = createField(DSL.name("my_path_7"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.voc_research_result.my_path_8</code>.
     */
    public final TableField<VocResearchResultRecord, String> MY_PATH_8 = createField(DSL.name("my_path_8"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.voc_research_result.my_path_9</code>.
     */
    public final TableField<VocResearchResultRecord, String> MY_PATH_9 = createField(DSL.name("my_path_9"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.voc_research_result.my_path_10</code>.
     */
    public final TableField<VocResearchResultRecord, String> MY_PATH_10 = createField(DSL.name("my_path_10"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.voc_research_result.my_path_11</code>.
     */
    public final TableField<VocResearchResultRecord, String> MY_PATH_11 = createField(DSL.name("my_path_11"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.voc_research_result.my_path_12</code>.
     */
    public final TableField<VocResearchResultRecord, String> MY_PATH_12 = createField(DSL.name("my_path_12"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.voc_research_result.my_path_13</code>.
     */
    public final TableField<VocResearchResultRecord, String> MY_PATH_13 = createField(DSL.name("my_path_13"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.voc_research_result.my_path_14</code>.
     */
    public final TableField<VocResearchResultRecord, String> MY_PATH_14 = createField(DSL.name("my_path_14"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.voc_research_result.my_path_15</code>.
     */
    public final TableField<VocResearchResultRecord, String> MY_PATH_15 = createField(DSL.name("my_path_15"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.voc_research_result.my_path_16</code>.
     */
    public final TableField<VocResearchResultRecord, String> MY_PATH_16 = createField(DSL.name("my_path_16"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.voc_research_result.my_path_17</code>.
     */
    public final TableField<VocResearchResultRecord, String> MY_PATH_17 = createField(DSL.name("my_path_17"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.voc_research_result.my_path_18</code>.
     */
    public final TableField<VocResearchResultRecord, String> MY_PATH_18 = createField(DSL.name("my_path_18"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.voc_research_result.my_path_19</code>.
     */
    public final TableField<VocResearchResultRecord, String> MY_PATH_19 = createField(DSL.name("my_path_19"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.voc_research_result.my_path_20</code>.
     */
    public final TableField<VocResearchResultRecord, String> MY_PATH_20 = createField(DSL.name("my_path_20"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    private final String tableName;

    /**
     * Create a <code>CUSTOMDB.voc_research_result</code> table reference
     */
    public CommonVocResearchResult(String companyGroup) {
        this(DSL.name("voc_research_result_" + companyGroup), null);
    }

    /**
     * Create an aliased <code>CUSTOMDB.voc_research_result</code> table reference
     */
    public CommonVocResearchResult(String alias, Table<VocResearchResultRecord> aliased) {
        this(DSL.name(alias), aliased);
    }

    private CommonVocResearchResult(Name alias, Table<VocResearchResultRecord> aliased) {
        this(alias, aliased, null);
    }

    private CommonVocResearchResult(Name alias, Table<VocResearchResultRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
        this.tableName = alias.last();
    }

    public <O extends Record> CommonVocResearchResult(CommonVocResearchResult table, Table<O> child, ForeignKey<O, VocResearchResultRecord> key) {
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
        return Arrays.<Index>asList(Indexes.VOC_RESEARCH_RESULT_COMPANY_ID, Indexes.VOC_RESEARCH_RESULT_MY_PATH_1, Indexes.VOC_RESEARCH_RESULT_MY_PATH_10, Indexes.VOC_RESEARCH_RESULT_MY_PATH_11, Indexes.VOC_RESEARCH_RESULT_MY_PATH_12, Indexes.VOC_RESEARCH_RESULT_MY_PATH_13, Indexes.VOC_RESEARCH_RESULT_MY_PATH_14, Indexes.VOC_RESEARCH_RESULT_MY_PATH_15, Indexes.VOC_RESEARCH_RESULT_MY_PATH_16, Indexes.VOC_RESEARCH_RESULT_MY_PATH_17, Indexes.VOC_RESEARCH_RESULT_MY_PATH_18, Indexes.VOC_RESEARCH_RESULT_MY_PATH_19, Indexes.VOC_RESEARCH_RESULT_MY_PATH_2, Indexes.VOC_RESEARCH_RESULT_MY_PATH_20, Indexes.VOC_RESEARCH_RESULT_MY_PATH_3, Indexes.VOC_RESEARCH_RESULT_MY_PATH_4, Indexes.VOC_RESEARCH_RESULT_MY_PATH_5, Indexes.VOC_RESEARCH_RESULT_MY_PATH_6, Indexes.VOC_RESEARCH_RESULT_MY_PATH_7, Indexes.VOC_RESEARCH_RESULT_MY_PATH_8, Indexes.VOC_RESEARCH_RESULT_MY_PATH_9, Indexes.VOC_RESEARCH_RESULT_RESEARCH_ID);
    }

    @Override
    public Identity<VocResearchResultRecord, Integer> getIdentity() {
        return Internal.createIdentity(this, this.SEQ);
    }

    @Override
    public UniqueKey<VocResearchResultRecord> getPrimaryKey() {
        return Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ);
    }

    @NotNull
    @Override
    public List<UniqueKey<VocResearchResultRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ));
    }

    @NotNull
    @Override
    public CommonVocResearchResult as(String alias) {
        return new CommonVocResearchResult(DSL.name(alias), this);
    }

    @NotNull
    @Override
    public CommonVocResearchResult as(Name alias) {
        return new CommonVocResearchResult(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonVocResearchResult rename(String name) {
        return new CommonVocResearchResult(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonVocResearchResult rename(Name name) {
        return new CommonVocResearchResult(name, null);
    }
}
