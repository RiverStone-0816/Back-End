package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.SendCategory;
import kr.co.eicn.ippbx.model.enums.SendCategoryType;
import kr.co.eicn.ippbx.model.form.SendFaxEmailCategoryFormRequest;
import kr.co.eicn.ippbx.model.search.SendCategorySearchRequest;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.jooq.Condition;
import org.jooq.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.SendCategory.SEND_CATEGORY;

@Getter
@Repository
public class SendFaxEmailCategoryRepository extends EicnBaseRepository<SendCategory, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SendCategory, String> {
    protected final Logger logger = LoggerFactory.getLogger(SendFaxEmailCategoryRepository.class);

    public SendFaxEmailCategoryRepository() {
        super(SEND_CATEGORY, SEND_CATEGORY.CATEGORY_CODE, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SendCategory.class);
    }

    public Pagination<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SendCategory> pagination(SendCategorySearchRequest search) {
        orderByFields.add(SEND_CATEGORY.CATEGORY_CODE.asc());
        return super.pagination(search, conditions(search));
    }

    public Record insertOnGeneratedKey(SendFaxEmailCategoryFormRequest formRequest) {
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SendCategory category = findOne(SEND_CATEGORY.CATEGORY_CODE.eq(formRequest.getCategoryCode()));
        if (category != null)
            throw new IllegalArgumentException("이미 등록된 카테고리 입니다.");

        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SendCategory sendCategoryRecord = new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SendCategory();
        sendCategoryRecord.setCategoryCode(formRequest.getCategoryCode());
        sendCategoryRecord.setCategoryName(formRequest.getCategoryName());
        sendCategoryRecord.setCategoryType(String.valueOf(formRequest.getCategoryType()));
        sendCategoryRecord.setCompanyId(getCompanyId());

        return super.insertOnGeneratedKey(sendCategoryRecord);
    }

    private List<Condition> conditions(SendCategorySearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();
        conditions.add(SEND_CATEGORY.CATEGORY_TYPE.notEqual(SendCategoryType.SMS.getCode()));

        return conditions;
    }
}
