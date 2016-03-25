/*
 * 涵洞经常检查model类
 */

package com.zggk.zggkandroid.entity;

public class Mod_culvert {
	public String project; //检查项目
	public String proDesc; //情况描述
	public String conclusion; //检查结论
	public String advice; //养护建议
	public String note; //备注
	
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getProDesc() {
		return proDesc;
	}
	public void setProDesc(String proDesc) {
		this.proDesc = proDesc;
	}
	public String getConclusion() {
		return conclusion;
	}
	public void setConclusion(String conclusion) {
		this.conclusion = conclusion;
	}
	public String getAdvice() {
		return advice;
	}
	public void setAdvice(String advice) {
		this.advice = advice;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
}
