package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.server.model.entity.customdb.ResultCustomInfoEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class ServiceTest {

	@Autowired
	private CustomDBResultCustomInfoService customDBResultCustomInfoService;
	@Autowired
	private EicnCdrService eicnCdrService;
	@Autowired
	private RecordFileService service;
	@Autowired
	private ProcessService processService;

//	@Test
	public void view() {
//		List<ResultCustomInfoEntity> resultCustomInfoEntities = customDBResultCustomInfoService.getRepository().viewResultCustomInfo();
//
//		for (ResultCustomInfoEntity resultCustomInfoEntity : resultCustomInfoEntities) {
//			log.info("data {}", resultCustomInfoEntity.toString());
//		}
	}

//	@Test
	public void process_execute() {
//		processService.execute("cmd.exe", "/c", "dir");
	}
}
