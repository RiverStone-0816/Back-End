package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.server.repository.pds.PDSCustomInfoRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PDSCustomInfoService extends ApiBaseService implements ApplicationContextAware {
	protected final Logger logger = LoggerFactory.getLogger(PDSCustomInfoService.class);
	private final Map<String, PDSCustomInfoRepository> repositories = new HashMap<>();
	private ApplicationContext applicationContext;

	public PDSCustomInfoRepository getRepository(Integer groupId) {
		if (g.getUser() == null || StringUtils.isEmpty(g.getUser().getCompanyId()))
			throw new IllegalStateException(message.getText("messages.company.notfound"));

		return repositories.computeIfAbsent(g.getUser().getCompanyId() + "_" + groupId, name -> {
			final PDSCustomInfoRepository repository = new PDSCustomInfoRepository(name);
			applicationContext.getAutowireCapableBeanFactory().autowireBean(repository);
			return repository;
		});
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
}
