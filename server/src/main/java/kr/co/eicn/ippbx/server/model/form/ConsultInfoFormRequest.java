package kr.co.eicn.ippbx.server.model.form;

import lombok.Data;

@Data
public class ConsultInfoFormRequest {
    private String consultType;
    private String result;
    private String suggestion;
    private String interest;
    private String etc;
    private String trTarget;
}
