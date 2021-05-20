package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EvaluationItemScore;
import kr.co.eicn.ippbx.model.entity.eicn.EvaluationResultEntity;
import kr.co.eicn.ippbx.model.enums.CallType;
import kr.co.eicn.ippbx.model.form.EvaluationItemScoreFormRequest;
import kr.co.eicn.ippbx.model.form.EvaluationResultFormRequest;
import kr.co.eicn.ippbx.model.search.EvaluationResultSearchRequest;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class EvaluationResultApiControllerTest extends BaseControllerTest {
	private final String TEST_URL = "/api/v1/admin/record/evaluation/result";

//	@Test
	protected void pagination() throws Exception {
		final EvaluationResultSearchRequest search = new EvaluationResultSearchRequest();
//		search.setStartDate();
//		search.setEndDate();
//		search.setEvaluationId();
//		search.setId();
//		search.setStatus();

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		final Pagination<EvaluationResultEntity> pagination = paginationData(result, EvaluationResultEntity.class);

		final List<EvaluationResultEntity> rows = pagination.getRows();

		for (EvaluationResultEntity row : rows) {
			log.info("상담원:{}, 고객번호:{}, 통화시간:{}, 평가지:{}, 평가시간:{}, 점수:{}, 진행상태:{}"
				, row.getTargetUserName()
				, row.getCdr().getInOut().equals(CallType.INBOUND.getCode()) ? row.getCdr().getSrc() : row.getCdr().getDst()
				, row.getCdr().getRingDate()
				, row.getForm().getName()
				, row.getEvaluationDate()
				, row.getScores().stream().mapToInt(EvaluationItemScore::getScore).sum()
				, row.getProcessStatus());
		}
	}

//	@Test
	protected void get() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{id}", 1)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		final EvaluationResultEntity data = getData(result, EvaluationResultEntity.class);
	}

//	@Test
	protected void stat() throws Exception {
		final EvaluationResultSearchRequest search = new EvaluationResultSearchRequest();
//		search.setStartDate();
//		search.setEndDate();
//		search.setEvaluationId();
//		search.setId();
//		search.setStatus();

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/stat")
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
	}

//	@Test
	protected void evaluationRegister() throws Exception {
		final EvaluationResultFormRequest form = new EvaluationResultFormRequest();

		form.setCdrId(858);
		form.setMemo("20점입니다");
		form.setTargetUserid("user03");
		form.setEvaluationId((long) 9);
		form.setResultTransfer(true);

		EvaluationItemScoreFormRequest score1 = new EvaluationItemScoreFormRequest();
		score1.setItemId((long) 22);
		score1.setScore((byte) 10);

		EvaluationItemScoreFormRequest score2 = new EvaluationItemScoreFormRequest();
		score2.setItemId((long) 25);
		score2.setScore((byte) 20);

		form.setScores(Arrays.asList(score1, score2));

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
	}
}
