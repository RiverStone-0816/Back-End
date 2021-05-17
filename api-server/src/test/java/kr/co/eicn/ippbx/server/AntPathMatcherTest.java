package kr.co.eicn.ippbx.server;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.AntPathMatcher;

@Slf4j
@SpringBootTest
public class AntPathMatcherTest {
	@Test
	public void pattern_check() {
		final AntPathMatcher matcher = new AntPathMatcher();
		log.info("match? {}", matcher.match("/**/api/**/*resource", "/api/v1/admin/user/1/resource-1111"));
		log.info("match? {}", matcher.match("/**/api/**/*resource", "/api-server/api/v1/admin/help/notice/8/specific-file-resource"));
	}
}
