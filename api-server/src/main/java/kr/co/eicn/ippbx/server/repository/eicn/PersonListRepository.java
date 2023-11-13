package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonChattBookmark;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.PersonList;
import kr.co.eicn.ippbx.model.dto.eicn.PersonOnHunt;
import kr.co.eicn.ippbx.model.enums.IdStatus;
import kr.co.eicn.ippbx.model.enums.LicenseListType;
import kr.co.eicn.ippbx.model.enums.PersonSort;
import kr.co.eicn.ippbx.model.search.ChattingMemberSearchRequest;
import kr.co.eicn.ippbx.model.search.StatUserSearchRequest;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Query;
import org.jooq.SortField;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.*;

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.QUEUE_MEMBER_TABLE;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.PersonList.PERSON_LIST;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.jooq.impl.DSL.noCondition;

@Getter
@Repository
public class PersonListRepository extends EicnBaseRepository<PersonList, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList, String> {
    protected final Logger logger = LoggerFactory.getLogger(PersonListRepository.class);

    private final PBXServerInterface pbxServerInterface;
    private final ServiceRepository serviceRepository;
    private final CacheService cacheService;

    public PersonListRepository(PBXServerInterface pbxServerInterface, ServiceRepository serviceRepository, CacheService cacheService) {
        super(PERSON_LIST, PERSON_LIST.ID, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList.class);
        this.pbxServerInterface = pbxServerInterface;
        this.serviceRepository = serviceRepository;
        this.cacheService = cacheService;
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList> findAll(PersonSort sort) {
        SortField<String> sortField;

        switch (sort) {
            case ID:
                sortField = PERSON_LIST.ID.asc();
                break;
            case NAME:
                sortField = PERSON_LIST.ID_NAME.asc();
                break;
            case EXTENSION:
                sortField = PERSON_LIST.EXTENSION.asc();
                break;
            case GROUP:
                sortField = PERSON_LIST.GROUP_CODE.asc();
                break;
            case LOGIN:
                sortField = PERSON_LIST.IS_LOGIN.desc();
                break;
            default:
                sortField = null;
        }

        return super.findAll(DSL.trueCondition(), sortField != null ? Collections.singletonList(sortField) : Collections.emptyList());
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList> findAllByGroup(List<String> group) {
        Condition groupCondition = null;

        if (group != null)
            for (int i = 0; i < group.size(); i++) {
                if (i == 0)
                    groupCondition = PERSON_LIST.GROUP_CODE.eq(group.get(i));
                else
                    groupCondition.or(PERSON_LIST.GROUP_CODE.eq(group.get(i)));
            }

        return groupCondition != null ? findAll(groupCondition) : findAll();
    }

    public void insertAllPbxServers(Object record) {
        this.insert(dsl, record);
    }

    public void insert(DSLContext dslContext, Object record) {
        super.insert(dslContext, record);

        cacheService.pbxServerList(getCompanyId())
                .forEach(e -> {
                    DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
                    super.insert(pbxDsl, record);
                });
    }

    public void updateProfilePhoto(String path) {
        dsl.update(PERSON_LIST)
                .set(PERSON_LIST.PROFILE_PHOTO, path)
                .where(compareCompanyId())
                .and(PERSON_LIST.ID.eq(g.getUser().getId()))
                .execute();
    }

    public int delete(String key) {
        final int r = super.delete(key);

        cacheService.pbxServerList(getCompanyId())
                .forEach(e -> {
                    DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
                    super.delete(pbxDsl, key);
                });

        return r;
    }

    public kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList findOneById(final String id) {
        return findOneByIdAndCompanyId(id, getCompanyId());
    }

    public kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList findOneByIdAndCompanyId(final String id, String companyId) {
        return dsl.selectFrom(PERSON_LIST)
                .where(PERSON_LIST.ID.eq(id))
                .and(PERSON_LIST.ID_STATUS.eq(""))
                .and(id.equals("master") || StringUtils.isEmpty(companyId) ? DSL.trueCondition() : PERSON_LIST.COMPANY_ID.eq(companyId))
                .fetchOneInto(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList.class);
    }

    public String findIdByPeer(final String peer) {
        return dsl.select(PERSON_LIST.ID)
                .from(PERSON_LIST)
                .where(compareCompanyId())
                .and(PERSON_LIST.PEER.eq(peer))
                .fetchOneInto(String.class);
    }

    public Optional<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList> findOneByKeyExtension(DSLContext dslContext, String extension) {
        return dslContext.selectFrom(PERSON_LIST)
                .where(compareCompanyId())
                .and(PERSON_LIST.EXTENSION.eq(extension))
                .fetchInto(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList.class)
                .stream()
                .findFirst();
    }

    public void partOrganizationCodeInitialize(List<String> groupTreeCodes) {
        partOrganizationCodeInitialize(dsl, groupTreeCodes);
    }

    public void partOrganizationCodeInitialize(DSLContext dslContext, List<String> groupTreeCodes) {
        final Collection<Query> queries = new ArrayList<>();

        for (String groupCode : groupTreeCodes) {
            queries.add(
                    DSL.update(PERSON_LIST)
                            .set(PERSON_LIST.GROUP_CODE, EMPTY)
                            .set(PERSON_LIST.GROUP_LEVEL, 0)
                            .set(PERSON_LIST.GROUP_TREE_NAME, EMPTY)
                            .where(compareCompanyId())
                            .and(PERSON_LIST.GROUP_CODE.eq(groupCode))
            );
        }

        dslContext.batch(queries).execute();
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList> findAllStatUser(StatUserSearchRequest search) {
        List<Condition> conditions = new ArrayList<>();

        if (g.getUser().getDataSearchAuthorityType() != null) {
            switch (g.getUser().getDataSearchAuthorityType()) {
                case NONE:
                    conditions.add(DSL.falseCondition());
                    break;
                case MINE:
                    conditions.add(PERSON_LIST.ID.eq(g.getUser().getId()));
                    break;
                case GROUP:
                    conditions.add(PERSON_LIST.GROUP_TREE_NAME.like(g.getUser().getGroupTreeName() + "%"));
                    break;
            }
        }

        conditions.add(PERSON_LIST.ID_TYPE.notEqual("J"));

        if (StringUtils.isNotEmpty(search.getGroupCode())) {
            conditions.add(PERSON_LIST.GROUP_TREE_NAME.like("%" + search.getGroupCode() + "%"));
        }

        Condition personCondition = noCondition();

        for (int i = 0; i < search.getPersonIds().size(); i++) {
            if (i == 0)
                personCondition = personCondition.and(PERSON_LIST.ID.eq(search.getPersonIds().get(i)));
            else
                personCondition = personCondition.or(PERSON_LIST.ID.eq(search.getPersonIds().get(i)));
        }

        conditions.add(personCondition);

        return findAll(conditions, Collections.singletonList(PERSON_LIST.GROUP_CODE.asc()));
    }

    public List<PersonOnHunt> findAllPersonOnHunt() {
        return dsl.select(PERSON_LIST.fields())
                .select(QUEUE_MEMBER_TABLE.QUEUE_NUMBER, QUEUE_MEMBER_TABLE.PAUSED)
                .from(PERSON_LIST)
                .leftJoin(QUEUE_MEMBER_TABLE)
                .on(QUEUE_MEMBER_TABLE.MEMBERNAME.eq(PERSON_LIST.PEER))
                .where(QUEUE_MEMBER_TABLE.QUEUE_NUMBER.isNotNull().and(PERSON_LIST.LICENSE_LIST.like("%" + LicenseListType.STAT.getCode() + "%")))
                .and(PERSON_LIST.ID_TYPE.notEqual("J"))
                .orderBy(PERSON_LIST.ID_NAME)
                .fetchInto(PersonOnHunt.class);
    }

    public Boolean checkDuplicate(String id) {
        return dsl.select(PERSON_LIST.fields())
                .from(PERSON_LIST)
                .where(PERSON_LIST.ID.eq(id))
                .and(PERSON_LIST.COMPANY_ID.eq(getCompanyId()))
                .fetch().size() > 0;
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList> findAllByChatting(ChattingMemberSearchRequest search) {
        return dsl.selectFrom(PERSON_LIST)
                .where(PERSON_LIST.ID_STATUS.notEqual(IdStatus.RETIRE.getCode()))
                .and(PERSON_LIST.COMPANY_ID.eq(getCompanyId()))
                .and(PERSON_LIST.LICENSE_LIST.like("%"+ LicenseListType.CHATT.getCode() +"%"))
                .and(StringUtils.isNotEmpty(search.getUserName()) ?
                        PERSON_LIST.ID_NAME.like("%" + search.getUserName() + "%").or(PERSON_LIST.ID.like("%" + search.getUserName() + "%")) : noCondition())
                .fetchInto(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList.class);
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList> findAllByBookmarkMemberId(ChattingMemberSearchRequest search, List<CommonChattBookmark> bookmarks) {
        Condition personCondition = noCondition();

        for (int i = 0; i < bookmarks.size(); i++) {
            if (i == 0)
                personCondition = personCondition.and(PERSON_LIST.ID.eq(bookmarks.get(i).getMemberid()));
            else
                personCondition = personCondition.or(PERSON_LIST.ID.eq(bookmarks.get(i).getMemberid()));
        }

        if (StringUtils.isNotEmpty(search.getUserName()))
            personCondition = personCondition.and(PERSON_LIST.ID_NAME.like("%" + search.getUserName() + "%"));

        return findAll(personCondition);
    }

    public void countLoginFail(String userId, Integer count) {
        dsl.update(PERSON_LIST)
                .set(PERSON_LIST.TRY_LOGIN_COUNT, count)
                .set(PERSON_LIST.TRY_LOGIN_DATE, DSL.now())
                .where(compareCompanyId())
                .and(PERSON_LIST.ID.eq(userId))
                .execute();
    }

    public List<String> personAllId(){
        return dsl.select(PERSON_LIST.ID)
                .from(PERSON_LIST)
                .where(compareCompanyId())
                .and(PERSON_LIST.ID_TYPE.notEqual("B"))
                .and(PERSON_LIST.ID_STATUS.isNull().or(PERSON_LIST.ID_STATUS.eq("")))
                .fetchInto(String.class);
    }

    public Map<String, String> getPeerToUserIdMap() {
        return dsl.select(PERSON_LIST.ID, PERSON_LIST.PEER)
                .from(PERSON_LIST)
                .where(compareCompanyId())
                .and(PERSON_LIST.PEER.isNotNull().and(PERSON_LIST.PEER.ne("")))
                .fetchMap(PERSON_LIST.PEER, PERSON_LIST.ID);
    }
}
