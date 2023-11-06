package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.SipBuddies;
import kr.co.eicn.ippbx.model.enums.ShellCommand;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.IpccUrlConnection;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import kr.co.eicn.ippbx.server.service.ProcessService;
import lombok.Getter;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.SipBuddies.SIP_BUDDIES;

@Getter
@Repository
public class SipBuddiesRepository extends EicnBaseRepository<SipBuddies, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SipBuddies, Integer> {
	protected final Logger logger = LoggerFactory.getLogger(ServiceRepository.class);
	private final PBXServerInterface pbxServerInterface;
	private final CacheService cacheService;
	private final ProcessService processService;

	public SipBuddiesRepository(PBXServerInterface pbxServerInterface, CacheService cacheService, ProcessService processService) {
		super(SIP_BUDDIES, SIP_BUDDIES.ID, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SipBuddies.class);
		this.pbxServerInterface = pbxServerInterface;
		this.cacheService = cacheService;
		this.processService = processService;
	}

	public void deleteAndNameAllPbxServers(String peer) {
		dsl.deleteFrom(SIP_BUDDIES).where(SIP_BUDDIES.NAME.eq(peer)).execute();

		cacheService.pbxServerList(getCompanyId()).forEach(e -> {
			DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
			pbxDsl.deleteFrom(SIP_BUDDIES).where(SIP_BUDDIES.NAME.eq(peer)).execute();
		});
	}

	public void updateAndMD5SecretAllPbxServers(String peer,String secret) {
		dsl.update(SIP_BUDDIES).set(SIP_BUDDIES.MD5SECRET, DSL.md5(secret)).where(SIP_BUDDIES.NAME.eq(peer)).execute();
		if (cacheService.pbxServerList(getCompanyId()).size() > 0) {
			cacheService.pbxServerList(getCompanyId()).forEach(e -> {
				DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
				pbxDsl.update(SIP_BUDDIES).set(SIP_BUDDIES.MD5SECRET, DSL.md5(secret)).where(SIP_BUDDIES.NAME.eq(peer)).execute();
				IpccUrlConnection.execute("http://" + e.getServer().getIp() + "/ipcc/multichannel/remote/pickup_update.jsp", peer);
			});
		} else {
			processService.execute(ShellCommand.PICKUP_UPDATE, peer, " &");
		}
	}
}
