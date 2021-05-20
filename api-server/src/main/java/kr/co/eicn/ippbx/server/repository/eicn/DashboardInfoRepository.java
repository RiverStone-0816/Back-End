package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.enums.DashboardInfoDashboardType;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.DashboardInfo;
import kr.co.eicn.ippbx.model.dto.eicn.DashListResponse;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.DashboardInfo.DASHBOARD_INFO;

@Getter
@Repository
public class DashboardInfoRepository extends EicnBaseRepository<DashboardInfo, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.DashboardInfo, Integer> {
	protected final Logger logger = LoggerFactory.getLogger(DashboardInfoRepository.class);

	public DashboardInfoRepository() {
		super(DASHBOARD_INFO, DASHBOARD_INFO.DASHBOARD_ID, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.DashboardInfo.class);
	}

	public void insert(String name, DashboardInfoDashboardType type, String value, Integer seq) {
		dsl.insertInto(DASHBOARD_INFO)
				.set(DASHBOARD_INFO.DASHBOARD_NAME, name)
				.set(DASHBOARD_INFO.DASHBOARD_TYPE, type)
				.set(DASHBOARD_INFO.DASHBOARD_VALUE, value)
				.set(DASHBOARD_INFO.DASHBOARD_UI_SEQ, seq)
				.set(DASHBOARD_INFO.COMPANY_ID, getCompanyId())
				.execute();
	}

	public void updateByValue(String name, String value, String oldValue) {
		dsl.update(DASHBOARD_INFO)
				.set(DASHBOARD_INFO.DASHBOARD_VALUE, value)
				.set(DASHBOARD_INFO.DASHBOARD_NAME, name)
				.where(compareCompanyId())
				.and(DASHBOARD_INFO.DASHBOARD_VALUE.eq(oldValue))
				.execute();
	}

	public void deleteByValue(String value) {
		dsl.delete(DASHBOARD_INFO)
				.where(compareCompanyId())
				.and(DASHBOARD_INFO.DASHBOARD_VALUE.eq(value))
				.execute();
	}

	public Optional<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.DashboardInfo> findOneByValue(String value) {
		return dsl.select().from(DASHBOARD_INFO).where(compareCompanyId()).and(DASHBOARD_INFO.DASHBOARD_VALUE.eq(value))
				.fetchOptional(record -> record.into(DASHBOARD_INFO).into(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.DashboardInfo.class));
	}

	public List<DashListResponse> getDashboardList(boolean isCloud) {
		return dsl.select()
				.from(DASHBOARD_INFO)
				.where(DASHBOARD_INFO.COMPANY_ID.eq(getCompanyId()))
				.or(DASHBOARD_INFO.DASHBOARD_TYPE.eq(DashboardInfoDashboardType.server_monitor).and(DASHBOARD_INFO.COMPANY_ID.eq(isCloud ? "cloud" : "")))
				.fetchInto(DashListResponse.class);
	}
}
