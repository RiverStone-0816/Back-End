package kr.co.eicn.ippbx.front.service.api.application.sms;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.co.eicn.ippbx.front.config.RequestGlobal;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.form.SendMessageFormRequest;
import kr.co.eicn.ippbx.util.ResultFailException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class MessageSendService extends ApiServerInterface {
    private static final String smsUrl = "https://dev.eicn.co.kr:8300/sendmessage";

    private final RequestGlobal g;

    public void post(SendMessageFormRequest form) throws IOException, ResultFailException {
        final MessageForm request = new MessageForm();
        final List<SendMessageRequest> targets = new ArrayList<>();

        request.setCompanyId(g.getUser().getCompanyId());
        request.setService(form.getContent().getBytes("euc-kr").length > 80 ? "MMS" : "SMS");
        for(String number : form.getTargetNumbers()) {
            final SendMessageRequest target = new SendMessageRequest();
            target.setAttachFile("");
            target.setPhoneNumber(number);
            target.setTitle(form.getContent().length() > 10 ? form.getContent().substring(0,10) : form.getContent());
            target.setMessage(form.getContent());

            targets.add(target);
        }
        request.setDatas(targets);

        getResponse(smsUrl, request, HttpMethod.POST, false);
    }

    @Data
    private class MessageForm {
        @JsonProperty("company_id")
        private String companyId;
        @JsonProperty("service")
        private String service;
        @JsonProperty("datas")
        private List<SendMessageRequest> datas;
    }

    @Data
    public class SendMessageRequest {
        @JsonProperty("attach_file")
        private String attachFile;
        @JsonProperty("phone_number")
        private String phoneNumber;
        @JsonProperty("title")
        private String title;
        @JsonProperty("message")
        private String message;
    }
}
