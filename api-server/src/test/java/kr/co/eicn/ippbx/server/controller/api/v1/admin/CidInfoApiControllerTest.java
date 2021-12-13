package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.model.form.CidInfoChangeRequest;
import kr.co.eicn.ippbx.model.form.CidInfoFormRequest;
import kr.co.eicn.ippbx.model.form.CidInfoUpdateFormRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class CidInfoApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/service/etc/extension";

    private FieldDescriptor[] cidInfoResponse = new FieldDescriptor[]{
            fieldWithPath("peer").type(JsonFieldType.STRING).description("개인번호"),
            fieldWithPath("extension").type(JsonFieldType.STRING).description("내선번호"),
            fieldWithPath("localPrefix").type(JsonFieldType.STRING).description("지역번호"),
            fieldWithPath("cid").type(JsonFieldType.STRING).description("CID"),
            fieldWithPath("billingNumber").type(JsonFieldType.STRING).description("과금번호"),
            fieldWithPath("dialStatus").type(JsonFieldType.VARIES).description("CRM연결 중 전화끊김 후 자동상태 WAIT:대기, POST_PROCESS:후처리"),
            fieldWithPath("logoutStatus").type(JsonFieldType.VARIES).description("CRM비연결시 상태 WAIT:대기, LOGOUT:로그아웃")
    };

    private FieldDescriptor[] requestUpdate = new FieldDescriptor[]{
            fieldWithPath("peer").type(JsonFieldType.STRING).description("내선번호").optional(),
            fieldWithPath("localPrefix").type(JsonFieldType.STRING).description("지역번호").optional(),
            fieldWithPath("cid").type(JsonFieldType.STRING).description("CID번호").optional(),
            fieldWithPath("billingNumber").type(JsonFieldType.STRING).description("과금번호").optional(),
            fieldWithPath("dialStatus").type(JsonFieldType.NUMBER).description("CRM 연결중 상태").optional(),
            fieldWithPath("logoutStatus").type(JsonFieldType.NUMBER).description("CRM 비연결시 상태").optional()
    };

    private FieldDescriptor[] changeCid = new FieldDescriptor[]{
            fieldWithPath("peers").type(JsonFieldType.ARRAY).description("내선번호목록"),
            fieldWithPath("cid").type(JsonFieldType.STRING).description("CID")
    };

    private FieldDescriptor[] cidList = new FieldDescriptor[]{
            fieldWithPath("cid").type(JsonFieldType.STRING).description("cid번호")
    };

    private FieldDescriptor[] numberList = new FieldDescriptor[]{
            fieldWithPath("number").type(JsonFieldType.STRING).description("과금번호")
    };

//    @Test
    public void pagination() throws Exception {
        final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(new JwtRequestPostProcessor())
                        .param("page", "1")
                        .param("limit", "10")
//                .param("extension", "")
//                .param("localPrefix", "")
//                .param("cid", "")
//                .param("billingNumber", "")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestParameters(
                                parameterWithName("page").description("현재 페이지"),
                                parameterWithName("limit").description("페이지 개수"),
                                parameterWithName("extension").description("내선번호").optional(),
                                parameterWithName("localPrefix").description("지역번호").optional(),
                                parameterWithName("cid").description("cid").optional(),
                                parameterWithName("billingNumber").description("과금번호").optional()
                        ),
                        responseFields(
                                fieldWithPath("data.rows[]").description("내선목록").optional(),
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional(),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("실제 반환될 데이터").optional()
                        )
                                .andWithPrefix("data.rows[]", cidInfoResponse)
                                .andWithPrefix("data.",
                                        fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지"),
                                        fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총갯수"))
                ))
                .andReturn();
    }

//    @Test
    public void put() throws Exception {
        CidInfoUpdateFormRequest form = new CidInfoUpdateFormRequest();
        List<CidInfoFormRequest> list = new ArrayList<>();
        CidInfoFormRequest entity = new CidInfoFormRequest();
        entity.setPeer("75490677");
        entity.setLocalPrefix("02");
        entity.setCid("11111111111");
        entity.setDialStatus((byte) 2);
        entity.setLogoutStatus((byte) 0);
        entity.setBillingNumber("07075490677");
        list.add(entity);
        form.setCidInfos(list);

        final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .content(mapper.writeValueAsString(form)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestFields(
                                fieldWithPath("cidInfos").description("cid정보목록")
                        )
                                .andWithPrefix("cidInfos.[]", requestUpdate),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional(),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("실제 반환될 데이터").optional()
                        )
                ))
                .andReturn();
    }

//    @Test
    public void putCid() throws Exception {
        CidInfoChangeRequest form = new CidInfoChangeRequest();

        List<String> peers = Arrays.asList("75490677", "75490789");
        form.setPeers(peers);
        form.setCid("00000000000");

        final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/cid")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .content(mapper.writeValueAsString(form)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestFields(changeCid),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional(),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("실제 반환될 데이터").optional()
                        )
                ))
                .andReturn();
    }

//    @Test
    public void getCidList() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/cids")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional(),
                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("실제 반환될 데이터").optional()
                        )
                                .and(fieldWithPath("data[]").description("cid목록").optional())
                                .andWithPrefix("data[]", cidList)
                ))
                .andReturn();
    }

//    @Test
    public void getNumberList() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/numbers")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional(),
                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("실제 반환될 데이터").optional()
                        )
                                .and(fieldWithPath("data[]").description("과금번호목록").optional())
                                .andWithPrefix("data[]", numberList)
                ))
                .andReturn();
    }
}
