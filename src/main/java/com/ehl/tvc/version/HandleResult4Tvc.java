package com.ehl.tvc.version;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;

public class HandleResult4Tvc extends HandleResult {
	public static Log log = LogFactory.getLog(HandleResult4Tvc.class);

	/**
	 * 处理返回结果
	 * 
	 * @param result
	 *            XML String
	 */
	private static  Pattern  pattern = Pattern.compile("out>(.*?)</");
	public Map<String, String> handlerResult(String result) {
		//log.info(result);
		Map<String, String> map = null;
		Document document = null;
		try {
			map = new HashMap<String, String>();
				String code = "";
				Matcher matcher =  pattern.matcher(result);
				if(matcher.find()){
					code = matcher.group(1);
				}
				map.put("code", code);
				map.put("message", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
