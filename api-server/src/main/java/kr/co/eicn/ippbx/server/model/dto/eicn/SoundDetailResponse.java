package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

@Data
public class SoundDetailResponse {
	private Integer seq;
	private String  soundName;
	private String  soundFile;
	private String  comment;
	private String  companyId;
}
