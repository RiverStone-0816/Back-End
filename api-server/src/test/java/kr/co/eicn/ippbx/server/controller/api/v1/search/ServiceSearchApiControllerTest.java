package kr.co.eicn.ippbx.server.controller.api.v1.search;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.server.model.dto.eicn.search.SearchServiceResponse;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class ServiceSearchApiControllerTest extends BaseControllerTest {
	private final String TEST_URL = "/api/v1/search/service";

	private FieldDescriptor[] searchServiceResponse = new FieldDescriptor[]{
			fieldWithPath("seq").type(JsonFieldType.NUMBER).description("시퀀스키"),
			fieldWithPath("svcName").type(JsonFieldType.STRING).description("대표번호 한글명"),
			fieldWithPath("svcNumber").type(JsonFieldType.STRING).description("11자리 대표번호"),
			fieldWithPath("svcCid").type(JsonFieldType.STRING).description("원서비스번호").optional(),
			fieldWithPath("companyId").type(JsonFieldType.STRING).description("회사아이디"),
			fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드").optional(),
			fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("윗레벨의 코드를 포함한 코드의 나열(002_003_008)").optional(),
			fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("조직레벨").optional(),
			fieldWithPath("serviceLevel").type(JsonFieldType.NUMBER).description("서비스레벨")

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
				.with(new JwtRequestPostProcessor()))
				.andExpect(status().isOk())
				.andDo(document.document(
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
						.and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("대표전화번호 목록").optional())
						.andWithPrefix("data.[]", searchServiceResponse)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();

		final List<SearchServiceResponse> responses = listData(result, SearchServiceResponse.class);

		log.info("response {}", responses);
	}
}
