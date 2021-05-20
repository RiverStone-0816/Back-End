package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.RouteApplication;
import kr.co.eicn.ippbx.server.repository.eicn.RouteApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RouteApplicationService extends ApiBaseService {
    private final RouteApplicationRepository repository;

    public RouteApplication findOneIfNullThrow(Integer seq) {
        return repository.findOneIfNullThrow(seq);
    }
}
