package com.zggk.zggkandroid.entity;


public class DmDinspRecord {
	private String dinspId;//日常巡查单ID（对象1ID）
	private String dssId; //自身ID
	private String inspTimeIntvl;//巡查时段（根据手机时间，上午：1，下午：2，晚上：3）
	private String inspTime;//巡查时间（明细病害录入手机的时分）
	private String issueType;//问题类型（病害：2，保洁：1  ）​
	private String lineDirect;//路线方向（上行：1，下行：2）
	private String stake;//桩号
	private String dssType;//病害类型​（病害类型选项的Value）​
	private String dssDegree;//病害严重程度（轻：01，中：02，重：03)
	private String mntnAdvice;//养护建议​
	private String facilityCat;//设施分类（路面：LM，路基：LJ，沿线设施：JA,绿化：LH）
	private String lane;//车道（选项的Value）
	private String dssPosition;//病害位置
	private String dssDesc;//病害描述
	private String dssCause;//病害成因初步判断
	private String dssL;//病害长
	private String dssLUnit;//
	private String dssW;//病害宽
	private String dssWUnit;//病害宽单位
	private String dssD;//病害深
	private String dssDUnit;//病害深单位
	private String dssN;//病害数量
	private String dssNUnit;//病害数量单位
	private String dssA;//病害面积
	private String dssAUnit;//病害面积单位
	private String dssV;//病害体积
	private String dssVUnit;//病害体积单位
	private String dssP;//病害百分比
	private String dssG;//病害角度
	private String dssImpFlag;//是否重点病害 0 否 1 是
	private String dssQuality;//病害性质：0 新病害、1 旧病害、2 修复后损坏,3修复良好
	private String bId;
	
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
	public String getDinspId() {
		return dinspId;
	}
	public void setDinspId(String dinspId) {
		this.dinspId = dinspId;
	}
	public String getInspTimeIntvl() {
		return inspTimeIntvl;
	}
	public void setInspTimeIntvl(String inspTimeIntvl) {
		this.inspTimeIntvl = inspTimeIntvl;
	}
	public String getInspTime() {
		return inspTime;
	}
	public void setInspTime(String inspTime) {
		this.inspTime = inspTime;
	}
	public String getIssueType() {
		return issueType;
	}
	public void setIssueType(String issueType) {
		this.issueType = issueType;
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
	public String getFacilityCat() {
		return facilityCat;
	}
	public void setFacilityCat(String facilityCat) {
		this.facilityCat = facilityCat;
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
	public String getDssCause() {
		return dssCause;
	}
	public void setDssCause(String dssCause) {
		this.dssCause = dssCause;
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
	

}
