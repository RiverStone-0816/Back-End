package kr.co.eicn.ippbx.server.repository.customdb;

import io.micrometer.core.instrument.util.StringUtils;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonKakaoChatbotBlock;
import kr.co.eicn.ippbx.model.form.ChatbotOpenBuilderBlockFormRequest;
import kr.co.eicn.ippbx.model.search.ChatbotOpenBuilderBlockSearchRequest;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.jooq.Condition;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Getter
public class KakaoChatbotBlockRepository extends CustomDBBaseRepository<CommonKakaoChatbotBlock, kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonKakaoChatbotBlock, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(KakaoChatbotBlockRepository.class);

    private final CommonKakaoChatbotBlock TABLE;

    public KakaoChatbotBlockRepository(String companyId) {
        super(new CommonKakaoChatbotBlock(companyId), new CommonKakaoChatbotBlock(companyId).SEQ, kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonKakaoChatbotBlock.class);
        TABLE = new CommonKakaoChatbotBlock(companyId);
    }

    public Pagination<kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonKakaoChatbotBlock> pagination(ChatbotOpenBuilderBlockSearchRequest search) {
        return super.pagination(search, getConditions(search));
    }

    private List<Condition> getConditions(ChatbotOpenBuilderBlockSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        if (StringUtils.isNotEmpty(search.getChatbotId()))
            conditions.add(TABLE.BOT_ID.eq(search.getChatbotId()));

        return conditions;
    }

    public void insert(ChatbotOpenBuilderBlockFormRequest form) {
        dsl.insertInto(TABLE)
                .set(TABLE.BOT_ID, form.getChatbotId())
                .set(TABLE.BOT_NAME, form.getChatbotName())
                .set(TABLE.BLOCK_NAME, form.getBlockName())
                .set(TABLE.BLOCK_ID, form.getBlockId())
                .set(TABLE.RESPONSE_TYPE, form.getResponseType())
                .set(TABLE.RESPONSE_GET_URL, form.getResponseGetUrl())
                .set(TABLE.RESPONSE_PARAM_NAMES, form.getResponseParamNames())
                .set(TABLE.EVENT_NAME, form.getEventName())
                .set(TABLE.USE_YN, form.getUseYn())
                .execute();
    }

    public void updateByKey(Integer seq, ChatbotOpenBuilderBlockFormRequest form) {
        dsl.update(TABLE)
                .set(TABLE.BOT_ID, form.getChatbotId())
                .set(TABLE.BOT_NAME, form.getChatbotName())
                .set(TABLE.BLOCK_NAME, form.getBlockName())
                .set(TABLE.BLOCK_ID, form.getBlockId())
                .set(TABLE.RESPONSE_TYPE, form.getResponseType())
                .set(TABLE.RESPONSE_GET_URL, form.getResponseGetUrl())
                .set(TABLE.RESPONSE_PARAM_NAMES, form.getResponseParamNames())
                .set(TABLE.EVENT_NAME, form.getEventName())
                .set(TABLE.USE_YN, form.getUseYn())
                .where(TABLE.SEQ.eq(seq))
                .execute();
    }

    public List<kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonKakaoChatbotBlock> findAllEventList(String botId) {
        return dsl.select(TABLE.BOT_ID,
                TABLE.BOT_NAME,
                TABLE.EVENT_NAME,
                TABLE.BLOCK_NAME)
                .from(TABLE)
                .where(TABLE.EVENT_NAME.isNotNull())
                .and(TABLE.EVENT_NAME.ne(""))
                .and(StringUtils.isNotEmpty(botId) ? TABLE.BOT_ID.eq(botId) : DSL.noCondition())
                .fetchInto(kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonKakaoChatbotBlock.class);
    }
}
