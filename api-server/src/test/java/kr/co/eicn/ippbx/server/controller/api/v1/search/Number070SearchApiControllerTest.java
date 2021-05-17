package kr.co.eicn.ippbx.server.controller.api.v1.search;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.server.model.dto.eicn.search.SearchNumber070Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class Number070SearchApiControllerTest extends BaseControllerTest {
	private final String TEST_URL = "/api/v1/search/number";

	private FieldDescriptor[] searchNumber070Response = new FieldDescriptor[] {
			fieldWithPath("number").type(JsonFieldType.STRING).description("070번호"),
			fieldWithPath("type").type(JsonFieldType.NUMBER).description("번호타입(0:큐, 1:개인, 2:대표, 3:회의)"),
			fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태(0:비사용, 1:사용)"),
			fieldWithPath("host").type(JsonFieldType.STRING).description("번호사용 IP"),
			fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사 아이디"),
			fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드").optional(),
			fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("윗레벨의 코드를 포함한 코드의 나열(002_003_008)").optional(),
			fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("조직레벨").optional(),
			fieldWithPath("kind").type(JsonFieldType.STRING).description("070번호종류"),
			fieldWithPath("hostName").type(JsonFieldType.STRING).description("소속교환기"),
	};

	@BeforeEach
	@Override
	protected void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
		document = document(
				"{class-name}/{method-name}",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint())
		);

		this.mockMvc = securityBuilder(webApplicationContext, restDocumentation);
	}

	@Test
	public void search() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.param("type", "")
				.param("status", "0"))
				.andExpect(status().isOk())
				.andDo(document.document(
						requestParameters(
								parameterWithName("type").description("번호타입(0:헌트, 1:개인, 2:대표, 3:회의)").optional(),
								parameterWithName("status").description("상태(0:비사용, 1:사용)").optional()
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
						.and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("내선전화기 목록").optional())
							.andWithPrefix("data.[]", searchNumber070Response)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();


		final List<SearchNumber070Response> responses = listData(result, SearchNumber070Response.class);

		log.info("data {}", responses);
	}
}
