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
public class StatInboundApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/stat/inbound";

    private FieldDescriptor[] statInboundTotalResponse = new FieldDescriptor[]{
            fieldWithPath("ttotal").type(JsonFieldType.NUMBER).description("전체콜 합계"),
            fieldWithPath("tonlyRead").type(JsonFieldType.NUMBER).description("단순조회 합계").optional(),
            fieldWithPath("tconnReq").type(JsonFieldType.NUMBER).description("연결요청 합계"),
            fieldWithPath("tsuccess").type(JsonFieldType.NUMBER).description("응대호 합계"),
            fieldWithPath("tcancel").type(JsonFieldType.NUMBER).description("포기호 합계"),
            fieldWithPath("tbillSecSum").type(JsonFieldType.NUMBER).description("총통화시간 합계"),
            fieldWithPath("tbillSecAvg").type(JsonFieldType.NUMBER).description("평균 통화시간 합계"),
            fieldWithPath("twaitAvg").type(JsonFieldType.NUMBER).description("평균 대기시간 합계"),
            fieldWithPath("tsuccessAvg").type(JsonFieldType.NUMBER).description("호응답률 합계"),
            fieldWithPath("tsvcLevelAvg").type(JsonFieldType.NUMBER).description("서비스레벨호 응답률 합계"),
            fieldWithPath("tivrAvg").type(JsonFieldType.NUMBER).description("단순조회율 합계").optional(),

            fieldWithPath("tresWait10").type(JsonFieldType.NUMBER).description("응대호 ~10초 합계"),
            fieldWithPath("tresWait20").type(JsonFieldType.NUMBER).description("응대호 ~20초 합계"),
            fieldWithPath("tresWait30").type(JsonFieldType.NUMBER).description("응대호 ~30초 합계"),
            fieldWithPath("tresWait40").type(JsonFieldType.NUMBER).description("응대호 ~40초 합계"),
            fieldWithPath("tresWait40After").type(JsonFieldType.NUMBER).description("응대호 ~40초이후 합계"),
            fieldWithPath("tcancelWait10").type(JsonFieldType.NUMBER).description("포기호 ~10초 합계").optional(),
            fieldWithPath("tcancelWait20").type(JsonFieldType.NUMBER).description("포기호 ~20초 합계").optional(),
            fieldWithPath("tcancelWait30").type(JsonFieldType.NUMBER).description("포기호 ~30초 합계").optional(),
            fieldWithPath("tcancelWait40").type(JsonFieldType.NUMBER).description("포기호 ~40초 합계").optional(),
            fieldWithPath("tcancelWait40After").type(JsonFieldType.NUMBER).description("포기호 ~40초이후 합계").optional(),
            fieldWithPath("inboundResponses").type(JsonFieldType.ARRAY).description("인바운드 통계")
    };

    private FieldDescriptor[] statInboundSummaryResponse = new FieldDescriptor[]{
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("아이디").optional(),
            fieldWithPath("timeUnit").type(JsonFieldType.STRING).description("생성일").optional(),
            fieldWithPath("total").type(JsonFieldType.NUMBER).description("전체콜"),
            fieldWithPath("onlyRead").type(JsonFieldType.NUMBER).description("단순조회"),
            fieldWithPath("connReq").type(JsonFieldType.NUMBER).description("연결요청"),
            fieldWithPath("success").type(JsonFieldType.NUMBER).description("응대호"),
            fieldWithPath("cancel").type(JsonFieldType.NUMBER).description("포기호"),
            fieldWithPath("billSecSum").type(JsonFieldType.NUMBER).description("총통화시간"),
            fieldWithPath("billSecAvg").type(JsonFieldType.NUMBER).description("평균 통화시간"),
            fieldWithPath("waitAvg").type(JsonFieldType.NUMBER).description("평균 대기시간"),
            fieldWithPath("successAvg").type(JsonFieldType.NUMBER).description("호응답률"),
            fieldWithPath("svcLevelAvg").type(JsonFieldType.NUMBER).description("서비스레벨호 응답률"),
            fieldWithPath("ivrAvg").type(JsonFieldType.NUMBER).description("단순조회율 평균"),

            fieldWithPath("waitSucc_0_10").type(JsonFieldType.NUMBER).description("응대호 ~10초"),
            fieldWithPath("waitSucc_10_20").type(JsonFieldType.NUMBER).description("응대호 ~20초"),
            fieldWithPath("waitSucc_20_30").type(JsonFieldType.NUMBER).description("응대호 ~30초"),
            fieldWithPath("waitSucc_30_40").type(JsonFieldType.NUMBER).description("응대호 ~40초"),
            fieldWithPath("waitSucc_40").type(JsonFieldType.NUMBER).description("응대호 ~40초이후"),
            fieldWithPath("waitCancel_0_10").type(JsonFieldType.NUMBER).description("포기호 ~10초"),
            fieldWithPath("waitCancel_10_20").type(JsonFieldType.NUMBER).description("포기호 ~20초"),
            fieldWithPath("waitCancel_20_30").type(JsonFieldType.NUMBER).description("포기호 ~30초"),
            fieldWithPath("waitCancel_30_40").type(JsonFieldType.NUMBER).description("포기호 ~40초"),
            fieldWithPath("waitCancel_40").type(JsonFieldType.NUMBER).description("포기호 ~40초이후"),
    };

    private FieldDescriptor[] queueNameResponse = new FieldDescriptor[]{
            fieldWithPath("number").type(JsonFieldType.STRING).description("번호"),
            fieldWithPath("hanName").type(JsonFieldType.STRING).description("han이름")
    };

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        document = document(
                "{class-name}/{method-name}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())
        );

        this.mockMvc = securityBuilder(webApplicationContext, restDocumentation);
    }

//    @Test
    protected void list() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("startDate", "2010-04-21")
                .param("endDate", "2020-04-20")
                .param("timeUnit", "HOUR")
//                .param("serviceNumbers", "")
//                .param("queueNames", "")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestParameters(
                                parameterWithName("startDate").description("시작일").optional(),
                                parameterWithName("endDate").description("종료일").optional(),
                                parameterWithName("timeUnit").description("검색주기").optional(),
                                parameterWithName("serviceNumbers").description("서비스").optional(),
                                parameterWithName("queueNames").description("헌트번호").optional()
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                                .and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("인바운드 총합계").optional())
                                .andWithPrefix("data.", statInboundTotalResponse)
                                .andWithPrefix("data.inboundResponses[]", statInboundSummaryResponse)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }

    /**
     * 헌트번호
     */
//    	@Test
    protected void person() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/queue-name")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                                .and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("상담결과유형 목록").optional())
                                .andWithPrefix("data.[]", queueNameResponse)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }
}
