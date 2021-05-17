package kr.co.eicn.ippbx.server.controller.api.v1;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.server.model.dto.configdb.MenuCompanyResponse;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
public class MenuCompanyApiControllerTest extends BaseControllerTest {
	private final String TEST_URL = "/api/menu";

	private FieldDescriptor[] menuBasicResponse = new FieldDescriptor[] {
			fieldWithPath("menuCode").type(JsonFieldType.STRING).description("메뉴코드"),
			fieldWithPath("menuName").type(JsonFieldType.STRING).description("메뉴명"),
			fieldWithPath("menuTreeName").type(JsonFieldType.STRING).description("윗레벨의 코드를 포함한 코드의 나열 ex>0003_0008_0001"),
			fieldWithPath("menuLevel").type(JsonFieldType.NUMBER).description("메뉴레벨"),
			fieldWithPath("parentMenuCode").type(JsonFieldType.STRING).description("상위메뉴코드").optional(),
			fieldWithPath("parentTreeName").type(JsonFieldType.STRING).description("윗레벨 코드의 나열 ex>0003_0008").optional(),
			fieldWithPath("menuActionExeId").type(JsonFieldType.STRING).description("연결액션").optional(),
			fieldWithPath("sequence").type(JsonFieldType.NUMBER).description("정렬순서"),
			fieldWithPath("viewYn").type(JsonFieldType.STRING).description("보임여부"),
			fieldWithPath("icon").type(JsonFieldType.STRING).description(""),
			fieldWithPath("actionType").type(JsonFieldType.STRING).description("PAGE, MENU, POP"),
			fieldWithPath("authType").type(JsonFieldType.STRING).description("권한타입"),
			fieldWithPath("groupLevelAuthYn").type(JsonFieldType.STRING).description("조직권한부여여부(A:모든조직허용, G:권한을 부여받은 조직만 허용, M:자신것만허용)").optional(),
			fieldWithPath("service").type(JsonFieldType.STRING).description("관련서비스유형(서비스 코드에 등록된 코드)"),
			fieldWithPath("serviceKind").type(JsonFieldType.STRING).description("")
	};

	private FieldDescriptor[] menuCompanyResponse = new FieldDescriptor[] {
			fieldWithPath("seq").type(JsonFieldType.NUMBER).description("SEQUENCE KEY"),
			fieldWithPath("userid").type(JsonFieldType.STRING).description("사용자아이디"),
			fieldWithPath("menuCode").type(JsonFieldType.STRING).description("메뉴코드"),
			fieldWithPath("menuName").type(JsonFieldType.STRING).description("메뉴명"),
			fieldWithPath("menuTreeName").type(JsonFieldType.STRING).description("윗레벨의 코드를 포함한 코드의 나열 ex>0003_0008_0001"),
			fieldWithPath("menuLevel").type(JsonFieldType.NUMBER).description("메뉴레벨"),
			fieldWithPath("parentMenuCode").type(JsonFieldType.STRING).description("상위메뉴코드").optional(),
			fieldWithPath("parentTreeName").type(JsonFieldType.STRING).description("윗레벨 코드의 나열 ex>0003_0008").optional(),
			fieldWithPath("menuActionExeId").type(JsonFieldType.STRING).description("연결액션").optional(),
			fieldWithPath("sequence").type(JsonFieldType.NUMBER).description("정렬순서"),
			fieldWithPath("viewYn").type(JsonFieldType.STRING).description("보임여부"),
			fieldWithPath("icon").type(JsonFieldType.STRING).description(""),
			fieldWithPath("actionType").type(JsonFieldType.STRING).description("PAGE, MENU, POP"),
			fieldWithPath("authType").type(JsonFieldType.STRING).description("권한타입"),
			fieldWithPath("groupLevelAuthYn").type(JsonFieldType.STRING).description("조직권한부여여부(A:모든조직허용, G:권한을 부여받은 조직만 허용, M:자신것만허용)").optional(),
			fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드").optional(),
			fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("조직레벨"),
	};

//	@Test
	protected void get_menus() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document.document(
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional())
								.and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("메뉴목록").optional())
								.andWithPrefix("data.[]", menuCompanyResponse)
								.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();

		final List<MenuCompanyResponse> responseData = listData(result, MenuCompanyResponse.class);
		for (MenuCompanyResponse menu : responseData) {
			log.info("{} {}.,  메뉴명: {}", (prefix(menu.getMenuLevel())), menu.getMenuLevel(), menu.getMenuName());
		}
	}

//	@Test
	protected void get_default_menus() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/default")
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document.document(
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional())
								.and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("메뉴목록").optional())
								.andWithPrefix("data.[]", menuBasicResponse)
								.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();
	}

//	@Test
	protected void get_user_menus() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/user")
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document.document(
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional())
								.and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("메뉴목록").optional())
								.andWithPrefix("data.[]", menuCompanyResponse)
								.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
				))
				.andReturn();

		final List<MenuCompanyResponse> responseData = listData(result, MenuCompanyResponse.class);
		for (MenuCompanyResponse menu : responseData) {
			log.info("{} {}.,  메뉴명: {}", (prefix(menu.getMenuLevel())), menu.getMenuLevel(), menu.getMenuName());
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
