package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.exception.DuplicateKeyException;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.PersonList;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CompanyTree;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PhoneInfo;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.QueueMemberTable;
import kr.co.eicn.ippbx.server.model.entity.eicn.CompanyEntity;
import kr.co.eicn.ippbx.server.model.entity.eicn.UserEntity;
import kr.co.eicn.ippbx.server.model.enums.*;
import kr.co.eicn.ippbx.server.model.form.PersonFormRequest;
import kr.co.eicn.ippbx.server.model.form.PersonFormUpdateRequest;
import kr.co.eicn.ippbx.server.model.search.PersonSearchRequest;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import kr.co.eicn.ippbx.server.util.ReflectionUtils;
import kr.co.eicn.ippbx.server.util.page.Pagination;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static kr.co.eicn.ippbx.server.jooq.eicn.tables.CompanyInfo.COMPANY_INFO;
import static kr.co.eicn.ippbx.server.jooq.eicn.tables.CompanyTree.COMPANY_TREE;
import static kr.co.eicn.ippbx.server.jooq.eicn.tables.PersonList.PERSON_LIST;
import static kr.co.eicn.ippbx.server.jooq.eicn.tables.PhoneInfo.PHONE_INFO;
import static kr.co.eicn.ippbx.server.jooq.eicn.tables.QueueMemberTable.QUEUE_MEMBER_TABLE;
import static org.apache.commons.lang3.StringUtils.*;

@Getter
@Repository
public class UserRepository extends EicnBaseRepository<PersonList, UserEntity, String> {
    protected final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    private final PersonListRepository personListRepository;
    private final PhoneInfoRepository phoneInfoRepository;
    private final QueueMemberTableRepository queueMemberTableRepository;
    private final WebSecureHistoryRepository webSecureHistoryRepository;
    private final CacheService cacheService;
    private final PBXServerInterface pbxServerInterface;
    private final PasswordEncoder passwordEncoder;
    private final CompanyTreeRepository companyTreeRepository;

    public UserRepository(PersonListRepository personListRepository, PhoneInfoRepository phoneInfoRepository, QueueMemberTableRepository queueMemberTableRepository, WebSecureHistoryRepository webSecureHistoryRepository, CacheService cacheService, PBXServerInterface pbxServerInterface, PasswordEncoder passwordEncoder, CompanyTreeRepository companyTreeRepository) {
        super(PERSON_LIST, PERSON_LIST.ID, UserEntity.class);

        this.personListRepository = personListRepository;
        this.phoneInfoRepository = phoneInfoRepository;
        this.queueMemberTableRepository = queueMemberTableRepository;
        this.webSecureHistoryRepository = webSecureHistoryRepository;
        this.cacheService = cacheService;
        this.pbxServerInterface = pbxServerInterface;
        this.passwordEncoder = passwordEncoder;
        this.companyTreeRepository = companyTreeRepository;

        addField(PERSON_LIST);
        addField(COMPANY_INFO);
        addField(COMPANY_TREE);

        // 아이디유형, 그룹명, 아이디
        orderByFields.add(PERSON_LIST.ID_TYPE.asc());
        orderByFields.add(COMPANY_TREE.GROUP_NAME.asc());
        orderByFields.add(PERSON_LIST.ID.asc());
    }

    @Override
    protected SelectConditionStep<Record> query(SelectJoinStep<Record> query) {
        return query
                .leftJoin(COMPANY_INFO).on(PERSON_LIST.COMPANY_ID.eq(COMPANY_INFO.COMPANY_ID))
                .leftJoin(COMPANY_TREE).on(PERSON_LIST.GROUP_CODE.eq(COMPANY_TREE.GROUP_CODE)
                        .and(COMPANY_TREE.COMPANY_ID.eq(getCompanyId())))
                .where();
    }

    @Override
    protected RecordMapper<Record, UserEntity> getMapper() {
        return record -> {
            final UserEntity entity = record.into(PERSON_LIST).into(UserEntity.class);
            entity.setCompany(record.into(COMPANY_INFO).into(CompanyEntity.class));
            entity.setCompanyTree(record.into(COMPANY_TREE).into(CompanyTree.class));

            return entity;
        };
    }

    public Pagination<UserEntity> pagination(PersonSearchRequest search) {
        return super.pagination(search, conditions(search));
    }

    public List<UserEntity> findAll(PersonSearchRequest search) {
        return super.findAll(conditions(search));
    }

    public UserEntity findOneId(final String id) {
        return dsl.select()
                .from(PERSON_LIST)
                .leftJoin(COMPANY_INFO).on(PERSON_LIST.COMPANY_ID.eq(COMPANY_INFO.COMPANY_ID))
                .leftJoin(COMPANY_TREE).on(PERSON_LIST.GROUP_CODE.eq(COMPANY_TREE.GROUP_CODE)
                        .and(COMPANY_TREE.COMPANY_ID.eq(getCompanyId())))
                .where(PERSON_LIST.ID.eq(id))
                .fetchOne(record -> {
                    final UserEntity entity = record.into(PERSON_LIST).into(UserEntity.class);
                    entity.setCompany(record.into(COMPANY_INFO).into(CompanyEntity.class));
                    entity.setCompanyTree(record.into(COMPANY_TREE).into(CompanyTree.class));

                    return entity;
                });
    }

    public void insert(PersonFormRequest form) {
        personListRepository.existsKeyIfNonNullThrow(form.getId(), "아이디");

        final PhoneInfo phone = phoneInfoRepository.findOne(PHONE_INFO.EXTENSION.eq(form.getExtension()));
        final CompanyTree companyTree = companyTreeRepository.findOneGroupCodeIfNullThrow(form.getGroupCode());

        if (isNotEmpty(form.getExtension())) {
            final Optional<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PersonList> usedExtensionPersonList = personListRepository.findOneByKeyExtension(dsl, form.getExtension());
            usedExtensionPersonList.ifPresent(e -> {
                if (!IdStatus.RETIRE.getCode().equals(e.getIdStatus())) {
                    throw new DuplicateKeyException("사용중인 내선번호입니다.");
                }
            });
        }

        final kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PersonList record = new kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PersonList();

        record.setId(form.getId());
        record.setIdType(form.getIdType());
        record.setPasswd(getSHA512(form.getPassword()));
        record.setIdName(form.getIdName());

        if (companyTree != null) {
            record.setGroupCode(companyTree.getGroupCode());
            record.setGroupLevel(companyTree.getGroupLevel());
            record.setGroupTreeName(companyTree.getGroupTreeName());
        }
        record.setExtension(form.getExtension());
        record.setIsLogin(EMPTY);
        record.setPhoneStatus(EMPTY);
        record.setEtc("L:".concat(defaultIfEmpty(form.getListeningRecordingAuthority(), RecordingAuthorityType.NONE.getCode()))
                .concat("|D:").concat(defaultIfEmpty(form.getDownloadRecordingAuthority(), RecordingAuthorityType.NONE.getCode()))
                .concat("|R:").concat(defaultIfEmpty(form.getRemoveRecordingAuthority(), RecordingAuthorityType.NONE.getCode()))
                .concat("|S:").concat(defaultIfEmpty(form.getDataSearchAuthority(), DataSearchAuthorityType.NONE.getCode())));
        record.setLogoutStatus((byte) 0);
        record.setDialStatus((byte) 2);
        record.setCompanyId(getCompanyId());
        record.setIsLoginMsg(EMPTY);
        record.setIsLoginChatt("N");
        record.setIdStatus(defaultString(form.getIdStatus()));
        record.setHpNumber(form.getHpNumber());
        record.setEmailInfo(form.getEmailInfo());
        record.setIsPds(form.getIsPds());
        record.setIsTalk(form.getIsTalk());
        record.setIsEmail(form.getIsEmail());
        record.setIsStat(form.getIsStat());
        record.setIsChatt(form.getIsChatt());
        record.setPeer(EMPTY);
        record.setTryLoginCount(0);

        if (phone != null)
            record.setPeer(phone.getPeer());

        personListRepository.insertAllPbxServers(record);

        final QueueMemberTable queueMemberTableRecord = new QueueMemberTable();
        queueMemberTableRecord.setQueueName("PDS_" + getCompanyId());
        queueMemberTableRecord.setMembername(record.getId());
        queueMemberTableRecord.setInterface(StringUtils.isNotEmpty(record.getPeer()) ? "SIP/".concat(record.getPeer()) : "SIP/");
        queueMemberTableRecord.setPenalty(0);
        queueMemberTableRecord.setPaused(3);
        queueMemberTableRecord.setIsLogin("N");
        queueMemberTableRecord.setUserid(record.getId());
        queueMemberTableRecord.setBlendingMode("M");
        queueMemberTableRecord.setCompanyId(getCompanyId());

        queueMemberTableRepository.insertAllPbxServers(queueMemberTableRecord);

        webSecureHistoryRepository.insert(WebSecureActionType.PERSON, WebSecureActionSubType.ADD, record.getId());
    }

    public void updateByKey(PersonFormUpdateRequest form, String id) {
        final kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PersonList record = findOneIfNullThrow(id);
        final PhoneInfo phone = phoneInfoRepository.findOne(PHONE_INFO.EXTENSION.eq(form.getExtension()));
        final CompanyTree companyTree = companyTreeRepository.findOneGroupCodeIfNullThrow(form.getGroupCode());

        if (phone != null)
            form.setExtension(phone.getExtension());
        if (isNotEmpty(form.getPassword()))
            form.setPassword(getSHA512(form.getPassword()));

        ReflectionUtils.copy(record, form);

        record.setIdStatus(defaultString(form.getIdStatus()));
        record.setEtc("L:".concat(defaultIfEmpty(form.getListeningRecordingAuthority(), RecordingAuthorityType.NONE.getCode()))
                .concat("|D:").concat(defaultIfEmpty(form.getDownloadRecordingAuthority(), RecordingAuthorityType.NONE.getCode()))
                .concat("|R:").concat(defaultIfEmpty(form.getRemoveRecordingAuthority(), RecordingAuthorityType.NONE.getCode()))
                .concat("|S:").concat(defaultIfEmpty(form.getDataSearchAuthority(), DataSearchAuthorityType.NONE.getCode())));
        record.setPeer(phone != null ? phone.getPeer() : "");

        if (companyTree != null) {
            record.setGroupCode(companyTree.getGroupCode());
            record.setGroupLevel(companyTree.getGroupLevel());
            record.setGroupTreeName(companyTree.getGroupTreeName());
        }

        personListRepository.updateByKey(record, id);

        cacheService.pbxServerList(getCompanyId())
                .forEach(e -> {
                    try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
                        personListRepository.updateByKey(pbxDsl, record, id);
                    }
                });

        final QueueMemberTable queueMemberTableRecord = new QueueMemberTable();
        queueMemberTableRecord.setQueueName("PDS_" + getCompanyId());
        queueMemberTableRecord.setInterface(StringUtils.isNotEmpty(record.getPeer()) ? "SIP/".concat(record.getPeer()) : "SIP/");
        queueMemberTableRecord.setMembername(id);
        queueMemberTableRecord.setInterface(EMPTY);
        queueMemberTableRecord.setPenalty(0);
        queueMemberTableRecord.setPaused(3);
        queueMemberTableRecord.setIsLogin("N");
        queueMemberTableRecord.setUserid(id);
        queueMemberTableRecord.setBlendingMode("M");
        queueMemberTableRecord.setCompanyId(getCompanyId());

        queueMemberTableRepository.insertOnConflictDoNothingAllPbxServers(queueMemberTableRecord);
        queueMemberTableRepository.updatePeerByUserId(record);

        webSecureHistoryRepository.insert(WebSecureActionType.PERSON, WebSecureActionSubType.MOD, id);
    }

    public int deleteOnIfNullThrow(String id) {
        final int r = super.deleteOnIfNullThrow(id);

        cacheService.pbxServerList(getCompanyId())
                .forEach(e -> {
                    try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
                        super.delete(pbxDsl, id);
                    }
                });

        // queue_member_table 삭제
        queueMemberTableRepository.deleteAllPbxServers(QUEUE_MEMBER_TABLE.QUEUE_NAME.like("PDS_" + getCompanyId() + "%").and(QUEUE_MEMBER_TABLE.MEMBERNAME.eq(id)));

        webSecureHistoryRepository.insert(WebSecureActionType.PERSON, WebSecureActionSubType.DEL, id);

        return r;
    }

    public void updatePassword(String id, String password) {
        findOneIfNullThrow(id);

        dsl.update(PERSON_LIST)
                .set(PERSON_LIST.PASSWD, getSHA512(password))
                .set(PERSON_LIST.PASS_CHANGE_DATE, DSL.now())
                .where(getCondition(id))
                .execute();

        cacheService.pbxServerList(getCompanyId())
                .forEach(e -> {
                    try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
                        pbxDsl.update(PERSON_LIST)
                                .set(PERSON_LIST.PASSWD, getSHA512(password))
                                .set(PERSON_LIST.PASS_CHANGE_DATE, DSL.now())
                                .where(getCondition(id))
                                .execute();
                    }
                });
    }

    private List<Condition> conditions(PersonSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        if (isNotEmpty(search.getId()))
            conditions.add(PERSON_LIST.ID.like("%" + search.getId() + "%"));
        if (isNotEmpty(search.getIdName()))
            conditions.add(PERSON_LIST.ID_NAME.like("%" + search.getIdName() + "%"));
        if (isNotEmpty(search.getExtension()))
            conditions.add(PERSON_LIST.EXTENSION.like("%" + search.getExtension() + "%"));
        if (isNotEmpty(search.getGroupCode()))
            conditions.add(COMPANY_TREE.COMPANY_ID.eq(getCompanyId()).and(COMPANY_TREE.GROUP_TREE_NAME.like("%" + search.getGroupCode() + "%")));
        if (Objects.nonNull(search.getIdStatus()))
            conditions.add(PERSON_LIST.ID_STATUS.eq(search.getIdStatus().getCode()));

        conditions.add(PERSON_LIST.ID_TYPE.ne(IdType.MASTER.getCode()));

        return conditions;
    }

    public int doUpdate(List<PersonFormRequest> forms) {
        int updateCount = 0;
        for (PersonFormRequest form : forms) {
            try {
                this.insert(form);
                updateCount++;
            } catch (DuplicateKeyException ignored) {
                logger.error("UserRepository doUpdate ERROR[id={}]", form.getId());
            }
        }

        return updateCount;
    }

    public List<UserEntity> findAllByIds(Collection<String> idList) {
        return findAll(PERSON_LIST.ID.in(idList));
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
            e.printStackTrace();
            rtnSHA = null;
        }
        return rtnSHA;
    }
}
