package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.server.model.dto.eicn.MonitorDisplayScreenType2Response;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
public class MonitorDisplayScreenType2ControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/monitor/screen/screen-type-2";

//    @Test
    protected void list() throws Exception {
        final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
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

        final MonitorDisplayScreenType2Response data = getData(result, MonitorDisplayScreenType2Response.class);

        log.info("data {}", data);
    }
}
