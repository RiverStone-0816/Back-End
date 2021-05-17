package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.CompanyInfo;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static kr.co.eicn.ippbx.server.jooq.eicn.tables.CompanyInfo.COMPANY_INFO;

@Getter
@Repository
public class CompanyInfoRepository extends EicnBaseRepository<CompanyInfo, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CompanyInfo, String> {
	protected final Logger logger = LoggerFactory.getLogger(CompanyInfoRepository.class);

	public CompanyInfoRepository() {
		super(COMPANY_INFO, COMPANY_INFO.COMPANY_ID, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CompanyInfo.class);
	}

	@Override
	public kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CompanyInfo findOneIfNullThrow(String key) {
		return super.findOneIfNullThrow(key);
	}

	public void updateTypeByContext (String uiType) {
		dsl.update(COMPANY_INFO)
				.set(COMPANY_INFO.ETC2, uiType)
				.where(compareCompanyId())
				.execute();
	}

	public String findServiceKey() {
		return super.findOne(compareCompanyId()).getEtc1();
	}

	public kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CompanyInfo findOneByCompanyId(final String companyId) {
		return dsl.selectFrom(COMPANY_INFO).where(COMPANY_INFO.COMPANY_ID.eq(companyId)).fetchOneInto(kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CompanyInfo.class);
	}
}