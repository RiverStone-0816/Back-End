package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.server.model.dto.eicn.SummaryPhoneInfoResponse;
import kr.co.eicn.ippbx.server.model.entity.eicn.OutScheduleListEntity;
import kr.co.eicn.ippbx.server.model.entity.eicn.OutScheduleSeedEntity;
import kr.co.eicn.ippbx.server.model.form.OutScheduleSeedFormRequest;
import kr.co.eicn.ippbx.server.model.form.ScheduleInfoFormRequest;
import kr.co.eicn.ippbx.server.model.form.ScheduleInfoUpdateFormRequest;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
public class OutboundWeekScheduleSeedApiControllerTest extends BaseControllerTest {
	private final String TEST_URL = "/api/v1/admin/sounds/schedule/outbound/week";

	private FieldDescriptor[] outScheduleSeedEntity = new FieldDescriptor[] {
			fieldWithPath("parent").type(JsonFieldType.NUMBER).description("발신 스케쥴러 유니크 값"),
			fieldWithPath("name").type(JsonFieldType.STRING).description("발신 스케쥴러 명"),
			fieldWithPath("extension").type(JsonFieldType.STRING).description("내선"),
			fieldWithPath("type").type(JsonFieldType.STRING).description("스케쥴러 구분(H:일별, W:주간)"),
			fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사 아이디"),
			fieldWithPath("outScheduleLists").type(JsonFieldType.ARRAY).description("발신 스케쥴 일정 목록").optional()
	};

	private FieldDescriptor[] outScheduleListEntity = new FieldDescriptor[] {
			fieldWithPath("code").type(JsonFieldType.NUMBER).description("발신 스케쥴러의 유니크 값"),
			fieldWithPath("type").type(JsonFieldType.STRING).description("스케쥴러 구분(H:일별, W:주간)"),
			fieldWithPath("name").type(JsonFieldType.STRING).description("일정명"),
			fieldWithPath("extension").type(JsonFieldType.STRING).description("내선"),
			fieldWithPath("fromtime").type(JsonFieldType.STRING).description("발신 스케쥴러 시작일(일별에서 사용)"),
			fieldWithPath("totime").type(JsonFieldType.STRING).description("발신 스케쥴러 종료일(일별에서 사용)"),
			fieldWithPath("week").type(JsonFieldType.STRING).description("주간 스케쥴러일 경우 요일 값"),
			fieldWithPath("fromhour").type(JsonFieldType.NUMBER).description("스케쥴러 실행 시작시간"),
			fieldWithPath("tohour").type(JsonFieldType.NUMBER).description("스케쥴러 실행 종료시간"),
			fieldWithPath("soundcode").type(JsonFieldType.STRING).description("음원 참조 키"),
			fieldWithPath("parent").type(JsonFieldType.NUMBER).description("스케쥴러 참조 키"),
			fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사 아이디"),
			fieldWithPath("soundName").type(JsonFieldType.STRING).description("음원명")
	};

	private FieldDescriptor[] outScheduleSeedDetailResponse = new FieldDescriptor[] {
			fieldWithPath("parent").type(JsonFieldType.NUMBER).description("스케쥴러 참조 키"),
			fieldWithPath("name").type(JsonFieldType.STRING).description("일정명"),
			fieldWithPath("extensions").type(JsonFieldType.ARRAY).description("추가된번호"),
			fieldWithPath("weeks").type(JsonFieldType.ARRAY).description("요일 목록"),
			fieldWithPath("fromhour").type(JsonFieldType.NUMBER).description("시작시간 분 (09:00 -> 540)"),
			fieldWithPath("tohour").type(JsonFieldType.NUMBER).description("종료시간 분 (18:00 -> 1080)"),
			fieldWithPath("fromtime").type(JsonFieldType.STRING).description("발신 스케쥴러 시작일(일별에서 사용)"),
			fieldWithPath("totime").type(JsonFieldType.STRING).description("발신 스케쥴러 종료일(일별에서 사용)"),
			fieldWithPath("soundCode").type(JsonFieldType.STRING).description("음원 참조 키"),
			fieldWithPath("soundName").type(JsonFieldType.STRING).description("음원명")
	};

	private FieldDescriptor[] outScheduleSeedFormRequest = new FieldDescriptor[] {
			fieldWithPath("name").type(JsonFieldType.STRING).description("일정명"),
			fieldWithPath("extensions").type(JsonFieldType.ARRAY).description("추가 내선번호"),
			fieldWithPath("weeks").type(JsonFieldType.ARRAY).description("요일"),
			fieldWithPath("fromhour").type(JsonFieldType.NUMBER).description("시작시간 분 (09:00 -> 540)"),
			fieldWithPath("tohour").type(JsonFieldType.NUMBER).description("종료시간 분 (18:00 -> 1080)"),
			fieldWithPath("soundCode").type(JsonFieldType.STRING).description("음원 참조키").optional()
	};

	private FieldDescriptor[] summarySoundListResponse = new FieldDescriptor[] {
			fieldWithPath("seq").type(JsonFieldType.NUMBER).description("음원 SEQUENCE KEY"),
			fieldWithPath("soundName").type(JsonFieldType.STRING).description("음원명"),
	};

	private FieldDescriptor[] summaryPhoneInfoResponse = new FieldDescriptor[] {
			fieldWithPath("extension").type(JsonFieldType.STRING).description("내선전화번호"),
			fieldWithPath("inUseIdName").type(JsonFieldType.STRING).description("사용중인 상담원명").optional()
	};

	/**
	 *  주간스케쥴러 목록조회
	 */
//	@Test
	public void list() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.param("name", ""))      // 발신 스케쥴러명
//				.param("week", "")) // 요일(Mon:월요일, Tue:화요일, Wed:수용일, Thu:목요일, Fri:금요일, Sat:토요일, Sun:일요일)
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						requestParameters(
								parameterWithName("name").description("발신 스케쥴러명")
//								parameterWithName("week").description("요일(Mon:월요일, Tue:화요일, Wed:수용일, Thu:목요일, Fri:금요일, Sat:토요일, Sun:일요일)").ignored()
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").description("실제 반환될 데이터").optional()
						)
						.andWithPrefix("data.[]", outScheduleSeedEntity)
						.and(fieldWithPath("data.[].outScheduleLists[]").type(JsonFieldType.ARRAY).description("발신 스케쥴 일정 목록").optional())
						.andWithPrefix("data.[].outScheduleLists[]", outScheduleListEntity)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();

		final List<OutScheduleSeedEntity> outScheduleSeedEntities = listData(result, OutScheduleSeedEntity.class);
		for (OutScheduleSeedEntity response : outScheduleSeedEntities) {
			log.info("-> 일정명 ({})", response.getName());
			for (OutScheduleListEntity outScheduleList : response.getOutScheduleLists()) {
				log.info("--> 번호({}), 요일({}), 시간({} ~ {}), 음원명({})"
					, outScheduleList.getExtension()
					, outScheduleList.getWeek()
					, outScheduleList.getFromhour() / 60 + ":" + outScheduleList.getFromhour() % 60, outScheduleList.getTohour() / 60 + ":" + outScheduleList.getTohour() % 60
					, outScheduleList.getSoundName());
			}
		}
	}

	/**
	 * 스케쥴러 상세조회
	 */
//	@Test
	public void get() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{parent}", 14)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						pathParameters(
								parameterWithName("parent").description("발신 스케쥴러 유니크 값")
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
						.andWithPrefix("data.", outScheduleSeedDetailResponse)
						.andWithPrefix("data.extensions[]", summaryPhoneInfoResponse)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();
	}

	/**
	 * 스케쥴러 추가
	 */
//	@Test
	@Order(1)
	protected void post() throws Exception {
		final OutScheduleSeedFormRequest form = new OutScheduleSeedFormRequest();
		form.setName("발신 스케쥴러1");
		final Set<String> extensions = new HashSet<>();

		extensions.add("0789");

		form.setExtensions(extensions);

		final Set<String> weeks = new HashSet<>();
		weeks.add("Moh");
		weeks.add("Tue");

		form.setWeeks(weeks);

		form.setFromhour(1039);
		form.setTohour(1098);

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andDo(print())
				.andExpect(status().isCreated())
				.andDo(document.document(
						requestFields(outScheduleSeedFormRequest),
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
	 * 스케쥴러 삭제
	 */
//	@Test
	@Order(4)
	protected void delete() throws Exception {
		this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{parent}", 27)
				.with(new JwtRequestPostProcessor()))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("result").value("success"))
				.andDo(document.document(
						pathParameters(
								parameterWithName("parent").description("발신 스케쥴러 유니크 값")
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
	 * 스케쥴 수정
	 */
//	@Test
	@Order(1)
	public void update() throws Exception {
		final OutScheduleSeedFormRequest form = new OutScheduleSeedFormRequest();
		form.setName("발신 스케쥴러 테스트 수정");
		final Set<String> extensions = new HashSet<>();

		extensions.add("0677");

		form.setExtensions(extensions);

		final Set<String> weeks = new HashSet<>();
		weeks.add("Sun");

		form.setWeeks(weeks);

		form.setFromhour(890);
		form.setTohour(1120);

		form.setSoundCode("11");

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/{parent}", 14)
				.contentType(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						pathParameters(
								parameterWithName("parent").description("발신 스케쥴러 유니크 값")
						),
						requestFields(outScheduleSeedFormRequest),
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
	 * 음원 목록 조회
	 */
//	@Test
	protected void add_sounds() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/add-sounds")
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
								.and(fieldWithPath("data").type(JsonFieldType.ARRAY).description("음원 목록").optional())
								.andWithPrefix("data.[]", summarySoundListResponse)
								.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();

	}

	/**
	 * 내선번호 목록조회
	 */
//	@Test
	protected void extension() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/add-extensions")
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
						.and(fieldWithPath("data").type(JsonFieldType.ARRAY).description("내선번호 목록").optional())
						.andWithPrefix("data.[]", summaryPhoneInfoResponse)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();

		final List<SummaryPhoneInfoResponse> summaryPhoneInfoResponses = listData(result, SummaryPhoneInfoResponse.class);
		for (SummaryPhoneInfoResponse summaryPhoneInfoRespons : summaryPhoneInfoResponses) {
			log.info(summaryPhoneInfoRespons.toString());
		}
	}
}
