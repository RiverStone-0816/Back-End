package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CompanyTree;
import kr.co.eicn.ippbx.server.model.entity.eicn.UserEntity;
import kr.co.eicn.ippbx.server.repository.eicn.CompanyServerRepository;
import kr.co.eicn.ippbx.server.repository.eicn.PersonListRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
@RequiredArgsConstructor
@Service
public class UserService extends ApiBaseService {
	private final PersonListRepository repository;
	private final CompanyServerRepository companyServerRepository;
	private final OrganizationService organizationService;

	public List<UserEntity> findAllUserEntity() {
		final List<CompanyTree> companyTrees = organizationService.getAllCompanyTrees();
		return repository.findAll().stream()
				.map(e -> {
					final UserEntity response = convertDto(e, UserEntity.class);

					if (isNotEmpty(e.getGroupCode())) {
						response.setCompanyTree(organizationService.findOneCompanyTree(e.getGroupCode()));
						response.setCompanyTrees(organizationService.getCompanyTrees(companyTrees, e.getGroupCode()));
					}

					return response;
				})
				.sorted(Comparator.comparing(UserEntity::getIdName))
				.collect(Collectors.toList());
	}
}
