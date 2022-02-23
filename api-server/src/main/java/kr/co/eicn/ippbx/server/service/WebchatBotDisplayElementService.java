package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotDispElement;
import kr.co.eicn.ippbx.model.dto.eicn.WebchatBotInfoResponse;
import kr.co.eicn.ippbx.model.enums.DisplayElementInputType;
import kr.co.eicn.ippbx.model.form.WebchatBotDisplayElementFormRequest;
import kr.co.eicn.ippbx.model.form.WebchatBotFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.WebchatBotDisplayElementRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class WebchatBotDisplayElementService extends ApiBaseService {
    private final WebchatBotDisplayElementRepository webchatBotDisplayElementRepository;

    public void insertDisplayElement(Integer displayId, WebchatBotFormRequest.DisplayElement elementInfo) {
        if (elementInfo != null) {
            WebchatBotDisplayElementFormRequest data = new WebchatBotDisplayElementFormRequest();

            data.setDisplayId(displayId);
            data.setOrder(elementInfo.getOrder());
            data.setTitle(elementInfo.getTitle());
            data.setContent(elementInfo.getContent());
            data.setImage(elementInfo.getImage());
            data.setUrl(elementInfo.getUrl());
            data.setInputType(elementInfo.getInputType());
            data.setParamName(elementInfo.getParamName());
            data.setDisplayName(elementInfo.getDisplayName());

            webchatBotDisplayElementRepository.insert(data);
        } else
            log.info("Display" + displayId + "element is null");
    }

    public void deleteByDisplayIdList(List<Integer> displayIdList) {
        webchatBotDisplayElementRepository.deleteByDisplayIdList(displayIdList);
    }

    public WebchatBotInfoResponse.DisplayElement convertEntityToResponse(WebchatBotDispElement entity) {
        WebchatBotInfoResponse.DisplayElement response = new WebchatBotInfoResponse.DisplayElement();

        response.setId(entity.getId());
        response.setDisplayId(entity.getDisplayId());
        response.setOrder(entity.getSequence());
        response.setTitle(entity.getTitle());
        response.setContent(entity.getContent());
        response.setImage(entity.getImage());
        response.setUrl(entity.getUrl());
        response.setInputType(DisplayElementInputType.of(entity.getInputType()));
        response.setParamName(entity.getInputParamName());
        response.setDisplayName(entity.getInputDisplayName());

        return response;
    }

    public Map<Integer, List<WebchatBotInfoResponse.DisplayElement>> findDisplayElementByDisplayId(List<Integer> displayIdList) {
        return webchatBotDisplayElementRepository.findAllInDisplayIdList(displayIdList).stream().map(this::convertEntityToResponse).collect(Collectors.groupingBy(WebchatBotInfoResponse.DisplayElement::getDisplayId));
    }
}
