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

    public void insert(Integer blockId, List<WebchatBotFormRequest.AuthResultElement> request) {
        if (request != null && request.size() > 0) {
            InsertValuesStep11<WebchatBotAuthresultElementRecord, Integer, String, String, String, String, String, String, String, String, String, String> query = dsl.insertInto(WEBCHAT_BOT_AUTHRESULT_ELEMENT, WEBCHAT_BOT_AUTHRESULT_ELEMENT.BLOCK_ID, WEBCHAT_BOT_AUTHRESULT_ELEMENT.RESULT_VALUE, WEBCHAT_BOT_AUTHRESULT_ELEMENT.RESULT_MENT, WEBCHAT_BOT_AUTHRESULT_ELEMENT.ACTION, WEBCHAT_BOT_AUTHRESULT_ELEMENT.NEXT_ACTION_DATA, WEBCHAT_BOT_AUTHRESULT_ELEMENT.NEXT_API_MENT, WEBCHAT_BOT_AUTHRESULT_ELEMENT.IS_RESULT_TPL_ENABLE, WEBCHAT_BOT_AUTHRESULT_ELEMENT.NEXT_API_RESULT_TPL, WEBCHAT_BOT_AUTHRESULT_ELEMENT.NEXT_API_NO_RESULT_MENT, WEBCHAT_BOT_AUTHRESULT_ELEMENT.NEXT_API_ERROR_MENT, WEBCHAT_BOT_AUTHRESULT_ELEMENT.COMPANY_ID);

            request.forEach(e -> query.values(
                    blockId,
                    e.getValue(),
                    e.getMent(),
                    e.getAction(),
                    e.getNextActionData(),
                    e.getNextApiMent(),
                    e.getEnableResultTemplate() ? "Y" : "N",
                    e.getNextApiResultTemplate(),
                    e.getNextApiNoResultMent(),
                    e.getNextApiErrorMent(),
                    getCompanyId()
            ));

            query.execute();
        }
    }

    public void deleteByBlockIdList(List<Integer> blockIdList) {
        if (blockIdList != null && blockIdList.size() > 0)
            dsl.deleteFrom(WEBCHAT_BOT_AUTHRESULT_ELEMENT)
                    .where(WEBCHAT_BOT_AUTHRESULT_ELEMENT.BLOCK_ID.in(blockIdList))
                    .execute();
    }
}
