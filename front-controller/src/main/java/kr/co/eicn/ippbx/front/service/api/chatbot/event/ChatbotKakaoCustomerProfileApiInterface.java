package kr.co.eicn.ippbx.front.service.api.chatbot.event;

import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.customdb.ChatKaKaoProfileInfoResponse;
import kr.co.eicn.ippbx.model.dto.customdb.ChatbotKakaoCustomerProfileResponse;
import kr.co.eicn.ippbx.model.dto.customdb.ChatbotSendEventDataResponse;
import kr.co.eicn.ippbx.model.search.ChatbotKakaoCustomerProfileSearchRequest;
import kr.co.eicn.ippbx.model.search.ChatbotProfileMsgSearchRequest;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.util.page.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ChatbotKakaoCustomerProfileApiInterface extends ApiServerInterface {
    private final Logger logger = LoggerFactory.getLogger(ChatbotKakaoCustomerProfileApiInterface.class);

    private final String subUrl = "/api/v1/admin/chatbot/event/profile/";

    public Pagination<ChatbotKakaoCustomerProfileResponse> getPagination(ChatbotKakaoCustomerProfileSearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl, search, ChatbotKakaoCustomerProfileResponse.class).getData();
    }

    public ChatbotSendEventDataResponse get(Integer seq) throws IOException, ResultFailException {
        return getData(subUrl + seq, null, ChatbotSendEventDataResponse.class).getData();
    }

    public ChatKaKaoProfileInfoResponse getProfileInfo(ChatbotProfileMsgSearchRequest search) throws IOException, ResultFailException {
        return getData(subUrl + "profile-info", search, ChatKaKaoProfileInfoResponse.class).getData();
    }
}

