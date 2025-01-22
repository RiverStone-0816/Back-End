package kr.co.eicn.ippbx.model.form.atcenter;

import lombok.Data;

@Data
public class ChattFormRequest {
    private String receiveUserid;
    private String channelId;
    private String channelNm;
    private String msg;
}
