package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
public class MonitorIVRApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/monitor/consultant/ivr";

    private FieldDescriptor[] responseFields = new FieldDescriptor[]{
            fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
            fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
            fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional(),
    };

    private FieldDescriptor[] ivrResponse = new FieldDescriptor[]{
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("SEQUENCE KEY"),
            fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드키"),
            fieldWithPath("root").type(JsonFieldType.NUMBER).description("루트키"),
            fieldWithPath("parent").type(JsonFieldType.NUMBER).description("부모키"),
            fieldWithPath("level").type(JsonFieldType.NUMBER).description("레벨"),
            fieldWithPath("treeName").type(JsonFieldType.STRING).description("IVR이름"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
            fieldWithPath("button").type(JsonFieldType.STRING).description("버튼"),
            fieldWithPath("type").type(JsonFieldType.NUMBER).description("연결설정"),
            fieldWithPath("typeData").type(JsonFieldType.STRING).description("연결데이타"),
            fieldWithPath("soundCode").type(JsonFieldType.STRING).description("음원관리 참조 정보").optional(),
            fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사 아이디"),
            fieldWithPath("nodes").type(JsonFieldType.ARRAY).description("자식 트리"),
            fieldWithPath("processingCustomerCount").type(JsonFieldType.NUMBER).description("진행중 고객수"),
            fieldWithPath("waitingCustomerCount").type(JsonFieldType.NUMBER).description("대기중 고객수")
    };

    /**
     * 목록조회
     */
//    @Test
    public void list() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("serviceNumber", "")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestParameters(
                                parameterWithName("serviceNumber").description("서비스번호")
                        ),
                        relaxedResponseFields(
                                responseFields
                        )
                                .and(fieldWithPath("data").type(JsonFieldType.ARRAY).description("반환 데이터").optional())
                                .andWithPrefix("data.[]", ivrResponse)
                                .and(fieldWithPath("data.[].nodes[]").type(JsonFieldType.ARRAY).description("계층 PDS IVR정보").optional())
                                .andWithPrefix("data.[].nodes[]", ivrResponse)
                ))
                .andReturn()

                ;
    }
}
