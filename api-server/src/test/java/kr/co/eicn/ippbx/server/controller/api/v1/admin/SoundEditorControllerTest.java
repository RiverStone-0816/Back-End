package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.model.form.SoundEditorFormRequest;
import kr.co.eicn.ippbx.model.form.SoundEditorListenRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class SoundEditorControllerTest extends BaseControllerTest {
	private final String TEST_URL = "/api/v1/admin/sounds/editor";

	private FieldDescriptor[] soundEditorFormRequest = new FieldDescriptor[] {
			fieldWithPath("playSpeed").type(JsonFieldType.NUMBER).description("재생속도(100:정상, 70:느리게, 40:많이 느리게, 130:빠르게, 180:많이빠르게)"),
			fieldWithPath("soundName").type(JsonFieldType.STRING).description("음원명/컬러링명"),
			fieldWithPath("fileName").type(JsonFieldType.STRING).description("파일명"),
			fieldWithPath("comment").type(JsonFieldType.STRING).description("음원 TEXT"),
	};

	private FieldDescriptor[] soundEditorListenRequest = new FieldDescriptor[] {
			fieldWithPath("playSpeed").type(JsonFieldType.NUMBER).description("재생속도(100:정상, 70:느리게, 40:많이 느리게, 130:빠르게, 180:많이빠르게)"),
			fieldWithPath("comment").type(JsonFieldType.STRING).description("음원 TEXT"),
	};

//	@Test
	public void make() throws Exception {
		final SoundEditorFormRequest form = new SoundEditorFormRequest();
		form.setPlaySpeed(100);
		form.setSoundName("ARS5");
		form.setFileName("wait2");
		form.setComment("안녕하세요 반갑습니다. 날씨가 춥네요. 감기 조심하세요.");

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL + "/make")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(form))
				.param("soundType", "sound")
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isCreated())
				.andDo(document.document(
						requestParameters(
								parameterWithName("soundType").description("음원타입(sound:ARS, moh:컬러링)")
						),
						requestFields(soundEditorFormRequest),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").type(JsonFieldType.NUMBER).description("SEQUENCE KEY").optional()
						)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();
	}

//	@Test
	public void preListen() throws Exception {
		final SoundEditorListenRequest form = new SoundEditorListenRequest();
		form.setPlaySpeed(100);
		form.setComment("미리듣기입니다.");

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL + "/pre-listen")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(form))
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						requestFields(soundEditorListenRequest),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").type(JsonFieldType.STRING).description("리소스 URL").optional()
						)
						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();

		final String url = getData(result, String.class);
		log.info("resource url {}", url);
	}

//	@Test
	public void resource() throws Exception {
		this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/resource")
				.contentType(MediaType.APPLICATION_JSON)
				.param("token", getAuthToken())
				.param("fileName", "demo_master.wav"))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						requestParameters(
								parameterWithName("token").description("토큰 키"),
								parameterWithName("fileName").description("파일명")
						),
						responseBody()
				))
				.andReturn();
	}
}
