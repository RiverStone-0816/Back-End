package kr.co.eicn.ippbx.front.service.api;

import kr.co.eicn.ippbx.front.model.form.FileForm;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.enums.TalkChannelType;
import kr.co.eicn.ippbx.model.form.WebchatFormBlocKFormRequest;
import kr.co.eicn.ippbx.model.form.WebchatBotFallbackFormRequest;
import kr.co.eicn.ippbx.model.form.WebchatBotFormRequest;
import kr.co.eicn.ippbx.model.search.ChatbotSearchRequest;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class ChatbotApiInterface extends ApiServerInterface {
    private static final String subUrl = "/api/v1/chat/bot/";
    private static final String authSubUrl = "/api/v1/chat/bot/auth-block/";

    @SneakyThrows
    public List<WebchatBotSummaryInfoResponse> list() {
        return getList(subUrl, null, WebchatBotSummaryInfoResponse.class).getData();
    }

    @SneakyThrows
    public Pagination<WebchatBotSummaryInfoResponse> pagination(ChatbotSearchRequest request) {
        return getPagination(subUrl + "pagination", request, WebchatBotSummaryInfoResponse.class).getData();
    }

    @SneakyThrows
    public WebchatBotInfoResponse getBotInfo(Integer id) {
        return getData(subUrl + id, null, WebchatBotInfoResponse.class).getData();
    }

    @SneakyThrows
    public WebchatBotFallbackInfoResponse getBotFallbackInfo(Integer id) {
        return getData(subUrl + id + "/fallback", null, WebchatBotFallbackInfoResponse.class).getData();
    }

    @SneakyThrows
    public WebchatBotInfoResponse.BlockInfo getBlock(Integer blockId) {
        return getData(subUrl + "blocks/" + blockId, null, WebchatBotInfoResponse.BlockInfo.class).getData();
    }

    public List<WebchatBotFormBlockInfoResponse> getAuthBlockList() throws IOException, ResultFailException {
        return getList(authSubUrl, null, WebchatBotFormBlockInfoResponse.class).getData();
    }

    public Integer addAuthBlock(WebchatFormBlocKFormRequest form) throws IOException, ResultFailException {
        return getData(HttpMethod.POST, authSubUrl, form, Integer.class, false).getData();
    }

    public void updateAuthBlock(Integer blockId, WebchatFormBlocKFormRequest form) throws IOException, ResultFailException {
        put(authSubUrl + blockId, form);
    }

    public void deleteAuthBlock(Integer blockId) throws IOException, ResultFailException {
        delete(authSubUrl + blockId);
    }

    @SneakyThrows
    public Integer post(WebchatBotFormRequest form) {
        return getData(HttpMethod.POST, subUrl, form, Integer.class, false).getData();
    }

    @SneakyThrows
    public void update(Integer id, WebchatBotFormRequest form) {
        put(subUrl + id + "/all", form);
    }

    @SneakyThrows
    public void updateBotInfo(Integer id, WebchatBotFormRequest form) {
        put(subUrl + id, form);
    }

    @SneakyThrows
    public void fallbackUpdate(Integer id, WebchatBotFallbackFormRequest form) {
        put(subUrl + id + "/fallback", form);
    }

    @SneakyThrows
    public void delete(Integer id) {
        delete(subUrl + id);
    }

    @SneakyThrows
    public Integer copy(Integer id) {
        return getData(HttpMethod.POST, subUrl + id + "/copy", null, Integer.class, false).getData();
    }

    @SneakyThrows
    public List<WebchatBotBlockSummaryResponse> getTemplateBlockList() {
        return getList(subUrl + "/blocks/template", null, WebchatBotBlockSummaryResponse.class).getData();
    }

    @SneakyThrows
    public String uploadImage(FileForm form, String companyId) {
        final String saveFileName = sendByMultipartFile(HttpMethod.POST, subUrl + "image", form, String.class, Collections.singletonMap("image", new FileResource(form.getFilePath(), form.getOriginalName())));

        uploadWebchatImageToGateway(companyId, saveFileName, TalkChannelType.EICN);

        return saveFileName;
    }

    @SneakyThrows
    public Resource getImage(String fileName) {
        val url = apiServerUrl + subUrl + "image?fileName=" + URLEncoder.encode(fileName, "UTF-8");
        final HttpHeaders headers = new HttpHeaders();
        if (StringUtils.isNotEmpty(getAccessToken())) headers.setBearerAuth(getAccessToken());

        return new RestTemplate().exchange(url, HttpMethod.GET, new HttpEntity<>(headers), Resource.class).getBody();
    }
}
