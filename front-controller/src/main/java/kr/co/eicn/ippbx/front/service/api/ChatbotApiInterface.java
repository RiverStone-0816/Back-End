package kr.co.eicn.ippbx.front.service.api;

import kr.co.eicn.ippbx.model.dto.eicn.SummaryWebchatBotInfoResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WebchatBotInfoResponse;
import kr.co.eicn.ippbx.model.form.WebchatBotFormRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ChatbotApiInterface extends ApiServerInterface {
    private static final String subUrl = "/api/v1/chat-bot/info/";

    @SneakyThrows
    public List<SummaryWebchatBotInfoResponse> list() {
        return getList(subUrl, null, SummaryWebchatBotInfoResponse.class).getData();
    }

    @SneakyThrows
    public WebchatBotInfoResponse getById(Integer id) {
        return get(subUrl+ "id/" + id, null, WebchatBotInfoResponse.class);
    }

    @SneakyThrows
    public void post(WebchatBotFormRequest form) {
        post(subUrl, form);
    }

    @SneakyThrows
    public void update(Integer id, WebchatBotFormRequest form) {
        put(subUrl + "id/" + id, form);
    }

    @SneakyThrows
    public void delete(Integer id) {
        delete(subUrl + "id/" + id);
    }

    @SneakyThrows
    public void copy(Integer id) {
        post(subUrl + "id/" + id, null);
    }
}
