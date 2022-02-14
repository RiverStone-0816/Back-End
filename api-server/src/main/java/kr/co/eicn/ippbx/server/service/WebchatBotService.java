package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotTree;
import kr.co.eicn.ippbx.model.dto.eicn.WebchatBotInfoResponse;
import kr.co.eicn.ippbx.model.enums.ButtonAction;
import kr.co.eicn.ippbx.model.enums.FallbackAction;
import kr.co.eicn.ippbx.model.form.WebchatBotFormRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.replace;

@Slf4j
@RequiredArgsConstructor
@Service
public class WebchatBotService extends ApiBaseService {
    private final WebchatBotInfoService webchatBotInfoService;
    private final WebchatBotTreeService webchatBotTreeService;
    private final WebchatBotBlockService webchatBotBlockService;
    private final WebchatBotDisplayService webchatBotDisplayService;
    private final WebchatBotDisplayElementService webchatBotDisplayElementService;
    private final WebchatBotButtonElementService webchatBotButtonElementService;
    private final WebchatBotApiParamService webchatBotApiParamService;
    private final ImageFileStorageService imageFileStorageService;

    @Value("${file.path.chatbot}")
    private String savePath;

    public Integer createWebchatBotInfo(WebchatBotFormRequest form) {
        final Integer botId = webchatBotInfoService.insert(form);

        final Integer realBlockId = insertRootBlock(botId, form).get(form.getNextBlockId());
        if (FallbackAction.CONNECT_BLOCK.equals(form.getFallbackAction()))
            webchatBotInfoService.updateBlockId(botId, realBlockId);

        return botId;
    }

    public void updateWebchatBotInfo(Integer botId, WebchatBotFormRequest form, boolean fallbackUpdate) {
        final WebchatBotInfoResponse oldData = getBotInfo(botId);
        if (oldData != null) {
            try {
                deleteAllBlockInfoById(botId);
                final Integer realBlockId = insertRootBlock(botId, form).get(form.getNextBlockId());
                if (FallbackAction.CONNECT_BLOCK.equals(form.getFallbackAction()))
                    form.setNextBlockId(realBlockId);

                webchatBotInfoService.updateById(botId, form, fallbackUpdate);
            } catch (Exception e) {
                // FIXME: 발생할 수 있는 Exception 정의하여 특정 Exception에 대응하도록 수정
                log.error(e.getMessage());
                e.printStackTrace();

                deleteAllBlockInfoById(botId);
                WebchatBotFormRequest copyData = convertDto(oldData, WebchatBotFormRequest.class);
                insertRootBlock(botId, copyData);

                throw e;
            }
        }
    }

    public HashMap<Integer, Integer> insertRootBlock(Integer botId, WebchatBotFormRequest request) {
        final HashMap<Integer, Integer> realBlockIdByVirtualBlockId = new HashMap<>();
        final HashMap<Integer, Integer> buttonUpdateSchedule = new HashMap<>();

        WebchatBotFormRequest.BlockInfo blockInfo = request.getBlockInfo();

        Integer parentButtonId = 0;
        String parentTreeName = "";
        Integer level = 0;

        insertBlock(botId, null, null, parentButtonId, parentTreeName, level, blockInfo, realBlockIdByVirtualBlockId, buttonUpdateSchedule);

        buttonUpdateSchedule.forEach((k, v) -> webchatBotButtonElementService.updateNextBlockId(k, realBlockIdByVirtualBlockId.get(v)));

        return realBlockIdByVirtualBlockId;
    }

    public void insertBlock(Integer botId, Integer rootId, Integer parentId, Integer parentButtonId, String parentTreeName, Integer level, WebchatBotFormRequest.BlockInfo blockInfo,
                            HashMap<Integer, Integer> realBlockIdByVirtualBlockId, HashMap<Integer, Integer> buttonUpdateSchedule) {
        final Integer blockId = webchatBotBlockService.insert(blockInfo);
        realBlockIdByVirtualBlockId.put(blockInfo.getId(), blockId);

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

        if (blockInfo.getButtonList() != null && blockInfo.getChildren() != null) {
            final Map<Integer, WebchatBotFormRequest.BlockInfo> blockInfoByVirtualId = blockInfo.getChildren().stream().collect(Collectors.toMap(WebchatBotFormRequest.BlockInfo::getId, e -> e));

            for (WebchatBotFormRequest.ButtonElement buttonElement : blockInfo.getButtonList()) {
                final Integer buttonId = webchatBotButtonElementService.insertButtonElement(blockId, buttonElement);
                if (ButtonAction.CONNECT_NEXT_BLOCK.equals(buttonElement.getAction()) && blockInfoByVirtualId.containsKey(buttonElement.getNextBlockId()))
                    insertBlock(botId, rootId, blockId, buttonId, treeName, level + 1, blockInfoByVirtualId.get(buttonElement.getNextBlockId()), realBlockIdByVirtualBlockId, buttonUpdateSchedule);

                if (ButtonAction.CONNECT_NEXT_BLOCK.equals(buttonElement.getAction()) || ButtonAction.CONNECT_BLOCK.equals(buttonElement.getAction()))
                    buttonUpdateSchedule.put(buttonId, buttonElement.getNextBlockId());

                if (ButtonAction.CONNECT_API.equals(buttonElement.getAction()))
                    for (WebchatBotFormRequest.ApiParam apiParam : buttonElement.getParamList())
                        webchatBotApiParamService.insert(buttonId, apiParam);
            }
        }
    }

    public void deleteBot(Integer botId) {
        deleteAllBlockInfoById(botId);
        webchatBotInfoService.deleteById(botId);
    }

    public void deleteAllBlockInfoById(Integer botId) {
        final List<Integer> blockIdList = webchatBotTreeService.findBlockIdListByBotId(botId);
        final List<Integer> displayIdList = webchatBotDisplayService.findDisplayIdListByBlockIdList(blockIdList);
        final List<Integer> buttonIdList = webchatBotButtonElementService.findIdListByBlockIdList(blockIdList);

        webchatBotApiParamService.deleteByButtonIdList(buttonIdList);
        webchatBotButtonElementService.deleteByBlockIdList(blockIdList);
        webchatBotDisplayElementService.deleteByDisplayIdList(displayIdList);
        webchatBotDisplayService.deleteByBlockIdList(blockIdList);
        webchatBotBlockService.deleteByBlockIdList(blockIdList);
        webchatBotTreeService.deleteByBotId(botId);
    }

    public WebchatBotInfoResponse getBotInfo(Integer botId) {
        final WebchatBotInfoResponse response = webchatBotInfoService.get(botId);
        final WebchatBotTree rootBlock = webchatBotTreeService.findRootBlock(botId);

        if (rootBlock != null) {
            final Integer rootBlockId = rootBlock.getBlockId();
            List<Integer> blockIdList = new ArrayList<>();
            List<Integer> displayIdList = new ArrayList<>();
            List<Integer> buttonIdList = new ArrayList<>();

            Map<Integer, List<Integer>> blockIdByParentId = webchatBotTreeService.findBlockIdByParentIdMapByBotId(botId, rootBlockId);
            blockIdList.add(rootBlockId);
            blockIdByParentId.values().forEach(blockIdList::addAll);

            Map<Integer, WebchatBotInfoResponse.BlockInfo> blockInfoById = webchatBotBlockService.findBlockInfoByIdInBlockIdList(blockIdList);

            Map<Integer, List<WebchatBotInfoResponse.DisplayInfo>> displayByBlockIdMap = webchatBotDisplayService.findDisplayByBlockId(blockIdList);
            displayByBlockIdMap.values().forEach(e -> e.forEach(f -> displayIdList.add(f.getId())));

            Map<Integer, List<WebchatBotInfoResponse.ButtonInfo>> buttonListByBlockIdMap = webchatBotButtonElementService.findButtonListByBlockId(blockIdList);
            buttonListByBlockIdMap.values().forEach(e -> e.forEach(f -> buttonIdList.add(f.getId())));

            Map<Integer, List<WebchatBotInfoResponse.DisplayElement>> displayElementByDisplayId = webchatBotDisplayElementService.findDisplayElementByDisplayId(displayIdList);
            Map<Integer, List<WebchatBotInfoResponse.ApiParam>> apiParamListByButtonId = webchatBotApiParamService.findApiParamListByButtonId(buttonIdList);

            response.setBlockInfo(setBlockDetailInfo(blockInfoById.get(rootBlockId), blockIdByParentId, blockInfoById, displayByBlockIdMap, displayElementByDisplayId, buttonListByBlockIdMap, apiParamListByButtonId));
        }

        return response;
    }

    public WebchatBotInfoResponse.BlockInfo setBlockDetailInfo(WebchatBotInfoResponse.BlockInfo block, Map<Integer, List<Integer>> blockIdByParentId, Map<Integer, WebchatBotInfoResponse.BlockInfo> blockInfoById,
                                                               Map<Integer, List<WebchatBotInfoResponse.DisplayInfo>> displayByBlockIdMap, Map<Integer, List<WebchatBotInfoResponse.DisplayElement>> displayElementByDisplayId,
                                                               Map<Integer, List<WebchatBotInfoResponse.ButtonInfo>> buttonListByBlockIdMap, Map<Integer, List<WebchatBotInfoResponse.ApiParam>> apiParamListByButtonId) {

        webchatBotDisplayService.setDisplayList(block, displayByBlockIdMap, displayElementByDisplayId);
        webchatBotButtonElementService.setButtonList(block, buttonListByBlockIdMap, apiParamListByButtonId);
        if (blockIdByParentId != null && blockIdByParentId.containsKey(block.getId())) {
            ArrayList<WebchatBotInfoResponse.BlockInfo> children = new ArrayList<>();

            for (Integer childId : blockIdByParentId.get(block.getId()))
                children.add(setBlockDetailInfo(blockInfoById.get(childId), blockIdByParentId, blockInfoById, displayByBlockIdMap, displayElementByDisplayId, buttonListByBlockIdMap, apiParamListByButtonId));

            block.setChildren(children);
        } else
            block.setChildren(new ArrayList<>());

        return block;
    }

    public Integer copy(Integer botId) {
        WebchatBotInfoResponse response = getBotInfo(botId);

        WebchatBotFormRequest copyData = convertDto(response, WebchatBotFormRequest.class);
        if (copyData.getName() != null) copyData.setName(copyData.getName() + "(+1)");
        else copyData.setName("_(+1)");

        return createWebchatBotInfo(copyData);
    }

    public String uploadImage(MultipartFile image) {
        final Path newPath = Paths.get(replace(savePath, "{0}", g.getUser().getCompanyId()));

        return imageFileStorageService.uploadImage(newPath, image);
    }

    public Resource getImage(String fileName) {
        final Path newPath = Paths.get(replace(savePath, "{0}", g.getUser().getCompanyId()));

        return imageFileStorageService.loadImage(newPath, fileName);
    }

    public WebchatBotInfoResponse.BlockInfo getBlockInfo(Integer blockId) {
        WebchatBotInfoResponse.BlockInfo blockInfo = webchatBotBlockService.getBlockInfo(blockId);
        if (blockInfo != null) {

            List<Integer> blockIdList = Collections.singletonList(blockId);
            List<Integer> displayIdList = new ArrayList<>();
            List<Integer> buttonIdList = new ArrayList<>();

            Map<Integer, WebchatBotInfoResponse.BlockInfo> blockInfoById = webchatBotBlockService.findBlockInfoByIdInBlockIdList(blockIdList);

            Map<Integer, List<WebchatBotInfoResponse.DisplayInfo>> displayByBlockIdMap = webchatBotDisplayService.findDisplayByBlockId(blockIdList);
            displayByBlockIdMap.values().forEach(e -> e.forEach(f -> displayIdList.add(f.getId())));

            Map<Integer, List<WebchatBotInfoResponse.ButtonInfo>> buttonListByBlockIdMap = webchatBotButtonElementService.findButtonListByBlockId(blockIdList);
            buttonListByBlockIdMap.values().forEach(e -> e.forEach(f -> buttonIdList.add(f.getId())));

            Map<Integer, List<WebchatBotInfoResponse.DisplayElement>> displayElementByDisplayId = webchatBotDisplayElementService.findDisplayElementByDisplayId(displayIdList);
            Map<Integer, List<WebchatBotInfoResponse.ApiParam>> apiParamListByButtonId = webchatBotApiParamService.findApiParamListByButtonId(buttonIdList);

            setBlockDetailInfo(blockInfo, null, blockInfoById, displayByBlockIdMap, displayElementByDisplayId, buttonListByBlockIdMap, apiParamListByButtonId);

        }
        return blockInfo;
    }
}
