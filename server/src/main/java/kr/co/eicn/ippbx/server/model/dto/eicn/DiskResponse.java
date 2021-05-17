package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

@Data
public class DiskResponse {
	private String used; // 사용중인 디스크
	private String avail; // 사용가능 디스크
	private String use; // 사용비율
}
