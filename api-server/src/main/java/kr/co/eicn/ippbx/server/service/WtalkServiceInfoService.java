package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.model.dto.eicn.search.SearchTalkServiceInfoResponse;
import kr.co.eicn.ippbx.server.repository.eicn.WtalkServiceInfoRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
@Service
public class WtalkServiceInfoService extends ApiBaseService {
    private final Logger logger = LoggerFactory.getLogger(WtalkServiceInfoService.class);

    private final WtalkServiceInfoRepository talkServiceInfoRepository;

    public List<SearchTalkServiceInfoResponse> getAllTalkServiceList() {
        return talkServiceInfoRepository.findAll().stream()
                .map(talkServiceInfo -> convertDto(talkServiceInfo, SearchTalkServiceInfoResponse.class))
                .collect(Collectors.toList());
    }
}
