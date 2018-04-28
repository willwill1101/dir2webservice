package com.ehl.tvc.version;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;

public class HandleResultV2 extends HandleResult {
	public static Log log = LogFactory.getLog(HandleResultV2.class);

	/**
	 * 处理返回结果
	 * 
	 * @param result
	 *            XML String
	 */
	public Map<String, String> handlerResult(String result) {
		log.info(result);
		Map<String, String> map = null;
		Document document = null;
		try {
			map = new HashMap<String, String>();
			document = DocumentHelper.parseText(result);
			Node node = document.selectSingleNode("//return[1]");
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
