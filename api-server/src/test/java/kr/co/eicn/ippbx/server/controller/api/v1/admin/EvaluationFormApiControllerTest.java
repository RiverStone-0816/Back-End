package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.server.jooq.eicn.enums.EvaluationFormUseType;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.EvaluationForm;
import kr.co.eicn.ippbx.server.model.entity.eicn.EvaluationCategoryEntity;
import kr.co.eicn.ippbx.server.model.entity.eicn.EvaluationFormEntity;
import kr.co.eicn.ippbx.server.model.form.EvaluationFormRequest;
import kr.co.eicn.ippbx.server.model.form.EvaluationFormVisibleRequest;
import kr.co.eicn.ippbx.server.util.page.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class EvaluationFormApiControllerTest extends BaseControllerTest {
	private final String TEST_URL = "/api/v1/admin/record/evaluation/form";

//	@Test
	protected void getCategories() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{id}/category", 2)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		final List<EvaluationCategoryEntity> entities = listData(result, EvaluationCategoryEntity.class);
	}

//	@Test
	protected void pagination() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		final Pagination<EvaluationForm> pagination = paginationData(result, EvaluationForm.class);

		final List<EvaluationForm> rows = pagination.getRows();

		for (EvaluationForm row : rows) {
			log.info("row {}", row.toString());
		}
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
		final EvaluationFormRequest form = new EvaluationFormRequest();
		form.setName("2020년 5월 평가(영업팀)");
		form.setUseType(EvaluationFormUseType.PERIOD);
		form.setMemo("5월 영업팀 분기평가2");
		form.setStartDate(Date.valueOf(LocalDate.now().toString()));
		form.setEndDate(Date.valueOf(LocalDate.now().plusDays(10).toString()));

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andDo(print())
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isCreated())
				.andReturn();

		pagination();
	}

//	@Test
	protected void put() throws Exception {
		final EvaluationFormRequest form = new EvaluationFormRequest();
		form.setName("2월 상담원 평가");
		form.setUseType(EvaluationFormUseType.N);
//		form.setMemo();
//		form.setStartDate(Date.valueOf(LocalDate.now().minusDays(3).toString()));
//		form.setEndDate(Date.valueOf(LocalDate.now().plusDays(10).toString()));

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/{id}", 2)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		pagination();
	}

//	@Test
	protected void hiddenUpdate() throws Exception {
		EvaluationFormVisibleRequest form = new EvaluationFormVisibleRequest();

		form.setId(2L);
		form.setHidden("Y");

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/hidden")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(Arrays.asList(form))))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		pagination();
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

//	@Test
	protected void copyForm() throws Exception {
		final EvaluationFormRequest form = new EvaluationFormRequest();
		form.setName("7월 평가");
		form.setUseType(EvaluationFormUseType.N);
		form.setMemo("2월 상담원 평가 복사");

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL + "/{id}/copy", 2)
				.contentType(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andDo(print())
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk())
				.andReturn();
	}
}
