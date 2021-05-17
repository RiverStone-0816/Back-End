package kr.co.eicn.ippbx.server;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class RegexTests {
//	@Test
	public void regex() {
		String encKey = "1234";
		log.info(encKey.replaceAll("(?<=.{3}).", "*"));
	}
}
