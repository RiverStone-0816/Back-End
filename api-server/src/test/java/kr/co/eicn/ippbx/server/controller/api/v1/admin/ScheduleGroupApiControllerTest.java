package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.model.dto.eicn.ScheduleGroupSummaryResponse;
import kr.co.eicn.ippbx.model.entity.eicn.ScheduleGroupListEntity;
import kr.co.eicn.ippbx.model.enums.ScheduleKind;
import kr.co.eicn.ippbx.model.form.ScheduleGroupFormRequest;
import kr.co.eicn.ippbx.model.form.ScheduleGroupListFormRequest;
import kr.co.eicn.ippbx.util.EnumUtils;
import kr.co.eicn.ippbx.util.spring.RequestMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class ScheduleGroupApiControllerTest extends BaseControllerTest {
	@Autowired
	private RequestMessage message;
	private final String TEST_URL = "/api/v1/admin/sounds/schedule";

	private FieldDescriptor[] scheduleGroupSummaryResponse = new FieldDescriptor[] {
			fieldWithPath("parent").type(JsonFieldType.NUMBER).description("고유키"),
			fieldWithPath("name").type(JsonFieldType.STRING).description("스케쥴유형명"),
			fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사 아이디"),
			fieldWithPath("scheduleGroupLists").type(JsonFieldType.ARRAY).description("스케쥴유형 목록").optional()
	};

	private FieldDescriptor[] scheduleGroupLists = new FieldDescriptor[] {
			fieldWithPath("child").type(JsonFieldType.NUMBER).description("고유키").optional(),
			fieldWithPath("parent").type(JsonFieldType.NUMBER).description("스케쥴유형키"),
			fieldWithPath("childName").type(JsonFieldType.STRING).description("항목명").optional(),
			fieldWithPath("fromhour").type(JsonFieldType.NUMBER).description("시작시간 분 (09:00 -> 540)"),
			fieldWithPath("tohour").type(JsonFieldType.NUMBER).description("종료시간 분 (18:00 -> 1080)"),
			fieldWithPath("kind").type(JsonFieldType.STRING).description("유형구분"),
			fieldWithPath("kindData").type(JsonFieldType.STRING).description("유형별 입력 데이터").optional(),
			fieldWithPath("kindSoundCode").type(JsonFieldType.STRING).description("음원 참조 키").optional(),
			fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사 아이디"),
			fieldWithPath("isStat").type(JsonFieldType.STRING).description("업무시간 통계반영여부"),
			fieldWithPath("kindSoundName").type(JsonFieldType.STRING).description("음원명").optional(),
			fieldWithPath("kindDataName").type(JsonFieldType.STRING).description("유형별 입력 데이터명").optional()
	};

	private FieldDescriptor[] scheduleGroupFormRequest = new FieldDescriptor[] {
		fieldWithPath("name").type(JsonFieldType.STRING).description("스케쥴 유형명")
	};

	private FieldDescriptor[] scheduleGroupListFormRequest = new FieldDescriptor[] {
			fieldWithPath("parent").type(JsonFieldType.NUMBER).description("스케쥴유형키"),
			fieldWithPath("fromhour").type(JsonFieldType.NUMBER).description("시작시간 분 (09:00 -> 540)"),
			fieldWithPath("tohour").type(JsonFieldType.NUMBER).description("종료시간 분 (18:00 -> 1080)"),
			fieldWithPath("kind").type(JsonFieldType.STRING).description("유형구분(S:음원만플레이, D:번호직접연결(내부번호연결), F:착신전환(외부번호연결), I:IVR연결, C:예외컨텍스트, V:음성사서함)"),
			fieldWithPath("kindData").type(JsonFieldType.STRING).description("유형별 입력 데이터(번호직접연결, 착신전환, IVR연결, 예외컨텍스트시 선택시 필수 입력사항) ").optional(),
			fieldWithPath("kindSoundCode").type(JsonFieldType.STRING).description("음원 키(음원만플레이, 음성사서함 선택시 필수 입력사항)").optional(),
			fieldWithPath("isStat").type(JsonFieldType.STRING).description("업무시간 통계반영여부(Y:N)").optional()
	};

	private FieldDescriptor[] summarySoundListResponse = new FieldDescriptor[] {
			fieldWithPath("seq").type(JsonFieldType.NUMBER).description("음원 SEQUENCE KEY"),
			fieldWithPath("soundName").type(JsonFieldType.STRING).description("음원명"),
	};

	private FieldDescriptor[] summaryNumber070Response = new FieldDescriptor[] {
			fieldWithPath("number").type(JsonFieldType.STRING).description("070번호"),
			fieldWithPath("type").type(JsonFieldType.NUMBER).description("번호타입(0:헌트, 1:개인, 2:대표, 3:회의)"),
			fieldWithPath("hanName").type(JsonFieldType.STRING).description("QUEUE 한글이름").optional()
	};

	private FieldDescriptor[] summaryIvrTreeResponse = new FieldDescriptor[] {
			fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드 키"),
			fieldWithPath("name").type(JsonFieldType.STRING).description("IVR정보명")
	};

	private FieldDescriptor[] summaryContextInfoResponse = new FieldDescriptor[] {
			fieldWithPath("context").type(JsonFieldType.STRING).description("context"),
			fieldWithPath("name").type(JsonFieldType.STRING).description("context명")
	};

//	@Test
	public void list() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").description("실제 반환될 데이터").optional()
						)
						.andWithPrefix("data.[]", scheduleGroupSummaryResponse)
						.and(fieldWithPath("data.[].scheduleGroupLists").type(JsonFieldType.ARRAY).description("스케쥴 항목 목록").optional())
						.andWithPrefix("data.[].scheduleGroupLists[]", scheduleGroupLists)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();

		final List<ScheduleGroupSummaryResponse> responses = listData(result, ScheduleGroupSummaryResponse.class);
		for (ScheduleGroupSummaryResponse respons : responses) {
			for (ScheduleGroupListEntity e : respons.getScheduleGroupLists()) {
				log.info("유형명 : ({}), 시간 : ({} ~ {}), 수행유형 : ({}), 수행데이터 : ({}), 음원 ({})"
					, respons.getName()
					, e.getFromhour(), e.getTohour()
					, message.getEnumText(EnumUtils.of(ScheduleKind.class, e.getKind()))
					, e.getKindDataName()
					, e.getKindSoundName());
			}
		}
	}

	/**
	 *  스케쥴유형 추가
	 */
//	@Test
	@Order(1)
	protected void post() throws Exception {
		final ScheduleGroupFormRequest form = new ScheduleGroupFormRequest();
		form.setName("SANGUCK"); // 유형명

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andDo(print())
				.andExpect(status().isCreated())
				.andDo(document.document(
						requestFields(scheduleGroupFormRequest),
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
	 * 스케쥴유형 삭제
	 */
//	@Test
//	protected void delete() throws Exception {
//		this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{parent}", 13)
//				.with(new JwtRequestPostProcessor()))
//				.andExpect(status().isOk())
//				.andExpect(MockMvcResultMatchers.jsonPath("result").value("success"))
//				.andDo(document.document(
//						pathParameters(
//								parameterWithName("parent").description("고유키")
//						),
//						responseFields(
//								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
//								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
//								fieldWithPath("data").type(JsonFieldType.NULL).description("'"),
//								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
//						)
//				))
//				.andReturn();
//	}

	/**
	 *  스케쥴유형 항목 상세조회
	 */
//	@Test
	public void item_get() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/item/{child}", 199)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						pathParameters(
								parameterWithName("child").description("항목 키")
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
						.and(fieldWithPath("data").type(JsonFieldType.OBJECT).description("스케쥴 항목 상세정보"))
						.andWithPrefix("data.", scheduleGroupLists)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();
	}

	/**
	 * 스케쥴유형 항목추가
	 */
//	@Test
	public void register_item() throws Exception {
		final ScheduleGroupListFormRequest form = new ScheduleGroupListFormRequest();
		form.setParent(140);
		form.setFromhour(0);
		form.setTohour(1439);
		form.setKind(ScheduleKind.CALL_FORWARDING.getCode()); // S:음원만플레이, D:번호직접여결(내부번호연결), F:착신전환(외부번호연결), I:IVR연결, C:예외컨텍스트, V:음성사서함
		form.setKindData("01030068249");
//		form.setKindSoundData("");

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL + "/item")
				.contentType(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andDo(print())
				.andExpect(status().isCreated())
				.andDo(document.document(
						requestFields(scheduleGroupListFormRequest),
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
	 * 스케쥴유형 항목수정
	 */
//	@Test
	public void update_item() throws Exception {
		final ScheduleGroupListFormRequest form = new ScheduleGroupListFormRequest();
		form.setParent(140);
		form.setFromhour(120);
		form.setTohour(1439);
		form.setKind(ScheduleKind.ONY_SOUND_PLAY.getCode()); // S:음원만플레이, D:번호직접여결(내부번호연결), F:착신전환(외부번호연결), I:IVR연결, C:예외컨텍스트, V:음성사서함
//		form.setKindData("01030068249");
		form.setKindSoundCode("12");

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/item/{child}", 199)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						pathParameters(
								parameterWithName("child").description("항목 키")
						),
						requestFields(scheduleGroupListFormRequest),
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
	 * 스케쥴유형 항목삭제
	 */
//	@Test
	@Order(4)
	public void delete_item() throws Exception {
		this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/item/{child}", 179)
				.with(new JwtRequestPostProcessor()))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("result").value("success"))
				.andDo(document.document(
						pathParameters(
								parameterWithName("child").description("항목 키")
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
	 * 스케쥴유형 항목 복사
	 */
//	@Test
	@Order(4)
	public void itemCopy() throws Exception {
		this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL + "/{parent}/{targetParent}/copy", 140, 137)
				.with(new JwtRequestPostProcessor()))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("result").value("success"))
				.andDo(document.document(
						pathParameters(
								parameterWithName("parent").description("원본 유형키"),
								parameterWithName("targetParent").description("대상 유형키")
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
	 * 음원 목록 조회
	 */
//	@Test
	protected void add_sounds_list() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/add-sounds-list")
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
						.and(fieldWithPath("data").type(JsonFieldType.ARRAY).description("음원 목록 조회").optional())
						.andWithPrefix("data.[]", summarySoundListResponse)
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

	/**
	 * Ivr 목록 조회
	 */
//	@Test
	protected void add_ivr_list() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/add-ivr-list")
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
						.and(fieldWithPath("data").type(JsonFieldType.ARRAY).description("Ivr 목록 조회").optional())
						.andWithPrefix("data.[]", summaryIvrTreeResponse)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();
	}

	/**
	 * context 목록 조회
	 */
//	@Test
	protected void add_context_list() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/add-context-list")
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
						.and(fieldWithPath("data").type(JsonFieldType.ARRAY).description("context 목록 조회").optional())
						.andWithPrefix("data.[]", summaryContextInfoResponse)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();
	}
}
