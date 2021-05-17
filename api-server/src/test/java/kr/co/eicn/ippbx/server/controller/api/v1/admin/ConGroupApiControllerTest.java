package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.server.model.form.ConGroupFormRequest;
import kr.co.eicn.ippbx.server.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.fileUpload;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class ConGroupApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/application/connect/group";

    private FieldDescriptor[] conGroupSummaryResponse = new FieldDescriptor[] {
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("시퀀스"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("연동DB 그룹명"),
            fieldWithPath("conType").type(JsonFieldType.NUMBER).description("연동DB 유형"),
            fieldWithPath("info").type(JsonFieldType.STRING).description("추가정보"),
            fieldWithPath("totalCnt").type(JsonFieldType.NUMBER).description("데이터수"),
            fieldWithPath("groupCode").type(JsonFieldType.STRING).description("그룹코드"),
            fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("그룹이름"),
            fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("그룹레벨"),
            fieldWithPath("groupTreeNames").type(JsonFieldType.ARRAY).description("계층 구조에 따른 array"),
            fieldWithPath("commonFields").type(JsonFieldType.ARRAY).description("업로드필드유형, 종류").optional()
    };

    private FieldDescriptor[] commonFieldDetailResponse = new FieldDescriptor[] {
            fieldWithPath("fieldInfo").type(JsonFieldType.STRING).description("업로드필드유형"),
            fieldWithPath("fieldType").type(JsonFieldType.STRING).description("업로드필드유형 종류")
    };

    private FieldDescriptor[] commonTypeResponse = new FieldDescriptor[] {
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("시퀀스"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("유형명")
    };

//      @Test
    protected void pagination() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("page", "1")
                .param("limit", "10")
                .param("conType", "")
                .param("name", ""))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestParameters(
                                parameterWithName("page").description("현재 페이지"),
                                parameterWithName("limit").description("페이지 개수"),
                                parameterWithName("conType").description("연동DB 유형").optional(),
                                parameterWithName("name").description("그룹명").optional()
                        ),
                        responseFields(
                                fieldWithPath("data.rows[]").description("연동DB 그룹관리").optional(),
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional(),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("실제 반환될 데이터").optional()
                        )
                                .andWithPrefix("data.rows[]", conGroupSummaryResponse)
                                .andWithPrefix("data.",
                                        fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지"),
                                        fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총갯수"))
                ))
                .andReturn();
    }

//    @Test
    protected void get() throws Exception {
        final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{seq}", 1)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("seq").description("아이디")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                                .and(fieldWithPath("data").type(JsonFieldType.OBJECT).description("지식관리 상세"))
                                .andWithPrefix("data.", conGroupSummaryResponse)
                                .andWithPrefix("data.commonFields[]", commonFieldDetailResponse)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }

//       @Test
    protected void post() throws Exception {
        final ConGroupFormRequest form = new ConGroupFormRequest();
        form.setName("con");        //그룹명
        form.setConType(1);    //연동DB 유형
        form.setInfo("");         //추가정보
        form.setGroupCode("");    //그룹코드

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(form))
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document.document(
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("그룹명"),
                                fieldWithPath("conType").type(JsonFieldType.NUMBER).description("연동DB 유형"),
                                fieldWithPath("info").type(JsonFieldType.STRING).description("추가정보"),
                                fieldWithPath("groupCode").type(JsonFieldType.STRING).description("그룹코드")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("data").type(JsonFieldType.NUMBER).description("실제 반환될 데이터").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                        )
                ))
                .andReturn();
    }

//       @Test
    protected void put() throws Exception {
        final ConGroupFormRequest form = new ConGroupFormRequest();
        form.setName("고객수정");        //그룹명
        form.setConType(1);    //연동DB 유형
        form.setInfo("info수정");         //추가정보
        form.setGroupCode("0002");    //그룹코드

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/{seq}", 3)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(form))
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("seq").description("시퀀스")
                        ),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("그룹명"),
                                fieldWithPath("conType").type(JsonFieldType.NUMBER).description("연동DB 유형"),
                                fieldWithPath("info").type(JsonFieldType.STRING).description("추가정보"),
                                fieldWithPath("groupCode").type(JsonFieldType.STRING).description("그룹코드")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("data").type(JsonFieldType.NUMBER).description("실제 반환될 데이터").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                        )
                ))
                .andReturn();
    }

//       @Test
    public void delete() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{seq}", 3)
                .with(new JwtRequestPostProcessor()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("result").value("success"))
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("seq").description("시퀀스")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("data").type(JsonFieldType.NULL).description(""),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                        )
                ))
                .andReturn();
    }

    /**
     * 연동DB 유형 목록조회
     */
//    	@Test
    protected void conType() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/con-type")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                                .and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("연동DB 유형 목록").optional())
                                .andWithPrefix("data.[]", commonTypeResponse)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }
}
