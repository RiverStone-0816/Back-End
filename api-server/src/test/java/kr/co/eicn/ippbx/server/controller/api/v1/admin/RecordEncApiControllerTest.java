package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.Number_070;
import kr.co.eicn.ippbx.server.model.dto.eicn.RecordEncKeyResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.RecordEncKeySummaryResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.SoundDetailResponse;
import kr.co.eicn.ippbx.server.model.enums.EncType;
import kr.co.eicn.ippbx.server.model.form.NumberTypeChangeRequest;
import kr.co.eicn.ippbx.server.model.form.RecordEncFormRequest;
import kr.co.eicn.ippbx.server.model.form.RecordEncKeyFormRequest;
import kr.co.eicn.ippbx.server.util.page.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class RecordEncApiControllerTest extends BaseControllerTest {
	private final String TEST_URL = "/api/v1/admin/record/file/enc";
	private static Integer id;

	private FieldDescriptor[] recordEncKeySummaryResponse = new FieldDescriptor[]{
			fieldWithPath("id").type(JsonFieldType.NUMBER).description("아이디"),
			fieldWithPath("createTime").type(JsonFieldType.STRING).description("암호키 적용시간"),
			fieldWithPath("encKey").type(JsonFieldType.STRING).description("암호키")
	};

	private FieldDescriptor[] recordEncFormRequest = new FieldDescriptor[]{
			fieldWithPath("encType").type(JsonFieldType.STRING).description("암호화 방식")
	};

	private FieldDescriptor[] recordEncKeyFormRequest = new FieldDescriptor[]{
			fieldWithPath("applyDate").type(JsonFieldType.STRING).description("암호키 적용시간(yyyy-MM-dd HH:mm:ss)"),
			fieldWithPath("encKey").type(JsonFieldType.STRING).description("암호키")
	};

	private FieldDescriptor[] recordEncKeyResponse = new FieldDescriptor[]{
			fieldWithPath("id").type(JsonFieldType.NUMBER).description("아이디"),
			fieldWithPath("createTime").type(JsonFieldType.STRING).description("암호키 적용시간"),
			fieldWithPath("encKey").type(JsonFieldType.STRING).description("암호키")
	};

	private FieldDescriptor[] recordEnc = new FieldDescriptor[]{
			fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사 아이디"),
			fieldWithPath("enable").type(JsonFieldType.STRING).description("암호화방식 구분(N:사용안함, B:AES_256, Y:ZIP)"),
			fieldWithPath("keyId").type(JsonFieldType.NUMBER).description("").optional(),
			fieldWithPath("externPlayer").type(JsonFieldType.STRING).description("전용플레이어 사용유무")
	};

//	@Test
//	@Order(2)
	public void get() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
						.andWithPrefix("data.", recordEnc)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();
	}

	/**
	 * 녹취파일 암호화 추가
	 */
//	@Test
//	@Order(1)
	public void post() throws Exception {
		final RecordEncFormRequest form = new RecordEncFormRequest();
		form.setEncType(EncType.NONE.getCode());

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(form))
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isCreated())
				.andDo(document.document(
						requestFields(recordEncFormRequest),
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
//	@Order(4)
	public void pagination() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/key")
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
						.and(fieldWithPath("data.rows[]").type(JsonFieldType.ARRAY).description("암호키 목록").optional())
						.andWithPrefix("data.rows[]", recordEncKeySummaryResponse)
						.andWithPrefix("data.",
								fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지"),
								fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총갯수")
						)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();

//		final Pagination<RecordEncKeySummaryResponse> pagination = paginationData(result, RecordEncKeySummaryResponse.class);
//
//		for (RecordEncKeySummaryResponse row : pagination.getRows()) {
//			log.info(row.toString());
//		}
	}

//	@Test
//	@Order(2)
	public void get_key() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/key/{id}", 5)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						pathParameters(
								parameterWithName("id").description("아이디")
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
						.andWithPrefix("data.", recordEncKeyResponse)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();

		final RecordEncKeyResponse data = getData(result, RecordEncKeyResponse.class);
		log.info(data.toString());
	}

	/**
	 * 암호키 추가
	 */
//	@Test
	public void key_register() throws Exception {
		final RecordEncKeyFormRequest form = new RecordEncKeyFormRequest();
		form.setApplyDate("2020-03-11 11:11:11");
		form.setEncKey("eicn1234");

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL + "/key")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(form))
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isCreated())
				.andDo(document.document(
						requestFields(recordEncKeyFormRequest),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").type(JsonFieldType.NUMBER).description("등록된 아이디").optional(),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				))
				.andReturn();
	}

	/**
	 *
	 */
//	@Test
	public void update() throws Exception {
		final RecordEncKeyFormRequest form = new RecordEncKeyFormRequest();
		form.setEncKey("123312321312");
		form.setApplyDate("2020-03-12 11:11:11"); //2020-02-10 02:11:11

		this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/key/{id}", 15)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						pathParameters(
								parameterWithName("id").description("아이디")
						),
						requestFields(recordEncKeyFormRequest),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").type(JsonFieldType.NULL).description("NULL").optional(),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				))
				.andReturn();
	}
}
