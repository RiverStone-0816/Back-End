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
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    protected final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

    private final ResponseUtils                responseUtils;
    private final JwtAuthenticationEntryPoint  jwtAuthenticationEntryPoint;
    private final JwtRequestFilter             jwtRequestFilter;
    private final CustomAuthenticationProvider authenticationProvider;

    public WebSecurityConfig(ResponseUtils responseUtils, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtRequestFilter jwtRequestFilter, CustomAuthenticationProvider authenticationProvider) {
        this.responseUtils = responseUtils;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtRequestFilter = jwtRequestFilter;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    @Order(2)
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests().mvcMatchers("api/v1/chat/config/image").permitAll();

        httpSecurity.httpBasic().disable()
                .csrf().disable()
                .authorizeRequests().antMatchers(HttpMethod.POST, "/auth/**").permitAll()
                .antMatchers("/error").permitAll()
                .antMatchers("/api/daemon").permitAll()
                .antMatchers("/api/main-board-notice/before").permitAll()
                .antMatchers("/api/main-board-notice/before/**").permitAll()
                .antMatchers("/api/v1/admin/record/history/**").permitAll()
                .antMatchers("/api/v*/master/**").hasAnyRole(IdType.MASTER.name())
                .anyRequest().authenticated().and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.addFilterBefore(exceptionHandlerFilter(), JwtRequestFilter.class);
        httpSecurity.authenticationProvider(authenticationProvider);

        return httpSecurity.build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain exceptionFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .requestMatchers(matchers -> matchers.antMatchers("/docs/index.html", "/favicon.ico"))
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
                .requestCache().disable()
                .securityContext().disable()
                .sessionManagement().disable();

        return httpSecurity.build();
    }

    @Bean
    public ExceptionHandlerFilter exceptionHandlerFilter() {
        return new ExceptionHandlerFilter(responseUtils);
    }
}
