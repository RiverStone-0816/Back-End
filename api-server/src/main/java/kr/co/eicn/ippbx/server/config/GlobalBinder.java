package kr.co.eicn.ippbx.server.config;

import kr.co.eicn.ippbx.util.spring.SystemMilliTimeToTimestampPropertyEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import static kr.co.eicn.ippbx.model.Constants.DEFAULT_DATE_PATTERN;
import static kr.co.eicn.ippbx.model.Constants.DEFAULT_TIME_PATTERN;

@ControllerAdvice
public class GlobalBinder {

    @InitBinder
    public void registerCustomEditors(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat(DEFAULT_DATE_PATTERN), true));
        binder.registerCustomEditor(Time.class, new CustomDateEditor(new SimpleDateFormat(DEFAULT_TIME_PATTERN), true));
        binder.registerCustomEditor(Timestamp.class, new SystemMilliTimeToTimestampPropertyEditor());
    }
}
