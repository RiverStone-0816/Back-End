package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotDispElement;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotDisplay;
import kr.co.eicn.ippbx.model.dto.eicn.WebchatBotInfoResponse;
import kr.co.eicn.ippbx.model.enums.DisplayType;
import kr.co.eicn.ippbx.model.form.WebchatBotDisplayFormRequest;
import kr.co.eicn.ippbx.model.form.WebchatBotFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.WebchatBotDisplayRepository;
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
public class WebchatBotDisplayService extends ApiBaseService {
    private final Logger logger = LoggerFactory.getLogger(WebchatBotDisplayService.class);

    private final WebchatBotDisplayRepository webchatBotDisplayRepository;

    public Integer insertDisplay(Integer blockId, WebchatBotFormRequest.DisplayInfo displayInfo) {
        final WebchatBotDisplayFormRequest data = new WebchatBotDisplayFormRequest();

        data.setBlockId(blockId);
        data.setOrder(displayInfo.getOrder());
        data.setType(displayInfo.getType());

        return webchatBotDisplayRepository.insert(data);
    }

    public List<Integer> findDisplayIdListByBlockIdList(List<Integer> blockIdList) {
        return webchatBotDisplayRepository.findDisplayListByBlockIdList(blockIdList).stream().map(WebchatBotDisplay::getId).collect(Collectors.toList());
    }

    public void deleteByBlockIdList(List<Integer> blockIdList) {
        webchatBotDisplayRepository.deleteByBlockIdList(blockIdList);
    }

    public WebchatBotInfoResponse.DisplayInfo convertEntityToResponse(WebchatBotDisplay entity) {
        WebchatBotInfoResponse.DisplayInfo response = new WebchatBotInfoResponse.DisplayInfo();

        response.setId(entity.getId());
        response.setBlockId(entity.getBlockId());
        response.setOrder(entity.getSequence());
        response.setType(DisplayType.of(entity.getType()));

        return response;
    }

    public Map<Integer, List<WebchatBotInfoResponse.DisplayInfo>> findDisplayByBlockId(List<Integer> blockIdList) {
        return webchatBotDisplayRepository.findDisplayListByBlockIdList(blockIdList).stream().map(this::convertEntityToResponse).collect(Collectors.groupingBy(WebchatBotInfoResponse.DisplayInfo::getBlockId));
    }

    public void setDisplayList(WebchatBotInfoResponse.BlockInfo block, Map<Integer, List<WebchatBotInfoResponse.DisplayInfo>> displayByBlockIdMap, Map<Integer, List<WebchatBotInfoResponse.DisplayElement>> displayElementByDisplayId) {
        if (displayByBlockIdMap.containsKey(block.getId())) {
            List<WebchatBotInfoResponse.DisplayInfo> displayInfoList = displayByBlockIdMap.get(block.getId());

            displayInfoList.forEach(e -> {
                if (displayElementByDisplayId.containsKey(e.getId()))
                    e.setElementList(displayElementByDisplayId.get(e.getId()));
            });

            block.setDisplayList(displayInfoList);
        } else {
            block.setDisplayList(new ArrayList<>());
        }
    }
}
