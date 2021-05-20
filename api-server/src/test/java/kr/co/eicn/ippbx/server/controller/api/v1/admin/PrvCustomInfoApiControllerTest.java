package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.model.entity.customdb.PrvCustomInfoEntity;
import kr.co.eicn.ippbx.model.form.PrvCustomInfoFormRequest;
import kr.co.eicn.ippbx.util.page.Pagination;
import org.junit.jupiter.api.Order;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static kr.co.eicn.ippbx.util.JsonResult.create;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PrvCustomInfoApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/outbound/preview/custominfo";

    /**
     * 고객데이터 리스트
     */
    //@Test
    @Order(1)
    public void getPagination() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{groupSeq}/data", 9)
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

        Pagination<PrvCustomInfoEntity> prvCustomInfoEntityPagination = paginationData(mvcResult, PrvCustomInfoEntity.class);
    }

    //@Test
    @Order(1)
    public void get() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{groupSeq}/data/{id}", 9, "9_20200522104925_3")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())

                .andReturn();
    }

    /**
     *  PRV데이터 추가
     */
    //@Test
    @Order(1)
    protected void post() throws Exception {
        final PrvCustomInfoFormRequest form = new PrvCustomInfoFormRequest();
        form.setGroupSeq(9);     //그룹SEQ
        form.setString_1("Insert TEST");    //고객정보유형
        form.setString_2("test22");    //상담결과유형
        form.setNumber_1("07075490108");

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(form))
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isCreated())
                /*문서작성필요.*/
                .andReturn();
    }

    //@Test
    @Order(1)
    protected void put() throws Exception {
        final PrvCustomInfoFormRequest form = new PrvCustomInfoFormRequest();
        form.setGroupSeq(9);     //그룹SEQ
        form.setString_1("Update TEST");    //고객정보유형
        form.setString_2("test3333");    //상담결과유형

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/{id}", "PRV_920200522112723")
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

    //@Test
    @Order(1)
    public void deleteData() throws Exception {
        String id = "PRV_920200522112723";
        Integer groupSeq = 9;
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL+"?groupSeq=9&id="+id)


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
}
