package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.model.dto.eicn.GroupUploadLogDetailResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class PDSUploadApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/outbound/pds/upload";

    private FieldDescriptor[] uploadSummaryResponse = new FieldDescriptor[] {
            fieldWithPath("uploadId").type(JsonFieldType.NUMBER).description("업로드아이디"),
            fieldWithPath("pdsGroupId").type(JsonFieldType.NUMBER).description("그룹아이디"),
            fieldWithPath("pdsGroupName").type(JsonFieldType.STRING).description("PDS 그룹명"),
            fieldWithPath("uploadDate").type(JsonFieldType.STRING).description("업로드일"),
            fieldWithPath("uploadName").type(JsonFieldType.STRING).description("업로드 파일명"),
            fieldWithPath("tryCnt").type(JsonFieldType.NUMBER).description("업로드 순서"),
            fieldWithPath("uploadCnt").type(JsonFieldType.NUMBER).description("업로드 데이터수"),
            fieldWithPath("uploadStatus").type(JsonFieldType.VARIES).description("업로드상태(U: 업로드완료, N:업로드중)")
    };

    private FieldDescriptor[] uploadDetailResponse = new FieldDescriptor[] {
            fieldWithPath("mentText").type(JsonFieldType.STRING).description("로그")
    };

    private FieldDescriptor[] pdsGroupResponse = new FieldDescriptor[] {
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("시퀀스"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("그룹명"),
            fieldWithPath("makeDate").type(JsonFieldType.STRING).description("그룹생성일")
    };

//     @Test
    @Order(5)
    protected void pagination() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("page", "1")
                .param("limit", "10")
                .param("pdsGroup", ""))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestParameters(
                                parameterWithName("page").description("현재 페이지"),
                                parameterWithName("limit").description("페이지 개수"),
                                parameterWithName("pdsGroup").description("PDS 그룹").optional()
                        ),
                        responseFields(
                                fieldWithPath("data.rows[]").description("PDS 업로드이력").optional(),
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional(),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("실제 반환될 데이터").optional()
                        )
                                .andWithPrefix("data.rows[]", uploadSummaryResponse)
                                .andWithPrefix("data.",
                                        fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지"),
                                        fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총갯수"))
                ))
                .andReturn();
    }

//    @Test
    public void uploadLog() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/upload-log/{uploadId}", "")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional())
                                .and(fieldWithPath("data.").type(JsonFieldType.OBJECT).description("업로드 이력"))
                                .andWithPrefix("data.", uploadDetailResponse)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                                )
                ))
                .andReturn();

        final GroupUploadLogDetailResponse data = getData(result, GroupUploadLogDetailResponse.class);
        log.info("data {}", data);
    }

    /**
     * 고객 그룹 목록
     */
//    	@Test
    protected void pdsGroup() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/pds-group")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                                .and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("고객DB 그룹 목록").optional())
                                .andWithPrefix("data.[]", pdsGroupResponse)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }


}
