package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class StatOutboundApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/stat/outbound";

    private FieldDescriptor[] statOutboundTotalResponse = new FieldDescriptor[]{
            fieldWithPath("ttotal").type(JsonFieldType.NUMBER).description("전체콜 합계"),
            fieldWithPath("tsuccess").type(JsonFieldType.NUMBER).description("응대호 합계").optional(),
            fieldWithPath("tcancel").type(JsonFieldType.NUMBER).description("포기호 합계"),
            fieldWithPath("tsuccessAvg").type(JsonFieldType.NUMBER).description("통화성공률 평균 합계"),
            fieldWithPath("tbillSecSum").type(JsonFieldType.NUMBER).description("총통화시간 합계"),
            fieldWithPath("tbillSecAvg").type(JsonFieldType.NUMBER).description("평균 통화시간 합계"),
            fieldWithPath("outboundResponses").type(JsonFieldType.ARRAY).description("아웃바운드 통계")
    };

    private FieldDescriptor[] statOutboundResponse = new FieldDescriptor[] {
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("아이디"),
            fieldWithPath("timeUnit").type(JsonFieldType.STRING).description("생성일"),
            fieldWithPath("total").type(JsonFieldType.NUMBER).description("전체콜"),
            fieldWithPath("success").type(JsonFieldType.NUMBER).description("응대호"),
            fieldWithPath("cancel").type(JsonFieldType.NUMBER).description("포기호"),
            fieldWithPath("successAvg").type(JsonFieldType.NUMBER).description("통화성공률"),
            fieldWithPath("billSecSum").type(JsonFieldType.NUMBER).description("총통화시간"),
            fieldWithPath("billSecAvg").type(JsonFieldType.NUMBER).description("평균 통화시간"),
    };

//    @Test
    protected void list() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("startDate", "2020-04-28")
                .param("endDate", "2020-05-03")
                .param("timeUnit", "WEEK")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestParameters(
                                parameterWithName("startDate").description("시작일").optional(),
                                parameterWithName("endDate").description("종료일").optional(),
                                parameterWithName("timeUnit").description("검색주기").optional()
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                                .and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("인바운드 총합계").optional())
                                .andWithPrefix("data.", statOutboundTotalResponse)
                                .andWithPrefix("data.outboundResponses[]", statOutboundResponse)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }

}
