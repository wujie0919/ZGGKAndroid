/*
 * 日常巡检model类
 */

package com.zggk.zggkandroid.entity;


public class Mod_prowled {

	public int beginScope;	//路线范围前面数字
	public int endScope;	//路线范围后面数字
	public int problemType; //问题类型 0：病害  1：保洁
	public int lineDirection; //路线方向 0：上行 1：下行
	public String lanePosition; //车道位置
	public int beginLineNum; //路线桩号  前面数字
	public int endLineNum; //路线桩号 后面数字
	public String diseaseType; //病害类型
	public int length; //长
	public int width; //宽
	public int deep; //深
	public int area; //面积
	public int num; //数量
	public int volume; // 体积
	public int doseaseNature; //病害性质
	public int advice; //养护建议
	public String reason; //初判原因
	public String picUrls; //图片路径
	
	public int getBeginScope() {
		return beginScope;
	}
	public void setBeginScope(int beginScope) {
		this.beginScope = beginScope;
	}
	public int getEndScope() {
		return endScope;
	}
	public void setEndScope(int endScope) {
		this.endScope = endScope;
	}
	public int getProblemType() {
		return problemType;
	}
	public void setProblemType(int problemType) {
		this.problemType = problemType;
	}
	public int getLineDirection() {
		return lineDirection;
	}
	public void setLineDirection(int lineDirection) {
		this.lineDirection = lineDirection;
	}
	public String getLanePosition() {
		return lanePosition;
	}
	public void setLanePosition(String lanePosition) {
		this.lanePosition = lanePosition;
	}
	public int getBeginLineNum() {
		return beginLineNum;
	}
	public void setBeginLineNum(int beginLineNum) {
		this.beginLineNum = beginLineNum;
	}
	public int getEndLineNum() {
		return endLineNum;
	}
	public void setEndLineNum(int endLineNum) {
		this.endLineNum = endLineNum;
	}
	public String getDiseaseType() {
		return diseaseType;
	}
	public void setDiseaseType(String diseaseType) {
		this.diseaseType = diseaseType;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getDeep() {
		return deep;
	}
	public void setDeep(int deep) {
		this.deep = deep;
	}
	public int getArea() {
		return area;
	}
	public void setArea(int area) {
		this.area = area;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getVolume() {
		return volume;
	}
	public void setVolume(int volume) {
		this.volume = volume;
	}
	public int getDoseaseNature() {
		return doseaseNature;
	}
	public void setDoseaseNature(int doseaseNature) {
		this.doseaseNature = doseaseNature;
	}
	public int getAdvice() {
		return advice;
	}
	public void setAdvice(int advice) {
		this.advice = advice;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getPicUrls() {
		return picUrls;
	}
	public void setPicUrls(String picUrls) {
		this.picUrls = picUrls;
	}
}
