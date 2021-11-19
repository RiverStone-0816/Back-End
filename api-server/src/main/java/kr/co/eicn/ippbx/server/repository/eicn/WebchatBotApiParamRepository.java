package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.WebchatBotApiParam;
import kr.co.eicn.ippbx.model.form.WebchatBotApiParamFormRequest;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.WebchatBotApiParam.WEBCHAT_BOT_API_PARAM;

@Getter
@Repository
public class WebchatBotApiParamRepository extends EicnBaseRepository<WebchatBotApiParam, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotApiParam, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(WebchatBotApiParamRepository.class);

    public WebchatBotApiParamRepository() {
        super(WEBCHAT_BOT_API_PARAM, WEBCHAT_BOT_API_PARAM.ID, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotApiParam.class);
    }

    public void insert(WebchatBotApiParamFormRequest request) {
        dsl.insertInto(WEBCHAT_BOT_API_PARAM)
                .set(WEBCHAT_BOT_API_PARAM.BTN_ID, request.getButtonId())
                .set(WEBCHAT_BOT_API_PARAM.TYPE, request.getType().getCode())
                .set(WEBCHAT_BOT_API_PARAM.PARAM_NAME, request.getParamName())
                .set(WEBCHAT_BOT_API_PARAM.DISPLAY_NAME, request.getDisplayName())
                .set(WEBCHAT_BOT_API_PARAM.COMPANY_ID, getCompanyId())
                .execute();
    }

    public void deleteByButtonIdList(List<Integer> buttonIdList) {
        dsl.deleteFrom(WEBCHAT_BOT_API_PARAM)
                .where(compareCompanyId())
                .and(WEBCHAT_BOT_API_PARAM.BTN_ID.in(buttonIdList))
                .execute();
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotApiParam> findAllInButtonIdList(List<Integer> buttonIdList) {
        return findAll(WEBCHAT_BOT_API_PARAM.BTN_ID.in(buttonIdList));
    }
}
