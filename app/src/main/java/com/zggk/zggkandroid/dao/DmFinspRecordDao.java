package com.zggk.zggkandroid.dao;

import com.zggk.zggkandroid.MainApplication;
import com.zggk.zggkandroid.common.DBHelperSingleton;
import com.zggk.zggkandroid.entity.DmFinsp;
import com.zggk.zggkandroid.entity.DmFinspRecord;
import com.zggk.zggkandroid.entity.DssTypeEntity;
import com.zggk.zggkandroid.entity.Mod_disease;

/**
 * 转换类和数据库插入类
 * @author Aaron
 *
 */

public class DmFinspRecordDao {

	public void insertFinspData(Mod_disease mDisease,DssTypeEntity mDssType) {
		DmFinspRecord record=new DmFinspRecord();
		DmFinsp finsp=(DmFinsp)DBHelperSingleton.getInstance().getObject(DmFinsp.class, "finspId='"+mDisease.getZhuId()+"'");
		
		if (finsp!=null) {
			record.setDssDesc(mDisease.getLocationDesc());
			record.setLane(mDisease.getLaneLocation());
			record.setDssId(MainApplication.getUUID());
			record.setStake(mDisease.getLandmarkStart()+"."+mDisease.getLandmarkEnd());
			record.setbId(mDisease.getId());
			record.setIsPhone("1");
			if (mDisease.getOrientation()==0) {
				record.setLineDirect("1");
			}else {
				record.setLineDirect("2");
			}
			switch (mDisease.getLevel()) {
				case 0:
					record.setDssDegree("01");
					break;
				case 1:
					record.setDssDegree("02");
					break;
				case 2:
					record.setDssDegree("03");
					break;
				default:
					break;
			}
			record.setMntnAdvice(mDisease.getAdvice());
			record.setFacilityCat(mDisease.getFacilityCat());
			if (mDisease.getLaneLocation().length()>0 || !mDisease.getLaneLocation().equals("")) {
				String lane=mDisease.getLaneLocation().substring(mDisease.getLaneLocation().length()-1);
				record.setLane(lane);
			}
			record.setDssCause(mDisease.getCause());
			record.setDssL(mDisease.getLength());
			record.setDssW(mDisease.getWidth());
			record.setDssD(mDisease.getDeep());
			record.setDssN(mDisease.getCount());
			record.setDssA(mDisease.getArea());
			record.setDssV(mDisease.getVolume());
			if (mDssType==null) {
				record.setDssAUnit("mm");
				record.setDssNUnit("处");
				record.setDssDUnit("m");
				record.setDssWUnit("m");
				record.setDssLUnit("m");
				record.setDssVUnit("m3");
			}else {
				record.setDssAUnit(mDssType.getDSS_A_UNIT());
				record.setDssNUnit(mDssType.getDSS_N_UNIT());
				record.setDssDUnit(mDssType.getDSS_D_UNIT());
				record.setDssWUnit(mDssType.getDSS_W_UNIT());
				record.setDssLUnit(mDssType.getDSS_L_UNIT());
				record.setDssVUnit(mDssType.getDSS_V_UNIT());
				record.setDssType(mDssType.getDSS_TYPE());
			}
			record.setDssQuality(String.valueOf(mDisease.getNature()));
			record.setFinspId(finsp.getFinspId());
			DBHelperSingleton.getInstance().insertOrReplaceData(record);
		}
	}
}
