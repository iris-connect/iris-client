/*
 * Copyright (C) 2012-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package iris.client_bff.events;

import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.pf4j.PluginWrapper;
import org.pf4j.spring.SpringPlugin;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 * A very simple plugin.
 *
 * @author Decebal Suiu
 */
public class EventsPlugin extends SpringPlugin {

	public EventsPlugin(PluginWrapper wrapper) {
		super(wrapper);
	}

	@Override
	public void start() {
		System.out.println("HelloPlugin.start()");
		getApplicationContext();
	}

	@Override
	public void stop() {
		System.out.println("HelloPlugin.stop()");
		super.stop(); // to close applicationContext
	}

	@Override
	protected ApplicationContext createApplicationContext() {

		var applicationContext = new AnnotationConfigApplicationContext();
		// applicationContext.setParent(SpringPluginManager.applicationContext);
		applicationContext.setClassLoader(getWrapper().getPluginClassLoader());
		applicationContext.scan(this.getClass().getPackageName());

		var pph = new PropertySourcesPlaceholderConfigurer();
		pph.setLocation(applicationContext.getResource("/plugin-spring.properties"));
		applicationContext.addBeanFactoryPostProcessor(pph);

		applicationContext.refresh();

		return applicationContext;
	}

	@Configuration
	@ComponentScan
	@EnableJpaRepositories
	// @PropertySource // ("classpath:plugin-spring.properties")
	static class EventsConfig {

		@Bean
		public DataSource dataSource() {
			return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
		}

		@Bean
		public EntityManagerFactory entityManagerFactory(ApplicationContext context, ResourceLoader loader) {

			LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
			factory.setBeanClassLoader(context.getClassLoader());
			factory.setResourceLoader(loader);
			factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
			factory.setPackagesToScan(this.getClass().getPackageName());
			factory.setDataSource(dataSource());
			factory.setJpaPropertyMap(Map.of(
					"hibernate.search.backend.analysis.configurer", "DocumentAnalysisConfigurer",
					"hibernate.search.mapping.configurer", "SearchMappingConfigurer"));
			factory.afterPropertiesSet();

			return factory.getObject();
		}

		// @Bean
		// public PlatformTransactionManager transactionManager() {
		// JpaTransactionManager txManager = new JpaTransactionManager();
		// txManager.setEntityManagerFactory(entityManagerFactory());
		// return txManager;
		// }

		// public TransactionManager transactionManager() {
		// DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
		// transactionManager.setDataSource(dataSource());
		//
		// return transactionManager;
		// }
	}
}
