package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

import java.sql.Date;

@Data
public class GroupUploadLogResponse {
    private Integer seq;    //시퀀스
    private String name;    //그룹명
    private Date makeDate;  //그룹생성일
}
