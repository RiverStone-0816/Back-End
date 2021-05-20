package kr.co.eicn.ippbx.server.config;

import kr.co.eicn.ippbx.server.config.security.CustomAuthenticationProvider;
import kr.co.eicn.ippbx.server.config.security.ExceptionHandlerFilter;
import kr.co.eicn.ippbx.server.config.security.JwtAuthenticationEntryPoint;
import kr.co.eicn.ippbx.server.config.security.JwtRequestFilter;
import kr.co.eicn.ippbx.model.enums.IdType;
import kr.co.eicn.ippbx.util.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	protected final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

	private final ResponseUtils responseUtils;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtRequestFilter jwtRequestFilter;
	private final CustomAuthenticationProvider authenticationProvider;

	public WebSecurityConfig(ResponseUtils responseUtils, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtRequestFilter jwtRequestFilter, CustomAuthenticationProvider authenticationProvider) {
		this.responseUtils = responseUtils;
		this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
		this.jwtRequestFilter = jwtRequestFilter;
		this.authenticationProvider = authenticationProvider;
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {


		httpSecurity.httpBasic().disable()
				.csrf().disable()
				.authorizeRequests().antMatchers(HttpMethod.POST,  "/auth/**").permitAll()
				.antMatchers("/error").permitAll()
				.antMatchers("/api/daemon").permitAll()
				.antMatchers("/api/v1/admin/record/history/**").permitAll()
				.antMatchers("/api/v*/master/**").hasAnyRole(IdType.MASTER.name())
				.anyRequest().authenticated().and()
				.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
		httpSecurity.addFilterBefore(exceptionHandlerFilter(), JwtRequestFilter.class);
	}

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers( "/docs/index.html", "/favicon.ico");
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(authenticationProvider);
	}

	@Bean
	public ExceptionHandlerFilter exceptionHandlerFilter() {
		return new ExceptionHandlerFilter(responseUtils);
	}
}
