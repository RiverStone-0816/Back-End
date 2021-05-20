package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.model.dto.eicn.ConfRoomDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.ConfRoomSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryNumber070Response;
import kr.co.eicn.ippbx.model.form.ConfRoomFormRequest;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class ConfRoomApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/conf/room";
    private static Integer seq;

    private FieldDescriptor[] confRoomDetailResponse = new FieldDescriptor[] {
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("시퀀스"),
            fieldWithPath("roomName").type(JsonFieldType.STRING).description("회의실명"),
            fieldWithPath("roomNumber").type(JsonFieldType.STRING).description("회의실번호"),
            fieldWithPath("roomCid").type(JsonFieldType.STRING).description("회의실RID"),
            fieldWithPath("roomShortNum").type(JsonFieldType.STRING).description("회의실단축번호"),
            fieldWithPath("groupCode").type(JsonFieldType.STRING).description("그룹코드"),
            fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("그룹코드나열"),
            fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("그룹레벨"),
            fieldWithPath("companyId").type(JsonFieldType.STRING).description("회사아이디"),
            fieldWithPath("companyTrees").type(JsonFieldType.ARRAY).description("회사소속")
    };

    private FieldDescriptor[] confRoomSummaryResponse = new FieldDescriptor[] {
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("시퀀스"),
            fieldWithPath("roomName").type(JsonFieldType.STRING).description("회의실명"),
            fieldWithPath("roomNumber").type(JsonFieldType.STRING).description("회의실번호"),
            fieldWithPath("roomCid").type(JsonFieldType.STRING).description("회의실RID"),
            fieldWithPath("roomShortNum").type(JsonFieldType.STRING).description("회의실단축번호"),
            fieldWithPath("groupCode").type(JsonFieldType.STRING).description("그룹코드"),
            fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("그룹코드나열"),
            fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("그룹레벨"),
            fieldWithPath("companyId").type(JsonFieldType.STRING).description("회사아이디"),
            fieldWithPath("hostName").type(JsonFieldType.STRING).description("소속교환기명"),
            fieldWithPath("companyTrees").type(JsonFieldType.ARRAY).description("회사소속")

    };

    private FieldDescriptor[] summaryNumber070Response = new FieldDescriptor[] {
            fieldWithPath("number").type(JsonFieldType.STRING).description("070번호"),
            fieldWithPath("type").type(JsonFieldType.NUMBER).description("번호타입(0:헌트, 1:개인, 2:대표, 3:회의)"),
            fieldWithPath("hanName").type(JsonFieldType.STRING).description("QUEUE 한글이름").optional()
    };

    private FieldDescriptor[] organizationSummaryResponse = new FieldDescriptor[] {
            fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드"),
            fieldWithPath("groupName").type(JsonFieldType.STRING).description("조직명"),
            fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("윗레벨의 코드를 포함한 코드의 나열(002_003_008)").optional(),
            fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("조직레벨")
    };
//      @Test
    @Order(4)
    protected void pagination() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("page", "1")
                .param("limit", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                        .and(fieldWithPath("data").type(JsonFieldType.OBJECT).description("반환될 데이터").optional())
                        .and(fieldWithPath("data.rows[]").type(JsonFieldType.ARRAY).description("회의실 목록").optional())
                        .andWithPrefix("data.rows[]", confRoomSummaryResponse)
                        .andWithPrefix("data.rows[].companyTrees[]", organizationSummaryResponse)
                        .andWithPrefix("data.",
                                fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지"),
                                fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총갯수"))
                        .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();

          final Pagination<ConfRoomSummaryResponse> pagination = paginationData(result, ConfRoomSummaryResponse.class);
          final List<ConfRoomSummaryResponse> rows = pagination.getRows();
          for (ConfRoomSummaryResponse row : rows) {
              log.info(row.toString());
          }
    }

//      @Test
    @Order(3)
    protected void get() throws Exception {
        final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{seq}", 12)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("seq").description("시퀀스")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                        .and(fieldWithPath("data").type(JsonFieldType.OBJECT).description("회의실 상세"))
                        .andWithPrefix("data.companyTrees[]", organizationSummaryResponse)
                        .andWithPrefix("data.", confRoomDetailResponse)
                        .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
          final ConfRoomDetailResponse data = getData(result, ConfRoomDetailResponse.class);

          log.info("data {}", data);
    }

//      @Test
    @Order(1)
    protected void post() throws Exception {
        final ConfRoomFormRequest form = new ConfRoomFormRequest();
        form.setRoomName("회의실명");         //회의실명
        form.setRoomNumber("00000000008");    //회의실번호
        form.setRoomCid("00000000008");   //회의실RID
        form.setRoomShortNum("12312312");  //회의실단축번호
        form.setGroupCode("0008");     //그룹코드
//        form.setGroupTreeName("0002_0003_0009");         //그룹코드나열
//        form.setGroupLevel(3);      //그룹레벨
//        form.setCompanyId("primium");      //회사아이디

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(form))
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document.document(
                        requestFields(
                                fieldWithPath("roomName").type(JsonFieldType.STRING).description("회의실명"),
                                fieldWithPath("roomNumber").type(JsonFieldType.STRING).description("회의실번호"),
                                fieldWithPath("roomCid").type(JsonFieldType.STRING).description("회의실RID"),
                                fieldWithPath("roomShortNum").type(JsonFieldType.STRING).description("회의실단축번호"),
                                fieldWithPath("groupCode").type(JsonFieldType.STRING).description("그룹코드"),
                                fieldWithPath("companyId").type(JsonFieldType.STRING).description("회사아이디")
                        )
                        , responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("data").type(JsonFieldType.NUMBER).description("실제 반환될 데이터").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                        )
                ))
                .andReturn();

        seq = getData(result, Integer.class);
        log.info("SEQUENCE KEY {}", seq);
    }

//       @Test
    @Order(2)
    protected void put() throws Exception {
        final ConfRoomFormRequest form = new ConfRoomFormRequest();
        form.setRoomName("회의실명");         //회의실명
        form.setRoomNumber("00000000009");    //회의실번호
        form.setRoomCid("00000000009");   //회의실RID
        form.setRoomShortNum("1111111111");  //회의실단축번호
        form.setGroupCode("0007");     //그룹코드
//        form.setGroupTreeName("0002_0003_0009");         //그룹코드나열
//        form.setGroupLevel(3);      //그룹레벨
//        form.setCompanyId("primium");      //회사아이디

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/{seq}", 11)
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
                                fieldWithPath("roomName").type(JsonFieldType.STRING).description("회의실명"),
                                fieldWithPath("roomNumber").type(JsonFieldType.STRING).description("회의실번호"),
                                fieldWithPath("roomCid").type(JsonFieldType.STRING).description("회의실RID"),
                                fieldWithPath("roomShortNum").type(JsonFieldType.STRING).description("회의실단축번호"),
                                fieldWithPath("groupCode").type(JsonFieldType.STRING).description("그룹코드"),
                                fieldWithPath("companyId").type(JsonFieldType.STRING).description("회사아이디")
                        )
                        ,
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("data").type(JsonFieldType.NUMBER).description("실제 반환될 데이터").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                        )
                ))
                .andReturn();
    }

//      @Test
    @Order(5)
    protected void delete() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{seq}", 11)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("result").value("success"))
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("seq").description("시퀀스")
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

//    @Test
    public void unusedConfRoomNumber() throws Exception {

        final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/unused-confroom-number")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                                .and(fieldWithPath("data").type(JsonFieldType.ARRAY).description("사용하지 않는 회의실 번호 리스트"))
                                .andWithPrefix("data.[]", summaryNumber070Response)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
        final List<SummaryNumber070Response> data = listData(result, SummaryNumber070Response.class);

        log.info("data {}", data);
    }

}
