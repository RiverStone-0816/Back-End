package kr.co.eicn.ippbx.front.service.api.atcenter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.eicn.ippbx.model.dto.atcenter.ChatRoomResponse;
import kr.co.eicn.ippbx.model.dto.atcenter.ChattingResponse;
import kr.co.eicn.ippbx.model.dto.atcenter.SystemCodeResponse;
import kr.co.eicn.ippbx.model.dto.atcenter.UserCompanyResponse;
import kr.co.eicn.ippbx.model.dto.atcenter.UserTradeResponse;
import kr.co.eicn.ippbx.model.form.atcenter.ChattFormRequest;
import kr.co.eicn.ippbx.model.form.atcenter.CustomResultFormRequest;
import kr.co.eicn.ippbx.model.search.atcenter.AtCenterSearchRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;


@Slf4j
@Service
public class AtCenterService {
    private static final Logger logger = LoggerFactory.getLogger(AtCenterService.class);

    @Value("${atcenter.api.url}")
    private String host;

    @Value("${atcenter.auth.srvcKey}")
    private String srvcKey;

    @Value("${atcenter.auth.scrKey}")
    private String scrKey;

    public List<UserCompanyResponse.DetailResponse> getMembers(AtCenterSearchRequest searchRequest) {
        final String uri = UriComponentsBuilder.fromUriString(host + "/api/v1/admin/cs/coun/controller/custInfo/")
                .queryParam("SRVC_KEYVAL", srvcKey)
                .queryParam("SCR_KEYVAL", scrKey)
                .toUriString();

        UserCompanyResponse data = postForEntityAndParse(uri, searchRequest, UserCompanyResponse.class, "고객 및 업체 정보 조회");

        if (Objects.equals(data.getResultCode(), "00"))
            return data.getData();

        logger.info("code: " + data.getResultCode() + " message: " + data.getResultMessage());
        throw new RuntimeException(data.getResultMessage());
    }

    public List<SystemCodeResponse.DetailResponse> getCodes(AtCenterSearchRequest.AtCenterSearchCodeRequest searchRequest) {
        final String uri = UriComponentsBuilder.fromUriString(host + "/api/v1/admin/cs/coun/controller/cnsltCD/")
                .queryParam("SRVC_KEYVAL", srvcKey)
                .queryParam("SCR_KEYVAL", scrKey)
                .toUriString();

        SystemCodeResponse data = postForEntityAndParse(uri, searchRequest, SystemCodeResponse.class, "시스템 코드 조회");

        if (Objects.equals(data.getResultCode(), "00"))
            return data.getData();

        logger.info("code: " + data.getResultCode() + " message: " + data.getResultMessage());
        throw new RuntimeException(data.getResultMessage());
    }

    public UserTradeResponse getTrades(AtCenterSearchRequest.AtCenterSearchTradeRequest searchRequest) {
        final String uri = UriComponentsBuilder.fromUriString(host + "/api/v1/admin/cs/coun/controller/custTrdInfo/")
                .queryParam("SRVC_KEYVAL", srvcKey)
                .queryParam("SCR_KEYVAL", scrKey)
                .toUriString();

        UserTradeResponse data = postForEntityAndParse(uri, searchRequest, UserTradeResponse.class, "고객 거래 정보 조회");

        if (Objects.equals(data.getResultCode(), "00"))
            return data;

        logger.info("code: " + data.getResultCode() + " message: " + data.getResultMessage());
        throw new RuntimeException(data.getResultMessage());
    }

    // todo:: 403 error 발생
    public void updateResultEvent(CustomResultFormRequest request) {
        final String uri = UriComponentsBuilder.fromUriString(host + "/api/v1/admin/cs/coun/controller/cnsltDtl/")
                .queryParam("SRVC_KEYVAL", srvcKey)
                .queryParam("SCR_KEYVAL", scrKey)
                .toUriString();

        try {
            ResponseEntity<String> response = new RestTemplate().exchange(uri,
                    HttpMethod.PUT,
                    new HttpEntity<>(request),
                    String.class);

            logger.info("---> " + response);

        } catch (RuntimeException e) {
            throw new IllegalStateException("상담 정보 수신 실패: " + e.getMessage());
        }
    }

    public List<ChatRoomResponse.DetailResponse> getChatRoom(AtCenterSearchRequest.AtCenterSearchChatRoomRequest searchRequest) {
        final String uri = UriComponentsBuilder.fromUriString(host + "/api/v1/admin/cs/mesg/controller/chatRoom/")
                .queryParam("SRVC_KEYVAL", srvcKey)
                .queryParam("SCR_KEYVAL", scrKey)
                .toUriString();

        ChatRoomResponse data = postForEntityAndParse(uri, searchRequest, ChatRoomResponse.class, "1:1 채팅방 조회");

        if (Objects.equals(data.getResultCode(), "00"))
            return data.getData();

        logger.info("code: " + data.getResultCode() + " message: " + data.getResultMessage());
        throw new RuntimeException(data.getResultMessage());
    }

    public List<ChattingResponse.DetailResponse> getChats(AtCenterSearchRequest.AtCenterSearchChatRequest searchRequest) {
        final String uri = UriComponentsBuilder.fromUriString(host + "/api/v1/admin/cs/mesg/controller/chatMessage/")
                .queryParam("SRVC_KEYVAL", srvcKey)
                .queryParam("SCR_KEYVAL", scrKey)
                .toUriString();

        ChattingResponse data = postForEntityAndParse(uri, searchRequest, ChattingResponse.class, "1:1 채팅 조회");

        if (Objects.equals(data.getResultCode(), "00"))
            return data.getData();

        logger.info("code: " + data.getResultCode() + " message: " + data.getResultMessage());
        throw new RuntimeException(data.getResultMessage());
    }

    // todo:: 403 error 발생
    public void updateChats(ChattFormRequest request) {
        final String uri = UriComponentsBuilder.fromUriString(host + "/api/v1/admin/cs/mesg/controller/chatMessage/")
                .queryParam("SRVC_KEYVAL", srvcKey)
                .queryParam("SCR_KEYVAL", scrKey)
                .toUriString();

        try {
            ResponseEntity<String> response = new RestTemplate().exchange(uri,
                    HttpMethod.PUT,
                    new HttpEntity<>(request),
                    String.class);

            logger.info("---> " + response);

        } catch (RuntimeException e) {
            throw new IllegalStateException("1:1 채팅 저장 실패: " + e.getMessage());
        }
    }


    private <T> T postForEntityAndParse(String uri, Object request, Class<T> responseType, String exceptionMsg) {
        try {
            final ResponseEntity<?> response = new RestTemplate().postForEntity(uri, request, String.class);
            return new ObjectMapper().readValue(Objects.requireNonNull(response.getBody()).toString(), responseType);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            throw new RuntimeException("JSON 변환 에러");
        } catch (RuntimeException e) {
            throw new IllegalStateException(exceptionMsg + " 실패: " + e.getMessage());
        }
    }
}
