//package kr.co.eicn.ippbx.front.service;
//
//import kr.co.eicn.ippbx.util.spring.RequestMessage;
//import kr.co.eicn.ippbx.front.model.form.LoginForm;
//import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
//import kr.co.eicn.ippbx.front.service.api.AuthApiInterface;
//import kr.co.eicn.ippbx.util.spring.SpringApplicationContextAware;
//import org.junit.Before;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.support.DefaultListableBeanFactory;
//import org.springframework.beans.factory.support.GenericBeanDefinition;
//import org.springframework.mock.web.MockHttpSession;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import java.io.IOException;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {RequestMessage.class, SpringApplicationContextAware.class})
//public abstract class AbstractApiInterfaceTest {
//    private static final Logger logger = LoggerFactory.getLogger(AbstractApiInterfaceTest.class);
//
//    protected AuthApiInterface authApiInterface;
//
//    @Before
//    public void init() throws IOException, ResultFailException {
//        final DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) SpringApplicationContextAware.getApplicationContext().getAutowireCapableBeanFactory();
//
//        final GenericBeanDefinition sessionBeanDefinition = new GenericBeanDefinition();
//        sessionBeanDefinition.setBeanClass(MockHttpSession.class);
//        sessionBeanDefinition.setAutowireCandidate(true);
//        sessionBeanDefinition.setScope("singleton");
//
//        beanFactory.registerBeanDefinition("session", sessionBeanDefinition);
//
//        authApiInterface = SpringApplicationContextAware.getApplicationContext().getAutowireCapableBeanFactory().createBean(AuthApiInterface.class);
//
//        final LoginForm loginForm = new LoginForm();
//        loginForm.setCompany("primium");
//        loginForm.setId("master");
//        loginForm.setPassword("orange90");
//        authApiInterface.login(loginForm);
//    }
//
//    protected <T extends ApiServerInterface> T getInterface(Class<T> klass) {
//        return SpringApplicationContextAware.getApplicationContext().getAutowireCapableBeanFactory().createBean(klass);
//    }
//}
