package kr.co.eicn.ippbx.server.controller.api;

import kr.co.eicn.ippbx.server.model.form.UserMenuSequenceUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class MenuCompanyApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/menu";

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

//    @Test
    protected void list() throws Exception {

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{userid}/{menuCode}/user-menu", "master", "0048")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
//                .param("processKind", "sequenceList")
        )
                .andDo(print())
                .andExpect(status().isOk())
//                .andDo(document.document(
//                        responseFields(
//                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
//                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
//                        )
//                                .and(fieldWithPath("data").type(JsonFieldType.OBJECT).description("지식관리 상세"))
//                                .andWithPrefix("data.", displayScreenResponse)
//                                .andWithPrefix("data.memberStatusResponses.", memberStatusResponse)
//                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
//                ))
                .andReturn();
    }

//    @Test
    protected void put() throws Exception {
        final UserMenuSequenceUpdateRequest form = new UserMenuSequenceUpdateRequest();
        final List<String> sequenceList = new ArrayList<>();
        sequenceList.add("3497");
        sequenceList.add("3498");
        sequenceList.add("3499");
        form.setMenuCodes(sequenceList);
//        final MenuFormRequest form = new MenuFormRequest();
//        form.setMenuName("도움말");
//        form.setView(Bool.Y.getValue());
//        form.setMenuActionExeId("");
//        form.setGroupLevelAuth(GroupLevelAuth.ALLOW_YOURSELF_ONLY.getCode());

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/{userid}/user-menu-sequence/{parentSeq}", "user01", 3496)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .content(mapper.writeValueAsString(form)))
                .andDo(print())
                .andExpect(status().isOk())
//                .andDo(document.document(
//                        responseFields(
//                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
//                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
//                        )
//                                .and(fieldWithPath("data").type(JsonFieldType.OBJECT).description("지식관리 상세"))
//                                .andWithPrefix("data.", displayScreenResponse)
//                                .andWithPrefix("data.memberStatusResponses.", memberStatusResponse)
//                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
//                ))
                .andReturn();
    }
}
