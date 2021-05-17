package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class NoticeApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/help/notice";
    private static Long id;

    @Autowired
    private StorageService fileStorageSystemService;

    private FieldDescriptor[] noticeSummaryResponse = new FieldDescriptor[]{
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("글번호"),
            fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
            fieldWithPath("noticeType").type(JsonFieldType.STRING).description("공지등록").optional(),
            fieldWithPath("createdAt").type(JsonFieldType.STRING).description("등록일"),
            fieldWithPath("writer").type(JsonFieldType.STRING).description("작성자"),
            fieldWithPath("viewCnt").type(JsonFieldType.NUMBER).description("조회수")
    };

    private FieldDescriptor[] noticeDetailResponse = new FieldDescriptor[]{
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("글번호"),
            fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
            fieldWithPath("createdAt").type(JsonFieldType.STRING).description("등록일"),
            fieldWithPath("noticeType").type(JsonFieldType.STRING).description("공지등록").optional(),
            fieldWithPath("writer").type(JsonFieldType.STRING).description("작성자"),
            fieldWithPath("viewCnt").type(JsonFieldType.NUMBER).description("조회수"),
            fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
            fieldWithPath("fileInfo").type(JsonFieldType.ARRAY).description("첨부파일").optional()
    };

    private FieldDescriptor[] fileNameDetailResponse = new FieldDescriptor[]{
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("아이디").optional(),
            fieldWithPath("path").type(JsonFieldType.STRING).description("파일경로").optional(),
            fieldWithPath("originalName").type(JsonFieldType.STRING).description("파일명").optional()
    };

//     @Test
    public void pagination() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("page", "1")
                .param("limit", "10")
                .param("startDate", "2019-01-01")
                .param("endDate", "2020-04-17")
                .param("searchText", ""))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestParameters(
                                parameterWithName("page").description("현재 페이지"),
                                parameterWithName("limit").description("페이지 개수"),
                                parameterWithName("startDate").description("시작일").optional(),
                                parameterWithName("endDate").description("종료일").optional(),
                                parameterWithName("searchText").description("검색텍스트").optional()
                        ),
                        responseFields(
                                fieldWithPath("data.rows[]").description("공지사항").optional(),
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("실제 반환될 데이터").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                        )
                                .andWithPrefix("data.rows[]", noticeSummaryResponse)
                                .andWithPrefix("data.",
                                fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지"),
                                fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총갯수"))
                ))
                .andReturn();
    }

//        @Test
    protected void get() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{id}", 4)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("id").description("글번호")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("실제 반환될 데이터").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                        )
                                .andWithPrefix("data.", noticeDetailResponse)
                                .andWithPrefix("data.fileInfo[]", fileNameDetailResponse)
                ))
                .andReturn();
    }

//    @Test
    protected void post() throws Exception {
        Resource resource = fileStorageSystemService.loadAsResource("D:/", "1.xlsx");
        MockMultipartFile file = new MockMultipartFile("files", "1.xlsx", "", org.apache.commons.io.IOUtils.toByteArray(resource.getURI()));
//        resource = fileStorageSystemService.loadAsResource("D:/", "dropdown.hwp");
//        MockMultipartFile twofile = new MockMultipartFile("files", "dropdown.hwp", "", org.apache.commons.io.IOUtils.toByteArray(resource.getURI()));

        final MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.multipart(TEST_URL).file(file)
                .with(new JwtRequestPostProcessor())
                .param("title", "1234")
                .param("content", "123456789")
                .param("noticeType", "N"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document.document(
                        requestParts(
                                partWithName("files").description("파일 업로드").optional()
                        ),
                        requestParameters(
                                parameterWithName("title").description("제목"),
                                parameterWithName("content").description("내용"),
                                parameterWithName("noticeType").description("공지등록")
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
    protected void put() throws Exception {
        Resource resource = fileStorageSystemService.loadAsResource("D:/", "1.xlsx");
        MockMultipartFile file = new MockMultipartFile("files", "1.xlsx", "", org.apache.commons.io.IOUtils.toByteArray(resource.getURI()));
////        resource = fileStorageSystemService.loadAsResource("D:/", "dropdown.hwp");
//        MockMultipartFile twofile = new MockMultipartFile("files", "dropdown.hwp", "", org.apache.commons.io.IOUtils.toByteArray(resource.getURI()));

        final MvcResult result = this.mockMvc.perform(fileUpload(TEST_URL + "/{id}", 5)
                .with(new JwtRequestPostProcessor())
                .param("title", "title")
                .param("content", "content수정")
                .param("noticeType", "Y"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("id").description("글번호")
                        ),
                        requestParts(
                                partWithName("files").description("파일 업로드").optional()
                        ),
                        requestParameters(
                                parameterWithName("title").description("제목"),
                                parameterWithName("content").description("내용"),
                                parameterWithName("noticeType").description("공지등록")
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
    protected void delete() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{id}", 5)
                .with(new JwtRequestPostProcessor()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("result").value("success"))
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("id").description("글번호")
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
    protected void deleteSpecificFile() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/delete-specific-file/{id}", 9)
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
     * 특정 파일 다운로드
     */
    public void specificFileResource() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{fileId}/specific-file-resource", id)
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
