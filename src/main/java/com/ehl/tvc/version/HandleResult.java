package com.ehl.tvc.version;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;

public abstract class HandleResult {
	public static Log log = LogFactory.getLog(HandleResult.class);

	/**
	 * 处理返回结果
	 * 
	 * @param result
	 *            XML String
	 */
	public Map<String, String> handlerResult(String result) {

		Map<String, String> map = null;
		Document document = null;
		try {
			map = new HashMap<String, String>();
			document = DocumentHelper.parseText(result);
			log.info(document.asXML());
			Node node = document.selectSingleNode("//inPeccancyInfoReturn[1]");
			if (node != null) {
				document = DocumentHelper.parseText(node.getText());
				String code = document.selectSingleNode("//code[1]").getText();
				String message = document.selectSingleNode("//message[1]").getText();
				map.put("code", code);
				map.put("message", message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;

	}
}
