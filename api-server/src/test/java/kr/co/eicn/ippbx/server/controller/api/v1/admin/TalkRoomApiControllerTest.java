package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import lombok.extern.slf4j.Slf4j;
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
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class TalkRoomApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/talk/history";

    private FieldDescriptor[] responseFields = new FieldDescriptor[]{
            fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
            fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
            fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional(),
            fieldWithPath("data").description("반환 데이터").optional()
    };

    private FieldDescriptor[] talkRoomResponse = new FieldDescriptor[]{
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("seq값").optional(),
            fieldWithPath("roomStartTime").type(JsonFieldType.STRING).description("시작시간").optional(),
            fieldWithPath("roomLastTime").type(JsonFieldType.STRING).description("마지막메세지시간").optional(),
            fieldWithPath("roomId").type(JsonFieldType.STRING).description("방id").optional(),
            fieldWithPath("roomName").type(JsonFieldType.STRING).description("대화방명").optional(),
            fieldWithPath("senderKey").type(JsonFieldType.STRING).description("상담톡서비스").optional(),
            fieldWithPath("userKey").type(JsonFieldType.STRING).description("상담톡서비스").optional(),
            fieldWithPath("roomStatus").type(JsonFieldType.STRING).description("대화방상태").optional(),
            fieldWithPath("maindbCustomName").type(JsonFieldType.STRING).description("고객명").optional(),
            fieldWithPath("idName").type(JsonFieldType.STRING).description("상담원명").optional()
    };

    private FieldDescriptor[] talkMsgResponse = new FieldDescriptor[]{
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("seq값").optional(),
            fieldWithPath("insertTime").type(JsonFieldType.STRING).description("입력시간").optional(),
            fieldWithPath("sendReceive").type(JsonFieldType.STRING).description("송수신 구분").optional(),
            fieldWithPath("userId").type(JsonFieldType.STRING).description("상담원 ID").optional(),
            fieldWithPath("idName").type(JsonFieldType.STRING).description("상담원 명").optional(),
            fieldWithPath("type").type(JsonFieldType.STRING).description("유형").optional(),
            fieldWithPath("content").type(JsonFieldType.STRING).description("내용").optional()
    };

//    @Test
    public void pagination() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(new JwtRequestPostProcessor())
//                .param("startDate", "")
//                .param("endDate", "")
//                .param("id", "")
                        .param("roomStatus", "C")
//                .param("senderKey", "")
//                .param("roomName", "")
                        .param("sort", "START_TIME")
                        .param("sequence", "desc")
                        .param("page", "1")
                        .param("limit", "10")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestParameters(
                                parameterWithName("page").description("현재 페이지"),
                                parameterWithName("limit").description("페이지 개수"),
                                parameterWithName("startDate").description("시작일").optional(),
                                parameterWithName("endDate").description("종료일").optional(),
                                parameterWithName("id").description("상담원id").optional(),
                                parameterWithName("roomStatus").description("대화방상태").optional(),
                                parameterWithName("senderKey").description("상담톡서비스").optional(),
                                parameterWithName("roomName").description("대화방명").optional(),
                                parameterWithName("sort").description("정렬데이터").optional(),
                                parameterWithName("sequence").description("정렬순서").optional()
                        ),
                        responseFields(responseFields)
                                .and(fieldWithPath("data.rows[]").description("상담톡대화방이력"))
                                .andWithPrefix("data.rows[]", talkRoomResponse)
                                .andWithPrefix("data.",
                                        fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지"),
                                        fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총갯수"))
                ))
                .andReturn();
    }

    @Test
    protected void messageHistory() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{roomId}/message", "0770893f2e86ed5fe5cc9c71cc5aa53d38214970_TVbeI2h8Y3Wx_20190528175950_527")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("roomId").description("방ID")
                        ),
                        responseFields(responseFields)
                                .and(fieldWithPath("data[]").description("대화이력"))
                                .andWithPrefix("data[]", talkMsgResponse)
                ))
                .andReturn();
    }

//    @Test
    protected void get() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{seq}", "1")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("roomStatus", "X")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("seq").description("seq값")
                        ),
                        requestParameters(
                                parameterWithName("roomStatus").description("방상태")
                        ),
                        responseFields(responseFields)
                                .and(fieldWithPath("data.").description("대화방정보"))
                                .andWithPrefix("data.", talkRoomResponse)
                ))
                .andReturn();
    }
}
