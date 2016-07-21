package com.wfj.exception.dal.cond;

import java.util.Date;

import com.wfj.exception.util.DateUtils;


public class MesAllProcessReqCond {

	private String code;
	private String dateType;
	private String codeType;
	private String startTime;
	private String endTime;
	private String currentTime;
	private String errLevel;
	private String processStatus;
	
	
	public String getProcessStatus() {
		return processStatus;
	}
	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDateType() {
		return dateType;
	}
	public void setDateType(String dateType) {
		this.dateType = dateType;
	}
	public String getCodeType() {
		return codeType;
	}
	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	public String getCurrentTime() {
		return currentTime;
	}
	public void setCurrentTime(String currentTime) {
		this.currentTime=currentTime;
	}
	public String getErrLevel() {
		return errLevel;
	}
	public void setErrLevel(String errLevel) {
		this.errLevel = errLevel;
	}
	@Override
	public String toString() {
		return "MesAllProcessReqCond [code=" + code + ", dateType=" + dateType + ", codeType=" + codeType + ", startTime=" + startTime + ", endTime=" + endTime + ", currentTime=" + currentTime
				+ ", errLevel=" + errLevel + ", processStatus=" + processStatus + "]";
	}
	
	
}
