package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.*;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Timestamp;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class SendFaxEmailHistoryApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/application/fax-email/history";

    private FieldDescriptor[] sendFaxEmailHistoryResponse = new FieldDescriptor[] {
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("아이디"),
            fieldWithPath("receiver").type(JsonFieldType.STRING).description("수신인"),
            fieldWithPath("receiverNumber").type(JsonFieldType.ARRAY).description("수신번호"),
            fieldWithPath("type").type(JsonFieldType.VARIES).description("발송매체(FAX, EMAIL)"),
            fieldWithPath("categoryName").type(JsonFieldType.VARIES).description("카테고리명").optional(),
            fieldWithPath("content").type(JsonFieldType.STRING).description("유형설명"),
            fieldWithPath("sendSort").type(JsonFieldType.VARIES).description("발송구분(D:직접입력, C:카테고리입력)").optional(),
            fieldWithPath("sendDate").type(JsonFieldType.STRING).description("발송일")
    };

    private FieldDescriptor[] sendFaxEmailResponse = new FieldDescriptor[]{
            fieldWithPath("phone").type(JsonFieldType.STRING).description("전화번호"),
            fieldWithPath("target").type(JsonFieldType.STRING).description("메일주소")
    };

//     @Test
    @Order(3)
    protected void pagination() throws Exception {
    final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(new JwtRequestPostProcessor())
                    .param("page", "1")
                    .param("limit", "10")
                    .param("startDate", "2020-01-31")
                    .param("endDate", "2020-03-10")
                    .param("target", "")
                    .param("type", "")

            )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andDo(document.document(
                            requestParameters(
                                    parameterWithName("page").description("현재 페이지"),
                                    parameterWithName("limit").description("페이지 개수"),
                                    parameterWithName("startDate").description("시작일").optional(),
                                    parameterWithName("endDate").description("종료일").optional(),
                                    parameterWithName("target").description("FAX번호 또는 EMAIL주소").optional(),
                                    parameterWithName("type").description("FAX/EMAIL").optional()
                            ),
                            responseFields(
                                    fieldWithPath("data.rows[]").description("발송이력").optional(),
                                    fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                    fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                    fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional(),
                                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("실제 반환될 데이터").optional()
                            )
                                    .andWithPrefix("data.rows[]", sendFaxEmailHistoryResponse)
                                    .andWithPrefix("data.rows[]receiverNumber[]", sendFaxEmailResponse)
                                    .andWithPrefix("data.",
                                            fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지"),
                                            fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총갯수"))
                    ))
                    .andReturn();
        }
}
