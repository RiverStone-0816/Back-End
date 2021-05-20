package kr.co.eicn.ippbx.server.config.security;

import kr.co.eicn.ippbx.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
	private static final long serialVersionUID = -7858869558953243875L;

	private final ResponseUtils responseUtils;

	public JwtAuthenticationEntryPoint(ResponseUtils responseUtils) {
		this.responseUtils = responseUtils;
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
	                     AuthenticationException authException) {

		log.warn("authentication fail -> {}", request.getRequestURI());
		responseUtils.setErrorResponse(HttpStatus.UNAUTHORIZED, response, "접근 권한이 없습니다.");
	}
}
