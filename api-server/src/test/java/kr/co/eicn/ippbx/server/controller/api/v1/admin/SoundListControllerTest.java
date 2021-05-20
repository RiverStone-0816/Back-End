package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.model.dto.eicn.SoundDetailResponse;
import kr.co.eicn.ippbx.server.service.StorageService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.fileUpload;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
public class SoundListControllerTest extends BaseControllerTest {
    private static final FieldDescriptor[] soundListSummaryResponse = new FieldDescriptor[]{
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("아이디"),
            fieldWithPath("soundName").type(JsonFieldType.STRING).description("음원명"),
            fieldWithPath("soundFile").type(JsonFieldType.STRING).description("음원파일명"),
            fieldWithPath("comment").type(JsonFieldType.STRING).description("코멘트").optional(),
            fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사아이디")
    };
    private static final FieldDescriptor[] soundDetailResponse = new FieldDescriptor[]{
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("아이디"),
            fieldWithPath("soundName").type(JsonFieldType.STRING).description("음원명"),
            fieldWithPath("soundFile").type(JsonFieldType.STRING).description("음원파일명"),
            fieldWithPath("comment").type(JsonFieldType.STRING).description("코멘트").optional(),
            fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사아이디")
    };
    private static final FieldDescriptor[] summarySoundListResponse = new FieldDescriptor[]{
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("음원 SEQUENCE KEY"),
            fieldWithPath("soundName").type(JsonFieldType.STRING).description("음원명"),
    };
    private static Integer testId;
    private final String TEST_URL = "/api/v1/admin/sounds/ars";
    @Autowired
    private StorageService fileStorageSystemService;

    //	@Test
    @Order(4)
    public void pagination() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("page", "1")
                .param("limit", "2")
                .param("soundName", ""))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestParameters(
                                parameterWithName("page").description("현재 페이지"),
                                parameterWithName("limit").description("페이지 개수"),
                                parameterWithName("soundName").description("음원명").optional()
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                                .and(fieldWithPath("data.rows[]").type(JsonFieldType.ARRAY).description("반환될 데이터").optional())
                                .andWithPrefix("data.rows[]", soundListSummaryResponse)
                                .andWithPrefix("data.",
                                        fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지"),
                                        fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총갯수")
                                )
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }

    //	@Test
    @Order(1)
    public void post() throws Exception {
        final Resource resource = fileStorageSystemService.loadAsResource("D:/download", "ars_lunch_140331.wav");
        MockMultipartFile file = new MockMultipartFile("file", "ars_lunch_140331.wav", "audio/x-wav", org.apache.commons.io.IOUtils.toByteArray(resource.getURI()));

        final MvcResult result = this.mockMvc.perform(fileUpload(TEST_URL).file(file)
                .with(new JwtRequestPostProcessor())
                .param("comment", "음원등록합니다.")
                .param("soundName", "테스트")
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document.document(
                        requestParts(
                                partWithName("file").description("음원파일 업로드")
                        ),
                        requestParameters(
                                parameterWithName("soundName").description("음원명"),
                                parameterWithName("comment").description("코멘트").optional()
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("data").type(JsonFieldType.NUMBER).description("등록 SEQUENCE 값").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                        )
                ))
                .andReturn();

        testId = getData(result, Integer.class);
    }

    //	@Test
    @Order(2)
    public void get() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{id}", testId)
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
                                .andWithPrefix("data.", soundDetailResponse)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();

        final SoundDetailResponse data = getData(result, SoundDetailResponse.class);
        log.info(data.toString());
    }

    //	@Test
    @Order(5)
    public void delete() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{seq}", testId)
                .with(new JwtRequestPostProcessor()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("result").value("success"))
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("seq").description("음원아이디")
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

    //	@Test
    @Order(3)
    public void resource() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{seq}/resource", 42)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .param("token", getAuthToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestParameters(parameterWithName("token").description("토큰 키")),
                        pathParameters(parameterWithName("seq").description("음원아이디")),
                        responseBody()
                ))
                .andReturn();
    }

    /**
     * 음원 목록 조회
     */
    //@Test
    protected void add_sounds_list() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/add-sounds-list")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                                .and(fieldWithPath("data").type(JsonFieldType.ARRAY).description("음원 목록 조회").optional())
                                .andWithPrefix("data.[]", summarySoundListResponse)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }

    /**
     * ARS멘트 음원 목록 조회
     */
    //@Test
    protected void ars_sounds_list() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/ars-sounds-list")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                                .and(fieldWithPath("data").type(JsonFieldType.ARRAY).description("음원 목록 조회").optional())
                                .andWithPrefix("data.[]", summarySoundListResponse)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }
}
