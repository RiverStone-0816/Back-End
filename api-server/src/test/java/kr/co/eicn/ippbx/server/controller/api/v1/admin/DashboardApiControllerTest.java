package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.DashboardInfo;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.DashboardViewList;
import kr.co.eicn.ippbx.server.model.dto.eicn.*;
import kr.co.eicn.ippbx.server.model.entity.eicn.OrganizationMeta;
import kr.co.eicn.ippbx.server.model.form.ConfRoomFormRequest;
import kr.co.eicn.ippbx.server.model.form.DashboardViewListFormRequest;
import kr.co.eicn.ippbx.server.util.page.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
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
public class DashboardApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/dashboard";
    private static Integer seq;

//    @Test
    public void customWaitMonitor() throws Exception {

        final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/custom-wait-monitor")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
//                .andDo(document.document(
//                        responseFields(
//                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
//                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
//                        )
//                                .and(fieldWithPath("data").type(JsonFieldType.ARRAY).description("사용하지 않는 회의실 번호 리스트"))
//                                .andWithPrefix("data.[]", summaryNumber070Response)
//                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
//                ))
                .andReturn();
        final DashHuntMonitorResponse data = getData(result, DashHuntMonitorResponse.class);

        log.info("data {}", data);
    }

//    @Test
    public void aveByService() throws Exception {

        final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/ave-by-service")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
//                .andDo(document.document(
//                        responseFields(
//                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
//                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
//                        )
//                                .and(fieldWithPath("data").type(JsonFieldType.ARRAY).description("사용하지 않는 회의실 번호 리스트"))
//                                .andWithPrefix("data.[]", summaryNumber070Response)
//                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
//                ))
                .andReturn();
        final DashAveByServiceResponse data = getData(result, DashAveByServiceResponse.class);

        log.info("data {}", data);
    }

//    @Test
    public void monitorByHunt() throws Exception {

        final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/monitor-by-hunt")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
//                .andDo(document.document(
//                        responseFields(
//                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
//                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
//                        )
//                                .and(fieldWithPath("data").type(JsonFieldType.ARRAY).description("사용하지 않는 회의실 번호 리스트"))
//                                .andWithPrefix("data.[]", summaryNumber070Response)
//                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
//                ))
                .andReturn();
        final DashMonitorByHuntResponse data = getData(result, DashMonitorByHuntResponse.class);

        log.info("data {}", data);
    }

//    @Test
    public void excellentCs() throws Exception {

        final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/excellent-cs")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
//                .andDo(document.document(
//                        responseFields(
//                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
//                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
//                        )
//                                .and(fieldWithPath("data").type(JsonFieldType.ARRAY).description("사용하지 않는 회의실 번호 리스트"))
//                                .andWithPrefix("data.[]", summaryNumber070Response)
//                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
//                ))
                .andReturn();
        final List<ExcellentConsultant> data = listData(result, ExcellentConsultant.class);

        log.info("data {}", data);
    }

//    @Test
    public void totalStat() throws Exception {

        final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/total-stat")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
//                .andDo(document.document(
//                        responseFields(
//                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
//                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
//                        )
//                                .and(fieldWithPath("data").type(JsonFieldType.ARRAY).description("사용하지 않는 회의실 번호 리스트"))
//                                .andWithPrefix("data.[]", summaryNumber070Response)
//                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
//                ))
                .andReturn();
        final DashTotalStatResponse data = getData(result, DashTotalStatResponse.class);

        log.info("data {}", data);
    }

//    @Test
    public void serviceStat() throws Exception {
        final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/service-stat/{number}", "07075490137")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
//                .andDo(document.document(
//                        responseFields(
//                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
//                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
//                        )
//                                .and(fieldWithPath("data").type(JsonFieldType.ARRAY).description("사용하지 않는 회의실 번호 리스트"))
//                                .andWithPrefix("data.[]", summaryNumber070Response)
//                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
//                ))
                .andReturn();
        final DashServiceStatResponse data = getData(result, DashServiceStatResponse.class);

        log.info("data {}", data);
    }

//    @Test
    public void totalMonitor() throws Exception {

        final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/total-monitor")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
//                .andDo(document.document(
//                        responseFields(
//                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
//                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
//                        )
//                                .and(fieldWithPath("data").type(JsonFieldType.ARRAY).description("사용하지 않는 회의실 번호 리스트"))
//                                .andWithPrefix("data.[]", summaryNumber070Response)
//                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
//                ))
                .andReturn();
        final DashHuntMonitorResponse data = getData(result, DashHuntMonitorResponse.class);

        log.info("data {}", data);
    }

//    @Test
    public void huntMonitor() throws Exception {

        final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/hunt-monitor/{number}", "00000000004")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
//                .andDo(document.document(
//                        responseFields(
//                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
//                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
//                        )
//                                .and(fieldWithPath("data").type(JsonFieldType.ARRAY).description("사용하지 않는 회의실 번호 리스트"))
//                                .andWithPrefix("data.[]", summaryNumber070Response)
//                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
//                ))
                .andReturn();
        final DashHuntMonitorResponse data = getData(result, DashHuntMonitorResponse.class);

        log.info("data {}", data);
    }

//    @Test
    public void currentCustomWait() throws Exception {

        final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/current-custom-wait")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
//                .andDo(document.document(
//                        responseFields(
//                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
//                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
//                        )
//                                .and(fieldWithPath("data").type(JsonFieldType.ARRAY).description("사용하지 않는 회의실 번호 리스트"))
//                                .andWithPrefix("data.[]", summaryNumber070Response)
//                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
//                ))
                .andReturn();
        final DashCurrentCustomWaitResponse data = getData(result, DashCurrentCustomWaitResponse.class);

        log.info("data {}", data);
    }

//    @Test
    public void currentResult() throws Exception {

        final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/current-result")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
//                .andDo(document.document(
//                        responseFields(
//                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
//                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
//                        )
//                                .and(fieldWithPath("data").type(JsonFieldType.ARRAY).description("사용하지 않는 회의실 번호 리스트"))
//                                .andWithPrefix("data.[]", summaryNumber070Response)
//                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
//                ))
                .andReturn();
        final DashHuntMonitorResponse data = getData(result, DashHuntMonitorResponse.class);

        log.info("data {}", data);
    }

//    @Test
    public void currentResultCall() throws Exception {

        final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/current-result-call")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
//                .andDo(document.document(
//                        responseFields(
//                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
//                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
//                        )
//                                .and(fieldWithPath("data").type(JsonFieldType.ARRAY).description("사용하지 않는 회의실 번호 리스트"))
//                                .andWithPrefix("data.[]", summaryNumber070Response)
//                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
//                ))
                .andReturn();
        final DashCurrentResultCallResponse data = getData(result, DashCurrentResultCallResponse.class);

        log.info("data {}", data);
    }

//    @Test
    public void serverMonitor() throws Exception {

        final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/server-monitor/{host}", "WEB_VIP")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
//                .andDo(document.document(
//                        responseFields(
//                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
//                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
//                        )
//                                .and(fieldWithPath("data").type(JsonFieldType.ARRAY).description("사용하지 않는 회의실 번호 리스트"))
//                                .andWithPrefix("data.[]", summaryNumber070Response)
//                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
//                ))
                .andReturn();
        final DashServerMonitorResponse data = getData(result, DashServerMonitorResponse.class);

        log.info("data {}", data);
    }

//        @Test
    public void dashboardList() throws Exception {

        final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/dashboard-list")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
//                .andDo(document.document(
//                        responseFields(
//                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
//                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
//                        )
//                                .and(fieldWithPath("data").type(JsonFieldType.ARRAY).description("사용하지 않는 회의실 번호 리스트"))
//                                .andWithPrefix("data.[]", summaryNumber070Response)
//                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
//                ))
                .andReturn();
        final List<DashListResponse> data = listData(result, DashListResponse.class);

        log.info("data {}", data);
    }

//        @Test
    public void dashboardViewList() throws Exception {

        final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/dashboard-view-list")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
//                .andDo(document.document(
//                        responseFields(
//                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
//                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
//                        )
//                                .and(fieldWithPath("data").type(JsonFieldType.ARRAY).description("사용하지 않는 회의실 번호 리스트"))
//                                .andWithPrefix("data.[]", summaryNumber070Response)
//                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
//                ))
                .andReturn();
        final List<DashViewListResponse> data = listData(result, DashViewListResponse.class);

        log.info("data {}", data);
    }

//          @Test
    @Order(1)
    protected void post() throws Exception {
        final DashboardViewListFormRequest form = new DashboardViewListFormRequest();
        form.setSeq(10);         //seq
        form.setDashboardInfoId(20);    //대시보드 정보 아이디

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(form))
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document.document(
                        requestFields(
                                fieldWithPath("seq").type(JsonFieldType.NUMBER).description("시퀀스"),
                                fieldWithPath("dashboardInfoId").type(JsonFieldType.NUMBER).description("대시보드 정보 id").optional(),
                                fieldWithPath("companyId").type(JsonFieldType.STRING).description("회사 아이디").optional()
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

//           @Test
    @Order(2)
    protected void put() throws Exception {
        final DashboardViewListFormRequest form = new DashboardViewListFormRequest();
        form.setSeq(10);         //seq
        form.setDashboardInfoId(22);    //대시보드 정보 아이디

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/{seq}", 10)
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
                                fieldWithPath("seq").type(JsonFieldType.NUMBER).description("시퀀스"),
                                fieldWithPath("dashboardInfoId").type(JsonFieldType.NUMBER).description("대시보드 정보 id").optional(),
                                fieldWithPath("companyId").type(JsonFieldType.STRING).description("회사 아이디").optional()
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

//          @Test
    @Order(5)
    protected void delete() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{seq}", 10)
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

}
