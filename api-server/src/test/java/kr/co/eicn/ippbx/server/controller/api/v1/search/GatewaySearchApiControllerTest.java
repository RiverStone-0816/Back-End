package kr.co.eicn.ippbx.server.controller.api.v1.search;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchGwInfoResponse;
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
public class GatewaySearchApiControllerTest extends BaseControllerTest {
	private final String TEST_URL = "/api/v1/search/gateway";

	private FieldDescriptor[] searchGatewayResponse = new FieldDescriptor[]{
			fieldWithPath("host").type(JsonFieldType.STRING).description("서버아이디"),
			fieldWithPath("ip").type(JsonFieldType.STRING).description("VIP IP"),
			fieldWithPath("ip1").type(JsonFieldType.STRING).description("MASTER IP"),
			fieldWithPath("ip2").type(JsonFieldType.STRING).description("SLAVE IP"),
			fieldWithPath("name").type(JsonFieldType.STRING).description("서벼명"),
			fieldWithPath("type").type(JsonFieldType.STRING).description("서버유형(SIP: SIP소프트스위치, CPG:CPG, ETC:기타, TRK:트렁크, SBC:SBC)"),
			fieldWithPath("numberNum").type(JsonFieldType.NUMBER).description(""),
			fieldWithPath("isDefault").type(JsonFieldType.STRING).description(""),
			fieldWithPath("trunkIds").type(JsonFieldType.STRING).description("")
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
						.and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("게이트웨이 목록").optional())
						.andWithPrefix("data.[]", searchGatewayResponse)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();

		final List<SearchGwInfoResponse> responses = listData(result, SearchGwInfoResponse.class);

		log.info("response {}", responses);
	}
}
