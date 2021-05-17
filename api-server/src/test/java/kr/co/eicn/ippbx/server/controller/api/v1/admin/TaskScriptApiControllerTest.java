package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.server.model.form.TaskScriptCategoryFormRequest;
import kr.co.eicn.ippbx.server.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.fileUpload;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class TaskScriptApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/help/script";
    private static Long id;

    @Autowired
    private StorageService fileStorageSystemService;

    private FieldDescriptor[] taskScriptSummaryResponse = new FieldDescriptor[] {
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("아이디"),
            fieldWithPath("categoryId").type(JsonFieldType.NUMBER).description("카테고리 아이디"),
            fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
            fieldWithPath("tag").type(JsonFieldType.STRING).description("태그"),
            fieldWithPath("createdAt").type(JsonFieldType.STRING).description("등록일")
    };

    private FieldDescriptor[] taskScriptDetailResponse = new FieldDescriptor[] {
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("아이디"),
            fieldWithPath("categoryId").type(JsonFieldType.NUMBER).description("카테고리 아이디"),
            fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
            fieldWithPath("createdAt").type(JsonFieldType.STRING).description("등록일"),
            fieldWithPath("tag").type(JsonFieldType.STRING).description("태그"),
            fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
            fieldWithPath("fileInfo").type(JsonFieldType.ARRAY).description("첨부파일").optional()
    };

    private FieldDescriptor[] fileNameDetailResponse = new FieldDescriptor[] {
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("아이디").optional(),
            fieldWithPath("path").type(JsonFieldType.STRING).description("파일경로").optional(),
            fieldWithPath("originalName").type(JsonFieldType.STRING).description("파일명").optional()
    };

    private FieldDescriptor[] taskScriptCategoryResponse = new FieldDescriptor[] {
            fieldWithPath("categoryId").type(JsonFieldType.NUMBER).description("아이디"),
            fieldWithPath("categoryName").type(JsonFieldType.STRING).description("카테고리명")
    };

//     @Test
    public void pagination() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("page", "1")
                .param("limit", "10")
                .param("startDate", "2019-01-01")
                .param("endDate", "2020-04-13")
                .param("title", "")
                .param("tag", "")
                .param("categoryId", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestParameters(
                                parameterWithName("page").description("현재 페이지"),
                                parameterWithName("limit").description("페이지 개수"),
                                parameterWithName("startDate").description("시작일").optional(),
                                parameterWithName("endDate").description("종료일").optional(),
                                parameterWithName("title").description("제목").optional(),
                                parameterWithName("tag").description("태그").optional(),
                                parameterWithName("categoryId").description("카테고리").optional()
                        ),
                        responseFields(
                                fieldWithPath("data.rows[]").description("지식관리").optional(),
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("실제 반환될 데이터").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                        )
                                .andWithPrefix("data.rows[]", taskScriptSummaryResponse)
                                .andWithPrefix("data.",
                                        fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지"),
                                        fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총갯수"))
                ))
                .andReturn();
    }

//   @Test
    @Order(3)
    protected void get() throws Exception {
        final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{id}", 1)
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
                                .and(fieldWithPath("data").type(JsonFieldType.OBJECT).description("지식관리 상세"))
                                .andWithPrefix("data.", taskScriptDetailResponse)
                                .andWithPrefix("data.fileInfo[]", fileNameDetailResponse)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }

    /**
     * 카테고리 분류
     */
//   @Test
    @Order(1)
    protected void insertTaskScriptCategory() throws Exception {
        final TaskScriptCategoryFormRequest form = new TaskScriptCategoryFormRequest();
        form.setName("category");        //분류명

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL+"/post-script-category")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(form))
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document.document(
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("분류명")
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

//   @Test
    @Order(2)
    protected void updateTaskScriptCategory() throws Exception {
        final TaskScriptCategoryFormRequest form = new TaskScriptCategoryFormRequest();
        form.setName("catrgory수정");        //분류명

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/put-script-category/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(form))
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("id").description("아이디")
                        ),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("분류명")
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

//     @Test
    @Order(5)
    protected void deleteTaskScriptCategory() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/delete-script-category/{id}", 3)
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
                                fieldWithPath("data").type(JsonFieldType.NULL).description(""),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                        )
                ))
                .andReturn();
    }

    /**
     *  스크립트
     */
//    @Test
    protected void post() throws Exception {
//        Resource resource = fileStorageSystemService.loadAsResource("D:/", "test.txt");
//        MockMultipartFile file = new MockMultipartFile("files", "test.txt", "", org.apache.commons.io.IOUtils.toByteArray(resource.getURI()));
//        resource = fileStorageSystemService.loadAsResource("D:/", "drop.txt");
//        MockMultipartFile twoFile = new MockMultipartFile("files", "drop.txt", "", org.apache.commons.io.IOUtils.toByteArray(resource.getURI()));

        final MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.multipart(TEST_URL)
                .with(new JwtRequestPostProcessor())
                .param("categoryId", "1")
                .param("title", "title")
                .param("tag", "tag")
                .param("content", "content"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document.document(
                        requestParts(
                                partWithName("files").description("파일 업로드").optional()
                        ),
                        requestParameters(
                                parameterWithName("categoryId").description("분류"),
                                parameterWithName("title").description("제목"),
                                parameterWithName("tag").description("태그"),
                                parameterWithName("content").description("내용")
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
    @Order(2)
    protected void put() throws Exception {
        final Resource resource = fileStorageSystemService.loadAsResource("D:/", "1.xlsx");
        MockMultipartFile file = new MockMultipartFile("files", "1.xlsx", "",  org.apache.commons.io.IOUtils.toByteArray(resource.getURI()));

        final MvcResult result = this.mockMvc.perform(fileUpload(TEST_URL + "/{id}", 1).file(file)
                .with(new JwtRequestPostProcessor())
                .param("categoryId", "1")
                .param("title", "제목수정")
                .param("tag", "tag")
                .param("content", "내용수정"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("id").description("아이디")
                        ),
                        requestParts(
                                partWithName("files").description("파일 업로드").optional()
                        ),
                        requestParameters(
                                parameterWithName("categoryId").description("분류"),
                                parameterWithName("title").description("제목"),
                                parameterWithName("tag").description("태그"),
                                parameterWithName("content").description("내용")
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

//  @Test
    @Order(5)
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

//    @Test
    @Order(4)
    protected void deleteSpecificFile() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/delete-specific-file/{id}", 5)
                .with(new JwtRequestPostProcessor()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("result").value("success"))
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("id").description("파일아이디")
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

    /**
     * 카테고리 목록
     */
//      @Test
    protected void taskScriptCategoryList() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/category-list")
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
                                .andWithPrefix("data.[]", taskScriptCategoryResponse)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }

    /**
     * 특정 파일 다운로드
     */
    @Order(2)
    public void specificFileResource() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "{fileId}/specific-file-resource", id)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .param("token", getAuthToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestParameters(
                                parameterWithName("token").description("토큰 키")
                        ),
                        responseBody()
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
