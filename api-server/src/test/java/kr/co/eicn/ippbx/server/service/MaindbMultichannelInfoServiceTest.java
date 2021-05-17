package kr.co.eicn.ippbx.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class MaindbMultichannelInfoServiceTest {

	@Autowired
	private MaindbMultichannelInfoService service;

}
