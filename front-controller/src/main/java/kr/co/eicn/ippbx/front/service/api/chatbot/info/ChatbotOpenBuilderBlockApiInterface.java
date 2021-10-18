package kr.co.eicn.ippbx.front.service.api.chatbot.info;

import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.customdb.ChatbotOpenBuilderInfoResponse;
import kr.co.eicn.ippbx.model.form.ChatbotOpenBuilderBlockFormRequest;
import kr.co.eicn.ippbx.model.search.ChatbotOpenBuilderBlockSearchRequest;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.util.page.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ChatbotOpenBuilderBlockApiInterface extends ApiServerInterface {
    private final Logger logger = LoggerFactory.getLogger(ChatbotOpenBuilderBlockApiInterface.class);

    private final String subUrl = "/api/v1/admin/chatbot/info/block/";

    public Pagination<ChatbotOpenBuilderInfoResponse> getPagination(ChatbotOpenBuilderBlockSearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl, search, ChatbotOpenBuilderInfoResponse.class).getData();
    }

    public ChatbotOpenBuilderInfoResponse get(Integer seq) throws IOException, ResultFailException {
        return getData(subUrl + seq, null, ChatbotOpenBuilderInfoResponse.class).getData();
    }

    public void post(ChatbotOpenBuilderBlockFormRequest form) throws IOException, ResultFailException {
        post(subUrl, form);
    }

    public void put(Integer seq, ChatbotOpenBuilderBlockFormRequest form) throws IOException, ResultFailException {
        put(subUrl + seq, form);
    }

    public void delete(Integer seq) throws IOException, ResultFailException {
        delete(subUrl + seq);
    }
}
