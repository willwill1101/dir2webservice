package com.ehl.tvc.schedule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.ehl.tvc.common.WorkConfig;
import com.ehl.tvc.schedule.work.BasicWork;
import com.ehl.tvc.schedule.work.BasicWork.WorkParam;
import com.ehl.tvc.schedule.work.Work;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;

@Service
public class ScheduleImpl implements Schedule, ApplicationContextAware {
	public static Log log = LogFactory.getLog(ScheduleImpl.class);
	@Autowired
	private WorkConfig workConfig;
	@Autowired
	private Configuration configuration;

	private ExecutorService executorService;

	@Override
	public void run() {

		log.info(workConfig);
		//
		if (executorService == null) {
			executorService = Executors.newFixedThreadPool(workConfig.getWorks().size());
		}
		//
		List<Map<String, String>> works = checkWork(workConfig.getWorks());

		//
		StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
		for (Map<String, String> work : works) {

			if (work.get(WorkParam.TEMPLATE) == null || "".equals(work.get(WorkParam.TEMPLATE))) {
				log.warn("没有为作业:"+work.get(WorkParam.NAME)+"配置数据发送模版");
				continue;
			}
			try {
				String teleplate = IOUtils.toString(this.getClass()
						.getResourceAsStream("/config/template/" + work.get(WorkParam.TEMPLATE) + ".xml"));
				log.debug(teleplate);
				stringTemplateLoader.putTemplate(work.get(WorkParam.NAME), teleplate);
			} catch (IOException e) {
				log.error("模版文件加载异常,work:" + work, e);
				System.exit(-1);
			}
		}
		configuration.setTemplateLoader(stringTemplateLoader);
		//
		for (Map<String, String> param : works) {
			Work work = applicationContext.getBean(param.get("type"), Work.class);
			work.setParams(param);
			executorService.execute(work);
			log.info("启动作业:" + work);
		}
	}

	public void startup() {
		new Thread(this).start();
	}

	// 参数检查
	public List<Map<String, String>> checkWork(List<Map<String, String>> works) {

		List<Map<String, String>> worksNew = new ArrayList<>();
		for (Map<String, String> work : works) {
			if ("true".equals(work.get(WorkParam.ENABLED))) {
				if (StringUtils.isBlank(work.get(WorkParam.NAME))) {
					throw new RuntimeException("参数" + WorkParam.NAME + "不能为空,work:" + work);
				}
				if (StringUtils.isBlank(work.get(WorkParam.SQL))) {
					throw new RuntimeException("参数" + WorkParam.SQL + "不能为空,work:" + work);
				}
				if (StringUtils.isBlank(work.get(WorkParam.TEMPLATE))) {
					throw new RuntimeException("参数" + WorkParam.TEMPLATE + "不能为空,work:" + work);
				}
				if (StringUtils.isBlank(work.get(WorkParam.URL))) {
					throw new RuntimeException("参数" + WorkParam.URL + "不能为空,work:" + work);
				}
				worksNew.add(work);
			}
		}
		return worksNew;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

	private ApplicationContext applicationContext;

}
