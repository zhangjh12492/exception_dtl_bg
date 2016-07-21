package com.wfj.exception.dal.entity;

import java.util.List;

public class MesSysProcessReq {

	private Integer id;
	private String sysCode;
	private Integer sysWarnCount;
	private Integer sysErrCount;
	private String createdTime;
	private List<MesBusiProcessReq> busiList=null;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public List<MesBusiProcessReq> getBusiList() {
		return busiList;
	}
	public void setBusiList(List<MesBusiProcessReq> busiList) {
		this.busiList = busiList;
	}
	public String getSysCode() {
		return sysCode;
	}
	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}
	public Integer getSysWarnCount() {
		return sysWarnCount;
	}
	public void setSysWarnCount(Integer sysWarnCount) {
		this.sysWarnCount = sysWarnCount;
	}
	public Integer getSysErrCount() {
		return sysErrCount;
	}
	public void setSysErrCount(Integer sysErrCount) {
		this.sysErrCount = sysErrCount;
	}
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	@Override
	public String toString() {
		return "MesSysProcessReq [id=" + id + ", sysCode=" + sysCode + ", sysWarnCount=" + sysWarnCount + ", sysErrCount=" + sysErrCount + ", createdTime=" + createdTime + ", busiList=" + busiList
				+ "]";
	}
	
	
	
}
