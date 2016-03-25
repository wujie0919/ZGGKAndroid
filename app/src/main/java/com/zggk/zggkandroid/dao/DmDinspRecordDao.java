package com.zggk.zggkandroid.dao;

import android.text.TextUtils;

import com.zggk.zggkandroid.MainApplication;
import com.zggk.zggkandroid.common.DBHelperSingleton;
import com.zggk.zggkandroid.entity.DmDinsp;
import com.zggk.zggkandroid.entity.DmDinspRecord;
import com.zggk.zggkandroid.entity.DssTypeEntity;
import com.zggk.zggkandroid.entity.Mod_disease;

public class DmDinspRecordDao {
	/**
	 * 插入日常巡查数据到数据库
	 */
	public void insertRiChang(Mod_disease mDisease,DssTypeEntity mDssType) {
		DmDinspRecord dinspRecord=new DmDinspRecord();
		DmDinsp dinsp=(DmDinsp)DBHelperSingleton.getInstance().getObject(DmDinsp.class, "dinspId='"+mDisease.getZhuId()+"'");
		if (dinsp!=null) {
			dinspRecord.setDssDesc(mDisease.getLocationDesc());
			dinspRecord.setDinspId(dinsp.getDinspId());
			dinspRecord.setDssId(MainApplication.getUUID());
			dinspRecord.setbId(mDisease.getId());
			// 早
			if (dinsp.getInspTimeIntvlM() != null) {
				dinspRecord.setInspTimeIntvl(String.valueOf(dinsp
						.getInspTimeIntvlM()));
			}
			// 下
			if (dinsp.getInspTimeIntvlA() != null) {
				dinspRecord.setInspTimeIntvl(String.valueOf(dinsp
						.getInspTimeIntvlA()));
			}
			// 晚
			if (dinsp.getInspTimeIntvlN() != null) {
				dinspRecord.setInspTimeIntvl(String.valueOf(dinsp
						.getInspTimeIntvlN()));
			}

			if (mDisease.getProblemType() == 0) {
				dinspRecord.setIssueType("2");
			} else {
				dinspRecord.setIssueType("1");
			}
			if (mDisease.getOrientation() == 0) {
				dinspRecord.setLineDirect("1");
			} else {
				dinspRecord.setLineDirect("2");
			}
			dinspRecord.setStake(mDisease.getLandmarkStart() + "."
					+ mDisease.getLandmarkEnd());

			switch (mDisease.getLevel()) {
			case 0:
				dinspRecord.setDssDegree("01");
				break;
			case 1:
				dinspRecord.setDssDegree("02");
				break;
			case 2:
				dinspRecord.setDssDegree("03");
				break;
			default:
				break;
			}
			dinspRecord.setMntnAdvice(mDisease.getAdvice());
			dinspRecord.setFacilityCat(mDisease.getFacilityCat());
			String laneLoacation = mDisease.getLaneLocation();
			if (!TextUtils.isEmpty(laneLoacation)) {
				String lane = laneLoacation.substring(laneLoacation.length() - 1);
				dinspRecord.setLane(lane);
			}
			dinspRecord.setDssCause(mDisease.getCause());
			dinspRecord.setDssL(mDisease.getLength());
			dinspRecord.setDssW(mDisease.getWidth());
			dinspRecord.setDssD(mDisease.getDeep());
			dinspRecord.setDssN(mDisease.getCount());
			dinspRecord.setDssA(mDisease.getArea());
			dinspRecord.setDssV(mDisease.getVolume());
			if (mDssType == null) {
				dinspRecord.setDssAUnit("m2");
				dinspRecord.setDssNUnit("处");
				dinspRecord.setDssDUnit("m");
				dinspRecord.setDssWUnit("m");
				dinspRecord.setDssLUnit("m");
				dinspRecord.setDssVUnit("m3");
			} else {
				dinspRecord.setDssAUnit(mDssType.getDSS_A_UNIT());
				dinspRecord.setDssNUnit(mDssType.getDSS_N_UNIT());
				dinspRecord.setDssDUnit(mDssType.getDSS_D_UNIT());
				dinspRecord.setDssWUnit(mDssType.getDSS_W_UNIT());
				dinspRecord.setDssLUnit(mDssType.getDSS_L_UNIT());
				dinspRecord.setDssVUnit(mDssType.getDSS_V_UNIT());
				dinspRecord.setDssType(mDssType.getDSS_TYPE());
			}
			dinspRecord.setInspTime(MainApplication.getNowDate());
			dinspRecord.setDssQuality(String.valueOf(mDisease.getNature()));
			DBHelperSingleton.getInstance().insertData(dinspRecord);
		}
	}

}
