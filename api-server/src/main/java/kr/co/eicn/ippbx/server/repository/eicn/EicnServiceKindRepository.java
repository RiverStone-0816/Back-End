package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.EicnServiceKind;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static kr.co.eicn.ippbx.server.jooq.eicn.tables.EicnServiceKind.EICN_SERVICE_KIND;

@Getter
@Repository
public class EicnServiceKindRepository extends EicnBaseRepository<EicnServiceKind, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.EicnServiceKind, String> {
	protected final Logger logger = LoggerFactory.getLogger(EicnServiceKindRepository.class);

	public EicnServiceKindRepository() {
		super(EICN_SERVICE_KIND, EICN_SERVICE_KIND.SERVICE_KIND, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.EicnServiceKind.class);
	}
}

