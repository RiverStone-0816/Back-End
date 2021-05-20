package kr.co.eicn.ippbx.server.config.security;

import kr.co.eicn.ippbx.util.ResponseUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class ExceptionHandlerFilter extends OncePerRequestFilter {

	private final ResponseUtils responseUtils;

	public ExceptionHandlerFilter(ResponseUtils responseUtils) {
		this.responseUtils = responseUtils;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (AuthenticationException e) {
			responseUtils.setErrorResponse(HttpStatus.UNAUTHORIZED, response, e);
		} catch (RuntimeException e) {
			e.printStackTrace();
			responseUtils.setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, response, e);
		}
	}
}
