package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.server.model.dto.eicn.DiskResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.FileSummaryResponse;
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

import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class RecordManagerApiControllerTest extends BaseControllerTest {
	private final String TEST_URL = "/api/v1/admin/record/file";

	private FieldDescriptor[] fileSummaryResponse = new FieldDescriptor[]{
			fieldWithPath("fileName").type(JsonFieldType.STRING).description("파일명"),
			fieldWithPath("size").type(JsonFieldType.NUMBER).description("파일 사이즈"),
	};

	private FieldDescriptor[] diskResponse = new FieldDescriptor[]{
			fieldWithPath("used").type(JsonFieldType.STRING).description("사용중인 디스크"),
			fieldWithPath("avail").type(JsonFieldType.STRING).description("사용가능 디스크"),
			fieldWithPath("use").type(JsonFieldType.STRING).description("사용비율")
	};

//	@Test
	@Order(1)
	protected void list() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andExpect(status().isOk())
				.andDo(document.document(
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional())
								.and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("녹취파일 목록").optional())
								.andWithPrefix("data.[]", fileSummaryResponse)
								.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				))
				.andReturn();

		final List<FileSummaryResponse> fileSummaryResponses = listData(result, FileSummaryResponse.class);
		for (FileSummaryResponse fileSummaryRespons : fileSummaryResponses) {
			log.info(fileSummaryRespons.toString());
		}
	}

//	@Test
	@Order(3)
	public void delete() throws Exception {
		this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{fileName}", "2019_12_14.tar.gz")
				.with(new JwtRequestPostProcessor()))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("result").value("success"))
				.andDo(document.document(
						pathParameters(
								parameterWithName("fileName").description("파일명")
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
		this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/resource")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.param("fileName", "2019_12_14.tar.gz")
				.param("token", getAuthToken()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						requestParameters(
								parameterWithName("fileName").description("파일명"),
								parameterWithName("token").description("토큰 키")
						),
						responseBody()
				))
				.andReturn();

	}

//	@Test
	public void disk() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/disk")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional())
								.and(fieldWithPath("data.").type(JsonFieldType.OBJECT).description("디스크 정보"))
								.andWithPrefix("data.", diskResponse)
								.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				))
				.andReturn();

		final DiskResponse data = getData(result, DiskResponse.class);
		log.info("data {}", data);
	}
}
