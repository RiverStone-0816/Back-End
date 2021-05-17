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
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
public class PDSMonitApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/outbound/pds/monit";

    private FieldDescriptor[] responseFields = new FieldDescriptor[]{
            fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
            fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
            fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional(),
            fieldWithPath("data").description("반환 데이터").optional()
    };

    private FieldDescriptor[] pdsMonitResponse = new FieldDescriptor[]{
            fieldWithPath("pdsGroup").type(JsonFieldType.OBJECT).description("PDS그룹정보").optional(),
            fieldWithPath("executeGroup").type(JsonFieldType.OBJECT).description("실행그룹정보").optional(),
            fieldWithPath("count").type(JsonFieldType.OBJECT).description("통계").optional(),
            fieldWithPath("persons").type(JsonFieldType.ARRAY).description("상담원").optional()
    };

    private FieldDescriptor[] pdsMonitGroupResponse = new FieldDescriptor[]{
            fieldWithPath("lastExecuteId").type(JsonFieldType.STRING).description("실행 ID"),
            fieldWithPath("runHost").type(JsonFieldType.STRING).description("실행호스트"),
            fieldWithPath("connectKind").type(JsonFieldType.STRING).description("연결대상")
    };

    private FieldDescriptor[] executeGroupResponse = new FieldDescriptor[]{
            fieldWithPath("pdsHost").type(JsonFieldType.STRING).description("pds호스트"),
            fieldWithPath("pdsGroupId").type(JsonFieldType.NUMBER).description("pds그룹아이디"),
            fieldWithPath("pdsStatus").type(JsonFieldType.STRING).description("pds상태"),
            fieldWithPath("executeName").type(JsonFieldType.STRING).description("실행명"),
            fieldWithPath("pdsName").type(JsonFieldType.STRING).description("pds명"),
            fieldWithPath("pdsAdminId").type(JsonFieldType.STRING).description("pds어드민id"),
            fieldWithPath("startDate").type(JsonFieldType.STRING).description("시작일시"),
            fieldWithPath("pdsTypeName").type(JsonFieldType.STRING).description("pds유형"),
            fieldWithPath("ridKind").type(JsonFieldType.STRING).description("rid유형"),
            fieldWithPath("ridData").type(JsonFieldType.STRING).description("rid값"),
            fieldWithPath("speedKind").type(JsonFieldType.STRING).description("속도유형"),
            fieldWithPath("speedData").type(JsonFieldType.NUMBER).description("속도값"),
            fieldWithPath("dialTimeout").type(JsonFieldType.STRING).description("타임아웃"),
            fieldWithPath("totalCnt").type(JsonFieldType.NUMBER).description("고객수"),
            fieldWithPath("numbersCnt").type(JsonFieldType.NUMBER).description("번호수")
    };

    private FieldDescriptor[] executePDSCustomInfoCountResponse = new FieldDescriptor[]{
            fieldWithPath("totalTry").type(JsonFieldType.NUMBER).description("진행된건"),
            fieldWithPath("totalMod").type(JsonFieldType.NUMBER).description("남은건"),
            fieldWithPath("rings").type(JsonFieldType.NUMBER).description("시도"),
            fieldWithPath("dialed").type(JsonFieldType.NUMBER).description("통화"),
            fieldWithPath("hanged").type(JsonFieldType.NUMBER).description("종료"),
            fieldWithPath("rst1").type(JsonFieldType.NUMBER).description("수신"),
            fieldWithPath("rst2").type(JsonFieldType.NUMBER).description("비수신"),
            fieldWithPath("rst3").type(JsonFieldType.NUMBER).description("통화중"),
            fieldWithPath("rst4").type(JsonFieldType.NUMBER).description("비연결"),
            fieldWithPath("rst5").type(JsonFieldType.NUMBER).description("기타")
    };

    private FieldDescriptor[] personListSummary = new FieldDescriptor[]{
            fieldWithPath("id").type(JsonFieldType.STRING).description("아이디"),
            fieldWithPath("idName").type(JsonFieldType.STRING).description("이름"),
            fieldWithPath("extension").type(JsonFieldType.STRING).description("내선"),
            fieldWithPath("peer").type(JsonFieldType.STRING).description("전화기id"),
            fieldWithPath("paused").type(JsonFieldType.STRING).description("상담원상태")
    };

    private FieldDescriptor[] pdsListResponse = new FieldDescriptor[]{
            fieldWithPath("lastExecuteId").type(JsonFieldType.STRING).description("실행아이디"),
            fieldWithPath("lastExecuteName").type(JsonFieldType.STRING).description("실행명")
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
    public void pagination() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("startDate", "2020-01-01")
//                .param("endDate", "")
//                .param("lastExecuteId", "")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestParameters(
                                parameterWithName("startDate").description("시작일").optional(),
                                parameterWithName("endDate").description("종료일").optional(),
                                parameterWithName("lastExecuteId").description("실행아이디").optional()
                        ),
                        responseFields(responseFields)
                                .and(fieldWithPath("data.rows[]").description("실행모니터링").optional())
                                .andWithPrefix("data.rows[].", pdsMonitResponse)
                                .andWithPrefix("data.rows[].pdsGroup.", pdsMonitGroupResponse)
                                .andWithPrefix("data.rows[].executeGroup.", executeGroupResponse)
                                .andWithPrefix("data.rows[].count.", executePDSCustomInfoCountResponse)
                                .andWithPrefix("data.rows[].persons[]", personListSummary)
                                .andWithPrefix("data.",
                                        fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지"),
                                        fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총갯수")
                                )
                ))
                .andReturn();
    }

//    @Test
    public void getList() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/running-pds-list")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("startDate", "")
                .param("endDate", ""))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestParameters(
                                parameterWithName("startDate").description("시작일"),
                                parameterWithName("endDate").description("종료일")
                        ),
                        responseFields(responseFields)
                                .andWithPrefix("data[]", pdsListResponse)
                ))
                .andReturn();
    }
}
