package kr.co.eicn.ippbx.meta.jooq.customdb.tables;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.CommonLearnGroupRecord;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.CommonTranscribeGroupRecord;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.jooq.impl.TableImpl;

import java.util.Collections;
import java.util.List;

public class CommonLearnGroup extends TableImpl<CommonLearnGroupRecord> {
    /**
     * The column <code>CUSTOMDB.transcribe_learn_*.seq</code>.
     */
    public final TableField<CommonLearnGroupRecord, Integer> SEQ = createField(DSL.name("seq"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>CUSTOMDB.transcribe_learn.company_id</code>. 컴퍼니아이디
     */
    public final TableField<CommonLearnGroupRecord, String> COMPANY_ID = createField(DSL.name("company_id"), org.jooq.impl.SQLDataType.VARCHAR(30).defaultValue(org.jooq.impl.DSL.inline("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "컴퍼니아이디");

    /**
     * The column <code>CUSTOMDB.transcribe_learn_*.groupName</code>. 학습그룹명
     */
    public final TableField<CommonLearnGroupRecord, String> GROUPNAME = createField(DSL.name("groupName"), org.jooq.impl.SQLDataType.VARCHAR(100).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "학습그룹명");

    /**
     * The column <code>CUSTOMDB.transcribe_learn_*.groupName</code>. 학습그룹정보
     */
    public final TableField<CommonLearnGroupRecord, String> LEARNGROUPCODE = createField(DSL.name("learnGroupCode"), org.jooq.impl.SQLDataType.VARCHAR(200).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "학습그룹정보");

    /**
     * The column <code>CUSTOMDB.transcribe_learn_*.userId</code>. 학습요청상태
     */
    public final TableField<CommonLearnGroupRecord, String> LEARNSTATUS = createField(DSL.name("learnStatus"), org.jooq.impl.SQLDataType.VARCHAR(10).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "학습요청상태");

    /**
     * The column <code>CUSTOMDB.transcribe_learn_*.status</code>. 학습모델파일
     */
    public final TableField<CommonLearnGroupRecord, String> LEARNFILENAME = createField(DSL.name("learnFileName"), org.jooq.impl.SQLDataType.VARCHAR(200).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "학습모델파일");

    private final String tableName;

    /**
     * Create a <code>CUSTOMDB.transcribe_learn_*</code> table reference
     */
    public CommonLearnGroup(String tableName) {
        this(DSL.name("transcribe_learn_" + tableName), null);
    }

    private CommonLearnGroup(Name alias, Table<CommonLearnGroupRecord> aliased){
        this(alias, aliased, null);
    }

    private CommonLearnGroup(Name alias, Table<CommonLearnGroupRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
        this.tableName = alias.last();
    }

    public <O extends Record> CommonLearnGroup(CommonLearnGroup table, Table<O> child, ForeignKey<O, CommonLearnGroupRecord> key) {
        super(child, key, table);
        this.tableName = table.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<CommonLearnGroupRecord, Integer> getIdentity() {
        return org.jooq.impl.Internal.createIdentity(this, this.SEQ);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<CommonLearnGroupRecord> getPrimaryKey() {
        return org.jooq.impl.Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<CommonLearnGroupRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ));
    }

    @Override
    public CommonLearnGroup as(String alias) {
        return new CommonLearnGroup(DSL.name(alias), this);
    }

    @Override
    public CommonLearnGroup as(Name alias) {
        return new CommonLearnGroup(alias, this);
    }

    @Override
    public CommonLearnGroup rename(String name) {
        return new CommonLearnGroup(DSL.name(name), null);
    }

    @Override
    public CommonLearnGroup rename(Name name) {
        return new CommonLearnGroup(name, null);
    }

}
