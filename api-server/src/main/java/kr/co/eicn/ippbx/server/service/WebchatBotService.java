package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.model.enums.ButtonAction;
import kr.co.eicn.ippbx.model.form.WebchatBotFormRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@AllArgsConstructor
@Service
public class WebchatBotService extends ApiBaseService {
    private final Logger logger = LoggerFactory.getLogger(WebchatBotService.class);

    private final WebchatBotInfoService webchatBotInfoService;
    private final WebchatBotTreeService webchatBotTreeService;
    private final WebchatBotBlockService webchatBotBlockService;
    private final WebchatBotDisplayService webchatBotDisplayService;
    private final WebchatBotDisplayElementService webchatBotDisplayElementService;
    private final WebchatBotButtonElementService webchatBotButtonElementService;
    private final WebchatBotApiParamService webchatBotApiParamService;

    public Integer createWebchatBotInfo(WebchatBotFormRequest form) {
        Integer botId = webchatBotInfoService.insert(form);

        insertRootBlock(botId, form);

        return botId;
    }

    public void updateWebchatBotInfo(Integer botId, WebchatBotFormRequest form) {
        if (webchatBotInfoService.isPresent(botId)) {
            deleteAllBlockInfoById(botId);
            webchatBotInfoService.updateById(botId, form);
            insertRootBlock(botId, form);
        }
    }

    public void insertRootBlock(Integer botId, WebchatBotFormRequest request) {
        WebchatBotFormRequest.BlockInfo blockInfo = request.getBlockInfo();

        Integer parentButtonId = 0;
        String parentTreeName = "";
        Integer level = 0;

        insertBlock(botId, null, null, parentButtonId, parentTreeName, level, blockInfo);
    }

    public void insertBlock(Integer botId, Integer rootId, Integer parentId, Integer parentButtonId, String parentTreeName, Integer level, WebchatBotFormRequest.BlockInfo blockInfo) {
        final HashMap<Integer, Integer> buttonIdByBlockId = new HashMap<>();
        Integer blockId = webchatBotBlockService.insert(blockInfo);
        if (rootId == null) rootId = blockId;
        if (parentId == null) parentId = blockId;
        String treeName = webchatBotTreeService.insert(botId, blockId, rootId, parentId, parentButtonId, parentTreeName, level);

        if (blockInfo.getDisplayList() != null) {
            for (WebchatBotFormRequest.DisplayInfo displayInfo : blockInfo.getDisplayList()) {
                final Integer displayId = webchatBotDisplayService.insertDisplay(blockId, displayInfo);

                for (WebchatBotFormRequest.DisplayElement displayElement : displayInfo.getElementList()) {
                    webchatBotDisplayElementService.insertDisplayElement(displayId, displayElement);
                }
            }
        }

        if (blockInfo.getButtonList() != null) {
            for (int buttonId = 0; buttonId < blockInfo.getButtonList().size(); buttonId++) {
                final WebchatBotFormRequest.ButtonElement buttonElement = blockInfo.getButtonList().get(buttonId);
                webchatBotButtonElementService.insertButtonElement(blockId, buttonId, buttonElement);

                for (WebchatBotFormRequest.ApiParam apiParam : buttonElement.getParamList()) {
                    webchatBotApiParamService.insert(buttonId, apiParam);
                }

                if (ButtonAction.CONNECT_NEXT_BLOCK.equals(buttonElement.getAction()))
                    buttonIdByBlockId.put(buttonElement.getNextBlockId(), buttonId);
            }
        }

        if (blockInfo.getChildren() != null) {
            for (WebchatBotFormRequest.BlockInfo child : blockInfo.getChildren()) {
                if (buttonIdByBlockId.containsKey(child.getId()))
                    insertBlock(botId, rootId, blockId, buttonIdByBlockId.get(child.getId()), treeName, level + 1, child);
            }
        }
    }

    public void deleteBot(Integer botId) {
        deleteAllBlockInfoById(botId);
        webchatBotInfoService.deleteById(botId);
    }

    public void deleteAllBlockInfoById(Integer botId) {
        final List<Integer> blockIdList = webchatBotTreeService.findBlockIdListByBotId(botId);
        final List<Integer> displayIdList = webchatBotDisplayService.findDisplayIdListByDisplayIdList(blockIdList);
        final List<Integer> buttonIdList = webchatBotButtonElementService.findIdListByBlockIdList(blockIdList);

        webchatBotApiParamService.deleteByButtonIdList(buttonIdList);
        webchatBotButtonElementService.deleteByBlockIdList(blockIdList);
        webchatBotDisplayElementService.deleteByDisplayIdList(displayIdList);
        webchatBotDisplayService.deleteByBlockIdList(blockIdList);
        webchatBotBlockService.deleteByBlockIdList(blockIdList);
        webchatBotTreeService.deleteByBotId(botId);
    }


}
