package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

@Data
public class TranscribeDataResponse {
    private Integer seq;   //시퀀스
    private String company_id;   //회사ID
    private Integer groupCode;   //그룹코드
    private String filePath;    //파일경로
    private String fileName;   //파일명
    private String userId;      //처리상담원
    private String hypinfo; //파일STT변환정보
    private String refinfo;
    private String learn;
    private float recRate;   //인식률
    private String status; //처리상태
    private String sttStatus;    //stt요청상태
    private String recStatus;    //인식률측정 요청상태
    private String studyStatus;  //모델학습 요청상태
    private Double nRate;         //n인식률
    private Double dRate;         //d인식률
    private Double sRate;         //s인식률
    private Double iRate;         //i인식률
    private Double aRate;        //a인식률

}
