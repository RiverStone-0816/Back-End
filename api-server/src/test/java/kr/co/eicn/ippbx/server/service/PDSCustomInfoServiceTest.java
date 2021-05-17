package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.server.repository.pds.PDSCustomInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class PDSCustomInfoServiceTest {
	@Autowired
	private PDSCustomInfoService service;

//	@Test
	public void test() {
		PDSCustomInfoRepository primium_2 = service.getRepository(2);
	}

//	@Test
	public void table_create() {
		service.getRepository(2).createTableIfNotExists();
	}
}
