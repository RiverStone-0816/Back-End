package kr.co.eicn.ippbx.front.config;

import org.springframework.session.MapSession;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.Session;

import java.util.Map;

public class CookieAndXHeaderIdSessionRepository extends MapSessionRepository {

    public CookieAndXHeaderIdSessionRepository(Map<String, Session> sessions) {
        super(sessions);
    }

    public MapSession createSession(String sessionId) {
        return new MapSession(sessionId);
    }
}
