/**
 * 
 */
package com.zggk.zggkandroid.entity;

import java.io.Serializable;

/**
 * @author xsh
 * 
 */
public class Mod_disease implements Serializable {

	public static final String TAB_ID = "id";

	private String id;
	private String zhuId;
	private String date;
	private String user;
	private String section;
	private String mileage;
	private String carNum;
	private String weather;

	private int type;// 0=日常巡查、1=路面经常检查、2=路基、3=设施、4=绿化
	private int problemType;
	private int orientation;
	private String laneLocation;
	private String lineName;// 路线名称

	private String landmarkStart;
	private String landmarkEnd;
	private String diseaseType;
	private String length, width, deep, area, count, volume;
	private int level, nature;
	private String advice;
	private String cause;
	private String path;
	private String bridge, component, component2, material, parts;
	private String percantage, angle;
	private String maneUnits, curingUnits;
	private boolean uploaded;
	private String direction;
	private String zongpin;
	private String comment;
	private String scale;
	private String structureName, describe, conclusion, project;
	private boolean isDisease = true;

	private String inspTimeIntvlStart, inspTimeIntvlEnd;
	private String locationDesc;// 位置描述

	private RouteEntity route;
	private String facilityCat;
	private String lineID; // 路线ID，关联路线表
	private String DSS_TYPE; // 病害类型

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public String getLocationDesc() {
		return locationDesc;
	}

	public void setLocationDesc(String locationDesc) {
		this.locationDesc = locationDesc;
	}

	public String getDSS_TYPE() {
		return DSS_TYPE;
	}

	public void setDSS_TYPE(String dSS_TYPE) {
		DSS_TYPE = dSS_TYPE;
	}

	public String getLineID() {
		return lineID;
	}

	public void setLineID(String lineID) {
		this.lineID = lineID;
	}

	public String getFacilityCat() {
		return facilityCat;
	}

	public void setFacilityCat(String facilityCat) {
		this.facilityCat = facilityCat;
	}

	public String getZhuId() {
		return zhuId;
	}

	public void setZhuId(String zhuId) {
		this.zhuId = zhuId;
	}

	public RouteEntity getRoute() {
		return route;
	}

	public void setRoute(RouteEntity route) {
		this.route = route;
	}

	public String getInspTimeIntvlStart() {
		return inspTimeIntvlStart;
	}

	public void setInspTimeIntvlStart(String inspTimeIntvlStart) {
		this.inspTimeIntvlStart = inspTimeIntvlStart;
	}

	public String getInspTimeIntvlEnd() {
		return inspTimeIntvlEnd;
	}

	public void setInspTimeIntvlEnd(String inspTimeIntvlEnd) {
		this.inspTimeIntvlEnd = inspTimeIntvlEnd;
	}

	public boolean isDisease() {
		return isDisease;
	}

	public void setDisease(boolean isDisease) {
		this.isDisease = isDisease;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public String getStructureName() {
		return structureName;
	}

	public void setStructureName(String structureName) {
		this.structureName = structureName;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getConclusion() {
		return conclusion;
	}

	public void setConclusion(String conclusion) {
		this.conclusion = conclusion;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getZongpin() {
		return zongpin;
	}

	public void setZongpin(String zongpin) {
		this.zongpin = zongpin;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isUploaded() {
		return uploaded;
	}

	public void setUploaded(boolean uploaded) {
		this.uploaded = uploaded;
	}

	public String getManeUnits() {
		return maneUnits;
	}

	public void setManeUnits(String maneUnits) {
		this.maneUnits = maneUnits;
	}

	public String getCuringUnits() {
		return curingUnits;
	}

	public void setCuringUnits(String curingUnits) {
		this.curingUnits = curingUnits;
	}

	public String getPercantage() {
		return percantage;
	}

	public void setPercantage(String percantage) {
		this.percantage = percantage;
	}

	public String getAngle() {
		return angle;
	}

	public void setAngle(String angle) {
		this.angle = angle;
	}

	public String getBridge() {
		return bridge;
	}

	public void setBridge(String bridge) {
		this.bridge = bridge;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getComponent2() {
		return component2;
	}

	public void setComponent2(String component2) {
		this.component2 = component2;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getParts() {
		return parts;
	}

	public void setParts(String parts) {
		this.parts = parts;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getMileage() {
		return mileage;
	}

	public void setMileage(String mileage) {
		this.mileage = mileage;
	}

	public String getCarNum() {
		return carNum;
	}

	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public int getProblemType() {
		return problemType;
	}

	public void setProblemType(int problemType) {
		this.problemType = problemType;
	}

	public int getOrientation() {
		return orientation;
	}

	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}

	public String getLaneLocation() {
		return laneLocation;
	}

	public void setLaneLocation(String laneLocation) {
		this.laneLocation = laneLocation;
	}

	public String getLandmarkStart() {
		return landmarkStart;
	}

	public void setLandmarkStart(String landmarkStart) {
		this.landmarkStart = landmarkStart;
	}

	public String getLandmarkEnd() {
		return landmarkEnd;
	}

	public void setLandmarkEnd(String landmarkEnd) {
		this.landmarkEnd = landmarkEnd;
	}

	public String getDiseaseType() {
		return diseaseType;
	}

	public void setDiseaseType(String diseaseType) {
		this.diseaseType = diseaseType;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getDeep() {
		return deep;
	}

	public void setDeep(String deep) {
		this.deep = deep;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	/**
	 * 
	 * @return 严重程度：0-轻，1-中，2-重
	 */
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * 
	 * @return 损坏性质,0-新病害，1-旧病害，2-修复后损坏
	 */
	public int getNature() {
		return nature;
	}

	public void setNature(int nature) {
		this.nature = nature;
	}

	public String getAdvice() {
		return advice;
	}

	public void setAdvice(String advice) {
		this.advice = advice;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
