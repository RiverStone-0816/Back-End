package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.model.dto.eicn.PersonSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryPhoneInfoResponse;
import kr.co.eicn.ippbx.model.enums.IdStatus;
import kr.co.eicn.ippbx.model.enums.IdType;
import kr.co.eicn.ippbx.model.enums.RecordingAuthorityType;
import kr.co.eicn.ippbx.model.form.PersonFormRequest;
import kr.co.eicn.ippbx.model.form.PersonFormUpdateRequest;
import kr.co.eicn.ippbx.model.form.PersonPasswordUpdateRequest;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class UserApiControllerTest extends BaseControllerTest {
	private final String TEST_URL = "/api/v1/admin/user/user";

	private FieldDescriptor[] personSummaryResponse = new FieldDescriptor[] {
			fieldWithPath("id").type(JsonFieldType.STRING).description("상담원 아이디"),
			fieldWithPath("idType").type(JsonFieldType.STRING).description("아이디 유형구분(J:마스터, A:슈퍼어드민, B:일반어드민, M:상담원)"),
			fieldWithPath("idName").type(JsonFieldType.STRING).description("한글이름").optional(),
			fieldWithPath("extension").type(JsonFieldType.STRING).description("내선전화번호").optional(),
			fieldWithPath("companyTrees").type(JsonFieldType.ARRAY).description("조직정보").optional(),
			fieldWithPath("listeningRecordingAuthority").type(JsonFieldType.STRING).description("녹취권한 듣기").optional(),
			fieldWithPath("downloadRecordingAuthority").type(JsonFieldType.STRING).description("녹취권한 다운").optional(),
			fieldWithPath("removeRecordingAuthority").type(JsonFieldType.STRING).description("녹취권한 삭제").optional(),
			fieldWithPath("isPds").type(JsonFieldType.STRING).description("PDS 사용여부").optional(),
			fieldWithPath("isStat").type(JsonFieldType.STRING).description("통계,모니터링,상담원연결여부").optional(),
			fieldWithPath("isTalk").type(JsonFieldType.STRING).description("상담톡 여부").optional(),
			fieldWithPath("isEmail").type(JsonFieldType.STRING).description("이메일상담 여부").optional()
	};

	private FieldDescriptor[] personSearchRequest = new FieldDescriptor[] {
	};

	private FieldDescriptor[] personDetailResponse = new FieldDescriptor[] {
			fieldWithPath("id").type(JsonFieldType.STRING).description("상담원 아이디"),
			fieldWithPath("idType").type(JsonFieldType.STRING).description("아이디 유형구분(J:마스터, A:슈퍼어드민, B:일반어드민, M:상담원)"),
			fieldWithPath("idName").type(JsonFieldType.STRING).description("한글이름").optional(),
			fieldWithPath("extension").type(JsonFieldType.STRING).description("내선전화번호").optional(),
			fieldWithPath("companyTrees").type(JsonFieldType.ARRAY).description("조직정보").optional(),
			fieldWithPath("listeningRecordingAuthority").type(JsonFieldType.STRING).description("녹취권한 듣기").optional(),
			fieldWithPath("downloadRecordingAuthority").type(JsonFieldType.STRING).description("녹취권한 다운").optional(),
			fieldWithPath("removeRecordingAuthority").type(JsonFieldType.STRING).description("녹취권한 삭제").optional(),
			fieldWithPath("isPds").type(JsonFieldType.STRING).description("PDS 사용여부").optional(),
			fieldWithPath("isStat").type(JsonFieldType.STRING).description("통계,모니터링,상담원연결여부").optional(),
			fieldWithPath("isTalk").type(JsonFieldType.STRING).description("상담톡 여부").optional(),
			fieldWithPath("isEmail").type(JsonFieldType.STRING).description("이메일상담 여부").optional(),

			fieldWithPath("groupCode").type(JsonFieldType.STRING).description("groupCode"),
			fieldWithPath("idStatus").type(JsonFieldType.VARIES).description("근무상태('': 정상근무, S:휴직, X:퇴직)").optional(),
			fieldWithPath("hpNumber").type(JsonFieldType.STRING).description("휴대전화번호").optional(),
			fieldWithPath("emailInfo").type(JsonFieldType.STRING).description("이메일").optional()
	};

	private FieldDescriptor[] personFormRequest = new FieldDescriptor[] {
			fieldWithPath("id").type(JsonFieldType.STRING).description("상담원 아이디"),
			fieldWithPath("idType").type(JsonFieldType.STRING).description("아이디 유형구분(J:마스터, A:슈퍼어드민, B:일반어드민, M:상담원)"),
			fieldWithPath("idName").type(JsonFieldType.STRING).description("한글이름"),
			fieldWithPath("password").type(JsonFieldType.STRING).description("패스워드 규칙:문자,숫자,특수문자의 조합으로 8~20자리"),
			fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드"),
			fieldWithPath("idStatus").type(JsonFieldType.VARIES).description("근무상태('': 정상근무, S:휴직, X:퇴직)").optional(),
			fieldWithPath("isPds").type(JsonFieldType.STRING).description("PDS 사용여부").optional(),
			fieldWithPath("isTalk").type(JsonFieldType.STRING).description("상담톡 여부").optional(),
			fieldWithPath("isEmail").type(JsonFieldType.STRING).description("이메일상담 여부").optional(),
			fieldWithPath("isStat").type(JsonFieldType.STRING).description("통계,모니터링,상담원연결여부").optional(),
			fieldWithPath("listeningRecordingAuthority").type(JsonFieldType.STRING).description("녹취권한 듣기").optional(),
			fieldWithPath("downloadRecordingAuthority").type(JsonFieldType.STRING).description("녹취권한 다운").optional(),
			fieldWithPath("removeRecordingAuthority").type(JsonFieldType.STRING).description("녹취권한 삭제").optional(),
			fieldWithPath("extension").type(JsonFieldType.STRING).description("내선전화번호").optional(),
			fieldWithPath("hpNumber").type(JsonFieldType.STRING).description("휴대전화번호"),
			fieldWithPath("emailInfo").type(JsonFieldType.STRING).description("이메일").optional()
	};
	private FieldDescriptor[] personFormUpdateRequest = new FieldDescriptor[]{
			fieldWithPath("idType").type(JsonFieldType.STRING).description("아이디 유형구분(J:마스터, A:슈퍼어드민, B:일반어드민, M:상담원)"),
			fieldWithPath("idName").type(JsonFieldType.STRING).description("한글이름"),
			fieldWithPath("password").type(JsonFieldType.STRING).description("패스워드 규칙:문자,숫자,특수문자의 조합으로 8~20자리"),
			fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드"),
			fieldWithPath("idStatus").type(JsonFieldType.STRING).description("근무상태('': 정상근무, S:휴직, X:퇴직)").optional(),
			fieldWithPath("isPds").type(JsonFieldType.STRING).description("PDS 사용여부").optional(),
			fieldWithPath("isTalk").type(JsonFieldType.STRING).description("상담톡 여부").optional(),
			fieldWithPath("isEmail").type(JsonFieldType.STRING).description("이메일상담 여부").optional(),
			fieldWithPath("isStat").type(JsonFieldType.STRING).description("통계,모니터링,상담원연결여부").optional(),
			fieldWithPath("listeningRecordingAuthority").type(JsonFieldType.STRING).description("녹취권한 듣기").optional(),
			fieldWithPath("downloadRecordingAuthority").type(JsonFieldType.STRING).description("녹취권한 다운").optional(),
			fieldWithPath("removeRecordingAuthority").type(JsonFieldType.STRING).description("녹취권한 삭제").optional(),
			fieldWithPath("extension").type(JsonFieldType.STRING).description("내선전화번호").optional(),
			fieldWithPath("hpNumber").type(JsonFieldType.STRING).description("휴대전화번호"),
			fieldWithPath("emailInfo").type(JsonFieldType.STRING).description("이메일").optional()
	};
	private FieldDescriptor[] personPasswordUpdateRequest = new FieldDescriptor[] {
			fieldWithPath("password").type(JsonFieldType.STRING).description("패스워드 규칙:문자,숫자,특수문자의 조합으로 8~20자리"),
			fieldWithPath("passwordConfirm").type(JsonFieldType.STRING).description("패스워드 확인"),
	};

	private FieldDescriptor[] summaryPhoneInfoResponse = new FieldDescriptor[] {
			fieldWithPath("extension").type(JsonFieldType.STRING).description("내선전화번호"),
			fieldWithPath("inUseIdName").type(JsonFieldType.STRING).description("사용중인 상담원명").optional()
	};

	private FieldDescriptor[] organizationSummaryResponse = new FieldDescriptor[] {
			fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드"),
			fieldWithPath("groupName").type(JsonFieldType.STRING).description("조직명"),
			fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("윗레벨의 코드를 포함한 코드의 나열(002_003_008)").optional(),
			fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("조직레벨")
	};

//	@Test
	@Override
	protected void pagination() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
					.param("page", "1")
					.param("limit", "10")
					.param("id", "")
					.param("idName", "")
					.param("extension", "")
					.param("groupCode", "")
					.param("idStatus", IdStatus.RETIRE.name())
					.param("order", "")
					.param("orderDirection", "")
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						requestParameters(
								parameterWithName("page").description("현재 페이지"),
								parameterWithName("limit").description("페이지 개수"),
								parameterWithName("id").description("아이디").optional(),
								parameterWithName("idName").description("성명").optional(),
								parameterWithName("extension").description("내선번호").optional(),
								parameterWithName("groupCode").description("조직코드").optional(),
								parameterWithName("order").description("정렬 필드(NAME:성명, GROUP:부서명, PDS:PDS적용여부, STAT:통계사용여부, TALK:상담톡여부)").optional(),
								parameterWithName("orderDirection").description("정렬 방향(ASC, DESC)").optional(),
								parameterWithName("idStatus").description("근무상태('': 정상근무, S:휴직, X:퇴직)").optional()
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
						.and(fieldWithPath("data").type(JsonFieldType.OBJECT).description("반환 될 데이터").optional())
						.and(fieldWithPath("data.rows[]").type(JsonFieldType.ARRAY).description("웹사용자 목록").optional())
						.andWithPrefix("data.rows[]", personSummaryResponse)
						.andWithPrefix("data.rows[].companyTrees[]", organizationSummaryResponse)
						.andWithPrefix("data.", fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지"),
								fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총갯수"))
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목ContextInfoApiControllerTest록").optional())
				))
				.andReturn();

		final Pagination<PersonSummaryResponse> summary = paginationData(result, PersonSummaryResponse.class);
		final List<PersonSummaryResponse> rows = summary.getRows();

		for (PersonSummaryResponse row : rows) {
			log.info("row {}", row.toString());
		}
	}

//	@Test
	@Override
	protected void get() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{id}", "user0790")
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						pathParameters(
								parameterWithName("id").description("상담원아이디")
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").type(JsonFieldType.OBJECT).description("웹사용자 상세조회").optional(),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
						.andWithPrefix("data.", personDetailResponse)
						.andWithPrefix("data.companyTrees[]", organizationSummaryResponse)
				))
				.andReturn();
	}

//	@Test
	@Override
	protected void post() throws Exception {
		final PersonFormRequest form = new PersonFormRequest();
		form.setId("user0888");          // 아이디
		form.setPassword("javaworld00!!"); // 패스워드
		form.setIdName("이상욱");     // 성명
		form.setIdType(IdType.USER.getCode()); // 아이디유형구분
		form.setGroupCode("0007"); // 조직코드
		form.setExtension("0789"); // 내선번호
		form.setIsPds("N"); // PDS여부
		form.setIsTalk("N"); // 상담톡여부
		form.setIsEmail("N"); // 이메일상담여부
		form.setIsStat("N"); // 통계, 모니터링, 상담화면 여부
		form.setListeningRecordingAuthority(RecordingAuthorityType.NONE.getCode()); // 녹취권한 듣기
		form.setDownloadRecordingAuthority(RecordingAuthorityType.NONE.getCode()); // 녹취권한 다운
		form.setRemoveRecordingAuthority(RecordingAuthorityType.NONE.getCode()); // 녹취권한 삭제
		form.setHpNumber(""); // 휴대전화번호
		form.setEmailInfo(""); // 이메일

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andDo(print())
				.andExpect(status().isCreated())
				.andDo(document.document(
						requestFields(personFormRequest),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").type(JsonFieldType.NULL).description("").optional(),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				))
				.andReturn();
	}

//	@Test
	protected void update_password() throws Exception {
		final PersonPasswordUpdateRequest form = new PersonPasswordUpdateRequest();
		form.setPassword("orange90!!");
		form.setPasswordConfirm("orange90!!");

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.patch(TEST_URL + "/{id}/password", "user0888")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andDo(print())
				.andDo(document.document(
						pathParameters(
								parameterWithName("id").description("상담원아이디")
						),
						requestFields(personPasswordUpdateRequest),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").type(JsonFieldType.NULL).description("").optional(),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				))
				.andExpect(status().isOk())
				.andReturn();
	}

//	@Test
	@Override
	protected void put() throws Exception {
		final PersonFormUpdateRequest form = new PersonFormUpdateRequest();
//		form.setId("user0777");          // 아이디
		form.setPassword("javaworld00!!"); // 패스워드
		form.setIdName("이상욱");     // 성명
		form.setIdType(IdType.USER.getCode()); // 아이디유형구분
		form.setGroupCode("0007"); // 조직코드
		form.setExtension("0789"); // 내선번호
		form.setIsPds("N"); // PDS여부
		form.setIsTalk("N"); // 상담톡여부
		form.setIsEmail("N"); // 이메일상담여부
		form.setIsStat("N"); // 통계, 모니터링, 상담화면 여부
		form.setListeningRecordingAuthority(RecordingAuthorityType.NONE.getCode()); // 녹취권한 듣기
		form.setDownloadRecordingAuthority(RecordingAuthorityType.NONE.getCode()); // 녹취권한 다운
		form.setRemoveRecordingAuthority(RecordingAuthorityType.NONE.getCode()); // 녹취권한 삭제
		form.setHpNumber("01030068249"); // 휴대전화번호
		form.setEmailInfo("sexyuck@gmail.com"); // 이메일

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/{id}", "user0888")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andDo(print())
				.andDo(document.document(
						pathParameters(
								parameterWithName("id").description("상담원아이디")
						),
						requestFields(personFormUpdateRequest),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").type(JsonFieldType.NULL).description("").optional(),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				))
				.andExpect(status().isOk())
				.andReturn();
	}

//	@Test
	@Override
	protected void delete() throws Exception {
		this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{id}", "user0888")
				.with(new JwtRequestPostProcessor()))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("result").value("success"))
				.andDo(document.document(
						pathParameters(
								parameterWithName("id").description("상담원아이디")
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
	protected void duplicate() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{id}/duplicate", "user0888")
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						pathParameters(
								parameterWithName("id").description("상담원아이디")
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
						.and(fieldWithPath("data").type(JsonFieldType.NULL).description("").optional())
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();
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
