package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.WebchatBotBtnElement;
import kr.co.eicn.ippbx.model.enums.ButtonAction;
import kr.co.eicn.ippbx.model.form.WebchatBotButtonElementFormRequest;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.WEBCHAT_BOT_BTN_ELEMENT;
import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.WEBCHAT_BOT_TREE;

@Getter
@Repository
public class WebchatBotButtonElementRepository extends EicnBaseRepository<WebchatBotBtnElement, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotBtnElement, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(WebchatBotButtonElementRepository.class);

    public WebchatBotButtonElementRepository() {
        super(WEBCHAT_BOT_BTN_ELEMENT, WEBCHAT_BOT_BTN_ELEMENT.ID, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotBtnElement.class);
    }

    public Integer insert(WebchatBotButtonElementFormRequest request) {
        return dsl.insertInto(WEBCHAT_BOT_BTN_ELEMENT)
                .set(WEBCHAT_BOT_BTN_ELEMENT.BLOCK_ID, request.getBlockId())
                .set(WEBCHAT_BOT_BTN_ELEMENT.SEQUENCE, request.getOrder())
                .set(WEBCHAT_BOT_BTN_ELEMENT.BTN_NAME, request.getButtonName())
                .set(WEBCHAT_BOT_BTN_ELEMENT.ACTION, request.getAction().getCode())
                .set(WEBCHAT_BOT_BTN_ELEMENT.NEXT_ACTION_DATA, request.getActionData())
                .set(WEBCHAT_BOT_BTN_ELEMENT.NEXT_API_MENT, request.getNextApiMent())
                .set(WEBCHAT_BOT_BTN_ELEMENT.IS_RESULT_TPL_ENABLE, request.getIsResultTemplateEnable() != null && request.getIsResultTemplateEnable() ? "Y" : "N")
                .set(WEBCHAT_BOT_BTN_ELEMENT.NEXT_API_RESULT_TPL, request.getNextApiResultTemplate())
                .set(WEBCHAT_BOT_BTN_ELEMENT.NEXT_API_NO_RESULT_MENT, request.getNextApiNoResultMent())
                .set(WEBCHAT_BOT_BTN_ELEMENT.NEXT_API_ERROR_MENT, request.getNextApiErrorMent())
                .set(WEBCHAT_BOT_BTN_ELEMENT.COMPANY_ID, getCompanyId())
                .returning()
                .fetchOne()
                .value1();
    }

    public void updateNextBlockId(Integer buttonId, Integer nextBlockId) {
        dsl.update(WEBCHAT_BOT_BTN_ELEMENT)
                .set(WEBCHAT_BOT_BTN_ELEMENT.NEXT_ACTION_DATA, String.valueOf(nextBlockId))
                .where(WEBCHAT_BOT_BTN_ELEMENT.ID.eq(buttonId))
                .execute();
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotBtnElement> findButtonListByBlockIdList(List<Integer> blockIdList) {
        return dsl.select(WEBCHAT_BOT_BTN_ELEMENT.fields())
                .select(WEBCHAT_BOT_TREE.PARENT_BTN_ID)
                .from(WEBCHAT_BOT_BTN_ELEMENT)
                .leftOuterJoin(WEBCHAT_BOT_TREE)
                .on(WEBCHAT_BOT_BTN_ELEMENT.ID.eq(WEBCHAT_BOT_TREE.PARENT_BTN_ID))
                .where(compareCompanyId())
                .and(WEBCHAT_BOT_BTN_ELEMENT.BLOCK_ID.in(blockIdList))
                .orderBy(WEBCHAT_BOT_BTN_ELEMENT.SEQUENCE)
                .fetch(e -> {
                    kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotBtnElement entity = e.into(WEBCHAT_BOT_BTN_ELEMENT).into(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotBtnElement.class);
                    if (Objects.equals(e.getValue(WEBCHAT_BOT_TREE.PARENT_BTN_ID), e.getValue(WEBCHAT_BOT_BTN_ELEMENT.ID)))
                        entity.setAction(ButtonAction.CONNECT_NEXT_BLOCK.getCode());

                    return entity;
                });
    }

    public void deleteByBlockIdList(List<Integer> blockIdList) {
        dsl.deleteFrom(WEBCHAT_BOT_BTN_ELEMENT)
                .where(compareCompanyId())
                .and(WEBCHAT_BOT_BTN_ELEMENT.BLOCK_ID.in(blockIdList))
                .execute();
    }
}
