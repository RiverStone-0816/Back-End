package kr.co.eicn.ippbx.server.config;

import kr.co.eicn.ippbx.server.util.jooq.ExceptionTranslator;
import org.jooq.ExecuteListener;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DelegatingDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;

public abstract class AbstractJooqPersistenceConfig {
	@Value("${jdbc.dialect}")
	private String dialect;

	abstract DataSource dataSource();

	protected DefaultDSLContext dsl() {
		return new DefaultDSLContext(configuration());
	}

	private DataSourceConnectionProvider connectionProvider() {
		return new DataSourceConnectionProvider(transactionAwareDataSource());
	}

	private ExecuteListener exceptionTranslator() {
		return new ExceptionTranslator();
	}

	private org.jooq.Configuration configuration() {
		DefaultConfiguration configuration = new DefaultConfiguration();
		configuration.setSQLDialect(SQLDialect.valueOf(dialect.toUpperCase()));
		configuration.setConnectionProvider(connectionProvider());
		configuration.setExecuteListenerProvider(new DefaultExecuteListenerProvider(exceptionTranslator()));
		configuration.set(new Settings().withRenderFormatted(true));
		return configuration;
	}

	protected DelegatingDataSource transactionAwareDataSource() {
		return new TransactionAwareDataSourceProxy(dataSource());
	}
}
