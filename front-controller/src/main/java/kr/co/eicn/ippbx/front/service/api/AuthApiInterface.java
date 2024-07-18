package kr.co.eicn.ippbx.front.service.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.eicn.ippbx.front.config.RequestGlobal;
import kr.co.eicn.ippbx.front.model.ArsAuthInfo;
import kr.co.eicn.ippbx.front.model.form.LoginForm;
import kr.co.eicn.ippbx.model.dto.eicn.PersonListSummary;
import kr.co.eicn.ippbx.model.entity.eicn.CompanyServerEntity;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SipBuddies;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AuthApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(AuthApiInterface.class);

    @Autowired
    HttpSession httpSession;

    final private RestTemplate restTemplate = new RestTemplate();
    final private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    protected RequestGlobal g;

    @Value("${assist.login.record.user}")
    private String LOGIN_USER_RECORD_URL;

    public void login(LoginForm form) throws IOException, ResultFailException {
        final String accessToken = getData(HttpMethod.POST, "/auth/authenticate", form, String.class, false).getData();
        setAccessToken(accessToken);
    }

    public void login(PersonListSummary user) throws IOException, ResultFailException {
        final String accessToken = getData(HttpMethod.POST, "/auth/mc-only", user, String.class, false).getData();
        setAccessToken(accessToken);
    }

    public void logout() throws IOException, ResultFailException {
        getResult("/auth/logout", null);
    }

    public List<CompanyServerEntity> getServer() throws IOException, ResultFailException {
        return getList("/api/ars-auth/server-info/", null, CompanyServerEntity.class).getData();
    }

    public ArsAuthInfo getArsAuth(String userId) throws IOException, ResultFailException {
        return getData("/api/ars-auth/" + userId, null, ArsAuthInfo.class).getData();
    }

    public SipBuddies getSoftPhoneAuth(String peer) throws IOException, ResultFailException {
        return getData("/api/softphone-auth/" + peer, null, SipBuddies.class).getData();
    }

    public void update(String peer, String secret)  throws IOException, ResultFailException {
        put("/api/softphone-auth/" + peer + "/" + secret , null);
    }

    public String getAccessToken() {
        return super.getAccessToken();
    }

    @Data
    static class SSOResult{
        private String result;
        private String result_data;
    }

    public void recordLoginUserSessionId() throws IOException, ResultFailException {
        String sessionId = httpSession.getId();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> variables = Collections.emptyMap();
        Map<String, Object> body = new HashMap<>();
        body.put("company_id", g.getUser().getCompanyId());
        body.put("userid", g.getUser().getId());
        body.put("session_id", sessionId);
        String requestBody = objectMapper.writeValueAsString(body);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        // todo - application.yml에서 받는 LOGIN_USER_RECORD_URL을 나중에 daemon_info 에서 가져오게 변경 해야 함
        ResponseEntity<SSOResult> ssoResultResponseEntity = restTemplate.postForEntity(LOGIN_USER_RECORD_URL, requestEntity, SSOResult.class);
        logger.info(String.valueOf(ssoResultResponseEntity.getBody()));
        if(ssoResultResponseEntity.getStatusCode().is2xxSuccessful()){
            logger.info("login sessionId record success");
        }else{
            logger.info("login sessionId record failure");
        }
    }
}
