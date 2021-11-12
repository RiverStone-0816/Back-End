package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.model.form.WebchatBotButtonElementFormRequest;
import kr.co.eicn.ippbx.model.form.WebchatBotFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.WebchatBotButtonElementRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class WebchatBotButtonElementService extends ApiBaseService {
    private final Logger logger = LoggerFactory.getLogger(WebchatBotButtonElementService.class);

    private final WebchatBotButtonElementRepository webchatBotButtonElementRepository;

    public void insertButtonElement(Integer blockId, Integer buttonId, WebchatBotFormRequest.ButtonElement buttonElement) {
        WebchatBotButtonElementFormRequest data = new WebchatBotButtonElementFormRequest();

        data.setButtonId(buttonId);
        data.setBlockId(blockId);
        data.setOrder(buttonElement.getOrder());
        data.setButtonName(buttonElement.getButtonName());
        data.setAction(buttonElement.getAction());
        data.setNextBlockId(buttonElement.getNextBlockId());
        data.setNextGroupId(buttonElement.getNextGroupId());
        data.setNextUrl(buttonElement.getNextUrl());
        data.setNextPhone(buttonElement.getNextPhone());
        data.setNextApiUrl(buttonElement.getNextApiUrl());
        data.setNextApiMent(buttonElement.getNextApiMent());
        data.setIsResultTemplateEnable(buttonElement.getIsResultTemplateEnable());
        data.setNextApiResultTemplate(buttonElement.getNextApiResultTemplate());
        data.setNextApiNoResultMent(buttonElement.getNextApiNoResultMent());
        data.setNextApiErrorMent(buttonElement.getNextApiErrorMent());

        webchatBotButtonElementRepository.insert(data);
    }

    public List<Integer> findIdListByBlockIdList(List<Integer> blockIdList) {
        return webchatBotButtonElementRepository.findIdListByBlockIdList(blockIdList);
    }

    public void deleteByBlockIdList(List<Integer> blockIdList) {
        webchatBotButtonElementRepository.deleteByBlockIdList(blockIdList);
    }
}
