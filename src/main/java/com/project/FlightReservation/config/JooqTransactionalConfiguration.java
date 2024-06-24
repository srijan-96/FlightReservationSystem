package com.project.FlightReservation.config;

import javax.sql.DataSource;

import org.jooq.ExecuteListenerProvider;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jooq.JooqExceptionTranslator;
import org.springframework.boot.autoconfigure.jooq.SpringTransactionProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

@Configuration
public class JooqTransactionalConfiguration
{

	@Autowired
	private DataSource dataSource;

	@Bean("transactionManager")
	public DataSourceTransactionManager dataSourceTransactionManager()
	{
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean
	public DataSourceConnectionProvider connectionProvider()
	{
		return new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource));
	}

	@Bean
	public DefaultDSLContext dsl()
	{
		return new DefaultDSLContext(config());
	}

	@Bean("exceptionTranslator")
	public JooqExceptionTranslator exceptionTranslator()
	{
		return new JooqExceptionTranslator();
	}

	@Bean
	public ExecuteListenerProvider executeListenerProvider()
	{
		return new DefaultExecuteListenerProvider(exceptionTranslator());
	}

	@Bean
	public DefaultConfiguration config()
	{
		DefaultConfiguration defaultConfiguration = new DefaultConfiguration();
		defaultConfiguration.set(SQLDialect.POSTGRES);
		defaultConfiguration.set(connectionProvider());
		defaultConfiguration.set(executeListenerProvider());
		defaultConfiguration.set(new SpringTransactionProvider(new DataSourceTransactionManager(dataSource)));
		return defaultConfiguration;
	}
}