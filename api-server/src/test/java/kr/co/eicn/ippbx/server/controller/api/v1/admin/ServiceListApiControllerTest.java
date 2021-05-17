package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.server.model.dto.eicn.ServiceListDetailResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.ServiceListSummaryResponse;
import kr.co.eicn.ippbx.server.model.form.ServiceListFormRequest;
import kr.co.eicn.ippbx.server.model.form.ServiceListFormUpdateRequest;
import kr.co.eicn.ippbx.server.repository.eicn.ServiceRepository;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class ServiceListApiControllerTest extends BaseControllerTest {
	private final String TEST_URL = "/api/v1/admin/user/tel/service";
	private static Integer seq;

	@Autowired
	private ServiceRepository serviceRepository;

	private FieldDescriptor[] serviceListResponse = new FieldDescriptor[] {
			fieldWithPath("seq").type(JsonFieldType.NUMBER).description("시퀀스키"),
			fieldWithPath("svcName").type(JsonFieldType.STRING).description("대표번호 한글명"),
			fieldWithPath("svcNumber").type(JsonFieldType.STRING).description("11자리 대표번호"),
			fieldWithPath("svcCid").type(JsonFieldType.STRING).description("원서비스번호").optional(),
			fieldWithPath("companyId").type(JsonFieldType.STRING).description("회사아이디"),
			fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드").optional(),
			fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("윗레벨의 코드를 포함한 코드의 나열(002_003_008)").optional(),
			fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("조직레벨").optional(),
			fieldWithPath("serviceLevel").type(JsonFieldType.NUMBER).description("서비스레벨"),
			fieldWithPath("hostName").type(JsonFieldType.STRING).description("교환기명").optional()
	};

	private FieldDescriptor[] organizationSummary = new FieldDescriptor[] {
			fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드").optional(),
			fieldWithPath("groupName").type(JsonFieldType.STRING).description("조직명").optional(),
			fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("윗레벨의 코드를 포함한 코드의 나열(002_003_008)").optional(),
			fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("조직레벨").optional()
	};

//	@Test
	@Order(4)
	protected void list() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andExpect(status().isOk())
				.andDo(document.document(
						responseFields(
									fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
									fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional())
								.and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("반환될 데이터").optional())
								.andWithPrefix("data.[]", serviceListResponse)
								.and(fieldWithPath("data.[].organizationSummary").type(JsonFieldType.ARRAY).description("차수별 상위 조직 요약정보").optional())
								.andWithPrefix("data.[].organizationSummary[]", organizationSummary)
								.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();

		final List<ServiceListSummaryResponse> responseData = listData(result, ServiceListSummaryResponse.class);

		log.info("response data {}", responseData.toString());
	}

//	@Test
	@Order(1)
	@Override
	protected void post() throws Exception {
		final ServiceListFormRequest form = new ServiceListFormRequest();
		form.setSvcNumber("01030068249");
		form.setSvcName("대표번호");
		form.setSvcCid("1544");
		form.setGroupCode("0008");
		form.setServiceLevel(2);

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(form))
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isCreated())
				.andDo(document.document(
						requestFields(
								fieldWithPath("svcName").type(JsonFieldType.STRING).description("대표번호 한글명"),
								fieldWithPath("svcNumber").type(JsonFieldType.STRING).description("11자리 대표번호"),
								fieldWithPath("svcCid").type(JsonFieldType.STRING).description("원서비스번호").optional(),
								fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드"),
								fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("윗레벨의 코드를 포함한 코드의 나열(002_003_008)").ignored(),
								fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("조직레벨").ignored(),
								fieldWithPath("serviceLevel").type(JsonFieldType.NUMBER).description("서비스레벨").optional()
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").type(JsonFieldType.NUMBER).description("등록 SEQUENCE 값").optional(),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				))
				.andReturn();

		seq = getData(result, Integer.class);
		log.info("SEQUENCE KEY {}", seq);
	}

//	@Test
	@Order(3)
	@Override
	protected void get() throws Exception {
		final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{seq}", seq)
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
								.and(fieldWithPath("data").type(JsonFieldType.OBJECT).description("반환 될 데이터"))
								.andWithPrefix("data.", serviceListResponse)
								.and(fieldWithPath("data.organizationSummary").type(JsonFieldType.ARRAY).description("차수별 상위 조직 요약정보").optional())
								.andWithPrefix("data.organizationSummary[]", organizationSummary)
								.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();

		final ServiceListDetailResponse data = getData(result, ServiceListDetailResponse.class);

		log.info(data.toString());

		assertTrue(() -> data.getSvcName().contains("TEST_CODE"));
	}

//	@Test
	@Order(2)
	@Override
	protected void put() throws Exception {
		final ServiceListFormUpdateRequest form = new ServiceListFormUpdateRequest();
		form.setSvcName("스마트컨택_고객센터TEST_CODE");
		form.setSvcNumber("01030068249");
		form.setSvcCid("07075498119");
		form.setGroupCode("0008");
		form.setServiceLevel(10);

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/{seq}", seq)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andExpect(status().isOk())
				.andDo(document.document(
						pathParameters(
								parameterWithName("seq").description("SEQUENCE KEY")
						),
						requestFields(
								fieldWithPath("svcName").type(JsonFieldType.STRING).description("대표번호 한글명"),
								fieldWithPath("svcNumber").type(JsonFieldType.STRING).description("11자리 대표번호"),
								fieldWithPath("svcCid").type(JsonFieldType.STRING).description("원서비스번호").optional(),
								fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드"),
								fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("윗레벨의 코드를 포함한 코드의 나열(002_003_008)").ignored(),
								fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("조직레벨").ignored(),
								fieldWithPath("serviceLevel").type(JsonFieldType.NUMBER).description("서비스레벨").optional()
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").type(JsonFieldType.NULL).description(""),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				))
				.andReturn();
	}

//	@Test
	@Order(5)
	@Override
	protected void delete() throws Exception {
		this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{seq}", seq)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("result").value("success"))
				.andDo(document.document(
						pathParameters(
								parameterWithName("seq").description("SEQUENCE KEY")
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").type(JsonFieldType.NULL).description(""),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				))
				.andReturn();

		assertNull(serviceRepository.findOne(seq));
	}
}
