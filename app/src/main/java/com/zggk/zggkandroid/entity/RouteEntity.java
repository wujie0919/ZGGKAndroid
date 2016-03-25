package com.zggk.zggkandroid.entity;

import java.io.Serializable;

/**
 * 线路实体类/保存用户数据
 * 
 * @author laowo
 * 
 */
public class RouteEntity implements Serializable {

	private String LINE_ID, LINE_CODE, LINE_ALLNAME, STARTSTAKE, ENDSTAKE,
			DOWN_START_STAKE_NUM, DOWN_END_STAKE_NUM;

	public String getLINE_CODE() {
		return LINE_CODE;
	}

	public void setLINE_CODE(String lINE_CODE) {
		LINE_CODE = lINE_CODE;
	}

	public String getLINE_ALLNAME() {
		return LINE_ALLNAME;
	}

	public void setLINE_ALLNAME(String lINE_ALLNAME) {
		LINE_ALLNAME = lINE_ALLNAME;
	}

	public String getLINE_ID() {
		return LINE_ID;
	}

	public void setLINE_ID(String lINE_ID) {
		LINE_ID = lINE_ID;
	}

	public String getSTARTSTAKE() {
		return STARTSTAKE;
	}

	public void setSTARTSTAKE(String sTARTSTAKE) {
		STARTSTAKE = sTARTSTAKE;
	}

	public String getENDSTAKE() {
		return ENDSTAKE;
	}

	public void setENDSTAKE(String eNDSTAKE) {
		ENDSTAKE = eNDSTAKE;
	}

	public String getDOWN_START_STAKE_NUM() {
		return DOWN_START_STAKE_NUM;
	}

	public void setDOWN_START_STAKE_NUM(String dOWN_START_STAKE_NUM) {
		DOWN_START_STAKE_NUM = dOWN_START_STAKE_NUM;
	}

	public String getDOWN_END_STAKE_NUM() {
		return DOWN_END_STAKE_NUM;
	}

	public void setDOWN_END_STAKE_NUM(String dOWN_END_STAKE_NUM) {
		DOWN_END_STAKE_NUM = dOWN_END_STAKE_NUM;
	}

}
