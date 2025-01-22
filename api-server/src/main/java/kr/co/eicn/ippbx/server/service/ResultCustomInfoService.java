package kr.co.eicn.ippbx.server.service;

import java.util.HashMap;
import java.util.Map;
import kr.co.eicn.ippbx.model.dto.customdb.ResultCustomInfoFromResponse;
import kr.co.eicn.ippbx.server.repository.customdb.ResultCustomInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class ResultCustomInfoService extends ApiBaseService implements ApplicationContextAware {
  protected final Logger logger = LoggerFactory.getLogger(ResultCustomInfoService.class);
  private final Map<String, ResultCustomInfoRepository> repositories = new HashMap<>();
  private ApplicationContext applicationContext;

  public ResultCustomInfoRepository getRepository() {
    return repositories.computeIfAbsent(g.getUser().getCompanyId(), companyId -> {
      final ResultCustomInfoRepository repository = new ResultCustomInfoRepository(companyId);
      applicationContext.getAutowireCapableBeanFactory().autowireBean(repository);
      return repository;
    });
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  public ResultCustomInfoFromResponse getCombinedResultCustomInfoBySeq(Integer seq) {
    return getRepository().getCombinedResultCustomInfoBySeq(seq);
  }
}
