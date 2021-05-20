package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchQueueResponse;
import kr.co.eicn.ippbx.model.form.GradeListFormRequest;
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
public class BlackRouteApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/acd/grade/blacklist";

    private FieldDescriptor[] blackRouteApiSummaryResponse = new FieldDescriptor[]{
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("SEQUENCE KEY"),
            fieldWithPath("blackNumber").type(JsonFieldType.STRING).description("BLACK번호"),
            fieldWithPath("type").type(JsonFieldType.STRING).description("라우트타입(A:호차단, B:헌트분배)"),
            fieldWithPath("huntNumber").type(JsonFieldType.STRING).description("헌트번호"),
            fieldWithPath("huntName").type(JsonFieldType.STRING).description("헌트한글명")
    };

    private FieldDescriptor[] blackRouteFormRequest = new FieldDescriptor[]{
            fieldWithPath("blackNumber").type(JsonFieldType.STRING).description("BLACK번호"),
            fieldWithPath("type").type(JsonFieldType.STRING).description("라우트타입(A:호차단, B:헌트분배)"),
            fieldWithPath("huntNumber").type(JsonFieldType.STRING).description("작성자")
    };

    private FieldDescriptor[] queue = new FieldDescriptor[]{
            fieldWithPath("hanName").type(JsonFieldType.STRING).description("QUEUE 한글이름"),
            fieldWithPath("number").type(JsonFieldType.STRING).description("QUEUE 11자리번호")
    };

    //@Test
    public void pagination() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("page", "1")
                .param("limit", "5")
                .param("blackNumber", ""))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestParameters(
                                parameterWithName("page").description("현재 페이지"),
                                parameterWithName("limit").description("페이지 개수"),
                                parameterWithName("blackNumber").description("BLACK번호").optional()
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()

                        )
                        .andWithPrefix("data.rows[]", blackRouteApiSummaryResponse)
                        .andWithPrefix("data.",
                                fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지"),
                                fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총갯수")
                        )
                        .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }

    //@Test
    public void get() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{seq}", 9)
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
                                .andWithPrefix("data.", blackRouteApiSummaryResponse)
                ))
                .andReturn();
    }

    //@Test
    public void post() throws Exception {
        final GradeListFormRequest form = new GradeListFormRequest();

        form.setGradeNumber("11111111122");
        form.setType("A");
        form.setQueueNumber("222222222233");

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .content(mapper.writeValueAsString(form)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document.document(
                        requestFields(blackRouteFormRequest),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("data").type(JsonFieldType.NUMBER).description("등록 SEQUENCE 값").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                        )
                ))
                .andReturn();
    }

    //@Test
    protected void put() throws Exception {
        final GradeListFormRequest form = new GradeListFormRequest();

        form.setGradeNumber("07075490101");
        form.setType("B");
        form.setQueueNumber("01000001234");

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/{seq}", 5)
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
    public void delete() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{seq}", 5)
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

    //@Test
    protected void queue() throws Exception{
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL+ "/queue")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패메시지").optional())
                                .and(fieldWithPath("data[]").type(JsonFieldType.ARRAY).description("QUEUE 목록").optional())
                                .andWithPrefix("data.[]", queue)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                                )
                        )
                )
                .andReturn();

        final List<SearchQueueResponse> summaryResponses = listData(result, SearchQueueResponse.class);
        for(SearchQueueResponse summaryResponse : summaryResponses){
            log.info(summaryResponse.toString());
        }
    }
}
