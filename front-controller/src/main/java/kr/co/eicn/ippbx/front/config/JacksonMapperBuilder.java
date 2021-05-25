package kr.co.eicn.ippbx.front.config;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonMapperBuilder implements Jackson2ObjectMapperBuilderCustomizer {
    @Override
    public void customize(Jackson2ObjectMapperBuilder builder) {
        final LocalDateTimeSerializer localSerializer = new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        final CustomOffsetDateTimeSerializer offsetSerializer = new CustomOffsetDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"));

        builder.simpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .serializerByType(LocalDateTime.class, localSerializer)
                .serializerByType(OffsetDateTime.class, offsetSerializer);
    }

    public class CustomOffsetDateTimeSerializer extends OffsetDateTimeSerializer {
        public CustomOffsetDateTimeSerializer(DateTimeFormatter formatter) {
            super(OffsetDateTimeSerializer.INSTANCE, false, formatter);
        }
    }
}
