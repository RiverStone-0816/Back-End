package kr.co.eicn.ippbx.server.controller.api.v1.search;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
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

public class PDSGroupSearchApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/search/pds-group";

    private FieldDescriptor[] searchPDSGroupResponse = new FieldDescriptor[]{
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("seq값").optional(),
            fieldWithPath("name").type(JsonFieldType.STRING).description("pds명").optional(),
            fieldWithPath("connectionKind").type(JsonFieldType.STRING).description("연결형식").optional(),
            fieldWithPath("resultKind").type(JsonFieldType.STRING).description("결과형식").optional()
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
    public void list() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional(),
                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("반환 데이터")
                        ).andWithPrefix("data[]", searchPDSGroupResponse)
                ))
                .andReturn();
    }
}
