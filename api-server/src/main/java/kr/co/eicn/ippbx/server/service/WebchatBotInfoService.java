package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatBotInfo;
import kr.co.eicn.ippbx.model.dto.eicn.WebchatBotFallbackInfoResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WebchatBotInfoResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WebchatBotSummaryInfoResponse;
import kr.co.eicn.ippbx.model.enums.FallbackAction;
import kr.co.eicn.ippbx.model.form.WebchatBotFallbackFormRequest;
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
        setFallbackData(response, entity);

        return response;
    }

    public WebchatBotFallbackInfoResponse getFallbackInfo(Integer seq) {
        WebchatBotInfo entity = webchatBotInfoRepository.findOne(seq);

        WebchatBotFallbackInfoResponse response = convertDto(entity, WebchatBotFallbackInfoResponse.class);
        setFallbackData(response, entity);

        return response;
    }

    private void setFallbackData(WebchatBotFallbackInfoResponse response, WebchatBotInfo entity) {
        response.setEnableCustomerInput(Objects.equals(entity.getIsCustinputEnable(), "Y"));
        response.setFallbackAction(FallbackAction.of(entity.getFallbackAction()));
        if (FallbackAction.CONNECT_MEMBER.getCode().equals(entity.getFallbackAction()) && entity.getFallbackActionData() != null)
            response.setNextGroupId(Integer.valueOf(entity.getFallbackActionData()));
        else if (FallbackAction.CONNECT_URL.getCode().equals(entity.getFallbackAction()))
            response.setNextUrl(entity.getFallbackActionData());
        else if (FallbackAction.CONNECT_BLOCK.getCode().equals(entity.getFallbackAction()) && entity.getFallbackActionData() != null)
            response.setNextBlockId(Integer.valueOf(entity.getFallbackActionData()));
        else if (FallbackAction.CONNECT_PHONE.getCode().equals(entity.getFallbackAction()))
            response.setNextPhone(entity.getFallbackActionData());
    }

    public WebchatBotInfo convertFormToData(WebchatBotFallbackFormRequest form) {
        WebchatBotInfo data = new WebchatBotInfo();

        data.setIsCustinputEnable(Objects.equals(form.getEnableCustomerInput(), true) ? "Y" : "N");
        data.setFallbackMent(form.getFallbackMent());
        data.setFallbackAction(form.getFallbackAction().getCode());

        if (FallbackAction.CONNECT_MEMBER.equals(form.getFallbackAction()) && form.getNextGroupId() != null)
            data.setFallbackActionData(String.valueOf(form.getNextGroupId()));
        else if (FallbackAction.CONNECT_URL.equals(form.getFallbackAction()))
            data.setFallbackActionData(form.getNextUrl());
        else if (FallbackAction.CONNECT_BLOCK.equals(form.getFallbackAction()) && form.getNextBlockId() != null)
            data.setFallbackActionData(String.valueOf(form.getNextBlockId()));
        else if (FallbackAction.CONNECT_PHONE.equals(form.getFallbackAction()))
            data.setFallbackActionData(form.getNextPhone());

        return data;
    }

    public Integer insert(WebchatBotFormRequest form) {
        WebchatBotInfo data = convertFormToData(form);
        data.setName(form.getName());

        return webchatBotInfoRepository.insert(data);
    }

    public void updateById(Integer id, WebchatBotFormRequest form, boolean fallbackUpdate) {
        WebchatBotInfo data = convertFormToData(form);
        data.setName(form.getName());

        webchatBotInfoRepository.updateById(id, data, fallbackUpdate);
    }

    public void updateBlockId(Integer id, Integer blockId) {
        if (blockId != null)
            webchatBotInfoRepository.updateBlockId(id, blockId);
    }

    public void updateFallbackInfo(Integer id, WebchatBotFallbackFormRequest form) {
        WebchatBotInfo data = convertFormToData(form);
        webchatBotInfoRepository.updateFallbackInfo(id, data);
    }

    public void deleteById(Integer id) {
        webchatBotInfoRepository.delete(id);
    }

    public boolean isPresent(Integer id) {
        return webchatBotInfoRepository.findOneIfNullThrow(id) != null;
    }
}
