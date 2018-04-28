package com.ehl.tvc.common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "work")
public class WorkConfig {
	private static final Log log = LogFactory.getLog(WorkConfig.class);
	public static final String OK = "1";
	public static final String FAIL = "2";
	//
	private HashMap<String, String> heads;
	private List<Map<String, String>> works;
	//
	private String version;
	private Integer timeout;
	private Integer coresize;
	private Integer sleeptime;
	private Integer batchsize;

	@Override
	public String toString() {
		return "WorkConfig [heads=" + heads + ", works=" + Arrays.asList(works) + "]";
	}

	@PostConstruct
	public void init() {
		log.info(this);
	}

	public HashMap<String, String> getHeads() {
		return heads;
	}

	public void setHeads(HashMap<String, String> heads) {
		this.heads = heads;
	}

	public List<Map<String, String>> getWorks() {
		return works;
	}

	public void setWorks(List<Map<String, String>> works) {
		this.works = works;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	public Integer getCoresize() {
		return coresize;
	}

	public void setCoresize(Integer coresize) {
		this.coresize = coresize;
	}

	public Integer getSleeptime() {
		return sleeptime;
	}

	public void setSleeptime(Integer sleeptime) {
		this.sleeptime = sleeptime;
	}

	public Integer getBatchsize() {
		return batchsize;
	}

	public void setBatchsize(Integer batchsize) {
		this.batchsize = batchsize;
	}

}
