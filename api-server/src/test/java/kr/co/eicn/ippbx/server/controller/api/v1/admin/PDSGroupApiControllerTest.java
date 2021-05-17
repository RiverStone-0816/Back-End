package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.server.model.enums.PDSGroupRidKind;
import kr.co.eicn.ippbx.server.model.enums.PDSGroupSpeedMultiple;
import kr.co.eicn.ippbx.server.model.form.MaindbGroupFormRequest;
import kr.co.eicn.ippbx.server.model.form.PDSExecuteFormRequest;
import kr.co.eicn.ippbx.server.model.form.PDSGroupFormRequest;
import kr.co.eicn.ippbx.server.util.page.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class PDSGroupApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/outbound/pds/group";

    private FieldDescriptor[] PDSGroupSummaryResponse = new FieldDescriptor[] {
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("그룹 SEQUENCE KEY"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("그룹명"),
            fieldWithPath("makeDate").type(JsonFieldType.STRING).description("그룹생성일"),
            fieldWithPath("lastUploadDate").type(JsonFieldType.STRING).description("마지막업로드날짜"),
            fieldWithPath("totalCnt").type(JsonFieldType.NUMBER).description("업로드데이터수"),
            fieldWithPath("uploadTryCnt").type(JsonFieldType.NUMBER).description("업로드횟수"),
            fieldWithPath("lastUploadStatus").type(JsonFieldType.STRING).description("마지막업로드상태(\"\":업로드안함, U:업로드중, C:업로드완료, E: 업로드완료(에러:))").optional(),
            fieldWithPath("lastExecuteDate").type(JsonFieldType.STRING).description("마지막실행한날"),
            fieldWithPath("executeTryCnt").type(JsonFieldType.NUMBER).description("실행횟수"),
            fieldWithPath("lastExecuteStatus").type(JsonFieldType.STRING).description("마지막실행상태(I:준비중, R:준비완료, P:진행중, S:정비, C:완료됨, D:마침, \"\":실행안됨)").optional()
    };

    private FieldDescriptor[] PDSGroupDetailResponse = new FieldDescriptor[] {
    		fieldWithPath("seq").type(JsonFieldType.NUMBER).description("그룹 SEQUENCE KEY"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("그룹명"),
            fieldWithPath("pdsType").type(JsonFieldType.NUMBER).description("업로드유형"),
            fieldWithPath("pdsTypeName").type(JsonFieldType.STRING).description("업로드유형명"),
            fieldWithPath("info").type(JsonFieldType.STRING).description("추가정보"),
            fieldWithPath("groupCode").type(JsonFieldType.STRING).description("소속").optional(),
            fieldWithPath("runHost").type(JsonFieldType.STRING).description("실행할 교환기"),
            fieldWithPath("dialTimeout").type(JsonFieldType.NUMBER).description("다이얼 시간"),
            fieldWithPath("isRecord").type(JsonFieldType.STRING).description("녹취사용 유무"),
            fieldWithPath("numberField").type(JsonFieldType.STRING).description("콜시도할전화번호필드"),
            fieldWithPath("ridKind").type(JsonFieldType.STRING).description("RID(발신번호)설정 구분"),
            fieldWithPath("ridData").type(JsonFieldType.STRING).description("RID(발신번호) 정보"),
            fieldWithPath("billingKind").type(JsonFieldType.STRING).description("과금번호설정 구분"),
            fieldWithPath("billingData").type(JsonFieldType.STRING).description("과금번호 정보"),
            fieldWithPath("resultKind").type(JsonFieldType.STRING).description("상담결과 구분"),
            fieldWithPath("resultType").type(JsonFieldType.NUMBER).description("상담결과 데이터"),
            fieldWithPath("connectKind").type(JsonFieldType.STRING).description("연결대상 구분"),
            fieldWithPath("connectData").type(JsonFieldType.STRING).description("연결대상 정보"),
            fieldWithPath("speedKind").type(JsonFieldType.STRING).description("PDS속도 기준"),
            fieldWithPath("speedData").type(JsonFieldType.NUMBER).description("PDS속도 정보"),
            fieldWithPath("companyTrees").type(JsonFieldType.ARRAY).description("조직트리 정보").optional()
    };

    private FieldDescriptor[] PDSGroupFormRequest = new FieldDescriptor[] {
            fieldWithPath("name").type(JsonFieldType.STRING).description("그룹명"),
            fieldWithPath("pdsType").type(JsonFieldType.NUMBER).description("업로드유형"),
            fieldWithPath("info").type(JsonFieldType.STRING).description("추가정보").optional(),
            fieldWithPath("groupCode").type(JsonFieldType.STRING).description("소속").optional(),
            fieldWithPath("runHost").type(JsonFieldType.STRING).description("실행할 교환기"),
            fieldWithPath("dialTimeout").type(JsonFieldType.NUMBER).description("다이얼 시간"),
            fieldWithPath("isRecord").type(JsonFieldType.STRING).description("녹취사용 유무"),
            fieldWithPath("numberField").type(JsonFieldType.STRING).description("콜시도할전화번호필드"),
            fieldWithPath("ridKind").type(JsonFieldType.STRING).description("RID(발신번호) 구분"),
            fieldWithPath("ridData").type(JsonFieldType.STRING).description("RID(발신번호) 정보"),
            fieldWithPath("billingKind").type(JsonFieldType.STRING).description("과금번호설정 구분"),
            fieldWithPath("billingData").type(JsonFieldType.STRING).description("과금번호 정보"),
            fieldWithPath("connectKind").type(JsonFieldType.STRING).description("연결대상 구분"),
            fieldWithPath("connectData").type(JsonFieldType.STRING).description("연결대상 정보"),
            fieldWithPath("speedKind").type(JsonFieldType.STRING).description("PDS속도 기준"),
            fieldWithPath("speedData").type(JsonFieldType.NUMBER).description("PDS속도 데이터"),
            fieldWithPath("resultKind").type(JsonFieldType.STRING).description("상담결과 구분").optional(),
            fieldWithPath("resultType").type(JsonFieldType.NUMBER).description("상담결과 데이터").optional()
    };

    private FieldDescriptor[] summaryNumber070Response = new FieldDescriptor[] {
            fieldWithPath("number").type(JsonFieldType.STRING).description("070번호"),
            fieldWithPath("type").type(JsonFieldType.NUMBER).description("번호타입(0:헌트, 1:개인, 2:대표, 3:회의)"),
            fieldWithPath("hanName").type(JsonFieldType.STRING).description("QUEUE 한글이름").optional()
    };

    private FieldDescriptor[] commonTypeResponse = new FieldDescriptor[] {
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("시퀀스"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("유형명")
    };

    private FieldDescriptor[] summaryCommonTypeResponse = new FieldDescriptor[] {
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("시퀀스"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("유형명")
    };

    private FieldDescriptor[] summaryCompanyServerResponse = new FieldDescriptor[] {
            fieldWithPath("host").type(JsonFieldType.STRING).description("호스트"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("호스트명")
    };

    private FieldDescriptor[] summaryPDSQueueNameResponse = new FieldDescriptor[] {
            fieldWithPath("host").type(JsonFieldType.STRING).description("해당 QUEUE 사용IP"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("QUEUE명"),
            fieldWithPath("hanName").type(JsonFieldType.STRING).description("QUEUE 한글명")
    };

    private FieldDescriptor[] summaryIvrTreeResponse = new FieldDescriptor[] {
            fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드 키"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("IVR정보명")
    };

    private FieldDescriptor[] summaryResearchListResponse = new FieldDescriptor[] {
            fieldWithPath("researchId").type(JsonFieldType.NUMBER).description(""),
            fieldWithPath("researchName").type(JsonFieldType.STRING).description("")
    };

    private FieldDescriptor[] summaryCommonFieldResponse = new FieldDescriptor[] {
            fieldWithPath("type").type(JsonFieldType.NUMBER).description("유형관리 참조키"),
            fieldWithPath("fieldId").type(JsonFieldType.STRING).description("필드아이디"),
            fieldWithPath("fieldInfo").type(JsonFieldType.STRING).description("필드정보")
    };

    private FieldDescriptor[] organizationSummaryResponse = new FieldDescriptor[] {
            fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드"),
            fieldWithPath("groupName").type(JsonFieldType.STRING).description("조직명"),
            fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("윗레벨의 코드를 포함한 코드의 나열(002_003_008)").optional(),
            fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("조직레벨")
    };

//    @Test
    protected void pagination() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("page", "1")
                .param("limit", "10")
                .param("startDate", "2020-03-21")
                .param("endDate", "2020-03-26")
                .param("pdsType", "")
                .param("name", ""))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestParameters(
                                parameterWithName("page").description("현재 페이지"),
                                parameterWithName("limit").description("페이지 개수"),
                                parameterWithName("startDate").description("시작일"),
                                parameterWithName("endDate").description("종료일"),
                                parameterWithName("pdsType").description("유형"),
                                parameterWithName("name").description("그룹명")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional(),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("실제 반환될 데이터").optional(),
                                fieldWithPath("data.rows[]").description("PDS그룹 목록").optional()
                        )
                        .andWithPrefix("data.rows[]", PDSGroupSummaryResponse)
                        .andWithPrefix("data.",
                            fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지"),
                            fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총갯수"))
                ))
                .andReturn();

        final Pagination<kr.co.eicn.ippbx.server.model.dto.eicn.PDSGroupSummaryResponse> pagination =
                paginationData(result, kr.co.eicn.ippbx.server.model.dto.eicn.PDSGroupSummaryResponse.class);

        for (kr.co.eicn.ippbx.server.model.dto.eicn.PDSGroupSummaryResponse row : pagination.getRows()) {
            log.info("그룹명:{}, 그룹생성일:{}, 마지막업로드날짜:{}, 업로드데이터수:{}, 업로드횟수:{}, 마지막업로드상태:{}, 마지막실행한날:{}, 실행횟수:{}, 실행상태:{}"
                , row.getName()
                , row.getMakeDate()
                , row.getLastUploadDate()
                , row.getTotalCnt()
                , row.getUploadTryCnt()
                , row.getLastUploadStatus()
                , row.getLastExecuteDate()
                , row.getExecuteTryCnt()
                , row.getLastExecuteStatus());
        }
    }

//    @Test
    protected void get() throws Exception {
        final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{seq}", 2)
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
                        .and(fieldWithPath("data").type(JsonFieldType.OBJECT).description("PDS그룹 상세"))
                        .andWithPrefix("data.", PDSGroupDetailResponse)
                        .andWithPrefix("data.companyTrees[]", organizationSummaryResponse)
                        .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }

//    @Test
    protected void post() throws Exception {
        final PDSGroupFormRequest form = new PDSGroupFormRequest();
        form.setName("PDS테스트");
        form.setPdsType(27);
//        form.setInfo("");
//        form.setGroupCode("");
        // PDS항목설정 정보
        form.setRunHost("localhost");   // 실행할 교환기
        form.setDialTimeout((byte) 30); // 다이얼 시간
        form.setIsRecord("N");  // 녹취사용 유무
        form.setNumberField("RS_NUMBER_1"); // 콜시도할전화번호필드
        form.setRidKind(PDSGroupRidKind.GROUP_BY_RID.getCode());
        form.setRidData("121212"); // RID(발신번호) 정보
        form.setBillingKind("NUMBER"); // 과금번호설정 구분
        form.setBillingData("07075490677"); // 과금번호 정보
        form.setSpeedKind("MEMBER");  // PDS속도 기준
        form.setSpeedData(PDSGroupSpeedMultiple.ONE.getCode()); // PDS속도 데이터
        form.setConnectKind("PDS_IVR"); // 연결대상 구분
        form.setConnectData("1"); // 연결대상 정보
        form.setResultKind("RS"); // 상담결과 구분
        form.setResultType(5); // 상담결과 유형 정보

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(form))
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document.document(
                        requestFields(PDSGroupFormRequest),
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
    protected void put() throws Exception {
        final PDSGroupFormRequest form = new PDSGroupFormRequest();
        form.setName("PDS테스트 수정"); // 그룹명
        form.setPdsType(5); // 업로드유형 common_type.seq
//        form.setInfo("");   // 추가정보
//        form.setGroupCode("");     // 소속

        // PDS항목설정 정보
        form.setRunHost("localhost");   // 실행할 교환기
        form.setDialTimeout((byte) 30); // 다이얼 시간
        form.setIsRecord("N");  // 녹취사용 유무
        form.setNumberField("RS_NUMBER_1"); // 콜시도할전화번호필드
        form.setRidData("121212"); // RID(발신번호) 정보
        form.setBillingKind("NUMBER"); // 과금번호설정 구분
        form.setBillingData("07075490677"); // 과금번호 정보
        form.setSpeedKind("MEMBER");  // PDS속도 기준
        form.setSpeedData(PDSGroupSpeedMultiple.ONE.getCode()); // PDS속도 데이터
        form.setConnectKind("PDS_IVR"); // 연결대상 구분
        form.setConnectData("1"); // 연결대상 정보
        form.setResultKind("RS"); // 상담결과 구분
        form.setResultType(5); // 상담결과 유형 정보

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
                        requestFields(PDSGroupFormRequest),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("data").type(JsonFieldType.NULL).description("").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                        )
                ))
                .andReturn();
    }

//    @Test
    protected void execute_request() throws Exception {
        final PDSExecuteFormRequest form = new PDSExecuteFormRequest();
        form.setExecuteName("실행 테스트"); //실행명

        // PDS항목설정 정보
        form.setRunHost("localhost");   // 실행할 교환기
        form.setDialTimeout((byte) 30); // 다이얼 시간
        form.setIsRecord("N");  // 녹취사용 유무
        form.setNumberField("RS_NUMBER_1"); // 콜시도할전화번호필드
        form.setRidData("121212"); // RID(발신번호) 정보
        form.setBillingKind("NUMBER"); // 과금번호설정 구분
        form.setBillingData("07075490677"); // 과금번호 정보
        form.setSpeedKind("MEMBER");  // PDS속도 기준
        form.setSpeedData(PDSGroupSpeedMultiple.ONE.getCode()); // PDS속도 데이터

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL + "/{seq}/execute", 1)
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
                                fieldWithPath("executeName").type(JsonFieldType.STRING).description("실행명"),
                                fieldWithPath("runHost").type(JsonFieldType.STRING).description("실행할 교환기"),
                                fieldWithPath("dialTimeout").type(JsonFieldType.NUMBER).description("다이얼 시간"),
                                fieldWithPath("isRecord").type(JsonFieldType.STRING).description("녹취사용 유무"),
                                fieldWithPath("numberField").type(JsonFieldType.STRING).description("콜시도할전화번호필드"),
                                fieldWithPath("ridKind").type(JsonFieldType.STRING).description("RID(발신번호) 구분"),
                                fieldWithPath("ridData").type(JsonFieldType.STRING).description("RID(발신번호) 정보"),
                                fieldWithPath("billingKind").type(JsonFieldType.STRING).description("과금번호설정 구분"),
                                fieldWithPath("billingData").type(JsonFieldType.STRING).description("과금번호 정보"),
                                fieldWithPath("connectKind").type(JsonFieldType.STRING).description("연결대상 구분"),
                                fieldWithPath("connectData").type(JsonFieldType.STRING).description("연결대상 정보"),
                                fieldWithPath("speedKind").type(JsonFieldType.STRING).description("PDS속도 기준"),
                                fieldWithPath("speedData").type(JsonFieldType.NUMBER).description("PDS속도 데이터"),
                                fieldWithPath("resultKind").type(JsonFieldType.STRING).description("상담결과 구분").optional(),
                                fieldWithPath("resultType").type(JsonFieldType.NUMBER).description("상담결과 데이터").optional()
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("data").type(JsonFieldType.NULL).description("").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                        )
                ))
                .andReturn();
    }

//    @Test
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

//    @Test
    protected void add_common_type() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/add-common-type")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("kind", "PDS"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                		requestParameters(parameterWithName("kind").description("업로드유형:PDS, 상담결과유형:RS")),
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
     * 실행할교환기 목록 조회
     */
//    @Test
    protected void add_servers() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/add-server")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                        .and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("교환기 목록").optional())
                        .andWithPrefix("data.[]", summaryCompanyServerResponse)
                        .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }

    /**
     * 번호 목록 조회
     */
//    @Test
    protected void add_numbers() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/add-numbers")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                        .and(fieldWithPath("data").type(JsonFieldType.ARRAY).description("번호 목록 조회").optional())
                        .andWithPrefix("data.[]", summaryNumber070Response)
                        .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }

    /**
     * 상담원그룹 연결대상 목록 조회(실행할교환기선택후사용가능)
     */
//    @Test
    protected void add_pds_queuename() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/add-pds-queue")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                        .and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("상담원그룹 목록").optional())
                        .andWithPrefix("data.[]", summaryPDSQueueNameResponse)
                        .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }

    /**
     * PDS IVR 목록조회
     */
//    @Test
    protected void add_pds_ivr() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/add-pds-ivr")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                        .and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("PDS IVR 목록").optional())
                        .andWithPrefix("data.[]", summaryIvrTreeResponse)
                        .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }

    /**
     * 상담결과유형 목록조회
     */
//    @Test
    protected void add_consultation_result() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/add-consultation-result")
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
                        .andWithPrefix("data.[]", summaryCommonTypeResponse)
                        .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }

    /**
     * 설문 목록 조회
     */
//    @Test
    protected void add_research_list() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/add-research")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                        .and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("설문 목록").optional())
                        .andWithPrefix("data.[]", summaryResearchListResponse)
                        .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }

    /**
     * 유형 필드 목록조회
     */
//    @Test
    protected void add_field() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/add-field")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                        .and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("필드 목록").optional())
                        .andWithPrefix("data.[]", summaryCommonFieldResponse)
                        .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }
}
