package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.MohConfig;
import lombok.Getter;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.MohConfig.MOH_CONFIG;

@Getter
@Repository
public class MohConfigRepository extends EicnBaseRepository<MohConfig, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.MohConfig, Integer> {
	private final Logger logger = LoggerFactory.getLogger(MohListRepository.class);

	public MohConfigRepository() {
		super(MOH_CONFIG, MOH_CONFIG.ID, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.MohConfig.class);
	}

	public void insert(final String categoryCode, final String varName, final String value) {
		insert(dsl, categoryCode, varName, value);
	}

	public void insert(DSLContext dslContext, final String categoryCode, final String varName, final String value) {
		dslContext.insertInto(MOH_CONFIG)
				.set(MOH_CONFIG.CAT_METRIC, 0)
				.set(MOH_CONFIG.VAR_METRIC, 0)
				.set(MOH_CONFIG.COMMENTED, 0)
				.set(MOH_CONFIG.FILENAME, "musiconhold.conf")
				.set(MOH_CONFIG.CATEGORY, categoryCode)
				.set(MOH_CONFIG.VAR_NAME, varName)
				.set( MOH_CONFIG.VAR_VAL, value)
				.execute();
	}
}
