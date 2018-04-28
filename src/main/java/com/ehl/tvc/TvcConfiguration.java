package com.ehl.tvc;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.ehl.tvc.common.WorkConfig;
import com.ehl.tvc.version.HandleResult;
import com.ehl.tvc.version.HandleResult4Tvc;
import com.ehl.tvc.version.HandleResultV1;
import com.ehl.tvc.version.HandleResultV2;

import freemarker.template.Configuration;

@org.springframework.context.annotation.Configuration
@ImportResource("classpath:config/spring.xml")
public class TvcConfiguration {

	@Autowired
	private WorkConfig workConfig;

	public static void main(String[] args) {
		SpringApplication.run(TvcConfiguration.class, args);
	}

	@Bean
	public JdbcTemplate primaryJdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	ThreadPoolTaskExecutor threadPoolTaskExecutor() {
		ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
		threadPool.setMaxPoolSize(workConfig.getCoresize());
		return threadPool;
	}

	@Bean
	Configuration configuration() {
		Configuration configuration = new Configuration(Configuration.VERSION_2_3_25);
		configuration.setDefaultEncoding("UTF-8");
		return configuration;
	}

	@Bean
	@Lazy
	@Scope("prototype")
	HandleResult handleResult() {
		String version = workConfig.getVersion();
		if ("v2.0".equals(version)) {
			return new HandleResultV2();
		}else if("tvc".equals(version)){
			return new HandleResult4Tvc();
		}
		return new HandleResultV1();
	}
}
