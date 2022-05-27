package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotFormresultElement;
import kr.co.eicn.ippbx.model.form.WebchatBotFormRequest;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.WEBCHAT_BOT_FORMRESULT_ELEMENT;

@Getter
@Repository
public class WebchatBotFormResultElementRepository extends EicnBaseRepository<kr.co.eicn.ippbx.meta.jooq.eicn.tables.WebchatBotFormresultElement, WebchatBotFormresultElement, Integer> {
    private final Logger logger = LoggerFactory.getLogger(WebchatBotFormResultElementRepository.class);

    public WebchatBotFormResultElementRepository() {
        super(WEBCHAT_BOT_FORMRESULT_ELEMENT, WEBCHAT_BOT_FORMRESULT_ELEMENT.ID, WebchatBotFormresultElement.class);

        orderByFields.add(WEBCHAT_BOT_FORMRESULT_ELEMENT.ID.asc());
    }

    public List<WebchatBotFormresultElement> findAllByBLockIdList(List<Integer> idList) {
        return findAll(WEBCHAT_BOT_FORMRESULT_ELEMENT.BLOCK_ID.in(idList));
    }

    public Integer insert(Integer blockId, WebchatBotFormRequest.AuthResultElement request) {
        return dsl.insertInto(WEBCHAT_BOT_FORMRESULT_ELEMENT)
                .set(WEBCHAT_BOT_FORMRESULT_ELEMENT.BLOCK_ID, blockId)
                .set(WEBCHAT_BOT_FORMRESULT_ELEMENT.RESULT_VALUE, request.getValue())
                .set(WEBCHAT_BOT_FORMRESULT_ELEMENT.RESULT_MENT, request.getMent())
                .set(WEBCHAT_BOT_FORMRESULT_ELEMENT.ACTION, request.getAction().getCode())
                .set(WEBCHAT_BOT_FORMRESULT_ELEMENT.NEXT_ACTION_DATA, request.getNextActionData())
                .set(WEBCHAT_BOT_FORMRESULT_ELEMENT.NEXT_API_MENT, request.getNextApiMent())
                .set(WEBCHAT_BOT_FORMRESULT_ELEMENT.IS_RESULT_TPL_ENABLE, request.getEnableResultTemplate() != null && request.getEnableResultTemplate() ? "Y" : "N")
                .set(WEBCHAT_BOT_FORMRESULT_ELEMENT.NEXT_API_RESULT_TPL, request.getNextApiResultTemplate())
                .set(WEBCHAT_BOT_FORMRESULT_ELEMENT.NEXT_API_NO_RESULT_MENT, request.getNextApiNoResultMent())
                .set(WEBCHAT_BOT_FORMRESULT_ELEMENT.NEXT_API_ERROR_MENT, request.getNextApiErrorMent())
                .set(WEBCHAT_BOT_FORMRESULT_ELEMENT.COMPANY_ID, getCompanyId())
                .returning()
                .fetchOne()
                .value1();
    }

    public void updateBlockId(Integer elementId, Integer blockId) {
        dsl.update(WEBCHAT_BOT_FORMRESULT_ELEMENT)
                .set(WEBCHAT_BOT_FORMRESULT_ELEMENT.NEXT_ACTION_DATA, String.valueOf(blockId))
                .where(compareCompanyId())
                .and(WEBCHAT_BOT_FORMRESULT_ELEMENT.ID.eq(elementId))
                .execute();
    }

    public void deleteByBlockIdList(List<Integer> blockIdList) {
        if (blockIdList != null && blockIdList.size() > 0)
            dsl.deleteFrom(WEBCHAT_BOT_FORMRESULT_ELEMENT)
                    .where(WEBCHAT_BOT_FORMRESULT_ELEMENT.BLOCK_ID.in(blockIdList))
                    .execute();
    }
}
