package kr.co.eicn.ippbx.server.controller.api.v1.search;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.server.model.dto.eicn.search.SearchQueueNameResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class QueueNameSearchApiControllerTest extends BaseControllerTest {
	private final String TEST_URL = "/api/v1/search/queue";

	private FieldDescriptor[] searchQueueResponse = new FieldDescriptor[] {
			fieldWithPath("seq").type(JsonFieldType.NUMBER).description("SEQ (데이터입력시자동증가)"),
			fieldWithPath("name").type(JsonFieldType.STRING).description("QUEUE 이름"),
			fieldWithPath("hanName").type(JsonFieldType.STRING).description("QUEUE 한글이름"),
			fieldWithPath("number").type(JsonFieldType.STRING).description("QUEUE 11자리번호"),
			fieldWithPath("subGroup").type(JsonFieldType.STRING).description("예비 QUEUE이름").optional(),
			fieldWithPath("queueTimeout").type(JsonFieldType.NUMBER).description("QUEUE 대기타임아웃시간"),
			fieldWithPath("strategy").type(JsonFieldType.STRING).description("QUEUE 분배방식"),
			fieldWithPath("cnt").type(JsonFieldType.NUMBER).description(""),
			fieldWithPath("busyContext").type(JsonFieldType.STRING).description("콜백여부 사용시 해당컨텍스트명"),
			fieldWithPath("svcNumber").type(JsonFieldType.STRING).description("인입번호").optional(),
			fieldWithPath("host").type(JsonFieldType.STRING).description("해당 QUEUE 사용IP"),
			fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사 아이디"),
			fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드"),
			fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("윗레벨의 코드를 포함한 코드의 나열(002_003_008)"),
			fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("조직레벨").ignored(),
			fieldWithPath("huntForwarding").type(JsonFieldType.STRING).description("포워딩번호").optional(),
			fieldWithPath("isForwarding").type(JsonFieldType.STRING).description("포워딩여부"),
			fieldWithPath("blendingMode").type(JsonFieldType.STRING).description("콜블랜딩정책여부"),
			fieldWithPath("blendingWaitExeYn").type(JsonFieldType.STRING).description("Y,N").optional(),
			fieldWithPath("blendingWaitCnt").type(JsonFieldType.NUMBER).description("대기명수"),
			fieldWithPath("blendingWaitLasttime").type(JsonFieldType.STRING).description("기준대기자명수(2016-01-01 01:00:00)"),
			fieldWithPath("blendingWaitKeeptime").type(JsonFieldType.NUMBER).description("기준명수초과대기시간"),
			fieldWithPath("blendingTimeExeYn").type(JsonFieldType.STRING).description("시간설정여부").optional(),
			fieldWithPath("blendingTimeFromtime").type(JsonFieldType.NUMBER).description("시작시간"),
			fieldWithPath("blendingTimeTotime").type(JsonFieldType.NUMBER).description("끝시간")
	};

	@BeforeEach
	@Override
	protected void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
		document = document(
				"{class-name}/{method-name}",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint())
		);

		this.mockMvc = securityBuilder(webApplicationContext, restDocumentation);
	}

	@Test
	public void search() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andExpect(status().isOk())
				.andDo(document.document(
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
								.and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("헌트그룹 목록").optional())
								.andWithPrefix("data.[]", searchQueueResponse)
								.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();

		final List<SearchQueueNameResponse> responses = listData(result, SearchQueueNameResponse.class);

		log.info("response {}", responses);
	}
}
