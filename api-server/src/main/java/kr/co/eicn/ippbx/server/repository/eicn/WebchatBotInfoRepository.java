package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.WebchatBotInfo;
import kr.co.eicn.ippbx.model.form.WebchatBotFormRequest;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.WebchatBotInfo.WEBCHAT_BOT_INFO;

@Getter
@Repository
public class WebchatBotInfoRepository extends EicnBaseRepository<WebchatBotInfo, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotInfo, Integer> {
    public final Logger logger = LoggerFactory.getLogger(WebchatBotInfoRepository.class);

    public WebchatBotInfoRepository() {
        super(WEBCHAT_BOT_INFO, WEBCHAT_BOT_INFO.ID, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotInfo.class);
    }

    public Integer insert(WebchatBotFormRequest form) {
        return dsl.insertInto(WEBCHAT_BOT_INFO)
                .set(WEBCHAT_BOT_INFO.NAME, form.getName())
                .set(WEBCHAT_BOT_INFO.FALLBACK_MENT, form.getFallbackMent())
                .set(WEBCHAT_BOT_INFO.FALLBACK_ACTION, form.getFallbackAction().getCode())
                .set(WEBCHAT_BOT_INFO.NEXT_BLOCK_ID, form.getNextBlockId())
                .set(WEBCHAT_BOT_INFO.NEXT_GROUP_ID, form.getNextGroupId())
                .set(WEBCHAT_BOT_INFO.NEXT_URL, form.getNextUrl())
                .set(WEBCHAT_BOT_INFO.NEXT_PHONE, form.getNextPhone())
                .set(WEBCHAT_BOT_INFO.COMPANY_ID, getCompanyId())
                .returning()
                .fetchOne()
                .value1();
    }

    public void updateById(Integer id, WebchatBotFormRequest form) {
        dsl.update(WEBCHAT_BOT_INFO)
                .set(WEBCHAT_BOT_INFO.NAME, form.getName())
                .set(WEBCHAT_BOT_INFO.FALLBACK_MENT, form.getFallbackMent())
                .set(WEBCHAT_BOT_INFO.FALLBACK_ACTION, form.getFallbackAction().getCode())
                .set(WEBCHAT_BOT_INFO.NEXT_BLOCK_ID, form.getNextBlockId())
                .set(WEBCHAT_BOT_INFO.NEXT_GROUP_ID, form.getNextGroupId())
                .set(WEBCHAT_BOT_INFO.NEXT_URL, form.getNextUrl())
                .set(WEBCHAT_BOT_INFO.NEXT_PHONE, form.getNextPhone())
                .where(WEBCHAT_BOT_INFO.ID.eq(id))
                .execute();
    }
}
