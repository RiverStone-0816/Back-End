package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.CurrentEicnCdr;
import kr.co.eicn.ippbx.server.model.entity.eicn.CurrentEICNCdrEntity;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.eicn.ippbx.server.jooq.eicn.tables.CurrentEicnCdr.CURRENT_EICN_CDR;

@Getter
@Repository
public class CurrentEICNCdrRepository extends EicnBaseRepository<CurrentEicnCdr, CurrentEICNCdrEntity, String> {
	protected final Logger logger = LoggerFactory.getLogger(CurrentEICNCdrRepository.class);

	public CurrentEICNCdrRepository() {
		super(CURRENT_EICN_CDR, CURRENT_EICN_CDR.UNIQUEID, CurrentEICNCdrEntity.class);

		orderByFields.add(CURRENT_EICN_CDR.RING_DATE.desc());
	}

	public List<CurrentEICNCdrEntity> findAllLimit() {
		return dsl.select()
				.from(CURRENT_EICN_CDR)
				.where(compareCompanyId())
				.limit(10)
				.fetchInto(CurrentEICNCdrEntity.class);
	}
}
