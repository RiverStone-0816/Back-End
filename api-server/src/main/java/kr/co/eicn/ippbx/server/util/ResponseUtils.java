package kr.co.eicn.ippbx.server.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static kr.co.eicn.ippbx.server.util.JsonResult.create;

@Log4j2
@Component
public class ResponseUtils {
	private final ObjectMapper objectMapper;

	public ResponseUtils(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public void setErrorResponse(HttpStatus status, HttpServletResponse response, Throwable ex){
		response.setStatus(status.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		try {
			log.warn(ex.getMessage());
			response.getWriter().println(objectMapper.writeValueAsString(create(ex.getMessage())));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setErrorResponse(HttpStatus status, HttpServletResponse response, String message){
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
