package kr.co.eicn.ippbx.server.controller.api.v1;

import kr.co.eicn.ippbx.model.search.ChattingMemberSearchRequest;
import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.model.form.ChattingMemberFormRequest;
import kr.co.eicn.ippbx.model.search.ChattingSearchRequest;
import lombok.extern.log4j.Log4j2;
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
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
public class ChattingRoomApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/chatt";

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
        ChattingSearchRequest search = new ChattingSearchRequest();
        search.setStartMessageId("user620_20200927205750_user620_20201007161159_148");
        search.setEndMessageId("user620_20201022064456_user03_20201028112602_646");
        search.setMessage("ㅎㅎ");
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(new JwtRequestPostProcessor())
//                .param("startMessageId", "user620_20201022064456_user03_20201028112602_646")
//                .param("endMessageId", "user620_20201022064456_user03_20201028110524_932")
//                .param("message", "ㅇㅇ")
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
    protected void post() throws Exception {
        final ChattingMemberFormRequest form = new ChattingMemberFormRequest();
        List<String> members  = new ArrayList<>();
//        members.add("master");
//        members.add("user03");
//        members.add("user01");
//        members.add("user620");
        members.add("user621");
        members.add("lee");
        form.setMemberList(members);
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{roomId}", "master_20201015170515")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
//                .param("newRoomName", "단체")
//                .content(mapper.writeValueAsString(form))
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
//                .andDo(document.document(
//                        responseFields(
//                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
//                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional())
//                                .and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("메뉴목록").optional())
//                                .andWithPrefix("data.[]", menuCompanyResponse)
//                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
//                ))
                .andReturn();
    }

//    @Test
    protected void bookmark_list() throws Exception {
        final ChattingMemberSearchRequest form = new ChattingMemberSearchRequest();
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/bookmark-list")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
//                .param("newRoomName", "단체")
//                .content(mapper.writeValueAsString(form))
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        log.info("result : " + result);
    }


}
