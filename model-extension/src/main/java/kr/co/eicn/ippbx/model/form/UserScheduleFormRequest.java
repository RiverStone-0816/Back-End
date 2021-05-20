package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.meta.jooq.eicn.enums.UserScheduleType;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserScheduleFormRequest extends BaseForm {
    private UserScheduleType type;
    private Boolean important = false;
    private Timestamp start = new Timestamp(System.currentTimeMillis());
    private Timestamp end = new Timestamp(System.currentTimeMillis());
    private String title;
    private String contents;
    private String userid;
    private String companyId;
}
