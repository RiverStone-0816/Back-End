package kr.co.eicn.ippbx.meta.jooq.customdb.tables;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.CommonTranscribeGroupRecord;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommonTranscribeGroup extends TableImpl<CommonTranscribeGroupRecord> {
    /**
     * The column <code>CUSTOMDB.transcribe_group_*.seq</code>.
     */
    public final TableField<CommonTranscribeGroupRecord, Integer> SEQ = createField(DSL.name("seq"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>CUSTOMDB.transcribe_group.company_id</code>. 컴퍼니아이디
     */
    public final TableField<CommonTranscribeGroupRecord, String> COMPANY_ID = createField(DSL.name("company_id"), org.jooq.impl.SQLDataType.VARCHAR(30).defaultValue(org.jooq.impl.DSL.inline("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "컴퍼니아이디");

    /**
     * The column <code>CUSTOMDB.transcribe_group_*.groupName</code>. 전사그룹명
     */
    public final TableField<CommonTranscribeGroupRecord, String> GROUPNAME = createField(DSL.name("groupName"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "전사그룹명");

    /**
     * The column <code>CUSTOMDB.transcribe_group_*.userId</code>. 그룹담당자ID
     */
    public final TableField<CommonTranscribeGroupRecord, String> USERID = createField(DSL.name("userId"), org.jooq.impl.SQLDataType.VARCHAR(30).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "그룹담당자ID");

    /**
     * The column <code>CUSTOMDB.transcribe_group_*.status</code>. 그룹진행상태
     */
    public final TableField<CommonTranscribeGroupRecord, String> STATUS = createField(DSL.name("status"), org.jooq.impl.SQLDataType.VARCHAR(10).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "그룹진행상태");

    /**
     * The column <code>CUSTOMDB.transcribe_group_*.fileCnt</code>. 녹취파일갯수
     */
    public final TableField<CommonTranscribeGroupRecord, Integer> FILECNT = createField(DSL.name("fileCnt"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "녹취파일갯수");

    /**
     * The column <code>CUSTOMDB.transcribe_group_*.filePath</code>. 녹취파일메인경로
     */
    public final TableField<CommonTranscribeGroupRecord, Double> RECRATE = createField(DSL.name("recRate"), SQLDataType.DOUBLE.defaultValue(DSL.inline("0", SQLDataType.DOUBLE)), this, "인식률");


    private final String tableName;

    /**
     * Create a <code>CUSTOMDB.transcribe_group_*</code> table reference
     */
    public CommonTranscribeGroup(String tableName) {
        this(DSL.name("transcribe_group_" + tableName), null);
    }

    private CommonTranscribeGroup(Name alias, Table<CommonTranscribeGroupRecord> aliased){
        this(alias, aliased, null);
    }

    private CommonTranscribeGroup(Name alias, Table<CommonTranscribeGroupRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
        this.tableName = alias.last();
    }

    public <O extends Record> CommonTranscribeGroup(CommonTranscribeGroup table, Table<O> child, ForeignKey<O, CommonTranscribeGroupRecord> key) {
        super(child, key, table);
        this.tableName = table.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<CommonTranscribeGroupRecord, Integer> getIdentity() {
        return org.jooq.impl.Internal.createIdentity(this, this.SEQ);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<CommonTranscribeGroupRecord> getPrimaryKey() {
        return Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<CommonTranscribeGroupRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ));
    }


    @Override
    public CommonTranscribeGroup as(String alias) {
        return new CommonTranscribeGroup(DSL.name(alias), this);
    }

    @Override
    public CommonTranscribeGroup as(Name alias) {
        return new CommonTranscribeGroup(alias, this);
    }

    @Override
    public CommonTranscribeGroup rename(String name) {
        return new CommonTranscribeGroup(DSL.name(name), null);
    }

    @Override
    public CommonTranscribeGroup rename(Name name) {
        return new CommonTranscribeGroup(name, null);
    }
}
