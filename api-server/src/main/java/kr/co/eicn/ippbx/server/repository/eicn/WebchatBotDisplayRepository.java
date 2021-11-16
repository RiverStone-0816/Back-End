package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.WebchatBotDisplay;
import kr.co.eicn.ippbx.model.form.WebchatBotDisplayFormRequest;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.WEBCHAT_BOT_DISPLAY;

@Getter
@Repository
public class WebchatBotDisplayRepository extends EicnBaseRepository<WebchatBotDisplay, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotDisplay, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(WebchatBotDisplayRepository.class);

    public WebchatBotDisplayRepository() {
        super(WEBCHAT_BOT_DISPLAY, WEBCHAT_BOT_DISPLAY.ID, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotDisplay.class);
    }

    public Integer insert(WebchatBotDisplayFormRequest request) {
        return dsl.insertInto(WEBCHAT_BOT_DISPLAY)
                .set(WEBCHAT_BOT_DISPLAY.BLOCK_ID, request.getBlockId())
                .set(WEBCHAT_BOT_DISPLAY.ORDER, request.getOrder())
                .set(WEBCHAT_BOT_DISPLAY.TYPE, request.getType().getCode())
                .returning(WEBCHAT_BOT_DISPLAY.ID)
                .fetchOne()
                .value1();
    }

    public List<Integer> findDisplayIdListByDisplayIdList(List<Integer> blockIdList) {
        return dsl.select(WEBCHAT_BOT_DISPLAY.ID)
                .from(WEBCHAT_BOT_DISPLAY)
                .where(WEBCHAT_BOT_DISPLAY.BLOCK_ID.in(blockIdList))
                .fetchInto(Integer.class);
    }

    public void deleteByBlockIdList(List<Integer> blockIdList) {
        dsl.deleteFrom(WEBCHAT_BOT_DISPLAY)
                .where(WEBCHAT_BOT_DISPLAY.BLOCK_ID.in(blockIdList))
                .execute();
    }
}
