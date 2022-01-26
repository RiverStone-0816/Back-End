package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.WebchatBotInfo;
import kr.co.eicn.ippbx.model.search.ChatbotSearchRequest;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.WebchatBotInfo.WEBCHAT_BOT_INFO;

@Getter
@Repository
public class WebchatBotInfoRepository extends EicnBaseRepository<WebchatBotInfo, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotInfo, Integer> {
    public final Logger logger = LoggerFactory.getLogger(WebchatBotInfoRepository.class);

    public WebchatBotInfoRepository() {
        super(WEBCHAT_BOT_INFO, WEBCHAT_BOT_INFO.ID, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotInfo.class);
    }

    public Pagination<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotInfo> pagination(ChatbotSearchRequest search) {
        return pagination(search, conditions(search));
    }

    private List<Condition> conditions(ChatbotSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        /*if (search.getStartDate() != null && search.getEndDate() != null)
            conditions.add(WEBCHAT_BOT_INFO.)*/

        if (StringUtils.isNotEmpty(search.getChatbotName()))
            conditions.add(WEBCHAT_BOT_INFO.NAME.contains(search.getChatbotName()));

        return conditions;
    }

    public Integer insert(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotInfo data) {
        return dsl.insertInto(WEBCHAT_BOT_INFO)
                .set(WEBCHAT_BOT_INFO.NAME, data.getName())
                .set(WEBCHAT_BOT_INFO.IS_CUSTINPUT_ENABLE, data.getIsCustinputEnable())
                .set(WEBCHAT_BOT_INFO.FALLBACK_MENT, data.getFallbackMent())
                .set(WEBCHAT_BOT_INFO.FALLBACK_ACTION, data.getFallbackAction())
                .set(WEBCHAT_BOT_INFO.FALLBACK_ACTION_DATA, data.getFallbackActionData())
                .set(WEBCHAT_BOT_INFO.COMPANY_ID, getCompanyId())
                .returning()
                .fetchOne()
                .value1();
    }

    public void updateById(Integer id, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotInfo data) {
        dsl.update(WEBCHAT_BOT_INFO)
                .set(WEBCHAT_BOT_INFO.NAME, data.getName())
                .set(WEBCHAT_BOT_INFO.IS_CUSTINPUT_ENABLE, data.getIsCustinputEnable())
                .set(WEBCHAT_BOT_INFO.FALLBACK_MENT, data.getFallbackMent())
                .set(WEBCHAT_BOT_INFO.FALLBACK_ACTION, data.getFallbackAction())
                .set(WEBCHAT_BOT_INFO.FALLBACK_ACTION_DATA, data.getFallbackActionData())
                .where(WEBCHAT_BOT_INFO.ID.eq(id))
                .execute();
    }

    public void updateBlockId(Integer id, Integer blockId) {
        dsl.update(WEBCHAT_BOT_INFO)
                .set(WEBCHAT_BOT_INFO.FALLBACK_ACTION_DATA, String.valueOf(blockId))
                .where(WEBCHAT_BOT_INFO.ID.eq(id))
                .execute();
    }
}
