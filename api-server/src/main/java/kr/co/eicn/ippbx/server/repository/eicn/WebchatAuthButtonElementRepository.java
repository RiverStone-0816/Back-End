package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatAuthBtnElement;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.records.WebchatAuthBtnElementRecord;
import kr.co.eicn.ippbx.model.form.WebchatAuthBlocKFormRequest;
import lombok.Getter;
import org.jooq.InsertValuesStep7;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.WEBCHAT_AUTH_BTN_ELEMENT;

@Getter
@Repository
public class WebchatAuthButtonElementRepository extends EicnBaseRepository<kr.co.eicn.ippbx.meta.jooq.eicn.tables.WebchatAuthBtnElement, WebchatAuthBtnElement, Integer> {
    private final Logger logger = LoggerFactory.getLogger(WebchatAuthButtonElementRepository.class);

    public WebchatAuthButtonElementRepository() {
        super(WEBCHAT_AUTH_BTN_ELEMENT, WEBCHAT_AUTH_BTN_ELEMENT.ID, WebchatAuthBtnElement.class);
        orderByFields.add(WEBCHAT_AUTH_BTN_ELEMENT.SEQUENCE.asc());
    }

    public Map<Integer, List<WebchatAuthBtnElement>> findAllByBlockIds(List<Integer> blockIds) {
        return findAll(WEBCHAT_AUTH_BTN_ELEMENT.AUTH_BLOCK_ID.in(blockIds)).stream().collect(Collectors.groupingBy(WebchatAuthBtnElement::getAuthBlockId));
    }

    public void deleteByBlockIdList(Set<Integer> blockIdList) {
        delete(WEBCHAT_AUTH_BTN_ELEMENT.AUTH_BLOCK_ID.in(blockIdList));
    }

    public void insert(Integer blockId, List<WebchatAuthBlocKFormRequest.AuthButtonInfo> form) {
        if (form.size() > 0) {
            InsertValuesStep7<WebchatAuthBtnElementRecord, Integer, Integer, String, String, String, String, String> query = dsl.insertInto(WEBCHAT_AUTH_BTN_ELEMENT, WEBCHAT_AUTH_BTN_ELEMENT.AUTH_BLOCK_ID, WEBCHAT_AUTH_BTN_ELEMENT.SEQUENCE, WEBCHAT_AUTH_BTN_ELEMENT.BTN_NAME, WEBCHAT_AUTH_BTN_ELEMENT.ACTION, WEBCHAT_AUTH_BTN_ELEMENT.NEXT_ACTION_DATA, WEBCHAT_AUTH_BTN_ELEMENT.API_RESULT_PARAM_NAME, WEBCHAT_AUTH_BTN_ELEMENT.COMPANY_ID);

            for (int i = 0; i < form.size(); i++) {
                WebchatAuthBlocKFormRequest.AuthButtonInfo button = form.get(i);

                query.values(blockId, i, button.getName(), button.getAction().getCode(), button.getActionData(), button.getResultParamName(), g.getUser().getCompanyId());
            }

            query.execute();
        }
    }
}
