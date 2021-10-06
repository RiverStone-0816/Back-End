package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.exception.DuplicateKeyException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.RandomCid;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTree;
import kr.co.eicn.ippbx.model.form.RandomCidFormRequest;
import kr.co.eicn.ippbx.model.search.RandomCidSearchRequest;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.CompanyTree.COMPANY_TREE;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.RandomCid.RANDOM_CID;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
@Repository
public class RandomRidRepository extends EicnBaseRepository<RandomCid, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.RandomCid, Integer> {
	protected final Logger logger = LoggerFactory.getLogger(RandomRidRepository.class);

	private final CacheService cacheService;
	private final PBXServerInterface pbxServerInterface;
	private final CompanyTreeRepository companyTreeRepository;

	public RandomRidRepository(CacheService cacheService, PBXServerInterface pbxServerInterface, CompanyTreeRepository companyTreeRepository) {
		super(RANDOM_CID, RANDOM_CID.SEQ, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.RandomCid.class);

		this.cacheService = cacheService;
		this.pbxServerInterface = pbxServerInterface;
		this.companyTreeRepository = companyTreeRepository;
	}

	public Pagination<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.RandomCid> pagination(RandomCidSearchRequest search) {
		return super.pagination(search, conditions(search), Arrays.asList(RANDOM_CID.GROUP_CODE.asc(), RANDOM_CID.SHORT_NUM.asc()));
	}

	public void insert(RandomCidFormRequest form) {
		final Optional<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.RandomCid> optionalDuplicatedShortNum =
				existsShortNum(form.getGroupCode(), form.getShortNum()).stream().findAny();
		if (optionalDuplicatedShortNum.isPresent())
			throw new DuplicateKeyException("이미 사용중인 단축번호가 있습니다.");

		final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.RandomCid record = new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.RandomCid();
		ReflectionUtils.copy(record, form);

		if (isNotEmpty(form.getGroupCode())) {
			final CompanyTree companyTree = companyTreeRepository.findOneByGroupCode(form.getGroupCode());
			if (companyTree != null) {
				record.setGroupCode(form.getGroupCode());
				record.setGroupTreeName(companyTree.getGroupTreeName());
				record.setGroupLevel(companyTree.getGroupLevel());
			}
		}
		record.setCompanyId(getCompanyId());

		super.insert(record);

		cacheService.pbxServerList(getCompanyId()).forEach(e -> {
			DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
			super.insert(pbxDsl, record);
		});
	}

	public void updateByKey(RandomCidFormRequest form, Integer seq) {
		final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.RandomCid entity = findOneIfNullThrow(seq);

		final List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.RandomCid> randomCids = existsShortNum(form.getGroupCode(), form.getShortNum());
		final long count = randomCids.stream().filter(e -> !e.getGroupCode().equals(entity.getGroupCode())).count();
		if (count > 0)
			throw new DuplicateKeyException("이미 사용중인 단축번호가 있습니다.");

		entity.setNumber(form.getNumber());
		entity.setShortNum(form.getShortNum());

		if (isNotEmpty(form.getGroupCode())) {
			final CompanyTree companyTree = companyTreeRepository.findOneByGroupCode(form.getGroupCode());
			if (companyTree != null) {
				entity.setGroupCode(form.getGroupCode());
				entity.setGroupTreeName(companyTree.getGroupTreeName());
				entity.setGroupLevel(companyTree.getGroupLevel());
			}
		}

		super.updateByKey(entity, seq);

		cacheService.pbxServerList(getCompanyId()).forEach(e -> {
			DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
			super.updateByKey(pbxDsl, entity, seq);
		});
	}

	public int delete(Integer key) {
		int delete = super.delete(key);

		cacheService.pbxServerList(getCompanyId()).forEach(e -> {
			DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
			super.delete(pbxDsl, key);
		});

		return delete;
	}

	public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.RandomCid> existsShortNum(String groupCode, Byte shortNum) {
		return findAll(RANDOM_CID.SHORT_NUM.eq(shortNum).and(groupCode != null ? RANDOM_CID.GROUP_CODE.eq(groupCode) : DSL.noCondition()));
	}

	private List<Condition> conditions(RandomCidSearchRequest search) {
		final List<Condition> conditions = new ArrayList<>();

		if (isNotEmpty(search.getGroupCode())) {
			conditions.add(
					DSL.exists(
							dsl.selectOne()
									.from(COMPANY_TREE)
									.where(COMPANY_TREE.GROUP_CODE.eq(RANDOM_CID.GROUP_CODE))
									.and(COMPANY_TREE.COMPANY_ID.eq(getCompanyId())
											.and(COMPANY_TREE.GROUP_TREE_NAME.like("%" + search.getGroupCode() + "%")))
					)

			);
		}
		if (isNotEmpty(search.getNumber()))
			conditions.add(RANDOM_CID.NUMBER.eq(search.getNumber()));

		return conditions;
	}
}
