package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.CurrentEicnCdr;
import kr.co.eicn.ippbx.model.entity.eicn.CurrentEICNCdrEntity;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import lombok.Getter;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.*;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.CurrentEicnCdr.CURRENT_EICN_CDR;

@Getter
@Repository
public class CurrentEICNCdrRepository extends EicnBaseRepository<CurrentEicnCdr, CurrentEICNCdrEntity, String> {
	protected final Logger logger = LoggerFactory.getLogger(CurrentEICNCdrRepository.class);

	private final CacheService cacheService;
	private final PBXServerInterface pbxServerInterface;

	public CurrentEICNCdrRepository(CacheService cacheService, PBXServerInterface pbxServerInterface) {
		super(CURRENT_EICN_CDR, CURRENT_EICN_CDR.UNIQUEID, CurrentEICNCdrEntity.class);
		this.cacheService = cacheService;
		this.pbxServerInterface = pbxServerInterface;

		orderByFields.add(CURRENT_EICN_CDR.RING_DATE.desc());
	}

	public List<CurrentEICNCdrEntity> findAllLimit() {
		return dsl.select()
				.from(CURRENT_EICN_CDR)
				.where(compareCompanyId())
				.limit(10)
				.fetchInto(CurrentEICNCdrEntity.class);
	}

	public Map<String, CurrentEICNCdrEntity> findAllFromBoth() {
		Map<String, CurrentEICNCdrEntity> map = new HashMap<>(dsl.selectFrom(CURRENT_EICN_CDR).where(compareCompanyId().and(CURRENT_EICN_CDR.PEER.notEqual(""))).fetchMap(CURRENT_EICN_CDR.PEER, CurrentEICNCdrEntity.class));

		cacheService.pbxServerList(getCompanyId()).forEach(e -> {
			DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
			map.putAll(pbxDsl.selectFrom(CURRENT_EICN_CDR).where(compareCompanyId().and(CURRENT_EICN_CDR.PEER.notEqual(""))).fetchMap(CURRENT_EICN_CDR.PEER, CurrentEICNCdrEntity.class));
		});

		return map;
	}
}
