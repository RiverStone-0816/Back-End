package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.model.dto.eicn.EmailMemberListSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.EmailReceiveGroupDetailResponse;
import kr.co.eicn.ippbx.model.form.EmailReceiveGroupFormRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class EmailReceiveGroupApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/email/mng/receive-group";

    private FieldDescriptor[] emailReceiveGroupFormRequest = new FieldDescriptor[] {
            fieldWithPath("groupName").type(JsonFieldType.STRING).description("이메일그룹명"),
            fieldWithPath("emailId").type(JsonFieldType.NUMBER).description("관련이메일서비스"),
            fieldWithPath("emailMemberLists").type(JsonFieldType.ARRAY).description("그룹멤버")
    };

    private FieldDescriptor[] emailReceiveGroupDetailResponse = new FieldDescriptor[] {
            fieldWithPath("groupId").type(JsonFieldType.NUMBER).description("그룹아이디"),
            fieldWithPath("serviceId").type(JsonFieldType.NUMBER).description("서비스아이디"),
            fieldWithPath("groupName").type(JsonFieldType.STRING).description("이메일그룹명"),
            fieldWithPath("serviceName").type(JsonFieldType.STRING).description("관련이메일 서비스명"),
            fieldWithPath("emailMemberLists").type(JsonFieldType.ARRAY).description("그룹멤버")
    };

    private FieldDescriptor[] emailReceiveGroupSummaryResponse = new FieldDescriptor[] {
            fieldWithPath("groupId").type(JsonFieldType.NUMBER).description("그룹아이디"),
            fieldWithPath("groupName").type(JsonFieldType.STRING).description("이메일그룹명"),
            fieldWithPath("serviceName").type(JsonFieldType.STRING).description("관련이메일 서비스명"),
            fieldWithPath("memberCnt").type(JsonFieldType.NUMBER).description("멤버수"),
            fieldWithPath("emailMemberLists").type(JsonFieldType.ARRAY).description("그룹멤버")
    };

    private FieldDescriptor[] emailMemberListSummaryResponse = new FieldDescriptor[] {
            fieldWithPath("idName").type(JsonFieldType.STRING).description("한글이름").optional(),
            fieldWithPath("peer").type(JsonFieldType.STRING).description("전화기 ID").optional(),
            fieldWithPath("extension").type(JsonFieldType.STRING).description("내선전화번호").optional(),
            fieldWithPath("hostName").type(JsonFieldType.STRING).description("교환기명").optional(),
            fieldWithPath("id").type(JsonFieldType.STRING).description("사용자 아이디").optional(),
            fieldWithPath("organization").type(JsonFieldType.OBJECT).description("조직정보").optional()
    };

    private FieldDescriptor[] organizationSummaryResponse = new FieldDescriptor[] {
            fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드").optional(),
            fieldWithPath("groupName").type(JsonFieldType.STRING).description("조직명").optional(),
            fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("윗레벨의 코드를 포함한 코드의 나열(002_003_008)").optional(),
            fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("조직레벨").optional()
    };

    private FieldDescriptor[] emailMngResponse = new FieldDescriptor[] {
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("시퀀스"),
            fieldWithPath("serviceName").type(JsonFieldType.STRING).description("서비스명")
    };

//        @Test
    @Order(1)
    protected void list() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                        .and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("이메일 수신그룹관리 목록").optional())
                        .andWithPrefix("data.[]", emailReceiveGroupSummaryResponse)
                        .and(fieldWithPath("data.[].emailMemberLists").type(JsonFieldType.ARRAY).description("추가된 사용자 목록").optional())
                        .andWithPrefix("data.[].emailMemberLists[]", emailMemberListSummaryResponse)
                        .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }

//      @Test
    @Order(2)
    public void get() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{groupId}", 1)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("groupId").description("그룹아이디")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("상담톡수신 상세정보").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                        )
                        .andWithPrefix("data.", emailReceiveGroupDetailResponse)
                        .andWithPrefix("data.emailMemberLists[]", emailMemberListSummaryResponse)
                        .andWithPrefix("data.emailMemberLists[].organization.", organizationSummaryResponse)
                ))
                .andReturn();

        final EmailReceiveGroupDetailResponse data = getData(result, EmailReceiveGroupDetailResponse.class);

        log.info("data {}", data);
    }

//    	@Test
    @Order(1)
    public void post() throws Exception {
        final EmailReceiveGroupFormRequest form = new EmailReceiveGroupFormRequest();
        form.setGroupName("이메일그룹");     //이메일그룹명
        form.setEmailId(2);   //관련이메일서비스

        final Set<String> emailMemberLists = new HashSet<>();
        emailMemberLists.add("user01");

        form.setEmailMemberLists(emailMemberLists);

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .content(mapper.writeValueAsString(form)))
                .andDo(print())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andDo(document.document(
                        requestFields(emailReceiveGroupFormRequest),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("data").type(JsonFieldType.NUMBER).description("등록 SEQUENCE 값").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                        )
                ))
                .andReturn();

        final String groupId = getData(result, String.class);
        log.info("groupId({})", groupId);
    }

//    	@Test
    @Order(2)
    protected void put() throws Exception {
        final EmailReceiveGroupFormRequest form = new EmailReceiveGroupFormRequest();

        form.setGroupName("이메일그룹수정");   //이메일그룹명
        form.setEmailId(1);     //관련이메일서비스

        final Set<String> emailMemberLists = new HashSet<>();
        emailMemberLists.add("user01");

        form.setEmailMemberLists(emailMemberLists);

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/{groupId}", 1)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .content(mapper.writeValueAsString(form)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("groupId").description("그룹아이디")
                        ),
                        requestFields(emailReceiveGroupFormRequest),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("data").type(JsonFieldType.NUMBER).description("등록 SEQUENCE 값").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                        )
                ))
                .andReturn();
    }

//      @Test
    @Order(5)
    protected void delete() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{groupId}", 1)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("result").value("success"))
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("groupId").description("그룹아이디")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("data").type(JsonFieldType.NUMBER).description("등록 SEQUENCE 값").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                        )
                ))
                .andReturn();
    }

    /**
     * 관련 이메일 서비스 목록조회
     */
//    	@Test
    protected void emailService() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/services")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                        .and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("이메일서비스 목록").optional())
                        .andWithPrefix("data.[]", emailMngResponse)
                        .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }

    /**
     *  추가 가능한 사용자 목록조회
     */
//    	@Test
    protected void addMember() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/add-member")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestParameters(
                                parameterWithName("groupId").description("그룹아이디").optional()
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                        .and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("추가 가능한 사용자 목록").optional())
                        .andWithPrefix("data.[]", emailMemberListSummaryResponse)
                        .andWithPrefix("data.[].organization.", organizationSummaryResponse)
                        .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();

        final List<EmailMemberListSummaryResponse> responses = listData(result, EmailMemberListSummaryResponse.class);
        for (EmailMemberListSummaryResponse response : responses) {
            log.info(response.toString());
        }
    }
}
