package com.wfj.exception.dal.entity;

public class MesAllProcessReq {

	private Integer id;
	private String code;
	private Integer warnCount;
	private Integer errorCount;
	private String createdTime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getWarnCount() {
		return warnCount;
	}
	public void setWarnCount(Integer warnCount) {
		this.warnCount = warnCount;
	}
	public Integer getErrorCount() {
		return errorCount;
	}
	public void setErrorCount(Integer errorCount) {
		this.errorCount = errorCount;
	}
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	@Override
	public String toString() {
		return "MesAllProcessReq [id=" + id + ", code=" + code + ", warnCount=" + warnCount + ", errorCount=" + errorCount + ", createdTime=" + createdTime + "]";
	}
	
	
}
