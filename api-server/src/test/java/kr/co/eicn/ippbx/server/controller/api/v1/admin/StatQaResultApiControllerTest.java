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
public class StatQaResultApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/stat/result";

    private FieldDescriptor[] responseFields = new FieldDescriptor[]{
            fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
            fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
            fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional(),
            fieldWithPath("data").type(JsonFieldType.ARRAY).description("반환 데이터").optional()
    };

    private FieldDescriptor[] statQaResultResponse = new FieldDescriptor[]{
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("type seq값"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("유형명"),
            fieldWithPath("fieldResponses").type(JsonFieldType.ARRAY).description("업무구분")
    };

    private FieldDescriptor[] statQaResultFieldResponse = new FieldDescriptor[]{
            fieldWithPath("fieldId").type(JsonFieldType.STRING).description("field값"),
            fieldWithPath("fieldName").type(JsonFieldType.STRING).description("업무구분명"),
            fieldWithPath("codeResponses").type(JsonFieldType.ARRAY).description("세부구분")
    };

    private FieldDescriptor[] statQaResultCodeResponse = new FieldDescriptor[]{
            fieldWithPath("codeId").type(JsonFieldType.STRING).description("codeId값"),
            fieldWithPath("codeName").type(JsonFieldType.STRING).description("세부구분명"),
            fieldWithPath("qaResultStat").type(JsonFieldType.ARRAY).description("날짜별통계값")
    };

    private FieldDescriptor[] qaResultStat = new FieldDescriptor[]{
            fieldWithPath("statDate").type(JsonFieldType.STRING).description("통계날짜"),
            fieldWithPath("count").type(JsonFieldType.NUMBER).description("건수")
    };

    private FieldDescriptor[] statQaResultIndividualResponse = new FieldDescriptor[]{
            fieldWithPath("seq")
    };

//    @Test
    public void list() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/individual/code-list")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
//                .param("startDate", "2020-05-01")
//                .param("endDate", "2020-05-02")
//                .param("type", "")
//                .param("codeId", "")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestParameters(
                                parameterWithName("startDate").description("시작일").optional(),
                                parameterWithName("endDate").description("종료일").optional(),
                                parameterWithName("id").description("상담원id").optional()
                        ),
                        responseFields(responseFields)
                                .and(fieldWithPath("data[]").description("상담결과통계"))
                                .andWithPrefix("data[]", statQaResultResponse)
                                .andWithPrefix("data[].fieldResponses[]", statQaResultFieldResponse)
                                .andWithPrefix("data[].fieldResponses[].codeResponses[]", statQaResultCodeResponse)
                                .andWithPrefix("data[].fieldResponses[].codeResponses[].qaResultStat[]", qaResultStat )
                ))
                .andReturn();
    }
}
