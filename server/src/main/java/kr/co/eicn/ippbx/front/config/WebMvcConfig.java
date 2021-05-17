package kr.co.eicn.ippbx.front.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.StdConverter;
import kr.co.eicn.ippbx.front.interceptor.LoginRequireInterceptor;
import kr.co.eicn.ippbx.front.service.FileService;
import kr.co.eicn.ippbx.front.util.JsonResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author tinywind
 */
@Configuration
@EnableWebMvc
@PropertySource(value = {"classpath:application.properties", "classpath:maven.properties"})
// @Import(SwaggerConfig.class)
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private LoginRequireInterceptor loginRequireInterceptor;
    @Autowired
    private FileService fileService;

    @Value("${application.devel:true}")
    private Boolean devel;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");//.setCachePeriod(31556926);

        if (devel) {
            registry.addResourceHandler("/doc/**").addResourceLocations("/doc/");
            registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
            registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        }
        super.addResourceHandlers(registry);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginRequireInterceptor);
        super.addInterceptors(registry);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(mappingJackson2HttpMessageConverter());
        super.configureMessageConverters(converters);
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        final MappingJackson2HttpMessageConverter converter = new JsonResultMessageConverter();
        converter.setObjectMapper(objectMapper);
        return converter;
    }

    @Bean
    public ViewResolver viewResolver() {
        final InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/jsp/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        final CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setDefaultEncoding("UTF-8");
        return multipartResolver;
    }

    @Bean
    public View jsonView() {
        return new MappingJackson2JsonView();
    }

    @Bean
    public Validator validator(MessageSource messageSource) {
        final LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(messageSource);
        return validator;
    }

    /*@Bean
    public ConversionServiceFactoryBean conversionServiceFactory() {
        final ConversionServiceFactoryBean factory = new ConversionServiceFactoryBean();
        final Set<Converter<?, ?>> converters = new HashSet<>();
        factory.setConverters(converters);
        converters.add(new TimestampConverter("yyyy-MM-dd HH:mm:ss.SSSSSS"));
        converters.add(new TimestampConverter("yyyy-MM-dd HH:mm:ss.SSSSS"));
        converters.add(new TimestampConverter("yyyy-MM-dd HH:mm:ss.SSSS"));
        converters.add(new TimestampConverter("yyyy-MM-dd HH:mm:ss.SSS"));
        converters.add(new TimestampConverter("yyyy-MM-dd HH:mm:ss.SS"));
        converters.add(new TimestampConverter("yyyy-MM-dd HH:mm:ss.S"));

        return factory;
    }*/

    public static class JsonResultMessageConverter extends MappingJackson2HttpMessageConverter {
        @Override
        protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
            super.writeInternal(JsonResult.data(object), type, outputMessage);
        }
    }

    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class TimestampConverter extends StdConverter<String, Timestamp> implements Converter<String, Timestamp> {
        private final String pattern;

        @SneakyThrows
        @Override
        public Timestamp convert(String value) {
            if (StringUtils.isEmpty(value))
                return null;

            final SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            dateFormat.setLenient(false);

            return new Timestamp(dateFormat.parse(value).getTime());
        }
    }
}
