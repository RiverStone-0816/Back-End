package kr.co.eicn.ippbx.meta.jooq.customdb.tables;

import kr.co.eicn.ippbx.meta.jooq.customdb.Customdb;
import kr.co.eicn.ippbx.meta.jooq.customdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.CommonTranscribeGroupRecord;
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

public class CommonTranscribeGroup extends TableImpl<CommonTranscribeGroupRecord> {
    /**
     * The column <code>CUSTOMDB.transcribe_group.seq</code>.
     */
    public final TableField<CommonTranscribeGroupRecord, Integer> SEQ = createField(DSL.name("seq"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>CUSTOMDB.transcribe_group.company_id</code>. 컴퍼니아이디
     */
    public final TableField<CommonTranscribeGroupRecord, String> COMPANY_ID = createField(DSL.name("company_id"), SQLDataType.VARCHAR(30).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "컴퍼니아이디");

    /**
     * The column <code>CUSTOMDB.transcribe_group.groupName</code>. 전사그룹명
     */
    public final TableField<CommonTranscribeGroupRecord, String> GROUPNAME = createField(DSL.name("groupName"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "전사그룹명");

    /**
     * The column <code>CUSTOMDB.transcribe_group.userId</code>. 그룹담당자ID
     */
    public final TableField<CommonTranscribeGroupRecord, String> USERID = createField(DSL.name("userId"), SQLDataType.VARCHAR(30).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "그룹담당자ID");

    /**
     * The column <code>CUSTOMDB.transcribe_group.status</code>. 그룹진행상태
     */
    public final TableField<CommonTranscribeGroupRecord, String> STATUS = createField(DSL.name("status"), SQLDataType.VARCHAR(10).defaultValue(DSL.field("'A'", SQLDataType.VARCHAR)), this, "그룹진행상태");

    /**
     * The column <code>CUSTOMDB.transcribe_group.fileCnt</code>. 녹취파일갯수
     */
    public final TableField<CommonTranscribeGroupRecord, Integer> FILECNT = createField(DSL.name("fileCnt"), SQLDataType.INTEGER.defaultValue(DSL.field("0", SQLDataType.INTEGER)), this, "녹취파일갯수");

    /**
     * The column <code>CUSTOMDB.transcribe_group.recRate</code>. 인식률
     */
    public final TableField<CommonTranscribeGroupRecord, Double> RECRATE = createField(DSL.name("recRate"), SQLDataType.DOUBLE.defaultValue(DSL.field("0", SQLDataType.DOUBLE)), this, "인식률");

    private final String tableName;

    /**
     * Create a <code>CUSTOMDB.transcribe_group_*</code> table reference
     */
    public CommonTranscribeGroup(String tableName) {
        this(DSL.name("transcribe_group_" + tableName), null);
    }

    private CommonTranscribeGroup(Name alias, Table<CommonTranscribeGroupRecord> aliased) {
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
    public Identity<CommonTranscribeGroupRecord, Integer> getIdentity() {
        return Internal.createIdentity(this, this.SEQ);
    }

    @Override
    public UniqueKey<CommonTranscribeGroupRecord> getPrimaryKey() {
        return Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ);
    }

    @NotNull
    @Override
    public List<UniqueKey<CommonTranscribeGroupRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ));
    }

    @NotNull
    @Override
    public CommonTranscribeGroup as(String alias) {
        return new CommonTranscribeGroup(DSL.name(alias), this);
    }

    @NotNull
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
