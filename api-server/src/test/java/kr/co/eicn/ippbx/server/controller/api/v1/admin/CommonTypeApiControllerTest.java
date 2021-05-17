package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.server.model.entity.eicn.CommonFieldEntity;
import kr.co.eicn.ippbx.server.model.entity.eicn.CommonTypeEntity;
import kr.co.eicn.ippbx.server.model.enums.Bool;
import kr.co.eicn.ippbx.server.model.enums.CommonTypeKind;
import kr.co.eicn.ippbx.server.model.form.CommonFieldFormRequest;
import kr.co.eicn.ippbx.server.model.form.CommonTypeFormRequest;
import kr.co.eicn.ippbx.server.model.form.CommonTypeUpdateFormRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class CommonTypeApiControllerTest extends BaseControllerTest {
	private final String TEST_URL = "/api/v1/admin/application/type";

	private FieldDescriptor[] commonTypeEntity = new FieldDescriptor[] {
			fieldWithPath("seq").type(JsonFieldType.NUMBER).description("SEQUENCE KEY"),
			fieldWithPath("name").type(JsonFieldType.STRING).description("유형명"),
			fieldWithPath("kind").type(JsonFieldType.STRING).description("유형구분(CST: 고객DB유형, CON:연동DB유형, RS:상담결과유형, MAINDB:고객정보유형, PRV: 프리뷰유형, PDS:PDS유형, CUSTOM_TALK:TALK유형)"),
			fieldWithPath("status").type(JsonFieldType.STRING).description("유형상태(U:사용중, D:삭제)"),
			fieldWithPath("etc").type(JsonFieldType.STRING).description("유형정보"),
			fieldWithPath("purpose").type(JsonFieldType.STRING).description("유형 용도(PCC: PDS[상담원 연결], PIC:PDS[IVR연결], SVY:설문조사, VDU:VOC[DB업로드], VCS:VOC[상담화면], ADU:ACS[DB업로드], ACS:ACS[상담화면])").optional(),
			fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사 아이디"),
			fieldWithPath("fields").type(JsonFieldType.ARRAY).description("유형필드 목록").optional()
	};

	private FieldDescriptor[] commonFieldEntity = new FieldDescriptor[] {
			fieldWithPath("seq").type(JsonFieldType.NUMBER).description("SEQUENCE KEY"),
			fieldWithPath("type").type(JsonFieldType.NUMBER).description("유형 참조 키"),
			fieldWithPath("fieldId").type(JsonFieldType.STRING).description("필드아이디"),
			fieldWithPath("fieldName").type(JsonFieldType.STRING).description("필드명"),
			fieldWithPath("fieldType").type(JsonFieldType.STRING).description("필드타입"),
			fieldWithPath("fieldUse").type(JsonFieldType.STRING).description("필드사용여부(D:미사용, U:사용)"),
			fieldWithPath("fieldInfo").type(JsonFieldType.STRING).description("기본필드명"),
			fieldWithPath("fieldSize").type(JsonFieldType.NUMBER).description("필드사이즈"),
			fieldWithPath("isneed").type(JsonFieldType.STRING).description("필수사용여부"),
			fieldWithPath("isdisplay").type(JsonFieldType.STRING).description("노출여부"),
			fieldWithPath("isdisplayList").type(JsonFieldType.STRING).description("리스트 노출여부"),
			fieldWithPath("issearch").type(JsonFieldType.STRING).description("검색여부"),
			fieldWithPath("displaySeq").type(JsonFieldType.NUMBER).description("순서"),
			fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사아이디"),
			fieldWithPath("iskey").type(JsonFieldType.STRING).description("필수키 여부"),
			fieldWithPath("isdefault").type(JsonFieldType.STRING).description("고정여부"),
			fieldWithPath("isenc").type(JsonFieldType.STRING).description("암호화여부"),
			fieldWithPath("codes").type(JsonFieldType.ARRAY).description("").optional().ignored(),
	};

	private FieldDescriptor[] commonTypeFormRequest = new FieldDescriptor[] {
			fieldWithPath("name").type(JsonFieldType.STRING).description("유형명"),
			fieldWithPath("kind").type(JsonFieldType.STRING).description("유형구분(CST: 고객DB유형, CON: 연동DB유형, RS: 상담결과유형)"),
			fieldWithPath("etc").type(JsonFieldType.STRING).description("유형정보").optional()
	};

	private FieldDescriptor[] commonTypeUpdateFormRequest = new FieldDescriptor[]{
			fieldWithPath("name").type(JsonFieldType.STRING).description("유형명"),
			fieldWithPath("kind").type(JsonFieldType.STRING).description("유형구분(CST: 고객DB유형, CON: 연동DB유형, RS: 상담결과유형)"),
			fieldWithPath("etc").type(JsonFieldType.STRING).description("유형정보").optional(),
			fieldWithPath("purpose").type(JsonFieldType.STRING).description("유형 용도(PCC: PDS[상담원 연결], PIC:PDS[IVR연결], SVY:설문조사, VDU:VOC[DB업로드], VCS:VOC[상담화면], ADU:ACS[DB업로드], ACS:ACS[상담화면])").optional(),
			fieldWithPath("fieldFormRequests").type(JsonFieldType.ARRAY).description("필드등록 정보 목록").optional()
	};

	private FieldDescriptor[] commonFieldFormRequest = new FieldDescriptor[]{
			fieldWithPath("id").type(JsonFieldType.STRING).description("기본필드 아이디"),
			fieldWithPath("fieldName").type(JsonFieldType.STRING).description("필드명"),
			fieldWithPath("fieldSize").type(JsonFieldType.NUMBER).description("필드사이즈(500자이내)"),
			fieldWithPath("isneed").type(JsonFieldType.VARIES).description("필수여부(Y:N)"),
			fieldWithPath("isdisplay").type(JsonFieldType.VARIES).description("상담원 노출여부(Y:N)"),
			fieldWithPath("isdisplayList").type(JsonFieldType.VARIES).description("리스트 노출여부(Y:N)"),
			fieldWithPath("issearch").type(JsonFieldType.VARIES).description("검색여부(Y:N)")
	};

	private FieldDescriptor[] commonBasicField = new FieldDescriptor[]{
			fieldWithPath("seq").type(JsonFieldType.NUMBER).description("SEQUENCE KEY"),
			fieldWithPath("id").type(JsonFieldType.STRING).description("아이디"),
			fieldWithPath("name").type(JsonFieldType.STRING).description("필드명"),
			fieldWithPath("type").type(JsonFieldType.STRING).description("필드타입"),
			fieldWithPath("serviceKind").type(JsonFieldType.VARIES).description("서비스종류(SC:구축형, CC:클컨라이트)"),
			fieldWithPath("info").type(JsonFieldType.VARIES).description("유형 서비스"),
			fieldWithPath("isdefault").type(JsonFieldType.VARIES).description("고정여부"),
			fieldWithPath("fieldSize").type(JsonFieldType.NUMBER).description("필드 사이즈")
	};

	/**
	 *  유형 목록조회
	 */
//	@Test
	public void list() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.param("kind", "CST")) // 유형구분 CST: 고객DB유형, CON: 연동DB유형, RS: 상담결과유형
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						requestParameters(
								parameterWithName("kind").description("유형구분(CST: 고객DB유형, CON: 연동DB유형, RS: 상담결과유형)")
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").description("실제 반환될 데이터").optional()
						)
						.andWithPrefix("data.[]", commonTypeEntity)
						.and(fieldWithPath("data.[].fields[]").type(JsonFieldType.ARRAY).description("유형 필드 목록").optional())
						.andWithPrefix("data.[].fields[]", commonFieldEntity)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();

		final List<CommonTypeEntity> rows = listData(result, CommonTypeEntity.class);

		for (CommonTypeEntity row : rows) {
			log.info("유형명: {}", row.getName());
			for (CommonFieldEntity field : row.getFields()) {
				log.info("--> 필드명:{}, 기본필드명:{}, 필수[키]:{}, 보이기:{}, 리스트:{}, 검색:{}"
						, field.getFieldInfo()
						, field.getFieldName()
						, field.getIsneed()
						, field.getIsneed()
						, field.getIsdisplayList()
						, field.getIssearch());
			}
		}
	}

	/**
	 *  유형 상세조회
	 */
//	@Test
	public void get() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{seq}", 7)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						pathParameters(
								parameterWithName("seq").description("유형 키")
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").description("실제 반환될 데이터").optional()
						)
						.andWithPrefix("data.", commonTypeEntity)
						.and(fieldWithPath("data.fields[]").type(JsonFieldType.ARRAY).description("유형 필드 목록").optional())
						.andWithPrefix("data.fields[]", commonFieldEntity)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();
	}

	/**
	 *  유형 추가
	 */
//	@Test
	public void post() throws Exception {
		final CommonTypeFormRequest form = new CommonTypeFormRequest();
		form.setName("고객3");
		form.setKind(CommonTypeKind.MAIN_DB.getCode());

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andDo(print())
				.andExpect(status().isCreated())
				.andDo(document.document(
						requestFields(
							fieldWithPath("name").type(JsonFieldType.STRING).description("유형명"),
							fieldWithPath("kind").type(JsonFieldType.STRING).description("유형타입(CST: 고객DB유형, CON: 연동DB유형, RS: 상담결과유형)"),
							fieldWithPath("etc").type(JsonFieldType.STRING).description("옵션정보").optional()
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").description("NULL").optional()
						)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();
	}

	/**
	 *  유형 수정
	 */
//	@Test
	public void put() throws Exception {
		final CommonTypeUpdateFormRequest form = new CommonTypeUpdateFormRequest();
		form.setName("EICN상담결과 테스트");
		form.setKind(CommonTypeKind.PDS.getCode());

		final CommonFieldFormRequest fieldFormRequest = new CommonFieldFormRequest();
		fieldFormRequest.setId("INT_3");    // COMMON_BASIC_FIELD.ID
		fieldFormRequest.setFieldName("AGE");  // 필드명
		fieldFormRequest.setIsneed("N"); // 필수여부
		fieldFormRequest.setIsdisplay("Y"); // 상담원 보여줌
		fieldFormRequest.setIsdisplayList("N"); // 리스트보여줌
		fieldFormRequest.setIssearch("Y"); // 검색
		fieldFormRequest.setFieldSize(200); // 문자열크기

		final CommonFieldFormRequest fieldFormRequest1 = new CommonFieldFormRequest();

		fieldFormRequest1.setId("CODE_10");    // COMMON_BASIC_FIELD.ID
		fieldFormRequest1.setFieldName("공통코드");  // 필드명
		fieldFormRequest1.setIsneed("N"); // 필수여부
		fieldFormRequest1.setIsdisplay("Y"); // 상담원 보여줌
		fieldFormRequest1.setIsdisplayList("N"); // 리스트보여줌
		fieldFormRequest1.setIssearch("Y"); // 검색
		fieldFormRequest1.setFieldSize(500); // 문자열크기

		final CommonFieldFormRequest fieldFormRequest2 = new CommonFieldFormRequest();

		fieldFormRequest2.setId("NUMBER_1");    // COMMON_BASIC_FIELD.ID
		fieldFormRequest2.setFieldName("전화번호1");  // 필드명
		fieldFormRequest2.setIsneed("Y"); // 필수여부
		fieldFormRequest2.setIsdisplay("Y"); // 상담원 보여줌
		fieldFormRequest2.setIsdisplayList("Y"); // 리스트보여줌
		fieldFormRequest2.setIssearch("Y"); // 검색
		fieldFormRequest2.setFieldSize(1); // 문자열크기

		final CommonFieldFormRequest fieldFormRequest3 = new CommonFieldFormRequest();

		fieldFormRequest3.setId("STRING_1");    // COMMON_BASIC_FIELD.ID
		fieldFormRequest3.setFieldName("고객명");  // 필드명
		fieldFormRequest3.setIsneed("Y"); // 필수여부
		fieldFormRequest3.setIsdisplay("Y"); // 상담원 보여줌
		fieldFormRequest3.setIsdisplayList("Y"); // 리스트보여줌
		fieldFormRequest3.setIssearch("Y"); // 검색
		fieldFormRequest3.setFieldSize(100); // 문자열크

		form.setFieldFormRequests(Arrays.asList(fieldFormRequest2, fieldFormRequest3));

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/{seq}", 27)
				.contentType(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						pathParameters(
								parameterWithName("seq").description("유형 키")
						),
						requestFields(commonTypeUpdateFormRequest)
						.andWithPrefix(".", commonTypeUpdateFormRequest)
						.andWithPrefix(".fieldFormRequests[]", commonFieldFormRequest),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").description("실제 반환될 데이터").optional()
						)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();
	}

	/**
	 *  유형 삭제
	 */
//	@Test
	public void update_status() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.patch(TEST_URL + "/{seq}", 7)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						pathParameters(
								parameterWithName("seq").description("유형 키")
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").type(JsonFieldType.NULL).description("'"),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				))
				.andReturn();
	}

	/**
	 *  필드 순서 변경
	 */
//	@Test
	public void move() throws Exception {
		final List<Integer> integers = Arrays.asList(90, 85);
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/{seq}/move", 7)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.param("sequences", "85,90"))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						pathParameters(
								parameterWithName("seq").description("유형 키")
						),
						requestParameters(
							parameterWithName("sequences").description("필드 키 목록")
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").description("NULL").optional()
						)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();
	}

	/**
	 *  유형 기본 필드 목록조회
	 */
//	@Test
	public void getBasicFields() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/basic-field")
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.param("kind", "CST"))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						requestParameters(parameterWithName("kind").description("유형구분")),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").description("실제 반환될 데이터").optional()
						)
						.andWithPrefix("data.[]", commonBasicField)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();
	}
}
