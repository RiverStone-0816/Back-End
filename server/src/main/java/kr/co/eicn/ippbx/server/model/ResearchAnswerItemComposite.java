package kr.co.eicn.ippbx.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class ResearchAnswerItemComposite {
	private Integer level;
	private String word; // 답변명
	private String path;
	private String parent;

	private ResearchQuestionItemComposite child;

	@JsonIgnore
	public boolean isLeaf() {
		return child == null;
	}
}
