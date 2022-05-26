package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatFormBtnElement;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatFormDispElement;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatFormBlock;
import kr.co.eicn.ippbx.model.dto.eicn.WebchatBotFormBlockInfoResponse;
import kr.co.eicn.ippbx.model.enums.AuthButtonAction;
import kr.co.eicn.ippbx.model.enums.AuthDisplayType;
import kr.co.eicn.ippbx.model.form.WebchatFormBlocKFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.WebchatFormBlockRepository;
import kr.co.eicn.ippbx.server.repository.eicn.WebchatFormButtonElementRepository;
import kr.co.eicn.ippbx.server.repository.eicn.WebchatFormDisplayElementRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class WebchatBotFormBlockService extends ApiBaseService {
    private final WebchatFormBlockRepository webchatAuthBlockRepository;
    private final WebchatFormDisplayElementRepository webchatAuthDisplayElementRepository;
    private final WebchatFormButtonElementRepository webchatAuthButtonElementRepository;

    public Integer insertAuthBlock(WebchatFormBlocKFormRequest authBlock) {
        Integer blockId = webchatAuthBlockRepository.insert(authBlock);

        webchatAuthButtonElementRepository.insert(blockId, authBlock.getButtons());
        webchatAuthDisplayElementRepository.insert(blockId, authBlock.getParams());

        return blockId;
    }

    public void updateAuthBlock(Integer formBlockId, WebchatFormBlocKFormRequest authBlock) {
        webchatAuthBlockRepository.update(formBlockId, authBlock);

        deleteAuthElements(Collections.singleton(formBlockId));

        webchatAuthButtonElementRepository.insert(formBlockId, authBlock.getButtons());
        webchatAuthDisplayElementRepository.insert(formBlockId, authBlock.getParams());
    }

    public void deleteById(Integer formBlockId) {
        webchatAuthBlockRepository.delete(formBlockId);

        deleteAuthElements(Collections.singleton(formBlockId));
    }

    private void deleteAuthElements(Set<Integer> formBlockIdSet) {
        webchatAuthButtonElementRepository.deleteByBlockIdList(formBlockIdSet);
        webchatAuthDisplayElementRepository.deleteByBlockIdList(formBlockIdSet);
    }

    public List<WebchatBotFormBlockInfoResponse> getAll() {
        List<WebchatFormBlock> authBlockList = webchatAuthBlockRepository.findAll();
        List<Integer> formBlockIdList = authBlockList.stream().map(WebchatFormBlock::getId).collect(Collectors.toList());
        Map<Integer, List<WebchatFormDispElement>> authDisplayByBlockIdMap = webchatAuthDisplayElementRepository.findAllByBlockIdList(formBlockIdList);
        Map<Integer, List<WebchatFormBtnElement>> authButtonByBlockIdMap = webchatAuthButtonElementRepository.findAllByBlockIds(formBlockIdList);

        return authBlockList.stream().map(e -> {
            WebchatBotFormBlockInfoResponse response = convertDto(e, WebchatBotFormBlockInfoResponse.class);

            response.setParams(convertAuthDisplay(authDisplayByBlockIdMap.get(e.getId())));
            response.setButtons(convertAuthButton(authButtonByBlockIdMap.get(e.getId())));

            return response;
        }).collect(Collectors.toList());
    }

    private List<WebchatBotFormBlockInfoResponse.AuthParamInfo> convertAuthDisplay(List<WebchatFormDispElement> displayList) {
        if (displayList == null || displayList.size() == 0)
            return new ArrayList<>();
        return displayList.stream().map(e -> {
            WebchatBotFormBlockInfoResponse.AuthParamInfo response = new WebchatBotFormBlockInfoResponse.AuthParamInfo();

            response.setId(e.getId());
            response.setBlockId(e.getFormBlockId());
            response.setSequence(e.getSequence());
            response.setType(AuthDisplayType.of(e.getInputType()));
            response.setTitle(e.getTitle());
            response.setName(e.getInputDisplayName());
            response.setParamName(e.getInputParamName());
            response.setNeedYn("Y".equals(e.getInputNeedYn()));

            return response;
        }).collect(Collectors.toList());
    }

    private List<WebchatBotFormBlockInfoResponse.AuthButtonInfo> convertAuthButton(List<WebchatFormBtnElement> buttonList) {
        if (buttonList == null || buttonList.size() == 0)
            return new ArrayList<>();
        return buttonList.stream().map(e -> {
            WebchatBotFormBlockInfoResponse.AuthButtonInfo response = new WebchatBotFormBlockInfoResponse.AuthButtonInfo();

            response.setId(e.getId());
            response.setBlockId(e.getFormBlockId());
            response.setSequence(e.getSequence());
            response.setName(e.getBtnName());
            response.setAction(AuthButtonAction.of(e.getAction()));
            response.setActionData(e.getNextActionData());
            response.setResultParamName(e.getApiResultParamName());

            return response;
        }).collect(Collectors.toList());
    }
}
