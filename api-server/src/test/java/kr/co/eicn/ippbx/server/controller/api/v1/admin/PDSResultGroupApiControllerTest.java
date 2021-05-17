package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.server.model.dto.eicn.*;
import kr.co.eicn.ippbx.server.model.enums.CallDistributionStrategy;
import kr.co.eicn.ippbx.server.model.enums.PDSResultGroupStrategy;
import kr.co.eicn.ippbx.server.model.form.*;
import kr.co.eicn.ippbx.server.repository.eicn.*;
import kr.co.eicn.ippbx.server.util.page.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static kr.co.eicn.ippbx.server.jooq.eicn.tables.QueueMemberTable.QUEUE_MEMBER_TABLE;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class PDSResultGroupApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/outbound/pds/result-group";
    private static String queueName;

    @Autowired
    private PDSQueueNameRepository queueNameRepository;
    @Autowired
    private PDSQueueTableRepository queueTableRepository;
    @Autowired
    private QueueMemberTableRepository queueMemberTableRepository;


    private FieldDescriptor[] pdsResultGroupSummaryResponse = new FieldDescriptor[] {
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("시퀀스"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("헌트그룹명(영문)"),
            fieldWithPath("hanName").type(JsonFieldType.STRING).description("헌트그룹명(한글)"),
            fieldWithPath("strategy").type(JsonFieldType.STRING).description("분배정책"),
            fieldWithPath("hostName").type(JsonFieldType.STRING).description("교환기명"),
            fieldWithPath("cnt").type(JsonFieldType.NUMBER).description("사용자수")
    };

    private FieldDescriptor[] pdsResultGroupDetailResponse = new FieldDescriptor[] {
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("시퀀스"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("QUEUE 이름"),
            fieldWithPath("hanName").type(JsonFieldType.STRING).description("QUEUE 한글이름"),
            fieldWithPath("strategy").type(JsonFieldType.STRING).description("분배정책(\"\":지정안됨, rrmemory:마지막통화한다음순서(roundrobin), ringall:동시모두, sequence:목록순서, leastrecent:최소한최근에받은순서, fewestcalls:연결횟수가적은순서, random:랜덤"),
            fieldWithPath("cnt").type(JsonFieldType.NUMBER).description("사용자 수"),
            fieldWithPath("hostName").type(JsonFieldType.STRING).description("해당 QUEUE 사용IP"),
            fieldWithPath("busyContext").type(JsonFieldType.STRING).description("비연결시 컨텍스트").optional(),
            fieldWithPath("addPersons").type(JsonFieldType.ARRAY).description("추가된 헌트사용자 목록").optional(),
    };

    private FieldDescriptor[] summaryQueuePersonResponse = new FieldDescriptor[] {
            fieldWithPath("idName").type(JsonFieldType.STRING).description("한글이름").optional(),
            fieldWithPath("peer").type(JsonFieldType.STRING).description("전화기 ID").optional(),
            fieldWithPath("extension").type(JsonFieldType.STRING).description("내선전화번호").optional(),
            fieldWithPath("hostName").type(JsonFieldType.STRING).description("교환기명").optional(),
            fieldWithPath("penalty").type(JsonFieldType.NUMBER).description("스킬순서값").optional(),
            fieldWithPath("companyTrees").type(JsonFieldType.ARRAY).description("조직정보").optional(),
            fieldWithPath("callRate").type(JsonFieldType.NUMBER).description("콜배율값").optional()
    };

    private FieldDescriptor[] pdsResultGroupFormRequest = new FieldDescriptor[] {
            fieldWithPath("name").type(JsonFieldType.STRING).description("QUEUE 이름").optional(),
            fieldWithPath("hanName").type(JsonFieldType.STRING).description("QUEUE 한글이름"),
            fieldWithPath("runHost").type(JsonFieldType.STRING).description("실행할 교환기").optional(),
            fieldWithPath("strategy").type(JsonFieldType.STRING).description("분배정책(ringall:동시모두, leastrecent:먼저끊은순서, fewestcalls:적게받은순서, rrmemory:마지막통화한다음순서, random:랜덤, skill:스킬순서, callrate:콜배율순서 )"),
            fieldWithPath("busyContext").type(JsonFieldType.STRING).description("비연결시 컨텍스트").optional(),
            fieldWithPath("addPersons").type(JsonFieldType.ARRAY).description("추가 사용자 목록").optional()
    };

    private FieldDescriptor[] pdsResultGroupFormUpdateRequest = new FieldDescriptor[] {
            fieldWithPath("name").type(JsonFieldType.STRING).description("QUEUE 이름").optional(),
            fieldWithPath("hanName").type(JsonFieldType.STRING).description("QUEUE 한글이름"),
            fieldWithPath("runHost").type(JsonFieldType.STRING).description("실행할 교환기").optional(),
            fieldWithPath("strategy").type(JsonFieldType.STRING).description("분배정책(ringall:동시모두, leastrecent:먼저끊은순서, fewestcalls:적게받은순서, rrmemory:마지막통화한다음순서, random:랜덤, skill:스킬순서, callrate:콜배율순서 )"),
            fieldWithPath("busyContext").type(JsonFieldType.STRING).description("비연결시 컨텍스트").optional(),
            fieldWithPath("addPersons").type(JsonFieldType.ARRAY).description("추가 사용자 목록").optional()
    };

    private FieldDescriptor[] pdsResultGroupPersonFormRequest = new FieldDescriptor[] {
            fieldWithPath("peer").type(JsonFieldType.STRING).description("전화기 ID"),
    };

    private FieldDescriptor[] summaryCompanyServerResponse = new FieldDescriptor[] {
            fieldWithPath("host").type(JsonFieldType.STRING).description("호스트"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("호스트명")
    };

    private FieldDescriptor[] context = new FieldDescriptor[] {
            fieldWithPath("context").type(JsonFieldType.STRING).description("context"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("context명"),
    };

    private FieldDescriptor[] summaryPersonResponses = new FieldDescriptor[] {
            fieldWithPath("idName").type(JsonFieldType.STRING).description("한글이름").optional(),
            fieldWithPath("peer").type(JsonFieldType.STRING).description("전화기 ID").optional(),
            fieldWithPath("extension").type(JsonFieldType.STRING).description("내선전화번호").optional(),
            fieldWithPath("hostName").type(JsonFieldType.STRING).description("교환기명").optional()
    };

    private FieldDescriptor[] organizationSummaryResponse = new FieldDescriptor[] {
            fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드"),
            fieldWithPath("groupName").type(JsonFieldType.STRING).description("조직명"),
            fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("윗레벨의 코드를 포함한 코드의 나열(002_003_008)").optional(),
            fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("조직레벨")
    };


//      @Test
    @Order(4)
    protected void pagination() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("page", "1")
                .param("limit", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                                .and(fieldWithPath("data").type(JsonFieldType.OBJECT).description("반환 될 데이터").optional())
                                .and(fieldWithPath("data.rows[]").type(JsonFieldType.ARRAY).description("헌트 그룹 목록").optional())
                                .andWithPrefix("data.rows[]", pdsResultGroupSummaryResponse)
                                .andWithPrefix("data.", fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지"),
                                        fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총갯수"))
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();

          final Pagination<PDSResultGroupSummaryResponse> pagination = paginationData(result, PDSResultGroupSummaryResponse.class);
          final List<PDSResultGroupSummaryResponse> rows = pagination.getRows();
          for (PDSResultGroupSummaryResponse row : rows) {
              log.info(row.toString());
          }
    }

//    	@Test
    @Order(3)
    protected void get() throws Exception {
        final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{name}", "PDS_QUEUE2")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("name").description("QUEUE 이름")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                                .and(fieldWithPath("data").type(JsonFieldType.OBJECT).description("헌트그룹 상세정보"))
                                .andWithPrefix("data.", pdsResultGroupDetailResponse)
                                .andWithPrefix("data.addPersons[]", summaryQueuePersonResponse)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();

        final PDSResultGroupDetailResponse data = getData(result, PDSResultGroupDetailResponse.class);

        log.info("data {}", data);
    }

//    @Test
    @Order(1)
    @Override
    protected void post() throws Exception {
        final PDSResultGroupFormRequest form = new PDSResultGroupFormRequest();
        form.setHanName("EICN_TEST_PDS_헌트");            // 헌트그룹명 (*)
        form.setStrategy(PDSResultGroupStrategy.RRMEMORY.getCode()); // 통화분배정책 - 마지막통화한다음순서 (*)
        form.setBusyContext(StringUtils.EMPTY);      // 비연결시 컨텍스트 - 지정안함
        final List<PDSResultGroupPersonFormRequest> addPersons = new ArrayList<>();
        final PDSResultGroupPersonFormRequest person = new PDSResultGroupPersonFormRequest();

        person.setUserId("75490677");
        addPersons.add(person);
        form.setAddPersons(addPersons);
        form.setRunHost("localhost");

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(form))
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document.document(
                        requestFields(pdsResultGroupFormRequest)
                                .andWithPrefix("addPersons[]", pdsResultGroupPersonFormRequest),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("data").type(JsonFieldType.STRING).description("QUEUE 이름").optional()
                        )
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();

        final String data = getData(result, String.class);
        log.info(data);
        queueName = data;
    }

//    	@Test
    @Order(2)
    protected void put() throws Exception {
        final PDSResultGroupFormRequest form = new PDSResultGroupFormRequest();
        form.setHanName("EICN_TEST_PDS_헌트 수정");            // 헌트그룹명 (*)
        form.setStrategy(CallDistributionStrategy.RINGALL.getCode()); // 통화분배정책 - 마지막통화한다음순서 (*)
        form.setBusyContext(StringUtils.EMPTY);      // 비연결시 컨텍스트 - 지정안함
        final List<PDSResultGroupPersonFormRequest> addPersons = new ArrayList<>();
        final PDSResultGroupPersonFormRequest person = new PDSResultGroupPersonFormRequest();

        person.setUserId("75490677");
        addPersons.add(person);
        form.setAddPersons(addPersons);
        form.setRunHost("localhost");

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/{name}", "PDS_QUEUE3")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .content(mapper.writeValueAsString(form)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("name").description("QUEUE 이름")
                        ),
                        requestFields(pdsResultGroupFormUpdateRequest)
                                .andWithPrefix("addPersons[]", pdsResultGroupPersonFormRequest),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("data").type(JsonFieldType.NULL).description("")
                        )
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }

//    	@Test
    @Order(5)
    @Override
    protected void delete() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{name}", "PDS_QUEUE3")
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("result").value("success"))
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("name").description("QUEUE 이름")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("data").type(JsonFieldType.NULL).description(""),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                        )
                ))
                .andReturn();

        assertNull(queueNameRepository.findOne(queueName));
        assertNull(queueTableRepository.findOne(queueName));
        assertEquals(0, queueMemberTableRepository.findAll(QUEUE_MEMBER_TABLE.QUEUE_NAME.eq(queueName)).size());
    }

    /**
     * 실행할교환기 목록 조회
     */
//    @Test
    protected void add_servers() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/add-server")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                                .and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("교환기 목록").optional())
                                .andWithPrefix("data.[]", summaryCompanyServerResponse)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }

    /**
     * CONTEXT 목록조회
     */
//	@Test
    protected void context() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/context")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                                .and(fieldWithPath("data").type(JsonFieldType.ARRAY).description("context 정보 목록"))
                                .andWithPrefix("data.[]", context)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();

        final List<SummaryContextInfoResponse> contextInfoResponses = listData(result, SummaryContextInfoResponse.class);
        for (SummaryContextInfoResponse contextInfoResponse : contextInfoResponses) {
            log.info(contextInfoResponse.toString());
        }
    }

    /**
     * 추가 가능한 사용자 목록조회
     */
//	@Test
    protected void add_on_persons() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/add-on-persons")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("name", "")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestParameters(
                                parameterWithName("name").description("QUEUE 이름").optional()
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                                .and(fieldWithPath("data").type(JsonFieldType.ARRAY).description("추가 가능 사용자").optional())
                                .andWithPrefix("data.[]", summaryPersonResponses)
                                .andWithPrefix("data[].companyTrees[]", organizationSummaryResponse)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();

        final List<SummaryPersonResponse> summaryPersonResponses = listData(result, SummaryPersonResponse.class);
        for (SummaryPersonResponse summaryPersonResponse : summaryPersonResponses) {
            log.info(summaryPersonResponse.toString());
        }
    }

}
