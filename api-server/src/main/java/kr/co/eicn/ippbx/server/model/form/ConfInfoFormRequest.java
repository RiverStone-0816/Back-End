package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Date;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class ConfInfoFormRequest extends BaseForm {
    @NotNull("회의명")
    private String confName;
    /**
     * reserveFromTime = (시간*60)+분
     * 예제 : 시간이 10시10분이면 reserveFromTime은 610
     * 회의종료시간도 동일
     */
    @NotNull("회의시작시간")
    private Integer reserveFromTime;
    @NotNull("회의종료시간")
    private Integer reserveToTime;
    private Integer confSound;
    @NotNull("녹취여부")
    private String isRecord;
    @NotNull("초대시RID")
    private String confCid;
    @NotNull("머신디텍트")
    private String isMachineDetect;
    private String confPasswd;
    private Set<String> confPeerMembers;//내부참여자
    private Set<ConfMemberOutPersonFormRequest> confOutMembers;//외부참여자
    @NotNull("회의실번호")
    private String roomNumber;
    @NotNull("회의날짜")
    private Date reserveDate;
}