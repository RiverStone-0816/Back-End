package kr.co.eicn.ippbx.model.form;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskScriptCategoryFormRequest extends BaseForm {
    private String name;
}
