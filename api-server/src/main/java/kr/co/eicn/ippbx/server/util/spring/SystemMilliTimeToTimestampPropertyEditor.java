package kr.co.eicn.ippbx.server.util.spring;

import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;

public class SystemMilliTimeToTimestampPropertyEditor extends PropertyEditorSupport {
    @Override
    public String getAsText() {
        final Timestamp value = (Timestamp) getValue();
        return value != null ? value.getTime() + "" : null;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtils.isEmpty(text))
            setValue(null);
        else
            setValue(new Timestamp(Long.parseLong(text)));
    }
}