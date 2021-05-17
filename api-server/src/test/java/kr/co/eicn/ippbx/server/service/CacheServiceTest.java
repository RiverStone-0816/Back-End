package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.server.model.entity.eicn.CompanyServerEntity;
import kr.co.eicn.ippbx.server.model.entity.eicn.ServerInfoEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class CacheServiceTest {
	@Autowired
	private CacheService cacheService;
	private long startTime;
	private long endTime;

	@BeforeEach
	public void onBefore() {
		startTime = System.currentTimeMillis();
	}

	@AfterEach
	public void onAfter() {
		endTime = System.currentTimeMillis();
		log.info("소요시간: {}ms", endTime - startTime);
	}

	@Test
	public void test1() {
		cacheService.pbxServerList("premium");
	}

	@Test
	public void test2() {
		cacheService.pbxServerList("premium");
	}

	@Test
	public void test3() {
		cacheService.pbxServerList("premium");
	}

	@Test
	public void test4() {
		cacheService.refresh("premium");
		cacheService.pbxServerList("premium");
	}
}
