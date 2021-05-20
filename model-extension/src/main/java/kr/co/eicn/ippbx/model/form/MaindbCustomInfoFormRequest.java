package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.model.enums.MultichannelChannelType;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class MaindbCustomInfoFormRequest extends BaseForm {

    private List<ChannelForm> channels;

    @NotNull("groupSeq")
    private Integer groupSeq;

    private Date date_1;
    private Date date_2;
    private Date date_3;
    private Date day_1;
    private Date day_2;
    private Date day_3;
    private Timestamp datetime_1;
    private Timestamp datetime_2;
    private Timestamp datetime_3;
    private Integer int_1;
    private Integer int_2;
    private Integer int_3;
    private Integer int_4;
    private Integer int_5;
    private String string_1;
    private String string_2;
    private String string_3;
    private String string_4;
    private String string_5;
    private String string_6;
    private String string_7;
    private String string_8;
    private String string_9;
    private String string_10;
    private String string_11;
    private String string_12;
    private String string_13;
    private String string_14;
    private String string_15;
    private String string_16;
    private String string_17;
    private String string_18;
    private String string_19;
    private String string_20;
    private String code_1;
    private String code_2;
    private String code_3;
    private String code_4;
    private String code_5;
    private String code_6;
    private String code_7;
    private String code_8;
    private String code_9;
    private String code_10;
    private String img_1;
    private String img_2;
    private String img_3;
    private String multicode_1;
    private String multicode_2;
    private String multicode_3;

    private String roomId;

    @Override
    public boolean validate(BindingResult bindingResult) {
        if (channels != null)
            for (int i = 0; i < channels.size(); i++)
                channels.get(i).validate("channels[" + i + "].", bindingResult);

        return super.validate(bindingResult);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class ChannelForm extends BaseForm {
        @NotNull
        private MultichannelChannelType type;
        @NotNull
        private String value;

        public boolean validate(String prefix, BindingResult bindingResult) {
            if (type == null)
                reject(bindingResult, prefix + "type", "messages.validator.blank", "type");
            if (StringUtils.isEmpty(value))
                reject(bindingResult, prefix + "value", "messages.validator.blank", "value");

            return super.validate(bindingResult);
        }
    }
}
