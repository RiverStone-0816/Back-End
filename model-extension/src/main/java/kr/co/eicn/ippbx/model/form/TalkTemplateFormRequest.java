package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.CodeHasable;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jooq.tools.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import static org.apache.commons.lang3.StringUtils.containsWhitespace;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
public class TalkTemplateFormRequest extends BaseForm {

    /**
     * @see kr.co.eicn.ippbx.model.enums.TalkTemplate;
     */
    @NotNull("템플릿 타입")
    private String type;
    @NotNull("템플릿 타입")
    private MentType typeMent;
    @NotNull("템플릿 유형 데이터")
    private String typeData;
    @NotNull("템플릿명")
    private String mentName;
    @NotNull("템플릿 멘트")
    private String ment;

    private MultipartFile file;
    private String originalFileName;
    private String filePath;

    @Override
    public boolean validate(BindingResult bindingResult) {
        if (isNotEmpty(type))
            if (containsWhitespace(type))
                reject(bindingResult, "type", "{빈 공백문자를 포함할 수 없습니다.}", "");

        if (MentType.PHOTO.equals(typeMent))
            if ((file == null || file.isEmpty()) && (StringUtils.isEmpty(originalFileName) || StringUtils.isEmpty(filePath)))
                reject(bindingResult, "file", "{파일을 선택해 주세요.}");

        return super.validate(bindingResult);
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
