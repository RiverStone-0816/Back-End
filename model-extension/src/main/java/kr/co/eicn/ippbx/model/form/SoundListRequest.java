package kr.co.eicn.ippbx.model.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.co.eicn.ippbx.model.enums.Bool;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

@EqualsAndHashCode(callSuper = true)
@Data
public class SoundListRequest extends BaseForm {
	@NotNull("음원명")
	private String  soundName;
	private Bool protectArsYn = Bool.N;
	@JsonIgnore
	@NotNull("음원파일")
	private MultipartFile file;
	private String  comment;
}
