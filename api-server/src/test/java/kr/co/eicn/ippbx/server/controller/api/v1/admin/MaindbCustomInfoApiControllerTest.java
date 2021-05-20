package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.model.enums.MultichannelChannelType;
import kr.co.eicn.ippbx.model.form.MaindbCustomInfoFormRequest;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Order;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
public class MaindbCustomInfoApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/application/maindb/custominfo";

    private FieldDescriptor[] customdb_group = new FieldDescriptor[]{
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("그룹SEQ"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("그룹명")
    };

    private FieldDescriptor[] search_item = new FieldDescriptor[]{
            fieldWithPath("fieldId").type(JsonFieldType.STRING).description("검색필드ID"),
            fieldWithPath("fieldInfo").type(JsonFieldType.STRING).description("검색필드명"),
            fieldWithPath("conGroupId").type(JsonFieldType.STRING).description("그룹ID 조회시 사용X").optional()
    };

    private FieldDescriptor[] commonFieldEntity = new FieldDescriptor[]{
            fieldWithPath("fieldId").type(JsonFieldType.STRING).description("그룹명"),
            fieldWithPath("fieldType").type(JsonFieldType.STRING).description("그룹SEQ"),
            fieldWithPath("fieldUse").type(JsonFieldType.STRING).description("그룹SEQ"),
            fieldWithPath("fieldInfo").type(JsonFieldType.STRING).description("그룹SEQ"),
            fieldWithPath("fieldSize").type(JsonFieldType.NUMBER).description("그룹명"),
            fieldWithPath("isneed").type(JsonFieldType.STRING).description("그룹SEQ"),
            fieldWithPath("isdisplay").type(JsonFieldType.STRING).description("그룹SEQ"),
            fieldWithPath("isdisplayList").type(JsonFieldType.STRING).description("그룹명"),
            fieldWithPath("issearch").type(JsonFieldType.STRING).description("그룹SEQ"),
            fieldWithPath("displaySeq").type(JsonFieldType.NUMBER).description("그룹명")
    };

    private FieldDescriptor[] list = new FieldDescriptor[]{
            fieldWithPath("maindbSysCustomId").type(JsonFieldType.STRING).description("고객ID"),
            fieldWithPath("maindbSysUploadDate").type(JsonFieldType.STRING).description("고객등록날짜"),
            fieldWithPath("maindbSysUpdateDate").type(JsonFieldType.STRING).description("고객수정날짜"),
            fieldWithPath("maindbSysGroupId").type(JsonFieldType.NUMBER).description("그룹ID"),
            fieldWithPath("maindbSysGroupType").type(JsonFieldType.NUMBER).description("그룹유형"),
            fieldWithPath("maindbSysResultType").type(JsonFieldType.NUMBER).description("상담결과유형"),
            fieldWithPath("maindbSysDamdangId").type(JsonFieldType.STRING).description("고객담당자ID"),
            fieldWithPath("maindbSysLastResultId").type(JsonFieldType.NUMBER).description("마지막등록ID"),
            fieldWithPath("maindbSysDamdangId").type(JsonFieldType.STRING).description("고객담당자ID"),
            fieldWithPath("maindbSysLastResultDate").type(JsonFieldType.STRING).description("마지막등록날짜"),
            fieldWithPath("maindbSysCompanyId").type(JsonFieldType.STRING).description("등록회사ID"),
            fieldWithPath("maindbDate_1").type(JsonFieldType.STRING).description("DATE_1").optional(),
            fieldWithPath("maindbDate_2").type(JsonFieldType.STRING).description("DATE_2").optional(),
            fieldWithPath("maindbDate_3").type(JsonFieldType.STRING).description("DATE_3").optional(),
            fieldWithPath("maindbDay_1").type(JsonFieldType.STRING).description("DAY_1").optional(),
            fieldWithPath("maindbDay_2").type(JsonFieldType.STRING).description("DAY_2").optional(),
            fieldWithPath("maindbDay_3").type(JsonFieldType.STRING).description("DAY_3").optional(),
            fieldWithPath("maindbDatetime_1").type(JsonFieldType.STRING).description("DATETIME_1").optional(),
            fieldWithPath("maindbDatetime_2").type(JsonFieldType.STRING).description("DATETIME_2").optional(),
            fieldWithPath("maindbDatetime_3").type(JsonFieldType.STRING).description("DATETIME_3").optional(),
            fieldWithPath("maindbInt_1").type(JsonFieldType.NUMBER).description("INT_1").optional(),
            fieldWithPath("maindbInt_2").type(JsonFieldType.NUMBER).description("INT_2").optional(),
            fieldWithPath("maindbInt_3").type(JsonFieldType.NUMBER).description("INT_3").optional(),
            fieldWithPath("maindbInt_4").type(JsonFieldType.NUMBER).description("INT_4").optional(),
            fieldWithPath("maindbInt_5").type(JsonFieldType.NUMBER).description("INT_5").optional(),
            fieldWithPath("maindbString_1").type(JsonFieldType.STRING).description("STRING_1").optional(),
            fieldWithPath("maindbString_2").type(JsonFieldType.STRING).description("STRING_2").optional(),
            fieldWithPath("maindbString_3").type(JsonFieldType.STRING).description("STRING_3").optional(),
            fieldWithPath("maindbString_4").type(JsonFieldType.STRING).description("STRING_4").optional(),
            fieldWithPath("maindbString_5").type(JsonFieldType.STRING).description("STRING_5").optional(),
            fieldWithPath("maindbString_6").type(JsonFieldType.STRING).description("STRING_6").optional(),
            fieldWithPath("maindbString_7").type(JsonFieldType.STRING).description("STRING_7").optional(),
            fieldWithPath("maindbString_8").type(JsonFieldType.STRING).description("STRING_8").optional(),
            fieldWithPath("maindbString_9").type(JsonFieldType.STRING).description("STRING_9").optional(),
            fieldWithPath("maindbString_10").type(JsonFieldType.STRING).description("STRING_10").optional(),
            fieldWithPath("maindbString_11").type(JsonFieldType.STRING).description("STRING_11").optional(),
            fieldWithPath("maindbString_12").type(JsonFieldType.STRING).description("STRING_12").optional(),
            fieldWithPath("maindbString_13").type(JsonFieldType.STRING).description("STRING_13").optional(),
            fieldWithPath("maindbString_14").type(JsonFieldType.STRING).description("STRING_14").optional(),
            fieldWithPath("maindbString_15").type(JsonFieldType.STRING).description("STRING_15").optional(),
            fieldWithPath("maindbString_16").type(JsonFieldType.STRING).description("STRING_16").optional(),
            fieldWithPath("maindbString_17").type(JsonFieldType.STRING).description("STRING_17").optional(),
            fieldWithPath("maindbString_18").type(JsonFieldType.STRING).description("STRING_18").optional(),
            fieldWithPath("maindbString_19").type(JsonFieldType.STRING).description("STRING_19").optional(),
            fieldWithPath("maindbString_20").type(JsonFieldType.STRING).description("STRING_20").optional(),
            fieldWithPath("maindbCode_1").type(JsonFieldType.STRING).description("CODE_1").optional(),
            fieldWithPath("maindbCode_2").type(JsonFieldType.STRING).description("CODE_2").optional(),
            fieldWithPath("maindbCode_3").type(JsonFieldType.STRING).description("CODE_3").optional(),
            fieldWithPath("maindbCode_4").type(JsonFieldType.STRING).description("CODE_4").optional(),
            fieldWithPath("maindbCode_5").type(JsonFieldType.STRING).description("CODE_5").optional(),
            fieldWithPath("maindbCode_6").type(JsonFieldType.STRING).description("CODE_6").optional(),
            fieldWithPath("maindbCode_7").type(JsonFieldType.STRING).description("CODE_7").optional(),
            fieldWithPath("maindbCode_8").type(JsonFieldType.STRING).description("CODE_8").optional(),
            fieldWithPath("maindbCode_9").type(JsonFieldType.STRING).description("CODE_9").optional(),
            fieldWithPath("maindbCode_10").type(JsonFieldType.STRING).description("CODE_10").optional(),
            fieldWithPath("maindbMulticode_1").type(JsonFieldType.STRING).description("MULTICODE_1").optional(),
            fieldWithPath("maindbMulticode_2").type(JsonFieldType.STRING).description("MULTICODE_2").optional(),
            fieldWithPath("maindbMulticode_3").type(JsonFieldType.STRING).description("MULTICODE_3").optional(),
            fieldWithPath("maindbConcode_1").type(JsonFieldType.STRING).description("CONCODE_1").optional(),
            fieldWithPath("maindbConcode_2").type(JsonFieldType.STRING).description("CONCODE_2").optional(),
            fieldWithPath("maindbConcode_3").type(JsonFieldType.STRING).description("CONCODE_3").optional(),
            fieldWithPath("maindbCscode_1").type(JsonFieldType.STRING).description("CSCODE_1").optional(),
            fieldWithPath("maindbCscode_2").type(JsonFieldType.STRING).description("CSCODE_2").optional(),
            fieldWithPath("maindbCscode_3").type(JsonFieldType.STRING).description("CSCODE_3").optional(),
            fieldWithPath("multiChannelInfo").type(JsonFieldType.ARRAY).description("MULTIINFO").optional()
    };

    private FieldDescriptor[] multichannelRequest = new FieldDescriptor[]{
            fieldWithPath("channelType").type(JsonFieldType.STRING).description("유형구분(EMAIL: 이메일, PHONE: 고객번호, TALK: 상담톡ID)"),
            fieldWithPath("channelData").type(JsonFieldType.STRING).description("유형데이터"),
    };

    //고객그룹조회
//    @Test
    protected void customdb_group() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/customdb_group")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                                .and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("고객DB그룹 목록").optional())
                                .andWithPrefix("data.[]", customdb_group)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }

    //검색항목조회
    //@Test
    protected void search_item() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/search_item")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                                .and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("검색항목 목록").optional())
                                .andWithPrefix("data.[]", search_item)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }

    /**
     * 고객데이터 리스트
     */
    //@Test
    @Order(1)
    public void getPagination() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{groupSeq}/data", 3)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("page", "1")
                .param("limit", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                /*
                .andDo(document.document(
                        requestParameters(
                                parameterWithName("page").description("현재 페이지"),
                                parameterWithName("limit").description("페이지 개수"),
                                parameterWithName("search_group").description("고객DB_ID").optional(),
                                parameterWithName("search_type").description("조회필드명").optional(),
                                parameterWithName("search_text1").description("조회값1").optional(),
                                parameterWithName("search_text2").description("조회값2").optional()
                        ),
                        responseFields(getCommonFieldDescriptors())
                                .andWithPrefix("data.rows[]", list)
                                .andWithPrefix("data.",
                                        fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지"),
                                        fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총갯수"))
                ))

                 */
                .andReturn();
    }

    /**
     * 고객데이터 필드조회
     */
//    @Test
    public void getType() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{type}", 7)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())/*
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("type").description("유형 키")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("data").description("실제 반환될 데이터").optional()
                        )
                                .andWithPrefix("data.", commonFieldEntity)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))*/
                .andReturn();
    }

    /**
     * 고객데이터 추가
     */
    //@Test
    @Order(1)
    protected void post() throws Exception {
        final MaindbCustomInfoFormRequest form = new MaindbCustomInfoFormRequest();
        form.setGroupSeq(18);     //그룹SEQ
        form.setString_1("Insert TEST");    //고객정보유형
        form.setString_2("test22");    //상담결과유형
        final MaindbCustomInfoFormRequest.ChannelForm channelForm = new MaindbCustomInfoFormRequest.ChannelForm();
        channelForm.setType(MultichannelChannelType.PHONE);
        channelForm.setValue("01090818141");
        form.setChannels(Arrays.asList(channelForm));


        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(form))
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isCreated())
                /*문서작성필요.*/
                .andReturn();
    }

    /**
     * 고객데이터 수정
     */
    //@Test
    @Order(1)
    protected void put() throws Exception {
        final MaindbCustomInfoFormRequest form = new MaindbCustomInfoFormRequest();
        form.setGroupSeq(3);     //그룹SEQ
        form.setString_1("Update TEST");    //고객정보유형
        form.setString_2("test3333");    //상담결과유형
        final MaindbCustomInfoFormRequest.ChannelForm channelForm = new MaindbCustomInfoFormRequest.ChannelForm();
        channelForm.setType(MultichannelChannelType.PHONE);
        channelForm.setValue("01090818140");
        form.setChannels(Arrays.asList(channelForm));

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/{id}", "CST_820200511214755")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(form))
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                /*
                    문서작성 필요.
                 */
                .andReturn();
    }

    /**
     * 고객데이터 삭제
     */

    //@Test
    @Order(1)
    public void deleteData() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{id}", "CST_320200428100202")
                .with(new JwtRequestPostProcessor()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("result").value("success"))
                /*
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

                 */
                .andReturn();
    }

}
