package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.SendMessageTemplate;
import kr.co.eicn.ippbx.model.form.SendMessageTemplateFormRequest;
import kr.co.eicn.ippbx.model.search.SendMessageTemplateSearchRequest;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.jooq.Condition;
import org.jooq.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.SendMessageTemplate.SEND_MESSAGE_TEMPLATE;

@Getter
@Repository
public class SendMessageTemplateRepository extends EicnBaseRepository<SendMessageTemplate, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SendMessageTemplate, Long> {
    protected final Logger logger = LoggerFactory.getLogger(SendSmsCategoryRepository.class);

    private final SendSmsCategoryRepository sendSmsCategoryRepository;

    SendMessageTemplateRepository(SendSmsCategoryRepository sendSmsCategoryRepository) {
        super(SEND_MESSAGE_TEMPLATE, SEND_MESSAGE_TEMPLATE.ID, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SendMessageTemplate.class);
        this.sendSmsCategoryRepository = sendSmsCategoryRepository;
    }

    public Pagination<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SendMessageTemplate> pagination(SendMessageTemplateSearchRequest search) {
        return super.pagination(search, conditions(search));
    }

    public Record insertOnGeneratedKey(SendMessageTemplateFormRequest formRequest) {
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SendMessageTemplate sendRecord = new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SendMessageTemplate();
        sendRecord.setCategoryCode(formRequest.getCategoryCode());
        sendRecord.setCompanyId(getCompanyId());
        sendRecord.setContent(formRequest.getContent());

        final Record record = super.insertOnGeneratedKey(sendRecord);

        return record;
    }

    private List<Condition> conditions(SendMessageTemplateSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        return conditions;
    }

}
