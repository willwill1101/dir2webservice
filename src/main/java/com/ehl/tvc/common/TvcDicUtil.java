package com.ehl.tvc.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class TvcDicUtil {
	
	@Autowired
	private SqlSessionFactory sessionFactory;
	
	public static String DIC_CLTXZT = "260004_";// 车辆通行状态
	public static String DIC_HPZL = "261006_";// 号牌种类
	public static String DIC_HPYS = "261005_";// 号牌颜色
	public static String DIC_CXFX = "261004_";// 车行方向
	public static String DIC_CDBH = "260010_";// 车道编号
	public static String DIC_ZPFX = "260003_";// 抓拍方向
	public static String DIC_CSYS = "261003_";// 车身颜色
	public static String DIC_CLPP = "261012_";// 车辆品牌
	public static String DIC_CLLX = "300001_";//车辆类型
	
	//public final static String checkSql = "select CONCAT(CONCAT(dmlb,'_'),dmsm),dm from T_ITGS_CODE t";
	/**
	 * 字典
	 */
	private static Map<String,String> checkMap = new HashMap<String,String>();
	
	
	@PostConstruct
	public void init() {
		SqlSession session = sessionFactory.openSession();
		List<Map> datas = null;
			try {
				datas = session.selectList("tvcDic.select");
				for (Map line : datas) {
					checkMap.put((String) line.get("dmsm"), (String) line.get("dm"));
				} 
			} catch (Exception e) {
				e.printStackTrace();
		}finally{
			session.close();
		}
	}
	
	public static String converName2Code(String name){
		return checkMap.get(name);
	}
}
