package com.ehl.tvc.schedule.task;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.ehl.tvc.common.WorkConfig;
import com.ehl.tvc.schedule.work.BasicWork.WorkParam;
import com.ehl.tvc.util.HttpUtil;
import com.ehl.tvc.version.HandleResult;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BasicTask implements Task {

	public static final Log log = LogFactory.getLog(Task.class);
	@Autowired
	private WorkConfig workConfig;
	@Autowired
	private Configuration configuration;
	@Autowired
	private HttpUtil httpUtil;
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	@Autowired
	HandleResult handleResult;

	//
	private Map<String, String> params;
	private Map<String, String> data;
	private CountDownLatch countDownLatch;

	@Override
	public void run() {
		try {

			log.info("准备违法数据:" + data);
			Template template = configuration.getTemplate(params.get(WorkParam.NAME));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			template.process(data, new OutputStreamWriter(out));
			String message = IOUtils.toString(out.toByteArray(), "UTF-8");
			out.close();
			HttpPost post = new HttpPost(params.get(WorkParam.URL));
			Map<String, String> postParam = workConfig.getHeads();
			if (postParam != null) {
				for (String key : postParam.keySet()) {
					post.setHeader(key, postParam.get(key));
				}
			}
			HttpEntity entity = new StringEntity(message, "UTF-8");
			post.setEntity(entity);
			HttpResponse response = httpUtil.execute(post);
			post.releaseConnection();
			String responseText = null;
			if (response != null) {
				responseText = IOUtils.toString(response.getEntity().getContent());
			}
			Map<String, String> responseMap = null;
			if (responseText != null) {
				responseMap = handleResult.handlerResult(responseText);
			}
			updateUploadFlag(data.get("XH"), responseMap);
			log.debug("发送数据:" + message);

		} catch (Exception e) {
			updateUploadFlag(data.get("XH"), null);
			log.error("IO异常!!!", e);
		} finally {
			countDownLatch.countDown();
		}

	}

	public void updateUploadFlag(String key, Map<String, String> result) {
		SqlSession session = sqlSessionFactory.openSession();
		try{
			if (result != null) {
				if (WorkConfig.OK.equals(result.get("code"))) {
					session.update(params.get(WorkParam.SQL) + ".success", key);
					session.close();
					log.info("违法记录上传成功,key:" + key + ",result:" + result);
					return;
				}
			}
			session.update(params.get(WorkParam.SQL) + ".fail", key);
			log.info("违法记录上传失败,key:" + key + ",result:" + result);
		}finally{
			session.close();
		}
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

	public CountDownLatch getCountDownLatch() {
		return countDownLatch;
	}

	public void setCountDownLatch(CountDownLatch countDownLatch) {
		this.countDownLatch = countDownLatch;
	}

}
