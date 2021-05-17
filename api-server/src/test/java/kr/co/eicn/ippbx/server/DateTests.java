package kr.co.eicn.ippbx.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@Slf4j
@SpringBootTest
public class DateTests {
//	@Test
	public void startDateIsAfterEndDate() {
		String startDate = LocalDate.now().toString();
		String endDate = LocalDate.now().toString();
		if (LocalDate.parse(startDate).isAfter(LocalDate.parse(endDate))) {
			log.info("true");
		} else {
			log.info("false");
		}
	}
}
