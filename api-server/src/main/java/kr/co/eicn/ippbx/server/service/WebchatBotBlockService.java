package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.model.dto.eicn.WebchatBotBlockSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WebchatBotInfoResponse;
import kr.co.eicn.ippbx.model.form.WebchatBotBlockFormRequest;
import kr.co.eicn.ippbx.model.form.WebchatBotFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.WebchatBotBlockRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class WebchatBotBlockService extends ApiBaseService {

    private final WebchatBotBlockRepository webchatbotBlockRepository;

    public List<WebchatBotBlockSummaryResponse> getTemplateBlockList() {
        return webchatbotBlockRepository.getAllTemplateBlockList();
    }

    public Integer insert(WebchatBotFormRequest.BlockInfo info) {
        WebchatBotBlockFormRequest data = new WebchatBotBlockFormRequest();

        data.setName(info.getName());
        data.setKeyword(StringUtils.isNotEmpty(info.getKeyword()) ? info.getKeyword().replaceAll("\\s+", "") : "");
        data.setIsTemplateEnable(info.getIsTemplateEnable());
        data.setPosX(info.getPosX());
        data.setPosY(info.getPosY());

        return webchatbotBlockRepository.insert(data);
    }

    public void deleteByBlockIdList(List<Integer> blockIdList) {
        webchatbotBlockRepository.deleteByBlockIdList(blockIdList);
    }

    public Map<Integer, WebchatBotInfoResponse.BlockInfo> findBlockInfoByIdInBlockIdList(List<Integer> blockIdList) {
        return webchatbotBlockRepository.findBlockInfoByIdInBlockIdList(blockIdList).stream().collect(Collectors.toMap(WebchatBotInfoResponse.BlockInfo::getId, e -> e));
    }

    public WebchatBotInfoResponse.BlockInfo getBlockInfo(Integer blockId) {
        return webchatbotBlockRepository.findOneIfNullThrow(blockId);
    }
}
