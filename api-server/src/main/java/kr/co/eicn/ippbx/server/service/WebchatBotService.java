package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.model.form.WebchatBotFormRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

        Integer blockId = 0;
        Integer rootId = 0;
        Integer parentId = 0;
        Integer parentButtonId = 0;
        String parentTreeName = "";
        Integer level = 0;

        insertBlock(botId, blockId, rootId, parentId, parentButtonId, parentTreeName, level, blockInfo);
    }

    public Integer insertBlock(Integer botId, Integer blockId, Integer rootId, Integer parentId, Integer parentButtonId, String parentTreeName, Integer level, WebchatBotFormRequest.BlockInfo blockInfo) {
        webchatBotBlockService.insert(blockId, blockInfo);
        String treeName = webchatBotTreeService.insert(botId, blockId, rootId, parentId, parentButtonId, parentTreeName, level);

        for (WebchatBotFormRequest.DisplayInfo displayInfo : blockInfo.getDisplayList()) {
            final Integer displayId = webchatBotDisplayService.insertDisplay(blockId, displayInfo);

            for (WebchatBotFormRequest.DisplayElement displayElement : displayInfo.getElementList()) {
                webchatBotDisplayElementService.insertDisplayElement(displayId, displayElement);
            }
        }

        if (blockInfo.getButtonList() != null) {
            for (int buttonId = 0; buttonId < blockInfo.getButtonList().size(); buttonId++) {
                final WebchatBotFormRequest.ButtonElement buttonElement = blockInfo.getButtonList().get(buttonId);
                webchatBotButtonElementService.insertButtonElement(blockId, buttonId, buttonElement);

                for (WebchatBotFormRequest.ApiParam apiParam : buttonElement.getParamList()) {
                    webchatBotApiParamService.insert(buttonId, apiParam);
                }

                if (buttonElement.getConnectedBlockInfo() != null) {
                    blockId = insertBlock(botId, blockId + 1, rootId, blockId, buttonId, treeName, level + 1, buttonElement.getConnectedBlockInfo());
                }
            }
        }

        return blockId;
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
