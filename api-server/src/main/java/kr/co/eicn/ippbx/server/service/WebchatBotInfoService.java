package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotInfo;
import kr.co.eicn.ippbx.model.dto.eicn.WebchatBotSummaryInfoResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WebchatBotInfoResponse;
import kr.co.eicn.ippbx.model.enums.FallbackAction;
import kr.co.eicn.ippbx.model.form.WebchatBotFormRequest;
import kr.co.eicn.ippbx.model.search.ChatbotSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.WebchatBotInfoRepository;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class WebchatBotInfoService extends ApiBaseService {
    private final Logger logger = LoggerFactory.getLogger(WebchatBotInfoService.class);

    private final WebchatBotInfoRepository webchatBotInfoRepository;

    public List<WebchatBotSummaryInfoResponse> getAllWebchatBotList() {
        return webchatBotInfoRepository.findAll().stream().map(e -> convertDto(e, WebchatBotSummaryInfoResponse.class)).collect(Collectors.toList());
    }

    public Pagination<WebchatBotSummaryInfoResponse> pagination(ChatbotSearchRequest search) {
        Pagination<WebchatBotInfo> pagination = webchatBotInfoRepository.pagination(search);
        List<WebchatBotSummaryInfoResponse> rows = pagination.getRows().stream().map(e -> convertDto(e, WebchatBotSummaryInfoResponse.class)).collect(Collectors.toList());

        return new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit());
    }

    public WebchatBotInfoResponse get(Integer id) {
        WebchatBotInfo entity = webchatBotInfoRepository.findOneIfNullThrow(id);
        WebchatBotInfoResponse response = convertDto(entity, WebchatBotInfoResponse.class);

        response.setFallbackAction(FallbackAction.of(entity.getFallbackAction()));

        return response;
    }

    public WebchatBotInfo convertFormToData(WebchatBotFormRequest form) {
        WebchatBotInfo data = new WebchatBotInfo();

        data.setName(form.getName());
        data.setIsCustinputEnable(Objects.equals(form.getEnableCustomerInput(), true) ? "Y" : "N");
        data.setFallbackMent(form.getFallbackMent());
        data.setFallbackAction(form.getFallbackAction().getCode());

        if (FallbackAction.CONNECT_MEMBER.equals(form.getFallbackAction()))
            data.setFallbackActionData(String.valueOf(form.getNextGroupId()));
        else if (FallbackAction.CONNECT_URL.equals(form.getFallbackAction()))
            data.setFallbackActionData(form.getNextUrl());
        else if (FallbackAction.CONNECT_BLOCK.equals(form.getFallbackAction()))
            data.setFallbackActionData(String.valueOf(form.getNextBlockId()));
        else if (FallbackAction.CONNECT_PHONE.equals(form.getFallbackAction()))
            data.setFallbackActionData(form.getNextPhone());

        return data;
    }

    public Integer insert(WebchatBotFormRequest form) {
        WebchatBotInfo data = convertFormToData(form);

        return webchatBotInfoRepository.insert(data);
    }

    public void updateById(Integer id, WebchatBotFormRequest form) {
        WebchatBotInfo data = convertFormToData(form);

        webchatBotInfoRepository.updateById(id, data);
    }

    public void updateBlockId(Integer id, Integer blockId) {
        if (blockId != null)
            webchatBotInfoRepository.updateBlockId(id, blockId);
    }

    public void deleteById(Integer id) {
        webchatBotInfoRepository.delete(id);
    }

    public boolean isPresent(Integer id) {
        return webchatBotInfoRepository.findOneIfNullThrow(id) != null;
    }
}
