package com.zggk.zggkandroid.entity;

/**
 * 病害类型实体类/保存系统数据
 * 
 * @author xushaohan
 * 
 */
public class DssTypeEntity {

	public static final String TAG_KEY = "parentId";

	private String parentId;
	private String DSS_TYPE;// 病害类型
	private String DSS_TYPE_NAME;// 病害类型名称
	private int MAX_DEGREE; // 最大病害标度
	private int HAVE_DSS_L; // 定量长（0,1）
	private String DSS_L_UNIT; // 定量长单位
	private int HAVE_DSS_W; // 定量宽
	private String DSS_W_UNIT; // 定量宽单位
	private int HAVE_DSS_D; // 定量深
	private String DSS_D_UNIT; // 定量深单位
	private int HAVE_DSS_N; // 数量
	private String DSS_N_UNIT; // 数量单位
	private int HAVE_DSS_A; // 面积
	private String DSS_A_UNIT; // 面积单位
	private int HAVE_DSS_V; // 体积
	private String DSS_V_UNIT; // 体积单位
	private String HAVE_DSS_UNIT; // 定量单位
	private String STRUCTCOMPCODE;
	private String HAVE_DSS_COLOM;// 必填项

	public String getHAVE_DSS_COLOM() {
		return HAVE_DSS_COLOM;
	}

	public void setHAVE_DSS_COLOM(String hAVE_DSS_COLOM) {
		HAVE_DSS_COLOM = hAVE_DSS_COLOM;
	}

	public String getSTRUCTCOMPCODE() {
		return STRUCTCOMPCODE;
	}

	public void setSTRUCTCOMPCODE(String sTRUCTCOMPCODE) {
		STRUCTCOMPCODE = sTRUCTCOMPCODE;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getDSS_TYPE() {
		return DSS_TYPE;
	}

	public void setDSS_TYPE(String dSS_TYPE) {
		DSS_TYPE = dSS_TYPE;
	}

	public String getDSS_TYPE_NAME() {
		return DSS_TYPE_NAME;
	}

	public void setDSS_TYPE_NAME(String dSS_TYPE_NAME) {
		DSS_TYPE_NAME = dSS_TYPE_NAME;
	}

	public int getMAX_DEGREE() {
		return MAX_DEGREE;
	}

	public void setMAX_DEGREE(int mAX_DEGREE) {
		MAX_DEGREE = mAX_DEGREE;
	}

	public int getHAVE_DSS_L() {
		return HAVE_DSS_L;
	}

	public void setHAVE_DSS_L(int hAVE_DSS_L) {
		HAVE_DSS_L = hAVE_DSS_L;
	}

	public String getDSS_L_UNIT() {
		return DSS_L_UNIT;
	}

	public void setDSS_L_UNIT(String dSS_L_UNIT) {
		DSS_L_UNIT = dSS_L_UNIT;
	}

	public int getHAVE_DSS_W() {
		return HAVE_DSS_W;
	}

	public void setHAVE_DSS_W(int hAVE_DSS_W) {
		HAVE_DSS_W = hAVE_DSS_W;
	}

	public String getDSS_W_UNIT() {
		return DSS_W_UNIT;
	}

	public void setDSS_W_UNIT(String dSS_W_UNIT) {
		DSS_W_UNIT = dSS_W_UNIT;
	}

	public int getHAVE_DSS_D() {
		return HAVE_DSS_D;
	}

	public void setHAVE_DSS_D(int hAVE_DSS_D) {
		HAVE_DSS_D = hAVE_DSS_D;
	}

	public String getDSS_D_UNIT() {
		return DSS_D_UNIT;
	}

	public void setDSS_D_UNIT(String dSS_D_UNIT) {
		DSS_D_UNIT = dSS_D_UNIT;
	}

	public int getHAVE_DSS_N() {
		return HAVE_DSS_N;
	}

	public void setHAVE_DSS_N(int hAVE_DSS_N) {
		HAVE_DSS_N = hAVE_DSS_N;
	}

	public String getDSS_N_UNIT() {
		return DSS_N_UNIT;
	}

	public void setDSS_N_UNIT(String dSS_N_UNIT) {
		DSS_N_UNIT = dSS_N_UNIT;
	}

	public int getHAVE_DSS_A() {
		return HAVE_DSS_A;
	}

	public void setHAVE_DSS_A(int hAVE_DSS_A) {
		HAVE_DSS_A = hAVE_DSS_A;
	}

	public String getDSS_A_UNIT() {
		return DSS_A_UNIT;
	}

	public void setDSS_A_UNIT(String dSS_A_UNIT) {
		DSS_A_UNIT = dSS_A_UNIT;
	}

	public int getHAVE_DSS_V() {
		return HAVE_DSS_V;
	}

	public void setHAVE_DSS_V(int hAVE_DSS_V) {
		HAVE_DSS_V = hAVE_DSS_V;
	}

	public String getDSS_V_UNIT() {
		return DSS_V_UNIT;
	}

	public void setDSS_V_UNIT(String dSS_V_UNIT) {
		DSS_V_UNIT = dSS_V_UNIT;
	}

	public String getHAVE_DSS_UNIT() {
		return HAVE_DSS_UNIT;
	}

	public void setHAVE_DSS_UNIT(String hAVE_DSS_UNIT) {
		HAVE_DSS_UNIT = hAVE_DSS_UNIT;
	}

	public static String getTagKey() {
		return TAG_KEY;
	}

}
