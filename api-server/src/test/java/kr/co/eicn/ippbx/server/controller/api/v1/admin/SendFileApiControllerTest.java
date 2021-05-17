package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.server.model.form.SendFileFormRequest;
import kr.co.eicn.ippbx.server.model.form.SendFileUpdateRequest;
import kr.co.eicn.ippbx.server.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.*;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.fileUpload;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class SendFileApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/application/fax-email/file";
    private static Long id;

    @Autowired
    private StorageService fileStorageSystemService;

    private FieldDescriptor[] sendFileResponse = new FieldDescriptor[] {
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("아이디"),
            fieldWithPath("sendMedia").type(JsonFieldType.VARIES).description("발송매체(FAX, EMAIL)"),
            fieldWithPath("sendName").type(JsonFieldType.STRING).description("발송물명").optional(),
            fieldWithPath("categoryCode").type(JsonFieldType.STRING).description("카테고리 코드"),
            fieldWithPath("categoryName").type(JsonFieldType.STRING).description("카테고리명"),
            fieldWithPath("desc").type(JsonFieldType.STRING).description("유형설명").optional(),
            fieldWithPath("createdAt").type(JsonFieldType.STRING).description("등록일").optional(),
            fieldWithPath("filePath").type(JsonFieldType.STRING).description("발송물 경로").optional(),
            fieldWithPath("fileName").type(JsonFieldType.STRING).description("발송물 이름").optional()
    };

    private FieldDescriptor[] sendCategorySummaryResponse = new FieldDescriptor[] {
            fieldWithPath("categoryCode").type(JsonFieldType.STRING).description("카테고리 코드"),
            fieldWithPath("categoryName").type(JsonFieldType.STRING).description("카테고리명")
    };

//      @Test
    @Order(5)
    protected void pagination() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("page", "1")
                .param("limit", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestParameters(
                                parameterWithName("page").description("현재 페이지"),
                                parameterWithName("limit").description("페이지 개수")
                        ),
                        responseFields(
                                fieldWithPath("data.rows[]").description("발송물 관리").optional(),
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional(),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("실제 반환될 데이터").optional()
                        )
                                .andWithPrefix("data.rows[]", sendFileResponse)
                                .andWithPrefix("data.",
                                        fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지"),
                                        fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총갯수"))
                ))
                .andReturn();
    }

//      @Test
    @Order(3)
    public void get() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{id}", 1)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("id").description("아이디")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                                .and(fieldWithPath("data").type(JsonFieldType.OBJECT).description("발송물 관리 상세"))
                                .andWithPrefix("data.", sendFileResponse)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }

//    @Test
    protected void post() throws Exception {
        final Resource resource = fileStorageSystemService.loadAsResource("D:/", "test.txt");
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "", org.apache.commons.io.IOUtils.toByteArray(resource.getURI()));

        final MvcResult result = this.mockMvc.perform(fileUpload(TEST_URL).file(file)
                .with(new JwtRequestPostProcessor())
                .param("categoryCode", "E002")
                .param("desc", "유형설명")
                .param("name", "발송명"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document.document(
                        requestParts(
                                partWithName("file").description("파일 업로드").optional()
                        ),
                        requestParameters(
                                parameterWithName("categoryCode").description("카테고리 코드"),
                                parameterWithName("desc").description("발송물 설명"),
                                parameterWithName("name").description("발송명")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("data").type(JsonFieldType.NUMBER).description("실제 반환될 데이터").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                        )
                ))
                .andReturn();
    }

//      @Test
    @Order(2)
    protected void put() throws Exception {
        final Resource resource = fileStorageSystemService.loadAsResource("D:/", "test.txt");
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "",  org.apache.commons.io.IOUtils.toByteArray(resource.getURI()));

        final MvcResult result = this.mockMvc.perform(fileUpload(TEST_URL + "/{id}", 3).file(file)
                .with(new JwtRequestPostProcessor())
                .param("desc", "유형수정")
                .param("name", "발송명"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("id").description("아이디")
                        ),
                        requestParts(
                                partWithName("file").description("파일 업로드").optional()
                        ),
                        requestParameters(
                                parameterWithName("desc").description("발송물 설명"),
                                parameterWithName("name").description("발송명")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("data").type(JsonFieldType.NUMBER).description("실제 반환될 데이터").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                        )
                ))
                .andReturn();
    }

//    @Test
    public void delete() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{id}", 1)
                .with(new JwtRequestPostProcessor()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("result").value("success"))
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("id").description("아이디")
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

    /**
     * 카테고리 조회
     */
//      @Test
    protected void sendCategory() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/category")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                                .and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("카테고리 목록").optional())
                                .andWithPrefix("data.[]", sendCategorySummaryResponse)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }

    /**
     * 파일 업로드
     */
    @Order(3)
    public void resource() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{id}/resource", id)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .param("token", getAuthToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestParameters(parameterWithName("token").description("토큰 키")),
                        pathParameters(parameterWithName("id").description("아이디")),
                        responseBody()
                ))
                .andReturn();
    }

}
