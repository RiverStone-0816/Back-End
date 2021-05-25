package kr.co.eicn.ippbx.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static kr.co.eicn.ippbx.util.JsonResult.create;

@Slf4j
public class ResponseUtils {
    private final ObjectMapper objectMapper;

    public ResponseUtils(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void setErrorResponse(HttpStatus status, HttpServletResponse response, Throwable ex) {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try {
            log.warn(ex.getMessage());
            response.getWriter().println(objectMapper.writeValueAsString(create(ex.getMessage())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setErrorResponse(HttpStatus status, HttpServletResponse response, String message) {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try {
            log.warn(message);
            response.getWriter().println(objectMapper.writeValueAsString(create(message)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
