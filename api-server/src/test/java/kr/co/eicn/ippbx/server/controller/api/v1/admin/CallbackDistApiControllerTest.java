package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.model.form.CallbackHuntDistFormRequest;
import kr.co.eicn.ippbx.model.form.CallbackUserDistFormRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class CallbackDistApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/record/callback/distribution";

    private FieldDescriptor[] callBackDistResponse = new FieldDescriptor[]{
            fieldWithPath("svcName").type(JsonFieldType.STRING).description("서비스명"),
            fieldWithPath("svcNumber").type(JsonFieldType.STRING).description("서비스원번호"),
            fieldWithPath("svcCid").type(JsonFieldType.STRING).description("서비스070번호"),
            fieldWithPath("hunts").type(JsonFieldType.ARRAY).description("헌트목록").optional()
    };

    private FieldDescriptor[] callBackHuntResponse = new FieldDescriptor[]{
            fieldWithPath("huntNumber").type(JsonFieldType.STRING).description("헌트번호").optional(),
            fieldWithPath("hanName").type(JsonFieldType.STRING).description("헌트명").optional(),
            fieldWithPath("idNames").type(JsonFieldType.ARRAY).description("상담원목록").optional()
    };

    private FieldDescriptor[] callBackPersonResponse = new FieldDescriptor[]{
            fieldWithPath("userid").type(JsonFieldType.STRING).description("상담원 id").optional(),
            fieldWithPath("idName").type(JsonFieldType.STRING).description("상담원명").optional()
    };

    private FieldDescriptor[] responseFields = new FieldDescriptor[]{
            fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
            fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
            fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional(),
            fieldWithPath("data").type(JsonFieldType.ARRAY).description("반환 데이터").optional()
    };

    private FieldDescriptor[] addHuntListResponse = new FieldDescriptor[]{
            fieldWithPath("name").type(JsonFieldType.STRING).description("큐이름"),
            fieldWithPath("hanName").type(JsonFieldType.STRING).description("헌트명").optional(),
            fieldWithPath("number").type(JsonFieldType.STRING).description("헌트번호")
    };

    private FieldDescriptor[] addPersonListResponse = new FieldDescriptor[]{
            fieldWithPath("id").type(JsonFieldType.STRING).description("아이디"),
            fieldWithPath("idName").type(JsonFieldType.STRING).description("상담원명"),
            fieldWithPath("extension").type(JsonFieldType.STRING).description("내선번호").optional(),
            fieldWithPath("peer").type(JsonFieldType.STRING).description("전화기아이디"),
            fieldWithPath("hostName").type(JsonFieldType.STRING).description("호스트명").optional()
    };

    @Order(5)
//    @Test
    public void list() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document.document(
                        responseFields(responseFields)
                                .andWithPrefix("data.[]", callBackDistResponse)
                                .andWithPrefix("data.[]hunts.[]", callBackHuntResponse)
                                .andWithPrefix("data.[]hunts.[]idNames.[].", callBackPersonResponse)
                ))
                .andReturn();
    }

    @Order(1)
//    @Test
    public void huntDistribution() throws Exception {
        final CallbackHuntDistFormRequest form = new CallbackHuntDistFormRequest();
        final List<String> hunts = new ArrayList<>();

//        hunts.add("07075490597");
//
//        form.setHunts(hunts);

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL+"/service/{svcNumber}/hunts", "07075490597")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .content(mapper.writeValueAsString(form))
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("svcNumber").description("분배할 서비스 번호")
                        ),
                        requestFields(
                                fieldWithPath("hunts[]").type(JsonFieldType.ARRAY).description("분배헌트목록").optional()
                        ),
                        responseFields(responseFields)
                ))
                .andReturn();

        log.info(String.valueOf(result));
    }

    @Order(3)
//    @Test
    public void addHuntList() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/add-hunts")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(responseFields)
                                .and(fieldWithPath("data[]").description("분배가능한 헌트 목록").optional())
                                .andWithPrefix("data[]", addHuntListResponse)
                ))
                .andReturn();
    }

    @Order(2)
//    @Test
    public void userDistribution() throws Exception {
        final CallbackUserDistFormRequest form = new CallbackUserDistFormRequest();
        final List<String> users = new ArrayList<>();

        users.add("user0789");
        users.add("user0777");

        form.setUsers(users);

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL + "/service/{svcNumber}/hunt/{huntNumber}/users", "07075490163", "07075490597")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .content(mapper.writeValueAsString(form)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document.document(
                        requestFields(
                                fieldWithPath("users[]").type(JsonFieldType.ARRAY).description("분배상담원 id목록").optional(),
                                fieldWithPath("huntNumber").type(JsonFieldType.STRING).description("분배할 헌트"),
                                fieldWithPath("svcNumber").type(JsonFieldType.STRING).description("분배할서비스번호")
                        ),
                        responseFields(responseFields)
                ))
                .andReturn();

        log.info(String.valueOf(result));
    }

    @Order(4)
//    @Test
    public void addPersonList() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/divisible-persons")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("huntNumber", "07075490597"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestParameters(
                                parameterWithName("huntNumber").description("헌트번호")
                        ),
                        responseFields(responseFields)
                                .and(fieldWithPath("data[]").description("분배가능한 상담원 목록").optional())
                                .andWithPrefix("data[]", addPersonListResponse)
                ))
                .andReturn();
    }
}
