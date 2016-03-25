package com.zggk.zggkandroid.entity;

public class DmFinsp {
	private String finspId;				//经常检查单ID 		---
	private String finspCode;			//经常检查单编码
	private String facilityCat;			//设施分类C
	private String mntOrgId;			//管养单位ID		--->
	private String mntnOrgNm;			//养护单位名称
	private String inspDate;				//开始检查日期
	private String inspEndDate;			//结束检查日期
	private String inspPerson;			//检查人
	private String lineCode;			//路线编码			--->
	private String lineDirect;			//调查方向（线路类型）	
	private String roadSection;			//检查路段
	private String startStake;			//起点桩号(非结构物)
	private String endStake;			//终点桩号(非结构物)
	private String structId;			//结构物编码
	private String structName;			//结构物名称
	private String remark;				//备注
	private String createUserId;		//创建人
	private String createTime;			//创建时间
	private String updateUserId;		//最后修改人
	private String updateTime;			//最后修改时间
	private String searchDept;

	public String getSearchDept() {
		return searchDept;
	}
	public void setSearchDept(String searchDept) {
		this.searchDept = searchDept;
	}
	public String getFinspId() {
		return finspId;
	}
	public void setFinspId(String finspId) {
		this.finspId = finspId;
	}
	public String getFinspCode() {
		return finspCode;
	}
	public void setFinspCode(String finspCode) {
		this.finspCode = finspCode;
	}
	public String getFacilityCat() {
		return facilityCat;
	}
	public void setFacilityCat(String facilityCat) {
		this.facilityCat = facilityCat;
	}
	public String getMntOrgId() {
		return mntOrgId;
	}
	public void setMntOrgId(String mntOrgId) {
		this.mntOrgId = mntOrgId;
	}
	public String getMntnOrgNm() {
		return mntnOrgNm;
	}
	public void setMntnOrgNm(String mntnOrgNm) {
		this.mntnOrgNm = mntnOrgNm;
	}
	public String getInspDate() {
		return inspDate;
	}
	public void setInspDate(String inspDate) {
		this.inspDate = inspDate;
	}
	public String getInspEndDate() {
		return inspEndDate;
	}
	public void setInspEndDate(String inspEndDate) {
		this.inspEndDate = inspEndDate;
	}
	public String getInspPerson() {
		return inspPerson;
	}
	public void setInspPerson(String inspPerson) {
		this.inspPerson = inspPerson;
	}
	public String getLineCode() {
		return lineCode;
	}
	public void setLineCode(String lineCode) {
		this.lineCode = lineCode;
	}
	public String getLineDirect() {
		return lineDirect;
	}
	public void setLineDirect(String lineDirect) {
		this.lineDirect = lineDirect;
	}
	public String getRoadSection() {
		return roadSection;
	}
	public void setRoadSection(String roadSection) {
		this.roadSection = roadSection;
	}
	public String getStartStake() {
		return startStake;
	}
	public void setStartStake(String startStake) {
		this.startStake = startStake;
	}
	public String getEndStake() {
		return endStake;
	}
	public void setEndStake(String endStake) {
		this.endStake = endStake;
	}
	public String getStructId() {
		return structId;
	}
	public void setStructId(String structId) {
		this.structId = structId;
	}
	public String getStructName() {
		return structName;
	}
	public void setStructName(String structName) {
		this.structName = structName;
	}
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
}
