package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

import java.sql.Date;

@Data
public class CalendarResponse {
    private Integer seq; // 자동증가 번호
    /**
     * @see kr.co.eicn.ippbx.model.enums.CalenderCate
     */
    private String cate; //일정 category
    private String title; //일정 타이틀
    private String type; //종일일정
    private String checking; //중요
    private Date sDate; // 시작일 yyyy-mm-dd
    private String shh; // 시작일 시간 00
    private String smm; // 시작일 분 00
    private Date eDate; // 끝일 yyyy-mm-dd
    private String ehh; // 끝일 시간 00
    private String emm; // 끝일 분 00
    private String contents; // 내용
    private String userid; // 작성자 아이디
    private String company_id; // 회사 아이디
}
