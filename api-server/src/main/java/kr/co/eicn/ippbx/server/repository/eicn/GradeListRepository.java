package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.enums.RouteApplicationType;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.GradeList;
import kr.co.eicn.ippbx.model.entity.eicn.GradeListEntity;
import kr.co.eicn.ippbx.model.form.GradeListFormRequest;
import kr.co.eicn.ippbx.model.search.GradeListSearchRequest;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.GRADE_LIST;
import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.QUEUE_NAME;
import static org.jooq.impl.DSL.noCondition;

@Getter
@Repository
public class GradeListRepository extends EicnBaseRepository<GradeList, GradeListEntity, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(GradeListRepository.class);

    private final CacheService cacheService;
    private final PBXServerInterface pbxServerInterface;
    private final QueueNameRepository queueNameRepository;

    public GradeListRepository(CacheService cacheService, PBXServerInterface pbxServerInterface, QueueNameRepository queueNameRepository) {
        super(GRADE_LIST, GRADE_LIST.SEQ, GradeListEntity.class);
        this.cacheService = cacheService;
        this.pbxServerInterface = pbxServerInterface;
        this.queueNameRepository = queueNameRepository;
    }

    @Override
    protected SelectConditionStep<Record> query(SelectJoinStep<Record> query) {
        return query
                .leftJoin(QUEUE_NAME).on(QUEUE_NAME.NUMBER.eq(GRADE_LIST.HUNT_NUMBER))
                .where();
    }

    @Override
    protected RecordMapper<Record, GradeListEntity> getMapper() {
        return record -> {
            final GradeListEntity entity = record.into(GRADE_LIST).into(GradeListEntity.class);
            entity.setHuntName(record.into(QUEUE_NAME.HAN_NAME).value1());
            return entity;
        };
    }

    public Pagination<GradeListEntity> pagination(GradeListSearchRequest search) {
        return super.pagination(search, conditions(search));
    }

    public Record insertOnGeneratedKey(GradeListFormRequest form) {
        final GradeListEntity record = new GradeListEntity();
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.GradeList duplicateGrade = findOne(GRADE_LIST.GRADE_NUMBER.eq(form.getGradeNumber()));
        if (Objects.nonNull(duplicateGrade))
            throw new IllegalArgumentException("해당 전화번호가 " + (duplicateGrade.getGrade().equals(RouteApplicationType.VIP.getLiteral()) ? "VIP" : "블랙리스트") + "에 등록되어있습니다.");

        record.setGrade(form.getGrade());
        record.setGradeNumber(form.getGradeNumber());
        record.setType(form.getType());
        record.setHuntNumber(form.getQueueNumber());
        record.setCompanyId(getCompanyId());

        cacheService.pbxServerList(getCompanyId()).forEach(e -> {
            DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
            super.insert(pbxDsl, record);
        });

        return super.insertOnGeneratedKey(record);
    }

    public void updateByKey(GradeListFormRequest form, Integer seq) {
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.GradeList record = findOneIfNullThrow(seq);
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.GradeList duplicateGrade = findOne(GRADE_LIST.GRADE_NUMBER.eq(form.getGradeNumber()));
        if (Objects.nonNull(duplicateGrade))
            throw new IllegalArgumentException("해당 전화번호가 " + (duplicateGrade.getGrade().equals(RouteApplicationType.VIP.getLiteral()) ? "VIP" : "블랙리스트") + "에 등록되어있습니다.");

        record.setGradeNumber(form.getGradeNumber());
        record.setType(form.getType());
        record.setHuntNumber(form.getQueueNumber());

        super.updateByKey(record, seq);

        cacheService.pbxServerList(getCompanyId()).forEach(e -> {
            DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
            super.updateByKey(pbxDsl, record, seq);
        });
    }

    public int deleteOnIfNullThrow(Integer seq) {
        int delete = super.deleteOnIfNullThrow(seq);

        cacheService.pbxServerList(getCompanyId()).forEach(e -> {
            DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
            super.delete(pbxDsl, seq);
        });

        return delete;
    }

    private List<Condition> conditions(GradeListSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();
        Condition numberCondition = noCondition();

        if (StringUtils.isNotEmpty(search.getGradeNumber()))
            numberCondition = numberCondition.or(GRADE_LIST.GRADE_NUMBER.eq(search.getGradeNumber()));
        for (String gradeNumber : search.getGradeNumbers()) {
            numberCondition = numberCondition.or(GRADE_LIST.GRADE_NUMBER.eq(gradeNumber));
        }

        conditions.add(numberCondition);

        if (StringUtils.isNotEmpty(search.getGrade()))
            conditions.add(GRADE_LIST.GRADE.eq(search.getGrade()));


        return conditions;
    }
}
