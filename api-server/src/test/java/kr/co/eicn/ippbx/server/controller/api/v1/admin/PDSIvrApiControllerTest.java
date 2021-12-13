package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.model.IvrTreeComposite;
import kr.co.eicn.ippbx.model.dto.eicn.PDSIvrResponse;
import kr.co.eicn.ippbx.model.enums.Button;
import kr.co.eicn.ippbx.model.enums.IvrTreeType;
import kr.co.eicn.ippbx.model.form.PDSIvrButtonMappingFormRequest;
import kr.co.eicn.ippbx.model.form.PDSIvrFormRequest;
import kr.co.eicn.ippbx.model.form.PDSIvrFormUpdateRequest;
import kr.co.eicn.ippbx.server.repository.eicn.SoundListRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class PDSIvrApiControllerTest extends BaseControllerTest {

	@Autowired
	private SoundListRepository soundListRepository;
	private final String TEST_URL = "/api/v1/admin/outbound/pds/ivr";

	private FieldDescriptor[] PDSIvrResponse = new FieldDescriptor[] {
			fieldWithPath("seq").type(JsonFieldType.NUMBER).description("SEQUENCE KEY"),
			fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드키"),
			fieldWithPath("root").type(JsonFieldType.NUMBER).description("루트키"),
			fieldWithPath("parent").type(JsonFieldType.NUMBER).description("부모키"),
			fieldWithPath("level").type(JsonFieldType.NUMBER).description("레벨"),
			fieldWithPath("treeName").type(JsonFieldType.STRING).description("IVR이름"),
			fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
			fieldWithPath("button").type(JsonFieldType.STRING).description("버튼"),
			fieldWithPath("type").type(JsonFieldType.NUMBER).description("연결설정"),
			fieldWithPath("typeData").type(JsonFieldType.STRING).description("연결데이타"),
			fieldWithPath("soundCode").type(JsonFieldType.STRING).description("음원관리 참조 정보").optional(),
			fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사 아이디"),
			fieldWithPath("nodes").type(JsonFieldType.ARRAY).description("자식 트리")
	};

	private FieldDescriptor[] PDSIvrDetailResponse = new FieldDescriptor[] {
			fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드키"),
			fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
			fieldWithPath("soundCode").type(JsonFieldType.STRING).description("음원관리 참조 정보"),
			fieldWithPath("buttonMappingList").type(JsonFieldType.ARRAY).description("버튼과 매핑되어지는 pds tree 정보").optional()
	};

	private FieldDescriptor[] buttonMappingList = new FieldDescriptor[] {
			fieldWithPath("seq").type(JsonFieldType.NUMBER).description("SEQUENCE KEY"),
			fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드키"),
			fieldWithPath("root").type(JsonFieldType.NUMBER).description("루트키"),
			fieldWithPath("parent").type(JsonFieldType.NUMBER).description("부모키"),
			fieldWithPath("level").type(JsonFieldType.NUMBER).description("레벨"),
			fieldWithPath("treeName").type(JsonFieldType.STRING).description("IVR이름"),
			fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
			fieldWithPath("button").type(JsonFieldType.STRING).description("버튼"),
			fieldWithPath("type").type(JsonFieldType.NUMBER).description("연결설정"),
			fieldWithPath("typeData").type(JsonFieldType.STRING).description("연결데이타"),
			fieldWithPath("soundCode").type(JsonFieldType.STRING).description("음원관리 참조 정보").optional(),
			fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사 아이디")
	};

	private FieldDescriptor[] PDSIvrFormUpdateRequest = new FieldDescriptor[] {
		fieldWithPath("name").type(JsonFieldType.STRING).description("IVR명"),
		fieldWithPath("soundCode").type(JsonFieldType.STRING).description("음원관리 참조 정보").optional(),
		fieldWithPath("buttonMappingFormRequests").type(JsonFieldType.ARRAY).description("버튼 매핑 정보")
	};

	private FieldDescriptor[] PDSIvrButtonMappingFormRequest = new FieldDescriptor[] {
			fieldWithPath("seq").type(JsonFieldType.NUMBER).description("SEQUENCE KEY").optional(),
			fieldWithPath("name").type(JsonFieldType.STRING).description("IVR명"),
			fieldWithPath("button").type(JsonFieldType.STRING).description("버튼과 매핑되는 정보"),
			fieldWithPath("type").type(JsonFieldType.NUMBER).description("연결설정(1:메뉴다시듣기, 2:PDS헌트연결, 4:음성안내종료, 5:하위IVR로연결, 6:이전단계로돌아가기, 7:처음으로돌아가기, 8:음원플레이)"),
			fieldWithPath("typeData").type(JsonFieldType.STRING).description("연결데이타").optional()
	};

	private FieldDescriptor[] summaryIvrTreeResponse = new FieldDescriptor[] {
			fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드 키"),
			fieldWithPath("name").type(JsonFieldType.STRING).description("IVR정보명")
	};

	private FieldDescriptor[] summaryPDSQueueNameResponse = new FieldDescriptor[] {
			fieldWithPath("name").type(JsonFieldType.STRING).description("PDS QUEUE 이름"),
			fieldWithPath("hanName").type(JsonFieldType.STRING).description("PDS QUEUE 한글이름"),
	};

	/**
	 *  목록조회
	 */
//	@Test
	public void list() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						relaxedResponseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").description("실제 반환될 데이터").optional()
						)
						.andWithPrefix("data.[]", PDSIvrResponse)
						.and(fieldWithPath("data.[].nodes[]").type(JsonFieldType.ARRAY).description("계층 PDS IVR정보").optional())
						.andWithPrefix("data.[].nodes[]", PDSIvrResponse)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();

		final List<PDSIvrResponse> pdsIvrComposites = listData(result, PDSIvrResponse.class);

		printTree(pdsIvrComposites);
	}

//	@Test
	public void get() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{code}", 10)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						pathParameters(
								parameterWithName("code").description("코드키")
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
						.andWithPrefix("data.", PDSIvrDetailResponse)
						.andWithPrefix("data.buttonMappingList[]", buttonMappingList)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();
	}

	/**
	 * 루트IVR추가
	 */
//	@Test
	@Order(1)
	protected void post() throws Exception {
		final PDSIvrFormRequest form = new PDSIvrFormRequest();
		form.setName("이상욱테스트 ");

//		soundListRepository.findAll()
//				.stream()
//				.findAny()
//		.ifPresent(e -> form.setSoundCode(String.valueOf(e.getSeq())));

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andDo(print())
				.andExpect(status().isCreated())
				.andDo(document.document(
						requestFields(
							fieldWithPath("name").type(JsonFieldType.STRING).description("IVR명"),
							fieldWithPath("soundCode").type(JsonFieldType.STRING).description("음원 참조 데이터").optional()
						),
						responseFields(
							fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
							fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
							fieldWithPath("data").type(JsonFieldType.NULL).description("").optional(),
							fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				))
				.andReturn();
	}

	/**
	 * 수정
	 */
//	@Test
	@Order(1)
	protected void put() throws Exception {
		final PDSIvrFormUpdateRequest form = new PDSIvrFormUpdateRequest();
		form.setName("이상욱테스트");

		final PDSIvrButtonMappingFormRequest buttonMappingFormRequest = new PDSIvrButtonMappingFormRequest();
		buttonMappingFormRequest.setButton("1");
		buttonMappingFormRequest.setName("버튼1");
		buttonMappingFormRequest.setType(IvrTreeType.RECORD_PLAY.getCode());
		buttonMappingFormRequest.setTypeData("38");

		final PDSIvrButtonMappingFormRequest buttonMappingFormRequest1 = new PDSIvrButtonMappingFormRequest();
		buttonMappingFormRequest.setSeq(317);
		buttonMappingFormRequest1.setButton("4");
		buttonMappingFormRequest1.setName("버튼4");
		buttonMappingFormRequest1.setType(IvrTreeType.RECORD_PLAY.getCode());
		buttonMappingFormRequest1.setTypeData("41");

		final PDSIvrButtonMappingFormRequest buttonMappingFormRequest2 = new PDSIvrButtonMappingFormRequest();
		buttonMappingFormRequest2.setButton("6");
		buttonMappingFormRequest2.setName("버튼6");
		buttonMappingFormRequest2.setType(IvrTreeType.LINK_TO_ANOTHER_IVR.getCode());

//		form.setButtonMappingFormRequests(Arrays.asList(buttonMappingFormRequest));
		form.setButtonMappingFormRequests(Arrays.asList(buttonMappingFormRequest1));
//		form.setButtonMappingFormRequests(Arrays.asList(buttonMappingFormRequest, buttonMappingFormRequest1));

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/{code}", 14)
				.contentType(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						pathParameters(
								parameterWithName("code").description("코드키")
						),
						requestFields(PDSIvrFormUpdateRequest)
						.andWithPrefix("buttonMappingFormRequests[]", PDSIvrButtonMappingFormRequest),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").type(JsonFieldType.NULL).description("").optional(),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				))
				.andReturn();
	}

	/**
	 * 하위 트리까지 삭제
	 */
//	@Test
	@Order(4)
	protected void delete() throws Exception {
		this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{code}", "14")
				.with(new JwtRequestPostProcessor()))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("result").value("success"))
				.andDo(document.document(
						pathParameters(
								parameterWithName("code").description("코드키")
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
	public void resource() throws Exception {
		this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{seq}/resource", "1")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.param("token", getAuthToken()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						pathParameters(parameterWithName("seq").description("SEQUENCE KEY")),
						requestParameters(parameterWithName("token").description("토큰 키")),
						responseBody()
				))
				.andReturn();
	}

//	@Test
	@Order(4)
	protected void rootNodes() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/root-node")
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
						.and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("루트IVR 목록").optional())
						.andWithPrefix("data.[]", summaryIvrTreeResponse)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();
	}

//	@Test
	protected void add_pds_queue_names() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/add-pds-queue")
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
								.and(fieldWithPath("data").type(JsonFieldType.ARRAY).description("PDS QUEUE목록 조회").optional())
								.andWithPrefix("data.[]", summaryPDSQueueNameResponse)
								.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();
	}

	private void ivrTreePrintTree(List<IvrTreeComposite> nodes) {
		for (IvrTreeComposite node : nodes) {
			log.info(prefix(StringUtils.countMatches(node.getTreeName(), '_')) + "({}){}, code: {}, root: {}, tree_name: {}"
					, StringUtils.countMatches(node.getTreeName(), '_')
					, node.getName()
					, node.getCode()
					, node.getRoot()
					, node.getTreeName());
			if (!node.isLeaf())
				ivrTreePrintTree(node.getNodes());
		}
	}

	private void printTree(List<PDSIvrResponse> nodes) {
		for (PDSIvrResponse node : nodes) {
			log.info(prefix(StringUtils.countMatches(node.getTreeName(), '_')) + "({}-{}){}, button:{}, type:{}, tree_name:{}"
					, node.getCode()
					, StringUtils.countMatches(node.getTreeName(), '_')
					, node.getName()
					, Button.of(node.getButton())
					, IvrTreeType.of(node.getType())
					, node.getTreeName());
			if (!node.getNodes().isEmpty())
				printTree(node.getNodes());
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
