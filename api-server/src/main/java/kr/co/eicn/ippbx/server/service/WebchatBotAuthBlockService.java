package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatAuthBlock;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatAuthBtnElement;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatAuthDispElement;
import kr.co.eicn.ippbx.model.dto.eicn.WebchatBotInfoResponse;
import kr.co.eicn.ippbx.model.enums.AuthButtonAction;
import kr.co.eicn.ippbx.model.enums.AuthDisplayType;
import kr.co.eicn.ippbx.model.form.WebchatBotFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.WebchatAuthBlockRepository;
import kr.co.eicn.ippbx.server.repository.eicn.WebchatAuthButtonElementRepository;
import kr.co.eicn.ippbx.server.repository.eicn.WebchatAuthDisplayElementRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class WebchatBotAuthBlockService extends ApiBaseService {
    private final WebchatAuthBlockRepository webchatAuthBlockRepository;
    private final WebchatAuthDisplayElementRepository webchatAuthDisplayElementRepository;
    private final WebchatAuthButtonElementRepository webchatAuthButtonElementRepository;

    public Map<Integer, Integer> insertAuthBlock(Integer botId, List<WebchatBotFormRequest.AuthBlockInfo> authBlockList) {
        Map<Integer, Integer> authIdMap = new HashMap<>();
        for (WebchatBotFormRequest.AuthBlockInfo authBlock : authBlockList) {
            Integer blockId = webchatAuthBlockRepository.insert(botId, authBlock);

            webchatAuthButtonElementRepository.insert(blockId, authBlock.getButtons());
            webchatAuthDisplayElementRepository.insert(blockId, authBlock.getParams());

            authIdMap.put(authBlock.getId(), blockId);
        }

        return authIdMap;
    }

    public void deleteAuthBlockByBotId(Integer botId) {
        List<Integer> authBlockIdList = webchatAuthBlockRepository.findAllByBotId(botId).stream().map(WebchatAuthBlock::getId).collect(Collectors.toList());
        webchatAuthBlockRepository.deleteByBotId(botId);
        webchatAuthButtonElementRepository.deleteByBlockIdList(authBlockIdList);
        webchatAuthDisplayElementRepository.deleteByBlockIdList(authBlockIdList);
    }

    public List<WebchatBotInfoResponse.AuthBlockInfo> findAllByBotId(Integer botId) {
        List<WebchatAuthBlock> allByBotId = webchatAuthBlockRepository.findAllByBotId(botId);
        List<Integer> authBlockIdList = allByBotId.stream().map(WebchatAuthBlock::getId).collect(Collectors.toList());
        Map<Integer, List<WebchatAuthDispElement>> authDisplayByBlockIdMap = webchatAuthDisplayElementRepository.findAllByBlockIdList(authBlockIdList);
        Map<Integer, List<WebchatAuthBtnElement>> authButtonByBlockIdMap = webchatAuthButtonElementRepository.findAllByBlockIds(authBlockIdList);

        return allByBotId.stream().map(e -> {
            WebchatBotInfoResponse.AuthBlockInfo response = convertDto(e, WebchatBotInfoResponse.AuthBlockInfo.class);

            response.setParams(convertAuthDisplay(authDisplayByBlockIdMap.get(e.getId())));
            response.setButtons(convertAuthButton(authButtonByBlockIdMap.get(e.getId())));

            return response;
        }).collect(Collectors.toList());
    }

    private List<WebchatBotInfoResponse.AuthParamInfo> convertAuthDisplay(List<WebchatAuthDispElement> displayList) {
        return displayList.stream().map(e -> {
            WebchatBotInfoResponse.AuthParamInfo response = new WebchatBotInfoResponse.AuthParamInfo();

            response.setId(e.getId());
            response.setBlockId(e.getAuthBlockId());
            response.setSequence(e.getSequence());
            response.setType(AuthDisplayType.of(e.getInputType()));
            response.setName(e.getInputDisplayName());
            response.setParamName(e.getInputParamName());
            response.setNeedYn("Y".equals(e.getInputNeedYn()));

            return response;
        }).collect(Collectors.toList());
    }

    private List<WebchatBotInfoResponse.AuthButtonInfo> convertAuthButton(List<WebchatAuthBtnElement> buttonList) {
        return buttonList.stream().map(e -> {
            WebchatBotInfoResponse.AuthButtonInfo response = new WebchatBotInfoResponse.AuthButtonInfo();

            response.setId(e.getId());
            response.setBlockId(e.getAuthBlockId());
            response.setSequence(e.getSequence());
            response.setName(e.getBtnName());
            response.setAction(AuthButtonAction.of(e.getAction()));
            response.setActionData(e.getNextActionData());

            return response;
        }).collect(Collectors.toList());
    }
}
