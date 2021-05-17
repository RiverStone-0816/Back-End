package kr.co.eicn.ippbx.server.schedule;

import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class Scheduling {

	private final CacheService cacheService;
	private final StorageService fileSystemStorageService;
	@Value("${file.path.temporary.default}")
	private String tempPath;

	/**
	 *초,분,시,일,월,요일,년도(생략가능)
	 *	@Scheduled(cron = "0 1 0 * * *")
	 *
	 */
	@Scheduled(fixedRate = 1000 * 60 * 60 * 6) // 6시간 캐쉬 초기화
	public void evictAllCachesAtIntervals() {
		cacheService.evictAllCacheValues();
	}
}
