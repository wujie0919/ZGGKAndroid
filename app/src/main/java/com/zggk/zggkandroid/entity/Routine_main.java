package com.zggk.zggkandroid.entity;
/**
 * 主表，用来存放主单
 * @author Aaron
 *
 */
public class Routine_main {
	private String rId; // 主单ID
	private String lineCode; //线路ID
	private String DateTime; //时间
	private String user_code;  
	private String org_id;
	
	public String getrId() {
		return rId;
	}
	public void setrId(String rId) {
		this.rId = rId;
	}
	public String getLineCode() {
		return lineCode;
	}
	public void setLineCode(String lineCode) {
		this.lineCode = lineCode;
	}
	public String getDateTime() {
		return DateTime;
	}
	public void setDateTime(String dateTime) {
		DateTime = dateTime;
	}
	public String getUser_code() {
		return user_code;
	}
	public void setUser_code(String user_code) {
		this.user_code = user_code;
	}
	public String getOrg_id() {
		return org_id;
	}
	public void setOrg_id(String org_id) {
		this.org_id = org_id;
	}
}
