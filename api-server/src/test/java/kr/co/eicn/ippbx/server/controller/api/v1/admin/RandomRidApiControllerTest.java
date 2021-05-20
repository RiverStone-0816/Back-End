package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.model.form.RandomCidFormRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class RandomRidApiControllerTest extends BaseControllerTest {
	private final String TEST_URL = "/api/v1/admin/user/tel/random-rid";

	private final FieldDescriptor[] randomCidResponse = new FieldDescriptor[] {
			fieldWithPath("seq").type(JsonFieldType.NUMBER).description("SEQUENCE KEY"),
			fieldWithPath("number").type(JsonFieldType.STRING).description("발신번호"),
			fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드"),
			fieldWithPath("shortNum").type(JsonFieldType.NUMBER).description("단축번호"),
			fieldWithPath("companyTrees").type(JsonFieldType.ARRAY).description("조직정보 목록").optional()
	};

	private FieldDescriptor[] organizationSummaryResponse = new FieldDescriptor[] {
			fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드"),
			fieldWithPath("groupName").type(JsonFieldType.STRING).description("조직명"),
			fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("윗레벨의 코드를 포함한 코드의 나열(002_003_008)").optional(),
			fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("조직레벨")
	};

//	@Test
	public void pagination() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.param("groupCode", "0014")
				.param("number", ""))
				.andExpect(status().isOk())
				.andDo(document.document(
						requestParameters(
								parameterWithName("groupCode").description("소속"),
								parameterWithName("number").description("번호")
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
						.and(fieldWithPath("data.rows[]").type(JsonFieldType.ARRAY).description("랜덤RID 목록").optional())
						.andWithPrefix("data.rows[]", randomCidResponse)
						.andWithPrefix("data.rows[].companyTrees[]", organizationSummaryResponse)
						.andWithPrefix("data.",
								fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지"),
								fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총갯수")
						)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();
	}

//	@Test
	public void get() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{seq}", 1)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andExpect(status().isOk())
				.andDo(document.document(
						pathParameters(
								parameterWithName("seq").description("SEQUENCE KEY")
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
						.and(fieldWithPath("data.").type(JsonFieldType.OBJECT).description("랜덤 RID 상세정보").optional())
						.andWithPrefix("data.", randomCidResponse)
						.andWithPrefix("data.companyTrees[]", organizationSummaryResponse)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();
	}

//	@Test
	public void post() throws Exception {
		final RandomCidFormRequest form = new RandomCidFormRequest();
		form.setNumber("0537168548");
		form.setShortNum((byte) 3);

		this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andDo(print())
				.andExpect(status().isCreated())
				.andDo(document.document(
						requestFields(
								fieldWithPath("number").type(JsonFieldType.STRING).description("발신번호"),
								fieldWithPath("shortNum").type(JsonFieldType.NUMBER).description("단축번호"),
								fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드").optional()
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").type(JsonFieldType.NULL).description("NULL").optional(),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				))
				.andReturn();
	}

//	@Test
	public void put() throws Exception {
			/*final RandomCidUpdateFormRequest form = new RandomCidUpdateFormRequest();
			form.setShortNum((byte) 2);
			form.setGroupCode("0014");

			this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/{seq}", 1)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.with(new JwtRequestPostProcessor())
					.content(mapper.writeValueAsString(form)))
					.andDo(print())
					.andExpect(status().isOk())
					.andDo(document.document(
							pathParameters(
									parameterWithName("seq").description("SEQUENCE KEY")
							),
							requestFields(
									fieldWithPath("shortNum").type(JsonFieldType.NUMBER).description("단축번호"),
									fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드").optional()
							),
							responseFields(
									fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
									fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
									fieldWithPath("data").type(JsonFieldType.NULL).description("NULL").optional(),
									fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
							)
					))
					.andReturn();*/
	}

//	@Test
	public void delete() throws Exception {
		this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{seq}", 1)
				.contentType(MediaType.APPLICATION_JSON)
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
								fieldWithPath("data").type(JsonFieldType.NULL).description("NULL").optional(),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				))
				.andReturn();
	}
}
