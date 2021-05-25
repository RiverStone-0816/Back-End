package kr.co.eicn.ippbx.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.eicn.ippbx.util.ResponseUtils;
import kr.co.eicn.ippbx.util.spring.SHA512PasswordEncoder;
import kr.co.eicn.ippbx.util.spring.RequestMessage;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanBuilder {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public RequestMessage requestMessage(@Qualifier("ymlMessageSource") MessageSource source) {
        return new RequestMessage(source);
    }

    @Bean
    public ResponseUtils responseUtils(ObjectMapper objectMapper) {
        return new ResponseUtils(objectMapper);
    }

    @Bean
    public SHA512PasswordEncoder sha512PasswordEncoder() {
        return new SHA512PasswordEncoder();
    }
}
