package kr.co.eicn.ippbx.server.repository;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.Number_070;
import kr.co.eicn.ippbx.server.model.form.NumberTypeChangeRequest;
import kr.co.eicn.ippbx.server.model.search.NumberSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.EicnBaseRepository;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import lombok.Getter;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.server.jooq.eicn.tables.Number_070.NUMBER_070;

@Getter
@Repository
public class NumberRepositoryTest extends EicnBaseRepository<Number_070, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.Number_070, String> {
	protected final Logger logger = LoggerFactory.getLogger(NumberRepositoryTest.class);

	private final CacheService cacheService;
	private final PBXServerInterface pbxServerInterface;

	public NumberRepositoryTest(CacheService cacheService, PBXServerInterface pbxServerInterface) {
		super(NUMBER_070, NUMBER_070.NUMBER, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.Number_070.class);
		orderByFields.add(NUMBER_070.NUMBER.asc());

		this.cacheService = cacheService;
		this.pbxServerInterface = pbxServerInterface;
	}

	public List<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.Number_070> findAll(NumberSearchRequest search) {
		return super.findAll(typeConditions(search));
	}

	public Map<String, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.Number_070> findByAllListCovertToMap(Condition condition) {
		final List<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.Number_070> entities = findAll(condition);
		return entities.stream()
				.collect(Collectors.toMap(kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.Number_070::getNumber, e -> e));
	}

	public void typeChange(NumberTypeChangeRequest form, String number) {
		this.updateByKey(form, number);

		cacheService.pbxServerList(getCompanyId())
				.forEach(e -> {
					try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
						this.updateByKey(pbxDsl, form, number);
					}
				});
	}

	private List<Condition> typeConditions(NumberSearchRequest search) {
		final List<Condition> conditions = new ArrayList<>();

		conditions.add(NUMBER_070.TYPE.eq(search.getType()));
		conditions.add((NUMBER_070.KIND.notEqual("B")));

		return conditions;
	}

	public void updateNumber070Status(final String number, final byte status) {
		dsl.update(NUMBER_070)
				.set(NUMBER_070.STATUS, status)
				.where(getCondition(number))
				.execute();

		cacheService.pbxServerList(getCompanyId()).forEach(e -> {
			try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
				pbxDsl.update(NUMBER_070)
						.set(NUMBER_070.STATUS, status)
						.where(getCondition(number))
						.execute();
			}
		});
	}
}
