package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

import java.util.List;

@Data
public class PickUpGroupDetailResponse {
	private Integer seq;
	private Integer groupcode; // 당겨받기 그룹코드
	private String groupname; // 당겨받기명
	private String host;
	private String hostName;

	private List<SummaryPickUpPersonResponse> addOnPersons;  // 추가 가능한 내선사용자
	private List<SummaryPickUpPersonResponse> addPersons;    // 추가된 내선사용자
}
