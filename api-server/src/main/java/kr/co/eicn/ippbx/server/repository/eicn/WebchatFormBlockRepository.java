package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatFormBlock;
import kr.co.eicn.ippbx.model.form.WebchatFormBlocKFormRequest;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.WEBCHAT_FORM_BLOCK;

@Getter
@Repository
public class WebchatFormBlockRepository extends EicnBaseRepository<kr.co.eicn.ippbx.meta.jooq.eicn.tables.WebchatFormBlock, WebchatFormBlock, Integer> {
    private final Logger logger = LoggerFactory.getLogger(WebchatFormBlockRepository.class);

    public WebchatFormBlockRepository() {
        super(WEBCHAT_FORM_BLOCK, WEBCHAT_FORM_BLOCK.ID, WebchatFormBlock.class);
    }

    public Integer insert(WebchatFormBlocKFormRequest request) {
        return dsl.insertInto(WEBCHAT_FORM_BLOCK)
                .set(WEBCHAT_FORM_BLOCK.NAME, request.getName())
                .set(WEBCHAT_FORM_BLOCK.COMPANY_ID, g.getUser().getCompanyId())
                .returning(WEBCHAT_FORM_BLOCK.ID)
                .fetchOne()
                .value1();
    }

    public void update(Integer id, WebchatFormBlocKFormRequest request) {
        dsl.update(WEBCHAT_FORM_BLOCK)
                .set(WEBCHAT_FORM_BLOCK.NAME, request.getName())
                .where(WEBCHAT_FORM_BLOCK.ID.eq(id))
                .execute();
    }
}
