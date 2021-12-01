package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.CodeHasable;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

@EqualsAndHashCode(callSuper = true)
@Data
public class TalkTemplateFormRequest extends BaseForm {

    /**
     * @see kr.co.eicn.ippbx.model.enums.TalkTemplate;
     */
    @NotNull("템플릿 타입")
    private String type;
    @NotNull("템플릿 타입")
    private MentType typeMent = MentType.TEXT;
    @NotNull("템플릿 유형 데이터")
    private String typeData;
    @NotNull("템플릿명")
    private String mentName;
    private String ment;

    private MultipartFile file;
    private String originalFileName;
    private String filePath;

    @Override
    public boolean validate(BindingResult bindingResult) {
        if (StringUtils.isNotEmpty(type))
            if (StringUtils.containsWhitespace(type))
                reject(bindingResult, "type", "{빈 공백문자를 포함할 수 없습니다.}", "");

        if (MentType.PHOTO.equals(typeMent)) {
            if ((file == null || file.isEmpty()) && (StringUtils.isEmpty(originalFileName) || StringUtils.isEmpty(filePath)))
                reject(bindingResult, "file", "{파일을 선택해 주세요.}");

        }  else if (MentType.TEXT.equals(typeMent)) {
            if (StringUtils.isEmpty(ment))
                reject(bindingResult, "file", "validator.blank", "멘트");
        }

        return super.validate(bindingResult);
    }

    public void setTypeMent(MentType value) {
        typeMent = value;
    }

    public void setTypeMent(String value) {
        typeMent = null;
        for (MentType e : MentType.values()) {
            if(e.getCode().equals(value) || e.name().equals(value)) {
                typeMent = e;
                return;
            }
        }
    }

    @Getter
    public enum MentType implements CodeHasable<String> {
        TEXT("T"), PHOTO("P");
        private final String code;

        MentType(String code) {
            this.code = code;
        }
    }
}
