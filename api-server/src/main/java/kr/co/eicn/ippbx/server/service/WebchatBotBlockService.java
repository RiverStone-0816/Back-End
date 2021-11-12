package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.model.form.WebchatBotBlockFormRequest;
import kr.co.eicn.ippbx.model.form.WebchatBotFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.WebchatBotBlockRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class WebchatBotBlockService extends ApiBaseService {
    private final Logger logger = LoggerFactory.getLogger(WebchatBotBlockService.class);

    private final WebchatBotBlockRepository webchatbotBlockRepository;

    public void insert(Integer blockId, WebchatBotFormRequest.BlockInfo info) {
        WebchatBotBlockFormRequest data = new WebchatBotBlockFormRequest();

        data.setName(info.getName());
        data.setKeyword(info.getKeyword());
        data.setIsTemplateEnable(info.getIsTemplateEnable());

        webchatbotBlockRepository.insert(blockId, data);
    }

    public void deleteByBlockIdList(List<Integer> blockIdList) {
        webchatbotBlockRepository.deleteByBlockIdList(blockIdList);
    }
}
