package kr.co.eicn.ippbx.util.spring.stroage;

import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
public class BasicSessionStorage implements SessionStorage {
    private final HttpSession session;

    @Override
    public String set(String sessionId, String key, Object value) {
        session.setAttribute(key, value);
        return sessionId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String sessionId, String key, Class<T> returnType) {
        return (T) session.getAttribute(key);
    }

    @Override
    public void remove(String sessionId, String key) {
        session.removeAttribute(key);
    }

    @Override
    public void expire(String sessionId) {
        session.invalidate();
    }
}
