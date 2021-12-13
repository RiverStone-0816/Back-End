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
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class ConCodeApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/application/code/con";

    private FieldDescriptor[] responseFields = new FieldDescriptor[]{
            fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
            fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
            fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional(),
            fieldWithPath("data").description("반환 데이터").optional()
    };

    private FieldDescriptor[] conCodeResponse = new FieldDescriptor[]{
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("commonType seq값").optional(),
            fieldWithPath("name").type(JsonFieldType.STRING).description("유형명").optional(),
            fieldWithPath("kind").type(JsonFieldType.STRING).description("용도").optional(),
            fieldWithPath("conFields").type(JsonFieldType.ARRAY).description("필드 목록")
    };

    private FieldDescriptor[] conCodeFieldResponse = new FieldDescriptor[]{
            fieldWithPath("fieldId").type(JsonFieldType.STRING).description("필드아이디"),
            fieldWithPath("fieldInfo").type(JsonFieldType.STRING).description("필드명"),
            fieldWithPath("conGroupId").type(JsonFieldType.NUMBER).description("연동그룹 id").optional()
    };

    private FieldDescriptor[] conGroupList = new FieldDescriptor[]{
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("연동그룹 seq값"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("연동그룹명")
    };

//    @Test
    public void list() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(responseFields)
                                .and(fieldWithPath("data[]").description("연동코드목록").optional())
                                .andWithPrefix("data[]", conCodeResponse)
                                .andWithPrefix("data[].conFields[]", conCodeFieldResponse)
                ))
                .andReturn();
    }

//    @Test
    public void put() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/type/{type}/field/{fieldId}/con", "1", "CST_CONCODE_1")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("type").description("유형 seq값"),
                                parameterWithName("fieldId").description("필드아이디")
                        ),
                        responseFields(responseFields)
                ))
                .andReturn();
    }

//    @Test
    public void getConGroupList() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/group")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(responseFields)
                                .andWithPrefix("data[]", conGroupList)
                ))
                .andReturn();
    }
}
