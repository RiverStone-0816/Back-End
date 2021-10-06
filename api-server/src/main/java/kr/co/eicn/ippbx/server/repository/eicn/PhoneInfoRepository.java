package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.PhoneInfo;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchPhoneInfoResponse;
import kr.co.eicn.ippbx.model.entity.eicn.CompanyServerEntity;
import kr.co.eicn.ippbx.model.form.CidInfoChangeRequest;
import kr.co.eicn.ippbx.model.form.CidInfoFormRequest;
import kr.co.eicn.ippbx.model.form.CidInfoUpdateFormRequest;
import kr.co.eicn.ippbx.model.search.CidInfoSearchRequest;
import kr.co.eicn.ippbx.model.search.search.SearchPhoneInfoRequest;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.QUEUE_MEMBER_TABLE;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.PhoneInfo.PHONE_INFO;

@Getter
@Repository
public class PhoneInfoRepository extends EicnBaseRepository<PhoneInfo, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PhoneInfo, String> {
    protected final Logger logger = LoggerFactory.getLogger(PhoneInfoRepository.class);
    private final PBXServerInterface pbxServerInterface;
    private final CacheService cacheService;

    public PhoneInfoRepository(PBXServerInterface pbxServerInterface, CacheService cacheService) {
        super(PHONE_INFO, PHONE_INFO.PEER, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PhoneInfo.class);
        this.pbxServerInterface = pbxServerInterface;
        this.cacheService = cacheService;
    }

    public List<SearchPhoneInfoResponse> search(SearchPhoneInfoRequest search) {
        return findAll(searchConditions(search)).stream()
                .map(e -> modelMapper.map(e, SearchPhoneInfoResponse.class))
                .collect(Collectors.toList());
    }

    private List<Condition> searchConditions(SearchPhoneInfoRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        return conditions;
    }

    public Pagination<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PhoneInfo> pagination(CidInfoSearchRequest search) {
        return super.pagination(search, conditions(search));
    }

    public List<Condition> conditions(CidInfoSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        if (StringUtils.isNotEmpty(search.getExtension()))
            conditions.add(PHONE_INFO.EXTENSION.eq(search.getExtension()));
        if (StringUtils.isNotEmpty(search.getLocalPrefix()))
            conditions.add(PHONE_INFO.LOCAL_PREFIX.eq(search.getLocalPrefix()));
        if (StringUtils.isNotEmpty(search.getCid()))
            conditions.add(PHONE_INFO.CID.eq(search.getCid()));
        if (StringUtils.isNotEmpty(search.getBillingNumber()))
            conditions.add(PHONE_INFO.BILLING_NUMBER.eq(search.getBillingNumber()));

        return conditions;
    }

    public void updatePage(CidInfoUpdateFormRequest form) {
        final List<CompanyServerEntity> pbxServerList = cacheService.pbxServerList(getCompanyId());

        updatePage(dsl, form);
        pbxServerList.forEach(e -> {
            DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
            updatePage(pbxDsl, form);
        });
    }

    public void updatePage(DSLContext dslContext, CidInfoUpdateFormRequest form) {
        for (CidInfoFormRequest info : form.getCidInfos()) {
            dslContext.update(PHONE_INFO)
                    .set(PHONE_INFO.LOCAL_PREFIX, info.getLocalPrefix())
                    .set(PHONE_INFO.CID, info.getCid())
                    // .set(PHONE_INFO.BILLING_NUMBER, info.getBillingNumber())
                    .set(PHONE_INFO.FIRST_STATUS, info.getFirstStatus())
                    .set(PHONE_INFO.DIAL_STATUS, info.getDialStatus())
                    .set(PHONE_INFO.LOGOUT_STATUS, info.getLogoutStatus())
                    .where(compareCompanyId())
                    .and(PHONE_INFO.PEER.eq(info.getPeer()))
                    .execute();

            dsl.update(QUEUE_MEMBER_TABLE)
                    .set(QUEUE_MEMBER_TABLE.PAUSED, (int) info.getLogoutStatus())
                    .where(QUEUE_MEMBER_TABLE.COMPANY_ID.eq(getCompanyId()))
                    .and(QUEUE_MEMBER_TABLE.MEMBERNAME.eq(info.getPeer()))
                    .and(QUEUE_MEMBER_TABLE.QUEUE_NAME.ne(QUEUE_MEMBER_TABLE.MEMBERNAME))
                    .and(QUEUE_MEMBER_TABLE.IS_LOGIN.eq("N"))
                    .execute();
        }
    }

    public void updateCid(CidInfoChangeRequest form) {
        List<CompanyServerEntity> pbxServerList = cacheService.pbxServerList(getCompanyId());
        updateCid(dsl, form);
        pbxServerList.forEach(e -> {
            DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
            updateCid(pbxDsl, form);
        });
    }

    public void updateCid(DSLContext dslContext, CidInfoChangeRequest form) {
        for (String peer : form.getPeers()) {
            dslContext.update(PHONE_INFO)
                    .set(PHONE_INFO.CID, form.getCid())
                    .where(compareCompanyId())
                    .and(PHONE_INFO.PEER.eq(peer))
                    .execute();
        }
    }

    public Map<String, String> findAllPhoneStatus() {
        List<CompanyServerEntity> pbxServerList = cacheService.pbxServerList(getCompanyId());
        Map<String, String> phoneInfoMap = new HashMap<>();
        pbxServerList.forEach(e -> {
            DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
            phoneInfoMap.putAll(getPhoneInfoMap(pbxDsl));
        });
        return phoneInfoMap;
    }

    public Map<String, String> getPhoneInfoMap(DSLContext dsl) {
        return dsl.select(PHONE_INFO.PEER, PHONE_INFO.PHONE_STATUS)
                        .from(PHONE_INFO)
                        .where(compareCompanyId())
                        .fetchMap(PHONE_INFO.PEER, PHONE_INFO.PHONE_STATUS);
    }

    //상담화면 수신경로
    public String getExtensionByNumber(String number) {
        return dsl.select(PHONE_INFO.EXTENSION)
                .from(PHONE_INFO)
                .where(compareCompanyId())
                .and(PHONE_INFO.VOIP_TEL.eq(number))
                .fetchOneInto(String.class);
    }
}
