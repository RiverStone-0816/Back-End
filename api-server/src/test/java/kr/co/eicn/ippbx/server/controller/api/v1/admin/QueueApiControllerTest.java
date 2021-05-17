package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.Number_070;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.QueueName;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ServiceList;
import kr.co.eicn.ippbx.server.model.dto.eicn.SummaryPersonResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.*;
import kr.co.eicn.ippbx.server.model.enums.CallDistributionStrategy;
import kr.co.eicn.ippbx.server.model.enums.ForwardingType;
import kr.co.eicn.ippbx.server.model.form.QueueFormRequest;
import kr.co.eicn.ippbx.server.model.form.QueueFormUpdateRequest;
import kr.co.eicn.ippbx.server.model.form.QueuePersonFormRequest;
import kr.co.eicn.ippbx.server.model.form.QueueUpdateBlendingFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.Number070Repository;
import kr.co.eicn.ippbx.server.repository.eicn.QueueMemberTableRepository;
import kr.co.eicn.ippbx.server.repository.eicn.QueueNameRepository;
import kr.co.eicn.ippbx.server.repository.eicn.QueueTableRepository;
import kr.co.eicn.ippbx.server.util.page.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static kr.co.eicn.ippbx.server.jooq.eicn.tables.QueueMemberTable.QUEUE_MEMBER_TABLE;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class QueueApiControllerTest extends BaseControllerTest {
	private final String TEST_URL = "/api/v1/admin/user/user/queue";
	private static String queueName;

	@Autowired
	private QueueNameRepository queueNameRepository;
	@Autowired
	private QueueTableRepository queueTableRepository;
	@Autowired
	private QueueMemberTableRepository queueMemberTableRepository;
	@Autowired
	private Number070Repository numberRepository;

	private FieldDescriptor[] queueSummaryResponse = new FieldDescriptor[] {
		fieldWithPath("name").type(JsonFieldType.STRING).description("QUEUE 이름"),
		fieldWithPath("hanName").type(JsonFieldType.STRING).description("QUEUE 한글이름"),
		fieldWithPath("number").type(JsonFieldType.STRING).description("QUEUE 11자리번호"),
		fieldWithPath("subGroupName").type(JsonFieldType.STRING).description("예비헌트 한글그룹명").optional(),
		fieldWithPath("strategy").type(JsonFieldType.STRING).description("분배정책(ringall:동시모두, leastrecent:먼저끊은순서, fewestcalls:적게받은순서, rrmemory:마지막통화한다음순서, random:랜덤, skill:스킬순서, callrate:콜배율순서 )"),
		fieldWithPath("personCount").type(JsonFieldType.NUMBER).description("사용자 수"),
		fieldWithPath("blendingMode").type(JsonFieldType.STRING).description("블랜딩").optional(),
		fieldWithPath("companyTrees").type(JsonFieldType.ARRAY).description("조직정보 목록").optional(),
		fieldWithPath("host").type(JsonFieldType.STRING).description("해당 QUEUE 사용IP")
	};

	private FieldDescriptor[] queueDetailResponse = new FieldDescriptor[] {
			fieldWithPath("name").type(JsonFieldType.STRING).description("QUEUE 이름"),
			fieldWithPath("hanName").type(JsonFieldType.STRING).description("QUEUE 한글이름"),
			fieldWithPath("number").type(JsonFieldType.STRING).description("QUEUE 11자리번호"),
			fieldWithPath("subGroupName").type(JsonFieldType.STRING).description("예비헌트 한글그룹명").optional(),
			fieldWithPath("strategy").type(JsonFieldType.STRING).description("분배정책(ringall:동시모두, leastrecent:먼저끊은순서, fewestcalls:적게받은순서, rrmemory:마지막통화한다음순서, random:랜덤, skill:스킬순서, callrate:콜배율순서 )"),
			fieldWithPath("personCount").type(JsonFieldType.NUMBER).description("사용자 수"),
			fieldWithPath("blendingMode").type(JsonFieldType.STRING).description("블랜딩").optional(),
			fieldWithPath("companyTrees").type(JsonFieldType.ARRAY).description("조직정보 목록").optional(),
			fieldWithPath("host").type(JsonFieldType.STRING).description("해당 QUEUE 사용IP"),

			fieldWithPath("svcNumber").type(JsonFieldType.STRING).description("서비스 번호").optional(),
			fieldWithPath("maxlen").type(JsonFieldType.NUMBER).description("최대대기자명수"),
			fieldWithPath("queueTimeout").type(JsonFieldType.NUMBER).description("헌트 총대기시간"),
			fieldWithPath("timeout").type(JsonFieldType.NUMBER).description("사용자별 대기시간"),
			fieldWithPath("musiconhold").type(JsonFieldType.STRING).description("대기음원").optional(),
			fieldWithPath("musiconholdName").type(JsonFieldType.STRING).description("음원명").optional(),
			fieldWithPath("busyContext").type(JsonFieldType.STRING).description("비연결시 컨텍스트").optional(),
			fieldWithPath("busyContextName").type(JsonFieldType.STRING).description("컨텍스트명").optional(),
			fieldWithPath("isForwarding").type(JsonFieldType.STRING).description("비상시 포워딩"),
			fieldWithPath("huntForwarding").type(JsonFieldType.STRING).description("비상시 포워딩 값").optional(),
			fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드"),
			fieldWithPath("subGroup").type(JsonFieldType.STRING).description("예비 QUEUE이름").optional(),

			fieldWithPath("addPersons").type(JsonFieldType.ARRAY).description("추가된 헌트사용자 목록").optional(),
	};

	private FieldDescriptor[] queueFormRequest = new FieldDescriptor[] {
		fieldWithPath("name").type(JsonFieldType.STRING).description("QUEUE 이름").optional(),
		fieldWithPath("hanName").type(JsonFieldType.STRING).description("QUEUE 한글이름"),
		fieldWithPath("number").type(JsonFieldType.STRING).description("QUEUE 11자리번호"),
		fieldWithPath("svcNumber").type(JsonFieldType.STRING).description("관련서비스").optional(),
		fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드"),
		fieldWithPath("strategy").type(JsonFieldType.STRING).description("분배정책(ringall:동시모두, leastrecent:먼저끊은순서, fewestcalls:적게받은순서, rrmemory:마지막통화한다음순서, random:랜덤, skill:스킬순서, callrate:콜배율순서 )"),
		fieldWithPath("subGroup").type(JsonFieldType.STRING).description("예비헌트").optional(),
		fieldWithPath("maxlen").type(JsonFieldType.NUMBER).description("최대대기자명수"),
		fieldWithPath("queueTimeout").type(JsonFieldType.NUMBER).description("헌트 총대기시간"),
		fieldWithPath("timeout").type(JsonFieldType.NUMBER).description("사용자별 대기시간"),
		fieldWithPath("musiconhold").type(JsonFieldType.STRING).description("대기음원"),
		fieldWithPath("busyContext").type(JsonFieldType.STRING).description("비연결시 컨텍스트").optional(),
		fieldWithPath("isForwarding").type(JsonFieldType.STRING).description("비상시 포워딩"),
		fieldWithPath("huntForwarding").type(JsonFieldType.STRING).description("비상시 포워딩 값").optional(),
		fieldWithPath("addPersons").type(JsonFieldType.ARRAY).description("추가 사용자 목록").optional()
	};

	private FieldDescriptor[] queueFormUpdateRequest = new FieldDescriptor[] {
			fieldWithPath("name").type(JsonFieldType.STRING).description("QUEUE 이름").optional(),
			fieldWithPath("hanName").type(JsonFieldType.STRING).description("QUEUE 한글이름"),
			fieldWithPath("number").type(JsonFieldType.STRING).description("QUEUE 11자리번호"),
			fieldWithPath("svcNumber").type(JsonFieldType.STRING).description("관련서비스").optional(),
			fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드"),
			fieldWithPath("strategy").type(JsonFieldType.STRING).description("분배정책(ringall:동시모두, leastrecent:먼저끊은순서, fewestcalls:적게받은순서, rrmemory:마지막통화한다음순서, random:랜덤, skill:스킬순서, callrate:콜배율순서 )"),
			fieldWithPath("subGroup").type(JsonFieldType.STRING).description("예비헌트").optional(),
			fieldWithPath("maxlen").type(JsonFieldType.NUMBER).description("최대대기자명수"),
			fieldWithPath("queueTimeout").type(JsonFieldType.NUMBER).description("헌트 총대기시간"),
			fieldWithPath("timeout").type(JsonFieldType.NUMBER).description("사용자별 대기시간"),
			fieldWithPath("musiconhold").type(JsonFieldType.STRING).description("대기음원"),
			fieldWithPath("busyContext").type(JsonFieldType.STRING).description("비연결시 컨텍스트").optional(),
			fieldWithPath("isForwarding").type(JsonFieldType.STRING).description("비상시 포워딩"),
			fieldWithPath("huntForwarding").type(JsonFieldType.STRING).description("비상시 포워딩 값").optional(),
			fieldWithPath("addPersons").type(JsonFieldType.ARRAY).description("추가 사용자 목록").optional()
	};

	private FieldDescriptor[] queuePersonFormRequest = new FieldDescriptor[] {
			fieldWithPath("peer").type(JsonFieldType.STRING).description("전화기 ID"),
			fieldWithPath("penalty").type(JsonFieldType.NUMBER).description("스킬순서값").optional(),
			fieldWithPath("callRate").type(JsonFieldType.NUMBER).description("콜배율값").optional()
	};

	private FieldDescriptor[] summaryServiceListResponse = new FieldDescriptor[] {
		fieldWithPath("svcName").type(JsonFieldType.STRING).description("대표번호 한글명"),
		fieldWithPath("svcNumber").type(JsonFieldType.STRING).description("11자리 대표번호"),
	};

	private FieldDescriptor[] summaryQueueResponse = new FieldDescriptor[] {
		fieldWithPath("name").type(JsonFieldType.STRING).description("QUEUE 이름"),
		fieldWithPath("hanName").type(JsonFieldType.STRING).description("QUEUE 한글이름"),
		fieldWithPath("number").type(JsonFieldType.STRING).description("QUEUE 11자리번호")
	};

	private FieldDescriptor[] summaryPersonResponses = new FieldDescriptor[] {
		fieldWithPath("idName").type(JsonFieldType.STRING).description("한글이름").optional(),
		fieldWithPath("peer").type(JsonFieldType.STRING).description("전화기 ID").optional(),
		fieldWithPath("extension").type(JsonFieldType.STRING).description("내선전화번호").optional(),
		fieldWithPath("hostName").type(JsonFieldType.STRING).description("교환기명").optional()
	};

	private FieldDescriptor[] summaryQueuePersonResponse = new FieldDescriptor[] {
			fieldWithPath("idName").type(JsonFieldType.STRING).description("한글이름").optional(),
			fieldWithPath("peer").type(JsonFieldType.STRING).description("전화기 ID").optional(),
			fieldWithPath("extension").type(JsonFieldType.STRING).description("내선전화번호").optional(),
			fieldWithPath("hostName").type(JsonFieldType.STRING).description("교환기명").optional(),
			fieldWithPath("penalty").type(JsonFieldType.NUMBER).description("스킬순서값").optional(),
			fieldWithPath("callRate").type(JsonFieldType.NUMBER).description("콜배율값").optional(),
			fieldWithPath("companyTrees").type(JsonFieldType.ARRAY).description("조직정보 목록").optional()
	};

	private FieldDescriptor[] organizationSummaryResponse = new FieldDescriptor[] {
			fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드"),
			fieldWithPath("groupName").type(JsonFieldType.STRING).description("조직명"),
			fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("윗레벨의 코드를 포함한 코드의 나열(002_003_008)").optional(),
			fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("조직레벨")
	};

	private FieldDescriptor[] mohList = new FieldDescriptor[] {
			fieldWithPath("category").type(JsonFieldType.STRING).description("카테고리"),
			fieldWithPath("mohName").type(JsonFieldType.STRING).description("컬러링명"),
	};

	private FieldDescriptor[] context = new FieldDescriptor[] {
			fieldWithPath("context").type(JsonFieldType.STRING).description("context"),
			fieldWithPath("name").type(JsonFieldType.STRING).description("context명"),
	};

	private FieldDescriptor[] blendingFormRequest = new FieldDescriptor[]{
			fieldWithPath("blendingMode").type(JsonFieldType.STRING).description("블랜딩정책"),
			fieldWithPath("waitingCount").type(JsonFieldType.NUMBER).description("기존고객대기자명수"),
			fieldWithPath("waitingKeepTime").type(JsonFieldType.NUMBER).description("기준명수 초과후 유지시간"),
			fieldWithPath("startHour").type(JsonFieldType.NUMBER).description("시작시간"),
			fieldWithPath("startMinute").type(JsonFieldType.NUMBER).description("시작분"),
			fieldWithPath("endHour").type(JsonFieldType.NUMBER).description("종료 시간"),
			fieldWithPath("endMinute").type(JsonFieldType.NUMBER).description("종료분"),
			fieldWithPath("blendingUser").type(JsonFieldType.ARRAY).description("블랜딩사용자 목록")
	};

    private FieldDescriptor[] blendingPersonResponse = new FieldDescriptor[]{
            fieldWithPath("blendingMode").type(JsonFieldType.STRING).description("블랜딩정책").optional(),
            fieldWithPath("extension").type(JsonFieldType.STRING).description("내선번호").optional(),
            fieldWithPath("idName").type(JsonFieldType.STRING).description("상담원명").optional()
    };

//	@Test
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
						.andWithPrefix("data.rows[]", queueSummaryResponse)
						.andWithPrefix("data.rows[].companyTrees[]", organizationSummaryResponse)
						.andWithPrefix("data.", fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지"),
								fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총갯수"))
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();

		final Pagination<QueueSummaryResponse> pagination = paginationData(result, QueueSummaryResponse.class);
		final List<QueueSummaryResponse> rows = pagination.getRows();
		for (QueueSummaryResponse row : rows) {
			log.info(row.toString());
		}
	}

//	@Test
	@Order(3)
	protected void get() throws Exception {
		final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{name}", "QUEUE2")
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
						.andWithPrefix("data.", queueDetailResponse)
						.andWithPrefix("data.companyTrees[]", organizationSummaryResponse)
						.andWithPrefix("data.addPersons[]", summaryQueuePersonResponse)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();

		final QueueDetailResponse data = getData(result, QueueDetailResponse.class);

		log.info("data {}", data);
	}

//	@Test
	@Order(1)
	protected void post() throws Exception {
		final QueueFormRequest form = new QueueFormRequest();
		form.setHanName("EICN_영업_헌트");            // 헌트그룹명 (*)
		form.setNumber("00000000007");                  // 헌트번호 (*)
//		form.setSvcNumber("");                          // 관련서비스
		form.setGroupCode("0009");                      // 조직코드 (*)
		form.setStrategy(CallDistributionStrategy.RRMEMORY.getCode()); // 통화분배정책 - 마지막통화한다음순서 (*)
//		form.setSubGroup("");                       // 예비헌트
		form.setMaxlen(15);                         // 최대대기자명수 (*)
		form.setQueueTimeout(50);                  // 헌트 총대기시간 (*)
		form.setTimeout(15);                       // 사용자별 대기시간 15초 이상 (*)
		form.setMusiconhold("default");             // 대기음원 (*)
		form.setIsForwarding(ForwardingType.NONE.getCode()); // 비상시 포워딩 (*)
		form.setHuntForwarding("");                 // 비상시 포워딩 값

		final List<QueuePersonFormRequest> addPersons = new ArrayList<>();
		final QueuePersonFormRequest person = new QueuePersonFormRequest();

		person.setPeer("75490677");
		addPersons.add(person);
		form.setAddPersons(addPersons);

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(form))
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isCreated())
				.andDo(document.document(
						requestFields(queueFormRequest)
						.andWithPrefix("addPersons[]", queuePersonFormRequest),
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

//	@Test
	@Order(2)
	protected void put() throws Exception {
		final QueueFormUpdateRequest form = new QueueFormUpdateRequest();
//		form.setName("QUEUE1"/*QUEUE1*/);            // 헌트그룹명 (*)
		form.setHanName("EICN_기술운영_헌트 TEST_CODE");            // 헌트그룹명 (*)
		form.setNumber("07075498130");                  // 헌트번호 (*)
//		form.setSvcNumber("07075498130");               // 관련서비스
		form.setGroupCode("0008");                      // 조직코드 (*)
		form.setStrategy(CallDistributionStrategy.RINGALL.getCode()); // 통화분배정책 - 마지막통화한다음순서 (*)
//		form.setSubGroup("");                       // 예비헌트
		form.setMaxlen(15);                         // 최대대기자명수 (*)
		form.setQueueTimeout(50);                  // 헌트 총대기시간 (*)
		form.setTimeout(15);                       // 사용자별 대기시간 15초 이상 (*)
		form.setMusiconhold("default");             // 대기음원 (*)
		form.setIsForwarding(ForwardingType.NONE.getCode()); // 비상시 포워딩 (*)
		form.setHuntForwarding("");                 // 비상시 포워딩 값

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/{name}", "QUEUE5")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						pathParameters(
								parameterWithName("name").description("QUEUE 이름")
						),
						requestFields(queueFormUpdateRequest)
								.andWithPrefix("addPersons[]", queuePersonFormRequest),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").type(JsonFieldType.NULL).description("")
						)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();
	}

//	@Test
	@Order(5)
	@Override
	protected void delete() throws Exception {
		final QueueName entity = queueNameRepository.findOne("QUEUE5");

		this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{name}", "QUEUE5")
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

		final Number_070 one = numberRepository.findOne(entity.getNumber());

		assertSame((byte)0, one.getStatus());
	}

	/**
	 * 헌트그룹 관련서비스 목록
	 */
//	@Test
	protected void services() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/services")
					.accept(MediaType.APPLICATION_JSON)
					.with(new JwtRequestPostProcessor())
					.param("host", "")
				)
				.andDo(print())
				.andDo(document.document(
						requestParameters(
								parameterWithName("host").description("해당 QUEUE 사용IP").optional()
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
						.and(fieldWithPath("data").type(JsonFieldType.ARRAY).description("헌트그룹 관련서비스 목록").optional())
						.andWithPrefix("data.[]", summaryServiceListResponse)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andExpect(status().isOk())
				.andReturn();

		final List<ServiceList> serviceLists = listData(result, ServiceList.class);
		for (ServiceList serviceList : serviceLists) {
			log.info(serviceList.toString());
		}
	}

	/**
	 * 헌트그룹 예비헌트 목록조회
	 */
//	@Test
	protected void sub_group() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/sub-groups")
					.accept(MediaType.APPLICATION_JSON)
					.with(new JwtRequestPostProcessor())
					.param("name", "07075498130")
				)
				.andDo(print())
				.andDo(document.document(
						requestParameters(
								parameterWithName("name").description("QUEUE 이름").optional()
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
						.and(fieldWithPath("data").type(JsonFieldType.ARRAY).description("예비헌트 목록").optional())
						.andWithPrefix("data.[]", summaryQueueResponse)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andExpect(status().isOk())
				.andReturn();

		final List<SummaryQueueResponse> summaryQueueResponses = listData(result, SummaryQueueResponse.class);
		for (SummaryQueueResponse summaryQueueResponse : summaryQueueResponses) {
			log.info(summaryQueueResponse.toString());
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
//				.andDo(document.document(
//					requestParameters(
//							parameterWithName("name").description("QUEUE 이름").optional()
//					),
//					responseFields(
//							fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
//							fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
//					)
//					.and(fieldWithPath("data").type(JsonFieldType.ARRAY).description("추가 가능 사용자").optional())
//					.andWithPrefix("data.[]", summaryPersonResponses)
//					.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
//				))
				.andReturn();

		final List<SummaryPersonResponse> summaryPersonResponses = listData(result, SummaryPersonResponse.class);
		for (SummaryPersonResponse summaryPersonResponse : summaryPersonResponses) {
			log.info(summaryPersonResponse.toString());
		}
	}

	/**
	 * 대기음원 목록조회
	 */
//	@Test
	protected void ring_back_tone() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/ring-back-tones")
						.accept(MediaType.APPLICATION_JSON)
						.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
						.and(fieldWithPath("data").type(JsonFieldType.ARRAY).description("대기 음원 목록").optional())
						.andWithPrefix("data.[]", mohList)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();

		final List<SummaryMohListResponse> mohLists = listData(result, SummaryMohListResponse.class);
		for (SummaryMohListResponse mohList : mohLists) {
			log.info(mohList.toString());
		}
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

//	@Test
	protected void blending() throws Exception {
		QueueUpdateBlendingFormRequest form = new QueueUpdateBlendingFormRequest();
		List<String> persons = new ArrayList<>();
		persons.add("75490677");

		form.setBlendingMode("W");
		form.setStartHour(0);
		form.setStartMinute(0);
		form.setEndHour(0);
		form.setEndMinute(0);
//		form.setWaitCnt(5);
//		form.setWaitKeepTime(5);
		form.setBlendingUser(persons);

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.patch(TEST_URL + "/{name}/blending", "QUEUE11")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						pathParameters(
								parameterWithName("name").description("큐네임")
						),
						requestFields(blendingFormRequest),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과").optional(),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				))
				.andReturn();
	}

//	@Test
    protected void getBlendingPerson() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{name}/blending", "QUEUE11")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("name").description("큐네임")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                        )
                                .and(fieldWithPath("data").type(JsonFieldType.ARRAY).description("블랜딩 상담원 목록"))
                                .andWithPrefix("data.[]", blendingPersonResponse)
                ))
                .andReturn();
    }
}
