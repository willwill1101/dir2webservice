package com.ehl.tvc.schedule.work;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.ehl.tvc.common.WorkConfig;
import com.ehl.tvc.schedule.task.BasicTask;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BasicWork implements Work, ApplicationContextAware {
	public static Log log = LogFactory.getLog(BasicWork.class);

	public volatile static Boolean started = true;
	private Map<String, String> params;
	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
	@Autowired
	private SqlSessionFactory sessionFactory;

	@Autowired
	private WorkConfig workConfig;

	@Override
	public void run() {
		String sql = params.get(WorkParam.SQL);
		while (started) {
			SqlSession session = sessionFactory.openSession();
			List<Map<String, String>> datas = null;
			try {
				datas = session.selectList(sql + ".select", workConfig.getBatchsize());
			} catch (Exception e) {
				log.error("数据获取异常!!!", e);
				continue;
			}finally{
				session.close();
			}
			long start = System.currentTimeMillis();
			log.debug("批次获取数据量:" + datas.size());
			if (datas.size() == 0) {
				try {
					Thread.sleep(workConfig.getSleeptime());
					log.debug("sleep:" + workConfig.getSleeptime());
				} catch (InterruptedException e) {
					log.error("线程中断", e);
				}
				continue;
			}
			CountDownLatch countDownLatch = new CountDownLatch(datas.size());
			for (int i = 0; i < datas.size(); i++) {
				BasicTask basicTask = applicationContext.getBean("basicTask", BasicTask.class);
				basicTask.setData(datas.get(i));
				basicTask.setCountDownLatch(countDownLatch);
				basicTask.setParams(params);
				threadPoolTaskExecutor.execute(basicTask);
			}
			try {
				countDownLatch.await(workConfig.getTimeout(), TimeUnit.SECONDS);
				long end = System.currentTimeMillis();
				log.info("披处理结束,上传记录数:" + datas.size() + ",耗时:" + (end - start));
			} catch (InterruptedException e) {
				log.error("作业超时", e);
			}
		}

	}

	@PostConstruct
	public void init() {
		log.info(this);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	private ApplicationContext applicationContext;

	@Override
	public String toString() {
		return "Work [params=" + params + ", threadPoolTaskExecutor=" + threadPoolTaskExecutor + ", sessionFactory="
				+ sessionFactory + ", workConfig=" + workConfig + ", applicationContext=" + applicationContext + "]";
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public static class WorkParam {
		public static final String ENABLED = "enabled";
		public static final String NAME = "name";
		public static final String TEMPLATE = "template";
		public static final String SQL = "sql";
		public static final String URL = "url";

	}

}
