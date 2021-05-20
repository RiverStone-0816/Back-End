package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.model.dto.eicn.ResearchItemResponse;
import kr.co.eicn.ippbx.model.dto.eicn.ResearchItemSummaryResponse;
import kr.co.eicn.ippbx.model.enums.ResearchItemSoundKind;
import kr.co.eicn.ippbx.model.form.ResearchItemFormRequest;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class ResearchItemApiControllerTest extends BaseControllerTest {
	private final String TEST_URL = "/api/v1/admin/outbound/research/item";

	private final FieldDescriptor[] researchItemResponse = new FieldDescriptor[] {
			fieldWithPath("seq").type(JsonFieldType.NUMBER).description("SEQUENCE KEY"),
			fieldWithPath("itemId").type(JsonFieldType.NUMBER).description("그룹 아이디"),
			fieldWithPath("itemName").type(JsonFieldType.STRING).description("문항명"),
			fieldWithPath("mappingNumber").type(JsonFieldType.NUMBER).description("문항 인덱스"),
			fieldWithPath("word").type(JsonFieldType.STRING).description("질문"),
			fieldWithPath("soundKind").type(JsonFieldType.STRING).description("음원구분(N:사용안함, T:TTS로읽음, S:등록된음원사용)"),
			fieldWithPath("soundCode").type(JsonFieldType.NUMBER).description("음원 참조 코드").optional(),
			fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드").optional(),
			fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("윗레벨의 코드를 포함한 코드의 나열(002_003_008)").optional(),
			fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("조직레벨").optional(),
			fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사 아이디"),
			fieldWithPath("answers").type(JsonFieldType.ARRAY).description("답변 목록(문항 인덱스 순으로 나열)").optional(),
			fieldWithPath("companyTrees").type(JsonFieldType.ARRAY).description("조직정보 목록").optional()
	};

	private FieldDescriptor[] organizationSummaryResponse = new FieldDescriptor[] {
			fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드").optional(),
			fieldWithPath("groupName").type(JsonFieldType.STRING).description("조직명").optional(),
			fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("윗레벨의 코드를 포함한 코드의 나열(002_003_008)").optional(),
			fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("조직레벨").optional()
	};

	private FieldDescriptor[] summarySoundListResponse = new FieldDescriptor[] {
			fieldWithPath("seq").type(JsonFieldType.NUMBER).description("음원 SEQUENCE KEY"),
			fieldWithPath("soundName").type(JsonFieldType.STRING).description("음원명"),
	};

//	@Test
	public void pagination() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.param("itemName", "")
				.param("word", ""))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document.document(
						requestParameters(
								parameterWithName("itemName").description("문항제목"),
								parameterWithName("word").description("질문")
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
						.and(fieldWithPath("data.rows[]").type(JsonFieldType.ARRAY).description("설문문항 목록").optional())
						.andWithPrefix("data.rows[]", researchItemResponse)
						.andWithPrefix("data.rows[].companyTrees[]", organizationSummaryResponse)
						.andWithPrefix("data.",
								fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지"),
								fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총갯수")
						)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();

		final Pagination<ResearchItemSummaryResponse> pagination = paginationData(result, ResearchItemSummaryResponse.class);
		List<ResearchItemSummaryResponse> rows = pagination.getRows();

		for (ResearchItemSummaryResponse row : rows) {
			log.info(row.toString());
		}
	}

//	@Test
	public void get() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{seq}", 283)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document.document(
						pathParameters(
								parameterWithName("seq").description("SEQUENCE KEY")
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
						.and(fieldWithPath("data.").type(JsonFieldType.OBJECT).description("문항 상세정보").optional())
						.andWithPrefix("data.", researchItemResponse)
						.andWithPrefix("data.companyTrees[]", organizationSummaryResponse)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();

		ResearchItemResponse data = getData(result, ResearchItemResponse.class);

		log.info("data {}", data);
	}

//	@Test
	public void post() throws Exception {
		final ResearchItemFormRequest form = new ResearchItemFormRequest();
		form.setItemName("당 지지도 설문조사");
		form.setWord("귀하께서 현재 어느 정당을 지지하고 계십니까? 대통합민주신당은 1번, 한나라당은 2번, 민주노동당은 3번, 민주당은 4번, 지지정당이 없으시면 5번을 눌러주세요.");
//		form.setSoundKind("T"); // 음원(ARS설문시사용) "":사용안함, T:TTS로읽음, S:등록된음원사용
//		form.setSoundCode(2);

		List<String> answers = Arrays.asList("대통합민주당", "한나라당", "민주노동당", "민주당", "지지정당없음");
		form.setAnswerRequests(answers);

		this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andDo(print())
				.andExpect(status().isCreated())
				.andDo(document.document(
						requestFields(
								fieldWithPath("itemName").type(JsonFieldType.STRING).description("문항명"),
								fieldWithPath("word").type(JsonFieldType.STRING).description("질문"),
								fieldWithPath("soundKind").type(JsonFieldType.STRING).description("음원구분(N:사용안함, T:TTS로읽음, S:등록된음원사용)").optional(),
								fieldWithPath("soundCode").type(JsonFieldType.STRING).description("음원 참조 코드").optional(),
								fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드").optional(),
								fieldWithPath("answerRequests").type(JsonFieldType.ARRAY).description("답변 목록(문항 인덱스 순으로 나열)").optional()
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").type(JsonFieldType.NULL).description("NULL").optional(),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				))
				.andReturn();
	}

//	@Test
	public void put() throws Exception {
		final ResearchItemFormRequest form = new ResearchItemFormRequest();
		form.setItemName("성별 질문 합니다.");
		form.setWord("남자는 1번, 여자는 2번을 눌러주세요.");
		form.setGroupCode("0009");
		form.setSoundKind(ResearchItemSoundKind.SOUND.getCode());
		form.setSoundCode(41);

		List<String> answers = Arrays.asList("남자", "여자", "중성");
		form.setAnswerRequests(answers);

		this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/{seq}", 283)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						pathParameters(
								parameterWithName("seq").description("SEQUENCE KEY")
						),
						requestFields(
								fieldWithPath("itemName").type(JsonFieldType.STRING).description("문항명"),
								fieldWithPath("word").type(JsonFieldType.STRING).description("질문"),
								fieldWithPath("soundKind").type(JsonFieldType.STRING).description("음원구분").optional(),
								fieldWithPath("soundCode").type(JsonFieldType.NUMBER).description("음원 참조 코드").optional(),
								fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드").optional(),
								fieldWithPath("answerRequests").type(JsonFieldType.ARRAY).description("답변 목록").optional()
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").type(JsonFieldType.NULL).description("NULL").optional(),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				))
				.andReturn();

		get();
	}

//	@Test
	public void delete() throws Exception {
		this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{seq}", 1)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						pathParameters(
								parameterWithName("seq").description("SEQUENCE KEY")
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").type(JsonFieldType.NULL).description("NULL").optional(),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				))
				.andReturn();
	}

	/**
	 * 음원 목록 조회
	 */
//	@Test
	protected void add_sounds() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/add-sounds")
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
								.and(fieldWithPath("data").type(JsonFieldType.ARRAY).description("음원 목록").optional())
								.andWithPrefix("data.[]", summarySoundListResponse)
								.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();

	}
}
