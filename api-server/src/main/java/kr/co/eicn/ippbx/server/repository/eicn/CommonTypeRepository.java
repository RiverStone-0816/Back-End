package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.CommonField;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.CommonType;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CommonBasicField;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CommonCode;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.records.CommonTypeRecord;
import kr.co.eicn.ippbx.server.model.entity.eicn.CommonCodeEntity;
import kr.co.eicn.ippbx.server.model.entity.eicn.CommonFieldEntity;
import kr.co.eicn.ippbx.server.model.entity.eicn.CommonTypeEntity;
import kr.co.eicn.ippbx.server.model.enums.CommonTypeKind;
import kr.co.eicn.ippbx.server.model.enums.CommonTypeStatus;
import kr.co.eicn.ippbx.server.model.form.CommonFieldFormRequest;
import kr.co.eicn.ippbx.server.model.form.CommonTypeFormRequest;
import kr.co.eicn.ippbx.server.model.form.CommonTypeUpdateFormRequest;
import kr.co.eicn.ippbx.server.util.ReflectionUtils;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SelectConditionStep;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.server.jooq.eicn.Tables.COMMON_CODE;
import static kr.co.eicn.ippbx.server.jooq.eicn.tables.CommonField.COMMON_FIELD;
import static kr.co.eicn.ippbx.server.jooq.eicn.tables.CommonType.COMMON_TYPE;

@Getter
@Repository
public class CommonTypeRepository extends EicnBaseRepository<CommonType, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CommonType, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(CommonTypeRepository.class);

    private final CommonBasicFieldRepository commonBasicFieldRepository;

    public CommonTypeRepository(CommonBasicFieldRepository commonBasicFieldRepository) {
        super(COMMON_TYPE, COMMON_TYPE.SEQ, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CommonType.class);
        this.commonBasicFieldRepository = commonBasicFieldRepository;
    }

    public Integer nextSequence(Integer seq) {
        final CommonField sequenceSeed = COMMON_FIELD.as("SEQUENCE_SEED");
        return dsl.select(DSL.ifnull(DSL.max(sequenceSeed.DISPLAY_SEQ), 0).add(1)).from(sequenceSeed.as("SEQUENCE_SEED")).where(sequenceSeed.TYPE.eq(seq)).fetchOneInto(Integer.class);
    }

    public List<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CommonType> findAllByKind(String kind) {
        return findAll(COMMON_TYPE.KIND.eq(kind).and(COMMON_TYPE.STATUS.eq("U")));
    }

    public List<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CommonType> findAllType() {
        return findAll(COMMON_TYPE.STATUS.eq("U").and(COMMON_TYPE.KIND.eq(CommonTypeKind.MAIN_DB.getCode()).or(COMMON_TYPE.KIND.eq(CommonTypeKind.LINK_DB.getCode())).or(COMMON_TYPE.KIND.eq(CommonTypeKind.CONSULTATION_RESULTS.getCode()))));
    }

    public List<CommonTypeEntity> getCommonTypeLists(final String kind) {
        final SelectConditionStep<Record> query = dsl.select()
                .from(COMMON_TYPE)
                .leftOuterJoin(COMMON_FIELD)
                .on(COMMON_TYPE.SEQ.eq(COMMON_FIELD.TYPE))
                .where(compareCompanyId())
                .and(COMMON_TYPE.STATUS.eq(CommonTypeStatus.USING.getCode()));

        query.and(COMMON_TYPE.KIND.eq(kind));

        final Map<CommonTypeRecord, Result<Record>> recordResultMap = query.orderBy(COMMON_TYPE.NAME.desc(), COMMON_FIELD.DISPLAY_SEQ)
                .fetch()
                .intoGroups(COMMON_TYPE);

        final List<CommonTypeEntity> commonTypeEntities = new ArrayList<>();

        recordResultMap.forEach(((record, records) -> {
            final CommonTypeEntity entity = record.into(COMMON_TYPE).into(CommonTypeEntity.class);
            entity.setFields(records.stream()
                    .filter(r -> r.getValue(COMMON_FIELD.SEQ) != null)
                    .map(r -> r.into(COMMON_FIELD).into(CommonFieldEntity.class))
                    .collect(Collectors.toList())
            );
            commonTypeEntities.add(entity);
        }));

        return commonTypeEntities;
    }

    public CommonTypeEntity getCommonType(Integer seq) {
        final kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CommonType commonType =
                findOne(COMMON_TYPE.STATUS.eq(CommonTypeStatus.USING.getCode()).and(COMMON_TYPE.SEQ.eq(seq)));
        final CommonTypeEntity entity = new CommonTypeEntity();
        ReflectionUtils.copy(entity, commonType);

        entity.setFields(dsl.select()
                .from(COMMON_FIELD)
                .where(COMMON_FIELD.COMPANY_ID.eq(getCompanyId()))
                .and(COMMON_FIELD.TYPE.eq(entity.getSeq()))
                .orderBy(COMMON_FIELD.DISPLAY_SEQ)
                .fetch(record -> record.into(COMMON_FIELD).into(CommonFieldEntity.class))
        );

        final Map<String, List<CommonCodeEntity>> codeByFieldMap = dsl.select()
                .from(COMMON_CODE)
                .where(COMMON_CODE.TYPE.eq(entity.getSeq()))
                .orderBy(COMMON_CODE.SEQUENCE.asc())
                .fetchInto(CommonCodeEntity.class)
                .stream()
                .collect(Collectors.groupingBy(CommonCode::getFieldId));

        for (CommonFieldEntity field : entity.getFields()) {
            if (codeByFieldMap.containsKey(field.getFieldId()))
                field.setCodes(codeByFieldMap.get(field.getFieldId()));
            else
                field.setCodes(new ArrayList<>());
        }

        return entity;
    }

    public void insert(CommonTypeFormRequest form) {
        final kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CommonType record = new kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CommonType();
        record.setName(form.getName());
        record.setEtc(form.getEtc());
        record.setKind(form.getKind());
        record.setStatus(CommonTypeStatus.USING.getCode());
        record.setCompanyId(getCompanyId());
        if (StringUtils.isNotEmpty(form.getPurpose()))
            record.setPurpose(form.getPurpose());
        record.setType(form.getType() != null ? form.getType() : "");

        super.insert(record);
    }

    public void update(CommonTypeUpdateFormRequest form, Integer seq) {
        final CommonTypeEntity commonType = getCommonType(seq);

        dsl.update(COMMON_TYPE)
                .set(dsl.newRecord(COMMON_TYPE, form))
                .where(compareCompanyId())
                .and(COMMON_TYPE.SEQ.eq(seq))
                .execute();

        commonType.getFields().stream()
                .filter(e -> form.getFieldFormRequests().stream().noneMatch(data -> e.getFieldId().equals(commonType.getKind() + "_" + data.getId())))
                .forEach(e -> dsl.deleteFrom(COMMON_FIELD)
                        .where(COMMON_FIELD.SEQ.eq(e.getSeq()))
                        .and(COMMON_FIELD.COMPANY_ID.eq(getCompanyId()))
                        .execute()
                );

        final Map<String, CommonBasicField> basicFieldMap =
                commonBasicFieldRepository.findAllServiceKindAndInfo(commonType.getKind()).stream().collect(Collectors.toMap(CommonBasicField::getId, e -> e));

        int rowIndex = 0;
        int maxIndex = 0;

        for (int i = 0, length = form.getFieldFormRequests().size(); i < length; i++) {
            final CommonFieldFormRequest fieldFormRequest = form.getFieldFormRequests().get(i);

            final CommonBasicField commonBasicField = basicFieldMap.get(fieldFormRequest.getId());
            if (Objects.isNull(commonBasicField))
                continue;

            final String fieldId = commonType.getKind() + "_" + commonBasicField.getId();

            if (commonType.getFields().stream().anyMatch(e -> e.getFieldId().equals(fieldId))) {

                for (int k = 0; k < commonType.getFields().size(); k++) {
                    if (maxIndex < commonType.getFields().get(k).getDisplaySeq())
                        maxIndex = commonType.getFields().get(k).getDisplaySeq();

                    if (commonType.getFields().get(k).getFieldId().equals(fieldId)) {
                        rowIndex = commonType.getFields().get(k).getDisplaySeq();
                        break;
                    } else {
                        rowIndex = maxIndex + 1;
                    }
                }

                dsl.update(COMMON_FIELD)
                        .set(COMMON_FIELD.FIELD_INFO, fieldFormRequest.getFieldName())
                        .set(COMMON_FIELD.ISNEED, fieldFormRequest.getIsneed())
                        .set(COMMON_FIELD.ISENC, "N")
                        .set(COMMON_FIELD.ISDISPLAY, fieldFormRequest.getIsdisplay())
                        .set(COMMON_FIELD.ISDISPLAY_LIST, fieldFormRequest.getIsdisplayList())
                        .set(COMMON_FIELD.ISSEARCH, fieldFormRequest.getIssearch())
                        .set(COMMON_FIELD.FIELD_SIZE, fieldFormRequest.getFieldSize())
                        .set(COMMON_FIELD.DISPLAY_SEQ, rowIndex)
                        .where(COMMON_FIELD.COMPANY_ID.eq(getCompanyId()))
                        .and(COMMON_FIELD.FIELD_ID.eq(fieldId))
                        .and(COMMON_FIELD.TYPE.eq(seq))
                        .execute();
            }else {
                dsl.insertInto(COMMON_FIELD)
                        .set(COMMON_FIELD.TYPE, seq)
                        .set(COMMON_FIELD.FIELD_ID, fieldId)
                        .set(COMMON_FIELD.FIELD_NAME, commonBasicField.getName())
                        .set(COMMON_FIELD.FIELD_TYPE, commonBasicField.getType())
                        .set(COMMON_FIELD.FIELD_USE, "D")
                        .set(COMMON_FIELD.ISNEED, fieldFormRequest.getIsneed())
                        .set(COMMON_FIELD.ISENC, "N")
                        .set(COMMON_FIELD.ISDISPLAY, fieldFormRequest.getIsdisplay())
                        .set(COMMON_FIELD.ISDISPLAY_LIST, fieldFormRequest.getIsdisplayList())
                        .set(COMMON_FIELD.ISSEARCH, fieldFormRequest.getIssearch())
                        .set(COMMON_FIELD.FIELD_INFO, fieldFormRequest.getFieldName())
                        .set(COMMON_FIELD.FIELD_SIZE, fieldFormRequest.getFieldSize())
                        .set(COMMON_FIELD.DISPLAY_SEQ, nextSequence(seq))
                        .set(COMMON_FIELD.COMPANY_ID, getCompanyId())
                        .execute();
            }
        }
    }

    /**
     * 유형 삭제
     */
    public void updateStatus(Integer seq) {
        findOneIfNullThrow(seq);

        dsl.update(COMMON_TYPE)
                .set(COMMON_TYPE.STATUS, CommonTypeStatus.DELETE.getCode())
                .where(compareCompanyId())
                .and(COMMON_TYPE.SEQ.eq(seq))
                .execute();
    }

    public List<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CommonType> findByKindStatus(CommonTypeKind kind, CommonTypeStatus status) {
        return findAll(COMMON_TYPE.KIND.eq(kind.getCode()).and(COMMON_TYPE.STATUS.eq(status.getCode())));
    }
}
