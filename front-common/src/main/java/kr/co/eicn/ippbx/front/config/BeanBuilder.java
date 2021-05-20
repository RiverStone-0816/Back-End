package kr.co.eicn.ippbx.front.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer;
import io.vavr.jackson.datatype.VavrModule;
import kr.co.eicn.ippbx.util.spring.RequestMessage;
import kr.co.eicn.ippbx.util.spring.TagExtender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class BeanBuilder implements Jackson2ObjectMapperBuilderCustomizer {

    @Bean
    public TaskScheduler taskScheduler() {
        final ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(Runtime.getRuntime().availableProcessors() * 2);
        return threadPoolTaskScheduler;
    }

    @Bean
    public ObjectMapper objectMapper(@Value("${user-data.application.debugging}") Boolean debugging) {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new VavrModule());
        objectMapper.registerModule(new JavaTimeModule());

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, debugging);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        return objectMapper;
    }

    @Override
    public void customize(Jackson2ObjectMapperBuilder builder) {
        final LocalDateTimeSerializer localSerializer = new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        final CustomOffsetDateTimeSerializer offsetSerializer = new CustomOffsetDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"));

        builder.simpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .serializerByType(LocalDateTime.class, localSerializer)
                .serializerByType(OffsetDateTime.class, offsetSerializer);
    }

    @Bean
    public TagExtender tagExtender() {
        return new TagExtender();
    }

    @Bean
    public RequestMessage requestMessage(MessageSource messageSource) {
        return new RequestMessage(messageSource);
    }

    public class CustomOffsetDateTimeSerializer extends OffsetDateTimeSerializer {
        public CustomOffsetDateTimeSerializer(DateTimeFormatter formatter) {
            super(OffsetDateTimeSerializer.INSTANCE, false, formatter);
        }
    }
}
