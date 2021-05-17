package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.server.model.enums.DupKeyKind;
import kr.co.eicn.ippbx.server.model.enums.IsDupNeedYn;
import kr.co.eicn.ippbx.server.model.form.MaindbGroupFormRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.*;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class MaindbGroupApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/application/maindb/group";

    private FieldDescriptor[] maindbGroupSummaryResponse = new FieldDescriptor[] {
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("시퀀스"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("그룹명"),
            fieldWithPath("info").type(JsonFieldType.STRING).description("추가정보"),
            fieldWithPath("makeDate").type(JsonFieldType.STRING).description("그룹생성일").optional(),
            fieldWithPath("maindbType").type(JsonFieldType.NUMBER).description("고객정보유형"),
            fieldWithPath("resultType").type(JsonFieldType.NUMBER).description("상담결과유형"),
            fieldWithPath("maindbName").type(JsonFieldType.STRING).description("고객정보 유형명"),
            fieldWithPath("resultName").type(JsonFieldType.STRING).description("상담결과 유형명"),
            fieldWithPath("lastUploadDate").type(JsonFieldType.STRING).description("마지막업로드날짜").optional(),
            fieldWithPath("isDupUse").type(JsonFieldType.VARIES).description("체크여부(Y: 체크함, N: 체크안함)").optional(),
            fieldWithPath("dupKeyKind").type(JsonFieldType.VARIES).description("체크항목(FN: 전화번호+필수항목, F: 필수항목, N: 전화번호)").optional(),
            fieldWithPath("dupNeedField").type(JsonFieldType.NUMBER).description("필수항목").optional(),
            fieldWithPath("dupIsUpdate").type(JsonFieldType.BOOLEAN).description("업로드시 처리방법(Y: 업데이트, N: 처리안함)").optional(),
            fieldWithPath("totalCnt").type(JsonFieldType.NUMBER).description("데이터수"),
            fieldWithPath("uploadTryCnt").type(JsonFieldType.NUMBER).description("업로드횟수").optional(),
            fieldWithPath("lastUploadStatus").type(JsonFieldType.BOOLEAN).description("마지막업로드상태(Y: 업로드완료, N:업로드안함)").optional(),
            fieldWithPath("groupCode").type(JsonFieldType.STRING).description("그룹코드"),
            fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("그룹이름"),
            fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("그룹레벨"),
            fieldWithPath("groupTreeNames").type(JsonFieldType.ARRAY).description("계층 구조에 따른 array")
    };

    private FieldDescriptor[] maindbGroupDetailResponse = new FieldDescriptor[] {
            fieldWithPath("commonFields").type(JsonFieldType.ARRAY).description("업로드유형필드, 종류")
    };

    private FieldDescriptor[] commonFieldDetailResponse = new FieldDescriptor[] {
            fieldWithPath("fieldInfo").type(JsonFieldType.STRING).description("업로드유형필드"),
            fieldWithPath("fieldType").type(JsonFieldType.STRING).description("업로드유형필드 종류")
    };

    private FieldDescriptor[] commonTypeResponse = new FieldDescriptor[] {
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("시퀀스"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("유형명")
    };

//    @Test
    protected void pagination() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("page", "1")
                .param("limit", "10")
                .param("maindbType", "")
                .param("name", ""))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestParameters(
                                parameterWithName("page").description("현재 페이지"),
                                parameterWithName("limit").description("페이지 개수"),
                                parameterWithName("maindbType").description("고객정보유형").optional(),
                                parameterWithName("name").description("그룹명").optional()
                        ),
                        responseFields(
                                fieldWithPath("data.rows[]").description("고객DB 그룹관리").optional(),
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional(),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("실제 반환될 데이터").optional()
                        )
                                .andWithPrefix("data.rows[]", maindbGroupSummaryResponse)
                                .andWithPrefix("data.",
                                        fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지"),
                                        fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총갯수"))
                ))
                .andReturn();
    }

//       @Test
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
                                .and(fieldWithPath("data").type(JsonFieldType.OBJECT).description("고객DB 그룹관리 상세"))
                                .andWithPrefix("data.", maindbGroupSummaryResponse)
                                .andWithPrefix("data.", maindbGroupDetailResponse)
                                .andWithPrefix("data.commonFields[]", commonFieldDetailResponse)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }

//      @Test
    protected void post() throws Exception {
        final MaindbGroupFormRequest form = new MaindbGroupFormRequest();
        form.setName("maindbTest");        //그룹명
        form.setMaindbType(38);    //고객정보유형
        form.setResultType(5);    //상담결과유형
        form.setIsDupUse(IsDupNeedYn.SKIP.getCode());
        form.setDupKeyKind(DupKeyKind.NUMBER.getCode());
        form.setDupIsUpdate(IsDupNeedYn.SKIP.getCode());
        form.setInfo("");         //추가정보
//        form.setGroupCode("0002");    //그룹코드
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(form))
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isCreated())
//                .andDo(document.document(
//                        requestFields(
//                                fieldWithPath("name").type(JsonFieldType.STRING).description("그룹명"),
//                                fieldWithPath("maindbType").type(JsonFieldType.NUMBER).description("고객정보유형"),
//                                fieldWithPath("resultType").type(JsonFieldType.NUMBER).description("상담결과유형"),
//                                fieldWithPath("info").type(JsonFieldType.STRING).description("추가정보"),
//                                fieldWithPath("groupCode").type(JsonFieldType.STRING).description("그룹코드")
//                        ),
//                        responseFields(
//                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
//                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
//                                fieldWithPath("data").type(JsonFieldType.NUMBER).description("실제 반환될 데이터").optional(),
//                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
//                        )
//                ))
                .andReturn();
    }

//      @Test
    protected void put() throws Exception {
        final MaindbGroupFormRequest form = new MaindbGroupFormRequest();
        form.setName("maindb수정");        //그룹명
        form.setMaindbType(1);    //고객정보유형
        form.setResultType(1);    //상담결과유형
        form.setInfo("정보수정");         //추가정보
        form.setGroupCode("0007");    //그룹코드

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/{seq}", 1)
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
                                fieldWithPath("maindbType").type(JsonFieldType.NUMBER).description("고객정보유형"),
                                fieldWithPath("resultType").type(JsonFieldType.NUMBER).description("상담결과유형"),
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

//    	@Test
    protected void delete() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{seq}", 1)
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
     * 고객정보유형 목록조회
     */
//    	@Test
    protected void maindbType() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/maindb-type")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                                .and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("고객정보유형 목록").optional())
                                .andWithPrefix("data.[]", commonTypeResponse)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }

    /**
     * 상담결과유형 목록조회
     */
//    	@Test
    protected void resultType() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/result-type")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                                .and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("상담결과유형 목록").optional())
                                .andWithPrefix("data.[]", commonTypeResponse)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }
}
