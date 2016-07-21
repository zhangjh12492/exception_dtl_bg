package com.wfj.exception.dal.entity;

/**
 * 异常信息发送规则配置基础类
 * @ClassName: MesEarlyWarnInfo
 * @author ZJHao
 * @date 2015-8-17 下午4:13:21
 *
 */
public class MesEarlyWarnInfo {

	private Integer id;	//主键
	private String description;	//描述
	private String flag;		//是否有效
	private String status;		//状态，是否开启
	private String sendType;	//发送方式,1.短信,2.邮件,3.短信+邮件
	private Integer warnCount;	//警告允许达到的数量
	private Integer errorCount;	//异常允许达到的数量
	private Integer sysId;	//系统id
	private Integer busiId;	//业务id
	
	/*预留字段*/
	private String reserved1;
	private String reserved2;
	private String reserved3;
	private String reserved4;
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSendType() {
		return sendType;
	}
	public void setSendType(String sendType) {
		this.sendType = sendType;
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
	public Integer getSysId() {
		return sysId;
	}
	public void setSysId(Integer sysId) {
		this.sysId = sysId;
	}
	public Integer getBusiId() {
		return busiId;
	}
	public void setBusiId(Integer busiId) {
		this.busiId = busiId;
	}
	public String getReserved1() {
		return reserved1;
	}
	public void setReserved1(String reserved1) {
		this.reserved1 = reserved1;
	}
	public String getReserved2() {
		return reserved2;
	}
	public void setReserved2(String reserved2) {
		this.reserved2 = reserved2;
	}
	public String getReserved3() {
		return reserved3;
	}
	public void setReserved3(String reserved3) {
		this.reserved3 = reserved3;
	}
	public String getReserved4() {
		return reserved4;
	}
	public void setReserved4(String reserved4) {
		this.reserved4 = reserved4;
	}
	@Override
	public String toString() {
		return "MesEarlyWarnInfo [id=" + id + ", description=" + description + ", flag=" + flag + ", status=" + status + ", sendType=" + sendType + ", warnCount=" + warnCount + ", errorCount="
				+ errorCount + ", sysId=" + sysId + ", busiId=" + busiId + ", reserved1=" + reserved1 + ", reserved2=" + reserved2 + ", reserved3=" + reserved3 + ", reserved4=" + reserved4 + "]";
	}
	
	
	
	
	
	
	
	
}
