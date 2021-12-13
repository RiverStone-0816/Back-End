package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchPersonListResponse;
import kr.co.eicn.ippbx.model.form.RAFormUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class RouteApplicationApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/acd/grade/routeapp";

    private FieldDescriptor[] raSummaryResponse = new FieldDescriptor[]{
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("SEQUENCE KEY"),
            fieldWithPath("inputDate").type(JsonFieldType.STRING).description("신청일").optional(),
            fieldWithPath("type").type(JsonFieldType.STRING).description("유형(VIP:VIP, BLACK:BLACK)").optional(),
            fieldWithPath("number").type(JsonFieldType.STRING).description("신청번호").optional(),
            fieldWithPath("appUserid").type(JsonFieldType.STRING).description("신청상담원ID").optional(),
            fieldWithPath("appUsername").type(JsonFieldType.STRING).description("신청상담원명").optional(),
            fieldWithPath("rstUserid").type(JsonFieldType.STRING).description("처리상담원ID").optional(),
            fieldWithPath("rstUsername").type(JsonFieldType.STRING).description("처리상담원명").optional(),
            fieldWithPath("memo").type(JsonFieldType.STRING).description("사유").optional(),
            fieldWithPath("result").type(JsonFieldType.STRING).description("처리여부(A:미처리, B:VIP, C:BLACK)").optional(),
            fieldWithPath("recordFile").type(JsonFieldType.STRING).description("녹취파일위치").optional()
    };

    private FieldDescriptor[] person = new FieldDescriptor[]{
            fieldWithPath("id").type(JsonFieldType.STRING).description("상담원ID"),
            fieldWithPath("idName").type(JsonFieldType.STRING).description("상담원명")
    };


    //@Test
    public void pagination() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("page", "1")
                .param("limit", "2")
                .param("startDate", "2020-03-01 00:00:00")
                .param("endDate", "2020-03-27 23:59:59"))
                //.param("type", "")
                //.param("number", "")
                //.param("appUserid", "")
                //.param("rstUserid", "")
                //.param("result", ""))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestParameters(
                                parameterWithName("page").description("현재 페이지"),
                                parameterWithName("limit").description("페이지 개수"),
                                parameterWithName("startDate").description("조회시작날짜").optional(),
                                parameterWithName("endDate").description("조회종료날짜").optional(),
                                parameterWithName("type").description("유형").optional(),
                                parameterWithName("number").description("신청번호").optional(),
                                parameterWithName("appUserid").description("신청상담원ID").optional(),
                                parameterWithName("rstUserid").description("처리상담원ID").optional(),
                                parameterWithName("result").description("처리결과").optional()
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                                .and(fieldWithPath("data.rows[]").type(JsonFieldType.ARRAY).description("반환될 데이터").optional())
                                .andWithPrefix("data.rows[]", raSummaryResponse)
                                .andWithPrefix("data.",
                                        fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지"),
                                        fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총갯수")
                                )
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }

    //@Test
    protected void person() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/person")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패메시지").optional())
                                .and(fieldWithPath("data[]").type(JsonFieldType.ARRAY).description("QUEUE 목록").optional())
                                .andWithPrefix("data.[]", person)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                                )
                        )
                )
                .andReturn();

        final List<SearchPersonListResponse> summaryResponses = listData(result, SearchPersonListResponse.class);
        for (SearchPersonListResponse summaryResponse : summaryResponses) {
            log.info(summaryResponse.toString());
        }
    }

    //@Test
    public void get() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{seq}", 2)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("seq").description("SEQUENCE KEY")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("상담톡 정보").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                        )
                                .andWithPrefix("data.", raSummaryResponse)
                ))
                .andReturn();
    }

    //@Test
    protected void put() throws Exception {
        final RAFormUpdateRequest form = new RAFormUpdateRequest();

        form.setRouteQueueNumber("07075490597");

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/{seq}", 1)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .content(mapper.writeValueAsString(form)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("seq").description("SEQUENCE KEY")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("data").type(JsonFieldType.NULL).description("").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                        )
                ))
                .andReturn();
    }

    //@Test
    public void resource() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{seq}/resource", 2)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .param("token", getAuthToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestParameters(parameterWithName("token").description("토큰 키")),
                        pathParameters(parameterWithName("seq").description("음원SEQ")),
                        responseBody()
                ))
                .andReturn();
    }

    //@Test
    public void delete() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{seq}", 2)
                .with(new JwtRequestPostProcessor()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("result").value("success"))
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("seq").description("SEQUENCE KEY")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("data").type(JsonFieldType.NULL).description("'"),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                        )
                ))
                .andReturn();
    }

}
