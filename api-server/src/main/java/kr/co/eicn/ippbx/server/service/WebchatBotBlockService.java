package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotBlock;
import kr.co.eicn.ippbx.model.dto.eicn.WebchatBotInfoResponse;
import kr.co.eicn.ippbx.model.form.WebchatBotBlockFormRequest;
import kr.co.eicn.ippbx.model.form.WebchatBotFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.WebchatBotBlockRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class WebchatBotBlockService extends ApiBaseService {
    private final Logger logger = LoggerFactory.getLogger(WebchatBotBlockService.class);

    private final WebchatBotBlockRepository webchatbotBlockRepository;

    public Integer insert(WebchatBotFormRequest.BlockInfo info) {
        WebchatBotBlockFormRequest data = new WebchatBotBlockFormRequest();

        data.setName(info.getName());
        data.setKeyword(info.getKeyword());
        data.setIsTemplateEnable(info.getIsTemplateEnable());
        data.setPosX(info.getPosX());
        data.setPosY(info.getPosY());

        return webchatbotBlockRepository.insert(data);
    }

    public void deleteByBlockIdList(List<Integer> blockIdList) {
        webchatbotBlockRepository.deleteByBlockIdList(blockIdList);
    }

    public Map<Integer, WebchatBotInfoResponse.BlockInfo> findBlockInfoByIdInBlockIdList(List<Integer> blockIdList) {
        return webchatbotBlockRepository.findInBlockIdList(blockIdList).stream().map(this::convertEntityToResponse).collect(Collectors.toMap(WebchatBotInfoResponse.BlockInfo::getId, e -> e));
    }

    public WebchatBotInfoResponse.BlockInfo convertEntityToResponse(WebchatBotBlock entity) {
        WebchatBotInfoResponse.BlockInfo response = new WebchatBotInfoResponse.BlockInfo();

        response.setId(entity.getId());
        response.setPosX(entity.getPosX());
        response.setPosY(entity.getPoxY());
        response.setName(entity.getName());
        response.setKeyword(entity.getKeyword());
        response.setIsTemplateEnable("Y".equals(entity.getIsTplEnable()));

        return response;
    }
}
