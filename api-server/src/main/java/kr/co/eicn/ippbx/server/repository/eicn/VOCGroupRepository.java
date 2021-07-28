package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.VocGroup;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.VocMemberList;
import kr.co.eicn.ippbx.model.enums.InOutTarget;
import kr.co.eicn.ippbx.model.enums.IsArsSms;
import kr.co.eicn.ippbx.model.enums.ProcessKind;
import kr.co.eicn.ippbx.model.enums.VocGroupSender;
import kr.co.eicn.ippbx.model.form.VOCGroupFormRequest;
import kr.co.eicn.ippbx.model.search.VOCGroupSearchRequest;
import kr.co.eicn.ippbx.server.service.*;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.ResearchList.RESEARCH_LIST;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.VocGroup.VOC_GROUP;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.VocMemberList.VOC_MEMBER_LIST;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
@Repository
public class VOCGroupRepository extends EicnBaseRepository<VocGroup, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.VocGroup, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(VOCGroupRepository.class);

    private final VocResearchResultService resultService;
    private final VocCustomListService customListService;
    private final StatDBStatVOCService statVOCService;
    private final VocMemberListRepository memberListRepository;
    private final ResearchListRepository researchListRepository;
    private final PBXServerInterface pbxServerInterface;
    private final CacheService cacheService;
    List<String> memberList = new ArrayList<>();

    public VOCGroupRepository(VocResearchResultService resultService, VocCustomListService customListService, StatDBStatVOCService statVOCService, VocMemberListRepository memberListRepository, ResearchListRepository researchListRepository, PBXServerInterface pbxServerInterface, CacheService cacheService) {
        super(VOC_GROUP, VOC_GROUP.SEQ, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.VocGroup.class);
        this.resultService = resultService;
        this.customListService = customListService;
        this.statVOCService = statVOCService;
        this.memberListRepository = memberListRepository;
        this.researchListRepository = researchListRepository;
        this.pbxServerInterface = pbxServerInterface;
        this.cacheService = cacheService;
    }

    @Override
    protected SelectConditionStep<Record> query(SelectJoinStep<Record> query) {
        return query
                .leftJoin(VOC_MEMBER_LIST)
                .on(VOC_MEMBER_LIST.VOC_GROUP_ID.eq(VOC_GROUP.SEQ))
                .where(DSL.noCondition());
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.VocGroup> findAll(VOCGroupSearchRequest search) {
        return super.findAll(conditions(search));
    }


    public Pagination<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.VocGroup> pagination(VOCGroupSearchRequest search) {
        return super.pagination(search, conditions(search), Collections.singletonList(VOC_GROUP.INSERT_DATE.desc()));
    }

    public void insertAllPbxServers(VOCGroupFormRequest form) {
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.VocGroup groupRecord = new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.VocGroup();
        groupRecord.setVocGroupName(form.getVocGroupName());
        groupRecord.setSender(form.getSender());
        groupRecord.setProcessKind(form.getProcessKind());
        groupRecord.setIsArsSms(form.getIsArsSms());
        groupRecord.setProcessKind(form.getProcessKind());
        groupRecord.setInformation(form.getInformation());
        groupRecord.setInsertDate(new Timestamp(System.currentTimeMillis()));
        groupRecord.setCompanyId(getCompanyId());
        if (form.getProcessKind().equals(ProcessKind.TERM.getCode())) {
            groupRecord.setStartTerm(form.getStartTerm());
            groupRecord.setEndTerm(form.getEndTerm());
        }

        if (form.getIsArsSms().equals(IsArsSms.ARS.getCode())) {
            if (form.getSender().equals("AUTO")) {
                if (Objects.isNull(researchListRepository.findOne(RESEARCH_LIST.RESEARCH_ID.eq(form.getArsResearchId())))) {
                    throw new IllegalArgumentException("해당 설문이 존재하지 않습니다.");
                }
                groupRecord.setOutboundTarget(form.getOutboundTarget());
                if (!form.getOutboundTarget().equals(InOutTarget.NO.getCode()))
                    groupRecord.setOutboundCallKind(form.getOutboundCallKind());
                if (form.getOutboundTarget().equals(InOutTarget.CIDNUM.getCode()))
                    groupRecord.setOutboundTargetCidnum(form.getOutboundTargetCidnum());

                groupRecord.setInboundTarget(form.getInboundTarget());
                if (!form.getInboundTarget().equals(InOutTarget.NO.getCode()))
                    groupRecord.setInboundCallKind(form.getInboundCallKind());
                if (form.getInboundTarget().equals(InOutTarget.SVCNUM.getCode())) {
                    groupRecord.setInboundTargetSvcnum(form.getInboundTargetSvcnum());
                }
            }
            groupRecord.setArsResearchId(form.getArsResearchId());
        }

        final Record record = super.insertOnGeneratedKey(groupRecord);

        final VocMemberList memberRecord = new VocMemberList();
        memberRecord.setSeq(memberListRepository.nextSequence());
        memberRecord.setVocGroupId(record.getValue(VOC_GROUP.SEQ));
        memberRecord.setCompanyId(getCompanyId());
        if (form.getIsArsSms().equals("ARS") && form.getSender().equals("AUTO")) {
            if (form.getOutboundTarget().contains(InOutTarget.MEMBER.getCode())) {
                memberRecord.setOutboundCallKind(form.getOutboundCallKind());
                memberList.addAll(form.getOutboundMemberList());
            }
            if (form.getInboundTarget().contains(InOutTarget.MEMBER.getCode())) {
                memberRecord.setInboundCallKind(form.getInboundCallKind());

                for (String member : form.getInboundMemberList()) {
                    if (!memberList.contains(member))
                        memberList.add(member);
                }
            }
            if (memberList.size() > 0) {
                for (String member : memberList) {
                    memberRecord.setUserid(member);
                    memberListRepository.insert(memberRecord);
                }
            }
        }

        if (form.getIsArsSms().equals(IsArsSms.ARS.getCode())) {
            resultService.getRepository(record.getValue(VOC_GROUP.SEQ)).createTableIfNotExists();
            statVOCService.getRepository(record.getValue(VOC_GROUP.SEQ)).createTableIfNotExists();
        }
        customListService.getRepository(record.getValue(VOC_GROUP.SEQ)).createTableIfNotExists();

        cacheService.pbxServerList(getCompanyId()).forEach(e -> {
            try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
                pbxDsl.insertInto(VOC_GROUP)
                        .set(dsl.newRecord(VOC_GROUP, groupRecord))
                        .execute();
            }
        });
    }

    public void updateByKey(VOCGroupFormRequest form, Integer groupId) {
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.VocGroup groupRecord = findOneIfNullThrow(groupId);
        groupRecord.setVocGroupName(form.getVocGroupName());
        groupRecord.setSender(form.getSender());
        groupRecord.setProcessKind(form.getProcessKind());
        groupRecord.setIsArsSms(form.getIsArsSms());
        groupRecord.setProcessKind(form.getProcessKind());
        groupRecord.setInformation(form.getInformation());
        if (form.getProcessKind().equals(ProcessKind.TERM.getCode())) {
            groupRecord.setStartTerm(form.getStartTerm());
            groupRecord.setEndTerm(form.getEndTerm());
        }

        if (form.getIsArsSms().equals(IsArsSms.ARS.getCode())) {
            if (form.getSender().equals("AUTO")) {
                if (Objects.isNull(researchListRepository.findOne(RESEARCH_LIST.RESEARCH_ID.eq(form.getArsResearchId())))) {
                    throw new IllegalArgumentException("해당 설문이 존재하지 않습니다.");
                }
                groupRecord.setOutboundTarget(form.getOutboundTarget());
                if (form.getOutboundTarget().equals(InOutTarget.NO.getCode()))
                    groupRecord.setOutboundCallKind(form.getOutboundCallKind());
                if (form.getOutboundTarget().equals(InOutTarget.CIDNUM.getCode()))
                    groupRecord.setOutboundTargetCidnum(form.getOutboundTargetCidnum());

                groupRecord.setInboundTarget(form.getInboundTarget());
                if (form.getInboundTarget().equals(InOutTarget.NO.getCode()))
                    groupRecord.setInboundCallKind(form.getInboundCallKind());
                if (form.getInboundTarget().equals(InOutTarget.SVCNUM.getCode())) {
                    groupRecord.setInboundTargetSvcnum(form.getInboundTargetSvcnum());
                }
            }
            groupRecord.setArsResearchId(form.getArsResearchId());
        }

        super.updateByKey(groupRecord, groupId);
        memberListRepository.delete(VOC_MEMBER_LIST.VOC_GROUP_ID.eq(groupId));

        final VocMemberList memberRecord = new VocMemberList();
        memberRecord.setSeq(memberListRepository.nextSequence());
        memberRecord.setVocGroupId(groupId);
        memberRecord.setCompanyId(getCompanyId());
        if (form.getIsArsSms().equals("ARS") && form.getSender().equals("AUTO")) {
            if (form.getOutboundTarget().contains(InOutTarget.MEMBER.getCode())) {
                memberRecord.setOutboundCallKind(form.getOutboundCallKind());
                memberList.addAll(form.getOutboundMemberList());
            }
            if (form.getInboundTarget().contains(InOutTarget.MEMBER.getCode())) {
                memberRecord.setInboundCallKind(form.getInboundCallKind());

                for (String member : form.getInboundMemberList()) {
                    if (!memberList.contains(member))
                        memberList.add(member);
                }
            }
            if (memberList.size() > 0) {
                for (String member : memberList) {
                    memberRecord.setUserid(member);
                    memberListRepository.insert(memberRecord);
                }
            }
        }

        cacheService.pbxServerList(getCompanyId()).forEach(e -> {
            try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
                super.updateByKey(pbxDsl, groupRecord, groupId);
            }
        });
    }

    public int deleteOnIfNullThrow(Integer groupId) {
        int deleteRow = super.deleteOnIfNullThrow(groupId);

        memberListRepository.delete(VOC_MEMBER_LIST.VOC_GROUP_ID.eq(groupId));

        customListService.getRepository(groupId).dropTableIfExists();

        statVOCService.getRepository(groupId).dropTableIfExists();

        cacheService.pbxServerList(getCompanyId()).forEach(e -> {
            try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
                super.delete(pbxDsl, groupId);
            }
        });

        return deleteRow;
    }

    public kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.VocGroup getLatestRegisterGroup() {
        return dsl.select().from(VOC_GROUP).where(compareCompanyId()).orderBy(VOC_GROUP.SEQ.desc())
                .limit(1)
                .fetchOneInto(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.VocGroup.class);
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.VocGroup> getArsSmsList(String type) {
        final VOCGroupSearchRequest search = new VOCGroupSearchRequest();
        search.setIsArsSms(type);
        search.setSender(VocGroupSender.MEMBER.getCode());
        search.getProcessKind().add(ProcessKind.CONTINUE.getCode());
        search.getProcessKind().add(ProcessKind.TERM.getCode());
        search.setStartTerm(new java.sql.Date(System.currentTimeMillis()));
        search.setEndTerm(new Date(System.currentTimeMillis()));

        return findAll(search);
    }

    private List<Condition> conditions(VOCGroupSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        List<Condition> processConditions = new ArrayList<>();

        for (int i = 0; i < search.getProcessKind().size(); i++) {
            Condition processCondition = VOC_GROUP.PROCESS_KIND.eq(search.getProcessKind().get(i));

            if (search.getProcessKind().get(i).equals(ProcessKind.TERM.getCode())) {
                final Condition conditionByStartTerm = VOC_GROUP.START_TERM.le(search.getStartTerm()).and(VOC_GROUP.END_TERM.ge(search.getStartTerm()));
                final Condition conditionByEndTerm = VOC_GROUP.START_TERM.le(search.getEndTerm()).and(VOC_GROUP.END_TERM.ge(search.getEndTerm()));

                processCondition = processCondition.and(conditionByStartTerm.or(conditionByEndTerm));
            }
            processConditions.add(processCondition);
        }

        conditions.add(processConditions.stream().reduce(Condition::or).orElse(DSL.noCondition()));

        if (isNotEmpty(search.getIsArsSms()))
            conditions.add(VOC_GROUP.IS_ARS_SMS.eq(search.getIsArsSms()));

        if (isNotEmpty(search.getSender()))
            conditions.add(VOC_GROUP.SENDER.eq(search.getSender()));

        return conditions;
    }
}
