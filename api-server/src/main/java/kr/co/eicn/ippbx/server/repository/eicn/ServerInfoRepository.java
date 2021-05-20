package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.ServerInfo;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.ServerInfo.*;

@Getter
@Repository
public class ServerInfoRepository extends EicnBaseRepository<ServerInfo, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ServerInfo, String> {
	protected final Logger logger = LoggerFactory.getLogger(ServerInfoRepository.class);

	public ServerInfoRepository() {
		super(SERVER_INFO, SERVER_INFO.HOST, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ServerInfo.class);
	}
}
