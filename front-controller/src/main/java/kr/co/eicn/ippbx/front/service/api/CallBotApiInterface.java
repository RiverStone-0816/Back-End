package kr.co.eicn.ippbx.front.service.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import kr.co.eicn.ippbx.model.CallBotRequest;
import kr.co.eicn.ippbx.model.CallBotResponse;
import kr.co.eicn.ippbx.util.ResultFailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;

@Service
public class CallBotApiInterface {
    private static final Logger logger = LoggerFactory.getLogger(CallBotApiInterface.class);

    private static final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();

    private static HttpComponentsClientHttpRequestFactory getFactory() {
        factory.setConnectTimeout(60 * 1000);
        factory.setReadTimeout(60 * 1000);
        return factory;
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${eicn.callbot.url}")
    private String callbotUrl;

    public CallBotResponse getCallbot(CallBotRequest request, String callbotUrl) throws IOException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        final RestTemplate restTemplate = new RestTemplate(getFactory());
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.defaultCharset()));

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String paramString = objectMapper.writeValueAsString(request);
        ResponseEntity<String> result = restTemplate.exchange(callbotUrl, HttpMethod.POST, new HttpEntity<>(paramString, headers), String.class);

        return objectMapper.readValue(result.getBody(), CallBotResponse.class);
    }
}
