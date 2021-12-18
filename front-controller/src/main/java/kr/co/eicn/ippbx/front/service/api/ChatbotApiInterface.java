package kr.co.eicn.ippbx.front.service.api;

import kr.co.eicn.ippbx.front.config.RequestGlobal;
import kr.co.eicn.ippbx.front.model.form.FileForm;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryWebchatBotInfoResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WebchatBotInfoResponse;
import kr.co.eicn.ippbx.model.form.WebchatBotFormRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class ChatbotApiInterface extends ApiServerInterface {
    private static final String subUrl = "/api/v1/chat/bot/";
    @Value("${eicn.webchat.image.url}")
    private String webchatUrl;
    @Autowired
    protected RequestGlobal g;

    @SneakyThrows
    public List<SummaryWebchatBotInfoResponse> list() {
        return getList(subUrl, null, SummaryWebchatBotInfoResponse.class).getData();
    }

    @SneakyThrows
    public WebchatBotInfoResponse getById(Integer id) {
        return getData(subUrl + id, null, WebchatBotInfoResponse.class).getData();
    }

    @SneakyThrows
    public Integer post(WebchatBotFormRequest form) {
        return getData(HttpMethod.POST, subUrl, form, Integer.class, false).getData();
    }

    @SneakyThrows
    public void update(Integer id, WebchatBotFormRequest form) {
        put(subUrl + id, form);
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
    public String uploadImage(FileForm form) {
        String saveFileName = sendByMultipartFile(HttpMethod.POST, subUrl + "image", form, String.class, Collections.singletonMap("image", new FileResource(form.getFilePath(), form.getOriginalName())));

        HashMap<String, String> parameterMap = new HashMap<>();

        parameterMap.put("company_id", g.getUser().getCompanyId());
        parameterMap.put("file_name", saveFileName);

        log.info(webchatUrl);
        log.info(parameterMap.toString());
        getResponse(webchatUrl, parameterMap, HttpMethod.POST, false);

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
