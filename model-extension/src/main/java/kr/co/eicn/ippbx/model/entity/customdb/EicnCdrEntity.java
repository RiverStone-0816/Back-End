package kr.co.eicn.ippbx.model.entity.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonEicnCdr;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.model.enums.AdditionalState;
import kr.co.eicn.ippbx.model.enums.CallStatus;
import kr.co.eicn.ippbx.model.enums.CallType;
import kr.co.eicn.ippbx.model.enums.ContextType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static org.apache.commons.lang3.StringUtils.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class EicnCdrEntity extends CommonEicnCdr {
    private PersonList personList;  // 상담원 정보
    private int nCallResult = 0;

    private String ivrPathValue;   // IVR value
    private String personNameValue; // 상담원 value
    private String callKindValue; // 콜이력 수발신 구분 value
    private String callStatusValue; // 호상태 value
    private String etcCallResultValue; // 콜 부가상태 값
    private String callingHangupValue; // 통화 종류

    /**
     * IVR value
     */
    public String getIvrPathValue() {
        final StringBuilder ivrPath = new StringBuilder();
        if (isEmpty(this.getIvrKey()))
            return EMPTY;

        for (String ivrKey : this.getIvrKey().split("\\|")) {
            String[] s = ivrKey.split("_");
            if (s.length == 3) {
                ivrPath.append(s[2]);
            }
        }
        return ivrPath.toString();
    }

    /**
     * 상담원 value
     */
    public String getPersonNameValue() {
        if (isNotEmpty(getTurnOverKind()) && AdditionalState.TRANSFEREE.getCode().equals(getTurnOverKind())) // 기존 etc3
            return getDst();
        if (personList != null && isNotEmpty(personList.getIdName()))
            return personList.getIdName();
        if (isNotEmpty(getSecondNum()))
            return getSecondNum();
        return getIniNum();
    }

    /**
     * 콜이력 수발신 구분 value
     */
    public String getCallKindValue() {
        final StringBuilder callKind = new StringBuilder();
        if (CallType.INBOUND.getCode().equals(getInOut())) callKind.append("수신");
        else if (CallType.OUTBOUND.getCode().equals(getInOut())) callKind.append("발신");
        if (isNotEmpty(getDcontext())) {
            if (getDcontext().equals("outbound_inner") || getDcontext().equals("inbound_inner"))
                callKind.append("(내선)");
        }
        return callKind.toString();
    }

    /**
     * 호상태 value
     */
    public String getCallStatusValue() {
        final StringBuilder result = new StringBuilder();
        if (isNotEmpty(getTurnOverKind())) { // 기존 etc3
            if (AdditionalState.LOCAL_TRANSFERER.getCode().equals(getTurnOverKind())
                    || AdditionalState.EXTEN_TRANSFERER.getCode().equals(getTurnOverKind())
                    || AdditionalState.SCD_TRANSFERER.getCode().equals(getTurnOverKind())
                    || AdditionalState.PICKUPEE.getCode().equals(getTurnOverKind())) {
                return "기타";
            }
        }

        if (isNotEmpty(getHangupCause())) { // 기존 etc1
            if (getHangupCause().startsWith(CallStatus.normal_clear.getCode())) { // 기존 etc1
                if (getBillsec() > 0) {
                    result.append("정상통화").append("(").append(getBillsec()).append("/").append(getDuration()).append(")");
                } else {
                    result.append("비수신");
                    nCallResult = 3;
                }
            } else if (getHangupCause().startsWith(CallStatus.user_busy.getCode())) {
                if (isNotEmpty(getInOut()) && (CallType.OUTBOUND.getCode().equals(getHangupCause())
                        || "inbound_inner".equals(getDcontext())
                        || "NEIGHBOR".equals(getDcontext()))) {
                    result.append("통화중");
                } else {
                    result.append("비수신");
                    nCallResult = 3;
                }
            } else if (getHangupCause().startsWith(CallStatus.no_answer.getCode() + "(")) {
                result.append("비수신").append("(").append(getBillsec()).append("/").append(getDuration()).append(")");
                nCallResult = 3;
            } else if (getHangupCause().startsWith(CallStatus.pds.getCode() + "(")) {
                result.append("PDS비연결").append("(").append(getBillsec()).append("/").append(getDuration()).append(")");
                nCallResult = 3;
            } else if (getHangupCause().startsWith(CallStatus.vm.getCode() + "(")) {
                result.append("VM").append("(").append(getBillsec()).append("/").append(getDuration()).append(")");
                nCallResult = 3;
            } else if (getHangupCause().startsWith(CallStatus.fax.getCode() + "(")) {
                result.append("fax").append("(").append(getBillsec()).append("/").append(getDuration()).append(")");
                nCallResult = 3;
            } else if (getHangupCause().startsWith(CallStatus.ars.getCode() + "(")) {
                result.append("ars").append("(").append(getBillsec()).append("/").append(getDuration()).append(")");
                nCallResult = 3;
            } else if (getHangupCause().startsWith(CallStatus.local_forward.getCode() + "(")) {
                if (getHangupCause().contains("not_avail_status_")) {
                    result.append("기타".concat(getHangupCause().replace("330", EMPTY).replace("not_avail_status_", "상태:")));
                } else if (getHangupCause().contains("forward_")) {
                    result.append("기타".concat(getHangupCause().replace("330", EMPTY).replace("forward_", "포워딩:")));
                } else if (getHangupCause().contains("local_")) {
                    result.append("기타(전화기포워딩)");
                } else if (getHangupCause().contains("trans_")) {
                    result.append("기타(돌려주기)");
                } else {
                    result.append("기타비수신");
                }
            } else {
                result.append("실패");
            }
        }
        return result.toString();
    }

    /**
     * 콜 부가상태 값
     */
    public String getEtcCallResultValue() {
        final StringBuilder result = new StringBuilder();
        final String suffix = "(" + getTurnOverNumber() + ")"; // 기존 etc2
        if (CallType.INBOUND.getCode().equals(getInOut()) && nCallResult == 3 && getBillsec() == 0) {
            if (ContextType.CALL_BACK.getCode().equals(getDcontext()) && getDetailCallstatus().contains("event_server_reserve")) {
                if (getDetailCallstatus().contains("yes"))
                    result.append("상담예약(성공)");
                else
                    result.append("상담중예약중끊음");
            } else if (startsWith(getDetailCallstatus(), "event_server_hunt") || startsWith(getDetailCallstatus(), "AgentCalledEvent")
                    || startsWith(getDetailCallstatus(), "event_server_pers")
                    || startsWith(getDetailCallstatus(), "NewStateEvent")) {
                if (isEmpty(getSecondNum()))
                    result.append("개인전화비수신");
                else
                    result.append("큐대기중끊음");
            } else if (startsWith(getDetailCallstatus(), "event_server_inbound") || startsWith(getDetailCallstatus(), "event_server_ivrkey")) {
                result.append("연결전끊음");
            } else {
                result.append(EMPTY);
            }
        } else {
            if (isEmpty(getTurnOverKind())) // 기존 etc3
                result.append(EMPTY);
            else
                result.append(TurnOverKindSearch(AdditionalState.of(getTurnOverKind()))).append(suffix); // 기존 etc3
        }
        return result.toString();
    }

    private String TurnOverKindSearch(AdditionalState enums){
        if(AdditionalState.HANGUP_BEFORE_CONNECT.equals(enums))
            return "연결전끊음";
        if(AdditionalState.CANCEL_CONNECT.equals(enums))
            return "포기호(헌트대기중끊음)";
        if(AdditionalState.PICKUPEE.equals(enums))
            return "당겨짐";
        if(AdditionalState.PICKUPER.equals(enums))
            return "당겨받음";
        if(AdditionalState.PROTECT.equals(enums))
            return "ARS멘트";
        if(AdditionalState.TRANSFEREE.equals(enums))
            return "돌려받음";
        if(AdditionalState.TRANSFERER.equals(enums))
            return "돌려줌";
        if(AdditionalState.FORWARD_TRANSFEREE.equals(enums))
            return "내선설정포워딩됨";
        if(AdditionalState.FORWARD_TRANSFERER.equals(enums))
            return "내선설정포워딩함";
        if(AdditionalState.SCD_TRANSFEREE.equals(enums))
            return "스케쥴포워딩됨";
        if(AdditionalState.SCD_TRANSFERER.equals(enums))
            return "스케쥴포워딩함";
        if(AdditionalState.LOCAL_TRANSFEREE.equals(enums))
            return "전화기포워딩됨";
        if(AdditionalState.LOCAL_TRANSFERER.equals(enums))
            return "전화기포워딩함";
        if(AdditionalState.ATTENDEDOUT_TRANSFEREE.equals(enums))
            return "외부돌려받음";
        if(AdditionalState.ATTENDEDOUT_TRANSFERER.equals(enums))
            return "외부돌려줌";
        if(AdditionalState.INNER_PICKUPER.equals(enums))
            return "INNER_PICKUPER";
        if(AdditionalState.INNER_TRANSFEREE.equals(enums))
            return "내선포워딩됨";
        if(AdditionalState.INNER_TRANSFERER.equals(enums))
            return "내선포워딩함";
        if(AdditionalState.INNER_TRANS_BACK.equals(enums))
            return "내선되돌려받음";
        if(AdditionalState.EXTEN_TRANSFERER.equals(enums))
            return "EXTEN_TRANSFERER";
        if(AdditionalState.REDIRECTOUT_TRANSFEREE.equals(enums))
            return "외부돌려받음";
        if(AdditionalState.REDIRECTOUT_TRANSFERER.equals(enums))
            return "외부돌려줌";
        if(AdditionalState.SPY.equals(enums))
            return "엿듣기";
        if(AdditionalState.TRANS_BACK.equals(enums))
            return "되돌려받음";

        return "";
    }
    /**
     * 통화 종류
     */
    public String getCallingHangupValue() {
        String calleeHangup = getCalleeHangup();
        String returnValue = EMPTY;

        if (defaultString(getSrc()).length() <= 4 && defaultString(getDst()).length() <= 4)
            return returnValue;

        if (CallType.OUTBOUND.getCode().equals(getInOut())) {
            if (getBillsec() > 0 && "Y".equals(calleeHangup)) {
                returnValue = "고객";
            } else if (getBillsec() > 0) {
                returnValue = "상담";
            }
        } else {
            if (getBillsec() > 0 && "Y".equals(calleeHangup)) {
                returnValue = "상담";
            } else if (getBillsec() > 0) {
                returnValue = "고객";
            }
        }

        return returnValue;
    }
}
