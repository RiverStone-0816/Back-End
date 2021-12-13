package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.model.entity.pds.PDSResultCustomInfoEntity;
import kr.co.eicn.ippbx.model.form.PDSResultCustomInfoFormRequest;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class PDSResultcustomInfoApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/outbound/pds/resultcustominfo";

   //@Test
    @Order(1)
    public void getPDSInfo() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/excutepds_info")


                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                //.param("createdStartDate", "2019-01-01 00:00:00")
                //.param("createdEndDate", "2020-05-05 23:59:59"))
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
    }

    //리스트
    //@Test
    @Order(1)
    public void getPagination() throws Exception {
        MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{executeId}/data", "2_20200405161857")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("page", "1")
                .param("limit", "3"))
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

        Pagination<PDSResultCustomInfoEntity> pdsResultCustomInfoEntityPagination = paginationData(result, PDSResultCustomInfoEntity.class);

        log.info(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(pdsResultCustomInfoEntityPagination.getRows()));

    }

    //수정
    //@Test
    @Order(1)
    protected void put() throws Exception {
        final PDSResultCustomInfoFormRequest form = new PDSResultCustomInfoFormRequest();
        //form.setGroupSeq(3);     //
        form.setString_1("Update TEST");    //
        form.setCode_1("B");
        //form.setCode_2("A");//

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/{executeId}/update?Seq=2", "2_20200405161857")
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
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL+"?executeId=2_20200405161857&seq="+2)


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

    //@Test
    protected void getExecutingPdsList() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/excutepds_info")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }
}
