package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.PickupGroup;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTree;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SipBuddies;
import kr.co.eicn.ippbx.model.entity.eicn.CompanyServerEntity;
import kr.co.eicn.ippbx.model.enums.ShellCommand;
import kr.co.eicn.ippbx.model.enums.WebSecureActionSubType;
import kr.co.eicn.ippbx.model.enums.WebSecureActionType;
import kr.co.eicn.ippbx.model.form.PickUpGroupFormRequest;
import kr.co.eicn.ippbx.model.form.PickUpGroupFormUpdateRequest;
import kr.co.eicn.ippbx.model.search.PickUpGroupSearchRequest;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.IpccUrlConnection;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import kr.co.eicn.ippbx.server.service.ProcessService;
import kr.co.eicn.ippbx.util.StringUtils;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.PhoneInfo.PHONE_INFO;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.PickupGroup.PICKUP_GROUP;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.SipBuddies.SIP_BUDDIES;
import static org.apache.commons.lang3.StringUtils.*;

@Getter
@Repository
public class PickUpGroupRepository extends EicnBaseRepository<PickupGroup, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PickupGroup, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(PickUpGroupRepository.class);
    private final CacheService cacheService;
    private final PBXServerInterface pbxServerInterface;
    private final PhoneInfoRepository phoneInfoRepository;
    private final SipBuddiesRepository sipBuddiesRepository;
    private final WebSecureHistoryRepository webSecureHistoryRepository;
    private final CompanyTreeRepository companyTreeRepository;
    private final ProcessService processService;

    public PickUpGroupRepository(CacheService cacheService, PBXServerInterface pbxServerInterface, PhoneInfoRepository phoneInfoRepository, SipBuddiesRepository sipBuddiesRepository, WebSecureHistoryRepository webSecureHistoryRepository, CompanyTreeRepository companyTreeRepository, ProcessService processService) {
        super(PICKUP_GROUP, PICKUP_GROUP.SEQ, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PickupGroup.class);
        this.cacheService = cacheService;
        this.pbxServerInterface = pbxServerInterface;
        this.phoneInfoRepository = phoneInfoRepository;
        this.sipBuddiesRepository = sipBuddiesRepository;
        this.webSecureHistoryRepository = webSecureHistoryRepository;
        this.companyTreeRepository = companyTreeRepository;
        this.processService = processService;
    }

    public Pagination<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PickupGroup> pagination(PickUpGroupSearchRequest search) {
        return super.pagination(search, conditions(search));
    }

    private List<Condition> conditions(PickUpGroupSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        if (isNotEmpty(search.getGroupCode()))
            conditions.add(PICKUP_GROUP.GROUP_TREE_NAME.like("%" + search.getGroupCode() + "%"));
        if (isNotEmpty(search.getGroupname()))
            conditions.add(PICKUP_GROUP.GROUPNAME.like("%" + search.getGroupname() + "%"));

        return conditions;
    }

    public Record insertOnGeneratedKey(PickUpGroupFormRequest form) {
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PickupGroup one = findOne(PICKUP_GROUP.GROUPNAME.eq(form.getGroupname()));
        final CompanyTree companyTree = companyTreeRepository.findOneGroupCodeIfNullThrow(form.getGroupCode());
        if (one != null)
            throw new IllegalArgumentException(form.getGroupname() + "은(는) 이미 등록된 그룹명입니다.");

        final List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PickupGroup> pickupGroups = findAll();

        final int limitMax = 100;
        final OptionalInt optionalToLimitMaxNextGroupCode = IntStream.rangeClosed(1, 100)
                .filter(i -> pickupGroups.stream().noneMatch(pickup -> pickup.getGroupcode() == i))
                .findFirst();

        if (!optionalToLimitMaxNextGroupCode.isPresent())
            throw new IllegalArgumentException("당겨받기 그룹수 초과(" + limitMax + ")");

        final int nextGroupCodeAsInt = optionalToLimitMaxNextGroupCode.getAsInt();
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PickupGroup record = new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PickupGroup();
        record.setGroupcode(nextGroupCodeAsInt);
        record.setGroupname(form.getGroupname());
        record.setGroupnamecode(getCompanyId() + "_" + nextGroupCodeAsInt);
        record.setHost(isEmpty(form.getHost()) ? "localhost" : form.getHost());
        record.setCompanyId(getCompanyId());

        if (companyTree != null) {
            record.setGroupCode(companyTree.getGroupCode());
            record.setGroupTreeName(companyTree.getGroupTreeName());
            record.setGroupLevel(companyTree.getGroupLevel());
        }

        final Record result = super.insertOnGeneratedKey(record);
        cacheService.pbxServerList(getCompanyId())
                .forEach(e -> {
                    try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
                        super.insert(pbxDsl, record);
                    }
                });

        webSecureHistoryRepository.insert(WebSecureActionType.PICKUP, WebSecureActionSubType.ADD, form.getGroupname());

        return result;
    }

    public void updateByKey(PickUpGroupFormUpdateRequest form, Integer groupcode) {
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PickupGroup pickupGroup = findOneIfNullThrow(PICKUP_GROUP.GROUPCODE.eq(groupcode));

        // 당겨받기그룹 업데이트
        dsl.update(PICKUP_GROUP)
                .set(PICKUP_GROUP.GROUPNAME, form.getGroupname())
                .where(compareCompanyId())
                .and(PICKUP_GROUP.GROUPCODE.eq(groupcode))
                .execute();

        cacheService.pbxServerList(getCompanyId())
                .forEach(e -> {
                    try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
                        pbxDsl.update(PICKUP_GROUP)
                                .set(PICKUP_GROUP.GROUPNAME, form.getGroupname())
                                .where(compareCompanyId())
                                .and(PICKUP_GROUP.GROUPCODE.eq(groupcode))
                                .execute();
                    }
                });

        // 전화기 당겨받기그룹 전체 초기화
        dsl.update(PHONE_INFO)
                .set(PHONE_INFO.PICKUP, EMPTY)
                .where(PHONE_INFO.COMPANY_ID.eq(getCompanyId()))
                .and(PHONE_INFO.PICKUP.eq(String.valueOf(pickupGroup.getGroupcode())))
                .execute();

        cacheService.pbxServerList(getCompanyId())
                .forEach(e -> {
                    try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
                        pbxDsl.update(PHONE_INFO)
                                .set(PHONE_INFO.PICKUP, EMPTY)
                                .where(PHONE_INFO.COMPANY_ID.eq(getCompanyId()))
                                .and(PHONE_INFO.PICKUP.eq(String.valueOf(pickupGroup.getGroupcode())))
                                .execute();
                    }
                });

        final String groupname = pickupGroup.getGroupname();

        // 고객사 아이디 + 당겨받기 그룹코드 데이터가 없다면 새로 만들어줌?..
        List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SipBuddies> resetPickUpSipBuddies;
        if (isNotEmpty(groupname)) {
            resetPickUpSipBuddies = sipBuddiesRepository.findAll(SIP_BUDDIES.NAMEDPICKUPGROUP.eq(groupname));
        } else {
            resetPickUpSipBuddies = sipBuddiesRepository.findAll(SIP_BUDDIES.PICKUPGROUP.eq(String.valueOf(pickupGroup.getGroupcode())));
            pickupGroup.setGroupnamecode(getCompanyId() + "_" + pickupGroup.getGroupcode());

            super.updateByKey(pickupGroup, pickupGroup.getSeq());

            cacheService.pbxServerList(getCompanyId())
                    .forEach(e -> {
                        try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
                            super.updateByKey(pbxDsl, pickupGroup, pickupGroup.getSeq());
                        }
                    });
        }
        // 전화기 정보(asterisk 사용테이블) 당겨받기그룹 초기화
        if (resetPickUpSipBuddies != null && resetPickUpSipBuddies.size() > 0) {
            for (kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SipBuddies resetPickUpSipBuddy : resetPickUpSipBuddies) {
                resetPickUpSipBuddy.setPickupgroup(EMPTY);
                resetPickUpSipBuddy.setCallgroup(EMPTY);
                resetPickUpSipBuddy.setNamedpickupgroup(EMPTY);
                resetPickUpSipBuddy.setNamedcallgroup(EMPTY);

                sipBuddiesRepository.updateByKey(resetPickUpSipBuddy, resetPickUpSipBuddy.getId());

                cacheService.pbxServerList(getCompanyId())
                        .forEach(e -> {
                            try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
                                sipBuddiesRepository.updateByKey(pbxDsl, resetPickUpSipBuddy, resetPickUpSipBuddy.getId());
                            }
                        });
            }
        }

        if (Objects.isNull(resetPickUpSipBuddies))
            resetPickUpSipBuddies = new ArrayList<>();

        final List<String> resetSipNames = resetPickUpSipBuddies.stream()
                .map(SipBuddies::getName)
                .collect(Collectors.toList());

        final Set<String> peers = form.getPeers();
        if (peers != null) {
            for (String peer : peers) {
                // 전화기 당겨받기그룹 업데이트
                dsl.update(PHONE_INFO)
                        .set(PHONE_INFO.PICKUP, String.valueOf(groupcode))
                        .where(PHONE_INFO.COMPANY_ID.eq(getCompanyId()))
                        .and(PHONE_INFO.PEER.eq(peer))
                        .execute();

                cacheService.pbxServerList(getCompanyId())
                        .forEach(e -> {
                            try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
                                pbxDsl.update(PHONE_INFO)
                                        .set(PHONE_INFO.PICKUP, String.valueOf(groupcode))
                                        .where(PHONE_INFO.COMPANY_ID.eq(getCompanyId()))
                                        .and(PHONE_INFO.PEER.eq(peer))
                                        .execute();
                            }
                        });
                // 전화기 정보(asterisk 사용테이블) 당겨받기그룹 업데이트
                dsl.update(SIP_BUDDIES)
                        .set(SIP_BUDDIES.PICKUPGROUP, EMPTY)
                        .set(SIP_BUDDIES.CALLGROUP, EMPTY)
                        .set(SIP_BUDDIES.NAMEDPICKUPGROUP, getCompanyId() + "_" + pickupGroup.getGroupcode())
                        .set(SIP_BUDDIES.NAMEDCALLGROUP, getCompanyId() + "_" + pickupGroup.getGroupcode())
                        .where(SIP_BUDDIES.NAME.eq(peer))
                        .execute();

                cacheService.pbxServerList(getCompanyId())
                        .forEach(e -> {
                            try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
                                pbxDsl.update(SIP_BUDDIES)
                                        .set(SIP_BUDDIES.PICKUPGROUP, EMPTY)
                                        .set(SIP_BUDDIES.CALLGROUP, EMPTY)
                                        .set(SIP_BUDDIES.NAMEDPICKUPGROUP, getCompanyId() + "_" + pickupGroup.getGroupcode())
                                        .set(SIP_BUDDIES.NAMEDCALLGROUP, getCompanyId() + "_" + pickupGroup.getGroupcode())
                                        .where(SIP_BUDDIES.NAME.eq(peer))
                                        .execute();
                            }
                        });

                if (resetSipNames.stream().anyMatch(e -> e.equals(peer)))
                    resetSipNames.remove(peer);
                else
                    resetSipNames.add(peer);
            }
        }


        if (resetSipNames.size() > 0) {
            cacheService.pbxServerList(getCompanyId())
                    .forEach(e -> {
                        if (!"localhost".equals(e.getHost())) {
                            IpccUrlConnection.execute("http://" + e.getServer().getIp() + "/ipcc/multichannel/remote/pickup_update.jsp",
                                    String.join("|", resetSipNames));
                        } else {
                            for (String peer : resetSipNames) {
                                processService.execute(ShellCommand.PICKUP_UPDATE, peer, " &");
                            }
                        }
                    });
        }

        webSecureHistoryRepository.insert(WebSecureActionType.PICKUP, WebSecureActionSubType.MOD, StringUtils.subStringBytes(form.getGroupname(), 400));
    }

    public int delete(Integer groupcode) {
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PickupGroup entity = findOneIfNullThrow(PICKUP_GROUP.GROUPCODE.eq(groupcode));
        final int r = super.delete(entity.getSeq());

        final Optional<CompanyServerEntity> optionalPbxServer = cacheService.pbxServerList(getCompanyId())
                .stream()
                .filter(e -> e.getHost().equals(entity.getHost()))
                .findFirst();

        optionalPbxServer.ifPresent(e -> {
            try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
                super.delete(pbxDsl, entity.getSeq());
            }
        });

        final List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PhoneInfo> pickUpPhones = phoneInfoRepository.findAll(PHONE_INFO.PICKUP.eq(String.valueOf(entity.getGroupcode())));
        for (kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PhoneInfo pickUpPhone : pickUpPhones) {
            pickUpPhone.setPickup(EMPTY);
            phoneInfoRepository.updateByKey(pickUpPhone, pickUpPhone.getPeer());

            optionalPbxServer.ifPresent(e -> {
                try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
                    phoneInfoRepository.updateByKey(pbxDsl, pickUpPhone, pickUpPhone.getPeer());
                }
            });
        }

        final List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SipBuddies> pickUpSipBuddies = sipBuddiesRepository.findAll(SIP_BUDDIES.PICKUPGROUP.eq(String.valueOf(entity.getGroupcode())));
        for (kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SipBuddies pickUpSipBuddy : pickUpSipBuddies) {
            pickUpSipBuddy.setPickupgroup(EMPTY);
            pickUpSipBuddy.setCallgroup(EMPTY);
            sipBuddiesRepository.updateByKey(pickUpSipBuddy, pickUpSipBuddy.getId());

            optionalPbxServer.ifPresent(e -> {
                try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
                    sipBuddiesRepository.updateByKey(pbxDsl, pickUpSipBuddy, pickUpSipBuddy.getId());
                }
            });
        }

        // 당겨받기그룹 업데이트
        if (pickUpSipBuddies.size() > 0) {
            optionalPbxServer.ifPresent(e -> IpccUrlConnection.execute("http://" + e.getServer().getIp() + "/ipcc/cc/remote/pickup_update.jsp",
                    pickUpSipBuddies.stream()
                            .filter(sip -> isNotEmpty(sip.getName()))
                            .map(SipBuddies::getName)
                            .collect(Collectors.joining("|"))
            ));
        }

        webSecureHistoryRepository.insert(WebSecureActionType.PICKUP, WebSecureActionSubType.DEL, entity.getGroupname());

        return r;
    }
}
