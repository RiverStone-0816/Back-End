package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.model.form.ContextInfoFormRequest;
import kr.co.eicn.ippbx.model.form.WebVoiceItemsDtmfFormRequest;
import kr.co.eicn.ippbx.model.form.WebVoiceItemsFormRequest;
import kr.co.eicn.ippbx.model.form.WebVoiceItemsInputFormRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class ContextInfoApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/service/context/context";

    private FieldDescriptor[] contextInfoResponse = new FieldDescriptor[] {
            fieldWithPath("context").type(JsonFieldType.STRING).description("컨텍스트명").optional(),
            fieldWithPath("name").type(JsonFieldType.STRING).description("컨텍스트한글명").optional(),
            fieldWithPath("isWebVoice").type(JsonFieldType.STRING).description("보이는 ARS 여부").optional()
    };

    private FieldDescriptor[] webVoiceInfoResponse = new FieldDescriptor[] {
            fieldWithPath("context").type(JsonFieldType.STRING).description("컨텍스트명").optional(),
            fieldWithPath("headerUse").type(JsonFieldType.STRING).description("헤더사용여부").optional(),
            fieldWithPath("textareaUse").type(JsonFieldType.STRING).description("텍스트사용여부").optional(),
            fieldWithPath("inputareaUse").type(JsonFieldType.STRING).description("입력사용여부").optional(),
            fieldWithPath("controlUse").type(JsonFieldType.STRING).description("컨트롤사용여부").optional(),
            fieldWithPath("items").type(JsonFieldType.ARRAY).description("내용").optional()
    };

    private FieldDescriptor[] webVoiceItemsResponse = new FieldDescriptor[] {
            fieldWithPath("areaType").type(JsonFieldType.STRING).description("구분").optional(),
            fieldWithPath("itemType").type(JsonFieldType.STRING).description("내용구분").optional(),
            fieldWithPath("itemName").type(JsonFieldType.STRING).description("내용").optional(),
            fieldWithPath("itemValue").type(JsonFieldType.STRING).description("값").optional(),
            fieldWithPath("isView").type(JsonFieldType.STRING).description("보임 여부 Y:보이게 N:안보이게").optional()
    };

    private FieldDescriptor[] webVoiceItemsRequest = new FieldDescriptor[] {
            fieldWithPath("context").type(JsonFieldType.STRING).description("컨텍스트").optional(),
            fieldWithPath("uiType").type(JsonFieldType.STRING).description("ui타입").optional(),
            fieldWithPath("bannerUrl").type(JsonFieldType.STRING).description("배너경로").optional(),
            fieldWithPath("bannerImgFile").type(JsonFieldType.STRING).description("배너사진파일").optional(),
            fieldWithPath("headerUse").type(JsonFieldType.STRING).description("헤더영역사용여부 Y:사용, N:사용안함"),
            fieldWithPath("headerStr").type(JsonFieldType.STRING).description("헤더내용").optional(),
            fieldWithPath("textareaUse").type(JsonFieldType.STRING).description("텍스트영역사용여부 Y:사용, N:사용안함"),
            fieldWithPath("textStr").type(JsonFieldType.STRING).description("텍스트영역내용").optional(),
            fieldWithPath("inputareaUse").type(JsonFieldType.STRING).description("입력영역내용 Y:사용, N:사용안함"),
            fieldWithPath("titles").type(JsonFieldType.ARRAY).description("타이틀").optional(),
            fieldWithPath("dtmfs").type(JsonFieldType.ARRAY).description("입력값").optional(),
            fieldWithPath("controlUse").type(JsonFieldType.STRING).description("컨트롤영역사용여부 Y:사용, N:사용안함"),
            fieldWithPath("tailUse").type(JsonFieldType.STRING).description("컨트롤영역사용여부 Y:사용, N:사용안함").optional(),
            fieldWithPath("companyId").description("회사아이디").optional()
    };

    private FieldDescriptor[] webVoiceItemsInputRequest = new FieldDescriptor[] {
            fieldWithPath("inputTitle").type(JsonFieldType.STRING).description("타이틀").optional(),
            fieldWithPath("maxLen").type(JsonFieldType.NUMBER).description("자리수").optional(),
            fieldWithPath("isView").type(JsonFieldType.STRING).description("보임여부 Y:보임, N:안보임")
    };

    private FieldDescriptor[] webVoiceItemsDtmfRequest = new FieldDescriptor[] {
            fieldWithPath("dtmfTitle").type(JsonFieldType.STRING).description("타이틀").optional(),
            fieldWithPath("dtmfValue").type(JsonFieldType.STRING).description("자리수").optional(),
            fieldWithPath("isView").type(JsonFieldType.STRING).description("보임여부 Y:보임, N:안보임")
    };

//    @Order(6)
//    @Test
    public void list() throws Exception{
        final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                        )
                           .andWithPrefix("data.[]",contextInfoResponse)
                ))
                .andReturn();

        log.info(String.valueOf(result));
    }

//    @Order(1)
//    @Test
    public void post() throws Exception{
        ContextInfoFormRequest form = new ContextInfoFormRequest();

        form.setContext("test");
        form.setName("테스트");
        form.setIsWebVoice("N");

        final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .content(mapper.writeValueAsString(form)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestFields(
                                fieldWithPath("context").description("컨텍스트명"),
                                fieldWithPath("name").description("컨텍스트한글명"),
                                fieldWithPath("isWebVoice").description("보이는 ARS 여부")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional(),
                                fieldWithPath("data").description("실제 표현 데이타").optional()
                        )
                ))
                .andReturn();

        log.info(String.valueOf(result));
    }

//    @Order(2)
//    @Test
    public void put() throws Exception {
        ContextInfoFormRequest form = new ContextInfoFormRequest();

        form.setContext("test2");
        form.setName("테스트2");
        form.setIsWebVoice("N");

        final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL+"/{context}","test")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .content(mapper.writeValueAsString(form)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("context").description("컨텍스트명")
                        ),
                        requestFields(
                                fieldWithPath("context").description("컨텍스트명"),
                                fieldWithPath("name").description("컨텍스트한글명"),
                                fieldWithPath("isWebVoice").description("보이는 ARS 여부")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional(),
                                fieldWithPath("data").description("실제 표현 데이타").optional()
                        )
                ))
                .andReturn();

        log.info(String.valueOf(result));
    }

//    @Order(5)
//    @Test
    public void delete() throws Exception {
        final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL+"/{context}","test")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("context").description("컨텍스트명")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional(),
                                fieldWithPath("data").description("실제 표현 데이타").optional()
                        )
                ))
                .andReturn();

        log.info(String.valueOf(result));
    }

//    @Order(4)
//    @Test
    public void voice() throws Exception {
        final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL+"/{context}","test1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("context").description("컨텍스트명")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                        )
                                .andWithPrefix("data.[]",webVoiceInfoResponse)
                                .andWithPrefix("data.[]items.[]",webVoiceItemsResponse)
                ))
                .andReturn();

        log.info(String.valueOf(result));
    }

//    @Order(3)
//    @Test
    public void apply() throws Exception {
        WebVoiceItemsFormRequest form = new WebVoiceItemsFormRequest();
        List<WebVoiceItemsInputFormRequest> titles = new ArrayList<>();
        List<WebVoiceItemsDtmfFormRequest> dtmfs = new ArrayList<>();

/*        titles.add(new WebVoiceItemsInputFormRequest("테스트1",4,"Y"));
        titles.add(new WebVoiceItemsInputFormRequest("테스트2",4,"N"));
        titles.add(new WebVoiceItemsInputFormRequest("테스트3",4,"Y"));

        dtmfs.add(new WebVoiceItemsDtmfFormRequest("테스트1","0","Y"));
        dtmfs.add(new WebVoiceItemsDtmfFormRequest("테스트2","1","Y"));
        dtmfs.add(new WebVoiceItemsDtmfFormRequest("테스트3","2","N"));
        dtmfs.add(new WebVoiceItemsDtmfFormRequest("테스트4","3","Y"));
        dtmfs.add(new WebVoiceItemsDtmfFormRequest("테스트5","4","Y"));
        dtmfs.add(new WebVoiceItemsDtmfFormRequest("테스트6","5","N"));
        dtmfs.add(new WebVoiceItemsDtmfFormRequest("테스트7","6","Y"));
        dtmfs.add(new WebVoiceItemsDtmfFormRequest("테스트8","7","Y"));
        dtmfs.add(new WebVoiceItemsDtmfFormRequest("테스트9","8","N"));*/


        form.setHeaderUse("Y");
        form.setHeaderStr("보이는 ARS 테스트");
        form.setTextareaUse("Y");
        form.setTextStr("텍스트영역 테스트 하는중");
        form.setInputareaUse("Y");
        form.setTitles(titles);
        form.setDtmfs(dtmfs);
        form.setControlUse("Y");

        final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL+"/{context}/apply","test1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .content(mapper.writeValueAsString(form)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("context").description("컨텍스트명")
                        ),
                        requestFields(webVoiceItemsRequest)
                            .andWithPrefix("titles.[]",webVoiceItemsInputRequest)
                            .andWithPrefix("dtmfs.[]",webVoiceItemsDtmfRequest),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional(),
                                fieldWithPath("data").description("실제 표현 데이타").optional()
                        )
                ))
                .andReturn();

        log.info(String.valueOf(result));
    }
}
