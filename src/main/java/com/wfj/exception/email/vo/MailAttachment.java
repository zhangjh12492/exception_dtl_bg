package com.wfj.exception.email.vo;

import javax.activation.DataSource;

public class MailAttachment {

	private DataSource file;
	private String fileName;
	public void setFile(DataSource file) {
		this.file = file;
	}
	public DataSource getFile() {
		return file;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
