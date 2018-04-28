package com.ehl.tvc.schedule.task;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.ehl.tvc.bean.TvcCar;
import com.ehl.tvc.common.WorkConfig;
import com.ehl.tvc.schedule.work.BasicWork.WorkParam;
import com.ehl.tvc.util.HttpUtil;
import com.ehl.tvc.version.HandleResult;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Dir2WebserviceTask implements Task {

	public static final Log log = LogFactory.getLog(Task.class);
	@Autowired
	private WorkConfig workConfig;
	@Autowired
	private Configuration configuration;
	@Autowired
	private HttpUtil httpUtil;
	@Autowired
	HandleResult handleResult;

	//
	private Map<String, String> params;
	private Object data;

	@Override
	public void run() {
		try {

			log.info("准备发送数据:" + data);
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
			String responseText = null;
			if (response != null) {
				responseText = IOUtils.toString(response.getEntity().getContent());
			}
			post.releaseConnection();
			Map<String, String> responseMap = null;
			if (responseText != null) {
				responseMap = handleResult.handlerResult(responseText);
			}
			String resultCode = responseMap.get("code");
			if("OK".equals(resultCode)){
				((TvcCar)data).doSuccess();
				log.info("发送成功数据:" + data.toString());
			}else{
				log.info("发送失败数据原始对象为:" + data.toString()+",发回报文为："+responseText);
				((TvcCar)data).doException(resultCode);
			}

		} catch (Exception e) {
			((TvcCar)data).doException(e.getMessage());
			log.error("IO异常!!!", e);
		} finally {
		}

	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	
	

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public void updateUploadFlag(String key, Map<String, String> result) {
		// TODO Auto-generated method stub
		
	}



}
