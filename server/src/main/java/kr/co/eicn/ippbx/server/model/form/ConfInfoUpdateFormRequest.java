package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class ConfInfoUpdateFormRequest extends BaseForm {
    private String status;
    private String inputDate;
    private String startDate;
    private String endDate;
    private String roomNumber;
    private String confName;
    private String confType;
    private String confPasswd;
    private Date reserveDate;
    private Integer reserveFromTime;
    private Integer reserveToTime;
    private String reserveAdmin;
    private Integer confSound;
    private String confCid;
    private String isRecord;
    private String isMachineDetect;
    private Integer totalMemberCnt;
    private Integer attendedMemberCnt;
    private String recordDir;
}