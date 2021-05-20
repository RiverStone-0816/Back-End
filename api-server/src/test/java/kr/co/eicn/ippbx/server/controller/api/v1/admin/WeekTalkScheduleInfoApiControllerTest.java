package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.model.dto.eicn.OrganizationSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.TalkServiceInfoResponse;
import kr.co.eicn.ippbx.model.entity.eicn.TalkScheduleInfoEntity;
import kr.co.eicn.ippbx.model.form.TalkScheduleInfoFormRequest;
import kr.co.eicn.ippbx.model.form.TalkScheduleInfoFormUpdateRequest;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Order;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
public class WeekTalkScheduleInfoApiControllerTest extends BaseControllerTest {
	private final String TEST_URL = "/api/v1/admin/talk/schedule/week";

	private FieldDescriptor[] talkServiceInfoResponse = new FieldDescriptor[] {
			fieldWithPath("kakaoServiceName").type(JsonFieldType.STRING).description("상담톡서비스명"),
			fieldWithPath("senderKey").type(JsonFieldType.STRING).description("상담톡키"),
			fieldWithPath("scheduleInfos").type(JsonFieldType.ARRAY).description("스케쥴러 목록")
	};

	private FieldDescriptor[] talkScheduleInfoDetailResponse = new FieldDescriptor[] {
			fieldWithPath("kakaoServiceName").type(JsonFieldType.STRING).description("상담톡서비스명"),
			fieldWithPath("senderKey").type(JsonFieldType.STRING).description("상담톡키"),
			fieldWithPath("isChattEnable").type(JsonFieldType.STRING).description("상담톡 활성화여부"),
			fieldWithPath("seq").type(JsonFieldType.NUMBER).description("SEQUENCE KEY"),
			fieldWithPath("type").type(JsonFieldType.STRING).description("스케쥴러 구분(H:주간, H:일별)"),
			fieldWithPath("senderKey").type(JsonFieldType.STRING).description("상담톡 키"),
			fieldWithPath("week").type(JsonFieldType.STRING).description("요일(1Mon:월요일, 2Tue:화요일, 3Wed:수용일, 4Thu:목요일, 5Fri:금요일, 6Sat:토요일, 7Sun:일요일)"),
			fieldWithPath("fromdate").type(JsonFieldType.STRING).description("시작일"),
			fieldWithPath("todate").type(JsonFieldType.STRING).description("종료일"),
			fieldWithPath("groupCode").type(JsonFieldType.STRING).description("같은 group_level 에서 unique한 코드 4자리 ex>0001").optional(),
			fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("윗레벨의 코드를 포함한 코드의 나열 ex>0003_0008_0001").optional(),
			fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("해당조직의 레벨 MAX 보다 같거나 작을것").optional(),
			fieldWithPath("groupId").type(JsonFieldType.NUMBER).description("유형아이디"),
			fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사 아이디"),
			fieldWithPath("isStat").type(JsonFieldType.STRING).description("통계 사용여부"),
			fieldWithPath("scheduleGroup").type(JsonFieldType.OBJECT).description("상담톡유형정보").optional(),
			fieldWithPath("companyTrees").type(JsonFieldType.ARRAY).description("조직정보 목록").optional(),
	};

	private FieldDescriptor[] talkScheduleInfoEntity = new FieldDescriptor[] {
			fieldWithPath("seq").type(JsonFieldType.NUMBER).description("SEQUENCE KEY"),
			fieldWithPath("type").type(JsonFieldType.STRING).description("스케쥴러 구분(W:주간, H:일별)"),
			fieldWithPath("senderKey").type(JsonFieldType.STRING).description("상담톡 키"),
			fieldWithPath("week").type(JsonFieldType.STRING).description("요일(1Mon:월요일, 2Tue:화요일, 3Wed:수용일, 4Thu:목요일, 5Fri:금요일, 6Sat:토요일, 7Sun:일요일)"),
			fieldWithPath("fromdate").type(JsonFieldType.STRING).description("시작일"),
			fieldWithPath("todate").type(JsonFieldType.STRING).description("종료일"),
			fieldWithPath("groupCode").type(JsonFieldType.STRING).description("같은 group_level 에서 unique한 코드 4자리 ex>0001").optional(),
			fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("윗레벨의 코드를 포함한 코드의 나열 ex>0003_0008_0001").optional(),
			fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("해당조직의 레벨 MAX 보다 같거나 작을것").optional(),
			fieldWithPath("groupId").type(JsonFieldType.NUMBER).description("유형아이디"),
			fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사 아이디"),
			fieldWithPath("isStat").type(JsonFieldType.STRING).description("통계 사용여부"),
			fieldWithPath("scheduleGroup").type(JsonFieldType.OBJECT).description("상담톡유형정보").optional(),
			fieldWithPath("companyTrees").type(JsonFieldType.ARRAY).description("조직정보 목록").optional()
	};

	private FieldDescriptor[] talkScheduleGroupEntity = new FieldDescriptor[] {
			fieldWithPath("parent").type(JsonFieldType.NUMBER).description("고유키"),
			fieldWithPath("name").type(JsonFieldType.STRING).description("스케쥴유형명"),
			fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사 아이디"),
			fieldWithPath("scheduleGroupLists").type(JsonFieldType.ARRAY).description("스케쥴유형 목록").optional(),
	};

	private FieldDescriptor[] organizationSummaryResponse = new FieldDescriptor[] {
			fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드"),
			fieldWithPath("groupName").type(JsonFieldType.STRING).description("조직명"),
			fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("윗레벨의 코드를 포함한 코드의 나열(002_003_008)").optional(),
			fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("조직레벨")
	};

	private FieldDescriptor[] talkScheduleGroupLists = new FieldDescriptor[] {
			fieldWithPath("child").type(JsonFieldType.NUMBER).description("고유키").optional(),
			fieldWithPath("parent").type(JsonFieldType.NUMBER).description("스케쥴유형키"),
			fieldWithPath("childName").type(JsonFieldType.STRING).description("항목명").optional(),
			fieldWithPath("fromhour").type(JsonFieldType.NUMBER).description("시작시간 분 (09:00 -> 540)"),
			fieldWithPath("tohour").type(JsonFieldType.NUMBER).description("종료시간 분 (18:00 -> 1080)"),
			fieldWithPath("kind").type(JsonFieldType.STRING).description("유형구분"),
			fieldWithPath("kindData").type(JsonFieldType.STRING).description("유형별 입력 데이터"),
			fieldWithPath("kindDataName").type(JsonFieldType.STRING).description("수행 데이터").optional(),
			fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사 아이디"),
			fieldWithPath("firstMentId").type(JsonFieldType.NUMBER).description("첫인사멘트"),
			fieldWithPath("limitNum").type(JsonFieldType.NUMBER).description("비접수 n개이상 초과시 자동멘트송출(0개는 무한대)"),
			fieldWithPath("limitMentId").type(JsonFieldType.NUMBER).description("비접수초과 0이상시 필수")
	};

	private FieldDescriptor[] talkScheduleInfoFormRequest = new FieldDescriptor[] {
		fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드").optional(),
		fieldWithPath("senderKeys").type(JsonFieldType.ARRAY).description("상담톡 서비스키"),
		fieldWithPath("groupId").type(JsonFieldType.NUMBER).description("스케쥴유형")
	};

	private FieldDescriptor[] talkScheduleInfoFormUpdateRequest = new FieldDescriptor[] {
			fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드").optional(),
			fieldWithPath("groupId").type(JsonFieldType.NUMBER).description("스케쥴유형")
	};

	private FieldDescriptor[] summaryTalkScheduleInfoResponse = new FieldDescriptor[] {
			fieldWithPath("parent").type(JsonFieldType.NUMBER).description("스케쥴유형 키"),
			fieldWithPath("name").type(JsonFieldType.STRING).description("스케쥴유형명").optional(),
	};

	private FieldDescriptor[] summaryTalkServiceResponse = new FieldDescriptor[] {
			fieldWithPath("senderKey").type(JsonFieldType.STRING).description("관련상담톡서비스"),
			fieldWithPath("kakaoServiceName").type(JsonFieldType.STRING).description("상담톡 서비스명"),
	};

	/**
	 *  상담톡 주간스케쥴러 목록조회
	 */
//	@Test
	public void list() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.param("senderKey", "")  //상담톡 서비스키
				.param("groupId", "") // 유형
				.param("searchDate", "") // 요일, 날짜
				.param("groupCode", "")) // 조직코드
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						requestParameters(
								parameterWithName("senderKey").description("상담톡 서비스"),
								parameterWithName("groupId").description("유형선택"),
								parameterWithName("searchDate").description("요일"),
								parameterWithName("groupCode").description("조직코드")
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").description("실제 반환될 데이터").optional()
						)
						.andWithPrefix("data.[]", talkServiceInfoResponse)
						.and(fieldWithPath("data.[].scheduleInfos[]").type(JsonFieldType.ARRAY).description("스케쥴 항목 목록").optional())
						.andWithPrefix("data.[].scheduleInfos[]", talkScheduleInfoEntity)
						.andWithPrefix("data.[].scheduleInfos[].scheduleGroup.", talkScheduleGroupEntity)
						.andWithPrefix("data.[].scheduleInfos[].scheduleGroup.scheduleGroupLists[]", talkScheduleGroupLists)
						.andWithPrefix("data.[].scheduleInfos[].companyTrees[]", organizationSummaryResponse)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();

		final List<TalkServiceInfoResponse> talkServiceInfoResponses = listData(result, TalkServiceInfoResponse.class);
		for (TalkServiceInfoResponse response : talkServiceInfoResponses) {
			for (TalkScheduleInfoEntity scheduleInfo : response.getScheduleInfos()) {
				log.info("번호 ({}), 소속정보 ({}), 요일 ({}), 유형 ({}), 스케쥴 KEY ({}), 항목 ({})"
						, response.getKakaoServiceName()
						, scheduleInfo.getCompanyTrees().stream().map(OrganizationSummaryResponse::getGroupName).collect(Collectors.joining(","))
						, scheduleInfo.getWeek()
						, scheduleInfo.getScheduleGroup().getName()
						, scheduleInfo.getSeq()
						, scheduleInfo.getScheduleGroup().getScheduleGroupLists().size());
			}
		}
	}

	/**
	 * 상담톡주간스케쥴러 유형보기
	 */
//	@Test
	public void get_type() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/service/type/{parent}", 1)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						pathParameters(
								parameterWithName("parent").description("스케쥴유형 그룹 키")
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
						.and(fieldWithPath("data").type(JsonFieldType.OBJECT).description("스케쥴 항목 상세정보"))
						.andWithPrefix("data.", talkScheduleGroupEntity)
						.andWithPrefix("data.scheduleGroupLists[]", talkScheduleGroupLists)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();
	}

	/**
	 * 상담톡주간스케쥴러 유형 상세조회
	 */
//	@Test
	public void get() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{seq}", 143)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						pathParameters(
								parameterWithName("seq").description("스케쥴러 일정 키")
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
						.andWithPrefix("data.", talkScheduleInfoDetailResponse)
						.andWithPrefix("data.scheduleGroup.", talkScheduleGroupEntity)
						.andWithPrefix("data.scheduleGroup.scheduleGroupLists[]", talkScheduleGroupLists)
						.andWithPrefix("data.companyTrees[]", organizationSummaryResponse)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();
	}

	/**
	 * 상담톡주간스케쥴러 추가
	 */
//	@Test
	@Order(1)
	protected void post() throws Exception {
		final TalkScheduleInfoFormRequest form = new TalkScheduleInfoFormRequest();
		form.setGroupId(9);

		final Set<String> senderKeys = new HashSet<>();
		senderKeys.add("28ee2687812321c674b2453cbd62c121bea349d4");
		form.setSenderKeys(senderKeys);

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andDo(print())
				.andExpect(status().isCreated())
				.andDo(document.document(
						requestFields(talkScheduleInfoFormRequest),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").type(JsonFieldType.NULL).description("").optional(),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				))
				.andReturn();
	}

	/**
	 * 상담톡주간스케쥴러 삭제
	 */
//	@Test
	@Order(4)
	protected void delete() throws Exception {
		this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{senderKey}", "0770893f2e86ed5fe5cc9c71cc5aa53d38214970")
				.with(new JwtRequestPostProcessor()))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("result").value("success"))
				.andDo(document.document(
						pathParameters(
								parameterWithName("senderKey").description("상담톡 서비스")
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").type(JsonFieldType.NULL).description("'"),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				))
				.andReturn();
	}

	/**
	 * 상담톡스케쥴러 스케쥴일정 수정
	 */
//	@Test
	@Order(1)
	public void update_type() throws Exception {
		final TalkScheduleInfoFormUpdateRequest form = new TalkScheduleInfoFormUpdateRequest();
		form.setGroupId(9);

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/service/type/{seq}", 138)
				.contentType(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						pathParameters(
								parameterWithName("seq").description("SEQUENCE KEY")
						),
						requestFields(talkScheduleInfoFormUpdateRequest),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").type(JsonFieldType.NULL).description("").optional(),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				))
				.andReturn();
	}

	/**
	 * 상담톡스케쥴유형 일정삭제
	 */
//	@Test
	@Order(2)
	protected void delete_type() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/service/type/{seq}", 138)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						pathParameters(
								parameterWithName("seq").description("SEQUENCE KEY")
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").type(JsonFieldType.NULL).description("").optional(),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				))
				.andReturn();
	}

	/**
	 * 상담톡 주간스케쥴러 스케쥴유형 목록조회
	 */
//	@Test
	@Order(4)
	protected void scheduleInfos() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/schedule-info")
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
						.and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("스케쥴유형 목록").optional())
						.andWithPrefix("data.[]", summaryTalkScheduleInfoResponse)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();
	}

	/**
	 * 관련상담톡서비스 목록조회
	 */
//	@Test
	@Order(4)
	protected void talkServices() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/add-services")
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
								.and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("상담톡서비스 목록").optional())
								.andWithPrefix("data.[]", summaryTalkServiceResponse)
								.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();
	}
}
