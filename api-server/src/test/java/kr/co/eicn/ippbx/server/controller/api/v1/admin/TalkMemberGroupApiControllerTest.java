package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryTalkGroupPersonResponse;
import kr.co.eicn.ippbx.model.dto.eicn.TalkMemberGroupDetailResponse;
import kr.co.eicn.ippbx.model.form.TalkMemberGroupFormRequest;
import lombok.extern.slf4j.Slf4j;
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

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class TalkMemberGroupApiControllerTest extends BaseControllerTest {
	private final String TEST_URL = "/api/v1/admin/talk/group/reception-group";

	private FieldDescriptor[] talkMemberGroupSummaryResponse = new FieldDescriptor[]{
			fieldWithPath("groupId").type(JsonFieldType.NUMBER).description("상담톡 그룹 아이디"),
			fieldWithPath("groupName").type(JsonFieldType.STRING).description("상담톡 그룹명"),
			fieldWithPath("kakaoServiceName").type(JsonFieldType.STRING).description("상담톡 서비스명"),
			fieldWithPath("memberCnt").type(JsonFieldType.NUMBER).description("멤버수"),
			fieldWithPath("persons").type(JsonFieldType.ARRAY).description("그룹멤버"),
	};

	private FieldDescriptor[] talkMemberGroupDetailResponse = new FieldDescriptor[]{
			fieldWithPath("groupId").type(JsonFieldType.NUMBER).description("상담톡 그룹 아이디"),
			fieldWithPath("groupName").type(JsonFieldType.STRING).description("상담톡 그룹명"),
			fieldWithPath("kakaoServiceName").type(JsonFieldType.STRING).description("상담톡 서비스명"),
			fieldWithPath("senderKey").type(JsonFieldType.STRING).description("관련상담톡서비스"),
			fieldWithPath("persons").type(JsonFieldType.ARRAY).description("그룹멤버").optional(),
	};

	private FieldDescriptor[] talkMemberGroupFormRequest = new FieldDescriptor[]{
			fieldWithPath("groupName").type(JsonFieldType.STRING).description("상담톡 그룹명"),
			fieldWithPath("senderKey").type(JsonFieldType.STRING).description("관련상담톡서비스"),
			fieldWithPath("personIds").type(JsonFieldType.ARRAY).description("추가사용자"),
	};

	private FieldDescriptor[] summaryTalkGroupPersonResponse = new FieldDescriptor[] {
			fieldWithPath("idName").type(JsonFieldType.STRING).description("한글이름").optional(),
			fieldWithPath("peer").type(JsonFieldType.STRING).description("전화기 ID").optional(),
			fieldWithPath("extension").type(JsonFieldType.STRING).description("내선전화번호").optional(),
			fieldWithPath("hostName").type(JsonFieldType.STRING).description("교환기명").optional(),
			fieldWithPath("id").type(JsonFieldType.STRING).description("상담원 아이디"),
			fieldWithPath("organization").type(JsonFieldType.OBJECT).description("조직정보").optional()
	};

	private FieldDescriptor[] organizationSummaryResponse = new FieldDescriptor[] {
			fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드").optional(),
			fieldWithPath("groupName").type(JsonFieldType.STRING).description("조직명").optional(),
			fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("윗레벨의 코드를 포함한 코드의 나열(002_003_008)").optional(),
			fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("조직레벨").optional()
	};

	private FieldDescriptor[] summaryTalkServiceResponse = new FieldDescriptor[] {
			fieldWithPath("senderKey").type(JsonFieldType.STRING).description("관련상담톡서비스"),
			fieldWithPath("kakaoServiceName").type(JsonFieldType.STRING).description("상담톡 서비스명"),
	};

//	@Test
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
						.and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("상담톡수신그룹 목록").optional())
						.andWithPrefix("data.[]", talkMemberGroupSummaryResponse)
						.and(fieldWithPath("data.[].persons").type(JsonFieldType.ARRAY).description("추가된 사용자 목록").optional())
						.andWithPrefix("data.[].persons[]", summaryTalkGroupPersonResponse)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();
	}

//	@Test
	@Order(1)
	public void post() throws Exception {
		final TalkMemberGroupFormRequest form = new TalkMemberGroupFormRequest();
		form.setGroupName("상품문의그룹1");

		final Set<String> persons = new HashSet<>();
		persons.add("user0681");

		form.setPersonIds(persons);

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andDo(print())
				.andExpect(status().isCreated())
				.andDo(document.document(
						requestFields(talkMemberGroupFormRequest),
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

//	@Test
	@Order(2)
	public void get() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{groupId}", 4)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						pathParameters(
								parameterWithName("groupId").description("상담톡 그룹 아이디")
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").type(JsonFieldType.OBJECT).description("상담톡수신 상세정보").optional(),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
						.andWithPrefix("data.", talkMemberGroupDetailResponse)
						.andWithPrefix("data.persons[]", summaryTalkGroupPersonResponse)
						.andWithPrefix("data.persons[].organization.", organizationSummaryResponse)
				))
				.andReturn();

		final TalkMemberGroupDetailResponse data = getData(result, TalkMemberGroupDetailResponse.class);

		log.info("data {}", data);
	}

//	@Test
	@Order(2)
	protected void put() throws Exception {
		final TalkMemberGroupFormRequest form = new TalkMemberGroupFormRequest();

		form.setGroupName("상품문의그룹TEST");

		final Set<String> persons = new HashSet<>();
		persons.add("user0681");

		form.setPersonIds(persons);

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/{groupId}", 4)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						pathParameters(
								parameterWithName("groupId").description("상담톡 그룹 아이디")
						),
						requestFields(talkMemberGroupFormRequest),
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
	@Order(5)
	public void delete() throws Exception {
		this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{groupId}", 4)
				.with(new JwtRequestPostProcessor()))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("result").value("success"))
				.andDo(document.document(
						pathParameters(
								parameterWithName("groupId").description("상담톡 그룹 아이디")
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
	 * 추가 가능한 사용자 목록조회
	 */
//	@Test
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

	/**
	 *  추가 가능한 사용자 목록조회
	 */
//	@Test
	protected void add_on_persons() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/add-on-persons")
				.accept(MediaType.APPLICATION_JSON)
//				.param("groupId", String.valueOf(2))
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						requestParameters(
							parameterWithName("groupId").description("상담톡 그룹 아이디").optional()
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
						.and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("추가 가능한 사용자 목록").optional())
							.andWithPrefix("data.[]", summaryTalkGroupPersonResponse)
						.andWithPrefix("data.[].organization.", organizationSummaryResponse)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();

		final List<SummaryTalkGroupPersonResponse> responses = listData(result, SummaryTalkGroupPersonResponse.class);
		for (SummaryTalkGroupPersonResponse response : responses) {
			log.info(response.toString());
		}
	}
}


