package kr.co.eicn.ippbx.util.spring;

import kr.co.eicn.ippbx.util.CodeHasable;
import org.springframework.context.MessageSource;

import java.util.Locale;

/**
 * @author tinywind
 */
public class RequestMessage {
    private final MessageSource source;

    public RequestMessage(MessageSource source) {
        this.source = source;
    }

    public String getText(String code) {
        return getText(code, new Object[0]);
    }

    public String getText(String code, Object... objects) {
        return source.getMessage(code, objects, "Message not found. code: " + code, Locale.getDefault());
    }

    public String getEnumText(Enum value) {
        return getText(value.getClass().getName() + "." + value.name());
    }

    public String getEnumCodeText(CodeHasable<?> value) {
        return getText(value.getClass().getName() + "." + value.getCode());
    }

    public String getEnumText(Object e, String member, Object value) {
        if (e == null || value == null) return "";
        return getText(e.getClass().getName() + "." + member + "." + value.toString());
    }
}
