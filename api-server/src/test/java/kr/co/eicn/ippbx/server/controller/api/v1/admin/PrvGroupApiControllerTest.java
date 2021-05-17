package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.server.model.enums.BillingKind;
import kr.co.eicn.ippbx.server.model.form.PrvGroupFormRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class PrvGroupApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/outbound/preview/group";

    private FieldDescriptor[] prvGroupSummaryResponse = new FieldDescriptor[] {
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("시퀀스"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("그룹명"),
            fieldWithPath("prvType").type(JsonFieldType.NUMBER).description("업로드유형").optional(),
            fieldWithPath("resultType").type(JsonFieldType.NUMBER).description("상담결과유형").optional(),
            fieldWithPath("prvTypeName").type(JsonFieldType.STRING).description("업로드 유형명").optional(),
            fieldWithPath("resultTypeName").type(JsonFieldType.STRING).description("상담결과 유형명").optional(),
            fieldWithPath("totalCnt").type(JsonFieldType.NUMBER).description("데이터수"),
            fieldWithPath("groupCode").type(JsonFieldType.STRING).description("그룹코드").optional(),
            fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("그룹이름").optional(),
            fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("그룹레벨").optional(),
            fieldWithPath("groupTreeNames").type(JsonFieldType.ARRAY).description("계층 구조에 따른 array").optional()
    };

    private FieldDescriptor[] prvGroupDetailResponse = new FieldDescriptor[] {
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("시퀀스"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("그룹명"),
            fieldWithPath("prvType").type(JsonFieldType.NUMBER).description("업로드유형"),
            fieldWithPath("resultType").type(JsonFieldType.NUMBER).description("상담결과유형"),
            fieldWithPath("prvTypeName").type(JsonFieldType.STRING).description("업로드 유형명"),
            fieldWithPath("resultTypeName").type(JsonFieldType.STRING).description("상담결과 유형명"),
            fieldWithPath("info").type(JsonFieldType.STRING).description("추가정보"),
            fieldWithPath("dialTimeout").type(JsonFieldType.NUMBER).description("다이얼시간"),
            fieldWithPath("ridKind").type(JsonFieldType.STRING).description("발신번호 설정"),
            fieldWithPath("ridData").type(JsonFieldType.STRING).description("발신번호 데이터").optional(),
            fieldWithPath("billingKind").type(JsonFieldType.STRING).description("과금번호 설정"),
            fieldWithPath("billingData").type(JsonFieldType.STRING).description("과금번호 데이터").optional(),
            fieldWithPath("totalCnt").type(JsonFieldType.NUMBER).description("데이터수"),
            fieldWithPath("memberKind").type(JsonFieldType.STRING).description("상담원 설정"),
            fieldWithPath("memberDataList").type(JsonFieldType.ARRAY).description("상담원").optional(),
            fieldWithPath("groupCode").type(JsonFieldType.STRING).description("그룹코드").optional(),
            fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("그룹이름").optional(),
            fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("그룹레벨").optional(),
            fieldWithPath("groupTreeNames").type(JsonFieldType.ARRAY).description("계층 구조에 따른 array").optional(),
            fieldWithPath("commonFields").type(JsonFieldType.ARRAY).description("업로드유형필드, 종류")
    };

    private FieldDescriptor[] commonFieldDetailResponse = new FieldDescriptor[]{
            fieldWithPath("fieldInfo").type(JsonFieldType.STRING).description("업로드유형필드").optional(),
            fieldWithPath("fieldType").type(JsonFieldType.STRING).description("업로드유형필드 종류").optional()
    };

    private FieldDescriptor[] person = new FieldDescriptor[]{
            fieldWithPath("id").type(JsonFieldType.STRING).description("상담원ID").optional(),
            fieldWithPath("idName").type(JsonFieldType.STRING).description("상담원명").optional()
    };

    private FieldDescriptor[] prvGroupResponse = new FieldDescriptor[]{
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("그룹ID").optional(),
            fieldWithPath("name").type(JsonFieldType.STRING).description("그륩명").optional()
    };

    private FieldDescriptor[] commonTypeResponse = new FieldDescriptor[] {
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("시퀀스"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("유형명")
               };

    private FieldDescriptor[] prvGroupNumber070Response = new FieldDescriptor[] {
            fieldWithPath("number").type(JsonFieldType.STRING).description("번호")
    };

    private FieldDescriptor[] prvGroupFormRequest = new FieldDescriptor[] {
            fieldWithPath("name").type(JsonFieldType.STRING).description("그룹명"),
            fieldWithPath("prvType").type(JsonFieldType.NUMBER).description("프리뷰 유형"),
            fieldWithPath("resultType").type(JsonFieldType.NUMBER).description("상담결과 유형"),
            fieldWithPath("groupCode").type(JsonFieldType.STRING).description("그룹코드").optional(),
            fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("그룹이름").optional(),
            fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("그룹레벨").optional(),
            fieldWithPath("info").type(JsonFieldType.STRING).description("추가정보").optional(),
            fieldWithPath("dialTimeout").type(JsonFieldType.NUMBER).description("다이얼시간"),
            fieldWithPath("ridKind").type(JsonFieldType.STRING).description("발신번호 설정"),
            fieldWithPath("ridData").type(JsonFieldType.STRING).description("(내선별, 프리뷰그룹별)").optional(),
            fieldWithPath("billingKind").type(JsonFieldType.STRING).description("과금번호 설정"),
            fieldWithPath("billingData").type(JsonFieldType.STRING).description("(내선별, 프리뷰그룹별 번호)").optional(),
            fieldWithPath("memberKind").type(JsonFieldType.STRING).description("상담원 설정"),
            fieldWithPath("memberDataList").type(JsonFieldType.ARRAY).description("(상담원 지정)").optional()
    };

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        document = document(
                "{class-name}/{method-name}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())
        );
        this.mockMvc = securityBuilder(webApplicationContext, restDocumentation);
    }

//    @Test
    @Order(5)
    protected void pagination() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("page", "1")
                .param("limit", "10")
                .param("prvTypeName", "")
                .param("name", ""))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestParameters(
                                parameterWithName("page").description("현재 페이지"),
                                parameterWithName("limit").description("페이지 개수"),
                                parameterWithName("prvTypeName").description("업로드유형").optional(),
                                parameterWithName("name").description("그룹명").optional()
                        ),
                        responseFields(
                                fieldWithPath("data.rows[]").description("프리뷰 그룹관리").optional(),
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional(),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("실제 반환될 데이터").optional()
                        )
                                .andWithPrefix("data.rows[]", prvGroupSummaryResponse)
                                .andWithPrefix("data.",
                                        fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지"),
                                        fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총갯수"))
                ))
                .andReturn();
    }

//     @Test
    @Order(3)
    protected void get() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{seq}", 1)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("seq").description("시퀀스")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("실제 반환될 데이터").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                        )
                                .andWithPrefix("data.", prvGroupDetailResponse)
                                .andWithPrefix("data.memberDataList[]", person)
                                .andWithPrefix("data.commonFields[]", commonFieldDetailResponse)
                ))
                .andReturn();
    }

//      @Test
    @Order(1)
    protected void post() throws Exception {
        final PrvGroupFormRequest form = new PrvGroupFormRequest();
        form.setName("prvGroup");     //그룹명
        form.setPrvType(1);
        form.setResultType(1);
        form.setInfo("info");

        form.setDialTimeout((byte) 30);
        form.setRidKind("PBX");
        form.setBillingKind("NUMBER");
        form.setBillingData("07075490677");
        form.setMemberKind("CAMPAIGN"); //상담원 설정

        final Set<String> memberList = new HashSet<>();
        memberList.add("user0677");
        form.setMemberDataList(memberList);   //(상담원 지정)

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(form))
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document.document(
                        requestFields(prvGroupFormRequest),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("data").type(JsonFieldType.NUMBER).description("실제 반환될 데이터").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                        )
                ))
                .andReturn();
    }

//      @Test
    @Order(2)
    protected void put() throws Exception {
        final PrvGroupFormRequest form = new PrvGroupFormRequest();
        form.setName("prvGroup수정");     //그룹명
        form.setPrvType(1);
        form.setResultType(1);
        form.setInfo("info");

        form.setDialTimeout((byte) 30);
        form.setRidKind("PBX");
        form.setBillingKind(BillingKind.DIRECT.getCode());
        form.setBillingData("data");
        form.setMemberKind("CAMPAIGN"); //상담원 설정

        final Set<String> memberList = new HashSet<>();
        memberList.add("user0677");
        memberList.add("user0678");
        form.setMemberDataList(memberList);   //(상담원 지정)

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/{seq}" , 6)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .content(mapper.writeValueAsString(form)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("seq").description("시퀀스")
                        ),
                        requestFields(prvGroupFormRequest),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("data").type(JsonFieldType.NUMBER).description("실제 반환될 데이터").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                        )
                ))
                .andReturn();
    }

//      @Test
    @Order(5)
    protected void delete() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{seq}", 6)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("result").value("success"))
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("seq").description("시퀀스")
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
     * 업로드유형 목록조회
     */
//   @Test
    protected void prvType() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/preview-type")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                                .and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("업로드유형 목록").optional())
                                .andWithPrefix("data.[]", commonTypeResponse)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }

    /**
     * 상담결과유형 목록조회
     */
//    	@Test
    protected void resultType() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/result-type")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                                .and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("상담결과유형 목록").optional())
                                .andWithPrefix("data.[]", commonTypeResponse)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }

    /**
     * 프리뷰그룹 번호선택 목록
     */
//    	@Test
    protected void prvNumber() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/preview-number")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                                .and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("번호선택 목록").optional())
                                .andWithPrefix("data.[]", prvGroupNumber070Response)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }

    /**
     * 상담원 목록
     */
//    	@Test
    protected void person() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/person")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                                .and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("상담결과유형 목록").optional())
                                .andWithPrefix("data.[]", person)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }

    /**
     * 프리뷰 수행 그룹
     */
//    	@Test
    protected void prvGroup() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/preview-group")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                                .and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("번호선택 목록").optional())
                                .andWithPrefix("data.[]", prvGroupResponse)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }
}
