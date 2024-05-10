package kr.co.eicn.ippbx.front.service.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import kr.co.eicn.ippbx.exception.UnauthorizedException;
import kr.co.eicn.ippbx.front.config.RequestGlobal;
import kr.co.eicn.ippbx.model.enums.TalkChannelType;
import kr.co.eicn.ippbx.util.AbstractRestInterface;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.util.UrlUtils;
import kr.co.eicn.ippbx.util.page.Pagination;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.net.ssl.*;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;


public abstract class ApiServerInterface extends AbstractRestInterface {
    public static final String SESSION_ACCESS_TOKEN = "SESSION_ACCESS_TOKEN";
    protected static final Logger logger = LoggerFactory.getLogger(ApiServerInterface.class);

    static {
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = (hostname, session) -> true;

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    protected final ObjectMapper objectMapper = new ObjectMapper();
    @Value("${eicn.realapiserver}")
    protected String apiServerUrl;
    @Value("${eicn.debugging}")
    protected String devel;
    @Autowired
    protected HttpSession session;
    @Value("${eicn.webchat.image.url}")
    private String webchatUrl;
    @Autowired
    protected RequestGlobal g;

    @PostConstruct
    public void setup() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, Boolean.parseBoolean(devel));
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    }

    protected <T> JavaType jsonResultType() {
        return typeFactory.constructType(JsonResult.class);
    }

    protected <T> JavaType jsonResultType(Class<T> dataType) {
        return typeFactory.constructParametricType(JsonResult.class, dataType);
    }

    protected <T> JavaType paginationJsonResultType(Class<T> dataType) {
        final JavaType type = typeFactory.constructParametricType(Pagination.class, dataType);
        return typeFactory.constructParametricType(JsonResult.class, type);
    }

    protected <T> JavaType listJsonResultType(Class<T> dataType) {
        final JavaType type = typeFactory.constructCollectionLikeType(ArrayList.class, dataType);
        return typeFactory.constructParametricType(JsonResult.class, type);
    }

    @Override
    protected <BODY> void post(String url, BODY o) throws HttpStatusCodeException, IOException, ResultFailException {
        final JsonResult<?> result = call(url, o, JsonResult.class, HttpMethod.POST, false);
        if (result.isFailure())
            throw new ResultFailException(result);
    }

    @Override
    protected <BODY> void put(String url, BODY o) throws HttpStatusCodeException, IOException, ResultFailException {
        final JsonResult<?> result = call(url, o, JsonResult.class, HttpMethod.PUT, false);
        if (result.isFailure())
            throw new ResultFailException(result);
    }

    protected void delete(String url) throws HttpStatusCodeException, IOException, ResultFailException {
        final JsonResult<?> result = call(url, null, JsonResult.class, HttpMethod.DELETE, false);
        if (result.isFailure())
            throw new ResultFailException(result);
    }

    @SuppressWarnings("unchecked")
    protected <BODY, RESPONSE> JsonResult<Pagination<RESPONSE>> getPagination(String url, BODY o, Class<RESPONSE> klass) throws IOException, ResultFailException {
        final JsonResult<Pagination<RESPONSE>> result = (JsonResult<Pagination<RESPONSE>>) call(url, o, paginationJsonResultType(klass), HttpMethod.GET, true);
        if (result.isFailure())
            throw new ResultFailException(result);
        return result;
    }

    @SuppressWarnings("unchecked")
    protected <BODY, RESPONSE> JsonResult<List<RESPONSE>> getList(String url, BODY o, Class<RESPONSE> klass) throws IOException, ResultFailException {
        final JsonResult<List<RESPONSE>> result = (JsonResult<List<RESPONSE>>) call(url, o, listJsonResultType(klass), HttpMethod.GET, true);
        if (result.isFailure())
            throw new ResultFailException(result);
        return result;
    }

    @SuppressWarnings("unchecked")
    protected <BODY, RESPONSE> JsonResult<RESPONSE> getData(String url, BODY o, JavaType type) throws IOException, ResultFailException {
        final JsonResult<RESPONSE> result = (JsonResult<RESPONSE>) call(url, o, type, HttpMethod.GET, true);
        if (result.isFailure())
            throw new ResultFailException(result);
        return result;
    }

    @SuppressWarnings("unchecked")
    protected <BODY, RESPONSE> JsonResult<RESPONSE> getData(String url, BODY o, Class<RESPONSE> klass) throws IOException, ResultFailException {
        final JsonResult<RESPONSE> result = (JsonResult<RESPONSE>) call(url, o, jsonResultType(klass), HttpMethod.GET, true);
        if (result.isFailure())
            throw new ResultFailException(result);
        return result;
    }

    protected <BODY> Resource getResource(String url) throws IOException, ResultFailException {
        return getResourceResponse(url);
    }

    protected <BODY> Resource getResourceImage(String url) throws IOException, ResultFailException {
        return getResourceResponseImage(url);
    }

    protected <BODY, RESPONSE> JsonResult<?> getResult(String url, BODY o) throws IOException, ResultFailException {
        final JsonResult<?> result = call(url, o, JsonResult.class, HttpMethod.GET, true);
        if (result.isFailure())
            throw new ResultFailException(result);
        return result;
    }

    @SuppressWarnings("unchecked")
    protected <BODY, RESPONSE> JsonResult<Pagination<RESPONSE>> getPagination(HttpMethod method, String url, BODY o, Class<RESPONSE> klass) throws IOException, ResultFailException {
        final JsonResult<Pagination<RESPONSE>> result = (JsonResult<Pagination<RESPONSE>>) call(url, o, paginationJsonResultType(klass), method, true);
        if (result.isFailure())
            throw new ResultFailException(result);
        return result;
    }

    @SuppressWarnings("unchecked")
    protected <BODY, RESPONSE> JsonResult<List<RESPONSE>> getList(HttpMethod method, String url, BODY o, Class<RESPONSE> klass) throws IOException, ResultFailException {
        final JsonResult<List<RESPONSE>> result = (JsonResult<List<RESPONSE>>) call(url, o, listJsonResultType(klass), method, true);
        if (result.isFailure())
            throw new ResultFailException(result);
        return result;
    }

    protected <BODY, RESPONSE> JsonResult<RESPONSE> getData(HttpMethod method, String url, BODY o, Class<RESPONSE> klass) throws IOException, ResultFailException {
        return getData(method, url, o, klass, true);
    }

    @SuppressWarnings("unchecked")
    protected <BODY, RESPONSE> JsonResult<RESPONSE> getData(HttpMethod method, String url, BODY o, Class<RESPONSE> klass, boolean urlParam) throws IOException, ResultFailException {
        final JsonResult<RESPONSE> result = (JsonResult<RESPONSE>) call(url, o, jsonResultType(klass), method, urlParam);
        if (result.isFailure())
            throw new ResultFailException(result);
        return result;
    }

    protected <BODY, RESPONSE> JsonResult<?> result(HttpMethod method, String url, BODY o) throws IOException, ResultFailException {
        final JsonResult<?> result = call(url, o, JsonResult.class, method, true);
        if (result.isFailure())
            throw new ResultFailException(result);
        return result;
    }

    @Override
    protected <BODY> Object call(String url, BODY o, JavaType javaType, HttpMethod method, boolean urlParam) throws IOException {
        return objectMapper.readValue(getResponse(url, o, method, urlParam), javaType);
    }

    @Override
    protected <BODY, RESPONSE> RESPONSE call(String url, BODY o, Class<RESPONSE> klass, HttpMethod method, boolean urlParam) throws IOException {
        final String response = getResponse(url, o, method, urlParam);
        return objectMapper.readValue(response, klass);
    }

    protected <BODY> String getResponse(String url, BODY o, HttpMethod method, boolean urlParam) throws JsonProcessingException {
        url = (url.startsWith("http") ? "" : apiServerUrl) + url;

        final RestTemplate template = new RestTemplate(getFactory());
        template.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.defaultCharset()));

        final HttpHeaders headers = new HttpHeaders();
        if (!method.equals(HttpMethod.GET))
            headers.setContentType(MediaType.APPLICATION_JSON);

        final String accessToken = getAccessToken();
        if (StringUtils.isNotEmpty(accessToken))
            headers.add("Authorization", "Bearer " + accessToken);

        headers.add("HTTP_CLIENT_IP", getClientIp());

        final UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

        String paramString = "";

        try {
            final Map<String, Object> params = o == null ? new HashMap<>() : objectMapper.convertValue(o, typeFactory.constructParametricType(Map.class, String.class, Object.class));
            paramString = objectMapper.writeValueAsString(params);
            if (method.equals(HttpMethod.GET) || urlParam)
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

            if (logger.isInfoEnabled()) {
                String userInfo = "";
                if (ObjectUtils.isNotEmpty(g) && ObjectUtils.isNotEmpty(g.getUser()))
                    userInfo = defaultIfEmpty(g.getUser().getCompanyId(), "") + "|" + defaultIfEmpty(g.getUser().getId(), "");
                MDC.put("userInfo", userInfo);

                logger.info("[" + method + "] " + (method.equals(HttpMethod.GET) || urlParam ? uriBuilder.build().encode().toUri() : url));
            }
        } catch (Exception ignored) {
            paramString = objectMapper.writeValueAsString(o);
        }

        try {
            final ResponseEntity<String> response = method.equals(HttpMethod.GET) || urlParam
                    ? template.exchange(uriBuilder.build().encode().toUri(), method, new HttpEntity<>(headers), String.class)
                    : template.exchange(url, method, new HttpEntity<>(paramString, headers), String.class);

            return response.getBody();
        } catch (HttpStatusCodeException e) {
            String userInfo = "";
            if (ObjectUtils.isNotEmpty(g) && ObjectUtils.isNotEmpty(g.getUser()))
                userInfo = defaultIfEmpty(g.getUser().getCompanyId(), "") + "|" + defaultIfEmpty(g.getUser().getId(), "") + " : ";
            MDC.put("userInfo", userInfo);

            logger.warn(e.getStatusCode() + " : " + e.getResponseBodyAsString());
            if (Objects.equals(e.getStatusCode(), HttpStatus.UNAUTHORIZED))
                throw new UnauthorizedException(e.getStatusText(), e.getResponseHeaders(), e.getResponseBodyAsByteArray(), Charset.defaultCharset());
            throw e;
        }
    }

    protected <BODY> Resource getResourceResponse(String url) {
        url = (url.startsWith("http") ? "" : apiServerUrl) + url;

        final RestTemplate template = new RestTemplate(getFactory());
        template.getMessageConverters().add(0, new ResourceHttpMessageConverter());

        final HttpHeaders headers = new HttpHeaders();

        headers.setContentType(new MediaType("audio", "mpeg"));
        final String accessToken = getAccessToken();
        if (StringUtils.isNotEmpty(accessToken))
            headers.add("Authorization", "Bearer " + accessToken);

        headers.add("HTTP_CLIENT_IP", getClientIp());

        final UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

        try {
            final ResponseEntity<Resource> response = template.exchange(uriBuilder.build().encode().toUri(), HttpMethod.GET, new HttpEntity<>(headers), Resource.class);

            return response.getBody();
        } catch (HttpStatusCodeException e) {
            logger.warn(e.getStatusCode() + " : " + e.getResponseBodyAsString());
            if (Objects.equals(e.getStatusCode(), HttpStatus.UNAUTHORIZED))
                throw new UnauthorizedException(e.getStatusText(), e.getResponseHeaders(), e.getResponseBodyAsByteArray(), Charset.defaultCharset());
            throw e;
        }
    }


    protected <BODY> ResponseEntity<Resource> getResourceResponseFrontDirect(String url) {
        url = (url.startsWith("http") ? "" : apiServerUrl) + url;

        final RestTemplate template = new RestTemplate(getFactory());
        template.getMessageConverters().add(0, new ResourceHttpMessageConverter());

        final HttpHeaders headers = new HttpHeaders();

        headers.setContentType(new MediaType("audio", "mpeg"));
        final String accessToken = getAccessToken();
        if (StringUtils.isNotEmpty(accessToken))
            headers.add("Authorization", "Bearer " + accessToken);

        headers.add("HTTP_CLIENT_IP", getClientIp());

        final UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

        try {
            final ResponseEntity<Resource> response = template.exchange(uriBuilder.build().encode().toUri(), HttpMethod.GET, new HttpEntity<>(headers), Resource.class);

            return response;
        } catch (HttpStatusCodeException e) {
            logger.warn(e.getStatusCode() + " : " + e.getResponseBodyAsString());
            if (Objects.equals(e.getStatusCode(), HttpStatus.UNAUTHORIZED))
                throw new UnauthorizedException(e.getStatusText(), e.getResponseHeaders(), e.getResponseBodyAsByteArray(), Charset.defaultCharset());
            throw e;
        }
    }

    protected <BODY> Resource getResourceResponseImage(String url) {
        url = (url.startsWith("http") ? "" : apiServerUrl) + url;

        final RestTemplate template = new RestTemplate(getFactory());
        template.getMessageConverters().add(0, new ResourceHttpMessageConverter());

        final HttpHeaders headers = new HttpHeaders();

        headers.setContentType(new MediaType("img", "jpg"));
        final String accessToken = getAccessToken();
        if (StringUtils.isNotEmpty(accessToken))
            headers.add("Authorization", "Bearer " + accessToken);

        headers.add("HTTP_CLIENT_IP", getClientIp());

        final UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

        try {
            final ResponseEntity<Resource> response = template.exchange(uriBuilder.build().encode().toUri(), HttpMethod.GET, new HttpEntity<>(headers), Resource.class);

            return response.getBody();
        } catch (HttpStatusCodeException e) {
            logger.warn(e.getStatusCode() + " : " + e.getResponseBodyAsString());
            if (Objects.equals(e.getStatusCode(), HttpStatus.UNAUTHORIZED))
                throw new UnauthorizedException(e.getStatusText(), e.getResponseHeaders(), e.getResponseBodyAsByteArray(), Charset.defaultCharset());
            throw e;
        }
    }

    protected <BODY> Resource getResourceResponseAll(String url) {
        url = (url.startsWith("http") ? "" : apiServerUrl) + url;

        final RestTemplate template = new RestTemplate(getFactory());
        template.getMessageConverters().add(0, new ResourceHttpMessageConverter());

        final HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        final String accessToken = getAccessToken();
        if (StringUtils.isNotEmpty(accessToken))
            headers.add("Authorization", "Bearer " + accessToken);

        headers.add("HTTP_CLIENT_IP", getClientIp());

        final UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

        try {
            final ResponseEntity<Resource> response = template.exchange(uriBuilder.build().encode().toUri(), HttpMethod.GET, new HttpEntity<>(headers), Resource.class);

            return response.getBody();
        } catch (HttpStatusCodeException e) {
            logger.warn(e.getStatusCode() + " : " + e.getResponseBodyAsString());
            if (Objects.equals(e.getStatusCode(), HttpStatus.UNAUTHORIZED))
                throw new UnauthorizedException(e.getStatusText(), e.getResponseHeaders(), e.getResponseBodyAsByteArray(), Charset.defaultCharset());
            throw e;
        }
    }

    protected String getAccessToken() {
        return (String) session.getAttribute(SESSION_ACCESS_TOKEN);
    }

    protected void setAccessToken(String accessToken) {
        session.setAttribute(SESSION_ACCESS_TOKEN, accessToken);
    }

    protected <BODY> void sendByMultipartFile(HttpMethod method, String url, BODY o, Map<String, FileResource> files) throws IOException, ResultFailException {
        sendByMultipartFile(method, url, o, jsonResultType(), files);
    }

    @SuppressWarnings("unchecked")
    protected <BODY, RESPONSE> RESPONSE sendByMultipartFile(HttpMethod method, String url, BODY o, Class<RESPONSE> klass, Map<String, FileResource> files) throws IOException, ResultFailException {
        return (RESPONSE) sendByMultipartFile(method, url, o, jsonResultType(klass), files);
    }

    protected <BODY> Object sendByMultipartFile(HttpMethod method, String url, BODY o, JavaType responseType, Map<String, FileResource> files) throws IOException, ResultFailException {
        final MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();

        if (o != null) {
            final Map<String, Object> params = objectMapper.convertValue(o, typeFactory.constructParametricType(Map.class, String.class, Object.class));
            params.forEach(parts::add);
        }
        files.forEach(parts::add);

        final RestTemplate restTemplate = new RestTemplate(getFactory());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        final String accessToken = getAccessToken();
        if (StringUtils.isNotEmpty(accessToken))
            headers.add("Authorization", "Bearer " + accessToken);

        final HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(parts, headers);

        try {
            final ResponseEntity<String> response = restTemplate.exchange(apiServerUrl + url, method, requestEntity, String.class);
            final JsonResult<?> result = objectMapper.readValue(response.getBody(), responseType);

            if (result.isFailure())
                throw new ResultFailException(result);
            return result.getData();
        } catch (HttpStatusCodeException e) {
            if (Objects.equals(e.getStatusCode(), HttpStatus.UNAUTHORIZED))
                throw new UnauthorizedException(e.getStatusText(), e.getResponseHeaders(), e.getResponseBodyAsByteArray(), Charset.defaultCharset());
            throw e;
        }
    }

    public String getClientIp() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getRemoteHost();
    }

    protected void uploadWebchatImageToGateway(String companyId, String fileName, TalkChannelType channelType) throws JsonProcessingException {
        HashMap<String, String> parameterMap = new HashMap<>();

        parameterMap.put("company_id", companyId);
        parameterMap.put("file_name", fileName);
        parameterMap.put("channel_type", channelType.getCode());

        getResponse(webchatUrl, parameterMap, HttpMethod.POST, false);
    }

    protected static class FileResource extends FileSystemResource {
        private final String filename;

        public FileResource(String filePath, String filename) {
            this(filePath, filename, true);
        }

        public FileResource(String filePath, String filename, boolean urlEncoded) {
            super(filePath);
            this.filename = urlEncoded ? UrlUtils.encode(filename) : filename;
        }

        @Override
        public String getFilename() {
            return filename;
        }
    }
}
