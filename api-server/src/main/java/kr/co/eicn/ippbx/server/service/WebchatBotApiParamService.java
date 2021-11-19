package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotApiParam;
import kr.co.eicn.ippbx.model.dto.eicn.WebchatBotInfoResponse;
import kr.co.eicn.ippbx.model.enums.ApiParameterType;
import kr.co.eicn.ippbx.model.form.WebchatBotApiParamFormRequest;
import kr.co.eicn.ippbx.model.form.WebchatBotFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.WebchatBotApiParamRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class WebchatBotApiParamService extends ApiBaseService {
    private final Logger logger = LoggerFactory.getLogger(WebchatBotApiParamService.class);

    private final WebchatBotApiParamRepository webchatBotApiParamRepository;

    public void insert(Integer buttonId, WebchatBotFormRequest.ApiParam apiParam) {
        WebchatBotApiParamFormRequest data = new WebchatBotApiParamFormRequest();

        data.setButtonId(buttonId);
        data.setType(apiParam.getType());
        data.setParamName(apiParam.getParamName());
        data.setDisplayName(apiParam.getDisplayName());

        webchatBotApiParamRepository.insert(data);
    }

    public void deleteByButtonIdList(List<Integer> buttonIdList) {
        webchatBotApiParamRepository.deleteByButtonIdList(buttonIdList);
    }

    public WebchatBotInfoResponse.ApiParam convertEntityToResponse(WebchatBotApiParam entity) {
        WebchatBotInfoResponse.ApiParam response = new WebchatBotInfoResponse.ApiParam();

        response.setId(entity.getId());
        response.setButtonId(entity.getBtnId());
        response.setType(ApiParameterType.of(entity.getType()));
        response.setParamName(entity.getParamName());
        response.setDisplayName(entity.getDisplayName());

        return response;
    }

    public Map<Integer, List<WebchatBotInfoResponse.ApiParam>> findApiParamListByButtonId(List<Integer> buttonIdList) {
        return webchatBotApiParamRepository.findAllInButtonIdList(buttonIdList).stream().map(this::convertEntityToResponse).collect(Collectors.groupingBy(WebchatBotInfoResponse.ApiParam::getButtonId));
    }
}
