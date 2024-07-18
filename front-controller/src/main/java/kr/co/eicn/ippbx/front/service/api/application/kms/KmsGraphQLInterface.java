package kr.co.eicn.ippbx.front.service.api.application.kms;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.eicn.ippbx.front.config.RequestGlobal;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
//@RequiredArgsConstructor
public class KmsGraphQLInterface {
//    final private String KMS_GRAPHQL_AUTH_URL = "https://assist.eicn.co.kr:3006/api/assist/graphql";
//    final private String KMS_GRAPHQL_URL = "https://assist.eicn.co.kr/kms/graphql";

    final private RestTemplate restTemplate = new RestTemplate();
    final private ObjectMapper objectMapper = new ObjectMapper();


    @Autowired
    protected RequestGlobal g;

    @Autowired
    HttpSession session;

    @Autowired
    HttpServletRequest request;

    @Autowired
    HttpServletResponse response;


    @Value("${assist.kms.auth.url}")
    private String KMS_GRAPHQL_AUTH_URL;

    @Value("${assist.kms.api.url}")
    private String KMS_GRAPHQL_URL;

    @Value("${assist.sso.token.get.url}")
    private String GET_ASSIST_TOKEN_SSO_URL;

    /**
     * 인증 토큰을 받아온다.
     */
//    public void getAuthenticate(HttpSession session) throws JsonProcessingException {
//        if(g.getKmsToken() == null) {
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//
////            String sessionId = session.getId();
////            HashMap<String, String> getData = getSSOToken(sessionId);
////            String token = getData.get("result_data");
//
////            if(token == null){
////                throw new IllegalStateException("kms로 보내려는 token이 존재하지 않습니다, (sessionId를 사용해 발급받는 token이 정상적이지 않음.)");
////            }
//
//            // 원래 사용하면 company, id, password 방식에 로그인
//            String query = "mutation { authenticate(companyLoginId:\""+ g.getUser().getCompanyId() +"\",loginId:\""+ g.getUser().getId() +"\",password:\""+ g.getPassword() +"\") {kmsToken}}";
////            String query = "mutation { authenticateViaToken(companyLoginId:\""+ g.getUser().getCompanyId() +"\",token:\""+ token +"\") {kmsToken}}";
//            Map<String, Object> variables = Collections.emptyMap();
//            Map<String, Object> body = new HashMap<>();
//            body.put("query", query);
//            body.put("variables", variables);
//            log.info("query = {}", query);
//
//            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
//            ResponseEntity<AuthenticateClass> responseEntity = restTemplate.postForEntity(KMS_GRAPHQL_AUTH_URL, requestEntity, AuthenticateClass.class);
//
//            if (responseEntity.getStatusCode().is2xxSuccessful()) {
//                g.setKmsToken(responseEntity.getBody().getData().getAuthenticateViaToken().getKmsToken());
//                log.info("발급 받은 KMS 토큰 = {}", g.getKmsToken());
//            } else {
//                System.out.println("Failed to call GraphQL API");
//            }
//        }
//    }

    /**
     * 인증 토큰을 받아온다.
     */
    public void getAuthenticate() {
        if(g.getKmsToken() == null) {
            log.info("getAuthenticate 호출@");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String query = "mutation { authenticate(companyLoginId:\""+ g.getUser().getCompanyId() +"\",loginId:\""+ g.getUser().getId() +"\",password:\""+ g.getPassword() +"\") {kmsToken}}";
            Map<String, Object> variables = Collections.emptyMap();
            Map<String, Object> body = new HashMap<>();
            body.put("query", query);
            body.put("variables", variables);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            ResponseEntity<AuthenticateClass> responseEntity = restTemplate.postForEntity(KMS_GRAPHQL_AUTH_URL, requestEntity, AuthenticateClass.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                g.setKmsToken(responseEntity.getBody().getData().getAuthenticate().getKmsToken());
            } else {
                System.out.println("Failed to call GraphQL API");
            }
        }
    }

    /**
     * 인증 토큰을 받고 쿠키에 등록 한다.
     */
    public void getAuthenticateWithCookie(HttpServletRequest request, HttpServletResponse response) {
        log.info("getAuthenticateWithCookie 호출@");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String query = "mutation { authenticate(companyLoginId:\""+ g.getUser().getCompanyId() +"\",loginId:\""+ g.getUser().getId() +"\",password:\""+ g.getPassword() +"\") {kmsToken}}";
        Map<String, Object> variables = Collections.emptyMap();
        Map<String, Object> body = new HashMap<>();
        body.put("query", query);
        body.put("variables", variables);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<AuthenticateClass> responseEntity = restTemplate.postForEntity(KMS_GRAPHQL_AUTH_URL, requestEntity, AuthenticateClass.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            g.setKmsToken(responseEntity.getBody().getData().getAuthenticate().getKmsToken());

            // kmsToken이 쿠키에 없으면, 쿠키 생성 후 저장
            Cookie[] cookies = request.getCookies();
            Optional<Cookie> findkmsTokenCookieOptional = Arrays.stream(cookies).filter(cookie -> StringUtils.equals(cookie.getName(), "kmsToken")).findFirst();
            if(findkmsTokenCookieOptional.isEmpty() && g != null && StringUtils.isNotEmpty(g.getKmsToken())){
                Cookie kmsTokenCookie = new Cookie("kmsToken", responseEntity.getBody().getData().getAuthenticate().getKmsToken());
                kmsTokenCookie.setMaxAge(24 * 60 * 60); // 쿠키 유효 시간 (예: 1일)
                kmsTokenCookie.setPath("/"); // 쿠키의 유효 경로 설정 (루트 경로로 설정)
                kmsTokenCookie.setHttpOnly(true); // JavaScript를 통한 접근 방지
                kmsTokenCookie.setSecure(true); // HTTPS를 통해서만 쿠키 전송
                response.addCookie(kmsTokenCookie);
                log.info("kmsTokenCookie 생성 및 추가 완료");
            }
        } else {
            System.out.println("Failed to call GraphQL API");
        }
    }

    @Data
    static class SSOResult{
        private String result;
        private String result_data;
    }

    /**
     * 상담지원 시스템간 SSO - KMS가 아닌 EICN 서버로 부터 발급 받는 token
     * @return
     */
    public HashMap<String, String> getSSOToken(String sessionId) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> variables = Collections.emptyMap();
        Map<String, Object> body = new HashMap<>();
        body.put("company_id", g.getUser().getCompanyId());
        body.put("userid", g.getUser().getId());
        body.put("session_id", sessionId);
        String requestBody = objectMapper.writeValueAsString(body);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<SSOResult> ssoResultResponseEntity = restTemplate.postForEntity(GET_ASSIST_TOKEN_SSO_URL, requestEntity, SSOResult.class);

        HashMap<String, String> result = new HashMap<>();


        if (ssoResultResponseEntity.getStatusCode().is2xxSuccessful()) {
            log.info("SSO getToken success");
            log.info(String.valueOf(ssoResultResponseEntity.getBody()));
            result.put("result", ssoResultResponseEntity.getBody().result);
            result.put("result_data", ssoResultResponseEntity.getBody().result_data);
        } else {
            log.info("SSO getToken failure");
            log.info(String.valueOf(ssoResultResponseEntity.getBody()));
            result.put("result", "failure");
            result.put("result_data", "");
        }
        return result;
    }

    /**
     * 로그인이 가능한지 확인 (해당 사용자가 KMS에 로그인 가능한 상태 인지 확인)
     */
    public Boolean loginAvailabilityCheck() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
//        String sessionId = session.getId();
//        HashMap<String, String> getData = getSSOToken(sessionId);
//        String token = getData.get("result_data");

//        if(token == null){
//            throw new IllegalStateException("kms로 보내려는 token이 존재하지 않습니다, (sessionId를 사용해 발급받는 token이 정상적이지 않음.)");
//        }

        // id, pw로 KMS 토큰 받는 버전
        String query = "mutation { authenticate(companyLoginId:\""+ g.getUser().getCompanyId() +"\",loginId:\""+ g.getUser().getId() +"\",password:\""+ g.getPassword() +"\") {kmsToken}}";
        // 상무님한테 발급받은 세션토큰으로 KMS 토큰 받는 버전
//        String query = "mutation { authenticateViaToken(companyLoginId:\""+ g.getUser().getCompanyId() +"\",token:\""+ token +"\") {kmsToken}}";
        Map<String, Object> variables = Collections.emptyMap();
        Map<String, Object> body = new HashMap<>();
        body.put("query", query);
        body.put("variables", variables);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<AuthenticateClass> responseEntity = restTemplate.postForEntity(KMS_GRAPHQL_AUTH_URL, requestEntity, AuthenticateClass.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            if(responseEntity.getBody().getData() == null){
                log.info("KMS 로그인 실패");
                return false;
            }else{
                String kmsToken = responseEntity.getBody().getData().getAuthenticate().getKmsToken();
//                String kmsToken = responseEntity.getBody().getData().getAuthenticateViaToken().getKmsToken();
                log.info("loginAvailabilityCheck > kmsToken = {}", kmsToken);
                return true;
            }
        } else {
            log.info("loginAvailabilityCheck 실패, http code가 200이 아닙니다.");
            log.info("responseEntity.getStatusCode()={}", responseEntity.getStatusCode());
            return false;
        }
    }

    /**
     * 발급 받은 kmsToken이 있는지 확인 후, 없으면 kmsToken 발급 요청
     * @throws JsonProcessingException
     */
//    private void checkExistsKmsAccessToken() throws JsonProcessingException {
//        if(StringUtils.isEmpty(g.getKmsToken())){
//            getAuthenticate(session);
//        }
//    }

    /**
     * 카테고리 정보를 가져온다.
     */
    public GetKnowledgeCategoriesClass getCategory() throws JsonProcessingException {
//        checkExistsKmsAccessToken();
        getAuthenticate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(g.getKmsToken());

        String query = "query { getKnowledgeCategories { id, level, parent, name, knowledgeCount, displaySequence } }";
        Map<String, Object> variables = Collections.emptyMap();
        Map<String, Object> body = new HashMap<>();
        body.put("query", query);
        body.put("variables", variables);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<GetKnowledgeCategoriesClass> responseEntity = restTemplate.postForEntity(KMS_GRAPHQL_URL, requestEntity, GetKnowledgeCategoriesClass.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        } else {
            System.out.println("Failed to call GraphQL API");
            return null;
        }
    }

    /**
     * 지식 정보를 가져온다.
     */
    public GetKnowledgeList getSearchKnowledge(String keyword, Integer category) throws JsonProcessingException {
//        checkExistsKmsAccessToken();
        getAuthenticate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(g.getKmsToken());

        String keywordQuery = StringUtils.isNotEmpty(keyword) ? keyword : "";
        String categoryQuery = category != null ? ", category: " + category : "";

        String query = "query { searchKnowledgeBySolr(keywords: \""+ keywordQuery +"\""+ categoryQuery +", limit: 100) { rows { id\n" +
                "            category\n" +
                "            title\n" +
                "            content\n" +
                "            hits\n" +
                "            liked\n" +
                "            likes\n" +
                "            updatedAt\n" +
                "            visible\n" +
                "            tags\n" +
                "            bookmarked\n" +
                "            bookmarkedCount } } }";
        Map<String, Object> variables = Collections.emptyMap();
        Map<String, Object> body = new HashMap<>();
        body.put("query", query);
        body.put("variables", variables);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<GetKnowledgeList> responseEntity = restTemplate.postForEntity(KMS_GRAPHQL_URL, requestEntity, GetKnowledgeList.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        } else {
            System.out.println("Failed to call GraphQL API");
            return null;
        }
    }

    public GetKnowledgeList getSearchKnowledge(String keyword, Integer category, String sort, String searchTypeFlag) throws JsonProcessingException {
//        checkExistsKmsAccessToken();
        getAuthenticate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(g.getKmsToken());

        String keywordQuery = StringUtils.isNotEmpty(keyword) ? keyword : "";
        String categoryQuery = category != null ? ", category: " + category : "";
        String sortQuery = StringUtils.isNotEmpty(sort) ? ", sort: " + sort : "";
        String searchTypeQuery = StringUtils.equals(searchTypeFlag, "tag") ? "searchType: TAG," : "";

        // 원본 String query = "query { searchKnowledgeBySolr(keywords: \""+ keywordQuery +"\""+ categoryQuery +", limit: 100) { rows { id\n" +
        String query = "query { searchKnowledgeBySolr("+searchTypeQuery+" keywords: \""+ keywordQuery +"\""+ categoryQuery + sortQuery + ", limit: 100) { rows { id\n" +
                "            category\n" +
                "            title\n" +
                "            content\n" +
                "            hits\n" +
                "            liked\n" +
                "            likes\n" +
                "            updatedAt\n" +
                "            visible\n" +
                "            tags\n" +
                "            bookmarked\n" +
                "            bookmarkedCount } } }";
        Map<String, Object> variables = Collections.emptyMap();
        Map<String, Object> body = new HashMap<>();
        body.put("query", query);
        body.put("variables", variables);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<GetKnowledgeList> responseEntity = restTemplate.postForEntity(KMS_GRAPHQL_URL, requestEntity, GetKnowledgeList.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        } else {
            System.out.println("Failed to call GraphQL API");
            return null;
        }
    }

    /**
     * 최근 확인한 지식 정보를 가져온다.
     */
    public GetRecentChangedKnowledgeList getRecentChangedKnowledge() throws JsonProcessingException {
//        checkExistsKmsAccessToken();
        getAuthenticate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(g.getKmsToken());

        String query = "query { getRecentChangedKnowledge { rows { id\n" +
                "            category\n" +
                "            title\n" +
                "            content\n" +
                "            hits\n" +
                "            liked\n" +
                "            likes\n" +
                "            updatedAt\n" +
                "            visible\n" +
                "            tags\n" +
                "            bookmarked\n" +
                "            bookmarkedCount } } }";
        Map<String, Object> variables = Collections.emptyMap();
        Map<String, Object> body = new HashMap<>();
        body.put("query", query);
        body.put("variables", variables);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<GetRecentChangedKnowledgeList> responseEntity = restTemplate.postForEntity(KMS_GRAPHQL_URL, requestEntity, GetRecentChangedKnowledgeList.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        } else {
            System.out.println("Failed to call GraphQL API");
            return null;
        }
    }

    /**
     * 북마크 지식 정보를 가져온다.
     */
    public GetMyBookmarkedKnowledgeList getMyBookmarkedKnowledge() throws JsonProcessingException {
//        checkExistsKmsAccessToken();
        getAuthenticate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(g.getKmsToken());

        String query = "query { getMyBookmarkedKnowledge { rows { knowledge { id\n" +
                "            category\n" +
                "            title\n" +
                "            content\n" +
                "            hits\n" +
                "            liked\n" +
                "            likes\n" +
                "            updatedAt\n" +
                "            visible\n" +
                "            tags\n" +
                "            bookmarked\n" +
                "            bookmarkedCount } } } }";
        Map<String, Object> variables = Collections.emptyMap();
        Map<String, Object> body = new HashMap<>();
        body.put("query", query);
        body.put("variables", variables);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<GetMyBookmarkedKnowledgeList> responseEntity = restTemplate.postForEntity(KMS_GRAPHQL_URL, requestEntity, GetMyBookmarkedKnowledgeList.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        } else {
            System.out.println("Failed to call GraphQL API");
            return null;
        }
    }

    /**
     * 지식 디테일을 조회한다.
     */
    public GetKnowledgeDetail getSearchIdKnowledge(Integer id) throws JsonProcessingException {
//        checkExistsKmsAccessToken();
        getAuthenticate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(g.getKmsToken());

        String query = "query { getKnowledge(id: "+ id +") { id\n" +
                "            category\n" +
                "            title\n" +
                "            content\n" +
                "            hits\n" +
                "            liked\n" +
                "            likes\n" +
                "            updatedAt\n" +
                "            visible\n" +
                "            tags\n" +
                "            bookmarked\n" +
                "            files {id}\n" +
                "            bookmarkedCount } }";
        Map<String, Object> variables = Collections.emptyMap();
        Map<String, Object> body = new HashMap<>();
        body.put("query", query);
        body.put("variables", variables);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<GetKnowledgeDetail> responseEntity = restTemplate.postForEntity(KMS_GRAPHQL_URL, requestEntity, GetKnowledgeDetail.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        } else {
            System.out.println("Failed to call GraphQL API");
            return null;
        }
    }

    /**
     * 기간별 탑랭킹을 조회 한다. (조회랭킹)
     */
    public GetTopHitKnowledgeList getTopHitKnowledge(String startDate, String endDate) throws JsonProcessingException {
//        checkExistsKmsAccessToken();
        getAuthenticate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(g.getKmsToken());

        String query = "query { getTopHitKnowledge(startDate:\"" + startDate +"\", endDate:\""+ endDate +"\") { knowledge { id\n" +
                "            category\n" +
                "            title\n" +
                "            content\n" +
                "            hits\n" +
                "            liked\n" +
                "            likes\n" +
                "            updatedAt\n" +
                "            visible\n" +
                "            tags\n" +
                "            bookmarked\n" +
                "            bookmarkedCount } } }";
        Map<String, Object> variables = Collections.emptyMap();
        Map<String, Object> body = new HashMap<>();
        body.put("query", query);
        body.put("variables", variables);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<GetTopHitKnowledgeList> responseEntity = restTemplate.postForEntity(KMS_GRAPHQL_URL, requestEntity, GetTopHitKnowledgeList.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        } else {
            System.out.println("Failed to call GraphQL API");
            return null;
        }
    }

    /**
     * 기간별 탑 랭킹 태그를 조회한다. (키워드랭킹)
     */
    public GetTopHitKnowledgeTagsList getTopHitKnowledgeTags(String startDate, String endDate) throws JsonProcessingException {
//        checkExistsKmsAccessToken();
        getAuthenticate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(g.getKmsToken());

        String query = "query { getTopHitKnowledgeTags(startDate:\"" + startDate +"\", endDate:\""+ endDate +"\", limit: 100) { tag\n" +
                "            hits } }";
        Map<String, Object> variables = Collections.emptyMap();
        Map<String, Object> body = new HashMap<>();
        body.put("query", query);
        body.put("variables", variables);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<GetTopHitKnowledgeTagsList> responseEntity = restTemplate.postForEntity(KMS_GRAPHQL_URL, requestEntity, GetTopHitKnowledgeTagsList.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        } else {
            System.out.println("Failed to call GraphQL API");
            return null;
        }
    }

    /**
     * 좋아요
     */
    public GetLikedResult likeKnowledge(Integer id, boolean like) throws JsonProcessingException {
//        checkExistsKmsAccessToken();
        getAuthenticate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(g.getKmsToken());


        String likeFlag = like == true ? "likeKnowledge" : "unlikeKnowledge";
        String query = "mutation {\r\n    " + likeFlag + "(id:  "+ id + ")\r\n}";
        Map<String, Object> variables = Collections.emptyMap();
        Map<String, Object> body = new HashMap<>();
        body.put("query", query);
        body.put("variables", variables);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<GetLikedResult> responseEntity = restTemplate.postForEntity(KMS_GRAPHQL_URL, requestEntity, GetLikedResult.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        } else {
            System.out.println("Failed to call GraphQL API");
            return null;
        }
    }

    /**
     * 북마크
     */
    public GetBookmarkedResult bookmarkKnowledge(Integer id, boolean bookmark) throws JsonProcessingException {
//        checkExistsKmsAccessToken();
        getAuthenticate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(g.getKmsToken());


        String bookmarkFlag = bookmark == true ? "bookmarkKnowledge" : "unbookmarkKnowledge";
        String query = "mutation {\r\n    " + bookmarkFlag + "(id:  "+ id + ")\r\n}";
        Map<String, Object> variables = Collections.emptyMap();
        Map<String, Object> body = new HashMap<>();
        body.put("query", query);
        body.put("variables", variables);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<GetBookmarkedResult> responseEntity = restTemplate.postForEntity(KMS_GRAPHQL_URL, requestEntity, GetBookmarkedResult.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        } else {
            System.out.println("Failed to call GraphQL API");
            return null;
        }
    }

    /**
     * 코멘트 등록 하기
     */
    public CreateKnowledgeUpdateRequestResult createComment(Integer id, String content) throws JsonProcessingException {
//        checkExistsKmsAccessToken();
        getAuthenticate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + g.getKmsToken());

        String query = "mutation { createKnowledgeUpdateRequest(knowledge: "+id+", content: \"" + content +"\") { id }}";
        Map<String, Object> variables = Collections.emptyMap();
        Map<String, Object> body = new HashMap<>();
        body.put("query", query);
        body.put("variables", variables);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<CreateKnowledgeUpdateRequestResult> responseEntity = restTemplate.postForEntity(KMS_GRAPHQL_URL, requestEntity, CreateKnowledgeUpdateRequestResult.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        } else {
            System.out.println("Failed to call GraphQL API");
            return null;
        }
    }

    // 코멘트 수정 요청 리스트 가져오기 (관리자 화면)
    public SearchKnowledgeUpdateRequestResponse searchKnowledgeUpdateRequestForAdmin() throws JsonProcessingException {
//        checkExistsKmsAccessToken();
        getAuthenticate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + g.getKmsToken());
        String query = "query {searchKnowledgeUpdateRequest(\n" +
                "  replied:false,\n" +
                "  sortDirection:ASC\n" +
                "){\n" +
                "  rows {\n" +
                "    createdAt,\n" +
                "    knowledge{\n" +
                "      category,\n" +
                "      title,\n" +
                "      id,\n" +
                "      content\n" +
                "    }\n" +
                "    requester,\n" +
                "    requesterName,\n" +
                "    requestContent,\n" +
                "    replyContent,\n" +
                "  }\n" +
                "} }";
        Map<String, Object> variables = Collections.emptyMap();
        Map<String, Object> body = new HashMap<>();
        body.put("query", query);
        body.put("variables", variables);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<SearchKnowledgeUpdateRequestResponse> responseEntity = restTemplate.postForEntity(KMS_GRAPHQL_URL, requestEntity, SearchKnowledgeUpdateRequestResponse.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        } else {
            System.out.println("Failed to call GraphQL API");
            return null;
        }
    }

    // 코멘트 수정 요청 리스트 가져오기 (상담사 화면)
    public SearchKnowledgeUpdateRequestResponse searchKnowledgeUpdateRequestForCounsel(String commentFilterType) throws JsonProcessingException {
//        checkExistsKmsAccessToken();
        getAuthenticate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + g.getKmsToken());

        String filterType = "";
        // 답변 완료만 가져오기
        if(StringUtils.equals(commentFilterType, "replied")){
            filterType = "replied: true,";
        }
        // 미답변만 가져오기
        if(StringUtils.equals(commentFilterType, "not-replied")){
            filterType = "replied: false,";
        }

        String query = "query {searchKnowledgeUpdateRequest(\n" +
                filterType + "\n" +
                "  sortDirection:ASC\n" +
                "){\n" +
                "  rows {\n" +
                "    createdAt,\n" +
                "    knowledge{\n" +
                "      category,\n" +
                "      title,\n" +
                "      id,\n" +
                "      content\n" +
                "    }\n" +
                "    requester,\n" +
                "    requesterName,\n" +
                "    requestContent,\n" +
                "    replyContent,\n" +
                "  }\n" +
                "} }";
        Map<String, Object> variables = Collections.emptyMap();
        Map<String, Object> body = new HashMap<>();
        body.put("query", query);
        body.put("variables", variables);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<SearchKnowledgeUpdateRequestResponse> responseEntity = restTemplate.postForEntity(KMS_GRAPHQL_URL, requestEntity, SearchKnowledgeUpdateRequestResponse.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        } else {
            System.out.println("Failed to call GraphQL API");
            return null;
        }
    }


    // id,pw 버전
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class AuthenticateClass {
        private Authenticate data;
    }
    @Data
    public static class Authenticate {
        private KmsToken authenticate;
    }
    @Data
    public static class KmsToken {
        private String kmsToken;
    }

    // sessionId 토큰 버전
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    @Data
//    public static class AuthenticateClass {
//        private Authenticate data;
//    }
//    @Data
//    public static class Authenticate {
//        private AuthenticateViaToken authenticateViaToken;
//    }
//    @Data
//    public static class AuthenticateViaToken {
//        private String kmsToken;
//    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class GetKnowledgeCategoriesClass {
        private GetKnowledgeCategories data;
    }

    @Data
    public static class GetKnowledgeCategories {
        private List<GetKnowledgeCategory> getKnowledgeCategories;
    }

    @Data
    public static class GetKnowledgeCategory {
        private Integer id;
        private Integer level;
        private Integer parent;
        private String name;
        private Integer knowledgeCount;
        private Integer displaySequence;
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class GetKnowledgeList {
        private GetKnowledges data;
    }

    @Data
    public static class GetKnowledges {
        private GetKnowledge searchKnowledgeBySolr;
    }

    @Data
    public static class GetKnowledge {
        private List<Knowledge> rows;
    }

    @Data
    public static class Knowledge {
        private Integer id;
        private Integer category;
        private String title;
        private String content;
        private Integer hits;
        private boolean liked;
        private Integer likes;
        private Timestamp updatedAt;
        private boolean visible;
        private List<String> tags;
        private boolean bookmarked;
        private Integer bookmarkedCount;
        private List<HashMap<String, Object>> files;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class GetKnowledgeDetail {
        private GetKnowledgeData data;
    }

    @Data
    public static class GetKnowledgeData {
        private Knowledge getKnowledge;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class GetRecentChangedKnowledgeList {
        private GetRecentChangedKnowledge data;
    }

    @Data
    public static class GetRecentChangedKnowledge {
        private GetKnowledge getRecentChangedKnowledge;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class GetMyBookmarkedKnowledgeList {
        private GetMyBookmarkedKnowledge data;
    }

    @Data
    public static class GetMyBookmarkedKnowledge {
        private GetMyBookmarkedKnowledgeRows getMyBookmarkedKnowledge;
    }

    @Data
    public static class GetMyBookmarkedKnowledgeRows {
        private List<KnowledgeItem> rows;
    }

    @Data
    public static class KnowledgeItem {
        private Knowledge knowledge;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class GetTopHitKnowledgeList {
        private GetTopHitKnowledgeItem data;
    }

    @Data
    public static class GetTopHitKnowledgeItem {
        private List<GetTopHitKnowledge> getTopHitKnowledge;
    }

    @Data
    public static class GetTopHitKnowledge {
        private Knowledge knowledge;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class GetTopHitKnowledgeTagsList {
        private GetTopHitKnowledgeTags data;
    }

    @Data
    public static class GetTopHitKnowledgeTags {
        private List<KnowledgeTags> getTopHitKnowledgeTags;
    }

    @Data
    public static class KnowledgeTags {
        private String tag;
        private Integer hits;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class GetLikedResult {
        private GetLiked data;
    }

    @Data
    public static class GetLiked {
        private String likeKnowledge;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class GetBookmarkedResult {
        private GetLiked data;
    }

    @Data
    public static class GetBookmarked {
        private String bookmarkKnowledge;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class CreateKnowledgeUpdateRequestResult {
        private CreateKnowledgeUpdateRequestData data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class CreateKnowledgeUpdateRequestData {
        private CreateKnowledgeUpdateRequest createKnowledgeUpdateRequest;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class  CreateKnowledgeUpdateRequest {
        private Integer id;
    }

    @Data
    public static class  SearchKnowledgeUpdateRequestResponse {
        private SearchKnowledgeUpdateRequestData data;
    }

    @Data
    public static class SearchKnowledgeUpdateRequestData {
        private SearchKnowledgeUpdateRequest searchKnowledgeUpdateRequest;


    }
    @Data
    public static class SearchKnowledgeUpdateRequest {
        private List<SearchKnowledgeUpdate> rows;
    }

    @Data
    public static class SearchKnowledgeUpdate {
        private Long createdAt;
        private String formatCreatedAt;
        private Knowledge knowledge;
        private Integer requester;
        private String requesterName;
        private String requestContent;
        private String replyContent;
        private String categoryName;
    }
}
