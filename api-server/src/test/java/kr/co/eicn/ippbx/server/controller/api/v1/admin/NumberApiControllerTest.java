package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.server.repository.NumberRepositoryTest;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.Number_070;
import kr.co.eicn.ippbx.model.dto.eicn.NumberSummaryResponse;
import kr.co.eicn.ippbx.model.form.NumberTypeChangeRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class NumberApiControllerTest extends BaseControllerTest {
	private final String TEST_URL = "/api/v1/admin/user/tel/number";
	private static String number = "";
	private static Byte type;

	@Autowired
	private NumberRepositoryTest repositoryTest;

	private final FieldDescriptor[] numberSummary = new FieldDescriptor[] {
			fieldWithPath("number").type(JsonFieldType.STRING).description("070번호"),
			fieldWithPath("type").type(JsonFieldType.NUMBER).description("번호타입(0:헌트, 1:개인, 2:대표, 3:회의)"),
			fieldWithPath("svcCid").type(JsonFieldType.STRING).description("CID").optional(),
			fieldWithPath("useService").type(JsonFieldType.STRING).description("사용중인서비스").optional(),
			fieldWithPath("isSchedule").type(JsonFieldType.STRING).description("일정사용여부").optional(),
			fieldWithPath("hostName").type(JsonFieldType.STRING).description("소속교환기"),
			fieldWithPath("isTypeChange").type(JsonFieldType.STRING).description("번호타입 변경가능여부"),
			fieldWithPath("originalNumber").type(JsonFieldType.STRING).description("번호이동원번호").optional()
	};

	private FieldDescriptor[] summaryNumber070Response = new FieldDescriptor[] {
			fieldWithPath("number").type(JsonFieldType.STRING).description("070번호"),
			fieldWithPath("type").type(JsonFieldType.NUMBER).description("번호타입(0:헌트, 1:개인, 2:대표, 3:회의)"),
			fieldWithPath("hanName").type(JsonFieldType.STRING).description("QUEUE 한글이름").optional()
	};

//	@Test
	@Order(1)
	public void list() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.param("type", "0"))
				.andExpect(status().isOk())
				.andDo(document.document(
						requestParameters(
								parameterWithName("type").description("번호타입")
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
						.and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("번호목록").optional())
						.andWithPrefix("data.[]", numberSummary)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();

		final List<NumberSummaryResponse> numberSummaryResponses = listData(result, NumberSummaryResponse.class);

		for (NumberSummaryResponse numberSummaryResponse : numberSummaryResponses) {
			log.info(numberSummaryResponse.toString());
		}

		if (numberSummaryResponses.size() > 0) {
			number = numberSummaryResponses.get(0).getNumber();
			type = numberSummaryResponses.get(0).getType();
		}
	}

//	@Test
	@Order(2)
	public void type_change() throws Exception {
		final NumberTypeChangeRequest form = new NumberTypeChangeRequest();
		form.setType((byte)1);

		this.mockMvc.perform(RestDocumentationRequestBuilders.patch(TEST_URL + "/{number}/type", number)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						pathParameters(
								parameterWithName("number").description("070번호")
						),
						requestFields(
								fieldWithPath("type").type(JsonFieldType.NUMBER).description("변경할 번호타입")
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").type(JsonFieldType.NULL).description("NULL").optional(),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				))
				.andReturn();

		final Number_070 entity = repositoryTest.findOne(number);
		log.info(entity.toString());

		assertEquals(entity.getType(), (byte)1);

		form.setType(type);
		repositoryTest.typeChange(form, number);
	}

//	@Test
	public void typeNumbers() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/type-numbers")
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.param("type", "0")
				.param("host", ""))
				.andExpect(status().isOk())
				.andDo(document.document(
						requestParameters(
								parameterWithName("type").description("번호타입(0:헌트, 1:개인, 2:대표, 3:회의)"),
								parameterWithName("host").description("번호사용ip").optional()
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
						.and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("번호목록").optional())
						.andWithPrefix("data.[]", summaryNumber070Response)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();
	}
}
