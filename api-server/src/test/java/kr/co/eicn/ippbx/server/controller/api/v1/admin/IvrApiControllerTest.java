package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.server.model.dto.eicn.IvrResponse;
import kr.co.eicn.ippbx.server.model.enums.Button;
import kr.co.eicn.ippbx.server.model.enums.IvrMenuType;
import kr.co.eicn.ippbx.server.model.form.IvrButtonMappingFormRequest;
import kr.co.eicn.ippbx.server.model.form.IvrFormRequest;
import kr.co.eicn.ippbx.server.model.form.IvrFormUpdateRequest;
import kr.co.eicn.ippbx.server.model.form.WebVoiceItemsFormRequest;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *  TODO: API명세 미작성
 */
@Log4j2
public class IvrApiControllerTest extends BaseControllerTest {
	private final String TEST_URL = "/api/v1/admin/sounds/ivr";

	private FieldDescriptor[] ivrResponse = new FieldDescriptor[] {
			fieldWithPath("seq").type(JsonFieldType.NUMBER).description("SEQUENCE KEY"),
			fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드키"),
			fieldWithPath("root").type(JsonFieldType.NUMBER).description("루트키"),
			fieldWithPath("parent").type(JsonFieldType.NUMBER).description("부모키"),
			fieldWithPath("level").type(JsonFieldType.NUMBER).description("레벨"),
			fieldWithPath("treeName").type(JsonFieldType.STRING).description("IVR이름"),
			fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
			fieldWithPath("button").type(JsonFieldType.STRING).description("버튼"),
			fieldWithPath("type").type(JsonFieldType.NUMBER).description("연결설정"),
			fieldWithPath("typeData").type(JsonFieldType.STRING).description("연결데이타").optional(),
			fieldWithPath("soundCode").type(JsonFieldType.STRING).description("음원관리 참조 정보").optional(),
			fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사 아이디"),
			fieldWithPath("nodes").type(JsonFieldType.ARRAY).description("자식 트리")
	};

	private FieldDescriptor[] ivrTree = new FieldDescriptor[] {
			fieldWithPath("seq").type(JsonFieldType.NUMBER).description("고유키"),
			fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드키"),
			fieldWithPath("root").type(JsonFieldType.NUMBER).description("루트키").optional(),
			fieldWithPath("parent").type(JsonFieldType.NUMBER).description("부모키").optional(),
			fieldWithPath("level").type(JsonFieldType.NUMBER).description("레벨").optional(),
			fieldWithPath("treeName").type(JsonFieldType.STRING).description("윗레벨의 코드를 포함한 코드의 나열 ex)0003_0008_0001").optional(),
			fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
			fieldWithPath("button").type(JsonFieldType.STRING).description("버튼"),
			fieldWithPath("type").type(JsonFieldType.NUMBER).description("연결설정"),
			fieldWithPath("typeData").type(JsonFieldType.STRING).description("연결데이타").optional(),
			fieldWithPath("soundCode").type(JsonFieldType.STRING).description("음성코드").optional(),
			fieldWithPath("companyId").type(JsonFieldType.STRING).description("회사아이디"),
			fieldWithPath("isWebVoice").type(JsonFieldType.STRING).description("웹보이스 유무").optional(),
			fieldWithPath("introSoundCode").type(JsonFieldType.STRING).description("도입음성코드").optional(),
			fieldWithPath("posX").type(JsonFieldType.NUMBER).description("에디터에서의 위젯 X위치").optional(),
			fieldWithPath("posY").type(JsonFieldType.NUMBER).description("에디터에서의 위젯 Y위치").optional(),
			fieldWithPath("ttsData").type(JsonFieldType.STRING).description("TTS문구").optional()
	};

	private FieldDescriptor[] ivrDetailResponse = new FieldDescriptor[] {
			fieldWithPath("seq").type(JsonFieldType.NUMBER).description("고유키"),
			fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드키"),
			fieldWithPath("root").type(JsonFieldType.NUMBER).description("루트키").optional(),
			fieldWithPath("parent").type(JsonFieldType.NUMBER).description("부모키").optional(),
			fieldWithPath("level").type(JsonFieldType.NUMBER).description("레벨").optional(),
			fieldWithPath("treeName").type(JsonFieldType.STRING).description("윗레벨의 코드를 포함한 코드의 나열 ex)0003_0008_0001").optional(),
			fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
			fieldWithPath("button").type(JsonFieldType.STRING).description("버튼"),
			fieldWithPath("type").type(JsonFieldType.NUMBER).description("연결설정"),
			fieldWithPath("typeData").type(JsonFieldType.STRING).description("연결데이타").optional(),
			fieldWithPath("soundCode").type(JsonFieldType.STRING).description("음성코드").optional(),
			fieldWithPath("companyId").type(JsonFieldType.STRING).description("회사아이디"),
			fieldWithPath("isWebVoice").type(JsonFieldType.STRING).description("웹보이스 유무").optional(),
			fieldWithPath("introSoundCode").type(JsonFieldType.STRING).description("도입음성코드").optional(),
			fieldWithPath("posX").type(JsonFieldType.NUMBER).description("에디터에서의 위젯 X위치").optional(),
			fieldWithPath("posY").type(JsonFieldType.NUMBER).description("에디터에서의 위젯 Y위치").optional(),
			fieldWithPath("ttsData").type(JsonFieldType.STRING).description("TTS문구").optional(),

			fieldWithPath("buttonMappingList").type(JsonFieldType.ARRAY).description("버튼과 매핑되어지는 pds tree 정보").optional()
	};

	private FieldDescriptor[] buttonMappingList = new FieldDescriptor[] {
			fieldWithPath("seq").type(JsonFieldType.NUMBER).description("고유키"),
			fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드키"),
			fieldWithPath("root").type(JsonFieldType.NUMBER).description("루트키").optional(),
			fieldWithPath("parent").type(JsonFieldType.NUMBER).description("부모키").optional(),
			fieldWithPath("level").type(JsonFieldType.NUMBER).description("레벨").optional(),
			fieldWithPath("treeName").type(JsonFieldType.STRING).description("윗레벨의 코드를 포함한 코드의 나열 ex)0003_0008_0001").optional(),
			fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
			fieldWithPath("button").type(JsonFieldType.STRING).description("버튼"),
			fieldWithPath("type").type(JsonFieldType.NUMBER).description("연결설정"),
			fieldWithPath("typeData").type(JsonFieldType.STRING).description("연결데이타").optional(),
			fieldWithPath("soundCode").type(JsonFieldType.STRING).description("음성코드").optional(),
			fieldWithPath("companyId").type(JsonFieldType.STRING).description("회사아이디"),
			fieldWithPath("isWebVoice").type(JsonFieldType.STRING).description("웹보이스 유무").optional(),
			fieldWithPath("introSoundCode").type(JsonFieldType.STRING).description("도입음성코드").optional(),
			fieldWithPath("posX").type(JsonFieldType.NUMBER).description("에디터에서의 위젯 X위치").optional(),
			fieldWithPath("posY").type(JsonFieldType.NUMBER).description("에디터에서의 위젯 Y위치").optional(),
			fieldWithPath("ttsData").type(JsonFieldType.STRING).description("TTS문구").optional()
	};

	private FieldDescriptor[] summaryIvrTreeResponse = new FieldDescriptor[] {
			fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드 키"),
			fieldWithPath("name").type(JsonFieldType.STRING).description("IVR정보명")
	};

//	@Test
	public void buildHierarchyTree() throws Exception {
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
						.andWithPrefix("data.[]", ivrResponse)
						.and(fieldWithPath("data.[].nodes[]").type(JsonFieldType.ARRAY).description("계층 IVR정보").optional())
						.andWithPrefix("data.[].nodes[]", ivrResponse)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();

		final List<IvrResponse> ivrComposites = listData(result, IvrResponse.class);

		printTree(ivrComposites);
	}

	@Test
	public void get() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/ivr-list")
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
//						.andWithPrefix("data.", ivrDetailResponse)
//						.andWithPrefix("data.buttonMappingList[]", buttonMappingList)
//						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
//				))
				.andReturn();

//		log.info(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(getData(result, IvrResponse.class)));
	}

	/**
	 * 루트IVR추가
	 */
	@Test
	protected void postRoot() throws Exception {
		final IvrFormRequest form = new IvrFormRequest();
//		form.setType(IvrTreeTypeNew.LINK_MENU.getCode());
//		form.setName("근무시간 IVR");
//		form.setIntroSoundCode("38");
//		form.setSoundCode("38");
//		form.setPosX(0);
//		form.setPosY(0);
//
//		form.setIsWebVoice("Y");
//
//		form.setTypeDataStrings(Arrays.asList(""));
//		form.setTtsDataStrings(Arrays.asList(""));
//
//		final IvrButtonMappingFormRequest button1 = new IvrButtonMappingFormRequest();
//		button1.setButton(Button.DIAL_1.getCode());
//		button1.setName("상품문의");
//
//		final IvrButtonMappingFormRequest button2 = new IvrButtonMappingFormRequest();
//		button2.setButton(Button.DIAL_2.getCode());
//		button2.setName("반송문의");
//
//		final IvrButtonMappingFormRequest button3 = new IvrButtonMappingFormRequest();
//		button3.setButton(Button.DIAL_3.getCode());
//		button3.setName("반품문의");
//
//		final IvrButtonMappingFormRequest button4 = new IvrButtonMappingFormRequest();
//		button4.setButton(Button.DIAL_4.getCode());
//		button4.setName("이전메뉴");
//
//		final IvrButtonMappingFormRequest button5 = new IvrButtonMappingFormRequest();
//		button5.setButton(Button.DIAL_5.getCode());
//		button5.setName("처음단계 ");
//
//		form.setButtons(Arrays.asList(button1, button2, button3, button4, button5));

		form.setParentSeq(1922);
		form.setName("상담원 IVR");
		form.setType(IvrMenuType.CONNECT_MENU_AFTER_DONE_EXCEPTION.getCode());
		form.setSoundCode("38");
		form.setIsWebVoice("N");
		form.setTypeDataStrings(Arrays.asList("busy_context"));

		final IvrButtonMappingFormRequest button0 = new IvrButtonMappingFormRequest();
		button0.setButton(Button.DIAL_0.getCode());
		button0.setName("메뉴다시듣기");

		final IvrButtonMappingFormRequest button1 = new IvrButtonMappingFormRequest();
		button1.setButton(Button.DIAL_1.getCode());
		button1.setName("대표번호 연결");

		final IvrButtonMappingFormRequest button2 = new IvrButtonMappingFormRequest();
		button2.setButton(Button.DIAL_2.getCode());
		button2.setName("직통전화 연결");

		final IvrButtonMappingFormRequest button3 = new IvrButtonMappingFormRequest();
		button3.setButton(Button.DIAL_PRE_STAGE.getCode());
		button3.setName("이전단계");

		form.setButtons(Arrays.asList(button0, button1, button2, button3));

		final WebVoiceItemsFormRequest web = new WebVoiceItemsFormRequest();
		web.setContext("context");

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL + "/{ivrCode}/apply", 13)
				.contentType(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(web)))
				.andDo(print())
				.andExpect(status().isCreated())
//				.andDo(document.document(
//						requestFields(
//							fieldWithPath("name").type(JsonFieldType.STRING).description("IVR명"),
//							fieldWithPath("soundCode").type(JsonFieldType.STRING).description("음원 참조 데이터").optional()
//						),
//						responseFields(
//							fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
//							fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
//							fieldWithPath("data").type(JsonFieldType.NULL).description("").optional(),
//							fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
//						)
//				))
				.andReturn();

		buildHierarchyTree();
	}


	/**
	 * 음원조회
	 * /api/v1/admin/user/tel/service/add-services
	 *
	 * 대표번호 조회
	 * /api/v1/admin/sounds/ivr/add-number-list
	 * NumberType.SERVICE
	 *
	 * 헌트번호 조회
	 * /api/v1/admin/sounds/ivr/add-number-list
	 * NumberType.HUNT
	 *
	 * 내선번호 조회
	 * api/v1/admin/user/tel/phone/add-extensions
	 *
	 * 비연결시 컨텍스트, 예외컨텍스트 조회
	 * /api/v1/admin/service/context/context/add-context
	 *
	 * 음원듣기
	 * /api/v1/admin/sounds/ars/{seq}/resource
	 *
	 */
//	@Test
	protected void put() throws Exception {
		final kr.co.eicn.ippbx.server.model.form.IvrFormUpdateRequest form = new IvrFormUpdateRequest();
		// -- 대표번호연결
		// 메뉴명(*)
		// 음원선택
		// 대표번호선택(*)
//		form.setName("영업대표번호연결");
//		form.setType(IvrTreeTypeNew.LINK_SERVICE_NUMBER.getCode());
////		form.setSoundCode("38");
//		form.setTypeDataStrings(Arrays.asList("07075490101"));

		// -- 헌트번호연결
		// 메뉴명(*)
		// 음원선택
		// 헌트번호선택(*)
		// 재시도횟수(*)
		// 재시도음원
		// 비연결시컨텍스트
//		form.setName("상담센터헌트연결");
//		form.setType(IvrTreeTypeNew.LINK_HUNT.getCode());
//		form.setSoundCode("38");
//		form.setTypeDataStrings(Arrays.asList("00000000006", "3", "TTS", "busy_context"));
//		form.setTtsDataStrings(Arrays.asList("재시도"));

		// -- 내선번호연결
		// 메뉴명(*)
		// 음원선택
		// 내선번호선택(*)
//		form.setName("비서실내선");
//		form.setType(IvrTreeTypeNew.LINK_INNER_PHONE.getCode());
//		form.setSoundCode("38");
//		form.setTypeDataStrings(Arrays.asList(""));

		// -- 음원연결후 종료
		// 메뉴명(*)
		// 음원선택(*)
//		form.setName("휴일비근무음원");
//		form.setType(IvrTreeTypeNew.CLOSE_AFTER_LINK_SOUND.getCode());
//		form.setSoundCode("38");

		// -- 외부번호연결
		// 메뉴명(*)
		// 음원선택
		// 연결될외부번호(*)
//		form.setName("공휴일비근무 안내");
//		form.setType(IvrTreeTypeNew.CONNECTING_BY_EXTERNAL_NUMBER.getCode());
//		form.setSoundCode("38");

		// -- 예외처리후종료
		// 메뉴명(*)
		// 음원선택
		// 예외컨텍스트선택(*)
//		form.setName("광고업무예외");
//		form.setType(IvrTreeTypeNew.CLOSE_MENU_AFTER_EXCEPTION_PROCESSING.getCode());
//		form.setSoundCode("38");

		// -- 예외처리후메뉴연결
		// 메뉴명(*)
		// 예외컨텍스트선택(*)
		// 음원선택(*)
		// 보이는ARS사용
//		form.setName("근무시간IVR");
//		form.setType(IvrTreeTypeNew.LINK_MENU_AFTER_EXCEPTION_PROCESSING.getCode());
//		form.setSoundCode("38");
//		form.setIsWebVoice("N");
//		form.setTypeDataStrings(Arrays.asList("busy_context"));
//
//		final IvrButtonMappingFormRequest button1 = new IvrButtonMappingFormRequest();
//		button1.setButton(Button.DIAL_1.getCode());
//		button1.setName("상품문의");
//
//		final IvrButtonMappingFormRequest button2 = new IvrButtonMappingFormRequest();
//		button2.setButton(Button.DIAL_2.getCode());
//		button2.setName("반송문의");
//
//		form.setButtons(Arrays.asList(button1, button2));

		// -- 예외처리후대표번호연결
		// 메뉴명(*)
		// 예외컨텍스트선택(*)
		// 음원선택
		// 연결번호선택(*)
//		form.setName("영업대표번호연결");
//		form.setType(IvrTreeTypeNew.LINK_SERVICE_NUMBER_AFTER_EXCEPTION_PROCESSING.getCode());
//		form.setSoundCode("38");

		// -- 예외처리후헌트번호연결
		// 메뉴명(*)
		// 예외컨텍스트선택(*)
		// 음원선택
		// 헌트번호선택(*)
		// 재시도횟수(*)
		// 재시도시음원
		// 비연결시컨텍스트
//		form.setName("상담센터헌트연결");
//		form.setType(IvrTreeTypeNew.LINK_HUNT_AFTER_EXCEPTION_PROCESSING.getCode());
//		form.setSoundCode("38");

		form.setName("상담원 IVR");
//		form.setType(IvrTreeTypeNew.LINK_MENU_AFTER_EXCEPTION_PROCESSING.getCode());
		form.setSoundCode("38");
		form.setIsWebVoice("N");
		form.setTypeDataStrings(Arrays.asList("busy_context"));

		final IvrButtonMappingFormRequest button0 = new IvrButtonMappingFormRequest();
		button0.setButton(Button.DIAL_0.getCode());
		button0.setName("메뉴다시듣기");

		final IvrButtonMappingFormRequest button1 = new IvrButtonMappingFormRequest();
		button1.setButton(Button.DIAL_1.getCode());
		button1.setName("대표번호 연결");

		final IvrButtonMappingFormRequest button2 = new IvrButtonMappingFormRequest();
		button2.setButton(Button.DIAL_2.getCode());
		button2.setName("직통전화 연결");

		final IvrButtonMappingFormRequest button3 = new IvrButtonMappingFormRequest();
		button3.setButton(Button.DIAL_PRE_STAGE.getCode());
		button3.setName("이전단계");

		form.setButtons(Arrays.asList(button0, button1, button2, button3));

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/{seq}", 1897)
				.contentType(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.content(mapper.writeValueAsString(form)))
				.andDo(print())
				.andExpect(status().isOk())
//				.andDo(document.document(
//						pathParameters(
//								parameterWithName("code").description("코드키")
//						),
//						requestFields(ivrFormUpdateRequest)
//						.andWithPrefix("buttonMappingFormRequests[]", ivrButtonMappingFormRequest),
//						responseFields(
//								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
//								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
//								fieldWithPath("data").type(JsonFieldType.NULL).description("").optional(),
//								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
//						)
//				))
				.andReturn();

		buildHierarchyTree();
	}

	/**
	 * 하위 트리까지 삭제
	 */
//	@Test
	protected void delete() throws Exception {
		this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{seq}", 1930)
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

		buildHierarchyTree();
	}

//	@Test
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
	protected void copy() throws Exception {
		this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL + "/{sourceId}/{targetId}/copy", 289, 626)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andExpect(status().isOk())
				.andDo(print())
				.andReturn();

		buildHierarchyTree();
	}

	//	@Test
	protected void add_ivr_list() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/add-ivr-list")
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
						)
						.and(fieldWithPath("data").type(JsonFieldType.ARRAY).description("IVR 목록 조회").optional())
						.andWithPrefix("data.[]", ivrTree)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();
	}

	private void printTree(List<kr.co.eicn.ippbx.server.model.dto.eicn.IvrResponse> nodes) {
		for (IvrResponse node : nodes) {
			log.info(prefix(StringUtils.countMatches(node.getTreeName(), '_')) + "({}-{}){}, button:{}, type:{}, tree_name:{}, type_date:{}, tts_data:{}"
					, node.getCode()
					, StringUtils.countMatches(node.getTreeName(), '_')
					, node.getName()
					, Button.of(node.getButton())
					, IvrMenuType.of(node.getType())
					, node.getTreeName()
					, node.getTypeData()
					, node.getTtsData());
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
