package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.ServiceCode;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static kr.co.eicn.ippbx.server.jooq.eicn.tables.ServiceCode.SERVICE_CODE;

@Getter
@Repository
public class ServiceCodeRepository extends EicnBaseRepository<ServiceCode, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ServiceCode, String> {
	private final Logger logger = LoggerFactory.getLogger(MohListRepository.class);

	public ServiceCodeRepository() {
		super(SERVICE_CODE, SERVICE_CODE.CODE, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ServiceCode.class);

		orderByFields.add(SERVICE_CODE.GROUP_NAME.asc());
		orderByFields.add(SERVICE_CODE.SEQUENCE.asc());
	}
}
