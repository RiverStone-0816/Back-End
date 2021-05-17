package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.server.model.dto.eicn.WebSecureHistoryResponse;
import kr.co.eicn.ippbx.server.util.page.Pagination;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
public class WebSecureHistoryControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/service/log/web-log";

    private FieldDescriptor[] webSecureHistoryResponse = new FieldDescriptor[] {
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("시퀀스키"),
            fieldWithPath("userName").type(JsonFieldType.STRING).description("실행자명"),
            fieldWithPath("userId").type(JsonFieldType.STRING).description("아이디"),
            fieldWithPath("insertDate").type(JsonFieldType.STRING).description("시간"),
            fieldWithPath("extension").type(JsonFieldType.STRING).description("관련내선"),
            fieldWithPath("idType").type(JsonFieldType.VARIES).description("아이디유형 MASTER : 마스터, SUPER_ADMIN : 슈퍼어드민, ADMIN : 어드민, USER : 상담원").optional(),
            fieldWithPath("secureIp").type(JsonFieldType.STRING).description("실행아이피").optional(),
            fieldWithPath("actionId").type(JsonFieldType.STRING).description("실행명"),
            fieldWithPath("actionSubId").type(JsonFieldType.STRING).description("상세실행명"),
            fieldWithPath("actionData").type(JsonFieldType.STRING).description("행동결과")
    };

//    @Test
    public void pagination() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("page", "2")
                .param("limit", "10")
//                .param("startTimestamp", "" + Timestamp.valueOf("2020-03-10 15:08:04").getTime())
//                .param("endTimestamp", "" + Timestamp.valueOf("2020-03-11 15:08:04").getTime())
//                .param("userName","마스터")
//                .param("actionId", "LOGIN")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestParameters(
                                parameterWithName("page").description("현재 페이지"),
                                parameterWithName("limit").description("페이지 개수"),
                                parameterWithName("startTimestamp").description("시작일").optional(),
                                parameterWithName("endTimestamp").description("종료일").optional(),
                                parameterWithName("userName").description("상담자").optional(),
                                parameterWithName("actionId").description("실행명").optional()
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional(),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("실제 반환될 데이터").optional()
                        )
                                .and(fieldWithPath("data.rows[]").description("웹로그 목록").optional())
                                .andWithPrefix("data.rows[]", webSecureHistoryResponse)
                                .andWithPrefix("data.",
                                        fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지"),
                                        fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총갯수"))
                ))
                .andReturn();

        final Pagination<WebSecureHistoryResponse> webSecureHistoryResponsePagination = paginationData(result, WebSecureHistoryResponse.class);

        for (WebSecureHistoryResponse row : webSecureHistoryResponsePagination.getRows()) {
            log.info(row.toString());
        }
    }

//    @Test
    @Override
    protected void delete() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("seq","9526"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("result").value("success"))
                .andDo(document.document(
                        requestParameters(
                                parameterWithName("seq").description("삭제할 seq")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional(),
                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("실제 반환될 데이터").optional()
                        )
                ))
                .andReturn();
    }

}
