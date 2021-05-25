package kr.co.eicn.ippbx.front.config;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Date;

@Slf4j
@WebListener
public class HttpSessionCheckingListener implements HttpSessionListener, ServletContextListener {
    static private int activeSessions = 0;

    public static int getActiveSessions() {
        return activeSessions;
    }

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        activeSessions++;
        log.debug("SessionCnt:" + activeSessions + " Session ID ".concat(event.getSession().getId()).concat(" created at ").concat(new Date().toString()));
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        activeSessions--;
        log.debug("SessionCnt:" + activeSessions + " Session ID ".concat(event.getSession().getId()).concat(" destroyed at ").concat(new Date().toString()));
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.debug("톰캣 실행!");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.debug("톰캣 종료!");
    }
}
