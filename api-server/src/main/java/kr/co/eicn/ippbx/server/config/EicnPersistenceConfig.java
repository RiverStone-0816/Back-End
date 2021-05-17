package kr.co.eicn.ippbx.server.config;

import lombok.EqualsAndHashCode;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@EqualsAndHashCode(callSuper = true)
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
public class EicnPersistenceConfig extends AbstractJooqPersistenceConfig {

	@Override
	@Primary
	@Bean(Constants.BEAN_DATASOURCE_EICN)
	@Qualifier(Constants.BEAN_DATASOURCE_EICN)
	@ConfigurationProperties(prefix = "spring.datasource.hikari.eicn")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(Constants.BEAN_DSL_EICN)
	public DefaultDSLContext dsl() {
		return super.dsl();
	}

	@Primary
	@Bean(Constants.BEAN_TRANSACTION_MANAGER_EICN)
	@Qualifier(Constants.BEAN_TRANSACTION_MANAGER_EICN)
	public DataSourceTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}
}
