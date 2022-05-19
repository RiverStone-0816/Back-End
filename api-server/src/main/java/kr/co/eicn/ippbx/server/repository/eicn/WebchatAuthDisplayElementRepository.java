package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatAuthDispElement;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.records.WebchatAuthDispElementRecord;
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

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.WEBCHAT_AUTH_DISP_ELEMENT;

@Getter
@Repository
public class WebchatAuthDisplayElementRepository extends EicnBaseRepository<kr.co.eicn.ippbx.meta.jooq.eicn.tables.WebchatAuthDispElement, WebchatAuthDispElement, Integer> {
    private final Logger logger = LoggerFactory.getLogger(WebchatAuthDisplayElementRepository.class);

    public WebchatAuthDisplayElementRepository() {
        super(WEBCHAT_AUTH_DISP_ELEMENT, WEBCHAT_AUTH_DISP_ELEMENT.ID, WebchatAuthDispElement.class);
    }

    public Map<Integer, List<WebchatAuthDispElement>> findAllByBlockIdList(List<Integer> blockIdList) {
        return findAll(WEBCHAT_AUTH_DISP_ELEMENT.AUTH_BLOCK_ID.in(blockIdList)).stream().collect(Collectors.groupingBy(WebchatAuthDispElement::getAuthBlockId));
    }

    public void deleteByBlockIdList(Set<Integer> blockIdList) {
        delete(WEBCHAT_AUTH_DISP_ELEMENT.AUTH_BLOCK_ID.in(blockIdList));
    }

    public void insert(Integer blockId, List<WebchatAuthBlocKFormRequest.AuthParamInfo> form) {
        if (form.size() > 0) {
            InsertValuesStep7<WebchatAuthDispElementRecord, Integer, Integer, String, String, String, String, String> query = dsl.insertInto(WEBCHAT_AUTH_DISP_ELEMENT, WEBCHAT_AUTH_DISP_ELEMENT.AUTH_BLOCK_ID, WEBCHAT_AUTH_DISP_ELEMENT.SEQUENCE, WEBCHAT_AUTH_DISP_ELEMENT.INPUT_TYPE, WEBCHAT_AUTH_DISP_ELEMENT.INPUT_PARAM_NAME, WEBCHAT_AUTH_DISP_ELEMENT.INPUT_DISPLAY_NAME, WEBCHAT_AUTH_DISP_ELEMENT.INPUT_NEED_YN, WEBCHAT_AUTH_DISP_ELEMENT.COMPANY_ID);

            for (int i = 0; i < form.size(); i++) {
                WebchatAuthBlocKFormRequest.AuthParamInfo param = form.get(i);

                query.values(blockId, i, param.getType().getCode(), param.getParamName(), param.getName(), param.getNeedYn() ? "Y" : "N", g.getUser().getCompanyId());
            }

            query.execute();
        }
    }
}
