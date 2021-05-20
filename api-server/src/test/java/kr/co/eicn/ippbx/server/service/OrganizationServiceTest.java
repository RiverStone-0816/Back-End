package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.exception.EntityNotFoundException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTree;
import kr.co.eicn.ippbx.model.OrganizationPerson;
import kr.co.eicn.ippbx.model.entity.eicn.Organization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.*;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class OrganizationServiceTest {
	@Autowired
	private OrganizationService service;

//	@Test
	public void organization_member() {
		final List<Organization> organizations = service.getAllOrganizationPersons();

		printOrganizationTree(organizations);
	}

//	@Test
	public void organization_summary() {
		final List<Organization> organizations = service.getAllOrganizationPersons();

		printOrganizationTree(organizations);

		final Organization entity = organizations.stream()
				.filter(e -> Objects.equals(e.getSeq(), 12))
				.findAny().orElseThrow(EntityNotFoundException::new);

		log.info(entity.getGroupCode() + " , " + entity.getGroupTreeName() + " , " + entity.getGroupLevel() + " , " + entity.getGroupName());

		final String parentTreeName = entity.getParentTreeName();
		if (isEmpty(parentTreeName)) {
			final List<Organization> child = getChildNodes(organizations, entity.getGroupCode());
			log.warn("---------------------------child--------------------------------------");
			printOrganizationTree(child);

			/**
			 * 자식 구성원들의 계층별 수.
			 * 자신이 레벨3 구성원일 때, List[0]은 레벨4 구성원의 수, List[1]은 레벨5 구성원의 수 등이 된다.
			 */
			final List<Integer> collect = countsOfChildren(child);
			for (int i = 0; i < collect.size(); i++) {
				final Integer e = collect.get(i);
				log.info("부모 노드 레벨 {} 하위 구성원수 : {}", (i + 1), e);
			}

			final long sum = child.stream()
					.mapToInt(e -> e.getPersons().size())
					.sum();

			log.warn("자신에게 속한 사용자 숫자 : {}", sum + entity.getPersons().size());
		}
		else {
			final String[] s = split(parentTreeName, "_");

			final List<Organization> parent = organizationSearch(organizations, e -> Arrays.stream(s).anyMatch(e1 -> e.getGroupCode().equals(e1)));

			log.warn("---------------------------parent--------------------------------------");
			printOrganizationTree(parent);
			log.warn("---------------------------//parent--------------------------------------");

			final boolean isLeaf = isLeaf(organizations, entity.getGroupCode());
			log.info("isLeaf? {}", isLeaf);

			if (isLeaf) {
				final int sum = Stream.of(entity).mapToInt(e -> e.getPersons().size()).sum();

				log.warn("자신에게 속한 사용자 숫자 : {}", sum);
			}
			// 자식 노드가 있다면
			else {
				final List<Organization> child = getChildNodes(organizations, entity.getGroupCode());
				log.warn("---------------------------child--------------------------------------");
				printOrganizationTree(child);

				/**
				 * 자식 구성원들의 계층별 수.
				 * 자신이 레벨3 구성원일 때, List[0]은 레벨4 구성원의 수, List[1]은 레벨5 구성원의 수 등이 된다.
				 */
				final List<Integer> collect = countsOfChildren(child);
				for (int i = 0; i < collect.size(); i++) {
					final Integer e = collect.get(i);
					log.info("부모 노드 레벨 {} 하위 구성원수 : {}", (i + 1), e);
				}

				final long sum = child.stream().mapToInt(e -> e.getPersons().size()).sum();

				log.warn("자신에게 속한 사용자 숫자 : {}", sum + entity.getPersons().size());
			}
		}

		final List<Organization> siblings = organizations.stream()
				.filter(e -> !e.getGroupCode().equals(entity.getGroupCode()))
				.filter(e -> defaultString(e.getParentGroupCode()).equals(defaultString(entity.getParentGroupCode())))
				.filter(e -> e.getGroupLevel().equals(entity.getGroupLevel()))
				.collect(toList());;

		log.info("\n");

		for (Organization sibling : siblings) {
			log.warn("---------------------------sibling--------------------------------------");
			log.info(sibling.getGroupLevel() + ", " + sibling.getGroupName());
			sibling.getPersons().forEach(e -> log.info(e.toString()));

			final List<Organization> siblingChildNodes = getChildNodes(organizations, sibling.getGroupCode());

			final List<Integer> integers = countsOfChildren(siblingChildNodes);
			for (int i = 0; i < integers.size(); i++) {
				final Integer e = integers.get(i);
				log.info("형제 노드 레벨 {} 하위 구성원수 : {}", (i + 1), e);
			}

			final long sum = siblingChildNodes.stream().mapToInt(e -> e.getPersons().size()).sum();

			log.warn("자신에게 속한 사용자 숫자 : {}", sum + sibling.getPersons().size());
			log.warn("---------------------------//sibling--------------------------------------");
		}
	}

//	@Test
	public void delete() {
//		service.deleteOrganization(8);
	}

	private List<Organization> organizationSearch(Predicate<Organization> predicate) {
		return organizationSearch(service.getAllOrganizationPersons(), predicate);
	}

	private List<Organization> organizationSearch(List<Organization> organizations, Predicate<Organization> predicate) {
		return organizations.stream()
				.filter(predicate)
				.collect(toList());
	}

	private boolean isLeaf(List<Organization> organizations, String groupCode) {
		return organizations.stream()
				.noneMatch(e -> e.getParentTreeName().contains(groupCode));
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

	private String prefix(Integer level) {
		String prefix = "-";
		for (int i = 0; i < level ; i++) {
			prefix += "--";
		}
		return prefix;
	}

	private void printOrganizationTree(List<Organization> organizations) {
		log.warn("----------------------------------------------------------------------");
		organizations
				.forEach(e -> {
							log.info(prefix(e.getGroupLevel()) + e.getGroupLevel() + ", " + "seq(" + e.getSeq() + "), " +
									e.getGroupCode() + ", " + e.getGroupName() + ", " + e.getParentTreeName() + ", "
									+ e.getGroupTreeName() + ", memberSize: " + e.getPersons().size()
							);
							if (e.getPersons().size() > 0) {
								for (OrganizationPerson member : e.getPersons()) {
									log.info(prefix(e.getGroupLevel() + 1) + (e.getGroupLevel() + 1) + ", "
											+ member.getId() + ", " + member.getIdName() + ", " + member.getPeer());
								}
							}
						}
				);
		log.warn("----------------------------------------------------------------------");
	}
}
