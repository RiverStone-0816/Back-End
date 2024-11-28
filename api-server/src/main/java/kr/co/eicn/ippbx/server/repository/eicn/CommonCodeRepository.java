package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.CommonCode;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.CommonField;
import kr.co.eicn.ippbx.model.form.CommonCodeFormRequest;
import kr.co.eicn.ippbx.model.search.StatQaResultSearchRequest;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.CommonCode.COMMON_CODE;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.CommonField.COMMON_FIELD;

@Getter
@Repository
public class CommonCodeRepository extends EicnBaseRepository<CommonCode, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonCode, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(CommonCodeRepository.class);

    public CommonCodeRepository() {
        super(COMMON_CODE, COMMON_CODE.SEQ, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonCode.class);
        orderByFields.add(COMMON_CODE.SEQUENCE.asc());
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonCode> findAllByType(Integer type) {
        return findAll(COMMON_CODE.TYPE.eq(type));
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonCode> findAllByTypeField(Integer type, String fieldId) {
        return findAll(COMMON_CODE.TYPE.eq(type).and(COMMON_CODE.FIELD_ID.eq(fieldId)));
    }

    public void deleteByKeyType(Integer type, String fieldId) {
        dsl.deleteFrom(COMMON_CODE)
                .where(compareCompanyId())
                .and(COMMON_CODE.TYPE.eq(type))
                .and(COMMON_CODE.FIELD_ID.eq(fieldId))
                .execute();
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

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonCode> individualCodeList(StatQaResultSearchRequest search) {
        final CommonCode codeTableA = COMMON_CODE.as("A");
        final CommonCode codeTableB = COMMON_CODE.as("B");
        final CommonField fieldTable = COMMON_FIELD.as("C");

        final List<Condition> conditions = new ArrayList<>();
        conditions.add(codeTableA.COMPANY_ID.eq(g.getUser().getCompanyId()));
        conditions.add(codeTableA.RELATED_FIELD_ID.eq(""));

        if (StringUtils.isNotEmpty(search.getFieldId()))
            conditions.add(codeTableA.FIELD_ID.eq(search.getFieldId()));

        if (ObjectUtils.isNotEmpty(search.getResultType()))
            conditions.add(codeTableA.TYPE.eq(search.getResultType()));

        conditions.add(codeTableA.FIELD_ID.like("RS_CODE_%"));
        conditions.add((dsl.selectCount()
                .from(codeTableB)
                .where(codeTableB.COMPANY_ID.eq(g.getUser().getCompanyId()))
                .and(codeTableB.TYPE.eq(codeTableA.TYPE))
                .and(codeTableB.RELATED_FIELD_ID.eq(codeTableA.FIELD_ID))).asField().eq(0));

        return dsl.select(codeTableA.fields())
                .from(codeTableA)
                .join(fieldTable)
                .on(fieldTable.TYPE.eq(codeTableA.TYPE).and(fieldTable.FIELD_ID.eq(codeTableA.FIELD_ID)))
                .where(conditions)
                .orderBy(codeTableA.TYPE, fieldTable.DISPLAY_SEQ, codeTableA.SEQUENCE)
                .fetchInto(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonCode.class);
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonCode> individualFieldList(StatQaResultSearchRequest search) {
        final CommonCode codeTableA = COMMON_CODE.as("A");
        final CommonCode codeTableB = COMMON_CODE.as("B");

        final List<Condition> conditions = new ArrayList<>();
        conditions.add(codeTableA.COMPANY_ID.eq(g.getUser().getCompanyId()));
        conditions.add(codeTableA.RELATED_FIELD_ID.eq(""));
        conditions.add(codeTableA.FIELD_ID.like("RS_CODE_%"));

        conditions.add((dsl.selectCount()
                .from(codeTableB)
                .where(codeTableB.COMPANY_ID.eq(g.getUser().getCompanyId()))
                .and(codeTableB.TYPE.eq(codeTableA.TYPE))
                .and(codeTableB.RELATED_FIELD_ID.eq(codeTableA.FIELD_ID))).asField().eq(0));

        return dsl.select(codeTableA.fields())
                .from(codeTableA)
                .where(conditions)
                .groupBy(codeTableA.TYPE, codeTableA.FIELD_ID)
                .orderBy(codeTableA.TYPE, codeTableA.FIELD_ID, codeTableA.SEQUENCE)
                .fetchInto(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonCode.class);
    }
}
