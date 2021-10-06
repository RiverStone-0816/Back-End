package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.exception.EntityNotFoundException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyInfo;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTree;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTreeLevelName;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.model.OrganizationPerson;
import kr.co.eicn.ippbx.model.dto.eicn.MemberSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.OrganizationPersonSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse;
import kr.co.eicn.ippbx.model.entity.eicn.CompanyServerEntity;
import kr.co.eicn.ippbx.model.entity.eicn.Organization;
import kr.co.eicn.ippbx.model.entity.eicn.OrganizationMeta;
import kr.co.eicn.ippbx.model.entity.eicn.OrganizationMetaChatt;
import kr.co.eicn.ippbx.model.form.CompanyTreeNameUpdateFormRequest;
import kr.co.eicn.ippbx.model.form.OrganizationFormRequest;
import kr.co.eicn.ippbx.model.form.OrganizationFormUpdateRequest;
import kr.co.eicn.ippbx.model.search.ChattingMemberSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.CompanyInfoRepository;
import kr.co.eicn.ippbx.server.repository.eicn.CompanyTreeLevelNameRepository;
import kr.co.eicn.ippbx.server.repository.eicn.CompanyTreeRepository;
import kr.co.eicn.ippbx.server.repository.eicn.PersonListRepository;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.CompanyTree.COMPANY_TREE;
import static org.apache.commons.lang3.StringUtils.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrganizationService extends ApiBaseService {
    private final CompanyTreeLevelNameRepository companyTreeLevelNameRepository;
    private final CompanyTreeRepository companyTreeRepository;
    private final PersonListRepository personListRepository;
    private final CompanyInfoRepository companyInfoRepository;
    private final PBXServerInterface pbxServerInterface;
    private final CacheService cacheService;
    private final ChattBookmarkService chattBookmarkService;

    public List<Organization> getAllOrganizationPersons() {
        final List<Organization> organizations = companyTreeRepository.getAllOrganization();
        final List<PersonList> persons = personListRepository.findAll();

        organizations.forEach(e -> e.setPersons(
                persons.stream()
                        .filter(person -> person.getGroupCode().equals(e.getGroupCode()))
                        .map(person -> modelMapper.map(person, OrganizationPerson.class))
                        .sorted(comparing(OrganizationPerson::getIdName))
                        .collect(Collectors.toList())
                )
        );
        return organizations;
    }

    public List<OrganizationMetaChatt> getOrganizationMetaChatt(ChattingMemberSearchRequest search) {
        final OrganizationMetaChatt organizationMetaChatt = new OrganizationMetaChatt();
        final List<PersonList> persons = personListRepository.findAllByChatting(search);
        final List<CompanyTree> companyTree = getAllCompanyTrees();
        int size = 0;
        if (companyTree.size() > 0) {
            for (CompanyTree level : companyTree) {
                if (level.getGroupLevel() > size) {
                    size = level.getGroupLevel();
                }
            }
        }
        organizationMeta(size, persons, companyTree, organizationMetaChatt);
        return organizationMetaChatt.getOrganizationMetaChatt();
    }

    private int organizationMeta(int size, List<PersonList> persons, List<CompanyTree> companyTrees, OrganizationMetaChatt metaChatt) {
        if (size == 0) {
            return 0;
        }
        final List<OrganizationMetaChatt> list = metaChatt.getOrganizationMetaChatt();
        List<OrganizationMetaChatt> returnList = new ArrayList<>();
        companyTrees.forEach(e -> {
            if (size == e.getGroupLevel()) {
                final OrganizationMetaChatt result = new OrganizationMetaChatt();
                ReflectionUtils.copy(result, e);
                result.setPersonList(persons.stream().filter(p -> e.getGroupCode().equals(p.getGroupCode())).sorted(comparing(PersonList::getIdName)).map(p -> convertDto(p, PersonDetailResponse.class)).collect(Collectors.toList()));
                if (list != null) {
                    result.setOrganizationMetaChatt(list.stream().filter(o -> o.getParentGroupCode().equals(result.getGroupCode())).collect(Collectors.toList()));
                }
                returnList.add(result);
            }
        });
        metaChatt.setOrganizationMetaChatt(returnList);
        return organizationMeta(size - 1, persons, companyTrees, metaChatt);
    }

    public List<CompanyTree> getAllCompanyTrees() {
        return companyTreeRepository.findAll(DSL.noCondition(), Arrays.asList(COMPANY_TREE.GROUP_TREE_NAME.asc(), COMPANY_TREE.PARENT_TREE_NAME.asc()));
    }

    public OrganizationPersonSummaryResponse getOrganizationPersonSummary(Integer seq) {
        final OrganizationPersonSummaryResponse summary = new OrganizationPersonSummaryResponse();
        final List<Organization> organizations = getAllOrganizationPersons();

        final Organization entity = organizations.stream().filter(e -> Objects.equals(e.getSeq(), seq)).findAny().orElseThrow(EntityNotFoundException::new);
        final String parentTreeName = entity.getParentTreeName();

        if (isEmpty(parentTreeName)) { // 최상위 노드
            final List<Organization> childNode = getChildNodes(organizations, entity.getGroupCode());
            final int childMemberCnt = childNode.stream().mapToInt(e -> e.getPersons().size()).sum();

            summary.setUserCountBelonging(childMemberCnt + entity.getPersons().size());
            summary.setCountsOfChildren(countsOfChildren(childNode));
        } else {
            // 전체 부모 조직정보
            summary.setParents(organizationGroupTree(organizations, parentTreeName));

            final boolean isLeaf = isLeaf(organizations, entity.getGroupCode());
            log.debug("isLeaf? {}", isLeaf);

            if (isLeaf) {
                summary.setUserCountBelonging(Stream.of(entity).mapToInt(e -> e.getPersons().size()).sum());
            }
            // 자식 노드가 있다면
            else {
                final List<Organization> childNode = getChildNodes(organizations, entity.getGroupCode());
                final int childMemberCnt = childNode.stream().mapToInt(e -> e.getPersons().size()).sum();

                summary.setUserCountBelonging(childMemberCnt + entity.getPersons().size());
                summary.setCountsOfChildren(countsOfChildren(childNode));
            }
        }

        final List<Organization> siblings = getSiblingNodes(organizations, entity);
        final List<MemberSummaryResponse> memberSummaries = new ArrayList<>(siblings.size());

        for (Organization sibling : siblings) {
            final List<Organization> siblingChildNodes = getChildNodes(organizations, sibling.getGroupCode());
            final int sum = siblingChildNodes.stream().mapToInt(e -> e.getPersons().size()).sum();

            final MemberSummaryResponse siblingNode = modelMapper.map(sibling, MemberSummaryResponse.class);

            siblingNode.setUserCountBelonging(sum + sibling.getPersons().size());
            siblingNode.setCountsOfChildren(countsOfChildren(siblingChildNodes));

            memberSummaries.add(siblingNode);
        }
        // 같은 레벨의 조직정보
        summary.setMemberSummaries(memberSummaries);

        return summary;
    }

    public Integer insertOrganization(OrganizationFormRequest form) {
        final CompanyInfo company = companyInfoRepository.findOne(DSL.noCondition());
        if (form.getGroupLevel() > company.getMaxTreeLevel())
            throw new IllegalArgumentException("조직도 레벨은 " + company.getMaxTreeLevel() + "까지 가능합니다.");

        return companyTreeRepository.insertOnGeneratedKey(form).get(COMPANY_TREE.SEQ);
    }

    public void updateOrganization(OrganizationFormUpdateRequest form, Integer key) {
        companyTreeRepository.updateByKey(form, key);
    }

    public void deleteOrganization(Integer key) {
        final CompanyTree entity = companyTreeRepository.findOneIfNullThrow(key);

        final List<String> childNode = organizationSearch(e -> e.getGroupCode().equals(entity.getGroupCode()) || e.getParentTreeName().contains(entity.getGroupCode()))
                .stream()
                .map(CompanyTree::getGroupCode)
                .collect(toList());

        companyTreeRepository.delete(COMPANY_TREE.GROUP_TREE_NAME.like("%" + entity.getGroupCode() + "%"));

        // 해당 부서를 참조하는 상담원 부서코드 초기화
        final List<CompanyServerEntity> pbxServerList = cacheService.pbxServerList(entity.getCompanyId());

        personListRepository.partOrganizationCodeInitialize(childNode);
        pbxServerList.forEach(e -> {
            DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
            personListRepository.partOrganizationCodeInitialize(pbxDsl, childNode);
        });
    }

    /**
     * 조직 메타 정보목록 반환
     */
    public List<CompanyTreeLevelName> listMetaType() {
        return companyTreeLevelNameRepository.listMetaType();
    }

    public void updateMetaType(Set<CompanyTreeNameUpdateFormRequest> record) {
        companyTreeLevelNameRepository.updateMetaTypes(record);
    }

    // 데이터 갱신이 필요할때
    private List<Organization> organizationSearch(Predicate<Organization> predicate) {
        return organizationSearch(getAllOrganizationPersons(), predicate);
    }

    /**
     * 조직정보를 찾는다.
     *
     * @return
     */
    private List<Organization> organizationSearch(List<Organization> organizations, Predicate<Organization> predicate) {
        return organizations.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public List<Organization> organizationGroupTree(String groupTreeName) {
        return organizationGroupTree(getAllOrganizationPersons(), groupTreeName);
    }

    /**
     * 차수별 조직정보를 반환한다.
     * - 조직, 조직에 속한 사용자 목록
     *
     * @return List<Organization>
     */
    public List<Organization> organizationGroupTree(List<Organization> organizations, String groupTreeName) {
        if (isEmpty(groupTreeName))
            return Collections.emptyList();

        return organizationSearch(organizations, e -> Arrays.stream(split(groupTreeName, "_")).anyMatch(code -> e.getGroupCode().equals(code)));
    }

    public List<CompanyTree> getCompanyTrees(String groupCode) {
        return getCompanyTrees(getAllCompanyTrees(), groupCode);
    }

    /**
     * 차수별 조직정보를 반환한다.
     *
     * @return List<CompanyTree>
     */
    public List<CompanyTree> getCompanyTrees(List<CompanyTree> companyTrees, String groupCode) {
        if (isEmpty(groupCode))
            return Collections.emptyList();

        return companyTrees.stream().filter(e -> e.getGroupCode().equals(groupCode)).findFirst()
                .map(companyTree -> companyTrees.stream().filter(e -> Arrays.stream(split(companyTree.getGroupTreeName(), "_")).anyMatch(code -> e.getGroupCode().equals(code)))
                        .collect(toList())).orElse(Collections.emptyList());
    }

    private boolean isLeaf(List<Organization> organizations, String groupCode) {
        return organizations.stream()
                .noneMatch(e -> e.getParentTreeName().contains(groupCode));
    }

    /**
     * 같은 레벨의 조직정보를 구한다.
     */
    private List<Organization> getSiblingNodes(List<Organization> organizations, Organization entity) {
        return organizations.stream()
                .filter(e -> !e.getGroupCode().equals(entity.getGroupCode()))
                .filter(e -> defaultString(e.getParentGroupCode()).equals(defaultString(entity.getParentGroupCode())))
                .filter(e -> e.getGroupLevel().equals(entity.getGroupLevel()))
                .collect(toList());
    }

    /**
     * 하위 조직정보를 구한다.
     */
    private List<Organization> getChildNodes(List<Organization> organizations, final String groupCode) {
        return organizationSearch(organizations, e -> e.getParentTreeName().contains(groupCode));
    }

    private List<Integer> countsOfChildren(List<Organization> childNode) {
        return childNode.stream()
                .collect(groupingBy(CompanyTree::getGroupLevel))
                .values().stream()
                .map(List::size)
                .collect(toList());
    }

    /**
     * 고객사 조직정보 조회
     */
    public CompanyTree findOneCompanyTree(final String groupCode) {
        return companyTreeRepository.findOneIfNullThrow(COMPANY_TREE.GROUP_CODE.eq(groupCode));
    }

    /**
     * 그룹레벨메타정보, 해당 그룹레벨에 조직정보 목록을 반환
     */
    public List<OrganizationMeta> getMetaTree() {
        final List<CompanyTree> companyTrees = getAllCompanyTrees();

        return companyTreeLevelNameRepository.findAll().stream()
                .map(e -> {
                    final OrganizationMeta organizationMeta = modelMapper.map(e, OrganizationMeta.class);
                    organizationMeta.setCompanyTrees(
                            companyTrees.stream()
                                    .filter(tree -> tree.getGroupLevel().equals(e.getGroupLevel()))
                                    .collect(toList())
                    );
                    return organizationMeta;
                })
                .collect(toList());
    }
}
