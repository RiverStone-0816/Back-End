package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class StatHuntApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/stat/hunt";

    private FieldDescriptor[] responseFields = new FieldDescriptor[]{
            fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
            fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
            fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional(),
            fieldWithPath("data").type(JsonFieldType.ARRAY).description("반환 데이터").optional()
    };

    private FieldDescriptor[] statHuntTotalResponse = new FieldDescriptor[]{
            fieldWithPath("tinTotal").type(JsonFieldType.NUMBER).description("연결요청 합계"),
            fieldWithPath("tinSuccess").type(JsonFieldType.NUMBER).description("응대호 합계"),
            fieldWithPath("tcancel").type(JsonFieldType.NUMBER).description("포기호 합계"),
            fieldWithPath("tcallBack").type(JsonFieldType.NUMBER).description("콜백 합계"),
            fieldWithPath("tinBillSecSum").type(JsonFieldType.NUMBER).description("총통화시간 합계"),
            fieldWithPath("tavgBillSec").type(JsonFieldType.NUMBER).description("평균통화시간 합계").optional(),
            fieldWithPath("tavgRateValue").type(JsonFieldType.NUMBER).description("응답률 합계").optional(),
            fieldWithPath("tserviceLevelOk").type(JsonFieldType.NUMBER).description("서비스레벨호응답률 합계"),
            fieldWithPath("statHuntResponse").type(JsonFieldType.ARRAY).description("서비스 별 통계데이터")
    };

    private FieldDescriptor[] statHuntResponse = new FieldDescriptor[]{
            fieldWithPath("startDate").type(JsonFieldType.STRING).description("시작날짜"),
            fieldWithPath("endDate").type(JsonFieldType.STRING).description("종료날짜"),
            fieldWithPath("svcName").type(JsonFieldType.STRING).description("대표서비스 명"),
            fieldWithPath("statHuntSvcResponse").type(JsonFieldType.ARRAY).description("헌트 별 통계데이터")
    };

    private FieldDescriptor[] statHuntSvcResponse = new FieldDescriptor[]{
            fieldWithPath("huntName").type(JsonFieldType.STRING).description("헌트명"),
            fieldWithPath("inboundResponse").type(JsonFieldType.ARRAY).description("통계데이터")
    };

    private FieldDescriptor[] statHuntInboundResponse = new FieldDescriptor[]{
            fieldWithPath("statDate").type(JsonFieldType.STRING).description("날짜"),
            fieldWithPath("statHour").type(JsonFieldType.NUMBER).description("시간"),
            fieldWithPath("inTotal").type(JsonFieldType.NUMBER).description("연결요청"),
            fieldWithPath("inSuccess").type(JsonFieldType.NUMBER).description("응대호"),
            fieldWithPath("callbackCount").type(JsonFieldType.NUMBER).description("콜백"),
            fieldWithPath("inBillSecSum").type(JsonFieldType.NUMBER).description("총통화시간"),
            fieldWithPath("avgBillSec").type(JsonFieldType.NUMBER).description("평균통화시간"),
            fieldWithPath("cancel").type(JsonFieldType.NUMBER).description("포기호"),
            fieldWithPath("avgRateValue").type(JsonFieldType.NUMBER).description("응답률"),
            fieldWithPath("serviceLevelOk").type(JsonFieldType.NUMBER).description("서비스레벨호응답률")
    };

//    @Test
    public void list() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(new JwtRequestPostProcessor())
                        .param("startDate", "2020-05-01")
                        .param("endDate", "2020-05-31")
                        .param("timeUnit", "MONTH")
//                .param("additionalCondition", "")
//                        .param("service", "")
//                        .param("group", "")
//                        .param("person", "")
        )
                .andDo(print())
                .andExpect(status().isOk())
//                .andDo(document.document(
//                        requestParameters(
//                                parameterWithName("startDate").description("시작일").optional(),
//                                parameterWithName("endDate").description("종료일").optional(),
//                                parameterWithName("searchCycle").description("검색주기").optional(),
//                                parameterWithName("additionalCondition").description("추가 조건 선택").optional(),
//                                parameterWithName("service").description("서비스 선택"),
//                                parameterWithName("group").description("부서 선택"),
//                                parameterWithName("person").description("상담원 선택")
//                        ),
//                        responseFields(responseFields)
//                                .and(fieldWithPath("data[]").type(JsonFieldType.ARRAY).description("헌트별 통계").optional())
//                                .andWithPrefix("data[]", statHuntTotalResponse)
//                                .andWithPrefix("data[].statHuntResponse[]", statHuntResponse)
//                                .andWithPrefix("data[].statHuntResponse[].statHuntSvcResponse[]", statHuntSvcResponse)
//                                .andWithPrefix("data[].statHuntResponse[].statHuntSvcResponse[].inboundResponse[]", statHuntInboundResponse)
//                ))
                .andReturn();
    }
}
