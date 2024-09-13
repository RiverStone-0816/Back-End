package kr.co.eicn.ippbx.meta.jooq.customdb.tables;

import kr.co.eicn.ippbx.meta.jooq.customdb.Customdb;
import kr.co.eicn.ippbx.meta.jooq.customdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.CommonTranscribeDataRecord;
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

public class CommonTranscribeData extends TableImpl<CommonTranscribeDataRecord> {
    /**
     * The column <code>CUSTOMDB.transcribe_data.seq</code>. 고유키
     */
    public final TableField<CommonTranscribeDataRecord, Integer> SEQ = createField(DSL.name("seq"), SQLDataType.INTEGER.nullable(false).identity(true), this, "고유키");

    /**
     * The column <code>CUSTOMDB.transcribe_data.company_id</code>. 고객사 아이디
     */
    public final TableField<CommonTranscribeDataRecord, String> COMPANY_ID = createField(DSL.name("company_id"), SQLDataType.VARCHAR(30).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "고객사 아이디");

    /**
     * The column <code>CUSTOMDB.transcribe_data.groupCode</code>. 전사그룹코드
     */
    public final TableField<CommonTranscribeDataRecord, Integer> GROUPCODE = createField(DSL.name("groupCode"), SQLDataType.INTEGER.nullable(false), this, "전사그룹코드");

    /**
     * The column <code>CUSTOMDB.transcribe_data.filePath</code>. 전사파일경로
     */
    public final TableField<CommonTranscribeDataRecord, String> FILEPATH = createField(DSL.name("filePath"), SQLDataType.VARCHAR(200).nullable(false).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "전사파일경로");

    /**
     * The column <code>CUSTOMDB.transcribe_data.fileName</code>. 전사파일명
     */
    public final TableField<CommonTranscribeDataRecord, String> FILENAME = createField(DSL.name("fileName"), SQLDataType.VARCHAR(200).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "전사파일명");

    /**
     * The column <code>CUSTOMDB.transcribe_data.userId</code>. 전사상담원ID
     */
    public final TableField<CommonTranscribeDataRecord, String> USERID = createField(DSL.name("userId"), SQLDataType.VARCHAR(30).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "전사상담원ID");

    /**
     * The column <code>CUSTOMDB.transcribe_data.hypInfo</code>. STT정보
     */
    public final TableField<CommonTranscribeDataRecord, String> HYPINFO = createField(DSL.name("hypInfo"), SQLDataType.CLOB.defaultValue(DSL.field("''", SQLDataType.CLOB)), this, "STT정보");

    /**
     * The column <code>CUSTOMDB.transcribe_data.refInfo</code>. 전사처리정보
     */
    public final TableField<CommonTranscribeDataRecord, String> REFINFO = createField(DSL.name("refInfo"), SQLDataType.CLOB.defaultValue(DSL.field("''", SQLDataType.CLOB)), this, "전사처리정보");

    /**
     * The column <code>CUSTOMDB.transcribe_data.status</code>. 처리상태
     */
    public final TableField<CommonTranscribeDataRecord, String> STATUS = createField(DSL.name("status"), SQLDataType.VARCHAR(10).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "처리상태");

    /**
     * The column <code>CUSTOMDB.transcribe_data.sttStatus</code>. STT 요청상태
     */
    public final TableField<CommonTranscribeDataRecord, String> STTSTATUS = createField(DSL.name("sttStatus"), SQLDataType.VARCHAR(10).defaultValue(DSL.field("'N'", SQLDataType.VARCHAR)), this, "STT 요청상태");

    /**
     * The column <code>CUSTOMDB.transcribe_data.recStatus</code>. 인식률측정 요청상태
     */
    public final TableField<CommonTranscribeDataRecord, String> RECSTATUS = createField(DSL.name("recStatus"), SQLDataType.VARCHAR(10).defaultValue(DSL.field("'N'", SQLDataType.VARCHAR)), this, "인식률측정 요청상태");

    /**
     * The column <code>CUSTOMDB.transcribe_data.learn</code>.
     */
    public final TableField<CommonTranscribeDataRecord, String> LEARN = createField(DSL.name("learn"), SQLDataType.VARCHAR(10).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.transcribe_data.nRate</code>. N인식률
     */
    public final TableField<CommonTranscribeDataRecord, Double> NRATE = createField(DSL.name("nRate"), SQLDataType.DOUBLE.defaultValue(DSL.field("0", SQLDataType.DOUBLE)), this, "N인식률");

    /**
     * The column <code>CUSTOMDB.transcribe_data.dRate</code>. D인식률
     */
    public final TableField<CommonTranscribeDataRecord, Double> DRATE = createField(DSL.name("dRate"), SQLDataType.DOUBLE.defaultValue(DSL.field("0", SQLDataType.DOUBLE)), this, "D인식률");

    /**
     * The column <code>CUSTOMDB.transcribe_data.sRate</code>. S인식률
     */
    public final TableField<CommonTranscribeDataRecord, Double> SRATE = createField(DSL.name("sRate"), SQLDataType.DOUBLE.defaultValue(DSL.field("0", SQLDataType.DOUBLE)), this, "S인식률");

    /**
     * The column <code>CUSTOMDB.transcribe_data.iRate</code>. I인식률
     */
    public final TableField<CommonTranscribeDataRecord, Double> IRATE = createField(DSL.name("iRate"), SQLDataType.DOUBLE.defaultValue(DSL.field("0", SQLDataType.DOUBLE)), this, "I인식률");

    /**
     * The column <code>CUSTOMDB.transcribe_data.aRate</code>. A인식률
     */
    public final TableField<CommonTranscribeDataRecord, Double> ARATE = createField(DSL.name("aRate"), SQLDataType.DOUBLE.defaultValue(DSL.field("0", SQLDataType.DOUBLE)), this, "A인식률");

    private final String tableName;

    /**
     * Create a <code>CUSTOMDB.transcribe_data_*</code> table reference
     */
    public CommonTranscribeData(String tableName) {
        this(DSL.name("transcribe_data_" + tableName), null);
    }

    private CommonTranscribeData(Name alias, Table<CommonTranscribeDataRecord> aliased) {
        this(alias, aliased, null);
    }

    private CommonTranscribeData(Name alias, Table<CommonTranscribeDataRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
        this.tableName = alias.last();
    }

    public <O extends Record> CommonTranscribeData(CommonTranscribeData table, Table<O> child, ForeignKey<O, CommonTranscribeDataRecord> key) {
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
    public Identity<CommonTranscribeDataRecord, Integer> getIdentity() {
        return Internal.createIdentity(this, this.SEQ);
    }

    @Override
    public UniqueKey<CommonTranscribeDataRecord> getPrimaryKey() {
        return Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ);
    }

    @NotNull
    @Override
    public List<UniqueKey<CommonTranscribeDataRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ));
    }

    @NotNull
    @Override
    public CommonTranscribeData as(String alias) {
        return new CommonTranscribeData(DSL.name(alias), this);
    }

    @NotNull
    @Override
    public CommonTranscribeData as(Name alias) {
        return new CommonTranscribeData(alias, this);
    }

    @Override
    public CommonTranscribeData rename(String name) {
        return new CommonTranscribeData(DSL.name(name), null);
    }

    @Override
    public CommonTranscribeData rename(Name name) {
        return new CommonTranscribeData(name, null);
    }
}
