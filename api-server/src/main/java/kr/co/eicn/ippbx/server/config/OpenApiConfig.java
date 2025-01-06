package kr.co.eicn.ippbx.server.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                                    .addSecuritySchemes("bearer-key",
                                                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }

    @Bean
    public GroupedOpenApi loginApi() {
        return GroupedOpenApi.builder()
                .group("1. 로그인 API")
                .packagesToScan("kr.co.eicn.ippbx.server.controller.api")
                .pathsToMatch("/auth/**")
                .build();
    }

    @Bean
    public GroupedOpenApi resultApi() {
        return GroupedOpenApi.builder()
                .group("2. 상담 API")
                .packagesToScan("kr.co.eicn.ippbx.server.controller.api.provision.result")
                .build();
    }

    @Bean
    public GroupedOpenApi monitorApi() {
        return GroupedOpenApi.builder()
                .group("3. 모니터링 API")
                .packagesToScan("kr.co.eicn.ippbx.server.controller.api.provision.monitor")
                .build();
    }

    @Bean
    public GroupedOpenApi sttApi() {
        return GroupedOpenApi.builder()
                .group("4. STT API")
                .packagesToScan("kr.co.eicn.ippbx.server.controller.api.provision.stt")
                .build();
    }
}
