package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.server.repository.CompanyTreeNameRepositoryTest;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CompanyTreeLevelName;
import kr.co.eicn.ippbx.server.model.OrganizationPerson;
import kr.co.eicn.ippbx.server.model.dto.eicn.CompanyTreeLevelNameResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.OrganizationPersonSummaryResponse;
import kr.co.eicn.ippbx.server.model.entity.eicn.Organization;
import kr.co.eicn.ippbx.server.model.form.CompanyTreeNameFormRequest;
import kr.co.eicn.ippbx.server.model.form.CompanyTreeNameUpdateFormRequest;
import kr.co.eicn.ippbx.server.model.form.OrganizationFormRequest;
import kr.co.eicn.ippbx.server.model.form.OrganizationFormUpdateRequest;
import kr.co.eicn.ippbx.server.service.OrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class OrganizationApiControllerTest extends BaseControllerTest {
	private final String TEST_URL = "/api/v1/admin/user/organization";
	private static Integer seq;

	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private OrganizationService organizationService;
	@Autowired
	private CompanyTreeNameRepositoryTest companyTreeNameRepositoryTest;

	private FieldDescriptor[] organization = new FieldDescriptor[] {
			fieldWithPath("seq").type(JsonFieldType.NUMBER).description("SEQUENCE KEY"),
			fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드").optional(),
			fieldWithPath("groupName").type(JsonFieldType.STRING).description("조직명").optional(),
			fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("윗레벨의 코드를 포함한 코드의 나열(002_003_008)").optional(),
			fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("조직레벨").optional(),
			fieldWithPath("parentGroupCode").type(JsonFieldType.STRING).description("상위 조직코드").optional(),
			fieldWithPath("parentTreeName").type(JsonFieldType.STRING).description("차수별 상위 조직코드(002_003)").optional(),
			fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사 아이디").optional(),
			fieldWithPath("persons[]").type(JsonFieldType.ARRAY).description("소속 사용자들").optional()
	};

	private FieldDescriptor[] organizationPerson = new FieldDescriptor[]{
			fieldWithPath("id").type(JsonFieldType.STRING).description("아이디"),
			fieldWithPath("idName").type(JsonFieldType.STRING).description("사용자명"),
			fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드"),
			fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("조직레벨"),
			fieldWithPath("extension").type(JsonFieldType.STRING).description("내선번호"),
			fieldWithPath("peer").type(JsonFieldType.STRING).description("전화기 아이디"),
			fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사아이디"),
			fieldWithPath("hpNumber").type(JsonFieldType.STRING).description("연락처"),
			fieldWithPath("emailInfo").type(JsonFieldType.STRING).description("메일정보")
	};

	private FieldDescriptor[] treeLevelNameFields = new FieldDescriptor[] {
			fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("차수"),
			fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("메타명")
	};

	private FieldDescriptor[] memberSummary = new FieldDescriptor[] {
			fieldWithPath("seq").type(JsonFieldType.NUMBER).description("SEQUENCE KEY"),
			fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드").optional(),
			fieldWithPath("groupName").type(JsonFieldType.STRING).description("조직명").optional(),
			fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("윗레벨의 코드를 포함한 코드의 나열(002_003_008)").optional(),
			fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("조직레벨").optional(),
			fieldWithPath("parentGroupCode").type(JsonFieldType.STRING).description("상위 조직코드").optional(),
			fieldWithPath("parentTreeName").type(JsonFieldType.STRING).description("차수별 상위 조직코드(002_003)").optional(),
			fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사 아이디").optional(),
			fieldWithPath("countsOfChildren").type(JsonFieldType.ARRAY).description("자식 구성원들의 계층별 수").optional(),
			fieldWithPath("userCountBelonging").type(JsonFieldType.NUMBER).description("자신에게 속한 사용자 숫자")
	};

//	@Test
	@Order(6)
	public void get_all_organization_members() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andExpect(status().isOk())
				.andDo(document.document(
						responseFields(
									fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
									fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
								)
								.and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("반환될 데이터").optional())
								.andWithPrefix("data.[]", organization)
								.andWithPrefix("data.[].persons[]", organizationPerson)
								.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
						)
				)
				.andReturn();

		final List<Organization> organizations = listData(result, Organization.class);

		printOrganizationTree(organizations);
	}

//	@Test
	@Order(5)
	public void get_organization_member() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{seq}", seq)
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
						.and(
								fieldWithPath("data").type(JsonFieldType.OBJECT).description("조직 정보"),
								fieldWithPath("data.persons[]").type(JsonFieldType.ARRAY).description("조직 구성원").optional()
						)
						.andWithPrefix("data.", organization)
						.andWithPrefix("data.persons[]", organizationPerson)
						.andWithPrefix("data.",
								fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지"),
								fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총갯수")
						)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();

		final Organization data = getData(result, Organization.class);
		log.info("{}", data);

		assertEquals(data.getGroupName(), "강원지역");
	}

//	@Test
	@Order(7)
	public void get_organization_member_summary() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{seq}/summary", 7)
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
						.and(
								fieldWithPath("data.parents").type(JsonFieldType.ARRAY).description("전체 부모 구성원 정보").optional(),
								fieldWithPath("data.parents[].persons[]").type(JsonFieldType.ARRAY).description("조직 구성원들").optional(),
								fieldWithPath("data.countsOfChildren").type(JsonFieldType.ARRAY).description("자식 구성원들의 계층별 수").optional(),
								fieldWithPath("data.userCountBelonging").type(JsonFieldType.NUMBER).description("자신에게 속한 사용자 숫자")
						)
						.andWithPrefix("data.parents[]", organization)
						.andWithPrefix("data.parents[].persons[]", organizationPerson)
						.andWithPrefix("data.countsOfChildren", fieldWithPath("[]").type(JsonFieldType.ARRAY).description("구성원의 수").optional())
						.and(fieldWithPath("data.memberSummaries[]").type(JsonFieldType.ARRAY).description("같은 레벨 구성원 정보").optional())
						.andWithPrefix("data.memberSummaries[]", memberSummary)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();

		final OrganizationPersonSummaryResponse data = getData(result, OrganizationPersonSummaryResponse.class);

		log.info("{}", data);
	}

//	@Test
	@Order(3)
	public void register() throws Exception {
		final OrganizationFormRequest form = new OrganizationFormRequest();
		form.setGroupName("고객상담센터");
		form.setParentGroupCode("0016");
		form.setGroupLevel(2);

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andExpect(status().isCreated())
				.andDo(document.document(
						requestFields(
								fieldWithPath("groupName").type(JsonFieldType.STRING).description("조직명"),
								fieldWithPath("parentGroupCode").type(JsonFieldType.STRING).description("상위 조직코드").optional(),
								fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("차수")),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").type(JsonFieldType.NUMBER).description("실제 반환될 데이터").optional(),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				))
				.andReturn();

		seq = getData(result, Integer.class);
	}

//	@Test
	@Order(4)
	public void update() throws Exception {
		final OrganizationFormUpdateRequest form = new OrganizationFormUpdateRequest();
		form.setGroupName("강원지역");

		this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/{seq}", seq)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(form))
				.with(new JwtRequestPostProcessor()))
				.andExpect(status().isOk())
				.andDo(document.document(
						requestFields(fieldWithPath("groupName").type(JsonFieldType.STRING).description("조직명")),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").type(JsonFieldType.NUMBER).description("실제 반환될 데이터").optional(),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				));
	}

//	@Test
	@Order(8)
	@Override
	protected void delete() throws Exception {
		this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{seq}", 49)
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

		final Organization any = organizationService.getAllOrganizationPersons().stream().filter(e -> Objects.equals(e.getSeq(), seq))
				.findAny()
				.orElse(null);

		assertNull(any);
	}

//	@Test
	@Order(2)
	public void list_meta_type() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/meta-type")
				.with(new JwtRequestPostProcessor()))
				.andExpect(status().isOk())
				.andDo(document.document(
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
						.and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("반환될 데이터").optional())
						.andWithPrefix("data.[]", treeLevelNameFields)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
					)
				)
				.andReturn();

		final List<CompanyTreeLevelNameResponse> companyTreeLevelNames = listData(result, CompanyTreeLevelNameResponse.class);

		log.info("{}", companyTreeLevelNames.size());
	}

//	@Test
	public void create_meta_type() {
		final CompanyTreeNameFormRequest form = new CompanyTreeNameFormRequest();
		form.setGroupLevel(1);
		form.setGroupTreeName("사업부");

		final CompanyTreeNameFormRequest form2 = new CompanyTreeNameFormRequest();
		form2.setGroupLevel(2);
		form2.setGroupTreeName("센터");

		final CompanyTreeNameFormRequest form3 = new CompanyTreeNameFormRequest();
		form3.setGroupLevel(3);
		form3.setGroupTreeName("지역");

		final CompanyTreeNameFormRequest form4 = new CompanyTreeNameFormRequest();
		form4.setGroupLevel(4);
		form4.setGroupTreeName("부서");

		final HashSet<CompanyTreeNameFormRequest> forms = new HashSet<>();

		forms.add(form);
		forms.add(form2);
		forms.add(form3);
		forms.add(form4);

		companyTreeNameRepositoryTest.createdMetaTypes(forms);
	}

//	@Test
	@Order(1)
	public void update_meta_type() throws Exception {
		final List<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CompanyTreeLevelName> test_code = getTestCode();

		final CompanyTreeLevelName companyTreeLevelName = test_code.get(0);
		final String groupTreeName = companyTreeLevelName.getGroupTreeName();

		final CompanyTreeNameUpdateFormRequest map = modelMapper.map(companyTreeLevelName, CompanyTreeNameUpdateFormRequest.class);
		map.setGroupTreeName(groupTreeName + "수정");

		Set param = new HashSet();
		param.add(map);

		this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/meta-type")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(param)))
				.andExpect(status().isOk())
				.andDo(document.document(
						requestFields(
								fieldWithPath("[]").description("메타 정보 집합")
						)
								.andWithPrefix("[]", fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("차수"))
								.andWithPrefix("[]", fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("메타명"))
						,
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").type(JsonFieldType.NULL).description("실제 반환될 데이터").optional(),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				))
				.andReturn();

		companyTreeNameRepositoryTest.updateByKey(companyTreeLevelName, companyTreeLevelName.getSeq());

	}

	public List<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CompanyTreeLevelName> getTestCode() {
		return companyTreeNameRepositoryTest.findAll()
				.stream()
				.sorted(Comparator.comparing(kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CompanyTreeLevelName::getGroupLevel))
				.collect(Collectors.toList());
	}

	private void printOrganizationTree(List<Organization> organizations) {
		log.warn("----------------------------------------------------------------------");
		organizations
				.forEach(e -> {
							log.info(prefix(e.getGroupLevel()) + e.getGroupLevel() + ", " + e.getSeq() + ", " +
									e.getGroupCode() + ", " + e.getGroupName() + ", " + e.getParentTreeName() + ", "
									+ e.getGroupTreeName() + ", memberSize: " + e.getPersons().size()
							);
							if (e.getPersons().size() > 0) {
								for (OrganizationPerson member : e.getPersons()) {
									log.info(prefix(e.getGroupLevel() + 1) + (e.getGroupLevel() + 1) + ", "
											+ member.getId() + ", " + member.getIdName() + ", " + member.getPeer());
								}
							}
						}
				);
		log.warn("----------------------------------------------------------------------");
	}

	private String prefix(Integer level) {
		String prefix = "-";
		for (int i = 0; i < level ; i++) {
			prefix += "--";
		}
		return prefix;
	}

}
