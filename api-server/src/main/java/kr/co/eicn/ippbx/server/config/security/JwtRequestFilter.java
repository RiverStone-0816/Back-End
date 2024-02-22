package kr.co.eicn.ippbx.server.config.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import kr.co.eicn.ippbx.model.UserDetails;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;
	private final PersonUserDetailsService personUserDetailsService;
	private final AntPathMatcher antPathMatcher;

	public JwtRequestFilter(JwtTokenProvider jwtTokenProvider, PersonUserDetailsService personUserDetailsService) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.personUserDetailsService = personUserDetailsService;
		antPathMatcher = new AntPathMatcher();
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException, AuthenticationException {

		String requestTokenHeader = request.getHeader("Authorization");

		if (HttpMethod.GET.matches(request.getMethod()) && antPathMatcher.match("/**/api/**/*resource", request.getRequestURI())) {
			final String token = request.getParameter("token");
			if (token != null)
				requestTokenHeader = "Bearer ".concat(token);
		}

		String username = null;
		String jwtToken = null;
		String companyId = null;
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);

			try {
				username = jwtTokenProvider.getUsernameFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				logger.warn("Unable to get JWT Token");
				throw new PreAuthenticatedCredentialsNotFoundException("토큰값을 입력 해 주세요.", e);
			} catch (ExpiredJwtException e) {
				logger.warn("JWT Token has expired");
				throw new CredentialsExpiredException("기간만료된 토큰입니다.", e);
			} catch (JwtException e) {
				throw new BadCredentialsException("잘못된 토큰 값입니다.", e);
			}
		}

		//Once we get the token validate it.
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails;

			companyId = jwtTokenProvider.getTokenValue("companyId", jwtToken);
			userDetails = personUserDetailsService.loadUserByUsername(companyId, username);

			// if token is valid configure Spring Security to manually set authentication
			if (jwtTokenProvider.validateToken(jwtToken, userDetails)) {
				final CompanyIdUsernamePasswordAuthenticationToken authenticationToken = new CompanyIdUsernamePasswordAuthenticationToken(userDetails);
				// After setting the Authentication in the context, we specify
				// that the current user is authenticated. So it passes the Spring Security Configurations successfully.
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
		}

		MDC.put("userInfo", defaultIfEmpty(companyId, "") + "|" + defaultIfEmpty(username, ""));
		chain.doFilter(request, response);
	}
}
