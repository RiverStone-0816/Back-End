package kr.co.eicn.ippbx.meta.jooq.customdb.tables;

import kr.co.eicn.ippbx.meta.jooq.customdb.Customdb;
import kr.co.eicn.ippbx.meta.jooq.customdb.Indexes;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.CommonKakaoChatbotBlockRecord;
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

public class CommonKakaoChatbotBlock extends TableImpl<CommonKakaoChatbotBlockRecord> {
    /**
     * The column <code>CUSTOMDB.kakao_chatbot_block.seq</code>.
     */
    public final TableField<CommonKakaoChatbotBlockRecord, Integer> SEQ = createField(DSL.name("seq"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_chatbot_block.bot_id</code>.
     */
    public final TableField<CommonKakaoChatbotBlockRecord, String> BOT_ID = createField(DSL.name("bot_id"), SQLDataType.VARCHAR(50).nullable(false), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_chatbot_block.bot_name</code>.
     */
    public final TableField<CommonKakaoChatbotBlockRecord, String> BOT_NAME = createField(DSL.name("bot_name"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_chatbot_block.block_id</code>.
     */
    public final TableField<CommonKakaoChatbotBlockRecord, String> BLOCK_ID = createField(DSL.name("block_id"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_chatbot_block.block_name</code>.
     */
    public final TableField<CommonKakaoChatbotBlockRecord, String> BLOCK_NAME = createField(DSL.name("block_name"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_chatbot_block.response_type</code>.
     */
    public final TableField<CommonKakaoChatbotBlockRecord, String> RESPONSE_TYPE = createField(DSL.name("response_type"), SQLDataType.VARCHAR(30).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_chatbot_block.response_get_url</code>.
     */
    public final TableField<CommonKakaoChatbotBlockRecord, String> RESPONSE_GET_URL = createField(DSL.name("response_get_url"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_chatbot_block.response_param_names</code>.
     */
    public final TableField<CommonKakaoChatbotBlockRecord, String> RESPONSE_PARAM_NAMES = createField(DSL.name("response_param_names"), SQLDataType.VARCHAR(100).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_chatbot_block.event_name</code>.
     */
    public final TableField<CommonKakaoChatbotBlockRecord, String> EVENT_NAME = createField(DSL.name("event_name"), SQLDataType.VARCHAR(50).defaultValue(DSL.field("''", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>CUSTOMDB.kakao_chatbot_block.use_yn</code>.
     */
    public final TableField<CommonKakaoChatbotBlockRecord, String> USE_YN = createField(DSL.name("use_yn"), SQLDataType.CHAR(1).defaultValue(DSL.field("''", SQLDataType.CHAR)), this, "");

    private final String tableName;

    /**
     * Create a <code>CUSTOMDB.kakao_chatbot_block</code> table reference
     */
    public CommonKakaoChatbotBlock(String companyName) {
        this(DSL.name("kakao_chatbot_block_" + companyName), null);
    }

    private CommonKakaoChatbotBlock(Name alias, Table<CommonKakaoChatbotBlockRecord> aliased) {
        this(alias, aliased, null);
    }

    private CommonKakaoChatbotBlock(Name alias, Table<CommonKakaoChatbotBlockRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
        this.tableName = alias.last();
    }

    public <O extends Record> CommonKakaoChatbotBlock(CommonKakaoChatbotBlock table, Table<O> child, ForeignKey<O, CommonKakaoChatbotBlockRecord> key) {
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
        return Arrays.<Index>asList(Indexes.KAKAO_CHATBOT_BLOCK_BLOCK_ID, Indexes.KAKAO_CHATBOT_BLOCK_BOT_ID);
    }

    @Override
    public Identity<CommonKakaoChatbotBlockRecord, Integer> getIdentity() {
        return Internal.createIdentity(this, this.SEQ);
    }

    @NotNull
    @Override
    public List<UniqueKey<CommonKakaoChatbotBlockRecord>> getKeys() {
        return Collections.singletonList(Internal.createUniqueKey(this, DSL.name("KEY_" + getName() + "_PRIMARY"), this.SEQ));
    }

    @NotNull
    @Override
    public CommonKakaoChatbotBlock as(String alias) {
        return new CommonKakaoChatbotBlock(DSL.name(alias), this);
    }

    @NotNull
    @Override
    public CommonKakaoChatbotBlock as(Name alias) {
        return new CommonKakaoChatbotBlock(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonKakaoChatbotBlock rename(String name) {
        return new CommonKakaoChatbotBlock(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonKakaoChatbotBlock rename(Name name) {
        return new CommonKakaoChatbotBlock(name, null);
    }
}
