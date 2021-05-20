package kr.co.eicn.ippbx.server.controller.api.v1.search;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchPhoneInfoResponse;
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
public class PhoneInfoSearchApiControllerTest extends BaseControllerTest {
	private final String TEST_URL = "/api/v1/search/phone";

	private FieldDescriptor[] searchPhoneResponse = new FieldDescriptor[] {
			fieldWithPath("peer").type(JsonFieldType.STRING).description("전화기아이디"),
			fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사 아이디"),
			fieldWithPath("extension").type(JsonFieldType.STRING).description("내선번호"),
			fieldWithPath("voipTel").type(JsonFieldType.STRING).description("개인070번호"),
			fieldWithPath("forwardWhen").type(JsonFieldType.STRING).description("착신전환여부 구분(N:착신전환안함, A:항상, B:통화중, C:부재중, T:통화중+부재중)"),
			fieldWithPath("forwarding").type(JsonFieldType.STRING).description("착신할전화번호 구분(E:내선, H:헌트, R:대표번호, O:외부번호)").optional(),
			fieldWithPath("prevent").type(JsonFieldType.STRING).description("금지프리픽스"),
			fieldWithPath("host").type(JsonFieldType.STRING).description("localhost 고정값"),
			fieldWithPath("pickup").type(JsonFieldType.STRING).description("pickup_group 테이블의 groupcode 컬럼값 참조"),
			fieldWithPath("localPrefix").type(JsonFieldType.STRING).description("지역번호"),
			fieldWithPath("logoutStatus").type(JsonFieldType.NUMBER).description("로그아웃 상태"),
			fieldWithPath("dialStatus").type(JsonFieldType.NUMBER).description("전화 상태"),
			fieldWithPath("phoneStatus").type(JsonFieldType.STRING).description("전화기 상태"),
			fieldWithPath("outboundGw").type(JsonFieldType.STRING).description("게이트웨이"),
			fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드"),
			fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("윗레벨의 코드를 포함한 코드의 나열(002_003_008)").optional(),
			fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("조직레벨").optional(),
			fieldWithPath("phoneKind").type(JsonFieldType.STRING).description("N 고정"),
			fieldWithPath("hpDeviceId").type(JsonFieldType.STRING).description("사용안함"),
			fieldWithPath("hpNumber").type(JsonFieldType.STRING).description("사용안함"),
			fieldWithPath("fwWhen").type(JsonFieldType.STRING).description("착신전환여부 구분값").optional(),
			fieldWithPath("fwKind").type(JsonFieldType.STRING).description("착신할전화번호 구분값").optional(),
			fieldWithPath("fwNum").type(JsonFieldType.STRING).description("착신할전화번호").optional(),
			fieldWithPath("recordType").type(JsonFieldType.STRING).description("녹취여부(M:녹취함, S:녹취안함)"),
			fieldWithPath("cid").type(JsonFieldType.STRING).description("발신번호"),
			fieldWithPath("billingNumber").type(JsonFieldType.STRING).description("과금번호"),
			fieldWithPath("originalNumber").type(JsonFieldType.STRING).description("번호이동원번호").optional()
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
				.param("cidNumber", ""))
				.andExpect(status().isOk())
				.andDo(document.document(
						requestParameters(
								parameterWithName("cidNumber").description("발신번호").optional()
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
								.and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("전화기정보 목록").optional())
								.andWithPrefix("data.[]", searchPhoneResponse)
								.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();

		final List<SearchPhoneInfoResponse> responses = listData(result, SearchPhoneInfoResponse.class);

		log.info("response {}", responses);
	}
}
