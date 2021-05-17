package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.server.model.dto.eicn.Number070ScheduleInfoResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.OrganizationSummaryResponse;
import kr.co.eicn.ippbx.server.model.entity.eicn.ScheduleInfoEntity;
import kr.co.eicn.ippbx.server.model.form.DayScheduleInfoFormRequest;
import kr.co.eicn.ippbx.server.model.form.DayScheduleInfoUpdateFormRequest;
import kr.co.eicn.ippbx.server.model.form.HolyScheduleInfoFormRequest;
import kr.co.eicn.ippbx.server.model.form.PeriodDateFormRequest;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Order;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
public class InboundDayScheduleInfoApiControllerTest extends BaseControllerTest {
	private final String TEST_URL = "/api/v1/admin/sounds/schedule/inbound/day";

	private FieldDescriptor[] number070ScheduleInfoResponse = new FieldDescriptor[] {
			fieldWithPath("number").type(JsonFieldType.STRING).description("070번호"),
			fieldWithPath("type").type(JsonFieldType.NUMBER).description("번호타입(0:헌트, 1:개인, 2:대표, 3:회의)"),
			fieldWithPath("svcName").type(JsonFieldType.STRING).description("대표번호 한글명").optional(),
			fieldWithPath("svcCid").type(JsonFieldType.STRING).description("대표번호 CID번호").optional(),
			fieldWithPath("scheduleInfos").type(JsonFieldType.ARRAY).description("스케쥴러 일정 목록")
	};

	private FieldDescriptor[] number070ScheduleInfoDetailResponse = new FieldDescriptor[] {
			fieldWithPath("number").type(JsonFieldType.STRING).description("070번호"),
//			fieldWithPath("type").type(JsonFieldType.NUMBER).description("번호타입(0:헌트, 1:개인, 2:대표, 3:회의)"),
			fieldWithPath("scheduleInfo").type(JsonFieldType.OBJECT).description("스케쥴러 일정 정보").optional()
	};

	private FieldDescriptor[] scheduleInfoEntity = new FieldDescriptor[] {
			fieldWithPath("seq").type(JsonFieldType.NUMBER).description("SEQUENCE KEY"),
			fieldWithPath("type").type(JsonFieldType.STRING).description("스케쥴러 구분(W:주간, H:일별)"),
			fieldWithPath("number").type(JsonFieldType.STRING).description("070번호"),
			fieldWithPath("week").type(JsonFieldType.STRING).description("요일(1Mon:월요일, 2Tue:화요일, 3Wed:수용일, 4Thu:목요일, 5Fri:금요일, 6Sat:토요일, 7Sun:일요일)"),
			fieldWithPath("fromdate").type(JsonFieldType.STRING).description("시작일"),
			fieldWithPath("todate").type(JsonFieldType.STRING).description("종료일"),
			fieldWithPath("groupCode").type(JsonFieldType.STRING).description("같은 group_level 에서 unique한 코드 4자리 ex>0001").optional(),
			fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("윗레벨의 코드를 포함한 코드의 나열 ex>0003_0008_0001").optional(),
			fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("해당조직의 레벨 MAX 보다 같거나 작을것").optional(),
			fieldWithPath("groupId").type(JsonFieldType.NUMBER).description("유형아이디"),
			fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사 아이디"),
			fieldWithPath("isStat").type(JsonFieldType.STRING).description("통계 사용여부"),
			fieldWithPath("scheduleGroup").type(JsonFieldType.OBJECT).description("스케쥴 유형정보").optional(),
			fieldWithPath("companyTrees").type(JsonFieldType.ARRAY).description("조직정보 목록").optional()
	};

	private FieldDescriptor[] scheduleGroupEntity = new FieldDescriptor[] {
			fieldWithPath("parent").type(JsonFieldType.NUMBER).description("고유키"),
			fieldWithPath("name").type(JsonFieldType.STRING).description("스케쥴유형명"),
			fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사 아이디"),
			fieldWithPath("scheduleGroupLists").type(JsonFieldType.ARRAY).description("스케쥴유형 목록").optional()
	};

	private FieldDescriptor[] organizationSummaryResponse = new FieldDescriptor[] {
			fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드").optional(),
			fieldWithPath("groupName").type(JsonFieldType.STRING).description("조직명").optional(),
			fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("윗레벨의 코드를 포함한 코드의 나열(002_003_008)").optional(),
			fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("조직레벨").optional()
	};

	private FieldDescriptor[] scheduleGroupLists = new FieldDescriptor[] {
			fieldWithPath("child").type(JsonFieldType.NUMBER).description("고유키").optional(),
			fieldWithPath("parent").type(JsonFieldType.NUMBER).description("스케쥴유형키"),
			fieldWithPath("childName").type(JsonFieldType.STRING).description("항목명").optional(),
			fieldWithPath("fromhour").type(JsonFieldType.NUMBER).description("시작시간 분 (09:00 -> 540)"),
			fieldWithPath("tohour").type(JsonFieldType.NUMBER).description("종료시간 분 (18:00 -> 1080)"),
			fieldWithPath("kind").type(JsonFieldType.STRING).description("유형구분"),
			fieldWithPath("kindData").type(JsonFieldType.STRING).description("유형별 입력 데이터"),
			fieldWithPath("kindDataName").type(JsonFieldType.STRING).description("수행 데이터").optional(),
			fieldWithPath("kindSoundCode").type(JsonFieldType.STRING).description("음원 참조키").optional(),
			fieldWithPath("kindSoundName").type(JsonFieldType.STRING).description("음원명").optional(),
			fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사 아이디")
	};

	private FieldDescriptor[] dayScheduleInfoFormRequest = new FieldDescriptor[] {
			fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드").optional(),
			fieldWithPath("numbers").type(JsonFieldType.ARRAY).description("서비스키"),
			fieldWithPath("groupId").type(JsonFieldType.NUMBER).description("스케쥴유형"),
			fieldWithPath("isStat").type(JsonFieldType.STRING).description("통계적용여부(Y:적용, N:비적용)"),
			fieldWithPath("fromDate").type(JsonFieldType.STRING).description("시작일(YYYY-MM-DD)"),
			fieldWithPath("toDate").type(JsonFieldType.STRING).description("종료일(YYYY-MM-DD)")
	};

	private FieldDescriptor[] holyScheduleInfoFormRequest = new FieldDescriptor[] {
			fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드").optional(),
			fieldWithPath("numbers").type(JsonFieldType.ARRAY).description("서비스키"),
			fieldWithPath("groupId").type(JsonFieldType.NUMBER).description("스케쥴유형"),
			fieldWithPath("isStat").type(JsonFieldType.STRING).description("통계적용여부(Y:적용, N:비적용)"),
			fieldWithPath("periodDates").type(JsonFieldType.ARRAY).description("기간 검색 요청 데이터 목록")
	};

	private FieldDescriptor[] scheduleInfoFormUpdateRequest = new FieldDescriptor[] {
			fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드").optional(),
			fieldWithPath("groupId").type(JsonFieldType.NUMBER).description("스케쥴유형"),
			fieldWithPath("isStat").type(JsonFieldType.STRING).description("통계적용여부(Y:적용, N:비적용)"),
			fieldWithPath("fromDate").type(JsonFieldType.STRING).description("시작일(YYYY-MM-DD)")
	};

	private FieldDescriptor[] summaryScheduleGroupResponse = new FieldDescriptor[] {
			fieldWithPath("parent").type(JsonFieldType.NUMBER).description("스케쥴유형 키"),
			fieldWithPath("name").type(JsonFieldType.STRING).description("스케쥴유형명").optional(),
	};

	private FieldDescriptor[] summaryNumber070Response = new FieldDescriptor[] {
			fieldWithPath("number").type(JsonFieldType.STRING).description("070번호"),
			fieldWithPath("type").type(JsonFieldType.NUMBER).description("번호타입(0:헌트, 1:개인, 2:대표, 3:회의)"),
			fieldWithPath("hanName").type(JsonFieldType.STRING).description("QUEUE 한글이름").optional()
	};

	/**
	 *  일별스케쥴러 목록조회
	 */
//	@Test
	public void list() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.param("number", "")  // 070번호
				.param("groupId", "") // 유형
				.param("searchDate", "") // 요일, 날짜
				.param("groupCode", "")) // 조직코드
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						requestParameters(
								parameterWithName("number").description("070넘버"),
								parameterWithName("groupId").description("유형선택"),
								parameterWithName("searchDate").description("요일(YYYY-MM-DD)"),
								parameterWithName("groupCode").description("조직코드")
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").description("실제 반환될 데이터").optional()
						)
						.andWithPrefix("data.[]", number070ScheduleInfoResponse)
						.and(fieldWithPath("data.[].scheduleInfos[]").type(JsonFieldType.ARRAY).description("스케쥴 항목 목록").optional())
						.andWithPrefix("data.[].scheduleInfos[]", scheduleInfoEntity)
						.andWithPrefix("data.[].scheduleInfos[].scheduleGroup.", scheduleGroupEntity)
						.andWithPrefix("data.[].scheduleInfos[].companyTrees[]", organizationSummaryResponse)
						.andWithPrefix("data.[].scheduleInfos[].scheduleGroup.scheduleGroupLists[]", scheduleGroupLists)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();

		final List<Number070ScheduleInfoResponse> number070ScheduleInfoResponses = listData(result, Number070ScheduleInfoResponse.class);
		for (Number070ScheduleInfoResponse response : number070ScheduleInfoResponses) {
			for (ScheduleInfoEntity scheduleInfo : response.getScheduleInfos()) {
				log.info("번호 ({}), 소속정보 ({}), 통계적용여부 ({}), 날짜 ({}), 유형 ({})"
					, response.getNumber()
					, Objects.nonNull(scheduleInfo.getCompanyTrees()) ? scheduleInfo.getCompanyTrees().stream().map(OrganizationSummaryResponse::getGroupName).collect(Collectors.joining(","))
								: null
					, scheduleInfo.getIsStat()
					, scheduleInfo.getFromdate()
					, scheduleInfo.getScheduleGroup().getName());
			}
		}
	}

	/**
	 * 일별스케쥴러 유형보기
	 */
//	@Test
	public void get_type() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/service/type/{parent}", 140)
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
						.andWithPrefix("data.", scheduleGroupEntity)
						.andWithPrefix("data.scheduleGroupLists[]", scheduleGroupLists)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();
	}

	/**
	 * 일별스케쥴러 일정 상세조회
	 */
//	@Test
	public void get() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{seq}", 667)
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
						.andWithPrefix("data.", number070ScheduleInfoDetailResponse)
						.andWithPrefix("data.scheduleInfo.", scheduleInfoEntity)
						.andWithPrefix("data.scheduleInfo.scheduleGroup.", scheduleGroupEntity)
						.andWithPrefix("data.scheduleInfo.scheduleGroup.scheduleGroupLists[]", scheduleGroupLists)
						.andWithPrefix("data.scheduleInfo.companyTrees[]", organizationSummaryResponse)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();
	}

	/**
	 * 일별스케쥴러 추가
	 */
//	@Test
	protected void post() throws Exception {
		final DayScheduleInfoFormRequest form = new DayScheduleInfoFormRequest();
		form.setGroupId(137);

		final Set<String> numbers = new HashSet<>();
		numbers.add("07075490677");

		form.setNumbers(numbers);

		form.setFromDate("2020-03-01");
		form.setToDate("2020-03-03");

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andDo(print())
				.andExpect(status().isCreated())
				.andDo(document.document(
						requestFields(dayScheduleInfoFormRequest),
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
	 * 일별스케쥴러 삭제
	 */
//	@Test
	protected void delete() throws Exception {
		this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{number}", "07075490677")
				.with(new JwtRequestPostProcessor()))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("result").value("success"))
				.andDo(document.document(
						pathParameters(
								parameterWithName("number").description("070넘버")
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
	 * 스케쥴일정 수정
	 */
//	@Test
	public void update_schedule() throws Exception {
		final DayScheduleInfoUpdateFormRequest form = new DayScheduleInfoUpdateFormRequest();
		form.setGroupId(140);
		form.setFromDate("2020-03-01");

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/service/type/{seq}", 668)
				.contentType(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						pathParameters(
								parameterWithName("seq").description("SEQUENCE KEY")
						),
						requestFields(scheduleInfoFormUpdateRequest),
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
	 * 공휴일 일괄등록
	 */
//	@Test
	protected void holyPost() throws Exception {
		final HolyScheduleInfoFormRequest form = new HolyScheduleInfoFormRequest();

		form.setGroupId(137);

		final Set<String> numbers = new HashSet<>();
		numbers.add("07075490677");

		form.setNumbers(numbers);

		final List<PeriodDateFormRequest> periodDates = new ArrayList<>();

		final PeriodDateFormRequest periodDate = new PeriodDateFormRequest();
		periodDate.setFromDate("2020-01-01");
		periodDate.setToDate("2020-01-01");

		periodDates.add(periodDate);

		form.setPeriodDates(periodDates);

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL + "/holy")
				.contentType(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andDo(print())
				.andExpect(status().isCreated())
				.andDo(document.document(
						requestFields(holyScheduleInfoFormRequest)
						.andWithPrefix("periodDates[]",
							fieldWithPath("fromDate").type(JsonFieldType.STRING).description("시작일"),
							fieldWithPath("toDate").type(JsonFieldType.STRING).description("종료일")
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
	 * 일별스케쥴러 스케쥴유형 목록조회
	 */
//	@Test
	@Order(4)
	protected void scheduleGroups() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/schedule-group")
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
						.andWithPrefix("data.[]", summaryScheduleGroupResponse)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();
	}

	/**
	 * 번호 목록 조회
	 */
//	@Test
	protected void add_number_list() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/add-number-list")
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
						.and(fieldWithPath("data").type(JsonFieldType.ARRAY).description("번호 목록 조회").optional())
						.andWithPrefix("data.[]", summaryNumber070Response)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();
	}
}
