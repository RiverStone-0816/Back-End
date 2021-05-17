package kr.co.eicn.ippbx.server.model.dto.statdb;

import lombok.Data;

@Data
public class StatVocResponse {
	private Integer level;
	private String path;
	private Integer count;
}
