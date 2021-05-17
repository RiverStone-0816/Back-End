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
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
public class TalkStatisticsPersonApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/talk/statistics/person";

    private FieldDescriptor[] personStatTalkResponse = new FieldDescriptor[]{
            fieldWithPath("idName").type(JsonFieldType.STRING).description("상담원명"),
            fieldWithPath("startRoomCnt").type(JsonFieldType.NUMBER).description("개설대화방수"),
            fieldWithPath("endRoomCnt").type(JsonFieldType.NUMBER).description("종료대화방수"),
            fieldWithPath("inMsgCnt").type(JsonFieldType.NUMBER).description("수신메시지수"),
            fieldWithPath("outMsgCnt").type(JsonFieldType.NUMBER).description("발신메시지수"),
            fieldWithPath("autoMentCnt").type(JsonFieldType.NUMBER).description("자동멘트수"),
            fieldWithPath("autoMentExceedCnt").type(JsonFieldType.NUMBER).description("초과자동멘트수")
    };

    private FieldDescriptor[] responseFields = new FieldDescriptor[]{
            fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
            fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
            fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional(),
            fieldWithPath("data").type(JsonFieldType.ARRAY).description("반환 데이터").optional()
    };

//    @Test
    public void list() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
//                .param("startDate", "2019-06-28")
//                .param("endDate", "2020-01-01")
//                .param("senderKey", "")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestParameters(
                                parameterWithName("startDate").description("시작일").optional(),
                                parameterWithName("endDate").description("종료일").optional(),
                                parameterWithName("senderKey").description("서비스").optional()
                        ),
                        responseFields(responseFields)
                                .and(fieldWithPath("data[]").type(JsonFieldType.ARRAY).description("상담원별 통계").optional())
                                .andWithPrefix("data[]",personStatTalkResponse)
                ))
                .andReturn();
    }
}
