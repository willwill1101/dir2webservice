package com.ehl.tvc.bean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class TvcCar implements Serializable {

	private static final long serialVersionUID = -3372308120661141446L;
	private static Logger log = Logger.getLogger(TvcCar.class);

	/**
	 * 号牌号码
	 */
	private String hphm;
	/**
	 * 号牌种类
	 */
	private String hpzl;
	/**
	 * 车道编号
	 */
	private String cdbh;
	/**
	 * 通过时间
	 */
	private String tgsj;
	/**
	 * 行驶速度
	 */
	private String xssd;
	/**
	 * 号牌颜色
	 */
	private String hpys;
	/**
	 * 车行方向
	 */
	private String cxfx;
	/**
	 * 抓拍方向
	 */
	private String zpfx;
	/**
	 * 车身颜色
	 */
	private String csys;
	/**
	 * 卡口编号
	 */
	private String kkbh;
	/**
	 * 设备编号
	 */
	private String sbbh;
	/**
	 * 车辆通行状态
	 */
	private String cltxzt;
	/**
	 * 车辆品牌
	 */
	private String clpp;
	/**
	 * 车辆中文品牌
	 */
	private String clzwpp;

	public String getClzwpp() {
		return clzwpp;
	}

	public void setClzwpp(String clzwpp) {
		this.clzwpp = clzwpp;
	}

	/**
	 * 车牌坐标
	 */
	private String cpzb;
	/**
	 * 驾驶室坐标
	 */
	private String jsszb;
	/**
	 * 图片路径1-全景图像
	 */
	private String tplj1;
	/**
	 * 图片路径2-特制图像
	 */
	private String tplj2;
	/**
	 * 图片路径3-号牌图像
	 */
	private String tplj3;// 号牌图像
	
	
	
    private String base64img;
	
	
	
	private File iniFile;
	
	private File imgFile;

	public String getCdbh() {
		return cdbh;
	}

	public String getClpp() {
		return clpp;
	}

	public String getCltxzt() {
		return cltxzt;
	}

	public String getCpzb() {
		return cpzb;
	}

	public String getCsys() {
		return csys;
	}

	public String getCxfx() {
		return cxfx;
	}

	public String getHphm() {
		return hphm;
	}

	public String getHpys() {
		return hpys;
	}

	public String getHpzl() {
		return hpzl;
	}

	public String getJsszb() {
		return jsszb;
	}

	public String getKkbh() {
		return kkbh;
	}

	public String getSbbh() {
		return sbbh;
	}

	public String getTgsj() {
		return tgsj;
	}

	public String getTplj1() {
		return tplj1;
	}

	public String getTplj2() {
		return tplj2;
	}

	public String getTplj3() {
		return tplj3;
	}

	public String getXssd() {
		return xssd;
	}

	public String getZpfx() {
		return zpfx;
	}

	public void setCdbh(String cdbh) {
		this.cdbh = cdbh;
	}

	public void setClpp(String clpp) {
		this.clpp = clpp;
	}

	public void setCltxzt(String cltxzt) {
		this.cltxzt = cltxzt;
	}

	public void setCpzb(String cpzb) {
		this.cpzb = cpzb;
	}

	public void setCsys(String csys) {
		this.csys = csys;
	}

	public void setCxfx(String cxfx) {
		this.cxfx = cxfx;
	}

	public void setHphm(String hphm) {
		this.hphm = hphm;
	}

	public void setHpys(String hpys) {
		this.hpys = hpys;
	}

	public void setHpzl(String hpzl) {
		this.hpzl = hpzl;
	}

	public void setJsszb(String jsszb) {
		this.jsszb = jsszb;
	}

	public void setKkbh(String kkbh) {
		this.kkbh = kkbh;
	}

	public void setSbbh(String sbbh) {
		this.sbbh = sbbh;
	}

	public void setTgsj(String tgsj) {
		this.tgsj = tgsj;
	}

	public void setTplj1(String tplj1) {
		this.tplj1 = tplj1;
	}

	public void setTplj2(String tplj2) {
		this.tplj2 = tplj2;
	}

	public void setTplj3(String tplj3) {
		this.tplj3 = tplj3;
	}

	public void setXssd(String xssd) {
		this.xssd = xssd;
	}

	public void setZpfx(String zpfx) {
		this.zpfx = zpfx;
	}
	
	


	public File getIniFile() {
		return iniFile;
	}

	public void setIniFile(File iniFile) {
		this.iniFile = iniFile;
	}

	public File getImgFile() {
		return imgFile;
	}

	public void setImgFile(File imgFile) {
		this.imgFile = imgFile;
	}
	
	

	public String getBase64img() {
		return base64img;
	}

	public void setBase64img(String base64img) {
		this.base64img = base64img;
	}

	@Override
	public String toString() {
		return "TvcCar [hphm=" + hphm + ", hpzl=" + hpzl + ", cdbh=" + cdbh + ", tgsj=" + tgsj + ", xssd=" + xssd
				+ ", hpys=" + hpys + ", cxfx=" + cxfx + ", zpfx=" + zpfx + ", csys=" + csys + ", kkbh=" + kkbh
				+ ", sbbh=" + sbbh + ", cltxzt=" + cltxzt + ", clpp=" + clpp + ", clzwpp=" + clzwpp + ", cpzb=" + cpzb
				+ ", jsszb=" + jsszb + ", tplj1=" + tplj1 + ", tplj2=" + tplj2 + ", tplj3=" + tplj3 + "]";
	}

	public void doException(String excepionmess){
		String basedir =  iniFile.getParentFile().getAbsolutePath();
		String bakDir = basedir+"/bak/"+System.currentTimeMillis()+"/";
		try {
			File bakDirFile = new File(bakDir);
			if(!bakDirFile.exists()){
				bakDirFile.mkdirs();
			}
			//Runtime.getRuntime().exec("mkdir -p "+bakDir);
			File errorFile =  new File (bakDir+"error");
			FileOutputStream erroroutput  = null;
			try {
				 erroroutput =  new FileOutputStream(errorFile);
				IOUtils.write(excepionmess, erroroutput, "UTF-8");
				erroroutput.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				IOUtils.closeQuietly(erroroutput);
			}
		
			
			iniFile.renameTo(new File(bakDir+iniFile.getName()));
			imgFile.renameTo(new File(bakDir+imgFile.getName()));
			
			//Runtime.getRuntime().exec("echo '"+excepionmess+"' >"+bakDir+"error" );
			//Runtime.getRuntime().exec("mv "+iniFile.getAbsolutePath()+" "+bakDir+iniFile.getName() );
			//Runtime.getRuntime().exec("mv "+imgFile.getAbsolutePath()+" "+bakDir+imgFile.getName() );
			log.info(iniFile.getName()+"文件处理失败,备份目录为："+bakDir);
		} catch (Exception e) {
			log.error(e);
		}
		
	}

	public void doSuccess(){
		iniFile.delete();
		imgFile.delete();
	}
	
	


}
