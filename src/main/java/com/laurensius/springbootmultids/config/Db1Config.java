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
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
		entityManagerFactoryRef = "db1EntityManager",
		transactionManagerRef = "db1TransactionManager",
		//basePackageClasses = MqttRawPackets.class,
		basePackages = {"com.laurensius.springbootmultids.repository.db1"})
@EnableTransactionManagement
public class Db1Config {

	private final Logger log = LoggerFactory.getLogger(Db1Config.class);

	private final PersistenceUnitManager persistenceUnitManager;

	public Db1Config(ObjectProvider<PersistenceUnitManager> persistenceUnitManager) {
		this.persistenceUnitManager = persistenceUnitManager.getIfAvailable();
	}

	@Bean
	@ConfigurationProperties("db1.jpa")
	public JpaProperties db1JpaProperties() {
		return new JpaProperties();
	}

    @Bean
    @Primary
    @ConfigurationProperties("db1.datasource")
    public DataSource db1DataSource() {
        return DataSourceBuilder.create().build();
    }

    /*
    @Bean
	@Primary
	@ConfigurationProperties("app.db1.datasource")
	public DataSourceProperties db1DataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean
	@Primary
	@ConfigurationProperties(prefix = "app.db1.datasource.properties")
	public DataSource db1DataSource() {
		return (DataSource) db1DataSourceProperties().initializeDataSourceBuilder()
				.type(DataSource.class).build();
	}
	*/

	@Bean
	public LocalContainerEntityManagerFactoryBean db1EntityManager(
			JpaProperties db1JpaProperties) {
		EntityManagerFactoryBuilder builder = createEntityManagerFactoryBuilder(
				db1JpaProperties);
		return builder
				.dataSource(db1DataSource())
				//.packages(MqttRawPackets.class)
				.packages("com.laurensius.springbootmultids.domain")
				.persistenceUnit("db1")
				.build();
	}

	@Bean
	@Primary
	public JpaTransactionManager db1TransactionManager(
			EntityManagerFactory db1EntityManager) {
		return new JpaTransactionManager(db1EntityManager);
	}

	private EntityManagerFactoryBuilder createEntityManagerFactoryBuilder(
			JpaProperties db1JpaProperties) {
		JpaVendorAdapter jpaVendorAdapter = createJpaVendorAdapter(db1JpaProperties);
		return new EntityManagerFactoryBuilder(jpaVendorAdapter,
				db1JpaProperties.getProperties(), this.persistenceUnitManager);
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
