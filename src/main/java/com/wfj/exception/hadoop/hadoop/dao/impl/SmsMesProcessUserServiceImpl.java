package com.wfj.exception.hadoop.hadoop.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.wfj.exception.dal.entity.MesBusiProcessReq;
import com.wfj.exception.dal.entity.MesSysProcessReq;
import com.wfj.exception.hadoop.hadoop.dao.SendMesProcessUserService;

/**
 * @author Administrator
 *
 */
@Service("smsProcessUserService")
public class SmsMesProcessUserServiceImpl implements SendMesProcessUserService {

	private static final Logger log=LoggerFactory.getLogger(SmsMesProcessUserServiceImpl.class);
	
	@Override
	public void sysErrMesEarlyWarn(MesSysProcessReq mesSys) throws Exception {
		
		
	}

	@Override
	public void sysWarnMesEarlyWarn(MesSysProcessReq mesSys) throws Exception {
		
	}

	@Override
	public void busiErrMesEarlyWarn(MesBusiProcessReq mesSys) throws Exception {
		
	}

	@Override
	public void busiWarnMesEarlyWarn(MesBusiProcessReq mesSys) throws Exception {
		
	}

	

}
