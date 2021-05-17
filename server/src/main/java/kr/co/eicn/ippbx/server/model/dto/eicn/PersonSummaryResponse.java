package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

import java.util.List;

@Data
public class PersonSummaryResponse {
    private String id;      // 아이디
    private String idType;  // 아이디유형구분
    private String idName;  // 한글이름
    private String peer;
    private String extension; // 내선번호
    private String companyId; // 고객사 아이디

    private List<OrganizationSummaryResponse> companyTrees; // 조직 정보
    private String listeningRecordingAuthority; // 녹취권한 듣기
    private String downloadRecordingAuthority; // 녹취권한 다운
    private String removeRecordingAuthority; // 녹취권한 삭제
    private String dataSearchAuthority;

    private String isPds;       // pds 사용여부
    private String isStat;      // 통계,모니터링,상담원연결여부
    private String isTalk;      // 상담톡 여부
    private String isEmail;     // 이메일상담 여부
    private String isChatt;		// 메신저 사용 여부
    private String isLoginChatt; //채팅 로그인 상태
}
