package kr.co.eicn.ippbx.server;

import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class PBXServerConnectionTest {

	@Autowired
	private PBXServerInterface pbxServerInterface;

//	@Test
	public void connection() {
		DSLContext pbx_vip = pbxServerInterface.using("PBX_VIP");
	}
}
