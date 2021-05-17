package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.server.model.ResearchAnswerItemComposite;
import kr.co.eicn.ippbx.server.model.ResearchQuestionItemComposite;
import kr.co.eicn.ippbx.server.model.dto.statdb.StatVocResponse;
import kr.co.eicn.ippbx.server.model.entity.eicn.ResearchListEntity;
import kr.co.eicn.ippbx.server.repository.eicn.ResearchListRepository;
import kr.co.eicn.ippbx.server.repository.eicn.ResearchTreeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class VOCStatisticsApiControllerTest extends BaseControllerTest {
	private final String TEST_URL = "/api/v1/admin/outbound/voc/statistics";

	private FieldDescriptor[] statVocResponse = new FieldDescriptor[] {
		fieldWithPath("level").type(JsonFieldType.NUMBER).description("답변레벨"),
		fieldWithPath("path").type(JsonFieldType.STRING).description("윗레벨의 path를 포함한 path의 나열 ex>0-4_1_2_0-4_2_6_1"),
		fieldWithPath("count").type(JsonFieldType.NUMBER).description("건수")
	};

	@Autowired
	private ResearchTreeRepository researchTreeRepository;

//	@Test
	public void list() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.param("startDate", "")
				.param("endDate", "")
				.param("seq", ""))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						requestParameters(
							parameterWithName("startDate").description("시작일"),
							parameterWithName("endDate").description("종료일"),
							parameterWithName("seq").description("SEQUENCE KEY")
						),
						responseFields(
							fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
							fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
						.andWithPrefix("data.[]", statVocResponse)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();

		final ResearchListEntity root = researchTreeRepository.getResearchScenarioLists(1);
		final List<StatVocResponse> statVocResponses = listData(result, StatVocResponse.class);

		printQLog(root.getScenario());

		for (StatVocResponse statVocRespons : statVocResponses) {
			log.info("level:{}, path:{}, count:{}", statVocRespons.getLevel(), statVocRespons.getPath(), statVocRespons.getCount());
		}
	}

	public void printQLog(ResearchQuestionItemComposite question) {
		log.info("{}{}.질문 :{}, path: {}", prefix(question.getLevel()), question.getLevel(), kr.co.eicn.ippbx.server.util.StringUtils.subStringBytes(question.getWord(), 30), question.getPath());
		if (question.getAnswerItems() != null)
			printALog(question.getAnswerItems());
		if (!question.isLeaf())
			question.getChildNode().forEach(this::printQLog);
	}

	public void printALog(List<ResearchAnswerItemComposite> answers) {
		for (ResearchAnswerItemComposite answerItem : answers) {
			log.info("{}{}.답변 :{}, parent:{}, path: {}", prefix(answerItem.getLevel()), answerItem.getLevel(), answerItem.getWord(), answerItem.getParent(), answerItem.getPath());
			if (!answerItem.isLeaf())
				printQLog(answerItem.getChild());
		}
	}

	private String prefix(Integer level) {
		StringBuilder prefix = new StringBuilder("-");
		for (int i = 0; i < level ; i++) {
			prefix.append("--");
		}
		return prefix.toString();
	}
}
