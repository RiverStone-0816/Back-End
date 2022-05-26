package kr.co.eicn.ippbx.front.config;

import kr.co.eicn.ippbx.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class JsonResultMessageConverter extends MappingJackson2HttpMessageConverter {
    private static final List<String> excludingRequests = Arrays.asList(
            "/swagger-resources/configuration/ui",
            "/swagger-resources/configuration/security",
            "/swagger-resources",
            "/v2/api-docs",
            "/api/user/session-check",
            "/ubiz/api/user/session-check"

    );

    private final HttpServletRequest request;

    @SneakyThrows
    @Override
    protected void writeInternal(@NotNull Object object, Type type, @NotNull HttpOutputMessage outputMessage) {
        if (excludingRequests.stream().anyMatch(e -> request.getRequestURI().startsWith(e))) {
            super.writeInternal(object, type, outputMessage);
        } else {
            super.writeInternal(JsonResult.data(object), type, outputMessage);
        }
    }
}
