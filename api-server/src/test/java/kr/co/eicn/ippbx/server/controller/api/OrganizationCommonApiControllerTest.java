package kr.co.eicn.ippbx.server.controller.api;

import kr.co.eicn.ippbx.server.model.dto.eicn.OrganizationSummaryResponse;
import kr.co.eicn.ippbx.server.model.entity.eicn.OrganizationMeta;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class OrganizationCommonApiControllerTest extends BaseControllerTest {
	private final String TEST_URL = "/api/organization";

	private FieldDescriptor[] companyTreeLevelName = new FieldDescriptor[] {
			fieldWithPath("seq").type(JsonFieldType.NUMBER).description("SEQUENCE KEY"),
			fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("조직레벨").optional(),
			fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("윗레벨의 코드를 포함한 코드의 나열(002_003_008)").optional(),
			fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사 아이디").optional()
	};

	private FieldDescriptor[] companyTrees = new FieldDescriptor[] {
			fieldWithPath("seq").type(JsonFieldType.NUMBER).description("SEQUENCE KEY"),
			fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드").optional(),
			fieldWithPath("groupName").type(JsonFieldType.STRING).description("조직명").optional(),
			fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("윗레벨의 코드를 포함한 코드의 나열(002_003_008)").optional(),
			fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("조직레벨").optional(),
			fieldWithPath("parentGroupCode").type(JsonFieldType.STRING).description("상위 조직코드").optional(),
			fieldWithPath("parentTreeName").type(JsonFieldType.STRING).description("차수별 상위 조직코드(002_003)").optional(),
			fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사 아이디").optional()
	};

	private FieldDescriptor[] organizationSummaryResponse = new FieldDescriptor[] {
			fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드"),
			fieldWithPath("groupName").type(JsonFieldType.STRING).description("조직명"),
			fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("윗레벨의 코드를 포함한 코드의 나열(002_003_008)").optional(),
			fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("조직레벨")
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

//	@Test
	public void getMetaTree() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/meta-tree")
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andExpect(status().isOk())
				.andDo(document.document(
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
						.and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("그룹레벨정보, 그룹레벨에 조직정보 목록을 반환").optional())
						.andWithPrefix("data.[]", companyTreeLevelName)
						.and(fieldWithPath("data.[].companyTrees").type(JsonFieldType.ARRAY).description("조직정보 목록").optional())
						.andWithPrefix("data.[].companyTrees[]", companyTrees)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();

		final List<OrganizationMeta> responses = listData(result, OrganizationMeta.class);

		log.info("response {}", responses);
	}

	/**
	 * 조직정보 조회
	 */
//	@Test
	public void get_company_tree_groupcode() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{groupCode}", "0007")
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(document.document(
						pathParameters(
								parameterWithName("groupCode").description("조직코드")
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
						.and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("조직정보 목록").optional())
						.andWithPrefix("data.", organizationSummaryResponse)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andExpect(status().isOk())
				.andReturn();
	}

	/**
	 * 조직정보 목록조회
	 *  - 차수별 조직정보 목록
	 */
//	@Test
	public void get_company_tree_groupCode_list() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{groupCode}/company-tree", "0007")
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andDo(document.document(
						pathParameters(
								parameterWithName("groupCode").description("조직코드")
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
								.and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("조직정보 목록").optional())
								.andWithPrefix("data.[]", organizationSummaryResponse)
								.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andExpect(status().isOk())
				.andReturn();
	}
}
