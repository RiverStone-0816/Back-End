package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.CurrentEicnCdr;
import kr.co.eicn.ippbx.model.entity.eicn.CurrentEICNCdrEntity;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public Map<String, CurrentEICNCdrEntity> findAllCurrentCdrByPeer() {
		Map<String, CurrentEICNCdrEntity> map = new HashMap<>();

		putAllEicnCdrEntitiesFromDSL(dsl, map);

		cacheService.pbxServerList(getCompanyId()).forEach(e -> {
			DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
			putAllEicnCdrEntitiesFromDSL(pbxDsl, map);
		});

		return map;
	}

	private void putAllEicnCdrEntitiesFromDSL(DSLContext dslContext, Map<String, CurrentEICNCdrEntity> resultMap) {
		dslContext.selectFrom(CURRENT_EICN_CDR)
				.where(compareCompanyId().and(CURRENT_EICN_CDR.PEER.notEqual("")))
				.fetchInto(CurrentEICNCdrEntity.class)
				.forEach(e -> {
					if (StringUtils.isNotEmpty(e.getPeer()))
						resultMap.putIfAbsent(e.getPeer(), e);
				});
	}

	public List<CurrentEICNCdrEntity> findAllByUniqueId(final String uniqueId) {
		return findAll(CURRENT_EICN_CDR.UNIQUEID.eq(uniqueId));
	}
}
