package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.WebchatBotBlock;
import kr.co.eicn.ippbx.model.form.WebchatBotBlockFormRequest;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.WEBCHAT_BOT_BLOCK;

@Getter
@Repository
public class WebchatBotBlockRepository extends EicnBaseRepository<WebchatBotBlock, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotBlock, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(WebchatBotBlockRepository.class);

    public WebchatBotBlockRepository() {
        super(WEBCHAT_BOT_BLOCK, WEBCHAT_BOT_BLOCK.ID, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotBlock.class);
    }

    public void insert(Integer blockId, WebchatBotBlockFormRequest request) {
        dsl.insertInto(WEBCHAT_BOT_BLOCK)
                .set(WEBCHAT_BOT_BLOCK.ID, blockId)
                .set(WEBCHAT_BOT_BLOCK.NAME, request.getName())
                .set(WEBCHAT_BOT_BLOCK.KEYWORD, request.getKeyword())
                .set(WEBCHAT_BOT_BLOCK.IS_TPL_ENABLE, request.getIsTemplateEnable() != null && request.getIsTemplateEnable() ? "Y" : "N")
                .set(WEBCHAT_BOT_BLOCK.COMPANY_ID, getCompanyId())
                .execute();
    }

    public void deleteByBlockIdList(List<Integer> blockIdList) {
        dsl.deleteFrom(WEBCHAT_BOT_BLOCK)
                .where(WEBCHAT_BOT_BLOCK.ID.in(blockIdList))
                .execute();
    }
}
