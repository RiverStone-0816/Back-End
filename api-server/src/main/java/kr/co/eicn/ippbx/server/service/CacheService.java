package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.model.entity.eicn.CompanyServerEntity;
import kr.co.eicn.ippbx.server.repository.eicn.CompanyServerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CacheService {

	private final CacheManager cacheManager;
	private final CompanyServerRepository companyServerRepository;

	@Cacheable(value = "company-server", key = "#companyId")
	public List<CompanyServerEntity> getCompanyServerList(String companyId) {
		return companyServerRepository.findAllCompanyId(companyId);
	}

	@Cacheable(value = "company-server-pbx", key = "#companyId")
	public List<CompanyServerEntity> pbxServerList(String companyId) {
		return getCompanyServerList(companyId).stream()
				.filter(e -> StringUtils.isNotEmpty(e.getHost()))
				.filter(e -> "P".equals(e.getType()))
				.filter(e -> !"localhost".equals(e.getHost()) && !"null".equals(e.getHost()))
				.collect(Collectors.toList());
	}

	@CacheEvict(value = "company-server-pbx", key = "#companyId")
	public void refresh(String companyId) {
		log.info("cache clear -> {}", companyId);
	}

	public void evictAllCacheValues() {
		cacheManager.getCacheNames().forEach(cacheName -> cacheManager.getCache(cacheName).clear());
	}
}
