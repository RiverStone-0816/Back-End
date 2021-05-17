package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.server.model.dto.eicn.*;
import kr.co.eicn.ippbx.server.model.form.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class ConfInfoApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/conf/info";
    private static Integer seq;

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

    private FieldDescriptor[] confInfoDetailResponse = new FieldDescriptor[] {
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("시퀀스"),
            fieldWithPath("status").type(JsonFieldType.STRING).description("회의상태"),
            fieldWithPath("inputDate").type(JsonFieldType.STRING).description("입력날짜?"),
            fieldWithPath("startDate").type(JsonFieldType.STRING).description("시작날짜?"),
            fieldWithPath("endDate").type(JsonFieldType.STRING).description("종료날짜?"),
            fieldWithPath("roomNumber").type(JsonFieldType.STRING).description("회의실번호"),
            fieldWithPath("confName").type(JsonFieldType.STRING).description("회의명"),
            fieldWithPath("confType").type(JsonFieldType.STRING).description("회의타입"),
            fieldWithPath("confPasswd").type(JsonFieldType.STRING).description("회의비밀번호"),
            fieldWithPath("reserveDate").type(JsonFieldType.STRING).description("회의예약날짜"),
            fieldWithPath("reserveFromTime").type(JsonFieldType.NUMBER).description("회의예약시작시간(분)"),
            fieldWithPath("reservetoTime").type(JsonFieldType.NUMBER).description("회의예약종료시간(분)"),
            fieldWithPath("reserveAdmin").type(JsonFieldType.STRING).description("회의실예약자"),
            fieldWithPath("reserveAdminName").type(JsonFieldType.STRING).description("회의실예약자이름"),
            fieldWithPath("confSound").type(JsonFieldType.NUMBER).description("회의참석시음원"),
            fieldWithPath("confCid").type(JsonFieldType.STRING).description("초대시Rid번호"),
            fieldWithPath("isRecord").type(JsonFieldType.STRING).description("녹취여부"),
            fieldWithPath("isMachineDetect").type(JsonFieldType.STRING).description("머신디텍트?"),
            fieldWithPath("totalMemberCnt").type(JsonFieldType.NUMBER).description("총멤버수"),
            fieldWithPath("attendedMemberCnt").type(JsonFieldType.NUMBER).description("참석한멤버수"),
            fieldWithPath("recordDir").type(JsonFieldType.STRING).description("녹취파일경로"),
            fieldWithPath("inMemberList").type(JsonFieldType.ARRAY).description("내부참여자"),
            fieldWithPath("outMemberList").type(JsonFieldType.ARRAY).description("외부참여자"),
            fieldWithPath("host").type(JsonFieldType.STRING).description("호스트"),
            fieldWithPath("companyId").type(JsonFieldType.STRING).description("회사아이디")

    };

    private FieldDescriptor[] confInfoUpdateRequest = new FieldDescriptor[]{
            fieldWithPath("status").type(JsonFieldType.STRING).description("회의상태").optional(),
            fieldWithPath("inputDate").type(JsonFieldType.STRING).description("입력날짜").optional(),
            fieldWithPath("startDate").type(JsonFieldType.STRING).description("시작날짜").optional(),
            fieldWithPath("endDate").type(JsonFieldType.STRING).description("종료날짜").optional(),
            fieldWithPath("roomNumber").type(JsonFieldType.STRING).description("회의실번호").optional(),
            fieldWithPath("confName").type(JsonFieldType.STRING).description("회의명").optional(),
            fieldWithPath("confType").type(JsonFieldType.STRING).description("회의타입").optional(),
            fieldWithPath("confPasswd").type(JsonFieldType.STRING).description("회의비밀번호").optional(),
            fieldWithPath("reserveDate").type(JsonFieldType.STRING).description("회의예약날짜").optional(),
            fieldWithPath("reserveFromTime").type(JsonFieldType.NUMBER).description("회의예약시작시간(분)").optional(),
            fieldWithPath("reserveToTime").type(JsonFieldType.NUMBER).description("회의예약종료시간(분)").optional(),
            fieldWithPath("reserveAdmin").type(JsonFieldType.STRING).description("회의실예약자").optional(),
            fieldWithPath("confSound").type(JsonFieldType.NUMBER).description("회의참석시음원").optional(),
            fieldWithPath("confCid").type(JsonFieldType.STRING).description("초대시Rid번호").optional(),
            fieldWithPath("isRecord").type(JsonFieldType.STRING).description("녹취여부").optional(),
            fieldWithPath("isMachineDetect").type(JsonFieldType.STRING).description("머신디텍트?").optional(),
            fieldWithPath("totalMemberCnt").type(JsonFieldType.NUMBER).description("총 참여자수").optional(),
            fieldWithPath("attendedMemberCnt").type(JsonFieldType.NUMBER).description("참석한 참여자수").optional(),
            fieldWithPath("recordDir").type(JsonFieldType.STRING).description("녹취파일경로").optional()
    };

    private FieldDescriptor[] confInfoFormRequest = new FieldDescriptor[] {
            fieldWithPath("confName").type(JsonFieldType.STRING).description("회의명"),
            fieldWithPath("reserveFromTime").type(JsonFieldType.NUMBER).description("회의시작시간"),
            fieldWithPath("reserveToTime").type(JsonFieldType.NUMBER).description("회의종료시간"),
            fieldWithPath("confSound").type(JsonFieldType.NUMBER).description("회의참석시음원").optional(),
            fieldWithPath("isRecord").type(JsonFieldType.STRING).description("녹취여부"),
            fieldWithPath("confCid").type(JsonFieldType.STRING).description("초대시RID"),
            fieldWithPath("isMachineDetect").type(JsonFieldType.STRING).description("머신디텍트유무"),
            fieldWithPath("confPasswd").type(JsonFieldType.STRING).description("비밀번호").optional(),
            fieldWithPath("confPeerMembers").type(JsonFieldType.ARRAY).description("내부참여자(peer)").optional(),
            fieldWithPath("confOutMembers").type(JsonFieldType.ARRAY).description("외부참여자([0]:참가자명, [1]:전화번호)").optional(),
            fieldWithPath("roomNumber").type(JsonFieldType.STRING).description("회의실번호"),
            fieldWithPath("reserveDate").type(JsonFieldType.NUMBER).description("회의날짜"),

    };

    private FieldDescriptor[] confMemberOutPersonFormRequest = new FieldDescriptor[] {
            fieldWithPath("memberName").type(JsonFieldType.STRING).description("외부참여자명"),
            fieldWithPath("memberNumber").type(JsonFieldType.STRING).description("외부참여자전화번호"),

    };

    private FieldDescriptor[] confInfoDuplicateFormRequest = new FieldDescriptor[] {
            fieldWithPath("reserveFromTime").type(JsonFieldType.NUMBER).description("회의시작시간"),
            fieldWithPath("reserveToTime").type(JsonFieldType.NUMBER).description("회의종료시간"),
            fieldWithPath("roomNumber").type(JsonFieldType.STRING).description("회의실번호"),
            fieldWithPath("reserveDate").type(JsonFieldType.STRING).description("회의날짜")
    };

    private FieldDescriptor[] confInfoSummaryResponse = new FieldDescriptor[] {
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("시퀀스"),
            fieldWithPath("confName").type(JsonFieldType.STRING).description("회의명"),
            fieldWithPath("reserveFromTime").type(JsonFieldType.NUMBER).description("회의예약시작시간(분)"),
            fieldWithPath("reservetoTime").type(JsonFieldType.NUMBER).description("회의예약종료시간(분)"),
            fieldWithPath("reserveDate").type(JsonFieldType.STRING).description("회의예약날짜")
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

    private FieldDescriptor[] summarySoundListResponse = new FieldDescriptor[] {
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("음원 SEQUENCE KEY"),
            fieldWithPath("soundName").type(JsonFieldType.STRING).description("음원명"),
    };

    private FieldDescriptor[] summaryPersonResponses = new FieldDescriptor[] {
            fieldWithPath("idName").type(JsonFieldType.STRING).description("한글이름").optional(),
            fieldWithPath("peer").type(JsonFieldType.STRING).description("전화기 ID").optional(),
            fieldWithPath("extension").type(JsonFieldType.STRING).description("내선전화번호").optional(),
            fieldWithPath("hostName").type(JsonFieldType.STRING).description("교환기명").optional()
    };

    private FieldDescriptor[] confMemberOutPersonsResponse = new FieldDescriptor[] {
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("시퀀스").optional(),
            fieldWithPath("memberType").type(JsonFieldType.STRING).description("멤버타입").optional(),
            fieldWithPath("memberName").type(JsonFieldType.STRING).description("멤버네임").optional(),
            fieldWithPath("memberNumber").type(JsonFieldType.STRING).description("멤버넘버").optional()
    };

    private FieldDescriptor[] minnutesResponse = new FieldDescriptor[] {
            fieldWithPath("minutesText").type(JsonFieldType.STRING).description("회의록")
    };

    private FieldDescriptor[] confInfoMinutesSaveFormRequest = new FieldDescriptor[] {
            fieldWithPath("confId").type(JsonFieldType.NUMBER).description("회의 시퀀스"),
            fieldWithPath("confMinute").type(JsonFieldType.STRING).description("회의록 내용")
    };


//    @Test
//    @Order(4)
    protected void getConfRoomList() throws Exception {
          final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/confroom-list")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                  .andDo(document.document(
                          responseFields(
                                  fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                  fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                          )
                                  .and(fieldWithPath("data[]").type(JsonFieldType.ARRAY).description("회의실 목록").optional())
                                  .andWithPrefix("data[]", confRoomSummaryResponse)
                                  .andWithPrefix("data[].companyTrees[]", organizationSummaryResponse)
                                  .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                  ))
                .andReturn();

          final List<ConfRoomSummaryResponse> data = listData(result, ConfRoomSummaryResponse.class);

          log.info("data {}", data);
    }

//    @Test
//    @Order(4)
    protected void getConfInfoList() throws Exception {
        final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("reserveYear", "2020")
                .param("reserveMonth", "3")
                .param("roomNumber", "00000000008")
        )
                .andDo(print())
                .andExpect(status().isOk())
                  .andDo(document.document(
                          requestParameters(
                                  parameterWithName("reserveYear").description("회의 날짜(년도)").optional(),
                                  parameterWithName("reserveMonth").description("회의 날짜(월)").optional(),
                                  parameterWithName("roomNumber").description("회의실 번호").optional()
                          ),
                          responseFields(
                                  fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                  fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                          )
                                  .and(fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("회의예약 리스트").optional())
                                  .andWithPrefix("data.[]", confInfoSummaryResponse)
                                  .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                  ))
                .andReturn();

        final List<ConfInfoSummaryResponse> data = listData(result, ConfInfoSummaryResponse.class);

        log.info("data {}", data);
    }

    /**
     * 추가 가능한 내부참여자 목록조회
     */
//	@Test
    protected void add_on_conf_persons() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/add_on_conf_persons")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("seq", "10")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestParameters(
                                parameterWithName("seq").description("회의 시퀀스").optional()
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                                .and(fieldWithPath("data").type(JsonFieldType.ARRAY).description("추가 가능 사용자").optional())
                                .andWithPrefix("data.[]", summaryPersonResponses)
                                .andWithPrefix("data[].companyTrees[]", organizationSummaryResponse)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();

        final List<SummaryPersonResponse> summaryPersonResponses = listData(result, SummaryPersonResponse.class);
        for (SummaryPersonResponse summaryPersonResponse : summaryPersonResponses) {
            log.info(summaryPersonResponse.toString());
        }
    }

    /**
     * 회의참석시음원 목록 조회
     */
//	@Test
    protected void add_sounds_list() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/add-sounds-list")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                                .and(fieldWithPath("data").type(JsonFieldType.ARRAY).description("음원 목록 조회").optional())
                                .andWithPrefix("data.[]", summarySoundListResponse)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }

//    @Test
    protected void get() throws Exception {
        final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{seq}", 11)
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
                        .and(fieldWithPath("data").type(JsonFieldType.OBJECT).description("회의예약 상세"))
                        .andWithPrefix("data.", confInfoDetailResponse)
                                .andWithPrefix("data.inMemberList[]", summaryPersonResponses)
                                .andWithPrefix("data.inMemberList[].companyTrees[]", organizationSummaryResponse)
                                .andWithPrefix("data.outMemberList[]", confMemberOutPersonsResponse)

                        .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
          final ConfInfoDetailResponse data = getData(result, ConfInfoDetailResponse.class);

          log.info("data {}", data);
    }

  @Test
//    @Order(1)
    protected void post() throws Exception {

	    final Set<String> confPeerMembers = new HashSet<>();
	    confPeerMembers.add("75490677");

        final Set<ConfMemberOutPersonFormRequest> confOutMembers = new HashSet<>();
        final ConfMemberOutPersonFormRequest outPersonForm = new ConfMemberOutPersonFormRequest();
        outPersonForm.setMemberName("외부참여자명111");
        outPersonForm.setMemberNumber("123123123");
        confOutMembers.add(outPersonForm);
        final ConfMemberOutPersonFormRequest outPersonForm2 = new ConfMemberOutPersonFormRequest();
        outPersonForm2.setMemberName("외부참여자명222");
        outPersonForm2.setMemberNumber("123123123");
        confOutMembers.add(outPersonForm2);

        final ConfInfoFormRequest form = new ConfInfoFormRequest();
        form.setConfName("회의명55555555");         //회의실명
        form.setReserveFromTime(2000);  //회의시작시간
        form.setReserveToTime(2200);    //회의종료시간
//        form.setConfSound(0);   //회의참석시음원
        form.setIsRecord("M");  //녹취여부
        form.setConfCid("1234567"); //초대시RID
        form.setIsMachineDetect("Y"); //머신디텍트유무
//        form.setConfPasswd("1234"); //비밀번호
//        form.setConfPeerMembers(confPeerMembers);   //내부참여자(peer)
        form.setConfOutMembers(confOutMembers); //외부참여자([0]:참가자명, [1]:전화번호)
        form.setRoomNumber("00000000008");    //회의실번호
        form.setReserveDate(Date.valueOf("2020-03-30"));  //회의날짜

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(form))
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document.document(
                        requestFields(confInfoFormRequest)
                                .andWithPrefix("confOutMembers.[]", confMemberOutPersonFormRequest),
                        responseFields(
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

//    @Test
//    @Order(2)
    protected void put() throws Exception {

        final Set<String> confPeerMembers = new HashSet<>();
        confPeerMembers.add("75490677");


        final Set<ConfMemberOutPersonFormRequest> confOutMembers = new HashSet<>();
        final ConfMemberOutPersonFormRequest outPersonForm = new ConfMemberOutPersonFormRequest();
        outPersonForm.setMemberName("외부참여자명");
        outPersonForm.setMemberNumber("01036963865");
        confOutMembers.add(outPersonForm);

        final ConfInfoFormRequest form = new ConfInfoFormRequest();

        form.setConfName("회의명수정999");         //회의실명
        form.setReserveFromTime(1400);  //회의시작시간
        form.setReserveToTime(1800);    //회의종료시간
        form.setConfSound(1);   //회의참석시음원
        form.setIsRecord("S");  //녹취여부
        form.setConfCid("1"); //초대시RID
        form.setIsMachineDetect("N"); //머신디텍트유무
        form.setConfPasswd("1234666"); //비밀번호
        form.setConfPeerMembers(confPeerMembers);   //내부참여자(peer)
        form.setConfOutMembers(confOutMembers); //외부참여자([0]:참가자명, [1]:전화번호)
        form.setRoomNumber("00000000008");    //회의실번호
        form.setReserveDate(Date.valueOf("2020-03-25"));  //회의날짜

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/{seq}", 24)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .content(mapper.writeValueAsString(form)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestFields(confInfoFormRequest)
                        .andWithPrefix("confOutMembers.[]", confMemberOutPersonFormRequest),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("data").type(JsonFieldType.NUMBER).description("실제 반환될 데이터").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                        )
                ))
                .andReturn();
    }

    /**
     * 회의예약 부분 수정
     */
//    	@Test
    public void update() throws Exception {
        final ConfInfoUpdateFormRequest form = new ConfInfoUpdateFormRequest();
        form.setConfName("22222222");
        form.setTotalMemberCnt(5);
        form.setStartDate("2020-03-27 00:00:05");

        this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/update/{seq}", 11)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .content(mapper.writeValueAsString(form)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("seq").description("시퀀스")
                        ),
                        requestFields(confInfoUpdateRequest),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("data").type(JsonFieldType.NULL).description("NULL").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                        )
                ))
                .andReturn();
    }

//      @Test
//    @Order(5)
    protected void delete() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{seq}", 9)
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

    /**
     * 회의시간 중복 조회
     */
//    	@Test
    protected void duplicate() throws Exception {

        final ConfInfoDuplicateFormRequest form = new ConfInfoDuplicateFormRequest();

        form.setReserveFromTime(1900);  //회의시작시간
        form.setReserveToTime(2200);    //회의종료시간
        form.setRoomNumber("00000000008");    //회의실번호
        form.setReserveDate("2020-03-25");  //회의날짜

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL + "/duplicate")
                .contentType(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .content(mapper.writeValueAsString(form)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestFields(confInfoDuplicateFormRequest),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                                .and(fieldWithPath("data").type(JsonFieldType.NULL).description("").optional())
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();

//        string duplicate = getData(result, String.class);
//        log.info("duplicate {}", duplicate);
    }

//        @Test
    public void minutes() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/minutes/{confInfoId}",11)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("confInfoId").description("아이디")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional())
                                .and(fieldWithPath("data.").type(JsonFieldType.OBJECT).description("업로드 이력"))
                                .andWithPrefix("data.", minnutesResponse)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                                )
                ))
                .andReturn();

        final ConfInfoMinutesResponse data = getData(result, ConfInfoMinutesResponse.class);
        log.info("data {}", data);
    }

    /**
     * 회의록 저장
     */
//    	@Test
    protected void minutesSave() throws Exception {

        final ConfInfoMinutesSaveFormRequest form = new ConfInfoMinutesSaveFormRequest();

        form.setConfId(10);  //회의시퀀스
        form.setConfMinute("회의록 저장4444444");    //회의록내용

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL + "/minutes-save")
                .contentType(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .content(mapper.writeValueAsString(form)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestFields(confInfoMinutesSaveFormRequest),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                                .and(fieldWithPath("data").type(JsonFieldType.NULL).description("").optional())
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();

//        string duplicate = getData(result, String.class);
//        log.info("duplicate {}", duplicate);
    }

}
