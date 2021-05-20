package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.model.dto.eicn.TalkMentSummaryResponse;
import kr.co.eicn.ippbx.model.form.TalkMentFormRequest;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Order;
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

@Log4j2
public class TalkCommentApiControllerTest extends BaseControllerTest {
	private final String TEST_URL = "/api/v1/admin/talk/group/auto-comment";
	private static Integer seq;

	private FieldDescriptor[] talkMentSummaryResponse = new FieldDescriptor[]{
			fieldWithPath("seq").type(JsonFieldType.NUMBER).description("SEQUENCE KEY"),
			fieldWithPath("mentName").type(JsonFieldType.STRING).description("멘트명"),
			fieldWithPath("ment").type(JsonFieldType.STRING).description("멘트")
	};

	private FieldDescriptor[] talkMentDetailResponse = new FieldDescriptor[]{
			fieldWithPath("seq").type(JsonFieldType.NUMBER).description("SEQUENCE KEY"),
			fieldWithPath("mentName").type(JsonFieldType.STRING).description("멘트명"),
			fieldWithPath("ment").type(JsonFieldType.STRING).description("멘트")
	};

	private FieldDescriptor[] talkMentFormRequest = new FieldDescriptor[]{
			fieldWithPath("mentName").type(JsonFieldType.STRING).description("멘트명"),
			fieldWithPath("ment").type(JsonFieldType.STRING).description("멘트")
	};

//	@Test
	@Order(1)
	protected void list() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andExpect(status().isOk())
				.andDo(document.document(
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional())
								.and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("상담톡정보 목록").optional())
								.andWithPrefix("data.[]", talkMentSummaryResponse)
								.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				))
				.andReturn();

		final List<TalkMentSummaryResponse> summaryResponses = listData(result, TalkMentSummaryResponse.class);
		for (TalkMentSummaryResponse summaryResponse : summaryResponses) {
			log.info(summaryResponse.toString());
		}
	}

//	@Test
	@Order(1)
	public void post() throws Exception {
		final TalkMentFormRequest form = new TalkMentFormRequest();
		form.setMentName("비업무시간멘트2");
		form.setMent("안녕하십니까? EICN 입니다.상담 시간이 \n" +
				"아닙니다.상담시간은 평일 오전 9시부터 오후 6시 까지 입니다.\n");

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andDo(print())
				.andExpect(status().isCreated())
				.andDo(document.document(
						requestFields(talkMentFormRequest),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").type(JsonFieldType.NUMBER).description("등록 SEQUENCE 값").optional(),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				))
				.andReturn();

	}

//	@Test
	@Order(2)
	public void get() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{seq}", 5)
				.accept(MediaType.APPLICATION_JSON)
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
								fieldWithPath("data").type(JsonFieldType.OBJECT).description("상담톡자동멘트 상세정보").optional(),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
						.andWithPrefix("data.", talkMentDetailResponse)
				))
				.andReturn();
	}

//	@Test
	@Order(2)
	protected void put() throws Exception {
		final TalkMentFormRequest form = new TalkMentFormRequest();
		form.setMentName("비업무시간멘트");
		form.setMent("안녕하십니까? EICN 입니다.상담 시간이 \n" +
				"아닙니다.상담시간은 평일 오전 9시부터 오후 6시 까지 입니다.\n");

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/{seq}", 5)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						pathParameters(
								parameterWithName("seq").description("SEQUENCE KEY")
						),
						requestFields(talkMentFormRequest),
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
		this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{seq}", 5)
				.with(new JwtRequestPostProcessor()))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("result").value("success"))
				.andDo(document.document(
						pathParameters(
								parameterWithName("seq").description("SEQUENCE KEY")
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
}
