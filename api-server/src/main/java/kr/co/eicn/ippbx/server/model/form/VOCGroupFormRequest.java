package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.model.enums.InOutTarget;
import kr.co.eicn.ippbx.server.model.enums.ProcessKind;
import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.BindingResult;

import java.sql.Date;
import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
public class VOCGroupFormRequest extends BaseForm {
    @NotNull("VOC/해피콜명")
    private String vocGroupName;
    /**
     * @see kr.co.eicn.ippbx.server.model.enums.ProcessKind
     */
    @NotNull("진행여부")
    private String processKind;
    private Date startTerm;
    private Date endTerm;
    /**
     * @see kr.co.eicn.ippbx.server.model.enums.IsArsSms
     */
    @NotNull("진행종류")
    private String isArsSms = "ARS";
    /**
     * @see kr.co.eicn.ippbx.server.model.enums.VocGroupSender
     */
    @NotNull("진행자")
    private String sender = "MEMBER";
    private Integer arsResearchId;     //설문선택
    /**
     * @see kr.co.eicn.ippbx.server.model.enums.InOutTarget
     */
    private String outboundTarget;
    private String inboundTarget;
    private List<String> outboundMemberList;
    private List<String> inboundMemberList;
    /**
     * @see kr.co.eicn.ippbx.server.model.enums.InOutCallKind
     */
    private String outboundCallKind;    //발신콜 상태
    private String inboundCallKind;     //수신콜 상태
    private String outboundTargetCidnum;        //발신콜 발신번호
    private String inboundTargetSvcnum;        //수신콜 대표번호
    private String information;

    @Override
    public boolean validate(BindingResult bindingResult) {
        if (isNotEmpty(processKind)) {
            if (processKind.equals(ProcessKind.TERM.getCode())) {
                if (Objects.isNull(startTerm))
                    reject(bindingResult, "startTerm", "{기간을 입력하여 주세요.}", "");
                if (Objects.isNull(endTerm))
                    reject(bindingResult, "endTerm", "{기간을 입력하여 주세요.}", "");
            }
        }
        if (isNotEmpty(isArsSms)) {
            if (Objects.isNull(arsResearchId) && isArsSms.equals("ARS"))
                reject(bindingResult, "researchId", "{설문을 선택하여 주세요.}", "");
        }
        if (isNotEmpty(isArsSms) && isArsSms.equals("ARS") && isNotEmpty(sender) && sender.equals("AUTO")) {
            if (Objects.nonNull(outboundTarget)) {
                if (Objects.isNull(outboundCallKind) && !outboundTarget.contains(InOutTarget.NO.getCode()))
                    reject(bindingResult, "outboundCallKind", "{발신콜 상태를 선택하여 주세요.}", "");
                if (Objects.isNull(outboundTargetCidnum) && outboundTarget.contains(InOutTarget.CIDNUM.getCode()))
                    reject(bindingResult, "outboundCidNum", "{발신번호를 선택하여 주세요.}", "");
                if (Objects.isNull(outboundMemberList) && outboundTarget.contains(InOutTarget.MEMBER.getCode()))
                    reject(bindingResult, "outboundMemberList", "{상담원을 선택하여 주세요.}", "");
            } else {
                reject(bindingResult, "outboundTarget", "{발신콜을 선택하여 주세요.}", "");
            }

            if (Objects.nonNull(inboundTarget)) {
                if (Objects.isNull(inboundCallKind) && !inboundTarget.contains(InOutTarget.NO.getCode()))
                    reject(bindingResult, "inboundCallKind", "{수신콜 상태를 선택하여 주세요.}", "");
                if (Objects.isNull(inboundTargetSvcnum) && inboundTarget.contains(InOutTarget.SVCNUM.getCode()))
                    reject(bindingResult, "inboundSvcNum", "{대표번호를 선택하여 주세요.}", "");
                if (Objects.isNull(inboundMemberList) && inboundTarget.contains(InOutTarget.MEMBER.getCode()))
                    reject(bindingResult, "inboundMemberList", "{상담원을 선택하여 주세요.}", "");
            } else {
                reject(bindingResult, "inboundTarget", "{수신콜을 선택하여 주세요.}", "");
            }
        }

        return super.validate(bindingResult);
    }
}
