package com.ehl.tvc.schedule.work.dir2work;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ehl.tvc.bean.TvcCar;
import com.ehl.tvc.common.EhlException;
import com.ehl.tvc.common.TvcDicUtil;
@Component
public class String2CarUtil {
	@Autowired
	private TvcDicUtil tvcDicUtil;

	public TvcCar convert(List<String> carbylist,TvcCar car) throws EhlException {
		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
		DateFormat df2 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		Map<String, String> map = new HashMap<String, String>();
		for (String str : carbylist) {
			String[] strs = str.split("=");
			if (strs.length == 2) {
				map.put(strs[0].trim(), strs[1].trim());
			}
		}
		//TvcCar car = new TvcCar();
		car.setJsszb("0,0,0,0");
		car.setZpfx("1");
		car.setCltxzt("1");
		
		for (FileField field : FileField.values()) {
			String v = map.get(field.toString());
			if (v == null) {
				throw new EhlException(field.toString() + ":为null:+"+carbylist);
			}
			switch (field) {
			case DeviceNO:
				car.setSbbh(v);
				break;
			case DevideIP:
				break;
			case CrossingNO:
				car.setKkbh(v);
				break;
			case CrossingName:
				break;
			case CaptureTime1: {
				try {
					car.setTgsj(df1.format(df2.parse(v)));
				} catch (ParseException e) {
					throw new EhlException(v + ":时间格式化错误");
				}
				break;
			}
			case RedLightBeginTime:
				break;
			case RedLighEndTime:
				break;
			case Plate: {
				car.setHphm(v);
				if ("未知".equals(v)) {
					car.setHphm("无牌");
				}
				break;
			}
			case PlateColor: {
				car.setHpys("4");
				car.setHpzl("99");
				String code = tvcDicUtil.converName2Code(tvcDicUtil.DIC_HPYS + v.substring(0, 1) + "色");
				if (code != null) {
					car.setHpys(code);
					if("1".equals(code)){
						car.setHpzl("01");
					}else if( "2".equals(code)){
						car.setHpzl("02");
					}
				}
				break;
			}
			case CarColor: {
				car.setCsys("Z");
				String code = tvcDicUtil.converName2Code(tvcDicUtil.DIC_CSYS + v.substring(0, 1));
				if (code != null) {
					car.setCsys(code);
				}

				break;
			}
			case CarType:
//				String code = tvcDicUtil.converName2Code(tvcDicUtil.DIC_CLLX);
//				car.
				break;
			case CarBrand: {
				car.setClpp("100100");
				String code = tvcDicUtil.converName2Code(tvcDicUtil.DIC_CLPP + v);
				if(code!=null){
					car.setClpp(code);
				}
				car.setClzwpp(v);
				break;
			}
			case SafetyBelt:
				break;
			case LaneNO:
				car.setCdbh(v);
				break;
			case LaneName:
				break;
			case LaneDirection:
				String code = tvcDicUtil.converName2Code(tvcDicUtil.DIC_CXFX +v.replace("从", "由").replace("到", "向"));
				car.setCxfx(code);
				break;
			case Speed:
				car.setXssd(v);
				break;
			case CarSpeedLowLimit:
				break;
			case CarSpeedUpLimit:
				break;
			case TruckSpeedLowLimit:
				break;
			case TruckSpeedUpLimit:
				break;
			case IllegalCode:
				break;
			case IllegalType:
				break;
			case PlateCoordinates: {
				car.setCpzb(v.replaceAll("@", ","));
				break;
			}
			case PicCount:
				break;
			case Picture1:
				car.setTplj1(v);
				break;
			default:
				break;
			}
		}
		return car;
	}
}
