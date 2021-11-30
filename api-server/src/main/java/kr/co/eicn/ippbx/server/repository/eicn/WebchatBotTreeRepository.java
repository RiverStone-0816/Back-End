package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.WebchatBotTree;
import kr.co.eicn.ippbx.model.form.WebchatBotTreeFormRequest;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.WEBCHAT_BOT_TREE;
import static org.jooq.impl.DSL.noCondition;

@Getter
@Repository
public class WebchatBotTreeRepository extends EicnBaseRepository<WebchatBotTree, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotTree, Integer> {
    protected Logger logger = LoggerFactory.getLogger(WebchatBotTreeRepository.class);

    public WebchatBotTreeRepository() {
        super(WEBCHAT_BOT_TREE, WEBCHAT_BOT_TREE.SEQ, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotTree.class);
    }

    public void insert(WebchatBotTreeFormRequest request) {
        dsl.insertInto(WEBCHAT_BOT_TREE)
                .set(WEBCHAT_BOT_TREE.CHATBOT_ID, request.getChatBotId())
                .set(WEBCHAT_BOT_TREE.BLOCK_ID, request.getBlockId())
                .set(WEBCHAT_BOT_TREE.ROOT_ID, request.getRootId())
                .set(WEBCHAT_BOT_TREE.PARENT_ID, request.getParentId())
                .set(WEBCHAT_BOT_TREE.PARENT_BTN_ID, request.getParentButtonId())
                .set(WEBCHAT_BOT_TREE.LEVEL, request.getLevel())
                .set(WEBCHAT_BOT_TREE.TREE_NAME, request.getTreeName())
                .set(WEBCHAT_BOT_TREE.COMPANY_ID, getCompanyId())
                .execute();
    }

    public void deleteByBotId(Integer botId) {
        dsl.deleteFrom(WEBCHAT_BOT_TREE)
                .where(compareCompanyId())
                .and(WEBCHAT_BOT_TREE.CHATBOT_ID.eq(botId))
                .execute();
    }

    public kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotTree findRootBlock(Integer botId) {
        return findOne(WEBCHAT_BOT_TREE.CHATBOT_ID.eq(botId)
                .and(WEBCHAT_BOT_TREE.LEVEL.eq(0))
                .and(WEBCHAT_BOT_TREE.BLOCK_ID.eq(WEBCHAT_BOT_TREE.ROOT_ID)));
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotTree> findTreeByBotId(Integer botId, Integer rootBlockId) {
        return dsl.select(WEBCHAT_BOT_TREE.fields())
                .from(WEBCHAT_BOT_TREE)
                .where(compareCompanyId())
                .and(WEBCHAT_BOT_TREE.CHATBOT_ID.eq(botId))
                .and(rootBlockId != null ? WEBCHAT_BOT_TREE.BLOCK_ID.ne(rootBlockId) : noCondition())
                .fetchInto(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotTree.class);
    }
}
