package kr.co.eicn.ippbx.front.config;

import kr.co.eicn.ippbx.util.spring.stroage.BasicSessionStorage;
import kr.co.eicn.ippbx.util.spring.stroage.SessionStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpSession;

@Configuration
public class SessionConfig {

    @Bean
    public SessionStorage sessionStorage(HttpSession session) {
        return new BasicSessionStorage(session);
    }
}
