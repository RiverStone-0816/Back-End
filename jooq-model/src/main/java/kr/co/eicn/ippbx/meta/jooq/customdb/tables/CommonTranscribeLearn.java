package kr.co.eicn.ippbx.meta.jooq.customdb.tables;

import kr.co.eicn.ippbx.meta.jooq.customdb.Customdb;
import kr.co.eicn.ippbx.meta.jooq.customdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.CommonTranscribeLearnRecord;
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

public class CommonTranscribeLearn extends TableImpl<CommonTranscribeLearnRecord> {
    /**
     * The column <code>CUSTOMDB.transcribe_learn.seq</code>. 고유키
     */
    public final TableField<CommonTranscribeLearnRecord, Integer> SEQ = createField(DSL.name("seq"), SQLDataType.INTEGER.nullable(false).identity(true), this, "고유키");

    /**
     * The column <code>CUSTOMDB.transcribe_learn.company_id</code>. 고객사 아이디
     */
    public final TableField<CommonTranscribeLearnRecord, String> COMPANY_ID = createField(DSL.name("company_id"), SQLDataType.VARCHAR(30).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "고객사 아이디");

    /**
     * The column <code>CUSTOMDB.transcribe_learn.groupName</code>. 학습그룹명
     */
    public final TableField<CommonTranscribeLearnRecord, String> GROUPNAME = createField(DSL.name("groupName"), SQLDataType.VARCHAR(100).nullable(false).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "학습그룹명");

    /**
     * The column <code>CUSTOMDB.transcribe_learn.learnGroupCode</code>. 학습그룹코드모음
     */
    public final TableField<CommonTranscribeLearnRecord, String> LEARNGROUPCODE = createField(DSL.name("learnGroupCode"), SQLDataType.VARCHAR(200).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "학습그룹코드모음");

    /**
     * The column <code>CUSTOMDB.transcribe_learn.learnStatus</code>. 학습요청상태
     */
    public final TableField<CommonTranscribeLearnRecord, String> LEARNSTATUS = createField(DSL.name("learnStatus"), SQLDataType.VARCHAR(10).nullable(false).defaultValue(DSL.field("'N'", SQLDataType.VARCHAR)), this, "학습요청상태");

    /**
     * The column <code>CUSTOMDB.transcribe_learn.learnFileName</code>. 학습모델파일
     */
    public final TableField<CommonTranscribeLearnRecord, String> LEARNFILENAME = createField(DSL.name("learnFileName"), SQLDataType.VARCHAR(200).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "학습모델파일");

    private final String tableName;

    /**
     * Create a <code>CUSTOMDB.transcribe_learn_*</code> table reference
     */
    public CommonTranscribeLearn(String tableName) {
        this(DSL.name("transcribe_learn_" + tableName), null);
    }

    private CommonTranscribeLearn(Name alias, Table<CommonTranscribeLearnRecord> aliased) {
        this(alias, aliased, null);
    }

    private CommonTranscribeLearn(Name alias, Table<CommonTranscribeLearnRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
        this.tableName = alias.last();
    }

    public <O extends Record> CommonTranscribeLearn(CommonTranscribeLearn table, Table<O> child, ForeignKey<O, CommonTranscribeLearnRecord> key) {
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
        return Arrays.<Index>asList(Indexes.TRANSCRIBE_LEARN_COMPANY_ID);
    }

    @Override
    public Identity<CommonTranscribeLearnRecord, Integer> getIdentity() {
        return Internal.createIdentity(this, this.SEQ);
    }

    @Override
    public UniqueKey<CommonTranscribeLearnRecord> getPrimaryKey() {
        return Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ);
    }

    @NotNull
    @Override
    public List<UniqueKey<CommonTranscribeLearnRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ));
    }

    @NotNull
    @Override
    public CommonTranscribeLearn as(String alias) {
        return new CommonTranscribeLearn(DSL.name(alias), this);
    }

    @NotNull
    @Override
    public CommonTranscribeLearn as(Name alias) {
        return new CommonTranscribeLearn(alias, this);
    }

    @Override
    public CommonTranscribeLearn rename(String name) {
        return new CommonTranscribeLearn(DSL.name(name), null);
    }

    @Override
    public CommonTranscribeLearn rename(Name name) {
        return new CommonTranscribeLearn(name, null);
    }

}
