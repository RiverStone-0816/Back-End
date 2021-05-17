package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.fileUpload;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PDSHistoryApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/outbound/pds/history";

    private FieldDescriptor[] PDSHistoryResponse = new FieldDescriptor[]{
            fieldWithPath("executeId").type(JsonFieldType.STRING).description("실행아이디").optional(),
            fieldWithPath("executeName").type(JsonFieldType.STRING).description("실행명").optional(),
            fieldWithPath("pdsName").type(JsonFieldType.STRING).description("pds 그룹명").optional(),
            fieldWithPath("startDate").type(JsonFieldType.STRING).description("실행시작시간").optional(),
            fieldWithPath("stopDate").type(JsonFieldType.STRING).description("실행종료시간").optional(),
            fieldWithPath("totalRunTime").type(JsonFieldType.NUMBER).description("수행시간").optional(),
            fieldWithPath("totalCnt").type(JsonFieldType.NUMBER).description("고객수").optional(),
            fieldWithPath("numberCnt").type(JsonFieldType.NUMBER).description("전화번호수").optional(),
            fieldWithPath("callTryCnt").type(JsonFieldType.NUMBER).description("진행된건").optional(),
            fieldWithPath("noCallCnt").type(JsonFieldType.NUMBER).description("남은건").optional(),
            fieldWithPath("lastCallStat").type(JsonFieldType.STRING).description("수신/비수신/통화중/비연결/기타").optional(),
            fieldWithPath("pdsStatus").type(JsonFieldType.STRING).description("상태").optional()
    };

    private FieldDescriptor[] responseField = new FieldDescriptor[]{
            fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
            fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
            fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional(),
            fieldWithPath("data").description("반환 데이터").optional()
    };

    //@Test
    public void pagination() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("startDate", "")
                .param("endDate", "")
                .param("seq", ""))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestParameters(
                                parameterWithName("startDate").description("시작날짜"),
                                parameterWithName("endDate").description("종료날짜"),
                                parameterWithName("seq").description("pds seq값")
                        ),
                        responseFields(responseField)
                                .and(fieldWithPath("data.rows[]").description("PDS 실행 이력").optional())
                                .andWithPrefix("data.rows[]", PDSHistoryResponse)
                                .andWithPrefix("data.",
                                        fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지"),
                                        fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총갯수"))
                ))
                .andReturn();
    }

//    @Test
    public void delete() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/execution/{executeId}", "18_20180918180000")
        .accept(MediaType.APPLICATION_JSON)
        .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("executeId").description("실행아이디")
                        ),
                        responseFields(responseField)
                ))
                .andReturn();
    }
}
