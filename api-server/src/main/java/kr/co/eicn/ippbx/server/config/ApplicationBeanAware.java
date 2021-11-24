package kr.co.eicn.ippbx.server.config;

import kr.co.eicn.ippbx.util.spring.SpringApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationBeanAware extends SpringApplicationContextAware {
    public static RequestGlobal requestGlobal() {
        return (RequestGlobal) applicationContext.getBean("requestGlobal");
    }
}
