package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

import java.util.Objects;

/**
 * 콜 부가상태
 * // 1=연결전끊음
 * // 2=큐대기중끊음
 * // PICKUPEE=당겨짐
 * // PICKUPER=당겨받음
 * // PROTECT=성희롱,폭언 멘트
 * // TRANSFEREE=돌려받음
 * // TRANSFERER=돌려줌
 * // FORWARD_TRANSFEREE=내선설정포워딩됨
 * // FORWARD_TRANSFERER=내선설정포워딩함
 * // SCD_TRANSFEREE=스케쥴포워딩됨
 * // SCD_TRANSFERER=스케쥴포워딩함
 * // LOCAL_TRANSFEREE=전화기포워딩됨
 * // LOCAL_TRANSFERER=전화기포워딩함
 * // ATTENDEDOUT_TRANSFEREE=외부돌려받음
 * // ATTENDEDOUT_TRANSFERER=외부돌려줌
 * // INNER_TRANSFEREE=내선포워딩됨
 * // INNER_TRANSFERER=내선포워딩함
 * // REDIRECTOUT_TRANSFEREE=외부돌려받음
 * // REDIRECTOUT_TRANSFERER=외부돌려줌
 * // SPY=엿듣기
 * // TRANS_BACK=되돌려받음
 */
public enum AdditionalState implements CodeHasable<String> {
    HANGUP_BEFORE_CONNECT("1"),
    CANCEL_CONNECT("2"),
    PICKUPEE("PICKUPEE"),
    PICKUPER("PICKUPER"),
    PROTECT("PROTECT"),
    TRANSFEREE("TRANSFEREE"),
    TRANSFERER("TRANSFERER"),
    FORWARD_TRANSFEREE("FORWARD_TRANSFEREE"),
    FORWARD_TRANSFERER("FORWARD_TRANSFERER"),
    SCD_TRANSFEREE("SCD_TRANSFEREE"),
    SCD_TRANSFERER("SCD_TRANSFERER"),
    LOCAL_TRANSFEREE("LOCAL_TRANSFEREE"),
    LOCAL_TRANSFERER("LOCAL_TRANSFERER"),
    ATTENDEDOUT_TRANSFEREE("ATTENDEDOUT_TRANSFEREE"),
    ATTENDEDOUT_TRANSFERER("ATTENDEDOUT_TRANSFERER"),
    INNER_PICKUPER("INNER_PICKUPER"),
    INNER_TRANSFEREE("INNER_TRANSFEREE"),
    INNER_TRANSFERER("INNER_TRANSFERER"),
    INNER_TRANS_BACK("INNER_TRANS_BACK"),
    INNER_LOCAL_TRANSFEREE("INNER_LOCAL_TRANSFEREE"),
    INNER_LOCAL_TRANSFERER("INNER_LOCAL_TRANSFERER"),
    EXTEN_TRANSFERER("EXTEN_TRANSFERER"),
    REDIRECTOUT_TRANSFEREE("REDIRECTOUT_TRANSFEREE"),
    REDIRECTOUT_TRANSFERER("REDIRECTOUT_TRANSFERER"),
    SPY("SPY"),
    TRANS_BACK("TRANS_BACK"),
    CALLBOT_TRANSFEREE("CALLBOT_TRANSFEREE"),
    CALLBOT_TRANSFERER("CALLBOT_TRANSFERER");

    private final String code;

    AdditionalState(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    public static AdditionalState of(String value) {
        for (AdditionalState e : values()) {
            if (Objects.equals(e.getCode(), value))
                return e;
        }
        return null;
    }
}
