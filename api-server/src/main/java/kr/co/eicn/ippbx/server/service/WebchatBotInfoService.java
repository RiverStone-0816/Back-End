package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.model.dto.eicn.SummaryWebchatBotInfoResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WebchatBotInfoResponse;
import kr.co.eicn.ippbx.model.form.WebchatBotFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.WebchatBotInfoRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class WebchatBotInfoService extends ApiBaseService {
    private final Logger logger = LoggerFactory.getLogger(WebchatBotInfoService.class);

    private final WebchatBotInfoRepository webchatBotInfoRepository;

    public List<SummaryWebchatBotInfoResponse> getAllWebchatBotList() {
        return webchatBotInfoRepository.findAll().stream().map(e -> convertDto(e, SummaryWebchatBotInfoResponse.class)).collect(Collectors.toList());
    }

    public WebchatBotInfoResponse getById(Integer id) {
        return convertDto(webchatBotInfoRepository.findOneIfNullThrow(id), WebchatBotInfoResponse.class);
    }

    public Integer insert(WebchatBotFormRequest form) {
        return webchatBotInfoRepository.insert(form);
    }

    public void updateById(Integer id, WebchatBotFormRequest form) {
        webchatBotInfoRepository.updateById(id, form);
    }

    public void deleteById(Integer id) {
        webchatBotInfoRepository.delete(id);
    }

    public boolean isPresent(Integer id) {
        return webchatBotInfoRepository.findOneIfNullThrow(id) != null;
    }
}
