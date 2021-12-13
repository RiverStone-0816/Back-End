package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.model.form.ResultCustomInfoFormRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class ResultCustomInfoControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/application/maindb/resultcustominfo";

    private FieldDescriptor[] customdb_group = new FieldDescriptor[]{
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("그룹SEQ"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("그룹명")
    };

    //고객그룹조회
    //@Test
    protected void customdb_group() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/customdb_group")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                                .and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("고객DB그룹 목록").optional())
                                .andWithPrefix("data.[]", customdb_group)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }

    /**
     * 상담이력 필드조회
     */
    //@Test
    public void getType() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{type}", 12)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())/*
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("type").description("유형 키")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("data").description("실제 반환될 데이터").optional()
                        )
                                .andWithPrefix("data.", commonFieldEntity)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))*/
                .andReturn();
    }

    /**
     * 상담이력 조회
     */
    //@Test
    @Order(1)
    public void getPagination() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{groupSeq}/data", 3)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("page", "1")
                .param("search_text2", ""))
                .andDo(print())
                .andExpect(status().isOk())
                /*
                .andDo(document.document(
                        requestParameters(
                                parameterWithName("page").description("현재 페이지"),
                                parameterWithName("limit").description("페이지 개수"),
                                parameterWithName("search_group").description("고객DB_ID").optional(),
                                parameterWithName("search_type").description("조회필드명").optional(),
                                parameterWithName("search_text1").description("조회값1").optional(),
                                parameterWithName("search_text2").description("조회값2").optional()
                        ),
                        responseFields(getCommonFieldDescriptors())
                                .andWithPrefix("data.rows[]", list)
                                .andWithPrefix("data.",
                                        fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지"),
                                        fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총갯수"))
                ))

                 */
                .andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        log.info(
                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                        objectMapper.readTree(mvcResult.getResponse().getContentAsString())
                )
        );

    }

    //수정이력조회SEQ
    //@Test
    public void get() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{seq}", 5)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
//                .andDo(document.document(
//                        pathParameters(
//                                parameterWithName("seq").description("SEQUENCE KEY")
//                        ),
//                        responseFields(
//                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
//                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
//                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("상담톡 정보").optional(),
//                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
//                        )
//                                .andWithPrefix("data.", csRouteApiGetResponse)
//                ))
                .andReturn();
    }

    //수정.
    //@Test
    @Order(1)
    protected void put() throws Exception {
        final ResultCustomInfoFormRequest form = new ResultCustomInfoFormRequest();
        form.setString_1("Update TEST");    //고객정보유형
        form.setString_2("test3333");    //상담결과유형

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/{seq}", 6)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(form))
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                /*
                    문서작성 필요.
                 */
                .andReturn();
    }

    //삭제
    //@Test
    @Order(1)
    public void deleteData() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{seq}", "6")
                .with(new JwtRequestPostProcessor()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("result").value("success"))
                /*
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

                 */
                .andReturn();
    }

    //재생
    //@Test
    @Order(1)
    public void resource() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/resource?uniqueId=1589005080.248" )
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .param("token", getAuthToken()))
                .andDo(print())
                .andExpect(status().isOk())
                /*
                .andDo(document.document(
                        requestParameters(parameterWithName("token").description("토큰 키")),
                        pathParameters(parameterWithName("seq").description("음원SEQ")),
                        responseBody()
                ))

                 */
                .andReturn();
    }

    /**
     *  상담이력데이터 추가
     */
     //@Test
    @Order(1)
    protected void post() throws Exception {
        final ResultCustomInfoFormRequest form = new ResultCustomInfoFormRequest();
        form.setGroupId(3);     //그룹SEQ
        form.setCustomId("18_oakadm_20190712132114");
        form.setString_1("Insert TEST");    //고객정보유형
        form.setString_2("test22");    //상담결과유형
        form.setUniqueId("");
        form.setCustomNumber("");
        form.setUserIdTr("TESTTEST");
        form.setSeq(10);

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(form))
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isCreated())
                /*문서작성필요.*/
                .andReturn();
    }
}
