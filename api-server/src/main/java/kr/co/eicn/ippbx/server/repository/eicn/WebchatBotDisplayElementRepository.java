package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.WebchatBotDispElement;
import kr.co.eicn.ippbx.model.form.WebchatBotDisplayElementFormRequest;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.WEBCHAT_BOT_DISP_ELEMENT;

@Getter
@Repository
public class WebchatBotDisplayElementRepository extends EicnBaseRepository<WebchatBotDispElement, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotDispElement, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(WebchatBotDisplayElementRepository.class);

    public WebchatBotDisplayElementRepository() {
        super(WEBCHAT_BOT_DISP_ELEMENT, WEBCHAT_BOT_DISP_ELEMENT.ID, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotDispElement.class);
    }

    public void insert(WebchatBotDisplayElementFormRequest request) {
        dsl.insertInto(WEBCHAT_BOT_DISP_ELEMENT)
                .set(WEBCHAT_BOT_DISP_ELEMENT.DISPLAY_ID, request.getDisplayId())
                .set(WEBCHAT_BOT_DISP_ELEMENT.SEQUENCE, request.getOrder())
                .set(WEBCHAT_BOT_DISP_ELEMENT.TITLE, request.getTitle())
                .set(WEBCHAT_BOT_DISP_ELEMENT.CONTENT, request.getContent())
                .set(WEBCHAT_BOT_DISP_ELEMENT.IMAGE, request.getImage())
                .set(WEBCHAT_BOT_DISP_ELEMENT.URL, request.getUrl())
                .set(WEBCHAT_BOT_DISP_ELEMENT.COMPANY_ID, getCompanyId())
                .execute();
    }

    public void deleteByDisplayIdList(List<Integer> displayIdList) {
        dsl.deleteFrom(WEBCHAT_BOT_DISP_ELEMENT)
                .where(compareCompanyId())
                .and(WEBCHAT_BOT_DISP_ELEMENT.DISPLAY_ID.in(displayIdList))
                .execute();
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotDispElement> findAllInDisplayIdList(List<Integer> displayIdList) {
        return findAll(WEBCHAT_BOT_DISP_ELEMENT.DISPLAY_ID.in(displayIdList));
    }
}
