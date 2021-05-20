package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.meta.jooq.configdb.tables.pojos.CommonMenuCompany;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTree;
import kr.co.eicn.ippbx.model.dto.configdb.UserMenuCompanyResponse;
import kr.co.eicn.ippbx.model.enums.GroupLevelAuth;
import kr.co.eicn.ippbx.server.repository.configdb.MenuCompanyRepository;
import kr.co.eicn.ippbx.server.repository.eicn.CompanyTreeRepository;
import kr.co.eicn.ippbx.server.repository.eicn.PersonListRepository;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MenuCompanyService extends ApiBaseService implements ApplicationContextAware {
    protected final Logger logger = LoggerFactory.getLogger(MenuCompanyService.class);
    private final Map<String, MenuCompanyRepository> repositories = new HashMap<>();
    private ApplicationContext applicationContext;

    private final PersonListRepository personListRepository;
    private final CompanyTreeRepository companyTreeRepository;

    public MenuCompanyService(PersonListRepository personListRepository, CompanyTreeRepository companyTreeRepository) {
        this.personListRepository = personListRepository;
        this.companyTreeRepository = companyTreeRepository;
    }

    public MenuCompanyRepository getRepository() {
        if (g.getUser() == null || StringUtils.isEmpty(g.getUser().getCompanyId()))
            throw new IllegalStateException(message.getText("messages.company.notfound"));

        return repositories.computeIfAbsent(g.getUser().getCompanyId(), companyId -> {
            final MenuCompanyRepository repository = new MenuCompanyRepository(companyId, personListRepository, companyTreeRepository);
            applicationContext.getAutowireCapableBeanFactory().autowireBean(repository);
            return repository;
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public List<CommonMenuCompany> getCompanyMenus() {
        return this.getRepository().getCompanyMenus();
    }

    public CommonMenuCompany getMenuByMenuCode(String menuCode) {
        return this.getRepository().getMenuByMenuCode(menuCode);
    }

    /**
     * 사용자별 메뉴 목록 조회
     */
    public List<UserMenuCompanyResponse> getUserMenus(String userid) {
        final List<CommonMenuCompany> menusForCompany = this.getRepository().findAllForCompany(userid);
        final List<CommonMenuCompany> menusForUser = this.getRepository().findAllByUserId(userid);

        final List<CommonMenuCompany> resultMenus = new ArrayList<>(menusForUser);
        for (CommonMenuCompany menu : menusForCompany) {
            if (resultMenus.contains(menu)) {
                CommonMenuCompany menuInResult = resultMenus.get(resultMenus.indexOf(menu));
                resultMenus.remove(menuInResult);

                if (Objects.equals(menu.getViewYn(), menuInResult.getViewYn()))
                    resultMenus.add(menuInResult);
                else if (Objects.equals("N", menuInResult.getViewYn()))
                    resultMenus.add(menuInResult);

            } else {
                resultMenus.add(menu);
            }
        }

        final List<UserMenuCompanyResponse> menus = this.convertToMenu(resultMenus);
        this.sortBySequence(menus);

        return menus;
    }

    /**
     * 메뉴 순서 목록 조회
     */
    public List<UserMenuCompanyResponse> getParentMenu(String userid) {
        final List<UserMenuCompanyResponse> menus = new ArrayList<>();
        final List<CommonMenuCompany> menusForCompany = this.getRepository().findAllForCompany(userid).stream().filter(e -> Objects.isNull(e.getParentMenuCode()) && e.getMenuLevel().equals(0)).collect(Collectors.toList());
        final List<CommonMenuCompany> menusForUser = this.getRepository().findAllByUserId(userid).stream().filter(e -> Objects.isNull(e.getParentMenuCode()) && e.getMenuLevel().equals(0)).collect(Collectors.toList());

        final List<CommonMenuCompany> resultMenus = new ArrayList<>(menusForUser);
        for (CommonMenuCompany menu : menusForCompany) {
            if (!resultMenus.contains(menu))
                resultMenus.add(menu);
        }
        resultMenus.forEach(e -> {
           final UserMenuCompanyResponse menu = new UserMenuCompanyResponse();
            ReflectionUtils.copy(menu, e);
            menus.add(menu);
        });

        return menus;
    }

    /**
     * 서브 메뉴 순서 목록 조회
     */
    public UserMenuCompanyResponse getChildrenMenuByParentMenu(String userid, String parentMenuCode) {
        final CommonMenuCompany parentMenu = this.getRepository().findOneByMenuCode(userid, parentMenuCode);
        final List<CommonMenuCompany> menusForCompany = this.getRepository().findAllForCompanyByParentMenuCode(parentMenuCode, personListRepository.findOne(userid).getIdType());
        final List<CommonMenuCompany> menusForUser = this.getRepository().findAllByUserIdAndParentMenuCode(userid, parentMenuCode);
        final UserMenuCompanyResponse menus = new UserMenuCompanyResponse();

        final List<CommonMenuCompany> resultMenus = new ArrayList<>(menusForUser);
        for (CommonMenuCompany menu : menusForCompany) {
            if (!resultMenus.contains(menu))
                resultMenus.add(menu);
        }

        ReflectionUtils.copy(menus, parentMenu);

        if (Objects.nonNull(menus.getGroupCode()) && Objects.equals(GroupLevelAuth.ALLOW_ONLY_AUTHORIZED_ORGANIZATIONS.getCode(), menus.getGroupLevelAuthYn())) {
            final CompanyTree companyTree = companyTreeRepository.findOneByGroupCode(menus.getGroupCode());
            if (companyTree != null)
                menus.setGroupName(companyTree.getGroupName());
        }

        joinToParentMenu(menus, resultMenus);

        return menus;
    }

    public List<UserMenuCompanyResponse> convertToMenu(List<kr.co.eicn.ippbx.meta.jooq.configdb.tables.pojos.CommonMenuCompany> menuCompanies) {
        final List<UserMenuCompanyResponse> menus = new ArrayList<>();

        final List<kr.co.eicn.ippbx.meta.jooq.configdb.tables.pojos.CommonMenuCompany> roots = menuCompanies.stream().filter(e -> StringUtils.isEmpty(e.getParentMenuCode())).collect(Collectors.toList());
        menuCompanies.removeAll(roots);

        roots.forEach(e -> {
            final UserMenuCompanyResponse menu = new UserMenuCompanyResponse();
            ReflectionUtils.copy(menu, e);

            if (Objects.nonNull(menu.getGroupCode()) && Objects.equals(GroupLevelAuth.ALLOW_ONLY_AUTHORIZED_ORGANIZATIONS.getCode(), menu.getGroupLevelAuthYn())) {
                final CompanyTree companyTree = companyTreeRepository.findOneByGroupCode(menu.getGroupCode());
                if (companyTree != null)
                    menu.setGroupName(companyTree.getGroupName());
            }

            menus.add(menu);
        });

        for (UserMenuCompanyResponse menu : menus) {
            joinToParentMenu(menu, menuCompanies);
        }

        return menus;
    }

    private void joinToParentMenu(UserMenuCompanyResponse parentMenu, List<kr.co.eicn.ippbx.meta.jooq.configdb.tables.pojos.CommonMenuCompany> menuCompanies) {
        final List<kr.co.eicn.ippbx.meta.jooq.configdb.tables.pojos.CommonMenuCompany> children = menuCompanies.stream().filter(e -> Objects.equals(parentMenu.getMenuCode(), e.getParentMenuCode())).collect(Collectors.toList());
        menuCompanies.removeAll(children);

        children.forEach(e -> {
            final UserMenuCompanyResponse menu = new UserMenuCompanyResponse();
            ReflectionUtils.copy(menu, e);
            parentMenu.getChildren().add(menu);

            if (Objects.nonNull(menu.getGroupCode()) && Objects.equals(GroupLevelAuth.ALLOW_ONLY_AUTHORIZED_ORGANIZATIONS.getCode(), menu.getGroupLevelAuthYn())) {
                final CompanyTree companyTree = companyTreeRepository.findOneByGroupCode(menu.getGroupCode());
                if (companyTree != null)
                    parentMenu.setGroupName(companyTree.getGroupName());
            }

            joinToParentMenu(menu, menuCompanies);
        });
    }

    public void sortBySequence(List<UserMenuCompanyResponse> menus) {
        menus.sort(Comparator.comparing(UserMenuCompanyResponse::getSequence));

        for (UserMenuCompanyResponse menu : menus) {
            sortBySequence(menu.getChildren());
        }
    }
}
