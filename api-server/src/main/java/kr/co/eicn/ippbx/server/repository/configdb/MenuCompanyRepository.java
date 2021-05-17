package kr.co.eicn.ippbx.server.repository.configdb;

import kr.co.eicn.ippbx.server.jooq.configdb.tables.CommonMenuCompany;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CompanyTree;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.server.model.enums.GroupLevelAuth;
import kr.co.eicn.ippbx.server.model.form.MenuFormRequest;
import kr.co.eicn.ippbx.server.model.form.UserMenuSequenceUpdateRequest;
import kr.co.eicn.ippbx.server.repository.eicn.CompanyTreeRepository;
import kr.co.eicn.ippbx.server.repository.eicn.PersonListRepository;
import lombok.Getter;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
public class MenuCompanyRepository extends ConfigDbBaseRepository<CommonMenuCompany, kr.co.eicn.ippbx.server.jooq.configdb.tables.pojos.CommonMenuCompany, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(MenuCompanyRepository.class);

    private final CommonMenuCompany TABLE;
    private final PersonListRepository personListRepository;
    private final CompanyTreeRepository companyTreeRepository;

    public MenuCompanyRepository(String companyId, PersonListRepository personListRepository, CompanyTreeRepository companyTreeRepository) {
        super(new CommonMenuCompany(companyId), new CommonMenuCompany(companyId).SEQ, kr.co.eicn.ippbx.server.jooq.configdb.tables.pojos.CommonMenuCompany.class);
        TABLE = new CommonMenuCompany(companyId);
        this.personListRepository = personListRepository;
        this.companyTreeRepository = companyTreeRepository;

        orderByFields.add(TABLE.SEQUENCE.asc());
    }

    public Integer nextSequence() {
        final CommonMenuCompany sequenceSeed = TABLE.as("SEQUENCE_SEED");
        return dsl.select(DSL.ifnull(DSL.max(sequenceSeed.SEQ), 0).add(1)).from(sequenceSeed.as("SEQUENCE_SEED")).fetchOneInto(Integer.class);
    }

    public List<kr.co.eicn.ippbx.server.jooq.configdb.tables.pojos.CommonMenuCompany> getCompanyMenus() {
        return findAll(TABLE.USERID.eq("COMP_" + getCompanyId()).and(TABLE.AUTH_TYPE.like("%" + g.getUser().getIdType() + "%").and(TABLE.VIEW_YN.eq("Y"))));
    }

    public Integer updateByKeyOrInsertByMenuCompany(MenuFormRequest form, String menuCode, String userid) {
        final kr.co.eicn.ippbx.server.jooq.configdb.tables.pojos.CommonMenuCompany menuCompany = findOneByMenuCode(userid, menuCode);
        if (findAll(TABLE.MENU_CODE.eq(menuCompany.getMenuCode()).and(TABLE.USERID.eq(userid))).size() > 1)
            throw new IllegalArgumentException("메뉴가 중복되었습니다.");

        menuCompany.setMenuName(form.getMenuName());
        menuCompany.setViewYn(Objects.equals(true, form.getView()) ? "Y" : "N");
        if (Objects.nonNull(form.getGroupLevelAuth())) {
            if (Objects.equals(GroupLevelAuth.ALLOW_ONLY_AUTHORIZED_ORGANIZATIONS.getCode(), form.getGroupLevelAuth())) {
                final CompanyTree companyTree = companyTreeRepository.findOneByGroupCode(form.getGroupCode());
                if (companyTree != null) {
                    menuCompany.setGroupCode(companyTree.getGroupCode());
                    menuCompany.setGroupLevel(companyTree.getGroupLevel());
                }
            }
            menuCompany.setGroupLevelAuthYn(form.getGroupLevelAuth());
        }

        if (menuCompany.getUserid().equals("COMP_" + getCompanyId())) {
            menuCompany.setSeq(nextSequence());
            menuCompany.setUserid(userid);

            super.insertOnGeneratedKey(menuCompany);

        } else
            super.updateByKey(menuCompany, menuCompany.getSeq());

        return menuCompany.getSeq();
    }

    public void updateMenuSequence(UserMenuSequenceUpdateRequest form, String userid) {
        final PersonList person = personListRepository.findOneIfNullThrow(userid);
        final List<kr.co.eicn.ippbx.server.jooq.configdb.tables.pojos.CommonMenuCompany> menuCompanySequences = findAllForCompanyByMenuCode(person.getIdType());
        final Map<String, kr.co.eicn.ippbx.server.jooq.configdb.tables.pojos.CommonMenuCompany> menuCompanyMap = findAll(TABLE.MENU_LEVEL.eq(0).and(TABLE.PARENT_MENU_CODE.isNull()).or(TABLE.PARENT_MENU_CODE.eq(""))
                .and(TABLE.USERID.eq("COMP_" + getCompanyId()))).stream().collect(Collectors.toMap(kr.co.eicn.ippbx.server.jooq.configdb.tables.pojos.CommonMenuCompany::getMenuCode, e -> e));
        final Map<String, kr.co.eicn.ippbx.server.jooq.configdb.tables.pojos.CommonMenuCompany> userMenuMap = findAll(TABLE.MENU_LEVEL.eq(0).and(TABLE.PARENT_MENU_CODE.isNull())
                .and(TABLE.USERID.eq(userid))).stream().collect(Collectors.toMap(kr.co.eicn.ippbx.server.jooq.configdb.tables.pojos.CommonMenuCompany::getMenuCode, e -> e));

        for (int i = 0; i < form.getMenuCodes().size(); i++) {
            if (Objects.nonNull(userMenuMap.get(form.getMenuCodes().get(i)))) {
                final kr.co.eicn.ippbx.server.jooq.configdb.tables.pojos.CommonMenuCompany userMenu = userMenuMap.get(form.getMenuCodes().get(i));
                userMenu.setSequence(menuCompanySequences.get(i).getSequence());

                super.updateByKey(userMenu, userMenu.getSeq());
            } else {
                if (Objects.nonNull(menuCompanyMap.get(form.getMenuCodes().get(i)))) {
                    final kr.co.eicn.ippbx.server.jooq.configdb.tables.pojos.CommonMenuCompany menuCompany = menuCompanyMap.get(form.getMenuCodes().get(i));
                    menuCompany.setSeq(nextSequence());
                    menuCompany.setUserid(userid);
                    menuCompany.setSequence(menuCompanySequences.get(i).getSequence());

                    super.insertOnGeneratedKey(menuCompany);
                }
            }
        }
    }

    public void updateSubMenuSequence(UserMenuSequenceUpdateRequest form, String parentMenuCode, String userid) {
        final PersonList person = personListRepository.findOneIfNullThrow(userid);
        final List<kr.co.eicn.ippbx.server.jooq.configdb.tables.pojos.CommonMenuCompany> menuCompanySequences = findAllForCompanyByParentMenuCode(parentMenuCode, person.getIdType());
        final Map<String, kr.co.eicn.ippbx.server.jooq.configdb.tables.pojos.CommonMenuCompany> menuCompanyMap = findAll(TABLE.PARENT_MENU_CODE.eq(parentMenuCode)
                .and(TABLE.USERID.eq("COMP_" + getCompanyId()))).stream().collect(Collectors.toMap(kr.co.eicn.ippbx.server.jooq.configdb.tables.pojos.CommonMenuCompany::getMenuCode, e -> e));
        final Map<String, kr.co.eicn.ippbx.server.jooq.configdb.tables.pojos.CommonMenuCompany> userMenuMap = findAll(TABLE.PARENT_MENU_CODE.eq(parentMenuCode)
                .and(TABLE.USERID.eq(userid))).stream().collect(Collectors.toMap(kr.co.eicn.ippbx.server.jooq.configdb.tables.pojos.CommonMenuCompany::getMenuCode, e -> e));

        for (int i = 0; i < form.getMenuCodes().size(); i++) {
            if (Objects.nonNull(userMenuMap.get(form.getMenuCodes().get(i)))) {
                final kr.co.eicn.ippbx.server.jooq.configdb.tables.pojos.CommonMenuCompany userMenu = userMenuMap.get(form.getMenuCodes().get(i));
                userMenu.setSequence(menuCompanySequences.get(i).getSequence());

                super.updateByKey(userMenu, userMenu.getSeq());
            } else {
                if (Objects.nonNull(menuCompanyMap.get(form.getMenuCodes().get(i)))) {
                    final kr.co.eicn.ippbx.server.jooq.configdb.tables.pojos.CommonMenuCompany menuCompany = menuCompanyMap.get(form.getMenuCodes().get(i));
                    menuCompany.setSeq(nextSequence());
                    menuCompany.setUserid(userid);
                    menuCompany.setSequence(menuCompanySequences.get(i).getSequence());

                    super.insertOnGeneratedKey(menuCompany);
                }
            }
        }
    }

    public void menuReset(String userid) {
        final PersonList person = personListRepository.findOneIfNullThrow(userid);
        super.delete(TABLE.USERID.eq(person.getId()));
    }

    public kr.co.eicn.ippbx.server.jooq.configdb.tables.pojos.CommonMenuCompany findOneByMenuCode(String userId, String menuCode) {
        final kr.co.eicn.ippbx.server.jooq.configdb.tables.pojos.CommonMenuCompany userMenu = findOne(TABLE.USERID.eq(userId).and(TABLE.MENU_CODE.eq(menuCode)));

        if (Objects.isNull(userMenu)) {
            final kr.co.eicn.ippbx.server.jooq.configdb.tables.pojos.CommonMenuCompany menuCompany = findOne(TABLE.USERID.eq("COMP_" + getCompanyId()).and(TABLE.MENU_CODE.eq(menuCode))
                    .and(TABLE.AUTH_TYPE.like("%" + personListRepository.findOne(userId).getIdType() + "%")));

            if (Objects.isNull(menuCompany))
                throw new IllegalArgumentException("존재하지 않는 메뉴입니다.");

            return menuCompany;
        }
        return userMenu;
    }

    public List<kr.co.eicn.ippbx.server.jooq.configdb.tables.pojos.CommonMenuCompany> findAllForCompany(String userId) {
        final PersonList person = personListRepository.findOneById(userId);
        return findAll(TABLE.USERID.eq("COMP_" + getCompanyId()).and(TABLE.VIEW_YN.eq("Y"))
                .and(DSL.cast(TABLE.AUTH_TYPE, String.class).likeRegex(person.getIdType())
                        .or(DSL.cast(TABLE.AUTH_TYPE, String.class).likeRegex(person.getIdType() + ","))
                        .or(DSL.cast(TABLE.AUTH_TYPE, String.class).likeRegex("," + person.getIdType()))
                        .or(DSL.cast(TABLE.AUTH_TYPE, String.class).likeRegex("," + person.getIdType() + ","))));
    }

    public List<kr.co.eicn.ippbx.server.jooq.configdb.tables.pojos.CommonMenuCompany> findAllByUserId(String userId) {
        final PersonList person = personListRepository.findOneById(userId);
        return findAll(TABLE.USERID.eq(userId)
                .and(DSL.cast(TABLE.AUTH_TYPE, String.class).likeRegex(person.getIdType())
                        .or(DSL.cast(TABLE.AUTH_TYPE, String.class).likeRegex(person.getIdType() + ","))
                        .or(DSL.cast(TABLE.AUTH_TYPE, String.class).likeRegex("," + person.getIdType()))
                        .or(DSL.cast(TABLE.AUTH_TYPE, String.class).likeRegex("," + person.getIdType() + ","))));
    }

    public List<kr.co.eicn.ippbx.server.jooq.configdb.tables.pojos.CommonMenuCompany> findAllByUserIdAndParentMenuCode(String userId, String parentMenuCode) {
        return findAll(TABLE.USERID.eq(userId).and(TABLE.PARENT_MENU_CODE.eq(parentMenuCode)));
    }

    public List<kr.co.eicn.ippbx.server.jooq.configdb.tables.pojos.CommonMenuCompany> findAllForCompanyByParentMenuCode(String parentMenuCode, String idType) {
        return dsl.selectFrom(TABLE)
                .where(TABLE.USERID.eq("COMP_" + getCompanyId()))
                .and(TABLE.PARENT_MENU_CODE.eq(parentMenuCode))
                .and(TABLE.AUTH_TYPE.like("%" + idType + "%"))
                .orderBy(TABLE.SEQUENCE.asc())
                .fetchInto(kr.co.eicn.ippbx.server.jooq.configdb.tables.pojos.CommonMenuCompany.class);
    }

    public List<kr.co.eicn.ippbx.server.jooq.configdb.tables.pojos.CommonMenuCompany> findAllForCompanyByMenuCode(String idType) {
        return dsl.selectFrom(TABLE)
                .where(TABLE.USERID.eq("COMP_" + getCompanyId()))
                .and(TABLE.AUTH_TYPE.like("%" + idType + "%"))
                .and(TABLE.PARENT_MENU_CODE.isNull()).or(TABLE.PARENT_MENU_CODE.eq(""))
                .and(TABLE.MENU_LEVEL.eq(0))
                .orderBy(TABLE.SEQUENCE.asc())
                .fetchInto(kr.co.eicn.ippbx.server.jooq.configdb.tables.pojos.CommonMenuCompany.class);
    }

    public kr.co.eicn.ippbx.server.jooq.configdb.tables.pojos.CommonMenuCompany getMenuByMenuCode(String menuCode) {
        final kr.co.eicn.ippbx.server.jooq.configdb.tables.pojos.CommonMenuCompany userMenu = dsl.selectFrom(TABLE)
                .where(TABLE.USERID.eq(g.getUser().getId()))
                .and(TABLE.MENU_CODE.eq(menuCode))
                .fetchOneInto(kr.co.eicn.ippbx.server.jooq.configdb.tables.pojos.CommonMenuCompany.class);

        if (Objects.isNull(userMenu)) {
            final kr.co.eicn.ippbx.server.jooq.configdb.tables.pojos.CommonMenuCompany menuCompany = findOne(TABLE.USERID.eq("COMP_" + getCompanyId()).and(TABLE.MENU_CODE.eq(menuCode)));

            if (Objects.isNull(menuCompany))
                throw new IllegalArgumentException("존재하지 않는 메뉴입니다.");

            return menuCompany;
        }

        return userMenu;
    }
}
