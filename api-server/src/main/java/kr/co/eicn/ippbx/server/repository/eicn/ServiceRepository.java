package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.enums.DashboardInfoDashboardType;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.ServiceList;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTree;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.Number_070;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchServiceResponse;
import kr.co.eicn.ippbx.model.entity.eicn.ServiceListEntity;
import kr.co.eicn.ippbx.model.form.ServiceListFormRequest;
import kr.co.eicn.ippbx.model.form.ServiceListFormUpdateRequest;
import kr.co.eicn.ippbx.model.search.ServiceListSearchRequest;
import kr.co.eicn.ippbx.model.search.search.SearchServiceRequest;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import lombok.Getter;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.Number_070.NUMBER_070;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.ServiceList.SERVICE_LIST;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
@Repository
public class ServiceRepository extends EicnBaseRepository<ServiceList, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ServiceList, Integer> {
	protected final Logger logger = LoggerFactory.getLogger(ServiceRepository.class);

	private final DashboardInfoRepository dashboardInfoRepository;
	private final PBXServerInterface pbxServerInterface;
	private final CacheService cacheService;
	private final CompanyTreeRepository companyTreeRepository;

	public ServiceRepository(DashboardInfoRepository dashboardInfoRepository, PBXServerInterface pbxServerInterface, CacheService cacheService, CompanyTreeRepository companyTreeRepository) {
		super(SERVICE_LIST, SERVICE_LIST.SEQ, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ServiceList.class);
		orderByFields.add(SERVICE_LIST.SEQ.asc());

		this.dashboardInfoRepository = dashboardInfoRepository;
		this.cacheService = cacheService;
		this.pbxServerInterface = pbxServerInterface;
		this.companyTreeRepository = companyTreeRepository;
	}

	public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ServiceList> findAll(ServiceListSearchRequest search) {
		return findAll(conditions(search));
	}

	public List<SearchServiceResponse> search(SearchServiceRequest search) {
		return findAll(searchConditions(search)).stream()
				.filter(e -> isNotEmpty(e.getSvcName()) && isNotEmpty(e.getSvcNumber()))
				.map(e -> modelMapper.map(e, SearchServiceResponse.class))
				.collect(Collectors.toList());
	}

	public List<ServiceListEntity> findAllJoinNumber070() {
		return dsl.select(SERVICE_LIST.fields())
				.select(NUMBER_070.fields())
				.from(SERVICE_LIST)
				.join(NUMBER_070)
				.on(SERVICE_LIST.SVC_NUMBER.eq(NUMBER_070.NUMBER)
					.and(NUMBER_070.COMPANY_ID.eq(getCompanyId())))
				.where(DSL.trueCondition())
				.and(compareCompanyId())
				.orderBy(SERVICE_LIST.SVC_NAME.asc())
				.fetch(record -> {
					final ServiceListEntity entity = record.into(SERVICE_LIST).into(ServiceListEntity.class);
					entity.setNumber070(record.into(NUMBER_070).into(Number_070.class));

					return entity;
				});
	}

	//상담화면 수신경로
	public String getSvcNumberByNumber(String number) {
		return dsl.select(SERVICE_LIST.SVC_NAME)
				.from(SERVICE_LIST)
				.where(compareCompanyId())
				.and(SERVICE_LIST.SVC_NUMBER.eq(number))
				.fetchOneInto(String.class);
	}

	//인입경로별 통계
	public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ServiceList> findAllBySvcNumber(List<String> svcNumbers) {
		Condition condition = DSL.noCondition();

		if (svcNumbers.size() > 0) {
			for (int i = 0; i < svcNumbers.size(); i++) {
				if (i == 0)
					condition = condition.and(SERVICE_LIST.SVC_NUMBER.eq(svcNumbers.get(i)));
				else
					condition = condition.or(SERVICE_LIST.SVC_NUMBER.eq(svcNumbers.get(i)));
			}
		}
		return findAll(condition);
	}

	public Record insertOnGeneratedKey(ServiceListFormRequest form) {
		if (isNotEmpty(form.getSvcCid()))
			form.setSvcCid(form.getSvcCid().replace("-", ""));
		if (isNotEmpty(form.getGroupCode())) {
			final CompanyTree companyTree = companyTreeRepository.findOneByGroupCode(form.getGroupCode());
			if (companyTree != null) {
				form.setGroupCode(companyTree.getGroupCode());
				form.setGroupLevel(companyTree.getGroupLevel());
				form.setGroupTreeName(companyTree.getGroupTreeName());
			}
		}
		form.setCompanyId(getCompanyId());

		final Record r = super.insertOnGeneratedKey(form);
		cacheService.pbxServerList(getCompanyId())
				.forEach(e -> {
					try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
						this.insert(pbxDsl, form);
					}
				});

		if (r.getValue(SERVICE_LIST.SEQ) != null)
			dashboardInfoRepository.insert(form.getSvcName().concat(" 통계"), DashboardInfoDashboardType.service_stat, form.getSvcNumber(), 2);

		return r;
	}

	public void updateByKey(ServiceListFormUpdateRequest form, Integer key) {
		final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ServiceList oldServiceInfo = findOneIfNullThrow(key);

		if (isEmpty(form.getSvcCid()))
			form.setSvcCid(form.getSvcNumber());
		if (isNotEmpty(form.getGroupCode())) {
			final CompanyTree companyTree = companyTreeRepository.findOneByGroupCode(form.getGroupCode());
			if (companyTree != null) {
				form.setGroupCode(companyTree.getGroupCode());
				form.setGroupLevel(companyTree.getGroupLevel());
				form.setGroupTreeName(companyTree.getGroupTreeName());
			}
		}
		form.setCompanyId(getCompanyId());

		super.updateByKey(form, key);
		cacheService.pbxServerList(getCompanyId())
				.forEach(e -> {
					try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
						this.updateByKey(pbxDsl, form, key);
					}
				});

		dashboardInfoRepository.updateByValue(form.getSvcName().concat(" 통계"), form.getSvcNumber(), oldServiceInfo.getSvcNumber());
	}

	@Override
	public int deleteOnIfNullThrow(Integer key) {
		final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ServiceList entity = findOneIfNullThrow(key);

		final int r = super.delete(key);
		cacheService.pbxServerList(getCompanyId())
				.forEach(e -> {
					try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
						super.delete(pbxDsl, key);
					}
				});

		dashboardInfoRepository.deleteByValue(entity.getSvcNumber());

		return r;
	}

	private List<Condition> conditions(ServiceListSearchRequest search) {
		final List<Condition> conditions = new ArrayList<>();

		return conditions;
	}

	private List<Condition> searchConditions(SearchServiceRequest search) {
		final List<Condition> conditions = new ArrayList<>();

		return conditions;
	}
}
