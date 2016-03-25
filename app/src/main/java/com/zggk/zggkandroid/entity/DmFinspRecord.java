package com.zggk.zggkandroid.entity;

public class DmFinspRecord {
	private String finspId;			//检查单ID			---
	private String lineDirect;		//路线方向
	private String stake;			//逻辑桩号
	private String dssType;			//病害类型				---
	private String dssDegree;		//病害严重程度（标度）		---
	private String mntnAdvice;		//养护建议
	private String lane;			//车道
	private String dssPosition;		//病害位置
	private String dssDesc;			//病害描述
	private String dssL;			//病害长
	private String dssLUnit;		//病害长单位
	private String dssW;			//病害宽
	private String dssWUnit;		//病害宽单位
	private String dssD;			//病害深
	private String dssDUnit;		//病害深单位
	private String dssN;			//病害数量
	private String dssNUnit;		//病害数量单位
	private String dssA;			//病害面积
	private String dssAUnit;		//病害面积单位
	private String dssV;			//病害体积
	private String dssVUnit;		//病害体积单位
	private String dssP;			//病害百分比
	private String dssG;			//病害角度
	private String finspItemId;		//
	

    private String dssImpFlag;			//是否重点病害 0 否 1 是
	private String dssQuality;			//病害性质：0 新病害、1 旧病害、2 修复后损坏
	private String hisDssId;			//历史病害ID
	private String dssCause;      		//病害成因初步判断
	private String isPhone;			// 来源 ： 0 pc端 ，1 移动端

	private String dssTypeName;			//病害类型名称

	//病害数量（描述）
	private String dssNum;
	
	
    private String structId;		//结构物ID
    private String facilityCat;		//结构物类型
    
	private String qualityName;
	private String flagName;

	
	private String rampId;	//匝道
	private String rstake;	//匝道桩号
	private String lineId;
	private String bId; //数据库表关联键
	private String dssId;//自身ID
	
	public String getDssId() {
		return dssId;
	}
	public void setDssId(String dssId) {
		this.dssId = dssId;
	}
	public String getbId() {
		return bId;
	}
	public void setbId(String bId) {
		this.bId = bId;
	}
	public String getFinspId() {
		return finspId;
	}
	public void setFinspId(String finspId) {
		this.finspId = finspId;
	}
	public String getLineDirect() {
		return lineDirect;
	}
	public void setLineDirect(String lineDirect) {
		this.lineDirect = lineDirect;
	}
	public String getStake() {
		return stake;
	}
	public void setStake(String stake) {
		this.stake = stake;
	}
	public String getDssType() {
		return dssType;
	}
	public void setDssType(String dssType) {
		this.dssType = dssType;
	}
	public String getDssDegree() {
		return dssDegree;
	}
	public void setDssDegree(String dssDegree) {
		this.dssDegree = dssDegree;
	}
	public String getMntnAdvice() {
		return mntnAdvice;
	}
	public void setMntnAdvice(String mntnAdvice) {
		this.mntnAdvice = mntnAdvice;
	}
	public String getLane() {
		return lane;
	}
	public void setLane(String lane) {
		this.lane = lane;
	}
	public String getDssPosition() {
		return dssPosition;
	}
	public void setDssPosition(String dssPosition) {
		this.dssPosition = dssPosition;
	}
	public String getDssDesc() {
		return dssDesc;
	}
	public void setDssDesc(String dssDesc) {
		this.dssDesc = dssDesc;
	}
	public String getDssL() {
		return dssL;
	}
	public void setDssL(String dssL) {
		this.dssL = dssL;
	}
	public String getDssLUnit() {
		return dssLUnit;
	}
	public void setDssLUnit(String dssLUnit) {
		this.dssLUnit = dssLUnit;
	}
	public String getDssW() {
		return dssW;
	}
	public void setDssW(String dssW) {
		this.dssW = dssW;
	}
	public String getDssWUnit() {
		return dssWUnit;
	}
	public void setDssWUnit(String dssWUnit) {
		this.dssWUnit = dssWUnit;
	}
	public String getDssD() {
		return dssD;
	}
	public void setDssD(String dssD) {
		this.dssD = dssD;
	}
	public String getDssDUnit() {
		return dssDUnit;
	}
	public void setDssDUnit(String dssDUnit) {
		this.dssDUnit = dssDUnit;
	}
	public String getDssN() {
		return dssN;
	}
	public void setDssN(String dssN) {
		this.dssN = dssN;
	}
	public String getDssNUnit() {
		return dssNUnit;
	}
	public void setDssNUnit(String dssNUnit) {
		this.dssNUnit = dssNUnit;
	}
	public String getDssA() {
		return dssA;
	}
	public void setDssA(String dssA) {
		this.dssA = dssA;
	}
	public String getDssAUnit() {
		return dssAUnit;
	}
	public void setDssAUnit(String dssAUnit) {
		this.dssAUnit = dssAUnit;
	}
	public String getDssV() {
		return dssV;
	}
	public void setDssV(String dssV) {
		this.dssV = dssV;
	}
	public String getDssVUnit() {
		return dssVUnit;
	}
	public void setDssVUnit(String dssVUnit) {
		this.dssVUnit = dssVUnit;
	}
	public String getDssP() {
		return dssP;
	}
	public void setDssP(String dssP) {
		this.dssP = dssP;
	}
	public String getDssG() {
		return dssG;
	}
	public void setDssG(String dssG) {
		this.dssG = dssG;
	}

	public String getFinspItemId() {
		return finspItemId;
	}
	public void setFinspItemId(String finspItemId) {
		this.finspItemId = finspItemId;
	}
	
	public String getDssImpFlag() {
		return dssImpFlag;
	}
	public void setDssImpFlag(String dssImpFlag) {
		this.dssImpFlag = dssImpFlag;
	}
	public String getDssQuality() {
		return dssQuality;
	}
	public void setDssQuality(String dssQuality) {
		this.dssQuality = dssQuality;
	}
	public String getHisDssId() {
		return hisDssId;
	}
	public void setHisDssId(String hisDssId) {
		this.hisDssId = hisDssId;
	}
	public String getDssCause() {
		return dssCause;
	}
	public void setDssCause(String dssCause) {
		this.dssCause = dssCause;
	}
	public String getIsPhone() {
		return isPhone;
	}
	public void setIsPhone(String isPhone) {
		this.isPhone = isPhone;
	}
	public String getDssTypeName() {
		return dssTypeName;
	}
	public void setDssTypeName(String dssTypeName) {
		this.dssTypeName = dssTypeName;
	}
	
	public String getDssNum() {
		return dssNum;
	}
	public void setDssNum(String dssNum) {
		this.dssNum = dssNum;
	}
	
	public String getStructId() {
		return structId;
	}
	public void setStructId(String structId) {
		this.structId = structId;
	}
	public String getFacilityCat() {
		return facilityCat;
	}
	public void setFacilityCat(String facilityCat) {
		this.facilityCat = facilityCat;
	}
	public String getQualityName() {
		return qualityName;
	}
	public void setQualityName(String qualityName) {
		this.qualityName = qualityName;
	}
	public String getFlagName() {
		return flagName;
	}
	public void setFlagName(String flagName) {
		this.flagName = flagName;
	}
	
	public String getRampId() {
		return rampId;
	}
	public void setRampId(String rampId) {
		this.rampId = rampId;
	}
	public String getRstake() {
		return rstake;
	}
	public void setRstake(String rstake) {
		this.rstake = rstake;
	}
	
	public String getLineId() {
		return lineId;
	}
	public void setLineId(String lineId) {
		this.lineId = lineId;
	}	
	
	
}
