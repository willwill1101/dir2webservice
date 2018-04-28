package com.ehl.tvc.schedule.work.dir2work;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.ehl.tvc.bean.TvcCar;
import com.ehl.tvc.common.WorkConfig;
import com.ehl.tvc.schedule.task.Dir2WebserviceTask;
import com.ehl.tvc.schedule.work.Work;
import com.ehl.tvc.util.FileUtil;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Dir2WebserviceWork implements Work, ApplicationContextAware {
	public static Log log = LogFactory.getLog(Dir2WebserviceWork.class);

	public volatile static Boolean started = true;
	private Map<String, String> params;
	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
	@Autowired
	private String2CarUtil string2CarUtil;

	@Autowired
	private WorkConfig workConfig;
	/**
	 * 存放预发送的tvccar
	 */
	private static LinkedBlockingQueue<TvcCar> queue = null;

	@Override
	public void run() {
		int queueCapacity =Integer.valueOf(params.get(WorkParam.QUEUE_CAPACITY));
		queue = new LinkedBlockingQueue<TvcCar>(queueCapacity);
		/*
		 * 启动读取文件读取线程
		 * 
		 */
		new ReadFileThread().start();
		while (started) {
			try {
				TvcCar car = queue.take();
				Dir2WebserviceTask basicTask = applicationContext.getBean("dir2WebserviceTask", Dir2WebserviceTask.class);
				basicTask.setParams(params);
				basicTask.setData(car);
				threadPoolTaskExecutor.execute(basicTask);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@PostConstruct
	public void init() {
		log.info(this);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	private ApplicationContext applicationContext;

	@Override
	public String toString() {
		return "Work [params=" + params + ", threadPoolTaskExecutor=" + threadPoolTaskExecutor + " workConfig="
				+ workConfig + ", applicationContext=" + applicationContext + "]";
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public static class WorkParam {
		public static final String ENABLED = "enabled";
		public static final String NAME = "name";
		public static final String TEMPLATE = "template";
		public static final String SQL = "sql";
		public static final String URL = "url";
		public static final String PATH = "datapath";
		public static final String FILE_SFX = "fileSfx";
		public static final String QUEUE_CAPACITY = "queueCapacity";
		public static final String INI_ENCODING = "iniEncoding";

	}

	class ReadFileThread extends Thread {

		String dataPath = params.get(WorkParam.PATH);
		String fileSuffix = params.get(WorkParam.FILE_SFX);
		String encoding = params.get(WorkParam.INI_ENCODING);

		@Override
		public void run() {

			while (started) {
				
				try {
//					if (queue.size()!=0) {
//						continue;
//					}
					// 读取指定目录指定后缀的文件
					File dir = new File(dataPath);
					File[] files = dir.listFiles(new FilenameFilter() {
						@Override
						public boolean accept(File dir, String name) {
							if (name.endsWith(fileSuffix)) {
								return true;
							}
							return false;
						}
					});

					// 没有数据是线程睡眠100毫秒
					if (files.length == 0) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							log.error(e);
						}
					}

					// 遍历转换文件然后删除成功的数据备份有问题的数据
					for (File iniFile : files) {
						if(!FileUtil.checkIniTransfered(iniFile)){
							log.warn("文件没有传输完毕："+iniFile.getName());
							continue;
						}
						InputStream inputStream = null;
						InputStream imgInputStream = null;
						ByteArrayOutputStream imgByteOutStream = null;
						File imgFile = null;
						TvcCar car = new TvcCar();
						String exceptionMess = null;
						try {
							imgFile = new File(dataPath + "/" + iniFile.getName().replace("ini", "jpg"));
							//创建发送目录
							File sendingFile = new File(dataPath + "/sending" );
							if(!sendingFile.exists()){
								sendingFile.mkdirs();
							}
							File iniFileSending =new File(dataPath+"/sending/"+iniFile.getName());
							File imgFileSending =new File(dataPath+"/sending/"+imgFile.getName());
//							Process p =  Runtime.getRuntime().exec("mv "+iniFile.getAbsolutePath()+" "+iniFileSending.getAbsolutePath() );
//							
//							Process p2 =   Runtime.getRuntime().exec("mv "+imgFile.getAbsolutePath()+" "+imgFileSending.getAbsolutePath() );
//							
//							p.waitFor();
//							p2.waitFor();
							iniFile.renameTo(iniFileSending);
							imgFile.renameTo(imgFileSending);
							iniFile= iniFileSending;
							imgFile= imgFileSending;
							car.setIniFile(iniFile);
							car.setImgFile(imgFile);
							// 解析ini文件为list
							inputStream = new FileInputStream(iniFile);
							List<String> carbylist = IOUtils.readLines(inputStream, encoding);
							inputStream.close();
							car = string2CarUtil.convert(carbylist, car);

							// 将图片数据解析为base64字符串
							imgByteOutStream = new ByteArrayOutputStream();
							imgInputStream = new FileInputStream(imgFile);
							IOUtils.copy(imgInputStream, imgByteOutStream);
							String imgbase64Str = Base64.encodeBase64String(imgByteOutStream.toByteArray());
							imgInputStream.close();
							imgByteOutStream.close();
							car.setBase64img(imgbase64Str);
							queue.put(car);
						} catch (Throwable e) {
							exceptionMess = e.getMessage();
							log.error(e);
						} finally {
							try {
								inputStream.close();
							} catch (Exception e) {
							}
							try {
								imgInputStream.close();
							} catch (Exception e) {
							}
							try {
								imgByteOutStream.close();
							} catch (Exception e) {
							}
						}
						if (exceptionMess != null) {
							// 如果有异常备份数据
							car.doException(exceptionMess);
						}

					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

	enum FileField {
		DeviceNO, DevideIP, CrossingNO, CrossingName, CaptureTime1, RedLightBeginTime, RedLighEndTime, Plate, PlateColor, CarColor, CarType, CarBrand, SafetyBelt, LaneNO, LaneName, LaneDirection, Speed, CarSpeedLowLimit, CarSpeedUpLimit, TruckSpeedLowLimit, TruckSpeedUpLimit, IllegalCode, IllegalType, PlateCoordinates, PicCount, Picture1

	}

}
