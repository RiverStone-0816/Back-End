package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.CommonCode;
import kr.co.eicn.ippbx.model.form.CommonCodeFormRequest;
import kr.co.eicn.ippbx.model.search.StatQaResultIndividualSearchRequest;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.CommonCode.COMMON_CODE;

@Getter
@Repository
public class CommonCodeRepository extends EicnBaseRepository<CommonCode, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonCode, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(CommonCodeRepository.class);

    public CommonCodeRepository() {
        super(COMMON_CODE, COMMON_CODE.SEQ, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonCode.class);
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonCode> findAllByType(Integer type) {
        return findAll(COMMON_CODE.TYPE.eq(type));
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonCode> findAllByTypeField(Integer type, String fieldId) {
        orderByFields.add(COMMON_CODE.SEQUENCE.asc());
        return findAll(COMMON_CODE.TYPE.eq(type).and(COMMON_CODE.FIELD_ID.eq(fieldId)));
    }

    public void deleteByKeyType(Integer type, String fieldId) {
        dsl.deleteFrom(COMMON_CODE)
                .where(compareCompanyId())
                .and(COMMON_CODE.TYPE.eq(type))
                .and(COMMON_CODE.FIELD_ID.eq(fieldId))
                .execute();
    }

    public void insertCodes(Integer type, String fieldId, List<CommonCodeFormRequest> codes) {
        insertCodes(type, fieldId, "", codes);
    }

    public void insertCodes(Integer type, String fieldId, String relatedFieldId, List<CommonCodeFormRequest> codes) {
        for (CommonCodeFormRequest code : codes) {
            dsl.insertInto(COMMON_CODE)
                    .set(COMMON_CODE.TYPE, type)
                    .set(COMMON_CODE.FIELD_ID, fieldId)
                    .set(COMMON_CODE.RELATED_FIELD_ID, StringUtils.isNotEmpty(relatedFieldId) ? relatedFieldId : "")
                    .set(COMMON_CODE.CODE_ID, code.getCodeId())
                    .set(COMMON_CODE.CODE_NAME, code.getCodeName())
                    .set(COMMON_CODE.COMPANY_ID, getCompanyId())
                    .set(COMMON_CODE.SEQUENCE, code.getSequence())
                    .set(COMMON_CODE.HIDE, code.getHide())
                    .set(COMMON_CODE.SCRIPT, code.getScript())
                    .execute();
        }
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonCode> individualCodeList(StatQaResultIndividualSearchRequest search) {
        final CommonCode a = COMMON_CODE.as("A");
        final CommonCode b = COMMON_CODE.as("B");

        Condition condition = a.RELATED_FIELD_ID.eq("");

        if (StringUtils.isNotEmpty(search.getFieldId()))
            condition = condition.and(a.FIELD_ID.eq(search.getFieldId()));

        return dsl.select(a.fields())
                .from(a)
                .where(a.COMPANY_ID.eq(g.getUser().getCompanyId()))
                .and((dsl.selectCount()
                        .from(b)
                        .where(b.COMPANY_ID.eq(g.getUser().getCompanyId()))
                        .and(b.TYPE.eq(a.TYPE))
                        .and(b.RELATED_FIELD_ID.eq(a.FIELD_ID))).asField().eq(0))
                .and(condition)
                .fetchInto(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonCode.class);
    }
}
