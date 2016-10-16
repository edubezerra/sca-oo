package br.cefetrj.sca.infra.cargadados;

import java.io.InputStream;
import java.util.Hashtable;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = { "br.cefetrj.sca.dominio",
		"br.cefetrj.sca.infra.cargadados",
		"br.cefetrj.sca.dominio.repositories"}, includeFilters = { @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = br.cefetrj.sca.dominio.AlunoFabrica.class) })
@EnableJpaRepositories(basePackages = "br.cefetrj.sca.dominio.repositories")
public class StandalonePersistenceConfig {

	@Value("${init-db:false}")
	private String initDatabase;

	@Bean
	public PlatformTransactionManager transactionManager() {
		EntityManagerFactory factory = entityManagerFactory();
		return new JpaTransactionManager(factory);
	}

	@Bean(name = "entityManagerFactory")
	public EntityManagerFactory entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(Boolean.TRUE);
		vendorAdapter.setShowSql(Boolean.FALSE);

		factory.setDataSource(dataSource());
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan("br.cefetrj.sca.dominio");

		Properties properties = new Properties();
		Hashtable<String, String> mymap = new Hashtable<String, String>();
		try {
			InputStream propertiesInputStream = Thread.currentThread()
					.getContextClassLoader()
					.getResourceAsStream("application.properties");
			properties.load(propertiesInputStream);

			for (String key : properties.stringPropertyNames()) {
				String value = properties.getProperty(key);
				mymap.put(key, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		Properties jpaProperties = new Properties();
		jpaProperties.put("hibernate.dialect",
				properties.getProperty("hibernate.dialect"));
		jpaProperties.put("hibernate.hbm2ddl.auto",
				properties.getProperty("hibernate.hbm2ddl.auto"));

		jpaProperties.put(
				"javax.persistence.schema-generation.database.action",
				"drop-and-create");

		factory.setJpaProperties(jpaProperties);

		factory.afterPropertiesSet();
		factory.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());
		return factory.getObject();
	}

	@Bean
	public HibernateExceptionTranslator hibernateExceptionTranslator() {
		return new HibernateExceptionTranslator();
	}

	@Bean
	public DataSource dataSource() {

		Properties properties = new Properties();
		Hashtable<String, String> mymap = new Hashtable<String, String>();
		try {
			InputStream propertiesInputStream = Thread.currentThread()
					.getContextClassLoader()
					.getResourceAsStream("application.properties");
			properties.load(propertiesInputStream);
			for (String key : properties.stringPropertyNames()) {
				String value = properties.getProperty(key);
				mymap.put(key, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(properties
				.getProperty("jdbc.driverClassName"));
		dataSource.setUrl(properties.getProperty("jdbc.url"));
		dataSource.setUsername(properties.getProperty("jdbc.username"));
		dataSource.setPassword(properties.getProperty("jdbc.password"));
		return dataSource;
	}

	@Bean
	public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
		System.out.println("**************************" + initDatabase);
		DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
		dataSourceInitializer.setDataSource(dataSource);
		ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
		databasePopulator.addScript(new ClassPathResource("db.sql"));
		dataSourceInitializer.setDatabasePopulator(databasePopulator);
		dataSourceInitializer.setEnabled(Boolean.parseBoolean(initDatabase));
		return dataSourceInitializer;
	}
}
