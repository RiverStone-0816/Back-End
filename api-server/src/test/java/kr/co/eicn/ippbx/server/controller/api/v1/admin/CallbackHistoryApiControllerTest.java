package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.server.model.enums.CallbackStatus;
import kr.co.eicn.ippbx.server.model.form.CallbackListUpdateFormRequest;
import kr.co.eicn.ippbx.server.model.form.CallbackRedistFormRequest;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static kr.co.eicn.ippbx.server.model.enums.CallbackStatus.NONE;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
public class CallbackHistoryApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/record/callback/history";

    private FieldDescriptor[] callbackHistoryResponse = new FieldDescriptor[]{
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("seq값").optional(),
            fieldWithPath("callerNumber").type(JsonFieldType.STRING).description("수신번호").optional(),
            fieldWithPath("callbackNumber").type(JsonFieldType.STRING).description("콜백번호").optional(),
            fieldWithPath("svcName").type(JsonFieldType.STRING).description("인입서비스").optional(),
            fieldWithPath("huntName").type(JsonFieldType.STRING).description("인입헌트").optional(),
            fieldWithPath("idName").type(JsonFieldType.STRING).description("상담원").optional(),
            fieldWithPath("status").type(JsonFieldType.VARIES).description("처리상태 NONE:미처리, COMPLETE:처리완료, PROCESSING:처리중").optional(),
            fieldWithPath("inputDate").type(JsonFieldType.STRING).description("입력일시").optional(),
            fieldWithPath("resultDate").type(JsonFieldType.STRING).description("처리일시").optional()
    };

    private FieldDescriptor[] responseFields = new FieldDescriptor[]{
            fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
            fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
            fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional(),
            fieldWithPath("data").description("반환 데이터").optional()
    };

    private FieldDescriptor[] addPersonListResponse = new FieldDescriptor[]{
            fieldWithPath("id").type(JsonFieldType.STRING).description("아이디"),
            fieldWithPath("idName").type(JsonFieldType.STRING).description("상담원명"),
            fieldWithPath("extension").type(JsonFieldType.STRING).description("내선번호").optional(),
            fieldWithPath("peer").type(JsonFieldType.STRING).description("전화기아이디"),
            fieldWithPath("hostName").type(JsonFieldType.STRING).description("호스트명").optional()
    };

    @Order(4)
//    @Test
    public void pagination() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("page", "1")
                .param("limit", "10")
                .param("startDate", "2020-01-01")
                .param("endDate", "2020-01-31")
//                .param("svcNumber", "")
//                .param("status", "")
//                .param("callbackNumber", "")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestParameters(
                                parameterWithName("page").description("현재 페이지"),
                                parameterWithName("limit").description("페이지 개수"),
                                parameterWithName("startDate").description("시작일").optional(),
                                parameterWithName("endDate").description("종료일").optional(),
                                parameterWithName("svcNumber").description("인입경로").optional(),
                                parameterWithName("status").description("처리상태 NONE:미처리 COMPLETE:처리완료 PROCESSING:처리중").optional(),
                                parameterWithName("callbackNumber").description("고객번호").optional()
                        ),
                        responseFields(responseFields)
                                .and(fieldWithPath("data.rows[]").description("콜백이력").optional())
                                .andWithPrefix("data.rows[]", callbackHistoryResponse)
                                .andWithPrefix("data.",
                                        fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지"),
                                        fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총갯수"))
                ))
                .andReturn();
    }

    @Order(1)
//    @Test
    protected void redistribution() throws Exception {
        final CallbackRedistFormRequest form = new CallbackRedistFormRequest();
        final List<Integer> svc = new ArrayList<>();
        final List<String> users = new ArrayList<>();
        svc.add(3);
        users.add("user0789");

        form.setServiceSequences(svc);
        form.setUsers(users);

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/redistribution")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .content(mapper.writeValueAsString(form)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document.document(
                        requestFields(
                                fieldWithPath("serviceSequences[]").type(JsonFieldType.ARRAY).description("콜백이력리스트"),
                                fieldWithPath("users[]").type(JsonFieldType.ARRAY).description("재분배사용자")
                        ),
                        responseFields(responseFields)
                ))
                .andReturn();

        log.info(result);
    }

//    @Test
    protected void put() throws Exception {
        final CallbackListUpdateFormRequest form = new CallbackListUpdateFormRequest();
        final List<Integer> integers = Arrays.asList(1, 2, 3);

        form.setServiceSequences(integers);
        form.setStatus(CallbackStatus.COMPLETE);

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .content(mapper.writeValueAsString(form)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestFields(
                                fieldWithPath("serviceSequences").description("콜백이력목록"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("처리상태")
                        ),
                        responseFields(responseFields)
                ))
                .andReturn();

        log.info(result);
    }

    @Order(3)
//    @Test
    protected void delete() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("seq","3")
                .param("seq", "4"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestParameters(
                                parameterWithName("seq").description("삭제시퀀스값")
                        ),
                        responseFields(responseFields)
                ))
                .andReturn();

        log.info(result);
    }

//    @Test
    public void addPersonList() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/add-persons")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(responseFields)
                                .and(fieldWithPath("data[]").description("분배가능한 상담원 목록").optional())
                                .andWithPrefix("data[]", addPersonListResponse)
                ))
                .andReturn();
    }

}
