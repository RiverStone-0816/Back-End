package kr.co.eicn.ippbx.server.repository.pds;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.CurrentPdsResearchResult;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.CurrentPdsResearchResult.CURRENT_PDS_RESEARCH_RESULT;

@Getter
public class CurrentPDSResearchResultRepository extends PDSDbBaseRepository<CurrentPdsResearchResult, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CurrentPdsResearchResult, Integer> {
	protected final Logger logger = LoggerFactory.getLogger(CurrentPDSResearchResultRepository.class);

	public CurrentPDSResearchResultRepository() {
		super(CURRENT_PDS_RESEARCH_RESULT, CURRENT_PDS_RESEARCH_RESULT.SEQ, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CurrentPdsResearchResult.class);
	}
}
