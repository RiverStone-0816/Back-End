package kr.co.eicn.ippbx.front.service.api;

import kr.co.eicn.ippbx.front.model.form.FileForm;
import kr.co.eicn.ippbx.model.dto.eicn.WebchatServiceInfoResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WebchatServiceSummaryInfoResponse;
import kr.co.eicn.ippbx.model.form.WebchatServiceInfoFormRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class WebchatConfigApiInterface extends ApiServerInterface {
    private static final String subUrl = "/api/v1/chat/config/";

    @SneakyThrows
    public List<WebchatServiceSummaryInfoResponse> list() {
        return getList(subUrl, null, WebchatServiceSummaryInfoResponse.class).getData();
    }

    @SneakyThrows
    public WebchatServiceInfoResponse get(Integer seq) {
        return getData(subUrl + seq, null, WebchatServiceInfoResponse.class).getData();
    }

    @SneakyThrows
    public Integer post(WebchatServiceInfoFormRequest form) {
        return getData(HttpMethod.POST, subUrl, form, Integer.class, false).getData();
    }

    @SneakyThrows
    public void update(Integer seq, WebchatServiceInfoFormRequest form) {
        put(subUrl + seq, form);
    }

    @SneakyThrows
    public void delete(Integer seq) {
        delete(subUrl + seq);
    }

    @SneakyThrows
    public String uploadImage(FileForm form) {
        return sendByMultipartFile(HttpMethod.POST, subUrl + "image", form, String.class, Collections.singletonMap("image", new FileResource(form.getFilePath(), form.getOriginalName())));
    }
}
