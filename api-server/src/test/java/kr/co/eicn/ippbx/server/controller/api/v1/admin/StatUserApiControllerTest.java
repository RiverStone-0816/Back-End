package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
public class StatUserApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/stat/user";

    private FieldDescriptor[] responseFields = new FieldDescriptor[]{
            fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
            fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
            fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional(),
            fieldWithPath("data").type(JsonFieldType.ARRAY).description("반환 데이터").optional()
    };

    private FieldDescriptor[] statUserResponse = new FieldDescriptor[]{
            fieldWithPath("standardTimeUnit").type(JsonFieldType.STRING).description("검색날").optional(),
            fieldWithPath("totalCnt").type(JsonFieldType.NUMBER).description("총 건수").optional(),
            fieldWithPath("totalBillSec").type(JsonFieldType.NUMBER).description("총 시간").optional(),
            fieldWithPath("outboundResponse").type(JsonFieldType.ARRAY).description("아웃바운드 통계").optional(),
            fieldWithPath("inboundResponse").type(JsonFieldType.ARRAY).description("인바운드 통계").optional(),
            fieldWithPath("memberStatusResponse").type(JsonFieldType.ARRAY).description("후처리 통계").optional()
    };

    private FieldDescriptor[] outboundResponse = new FieldDescriptor[]{
            fieldWithPath("statDate").type(JsonFieldType.STRING).description("통계시작날짜").optional(),
            fieldWithPath("statHour").type(JsonFieldType.NUMBER).description("통계종료날짜").optional(),
            fieldWithPath("groupCode").type(JsonFieldType.STRING).description("그룹code").optional(),
            fieldWithPath("userid").type(JsonFieldType.STRING).description("상담원 id").optional(),
            fieldWithPath("outTotal").type(JsonFieldType.NUMBER).description("총 시도콜").optional(),
            fieldWithPath("outSuccess").type(JsonFieldType.NUMBER).description("성공호").optional(),
            fieldWithPath("fails").type(JsonFieldType.NUMBER).description("비수신").optional(),
            fieldWithPath("outBillSecSum").type(JsonFieldType.NUMBER).description("총 통화시간").optional(),
            fieldWithPath("avgBillSec").type(JsonFieldType.NUMBER).description("평균통화시간").optional(),
            fieldWithPath("avgRateValue").type(JsonFieldType.NUMBER).description("통화성공률").optional()
    };

    private FieldDescriptor[] inboundResponse = new FieldDescriptor[]{
            fieldWithPath("statDate").type(JsonFieldType.STRING).description("통계시작날짜").optional(),
            fieldWithPath("statHour").type(JsonFieldType.NUMBER).description("통계종료날짜").optional(),
            fieldWithPath("inTotal").type(JsonFieldType.NUMBER).description("전체콜").optional(),
            fieldWithPath("inSuccess").type(JsonFieldType.NUMBER).description("응대호").optional(),
            fieldWithPath("inBillSecSum").type(JsonFieldType.NUMBER).description("총 통화시간").optional(),
            fieldWithPath("avgBillSec").type(JsonFieldType.NUMBER).description("평균통화시간").optional(),
            fieldWithPath("cancel").type(JsonFieldType.NUMBER).description("포기호").optional(),
            fieldWithPath("avgRateValue").type(JsonFieldType.NUMBER).description("응대율").optional()
    };

    private FieldDescriptor[] memberStatusResponse = new FieldDescriptor[]{
            fieldWithPath("statDate").type(JsonFieldType.STRING).description("통계시작날짜").optional(),
            fieldWithPath("statHour").type(JsonFieldType.NUMBER).description("통계종료날짜").optional(),
            fieldWithPath("userId").type(JsonFieldType.STRING).description("상담원id").optional(),
            fieldWithPath("total").type(JsonFieldType.NUMBER).description("후처리 건수").optional(),
            fieldWithPath("diffSum").type(JsonFieldType.NUMBER).description("총 후처리시간").optional(),
            fieldWithPath("avgDiff").type(JsonFieldType.NUMBER).description("후처리 평균시간").optional()
    };

//    @Test
    public void list() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(new JwtRequestPostProcessor())
                        .param("startDate", "2020-04-01")
                        .param("endDate", "2020-04-30")
                        .param("timeUnit", "WEEK")
//                        .param("person", "T")
//                        .param("service", "")
//                        .param("group", "")
                        .param("personIds", "user0777")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestParameters(
                                parameterWithName("startDate").description("시작일").optional(),
                                parameterWithName("endDate").description("종료일").optional(),
                                parameterWithName("timeUnit").description("검색주기").optional(),
                                parameterWithName("additionalCondition").description("추가 조건 선택").optional(),
                                parameterWithName("person").description("직통").optional(),
                                parameterWithName("service").description("서비스 선택").optional(),
                                parameterWithName("group").description("부서 선택").optional(),
                                parameterWithName("person").description("상담원 선택").optional()
                        ),
                        responseFields(responseFields)
                                .and(fieldWithPath("data[]").type(JsonFieldType.ARRAY).description("시간별 통계").optional())
                                .andWithPrefix("data[]", statUserResponse)
                                .andWithPrefix("data[].outboundResponse[]", outboundResponse)
                                .andWithPrefix("data[].inboundResponse[]", inboundResponse)
                                .andWithPrefix("data[].memberStatusResponse[]", memberStatusResponse)
                ))
                .andReturn();
    }

    @Test
    public void myCallStatTEST() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL+"/my-call-stat")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        log.info(String.valueOf(result));
    }
}
