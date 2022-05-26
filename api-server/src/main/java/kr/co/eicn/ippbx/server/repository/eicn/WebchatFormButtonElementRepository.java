package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatFormBtnElement;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.records.WebchatFormBtnElementRecord;
import kr.co.eicn.ippbx.model.form.WebchatFormBlocKFormRequest;
import lombok.Getter;
import org.jooq.InsertValuesStep7;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.WEBCHAT_FORM_BTN_ELEMENT;

@Getter
@Repository
public class WebchatFormButtonElementRepository extends EicnBaseRepository<kr.co.eicn.ippbx.meta.jooq.eicn.tables.WebchatFormBtnElement, WebchatFormBtnElement, Integer> {
    private final Logger logger = LoggerFactory.getLogger(WebchatFormButtonElementRepository.class);

    public WebchatFormButtonElementRepository() {
        super(WEBCHAT_FORM_BTN_ELEMENT, WEBCHAT_FORM_BTN_ELEMENT.ID, WebchatFormBtnElement.class);
        orderByFields.add(WEBCHAT_FORM_BTN_ELEMENT.SEQUENCE.asc());
    }

    public Map<Integer, List<WebchatFormBtnElement>> findAllByBlockIds(List<Integer> blockIds) {
        return findAll(WEBCHAT_FORM_BTN_ELEMENT.FORM_BLOCK_ID.in(blockIds)).stream().collect(Collectors.groupingBy(WebchatFormBtnElement::getFormBlockId));
    }

    public void deleteByBlockIdList(Set<Integer> blockIdList) {
        delete(WEBCHAT_FORM_BTN_ELEMENT.FORM_BLOCK_ID.in(blockIdList));
    }

    public void insert(Integer blockId, List<WebchatFormBlocKFormRequest.AuthButtonInfo> form) {
        if (form.size() > 0) {
            InsertValuesStep7<WebchatFormBtnElementRecord, Integer, Integer, String, String, String, String, String> query = dsl.insertInto(WEBCHAT_FORM_BTN_ELEMENT, WEBCHAT_FORM_BTN_ELEMENT.FORM_BLOCK_ID, WEBCHAT_FORM_BTN_ELEMENT.SEQUENCE, WEBCHAT_FORM_BTN_ELEMENT.BTN_NAME, WEBCHAT_FORM_BTN_ELEMENT.ACTION, WEBCHAT_FORM_BTN_ELEMENT.NEXT_ACTION_DATA, WEBCHAT_FORM_BTN_ELEMENT.API_RESULT_PARAM_NAME, WEBCHAT_FORM_BTN_ELEMENT.COMPANY_ID);

            for (int i = 0; i < form.size(); i++) {
                WebchatFormBlocKFormRequest.AuthButtonInfo button = form.get(i);

                query.values(blockId, i, button.getName(), button.getAction().getCode(), button.getActionData(), button.getResultParamName(), g.getUser().getCompanyId());
            }

            query.execute();
        }
    }
}
