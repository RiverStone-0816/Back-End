package kr.co.eicn.ippbx.server.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({Constants.REPOSITORY_PACKAGE})
public class MybatisPersistenceConfig {
}
