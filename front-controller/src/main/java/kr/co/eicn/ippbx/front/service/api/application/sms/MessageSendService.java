package kr.co.eicn.ippbx.front.service.api.application.sms;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.co.eicn.ippbx.front.config.RequestGlobal;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.front.service.api.DaemonInfoInterface;
import kr.co.eicn.ippbx.model.form.SendMessageFormRequest;
import kr.co.eicn.ippbx.util.ResultFailException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class MessageSendService extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(MessageSendService.class);

    @Value("${eicn.message.url.id}")
    private String messageUrlId;

    private final DaemonInfoInterface daemonInfoInterface;
    private final RequestGlobal g;

    public void post(SendMessageFormRequest form) throws IOException, ResultFailException {
        final MessageForm request = new MessageForm();
        final List<SendMessageRequest> targets = new ArrayList<>();

        request.setCompanyId(g.getUser().getCompanyId());
        request.setService(form.getContent().getBytes(StandardCharsets.UTF_8).length < 90 ? "SMS" : "MMS");
        for(String number : form.getTargetNumbers()) {
            final SendMessageRequest target = new SendMessageRequest();
            target.setAttachFile("");
            target.setPhoneNumber(number);
            target.setTitle(form.getContent().length() > 10 ? form.getContent().substring(0,10) : form.getContent());
            target.setMessage(form.getContent());

            targets.add(target);
        }
        request.setDatas(targets);

        final String response = getResponse(daemonInfoInterface.getSocketList().get(messageUrlId).concat("/sendmessage"), request, HttpMethod.POST, false);
        logger.info("SMS response : " + response);
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
    private class SendMessageRequest {
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
