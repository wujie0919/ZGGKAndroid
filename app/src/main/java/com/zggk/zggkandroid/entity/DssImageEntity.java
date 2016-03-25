package com.zggk.zggkandroid.entity;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class DssImageEntity implements KvmSerializable {

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

	private String dssId;
	private String fileName;
	private byte[] imgO;

	public String getDssId() {
		return dssId;
	}

	public void setDssId(String dssId) {
		this.dssId = dssId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getImgO() {
		return imgO;
	}

	public void setImgO(byte[] imgO) {
		this.imgO = imgO;
	}

}
