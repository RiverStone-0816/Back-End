package kr.co.eicn.ippbx.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

public abstract class AbstractRestInterface {
    private static final Logger logger = LoggerFactory.getLogger(AbstractRestInterface.class);
    private static final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
    protected TypeFactory typeFactory = new ObjectMapper().getTypeFactory();

    protected static HttpComponentsClientHttpRequestFactory getFactory() {
        factory.setConnectTimeout(60 * 1000);
        factory.setReadTimeout(60 * 1000);
        return factory;
    }

    protected <BODY> Object get(String url, BODY o, JavaType javaType) throws HttpStatusCodeException, IOException {
        return call(url, o, javaType, HttpMethod.GET, false);
    }

    protected <BODY> void post(String url, BODY o) throws HttpStatusCodeException, IOException, ResultFailException {
        final JsonResult<?> result = post(url, o, JsonResult.class);
        if (result.isFailure())
            throw new ResultFailException(result.getRepresentMessage());
    }

    protected <BODY> void put(String url, BODY o) throws HttpStatusCodeException, IOException, ResultFailException {
        final JsonResult<?> result = put(url, o, JsonResult.class);
        if (result.isFailure())
            throw new ResultFailException(result.getRepresentMessage());
    }

    protected <BODY> void patch(String url, BODY o) throws HttpStatusCodeException, IOException, ResultFailException {
        final JsonResult<?> result = patch(url, o, JsonResult.class);
        if (result.isFailure())
            throw new ResultFailException(result.getRepresentMessage());
    }

    protected <BODY> Object post(String url, BODY o, JavaType javaType) throws HttpStatusCodeException, IOException {
        return call(url, o, javaType, HttpMethod.POST, false);
    }

    protected <BODY> Object put(String url, BODY o, JavaType javaType) throws HttpStatusCodeException, IOException {
        return call(url, o, javaType, HttpMethod.PUT, false);
    }

    protected <BODY> Object patch(String url, BODY o, JavaType javaType) throws HttpStatusCodeException, IOException {
        return call(url, o, javaType, HttpMethod.PATCH, false);
    }

    protected <BODY> Object delete(String url, BODY o, JavaType javaType) throws HttpStatusCodeException, IOException {
        return call(url, o, javaType, HttpMethod.DELETE, false);
    }

    protected <BODY> Object call(String url, BODY o, JavaType javaType, HttpMethod method, boolean urlParam) throws HttpStatusCodeException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        final RestTemplate template = new RestTemplate(/*getFactory()*/);
        @SuppressWarnings("unchecked") final Map<String, Object> params = (Map<String, Object>) objectMapper.convertValue(o, Map.class);
        template.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.defaultCharset()));

        if (logger.isInfoEnabled())
            logger.info("[" + method + "] " + (method.equals(HttpMethod.GET) ? UrlUtils.joinQueryString(url, params) : url));

        final HttpHeaders headers = new HttpHeaders();
        if (!method.equals(HttpMethod.GET))
            headers.setContentType(MediaType.APPLICATION_JSON);

        final ResponseEntity<String> response = method.equals(HttpMethod.GET) || urlParam
                ? template.exchange(UrlUtils.joinQueryString(url, params), method, new HttpEntity<>(headers), String.class)
                : template.exchange(url, method, new HttpEntity<>(objectMapper.writeValueAsString(params), headers), String.class);

        return objectMapper.readValue(response.getBody(), javaType);
    }

    protected <BODY, RESPONSE> RESPONSE get(String url, BODY o, Class<RESPONSE> klass) throws HttpStatusCodeException, IOException {
        return call(url, o, klass, HttpMethod.GET, false);
    }

    protected <BODY, RESPONSE> RESPONSE post(String url, BODY o, Class<RESPONSE> klass) throws HttpStatusCodeException, IOException {
        return call(url, o, klass, HttpMethod.POST, false);
    }

    protected <BODY, RESPONSE> RESPONSE put(String url, BODY o, Class<RESPONSE> klass) throws HttpStatusCodeException, IOException {
        return call(url, o, klass, HttpMethod.PUT, false);
    }

    protected <BODY, RESPONSE> RESPONSE patch(String url, BODY o, Class<RESPONSE> klass) throws HttpStatusCodeException, IOException {
        return call(url, o, klass, HttpMethod.PATCH, false);
    }

    protected <BODY, RESPONSE> RESPONSE delete(String url, BODY o, Class<RESPONSE> klass) throws HttpStatusCodeException, IOException {
        return call(url, o, klass, HttpMethod.DELETE, false);
    }

    protected <BODY, RESPONSE> RESPONSE call(String url, BODY o, Class<RESPONSE> klass, HttpMethod method) throws HttpStatusCodeException, IOException {
        return call(url, o, klass, method, false);
    }

    protected <BODY, RESPONSE> RESPONSE call(String url, BODY o, Class<RESPONSE> klass, HttpMethod method, boolean urlParam) throws HttpStatusCodeException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        final RestTemplate template = new RestTemplate(/*getFactory()*/);
        @SuppressWarnings("unchecked") final Map<String, Object> params = (Map<String, Object>) objectMapper.convertValue(o, Map.class);
        template.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.defaultCharset()));

        if (logger.isInfoEnabled())
            logger.info("[" + method + "] " + (method.equals(HttpMethod.GET) ? UrlUtils.joinQueryString(url, params) : url));

        final HttpHeaders headers = new HttpHeaders();
        if (!method.equals(HttpMethod.GET))
            headers.setContentType(MediaType.APPLICATION_JSON);

        final ResponseEntity<String> response = method.equals(HttpMethod.GET) || urlParam
                ? template.exchange(UrlUtils.joinQueryString(url, params), method, new HttpEntity<>(headers), String.class)
                : template.exchange(url, method, new HttpEntity<>(objectMapper.writeValueAsString(params), headers), String.class);

        return objectMapper.readValue(response.getBody(), klass);
    }
}
