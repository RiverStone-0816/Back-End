package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.DashboardViewList;
import kr.co.eicn.ippbx.model.form.DashboardViewListFormRequest;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.DashboardViewList.DASHBOARD_VIEW_LIST;

@Getter
@Repository
public class DashboardViewListRepository extends EicnBaseRepository<DashboardViewList, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.DashboardViewList, Integer> {
	protected final Logger logger = LoggerFactory.getLogger(DashboardViewListRepository.class);

	public DashboardViewListRepository() {
		super(DASHBOARD_VIEW_LIST, DASHBOARD_VIEW_LIST.SEQ, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.DashboardViewList.class);
	}

	public void updateByKeySeqAndComapnyId(DashboardViewListFormRequest form, Integer seq) {

		dsl.update(DASHBOARD_VIEW_LIST)
				.set(DASHBOARD_VIEW_LIST.DASHBOARD_INFO_ID, form.getDashboardInfoId())
				.where(DASHBOARD_VIEW_LIST.COMPANY_ID.eq(getCompanyId()))
				.and(DASHBOARD_VIEW_LIST.SEQ.eq(seq))
				.execute();
	}

	public void deleteByKeySeqAndComapnyId(Integer seq) {
		dsl.delete(DASHBOARD_VIEW_LIST)
				.where(DASHBOARD_VIEW_LIST.COMPANY_ID.eq(getCompanyId()))
				.and(DASHBOARD_VIEW_LIST.SEQ.eq(seq))
				.execute();
	}

}
