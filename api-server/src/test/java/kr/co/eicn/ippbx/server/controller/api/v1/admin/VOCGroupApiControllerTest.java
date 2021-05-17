package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.server.model.enums.InOutCallKind;
import kr.co.eicn.ippbx.server.model.enums.InOutTarget;
import kr.co.eicn.ippbx.server.model.enums.ProcessKind;
import kr.co.eicn.ippbx.server.model.form.VOCGroupFormRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class VOCGroupApiControllerTest extends BaseControllerTest {
	private final String TEST_URL = "/api/v1/admin/outbound/voc/group";

	private FieldDescriptor[] VOCGroupResponse = new FieldDescriptor[] {
			fieldWithPath("seq").type(JsonFieldType.NUMBER).description("SEQUENCE KEY"),
			fieldWithPath("vocGroupName").type(JsonFieldType.STRING).description("VOC그룹명"),
			fieldWithPath("vocInfo").type(JsonFieldType.STRING).description("VOC설명"),
			fieldWithPath("researchId").type(JsonFieldType.NUMBER).description("설문 아이디"),
			fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사 아이디")
	};

	private FieldDescriptor[] summaryResearchListResponse = new FieldDescriptor[] {
			fieldWithPath("researchId").type(JsonFieldType.NUMBER).description(""),
			fieldWithPath("researchName").type(JsonFieldType.STRING).description("")
	};

//	@Test
	public void pagination() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/search")
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.param("processKind", "T")
				.param("startTerm", "2020-05-01")
				.param("endTerm", "2020-05-31"))
				.andExpect(status().isOk())
				.andDo(print())
//				.andDo(document.document(
//						responseFields(
//								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
//							fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
//						)
//						.and(fieldWithPath("data.rows[]").type(JsonFieldType.ARRAY).description("VOC그룹 목록").optional())
//						.andWithPrefix("data.rows[]", VOCGroupResponse)
//						.andWithPrefix("data.rows[].research.", summaryResearchListResponse)
//						.andWithPrefix("data.",
//								fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지"),
//								fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총갯수")
//						)
//						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
//				))
				.andReturn();
	}

	@Test
	public void get() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{seq}", 5)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
//				.andDo(document.document(
//						pathParameters(
//								parameterWithName("seq").description("SEQUENCE KEY")
//						),
//						responseFields(
//								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
//								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
//						)
//						.andWithPrefix("data.", VOCGroupResponse)
//						.andWithPrefix("data.research.", summaryResearchListResponse)
//						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
//				))
				.andReturn();
	}

//	@Test
	protected void post() throws Exception {
		final VOCGroupFormRequest form = new VOCGroupFormRequest();
		List<String> inboundMemberList = new ArrayList<>();
		inboundMemberList.add("user01");
		inboundMemberList.add("user02");
		inboundMemberList.add("user03");
		List<String> outboundMemberList = new ArrayList<>();
		outboundMemberList.add("user01");
		outboundMemberList.add("user02");

		form.setVocGroupName("VOC_TEST");
		form.setProcessKind(ProcessKind.TERM.getCode());
		form.setStartTerm(Date.valueOf("2020-05-24"));
		form.setEndTerm(Date.valueOf("2020-06-08"));
		form.setSender("AUTO");
		form.setIsArsSms("ARS");
		form.setArsResearchId(1);
		form.setOutboundTarget(InOutTarget.MEMBER.getCode());
		form.setOutboundMemberList(outboundMemberList);
		form.setOutboundCallKind(InOutCallKind.NOANSWER.getCode());
		form.setInboundTarget(InOutTarget.SVCNUM.getCode());
		form.setInboundCallKind(InOutCallKind.ANSWER.getCode());
		form.setInboundTargetSvcnum("07075490384");
		form.setInboundMemberList(inboundMemberList);
		form.setInformation("VOC_TEST 입니다.");

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andDo(print())
				.andExpect(status().isCreated())
//				.andDo(document.document(
//						requestFields(VOCGroupFormRequest),
//						responseFields(
//								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
//								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
//								fieldWithPath("data").type(JsonFieldType.NULL).description("").optional(),
//								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
//						)
//				))
				.andReturn();
	}

//	@Test
	protected void put() throws Exception {
		final VOCGroupFormRequest form = new VOCGroupFormRequest();
        List<String> memberList = new ArrayList<>();
        memberList.add("user03");

        form.setVocGroupName("5월 서비스 불만 관련 VOC");
        form.setProcessKind(ProcessKind.CONTINUE.getCode());
        form.setIsArsSms("ARS");
        form.setSender("AUTO");
        form.setArsResearchId(1);
        form.setOutboundTarget(InOutTarget.MEMBER.getCode());
        form.setOutboundMemberList(memberList);
        form.setOutboundCallKind(InOutCallKind.ANSWER.getCode());
        form.setInboundTarget(InOutTarget.MEMBER.getCode());
		form.setInboundMemberList(memberList);
        form.setInboundCallKind(InOutCallKind.ALL.getCode());
//		form.setInformation("test");

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/{seq}", 2)
				.contentType(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andDo(print())
				.andExpect(status().isOk())
//				.andDo(document.document(
//						pathParameters(
//								parameterWithName("seq").description("SEQUENCE KEY")
//						),
//						requestFields(VOCGroupFormRequest),
//						responseFields(
//								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
//								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
//								fieldWithPath("data").type(JsonFieldType.NULL).description("").optional(),
//								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
//						)
//				))
				.andReturn();
	}

//	@Test
	protected void delete() throws Exception {
		this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{seq}", 5)
				.with(new JwtRequestPostProcessor()))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("result").value("success"))
				.andDo(document.document(
						pathParameters(
								parameterWithName("seq").description("SEQUENCE KEY")
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
}
