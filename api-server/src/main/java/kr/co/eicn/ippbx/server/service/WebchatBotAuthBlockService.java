package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatAuthBlock;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatAuthBtnElement;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatAuthDispElement;
import kr.co.eicn.ippbx.model.dto.eicn.WebchatBotAuthBlockInfoResponse;
import kr.co.eicn.ippbx.model.enums.AuthButtonAction;
import kr.co.eicn.ippbx.model.enums.AuthDisplayType;
import kr.co.eicn.ippbx.model.form.WebchatAuthBlocKFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.WebchatAuthBlockRepository;
import kr.co.eicn.ippbx.server.repository.eicn.WebchatAuthButtonElementRepository;
import kr.co.eicn.ippbx.server.repository.eicn.WebchatAuthDisplayElementRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class WebchatBotAuthBlockService extends ApiBaseService {
    private final WebchatAuthBlockRepository webchatAuthBlockRepository;
    private final WebchatAuthDisplayElementRepository webchatAuthDisplayElementRepository;
    private final WebchatAuthButtonElementRepository webchatAuthButtonElementRepository;

    public Integer insertAuthBlock(Integer botId, WebchatAuthBlocKFormRequest authBlock) {
        Integer blockId = webchatAuthBlockRepository.insert(botId, authBlock);

        webchatAuthButtonElementRepository.insert(blockId, authBlock.getButtons());
        webchatAuthDisplayElementRepository.insert(blockId, authBlock.getParams());

        return blockId;
    }

    public void updateAuthBlock(Integer authBlockId, WebchatAuthBlocKFormRequest authBlock) {
        webchatAuthBlockRepository.update(authBlockId, authBlock);

        deleteAuthElements(Collections.singleton(authBlockId));

        webchatAuthButtonElementRepository.insert(authBlockId, authBlock.getButtons());
        webchatAuthDisplayElementRepository.insert(authBlockId, authBlock.getParams());
    }

    public void deleteById(Integer authBlockId) {
        webchatAuthBlockRepository.delete(authBlockId);

        deleteAuthElements(Collections.singleton(authBlockId));
    }

    public void deleteAuthBlockByBotId(Integer botId) {
        Set<Integer> authBlockIdSet = webchatAuthBlockRepository.findAllDeleteBlockByBotId(botId).stream().map(WebchatAuthBlock::getId).collect(Collectors.toSet());
        webchatAuthBlockRepository.deleteByBotId(botId);
        deleteAuthElements(authBlockIdSet);
    }

    private void deleteAuthElements(Set<Integer> authBlockIdSet) {
        webchatAuthButtonElementRepository.deleteByBlockIdList(authBlockIdSet);
        webchatAuthDisplayElementRepository.deleteByBlockIdList(authBlockIdSet);
    }

    public List<WebchatBotAuthBlockInfoResponse> findAllByBotId(Integer botId) {
        List<WebchatAuthBlock> authBlockList = webchatAuthBlockRepository.findAllByBotId(botId);
        List<Integer> authBlockIdList = authBlockList.stream().map(WebchatAuthBlock::getId).collect(Collectors.toList());
        Map<Integer, List<WebchatAuthDispElement>> authDisplayByBlockIdMap = webchatAuthDisplayElementRepository.findAllByBlockIdList(authBlockIdList);
        Map<Integer, List<WebchatAuthBtnElement>> authButtonByBlockIdMap = webchatAuthButtonElementRepository.findAllByBlockIds(authBlockIdList);

        return authBlockList.stream().map(e -> {
            WebchatBotAuthBlockInfoResponse response = convertDto(e, WebchatBotAuthBlockInfoResponse.class);

            response.setUsingOtherBot("Y".equals(e.getOtherBotUseYn()));
            response.setParams(convertAuthDisplay(authDisplayByBlockIdMap.get(e.getId())));
            response.setButtons(convertAuthButton(authButtonByBlockIdMap.get(e.getId())));

            return response;
        }).collect(Collectors.toList());
    }

    private List<WebchatBotAuthBlockInfoResponse.AuthParamInfo> convertAuthDisplay(List<WebchatAuthDispElement> displayList) {
        if (displayList == null || displayList.size() == 0)
            return new ArrayList<>();
        return displayList.stream().map(e -> {
            WebchatBotAuthBlockInfoResponse.AuthParamInfo response = new WebchatBotAuthBlockInfoResponse.AuthParamInfo();

            response.setId(e.getId());
            response.setBlockId(e.getAuthBlockId());
            response.setSequence(e.getSequence());
            response.setType(AuthDisplayType.of(e.getInputType()));
            response.setTitle(e.getTitle());
            response.setName(e.getInputDisplayName());
            response.setParamName(e.getInputParamName());
            response.setNeedYn("Y".equals(e.getInputNeedYn()));

            return response;
        }).collect(Collectors.toList());
    }

    private List<WebchatBotAuthBlockInfoResponse.AuthButtonInfo> convertAuthButton(List<WebchatAuthBtnElement> buttonList) {
        if (buttonList == null || buttonList.size() == 0)
            return new ArrayList<>();
        return buttonList.stream().map(e -> {
            WebchatBotAuthBlockInfoResponse.AuthButtonInfo response = new WebchatBotAuthBlockInfoResponse.AuthButtonInfo();

            response.setId(e.getId());
            response.setBlockId(e.getAuthBlockId());
            response.setSequence(e.getSequence());
            response.setName(e.getBtnName());
            response.setAction(AuthButtonAction.of(e.getAction()));
            response.setActionData(e.getNextActionData());
            response.setResultParamName(e.getApiResultParamName());

            return response;
        }).collect(Collectors.toList());
    }
}
