package kr.co.eicn.ippbx.meta.jooq.customdb.tables;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.CommonTranscribeDataRecord;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.TranscribeDataRecord;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import java.util.Collections;
import java.util.List;

public class CommonTranscribeData extends TableImpl<CommonTranscribeDataRecord> {
    /**
     * The column <code>CUSTOMDB.transcribe_data.seq</code>. SEQUENCE KEY
     */
    public final TableField<CommonTranscribeDataRecord, Integer> SEQ = createField(DSL.name("seq"), SQLDataType.INTEGER.nullable(false).identity(true), this, "SEQUENCE KEY");

    /**
     * The column <code>CUSTOMDB.transcribe_data.company_id</code>. 고객사 아이디
     */
    public final TableField<CommonTranscribeDataRecord, String> COMPANY_ID = createField(DSL.name("company_id"), SQLDataType.VARCHAR(30).defaultValue(DSL.inline("''", SQLDataType.VARCHAR)), this, "고객사 아이디");

    /**
     * The column <code>CUSTOMDB.transcribe_data.groupCode</code>. 전사그룹코드
     */
    public final TableField<CommonTranscribeDataRecord, Integer> GROUPCODE = createField(DSL.name("groupCode"), SQLDataType.INTEGER.nullable(false), this, "전사그룹코드");

    /**
     * The column <code>CUSTOMDB.transcribe_data.filePath</code>. 전사파일경로
     */
    public final TableField<CommonTranscribeDataRecord, String> FILEPATH = createField(DSL.name("filePath"), SQLDataType.VARCHAR(200).nullable(false).defaultValue(DSL.inline("''", SQLDataType.VARCHAR)), this, "전사파일경로");

    /**
     * The column <code>CUSTOMDB.transcribe_data.fileName</code>. 전사파일명
     */
    public final TableField<CommonTranscribeDataRecord, String> FILENAME = createField(DSL.name("fileName"), SQLDataType.VARCHAR(50).nullable(false).defaultValue(DSL.inline("''", SQLDataType.VARCHAR)), this, "전사파일명");

    /**
     * The column <code>CUSTOMDB.transcribe_data.userId</code>. 전사상담원ID
     */
    public final TableField<CommonTranscribeDataRecord, String> USERID = createField(DSL.name("userId"), SQLDataType.VARCHAR(30).defaultValue(DSL.inline("''", SQLDataType.VARCHAR)), this, "전사상담원ID");

    /**
     * The column <code>CUSTOMDB.transcribe_data.hypInfo</code>.
     */
    public final TableField<CommonTranscribeDataRecord, String> HYPINFO = createField(DSL.name("hypInfo"), SQLDataType.CLOB.defaultValue(DSL.inline("''", SQLDataType.CLOB)), this, "");

    /**
     * The column <code>CUSTOMDB.transcribe_data.refInfo</code>.
     */
    public final TableField<CommonTranscribeDataRecord, String> REFINFO = createField(DSL.name("refInfo"), SQLDataType.CLOB.defaultValue(DSL.inline("''", SQLDataType.CLOB)), this, "");

    /**
     * The column <code>CUSTOMDB.transcribe_data.status</code>.
     */
    public final TableField<CommonTranscribeDataRecord, String> STATUS = createField(DSL.name("status"), SQLDataType.VARCHAR(10).defaultValue(DSL.inline("''", SQLDataType.VARCHAR)), this, "");


    /**
     * The column <code>CUSTOMDB.transcribe_data.sttStatus</code>. STT 요청상태
     */
    public final TableField<CommonTranscribeDataRecord, String> STTSTATUS = createField(DSL.name("sttStatus"), SQLDataType.VARCHAR(10).defaultValue(DSL.inline("'N'", SQLDataType.VARCHAR)), this, "STT 요청상태");

    /**
     * The column <code>CUSTOMDB.transcribe_data.recStatus</code>. 인식률측정 요청상태
     */
    public final TableField<CommonTranscribeDataRecord, String> RECSTATUS = createField(DSL.name("recStatus"), SQLDataType.VARCHAR(10).defaultValue(DSL.inline("'N'", SQLDataType.VARCHAR)), this, "인식률측정 요청상태");

    /**
     * The column <code>CUSTOMDB.transcribe_data.learn</code>.
     */
    public final TableField<CommonTranscribeDataRecord, String> LEARN = createField(DSL.name("learn"), SQLDataType.VARCHAR(10).defaultValue(DSL.inline("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.transcribe_data.nRate</code>. N인식률
     */
    public final TableField<CommonTranscribeDataRecord, Double> NRATE = createField(DSL.name("nRate"), SQLDataType.DOUBLE.defaultValue(DSL.inline("0", SQLDataType.DOUBLE)), this, "N인식률");

    /**
     * The column <code>CUSTOMDB.transcribe_data.dRate</code>. D인식률
     */
    public final TableField<CommonTranscribeDataRecord, Double> DRATE = createField(DSL.name("dRate"), SQLDataType.DOUBLE.defaultValue(DSL.inline("0", SQLDataType.DOUBLE)), this, "D인식률");

    /**
     * The column <code>CUSTOMDB.transcribe_data.sRate</code>. S인식률
     */
    public final TableField<CommonTranscribeDataRecord, Double> SRATE = createField(DSL.name("sRate"), SQLDataType.DOUBLE.defaultValue(DSL.inline("0", SQLDataType.DOUBLE)), this, "S인식률");

    /**
     * The column <code>CUSTOMDB.transcribe_data.iRate</code>. I인식률
     */
    public final TableField<CommonTranscribeDataRecord, Double> IRATE = createField(DSL.name("iRate"), SQLDataType.DOUBLE.defaultValue(DSL.inline("0", SQLDataType.DOUBLE)), this, "I인식률");

    /**
     * The column <code>CUSTOMDB.transcribe_data.aRate</code>. A인식률
     */
    public final TableField<CommonTranscribeDataRecord, Double> ARATE = createField(DSL.name("aRate"), SQLDataType.DOUBLE.defaultValue(DSL.inline("0", SQLDataType.DOUBLE)), this, "A인식률");


    private final String tableName;

    /**
     * Create a <code>CUSTOMDB.transcribe_data_*</code> table reference
     */
    public CommonTranscribeData(String tableName) {
        this(DSL.name("transcribe_data_" + tableName), null);
    }

    private CommonTranscribeData(Name alias, Table<CommonTranscribeDataRecord> aliased){
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<CommonTranscribeDataRecord, Integer> getIdentity() {
        return org.jooq.impl.Internal.createIdentity(this, this.SEQ);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<CommonTranscribeDataRecord> getPrimaryKey() {
        return org.jooq.impl.Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<CommonTranscribeDataRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ));
    }


    @Override
    public CommonTranscribeData as(String alias) {
        return new CommonTranscribeData(DSL.name(alias), this);
    }

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
