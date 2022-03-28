package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotBtnElement;
import kr.co.eicn.ippbx.model.dto.eicn.WebchatBotInfoResponse;
import kr.co.eicn.ippbx.model.enums.ButtonAction;
import kr.co.eicn.ippbx.model.form.WebchatBotButtonElementFormRequest;
import kr.co.eicn.ippbx.model.form.WebchatBotFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.WebchatBotButtonElementRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class WebchatBotButtonElementService extends ApiBaseService {
    private final Logger logger = LoggerFactory.getLogger(WebchatBotButtonElementService.class);

    private final WebchatBotButtonElementRepository webchatBotButtonElementRepository;

    public Integer insertButtonElement(Integer blockId, WebchatBotFormRequest.ButtonElement buttonElement) {
        WebchatBotButtonElementFormRequest data = new WebchatBotButtonElementFormRequest();

        data.setBlockId(blockId);
        data.setOrder(buttonElement.getOrder());
        data.setButtonName(buttonElement.getButtonName());
        data.setIsResultTemplateEnable(buttonElement.getIsResultTemplateEnable());
        data.setAction(buttonElement.getAction());

        if (ButtonAction.CONNECT_BLOCK.equals(buttonElement.getAction()) || ButtonAction.CONNECT_NEXT_BLOCK.equals(buttonElement.getAction())
                || ButtonAction.CONNECT_BEFORE_BLOCK.equals(buttonElement.getAction()) || ButtonAction.CONNECT_FIRST_BLOCK.equals(buttonElement.getAction())) {
            if (ButtonAction.CONNECT_BEFORE_BLOCK.equals(buttonElement.getAction()) || ButtonAction.CONNECT_FIRST_BLOCK.equals(buttonElement.getAction()))
                data.setActionData(buttonElement.getAction().getCode());
            else
                data.setActionData(String.valueOf(buttonElement.getNextBlockId()));
            data.setAction(ButtonAction.CONNECT_BLOCK);
        }
        else if (ButtonAction.CONNECT_MEMBER.equals(buttonElement.getAction()))
            data.setActionData(String.valueOf(buttonElement.getNextGroupId()));
        else if (ButtonAction.CONNECT_URL.equals(buttonElement.getAction()))
            data.setActionData(String.valueOf(buttonElement.getNextUrl()));
        else if (ButtonAction.CONNECT_PHONE.equals(buttonElement.getAction()))
            data.setActionData(String.valueOf(buttonElement.getNextPhone()));
        else if (ButtonAction.CONNECT_API.equals(buttonElement.getAction())) {
            data.setActionData(String.valueOf(buttonElement.getNextApiUrl()));
            data.setNextApiMent(buttonElement.getNextApiMent());
            data.setNextApiResultTemplate(buttonElement.getNextApiResultTemplate());
            data.setNextApiNoResultMent(buttonElement.getNextApiNoResultMent());
            data.setNextApiErrorMent(buttonElement.getNextApiErrorMent());
        }

        return webchatBotButtonElementRepository.insert(data);
    }

    public void updateNextBlockId(Integer buttonId, Integer nextBlockId) {
        webchatBotButtonElementRepository.updateNextBlockId(buttonId, nextBlockId);
    }

    public List<Integer> findIdListByBlockIdList(List<Integer> blockIdList) {
        return webchatBotButtonElementRepository.findButtonListByBlockIdList(blockIdList).stream().map(WebchatBotBtnElement::getId).collect(Collectors.toList());
    }

    public void deleteByBlockIdList(List<Integer> blockIdList) {
        webchatBotButtonElementRepository.deleteByBlockIdList(blockIdList);
    }

    public Integer convertStringToInteger(String value) {
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public WebchatBotInfoResponse.ButtonInfo convertEntityToResponse(WebchatBotBtnElement entity) {
        WebchatBotInfoResponse.ButtonInfo response = new WebchatBotInfoResponse.ButtonInfo();

        response.setId(entity.getId());
        response.setBlockId(entity.getBlockId());
        response.setOrder(entity.getSequence());
        response.setName(entity.getBtnName());
        response.setIsResultTemplateEnable("Y".equals(entity.getIsResultTplEnable()));
        response.setAction(ButtonAction.of(entity.getAction()));


        if (ButtonAction.CONNECT_BEFORE_BLOCK.equals(ButtonAction.of(entity.getNextActionData())) || ButtonAction.CONNECT_FIRST_BLOCK.equals(ButtonAction.of(entity.getNextActionData())))
            response.setAction(ButtonAction.of(entity.getNextActionData()));
        else if (ButtonAction.CONNECT_BLOCK.equals(ButtonAction.of(entity.getAction())) || ButtonAction.CONNECT_NEXT_BLOCK.equals(ButtonAction.of(entity.getAction())))
            response.setNextBlockId(convertStringToInteger(entity.getNextActionData()));
        else if (ButtonAction.CONNECT_MEMBER.equals(ButtonAction.of(entity.getAction())))
            response.setNextGroupId(convertStringToInteger(entity.getNextActionData()));
        else if (ButtonAction.CONNECT_URL.equals(ButtonAction.of(entity.getAction())))
            response.setNextUrl(entity.getNextActionData());
        else if (ButtonAction.CONNECT_PHONE.equals(ButtonAction.of(entity.getAction())))
            response.setNextPhone(entity.getNextActionData());
        else if (ButtonAction.CONNECT_API.equals(ButtonAction.of(entity.getAction()))) {
            response.setNextApiUrl(entity.getNextActionData());
            response.setNextApiMent(entity.getNextApiMent());
            response.setNextApiResultTemplate(entity.getNextApiResultTpl());
            response.setNextApiNoResultMent(entity.getNextApiNoResultMent());
            response.setNextApiErrorMent(entity.getNextApiErrorMent());
        }

        return response;
    }

    public Map<Integer, List<WebchatBotInfoResponse.ButtonInfo>> findButtonListByBlockId(List<Integer> blockIdList) {
        return webchatBotButtonElementRepository.findButtonListByBlockIdList(blockIdList).stream().map(this::convertEntityToResponse).collect(Collectors.groupingBy(WebchatBotInfoResponse.ButtonInfo::getBlockId));
    }

    public void setButtonList(WebchatBotInfoResponse.BlockInfo block, Map<Integer, List<WebchatBotInfoResponse.ButtonInfo>> buttonListByBlockIdMap) {
        if (buttonListByBlockIdMap.containsKey(block.getId())) {
            List<WebchatBotInfoResponse.ButtonInfo> buttonInfoList = buttonListByBlockIdMap.get(block.getId());

            block.setButtonList(buttonInfoList);
        } else {
            block.setButtonList(new ArrayList<>());
        }
    }
}
