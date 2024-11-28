package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.CommonField;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonCode;
import kr.co.eicn.ippbx.model.search.StatQaResultSearchRequest;
import lombok.Getter;
import org.jooq.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.CommonField.COMMON_FIELD;

@Getter
@Repository
public class CommonFieldRepository extends EicnBaseRepository<CommonField, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonField, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(CommonFieldRepository.class);

    private final CommonCodeRepository commonCodeRepository;

    public CommonFieldRepository(CommonCodeRepository commonCodeRepository) {
        super(COMMON_FIELD, COMMON_FIELD.SEQ, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonField.class);
        this.commonCodeRepository = commonCodeRepository;

        orderByFields.add(COMMON_FIELD.SEQ.asc());
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonField> findAllCodeField() {
        return super.findAll(COMMON_FIELD.FIELD_TYPE.eq("CODE").or(COMMON_FIELD.FIELD_TYPE.eq("MULTICODE")));
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonField> findAllConCode() {
        return super.findAll(COMMON_FIELD.FIELD_TYPE.eq("CONCODE"));
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonField> findAllCommonField(Integer type) {
        return super.findAll(COMMON_FIELD.FIELD_USE.eq("D").and(COMMON_FIELD.TYPE.eq(type)));
    }

    public void changeTheOrder(List<Integer> sequences, Integer seq) {
        final Map<Integer, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonField> fieldMap = findAll(COMMON_FIELD.TYPE.eq(seq)).stream().collect(Collectors.toMap(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonField::getSeq, e -> e));
        for (int i = 0, length = sequences.size(); i < length; i++) {
            final Integer sequence = sequences.get(i);
            if (Objects.nonNull(fieldMap.get(sequence))) {
                dsl.update(COMMON_FIELD)
                        .set(COMMON_FIELD.DISPLAY_SEQ, i)
                        .where(compareCompanyId())
                        .and(COMMON_FIELD.SEQ.eq(sequence))
                        .execute();
            }
        }
    }

    public kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonField findOneByTypeFieldId(Integer type, String fieldId) {
        return findOneIfNullThrow(COMMON_FIELD.TYPE.eq(type).and(COMMON_FIELD.FIELD_ID.eq(fieldId)));
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonField> findAllRelatedField(Integer type, String fieldId) {
        List<Condition> conditions = new ArrayList<>();

        conditions.add(COMMON_FIELD.TYPE.eq(type));
        conditions.add(COMMON_FIELD.FIELD_ID.notEqual(fieldId));
        conditions.add(COMMON_FIELD.FIELD_ID.notLike("%CODE_1"));
        conditions.add(COMMON_FIELD.FIELD_TYPE.eq("CODE"));

        return findAll(conditions);
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonField> findAllByFieldId() {
        final Map<String, List<CommonCode>> commonCodeMap = commonCodeRepository.individualCodeList(new StatQaResultSearchRequest())
                .stream().collect(Collectors.groupingBy(CommonCode::getFieldId));
        return findAll(COMMON_FIELD.FIELD_TYPE.eq("CODE"), COMMON_FIELD.DISPLAY_SEQ.asc()).stream()
                .filter(e -> Objects.isNull(commonCodeMap.get(e.getFieldId())))
                .collect(Collectors.toList());
    }

    public kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonField findOneFieldId(Integer type, String fieldId) {
        return findOne(COMMON_FIELD.TYPE.eq(type).and(COMMON_FIELD.FIELD_ID.eq(fieldId)));
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonField> individualFieldList() {
        final Map<Integer, Set<String>> commonCodeMap = commonCodeRepository.individualFieldList(new StatQaResultSearchRequest())
                .stream().collect(Collectors.groupingBy(CommonCode::getType, Collectors.mapping(CommonCode::getFieldId, Collectors.toSet())));
        return findAll(COMMON_FIELD.FIELD_TYPE.eq("CODE"), COMMON_FIELD.DISPLAY_SEQ.asc()).stream()
                .filter(e -> commonCodeMap.containsKey(e.getType()) && commonCodeMap.get(e.getType()).contains(e.getFieldId()))
                .collect(Collectors.toList());
    }
}
