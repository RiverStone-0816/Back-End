package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.model.form.WebchatBotApiParamFormRequest;
import kr.co.eicn.ippbx.model.form.WebchatBotFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.WebchatBotApiParamRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class WebchatBotApiParamService extends ApiBaseService {
    private final Logger logger = LoggerFactory.getLogger(WebchatBotApiParamService.class);

    private final WebchatBotApiParamRepository webchatBotApiParamRepository;

    public void insert(Integer buttonId, WebchatBotFormRequest.ApiParam apiParam) {
        WebchatBotApiParamFormRequest data = new WebchatBotApiParamFormRequest();

        data.setButtonId(buttonId);
        data.setType(apiParam.getType().getCode());
        data.setParamName(apiParam.getParamName());
        data.setDisplayName(apiParam.getDisplayName());

        webchatBotApiParamRepository.insert(data);
    }

    public void deleteByButtonIdList(List<Integer> buttonIdList) {
        webchatBotApiParamRepository.deleteByButtonIdList(buttonIdList);
    }
}
