package com.ehl.tvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;

import com.ehl.tvc.schedule.Schedule;
import com.ehl.tvc.schedule.ScheduleImpl;
import com.ehl.tvc.util.HttpUtil;

import de.codecentric.boot.admin.config.EnableAdminServer;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAdminServer
public class TvcDir2WebserviceApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(TvcDir2WebserviceApplication.class, args);


		// 初始化HttpUtil
		HttpUtil httpUtil = context.getBean(HttpUtil.class);
		httpUtil.init();

		// 启动调度程序
		Schedule schedule = context.getBean(ScheduleImpl.class);
		schedule.startup();

	}
}
