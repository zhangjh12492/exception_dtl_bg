package com.wfj.exception.dal.service;

import java.util.List;
import java.util.Map;

import com.wfj.exception.dal.entity.BusiInfo;
import com.wfj.exception.dal.entity.MesEarlyWarnInfo;
import com.wfj.exception.dal.entity.MesSysProcessReq;

public interface MesEarlyWarnService {

	void proceSysEarlyWarnConfig(MesSysProcessReq sysReq) throws Exception;
	
	void proceBusiEarlyWarnConfig(Map<String,String> param) throws Exception;
	
	void insertBusiByReduce(Map<String,BusiInfo> busis) throws Exception;
}
