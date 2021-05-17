package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.SipBuddies;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import lombok.Getter;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static kr.co.eicn.ippbx.server.jooq.eicn.tables.SipBuddies.SIP_BUDDIES;

@Getter
@Repository
public class SipBuddiesRepository extends EicnBaseRepository<SipBuddies, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.SipBuddies, Integer> {
	protected final Logger logger = LoggerFactory.getLogger(ServiceRepository.class);
	private final PBXServerInterface pbxServerInterface;
	private final CacheService cacheService;

	public SipBuddiesRepository(PBXServerInterface pbxServerInterface, CacheService cacheService) {
		super(SIP_BUDDIES, SIP_BUDDIES.ID, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.SipBuddies.class);
		this.pbxServerInterface = pbxServerInterface;
		this.cacheService = cacheService;
	}

	public void deleteAndNameAllPbxServers(String peer) {
		dsl.deleteFrom(SIP_BUDDIES).where(SIP_BUDDIES.NAME.eq(peer)).execute();

		cacheService.pbxServerList(getCompanyId()).forEach(e -> {
			try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
				pbxDsl.deleteFrom(SIP_BUDDIES).where(SIP_BUDDIES.NAME.eq(peer)).execute();
			}
		});
	}
}
