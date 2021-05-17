package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.server.repository.customdb.CustomDBResultCustomInfoRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CustomDBResultCustomInfoService extends ApiBaseService implements ApplicationContextAware {
	protected final Logger logger = LoggerFactory.getLogger(CustomDBResultCustomInfoService.class);
	private final Map<String, CustomDBResultCustomInfoRepository> repositories = new HashMap<>();
	private ApplicationContext applicationContext;

	public CustomDBResultCustomInfoRepository getRepository() {
		if (g.getUser() == null || StringUtils.isEmpty(g.getUser().getCompanyId()))
			throw new IllegalStateException(message.getText("messages.company.notfound"));

		return repositories.computeIfAbsent(g.getUser().getCompanyId(), companyId -> {
			final CustomDBResultCustomInfoRepository repository = new CustomDBResultCustomInfoRepository(companyId);
			applicationContext.getAutowireCapableBeanFactory().autowireBean(repository);
			return repository;
		});
	}

	public CustomDBResultCustomInfoRepository getRepository(Integer executeId) {
		if (g.getUser() == null || StringUtils.isEmpty(g.getUser().getCompanyId()))
			throw new IllegalStateException(message.getText("messages.company.notfound"));

		return repositories.computeIfAbsent(g.getUser().getCompanyId() + "_" + executeId, name -> {
			final CustomDBResultCustomInfoRepository repository = new CustomDBResultCustomInfoRepository(name);
			applicationContext.getAutowireCapableBeanFactory().autowireBean(repository);
			return repository;
		});
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
}
