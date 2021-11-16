package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.WebchatBotBtnElement;
import kr.co.eicn.ippbx.model.form.WebchatBotButtonElementFormRequest;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.WEBCHAT_BOT_BTN_ELEMENT;

@Getter
@Repository
public class WebchatBotButtonElementRepository extends EicnBaseRepository<WebchatBotBtnElement, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotBtnElement, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(WebchatBotButtonElementRepository.class);

    public WebchatBotButtonElementRepository() {
        super(WEBCHAT_BOT_BTN_ELEMENT, WEBCHAT_BOT_BTN_ELEMENT.SEQ, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotBtnElement.class);
    }

    public void insert(WebchatBotButtonElementFormRequest request) {
        dsl.insertInto(WEBCHAT_BOT_BTN_ELEMENT)
                .set(WEBCHAT_BOT_BTN_ELEMENT.BTN_ID, request.getButtonId())
                .set(WEBCHAT_BOT_BTN_ELEMENT.BLOCK_ID, request.getBlockId())
                .set(WEBCHAT_BOT_BTN_ELEMENT.ORDER, request.getOrder())
                .set(WEBCHAT_BOT_BTN_ELEMENT.BTN_NAME, request.getButtonName())
                .set(WEBCHAT_BOT_BTN_ELEMENT.ACTION, request.getAction().getCode())
                .set(WEBCHAT_BOT_BTN_ELEMENT.NEXT_BLOCK_ID, request.getNextBlockId())
                .set(WEBCHAT_BOT_BTN_ELEMENT.NEXT_GROUP_ID, request.getNextGroupId())
                .set(WEBCHAT_BOT_BTN_ELEMENT.NEXT_URL, request.getNextUrl())
                .set(WEBCHAT_BOT_BTN_ELEMENT.NEXT_PHONE, request.getNextPhone())
                .set(WEBCHAT_BOT_BTN_ELEMENT.NEXT_API_URL, request.getNextApiUrl())
                .set(WEBCHAT_BOT_BTN_ELEMENT.NEXT_API_MENT, request.getNextApiMent())
                .set(WEBCHAT_BOT_BTN_ELEMENT.IS_RESULT_TPL_ENABLE, request.getIsResultTemplateEnable() != null && request.getIsResultTemplateEnable() ? "Y" : "N")
                .set(WEBCHAT_BOT_BTN_ELEMENT.NEXT_API_RESULT_TPL, request.getNextApiResultTemplate())
                .set(WEBCHAT_BOT_BTN_ELEMENT.NEXT_API_NO_RESULT_MENT, request.getNextApiNoResultMent())
                .set(WEBCHAT_BOT_BTN_ELEMENT.NEXT_API_ERROR_MENT, request.getNextApiErrorMent())
                .set(WEBCHAT_BOT_BTN_ELEMENT.COMPANY_ID, getCompanyId())
                .execute();
    }

    public List<Integer> findIdListByBlockIdList(List<Integer> blockIdList) {
        return dsl.select(WEBCHAT_BOT_BTN_ELEMENT.BTN_ID)
                .from(WEBCHAT_BOT_BTN_ELEMENT)
                .where(compareCompanyId())
                .and(WEBCHAT_BOT_BTN_ELEMENT.BLOCK_ID.in(blockIdList))
                .fetchInto(Integer.class);
    }

    public void deleteByBlockIdList(List<Integer> blockIdList) {
        dsl.deleteFrom(WEBCHAT_BOT_BTN_ELEMENT)
                .where(compareCompanyId())
                .and(WEBCHAT_BOT_BTN_ELEMENT.BLOCK_ID.in(blockIdList))
                .execute();
    }
}
