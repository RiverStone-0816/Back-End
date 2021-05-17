package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.server.model.form.MonitControlChangeRequest;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
public class MonitControlApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/service/etc/monit";

    private FieldDescriptor[] monitControlResponse = new FieldDescriptor[] {
            fieldWithPath("groupCode").type(JsonFieldType.STRING).description("그룹코드").optional(),
            fieldWithPath("groupName").type(JsonFieldType.STRING).description("그룹명").optional(),
            fieldWithPath("person").type(JsonFieldType.ARRAY).description("상담원목록").optional()
    };

    private FieldDescriptor[] personListResponse = new FieldDescriptor[] {
            fieldWithPath("id").type(JsonFieldType.STRING).description("상담원아이디"),
            fieldWithPath("idName").type(JsonFieldType.STRING).description("상담원명"),
            fieldWithPath("extension").type(JsonFieldType.STRING).description("내선번호"),
            fieldWithPath("peer").type(JsonFieldType.STRING).description("개인번호"),
            fieldWithPath("paused").type(JsonFieldType.STRING).description("상태")
    };

//    @Test
    public void list() throws Exception{
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("groupCode","0015"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestParameters(
                                parameterWithName("groupCode").description("그룹코드")
                       ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional(),
                                fieldWithPath("data").description("실제 반환될 데이터").optional()
                        )
                                .andWithPrefix("data.[]",monitControlResponse)
                                .andWithPrefix("data.[].person.[]",personListResponse)
                ))
                .andReturn();

        log.info(result);
    }

//    @Test
    public void put() throws Exception {
        final MonitControlChangeRequest form = new MonitControlChangeRequest();
//        form.setPeer("75490790");
//        form.setPaused("REST");
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .content(mapper.writeValueAsString(form)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestFields(
                                fieldWithPath("peer").type(JsonFieldType.STRING).description("개인번호"),
                                fieldWithPath("paused").type(JsonFieldType.STRING).description("상담원상태")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional(),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("실제 반환될 데이터").optional()
                        )
                ))
                .andReturn();

        log.info(result);
    }

}
