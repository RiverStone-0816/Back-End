package kr.co.eicn.ippbx.front.service.api.chatbot.event;

import kr.co.eicn.ippbx.exception.UnauthorizedException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.customdb.ChatbotEventHistoryResponse;
import kr.co.eicn.ippbx.model.form.ChatbotSendEventFormRequest;
import kr.co.eicn.ippbx.model.search.ChatbotEventHistorySearchRequest;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.util.page.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

@Service
public class ChatbotEventHistoryApiInterface extends ApiServerInterface {
    private final Logger logger = LoggerFactory.getLogger(ChatbotEventHistoryApiInterface.class);

    private final String subUrl = "/api/v1/admin/chatbot/event/history/";

    public Pagination<ChatbotEventHistoryResponse> getPagination(ChatbotEventHistorySearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl, search, ChatbotEventHistoryResponse.class).getData();
    }

    public void post(ChatbotSendEventFormRequest form) throws IOException, ResultFailException {
        Integer data = getData(HttpMethod.POST, subUrl, form, Integer.class).getData();
        sendEventToKakao(form, data);
    }

    private void sendEventToKakao(ChatbotSendEventFormRequest form, Integer eventId) {
        String url = "http://localhost:8101/custom_event";

        final RestTemplate template = new RestTemplate(getFactory());
        template.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.defaultCharset()));

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        final UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

        try {
            final Map<String, Object> params = new HashMap<>();

            params.put("bot_id", form.getBotId());
            params.put("event_name", form.getEventName());
            params.put("user_type", form.getUserType());
            params.put("user_id", form.getUserId());
            params.put("user_data", form.getEventData());
            params.put("param_event_id", eventId);

            params.forEach((key, value) -> {
                if (value == null)
                    return;

                if (value.getClass().isArray()) {
                    if (((Object[]) value).length > 0)
                        uriBuilder.queryParam(key, value);
                } else if (value instanceof Iterable) {
                    if (((Iterable<?>) value).iterator().hasNext()) {
                        Iterable<?> iter = ((Iterable<?>) value);

                        final List<Object> objects = new ArrayList<>();
                        iter.forEach(objects::add);

                        uriBuilder.queryParam(key, objects.toArray());
                    }
                } else {
                    uriBuilder.queryParam(key, value);
                }
            });

            if (logger.isInfoEnabled())
                logger.info("[GET] " + uriBuilder.build().encode().toUri());
        } catch (Exception ignored) {

        }

        try {
            template.exchange(uriBuilder.build().encode().toUri(), HttpMethod.GET, new HttpEntity<>(headers), String.class);

        } catch (HttpStatusCodeException e) {
            logger.warn(e.getStatusCode() + " : " + e.getResponseBodyAsString());
            if (Objects.equals(e.getStatusCode(), HttpStatus.UNAUTHORIZED))
                throw new UnauthorizedException(e.getStatusText(), e.getResponseHeaders(), e.getResponseBodyAsByteArray(), Charset.defaultCharset());
            throw e;
        }
    }
}

