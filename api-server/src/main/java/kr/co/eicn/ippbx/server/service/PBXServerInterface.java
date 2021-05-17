package kr.co.eicn.ippbx.server.service;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PBXServerInterface {
	@Value("${jdbc.dialect}")
	private String dialect;
	@Value("${jdbc.port}")
	private String port;
	@Value("${jdbc.username}")
	private String userName;
	@Value("${jdbc.password}")
	private String password;
	@Value("${jdbc.options}")
	private String jdbcOptions;
	private final String SERVICE = "eicn";

	private String getJdbcUrl(final String host, final String service) {
		return "jdbc:" + dialect + "://" + host + ":" + port + "/" + service + "?" +
				jdbcOptions;
	}

	public DSLContext using(final String host) {
		return DSL.using(getJdbcUrl(host, SERVICE), userName, password);
	}

	// 서비스 지정
	public DSLContext using(final String host, final String service) {
		return DSL.using(getJdbcUrl(host, service), userName, password);
	}
}
