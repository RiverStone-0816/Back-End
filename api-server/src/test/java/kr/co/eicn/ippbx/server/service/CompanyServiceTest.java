package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.server.model.entity.eicn.CompanyLicenceEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class CompanyServiceTest {

	@Autowired
	private CompanyService service;

//	@Test
	public void licenceinfo() {
		final CompanyLicenceEntity companyLicenceInfo = service.getCompanyLicenceInfo();

		log.info("licence {}", companyLicenceInfo.toString());
	}
}
