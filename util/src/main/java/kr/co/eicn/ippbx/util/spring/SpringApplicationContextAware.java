package kr.co.eicn.ippbx.util.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

public class SpringApplicationContextAware implements ApplicationContextAware {
    protected static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringApplicationContextAware.applicationContext = applicationContext;
    }

    public static RequestMappingHandlerMapping requestMappingHandlerMapping() {
        return (RequestMappingHandlerMapping) applicationContext.getBean("requestMappingHandlerMapping");
    }

    public static MessageSource messageSource() {
        return (MessageSource) applicationContext.getBean("messageSource");
    }

    public static RequestMessage requestMessage() {
        return (RequestMessage) applicationContext.getBean("requestMessage");
    }

    public static Validator validator() {
        return (Validator) applicationContext.getBean("validator");
    }

    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }
}
