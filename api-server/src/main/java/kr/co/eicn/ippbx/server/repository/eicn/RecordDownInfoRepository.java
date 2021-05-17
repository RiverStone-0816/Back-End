package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.RecordDownInfo;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static kr.co.eicn.ippbx.server.jooq.eicn.tables.RecordDownInfo.RECORD_DOWN_INFO;

@Getter
@Repository
public class RecordDownInfoRepository extends EicnBaseRepository<RecordDownInfo, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.RecordDownInfo, Integer> {
	protected final Logger logger = LoggerFactory.getLogger(RecordEncRepository.class);

	public RecordDownInfoRepository() {
		super(RECORD_DOWN_INFO, RECORD_DOWN_INFO.SEQ, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.RecordDownInfo.class);
	}

	public void deleteByParent(final Integer seq) {
		delete(RECORD_DOWN_INFO.PARENT.eq(seq));
	}
}
