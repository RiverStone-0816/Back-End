package kr.co.eicn.ippbx.server.security;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.model.enums.WebSecureActionType;
import kr.co.eicn.ippbx.model.form.LoginRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class AuthenticationTest extends BaseControllerTest {

//	@Test
	public void authenticateSuccess() throws Exception {
		final LoginRequest authenticationRequest = new LoginRequest();
		authenticationRequest.setCompany("premium");
		authenticationRequest.setId("master");
		authenticationRequest.setPassword("orange90");
		authenticationRequest.setExtension("");
		authenticationRequest.setActionType(WebSecureActionType.LOGIN.getCode());

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post("/auth/authenticate")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(authenticationRequest)))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
					requestFields(
						fieldWithPath("company").type(JsonFieldType.STRING).description("고객사 아이디"),
						fieldWithPath("id").type(JsonFieldType.STRING).description("고객 아이디"),
						fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
						fieldWithPath("extension").type(JsonFieldType.STRING).description("내선번호").optional(),
						fieldWithPath("actionType").type(JsonFieldType.STRING).description("로그인 시도여부")
					),
					responseFields(
							fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
							fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
							fieldWithPath("data").type(JsonFieldType.STRING).description("토큰정보"),
							fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
					)
				))
				.andReturn();

		final String token = getData(result, String.class);
		log.info(token);
	}
}
