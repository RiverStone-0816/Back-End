package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.DaemonInfo;
import kr.co.eicn.ippbx.model.enums.DaemonType;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.DaemonInfo.*;

@Getter
@Repository
public class DaemonRepository extends EicnBaseRepository<DaemonInfo, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.DaemonInfo, String> {
	protected final Logger logger = LoggerFactory.getLogger(DaemonRepository.class);

	public DaemonRepository() {
		super(DAEMON_INFO, DAEMON_INFO.ID, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.DaemonInfo.class);
	}

	public String getDaemonName(String id) {
		return dsl.select(DAEMON_INFO.NAME)
				.from(DAEMON_INFO)
				.where(DAEMON_INFO.ID.eq(id))
				.fetchOneInto(String.class);
	}

	public Map<String, String> findAllNodeJSDaemon() {
		return findAll(DAEMON_INFO.TYPE.eq(DaemonType.NODEJS.getCode())).stream().collect(Collectors.toMap(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.DaemonInfo::getId, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.DaemonInfo::getSocketUrlIn));
	}
}
