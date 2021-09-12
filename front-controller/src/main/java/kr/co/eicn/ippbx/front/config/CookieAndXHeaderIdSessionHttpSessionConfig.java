package kr.co.eicn.ippbx.front.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;

import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@EnableSpringHttpSession
@Configuration(proxyBeanMethods = false)
public class CookieAndXHeaderIdSessionHttpSessionConfig {

    @Bean
    public CookieAndXHeaderIdSessionRepository cookieAndXHeaderIdSessionRepository() {
        return new CookieAndXHeaderIdSessionRepository(new ConcurrentHashMap<>());
    }

}
