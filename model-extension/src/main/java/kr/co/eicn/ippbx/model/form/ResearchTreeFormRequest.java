package kr.co.eicn.ippbx.model.form;

import com.fasterxml.jackson.annotation.JsonInclude;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;
import java.util.TreeMap;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class ResearchTreeFormRequest extends BaseForm {
    @Valid
    private final TreeMap<Byte, ResearchTreeFormRequest> childrenMappedByAnswerNumber = new TreeMap<>();
    @NotNull("문항 아이디")
    private Integer itemId;
    @NotNull("답변에 설문아이템이 달릴수 있는가?")
    private boolean hasableAnswersChildTree;
    @Valid
    private ResearchTreeFormRequest childNotMappedByAnswerNumber;

    public ResearchTreeFormRequest(Integer itemId, boolean hasableAnswersChildTree) {
        this.itemId = itemId;
        this.hasableAnswersChildTree = hasableAnswersChildTree;
    }

    @JsonInclude
    public boolean isLeaf() {
        return childrenMappedByAnswerNumber.isEmpty() && childNotMappedByAnswerNumber == null;
    }

    @Override
    public boolean validate(BindingResult bindingResult) {
        if (!childrenMappedByAnswerNumber.isEmpty() && childNotMappedByAnswerNumber != null)
            reject(bindingResult, "childrenMappedByAnswerNumber", "{답변번호에 연결된 설문아이템과 답변번호에 연결되지 않은 설문아이템은 동시 존재할 수 없습니다.}");

        if (hasableAnswersChildTree && childNotMappedByAnswerNumber != null)
            reject(bindingResult, "childrenMappedByAnswerNumber", "{답변번호에 연결된 설문아이템과 답변번호에 연결되지 않은 설문아이템은 동시 존재할 수 없습니다.}");

        return super.validate(bindingResult);
    }
}
