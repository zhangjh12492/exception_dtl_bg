package com.wfj.exception.hadoop.hadoop.dao;

import com.wfj.exception.dal.entity.MesBusiProcessReq;
import com.wfj.exception.dal.entity.MesSysProcessReq;

public interface SendMesProcessUserService {

	void sysErrMesEarlyWarn(MesSysProcessReq mesSys) throws Exception;
	
	void sysWarnMesEarlyWarn(MesSysProcessReq mesSys) throws Exception;
	
	void busiErrMesEarlyWarn(MesBusiProcessReq mesSys) throws Exception;
	
	void busiWarnMesEarlyWarn(MesBusiProcessReq mesSys) throws Exception;
	
	
}
