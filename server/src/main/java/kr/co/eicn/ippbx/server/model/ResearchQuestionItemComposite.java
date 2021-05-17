package kr.co.eicn.ippbx.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.LinkedList;

@Data
public class ResearchQuestionItemComposite {
	private Integer level;
	private String word; // 질문명
	private String path;
	private String parent;

	private LinkedList<ResearchAnswerItemComposite> answerItems;
	private LinkedList<ResearchQuestionItemComposite> childNode; // TODO: refac!!!

	@JsonIgnore
	public boolean isLeaf() {
		return childNode == null || childNode.isEmpty();
	}

	@JsonIgnore
	public void addAnswerItems(ResearchAnswerItemComposite node) {
		if (answerItems == null)
			answerItems = new LinkedList<>();

		answerItems.add(node);
	}

	@JsonIgnore
	public void addChildNode(ResearchQuestionItemComposite node) {
		if (childNode == null)
			childNode = new LinkedList<>();

		childNode.add(node);
	}
}
