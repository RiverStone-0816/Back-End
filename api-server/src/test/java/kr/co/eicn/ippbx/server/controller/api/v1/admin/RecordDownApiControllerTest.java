package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.server.model.dto.eicn.RecordDownSummaryResponse;
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

import java.util.HashMap;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class RecordDownApiControllerTest extends BaseControllerTest {
	private final String TEST_URL = "/api/v1/admin/record/history/record-multi-down";

	private FieldDescriptor[] recordDownSummaryResponse = new FieldDescriptor[]{
			fieldWithPath("seq").type(JsonFieldType.NUMBER).description("SEQUENCE KEY"),
			fieldWithPath("requestdate").type(JsonFieldType.STRING).description("실행일"),
			fieldWithPath("status").type(JsonFieldType.STRING).description("다운로드 준비상태(A:요청완료, B:준비중, C:준비완료)"),
			fieldWithPath("downName").type(JsonFieldType.STRING).description("다운로드명"),
			fieldWithPath("userName").type(JsonFieldType.STRING).description("실행자"),
			fieldWithPath("userid").type(JsonFieldType.STRING).description("실행자 아이디"),
			fieldWithPath("downFolder").type(JsonFieldType.STRING).description("파일명"),
			fieldWithPath("size").type(JsonFieldType.NUMBER).description("파일 사이즈(Byte)").optional()
	};

//	@Test
	@Order(1)
	protected void list() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.param("startDate", "2020-03-16")
				.param("endDate", "2020-03-17"))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document.document(
						requestParameters(
								parameterWithName("startDate").description("시작일").optional(),
								parameterWithName("endDate").description("종료일").optional()
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional())
								.and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("녹취다운목록").optional())
								.andWithPrefix("data.[]", recordDownSummaryResponse)
								.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				))
				.andReturn();

		final List<RecordDownSummaryResponse> recordDownSummaryResponses = listData(result, RecordDownSummaryResponse.class);
		for (RecordDownSummaryResponse recordDownSummaryRespons : recordDownSummaryResponses) {
			log.info(recordDownSummaryRespons.toString());
		}
	}

//	@Test
	@Order(3)
	public void delete() throws Exception {
		this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{seq}", 1)
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
	}

//	@Test
	@Order(2)
	public void resource() throws Exception {
		this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{seq}/resource", 1)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.param("token", getAuthToken()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						requestParameters(parameterWithName("token").description("토큰 키")),
						pathParameters(parameterWithName("seq").description("SEQUENCE KEY")),
						responseBody()
				))
				.andReturn();

	}
}
