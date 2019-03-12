package com.laurensius.springbootmultids.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
		entityManagerFactoryRef = "db2EntityManager",
		transactionManagerRef = "db2TransactionManager",
		//basePackageClasses = MqttRawPackets.class,
		basePackages = {"com.laurensius.springbootmultids.repository.db2"})
public class Db2Config {

    private final Logger log = LoggerFactory.getLogger(Db1Config.class);

    private final PersistenceUnitManager persistenceUnitManager;

	public Db2Config(ObjectProvider<PersistenceUnitManager> persistenceUnitManager) {
		this.persistenceUnitManager = persistenceUnitManager.getIfAvailable();
	}

	@Bean
	@ConfigurationProperties("db2.jpa")
	public JpaProperties db2JpaProperties() {
		return new JpaProperties();
	}

	@Bean
	@ConfigurationProperties("db2.datasource")
	public DataSource db2DataSource() {
		return DataSourceBuilder.create().build();
	}

	/*
	@Bean
	@ConfigurationProperties("app.db2.datasource")
	public DataSourceProperties db2DataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean
	@ConfigurationProperties(prefix = "app.db2.datasource.properties")
	public DataSource db2DataSource() {
		return (DataSource) db2DataSourceProperties().initializeDataSourceBuilder()
				.type(DataSource.class).build();
	}
	*/

	@Bean
	public LocalContainerEntityManagerFactoryBean db2EntityManager(
			JpaProperties db2JpaProperties) {
		EntityManagerFactoryBuilder builder = createEntityManagerFactoryBuilder(
				db2JpaProperties);
		return builder
				.dataSource(db2DataSource())
				//.packages(MqttRawPackets.class)
				.packages("com.laurensius.springbootmultids.domain")
				.persistenceUnit("db2")
				.build();
	}

	@Bean
	public JpaTransactionManager db2TransactionManager(
			EntityManagerFactory db2EntityManager) {
		return new JpaTransactionManager(db2EntityManager);
	}

	private EntityManagerFactoryBuilder createEntityManagerFactoryBuilder(
			JpaProperties db2JpaProperties) {
		JpaVendorAdapter jpaVendorAdapter = createJpaVendorAdapter(db2JpaProperties);
		return new EntityManagerFactoryBuilder(jpaVendorAdapter,
				db2JpaProperties.getProperties(), this.persistenceUnitManager);
	}

	private JpaVendorAdapter createJpaVendorAdapter(JpaProperties jpaProperties) {
		AbstractJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setShowSql(jpaProperties.isShowSql());
		adapter.setDatabase(jpaProperties.getDatabase());
		adapter.setDatabasePlatform(jpaProperties.getDatabasePlatform());
		adapter.setGenerateDdl(jpaProperties.isGenerateDdl());
		return adapter;
	}

}
