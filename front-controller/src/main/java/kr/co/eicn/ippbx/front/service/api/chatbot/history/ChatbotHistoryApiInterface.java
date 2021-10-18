package kr.co.eicn.ippbx.front.service.api.chatbot.history;

import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.customdb.ChatbotHistoryResponse;
import kr.co.eicn.ippbx.model.search.ChatbotHistorySearchRequest;
import kr.co.eicn.ippbx.model.search.ChatbotProfileMsgSearchRequest;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.util.page.Pagination;
import org.jooq.exception.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatbotHistoryApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(ChatbotHistoryApiInterface.class);
    private static final String subUrl = "/api/v1/admin/chatbot/history/";

    public Pagination<ChatbotHistoryResponse> getPagination(ChatbotHistorySearchRequest search) throws IOException, java.io.IOException, ResultFailException {
        return getPagination(subUrl, search, ChatbotHistoryResponse.class).getData();
    }

    public List<ChatbotHistoryResponse> getChatbotMessageHistory(ChatbotProfileMsgSearchRequest search) throws IOException, java.io.IOException, ResultFailException {
        return getList(subUrl + "chat-message", search, ChatbotHistoryResponse.class).getData();
    }
}
