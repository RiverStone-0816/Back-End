package kr.co.eicn.ippbx.front;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@ServletComponentScan
@EnableAspectJAutoProxy
@EnableScheduling
@SpringBootApplication
public class FrontApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(FrontApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(FrontApplication.class);
    }
}
