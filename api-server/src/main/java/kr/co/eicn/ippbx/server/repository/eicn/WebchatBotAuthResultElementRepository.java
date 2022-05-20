package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotAuthresultElement;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.records.WebchatBotAuthresultElementRecord;
import kr.co.eicn.ippbx.model.form.WebchatBotFormRequest;
import lombok.Getter;
import org.jooq.InsertValuesStep11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.WEBCHAT_AUTH_BTN_ELEMENT;
import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.WEBCHAT_BOT_AUTHRESULT_ELEMENT;

@Getter
@Repository
public class WebchatBotAuthResultElementRepository extends EicnBaseRepository<kr.co.eicn.ippbx.meta.jooq.eicn.tables.WebchatBotAuthresultElement, WebchatBotAuthresultElement, Integer> {
    private final Logger logger = LoggerFactory.getLogger(WebchatBotAuthResultElementRepository.class);

    public WebchatBotAuthResultElementRepository() {
        super(WEBCHAT_BOT_AUTHRESULT_ELEMENT, WEBCHAT_BOT_AUTHRESULT_ELEMENT.ID, WebchatBotAuthresultElement.class);

        orderByFields.add(WEBCHAT_BOT_AUTHRESULT_ELEMENT.ID.asc());
    }

    public List<WebchatBotAuthresultElement> findAllByBLockIdList(List<Integer> idList) {
        return findAll(WEBCHAT_BOT_AUTHRESULT_ELEMENT.BLOCK_ID.in(idList));
    }

    public Integer insert(Integer blockId, WebchatBotFormRequest.AuthResultElement request) {
        return dsl.insertInto(WEBCHAT_BOT_AUTHRESULT_ELEMENT)
                .set(WEBCHAT_BOT_AUTHRESULT_ELEMENT.BLOCK_ID, blockId)
                .set(WEBCHAT_BOT_AUTHRESULT_ELEMENT.RESULT_VALUE, request.getValue())
                .set(WEBCHAT_BOT_AUTHRESULT_ELEMENT.RESULT_MENT, request.getMent())
                .set(WEBCHAT_BOT_AUTHRESULT_ELEMENT.ACTION, request.getAction().getCode())
                .set(WEBCHAT_BOT_AUTHRESULT_ELEMENT.NEXT_ACTION_DATA, request.getNextActionData())
                .set(WEBCHAT_BOT_AUTHRESULT_ELEMENT.NEXT_API_MENT, request.getNextApiMent())
                .set(WEBCHAT_BOT_AUTHRESULT_ELEMENT.IS_RESULT_TPL_ENABLE, request.getEnableResultTemplate() != null && request.getEnableResultTemplate() ? "Y" : "N")
                .set(WEBCHAT_BOT_AUTHRESULT_ELEMENT.NEXT_API_RESULT_TPL, request.getNextApiResultTemplate())
                .set(WEBCHAT_BOT_AUTHRESULT_ELEMENT.NEXT_API_NO_RESULT_MENT, request.getNextApiNoResultMent())
                .set(WEBCHAT_BOT_AUTHRESULT_ELEMENT.NEXT_API_ERROR_MENT, request.getNextApiErrorMent())
                .set(WEBCHAT_BOT_AUTHRESULT_ELEMENT.COMPANY_ID, getCompanyId())
                .returning()
                .fetchOne()
                .value1();
    }

    public void updateBlockId(Integer elementId, Integer blockId) {
        dsl.update(WEBCHAT_BOT_AUTHRESULT_ELEMENT)
                .set(WEBCHAT_BOT_AUTHRESULT_ELEMENT.NEXT_ACTION_DATA, String.valueOf(blockId))
                .where(compareCompanyId())
                .and(WEBCHAT_BOT_AUTHRESULT_ELEMENT.ID.eq(elementId))
                .execute();
    }

    public void deleteByBlockIdList(List<Integer> blockIdList) {
        if (blockIdList != null && blockIdList.size() > 0)
            dsl.deleteFrom(WEBCHAT_BOT_AUTHRESULT_ELEMENT)
                    .where(WEBCHAT_BOT_AUTHRESULT_ELEMENT.BLOCK_ID.in(blockIdList))
                    .execute();
    }
}
