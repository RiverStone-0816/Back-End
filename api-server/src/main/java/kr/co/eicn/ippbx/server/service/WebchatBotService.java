package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.model.dto.eicn.WebchatBotInfoResponse;
import kr.co.eicn.ippbx.model.enums.ButtonAction;
import kr.co.eicn.ippbx.model.enums.FallbackAction;
import kr.co.eicn.ippbx.model.form.WebchatBotFormRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
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
    private final FileSystemStorageService fileSystemStorageService;

    @Value("${file.path.chatbot}")
    private String savePath;

    public Integer createWebchatBotInfo(WebchatBotFormRequest form) {
        final Integer botId = webchatBotInfoService.insert(form);

        final Integer realBlockId = insertRootBlock(botId, form).get(form.getNextBlockId());
        if (FallbackAction.CONNECT_BLOCK.equals(form.getFallbackAction()))
            webchatBotInfoService.updateBlockId(botId, realBlockId);

        return botId;
    }

    public void updateWebchatBotInfo(Integer botId, WebchatBotFormRequest form) {
        if (webchatBotInfoService.isPresent(botId)) {
            deleteAllBlockInfoById(botId);

            final Integer realBlockId = insertRootBlock(botId, form).get(form.getNextBlockId());
            if (FallbackAction.CONNECT_BLOCK.equals(form.getFallbackAction()))
                form.setNextBlockId(realBlockId);

            webchatBotInfoService.updateById(botId, form);
        }
    }

    public HashMap<Integer, Integer> insertRootBlock(Integer botId, WebchatBotFormRequest request) {
        final HashMap<Integer, Integer> virtualBlockIdByRealBlockId = new HashMap<>();

        WebchatBotFormRequest.BlockInfo blockInfo = request.getBlockInfo();

        Integer parentButtonId = 0;
        String parentTreeName = "";
        Integer level = 0;

        insertBlock(botId, null, null, parentButtonId, parentTreeName, level, blockInfo, virtualBlockIdByRealBlockId);

        return virtualBlockIdByRealBlockId;
    }

    public Integer insertBlock(Integer botId, Integer rootId, Integer parentId, Integer parentButtonId, String parentTreeName, Integer level, WebchatBotFormRequest.BlockInfo blockInfo, HashMap<Integer, Integer> virtualBlockIdByRealBlockId) {
        final Integer blockId = webchatBotBlockService.insert(blockInfo);
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

        Map<Integer, WebchatBotFormRequest.BlockInfo> blockInfoByVirtualId = blockInfo.getChildren().stream().collect(Collectors.toMap(WebchatBotFormRequest.BlockInfo::getId, e -> e));

        if (blockInfo.getButtonList() != null && blockInfo.getChildren() != null) {
            for (int buttonId = 0; buttonId < blockInfo.getButtonList().size(); buttonId++) {
                final WebchatBotFormRequest.ButtonElement buttonElement = blockInfo.getButtonList().get(buttonId);

                if (ButtonAction.CONNECT_NEXT_BLOCK.equals(buttonElement.getAction()) && blockInfoByVirtualId.containsKey(buttonElement.getNextBlockId())) {
                    Integer childBlockId = insertBlock(botId, rootId, blockId, buttonId, treeName, level + 1, blockInfoByVirtualId.get(buttonElement.getNextBlockId()), virtualBlockIdByRealBlockId);
                    virtualBlockIdByRealBlockId.put(buttonElement.getNextBlockId(), childBlockId);
                }
            }

            for (int buttonId = 0; buttonId < blockInfo.getButtonList().size(); buttonId++) {
                final WebchatBotFormRequest.ButtonElement buttonElement = blockInfo.getButtonList().get(buttonId);

                if (ButtonAction.CONNECT_NEXT_BLOCK.equals(buttonElement.getAction()) || ButtonAction.CONNECT_BLOCK.equals(buttonElement.getAction()))
                    buttonElement.setNextBlockId(virtualBlockIdByRealBlockId.get(buttonElement.getNextBlockId()));

                webchatBotButtonElementService.insertButtonElement(blockId, buttonId, buttonElement);

                for (WebchatBotFormRequest.ApiParam apiParam : buttonElement.getParamList())
                    webchatBotApiParamService.insert(buttonId, apiParam);
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
        final List<Integer> displayIdList = webchatBotDisplayService.findDisplayIdListByBlockIdList(blockIdList);
        final List<Integer> buttonIdList = webchatBotButtonElementService.findIdListByBlockIdList(blockIdList);

        webchatBotApiParamService.deleteByButtonIdList(buttonIdList);
        webchatBotButtonElementService.deleteByBlockIdList(blockIdList);
        webchatBotDisplayElementService.deleteByDisplayIdList(displayIdList);
        webchatBotDisplayService.deleteByBlockIdList(blockIdList);
        webchatBotBlockService.deleteByBlockIdList(blockIdList);
        webchatBotTreeService.deleteByBotId(botId);
    }

    public WebchatBotInfoResponse get(Integer botId) {
        final WebchatBotInfoResponse response = webchatBotInfoService.get(botId);
        final Integer rootBlockId = webchatBotTreeService.findRootBlockId(botId);
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

        return response;
    }

    public WebchatBotInfoResponse.BlockInfo setBlockDetailInfo(WebchatBotInfoResponse.BlockInfo block, Map<Integer, List<Integer>> blockIdByParentId, Map<Integer, WebchatBotInfoResponse.BlockInfo> blockInfoById,
                                                               Map<Integer, List<WebchatBotInfoResponse.DisplayInfo>> displayByBlockIdMap, Map<Integer, List<WebchatBotInfoResponse.DisplayElement>> displayElementByDisplayId,
                                                               Map<Integer, List<WebchatBotInfoResponse.ButtonInfo>> buttonListByBlockIdMap, Map<Integer, List<WebchatBotInfoResponse.ApiParam>> apiParamListByButtonId) {

        webchatBotDisplayService.setDisplayList(block, displayByBlockIdMap, displayElementByDisplayId);
        webchatBotButtonElementService.setButtonList(block, buttonListByBlockIdMap, apiParamListByButtonId);
        if (blockIdByParentId.containsKey(block.getId())) {
            ArrayList<WebchatBotInfoResponse.BlockInfo> children = new ArrayList<>();

            for (Integer childId : blockIdByParentId.get(block.getId()))
                children.add(setBlockDetailInfo(blockInfoById.get(childId), blockIdByParentId, blockInfoById, displayByBlockIdMap, displayElementByDisplayId, buttonListByBlockIdMap, apiParamListByButtonId));

            block.setChildren(children);
        } else
            block.setChildren(new ArrayList<>());

        return block;
    }

    public Integer copy(Integer botId) {
        WebchatBotInfoResponse response = get(botId);

        WebchatBotFormRequest copyData = convertDto(response, WebchatBotFormRequest.class);

        return createWebchatBotInfo(copyData);
    }

    public String uploadImage(MultipartFile image) {
        final String saveFileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()).concat("_") + image.getOriginalFilename();
        final Path newPath = Paths.get(replace(savePath, "{0}", g.getUser().getCompanyId()));

        if (!StringUtils.endsWithAny(Objects.requireNonNull(image.getOriginalFilename()).toLowerCase(), ".jpg", "jpeg", ".png"))
            throw new IllegalArgumentException("알 수 없는 파일 확장자입니다.");

        fileSystemStorageService.store(newPath, saveFileName, image);

        return saveFileName;
    }

    public Resource getImage(String fileName) {
        final Path newPath = Paths.get(replace(savePath, "{0}", g.getUser().getCompanyId()));

        if (!StringUtils.endsWithAny(Objects.requireNonNull(fileName).toLowerCase(), ".jpg", "jpeg", ".png"))
            throw new IllegalArgumentException("알 수 없는 파일 확장자입니다.");

        return fileSystemStorageService.loadAsResource(newPath, fileName);
    }
}
