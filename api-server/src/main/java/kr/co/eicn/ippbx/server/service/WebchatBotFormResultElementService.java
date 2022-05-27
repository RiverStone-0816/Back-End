package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotFormresultElement;
import kr.co.eicn.ippbx.model.dto.eicn.WebchatBotInfoResponse;
import kr.co.eicn.ippbx.model.enums.ButtonAction;
import kr.co.eicn.ippbx.model.form.WebchatBotFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.WebchatBotFormResultElementRepository;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class WebchatBotFormResultElementService extends ApiBaseService {
    private final WebchatBotFormResultElementRepository webchatBotFormResultElementRepository;

    public Map<Integer, List<WebchatBotInfoResponse.AuthResultElement>> findAllByBlockIdList(List<Integer> blockIdList) {
        return webchatBotFormResultElementRepository.findAllByBLockIdList(blockIdList)
                .stream()
                .map(this::convertEntityToResponse)
                .collect(Collectors.groupingBy(WebchatBotInfoResponse.AuthResultElement::getBlockId));
    }

    private WebchatBotInfoResponse.AuthResultElement convertEntityToResponse(WebchatBotFormresultElement entity) {
        WebchatBotInfoResponse.AuthResultElement response = new WebchatBotInfoResponse.AuthResultElement();

        response.setId(entity.getId());
        response.setBlockId(entity.getBlockId());
        response.setValue(entity.getResultValue());
        response.setMent(entity.getResultMent());
        response.setAction(ButtonAction.of(entity.getAction()));

        if (ButtonAction.CONNECT_BEFORE_BLOCK.equals(ButtonAction.of(entity.getNextActionData())) || ButtonAction.CONNECT_FIRST_BLOCK.equals(ButtonAction.of(entity.getNextActionData())))
            response.setAction(ButtonAction.of(entity.getNextActionData()));

        response.setNextActionData(entity.getNextActionData());
        response.setNextApiMent(entity.getNextApiMent());
        response.setEnableResultTemplate("Y".equals(entity.getIsResultTplEnable()));
        response.setNextApiResultTemplate(entity.getNextApiResultTpl());
        response.setNextApiNoResultMent(entity.getNextApiNoResultMent());
        response.setNextApiErrorMent(entity.getNextApiErrorMent());

        return response;
    }

    public Integer insert(Integer blockId, WebchatBotFormRequest.AuthResultElement authResultElement) {
        WebchatBotFormRequest.AuthResultElement data = new WebchatBotFormRequest.AuthResultElement();

        ReflectionUtils.copy(data, authResultElement);

        if (ButtonAction.CONNECT_BLOCK.equals(authResultElement.getAction()) || ButtonAction.CONNECT_NEXT_BLOCK.equals(authResultElement.getAction())
                || ButtonAction.CONNECT_BEFORE_BLOCK.equals(authResultElement.getAction()) || ButtonAction.CONNECT_FIRST_BLOCK.equals(authResultElement.getAction())) {
            if (ButtonAction.CONNECT_BEFORE_BLOCK.equals(authResultElement.getAction()) || ButtonAction.CONNECT_FIRST_BLOCK.equals(authResultElement.getAction()))
                data.setNextActionData(authResultElement.getAction().getCode());
            else
                data.setNextActionData(String.valueOf(authResultElement.getNextActionData()));
            data.setAction(ButtonAction.CONNECT_BLOCK);
        }

        return webchatBotFormResultElementRepository.insert(blockId, data);
    }

    public void updateNextBlockId(Integer elementId, Integer blockId) {
        webchatBotFormResultElementRepository.updateBlockId(elementId, blockId);
    }

    public void deleteByBlockIdList(List<Integer> blockIdList) {
        webchatBotFormResultElementRepository.deleteByBlockIdList(blockIdList);
    }
}
