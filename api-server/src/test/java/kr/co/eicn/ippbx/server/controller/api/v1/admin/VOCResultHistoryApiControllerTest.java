package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.server.model.entity.customdb.VocResearchResultEntity;
import kr.co.eicn.ippbx.server.util.page.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class VOCResultHistoryApiControllerTest extends BaseControllerTest {
	private final String TEST_URL = "/api/v1/admin/outbound/voc/result-history";

	private FieldDescriptor[] vocResultEntity = new FieldDescriptor[] {
		fieldWithPath("seq").type(JsonFieldType.NUMBER).description("SEQUENCE KEY"),
		fieldWithPath("resultDate").type(JsonFieldType.STRING).description("설문일시"),
		fieldWithPath("uniqueid").type(JsonFieldType.STRING).description("유니크 아이디"),
		fieldWithPath("hangupCause").type(JsonFieldType.STRING).description("전화행업코드"),
		fieldWithPath("hangupMsg").type(JsonFieldType.STRING).description("전화행업메시지"),
		fieldWithPath("billsec").type(JsonFieldType.NUMBER).description("통화시간"),
		fieldWithPath("customNumber").type(JsonFieldType.STRING).description("고객 전화번호"),
		fieldWithPath("userid").type(JsonFieldType.STRING).description("상담자 아이디"),
		fieldWithPath("vocGroupId").type(JsonFieldType.NUMBER).description("VOC그룹 아이디"),
		fieldWithPath("researchId").type(JsonFieldType.NUMBER).description("설문 아이디"),
		fieldWithPath("treePath").type(JsonFieldType.STRING).description("윗레벨의 답변 PATH를 포함한 PATH의 나열(0-1_1_1_1-1_1_1_2)"),
		fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사 아이디"),

		fieldWithPath("idName").type(JsonFieldType.STRING).description("상담자").optional(),
		fieldWithPath("group").type(JsonFieldType.OBJECT).description("VOC그룹").optional(),
		fieldWithPath("group.research.").type(JsonFieldType.OBJECT).description("설문정보").optional(),

		fieldWithPath("results").type(JsonFieldType.ARRAY).description("문항트리의 레벨이 3이라면, List[0], List[1], List[2]의 답변정보가 전달된다").optional()
	};

	private FieldDescriptor[] vocGroupEntity = new FieldDescriptor[] {
			fieldWithPath("seq").type(JsonFieldType.NUMBER).description("SEQUENCE KEY"),
			fieldWithPath("vocGroupName").type(JsonFieldType.STRING).description("VOC그룹명"),
			fieldWithPath("vocInfo").type(JsonFieldType.STRING).description("VOC설명"),
			fieldWithPath("researchId").type(JsonFieldType.NUMBER).description("설문 아이디"),
			fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사 아이디")
	};

	private final FieldDescriptor[] researchList = new FieldDescriptor[] {
			fieldWithPath("seq").type(JsonFieldType.NUMBER).description("SEQUENCE KEY"),
			fieldWithPath("regdate").type(JsonFieldType.STRING).description("등록일"),
			fieldWithPath("researchName").type(JsonFieldType.STRING).description("설문명"),
			fieldWithPath("researchId").type(JsonFieldType.NUMBER).description("설문 아이디"),
			fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드").optional(),
			fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("윗레벨의 코드를 포함한 코드의 나열(002_003_008)").optional(),
			fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("조직레벨").optional(),
			fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사 아이디"),
			fieldWithPath("haveTree").type(JsonFieldType.STRING).description("시나리오 여부"),
			fieldWithPath("ttsFieldName").type(JsonFieldType.STRING).description("TTS필드명")
	};

//	@Test
	public void pagination() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.param("startDate", "2020-04-06")
				.param("endDate", "2020-05-20")
				.param("vocGroupSeq", "1"))
				.andDo(print())
				.andExpect(status().isOk())
//				.andDo(document.document(
//						requestParameters(
//							parameterWithName("startDate").description("시작일"),
//							parameterWithName("endDate").description("종료일"),
//							parameterWithName("seq").description("SEQUENCE KEY")
//						),
//						responseFields(
//							fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
//							fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
//						)
//						.and(fieldWithPath("data.rows[]").type(JsonFieldType.ARRAY).description("VOC결과이력 목록").optional())
//						.andWithPrefix("data.rows[]", vocResultEntity)
//						.andWithPrefix("data.rows[].group.", vocGroupEntity)
//						.andWithPrefix("data.rows[].group.research.", researchList)
//						.andWithPrefix("data.rows[].results",
//								fieldWithPath("[]").type(JsonFieldType.ARRAY).description("문항트리의 레벨이 3이라면, List[0], List[1], List[2]의 답변정보가 전달된다"))
//						.andWithPrefix("data.",
//								fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지"),
//								fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총갯수")
//						)
//						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
//				))
				.andReturn();

		final Pagination<VocResearchResultEntity> pagination = paginationData(result, VocResearchResultEntity.class);
		final List<VocResearchResultEntity> rows = pagination.getRows();
		for (VocResearchResultEntity row : rows) {
			log.info("날짜:{}, 고객전화번호:{}, 상담사:{}, VOC명:{}, 설문명:{}, VOC결과:{}"
					, row.getResultDate()
					, row.getCustomNumber()
					, row.getIdName()
					, row.getGroup() != null ? row.getGroup().getVocGroupName() : EMPTY
					, row.getGroup() != null ? row.getGroup().getResearch() != null ? row.getGroup().getResearch().getResearchName() : EMPTY : EMPTY
					, row.getResults());
		}
	}

//	@Test
	public void get_max_level() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{seq}/max-level", 1 )
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						pathParameters(
								parameterWithName("seq").description("VOC그룹 SEQUENCE KEY")
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").type(JsonFieldType.NUMBER).description("해당 트리에 MAX LEVEL").optional(),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				))
				.andReturn();
	}
}
