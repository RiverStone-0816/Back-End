package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.EicnMonitData;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.EICN_MONIT_DATA;

@Getter
@Repository
public class EicnMonitDataRepository extends EicnBaseRepository<EicnMonitData, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EicnMonitData, Integer> {
	protected final Logger logger = LoggerFactory.getLogger(EicnMonitDataRepository.class);

	public EicnMonitDataRepository() {
		super(EICN_MONIT_DATA, EICN_MONIT_DATA.SEQ, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EicnMonitData.class);
	}

	public List<String> getMonitData(String host) {

//		final List<String> serverData = eicnMonitDataRepository.findAll(EICN_MONIT_DATA.HOST.eq(host)).stream()
////				.filter(e -> e.getHost().equals(host))
//				.map(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EicnMonitData::getData)
//				.collect(Collectors.toList());

		//		DashCurrentResultCallResponse response = new DashCurrentResultCallResponse();
//		cacheService.pbxServerList(getCompanyId())
//				.forEach(e -> {
//					DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
//					DashCurrentResultCallResponse res = pbxDsl.select(DSL.count(DSL.when(CURRENT_MEMBER_STATUS.IN_OUT.eq("I"), 1)).as("in_calling_cnt"))
//							.select(DSL.count(DSL.when(CURRENT_MEMBER_STATUS.IN_OUT.eq("O"), 1)).as("out_calling_cnt"))
//							.from(CURRENT_MEMBER_STATUS)
//							.where(compareCompanyId())
//							.and(CURRENT_MEMBER_STATUS.STATUS.eq(CURRENT_MEMBER_STATUS.NEXT_STATUS))
//							.and(CURRENT_MEMBER_STATUS.STATUS.eq("1"))
//							.fetchOneInto(DashCurrentResultCallResponse.class);
//					response.setOutCallingCnt(response.getOutCallingCnt() + res.getOutCallingCnt());
//					response.setInCallingCnt(response.getInCallingCnt() + res.getInCallingCnt());
//				});

		return dsl.select(EICN_MONIT_DATA.DATA)
				.from(EICN_MONIT_DATA)
				.where(EICN_MONIT_DATA.HOST.eq(host)).fetch(EICN_MONIT_DATA.DATA);
	}
}
