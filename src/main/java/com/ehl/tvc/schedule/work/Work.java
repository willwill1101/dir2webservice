package com.ehl.tvc.schedule.work;

import java.util.Map;

public interface Work extends Runnable {
	public void setParams(Map<String, String> params) ;
}
