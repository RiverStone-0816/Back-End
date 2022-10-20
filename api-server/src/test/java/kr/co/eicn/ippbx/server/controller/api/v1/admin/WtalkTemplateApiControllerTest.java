package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.model.dto.eicn.WtalkTemplateSummaryResponse;
import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;

import kr.co.eicn.ippbx.model.dto.eicn.search.SearchPersonListResponse;
import kr.co.eicn.ippbx.model.form.TalkTemplateFormRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
public class WtalkTemplateApiControllerTest extends BaseControllerTest {

    private final String TEST_URL = "/api/v1/admin/talk/template";

    private FieldDescriptor[] talkTemplateApiSummaryResponse = new FieldDescriptor[]{
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("SEQUENCE KEY"),
            fieldWithPath("type").type(JsonFieldType.STRING).description("유형(P:개인, G:그룹, C:회사)"),
            fieldWithPath("typeData").type(JsonFieldType.STRING).description("유형데이타(유형에따라 P:, G:조직그룹값, C:회사아이디)"),
            fieldWithPath("writeUserid").type(JsonFieldType.STRING).description("작성자ID"),
            fieldWithPath("writeUserName").type(JsonFieldType.STRING).description("작성자명"),
            fieldWithPath("mentName").type(JsonFieldType.STRING).description("템플릿명"),
            fieldWithPath("ment").type(JsonFieldType.STRING).description("멘트")
    };

    private FieldDescriptor[] talkTemplateFormRequest = new FieldDescriptor[]{
            fieldWithPath("type").type(JsonFieldType.STRING).description("유형(P:개인, G:그룹, C:회사)"),
            fieldWithPath("typeData").type(JsonFieldType.STRING).description("유형데이타(유형에따라 P:, G:조직그룹값, C:회사아이디)"),
            fieldWithPath("writeUserid").type(JsonFieldType.STRING).description("작성자"),
            fieldWithPath("mentName").type(JsonFieldType.STRING).description("템플릿명"),
            fieldWithPath("ment").type(JsonFieldType.STRING).description("멘트")
    };

    private FieldDescriptor[] person = new FieldDescriptor[]{
            fieldWithPath("id").type(JsonFieldType.STRING).description("상담원ID"),
            fieldWithPath("idName").type(JsonFieldType.STRING).description("상담원명")
    };

    //@Test
    @Order(1)
    protected void list() throws Exception{
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andExpect(status().isOk())
                .andDo(document.document(
                            responseFields(
                                    fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                    fieldWithPath("reason").type(JsonFieldType.STRING).description("실패메시지").optional())
                                    .and(fieldWithPath("data[]").type(JsonFieldType.ARRAY).description("상담톡템플릿 목록").optional())
                                    .andWithPrefix("data.[]", talkTemplateApiSummaryResponse)
                                    .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                            )
                        )
                )
                .andReturn();

        final List<WtalkTemplateSummaryResponse> summaryResponses = listData(result, WtalkTemplateSummaryResponse.class);
        for(WtalkTemplateSummaryResponse summaryResponse : summaryResponses){
            log.info(summaryResponse.toString());
        }
    }

    //@Test
    @Order(2)
    public void post() throws Exception {
        final TalkTemplateFormRequest form = new TalkTemplateFormRequest();

        form.setType("P");
        form.setTypeData("master");
        form.setMentName("20200218 Test");
        form.setMent("20200228");

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .content(mapper.writeValueAsString(form)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document.document(
                        requestFields(talkTemplateFormRequest),
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
    @Order(1)
    public void get() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{seq}", 8)
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
                                .andWithPrefix("data.", talkTemplateApiSummaryResponse)
                ))
                .andReturn();
    }

    //@Test
    @Order(1)
    protected void put() throws Exception {
        final TalkTemplateFormRequest form = new TalkTemplateFormRequest();

        form.setType("P");
        form.setTypeData("master");
        form.setMentName("2020 Update");
        form.setMent("2020-02-18 Update");

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/{seq}", 8)
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

//    @Test
    @Order(5)
    public void delete() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{seq}", 8)
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
    protected void person() throws Exception{
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL+ "/person")
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
        for(SearchPersonListResponse summaryResponse : summaryResponses){
            log.info(summaryResponse.toString());
        }
    }

    @Test
    @Order(1)
    protected void pagnation() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL )
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("type","P"))
                .andDo(print())
                .andExpect(status().isOk())

                .andReturn();
    }


}
