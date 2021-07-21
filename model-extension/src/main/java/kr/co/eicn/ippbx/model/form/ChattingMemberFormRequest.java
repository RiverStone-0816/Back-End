package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChattingMemberFormRequest extends BaseForm {
    private List<String> memberList = new ArrayList<>();

    @Override
    public boolean validate(BindingResult bindingResult) {
        if (memberList.size() < 2)
            reject(bindingResult, "memberList", "{맴버가 2명이상이어야 합니다.}");

        return super.validate(bindingResult);
    }
}
