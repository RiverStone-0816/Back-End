package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.model.dto.eicn.MohDetailResponse;
import kr.co.eicn.ippbx.server.service.StorageService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.fileUpload;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
public class MohListControllerTest extends BaseControllerTest {
	private final String TEST_URL = "/api/v1/admin/sounds/ring-back-tone";
	private static String categoryCode = "COLOR_22";

	@Autowired
	private StorageService fileStorageSystemService;

	private FieldDescriptor[] mohListSummaryResponse = new FieldDescriptor[] {
			fieldWithPath("category").type(JsonFieldType.STRING).description("음원아이디"),
			fieldWithPath("mohName").type(JsonFieldType.STRING).description("컬러링명"),
			fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사 아이디"),
			fieldWithPath("companySeq").type(JsonFieldType.NUMBER).description("고객사 SEQUENCE KEY"),
			fieldWithPath("directory").type(JsonFieldType.STRING).description("디렉토리경로"),
			fieldWithPath("fileName").type(JsonFieldType.STRING).description("파일명")
	};

	private FieldDescriptor[] mohListRequest = new FieldDescriptor[] {
		fieldWithPath("category").type(JsonFieldType.STRING).description("음원아이디"),
		fieldWithPath("mohName").type(JsonFieldType.STRING).description("컬러링명"),
		fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사 아이디"),
		fieldWithPath("companySeq").type(JsonFieldType.NUMBER).description("고객사 SEQUENCE KEY"),
		fieldWithPath("directory").type(JsonFieldType.STRING).description("디렉토리경로"),
		fieldWithPath("fileName").type(JsonFieldType.STRING).description("파일명")
	};

//	@Test
	@Order(4)
	public void pagination() throws Exception {
		this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.param("page", "1")
				.param("limit", "10")
				.param("mohName", ""))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						requestParameters(
								parameterWithName("page").description("현재 페이지"),
								parameterWithName("limit").description("페이지 개수"),
								parameterWithName("mohName").description("컬러링명").optional()
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
						.and(fieldWithPath("data.rows[]").type(JsonFieldType.ARRAY).description("반환될 데이터").optional())
						.andWithPrefix("data.rows[]", mohListSummaryResponse)
						.andWithPrefix("data.",
							fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지"),
							fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총갯수")
						)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();
	}

//	@Test
	@Order(1)
	protected void post() throws Exception {
//		final Resource resource = fileStorageSystemService.loadAsResource("D:/download", "ars_lunch_140331.wav");
//		MockMultipartFile file = new MockMultipartFile("file", "ars_lunch_140331.wav", "audio/x-wav", IOUtils.toByteArray(resource.getURI()));
		MockMultipartFile file = new MockMultipartFile("file", "wave.wav", "audio/x-wav", "<wave.wav>".getBytes());

		final MvcResult result = this.mockMvc.perform(fileUpload(TEST_URL).file(file)
				.param("mohName", "유플러스 대표번호1")
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isCreated())
				.andDo(document.document(
						requestParts(
								partWithName("file").description("음원파일 업로드")
						),
						requestParameters(
								parameterWithName("mohName").description("음원명")
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").type(JsonFieldType.STRING).description("등록된 카테고리 값").optional(),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				))
				.andReturn();

		final String data = getData(result, String.class);
		log.info("categoryCode {}", data);
		categoryCode = data;
	}

//	@Test
	@Order(2)
	protected void get() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{category}", categoryCode)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						pathParameters(
								parameterWithName("category").description("음원아이디")
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
						.and(fieldWithPath("data").type(JsonFieldType.OBJECT).description("컬러링 정보"))
						.andWithPrefix("data.", mohListRequest)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();

		final MohDetailResponse data = getData(result, MohDetailResponse.class);
		log.info(data.toString());
	}

//	@Test
	@Order(4)
	@Override
	protected void delete() throws Exception {
		this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{category}", categoryCode)
				.with(new JwtRequestPostProcessor()))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("result").value("success"))
				.andDo(document.document(
						pathParameters(
								parameterWithName("category").description("음원아이디")
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
	@Order(3)
	public void resource() throws Exception {
		this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{category}/resource", "3")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.param("token", getAuthToken()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						pathParameters(parameterWithName("category").description("음원아이디")),
						requestParameters(parameterWithName("token").description("토큰 키")),
						responseBody()
				))
				.andReturn();
	}
}
