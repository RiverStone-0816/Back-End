package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.server.model.dto.eicn.GroupUploadLogDetailResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.*;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class MaindbUploadApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/application/maindb/upload";

    private FieldDescriptor[] maindbUploadResponse = new FieldDescriptor[] {
            fieldWithPath("uploadId").type(JsonFieldType.STRING).description("업로드아이디"),
            fieldWithPath("maindbGroupName").type(JsonFieldType.STRING).description("고객DB 그룹명"),
            fieldWithPath("uploadDate").type(JsonFieldType.STRING).description("업로드일"),
            fieldWithPath("uploadName").type(JsonFieldType.STRING).description("업로드파일명"),
            fieldWithPath("tryCnt").type(JsonFieldType.NUMBER).description("업로드순서"),
            fieldWithPath("uploadCnt").type(JsonFieldType.NUMBER).description("업로드 데이터수").optional(),
            fieldWithPath("uploadStatus").type(JsonFieldType.VARIES).description("업로드상태(U: 업로드완료, N:업로드중)").optional()
    };

    private FieldDescriptor[] uploadDetailResponse = new FieldDescriptor[] {
            fieldWithPath("mentText").type(JsonFieldType.STRING).description("로그")
    };

    private FieldDescriptor[] maindbGroupResponse = new FieldDescriptor[] {
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("시퀀스"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("그룹명"),
            fieldWithPath("makeDate").type(JsonFieldType.STRING).description("그룹생성일")
    };

//      @Test
    protected void pagination() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("page", "1")
                .param("limit", "10")
//                .param("startDate", "2020-03-12")
//                .param("endDate", "2020-05-11")
                .param("maindbGroupSeq", ""))
                .andDo(print())
                .andExpect(status().isOk())
//                .andDo(document.document(
//                        requestParameters(
//                                parameterWithName("page").description("현재 페이지"),
//                                parameterWithName("limit").description("페이지 개수"),
//                                parameterWithName("startDate").description("시작일").optional(),
//                                parameterWithName("endDate").description("종료일").optional(),
//                                parameterWithName("maindbGroupSeq").description("고객DB 그룹").optional()
//                        ),
//                        responseFields(
//                                fieldWithPath("data.rows[]").description("고객DB 업로드이력").optional(),
//                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
//                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
//                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional(),
//                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("실제 반환될 데이터").optional()
//                        )
//                                .andWithPrefix("data.rows[]", maindbUploadResponse)
//                                .andWithPrefix("data.",
//                                        fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지"),
//                                        fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총갯수"))
//                ))
                .andReturn();
    }

//    @Test
    public void uploadLog() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/upload-log/{uploadId}","PDS_2_20200326202749")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("uploadId").description("아이디")
                        ),
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
     * 고객DB 그룹 목록조회
     */
//    	@Test
    protected void maindbGroup() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/maindb-group")
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
                                .andWithPrefix("data.[]", maindbGroupResponse)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }

}
