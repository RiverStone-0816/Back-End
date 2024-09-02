package kr.co.eicn.ippbx.meta.jooq.pds.tables;

import kr.co.eicn.ippbx.meta.jooq.pds.Indexes;
import kr.co.eicn.ippbx.meta.jooq.pds.Pds;
import kr.co.eicn.ippbx.meta.jooq.pds.tables.records.PdsResearchResultRecord;
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

public class CommonPDSResearchResult extends TableImpl<PdsResearchResultRecord> {
    /**
     * The class holding records for this type
     */
    @NotNull
    @Override
    public Class<PdsResearchResultRecord> getRecordType() {
        return PdsResearchResultRecord.class;
    }

    /**
     * The column <code>PDS.pds_research_result.seq</code>.
     */
    public final TableField<PdsResearchResultRecord, Integer> SEQ = createField(DSL.name("seq"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>PDS.pds_research_result.result_date</code>.
     */
    public final TableField<PdsResearchResultRecord, Timestamp> RESULT_DATE = createField(DSL.name("result_date"), SQLDataType.TIMESTAMP(0).nullable(false).defaultValue(DSL.field("'2010-01-01 00:00:00'", SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>PDS.pds_research_result.uniqueid</code>.
     */
    public final TableField<PdsResearchResultRecord, String> UNIQUEID = createField(DSL.name("uniqueid"), SQLDataType.VARCHAR(50).nullable(false).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_research_result.hangup_cause</code>.
     */
    public final TableField<PdsResearchResultRecord, String> HANGUP_CAUSE = createField(DSL.name("hangup_cause"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_research_result.hangup_msg</code>.
     */
    public final TableField<PdsResearchResultRecord, String> HANGUP_MSG = createField(DSL.name("hangup_msg"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_research_result.billsec</code>.
     */
    public final TableField<PdsResearchResultRecord, Integer> BILLSEC = createField(DSL.name("billsec"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>PDS.pds_research_result.custom_id</code>.
     */
    public final TableField<PdsResearchResultRecord, String> CUSTOM_ID = createField(DSL.name("custom_id"), SQLDataType.VARCHAR(50).nullable(false).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_research_result.click_key</code>.
     */
    public final TableField<PdsResearchResultRecord, String> CLICK_KEY = createField(DSL.name("click_key"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_research_result.custom_number</code>.
     */
    public final TableField<PdsResearchResultRecord, String> CUSTOM_NUMBER = createField(DSL.name("custom_number"), SQLDataType.VARCHAR(50).nullable(false).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_research_result.execute_id</code>.
     */
    public final TableField<PdsResearchResultRecord, String> EXECUTE_ID = createField(DSL.name("execute_id"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_research_result.group_id</code>.
     */
    public final TableField<PdsResearchResultRecord, Integer> GROUP_ID = createField(DSL.name("group_id"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>PDS.pds_research_result.userid</code>.
     */
    public final TableField<PdsResearchResultRecord, String> USERID = createField(DSL.name("userid"), SQLDataType.VARCHAR(30).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_research_result.research_id</code>.
     */
    public final TableField<PdsResearchResultRecord, Integer> RESEARCH_ID = createField(DSL.name("research_id"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>PDS.pds_research_result.tree_path</code>.
     */
    public final TableField<PdsResearchResultRecord, String> TREE_PATH = createField(DSL.name("tree_path"), SQLDataType.VARCHAR(800).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_research_result.my_path_1</code>.
     */
    public final TableField<PdsResearchResultRecord, String> MY_PATH_1 = createField(DSL.name("my_path_1"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_research_result.my_path_2</code>.
     */
    public final TableField<PdsResearchResultRecord, String> MY_PATH_2 = createField(DSL.name("my_path_2"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_research_result.my_path_3</code>.
     */
    public final TableField<PdsResearchResultRecord, String> MY_PATH_3 = createField(DSL.name("my_path_3"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_research_result.my_path_4</code>.
     */
    public final TableField<PdsResearchResultRecord, String> MY_PATH_4 = createField(DSL.name("my_path_4"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_research_result.my_path_5</code>.
     */
    public final TableField<PdsResearchResultRecord, String> MY_PATH_5 = createField(DSL.name("my_path_5"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_research_result.my_path_6</code>.
     */
    public final TableField<PdsResearchResultRecord, String> MY_PATH_6 = createField(DSL.name("my_path_6"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_research_result.my_path_7</code>.
     */
    public final TableField<PdsResearchResultRecord, String> MY_PATH_7 = createField(DSL.name("my_path_7"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_research_result.my_path_8</code>.
     */
    public final TableField<PdsResearchResultRecord, String> MY_PATH_8 = createField(DSL.name("my_path_8"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_research_result.my_path_9</code>.
     */
    public final TableField<PdsResearchResultRecord, String> MY_PATH_9 = createField(DSL.name("my_path_9"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_research_result.my_path_10</code>.
     */
    public final TableField<PdsResearchResultRecord, String> MY_PATH_10 = createField(DSL.name("my_path_10"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_research_result.my_path_11</code>.
     */
    public final TableField<PdsResearchResultRecord, String> MY_PATH_11 = createField(DSL.name("my_path_11"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_research_result.my_path_12</code>.
     */
    public final TableField<PdsResearchResultRecord, String> MY_PATH_12 = createField(DSL.name("my_path_12"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_research_result.my_path_13</code>.
     */
    public final TableField<PdsResearchResultRecord, String> MY_PATH_13 = createField(DSL.name("my_path_13"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_research_result.my_path_14</code>.
     */
    public final TableField<PdsResearchResultRecord, String> MY_PATH_14 = createField(DSL.name("my_path_14"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_research_result.my_path_15</code>.
     */
    public final TableField<PdsResearchResultRecord, String> MY_PATH_15 = createField(DSL.name("my_path_15"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_research_result.my_path_16</code>.
     */
    public final TableField<PdsResearchResultRecord, String> MY_PATH_16 = createField(DSL.name("my_path_16"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_research_result.my_path_17</code>.
     */
    public final TableField<PdsResearchResultRecord, String> MY_PATH_17 = createField(DSL.name("my_path_17"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_research_result.my_path_18</code>.
     */
    public final TableField<PdsResearchResultRecord, String> MY_PATH_18 = createField(DSL.name("my_path_18"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_research_result.my_path_19</code>.
     */
    public final TableField<PdsResearchResultRecord, String> MY_PATH_19 = createField(DSL.name("my_path_19"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_research_result.my_path_20</code>.
     */
    public final TableField<PdsResearchResultRecord, String> MY_PATH_20 = createField(DSL.name("my_path_20"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PDS.pds_research_result.company_id</code>.
     */
    public final TableField<PdsResearchResultRecord, String> COMPANY_ID = createField(DSL.name("company_id"), SQLDataType.VARCHAR(30).nullable(false).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

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

    @Override
    public Schema getSchema() {
        return Pds.PDS;
    }

    @NotNull
    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.PDS_RESEARCH_RESULT_COMPANY_ID, Indexes.PDS_RESEARCH_RESULT_MY_PATH_1, Indexes.PDS_RESEARCH_RESULT_MY_PATH_10, Indexes.PDS_RESEARCH_RESULT_MY_PATH_11, Indexes.PDS_RESEARCH_RESULT_MY_PATH_12, Indexes.PDS_RESEARCH_RESULT_MY_PATH_13, Indexes.PDS_RESEARCH_RESULT_MY_PATH_14, Indexes.PDS_RESEARCH_RESULT_MY_PATH_15, Indexes.PDS_RESEARCH_RESULT_MY_PATH_16, Indexes.PDS_RESEARCH_RESULT_MY_PATH_17, Indexes.PDS_RESEARCH_RESULT_MY_PATH_18, Indexes.PDS_RESEARCH_RESULT_MY_PATH_19, Indexes.PDS_RESEARCH_RESULT_MY_PATH_2, Indexes.PDS_RESEARCH_RESULT_MY_PATH_20, Indexes.PDS_RESEARCH_RESULT_MY_PATH_3, Indexes.PDS_RESEARCH_RESULT_MY_PATH_4, Indexes.PDS_RESEARCH_RESULT_MY_PATH_5, Indexes.PDS_RESEARCH_RESULT_MY_PATH_6, Indexes.PDS_RESEARCH_RESULT_MY_PATH_7, Indexes.PDS_RESEARCH_RESULT_MY_PATH_8, Indexes.PDS_RESEARCH_RESULT_MY_PATH_9, Indexes.PDS_RESEARCH_RESULT_RESEARCH_ID);
    }

    @Override
    public Identity<PdsResearchResultRecord, Integer> getIdentity() {
        return Internal.createIdentity(this, this.SEQ);
    }

    @Override
    public UniqueKey<PdsResearchResultRecord> getPrimaryKey() {
        return Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ);
    }

    @NotNull
    @Override
    public List<UniqueKey<PdsResearchResultRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ));
    }

    @NotNull
    @Override
    public CommonPDSResearchResult as(String alias) {
        return new CommonPDSResearchResult(DSL.name(alias), this);
    }

    @NotNull
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
