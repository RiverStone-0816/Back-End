package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatAuthBlock;
import kr.co.eicn.ippbx.model.form.WebchatBotFormRequest;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.WEBCHAT_AUTH_BLOCK;

@Getter
@Repository
public class WebchatAuthBlockRepository extends EicnBaseRepository<kr.co.eicn.ippbx.meta.jooq.eicn.tables.WebchatAuthBlock, WebchatAuthBlock, Integer> {
    private final Logger logger = LoggerFactory.getLogger(WebchatAuthBlockRepository.class);

    public WebchatAuthBlockRepository() {
        super(WEBCHAT_AUTH_BLOCK, WEBCHAT_AUTH_BLOCK.ID, WebchatAuthBlock.class);
    }

    public List<WebchatAuthBlock> findAllByBotId(Integer botId) {
        return findAll(WEBCHAT_AUTH_BLOCK.BOT_ID.eq(botId).or(WEBCHAT_AUTH_BLOCK.OTHER_BOT_USE_YN.eq("Y")));
    }

    public void deleteByBotId(Integer botId) {
        delete(WEBCHAT_AUTH_BLOCK.BOT_ID.eq(botId));
    }

    public Integer insert(Integer botId, WebchatBotFormRequest.AuthBlockInfo form) {
        return dsl.insertInto(WEBCHAT_AUTH_BLOCK)
                .set(WEBCHAT_AUTH_BLOCK.BOT_ID, botId)
                .set(WEBCHAT_AUTH_BLOCK.NAME, form.getName())
                .set(WEBCHAT_AUTH_BLOCK.TITLE, form.getTitle())
                .set(WEBCHAT_AUTH_BLOCK.OTHER_BOT_USE_YN, form.getUsingOtherBot() ? "Y" : "N")
                .set(WEBCHAT_AUTH_BLOCK.COMPANY_ID, g.getUser().getCompanyId())
                .returning(WEBCHAT_AUTH_BLOCK.ID)
                .fetchOne()
                .value1();
    }
}
