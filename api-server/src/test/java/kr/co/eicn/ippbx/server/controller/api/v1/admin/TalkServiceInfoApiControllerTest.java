package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.model.dto.eicn.TalkServiceSummaryResponse;
import kr.co.eicn.ippbx.model.form.TalkServiceInfoFormRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class TalkServiceInfoApiControllerTest extends BaseControllerTest {
	private final String TEST_URL = "/api/v1/admin/talk/info/service";
	private static Integer seq;

	private FieldDescriptor[] talkServiceSummaryResponse = new FieldDescriptor[]{
			fieldWithPath("seq").type(JsonFieldType.NUMBER).description("SEQUENCE KEY"),
			fieldWithPath("kakaoServiceName").type(JsonFieldType.STRING).description("상담톡 서비스명"),
			fieldWithPath("kakaoServiceId").type(JsonFieldType.STRING).description("상담톡아이디"),
			fieldWithPath("senderKey").type(JsonFieldType.STRING).description("상담톡키"),
			fieldWithPath("isChattEnable").type(JsonFieldType.STRING).description("상담창활성화"),
	};

	private FieldDescriptor[] talkServiceDetailResponse = new FieldDescriptor[]{
			fieldWithPath("seq").type(JsonFieldType.NUMBER).description("SEQUENCE KEY"),
			fieldWithPath("kakaoServiceName").type(JsonFieldType.STRING).description("상담톡 서비스명"),
			fieldWithPath("kakaoServiceId").type(JsonFieldType.STRING).description("상담톡아이디"),
			fieldWithPath("senderKey").type(JsonFieldType.STRING).description("상담톡키"),
			fieldWithPath("isChattEnable").type(JsonFieldType.STRING).description("상담창활성화"),
	};

	private FieldDescriptor[] talkServiceInfoFormRequest = new FieldDescriptor[]{
			fieldWithPath("kakaoServiceName").type(JsonFieldType.STRING).description("상담톡 서비스명"),
			fieldWithPath("kakaoServiceId").type(JsonFieldType.STRING).description("상담톡아이디"),
			fieldWithPath("senderKey").type(JsonFieldType.STRING).description("상담톡키"),
			fieldWithPath("isChattEnable").type(JsonFieldType.STRING).description("상담창활성화"),
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
								.andWithPrefix("data.[]", talkServiceSummaryResponse)
								.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				))
				.andReturn();

		final List<TalkServiceSummaryResponse> summaryResponses = listData(result, TalkServiceSummaryResponse.class);
		for (TalkServiceSummaryResponse summaryResponse : summaryResponses) {
			log.info(summaryResponse.toString());
		}
	}

//	@Test
	@Order(1)
	public void post() throws Exception {
		final TalkServiceInfoFormRequest form = new TalkServiceInfoFormRequest();
		form.setSenderKey("28ee2687812321c674b2453cbd62c121bea349d4");
		form.setKakaoServiceName("상품문의");
		form.setKakaoServiceId("bzc_mbi_78");
		form.setIsChattEnable("Y");

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andDo(print())
				.andExpect(status().isCreated())
				.andDo(document.document(
						requestFields(talkServiceInfoFormRequest),
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
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{seq}", 2)
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
								fieldWithPath("data").type(JsonFieldType.OBJECT).description("상담톡 정보").optional(),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
						.andWithPrefix("data.", talkServiceDetailResponse)
				))
				.andReturn();
	}

//	@Test
	@Order(2)
	protected void put() throws Exception {
		final TalkServiceInfoFormRequest form = new TalkServiceInfoFormRequest();
		form.setSenderKey("28ee2687812321c674b2453cbd62c121bea349d4");
		form.setKakaoServiceName("상품문의 TEST");
		form.setKakaoServiceId("bzc_mbi_7822");
		form.setIsChattEnable("Y");

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/{seq}", 2)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						pathParameters(
								parameterWithName("seq").description("SEQUENCE KEY")
						),
						requestFields(talkServiceInfoFormRequest),
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
		this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{seq}", 4)
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
