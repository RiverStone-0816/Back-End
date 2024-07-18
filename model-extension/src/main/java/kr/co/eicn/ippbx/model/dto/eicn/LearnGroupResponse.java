package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

import java.util.List;

@Data
public class LearnGroupResponse {
    private Integer seq;   //시퀀스
    private String groupName;   //그룹명
    private List<String> groupList;
    private String LearnGroupCode; //학습그룹코드 '|' 구분
    private String learnStatus; //학습요청상태
    private String learnFileName; //학습모델파일
}
