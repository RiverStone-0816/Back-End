package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.model.entity.eicn.EvaluationFormEntity;
import kr.co.eicn.ippbx.model.form.EvaluationCategoryFormRequest;
import kr.co.eicn.ippbx.model.form.EvaluationItemFormRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class EvaluationItemApiControllerTest extends BaseControllerTest {
	private final String TEST_URL = "/api/v1/admin/record/evaluation/item";

	public void list() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
	}

//	@Test
	public void get() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{id}", 2)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		final EvaluationFormEntity data = getData(result, EvaluationFormEntity.class);

		log.info("{}", data);
	}

//	@Test
	public void post() throws Exception {
		final EvaluationCategoryFormRequest category = new EvaluationCategoryFormRequest();
		category.setName("첫인사");

		EvaluationItemFormRequest item1 = new EvaluationItemFormRequest();
		item1.setName("매우");
		item1.setValuationBasis("인사말,소속,이름 정확하게 응대");
		item1.setMaxScore(10);
//		item1.setRemark();

		EvaluationItemFormRequest item2 = new EvaluationItemFormRequest();
		item2.setName("그냥");
		item2.setValuationBasis("인사, 소속, 이름 얘기 하였으나 무성의한 첫인사");
		item2.setMaxScore(5);
//		item1.setRemark();
		EvaluationItemFormRequest item3 = new EvaluationItemFormRequest();
		item3.setName("보통");
		item3.setValuationBasis("항목 중 한가지 이상 빠트리거나 인사를 하지 않을 경우");
		item3.setMaxScore(0);

		category.setItems(Arrays.asList(item1, item2, item3));

		final EvaluationCategoryFormRequest category1 = new EvaluationCategoryFormRequest();
		category1.setName("도입무의 명확성");

		EvaluationItemFormRequest category1Item1 = new EvaluationItemFormRequest();
		category1Item1.setName("고객 요약 확인");
		category1Item1.setValuationBasis("고객의 니즈 파악 및 문의 내용을 요약해서 확인");
		category1Item1.setMaxScore(70);

		EvaluationItemFormRequest category1Item2 = new EvaluationItemFormRequest();
		category1Item2.setName("고객 요약 확인");
		category1Item2.setValuationBasis("고객니즈 파악 및 문의 내용 확인하지 않고 바로 고객 정보 확인(통신사 확인)");
		category1Item2.setMaxScore(15);

		category1.setItems(Arrays.asList(category1Item1, category1Item2));

		final List<EvaluationCategoryFormRequest> evaluationCategoryFormRequests = Arrays.asList(category, category1);

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL + "/target/{evaluationId}", 9)
				.contentType(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(evaluationCategoryFormRequests)))
				.andDo(print())
				.andExpect(status().isCreated())
				.andReturn();
	}

//	@Test
	protected void delete() throws Exception {
		this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{groupId}", 1)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("result").value("success"))
				.andReturn();
	}
}
