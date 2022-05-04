package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotAuthresultElement;
import kr.co.eicn.ippbx.model.dto.eicn.WebchatBotInfoResponse;
import kr.co.eicn.ippbx.model.form.WebchatBotFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.WebchatBotAuthResultElementRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class WebchatBotAuthResultElementService extends ApiBaseService {
    private final WebchatBotAuthResultElementRepository webchatBotAuthResultElementRepository;

    public Map<Integer, List<WebchatBotInfoResponse.AuthResultElement>> findAllByBlockIdList(List<Integer> blockIdList) {
        return webchatBotAuthResultElementRepository.findAllByBLockIdList(blockIdList)
                .stream()
                .map(this::convertEntityToResponse)
                .collect(Collectors.groupingBy(WebchatBotInfoResponse.AuthResultElement::getBlockId));
    }

    private WebchatBotInfoResponse.AuthResultElement convertEntityToResponse(WebchatBotAuthresultElement entity) {
        WebchatBotInfoResponse.AuthResultElement response = new WebchatBotInfoResponse.AuthResultElement();

        response.setId(entity.getId());
        response.setBlockId(entity.getBlockId());
        response.setValue(entity.getResultValue());
        response.setMent(entity.getResultMent());
        response.setAction(entity.getResultAction());
        response.setNextActionData(entity.getResultNextActionData());

        return response;
    }

    public void insert(Integer blockId, List<WebchatBotFormRequest.AuthResultElement> authResultElementList) {
        webchatBotAuthResultElementRepository.insert(blockId, authResultElementList);
    }
}
