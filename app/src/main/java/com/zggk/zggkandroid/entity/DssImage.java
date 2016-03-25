package com.zggk.zggkandroid.entity;

public class DssImage {
	private String dssId;
	private String fileName;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	private String filePath;
	
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
}
