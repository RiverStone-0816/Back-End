package kr.co.eicn.ippbx.server.model.dto.eicn;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@JsonSerialize
@Getter
@Setter
public class MohDetailResponse {
	private String  category;
	private String  mohName;
	private String  companyId;
	private Integer companySeq;
	private String  directory;
	private String  fileName;
}
