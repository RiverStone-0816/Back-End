package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.SoundList;
import kr.co.eicn.ippbx.server.model.search.SoundListSearchRequest;
import kr.co.eicn.ippbx.server.util.page.Pagination;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static kr.co.eicn.ippbx.server.jooq.eicn.tables.SoundList.SOUND_LIST;

@Getter
@Repository
public class SoundListRepository extends EicnBaseRepository<SoundList, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.SoundList, Integer> {
	protected final Logger logger = LoggerFactory.getLogger(SoundListRepository.class);

	public SoundListRepository() {
		super(SOUND_LIST, SOUND_LIST.SEQ, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.SoundList.class);
		orderByFields.add(SOUND_LIST.SEQ.desc());
	}

	public List<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.SoundList> findAll(SoundListSearchRequest search) {
		return super.findAll(conditions(search));
	}

	public Pagination<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.SoundList> pagination(SoundListSearchRequest search) {
		return super.pagination(search, conditions(search));
	}

	public Integer nextSequence() {
		final SoundList sequenceSeed = SOUND_LIST.as("SEQUENCE_SEED");
		return dsl.select(DSL.ifnull(DSL.max(sequenceSeed.SEQ), 0).add(1)).from(sequenceSeed).fetchOneInto(Integer.class);
	}

	public void deleteBySoundFile(DSLContext dslContext, final String soundFile) {
		dslContext.deleteFrom(SOUND_LIST)
				.where(SOUND_LIST.SOUND_FILE.eq(soundFile))
				.and(compareCompanyId())
				.execute();
	}

	private List<Condition> conditions(SoundListSearchRequest search) {
		final List<Condition> conditions = new ArrayList<>();

		if (StringUtils.isNotEmpty(search.getSoundName()))
			conditions.add(SOUND_LIST.SOUND_NAME.like("%" + search.getSoundName() + "%"));

		if (search.getArs() != null)
			conditions.add(SOUND_LIST.ARS.eq(search.getArs()));

		return conditions;
	}
	public List<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.SoundList> findAllArs() {
		return findAll(SOUND_LIST.ARS.eq(true));
	}
}
