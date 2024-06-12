package kr.co.eicn.ippbx.meta.jooq.customdb.tables;

import kr.co.eicn.ippbx.meta.jooq.customdb.Customdb;
import kr.co.eicn.ippbx.meta.jooq.customdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.customdb.Keys;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.VocResearchResultRecord;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

public class CommonVocResearchResult extends TableImpl<VocResearchResultRecord> {

    /**
     * The reference instance of <code>CUSTOMDB.voc_research_result</code>
     */
    public static final VocResearchResult VOC_RESEARCH_RESULT = new VocResearchResult();
    /**
     * The column <code>CUSTOMDB.voc_research_result.seq</code>.
     */
    public final TableField<VocResearchResultRecord, Integer> SEQ = createField(DSL.name("seq"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "");
    /**
     * The column <code>CUSTOMDB.voc_research_result.company_id</code>.
     */
    public final TableField<VocResearchResultRecord, String> COMPANY_ID = createField(DSL.name("company_id"), org.jooq.impl.SQLDataType.VARCHAR(30).nullable(false).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>CUSTOMDB.voc_research_result.result_date</code>.
     */
    public final TableField<VocResearchResultRecord, Timestamp> RESULT_DATE = createField(DSL.name("result_date"), org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(org.jooq.impl.DSL.inline("2009-12-31 15:00:00", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");
    /**
     * The column <code>CUSTOMDB.voc_research_result.uniqueid</code>.
     */
    public final TableField<VocResearchResultRecord, String> UNIQUEID = createField(DSL.name("uniqueid"), org.jooq.impl.SQLDataType.VARCHAR(50).nullable(false).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>CUSTOMDB.voc_research_result.hangup_cause</code>.
     */
    public final TableField<VocResearchResultRecord, String> HANGUP_CAUSE = createField(DSL.name("hangup_cause"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>CUSTOMDB.voc_research_result.hangup_msg</code>.
     */
    public final TableField<VocResearchResultRecord, String> HANGUP_MSG = createField(DSL.name("hangup_msg"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>CUSTOMDB.voc_research_result.billsec</code>.
     */
    public final TableField<VocResearchResultRecord, Integer> BILLSEC = createField(DSL.name("billsec"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");
    /**
     * The column <code>CUSTOMDB.voc_research_result.click_key</code>.
     */
    public final TableField<VocResearchResultRecord, String> CLICK_KEY = createField(DSL.name("click_key"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>CUSTOMDB.voc_research_result.custom_number</code>.
     */
    public final TableField<VocResearchResultRecord, String> CUSTOM_NUMBER = createField(DSL.name("custom_number"), org.jooq.impl.SQLDataType.VARCHAR(50).nullable(false).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>CUSTOMDB.voc_research_result.voc_group_id</code>.
     */
    public final TableField<VocResearchResultRecord, Integer> VOC_GROUP_ID = createField(DSL.name("voc_group_id"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");
    /**
     * The column <code>CUSTOMDB.voc_research_result.userid</code>.
     */
    public final TableField<VocResearchResultRecord, String> USERID = createField(DSL.name("userid"), org.jooq.impl.SQLDataType.VARCHAR(30).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>CUSTOMDB.voc_research_result.extension</code>.
     */
    public final TableField<VocResearchResultRecord, String> EXTENSION = createField(DSL.name("extension"), org.jooq.impl.SQLDataType.VARCHAR(30).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>CUSTOMDB.voc_research_result.research_id</code>.
     */
    public final TableField<VocResearchResultRecord, Integer> RESEARCH_ID = createField(DSL.name("research_id"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");
    /**
     * The column <code>CUSTOMDB.voc_research_result.tree_path</code>.
     */
    public final TableField<VocResearchResultRecord, String> TREE_PATH = createField(DSL.name("tree_path"), org.jooq.impl.SQLDataType.VARCHAR(800).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>CUSTOMDB.voc_research_result.my_path_1</code>.
     */
    public final TableField<VocResearchResultRecord, String> MY_PATH_1 = createField(DSL.name("my_path_1"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>CUSTOMDB.voc_research_result.my_path_2</code>.
     */
    public final TableField<VocResearchResultRecord, String> MY_PATH_2 = createField(DSL.name("my_path_2"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>CUSTOMDB.voc_research_result.my_path_3</code>.
     */
    public final TableField<VocResearchResultRecord, String> MY_PATH_3 = createField(DSL.name("my_path_3"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>CUSTOMDB.voc_research_result.my_path_4</code>.
     */
    public final TableField<VocResearchResultRecord, String> MY_PATH_4 = createField(DSL.name("my_path_4"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>CUSTOMDB.voc_research_result.my_path_5</code>.
     */
    public final TableField<VocResearchResultRecord, String> MY_PATH_5 = createField(DSL.name("my_path_5"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>CUSTOMDB.voc_research_result.my_path_6</code>.
     */
    public final TableField<VocResearchResultRecord, String> MY_PATH_6 = createField(DSL.name("my_path_6"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>CUSTOMDB.voc_research_result.my_path_7</code>.
     */
    public final TableField<VocResearchResultRecord, String> MY_PATH_7 = createField(DSL.name("my_path_7"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>CUSTOMDB.voc_research_result.my_path_8</code>.
     */
    public final TableField<VocResearchResultRecord, String> MY_PATH_8 = createField(DSL.name("my_path_8"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>CUSTOMDB.voc_research_result.my_path_9</code>.
     */
    public final TableField<VocResearchResultRecord, String> MY_PATH_9 = createField(DSL.name("my_path_9"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>CUSTOMDB.voc_research_result.my_path_10</code>.
     */
    public final TableField<VocResearchResultRecord, String> MY_PATH_10 = createField(DSL.name("my_path_10"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>CUSTOMDB.voc_research_result.my_path_11</code>.
     */
    public final TableField<VocResearchResultRecord, String> MY_PATH_11 = createField(DSL.name("my_path_11"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>CUSTOMDB.voc_research_result.my_path_12</code>.
     */
    public final TableField<VocResearchResultRecord, String> MY_PATH_12 = createField(DSL.name("my_path_12"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>CUSTOMDB.voc_research_result.my_path_13</code>.
     */
    public final TableField<VocResearchResultRecord, String> MY_PATH_13 = createField(DSL.name("my_path_13"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>CUSTOMDB.voc_research_result.my_path_14</code>.
     */
    public final TableField<VocResearchResultRecord, String> MY_PATH_14 = createField(DSL.name("my_path_14"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>CUSTOMDB.voc_research_result.my_path_15</code>.
     */
    public final TableField<VocResearchResultRecord, String> MY_PATH_15 = createField(DSL.name("my_path_15"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>CUSTOMDB.voc_research_result.my_path_16</code>.
     */
    public final TableField<VocResearchResultRecord, String> MY_PATH_16 = createField(DSL.name("my_path_16"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>CUSTOMDB.voc_research_result.my_path_17</code>.
     */
    public final TableField<VocResearchResultRecord, String> MY_PATH_17 = createField(DSL.name("my_path_17"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>CUSTOMDB.voc_research_result.my_path_18</code>.
     */
    public final TableField<VocResearchResultRecord, String> MY_PATH_18 = createField(DSL.name("my_path_18"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>CUSTOMDB.voc_research_result.my_path_19</code>.
     */
    public final TableField<VocResearchResultRecord, String> MY_PATH_19 = createField(DSL.name("my_path_19"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>CUSTOMDB.voc_research_result.my_path_20</code>.
     */
    public final TableField<VocResearchResultRecord, String> MY_PATH_20 = createField(DSL.name("my_path_20"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
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
    public CommonVocResearchResult(Name alias) {
        this(alias, VOC_RESEARCH_RESULT);
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

    /**
     * The class holding records for this type
     */
    @Override
    public Class<VocResearchResultRecord> getRecordType() {
        return VocResearchResultRecord.class;
    }

    @Override
    public Schema getSchema() {
        return Customdb.CUSTOMDB;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.VOC_RESEARCH_RESULT_MY_PATH_1, Indexes.VOC_RESEARCH_RESULT_MY_PATH_10, Indexes.VOC_RESEARCH_RESULT_MY_PATH_11, Indexes.VOC_RESEARCH_RESULT_MY_PATH_12, Indexes.VOC_RESEARCH_RESULT_MY_PATH_13, Indexes.VOC_RESEARCH_RESULT_MY_PATH_14, Indexes.VOC_RESEARCH_RESULT_MY_PATH_15, Indexes.VOC_RESEARCH_RESULT_MY_PATH_16, Indexes.VOC_RESEARCH_RESULT_MY_PATH_17, Indexes.VOC_RESEARCH_RESULT_MY_PATH_18, Indexes.VOC_RESEARCH_RESULT_MY_PATH_19, Indexes.VOC_RESEARCH_RESULT_MY_PATH_2, Indexes.VOC_RESEARCH_RESULT_MY_PATH_20, Indexes.VOC_RESEARCH_RESULT_MY_PATH_3, Indexes.VOC_RESEARCH_RESULT_MY_PATH_4, Indexes.VOC_RESEARCH_RESULT_MY_PATH_5, Indexes.VOC_RESEARCH_RESULT_MY_PATH_6, Indexes.VOC_RESEARCH_RESULT_MY_PATH_7, Indexes.VOC_RESEARCH_RESULT_MY_PATH_8, Indexes.VOC_RESEARCH_RESULT_MY_PATH_9, Indexes.VOC_RESEARCH_RESULT_RESEARCH_ID);
    }

    @Override
    public Identity<VocResearchResultRecord, Integer> getIdentity() {
        return (Identity<VocResearchResultRecord, Integer>) super.getIdentity();
    }

    @Override
    public UniqueKey<VocResearchResultRecord> getPrimaryKey() {
        return Keys.KEY_VOC_RESEARCH_RESULT_PRIMARY;
    }

    @Override
    public List<UniqueKey<VocResearchResultRecord>> getKeys() {
        return Arrays.<UniqueKey<VocResearchResultRecord>>asList(Keys.KEY_VOC_RESEARCH_RESULT_PRIMARY);
    }

    @Override
    public CommonVocResearchResult as(String alias) {
        return new CommonVocResearchResult(DSL.name(alias), this);
    }

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
