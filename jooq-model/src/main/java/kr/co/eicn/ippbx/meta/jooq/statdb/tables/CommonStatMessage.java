package kr.co.eicn.ippbx.meta.jooq.statdb.tables;

import kr.co.eicn.ippbx.meta.jooq.statdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.statdb.Keys;
import kr.co.eicn.ippbx.meta.jooq.statdb.Statdb;
import kr.co.eicn.ippbx.meta.jooq.statdb.tables.records.StatMessageRecord;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

public class CommonStatMessage extends TableImpl<StatMessageRecord> {
    /**
     * The reference instance of <code>STATDB.stat_message</code>
     */
    public static final StatMessage STAT_MESSAGE = new StatMessage();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<StatMessageRecord> getRecordType() {
        return StatMessageRecord.class;
    }

    /**
     * The column <code>STATDB.stat_message.stat_date</code>.
     */
    public final TableField<StatMessageRecord, Date> STAT_DATE = createField(DSL.name("stat_date"), org.jooq.impl.SQLDataType.DATE.nullable(false).defaultValue(org.jooq.impl.DSL.inline("'2009-01-01'", org.jooq.impl.SQLDataType.DATE)), this, "");

    /**
     * The column <code>STATDB.stat_message.stat_hour</code>.
     */
    public final TableField<StatMessageRecord, Byte> STAT_HOUR = createField(DSL.name("stat_hour"), org.jooq.impl.SQLDataType.TINYINT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.TINYINT)), this, "");

    /**
     * The column <code>STATDB.stat_message.service</code>. SMS,MMS,LMS,KAKAO,RCS
     */
    public final TableField<StatMessageRecord, String> SERVICE = createField(DSL.name("service"), org.jooq.impl.SQLDataType.VARCHAR(10).nullable(false).defaultValue(org.jooq.impl.DSL.inline("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "SMS,MMS,LMS,KAKAO,RCS");

    /**
     * The column <code>STATDB.stat_message.project_id</code>. 메세지허브프로젝트아이디
     */
    public final TableField<StatMessageRecord, String> PROJECT_ID = createField(DSL.name("project_id"), org.jooq.impl.SQLDataType.VARCHAR(50).nullable(false).defaultValue(org.jooq.impl.DSL.inline("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "메세지허브프로젝트아이디");

    /**
     * The column <code>STATDB.stat_message.api_key</code>. 메세지허브프로젝트API키
     */
    public final TableField<StatMessageRecord, String> API_KEY = createField(DSL.name("api_key"), org.jooq.impl.SQLDataType.VARCHAR(50).nullable(false).defaultValue(org.jooq.impl.DSL.inline("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "메세지허브프로젝트API키");

    /**
     * The column <code>STATDB.stat_message.userid</code>. 사용자아이디
     */
    public final TableField<StatMessageRecord, String> USERID = createField(DSL.name("userid"), org.jooq.impl.SQLDataType.VARCHAR(50).nullable(false).defaultValue(org.jooq.impl.DSL.inline("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "사용자아이디");

    /**
     * The column <code>STATDB.stat_message.res_code</code>. 결과코드
     */
    public final TableField<StatMessageRecord, String> RES_CODE = createField(DSL.name("res_code"), org.jooq.impl.SQLDataType.VARCHAR(10).nullable(false).defaultValue(org.jooq.impl.DSL.inline("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "결과코드");

    /**
     * The column <code>STATDB.stat_message.res_data_code</code>. 메세지발송코드
     */
    public final TableField<StatMessageRecord, String> RES_DATA_CODE = createField(DSL.name("res_data_code"), org.jooq.impl.SQLDataType.VARCHAR(10).nullable(false).defaultValue(org.jooq.impl.DSL.inline("''", org.jooq.impl.SQLDataType.VARCHAR)), this, "메세지발송코드");

    /**
     * The column <code>STATDB.stat_message.total</code>.
     */
    public final TableField<StatMessageRecord, Integer> TOTAL = createField(DSL.name("total"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * Create a <code>STATDB.stat_message</code> table reference
     */
    public CommonStatMessage(String companyName) {
        this(DSL.name("stat_message_" + companyName), null);
    }

    /**
     * Create an aliased <code>STATDB.stat_message</code> table reference
     */

    private CommonStatMessage(Name alias, Table<StatMessageRecord> aliased) {
        this(alias, aliased, null);
    }

    private CommonStatMessage(Name alias, Table<StatMessageRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    public <O extends Record> CommonStatMessage(Table<O> child, ForeignKey<O, StatMessageRecord> key) {
        super(child, key, STAT_MESSAGE);
    }

    @Override
    public Schema getSchema() {
        return Statdb.STATDB;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.STAT_MESSAGE_PROJECT_ID, Indexes.STAT_MESSAGE_RES_CODE, Indexes.STAT_MESSAGE_RES_DATA_CODE, Indexes.STAT_MESSAGE_SERVICE, Indexes.STAT_MESSAGE_STAT_DATE, Indexes.STAT_MESSAGE_STAT_HOUR, Indexes.STAT_MESSAGE_USERID);
    }

    @Override
    public UniqueKey<StatMessageRecord> getPrimaryKey() {
        return Keys.KEY_STAT_MESSAGE_PRIMARY;
    }

    @Override
    public List<UniqueKey<StatMessageRecord>> getKeys() {
        return Arrays.<UniqueKey<StatMessageRecord>>asList(Keys.KEY_STAT_MESSAGE_PRIMARY);
    }

    @Override
    public CommonStatMessage as(String alias) {
        return new CommonStatMessage(DSL.name(alias), this);
    }

    @Override
    public CommonStatMessage as(Name alias) {
        return new CommonStatMessage(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonStatMessage rename(String name) {
        return new CommonStatMessage(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonStatMessage rename(Name name) {
        return new CommonStatMessage(name, null);
    }

    // -------------------------------------------------------------------------
    // Row9 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row9<Date, Byte, String, String, String, String, String, String, Integer> fieldsRow() {
        return (Row9) super.fieldsRow();
    }
}
