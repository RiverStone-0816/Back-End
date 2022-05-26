package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatFormDispElement;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.records.WebchatFormDispElementRecord;
import kr.co.eicn.ippbx.model.form.WebchatFormBlocKFormRequest;
import lombok.Getter;
import org.jooq.InsertValuesStep8;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.WEBCHAT_FORM_DISP_ELEMENT;

@Getter
@Repository
public class WebchatFormDisplayElementRepository extends EicnBaseRepository<kr.co.eicn.ippbx.meta.jooq.eicn.tables.WebchatFormDispElement, WebchatFormDispElement, Integer> {
    private final Logger logger = LoggerFactory.getLogger(WebchatFormDisplayElementRepository.class);

    public WebchatFormDisplayElementRepository() {
        super(WEBCHAT_FORM_DISP_ELEMENT, WEBCHAT_FORM_DISP_ELEMENT.ID, WebchatFormDispElement.class);
        orderByFields.add(WEBCHAT_FORM_DISP_ELEMENT.SEQUENCE.asc());
    }

    public Map<Integer, List<WebchatFormDispElement>> findAllByBlockIdList(List<Integer> blockIdList) {
        return findAll(WEBCHAT_FORM_DISP_ELEMENT.FORM_BLOCK_ID.in(blockIdList)).stream().collect(Collectors.groupingBy(WebchatFormDispElement::getFormBlockId));
    }

    public void deleteByBlockIdList(Set<Integer> blockIdList) {
        delete(WEBCHAT_FORM_DISP_ELEMENT.FORM_BLOCK_ID.in(blockIdList));
    }

    public void insert(Integer blockId, List<WebchatFormBlocKFormRequest.AuthParamInfo> form) {
        if (form.size() > 0) {
            InsertValuesStep8<WebchatFormDispElementRecord, Integer, Integer, String, String, String, String, String, String> query = dsl.insertInto(WEBCHAT_FORM_DISP_ELEMENT, WEBCHAT_FORM_DISP_ELEMENT.FORM_BLOCK_ID, WEBCHAT_FORM_DISP_ELEMENT.SEQUENCE, WEBCHAT_FORM_DISP_ELEMENT.INPUT_TYPE, WEBCHAT_FORM_DISP_ELEMENT.TITLE, WEBCHAT_FORM_DISP_ELEMENT.INPUT_PARAM_NAME, WEBCHAT_FORM_DISP_ELEMENT.INPUT_DISPLAY_NAME, WEBCHAT_FORM_DISP_ELEMENT.INPUT_NEED_YN, WEBCHAT_FORM_DISP_ELEMENT.COMPANY_ID);

            for (int i = 0; i < form.size(); i++) {
                WebchatFormBlocKFormRequest.AuthParamInfo param = form.get(i);

                query.values(blockId, i, param.getType().getCode(), param.getTitle(), param.getParamName(), param.getName(), param.getNeedYn() ? "Y" : "N", g.getUser().getCompanyId());
            }

            query.execute();
        }
    }
}
