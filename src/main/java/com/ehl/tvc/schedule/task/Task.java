package com.ehl.tvc.schedule.task;

import java.util.Map;

public interface Task extends Runnable {
	public void updateUploadFlag(String key, Map<String, String> result);
}