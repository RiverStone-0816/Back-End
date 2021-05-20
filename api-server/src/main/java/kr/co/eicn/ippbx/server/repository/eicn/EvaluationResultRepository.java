package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonEicnCdr;
import kr.co.eicn.ippbx.meta.jooq.eicn.enums.EvaluationFormUseType;
import kr.co.eicn.ippbx.meta.jooq.eicn.enums.EvaluationResultProcessStatus;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.EvaluationResult;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EvaluationForm;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EvaluationItem;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.model.entity.customdb.EicnCdrEntity;
import kr.co.eicn.ippbx.model.entity.eicn.EvaluationFormEntity;
import kr.co.eicn.ippbx.model.entity.eicn.EvaluationResultEntity;
import kr.co.eicn.ippbx.model.entity.eicn.EvaluationResultStatResponse;
import kr.co.eicn.ippbx.model.form.DisputeEvaluationFormRequest;
import kr.co.eicn.ippbx.model.form.EvaluationItemScoreFormRequest;
import kr.co.eicn.ippbx.model.form.EvaluationResultFormRequest;
import kr.co.eicn.ippbx.model.search.EvaluationResultSearchRequest;
import kr.co.eicn.ippbx.server.service.EicnCdrService;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.EvaluationForm.EVALUATION_FORM;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.EvaluationItemScore.EVALUATION_ITEM_SCORE;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.EvaluationResult.EVALUATION_RESULT;

@Getter
@Setter
@Repository
public class EvaluationResultRepository extends EicnBaseRepository<EvaluationResult, EvaluationResultEntity, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(EvaluationResultRepository.class);
    private final EvaluationFormRepository evaluationFormRepository;
    private final EvaluationItemScoreRepository evaluationItemScoreRepository;
    private final PersonListRepository personListRepository;
    private final EicnCdrService eicnCdrService;
    private CommonEicnCdr cdrTable;

    public EvaluationResultRepository(EvaluationFormRepository evaluationFormRepository, EvaluationItemScoreRepository evaluationItemScoreRepository, PersonListRepository personListRepository, EicnCdrService eicnCdrService) {
        super(EVALUATION_RESULT, EVALUATION_RESULT.ID, EvaluationResultEntity.class);
        orderByFields.add(EVALUATION_RESULT.EVALUATION_DATE.desc());

        this.personListRepository = personListRepository;
        this.evaluationFormRepository = evaluationFormRepository;
        this.evaluationItemScoreRepository = evaluationItemScoreRepository;
        this.eicnCdrService = eicnCdrService;
    }

    @Override
    protected SelectConditionStep<Record> query(SelectJoinStep<Record> query) {
        cdrTable = eicnCdrService.getRepository().getTABLE();

        return query
                .join(EVALUATION_FORM)
                .on(EVALUATION_RESULT.EVALUATION_ID.eq(EVALUATION_FORM.ID))
                .join(cdrTable)
                .on(EVALUATION_RESULT.CDR_ID.eq(cdrTable.SEQ))
                .where(!g.isAdmin() ? Arrays.asList(EVALUATION_RESULT.TARGET_USERID.eq(g.getUser().getId()), EVALUATION_RESULT.RESULT_TRANSFER.eq(true))
                        : Collections.emptyList());
    }

    @Override
    protected RecordMapper<Record, EvaluationResultEntity> getMapper() {
        return record -> {
            final EvaluationResultEntity entity = record.into(EVALUATION_RESULT).into(EvaluationResultEntity.class);
            entity.setForm(record.into(EVALUATION_FORM).into(EvaluationFormEntity.class));
            entity.setCdr(record.into(EicnCdrEntity.class));

            return entity;
        };
    }

    public Pagination<EvaluationResultEntity> pagination(EvaluationResultSearchRequest search) {
        return super.pagination(search, conditions(search));
    }

    public List<EvaluationResultEntity> findAllByTargetUserIdAndEvaluationId(final String targetUserId, final Long evaluationId) {
        return super.findAll(EVALUATION_RESULT.TARGET_USERID.eq(targetUserId).and(EVALUATION_RESULT.EVALUATION_ID.eq(evaluationId)), Arrays.asList(EVALUATION_RESULT.EVALUATION_DATE.asc()));
    }

    protected void postProcedure(List<EvaluationResultEntity> evaluationResultEntities) {
        if (!evaluationResultEntities.isEmpty()) {
            final Map<String, String> personListMap = personListRepository.findAll().stream().collect(Collectors.toMap(PersonList::getId, PersonList::getIdName));
            evaluationResultEntities.forEach(e -> {
                final EvaluationFormEntity evaluationFormEntity = evaluationFormRepository.get(e.getEvaluationId());
                e.getForm().setCategories(evaluationFormEntity.getCategories());

                if (personListMap.get(e.getTargetUserid()) != null)
                    e.setTargetUserName(personListMap.get(e.getTargetUserid()));

                e.setScores(evaluationItemScoreRepository.findAllByTargetUserIdAndCdrIdAndEvaluationId(e.getTargetUserid(), e.getCdrId(), e.getEvaluationId()));
            });
        }
    }

    public List<Condition> conditions(EvaluationResultSearchRequest search) {
        final List<org.jooq.Condition> conditions = new ArrayList<>();

        if (search.getStartEvaluationDate() != null)
            conditions.add(DSL.date(EVALUATION_RESULT.EVALUATION_DATE).ge(search.getStartEvaluationDate()));
        if (search.getEndEvaluationDate() != null)
            conditions.add(DSL.date(EVALUATION_RESULT.EVALUATION_DATE).le(search.getEndEvaluationDate()));

        if (search.getStartRingDate() != null)
            conditions.add(DSL.date(cdrTable.RING_DATE).ge(search.getStartRingDate()));
        if (search.getEndRingDate() != null)
            conditions.add(DSL.date(cdrTable.RING_DATE).le(search.getEndRingDate()));

        if (search.getEvaluationId() != null)
            conditions.add(EVALUATION_RESULT.EVALUATION_ID.eq(search.getEvaluationId()));
        if (StringUtils.isNotEmpty(search.getUserId()))
            conditions.add(EVALUATION_RESULT.TARGET_USERID.eq(search.getUserId()));
        if (search.getStatus() != null)
            conditions.add(EVALUATION_RESULT.PROCESS_STATUS.eq(search.getStatus()));

        return conditions;
    }

    public List<EvaluationResultStatResponse> statistics(EvaluationResultSearchRequest search) {
        final AggregateFunction<Integer> count = DSL.countDistinct(EVALUATION_RESULT.TARGET_USERID, EVALUATION_RESULT.EVALUATION_ID, EVALUATION_RESULT.CDR_ID);
        final AggregateFunction<BigDecimal> sum = DSL.sum(EVALUATION_ITEM_SCORE.SCORE);
        final Map<String, String> personListMap = personListRepository.findAll().stream().collect(Collectors.toMap(PersonList::getId, PersonList::getIdName));
        final Map<Long, String> evaluationFormMap = evaluationFormRepository.findAll().stream().collect(Collectors.toMap(EvaluationForm::getId, EvaluationForm::getName));

        return dsl.select(EVALUATION_RESULT.TARGET_USERID
                , EVALUATION_RESULT.EVALUATION_ID
                , DSL.maxDistinct(EVALUATION_RESULT.EVALUATION_DATE).as("max_evaluation_date")
                , count.as("cnt")
                , sum.as("total")
                , sum.divide(count).as("avg")
        )
                .from(EVALUATION_RESULT)
                .leftJoin(EVALUATION_ITEM_SCORE)
                .on(EVALUATION_RESULT.EVALUATION_ID.eq(EVALUATION_ITEM_SCORE.EVALUATION_ID))
                .and(EVALUATION_RESULT.TARGET_USERID.eq(EVALUATION_ITEM_SCORE.TARGET_USERID)
                        .and(EVALUATION_RESULT.CDR_ID.eq(EVALUATION_ITEM_SCORE.CDR_ID)))
                .where(EVALUATION_RESULT.COMPANY_ID.eq(getCompanyId()))
                .and(conditions(search).stream().reduce((Condition::and)).orElse(DSL.noCondition()))
                .groupBy(EVALUATION_RESULT.TARGET_USERID, EVALUATION_RESULT.EVALUATION_ID)
                .fetch(r -> {
                    final EvaluationResultStatResponse response = r.into(EvaluationResultStatResponse.class);

                    if (personListMap.get(response.getTargetUserid()) != null)
                        response.setTargetUserName(personListMap.get(response.getTargetUserid()));
                    if (evaluationFormMap.get(response.getEvaluationId()) != null)
                        response.setEvaluationName(evaluationFormMap.get(response.getEvaluationId()));

                    return response;
                });
    }

    public int delete(Integer id) {
        dsl.deleteFrom(EVALUATION_ITEM_SCORE).where(EVALUATION_ITEM_SCORE.CDR_ID.in(
                DSL.select(EVALUATION_RESULT.CDR_ID).from(EVALUATION_RESULT).where(EVALUATION_RESULT.ID.eq(id))
        )).execute();

        return dsl.deleteFrom(EVALUATION_RESULT).where(EVALUATION_RESULT.ID.eq(id)).execute();
    }

    public void evaluationRegister(EvaluationResultFormRequest form) {
        final EvaluationForm evaluationForm = evaluationFormRepository.findOne(form.getEvaluationId());
        final EvaluationResultEntity result = dsl.select().from(EVALUATION_RESULT)
                .where(compareCompanyId())
                .and(EVALUATION_RESULT.CDR_ID.eq(form.getCdrId()))
                .fetchOneInto(EvaluationResultEntity.class);

        final LocalDate now = LocalDate.now();
        boolean isEvaluation = false;

        if (Objects.equals(EvaluationFormUseType.IN_PROGRESS, evaluationForm.getUseType())) {
            if (Objects.nonNull(evaluationForm.getStartDate()) && Objects.nonNull(evaluationForm.getEndDate())) {
                final LocalDate startDate = evaluationForm.getStartDate().toLocalDate();
                final LocalDate endDate = evaluationForm.getEndDate().toLocalDate();

                if ((now.isAfter(startDate) || now.isEqual(startDate)) && (now.isBefore(endDate) || now.isEqual(endDate))) {
                    isEvaluation = true;
                }
            } else {
                isEvaluation = true;
            }
        } else if (Objects.equals(EvaluationFormUseType.PERIOD, evaluationForm.getUseType())) {
            final LocalDate startDate = evaluationForm.getStartDate().toLocalDate();
            final LocalDate endDate = evaluationForm.getEndDate().toLocalDate();

            if ((now.isAfter(startDate) || now.isEqual(startDate)) && (now.isBefore(endDate) || now.isEqual(endDate))) {
                isEvaluation = true;
            }
        }

        if (!isEvaluation)
            throw new IllegalStateException("평가를 진행할수 없는 상태이거나 진행기간,이의제기기간이 아닙니다.");

        final boolean isQa = g.isAdmin();

        if (!isQa && (result == null || !Objects.equals(EvaluationResultProcessStatus.EVALUATION_ING, result.getProcessStatus())))
            throw new IllegalStateException("이의제기를 할 수 있는 상태가 아닙니다.");

        final EvaluationFormEntity evaluationFormEntity = evaluationFormRepository.get(form.getEvaluationId());

        if (form.getScores() != null) {
            final int totalScore = form.getScores().stream().mapToInt(EvaluationItemScoreFormRequest::getScore).sum();
            final int maxScore = evaluationFormEntity.getCategories().stream().flatMap(category -> category.getItems().stream()).mapToInt(EvaluationItem::getMaxScore).sum();

            if (totalScore > maxScore)
                throw new IllegalStateException("배점의 합이 " + maxScore + "보다 높을 수 없습니다.");
        }

        if (result != null) {
            if (result.getProcessStatus().equals(EvaluationResultProcessStatus.COMPLETE))
                throw new IllegalArgumentException("이미 평가한 상담원입니다.");

            if (isQa) {
                dsl.update(EVALUATION_RESULT)
                        .set(EVALUATION_RESULT.TARGET_USERID, form.getTargetUserid())
                        .set(EVALUATION_RESULT.CDR_ID, form.getCdrId())
                        .set(EVALUATION_RESULT.RESULT_TRANSFER, form.isResultTransfer())
                        .set(EVALUATION_RESULT.EVALUATION_USERID, g.getUser().getId())
                        .set(EVALUATION_RESULT.EVALUATION_DATE, DSL.now())
                        .set(EVALUATION_RESULT.MEMO, form.getMemo())
                        .set(EVALUATION_RESULT.UPDATE_USERID, g.getUser().getId())
                        .set(EVALUATION_RESULT.UPDATE_DATE, DSL.now())
                        .where(compareCompanyId())
                        .and(EVALUATION_RESULT.EVALUATION_ID.eq(form.getEvaluationId()))
                        .and(EVALUATION_RESULT.ID.eq(result.getId()))
                        .execute();

                final List<EvaluationItemScoreFormRequest> scores = form.getScores();

                for (EvaluationItemScoreFormRequest score : scores) {
                    dsl.update(EVALUATION_ITEM_SCORE)
                            .set(EVALUATION_ITEM_SCORE.SCORE, score.getScore())
                            .where(EVALUATION_ITEM_SCORE.TARGET_USERID.eq(form.getTargetUserid()))
                            .and(EVALUATION_ITEM_SCORE.CDR_ID.eq(form.getCdrId()))
                            .and(EVALUATION_ITEM_SCORE.EVALUATION_ID.eq(form.getEvaluationId()))
                            .and(EVALUATION_ITEM_SCORE.ITEM_ID.eq(score.getItemId()))
                            .execute();
                }
            }
        } else {
            dsl.insertInto(EVALUATION_RESULT)
                    .set(EVALUATION_RESULT.EVALUATION_ID, form.getEvaluationId())
                    .set(EVALUATION_RESULT.TARGET_USERID, form.getTargetUserid())
                    .set(EVALUATION_RESULT.CDR_ID, form.getCdrId())
                    .set(EVALUATION_RESULT.RESULT_TRANSFER, form.isResultTransfer())
                    .set(EVALUATION_RESULT.EVALUATION_USERID, g.getUser().getId())
                    .set(EVALUATION_RESULT.EVALUATION_DATE, DSL.now())
                    .set(EVALUATION_RESULT.PROCESS_STATUS, EvaluationResultProcessStatus.EVALUATION_ING)
                    .set(EVALUATION_RESULT.MEMO, form.getMemo())
                    .set(EVALUATION_RESULT.UPDATE_USERID, g.getUser().getId())
                    .set(EVALUATION_RESULT.UPDATE_DATE, DSL.now())
                    .set(EVALUATION_RESULT.COMPANY_ID, getCompanyId())
                    .execute();

            final List<EvaluationItemScoreFormRequest> scores = form.getScores();

            for (EvaluationItemScoreFormRequest score : scores) {
                dsl.insertInto(EVALUATION_ITEM_SCORE)
                        .set(EVALUATION_ITEM_SCORE.EVALUATION_ID, form.getEvaluationId())
                        .set(EVALUATION_RESULT.CDR_ID, form.getCdrId())
                        .set(EVALUATION_ITEM_SCORE.TARGET_USERID, form.getTargetUserid())
                        .set(EVALUATION_ITEM_SCORE.ITEM_ID, score.getItemId())
                        .set(EVALUATION_ITEM_SCORE.SCORE, score.getScore())
                        .execute();
            }
        }
    }

    public void complete(Integer id, EvaluationResultFormRequest form) {
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EvaluationResult result = findOneIfNullThrow(id);
        if (EvaluationResultProcessStatus.COMPLETE.equals(result.getProcessStatus()))
            throw new IllegalArgumentException("이미 평가한 상담원입니다.");
        if (!Arrays.asList("J", "A", "B").contains(g.getUser().getIdType()))
            throw new IllegalStateException("관리자만 가능합니다.");

        final int affectedRows = dsl.update(EVALUATION_RESULT)
                .set(EVALUATION_RESULT.EVALUATION_ID, form.getEvaluationId())
                .set(EVALUATION_RESULT.TARGET_USERID, form.getTargetUserid())
                .set(EVALUATION_RESULT.CDR_ID, form.getCdrId())
                .set(EVALUATION_RESULT.RESULT_TRANSFER, form.isResultTransfer())
                .set(EVALUATION_RESULT.EVALUATION_USERID, g.getUser().getId())
                .set(EVALUATION_RESULT.EVALUATION_DATE, DSL.now())
                .set(EVALUATION_RESULT.PROCESS_STATUS, EvaluationResultProcessStatus.COMPLETE)
                .set(EVALUATION_RESULT.MEMO, form.getMemo())
                .set(EVALUATION_RESULT.UPDATE_USERID, g.getUser().getId())
                .set(EVALUATION_RESULT.UPDATE_DATE, DSL.now())
                .set(EVALUATION_RESULT.COMPANY_ID, getCompanyId())
                .where(compareCompanyId())
                .and(EVALUATION_RESULT.ID.eq(id))
                .and(EVALUATION_RESULT.PROCESS_STATUS.eq(EvaluationResultProcessStatus.OBJECTION))
                .execute();

        if (affectedRows > 0)
            for (EvaluationItemScoreFormRequest score : form.getScores()) {
                dsl.update(EVALUATION_ITEM_SCORE)
                        .set(EVALUATION_ITEM_SCORE.SCORE, score.getScore())
                        .where(EVALUATION_ITEM_SCORE.TARGET_USERID.eq(form.getTargetUserid()))
                        .and(EVALUATION_ITEM_SCORE.CDR_ID.eq(form.getCdrId()))
                        .and(EVALUATION_ITEM_SCORE.EVALUATION_ID.eq(form.getEvaluationId()))
                        .and(EVALUATION_ITEM_SCORE.ITEM_ID.eq(score.getItemId()))
                        .execute();
            }
    }

    public void dispute(Integer id, DisputeEvaluationFormRequest form) {
        dsl.update(EVALUATION_RESULT)
                .set(EVALUATION_RESULT.PROCESS_STATUS, EvaluationResultProcessStatus.OBJECTION)
                .set(EVALUATION_RESULT.CHALLENGE_MEMO, form.getChallengeMemo())
                .set(EVALUATION_RESULT.UPDATE_USERID, g.getUser().getId())
                .set(EVALUATION_RESULT.UPDATE_DATE, DSL.now())
                .where(EVALUATION_RESULT.ID.eq(id))
                .and(EVALUATION_RESULT.PROCESS_STATUS.eq(EvaluationResultProcessStatus.EVALUATION_ING))
                .and(EVALUATION_RESULT.TARGET_USERID.eq(g.getUser().getId()))
                .execute();
    }

    public void confirm(Integer id) {
        dsl.update(EVALUATION_RESULT)
                .set(EVALUATION_RESULT.PROCESS_STATUS, EvaluationResultProcessStatus.COMPLETE)
                .set(EVALUATION_RESULT.UPDATE_USERID, g.getUser().getId())
                .set(EVALUATION_RESULT.UPDATE_DATE, DSL.now())
                .where(EVALUATION_RESULT.ID.eq(id))
                .and(EVALUATION_RESULT.PROCESS_STATUS.eq(EvaluationResultProcessStatus.OBJECTION))
                .and(EVALUATION_RESULT.TARGET_USERID.eq(g.getUser().getId()))
                .execute();
    }
}
