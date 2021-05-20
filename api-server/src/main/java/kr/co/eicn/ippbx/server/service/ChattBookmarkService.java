package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonChattBookmark;
import kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse;
import kr.co.eicn.ippbx.model.search.ChattingMemberSearchRequest;
import kr.co.eicn.ippbx.server.repository.customdb.ChattBookmarkRepository;
import kr.co.eicn.ippbx.server.repository.eicn.PersonListRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChattBookmarkService extends ApiBaseService implements ApplicationContextAware {
    protected final Logger logger = LoggerFactory.getLogger(ChattBookmarkService.class);
    private final Map<String, ChattBookmarkRepository> repositories = new HashMap<>();
    private ApplicationContext applicationContext;
    private final PersonListRepository personListRepository;

    public ChattBookmarkService(PersonListRepository personListRepository) {
        this.personListRepository = personListRepository;
    }

    public ChattBookmarkRepository getRepository() {
        return repositories.computeIfAbsent(g.getUser().getCompanyId(), companyId -> {
            final ChattBookmarkRepository repository = new ChattBookmarkRepository(companyId);
            applicationContext.getAutowireCapableBeanFactory().autowireBean(repository);
            return repository;
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public List<kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonChattBookmark> findAllByUserId() {
        return this.getRepository().findAllByUserId();
    }

    public List<PersonDetailResponse> getBookmarks(ChattingMemberSearchRequest search) {
        final List<CommonChattBookmark> bookmarks = findAllByUserId();
        if (bookmarks.size() > 0)
            return personListRepository.findAllByBookmarkMemberId(search, bookmarks).stream()
                    .map(e -> convertDto(e, PersonDetailResponse.class))
                    .collect(Collectors.toList());
        else
            return new ArrayList<>();
    }
}
