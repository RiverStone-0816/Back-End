package kr.co.eicn.ippbx.server.controller.api.v1.consultation;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.server.jooq.eicn.enums.TodoListTodoStatus;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.TodoList;
import kr.co.eicn.ippbx.server.model.dto.eicn.TalkCurrentListResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.TalkCurrentMsgResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.TodoDataResponse;
import kr.co.eicn.ippbx.server.model.form.TalkCurrentListSearchRequest;
import kr.co.eicn.ippbx.server.model.form.TodoListUpdateFormRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class MainApiControllerTest extends BaseControllerTest {
    private static Integer seq;
    private final String TEST_URL = "/api/v1/consultation/main";
    private final FieldDescriptor[] todoResponse = new FieldDescriptor[]{
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("시퀀스"),
            fieldWithPath("todoKind").type(JsonFieldType.STRING).description("Toto 타이틀"),
            fieldWithPath("regDate").type(JsonFieldType.STRING).description("접수시간"),
            fieldWithPath("updateDate").type(JsonFieldType.STRING).description("업데이트시간"),
            fieldWithPath("userId").type(JsonFieldType.STRING).description("userId"),
            fieldWithPath("todoInfo").type(JsonFieldType.STRING).description("고객정보").optional(),
            fieldWithPath("todoStatus").type(JsonFieldType.STRING).description("todo 상태"),
            fieldWithPath("detailConnectInfo").type(JsonFieldType.STRING).description("연결할 정보").optional(),
            fieldWithPath("companyId").type(JsonFieldType.STRING).description("회사아이디")
    };

    private final FieldDescriptor[] todoDataResponse = new FieldDescriptor[]{
            fieldWithPath("todoKind").type(JsonFieldType.STRING).description("Toto 타이틀"),
            fieldWithPath("total").type(JsonFieldType.NUMBER).description("총 접수 건수"),
            fieldWithPath("success").type(JsonFieldType.NUMBER).description("처리 건수").optional(),
            fieldWithPath("successRate").type(JsonFieldType.NUMBER).description("처리율")
    };

    //    @Test
    public void getTodoList() throws Exception {

        final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/to-do")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                                .and(fieldWithPath("data").type(JsonFieldType.ARRAY).description("todo 리스트"))
                                .andWithPrefix("data.[]", todoResponse)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
        final List<TodoList> data = listData(result, TodoList.class);

        log.info("data {}", data);
    }

    //    @Test
    public void getTodoMonitor() throws Exception {

        final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/to-do-monitor")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                                .and(fieldWithPath("data").type(JsonFieldType.ARRAY).description("todo monitor 데이터 리스트"))
                                .andWithPrefix("data.[]", todoDataResponse)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
        final List<TodoDataResponse> data = listData(result, TodoDataResponse.class);

        log.info("data {}", data);
    }

//            @Test
    protected void list() throws Exception {
        final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/receive-path/07075498130/00000000004")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("uniqueId", "1589440651.1659")
        )
                .andDo(print())
                .andExpect(status().isOk())
//                .andDo(document.document(
//                        responseFields(
//                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
//                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
//                        )
//                                .and(fieldWithPath("data").type(JsonFieldType.OBJECT).description("지식관리 상세"))
//                                .andWithPrefix("data.", displayScreenResponse)
//                                .andWithPrefix("data.memberStatusResponses.", memberStatusResponse)
//                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
//                ))
                .andReturn();
    }

    //        @Test
    public void todoListStatusUpdate() throws Exception {
        TodoListUpdateFormRequest form = new TodoListUpdateFormRequest();
        form.setSeq(15);
        form.setTodoStatus(TodoListTodoStatus.ING);

        final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/to-do")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .content(mapper.writeValueAsString(form)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestFields(
                                fieldWithPath("seq").description("todolist시퀀스"),
                                fieldWithPath("todoStatus").type(JsonFieldType.VARIES).description("처리상태")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional(),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("실제 반환될 데이터").optional()
                        )
                ))
                .andReturn();
    }

//    @Test
    protected void currentTalkList() throws Exception {
        TalkCurrentListSearchRequest form = new TalkCurrentListSearchRequest();
        form.setMode("TOT");

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/current-talk-list")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("mode", "TOT")
                .param("authType", "")
                .param("orderBy", "")
                )
                .andDo(print())
                .andExpect(status().isOk())
//                .andDo(document.document(
//                        responseFields(
//                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
//                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
//                        )
//                                .and(fieldWithPath("data").type(JsonFieldType.OBJECT).description("지식관리 상세"))
//                                .andWithPrefix("data.", displayScreenResponse)
//                                .andWithPrefix("data.memberStatusResponses.", memberStatusResponse)
//                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
//                ))
                .andReturn();

        final List<TalkCurrentListResponse> data = listData(result, TalkCurrentListResponse.class);
        log.info("data {}", data);
    }

//        @Test
    protected void currentTalkMsg() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/current-talk-msg/{roomId}", "a4261fb7312670e8d5ba0eee45814bcb860c6146_Erz2eAOL61Rp_20200527055306_659")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
//                .andDo(document.document(
//                        responseFields(
//                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
//                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
//                        )
//                                .and(fieldWithPath("data").type(JsonFieldType.OBJECT).description("지식관리 상세"))
//                                .andWithPrefix("data.", displayScreenResponse)
//                                .andWithPrefix("data.memberStatusResponses.", memberStatusResponse)
//                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
//                ))
                .andReturn();

        final TalkCurrentMsgResponse data = getData(result, TalkCurrentMsgResponse.class);
        log.info("data {}", data);
    }

//    @Test
    protected void talkRemoveRoom() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/talk-remove-room")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("roomId", "a4261fb7312670e8d5ba0eee45814bcb860c6146_UY2Gz-Tj0kpw_20200522100256_510")
        )
                .andDo(print())
                .andExpect(status().isOk())
//                .andDo(document.document(
//                        responseFields(
//                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
//                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
//                        )
//                                .and(fieldWithPath("data").type(JsonFieldType.OBJECT).description("지식관리 상세"))
//                                .andWithPrefix("data.", displayScreenResponse)
//                                .andWithPrefix("data.memberStatusResponses.", memberStatusResponse)
//                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
//                ))
                .andReturn();

        log.info("result {}", result);
    }

    @Test
    protected void CalendarTest() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/calender-info/{year}/{mm}","2020","01")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        log.info("result {}", result);
    }
}
