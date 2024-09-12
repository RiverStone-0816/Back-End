package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.Tables;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.PdsGroup;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonType;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTree;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.HistoryPdsGroup;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.records.ExecutePdsGroupRecord;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.records.PdsGroupRecord;
import kr.co.eicn.ippbx.model.entity.eicn.CompanyServerEntity;
import kr.co.eicn.ippbx.model.entity.eicn.ExecutePDSGroupEntity;
import kr.co.eicn.ippbx.model.enums.*;
import kr.co.eicn.ippbx.model.form.PDSExecuteFormRequest;
import kr.co.eicn.ippbx.model.form.PDSGroupFormRequest;
import kr.co.eicn.ippbx.model.search.PDSGroupSearchRequest;
import kr.co.eicn.ippbx.model.search.PDSMonitSearchRequest;
import kr.co.eicn.ippbx.server.config.Constants;
import kr.co.eicn.ippbx.server.service.*;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.*;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.ExecutePdsGroup.EXECUTE_PDS_GROUP;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.PdsGroup.PDS_GROUP;
import static org.apache.commons.lang3.StringUtils.*;

@Getter
@Repository
public class PDSGroupRepository extends EicnBaseRepository<PdsGroup, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsGroup, Integer> {
    private final Logger logger = LoggerFactory.getLogger(PDSGroupRepository.class);

    private final PDSCustomInfoService        pdsCustomInfoService;
    private final ExecutePDSCustomInfoService executePDSCustomInfoService;
    private final ExecutePDSGroupRepository   executePDSGroupRepository;
    private final PDSResearchResultService    pdsResearchResultService;
    private final PDSResultCustomInfoService  pdsResultCustomInfoService;
    private final HistoryPDSGroupRepository   historyPDSGroupRepository;
    private final CompanyTreeRepository       companyTreeRepository;
    private final WebSecureHistoryRepository  webSecureHistoryRepository;
    private final HistoryUploadInfoRepository historyUploadInfoRepository;
    private final CommonTypeRepository        commonTypeRepository;
    private final CacheService                cacheService;
    private final PBXServerInterface          pbxServerInterface;

    public PDSGroupRepository(PDSCustomInfoService pdsCustomInfoService, ExecutePDSCustomInfoService executePDSCustomInfoService, ExecutePDSGroupRepository executePDSGroupRepository,
            PDSResearchResultService pdsResearchResultService, PDSResultCustomInfoService pdsResultCustomInfoService, HistoryPDSGroupRepository historyPDSGroupRepository,
            CompanyTreeRepository companyTreeRepository, WebSecureHistoryRepository webSecureHistoryRepository, HistoryUploadInfoRepository historyUploadInfoRepository,
            CommonTypeRepository commonTypeRepository, CacheService cacheService, PBXServerInterface pbxServerInterface
    ) {
        super(PDS_GROUP, PDS_GROUP.SEQ, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsGroup.class);
        orderByFields.add(PDS_GROUP.MAKE_DATE.asc());

        this.pdsCustomInfoService = pdsCustomInfoService;
        this.executePDSCustomInfoService = executePDSCustomInfoService;
        this.executePDSGroupRepository = executePDSGroupRepository;
        this.pdsResearchResultService = pdsResearchResultService;
        this.pdsResultCustomInfoService = pdsResultCustomInfoService;
        this.historyPDSGroupRepository = historyPDSGroupRepository;
        this.companyTreeRepository = companyTreeRepository;
        this.webSecureHistoryRepository = webSecureHistoryRepository;
        this.historyUploadInfoRepository = historyUploadInfoRepository;
        this.commonTypeRepository = commonTypeRepository;
        this.cacheService = cacheService;
        this.pbxServerInterface = pbxServerInterface;
    }

    public Pagination<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsGroup> pagination(PDSGroupSearchRequest search) {
        return pagination(search, conditions(search), Collections.singletonList(PDS_GROUP.MAKE_DATE.desc()));
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsGroup> findAllOrderByLastExecuteDate() {
        return dsl.select(PDS_GROUP.fields())
                .from(PDS_GROUP)
                .where(compareCompanyId())
                .orderBy(PDS_GROUP.LAST_EXECUTE_DATE.desc())
                .fetchInto(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsGroup.class);
    }

    public void insertWithPDSCustomInfoTableCreate(PDSGroupFormRequest form) {
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsGroup record = new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsGroup();

        final Optional<CommonType> optionalPdsTypeEntity =
                commonTypeRepository.findAll(COMMON_TYPE.SEQ.eq(form.getPdsType())
                                                     .and(COMMON_TYPE.KIND.eq(CommonTypeKind.PDS.getCode()))
                                                     .and(COMMON_TYPE.STATUS.eq(CommonTypeStatus.USING.getCode()))).stream().findAny();
        if (!optionalPdsTypeEntity.isPresent())
            throw new IllegalArgumentException("PDS유형만 등록 할 수 있습니다.");

        record.setName(form.getName());
        record.setInfo(defaultString(form.getInfo()));
        if (isNotEmpty(form.getGroupCode())) {
            final CompanyTree companyTree = companyTreeRepository.findOneByGroupCode(form.getGroupCode());
            if (companyTree != null) {
                record.setGroupCode(form.getGroupCode());
                record.setGroupTreeName(companyTree.getGroupTreeName());
                record.setGroupLevel(companyTree.getGroupLevel());
            }
        }

        record.setTotalCnt(0);
        record.setPdsType(form.getPdsType());
        record.setCompanyId(getCompanyId());

        record.setRidKind(form.getRidKind());
        record.setRidData(form.getRidData());
        record.setBillingKind(defaultIfEmpty(form.getBillingKind(), PDSGroupBillingKind.PBX.getCode()));
        record.setBillingData(defaultString(form.getBillingData()));
        record.setConnectKind(form.getConnectKind());
        record.setConnectData(form.getConnectData());
        record.setSpeedKind(form.getSpeedKind());
        record.setSpeedData(form.getSpeedData());
        record.setResultKind(form.getResultKind());
        record.setResultType((PDSGroupResultKind.NONE.getCode().equals(form.getResultKind())) || (form.getResultType() == null) ? 0 : form.getResultType());

        record.setDialTimeout(form.getDialTimeout());
        record.setMachineDetect("N"); // 사용안함 default로 세팅
        record.setIsRecord(form.getIsRecord());
        record.setNumberField("ALL".equals(form.getNumberField()) ? EMPTY : form.getNumberField());
        record.setRunHost(form.getRunHost());
        record.setLastUploadId(EMPTY);
        record.setLastUploadName(EMPTY);
        record.setLastUploadStatus(EMPTY);
        record.setUploadTryCnt(0);
        record.setLastExecuteId(EMPTY);
        record.setLastExecuteName(EMPTY);
        record.setLastExecuteStatus(EMPTY);
        record.setExecuteTryCnt(0);

        // pds_group 등록은 web서버에만(김옥중)
        final PdsGroupRecord r = dsl.insertInto(PDS_GROUP)
                .set(dsl.newRecord(PDS_GROUP, record))

                .set(PDS_GROUP.MAKE_DATE, DSL.now())
                .returning()
                .fetchOne();

        pdsCustomInfoService.getRepository(r.getSeq()).createTableIfNotExists();

        webSecureHistoryRepository.insert(WebSecureActionType.PDS_GROUP, WebSecureActionSubType.ADD, form.getName());
    }

    public void updateByKey(PDSGroupFormRequest form, Integer seq) {
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsGroup record = findOneIfNullThrow(seq);

        final Optional<CommonType> optionalPdsTypeEntity =
                commonTypeRepository.findAll(COMMON_TYPE.SEQ.eq(form.getPdsType())
                                                     .and(COMMON_TYPE.KIND.eq(CommonTypeKind.PDS.getCode()))
                                                     .and(COMMON_TYPE.STATUS.eq(CommonTypeStatus.USING.getCode()))).stream().findAny();
        if (!optionalPdsTypeEntity.isPresent())
            throw new IllegalArgumentException("PDS유형만 등록 할 수 있습니다.");

        record.setName(form.getName());
        record.setPdsType(form.getPdsType());
        record.setInfo(defaultString(form.getInfo()));
        if (isNotEmpty(form.getGroupCode())) {
            final CompanyTree companyTree = companyTreeRepository.findOneByGroupCode(form.getGroupCode());
            if (companyTree != null) {
                record.setGroupCode(form.getGroupCode());
                record.setGroupTreeName(companyTree.getGroupTreeName());
                record.setGroupLevel(companyTree.getGroupLevel());
            }
        }
        record.setRunHost(form.getRunHost());
        record.setDialTimeout(form.getDialTimeout());
        record.setIsRecord(form.getIsRecord());
        record.setNumberField("ALL".equals(form.getNumberField()) ? EMPTY : form.getNumberField());
        record.setRidKind(form.getRidKind());
        record.setRidData(form.getRidData());
        record.setBillingKind(defaultIfEmpty(form.getBillingKind(), PDSGroupBillingKind.PBX.getCode()));
        record.setBillingData(defaultString(form.getBillingData()));
        record.setConnectKind(form.getConnectKind());
        record.setConnectData(form.getConnectData());
        record.setSpeedKind(form.getSpeedKind());
        record.setSpeedData(form.getSpeedData());
        record.setResultKind(form.getResultKind());
        record.setResultType((PDSGroupResultKind.NONE.getCode().equals(form.getResultKind())) || (form.getResultType() == null) ? 0 : form.getResultType());

        super.updateByKey(record, seq);

        webSecureHistoryRepository.insert(WebSecureActionType.PDS_GROUP, WebSecureActionSubType.MOD, seq + "(" + seq + ")");
    }


    public void executeRequest(PDSExecuteFormRequest form, Integer seq) {
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsGroup entity = findOneIfNullThrow(seq);

        if (entity.getTotalCnt() == 0)
            throw new IllegalArgumentException("업로드된 데이터가 없습니다.");
        if ("U".equals(entity.getLastUploadStatus()))
            throw new IllegalArgumentException("업로드중이므로 실행할 수 없습니다.");
        if (isNotEmpty(entity.getLastExecuteStatus()) && !PDSGroupExecuteStatus.FINISHED.getCode().equals(entity.getLastExecuteStatus()))
            throw new IllegalArgumentException("이미 실행요청된 그룹입니다.");

        final String executeId = seq + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        final List<ExecutePDSGroupEntity> executePDSGroupEntities = new ArrayList<>();

        if (Constants.LOCAL_HOST.equals(entity.getRunHost()))
            executePDSGroupEntities.addAll(executePDSGroupRepository.findAllGroupId(dsl, entity.getSeq()));
        else {
            final Optional<CompanyServerEntity> optionalServer = cacheService.pbxServerList(getCompanyId()).stream().filter(server -> server.getHost().equals(form.getRunHost())).findAny();
            if (optionalServer.isPresent()) {
                final CompanyServerEntity server = optionalServer.get();
                DSLContext pbxDsl = pbxServerInterface.using(server.getHost());
                executePDSGroupEntities.addAll(executePDSGroupRepository.findAllGroupId(pbxDsl, entity.getSeq()));
            }
        }

        final ExecutePdsGroupRecord executePdsGroupRecord = new ExecutePdsGroupRecord();

        if (executePDSGroupEntities.isEmpty()) {
            executePdsGroupRecord.setExecuteName(form.getExecuteName());
            executePdsGroupRecord.setExecuteId(executeId);
            executePdsGroupRecord.setPdsStatus(PDSGroupExecuteStatus.PREPARING.getCode());
            executePdsGroupRecord.setTotalRunTime(0);
            executePdsGroupRecord.setPdsGroupId(seq);
            executePdsGroupRecord.setPdsName(entity.getName());
            executePdsGroupRecord.setTotalCnt(entity.getTotalCnt());
            executePdsGroupRecord.setPdsType(entity.getPdsType());
            executePdsGroupRecord.setCompanyId(getCompanyId());
            executePdsGroupRecord.setRidKind(form.getRidKind());
            executePdsGroupRecord.setRidData(form.getRidData());
            executePdsGroupRecord.setBillingKind(form.getBillingKind());
            executePdsGroupRecord.setBillingData(form.getBillingData());
            executePdsGroupRecord.setConnectKind(entity.getConnectKind());
            executePdsGroupRecord.setConnectData(entity.getConnectData());
            executePdsGroupRecord.setSpeedKind(form.getSpeedKind());
            executePdsGroupRecord.setSpeedData(form.getSpeedData());
            executePdsGroupRecord.setResultKind(entity.getResultKind());
            executePdsGroupRecord.setResultType(entity.getResultType());

            executePdsGroupRecord.setDialTimeout(form.getDialTimeout());
            executePdsGroupRecord.setMachineDetect("N");
            executePdsGroupRecord.setIsRecord(form.getIsRecord());
            executePdsGroupRecord.setNumberField("ALL".equals(form.getNumberField()) ? EMPTY : form.getNumberField());
            executePdsGroupRecord.setRunHost(form.getRunHost());

            executePdsGroupRecord.setNumbersCnt(0);
            executePdsGroupRecord.setCalltryCnt(0);
            executePdsGroupRecord.setNocallCnt(0);
            executePdsGroupRecord.setLastCallStat(EMPTY);
            executePdsGroupRecord.setCurrentChanCnt(0);
            executePdsGroupRecord.setAdminid(g.getUser().getId());

            executePdsGroupRecord.setRequestStopKind("ALL");
            executePdsGroupRecord.setRequestStopDate(Timestamp.valueOf("1970-01-01 00:00:00"));
            executePdsGroupRecord.setRequestStopCnt(0);
            executePdsGroupRecord.setTtsField(EMPTY);
            executePdsGroupRecord.setIntroSound(0);
            executePdsGroupRecord.setEndingSound(0);
            executePdsGroupRecord.setReserveYn("N");
            executePdsGroupRecord.setReserveDate(Timestamp.valueOf("1970-01-01 00:00:00"));
            executePdsGroupRecord.setStartDate(new Timestamp(System.currentTimeMillis()));

            if (isNotEmpty(entity.getGroupCode())) {
                executePdsGroupRecord.setGroupCode(entity.getGroupCode());
                executePdsGroupRecord.setGroupTreeName(entity.getGroupTreeName());
                executePdsGroupRecord.setGroupLevel(entity.getGroupLevel());
            }

            if (Constants.LOCAL_HOST.equals(entity.getRunHost())) {
                dsl.insertInto(EXECUTE_PDS_GROUP)
                        .set(executePdsGroupRecord)

                        .set(EXECUTE_PDS_GROUP.START_DATE, DSL.now())
                        .execute();
            } else {
                cacheService.pbxServerList(getCompanyId()).stream().filter(server -> server.getHost().equals(form.getRunHost())).findAny().ifPresent(server -> {
                    DSLContext pbxDsl = pbxServerInterface.using(server.getHost());
                    pbxDsl.insertInto(EXECUTE_PDS_GROUP)
                            .set(executePdsGroupRecord)

                            .set(EXECUTE_PDS_GROUP.START_DATE, DSL.now())
                            .execute();
                });
            }

            historyPDSGroupRepository.insert(executePdsGroupRecord);

            if (PDSGroupConnectKind.ARS_RSCH.getCode().equals(entity.getConnectKind()))
                pdsResearchResultService.getRepository(executeId).createTableIfNotExists();
        } else {
            final ExecutePDSGroupEntity executePdsGroup = executePDSGroupEntities.get(0);
            if (PDSGroupExecuteStatus.READY.getCode().equals(executePdsGroup.getPdsStatus())
                    || PDSGroupExecuteStatus.PROCEEDING.getCode().equals(executePdsGroup.getPdsStatus()))
                throw new IllegalStateException("PDS(" + executePdsGroup.getPdsName() + ")가 진행중입니다.");

            executePdsGroupRecord.setExecuteName(form.getExecuteName());
            executePdsGroupRecord.setExecuteId(executeId);
            executePdsGroupRecord.setPdsStatus(PDSGroupExecuteStatus.PREPARING.getCode());
            executePdsGroupRecord.setTotalRunTime(0);
            executePdsGroupRecord.setPdsGroupId(seq);
            executePdsGroupRecord.setPdsName(entity.getName());
            executePdsGroupRecord.setTotalCnt(entity.getTotalCnt());
            executePdsGroupRecord.setPdsType(entity.getPdsType());
            executePdsGroupRecord.setCompanyId(getCompanyId());
            executePdsGroupRecord.setRidKind(form.getRidKind());
            executePdsGroupRecord.setRidData(form.getRidData());
            executePdsGroupRecord.setBillingKind(form.getBillingKind());
            executePdsGroupRecord.setBillingData(form.getBillingData());
            executePdsGroupRecord.setConnectKind(entity.getConnectKind());
            executePdsGroupRecord.setConnectData(entity.getConnectData());
            executePdsGroupRecord.setSpeedKind(form.getSpeedKind());
            executePdsGroupRecord.setSpeedData(form.getSpeedData());
            executePdsGroupRecord.setResultKind(entity.getResultKind());
            executePdsGroupRecord.setResultType(entity.getResultType());

            executePdsGroupRecord.setDialTimeout(form.getDialTimeout());
            executePdsGroupRecord.setMachineDetect("N");
            executePdsGroupRecord.setIsRecord(form.getIsRecord());
            executePdsGroupRecord.setNumberField("ALL".equals(form.getNumberField()) ? EMPTY : form.getNumberField());
            executePdsGroupRecord.setRunHost(form.getRunHost());

            executePdsGroupRecord.setNumbersCnt(0);
            executePdsGroupRecord.setCalltryCnt(0);
            executePdsGroupRecord.setNocallCnt(0);
            executePdsGroupRecord.setLastCallStat(EMPTY);
            executePdsGroupRecord.setCurrentChanCnt(0);
            executePdsGroupRecord.setAdminid(g.getUser().getId());

            executePdsGroupRecord.setRequestStopKind("ALL");
            executePdsGroupRecord.setRequestStopDate(Timestamp.valueOf("1970-01-01 00:00:00"));
            executePdsGroupRecord.setRequestStopCnt(0);
            executePdsGroupRecord.setTtsField(EMPTY);
            executePdsGroupRecord.setIntroSound(0);
            executePdsGroupRecord.setEndingSound(0);
            executePdsGroupRecord.setReserveYn("N");
            executePdsGroupRecord.setReserveDate(Timestamp.valueOf("1970-01-01 00:00:00"));

            if (Constants.LOCAL_HOST.equals(entity.getRunHost()))
                executePDSGroupRepository.update(dsl, executePdsGroupRecord, Tables.EXECUTE_PDS_GROUP.PDS_GROUP_ID.eq(seq));
            else
                cacheService.pbxServerList(getCompanyId()).stream().filter(server -> server.getHost().equals(form.getRunHost())).findAny().ifPresent(server -> {
                    DSLContext pbxDsl = pbxServerInterface.using(server.getHost());
                    executePDSGroupRepository.update(pbxDsl, executePdsGroupRecord, Tables.EXECUTE_PDS_GROUP.PDS_GROUP_ID.eq(seq));
                });
        }

        dsl.update(PDS_GROUP)
                .set(PDS_GROUP.LAST_EXECUTE_ID, executeId)
                .set(PDS_GROUP.LAST_EXECUTE_NAME, form.getExecuteName())
                .set(PDS_GROUP.LAST_EXECUTE_STATUS, PDSGroupExecuteStatus.PREPARING.getCode())
                .set(PDS_GROUP.LAST_EXECUTE_DATE, DSL.now())
                .set(PDS_GROUP.EXECUTE_TRY_CNT, PDS_GROUP.EXECUTE_TRY_CNT.add(1))
                .where(PDS_GROUP.SEQ.eq(entity.getSeq()))
                .execute();

        if (Constants.LOCAL_HOST.equals(form.getRunHost()))
            executePDSCustomInfoService.getRepository(executeId).createTableIfNotExists();
        else
            cacheService.pbxServerList(getCompanyId()).stream().filter(server -> server.getHost().equals(form.getRunHost())).findAny().ifPresent(server -> {
                DSLContext pbxDsl = pbxServerInterface.using(server.getHost(), "PDS");
                executePDSCustomInfoService.getRepository(executeId).createTableIfNotExists(pbxDsl);
            });

    }

    public int deleteWithRelationShipTable(Integer seq) {
        final int deleteRow = deleteOnIfNullThrow(seq);
        final List<HistoryPdsGroup> historyPdsGroups = historyPDSGroupRepository.findAllGroupId(seq);

        for (HistoryPdsGroup group : historyPdsGroups) {
            dsl.update(HISTORY_PDS_GROUP)
                    .set(HISTORY_PDS_GROUP.PDS_STATUS, "X")
                    .where(HISTORY_PDS_GROUP.COMPANY_ID.eq(getCompanyId()))
                    .and(HISTORY_PDS_GROUP.SEQ.eq(group.getSeq()))
                    .execute();

            pdsResultCustomInfoService.getRepository(group.getExecuteId()).dropTableIfExists();
        }

        historyUploadInfoRepository.delete(HISTORY_UPLOAD_INFO.KIND.eq("PDS").and(HISTORY_UPLOAD_INFO.GROUP_ID.eq(seq)));

        webSecureHistoryRepository.insert(WebSecureActionType.PDS_GROUP, WebSecureActionSubType.DEL, seq + "(" + seq + ")");

        return deleteRow;
    }

    private List<Condition> conditions(PDSGroupSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        if (search.getStartDate() != null)
            conditions.add(PDS_GROUP.MAKE_DATE.ge(DSL.timestamp(search.getStartDate() + " 00:00:00")));
        if (search.getEndDate() != null)
            conditions.add(PDS_GROUP.MAKE_DATE.le(DSL.timestamp(search.getEndDate() + " 23:59:59")));
        if (search.getPdsType() != null)
            conditions.add(PDS_GROUP.PDS_TYPE.eq(search.getPdsType()));
        if (isNotEmpty(search.getName()))
            conditions.add(PDS_GROUP.NAME.eq(search.getName()));

        return conditions;
    }

    public Pagination<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsGroup> pagination(PDSMonitSearchRequest search) {
        return super.pagination(search, conditions(search));
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsGroup> findAll(PDSMonitSearchRequest search) {
        return findAll(conditions(search));
    }

    private List<Condition> conditions(PDSMonitSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        if (search.getStartDate() != null)
            conditions.add(PDS_GROUP.LAST_EXECUTE_DATE.ge(DSL.timestamp(search.getStartDate() + " 00:00:00")));
        if (search.getEndDate() != null)
            conditions.add(PDS_GROUP.LAST_EXECUTE_DATE.le(DSL.timestamp(search.getEndDate() + " 23:59:59")));
        if (isNotEmpty(search.getLastExecuteId()))
            conditions.add(PDS_GROUP.LAST_EXECUTE_ID.eq(search.getLastExecuteId()));

        conditions.add(PDS_GROUP.LAST_EXECUTE_STATUS.notEqual(""));
        conditions.add(PDS_GROUP.LAST_EXECUTE_STATUS.notEqual("D"));

        orderByFields.clear();
        orderByFields.add(PDS_GROUP.LAST_EXECUTE_DATE.asc());

        return conditions;
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsGroup> findAll(PDSGroupSearchRequest search) {
        return findAll(conditions(search));
    }
}
