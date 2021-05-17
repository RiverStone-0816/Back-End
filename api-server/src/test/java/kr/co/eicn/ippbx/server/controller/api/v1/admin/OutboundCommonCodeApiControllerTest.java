package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.server.model.form.CommonCodeFormRequest;
import kr.co.eicn.ippbx.server.model.form.CommonCodeUpdateFormRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class OutboundCommonCodeApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/outbound/code";

    private FieldDescriptor[] multiCodeResponse = new FieldDescriptor[]{
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("commonType seq값").optional(),
            fieldWithPath("name").type(JsonFieldType.STRING).description("유형명").optional(),
            fieldWithPath("kind").type(JsonFieldType.STRING).description("용도").optional(),
            fieldWithPath("commonFields").type(JsonFieldType.ARRAY).description("필드 목록").optional()
    };

    private FieldDescriptor[] commonFieldResponse = new FieldDescriptor[]{
            fieldWithPath("displaySeq").type(JsonFieldType.NUMBER).description("순서").optional(),
            fieldWithPath("fieldId").type(JsonFieldType.STRING).description("필드아이디").optional(),
            fieldWithPath("fieldInfo").type(JsonFieldType.STRING).description("필드명").optional(),
            fieldWithPath("relatedFieldId").type(JsonFieldType.STRING).description("연동하위코드아이디").optional(),
            fieldWithPath("relatedFieldInfo").type(JsonFieldType.STRING).description("연동하위코드명").optional(),
            fieldWithPath("commonCodes").type(JsonFieldType.ARRAY).description("코드 목록").optional()
    };

    private FieldDescriptor[] commonCodeDetailResponse = new FieldDescriptor[]{
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("seq값").optional(),
            fieldWithPath("codeId").type(JsonFieldType.STRING).description("코드id").optional(),
            fieldWithPath("codeName").type(JsonFieldType.STRING).description("코드명").optional(),
            fieldWithPath("sequence").type(JsonFieldType.NUMBER).description("순서").optional(),
            fieldWithPath("hide").type(JsonFieldType.STRING).description("숨김여부 Y:숨김, N:숨기지않음").optional(),
            fieldWithPath("script").type(JsonFieldType.STRING).description("설명").optional()
    };

    private FieldDescriptor[] responseFields = new FieldDescriptor[]{
            fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
            fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
            fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional(),
            fieldWithPath("data").type(JsonFieldType.ARRAY).description("반환 데이터").optional()
    };

    private FieldDescriptor[] commonCodeUpdateFormRequest = new FieldDescriptor[]{
            fieldWithPath("relatedFieldId").type(JsonFieldType.STRING).description("연동하위코드").optional(),
            fieldWithPath("codes").type(JsonFieldType.ARRAY).description("코드목록").optional()
    };

    private FieldDescriptor[] commonCodeFormRequest = new FieldDescriptor[]{
            fieldWithPath("codeId").type(JsonFieldType.STRING).description("코드 id"),
            fieldWithPath("codeName").type(JsonFieldType.STRING).description("코드명"),
            fieldWithPath("sequence").type(JsonFieldType.NUMBER).description("순서"),
            fieldWithPath("hide").type(JsonFieldType.STRING).description("숨김여부 Y:숨김, N:숨기지않음"),
            fieldWithPath("script").type(JsonFieldType.STRING).description("설명")
    };

    private FieldDescriptor[] relatedFieldResponse = new FieldDescriptor[]{
            fieldWithPath("fieldId").type(JsonFieldType.STRING).description("필드 id").optional(),
            fieldWithPath("fieldInfo").type(JsonFieldType.STRING).description("필드 명").optional()
    };

    @Order(2)
//    @Test
    public void list() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("kind", "PDS")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestParameters(
                                parameterWithName("kind").description("유형")
                        ),
                        responseFields(responseFields)
                                .and(fieldWithPath("data[]").description("연동코드목록").optional())
                                .andWithPrefix("data[]", multiCodeResponse)
                                .andWithPrefix("data[].commonFields[]", commonFieldResponse)
                                .andWithPrefix("data[].commonFields[].commonCodes[]", commonCodeDetailResponse)
                ))
                .andReturn();
    }

    @Order(1)
//    @Test
    public void put() throws Exception {
        CommonCodeUpdateFormRequest updateForm = new CommonCodeUpdateFormRequest();
        CommonCodeFormRequest codeForm = new CommonCodeFormRequest();

        List<CommonCodeFormRequest> codes = new ArrayList<>();

        codeForm.setCodeId("A");
        codeForm.setCodeName("접수");
        codeForm.setSequence(1);
        codeForm.setHide("N");
        codeForm.setScript("");

        codes.add(codeForm);
        updateForm.setCodes(codes);

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/type/{type}/field/{fieldId}/code", 5, "RS_CODE_2")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .content(mapper.writeValueAsString(updateForm)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("type").description("유형의 seq값"),
                                parameterWithName("fieldId").description("필드 id값")
                        ),
                        requestFields(commonCodeUpdateFormRequest)
                                .andWithPrefix("codes[]", commonCodeFormRequest),
                        responseFields(responseFields)
                ))
                .andReturn();
    }

//    @Test
    public void getRelatedField() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/type/{type}/field/{fieldId}/related", 5, "RS_CODE_10")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("type").description("유형의 seq값"),
                                parameterWithName("fieldId").description("필드 id값")
                        ),
                        responseFields(responseFields)
                                .andWithPrefix("data[]", relatedFieldResponse)
                ))
                .andReturn();
    }

    @Order(2)
//    @Test
    public void post() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL + "/type/{type}/field/{fieldId}/code", 5, "RS_CODE_2")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("type").description("유형의 seq값"),
                                parameterWithName("fieldId").description("필드 id값")
                        ),
                        requestFields(fieldWithPath("commonCodeUpload[]").type(JsonFieldType.ARRAY).description("코드"))
                                .andWithPrefix("commonCodeUpload[]", commonCodeFormRequest),
                        responseFields(responseFields)
                ))
                .andReturn();
    }

//    @Test
    public void getField() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/type/{type}/field/{fieldId}", 5, "RS_CODE_10")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("type").description("유형의 seq값"),
                                parameterWithName("fieldId").description("fieldId값")
                        ),
                        responseFields(responseFields)
                                .andWithPrefix("data.", commonFieldResponse)
                                .andWithPrefix("data.commonCodes[]", commonCodeDetailResponse)
                ))
                .andReturn();
    }
}
