package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class CompanyApiControllerTest extends BaseControllerTest {
	private final String TEST_URL = "/api/company";

	private FieldDescriptor[] companyLicenceEntity = new FieldDescriptor[] {
			fieldWithPath("recordLicense").type(JsonFieldType.OBJECT).description("레코드 라이센스 정보"),
			fieldWithPath("pdsLicense").type(JsonFieldType.OBJECT).description("PDS 라이센스 정보"),
			fieldWithPath("statLicence").type(JsonFieldType.OBJECT).description("통계,모니터링,상담원연결여부 정보"),
			fieldWithPath("talkLicense").type(JsonFieldType.OBJECT).description("상담톡 라이센스 정보"),
			fieldWithPath("emailLicense").type(JsonFieldType.OBJECT).description("이메일상담여부 라이센스 정보"),
	};

	private FieldDescriptor[] licenseInfo = new FieldDescriptor[] {
			fieldWithPath("licence").type(JsonFieldType.NUMBER).description("라이센스 수"),
			fieldWithPath("currentLicence").type(JsonFieldType.NUMBER).description("현재 라이센스 수"),
	};

	private FieldDescriptor[] companyInfo = new FieldDescriptor[] {
			fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사 아이디"),
			fieldWithPath("companyName").type(JsonFieldType.STRING).description("고객사명"),
			fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
			fieldWithPath("regDate").type(JsonFieldType.STRING).description("등록일"),
			fieldWithPath("damdang").type(JsonFieldType.STRING).description("담당자"),
			fieldWithPath("contact").type(JsonFieldType.STRING).description("연락처"),
			fieldWithPath("extensionNum").type(JsonFieldType.NUMBER).description("내선자리수 (3 또는 4)"),
			fieldWithPath("maxTreeLevel").type(JsonFieldType.NUMBER).description("추가 회사 조직도의 MAX 레벨설정"),
			fieldWithPath("numLicense").type(JsonFieldType.NUMBER).description("번호 회선 수"),
			fieldWithPath("ivrLicense").type(JsonFieldType.NUMBER).description("ivr 회선 수"),
			fieldWithPath("recordLicense").type(JsonFieldType.NUMBER).description("녹취 회선 수"),
			fieldWithPath("pdsLicense").type(JsonFieldType.NUMBER).description("pds 가능 상담원 수"),
			fieldWithPath("statLicense").type(JsonFieldType.NUMBER).description("통계, 모니터링, 상담원연결, 가능 상담원 수"),
			fieldWithPath("talkLicense").type(JsonFieldType.NUMBER).description(""),
			fieldWithPath("emailLicense").type(JsonFieldType.NUMBER).description(""),
			fieldWithPath("uiType").type(JsonFieldType.STRING).description("디자인색상"),
			fieldWithPath("appDir").type(JsonFieldType.STRING).description("context 디렉터리"),
			fieldWithPath("language").type(JsonFieldType.STRING).description("사용 언어"),
			fieldWithPath("service").type(JsonFieldType.STRING).description("사용 서비스"),
			fieldWithPath("information").type(JsonFieldType.STRING).description("비고"),
			fieldWithPath("etc1").type(JsonFieldType.STRING).description("웹보이스 키"),
			fieldWithPath("etc2").type(JsonFieldType.STRING).description("웹보이스 정보"),
			fieldWithPath("etc3").type(JsonFieldType.STRING).description("웹보이스 정보"),
	};

//	@Test
	protected void list() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/licence")
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
						.and(fieldWithPath("data").type(JsonFieldType.OBJECT).description("반환 될 데이터").optional())
						.andWithPrefix("data.", companyLicenceEntity)
						.andWithPrefix("data.recordLicense.", licenseInfo)
						.andWithPrefix("data.pdsLicense.", licenseInfo)
						.andWithPrefix("data.statLicence.", licenseInfo)
						.andWithPrefix("data.talkLicense.", licenseInfo)
						.andWithPrefix("data.emailLicense.", licenseInfo)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러").optional())
				))
				.andReturn();
	}

//	@Test
	protected void getInfo() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/info")
		.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러").optional()
						)
								.and(fieldWithPath("data").type(JsonFieldType.OBJECT).description("반환 될 데이터").optional())
								.andWithPrefix("data.", companyInfo)
				))
				.andReturn();
	}
}
