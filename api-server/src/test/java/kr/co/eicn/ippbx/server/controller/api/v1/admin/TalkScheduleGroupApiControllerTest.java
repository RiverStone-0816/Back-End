package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.server.model.dto.eicn.MohDetailResponse;
import kr.co.eicn.ippbx.server.model.form.TalkMemberGroupFormRequest;
import kr.co.eicn.ippbx.server.model.form.TalkScheduleGroupFormRequest;
import kr.co.eicn.ippbx.server.model.form.TalkScheduleGroupListFormRequest;
import kr.co.eicn.ippbx.server.service.StorageService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.fileUpload;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
public class TalkScheduleGroupApiControllerTest extends BaseControllerTest {
	private final String TEST_URL = "/api/v1/admin/talk/schedule/type";
	private static String categoryCode = "COLOR_22";

	private FieldDescriptor[] talkScheduleGroupSummaryResponse = new FieldDescriptor[] {
			fieldWithPath("parent").type(JsonFieldType.NUMBER).description("고유키"),
			fieldWithPath("name").type(JsonFieldType.STRING).description("스케쥴유형명"),
			fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사 아이디"),
			fieldWithPath("scheduleGroupLists").type(JsonFieldType.ARRAY).description("스케쥴유형 목록").optional()
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

	private FieldDescriptor[] talkScheduleGroupFormRequest = new FieldDescriptor[] {
		fieldWithPath("name").type(JsonFieldType.STRING).description("스케쥴명")
	};

	private FieldDescriptor[] talkScheduleGroupListDetailResponse = new FieldDescriptor[] {
			fieldWithPath("category").type(JsonFieldType.STRING).description("음원아이디"),
			fieldWithPath("mohName").type(JsonFieldType.STRING).description("컬러링명"),
			fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사 아이디"),
			fieldWithPath("companySeq").type(JsonFieldType.NUMBER).description("고객사 SEQUENCE KEY"),
			fieldWithPath("directory").type(JsonFieldType.STRING).description("디렉토리경로"),
			fieldWithPath("fileName").type(JsonFieldType.STRING).description("파일명")
	};

	private FieldDescriptor[] talkScheduleGroupListFormRequest = new FieldDescriptor[] {
			fieldWithPath("parent").type(JsonFieldType.NUMBER).description("스케쥴유형키"),
			fieldWithPath("fromhour").type(JsonFieldType.NUMBER).description("시작시간 분 (09:00 -> 540)"),
			fieldWithPath("tohour").type(JsonFieldType.NUMBER).description("종료시간 분 (18:00 -> 1080)"),
			fieldWithPath("kind").type(JsonFieldType.STRING).description("유형구분"),
			fieldWithPath("kindData").type(JsonFieldType.STRING).description("유형별 입력 데이터"),
			fieldWithPath("firstMentId").type(JsonFieldType.NUMBER).description("첫인사멘트"),
			fieldWithPath("limitNum").type(JsonFieldType.NUMBER).description("비접수 n개이상 초과시 자동멘트송출(0개는 무한대)"),
			fieldWithPath("limitMentId").type(JsonFieldType.NUMBER).description("비접수초과 0이상시 필수")
	};

	private FieldDescriptor[] summaryTalkMentResponse = new FieldDescriptor[] {
			fieldWithPath("seq").type(JsonFieldType.NUMBER).description("SEQUENCE KEY"),
			fieldWithPath("mentName").type(JsonFieldType.STRING).description("멘트명")
	};

//	@Test
	public void list() throws Exception {
		this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
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
						.andWithPrefix("data.[]", talkScheduleGroupSummaryResponse)
						.and(fieldWithPath("data.[].scheduleGroupLists").type(JsonFieldType.ARRAY).description("스케쥴 항목 목록").optional())
						.andWithPrefix("data.[].scheduleGroupLists[]", talkScheduleGroupLists)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();
	}

//	@Test
	@Order(1)
	protected void post() throws Exception {
		final TalkScheduleGroupFormRequest form = new TalkScheduleGroupFormRequest();
		form.setName("상담톡유형3");

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andDo(print())
				.andExpect(status().isCreated())
				.andDo(document.document(
						requestFields(talkScheduleGroupFormRequest),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").type(JsonFieldType.NULL).description("").optional(),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				))
				.andReturn();
	}

//	@Test
	@Order(4)
	protected void delete() throws Exception {
		this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{parent}", 10)
				.with(new JwtRequestPostProcessor()))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("result").value("success"))
				.andDo(document.document(
						pathParameters(
								parameterWithName("parent").description("고유키")
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
	 *  스케쥴유형 항목 상세조회
	 */
//	@Test
	@Order(2)
	public void item_get() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/item/{child}", 18)
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
						.andWithPrefix("data.", talkScheduleGroupLists)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();
	}

//	@Test
	@Order(1)
	public void item_register() throws Exception {
		final TalkScheduleGroupListFormRequest form = new TalkScheduleGroupListFormRequest();
		form.setParent(11);
		form.setFromhour(890);
		form.setTohour(950);
		form.setKind("A"); // 유형구분 - A:자동멘트전송 , G:서비스별그룹연결
		form.setKindData("ssssssssssssssssssssssssss"); // 1

//		form.setFirstMentId(2);
//		form.setLimitNum(2);
//		form.setLimitMentId(2);

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL + "/item")
				.contentType(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andDo(print())
				.andExpect(status().isCreated())
				.andDo(document.document(
						requestFields(talkScheduleGroupListFormRequest),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").type(JsonFieldType.NULL).description("").optional(),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				))
				.andReturn();
	}

//	@Test
	@Order(2)
	protected void item_update() throws Exception {
		final TalkScheduleGroupListFormRequest form = new TalkScheduleGroupListFormRequest();
		form.setParent(11);
		form.setFromhour(900);
		form.setTohour(1000);
		form.setKind("A"); // 유형구분 - A:자동멘트전송 , G:서비스별그룹연결
		form.setKindData("2"); // 1

//		form.setFirstMentId(2);
//		form.setLimitNum(2);
//		form.setLimitMentId(2);

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/item/{child}", 18)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						pathParameters(
								parameterWithName("child").description("항목 키")
						),
						requestFields(talkScheduleGroupListFormRequest),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").type(JsonFieldType.NULL).description("").optional(),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				))
				.andReturn();
	}

//	@Test
	@Order(4)
	protected void item_delete() throws Exception {
		this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/item/{child}", 18)
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

//	@Test
	protected void getTalkMent() throws Exception {
		this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/talk-ment")
				.with(new JwtRequestPostProcessor()))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(MockMvcResultMatchers.jsonPath("result").value("success"))
				.andDo(document.document(
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").type(JsonFieldType.ARRAY).description("상담톡 멘트 목록"),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
						.andWithPrefix("data.[]", summaryTalkMentResponse)
				))
				.andReturn();
	}
}
