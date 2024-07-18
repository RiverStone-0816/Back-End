package kr.co.eicn.ippbx.model.dto.eicn;


import lombok.Data;

import java.util.List;

@Data
public class TranscribeGroupResponse {
    private Integer seq;   //시퀀스
    private String groupName;   //그룹명
    private String userId;   //담당상담원명
    private String status;   //그룹진행상태
    private Integer fileCnt;   //업로드파일수
    private Double recRate;   //인식률

    private List<TranscribeDataResponse> dataInfos; //파일데이터 목록
}
