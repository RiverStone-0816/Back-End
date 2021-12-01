package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.WebchatServiceInfo;
import kr.co.eicn.ippbx.model.form.WebchatServiceInfoFormRequest;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.WebchatServiceInfo.WEBCHAT_SERVICE_INFO;

@Getter
@Repository
public class WebchatServiceInfoRepository extends EicnBaseRepository<WebchatServiceInfo, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatServiceInfo, Integer> {
    protected Logger logger = LoggerFactory.getLogger(WebchatServiceInfoRepository.class);

    public WebchatServiceInfoRepository() {
        super(WEBCHAT_SERVICE_INFO, WEBCHAT_SERVICE_INFO.SEQ, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatServiceInfo.class);
    }

    public void insert(WebchatServiceInfoFormRequest form) {
        dsl.insertInto(WEBCHAT_SERVICE_INFO)
                .set(WEBCHAT_SERVICE_INFO.COMPANY_ID, getCompanyId())
                .set(WEBCHAT_SERVICE_INFO.WEBCHAT_SERVICE_NAME, form.getChannelName())
                .set(WEBCHAT_SERVICE_INFO.SENDER_KEY, form.getSenderKey())
                .set(WEBCHAT_SERVICE_INFO.IS_CHATT_ENABLE, form.getEnableChat() != null && form.getEnableChat() ? "Y" : "N")
                .set(WEBCHAT_SERVICE_INFO.DISPLAY_COMPANY_NAME, form.getDisplayCompanyName())
                .set(WEBCHAT_SERVICE_INFO.MSG, form.getMessage())
                .set(WEBCHAT_SERVICE_INFO.IMAGE, form.getImage())
                .set(WEBCHAT_SERVICE_INFO.BGCOLOR, form.getBackgroundColor())
                .set(WEBCHAT_SERVICE_INFO.PROFILE, form.getProfile())
                .execute();
    }

    public void update(Integer seq, WebchatServiceInfoFormRequest form) {
        dsl.update(WEBCHAT_SERVICE_INFO)
                .set(WEBCHAT_SERVICE_INFO.WEBCHAT_SERVICE_NAME, form.getChannelName())
                .set(WEBCHAT_SERVICE_INFO.SENDER_KEY, form.getSenderKey())
                .set(WEBCHAT_SERVICE_INFO.IS_CHATT_ENABLE, form.getEnableChat() != null && form.getEnableChat() ? "Y" : "N")
                .set(WEBCHAT_SERVICE_INFO.DISPLAY_COMPANY_NAME, form.getDisplayCompanyName())
                .set(WEBCHAT_SERVICE_INFO.MSG, form.getMessage())
                .set(WEBCHAT_SERVICE_INFO.IMAGE, form.getImage())
                .set(WEBCHAT_SERVICE_INFO.BGCOLOR, form.getBackgroundColor())
                .set(WEBCHAT_SERVICE_INFO.PROFILE, form.getProfile())
                .where(WEBCHAT_SERVICE_INFO.SEQ.eq(seq))
                .and(compareCompanyId())
                .execute();
    }
}
