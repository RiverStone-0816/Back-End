package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

@Data
public class FileSummaryResponse {
	private String fileName; // 파일명
	private Long size;      // 파일 사이즈
}
