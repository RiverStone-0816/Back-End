package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.enums.SipBuddiesType;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.PhoneInfo;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.*;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.records.PhoneInfoRecord;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.records.SipBuddiesRecord;
import kr.co.eicn.ippbx.model.entity.eicn.CompanyLicenceEntity;
import kr.co.eicn.ippbx.model.entity.eicn.CompanyServerEntity;
import kr.co.eicn.ippbx.model.enums.*;
import kr.co.eicn.ippbx.model.form.PhoneInfoFormRequest;
import kr.co.eicn.ippbx.model.form.PhoneInfoUpdateFormRequest;
import kr.co.eicn.ippbx.model.search.PhoneSearchRequest;
import kr.co.eicn.ippbx.server.service.*;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.InsertSetMoreStep;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.PhoneInfo.PHONE_INFO;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.QueueMemberTable.QUEUE_MEMBER_TABLE;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.QueueTable.QUEUE_TABLE;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.SipBuddies.SIP_BUDDIES;
import static org.apache.commons.lang3.StringUtils.*;
import static org.jooq.tools.StringUtils.defaultString;

@Getter
@Repository
public class ExtensionRepository extends EicnBaseRepository<PhoneInfo, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PhoneInfo, String> {
    protected final Logger logger = LoggerFactory.getLogger(ExtensionRepository.class);
    private final CompanyService companyService;
    private final Number070Repository numberRepository;
    private final CompanyServerRepository companyServerRepository;
    private final SipBuddiesRepository sipBuddiesRepository;
    private final QueueTableRepository queueTableRepository;
    private final QueueMemberTableRepository queueMemberTableRepository;
    private final WebSecureHistoryRepository webSecureHistoryRepository;
    private final PersonListRepository personListRepository;
    private final PBXServerInterface pbxServerInterface;
    private final CacheService cacheService;
    private final ProcessService processService;

    public ExtensionRepository(CompanyService companyService, Number070Repository numberRepository, CompanyServerRepository companyServerRepository, SipBuddiesRepository sipBuddiesRepository, QueueTableRepository queueTableRepository,
                               QueueMemberTableRepository queueMemberTableRepository, WebSecureHistoryRepository webSecureHistoryRepository, PersonListRepository personListRepository,
                               PBXServerInterface pbxServerInterface, CacheService cacheService, ProcessService processService) {
        super(PHONE_INFO, PHONE_INFO.PEER, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PhoneInfo.class);
        this.companyService = companyService;
        this.numberRepository = numberRepository;
        this.companyServerRepository = companyServerRepository;
        this.sipBuddiesRepository = sipBuddiesRepository;
        this.queueTableRepository = queueTableRepository;
        this.queueMemberTableRepository = queueMemberTableRepository;
        this.webSecureHistoryRepository = webSecureHistoryRepository;
        this.personListRepository = personListRepository;
        this.pbxServerInterface = pbxServerInterface;
        this.cacheService = cacheService;
        this.processService = processService;
    }

    public Pagination<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PhoneInfo> pagination(PhoneSearchRequest search) {
        return super.pagination(search, conditions(search), Collections.singletonList(PHONE_INFO.EXTENSION.asc()));
    }

    /**
     * 1. phone_info insert 내선정보 등록
     * 2. sipbuddies insert
     * 3. queue_member_table 개인큐 생성
     * 4. queue_table 라우팅큐 생성
     */
    public void insert(PhoneInfoFormRequest form) {
        final Number_070 number = numberRepository.findOneIfNullThrow(form.getNumber());
        if (Number070Status.USE.getCode().equals(number.getStatus()))
            throw new IllegalArgumentException("이미 사용중인 070번호입니다.");

        final CompanyLicenceEntity companyLicenceInfo = companyService.getCompanyLicenceInfo();

        // 라이센스 체크
        if (form.getRecordType().equals(RecordType.RECORDING.getCode())) {
            if (!companyLicenceInfo.getRecordLicense().isUseLicense())
                form.setRecordType(RecordType.NONE_RECORDING.getCode());
        }

        if (form.getStt().equals(SttType.STT.getCode())) {
            if (!companyLicenceInfo.getSttLicense().isUseLicense())
                form.setStt(SttType.NONE_STT.getCode());
        }

        if (form.getSoftphone().equals(SoftPhoneType.SOFTPHONE.getCode())) {
            if (!companyLicenceInfo.getSoftPhoneLicense().isUseLicense()) {
                form.setSoftphone(SoftPhoneType.NONE_SOFTPHONE.getCode());
                form.setPhoneKind("N");
            }
        }

        final PhoneInfoRecord insertRecord = new PhoneInfoRecord();
        insertRecord.setPeer(form.getPeer());
        insertRecord.setExtension(form.getExtension());
        insertRecord.setVoipTel(number.getNumber());
        insertRecord.setForwardWhen(form.getForwardWhen());
        insertRecord.setForwarding(ForwardWhen.NONE.getCode().equals(form.getForwardWhen()) ? EMPTY : form.getForwardKind().concat("|").concat(form.getForwardNum()));

        insertRecord.setPrevent(form.getPrevent());
        insertRecord.setRecordType(form.getRecordType());
        insertRecord.setCid(isEmpty(form.getCid()) ? number.getNumber() : form.getCid());
        insertRecord.setOutboundGw(form.getOutboundGw());
        insertRecord.setBillingNumber(StringUtils.isNotEmpty(form.getBillingNumber()) ? form.getBillingNumber() : number.getNumber());
        insertRecord.setLocalPrefix(form.getLocalPrefix());
        insertRecord.setGroupCode(number.getGroupCode());
        insertRecord.setGroupLevel(number.getGroupLevel());
        insertRecord.setGroupTreeName(number.getGroupTreeName());
        insertRecord.setHost(defaultString(number.getHost(), "localhost"));
        insertRecord.setPickup(EMPTY);
        insertRecord.setLogoutStatus((byte) 0);
        insertRecord.setDialStatus((byte) 2);
        insertRecord.setPhoneStatus("registered");
        insertRecord.setCompanyId(getCompanyId());
        insertRecord.setStt(SttType.STT.getCode().equals(form.getStt()) ? SttType.STT.getCode() : SttType.NONE_STT.getCode() );
        insertRecord.setSoftphone(SoftPhoneType.SOFTPHONE.getCode().equals(form.getSoftphone()) ? SoftPhoneType.SOFTPHONE.getCode() : SoftPhoneType.NONE_SOFTPHONE.getCode());

        insertRecord.setPhoneKind(StringUtils.isEmpty(form.getPhoneKind()) ? "N" : form.getPhoneKind()); //사용안함
        insertRecord.setHpDeviceId(EMPTY); //사용안함
        insertRecord.setOriginalNumber(isEmpty(form.getOriginalNumber()) ? number.getNumber() : form.getOriginalNumber());
        insertRecord.setOutboundGw(form.getOutboundGw());

        // 고객사 PBX HOST정보
        final List<CompanyServerEntity> pbxServerList = cacheService.pbxServerList(getCompanyId());

        deleteWithInsertPhone(dsl, insertRecord);
        pbxServerList.forEach(e -> {
            DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
            deleteWithInsertPhone(pbxDsl, insertRecord);
        });

        final SipBuddiesRecord sipBuddiesRecord = new SipBuddiesRecord();
        sipBuddiesRecord.setAccountcode(form.getNumber());
        sipBuddiesRecord.setCallerid("\"" + form.getExtension() + "\" <" + form.getNumber() + ">");
        sipBuddiesRecord.setMd5secret(form.getPeer() + ":eicn:" + form.getPasswd());
        sipBuddiesRecord.setType(form.getType().equals(ServiceKind.CC.getCode()) ? SipBuddiesType.friend : SipBuddiesType.peer);
        sipBuddiesRecord.setName(form.getPeer());
        sipBuddiesRecord.setPickupgroup("1");
        sipBuddiesRecord.setCallgroup("1");

        deleteAndInsertBuddies(dsl, sipBuddiesRecord);
        pbxServerList.forEach(e -> {
            DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
            deleteAndInsertBuddies(pbxDsl, sipBuddiesRecord);
        });

        // 개인큐를 위한 큐설정
        final QueueMemberTable queueMemberEntity = new QueueMemberTable();
        queueMemberEntity.setMembername(form.getPeer());
        queueMemberEntity.setInterface("SIP/".concat(form.getPeer()));
        queueMemberEntity.setPenalty(1);
        queueMemberEntity.setIsLogin("N");
        queueMemberEntity.setBlendingMode("N");
        queueMemberEntity.setCompanyId(g.getUser().getCompanyId());
        queueMemberEntity.setPaused(0);
        queueMemberEntity.setQueueName(EMPTY);

        queueMemberTableRepository.insertAllPbxServers(queueMemberEntity);

        // 개인큐를 위한 멤버설정
        queueMemberEntity.setPaused(0);
        queueMemberEntity.setQueueName(form.getPeer());

        queueMemberTableRepository.insertAllPbxServers(queueMemberEntity);

        // DB라우팅큐를 위한 멤버설정
        queueMemberEntity.setQueueName("QUEUE".concat(form.getPeer()));
        queueMemberTableRepository.insertAllPbxServers(queueMemberEntity);

        // 개인큐를 위한 큐설정
        final QueueTable queueTableEntity = new QueueTable();
        queueTableEntity.setName(form.getPeer());
        queueTableEntity.setMusiconhold("default");
        queueTableEntity.setTimeout(20);
        queueTableEntity.setLeavewhenempty("no");
        queueTableEntity.setMonitorType("1");
        queueTableEntity.setMonitorFormat("wav");
        queueTableEntity.setAnnounceFrequency(0);
        queueTableEntity.setAnnounceRoundSeconds(0);
        queueTableEntity.setAnnounceHoldtime("no");
        queueTableEntity.setRetry(1);
        queueTableEntity.setWrapuptime(0);
        queueTableEntity.setMaxlen(20);
        queueTableEntity.setServicelevel(60);
        queueTableEntity.setStrategy(CallDistributionStrategy.RRMEMORY.getCode());
        queueTableEntity.setJoinempty("yes");
        queueTableEntity.setLeavewhenempty("no");
        queueTableEntity.setEventmemberstatus(false);
        queueTableEntity.setEventwhencalled(true);
        queueTableEntity.setReportholdtime(false);
        queueTableEntity.setMemberdelay(0);
        queueTableEntity.setWeight(0);
        queueTableEntity.setTimeoutrestart(false);
        queueTableEntity.setPeriodicAnnounce(EMPTY);
        queueTableEntity.setPeriodicAnnounceFrequency(0);
        queueTableEntity.setRinginuse(false);
        queueTableEntity.setSetinterfacevar(true);

        queueTableRepository.insertAllPbxServers(queueTableEntity);

        // DB라우팅등을 위해 QUEUE+peer로 큐를 만듬.
        queueTableEntity.setName("QUEUE" + form.getPeer());
        queueTableEntity.setMusiconhold(EMPTY);
        queueTableEntity.setTimeout(20);
        queueTableEntity.setLeavewhenempty("inuse,ringing,paused,invalid,unavailable");
        queueTableEntity.setMaxlen(0);

        queueTableRepository.insertAllPbxServers(queueTableEntity);

        // 070전화번호 업데이트
        numberRepository.updateStatusAllPbxServers(number.getNumber(), Number070Status.USE.getCode());

        webSecureHistoryRepository.insert(form.getExtension(), WebSecureActionType.PHONE, WebSecureActionSubType.ADD, form.getExtension());
    }

    public void update(PhoneInfoUpdateFormRequest form) {
        final Number_070 number = numberRepository.findOneIfNullThrow(form.getNumber());
        final CompanyLicenceEntity companyLicenceInfo = companyService.getCompanyLicenceInfo();

        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PhoneInfo old = findOne(form.getOldPeer());
        final List<CompanyServerEntity> pbxServerList = cacheService.pbxServerList(getCompanyId());

        // 라이센스 체크
        if (RecordType.NONE_RECORDING.getCode().equals(old.getRecordType()) && RecordType.RECORDING.getCode().equals(form.getRecordType())) {
            if (!companyLicenceInfo.getRecordLicense().isUseLicense())
                form.setRecordType(RecordType.NONE_RECORDING.getCode());
        }

        if (SttType.NONE_STT.getCode().equals(old.getStt()) && SttType.STT.getCode().equals(form.getStt())) {
            if (!companyLicenceInfo.getSttLicense().isUseLicense())
                form.setStt(SttType.NONE_STT.getCode());
        }

        if (SoftPhoneType.NONE_SOFTPHONE.getCode().equals(old.getSoftphone()) && SoftPhoneType.SOFTPHONE.getCode().equals(form.getSoftphone())) {
            if (!companyLicenceInfo.getSoftPhoneLicense().isUseLicense()) {
                form.setSoftphone(SoftPhoneType.NONE_SOFTPHONE.getCode());
                form.setPhoneKind("N");
            }
        }

        final Number_070 oldNumber070 = numberRepository.findOne(old.getVoipTel());

        // number_070번호가 바꼈다면 number_070 사용유무 변경
        if (oldNumber070 != null)
            numberRepository.updateStatusAllPbxServers(old.getVoipTel(), Number070Status.NON_USE.getCode());

        // 신규 number_070 사용으로 상태변경
        numberRepository.updateStatusAllPbxServers(form.getNumber(), Number070Status.USE.getCode());

        // 신규 연락처 등록
        final PhoneInfoRecord insertRecord = new PhoneInfoRecord();
        insertRecord.setPeer(form.getPeer());
        insertRecord.setExtension(form.getExtension());
        insertRecord.setVoipTel(form.getNumber());
        insertRecord.setForwardWhen(form.getForwardWhen());
        insertRecord.setForwarding(ForwardWhen.NONE.getCode().equals(form.getForwardWhen())
                ? EMPTY : form.getForwardKind().concat("|").concat(form.getForwardNum()));
        insertRecord.setPrevent(form.getPrevent());
        insertRecord.setRecordType(form.getRecordType());
        insertRecord.setCid(Objects.isNull(form.getCid()) ? number.getNumber() : form.getCid());
        insertRecord.setOutboundGw(form.getOutboundGw());
        insertRecord.setBillingNumber(StringUtils.isNotEmpty(form.getBillingNumber()) ? form.getBillingNumber() : number.getNumber());
        insertRecord.setLocalPrefix(form.getLocalPrefix());
        insertRecord.setHost(defaultString(number.getHost(), "localhost"));
        insertRecord.setCompanyId(getCompanyId());
        insertRecord.setGroupCode(old.getGroupCode());
        insertRecord.setGroupLevel(old.getGroupLevel());
        insertRecord.setGroupTreeName(old.getGroupTreeName());
        insertRecord.setFirstStatus(old.getFirstStatus());
        insertRecord.setLogoutStatus(old.getLogoutStatus());
        insertRecord.setDialStatus(old.getDialStatus());
        insertRecord.setPhoneStatus("registered");
        insertRecord.setPickup(old.getPickup());
        insertRecord.setStt(SttType.STT.getCode().equals(form.getStt()) ? SttType.STT.getCode() : SttType.NONE_STT.getCode());
        insertRecord.setSoftphone(SoftPhoneType.SOFTPHONE.getCode().equals(form.getSoftphone()) ? SoftPhoneType.SOFTPHONE.getCode() : SoftPhoneType.NONE_SOFTPHONE.getCode());
        insertRecord.setPhoneKind(StringUtils.isEmpty(form.getPhoneKind()) ? "N" : form.getPhoneKind());
        insertRecord.setHpNumber(EMPTY);
        insertRecord.setHpDeviceId(EMPTY);
        insertRecord.setOutboundGw(form.getOutboundGw());
        insertRecord.setOriginalNumber(isEmpty(form.getOriginalNumber()) ? old.getVoipTel() : form.getOriginalNumber());

        // 이전 연락처 삭제
        dsl.deleteFrom(PHONE_INFO)
                .where(compareCompanyId())
                .and(PHONE_INFO.PEER.eq(old.getPeer()))
                .execute();

        deleteWithInsertPhone(dsl, insertRecord);

        pbxServerList.forEach(entity -> {
            DSLContext pbxDsl = pbxServerInterface.using(entity.getHost());
            pbxDsl.deleteFrom(PHONE_INFO)
                    .where(compareCompanyId())
                    .and(PHONE_INFO.PEER.eq(old.getPeer()))
                    .execute();
            deleteWithInsertPhone(pbxDsl, insertRecord);
        });

        SipBuddies sipBuddies = null;

        if ("localhost".equals(number.getHost())) {
            sipBuddies = dsl.select(SIP_BUDDIES.fields())
                    .from(SIP_BUDDIES)
                    .where(DSL.trueCondition())
                    .and(SIP_BUDDIES.NAME.eq(form.getPeer()))
                    .fetchOneInto(SipBuddies.class);
        } else {
            final Optional<CompanyServerEntity> byPbxHost = pbxServerList.stream()
                    .filter(e -> e.getHost().equals(number.getHost()))
                    .findAny();
            if (byPbxHost.isPresent()) {
                DSLContext pbxDsl = pbxServerInterface.using(number.getHost());
                sipBuddies = pbxDsl.select(SIP_BUDDIES.fields())
                        .from(SIP_BUDDIES)
                        .where(DSL.trueCondition())
                        .and(SIP_BUDDIES.NAME.eq(form.getPeer()))
                        .fetchOneInto(SipBuddies.class);
            }
        }
        if (sipBuddies != null) {
            if (isNotEmpty(form.getPasswd()))
                sipBuddies.setMd5secret(form.getPeer() + ":eicn:" + form.getPasswd());

            updateSipBuddies(form, sipBuddies, dsl);

            processService.execute(ShellCommand.PICKUP_UPDATE, form.getPeer(), " &");

            for (CompanyServerEntity entity : pbxServerList) {
                DSLContext pbxDsl = pbxServerInterface.using(entity.getHost());
                updateSipBuddies(form, sipBuddies, pbxDsl);
                IpccUrlConnection.execute("http://" + entity.getServer().getIp() + "/ipcc/multichannel/remote/pickup_update.jsp", form.getPeer());
            }
        } else {
            // sip_buddies 저장
            final SipBuddiesRecord sipBuddiesRecord = new SipBuddiesRecord();
            sipBuddiesRecord.setAccountcode(form.getNumber());
            sipBuddiesRecord.setCallerid("\"" + form.getExtension() + "\" <" + form.getNumber() + ">");
            sipBuddiesRecord.setType(form.getType().equals(ServiceKind.CC.getCode()) ? SipBuddiesType.friend : SipBuddiesType.peer);
            sipBuddiesRecord.setName(form.getPeer());

            if (isNotEmpty(form.getPasswd()))
                sipBuddiesRecord.setMd5secret(form.getPeer() + ":eicn:" + form.getPasswd());

            sipBuddiesRepository.deleteAndNameAllPbxServers(old.getPeer());

            deleteAndInsertBuddies(dsl, sipBuddiesRecord);

            pbxServerList.forEach(e -> {
                DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
                deleteAndInsertBuddies(pbxDsl, sipBuddiesRecord);
            });
        }
        // peer가 변경되었다면 큐 정보 삭제
        if (!old.getPeer().equals(form.getPeer())) {
            dsl.deleteFrom(QUEUE_MEMBER_TABLE)
                    .where(QUEUE_MEMBER_TABLE.COMPANY_ID.eq(getCompanyId()))
                    .and(QUEUE_MEMBER_TABLE.MEMBERNAME.eq(old.getPeer()))
                    .execute();
            pbxServerList.forEach(entity -> {
                DSLContext pbxDsl = pbxServerInterface.using(entity.getHost());
                pbxDsl.deleteFrom(QUEUE_MEMBER_TABLE)
                        .where(QUEUE_MEMBER_TABLE.COMPANY_ID.eq(getCompanyId()))
                        .and(QUEUE_MEMBER_TABLE.MEMBERNAME.eq(old.getPeer()))
                        .execute();
            });
        }

        Map<String, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueMemberTable> queueMemberTableMap;

        Optional<CompanyServerEntity> byPbxHost = pbxServerList.stream()
                .filter(e -> e.getHost().equals(number.getHost()))
                .findAny();
        if (byPbxHost.isPresent()) {
            DSLContext pbxDsl = pbxServerInterface.using(byPbxHost.get().getHost());
            queueMemberTableMap = queueMemberTableRepository.findByMemberName(pbxDsl, form.getPeer());
        } else {
            queueMemberTableMap = queueMemberTableRepository.findByMemberName(dsl, form.getPeer());
        }
        // --> queue_member_table 처리
        // 개별큐를 위한 멤버설정 membername==peer queuename=''
        int paused = 9;
        String isLogin = "N";
        final QueueMemberTable queueMemberTableEntity = new QueueMemberTable();
        queueMemberTableEntity.setQueueName(EMPTY);
        queueMemberTableEntity.setMembername(form.getPeer());
        queueMemberTableEntity.setInterface("SIP/".concat(form.getPeer()));
        queueMemberTableEntity.setPenalty(0);
        queueMemberTableEntity.setPaused(paused);
        queueMemberTableEntity.setIsLogin(isLogin);
        queueMemberTableEntity.setBlendingMode("N");
        queueMemberTableEntity.setCompanyId(g.getUser().getCompanyId());

        if (Objects.nonNull(queueMemberTableMap.get(""))) {
            paused = queueMemberTableMap.get(EMPTY).getPaused();
            isLogin = queueMemberTableMap.get(EMPTY).getIsLogin();
        }
        if (Objects.isNull(queueMemberTableMap.get("")))
            queueMemberTableRepository.insertAllPbxServers(queueMemberTableEntity);

        //개별큐를 위한 멤버설정 membername==peer peer==queuename 상담원화면에서 받기버튼사용시
        if (Objects.isNull(queueMemberTableMap.get(form.getPeer()))) {
            queueMemberTableEntity.setQueueName(form.getPeer());
            queueMemberTableEntity.setPaused(paused);
            queueMemberTableEntity.setIsLogin(isLogin);

            queueMemberTableRepository.insertAllPbxServers(queueMemberTableEntity);
        }
        //DB라우팅큐를 위한 멤버설정
        if (Objects.isNull(queueMemberTableMap.get("QUEUE" + form.getPeer()))) {
            queueMemberTableEntity.setQueueName("QUEUE" + form.getPeer());
            queueMemberTableEntity.setPaused(paused);
            queueMemberTableEntity.setIsLogin(isLogin);

            queueMemberTableRepository.insertAllPbxServers(queueMemberTableEntity);
        }
        // --> queue_table 처리
        if (queueTableRepository.fetchCount(QUEUE_TABLE.NAME.eq(old.getPeer())) > 0) {
            queueTableRepository.deleteQueueTable(old.getPeer());
            queueTableRepository.deleteQueueTable(form.getPeer());
        }
        if (queueTableRepository.fetchCount(QUEUE_TABLE.NAME.eq("QUEUE".concat(old.getPeer()))) > 0) {
            queueTableRepository.deleteQueueTable("QUEUE".concat(old.getPeer()));
            queueTableRepository.deleteQueueTable("QUEUE".concat(form.getPeer()));
        }

        pbxServerList.stream()
                .filter(e -> e.getHost().equals(number.getHost()))
                .forEach(e -> {
                    DSLContext pbxContext = pbxServerInterface.using(number.getHost());
                    int result = pbxContext.selectFrom(QUEUE_TABLE)
                            .where(compareCompanyId(QUEUE_TABLE))
                            .and(QUEUE_TABLE.NAME.eq(old.getPeer()))
                            .execute();
                    if (result == 1) {
                        queueTableRepository.deleteQueueTable(pbxContext, old.getPeer());
                        queueTableRepository.deleteQueueTable(pbxContext, form.getPeer());
                    }
                    int queueResult = pbxContext.selectFrom(QUEUE_TABLE)
                            .where(compareCompanyId(QUEUE_TABLE))
                            .and(QUEUE_TABLE.NAME.eq("QUEUE".concat(old.getPeer())))
                            .execute();
                    if (queueResult == 1) {
                        queueTableRepository.deleteQueueTable(pbxContext, "QUEUE".concat(old.getPeer()));
                        queueTableRepository.deleteQueueTable(pbxContext, "QUEUE".concat(form.getPeer()));
                    }
                });

        //개인큐를 위한 큐설정
        final QueueTable queueTableRecord = new QueueTable();
        queueTableRecord.setName(form.getPeer());
        queueTableRecord.setMusiconhold("default");
        queueTableRecord.setTimeout(20);
        queueTableRecord.setLeavewhenempty("no");
        queueTableRecord.setMaxlen(20);
        queueTableRecord.setMonitorType("1");

        queueTableRecord.setAnnounceFrequency(0);
        queueTableRecord.setAnnounceRoundSeconds(0);
        queueTableRecord.setAnnounceHoldtime("no");
        queueTableRecord.setRetry(1);
        queueTableRecord.setWrapuptime(0);
        queueTableRecord.setServicelevel(60);
        queueTableRecord.setStrategy("rrmemory");
        queueTableRecord.setJoinempty("yes");
        queueTableRecord.setEventmemberstatus(false);
        queueTableRecord.setEventwhencalled(true);
        queueTableRecord.setReportholdtime(false);
        queueTableRecord.setMemberdelay(0);
        queueTableRecord.setWeight(0);
        queueTableRecord.setTimeoutrestart(false);
        queueTableRecord.setPeriodicAnnounce("");
        queueTableRecord.setPeriodicAnnounceFrequency(0);
        queueTableRecord.setRinginuse(false);
        queueTableRecord.setSetinterfacevar(true);

        // 개인큐를 위한 큐설정
        queueTableRepository.insertAllPbxServers(queueTableRecord);

        // DB라우팅등을 위해 QUEUE+peer로 큐를 만듬.
        queueTableRecord.setName("QUEUE" + form.getPeer());
        queueTableRecord.setMusiconhold(EMPTY);
        queueTableRecord.setTimeout(20);
        queueTableRecord.setLeavewhenempty("inuse,ringing,paused,invalid,unavailable");
        queueTableRecord.setMaxlen(0);

        queueTableRepository.insertAllPbxServers(queueTableRecord);

        if (cacheService.pbxServerList(getCompanyId()).size() > 0) {
            cacheService.pbxServerList(getCompanyId())
                    .forEach(e -> {
                        if (!"localhost".equals(e.getHost())) {
                            IpccUrlConnection.execute("http://" + e.getServer().getIp() + "/ipcc/multichannel/remote/pickup_update.jsp",
                                    form.getPeer());
                        } else {
                            processService.execute(ShellCommand.PICKUP_UPDATE, form.getPeer(), " &");
                        }
                    });
        } else {
            processService.execute(ShellCommand.PICKUP_UPDATE, form.getPeer(), " &");
        }

          webSecureHistoryRepository.insert(form.getExtension(), WebSecureActionType.PHONE, WebSecureActionSubType.MOD, form.getExtension());
    }

    private void deleteWithInsertPhone(DSLContext dslContext, PhoneInfoRecord record) {
        dslContext.delete(PHONE_INFO)
                .where(compareCompanyId())
                .and(PHONE_INFO.PEER.eq(record.getPeer()))
                .execute();

        dslContext.insertInto(PHONE_INFO)
                .set(record)
                .execute();
    }

    private void deleteAndInsertBuddies(DSLContext dslContext, SipBuddiesRecord record) {
        dslContext.delete(SIP_BUDDIES)
                .where(SIP_BUDDIES.NAME.eq(record.getName()))
                .execute();

        InsertSetMoreStep<SipBuddiesRecord> query = dslContext.insertInto(SIP_BUDDIES)
                .set(SIP_BUDDIES.CANREINVITE, "no")
                .set(SIP_BUDDIES.CONTEXT, "outbound")
                .set(SIP_BUDDIES.HOST, "dynamic")
                .set(SIP_BUDDIES.NAT, "yes")
                .set(SIP_BUDDIES.QUALIFY, "yes")
                .set(SIP_BUDDIES.SECRET, isNotEmpty(record.getMd5secret()) ? getSHA512(record.getMd5secret()) : EMPTY)
                .set(SIP_BUDDIES.DISALLOW, "all")
                .set(SIP_BUDDIES.ALLOW, "alaw;ulaw")
                .set(SIP_BUDDIES.CANCALLFORWARD, "yes")
                .set(SIP_BUDDIES.PICKUPGROUP, EMPTY)
                .set(SIP_BUDDIES.CALLGROUP, EMPTY);

        if (isNotEmpty(record.getAccountcode()))
            query.set(SIP_BUDDIES.ACCOUNTCODE, record.getAccountcode());
        if (isNotEmpty(record.getCallerid()))
            query.set(SIP_BUDDIES.CALLERID, record.getCallerid());
        if (isNotEmpty(record.getMd5secret()))
            query.set(SIP_BUDDIES.MD5SECRET, DSL.field("md5({0})", String.class, record.getMd5secret()));
        if (record.getType() != null)
            query.set(SIP_BUDDIES.TYPE, record.getType());
        if (record.getName() != null)
            query.set(SIP_BUDDIES.NAME, record.getName());

        query.execute();
    }

    public int updateSipBuddies(PhoneInfoUpdateFormRequest form, SipBuddies sipBuddies, DSLContext pbxDsl) {
        return pbxDsl.update(SIP_BUDDIES)
                .set(SIP_BUDDIES.ACCOUNTCODE, form.getNumber())
                .set(SIP_BUDDIES.CALLERID, "\"" + form.getExtension() + "\" <" + form.getNumber() + ">")
                .set(SIP_BUDDIES.TYPE, sipBuddies.getType())
                .set(SIP_BUDDIES.MD5SECRET, StringUtils.isNotEmpty(form.getPasswd()) ? DSL.md5(sipBuddies.getMd5secret()) : DSL.field("{0}", String.class, sipBuddies.getMd5secret()))
                .set(SIP_BUDDIES.SECRET, StringUtils.isNotEmpty(form.getPasswd()) ? getSHA512(sipBuddies.getMd5secret()) : sipBuddies.getSecret())
                .set(SIP_BUDDIES.PICKUPGROUP, sipBuddies.getPickupgroup())
                .set(SIP_BUDDIES.CALLGROUP, sipBuddies.getCallgroup())
                .set(SIP_BUDDIES.NAMEDCALLGROUP, sipBuddies.getNamedcallgroup())
                .set(SIP_BUDDIES.NAMEDPICKUPGROUP, sipBuddies.getNamedpickupgroup())
                .where(SIP_BUDDIES.NAME.eq(sipBuddies.getName()))
                .execute();
    }

    /**
     * 1. number_070 사용안함으로 설정
     * 2. queue_table 헌트정보 삭제
     * 3. queue_member_table 헌트멤버정보 삭제
     * 4. person_list 전화기를 사용하는 상담원 정보 업데이트
     * 5. phone_info 전화기에 대한 정보를 삭제한다.
     */
    public void deleteWithForeign(String peer) {
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PhoneInfo phoneInfoEntity = this.findOneIfNullThrow(peer);
        final Number_070 numberEntity = numberRepository.findOne(phoneInfoEntity.getVoipTel());
        final List<CompanyServerEntity> pbxServerList = cacheService.pbxServerList(getCompanyId());

        numberRepository.updateStatusAllPbxServers(numberEntity.getNumber(), Number070Status.NON_USE.getCode());

        sipBuddiesRepository.deleteAndNameAllPbxServers(peer);

        queueTableRepository.deleteAllPbxServers(peer);
        queueTableRepository.deleteAllPbxServers("QUEUE" + peer);

        queueMemberTableRepository.deleteByKeyMemberNameAllPbxServers(peer);

        final Optional<PersonList> extensionUser = personListRepository.findOneByKeyExtension(dsl, phoneInfoEntity.getExtension());
        extensionUser.ifPresent(user -> {
            user.setExtension("");
            user.setPeer("");
            personListRepository.updateByKey(user, user.getId());
            pbxServerList.forEach(e -> {
                DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
                personListRepository.updateByKey(pbxDsl, user, user.getId());
            });
        });

        this.delete(peer);

        webSecureHistoryRepository.insert(peer, WebSecureActionType.PHONE, WebSecureActionSubType.DEL, peer);
    }

    public int delete(String key) {
        return this.delete(dsl, key);
    }

    public int delete(DSLContext dsl, String key) {
        final int r = super.delete(dsl, key);

        cacheService.pbxServerList(getCompanyId())
                .forEach(e -> {
                    DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
                    super.delete(pbxDsl, key);
                });

        return r;
    }

    private List<Condition> conditions(PhoneSearchRequest search) {
        final List<org.jooq.Condition> conditions = new ArrayList<>();

        if (isNotEmpty(search.getExtension()))
            conditions.add(PHONE_INFO.EXTENSION.like("%" + search.getExtension() + "%"));
        if (isNotEmpty(search.getVoipTel()))
            conditions.add(PHONE_INFO.VOIP_TEL.like("%" + search.getVoipTel() + "%"));
        if (isNotEmpty(search.getLocalPrefix()))
            conditions.add(PHONE_INFO.LOCAL_PREFIX.like("%" + search.getLocalPrefix() + "%"));
        if (isNotEmpty(search.getCid()))
            conditions.add(PHONE_INFO.CID.like("%" + search.getCid() + "%"));

        return conditions;
    }

    private String getSHA512(String str) {
        String rtnSHA = "";
        try{
            MessageDigest sh = MessageDigest.getInstance("SHA-512");
            sh.update(str.getBytes());
            byte[] byteData = sh.digest();
            StringBuffer sb = new StringBuffer();
            for (byte byteDatum : byteData) {
                sb.append(Integer.toString((byteDatum & 0xff) + 0x100, 16).substring(1));
            }
            rtnSHA = sb.toString();
        }catch(NoSuchAlgorithmException e){
            logger.error("Exception!", e);
            rtnSHA = null;
        }
        return rtnSHA;
    }
}
