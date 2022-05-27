package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.model.dto.eicn.WtalkServiceInfoResponse;
import kr.co.eicn.ippbx.model.entity.eicn.WtalkScheduleInfoEntity;
import kr.co.eicn.ippbx.model.enums.TalkChannelType;
import kr.co.eicn.ippbx.model.search.TalkServiceInfoSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.WebchatServiceInfoRepository;
import kr.co.eicn.ippbx.server.repository.eicn.WtalkScheduleInfoRepository;
import kr.co.eicn.ippbx.server.repository.eicn.WtalkServiceInfoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Service
public class WtalkScheduleService extends ApiBaseService {
    private final WtalkServiceInfoRepository talkServiceInfoRepository;
    private final WtalkScheduleInfoRepository talkScheduleInfoRepository;
    private final WebchatServiceInfoRepository webchatServiceInfoRepository;

    public List<WtalkServiceInfoResponse> list(TalkServiceInfoSearchRequest search) {
        final List<WtalkServiceInfoResponse> response = new ArrayList<>();
        final Map<String, List<WtalkScheduleInfoEntity>> talkScheduleInfoMap = talkScheduleInfoRepository.getTalkServiceInfoLists(search);

        talkServiceInfoRepository.findAll().forEach(e -> {
            WtalkServiceInfoResponse row = convertDto(e, WtalkServiceInfoResponse.class);

            row.setChannelType(TalkChannelType.KAKAO);
            row.setScheduleInfos(talkScheduleInfoMap.get(row.getSenderKey()));

            if (row.getScheduleInfos() != null && row.getScheduleInfos().size() > 0)
                response.add(row);
        });

        webchatServiceInfoRepository.findAll().forEach(e -> {
            WtalkServiceInfoResponse row = convertDto(e, WtalkServiceInfoResponse.class);

            row.setChannelType(TalkChannelType.EICN);
            row.setKakaoServiceName(e.getWebchatServiceName());
            row.setScheduleInfos(talkScheduleInfoMap.get(row.getSenderKey()));

            if (row.getScheduleInfos() != null && row.getScheduleInfos().size() > 0)
                response.add(row);
        });

        return response;
    }
}
