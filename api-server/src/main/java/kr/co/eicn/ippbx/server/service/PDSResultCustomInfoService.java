package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.server.repository.pds.PDSResultCustomInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PDSResultCustomInfoService extends ApiBaseService implements ApplicationContextAware {
	protected final Logger logger = LoggerFactory.getLogger(PDSResultCustomInfoService.class);
	private final Map<String, PDSResultCustomInfoRepository> repositories = new HashMap<>();
	private ApplicationContext applicationContext;

	public PDSResultCustomInfoRepository getRepository(String executeId) {
		return repositories.computeIfAbsent(g.getUser().getCompanyId() + "_" + executeId, name -> {
			final PDSResultCustomInfoRepository repository = new PDSResultCustomInfoRepository(name);
			applicationContext.getAutowireCapableBeanFactory().autowireBean(repository);

			repository.createTableIfNotExists();

			return repository;
		});
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
}
