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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
public class MonitorPartApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/monitor/consultant/part";

    private FieldDescriptor[] responseFields = new FieldDescriptor[]{
            fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
            fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
            fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional(),
    };

    private FieldDescriptor[] queueMonitorResponse = new FieldDescriptor[]{
            fieldWithPath("centerStatus").type(JsonFieldType.OBJECT).description("센터현황 모니터링").optional(),
            fieldWithPath("huntMonitorList").type(JsonFieldType.ARRAY).description("헌트모니터링").optional(),
            fieldWithPath("personMonitorList").type(JsonFieldType.ARRAY).description("상담원모니터링").optional(),
            fieldWithPath("totalResponse").type(JsonFieldType.OBJECT).description("통합통계모니터링").optional(),
            fieldWithPath("huntStatList").type(JsonFieldType.ARRAY).description("헌트별 통계 모니터링").optional()
    };

    private FieldDescriptor[] centerStatusResponse = new FieldDescriptor[]{
            fieldWithPath("rateValue").type(JsonFieldType.NUMBER).description("응대율").optional(),
            fieldWithPath("inboundCall").type(JsonFieldType.NUMBER).description("인바운드통화중").optional(),
            fieldWithPath("outboundCall").type(JsonFieldType.NUMBER).description("아웃바운드통화중").optional(),
            fieldWithPath("afterTreatment").type(JsonFieldType.NUMBER).description("후처리").optional(),
            fieldWithPath("unprocessedCallback").type(JsonFieldType.NUMBER).description("미처리콜백").optional()
    };

    private FieldDescriptor[] huntStatusResponse = new FieldDescriptor[]{
            fieldWithPath("huntName").type(JsonFieldType.STRING).description("헌트명").optional(),
            fieldWithPath("customWait").type(JsonFieldType.NUMBER).description("고객대기").optional(),
            fieldWithPath("wait").type(JsonFieldType.NUMBER).description("대기").optional(),
            fieldWithPath("logoutWait").type(JsonFieldType.NUMBER).description("비로긴대기").optional(),
            fieldWithPath("consulting").type(JsonFieldType.NUMBER).description("상담중").optional(),
            fieldWithPath("afterTreatment").type(JsonFieldType.NUMBER).description("후처리").optional(),
            fieldWithPath("rest").type(JsonFieldType.NUMBER).description("휴식").optional(),
            fieldWithPath("meal").type(JsonFieldType.NUMBER).description("식사").optional(),
            fieldWithPath("meeting").type(JsonFieldType.NUMBER).description("회의중").optional(),
            fieldWithPath("pds").type(JsonFieldType.NUMBER).description("PDS").optional(),
            fieldWithPath("loginCount").type(JsonFieldType.NUMBER).description("로긴수").optional(),
            fieldWithPath("logoutCount").type(JsonFieldType.NUMBER).description("로그아웃").optional(),
            fieldWithPath("total").type(JsonFieldType.NUMBER).description("전체").optional()
    };

    private FieldDescriptor[] personResponse = new FieldDescriptor[]{
            fieldWithPath("idName").type(JsonFieldType.STRING).description("상담사명").optional(),
            fieldWithPath("userId").type(JsonFieldType.STRING).description("상담사 ID").optional(),
            fieldWithPath("extension").type(JsonFieldType.STRING).description("내선번호").optional(),
            fieldWithPath("statusResponse").type(JsonFieldType.OBJECT).description("상담사 개인별 상태").optional(),
            fieldWithPath("statResponse").type(JsonFieldType.OBJECT).description("상담사 개인별 통계").optional()
    };

    private FieldDescriptor[] statusResponse = new FieldDescriptor[]{
            fieldWithPath("phone").type(JsonFieldType.STRING).description("전화기").optional(),
            fieldWithPath("isLogin").type(JsonFieldType.STRING).description("로그인").optional(),
            fieldWithPath("hunt").type(JsonFieldType.STRING).description("인입헌트").optional(),
            fieldWithPath("status").type(JsonFieldType.STRING).description("후처리").optional(),
            fieldWithPath("keepTime").type(JsonFieldType.NUMBER).description("미처리콜백").optional(),
            fieldWithPath("sendReceive").type(JsonFieldType.STRING).description("발신/수신").optional(),
            fieldWithPath("number").type(JsonFieldType.STRING).description("고객번호").optional()
    };

    private FieldDescriptor[] statResponse = new FieldDescriptor[]{
            fieldWithPath("receive").type(JsonFieldType.NUMBER).description("수신").optional(),
            fieldWithPath("sender").type(JsonFieldType.NUMBER).description("발신").optional(),
            fieldWithPath("total").type(JsonFieldType.NUMBER).description("합계").optional(),
            fieldWithPath("totalCallTime").type(JsonFieldType.NUMBER).description("총통화시간").optional(),
            fieldWithPath("avgCallTime").type(JsonFieldType.NUMBER).description("평균통화시간").optional()
    };

    private FieldDescriptor[] totalStatResponse = new FieldDescriptor[]{
            fieldWithPath("rateValue").type(JsonFieldType.NUMBER).description("응대율").optional(),
            fieldWithPath("serviceRateValue").type(JsonFieldType.NUMBER).description("서비스레벨 응대율").optional(),
            fieldWithPath("inCount").type(JsonFieldType.NUMBER).description("인입호수").optional(),
            fieldWithPath("viewCount").type(JsonFieldType.NUMBER).description("단순조회").optional(),
            fieldWithPath("connectCount").type(JsonFieldType.NUMBER).description("연결요청").optional(),
            fieldWithPath("inboundRate").type(JsonFieldType.NUMBER).description("IB응대호").optional(),
            fieldWithPath("inboundGiveUp").type(JsonFieldType.NUMBER).description("IB포기호").optional(),
            fieldWithPath("callback").type(JsonFieldType.NUMBER).description("콜백").optional(),
            fieldWithPath("outboundRate").type(JsonFieldType.NUMBER).description("OB응대호").optional(),
            fieldWithPath("outboundNoAnswer").type(JsonFieldType.NUMBER).description("OB무응답호").optional()
    };

    private FieldDescriptor[] huntStatResponse = new FieldDescriptor[]{
            fieldWithPath("huntName").type(JsonFieldType.STRING).description("헌트명").optional(),
            fieldWithPath("inCount").type(JsonFieldType.NUMBER).description("인입호수").optional(),
            fieldWithPath("viewCount").type(JsonFieldType.NUMBER).description("단순조회").optional(),
            fieldWithPath("connectCount").type(JsonFieldType.NUMBER).description("연결요청").optional(),
            fieldWithPath("rateCount").type(JsonFieldType.NUMBER).description("응답호").optional(),
            fieldWithPath("giveUpCount").type(JsonFieldType.NUMBER).description("포기호").optional(),
            fieldWithPath("callBack").type(JsonFieldType.NUMBER).description("콜백").optional(),
            fieldWithPath("rateValue").type(JsonFieldType.NUMBER).description("응답율").optional()
    };

//    @Test
    public void list() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/excellent-consultant")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(responseFields)
                                .and(fieldWithPath("data").type(JsonFieldType.OBJECT).description("반환 데이터").optional())
                                .andWithPrefix("data.", queueMonitorResponse)
                                .andWithPrefix("data.centerStatus.", centerStatusResponse)
                                .andWithPrefix("data.huntMonitorList[]", huntStatusResponse)
                                .andWithPrefix("data.personMonitorList[]", personResponse)
                                .andWithPrefix("data.personMonitorList[].statusResponse.", statusResponse)
                                .andWithPrefix("data.personMonitorList[].statResponse.", statResponse)
                                .andWithPrefix("data.totalResponse.", totalStatResponse)
                                .andWithPrefix("data.huntStatList[].", huntStatResponse)
                ))
                .andReturn();
    }

//    @Test
    public void individual_stat() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/individual-stat")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                /*문서작성필요.*/
                .andReturn();

        log.info(result);

    }
}
