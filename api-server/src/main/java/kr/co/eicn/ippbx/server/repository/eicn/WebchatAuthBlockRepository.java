package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatAuthBlock;
import kr.co.eicn.ippbx.model.form.WebchatAuthBlocKFormRequest;
import lombok.Getter;
import org.jooq.Condition;
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
        Condition condition = WEBCHAT_AUTH_BLOCK.OTHER_BOT_USE_YN.eq("Y");
        if (botId != null)
            condition = condition.or(WEBCHAT_AUTH_BLOCK.BOT_ID.eq(botId));

        return findAll(condition);
    }

    public List<WebchatAuthBlock> findAllDeleteBlockByBotId(Integer botId) {
        return findAll(WEBCHAT_AUTH_BLOCK.BOT_ID.eq(botId));
    }

    public void deleteByBotId(Integer botId) {
        delete(WEBCHAT_AUTH_BLOCK.BOT_ID.eq(botId));
    }

    public Integer insert(Integer botId, WebchatAuthBlocKFormRequest request) {
        return dsl.insertInto(WEBCHAT_AUTH_BLOCK)
                .set(WEBCHAT_AUTH_BLOCK.BOT_ID, botId)
                .set(WEBCHAT_AUTH_BLOCK.NAME, request.getName())
                .set(WEBCHAT_AUTH_BLOCK.OTHER_BOT_USE_YN, request.getUsingOtherBot() ? "Y" : "N")
                .set(WEBCHAT_AUTH_BLOCK.COMPANY_ID, g.getUser().getCompanyId())
                .returning(WEBCHAT_AUTH_BLOCK.ID)
                .fetchOne()
                .value1();
    }

    public void update(Integer id, WebchatAuthBlocKFormRequest request) {
        dsl.update(WEBCHAT_AUTH_BLOCK)
                .set(WEBCHAT_AUTH_BLOCK.NAME, request.getName())
                .set(WEBCHAT_AUTH_BLOCK.OTHER_BOT_USE_YN, request.getUsingOtherBot() ? "Y" : "N")
                .where(WEBCHAT_AUTH_BLOCK.ID.eq(id))
                .execute();
    }
}
