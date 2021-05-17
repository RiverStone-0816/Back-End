package kr.co.eicn.ippbx.server.util.spring;

import kr.co.eicn.ippbx.front.config.RequestGlobal;
import kr.co.eicn.ippbx.front.config.RequestMessage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

public class SpringApplicationContextAware {
    
    public static ApplicationContext getApplicationContext() {
        return kr.co.eicn.ippbx.front.util.spring.SpringApplicationContextAware.getApplicationContext();
    }

    public static RequestMappingHandlerMapping requestMappingHandlerMapping() {
        return (RequestMappingHandlerMapping) getApplicationContext().getBean("requestMappingHandlerMapping");
    }

    public static RequestGlobal requestGlobal() {
        return ((RequestGlobal) getApplicationContext().getBean("requestGlobal"));
    }

    public static MessageSource messageSource() {
        return (MessageSource) getApplicationContext().getBean("messageSource");
    }

    public static RequestMessage requestMessage() {
        return (RequestMessage) getApplicationContext().getBean("requestMessage");
    }

    public static Validator validator() {
        return (Validator) getApplicationContext().getBean("validator");
    }

    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }
}
