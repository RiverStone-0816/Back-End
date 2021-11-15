package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.model.form.WebchatBotDisplayFormRequest;
import kr.co.eicn.ippbx.model.form.WebchatBotFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.WebchatBotDisplayElementRepository;
import kr.co.eicn.ippbx.server.repository.eicn.WebchatBotDisplayRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class WebchatBotDisplayService extends ApiBaseService {
    private final Logger logger = LoggerFactory.getLogger(WebchatBotDisplayService.class);

    private final WebchatBotDisplayRepository webchatBotDisplayRepository;
    private final WebchatBotDisplayElementRepository webchatBotDisplayElementRepository;

    public Integer insertDisplay(Integer blockId, WebchatBotFormRequest.DisplayInfo displayInfo) {
        final WebchatBotDisplayFormRequest data = new WebchatBotDisplayFormRequest();

        data.setBlockId(blockId);
        data.setOrder(displayInfo.getOrder());
        data.setType(displayInfo.getType().getCode());

        return webchatBotDisplayRepository.insert(data);
    }

    public List<Integer> findDisplayIdListByDisplayIdList(List<Integer> blockIdList) {
        return webchatBotDisplayRepository.findDisplayIdListByDisplayIdList(blockIdList);
    }

    public void deleteByBlockIdList(List<Integer> blockIdList) {
        webchatBotDisplayRepository.deleteByBlockIdList(blockIdList);
    }
}
