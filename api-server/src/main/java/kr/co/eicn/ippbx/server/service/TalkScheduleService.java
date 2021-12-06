package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.model.dto.eicn.TalkServiceInfoResponse;
import kr.co.eicn.ippbx.model.entity.eicn.TalkScheduleInfoEntity;
import kr.co.eicn.ippbx.model.enums.TalkChannelType;
import kr.co.eicn.ippbx.model.search.TalkServiceInfoSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.TalkScheduleInfoRepository;
import kr.co.eicn.ippbx.server.repository.eicn.TalkServiceInfoRepository;
import kr.co.eicn.ippbx.server.repository.eicn.WebchatServiceInfoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Service
public class TalkScheduleService extends ApiBaseService {
    private final TalkServiceInfoRepository talkServiceInfoRepository;
    private final TalkScheduleInfoRepository talkScheduleInfoRepository;
    private final WebchatServiceInfoRepository webchatServiceInfoRepository;

    public List<TalkServiceInfoResponse> list(TalkServiceInfoSearchRequest search) {
        final List<TalkServiceInfoResponse> response = new ArrayList<>();
        final Map<String, List<TalkScheduleInfoEntity>> talkScheduleInfoMap = talkScheduleInfoRepository.getTalkServiceInfoLists(search);

        talkServiceInfoRepository.findAll().forEach(e -> {
            TalkServiceInfoResponse row = convertDto(e, TalkServiceInfoResponse.class);

            row.setChannelType(TalkChannelType.KAKAO);
            row.setScheduleInfos(talkScheduleInfoMap.get(row.getSenderKey()));

            if (row.getScheduleInfos() != null && row.getScheduleInfos().size() > 0)
                response.add(row);
        });

        webchatServiceInfoRepository.findAll().forEach(e -> {
            TalkServiceInfoResponse row = convertDto(e, TalkServiceInfoResponse.class);

            row.setChannelType(TalkChannelType.EICN);
            row.setKakaoServiceName(e.getWebchatServiceName());
            row.setScheduleInfos(talkScheduleInfoMap.get(row.getSenderKey()));

            if (row.getScheduleInfos() != null && row.getScheduleInfos().size() > 0)
                response.add(row);
        });

        return response;
    }
}
