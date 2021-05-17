package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.server.model.RecordFile;
import kr.co.eicn.ippbx.server.model.dto.customdb.CommonEicnCdrResponse;
import kr.co.eicn.ippbx.server.model.entity.customdb.EicnCdrEvaluationEntity;
import kr.co.eicn.ippbx.server.model.form.RecordDownFormRequest;
import kr.co.eicn.ippbx.server.model.search.RecordCallSearch;
import kr.co.eicn.ippbx.server.util.MultiValueMapConverter;
import kr.co.eicn.ippbx.server.util.page.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.MultiValueMap;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class RecordApiControllerTest extends BaseControllerTest {
	private final String TEST_URL = "/api/v1/admin/record/history";

	private final FieldDescriptor[] recordEntity = new FieldDescriptor[] {
			fieldWithPath("seq").type(JsonFieldType.NUMBER).description(""),
			fieldWithPath("ringDate").type(JsonFieldType.STRING).description("전화수발신시간").optional(),
			fieldWithPath("waitingDate").type(JsonFieldType.STRING).description("헌트에들어와서기다리기시작한시간").optional(),
			fieldWithPath("dialupDate").type(JsonFieldType.STRING).description("상대방이전화를받은시간").optional(),
			fieldWithPath("hangupDate").type(JsonFieldType.STRING).description("전화가종료된시간").optional(),
			fieldWithPath("duration").type(JsonFieldType.NUMBER).description("hangup_date-ring_date").optional(),
			fieldWithPath("billsec").type(JsonFieldType.NUMBER).description("hangup_date-dialup_date").optional(),
			fieldWithPath("waitsec").type(JsonFieldType.NUMBER).description("waiting_date-dialup_date").optional(),
			fieldWithPath("inOut").type(JsonFieldType.STRING).description("수신/발신여부").optional(),
			fieldWithPath("dcontext").type(JsonFieldType.STRING).description("다이얼플랜에서의컨텍스트").optional(),
			fieldWithPath("peer").type(JsonFieldType.STRING).description("전화기아이디").optional(),
			fieldWithPath("src").type(JsonFieldType.STRING).description("수신내선또는수신고객번호").optional(),
			fieldWithPath("dst").type(JsonFieldType.STRING).description("발신내선또는발신고객번호").optional(),
			fieldWithPath("callstatus").type(JsonFieldType.STRING).description("전화상태 C-클릭투콜 R-링이가는중 D-전화받음 H-전화끊음").optional(),
			fieldWithPath("detailCallstatus").type(JsonFieldType.STRING).description("다이얼플랜상디테일콜상태또는단계").optional(),
			fieldWithPath("recordInfo").type(JsonFieldType.STRING).description("녹취 S-녹취없음 M-녹취됨 M_$순차 -부분녹취가 발생하여 녹취가 여려개임").optional(),
			fieldWithPath("recordFile").type(JsonFieldType.STRING).description("녹취파일경로").optional(),
			fieldWithPath("uniqueid").type(JsonFieldType.STRING).description("아스터리스크 발신쪽 콜유니크 아이디 돌려줬을경우 같을수도 있음").optional(),
			fieldWithPath("dstUniqueid").type(JsonFieldType.STRING).description("아스터리스크 수신쪽 콜유니크").optional(),
			fieldWithPath("channel").type(JsonFieldType.STRING).description("아스터리스크 발신쪽 채널아이디").optional(),
			fieldWithPath("dstChannel").type(JsonFieldType.STRING).description("아스터리스크 수신쪽 채널아이디").optional(),
			fieldWithPath("iniNum").type(JsonFieldType.STRING).description("인바운드에서는첫인입대표번호, 아웃바운드에서는 발신CID").optional(),
			fieldWithPath("secondNum").type(JsonFieldType.STRING).description("인바운드에서는헌트번호 아웃바운드에서는 발신과금번호").optional(),
			fieldWithPath("ivrKey").type(JsonFieldType.STRING).description("인바운드에서 IVR순서").optional(),
			fieldWithPath("queueStrategy").type(JsonFieldType.STRING).description("헌트의 콜분배 STRATEGY").optional(),
			fieldWithPath("queueSequence").type(JsonFieldType.STRING).description("헌트의 콜분배순서 전화기아이디").optional(),
			fieldWithPath("calleeHangup").type(JsonFieldType.STRING).description("Y-받은사람이먼저끊음 N-건사람이 먼저끊음").optional(),
			fieldWithPath("webVoiceInfo").type(JsonFieldType.STRING).description("보이는ARS사용여부").optional(),
			fieldWithPath("vipBlack").type(JsonFieldType.STRING).description("VIP-V,BLACK-B").optional(),
			fieldWithPath("userid").type(JsonFieldType.STRING).description("전화기사용자아이디").optional(),
			fieldWithPath("groupCode").type(JsonFieldType.STRING).description("전화기사용자소속코드").optional(),
			fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("전화기사용자소속트리명").optional(),
			fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("전화기사용자소속트리레벨").optional(),
			fieldWithPath("companyId").type(JsonFieldType.STRING).description("컴퍼니아이디").optional(),
			fieldWithPath("host").type(JsonFieldType.STRING).description("콜이진행된PBX_SERVER").optional(),
			fieldWithPath("hangupCause").type(JsonFieldType.STRING).description("전화가끊어진원인").optional(),
			fieldWithPath("cmpClickKey").type(JsonFieldType.STRING).description("전화돌려주기,당겨받기 순서").optional(),
			fieldWithPath("cmpClickFrom").type(JsonFieldType.STRING).description("콜을시도한성격 PDS,PRV,MAINDB등등").optional(),
			fieldWithPath("cmpGroupId").type(JsonFieldType.NUMBER).description("캠페인그룹").optional(),
			fieldWithPath("cmpCustomid").type(JsonFieldType.STRING).description("캠페인고객아이디").optional(),
			fieldWithPath("turnOverCnt").type(JsonFieldType.NUMBER).description("전화돌려주기,당겨받기 순서").optional(),
			fieldWithPath("turnOverKind").type(JsonFieldType.STRING).description("전화돌려주기,당겨받기 종류").optional(),
			fieldWithPath("turnOverNumber").type(JsonFieldType.STRING).description("전화돌려주기,당겨받기 번호").optional(),
			fieldWithPath("turnOverUniqueid").type(JsonFieldType.STRING).description("전화돌려주기,당겨받기 uniqueid").optional(),
			fieldWithPath("etc1").type(JsonFieldType.STRING).description("기타1").optional(),
			fieldWithPath("etc2").type(JsonFieldType.STRING).description("기타2").optional(),
			fieldWithPath("etc3").type(JsonFieldType.STRING).description("기타3").optional(),
			fieldWithPath("etc4").type(JsonFieldType.STRING).description("기타4").optional(),
			fieldWithPath("etc5").type(JsonFieldType.STRING).description("기타5").optional(),

			fieldWithPath("personList").type(JsonFieldType.OBJECT).description("상담원 정보").optional(),
			fieldWithPath("service").type(JsonFieldType.OBJECT).description("대표번호 서비스 정보").optional(),
			fieldWithPath("ivrPathValue").type(JsonFieldType.STRING).description("IVR value").optional(),
			fieldWithPath("personNameValue").type(JsonFieldType.STRING).description("상담원명").optional(),
			fieldWithPath("callKindValue").type(JsonFieldType.STRING).description("콜이력 수발신 구분").optional(),
			fieldWithPath("callStatusValue").type(JsonFieldType.STRING).description("호상태").optional(),
			fieldWithPath("etcCallResultValue").type(JsonFieldType.STRING).description("콜 부가상태").optional(),
			fieldWithPath("callingHangupValue").type(JsonFieldType.STRING).description("콜 부가상태").optional()
	};

	private FieldDescriptor[] personList = new FieldDescriptor[] {
			fieldWithPath("id").type(JsonFieldType.STRING).description("상담원 아이디").optional(),
			fieldWithPath("idType").type(JsonFieldType.STRING).description("아이디 유형구분(J:마스터, A:슈퍼어드민, B:일반어드민, M:상담원)").optional(),
			fieldWithPath("idName").type(JsonFieldType.STRING).description("한글이름").optional(),
			fieldWithPath("extension").type(JsonFieldType.STRING).description("내선전화번호").optional(),
			fieldWithPath("companyTrees").type(JsonFieldType.ARRAY).description("조직정보").optional(),
			fieldWithPath("listeningRecordingAuthority").type(JsonFieldType.STRING).description("녹취권한 듣기").optional(),
			fieldWithPath("downloadRecordingAuthority").type(JsonFieldType.STRING).description("녹취권한 다운").optional(),
			fieldWithPath("removeRecordingAuthority").type(JsonFieldType.STRING).description("녹취권한 삭제").optional(),
			fieldWithPath("isPds").type(JsonFieldType.STRING).description("PDS 사용여부").optional(),
			fieldWithPath("isStat").type(JsonFieldType.STRING).description("통계,모니터링,상담원연결여부").optional(),
			fieldWithPath("isTalk").type(JsonFieldType.STRING).description("상담톡 여부").optional(),
			fieldWithPath("isEmail").type(JsonFieldType.STRING).description("이메일상담 여부").optional(),

			fieldWithPath("groupCode").type(JsonFieldType.STRING).description("groupCode").optional(),
			fieldWithPath("idStatus").type(JsonFieldType.VARIES).description("근무상태('': 정상근무, S:휴직, X:퇴직)").optional(),
			fieldWithPath("hpNumber").type(JsonFieldType.STRING).description("휴대전화번호").optional(),
			fieldWithPath("emailInfo").type(JsonFieldType.STRING).description("이메일").optional()
	};

	private FieldDescriptor[] service = new FieldDescriptor[] {
			fieldWithPath("seq").type(JsonFieldType.NUMBER).description("시퀀스키").optional(),
			fieldWithPath("svcName").type(JsonFieldType.STRING).description("대표번호 한글명").optional(),
			fieldWithPath("svcNumber").type(JsonFieldType.STRING).description("11자리 대표번호").optional(),
			fieldWithPath("svcCid").type(JsonFieldType.STRING).description("원서비스번호").optional(),
			fieldWithPath("companyId").type(JsonFieldType.STRING).description("회사아이디").optional(),
			fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드").optional(),
			fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("윗레벨의 코드를 포함한 코드의 나열(002_003_008)").optional(),
			fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("조직레벨").optional(),
			fieldWithPath("serviceLevel").type(JsonFieldType.NUMBER).description("서비스레벨").optional(),
			fieldWithPath("hostName").type(JsonFieldType.STRING).description("교환기명").optional()
	};

//	@Test
	public void pagination() throws Exception {
		final RecordCallSearch search = new RecordCallSearch();
		search.setStartTimestamp(Timestamp.valueOf("2020-11-23 09:00:00")); // 시작일시 yyyy-MM-dd HH:mm:ss
		search.setEndTimestamp(Timestamp.valueOf("2020-11-23 18:00:00")); // 종료일시
//		search.setGroupCode(""); // 조직코드
//		search.setUserId("user0101"); // 통화자 아이디
//		search.setExtension("8897"); // 내선번호
//		search.setPhone("01030068249"); // 전화번호

//		search.setSort(RecordCallSearch.Sort.BILLSEC_ASC); // 정렬필드
//		search.setByCallTime(RecordCallSearch.CallTime.FIVE_TEN.name());    // 통화시간별
//		search.setStartTime(Time.valueOf("00:00:11")); // 통화 시작시간 HH:mm:ss
//		search.setEndTime(Time.valueOf("00:01:00")); //  통화 종료시간  HH:mm:ss
//		search.setCallType(RecordCallSearch.SearchCallType.INBOUND.getCode()); // 기타선택 통화 종륲
//		search.setCallStatus(RecordCallSearch.SearchCallStatus.findByCallStatus("16").getCode()); //  호상태
//		search.setEtcStatus(RecordCallSearch.SearchAdditionalState.findByAdditionalState("1").getCode()); // 부가상태
		search.setIvrCode(""); // IVR code
		search.setIvrKey(""); // ivr key value  {type}_{button}
		search.setIniNum(""); // 인입서비스(대표번호) 헌트경로별
//		search.setCustomerRating(RecordCallSearch.SearchCustomRating.NORMAL.getCode()); // 고객등급

		final MultiValueMap<String, String> params = MultiValueMapConverter.convert(mapper, search);

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/search")
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
//				.params(params))
				.andExpect(status().isOk())
//				.andDo(document.document(
//						requestParameters(
//							parameterWithName("page").description("현재 페이지").optional(),
//							parameterWithName("limit").description("페이지 개수").optional(),
//							parameterWithName("startTimestamp").description("시작일시").optional(),
//							parameterWithName("endTimestamp").description("종료일시").optional(),
//							parameterWithName("groupCode").description("조직코드").optional(),
//							parameterWithName("userId").description("통화자 아이디").optional(),
//							parameterWithName("extension").description("내선번호").optional(),
//							parameterWithName("phone").description("전화번호").optional(),
//							parameterWithName("sort").description("정렬필드").optional(),
//							parameterWithName("byCallTime").description("통화시간별").optional(),
//							parameterWithName("startTime").description("통화 시작시간 HH:mm:ss").optional(),
//							parameterWithName("endTime").description("통화 종료시간 HH:mm:ss").optional(),
//							parameterWithName("callType").description("기타선택").optional(),
//							parameterWithName("callStatus").description("호상태").optional(),
//							parameterWithName("etcStatus").description("부가상태").optional(),
//							parameterWithName("ivrCode").description("IVR CODE").optional(),
//							parameterWithName("ivrKey").description("ivr 키 {type}_{button}").optional(),
//							parameterWithName("iniNum").description("인입서비스(대표번호) 헌트경로별").optional(),
//							parameterWithName("customerRating").description("고객등급 일반, VIP, 블랙리스트").optional(),
//							parameterWithName("orderDirection").description("정렬 방향(ASC, DESC)").optional(),
//							parameterWithName("offset").description("사용안함").optional().ignored()
//						),
//						responseFields(
//								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
//								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
//						)
//						.and(fieldWithPath("data.rows[]").type(JsonFieldType.ARRAY).description("녹취통화이력 목록").optional())
//
//						.andWithPrefix("data.rows[]", recordEntity)
//						.andWithPrefix("data.rows[].personList.", personList)
//						.andWithPrefix("data.rows[].service.", service)
//						.andWithPrefix("data.",
//							fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지"),
//							fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총갯수")
//						)
//						.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
//				))
				.andReturn();

		final Pagination<CommonEicnCdrResponse> pagination = paginationData(result, CommonEicnCdrResponse.class);
		final List<CommonEicnCdrResponse> rows = pagination.getRows();
		for (CommonEicnCdrResponse row : rows) {
			log.info("seq:{}, uniqueId:{}, 고객등급:{}, 대표번호:{}, 발신번호:{}, 수신번호:{}, 시간:{}, 수/발신:{}, 통화자:{}, 호상태(초):{}, 부가상태:{}, IVR:{}, 종류:{}, 녹취유무:{}"
			 , row.getSeq()
			 , row.getUniqueid()
			 , row.getVipBlack()
			 , row.getService() != null ? row.getService().getSvcName() : ""
			 , row.getSrc()
			 , row.getDst()
			 , row.getRingDate()
			 , row.getInOut()
			 , row.getPersonNameValue()
			 , row.getCallStatusValue()
			 , row.getEtcCallResultValue()
			 , row.getIvrPathValue()
			 , row.getCallingHangupValue()
			 , row.getRecordFile());
		}
	}

//	@Test
	public void getFiles() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{seq}/record-files", 978)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
//				.andDo(document.document(
//					pathParameters(parameterWithName("seq").description("SEQUENCE KEY")),
//					responseFields(
//							fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
//							fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
//							fieldWithPath("data").description("녹취파일 목록").optional()
//					)
//					.andWithPrefix("data.[]", fieldWithPath("filePath").type(JsonFieldType.STRING).description("파일명을 포함한 파일경로"),
//							fieldWithPath("recordFileKind").type(JsonFieldType.STRING).description("녹취통화 구분"))
//					.and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
//				))
				.andExpect(status().isOk())
				.andReturn();

		final List<RecordFile> recordFiles = listData(result, RecordFile.class);
	}

	//	@Test
	public void resource() throws Exception {
		MvcResult mvcResult = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/resource")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.param("token", getAuthToken())
				.param("path", ""))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						requestParameters(
								parameterWithName("token").description("토큰 키"),
								parameterWithName("path").description("")
						),
						responseBody()
				))
				.andReturn();
	}

	/**
	 * 녹취 일괄다운로드 등록
	 */
//	@Test
	public void recordDownRegister() throws Exception {
		final RecordDownFormRequest form = new RecordDownFormRequest();
		form.setDownName("녹취 일괄등록 테스트");
		form.setSequences(Arrays.asList(9, 10, 11));

		final MultiValueMap<String, String> params = MultiValueMapConverter.convert(mapper, form);

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL + "/record-batch-download-register")
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor())
				.params(params))
				.andExpect(status().isOk())
				.andDo(document.document(
						requestParameters(
								parameterWithName("downName").description("다운로드명"),
								parameterWithName("sequence").description("통화이력 SEQUENCE 목록")
						),
						responseFields(
								fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
								fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
								fieldWithPath("data").type(JsonFieldType.STRING).description("").optional(),
								fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
						)
				))
				.andReturn();

		String ret = getData(result, String.class);

		log.info("ret {}", ret);
	}

//	@Test
	protected void get() throws Exception {
		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{seq}/evaluation", 858)
				.accept(MediaType.APPLICATION_JSON)
				.with(new JwtRequestPostProcessor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		final EicnCdrEvaluationEntity data = getData(result, EicnCdrEvaluationEntity.class);
	}
}
