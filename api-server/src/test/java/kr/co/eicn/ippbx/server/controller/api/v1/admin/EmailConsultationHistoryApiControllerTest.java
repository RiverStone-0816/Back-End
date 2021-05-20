package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.model.form.EmailConsultationHistoryFormRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.*;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class EmailConsultationHistoryApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/email/history/consultation-history";

    private FieldDescriptor[] consultationHistoryResponse = new FieldDescriptor[] {
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("아이디"),
            fieldWithPath("userName").type(JsonFieldType.STRING).description("분배사용자").optional(),
            fieldWithPath("userTrName").type(JsonFieldType.STRING).description("이관사용자").optional(),
            fieldWithPath("resultCodeName").type(JsonFieldType.VARIES).description("처리여부(A:접수, B:처리중, C:처리완료)").optional(),
            fieldWithPath("resultServiceName").type(JsonFieldType.STRING).description("관련서비스").optional(),
            fieldWithPath("resultKindName").type(JsonFieldType.STRING).description("상담종류").optional(),
            fieldWithPath("sentDate").type(JsonFieldType.STRING).description("메일수신기간").optional(),
            fieldWithPath("fromName").type(JsonFieldType.STRING).description("발신자명").optional(),
            fieldWithPath("fromEmail").type(JsonFieldType.STRING).description("발신이메일").optional(),
            fieldWithPath("subject").type(JsonFieldType.STRING).description("제목").optional(),
            fieldWithPath("customName").type(JsonFieldType.STRING).description("고객명").optional(),
            fieldWithPath("customCompanyName").type(JsonFieldType.STRING).description("고객사정보").optional()
    };

    private FieldDescriptor[] commonCodeResponse = new FieldDescriptor[]{
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("아이디"),
            fieldWithPath("codeId").type(JsonFieldType.STRING).description("코드아이디"),
            fieldWithPath("codeName").type(JsonFieldType.STRING).description("코드이름")
    };

//      @Test
    @Order(3)
    protected void pagination() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("page", "1")
                .param("limit", "10")
                .param("startDate", "2019-02-01")
                .param("endDate", "2020-03-02")
                .param("userId", "")
                .param("resultCode", "")
                .param("fromEmail", "")
                .param("fromName", "")
                .param("customName", "")
                .param("customCompanyName", "")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestParameters(
                                parameterWithName("page").description("현재 페이지"),
                                parameterWithName("limit").description("페이지 개수"),
                                parameterWithName("startDate").description("시작일").optional(),
                                parameterWithName("endDate").description("종료일").optional(),
                                parameterWithName("userId").description("상담원아이디").optional(),
                                parameterWithName("resultCode").description("처리여부(A:접수, B:처리중, C:처리완료)").optional(),
                                parameterWithName("fromEmail").description("이메일").optional(),
                                parameterWithName("fromName").description("발신자명").optional(),
                                parameterWithName("customName").description("고객명").optional(),
                                parameterWithName("customCompanyName").description("고객사명").optional()
                        ),
                        responseFields(
                                fieldWithPath("data.rows[]").type(JsonFieldType.ARRAY).description("이메일이력").optional(),
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional(),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("실제 반환될 데이터").optional()
                        )
                        .andWithPrefix("data.rows[]", consultationHistoryResponse)
                        .andWithPrefix("data.",
                                fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지"),
                                fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총갯수"))
                ))
                .andReturn();
    }

    /**
     * 이메일 재분배
     */
//      @Test
    public void redistribution() throws Exception {
          final EmailConsultationHistoryFormRequest form = new EmailConsultationHistoryFormRequest();
          final List<Integer> id = new ArrayList<>();
          final List<String> userIds = new ArrayList<>();
          id.add(1);
          userIds.add("user0678");

          form.setIds(id);
          form.setUserIds(userIds);

          final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/redistribution")
                  .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                  .with(new JwtRequestPostProcessor())
                  .content(mapper.writeValueAsString(form)))
                  .andDo(print())
                  .andDo(MockMvcResultHandlers.print())
                  .andExpect(status().isCreated())
                  .andDo(document.document(
                          requestFields(
                                  fieldWithPath("ids[]").type(JsonFieldType.ARRAY).description("아이디"),
                                  fieldWithPath("userIds[]").type(JsonFieldType.ARRAY).description("재분배할 사용자아이디")
                          ),
                          responseFields(
                                  fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                  fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                  fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional(),
                                  fieldWithPath("data").description("실제 반환될 데이터").optional()
                          )
                  ))
                  .andReturn();
    }

    /**
     * 처리상태 목록조회
     */
//    	@Test
    protected void emailCommonCode() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/common-code")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                        .and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("처리상태 목록").optional())
                        .andWithPrefix("data.[]", commonCodeResponse)
                        .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }
}
