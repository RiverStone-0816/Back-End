package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.model.dto.eicn.PhoneInfoDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.PhoneInfoSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryPhoneInfoResponse;
import kr.co.eicn.ippbx.model.enums.ForwardWhen;
import kr.co.eicn.ippbx.model.enums.RecordType;
import kr.co.eicn.ippbx.model.form.PhoneInfoFormRequest;
import kr.co.eicn.ippbx.model.form.PhoneInfoUpdateFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.PhoneInfoRepository;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class PhoneApiControllerTest extends BaseControllerTest {
	private final String TEST_URL = "/api/v1/admin/user/tel/phone";
	private static String peer;

	@Autowired
	private PhoneInfoRepository phoneInfoRepository;

	private FieldDescriptor[] phoneInfoSummaryResponse = new FieldDescriptor[] {
		fieldWithPath("peer").type(JsonFieldType.STRING).description("전화기아이디"),
		fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사 아이디"),
		fieldWithPath("extension").type(JsonFieldType.STRING).description("내선번호"),
		fieldWithPath("voipTel").type(JsonFieldType.STRING).description("개인070번호"),
		fieldWithPath("forwardWhen").type(JsonFieldType.STRING).description("착신전환여부 구분(N:착신전환안함, A:항상, B:통화중, C:부재중, T:통화중+부재중)"),
		fieldWithPath("forwarding").type(JsonFieldType.STRING).description("착신할전화번호 구분(E:내선, H:헌트, R:대표번호, O:외부번호)").optional(),
		fieldWithPath("fwWhen").type(JsonFieldType.STRING).description("착신전환여부 구분값").optional(),
		fieldWithPath("fwKind").type(JsonFieldType.STRING).description("착신할전화번호 구분값").optional(),
		fieldWithPath("fwNum").type(JsonFieldType.STRING).description("착신할전화번호").optional(),
		fieldWithPath("recordType").type(JsonFieldType.STRING).description("녹취여부(M:녹취함, S:녹취안함)"),
		fieldWithPath("cid").type(JsonFieldType.STRING).description("발신번호"),
		fieldWithPath("billingNumber").type(JsonFieldType.STRING).description("과금번호"),
		fieldWithPath("localPrefix").type(JsonFieldType.STRING).description("지역번호"),
		fieldWithPath("originalNumber").type(JsonFieldType.STRING).description("번호이동원번호").optional()
	};

	private FieldDescriptor[] phoneInfoDetailResponse = new FieldDescriptor[] {
			fieldWithPath("peer").type(JsonFieldType.STRING).description("전화기아이디"),
			fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사 아이디"),
			fieldWithPath("extension").type(JsonFieldType.STRING).description("내선번호"),
			fieldWithPath("voipTel").type(JsonFieldType.STRING).description("개인070번호"),
			fieldWithPath("forwardWhen").type(JsonFieldType.STRING).description("착신전환여부 구분(N:착신전환안함, A:항상, B:통화중, C:부재중, T:통화중+부재중)"),
			fieldWithPath("forwarding").type(JsonFieldType.STRING).description("착신할전화번호 구분(E:내선, H:헌트, R:대표번호, O:외부번호)").optional(),
			fieldWithPath("fwWhen").type(JsonFieldType.STRING).description("착신전환여부 구분값").optional(),
			fieldWithPath("fwKind").type(JsonFieldType.STRING).description("착신할전화번호 구분값").optional(),
			fieldWithPath("fwNum").type(JsonFieldType.STRING).description("착신할전화번호").optional(),
			fieldWithPath("recordType").type(JsonFieldType.STRING).description("녹취여부(M:녹취함, S:녹취안함)"),
			fieldWithPath("cid").type(JsonFieldType.STRING).description("발신번호"),
			fieldWithPath("billingNumber").type(JsonFieldType.STRING).description("과금번호"),
			fieldWithPath("localPrefix").type(JsonFieldType.STRING).description("지역번호"),
			fieldWithPath("originalNumber").type(JsonFieldType.STRING).description("번호이동원번호").optional(),
			fieldWithPath("outboundGw").type(JsonFieldType.STRING).description("게이트웨이"),
			fieldWithPath("prevent").type(JsonFieldType.STRING).description("금지프리픽스"),
			fieldWithPath("passwd").type(JsonFieldType.STRING).description("전화기암호"),
			fieldWithPath("number070").type(JsonFieldType.OBJECT).description("070번호 정보").optional()
	};

	private FieldDescriptor[] number070 = new FieldDescriptor[] {
			fieldWithPath("number").type(JsonFieldType.STRING).description("070번호"),
			fieldWithPath("type").type(JsonFieldType.NUMBER).description("번호타입(0:헌트, 1:개인, 2:대표, 3:회의)"),
			fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태(0:비사용, 1:사용)"),
			fieldWithPath("host").type(JsonFieldType.STRING).description("번호사용 IP"),
			fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사 아이디"),
			fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드").optional(),
			fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("윗레벨의 코드를 포함한 코드의 나열(002_003_008)").optional(),
			fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("조직레벨").optional(),
			fieldWithPath("kind").type(JsonFieldType.STRING).description("070번호종류")
	};

	private FieldDescriptor[] phoneInfoFormRequest = new FieldDescriptor[] {
			fieldWithPath("extension").type(JsonFieldType.STRING).description("내선번호"),
			fieldWithPath("number").type(JsonFieldType.STRING).description("070번호"),
			fieldWithPath("peer").type(JsonFieldType.STRING).description("전화기아이디"),
			fieldWithPath("passwd").type(JsonFieldType.STRING).description("전화기암호"),
			fieldWithPath("outboundGw").type(JsonFieldType.STRING).description("게이트웨이"),
			fieldWithPath("cid").type(JsonFieldType.STRING).description("발신번호"),
			fieldWithPath("localPrefix").type(JsonFieldType.STRING).description("지역번호"),
			fieldWithPath("recordType").type(JsonFieldType.STRING).description("녹취여부"),
			fieldWithPath("forwardWhen").type(JsonFieldType.STRING).description("착신전환여부 구분(N:착신전환안함, A:항상, B:통화중, C:부재중, T:통화중+부재중)").optional(),
			fieldWithPath("forwarding").type(JsonFieldType.STRING).description("착신할전화번호 구분(E:내선, H:헌트, R:대표번호, O:외부번호)").optional(),
			fieldWithPath("forwardNum").type(JsonFieldType.STRING).description("착신할전화번호").optional(),
			fieldWithPath("originalNumber").type(JsonFieldType.STRING).description("번호이동원번호").optional(),
			fieldWithPath("prevent").type(JsonFieldType.STRING).description("금지프리픽스").optional(),
			fieldWithPath("forwardKind").type(JsonFieldType.STRING).description("착신할전화번호 구분값").optional()
	};

	private FieldDescriptor[] phoneInfoUpdateFormRequest = new FieldDescriptor[] {
			fieldWithPath("extension").type(JsonFieldType.STRING).description("내선번호"),
			fieldWithPath("number").type(JsonFieldType.STRING).description("070번호"),
			fieldWithPath("peer").type(JsonFieldType.STRING).description("전화기아이디"),
			fieldWithPath("passwd").type(JsonFieldType.STRING).description("전화기암호"),
			fieldWithPath("outboundGw").type(JsonFieldType.STRING).description("게이트웨이"),
			fieldWithPath("cid").type(JsonFieldType.STRING).description("발신번호"),
			fieldWithPath("localPrefix").type(JsonFieldType.STRING).description("지역번호"),
			fieldWithPath("recordType").type(JsonFieldType.STRING).description("녹취여부"),
			fieldWithPath("forwardWhen").type(JsonFieldType.STRING).description("착신전환여부 구분(N:착신전환안함, A:항상, B:통화중, C:부재중, T:통화중+부재중)").optional(),
			fieldWithPath("forwarding").type(JsonFieldType.STRING).description("착신할전화번호 구분(E:내선, H:헌트, R:대표번호, O:외부번호)").optional(),
			fieldWithPath("forwardNum").type(JsonFieldType.STRING).description("착신할전화번호").optional(),
			fieldWithPath("originalNumber").type(JsonFieldType.STRING).description("번호이동원번호").optional(),
			fieldWithPath("prevent").type(JsonFieldType.STRING).description("금지프리픽스").optional(),
			fieldWithPath("forwardKind").type(JsonFieldType.STRING).description("착신할전화번호 구분값").optional(),
			fieldWithPath("oldPeer").type(JsonFieldType.STRING).description("기존전화기아이디")
	};

	private FieldDescriptor[] summaryPhoneInfoResponse = new FieldDescriptor[] {
			fieldWithPath("extension").type(JsonFieldType.STRING).description("내선전화번호"),
			fieldWithPath("inUseIdName").type(JsonFieldType.STRING).description("사용중인 상담원명").optional()
	};

//	@Test
	@Order(3)
	protected void pagination() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.param("page", "1")
				.param("limit", "10")
				.param("extension", "")
				.param("voipTel", "")
				.param("localPrefix", "")
				.param("cid", ""))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						requestParameters(
								parameterWithName("page").description("현재 페이지"),
								parameterWithName("limit").description("페이지 개수"),
								parameterWithName("extension").description("내선번호").optional(),
								parameterWithName("voipTel").description("070번호").optional(),
								parameterWithName("localPrefix").description("지역번호").optional(),
								parameterWithName("cid").description("CID번호").optional()
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
						.and(fieldWithPath("data").type(JsonFieldType.OBJECT).description("반환 될 데이터").optional())
						.and(fieldWithPath("data.rows[]").type(JsonFieldType.ARRAY).description("내선 정보 목록").optional())
							.andWithPrefix("data.rows[]", phoneInfoSummaryResponse)
						.andWithPrefix("data.", fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지"),
							fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총갯수"))
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();

		final Pagination<PhoneInfoSummaryResponse> pagination = paginationData(result, PhoneInfoSummaryResponse.class);
		final List<PhoneInfoSummaryResponse> rows = pagination.getRows();
		for (PhoneInfoSummaryResponse row : rows) {
			log.info(row.toString());
		}
	}

//	@Test
	@Order(4)
	protected void get() throws Exception {
		final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{peer}", "75498130")
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						pathParameters(
								parameterWithName("peer").description("전화기아이디")
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
						.and(fieldWithPath("data").type(JsonFieldType.OBJECT).description("내선정보"))
						.andWithPrefix("data.", phoneInfoDetailResponse)
						.andWithPrefix("data.number070.", number070)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();

		final PhoneInfoDetailResponse data = getData(result, PhoneInfoDetailResponse.class);

		log.info(data.toString());

		assertEquals("003", data.getOriginalNumber());
	}

//	@Test
	@Order(1)
	@Override
	protected void post() throws Exception {
		final PhoneInfoFormRequest form = new PhoneInfoFormRequest();
		form.setExtension("1234");  // 내선번호
		form.setNumber("00000000009");     // 개인070번호
		form.setPeer("00000009");      // 전화기아이디
		form.setPasswd("user!234");    // 전화기 암호
		form.setOutboundGw("gw.eicn");   // 게이트웨이
		form.setCid("15777711");         // CID번호
		form.setLocalPrefix("02");   // 지역번호
		form.setRecordType(RecordType.NONE_RECORDING.getCode());    //녹취여부
		form.setForwardWhen(ForwardWhen.NONE.getCode());    // 착신전환여부
		form.setForwardKind(""); //
		form.setForwardNum("");     // forwardWhen true 착신할전화번호
		form.setPrevent("");       // 금지프리픽스
		form.setOriginalNumber("002"); // 번호이동원번호

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(form))
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isCreated())
				.andDo(document.document(
						requestFields(phoneInfoFormRequest),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").type(JsonFieldType.NULL).description("실제 반환될 데이터").optional(),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				))
				.andReturn();
	}

//	@Test
	@Order(2)
	@Override
	protected void put() throws Exception {
		final PhoneInfoUpdateFormRequest form = new PhoneInfoUpdateFormRequest();
		form.setExtension("0168");  // 내선번호
		form.setNumber("07075498130");     // 개인070번호
		form.setPeer("75498130");      // 전화기아이디
		form.setPasswd("javaworld00!!");    // 전화기 암호
		form.setOutboundGw("gw.eicn");   // 게이트웨이
		form.setCid("15777711");         // CID번호
		form.setLocalPrefix("02");   // 지역번호
		form.setRecordType(RecordType.NONE_RECORDING.getCode());    //녹취여부
		form.setForwardWhen(ForwardWhen.NONE.getCode());    // 착신전환여부
		form.setForwardKind(""); //
		form.setForwardNum("");     // forwardWhen true 착신할전화번호
		form.setPrevent("");       // 금지프리픽스
		form.setOriginalNumber("003");
		form.setOldPeer("75498130");

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/{peer}", "75498130")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(form))
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						pathParameters(
								parameterWithName("peer").description("전화기아이디")
						),
						requestFields(phoneInfoUpdateFormRequest),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").type(JsonFieldType.NUMBER).description("실제 반환될 데이터").optional(),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				))
				.andReturn();
	}

//	@Test
	@Order(5)
	@Override
	protected void delete() throws Exception {
		peer = "75498130";
		this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{peer}", peer)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("result").value("success"))
				.andDo(document.document(
						pathParameters(
								parameterWithName("peer").description("전화기아이디")
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").type(JsonFieldType.NULL).description(""),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				))
				.andReturn();

		assertNull(phoneInfoRepository.findOne(peer));
	}

	//	@Test
	protected void extension() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/extensions")
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.param("extension", ""))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						requestParameters(
								parameterWithName("extension").description("내선전화번호").optional()
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
								.and(fieldWithPath("data").type(JsonFieldType.ARRAY).description("내선번호 목록").optional())
								.andWithPrefix("data.[]", summaryPhoneInfoResponse)
								.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();

		final List<SummaryPhoneInfoResponse> summaryPhoneInfoResponses = listData(result, SummaryPhoneInfoResponse.class);
		for (SummaryPhoneInfoResponse summaryPhoneInfoRespons : summaryPhoneInfoResponses) {
			log.info(summaryPhoneInfoRespons.toString());
		}
	}
}
