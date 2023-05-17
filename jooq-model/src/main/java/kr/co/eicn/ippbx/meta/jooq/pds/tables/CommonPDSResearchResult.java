package kr.co.eicn.ippbx.meta.jooq.pds.tables;

import kr.co.eicn.ippbx.meta.jooq.pds.Indexes;
import kr.co.eicn.ippbx.meta.jooq.pds.Keys;
import kr.co.eicn.ippbx.meta.jooq.pds.Pds;
import kr.co.eicn.ippbx.meta.jooq.pds.tables.records.PdsResearchResultRecord;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

public class CommonPDSResearchResult extends TableImpl<PdsResearchResultRecord> {
    /**
     * The column <code>PDS.pds_research_result.seq</code>.
     */
    public final TableField<PdsResearchResultRecord, Integer> SEQ = createField(DSL.name("seq"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "");
    /**
     * The column <code>PDS.pds_research_result.result_date</code>.
     */
    public final TableField<PdsResearchResultRecord, Timestamp> RESULT_DATE = createField(DSL.name("result_date"), org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(DSL.inline("2010-01-01 00:00:00", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");
    /**
     * The column <code>PDS.pds_research_result.uniqueid</code>.
     */
    public final TableField<PdsResearchResultRecord, String> UNIQUEID = createField(DSL.name("uniqueid"), org.jooq.impl.SQLDataType.VARCHAR(50).nullable(false).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_research_result.hangup_cause</code>.
     */
    public final TableField<PdsResearchResultRecord, String> HANGUP_CAUSE = createField(DSL.name("hangup_cause"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_research_result.hangup_msg</code>.
     */
    public final TableField<PdsResearchResultRecord, String> HANGUP_MSG = createField(DSL.name("hangup_msg"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_research_result.billsec</code>.
     */
    public final TableField<PdsResearchResultRecord, Integer> BILLSEC = createField(DSL.name("billsec"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");
    /**
     * The column <code>PDS.pds_research_result.custom_id</code>.
     */
    public final TableField<PdsResearchResultRecord, String> CUSTOM_ID = createField(DSL.name("custom_id"), org.jooq.impl.SQLDataType.VARCHAR(50).nullable(false).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_research_result.click_key</code>.
     */
    public final TableField<PdsResearchResultRecord, String> CLICK_KEY = createField(DSL.name("click_key"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_research_result.custom_number</code>.
     */
    public final TableField<PdsResearchResultRecord, String> CUSTOM_NUMBER = createField(DSL.name("custom_number"), org.jooq.impl.SQLDataType.VARCHAR(50).nullable(false).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_research_result.execute_id</code>.
     */
    public final TableField<PdsResearchResultRecord, String> EXECUTE_ID = createField(DSL.name("execute_id"), org.jooq.impl.SQLDataType.VARCHAR(50).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_research_result.group_id</code>.
     */
    public final TableField<PdsResearchResultRecord, Integer> GROUP_ID = createField(DSL.name("group_id"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");
    /**
     * The column <code>PDS.pds_research_result.userid</code>.
     */
    public final TableField<PdsResearchResultRecord, String> USERID = createField(DSL.name("userid"), org.jooq.impl.SQLDataType.VARCHAR(30).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_research_result.research_id</code>.
     */
    public final TableField<PdsResearchResultRecord, Integer> RESEARCH_ID = createField(DSL.name("research_id"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");
    /**
     * The column <code>PDS.pds_research_result.tree_path</code>.
     */
    public final TableField<PdsResearchResultRecord, String> TREE_PATH = createField(DSL.name("tree_path"), org.jooq.impl.SQLDataType.VARCHAR(800).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_research_result.my_path_1</code>.
     */
    public final TableField<PdsResearchResultRecord, String> MY_PATH_1 = createField(DSL.name("my_path_1"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_research_result.my_path_2</code>.
     */
    public final TableField<PdsResearchResultRecord, String> MY_PATH_2 = createField(DSL.name("my_path_2"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_research_result.my_path_3</code>.
     */
    public final TableField<PdsResearchResultRecord, String> MY_PATH_3 = createField(DSL.name("my_path_3"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_research_result.my_path_4</code>.
     */
    public final TableField<PdsResearchResultRecord, String> MY_PATH_4 = createField(DSL.name("my_path_4"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_research_result.my_path_5</code>.
     */
    public final TableField<PdsResearchResultRecord, String> MY_PATH_5 = createField(DSL.name("my_path_5"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_research_result.my_path_6</code>.
     */
    public final TableField<PdsResearchResultRecord, String> MY_PATH_6 = createField(DSL.name("my_path_6"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_research_result.my_path_7</code>.
     */
    public final TableField<PdsResearchResultRecord, String> MY_PATH_7 = createField(DSL.name("my_path_7"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_research_result.my_path_8</code>.
     */
    public final TableField<PdsResearchResultRecord, String> MY_PATH_8 = createField(DSL.name("my_path_8"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_research_result.my_path_9</code>.
     */
    public final TableField<PdsResearchResultRecord, String> MY_PATH_9 = createField(DSL.name("my_path_9"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_research_result.my_path_10</code>.
     */
    public final TableField<PdsResearchResultRecord, String> MY_PATH_10 = createField(DSL.name("my_path_10"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_research_result.my_path_11</code>.
     */
    public final TableField<PdsResearchResultRecord, String> MY_PATH_11 = createField(DSL.name("my_path_11"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_research_result.my_path_12</code>.
     */
    public final TableField<PdsResearchResultRecord, String> MY_PATH_12 = createField(DSL.name("my_path_12"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_research_result.my_path_13</code>.
     */
    public final TableField<PdsResearchResultRecord, String> MY_PATH_13 = createField(DSL.name("my_path_13"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_research_result.my_path_14</code>.
     */
    public final TableField<PdsResearchResultRecord, String> MY_PATH_14 = createField(DSL.name("my_path_14"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_research_result.my_path_15</code>.
     */
    public final TableField<PdsResearchResultRecord, String> MY_PATH_15 = createField(DSL.name("my_path_15"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_research_result.my_path_16</code>.
     */
    public final TableField<PdsResearchResultRecord, String> MY_PATH_16 = createField(DSL.name("my_path_16"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_research_result.my_path_17</code>.
     */
    public final TableField<PdsResearchResultRecord, String> MY_PATH_17 = createField(DSL.name("my_path_17"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_research_result.my_path_18</code>.
     */
    public final TableField<PdsResearchResultRecord, String> MY_PATH_18 = createField(DSL.name("my_path_18"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_research_result.my_path_19</code>.
     */
    public final TableField<PdsResearchResultRecord, String> MY_PATH_19 = createField(DSL.name("my_path_19"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_research_result.my_path_20</code>.
     */
    public final TableField<PdsResearchResultRecord, String> MY_PATH_20 = createField(DSL.name("my_path_20"), org.jooq.impl.SQLDataType.VARCHAR(20).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    /**
     * The column <code>PDS.pds_research_result.company_id</code>.
     */
    public final TableField<PdsResearchResultRecord, String> COMPANY_ID = createField(DSL.name("company_id"), org.jooq.impl.SQLDataType.VARCHAR(30).nullable(false).defaultValue(DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "");
    private final String tableName;

    /**
     * Create an aliased <code>PDS.pds_research_result</code> table reference
     */
    public CommonPDSResearchResult(String executeId) {
        this(DSL.name("pds_research_result_" + executeId), null);
    }

    private CommonPDSResearchResult(Name alias, Table<PdsResearchResultRecord> aliased) {
        this(alias, aliased, null);
    }

    private CommonPDSResearchResult(Name alias, Table<PdsResearchResultRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
        this.tableName = alias.last();
    }

    public <O extends Record> CommonPDSResearchResult(CommonPDSResearchResult table, Table<O> child, ForeignKey<O, PdsResearchResultRecord> key) {
        super(child, key, table);
        this.tableName = table.getName();
    }

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PdsResearchResultRecord> getRecordType() {
        return PdsResearchResultRecord.class;
    }

    @Override
    public Schema getSchema() {
        return Pds.PDS;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.PDS_RESEARCH_RESULT_MY_PATH_1, Indexes.PDS_RESEARCH_RESULT_MY_PATH_10, Indexes.PDS_RESEARCH_RESULT_MY_PATH_11, Indexes.PDS_RESEARCH_RESULT_MY_PATH_12, Indexes.PDS_RESEARCH_RESULT_MY_PATH_13, Indexes.PDS_RESEARCH_RESULT_MY_PATH_14, Indexes.PDS_RESEARCH_RESULT_MY_PATH_15, Indexes.PDS_RESEARCH_RESULT_MY_PATH_16, Indexes.PDS_RESEARCH_RESULT_MY_PATH_17, Indexes.PDS_RESEARCH_RESULT_MY_PATH_18, Indexes.PDS_RESEARCH_RESULT_MY_PATH_19, Indexes.PDS_RESEARCH_RESULT_MY_PATH_2, Indexes.PDS_RESEARCH_RESULT_MY_PATH_20, Indexes.PDS_RESEARCH_RESULT_MY_PATH_3, Indexes.PDS_RESEARCH_RESULT_MY_PATH_4, Indexes.PDS_RESEARCH_RESULT_MY_PATH_5, Indexes.PDS_RESEARCH_RESULT_MY_PATH_6, Indexes.PDS_RESEARCH_RESULT_MY_PATH_7, Indexes.PDS_RESEARCH_RESULT_MY_PATH_8, Indexes.PDS_RESEARCH_RESULT_MY_PATH_9, Indexes.PDS_RESEARCH_RESULT_RESEARCH_ID);
    }

    @Override
    public Identity<PdsResearchResultRecord, Integer> getIdentity() {
        return (Identity<PdsResearchResultRecord, Integer>) super.getIdentity();
    }

    @Override
    public UniqueKey<PdsResearchResultRecord> getPrimaryKey() {
        return Keys.KEY_PDS_RESEARCH_RESULT_PRIMARY;
    }

    @Override
    public List<UniqueKey<PdsResearchResultRecord>> getKeys() {
        return Arrays.<UniqueKey<PdsResearchResultRecord>>asList(Keys.KEY_PDS_RESEARCH_RESULT_PRIMARY);
    }

    @Override
    public CommonPDSResearchResult as(String alias) {
        return new CommonPDSResearchResult(DSL.name(alias), this);
    }

    @Override
    public CommonPDSResearchResult as(Name alias) {
        return new CommonPDSResearchResult(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonPDSResearchResult rename(String name) {
        return new CommonPDSResearchResult(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonPDSResearchResult rename(Name name) {
        return new CommonPDSResearchResult(name, null);
    }
}
