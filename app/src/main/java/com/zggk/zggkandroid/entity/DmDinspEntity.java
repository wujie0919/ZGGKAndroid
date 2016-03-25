package com.zggk.zggkandroid.entity;

import java.util.Date;
import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

/**
 * 日常巡查主单
 * 
 * @author xushaohan
 * 
 */
public class DmDinspEntity implements KvmSerializable {

	@Override
	public Object getProperty(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return 32;
	}

	@Override
	public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setProperty(int arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

	private String lineCode; // 路线ID
	private String mntOrgId; // 管养单位
	private String mntnOrgNm; // 养护单位名称
	private Date inspDate; // 巡查日期
	private Integer inspTimeIntvlM; // 巡查时段_上午
	private String inspTimeIntvlStartM; // 上午巡查开始时间
	private String inspTimeIntvlEndM; // 上午巡查结束时间
	private String inspScopeM; // 巡查路段范围_上午
	private String weatherM;// 天气情况_上午
	private String inspDistanceM; // 巡查里程_上午
	private String inspPersonM; // 巡查人员_上午
	private String inspCarM; // 巡查车牌号_上午
	private Integer inspTimeIntvlA; // 巡查时段_下午
	private String inspTimeIntvlStartA; // 下午巡查开始时间
	private String inspTimeIntvlEndA; // 下午巡查结束时间
	private String inspScopeA; // 巡查路段_下午
	private String weatherA;// 天气情况_下午
	private String inspDistanceA;// 巡查里程(公里)_下午
	private String inspPersonA; // 巡查人员_下午
	private String inspCarA; // 巡查车牌号_下午
	private Integer inspTimeIntvlN; // 巡查时段_晚上
	private String inspTimeIntvlStartN; // 晚上巡查开始时间
	private String inspTimeIntvlEndN; // 晚上巡查结束时间
	private String inspScopeN; // 巡查路段范围_晚上
	private String weatherN; // 天气情况_晚上
	private String inspDistanceN; // 巡查里程_晚上
	private String inspPersonN; // 巡查人员_晚上
	private String inspCarN; // 巡查车牌号_晚上
	private String remark; // 备注
	private String createUserId; // 创建人
	private Date createTime; // 创建时间
	private String searchDept; // 巡查单位

	public String getLineCode() {
		return lineCode;
	}

	public void setLineCode(String lineCode) {
		this.lineCode = lineCode;
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

	public Date getInspDate() {
		return inspDate;
	}

	public void setInspDate(Date inspDate) {
		this.inspDate = inspDate;
	}

	public Integer getInspTimeIntvlM() {
		return inspTimeIntvlM;
	}

	public void setInspTimeIntvlM(Integer inspTimeIntvlM) {
		this.inspTimeIntvlM = inspTimeIntvlM;
	}

	public String getInspTimeIntvlStartM() {
		return inspTimeIntvlStartM;
	}

	public void setInspTimeIntvlStartM(String inspTimeIntvlStartM) {
		this.inspTimeIntvlStartM = inspTimeIntvlStartM;
	}

	public String getInspTimeIntvlEndM() {
		return inspTimeIntvlEndM;
	}

	public void setInspTimeIntvlEndM(String inspTimeIntvlEndM) {
		this.inspTimeIntvlEndM = inspTimeIntvlEndM;
	}

	public String getInspScopeM() {
		return inspScopeM;
	}

	public void setInspScopeM(String inspScopeM) {
		this.inspScopeM = inspScopeM;
	}

	public String getWeatherM() {
		return weatherM;
	}

	public void setWeatherM(String weatherM) {
		this.weatherM = weatherM;
	}

	public String getInspDistanceM() {
		return inspDistanceM;
	}

	public void setInspDistanceM(String inspDistanceM) {
		this.inspDistanceM = inspDistanceM;
	}

	public String getInspPersonM() {
		return inspPersonM;
	}

	public void setInspPersonM(String inspPersonM) {
		this.inspPersonM = inspPersonM;
	}

	public String getInspCarM() {
		return inspCarM;
	}

	public void setInspCarM(String inspCarM) {
		this.inspCarM = inspCarM;
	}

	public Integer getInspTimeIntvlA() {
		return inspTimeIntvlA;
	}

	public void setInspTimeIntvlA(Integer inspTimeIntvlA) {
		this.inspTimeIntvlA = inspTimeIntvlA;
	}

	public String getInspTimeIntvlStartA() {
		return inspTimeIntvlStartA;
	}

	public void setInspTimeIntvlStartA(String inspTimeIntvlStartA) {
		this.inspTimeIntvlStartA = inspTimeIntvlStartA;
	}

	public String getInspTimeIntvlEndA() {
		return inspTimeIntvlEndA;
	}

	public void setInspTimeIntvlEndA(String inspTimeIntvlEndA) {
		this.inspTimeIntvlEndA = inspTimeIntvlEndA;
	}

	public String getInspScopeA() {
		return inspScopeA;
	}

	public void setInspScopeA(String inspScopeA) {
		this.inspScopeA = inspScopeA;
	}

	public String getWeatherA() {
		return weatherA;
	}

	public void setWeatherA(String weatherA) {
		this.weatherA = weatherA;
	}

	public String getInspDistanceA() {
		return inspDistanceA;
	}

	public void setInspDistanceA(String inspDistanceA) {
		this.inspDistanceA = inspDistanceA;
	}

	public String getInspPersonA() {
		return inspPersonA;
	}

	public void setInspPersonA(String inspPersonA) {
		this.inspPersonA = inspPersonA;
	}

	public String getInspCarA() {
		return inspCarA;
	}

	public void setInspCarA(String inspCarA) {
		this.inspCarA = inspCarA;
	}

	public Integer getInspTimeIntvlN() {
		return inspTimeIntvlN;
	}

	public void setInspTimeIntvlN(Integer inspTimeIntvlN) {
		this.inspTimeIntvlN = inspTimeIntvlN;
	}

	public String getInspTimeIntvlStartN() {
		return inspTimeIntvlStartN;
	}

	public void setInspTimeIntvlStartN(String inspTimeIntvlStartN) {
		this.inspTimeIntvlStartN = inspTimeIntvlStartN;
	}

	public String getInspTimeIntvlEndN() {
		return inspTimeIntvlEndN;
	}

	public void setInspTimeIntvlEndN(String inspTimeIntvlEndN) {
		this.inspTimeIntvlEndN = inspTimeIntvlEndN;
	}

	public String getInspScopeN() {
		return inspScopeN;
	}

	public void setInspScopeN(String inspScopeN) {
		this.inspScopeN = inspScopeN;
	}

	public String getWeatherN() {
		return weatherN;
	}

	public void setWeatherN(String weatherN) {
		this.weatherN = weatherN;
	}

	public String getInspDistanceN() {
		return inspDistanceN;
	}

	public void setInspDistanceN(String inspDistanceN) {
		this.inspDistanceN = inspDistanceN;
	}

	public String getInspPersonN() {
		return inspPersonN;
	}

	public void setInspPersonN(String inspPersonN) {
		this.inspPersonN = inspPersonN;
	}

	public String getInspCarN() {
		return inspCarN;
	}

	public void setInspCarN(String inspCarN) {
		this.inspCarN = inspCarN;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getSearchDept() {
		return searchDept;
	}

	public void setSearchDept(String searchDept) {
		this.searchDept = searchDept;
	}

}
