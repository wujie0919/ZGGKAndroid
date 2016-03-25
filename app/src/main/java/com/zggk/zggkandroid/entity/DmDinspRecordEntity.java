package com.zggk.zggkandroid.entity;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

/**
 * 日常巡查病害录入实体类
 * 
 * @author xushaohan
 * 
 */
public class DmDinspRecordEntity implements KvmSerializable {

	@Override
	public Object getProperty(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setProperty(int arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

	private String dinspId; // 日常巡查单ID（对象1ID）
	private String inspTimeIntvl; // 巡查时段（根据手机时间，上午：1，下午：2，晚上：3）
	private String inspTime; // 巡查时间（明细病害录入手机的时分）
	private String issueType; // 问题类型（病害：2，保洁：1 ）
	private String lineDirect; // 路线方向（上行：1，下行：2）
	private Double stake; // 桩号
	private String dssType; // 病害类型 （病害类型选项的Value）
	private String dssDegree; // 病害严重程度（轻：01，中：02，重：03）
	private String mntnAdvice; // 养护建议
	private String facilityCat; // 设施分类（路面：LM，路基：LJ，沿线设施：JA,绿化：LH）
	private String lane; // 车道（选项的Value）
	private String dssPosition; // 病害位置
	private String dssDesc; // 病害描述
	private String dssCause; // 病害成因初步判断
	private Double dssL; // 病害长
	private String dssLUnit; // 病害长单位
	private Double dssW; // 病害宽
	private String dssWUnit; // 病害宽单位
	private Double dssD; // 病害深
	private String dssDUnit; // 病害深单位
	private Double dssN; // 病害数量
	private String dssNUnit; // 病害数量单位
	private Double dssA; // 病害面积
	private String dssAUnit; // 病害面积单位
	private Double dssV; // 病害体积
	private String dssVUnit; // 病害体积单位
	private Double dssP; // 病害百分比
	private Double dssG; // 病害角度
	private Integer dssImpFlag; // 是否重点病害 0 否 1 是
	private Integer dssQuality; // 病害性质：0 新病害、1 旧病害、2 修复后损坏,3修复良好

	// --private String rampId; //匝道

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

	public Double getStake() {
		return stake;
	}

	public void setStake(Double stake) {
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

	public Double getDssL() {
		return dssL;
	}

	public void setDssL(Double dssL) {
		this.dssL = dssL;
	}

	public String getDssLUnit() {
		return dssLUnit;
	}

	public void setDssLUnit(String dssLUnit) {
		this.dssLUnit = dssLUnit;
	}

	public Double getDssW() {
		return dssW;
	}

	public void setDssW(Double dssW) {
		this.dssW = dssW;
	}

	public String getDssWUnit() {
		return dssWUnit;
	}

	public void setDssWUnit(String dssWUnit) {
		this.dssWUnit = dssWUnit;
	}

	public Double getDssD() {
		return dssD;
	}

	public void setDssD(Double dssD) {
		this.dssD = dssD;
	}

	public String getDssDUnit() {
		return dssDUnit;
	}

	public void setDssDUnit(String dssDUnit) {
		this.dssDUnit = dssDUnit;
	}

	public Double getDssN() {
		return dssN;
	}

	public void setDssN(Double dssN) {
		this.dssN = dssN;
	}

	public String getDssNUnit() {
		return dssNUnit;
	}

	public void setDssNUnit(String dssNUnit) {
		this.dssNUnit = dssNUnit;
	}

	public Double getDssA() {
		return dssA;
	}

	public void setDssA(Double dssA) {
		this.dssA = dssA;
	}

	public String getDssAUnit() {
		return dssAUnit;
	}

	public void setDssAUnit(String dssAUnit) {
		this.dssAUnit = dssAUnit;
	}

	public Double getDssV() {
		return dssV;
	}

	public void setDssV(Double dssV) {
		this.dssV = dssV;
	}

	public String getDssVUnit() {
		return dssVUnit;
	}

	public void setDssVUnit(String dssVUnit) {
		this.dssVUnit = dssVUnit;
	}

	public Double getDssP() {
		return dssP;
	}

	public void setDssP(Double dssP) {
		this.dssP = dssP;
	}

	public Double getDssG() {
		return dssG;
	}

	public void setDssG(Double dssG) {
		this.dssG = dssG;
	}

	public Integer getDssImpFlag() {
		return dssImpFlag;
	}

	public void setDssImpFlag(Integer dssImpFlag) {
		this.dssImpFlag = dssImpFlag;
	}

	public Integer getDssQuality() {
		return dssQuality;
	}

	public void setDssQuality(Integer dssQuality) {
		this.dssQuality = dssQuality;
	}

}
