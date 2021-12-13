package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
public class StatTotalApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/stat/total";

    private FieldDescriptor[] statInboundTotalResponse = new FieldDescriptor[]{
            fieldWithPath("ttotal").type(JsonFieldType.NUMBER).description("전체콜 합계"),
            fieldWithPath("tonlyRead").type(JsonFieldType.NUMBER).description("단순조회 합계"),
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

    private FieldDescriptor[] statInboundSummaryResponse = new FieldDescriptor[] {
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("아이디"),
            fieldWithPath("timeUnit").type(JsonFieldType.STRING).description("생성일").optional(),
            fieldWithPath("total").type(JsonFieldType.NUMBER).description("전체콜"),
            fieldWithPath("onlyRead").type(JsonFieldType.NUMBER).description("단순조회"),
            fieldWithPath("connReq").type(JsonFieldType.NUMBER).description("연결요청"),
            fieldWithPath("success").type(JsonFieldType.NUMBER).description("응대호"),
            fieldWithPath("cancel").type(JsonFieldType.NUMBER).description("포기호"),
            fieldWithPath("billSecSum").type(JsonFieldType.NUMBER).description("총통화시간"),
            fieldWithPath("handlePerUser").type(JsonFieldType.NUMBER).description("1인당 처리 콜수"),
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

    private FieldDescriptor[] statOutboundTotalResponse = new FieldDescriptor[]{
            fieldWithPath("ttotal").type(JsonFieldType.NUMBER).description("전체콜 합계").optional(),
            fieldWithPath("tsuccess").type(JsonFieldType.NUMBER).description("응대호 합계").optional(),
            fieldWithPath("tcancel").type(JsonFieldType.NUMBER).description("포기호 합계").optional(),
            fieldWithPath("tsuccessAvg").type(JsonFieldType.NUMBER).description("통화성공률 평균 합계").optional(),
            fieldWithPath("tbillSecSum").type(JsonFieldType.NUMBER).description("총통화시간 합계").optional(),
            fieldWithPath("tbillSecAvg").type(JsonFieldType.NUMBER).description("평균 통화시간 합계").optional(),
            fieldWithPath("outboundResponses").type(JsonFieldType.ARRAY).description("아웃바운드 통계").optional()
    };

    private FieldDescriptor[] statOutboundResponse = new FieldDescriptor[] {
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("아이디"),
            fieldWithPath("timeUnit").type(JsonFieldType.STRING).description("생성일").optional(),
            fieldWithPath("total").type(JsonFieldType.NUMBER).description("전체콜"),
            fieldWithPath("success").type(JsonFieldType.NUMBER).description("응대호"),
            fieldWithPath("cancel").type(JsonFieldType.NUMBER).description("포기호"),
            fieldWithPath("successAvg").type(JsonFieldType.NUMBER).description("통화성공률"),
            fieldWithPath("billSecSum").type(JsonFieldType.NUMBER).description("총통화시간"),
            fieldWithPath("billSecAvg").type(JsonFieldType.NUMBER).description("평균 통화시간"),
            fieldWithPath("handlePerUser").type(JsonFieldType.NUMBER).description("1인당 처리 콜수")
    };

    private FieldDescriptor[] statTotalResponse = new FieldDescriptor[] {
            fieldWithPath("timeUnit").type(JsonFieldType.STRING).description("생성일"),
            fieldWithPath("totalCnt").type(JsonFieldType.NUMBER).description("전체콜"),
            fieldWithPath("totalBillSec").type(JsonFieldType.NUMBER).description("총통화시간"),
            fieldWithPath("inboundTotalResponse").type(JsonFieldType.OBJECT).description("포기호").optional(),
            fieldWithPath("outboundTotalResponse").type(JsonFieldType.OBJECT).description("통화성공률").optional()
    };

//   @Test
    protected void list() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("startDate", "2020-04-20")
                .param("endDate", "2020-04-22")
                .param("timeUnit", "DATE")
//                .param("busy", "true")
//                .param("serviceNumbers", "")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestParameters(
                                parameterWithName("startDate").description("시작일").optional(),
                                parameterWithName("endDate").description("종료일").optional(),
                                parameterWithName("timeUnit").description("검색주기").optional(),
                                parameterWithName("serviceNumbers").description("서비스리스트").optional()
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                                .and(fieldWithPath("data.").type(JsonFieldType.OBJECT).description("인바운드 총합계").optional())
                                .andWithPrefix("data.", statTotalResponse)
                                .andWithPrefix("data.outboundTotalResponse.", statOutboundTotalResponse)
                                .andWithPrefix("data.outboundTotalResponse.outboundResponses[]", statOutboundResponse)
                                .andWithPrefix("data.inboundTotalResponse.", statInboundTotalResponse)
                                .andWithPrefix("data.inboundTotalResponse.inboundResponses[]", statInboundSummaryResponse)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }
}
