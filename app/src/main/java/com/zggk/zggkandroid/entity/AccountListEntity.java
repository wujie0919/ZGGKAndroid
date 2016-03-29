package com.zggk.zggkandroid.entity;

import java.io.Serializable;

public class AccountListEntity implements Serializable{

	private String ORG_NAME,USER_CODE,ID,PASSWORD,USER_NAME,ORG_ID,UNT_NAME,ORG_NAME_EN;
	
	public String getORG_NAME_EN() {
		return ORG_NAME_EN;
	}

	public void setORG_NAME_EN(String oRG_NAME_EN) {
		ORG_NAME_EN = oRG_NAME_EN;
	}

	public String getORG_NAME() {
		return ORG_NAME;
	}

	public void setORG_NAME(String oRG_NAME) {
		ORG_NAME = oRG_NAME;
	}

	public String getUSER_CODE() {
		return USER_CODE;
	}

	public void setUSER_CODE(String uSER_CODE) {
		USER_CODE = uSER_CODE;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getPASSWORD() {
		return PASSWORD;
	}

	public void setPASSWORD(String pASSWORD) {
		PASSWORD = pASSWORD;
	}

	public String getUSER_NAME() {
		return USER_NAME;
	}

	public void setUSER_NAME(String uSER_NAME) {
		USER_NAME = uSER_NAME;
	}

	public String getORG_ID() {
		return ORG_ID;
	}

	public void setORG_ID(String oRG_ID) {
		ORG_ID = oRG_ID;
	}

	public String getUNT_NAME() {
		return UNT_NAME;
	}

	public void setUNT_NAME(String uNT_NAME) {
		UNT_NAME = uNT_NAME;
	}
	
}
