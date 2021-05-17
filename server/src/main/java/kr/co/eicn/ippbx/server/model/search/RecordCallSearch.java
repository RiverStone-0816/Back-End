package kr.co.eicn.ippbx.server.model.search;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.co.eicn.ippbx.server.config.Constants;
import kr.co.eicn.ippbx.server.model.enums.AdditionalState;
import kr.co.eicn.ippbx.server.model.enums.CallStatus;
import kr.co.eicn.ippbx.server.util.CodeHasable;
import kr.co.eicn.ippbx.server.util.SortField;
import kr.co.eicn.ippbx.server.util.page.PageForm;
import kr.co.eicn.ippbx.server.util.page.PageQueryable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jooq.SortOrder;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
public class RecordCallSearch extends PageForm {
    @PageQueryable
    protected Integer limit = 30;

    @PageQueryable
    private Timestamp startTimestamp;   //시작일시
    @PageQueryable
    private Timestamp endTimestamp;     //종료일시
    @PageQueryable
    private String groupCode; // 조직코드
    @PageQueryable
    private String userId;    // 통화자 아이디
    @PageQueryable
    private String extension; // 내선번호
    @PageQueryable
    private String phone;     // 전화번호

    @PageQueryable
    private Sort sort = Sort.TIME_DESC; //  정렬필드

    /**
     * @see CallTime
     */
    @PageQueryable
    private String byCallTime;  // 통화시간별
    @PageQueryable
    @JsonFormat(pattern = Constants.DEFAULT_TIME_PATTERN)
    private Time startTime; // 통화 시작시간 HH:mm:ss
    @PageQueryable
    @JsonFormat(pattern = Constants.DEFAULT_TIME_PATTERN)
    private Time endTime;   // 통화 종료시간 HH:mm:ss

    /**
     * @see SearchCallType
     */
    @PageQueryable
    private String callType; // 기타선택
    /**
     * @see SearchCallStatus
     */
    @PageQueryable
    private String callStatus; // 호상태

    /**
     * @see SearchAdditionalState
     */
    @PageQueryable
    private String etcStatus; // 부가상태

    @PageQueryable
    // parent == 0 조회
    private String ivrCode; // IVR CODE
    @PageQueryable
    // parent != 0 and type != 6 and type != 7 조회
    private String ivrKey; // ivr 키 {type}_{button}

    @PageQueryable
    private String secondNum;    // 인입서비스(대표번호) 헌트경로별

    @PageQueryable
    private String iniNum;      //대표번호

    /**
     * @see SearchCustomRating
     */
    @PageQueryable
    private String customerRating; // 고객등급 일반, VIP, 블랙리스트

    @PageQueryable
    private List<Integer> seqList = new ArrayList<>();

    @PageQueryable
    private Boolean batchDownloadMode = false;

    @PageQueryable
    private Boolean batchEvaluationMode = false;

    /**
     * 정렬순서
     * 시간내림차순, 시간오름차순, 통화시간내림차순, 통화시간오름차순, 발신번호내림차순, 발신번호오름차순,
     * 수신번호내림차순, 수신번호오름차순
     */
    public enum Sort implements SortField {
        TIME_DESC("ring_date", SortOrder.DESC), TIME_ASC("ring_date", SortOrder.ASC), BILLSEC_DESC("billsec", SortOrder.DESC),
        BILLSEC_ASC("billsec", SortOrder.ASC), SRC_DESC("src", SortOrder.DESC), SRC_ASC("src", SortOrder.ASC),
        DST_DESC("dst", SortOrder.DESC), DST_ASC("dst", SortOrder.ASC);

        private final String field;
        private final SortOrder orderDirection;

        Sort(String field, SortOrder orderDirection) {
            this.field = field;
            this.orderDirection = orderDirection;
        }

        public static Sort of(Sort value) {
            for (Sort sort : values()) {
                if (Objects.equals(sort, value))
                    return sort;
            }
            return null;
        }

        @Override
        public String field() {
            return this.field;
        }

        public SortOrder getOrderDirection() {
            return orderDirection;
        }
    }

    public enum SearchCallType implements CodeHasable<String> {
        INBOUND("I"), OUTBOUND("O"), INBOUND_INNER("inbound_inner"), OUTBOUND_INNER("outbound_inner"), PDS("PDS");

        private final String code;

        SearchCallType(String code) {
            this.code = code;
        }

        public static SearchCallType of(String value) {
            for (SearchCallType searchCallType : values()) {
                if (searchCallType.getCode().equals(value)) {
                    return searchCallType;
                }
            }
            return null;
        }

        @Override
        public String getCode() {
            return this.code;
        }
    }

    public enum SearchCallStatus {
        SET(Arrays.asList(CallStatus.normal_clear, CallStatus.no_answer, CallStatus.user_busy, CallStatus.fail, CallStatus.local_forward));

        private final List<CallStatus> codes;

        SearchCallStatus(List<CallStatus> codes) {
            this.codes = codes;
        }

        public static CallStatus findByCallStatus(String code) {
            return SearchCallStatus.SET.getCodes().stream().filter(e -> e.getCode().equals(code)).findAny().orElse(null);
        }

        public List<CallStatus> getCodes() {
            return codes;
        }
    }

    public enum SearchAdditionalState {
        SET(Arrays.asList(AdditionalState.HANGUP_BEFORE_CONNECT, AdditionalState.CANCEL_CONNECT, AdditionalState.PICKUPEE, AdditionalState.PICKUPER, AdditionalState.TRANSFEREE,
                AdditionalState.TRANSFERER, AdditionalState.FORWARD_TRANSFEREE, AdditionalState.FORWARD_TRANSFERER, AdditionalState.SCD_TRANSFEREE, AdditionalState.SCD_TRANSFERER,
                AdditionalState.LOCAL_TRANSFEREE, AdditionalState.LOCAL_TRANSFERER)
        );

        private final List<AdditionalState> codes;

        SearchAdditionalState(List<AdditionalState> codes) {
            this.codes = codes;
        }

        public static AdditionalState findByAdditionalState(String code) {
            return SearchAdditionalState.SET.getCodes().stream().filter(e -> e.getCode().equals(code)).findAny().orElse(null);
        }

        public List<AdditionalState> getCodes() {
            return codes;
        }
    }

    //고객등급
    public enum SearchCustomRating implements CodeHasable<String> {
        NORMAL(""), VIP("V"), BLACK_LIST("B");

        private final String code;

        SearchCustomRating(String code) {
            this.code = code;
        }

        public static SearchCustomRating of(String value) {
            for (SearchCustomRating type : values()) {
                if (type.getCode().equals(value)) {
                    return type;
                }
            }
            return null;
        }

        @Override
        public String getCode() {
            return this.code;
        }
    }

    /**
     * ONE_LE:1분이하, ONE_TWO:1~2분, TWO_THREE:2~3분, THREE_FIVE:3~5분, FIVE_TEN:5~10분, TEN_THIRTY:10~30분, THIRTY_GE:30분이상
     */
    public enum CallTime {
        ONE_LE, ONE_TWO, TWO_THREE, THREE_FIVE, FIVE_TEN, TEN_THIRTY, THIRTY_GE
    }
}
