package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
public class LoginHistoryControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/service/log/history";

    private FieldDescriptor[] loginHistoryResponse = new FieldDescriptor[] {
        fieldWithPath("seq").type(JsonFieldType.NUMBER).description("시퀀스키"),
        fieldWithPath("loginDate").type(JsonFieldType.STRING).description("로그인시간"),
        fieldWithPath("logoutDate").type(JsonFieldType.STRING).description("로그아웃시간"),
        fieldWithPath("userId").type(JsonFieldType.STRING).description("사용자ID"),
        fieldWithPath("userName").type(JsonFieldType.STRING).description("사용자명"),
        fieldWithPath("extension").type(JsonFieldType.STRING).description("내선번호"),
        fieldWithPath("companyTrees").type(JsonFieldType.ARRAY).description("내선부서").optional(),
        fieldWithPath("dialStatus").type(JsonFieldType.STRING).description("로그인시전화끊은후상태 WAIT:대기 POST_PROCESS:후처리상태"),
        fieldWithPath("logoutStatus").type(JsonFieldType.STRING).description("로그아웃상태 WAIT:대기상태(수신허용) LOGOUT:로그아웃(수신불허용)")
    };

//    @Test
    public void pagination() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("page", "1")
                .param("limit", "10")
                .param("startDate", "2019-01-01")
                .param("endDate", "2020-01-31")
                .param("userId","user0790")
                .param("userName","유저0790")
                .param("extension","0790")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestParameters(
                                parameterWithName("page").description("현재 페이지"),
                                parameterWithName("limit").description("페이지 개수"),
                                parameterWithName("startDate").description("시작시간").optional(),
                                parameterWithName("endDate").description("종료시간").optional(),
                                parameterWithName("userId").description("아이디").optional(),
                                parameterWithName("userName").description("로그인명").optional(),
                                parameterWithName("extension").description("내선번호").optional()
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional(),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("실제 반환될 데이터").optional()
                        )
                                .and(fieldWithPath("data.rows[]").description("로그인 이력").optional())
                                .andWithPrefix("data.rows[]", loginHistoryResponse)
                                .andWithPrefix("data.",
                                        fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지"),
                                        fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총갯수"))
                ))
                .andReturn();
    }
}
