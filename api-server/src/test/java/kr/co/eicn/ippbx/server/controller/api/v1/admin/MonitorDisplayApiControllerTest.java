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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
public class MonitorDisplayApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/monitor/screen/screen";

    private FieldDescriptor[] displayScreenResponse = new FieldDescriptor[] {
            fieldWithPath("statDate").type(JsonFieldType.STRING).description("날짜"),
            fieldWithPath("waitSum").type(JsonFieldType.NUMBER).description("대기호"),
            fieldWithPath("resAve").type(JsonFieldType.NUMBER).description("전광판명칭"),
            fieldWithPath("total").type(JsonFieldType.NUMBER).description("사용디자인"),
            fieldWithPath("connReq").type(JsonFieldType.NUMBER).description("사용디자인"),
            fieldWithPath("success").type(JsonFieldType.NUMBER).description("표시데이터"),
            fieldWithPath("callBack").type(JsonFieldType.NUMBER).description("슬라이딩 문구 사용여부"),
            fieldWithPath("cancel").type(JsonFieldType.NUMBER).description("표시데이터"),
            fieldWithPath("svcLevelAve").type(JsonFieldType.NUMBER).description("표시데이터"),
            fieldWithPath("news").type(JsonFieldType.STRING).description("한줄 공지사항"),
            fieldWithPath("memberStatusResponses").type(JsonFieldType.OBJECT).description("멤버상태")
    };

    private FieldDescriptor[] memberStatusResponse = new FieldDescriptor[] {
            fieldWithPath("memberCnt").type(JsonFieldType.NUMBER).description("총인원"),
            fieldWithPath("customWaitCnt").type(JsonFieldType.NUMBER).description("고객대기"),
            fieldWithPath("counselWaitCnt").type(JsonFieldType.NUMBER).description("상담대기"),
            fieldWithPath("waitStatus").type(JsonFieldType.NUMBER).description("대기"),
            fieldWithPath("counselStatus").type(JsonFieldType.NUMBER).description("상담"),
            fieldWithPath("postprocessStatus").type(JsonFieldType.NUMBER).description("후처리"),
            fieldWithPath("restStatus").type(JsonFieldType.NUMBER).description("휴식"),
            fieldWithPath("mealStatus").type(JsonFieldType.NUMBER).description("식사"),
            fieldWithPath("otherStatus").type(JsonFieldType.NUMBER).description("다른업무"),
            fieldWithPath("educateStatus").type(JsonFieldType.NUMBER).description("표시데이터"),
            fieldWithPath("webStatus").type(JsonFieldType.NUMBER).description("표시데이터")
    };

//    @Test
    protected void list() throws Exception {
        final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                                .and(fieldWithPath("data").type(JsonFieldType.OBJECT).description("지식관리 상세"))
                                .andWithPrefix("data.", displayScreenResponse)
                                .andWithPrefix("data.memberStatusResponses.", memberStatusResponse)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }
}
