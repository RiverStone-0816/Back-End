package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.model.form.WebchatBotDisplayElementFormRequest;
import kr.co.eicn.ippbx.model.form.WebchatBotFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.WebchatBotDisplayElementRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class WebchatBotDisplayElementService extends ApiBaseService {
    private final Logger logger = LoggerFactory.getLogger(WebchatBotDisplayElementService.class);

    private final WebchatBotDisplayElementRepository webchatBotDisplayElementRepository;

    public void insertDisplayElement(Integer displayId, WebchatBotFormRequest.DisplayElement elementInfo) {
        WebchatBotDisplayElementFormRequest data = new WebchatBotDisplayElementFormRequest();

        data.setDisplayId(displayId);
        data.setOrder(elementInfo.getOrder());
        data.setTitle(elementInfo.getTitle());
        data.setContent(elementInfo.getContent());
        data.setImage(elementInfo.getImage());
        data.setUrl(elementInfo.getUrl());

        webchatBotDisplayElementRepository.insert(data);
    }

    public void deleteByDisplayIdList(List<Integer> displayIdList) {
        webchatBotDisplayElementRepository.deleteByDisplayIdList(displayIdList);
    }
}
