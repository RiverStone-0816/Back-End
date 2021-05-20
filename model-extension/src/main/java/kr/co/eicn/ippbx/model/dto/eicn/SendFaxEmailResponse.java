package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

@Data
public class SendFaxEmailResponse {
    private String phone;   //전화번호
    private String target;  //메일주소
}
