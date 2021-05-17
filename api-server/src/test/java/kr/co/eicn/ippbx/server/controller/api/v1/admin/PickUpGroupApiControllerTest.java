package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.server.model.dto.eicn.PickUpGroupDetailResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.PickUpGroupSummaryResponse;
import kr.co.eicn.ippbx.server.model.form.PickUpGroupFormRequest;
import kr.co.eicn.ippbx.server.model.form.PickUpGroupFormUpdateRequest;
import kr.co.eicn.ippbx.server.repository.eicn.PickUpGroupRepository;
import kr.co.eicn.ippbx.server.util.page.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static kr.co.eicn.ippbx.server.jooq.eicn.tables.PickupGroup.PICKUP_GROUP;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class PickUpGroupApiControllerTest extends BaseControllerTest {
	private final String TEST_URL = "/api/v1/admin/user/user/pickup-group";
	private static Integer groupCode;

	@Autowired
	private PickUpGroupRepository repository;

	private FieldDescriptor[] organizationSummaryResponse = new FieldDescriptor[] {
			fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드"),
			fieldWithPath("groupName").type(JsonFieldType.STRING).description("조직명"),
			fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("윗레벨의 코드를 포함한 코드의 나열(002_003_008)").optional(),
			fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("조직레벨")
	};

	private FieldDescriptor[] pickUpGroupSummaryResponse = new FieldDescriptor[] {
			fieldWithPath("seq").type(JsonFieldType.NUMBER).description("SEQUENCE KEY"),
			fieldWithPath("groupcode").type(JsonFieldType.NUMBER).description("당겨받기 그룹코드"),
			fieldWithPath("groupname").type(JsonFieldType.STRING).description("당겨받기 그룹명"),
			fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드"),
			fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("윗레벨의 코드를 포함한 코드의 나열(002_003_008)").optional(),
			fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("조직레벨").optional(),
			fieldWithPath("host").type(JsonFieldType.STRING).description("교환기 호스트"),
			fieldWithPath("hostName").type(JsonFieldType.STRING).description("교환기명").optional(),
			fieldWithPath("personCount").type(JsonFieldType.NUMBER).description("구성인원수")
	};

	private FieldDescriptor[] pickUpGroupDetailResponse = new FieldDescriptor[] {
			fieldWithPath("seq").type(JsonFieldType.NUMBER).description("SEQUENCE KEY"),
			fieldWithPath("groupcode").type(JsonFieldType.NUMBER).description("당겨받기 그룹코드"),
			fieldWithPath("groupname").type(JsonFieldType.STRING).description("당겨받기 그룹명"),
			fieldWithPath("host").type(JsonFieldType.STRING).description("교환기 호스트"),
			fieldWithPath("hostName").type(JsonFieldType.STRING).description("교환기명").optional()
	};

	private FieldDescriptor[] pickUpPersonResponse = new FieldDescriptor[] {
			fieldWithPath("idName").type(JsonFieldType.STRING).description("상담원명"),
			fieldWithPath("peer").type(JsonFieldType.STRING).description("전화기 아이디"),
			fieldWithPath("extension").type(JsonFieldType.STRING).description("내선번호"),
			fieldWithPath("hostName").type(JsonFieldType.STRING).description("교환기명").optional(),
			fieldWithPath("pickup").type(JsonFieldType.STRING).description("당겨받기 그룹코드").optional(),
			fieldWithPath("pickupName").type(JsonFieldType.STRING).description("당겨받기 그룹명").optional()
	};

	private FieldDescriptor[] pickUpGroupFormRequest = new FieldDescriptor[] {
			fieldWithPath("groupname").type(JsonFieldType.STRING).description("당겨받기명"),
			fieldWithPath("host").type(JsonFieldType.STRING).description("교환기 호스트").optional(),
			fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드"),
	};

	private FieldDescriptor[] pickUpGroupFormUpdateRequest = new FieldDescriptor[] {
			fieldWithPath("groupname").type(JsonFieldType.STRING).description("당겨받기명"),
			fieldWithPath("peers").type(JsonFieldType.ARRAY).description("전화기 아이디").optional(),
			fieldWithPath("host").type(JsonFieldType.STRING).description("교환기 호스트").optional()
	};

//	@Test
	@Order(4)
	public void pagination() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.param("groupCode", "")
				.param("groupname", ""))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						requestParameters(
								parameterWithName("groupCode").description("조직코드").optional(),
								parameterWithName("groupname").description("당겨받기 그룹명").optional()
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
						.and(fieldWithPath("data").type(JsonFieldType.OBJECT).description("반환 될 데이터").optional())
						.and(fieldWithPath("data.rows[]").type(JsonFieldType.ARRAY).description("당겨받기그룹 목록").optional())
						.andWithPrefix("data.rows[]", pickUpGroupSummaryResponse)
						.and(fieldWithPath("data.rows[].companyTrees").type(JsonFieldType.ARRAY).description("조직트리의 정보").optional())
						.andWithPrefix("data.rows[].companyTrees[]", organizationSummaryResponse)
						.andWithPrefix("data.", fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지"),
								fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총갯수"))
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();

		final Pagination<PickUpGroupSummaryResponse> pagination = paginationData(result, PickUpGroupSummaryResponse.class);
		final List<PickUpGroupSummaryResponse> rows = pagination.getRows();

		for (PickUpGroupSummaryResponse row : rows) {
			log.info("row {}", row);
		}
	}

//	@Test
	@Order(3)
	protected void get() throws Exception {
		final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{groupcode}", 4)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document.document(
						pathParameters(
								parameterWithName("groupcode").description("당겨받기 그룹코드")
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
						.and(fieldWithPath("data").type(JsonFieldType.OBJECT).description("당겨받기 그룹정보"))
						.andWithPrefix("data.", pickUpGroupDetailResponse)
						.and(fieldWithPath("data.addOnPersons[]").type(JsonFieldType.ARRAY).description("추가 가능한 내선사용자").optional())
						.andWithPrefix("data.addOnPersons[]", pickUpPersonResponse)
						.and(fieldWithPath("data.addPersons[]").type(JsonFieldType.ARRAY).description("추가된 내선사용자").optional())
						.andWithPrefix("data.addPersons[]", pickUpPersonResponse)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();

		final PickUpGroupDetailResponse data = getData(result, PickUpGroupDetailResponse.class);

		log.info("data {}", data);
	}

//	@Test
	@Order(1)
	protected void post() throws Exception {
		final PickUpGroupFormRequest form = new PickUpGroupFormRequest();
		form.setGroupCode("0008"); // 조직코드 // 고객상담팀
		form.setGroupname("영업팀"); // 당겨받기명
		form.setHost("localhost"); //

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(form))
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isCreated())
				.andDo(document.document(
						requestFields(pickUpGroupFormRequest),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").type(JsonFieldType.NUMBER).description("생성된 당겨받기 그룹코드"),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				))
				.andReturn();

		final Integer data = getData(result, Integer.class);

		log.info("data {}", data);
		groupCode = data;
	}

//	@Test
	@Order(2)
	protected void put() throws Exception {
		final PickUpGroupFormUpdateRequest form = new PickUpGroupFormUpdateRequest();
		form.setGroupname("영업TEST");
		form.setHost("localhost");
		final Set<String> peers = new HashSet<>();
		peers.add("75490677");

		form.setPeers(peers);

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/{groupcode}", 5)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						pathParameters(
								parameterWithName("groupcode").description("당겨받기 그룹코드")
						),
						requestFields(pickUpGroupFormUpdateRequest),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.NULL).description("실패 메시지").optional(),
								fieldWithPath("data").type(JsonFieldType.NULL).description("Null").optional(),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				))
				.andReturn();

	}

//	@Test
	@Order(5)
	protected void delete() throws Exception {
		this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{groupcode}", 5)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("result").value("success"))
				.andDo(document.document(
						pathParameters(
								parameterWithName("groupcode").description("당겨받기 그룹코드")
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").type(JsonFieldType.NULL).description(""),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				))
				.andReturn();

		assertNull(repository.findOne(PICKUP_GROUP.GROUPCODE.eq(groupCode)));
	}
}
