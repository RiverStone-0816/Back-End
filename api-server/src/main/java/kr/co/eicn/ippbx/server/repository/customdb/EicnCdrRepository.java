package kr.co.eicn.ippbx.server.repository.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonEicnCdr;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.model.dto.customdb.MainInOutInfoResponse;
import kr.co.eicn.ippbx.model.entity.customdb.EicnCdrEntity;
import kr.co.eicn.ippbx.model.entity.customdb.EicnCdrEvaluationEntity;
import kr.co.eicn.ippbx.model.entity.eicn.EicnCdrEvaluationResultEntity;
import kr.co.eicn.ippbx.model.enums.AdditionalState;
import kr.co.eicn.ippbx.model.enums.CallStatus;
import kr.co.eicn.ippbx.model.enums.CallType;
import kr.co.eicn.ippbx.model.search.RecordCallSearch;
import kr.co.eicn.ippbx.server.repository.eicn.EvaluationFormRepository;
import kr.co.eicn.ippbx.server.repository.eicn.PersonListRepository;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.apache.commons.lang3.EnumUtils;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
import org.jooq.SelectJoinStep;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.EvaluationResult.EVALUATION_RESULT;
import static kr.co.eicn.ippbx.model.enums.AdditionalState.PICKUPER;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.jooq.impl.DSL.timestamp;

@Getter
public class EicnCdrRepository extends CustomDBBaseRepository<CommonEicnCdr, EicnCdrEntity, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(EicnCdrRepository.class);

    private final CommonEicnCdr TABLE;
    @Autowired
    private EvaluationFormRepository evaluationFormRepository;
    @Autowired
    private PersonListRepository personListRepository;

    public EicnCdrRepository(String companyId) {
        super(new CommonEicnCdr(companyId), new CommonEicnCdr(companyId).SEQ, EicnCdrEntity.class);
        TABLE = new CommonEicnCdr(companyId);

        addField(TABLE.SEQ);
        addField(TABLE.RING_DATE);
        addField(TABLE.DURATION);
        addField(TABLE.BILLSEC);
        addField(TABLE.IN_OUT);
        addField(TABLE.DCONTEXT);
        addField(TABLE.SRC);
        addField(TABLE.DST);
        addField(TABLE.DETAIL_CALLSTATUS);
        addField(TABLE.RECORD_INFO);
        addField(TABLE.RECORD_FILE);
        addField(TABLE.UNIQUEID);
        addField(TABLE.DST_UNIQUEID);
        addField(TABLE.INI_NUM);
        addField(TABLE.SECOND_NUM);
        addField(TABLE.IVR_KEY);
        addField(TABLE.CALLEE_HANGUP);
        addField(TABLE.USERID);
        addField(TABLE.HOST);
        addField(TABLE.HANGUP_CAUSE);
        addField(TABLE.TURN_OVER_KIND);
        addField(TABLE.TURN_OVER_NUMBER);

        orderByFields.add(TABLE.RING_DATE.desc());
    }

    @Override
    protected SelectConditionStep<Record> query(SelectJoinStep<Record> query) {
        return query.where(TABLE.CALLSTATUS.eq("H"));
    }

    public List<EicnCdrEntity> findAll(RecordCallSearch search) {
        return super.findAll(conditions(search));
    }

    public Pagination<EicnCdrEntity> pagination(RecordCallSearch search) {
        return super.pagination(search, conditions(search));
    }

    private List<Condition> conditions(RecordCallSearch search) {
        final List<Condition> conditions = new ArrayList<>();

        if (g.getUser().getDataSearchAuthorityType() != null) {
            switch (g.getUser().getDataSearchAuthorityType()) {
                case NONE:
                    conditions.add(DSL.falseCondition());
                    return conditions;
                case MINE:
                    conditions.add(TABLE.USERID.eq(g.getUser().getId()));
                    break;
                case GROUP:
                    conditions.add(TABLE.GROUP_TREE_NAME.like(g.getUser().getGroupTreeName() + "%"));
                    break;
            }
        }

        if (search.getSeqList() != null && !search.getSeqList().isEmpty())
            conditions.add(TABLE.SEQ.in(search.getSeqList()));
        if (search.getStartTimestamp() != null)
            conditions.add(TABLE.RING_DATE.ge(search.getStartTimestamp()));
        if (search.getEndTimestamp() != null)
            conditions.add(TABLE.RING_DATE.le(search.getEndTimestamp()));
        if (isNotEmpty(search.getGroupCode()))
            conditions.add(TABLE.GROUP_TREE_NAME.like("%" + search.getGroupCode() + "%"));
        if (isNotEmpty(search.getPhone()))
            conditions.add(DSL.and(TABLE.DST.like("%" + search.getPhone() + "%")).or(TABLE.SRC.like("%" + search.getPhone() + "%"))
                    .or(TABLE.TURN_OVER_NUMBER.like("%" + search.getPhone() + "%"))
            );
        if (isNotEmpty(search.getUserId()))
            conditions.add(TABLE.USERID.eq(search.getUserId()));

        final RecordCallSearch.SearchCallType callType = RecordCallSearch.SearchCallType.of(search.getCallType());
        if (callType != null) {
            switch (callType) {
                case INBOUND:
                    conditions.add(TABLE.IN_OUT.eq(CallType.INBOUND.getCode()).and(TABLE.DCONTEXT.ne("inbound_inner")));
                    break;
                case OUTBOUND:
                    conditions.add(TABLE.IN_OUT.eq(CallType.OUTBOUND.getCode()).and(TABLE.DCONTEXT.ne("outbound_inner")));
                    break;
                case INBOUND_INNER: conditions.add(TABLE.DCONTEXT.eq("inbound_inner")); break;
                case OUTBOUND_INNER: conditions.add(TABLE.DCONTEXT.eq("outbound_inner")); break;
                case PDS:
                    conditions.add(TABLE.DCONTEXT.eq("outbound").and(TABLE.CMP_CLICK_FROM.eq("PDS")));
                    break;
            }
        }

        final CallStatus byCallStatus = RecordCallSearch.SearchCallStatus.findByCallStatus(search.getCallStatus());
        if (byCallStatus != null) { // 호상태
            switch (byCallStatus) {
                case fail:
                    conditions.add(
                            TABLE.HANGUP_CAUSE.notLike("0(%")
                                    .and(TABLE.HANGUP_CAUSE.notLike("16%"))
                                    .and(TABLE.HANGUP_CAUSE.notLike("19(%"))
                                    .and(TABLE.HANGUP_CAUSE.notLike("17(%"))
                    );
                    break;
                case normal_clear:
                    conditions.add(TABLE.BILLSEC.gt(0));
                    break;
                case user_busy:
                    conditions.add(TABLE.HANGUP_CAUSE.like("17(%").and(TABLE.IN_OUT.eq("O")));
                    break;
                case no_answer:
                    conditions.add(
                            DSL.and(DSL.and(TABLE.HANGUP_CAUSE.like("19(%").and(TABLE.IN_OUT.eq("O")))
                                    .or(DSL.and(TABLE.HANGUP_CAUSE.like("19%").or(TABLE.HANGUP_CAUSE.like("17%"))
                                            .and(TABLE.IN_OUT.eq(CallType.INBOUND.getCode())).and(TABLE.BILLSEC.eq(0)))
                                    )
                            )
                    );
                    break;
                default:
                    conditions.add(TABLE.HANGUP_CAUSE.like(search.getCallStatus().concat("(%")));
                    break;
            }
        }

        final AdditionalState byAdditionalState = RecordCallSearch.SearchAdditionalState.findByAdditionalState(search.getEtcStatus());
        if (byAdditionalState != null) { // 부가상태
            switch (byAdditionalState) {
                case HANGUP_BEFORE_CONNECT: // 연결전끊음
                    conditions.add(
                            TABLE.ETC3.eq(EMPTY).or(TABLE.ETC3.isNull()).and(TABLE.IN_OUT.eq(CallType.INBOUND.getCode()))
                                    .and(TABLE.BILLSEC.eq(0))
//                                    .and(DSL.and(TABLE.HANGUP_CAUSE.like("19(%")).or(TABLE.HANGUP_CAUSE.like("17(%")))
                                    .and(TABLE.DETAIL_CALLSTATUS.like("%event_server_inbound%"))
                    );
                    break;
                case CANCEL_CONNECT: // 포기호(헌트대기중끊음)
                    conditions.add(TABLE.IN_OUT.eq(CallType.INBOUND.getCode()).and(TABLE.BILLSEC.equal(0))
                            .and(TABLE.SECOND_NUM.ne(EMPTY))
                            .and(DSL.or(TABLE.TURN_OVER_KIND.eq(EMPTY).or(TABLE.TURN_OVER_KIND.isNull()).or(TABLE.TURN_OVER_KIND.eq(PICKUPER.getCode())))));
                    break;

                case TRANSFEREE: //돌려받음
                    conditions.add(TABLE.TURN_OVER_KIND.eq("TRANSFEREE").and(TABLE.TURN_OVER_NUMBER.isNotNull().or(TABLE.TURN_OVER_NUMBER.notEqual(""))));

                    break;
                default:
                    conditions.add(TABLE.TURN_OVER_KIND.eq(search.getEtcStatus()));
                    break;
            }
        }

        if (isNotEmpty(search.getExtension()))
            conditions.add(DSL.and(TABLE.DST.eq(search.getExtension()).or(TABLE.SRC.eq(search.getExtension())).or(TABLE.TURN_OVER_NUMBER.eq(search.getExtension()))));
        if (isNotEmpty(search.getIvrCode()))
            conditions.add(TABLE.IVR_KEY.like("%|" + search.getIvrCode() + "_" + search.getIvrKey() + "%"));
        if (isNotEmpty(search.getIniNum()))
            conditions.add(TABLE.INI_NUM.eq(search.getIniNum()));
        if (isNotEmpty(search.getSecondNum()))
            conditions.add(TABLE.SECOND_NUM.eq(search.getSecondNum()));

        final RecordCallSearch.CallTime byCallTime = EnumUtils.getEnum(RecordCallSearch.CallTime.class, search.getByCallTime());
        if (byCallTime != null) {
            switch (byCallTime) {
                case ONE_LE:
                    conditions.add(TABLE.BILLSEC.le(60));
                    break;
                case ONE_TWO:
                    conditions.add(TABLE.BILLSEC.ge(60).and(TABLE.BILLSEC.le(2 * 60)));
                    break;
                case TWO_THREE:
                    conditions.add(TABLE.BILLSEC.ge(2 * 60).and(TABLE.BILLSEC.le(3 * 60)));
                    break;
                case THREE_FIVE:
                    conditions.add(TABLE.BILLSEC.ge(3 * 60).and(TABLE.BILLSEC.le(5 * 60)));
                    break;
                case FIVE_TEN:
                    conditions.add(TABLE.BILLSEC.ge(5 * 60).and(TABLE.BILLSEC.le(10 * 60)));
                    break;
                case TEN_THIRTY:
                    conditions.add(TABLE.BILLSEC.ge(10 * 60).and(TABLE.BILLSEC.le(30 * 60)));
                    break;
                case THIRTY_GE:
                    conditions.add(TABLE.BILLSEC.ge(30 * 60));
                    break;
            }
        }
        if (byCallTime == null) {
            if (search.getStartTime() != null) {
                final LocalTime startTime = search.getStartTime().toLocalTime();
                conditions.add(TABLE.BILLSEC.ge((startTime.getMinute() * 60) + (startTime.getSecond())));
            }
            if (search.getEndTime() != null) {
                final LocalTime endTime = search.getEndTime().toLocalTime();
                conditions.add(TABLE.BILLSEC.le((endTime.getMinute() * 60) + (endTime.getSecond())));
            }
        }

        final RecordCallSearch.SearchCustomRating byCustomRating = EnumUtils.getEnum(RecordCallSearch.SearchCustomRating.class, search.getCustomerRating());
        if (byCustomRating != null) {
            switch (byCustomRating) {
                case NORMAL:
                    conditions.add(DSL.and(TABLE.VIP_BLACK.ne(RecordCallSearch.SearchCustomRating.VIP.getCode())
                            .or(TABLE.VIP_BLACK.ne(RecordCallSearch.SearchCustomRating.BLACK_LIST.getCode()))));
                    break;
                case VIP:
                    conditions.add(TABLE.VIP_BLACK.eq(RecordCallSearch.SearchCustomRating.VIP.getCode()));
                    break;
                case BLACK_LIST:
                    conditions.add(TABLE.VIP_BLACK.eq(RecordCallSearch.SearchCustomRating.BLACK_LIST.getCode()));
                    break;
            }
        }

        if (search.getBatchDownloadMode() || search.getBatchEvaluationMode())
            conditions.add(TABLE.HANGUP_CAUSE.startsWith(CallStatus.normal_clear.getCode()).and(TABLE.BILLSEC.gt(0)).and(TABLE.RECORD_FILE.ne("")));


        return conditions;
    }

    public List<MainInOutInfoResponse> getCallHistory() {
        LocalDate currentDate = LocalDate.now();
        return dsl.select(TABLE.SEQ.as("seq"))
                .select(TABLE.IN_OUT.as("inOut"))
                .select(TABLE.SRC.as("customNumber"))
                .select(TABLE.RING_DATE.as("ringDate"))
                .select(TABLE.BILLSEC.as("billsec"))
                .select(TABLE.DST.as("receivePath"))
                .select(TABLE.UNIQUEID.as("uniqueId"))
                .from(TABLE)
                .where(compareCompanyId())
                .and(TABLE.RING_DATE.ge(timestamp(currentDate + " 00:00:00")))
                .and(TABLE.RING_DATE.le(timestamp(currentDate + " 23:59:59")))
                .fetchInto(MainInOutInfoResponse.class);
    }

    public MainInOutInfoResponse getCallHistoryByUniqueId(String uniqueId) {
        return dsl.select(TABLE.SEQ.as("seq"))
                .select(TABLE.IN_OUT.as("inOut"))
                .select(TABLE.SRC.as("customNumber"))
                .select(TABLE.RING_DATE.as("ringDate"))
                .select(TABLE.BILLSEC.as("billsec"))
                .select(TABLE.DST.as("receivePath"))
                .select(TABLE.UNIQUEID.as("uniqueId"))
                .from(TABLE)
                .where(compareCompanyId())
                .and(TABLE.UNIQUEID.eq(uniqueId))
                .fetchOneInto(MainInOutInfoResponse.class);
    }

    //uniqueId unique 하지 않음
    public List<EicnCdrEntity> findAllByUniqueId(final String uniqueId) {
        return findAll(TABLE.UNIQUEID.eq(uniqueId));
    }

    public List<EicnCdrEvaluationEntity> getEvaluationLists(final List<Integer> sequences) {
        final Map<String, String> personListMap = personListRepository.getIdAndNameMap();

        return dsl.select()
                .from(TABLE)
                .leftJoin(EVALUATION_RESULT)
                .on(TABLE.SEQ.eq(EVALUATION_RESULT.CDR_ID))
                .where(compareCompanyId())
                .and(TABLE.SEQ.in(sequences))
                .fetch(r -> {
                    final EicnCdrEvaluationEntity entity = r.into(EicnCdrEvaluationEntity.class);
                    final EicnCdrEvaluationResultEntity result = r.into(EVALUATION_RESULT).into(EicnCdrEvaluationResultEntity.class);

                    if (result != null && result.getId() != null) {
                        entity.setEvaluationResult(result);
                        entity.getEvaluationResult().setForm(evaluationFormRepository.get(result.getEvaluationId()));

                        if (personListMap.get(result.getTargetUserid()) != null)
                            entity.getEvaluationResult().setTargetUserName(personListMap.get(result.getTargetUserid()));
                    }

                    return entity;
                });
    }

    public EicnCdrEvaluationEntity getEvaluation(final Integer seq) {
        return dsl.select()
                .from(TABLE)
                .leftJoin(EVALUATION_RESULT)
                .on(TABLE.SEQ.eq(EVALUATION_RESULT.CDR_ID))
                .where(compareCompanyId())
                .and(TABLE.SEQ.eq(seq))
                .fetchOne(r -> {
                    final EicnCdrEvaluationEntity entity = r.into(EicnCdrEvaluationEntity.class);
                    final EicnCdrEvaluationResultEntity result = r.into(EVALUATION_RESULT).into(EicnCdrEvaluationResultEntity.class);

                    if (result != null && result.getId() != null) {
                        entity.setEvaluationResult(result);
                        entity.getEvaluationResult().setForm(evaluationFormRepository.get(result.getEvaluationId()));

                        final PersonList targetUser = personListRepository.findOne(result.getTargetUserid());

                        if (targetUser != null)
                            entity.getEvaluationResult().setTargetUserName(targetUser.getIdName());
                    }

                    return entity;
                });
    }

    public void removeRecordFile(Integer seq) {
        dsl.update(TABLE)
                .set(TABLE.RECORD_INFO, "")
                .set(TABLE.RECORD_FILE, "")
                .where(TABLE.SEQ.eq(seq))
                .execute();
    }
}
